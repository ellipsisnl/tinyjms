package nl.ellipsis.tinyjms.client;

import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.Topic;

/**
 * A client uses a <CODE>MessageConsumer</CODE> object to receive messages
 * from a destination. A <CODE> MessageConsumer</CODE> object is created by
 * passing a <CODE>Destination</CODE> object to a message-consumer creation
 * method supplied by a session.
 * <P>
 * <CODE>MessageConsumer</CODE> is the parent interface for all message
 * consumers.
 * <P>
 * A message consumer can be created with a message selector. A message selector
 * allows the client to restrict the messages delivered to the message consumer
 * to those that match the selector.
 * <P>
 * A client may either synchronously receive a message consumer's messages or
 * have the consumer asynchronously deliver them as they arrive.
 * <P>
 * For synchronous receipt, a client can request the next message from a message
 * consumer using one of its <CODE> receive</CODE> methods. There are several
 * variations of <CODE>receive</CODE> that allow a client to poll or wait for
 * the next message.
 * <P>
 * For asynchronous delivery, a client can register a
 * <CODE>MessageListener</CODE> object with a message consumer. As messages
 * arrive at the message consumer, it delivers them by calling the
 * <CODE>MessageListener</CODE>'s<CODE>
 * onMessage</CODE> method.
 * <P>
 * It is a client programming error for a <CODE>MessageListener</CODE> to
 * throw an exception.
 *
 *
 * @see javax.jms.MessageConsumer
 * @see javax.jms.QueueReceiver
 * @see javax.jms.TopicSubscriber
 * @see javax.jms.Session
 */
public class TinyJmsMessageConsumer implements MessageConsumer {
	private Destination destination;
	private String messageSelector;
	private MessageListener messageListener;

    public TinyJmsMessageConsumer(Destination destination) throws JMSException {
		this.destination = destination;
		registerMessageConsumer();
	}

	public TinyJmsMessageConsumer(Destination destination, String messageSelector) throws JMSException {
		this.destination = destination;
		this.messageSelector = messageSelector;
		registerMessageConsumer();
	}

	/**
     * Closes the message consumer.
     * <P>
     * Since a provider may allocate some resources on behalf of a <CODE>
     * MessageConsumer</CODE>
     * outside the Java virtual machine, clients should close them when they are
     * not needed. Relying on garbage collection to eventually reclaim these
     * resources may not be timely enough.
     * <P>
     * This call blocks until a <CODE>receive</CODE> or message listener in
     * progress has completed. A blocked message consumer <CODE>receive </CODE>
     * call returns null when this message consumer is closed.
     *
     * @throws JMSException if the JMS provider fails to close the consumer due
     *                 to some internal error.
     */
	@Override
	public void close() throws JMSException {
		// TODO Auto-generated method stub
		
	}

    /**
     * Gets the message consumer's <CODE>MessageListener</CODE>.
     *
     * @return the listener for the message consumer, or null if no listener is
     *         set
     * @throws JMSException if the JMS provider fails to get the message
     *                 listener due to some internal error.
     * @see javax.jms.MessageConsumer#setMessageListener(javax.jms.MessageListener)
     */
	@Override
	public MessageListener getMessageListener() throws JMSException {
		return messageListener;
	}

    /**
     * Gets this message consumer's message selector expression.
     *
     * @return this message consumer's message selector, or null if no message
     *         selector exists for the message consumer (that is, if the message
     *         selector was not set or was set to null or the empty string)
     * @throws JMSException if the JMS provider fails to receive the next
     *                 message due to some internal error.
     */
	@Override
	public String getMessageSelector() throws JMSException {
		return messageSelector;
	}

    /**
     * Receives the next message produced for this message consumer.
     * <P>
     * This call blocks indefinitely until a message is produced or until this
     * message consumer is closed.
     * <P>
     * If this <CODE>receive</CODE> is done within a transaction, the consumer
     * retains the message until the transaction commits.
     *
     * @return the next message produced for this message consumer, or null if
     *         this message consumer is concurrently closed
     */
	@Override
	public Message receive() throws JMSException {
		// TODO Auto-generated method stub
		return null;
	}

	/**
     * Receives the next message that arrives within the specified timeout
     * interval.
     * <P>
     * This call blocks until a message arrives, the timeout expires, or this
     * message consumer is closed. A <CODE>timeout</CODE> of zero never
     * expires, and the call blocks indefinitely.
     *
     * @param timeout the timeout value (in milliseconds), a time out of zero
     *                never expires.
     * @return the next message produced for this message consumer, or null if
     *         the timeout expires or this message consumer is concurrently
     *         closed
     */
	@Override
	public Message receive(long timeout) throws JMSException {
		// TODO Auto-generated method stub
		return null;
	}

    /**
     * Receives the next message if one is immediately available.
     *
     * @return the next message produced for this message consumer, or null if
     *         one is not available
     * @throws JMSException if the JMS provider fails to receive the next
     *                 message due to some internal error.
     */
	@Override
	public Message receiveNoWait() throws JMSException {
		// TODO Auto-generated method stub
		return null;
	}

    /**
     * Sets the message consumer's <CODE>MessageListener</CODE>.
     * <P>
     * Setting the message listener to null is the equivalent of unsetting the
     * message listener for the message consumer.
     * <P>
     * The effect of calling <CODE>MessageConsumer.setMessageListener</CODE>
     * while messages are being consumed by an existing listener or the consumer
     * is being used to consume messages synchronously is undefined.
     *
     * @param listener the listener to which the messages are to be delivered
     * @throws JMSException if the JMS provider fails to receive the next
     *                 message due to some internal error.
     * @see javax.jms.MessageConsumer#getMessageListener
     */
	@Override
	public void setMessageListener(MessageListener messageListener) throws JMSException {
		this.messageListener = messageListener;
	}
	
	///////// INTERNAL

	protected Destination getDestination() {
		return destination;
	}
	
	private void registerMessageConsumer() throws JMSException {
		if(destination instanceof TinyJmsDestination) {
			TinyJmsDestination jmsDestination = (TinyJmsDestination) destination;
			jmsDestination.setMessageConsumer(this);
		}
	}

}
