package momin;

import java.util.Properties;

import javax.jms.*;
import javax.naming.Context;
import javax.naming.InitialContext;

public class QueueProducer {

	private static final String DEFAULT_CONNECTION_FACTORY = "jms/RemoteConnectionFactory";
    private static final String DEFAULT_DESTINATION = "jms/queue/fred";
    private static final String DEFAULT_USERNAME = "momin";
    private static final String DEFAULT_PASSWORD = "momin@1234";
    private static final String INITIAL_CONTEXT_FACTORY = "org.jboss.naming.remote.client.InitialContextFactory";
    private static final String PROVIDER_URL = "remote://localhost:4447";
    
	public static void main(String[] args)  throws Exception{
		 ConnectionFactory connectionFactory = null;
	     Connection connection = null;
	     Session session = null;
	     MessageProducer messageProducer = null;
	     Destination destination = null;
	     TextMessage textMessage = null;
	     Context context = null;
	     
	     
		try {
		     context = QueueProducer.getInitialConext();
			// Perform the JNDI lookups
	        String connectionFactoryString = System.getProperty("connection.factory", DEFAULT_CONNECTION_FACTORY);
	        connectionFactory = (ConnectionFactory) context.lookup(connectionFactoryString);

	        String destinationString = System.getProperty("destination", DEFAULT_DESTINATION);
	        destination = (Destination) context.lookup(destinationString);
	        
	        // Create the JMS connection, session, producer
            connection = connectionFactory.createConnection(System.getProperty("username", DEFAULT_USERNAME), System.getProperty("password", DEFAULT_PASSWORD));
            session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
            messageProducer = session.createProducer(destination);
            connection.start();
           
            
            String content = System.getProperty("message.content", "hello world from jms ...........");
            textMessage = session.createTextMessage(content);
            messageProducer.send(textMessage);
            System.out.println("jms message send.................");
            Thread.sleep(Integer.MAX_VALUE);

		}catch(Exception e) {
			e.getMessage();
		} finally {
            if (context != null) {
                context.close();
            }

            // closing the connection takes care of the session, producer, and consumer
            if (connection != null) {
                connection.close();
            }
        }
		
	}
	
	 public static Context getInitialConext() throws Exception {
	    	Context context = null;
	    	try {
	    		final Properties env = new Properties();
	            env.put(Context.INITIAL_CONTEXT_FACTORY, INITIAL_CONTEXT_FACTORY);
	            env.put(Context.PROVIDER_URL, System.getProperty(Context.PROVIDER_URL, PROVIDER_URL));
	            env.put(Context.SECURITY_PRINCIPAL, System.getProperty("username", DEFAULT_USERNAME));
	            env.put(Context.SECURITY_CREDENTIALS, System.getProperty("password", DEFAULT_PASSWORD));
	            
	            context = new InitialContext(env);
	    	}catch(Exception e) {
	    		System.out.println("error here");
	    		System.out.println(e.getMessage());
	    	}
	    	
	    	return context;
	    	
	    }

}
