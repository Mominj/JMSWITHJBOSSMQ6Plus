package momin;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.MessageConsumer;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.naming.Context;

public class QueueConsumer implements  MessageListener{

	private static final String DEFAULT_CONNECTION_FACTORY = "jms/RemoteConnectionFactory";
    private static final String DEFAULT_DESTINATION = "jms/queue/fred";
    private static final String DEFAULT_USERNAME = "momin";
    private static final String DEFAULT_PASSWORD = "momin@1234";

    
	public static void main(String[] args)  throws Exception{
		 ConnectionFactory connectionFactory = null;
	     Connection connection = null;
	     Session session = null;
	     MessageConsumer consumer = null;
	     Destination destination = null;
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
            consumer = session.createConsumer(destination);
            connection.start();
            
            
            consumer.setMessageListener(new QueueConsumer());
			Thread.sleep(20000);

		}catch(Exception e) {
			System.out.println(e.getMessage());
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
	

	@Override
	public void onMessage(Message message)  {
		try {
            System.out.println("message received........");
            System.out.println(((TextMessage) message).getText());
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

        } catch (JMSException e) {
            e.getMessage();
        }
		
	}

}
