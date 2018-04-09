package pl.rkalaska.resourceloader.jms;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import pl.rkalaska.resourceloader.model.TaskModel;

import javax.annotation.Resource;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;
import javax.ejb.Timeout;
import javax.ejb.Timer;
import javax.inject.Inject;

@Stateless
public class RetryTimerBean {

    private static final Log LOG = LogFactory.getLog(RetryTimerBean.class);

    @Inject
    private
    JmsProducer jmsProducer;

    @Resource
    private SessionContext context;

    public void createTimer(long expirationMinutes, TaskModel taskModel) {
        long durationMillis = expirationMinutes*60000;
        context.getTimerService().createTimer(durationMillis, taskModel);
    }

    @Timeout
    public void timeOutHandler(Timer timer){
        TaskModel task = (TaskModel) timer.getInfo();
        task.setRetryCnt(task.getRetryCnt()+1);
        LOG.info("Timer for url: "+task.getUrl()+"has expired, reattempt to download resource - retry nr: "+task.getRetryCnt());
        timer.cancel();
        jmsProducer.sendMessage(task);
    }

}
