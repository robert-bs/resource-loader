package pl.rkalaska.resourceloader.jms;

import pl.rkalaska.resourceloader.model.TaskModel;

import javax.enterprise.context.RequestScoped;
import javax.jms.ConnectionFactory;
import javax.jms.JMSContext;
import javax.jms.Queue;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

@RequestScoped
public class JmsProducer {

    static {
        init();
    }

    static private Queue taskQueue;
    static private JMSContext jmsContext;

    public void sendMessage(TaskModel task) {
        jmsContext.createProducer().send(taskQueue, task);
    }

    private static void init() {
        try {
            Context context = new InitialContext();
            ConnectionFactory cf =(ConnectionFactory)context.lookup(System.getProperty("jms.connectionFactory"));
            taskQueue = (Queue) context.lookup(System.getProperty("jms.queue"));
            jmsContext = cf.createContext();
        } catch (NamingException e) {
            throw new IllegalStateException("Unable to get parameters for JMS in JmsProducer");
        }
    }
}
