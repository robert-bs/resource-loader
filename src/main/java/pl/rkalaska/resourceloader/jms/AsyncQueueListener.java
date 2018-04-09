package pl.rkalaska.resourceloader.jms;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import pl.rkalaska.resourceloader.DAO.IResourceDAO;
import pl.rkalaska.resourceloader.entity.ResourceDTO;
import pl.rkalaska.resourceloader.model.TaskModel;
import pl.rkalaska.resourceloader.services.configuration.ConfigurationService;
import pl.rkalaska.resourceloader.services.download.IDownloadService;

import javax.ejb.MessageDriven;
import javax.inject.Inject;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.sql.rowset.serial.SerialBlob;
import java.sql.SQLException;

@MessageDriven(name="AsyncQueueListener")
public class AsyncQueueListener implements MessageListener {

    private static final Log LOG = LogFactory.getLog(AsyncQueueListener.class);


    @Inject
    private ConfigurationService configService;

    @Inject
    private IResourceDAO resourceDAO;

    @Inject
    private
    IDownloadService downloadService;

    @Inject
    private
    RetryTimerBean retryTimerBean;

    public AsyncQueueListener() {

    }

    @Override
    public void onMessage(Message message) {
       try {
           LOG.info("Message received");
           TaskModel task = message.getBody(TaskModel.class);
           LOG.info("URL: "+task.getUrl()+" retry nr "+task.getRetryCnt());

           byte[] content = downloadService.getResource(task.getUrl());
           if(content == null) {
               LOG.error("Content not downloaded - retry nr: "+task.getRetryCnt());
               if(task.getRetryCnt() <= configService.getConfiguration().getDowaloadMaxRetry()) {
                   retryTimerBean.createTimer(configService.getConfiguration().getDownloadRetryMin(), task);
               } else {
                   LOG.error("Unable to get resource from: "+task.getUrl()+" over "+configService.getConfiguration().getDowaloadMaxRetry()+  "retries");
               }
           } else {
               ResourceDTO r = new ResourceDTO();
               r.setUrl(task.getUrl());
               r.setContent(new SerialBlob(content));
               resourceDAO.storeResource(r);
           }

       } catch(JMSException e) {
           LOG.error("Unable to resolve message");
           e.printStackTrace();
       } catch (SQLException e) {
           LOG.error("Unable to make blob from content");
           e.printStackTrace();
       }

    }
}
