package nl.ellipsis.tpjms.provider;

import java.net.URI;

import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.Queue;
import javax.jms.Topic;

/**
 * Interface for Tiny JMS providers.
 */
public interface TPJMSProvider
{
	/**
	 * Connects to a provider.
	 * 
	 * @param uri
	 *          connection URI
	 * @param username
	 *          user name
	 * @param password
	 *          password
	 * @return connection context
	 * @throws JMSException
	 *           if connection fails
	 */
	public TPJMSConnectionContext connect(URI uri, String username, String password) throws JMSException;

	/**
	 * Closes a connection to a provider.
	 * 
	 * @param context
	 *          connection context
	 * @throws JMSException
	 *           if close fails
	 */
	public void close(TPJMSConnectionContext context) throws JMSException;
	
	public TPJMSSessionContext createSession() throws JMSException;

	public Topic createTopic(String topicName) throws JMSException;

	public boolean registerMessageConsumer(Topic topic, MessageConsumer topicSubscriber) throws JMSException;

	public void send(Destination destination, Message message) throws JMSException;

	public Queue createQueue(String queueName) throws JMSException;
}
