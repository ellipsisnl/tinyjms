package nl.ellipsis.tpjms.core.session;

import java.util.Enumeration;

import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Queue;
import javax.jms.QueueBrowser;

/**
 * A client uses a QueueBrowser object to look at messages on a queue without removing them.
 * The getEnumeration method returns a java.util.Enumeration that is used to scan the queue's messages. It may be an enumeration of the entire content of a queue, or it may contain only the messages matching a message selector.
 * 
 * Messages may be arriving and expiring while the scan is done. The JMS API does not require the content of an enumeration to be a static snapshot of queue content. Whether these changes are visible or not depends on the JMS provider.
 * 
 * A message must not be returned by a QueueBrowser before its delivery time has been reached.
 * 
 * A QueueBrowser can be created from either a Session or a QueueSession.

 * @since JMS 1.0
 * @version JMS 2.0
 * 
 * @see javax.jms.Session#createBrowser(javax.jms.Queue), javax.jms.QueueSession.createBrowser(javax.jms.Queue), javax.jms.QueueReceiver
 *
 */
public class TPJMSQueueBrowser implements QueueBrowser {
	
	/**
	 * Connection
	 */
	private final TPJMSSession session;

	/**
	 * Queue destination
	 */
	private final Destination destination;

	/**
	 * Message selector expression.
	 */
	private final String messageSelector;
	

	public TPJMSQueueBrowser(TPJMSSession session, Queue queue) throws JMSException {
		this.session = session;
		this.destination = queue;
		this.messageSelector = null;
	}

	public TPJMSQueueBrowser(TPJMSSession session, Queue queue, String messageSelector) throws JMSException {
		this.session = session;
		this.destination = queue;
		this.messageSelector = messageSelector;
	}

	/**
	 * Closes the QueueBrowser.
	 * Since a provider may allocate some resources on behalf of a QueueBrowser outside the Java virtual machine, clients should close them when they are not needed. Relying on garbage collection to eventually reclaim these resources may not be timely enough.
	 * 
	 * @see interface java.lang.AutoCloseable#close()
	 * 
	 * @throws JMSException - if the JMS provider fails to close this browser due to some internal error.
	 */
	@Override
	public void close() throws JMSException {
		throw new JMSException("Not yet implemented");
	}

	/**
	 * Gets an enumeration for browsing the current queue messages in the order they would be received.
	 * 
	 * @return An enumeration for browsing the messages
	 * @throws JMSException - if the JMS provider fails to get the enumeration for this browser due to some internal error.
	 */
	@Override
	public Enumeration getEnumeration() throws JMSException {
		throw new JMSException("Not yet implemented");
	}

	/**
	 * Gets this queue browser's message selector expression.
	 * 
	 * @return This queue browser's message selector, or null if no message selector exists for the message consumer (that is, if the message selector was not set or was set to null or the empty string)
	 * @throws JMSException - if the JMS provider fails to get the message selector for this browser due to some internal error.
	 */
	@Override
	public String getMessageSelector() throws JMSException {
		return messageSelector;
	}

	/**
	 * Gets the queue associated with this queue browser.
	 * 
	 * @return the queue
	 * @throws JMSException - if the JMS provider fails to get the queue associated with this browser due to some internal error.
	 */
	@Override
	public Queue getQueue() throws JMSException {
		return (Queue) destination;
	}

}
