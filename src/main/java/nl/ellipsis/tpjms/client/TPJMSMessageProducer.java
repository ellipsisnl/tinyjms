package nl.ellipsis.tpjms.client;

import java.util.concurrent.locks.ReentrantReadWriteLock;

import javax.jms.*;
import org.apache.logging.log4j.*;

/**
 * A client uses a <CODE>MessageProducer</CODE> object to send messages to a
 * destination. A <CODE>MessageProducer</CODE> object is created by passing a
 * <CODE>Destination</CODE> object to a message-producer creation method
 * supplied by a session.
 * <P>
 * <CODE>MessageProducer</CODE> is the parent interface for all message
 * producers.
 * <P>
 * A client also has the option of creating a message producer without supplying
 * a destination. In this case, a destination must be provided with every send
 * operation. A typical use for this kind of message producer is to send replies
 * to requests using the request's <CODE>JMSReplyTo</CODE> destination.
 * <P>
 * A client can specify a default delivery mode, priority, and time to live for
 * messages sent by a message producer. It can also specify the delivery mode,
 * priority, and time to live for an individual message.
 * <P>
 * A client can specify a time-to-live value in milliseconds for each message it
 * sends. This value defines a message expiration time that is the sum of the
 * message's time-to-live and the GMT when it is sent (for transacted sends,
 * this is the time the client sends the message, not the time the transaction
 * is committed).
 * <P>
 * A JMS provider should do its best to expire messages accurately; however, the
 * JMS API does not define the accuracy provided.
 *
 *
 * @see javax.jms.TopicPublisher
 * @see javax.jms.QueueSender
 * @see javax.jms.Session#createProducer
 */
public class TPJMSMessageProducer implements MessageProducer
{
	private static final Logger logger = LogManager.getLogger(TPJMSMessageProducer.class);

	/**
	 * Lock used to guard configuration parameters.
	 */
	private final ReentrantReadWriteLock configLock = new ReentrantReadWriteLock();

	/**
	 * Delivery mode - must be gated by configLock.
	 */
	private int defaultDeliveryMode = Message.DEFAULT_DELIVERY_MODE;
	
	/**
	 * Priority - must be gated by configLock.
	 */
	private int defaultPriority = Message.DEFAULT_PRIORITY;
	
	/**
	 * Time-to-live in milliseconds - must be gated by configLock.
	 */
	private long defaultTimeToLive = Message.DEFAULT_TIME_TO_LIVE;
	
	/**
	 * Destination
	 */
	private final Destination defaultDestination;
	
	TPJMSMessageProducer(Destination destination) throws JMSException
	{
		this.defaultDestination = destination;
		
		if (destination != null)
			validateDestination(destination);
	}
	
	/**
	 * Closes the message producer.
	 * 
	 * <p>
	 * Since a provider may allocate some resources on behalf of a
	 * <code>MessageProducer</code> outside the Java virtual machine, clients
	 * should close them when they are not needed. Relying on garbage collection
	 * to eventually reclaim these resources may not be timely enough.
	 * </p>
	 * 
	 * @throws JMSException
	 *           if the JMS provider fails to close the producer due to some
	 *           internal error.
	 */
	@Override
	public void close() throws JMSException
	{
		// TODO Auto-generated method stub
		logger.warn("close() not implmented");
	}

	/**
	 * Gets the producer's default delivery mode.
	 * 
	 * @return the message delivery mode for this message producer
	 * @see #setDeliveryMode(int)
	 */
	@Override
	public int getDeliveryMode()
	{
		try
		{
			configLock.readLock().lock();
			return defaultDeliveryMode;
		}
		finally
		{
			configLock.readLock().unlock();
		}
	}

	/**
	 * Gets the destination associated with this MessageProducer.
	 * 
	 * @return this producer's Destination
	 * @since JMS 1.1
	 */
	@Override
	public Destination getDestination()
	{
		return defaultDestination;
	}

	/**
	 * Gets an indication of whether message IDs are disabled.
	 * 
	 * @return <code>false</code>
	 */
	@Override
	public boolean getDisableMessageID()
	{
		return false;
	}

	/**
	 * Gets an indication of whether message timestamps are disabled.
	 * 
	 * @return <code>false</code>
	 */
	@Override
	public boolean getDisableMessageTimestamp()
	{
		return false;
	}

	/**
	 * Gets the producer's default priority.
	 * 
	 * @return the message priority for this message producer
	 * @see #setPriority(int)
	 */
	@Override
	public int getPriority()
	{
		try
		{
			configLock.readLock().lock();
			return defaultPriority;
		}
		finally
		{
			configLock.readLock().unlock();
		}
	}

	/**
	 * Gets the default length of time in milliseconds from its dispatch time that
	 * a produced message should be retained by the message system.
	 * 
	 * @return the message time to live in milliseconds; zero is unlimited
	 * @see #setTimeToLive(long)
	 */
	@Override
	public long getTimeToLive()
	{
		try
		{
			configLock.readLock().lock();
			return defaultTimeToLive;
		}
		finally
		{
			configLock.readLock().unlock();
		}
	}

	/**
	 * Sends a message using the MessageProducer's default delivery mode,
	 * priority, and time to live.
	 * 
	 * @param message
	 *          the message to send
	 * 
	 * @throws JMSException
	 *           if the JMS provider fails to send the message due to some
	 *           internal error.
	 * @throws MessageFormatException
	 *           if an invalid message is specified.
	 * @throws InvalidDestinationException
	 *           if a client uses this method with a <code>MessageProducer</code>
	 *           with an invalid destination.
	 * @throws UnsupportedOperationException
	 *           - if a client uses this method with a
	 *           <code>MessageProducer</code> that did not specify a destination
	 *           at creation time.
	 * @since JMS 1.1
	 * @see Session#createProducer(javax.jms.Destination)
	 * @see MessageProducer
	 */
	@Override
	public void send(Message message) throws JMSException
	{
		if (defaultDestination == null)
		{
			throw new UnsupportedOperationException("This producer requires a destination to be specified at send() time.");
		}
		
		int deliveryMode;
		int priority;
		long timeToLive;
		
		try 
		{
			configLock.readLock().lock();
			
			deliveryMode = defaultDeliveryMode;
			priority = defaultPriority;
			timeToLive = defaultTimeToLive;
		}
		finally
		{
			configLock.readLock().unlock();
		}

		sendInternal(defaultDestination, message, deliveryMode, priority, timeToLive);
	}

	/**
	 * Sends a message to a destination for an unidentified message producer. Uses
	 * the <code>MessageProducer</code>'s default delivery mode, priority, and
	 * time to live.
	 * 
	 * <p>
	 * Typically, a message producer is assigned a destination at creation time;
	 * however, the JMS API also supports unidentified message producers, which
	 * require that the destination be supplied every time a message is sent.
	 * </p>
	 * 
	 * @param destination
	 *          the destination to send this message to
	 * @param message
	 *          the message to send
	 * @throws JMSException
	 *           if the JMS provider fails to send the message due to some
	 *           internal error.
	 * @throws MessageFormatException
	 *           if an invalid message is specified.
	 * @throws InvalidDestinationException
	 *           if a client uses this method with an invalid destination.
	 * @throws UnsupportedOperationException
	 *           if a client uses this method with a MessageProducer that
	 *           specified a destination at creation time.
	 * @since JMS 1.1
	 * @see Session#createProducer(javax.jms.Destination)
	 * @see MessageProducer
	 */
	@Override
	public void send(Destination destination, Message message) throws JMSException
	{
		if (defaultDestination != null)
		{
			throw new UnsupportedOperationException("This producer does not allow a destination to be specified at send() time");
		}
		
		int deliveryMode;
		int priority;
		long timeToLive;
		
		try 
		{
			configLock.readLock().lock();
			
			deliveryMode = defaultDeliveryMode;
			priority = defaultPriority;
			timeToLive = defaultTimeToLive;
		}
		finally
		{
			configLock.readLock().unlock();
		}

		sendInternal(destination, message, deliveryMode, priority, timeToLive);
	}

	/**
	 * Sends a message to the destination, specifying delivery mode, priority, and
	 * time to live.
	 * 
	 * @param message
	 *          the message to send
	 * @param deliveryMode
	 *          the delivery mode to use
	 * @param priority
	 *          the priority for this message
	 * @param timeToLive
	 *          the message's lifetime (in milliseconds)
	 * @throws JMSException
	 *           if the JMS provider fails to send the message due to some
	 *           internal error.
	 * @throws MessageFormatException
	 *           if an invalid message is specified.
	 * @throws InvalidDestinationException
	 *           if a client uses this method with a <code>MessageProducer</code>
	 *           with an invalid destination.
	 * @throws UnsupportedOperationException
	 *           if a client uses this method with a <code>MessageProducer</code>
	 *           that did not specify a destination at creation time.
	 * @since JMS 1.1
	 * @see Session#createProducer(javax.jms.Destination)
	 */
	@Override
	public void send(Message message, int deliveryMode, int priority, long timeToLive) throws JMSException
	{
		if (defaultDestination == null)
		{
			throw new UnsupportedOperationException("This producer requires a destination to be specified at send() time.");
		}
		
		sendInternal(defaultDestination, message, deliveryMode, priority, timeToLive);
	}

	/**
	 * Sends a message to a destination for an unidentified message producer,
	 * specifying delivery mode, priority and time to live.
	 * 
	 * <p>
	 * Typically, a message producer is assigned a destination at creation time;
	 * however, the JMS API also supports unidentified message producers, which
	 * require that the destination be supplied every time a message is sent.
	 * </p>
	 * 
	 * @param destination
	 *          the destination to send this message to
	 * @param message
	 *          the message to send
	 * @param deliveryMode
	 *          the delivery mode to use
	 * @param priority
	 *          the priority for this message
	 * @param timeToLive
	 *          the message's lifetime (in milliseconds)
	 * @throws JMSException
	 *           if the JMS provider fails to send the message due to some
	 *           internal error.
	 * @throws MessageFormatException
	 *           if an invalid message is specified.
	 * @param InvalidDestinationException
	 *          if a client uses this method with an invalid destination.
	 * @since JMS 1.1
	 * @see Session#createProducer(javax.jms.Destination)
	 */
	@Override
	public void send(Destination destination, Message message, int deliveryMode, int priority, long timeToLive) throws JMSException
	{
		if (defaultDestination != null)
		{
			throw new UnsupportedOperationException("This producer does not allow a destination to be specified at send() time");
		}
		
		sendInternal(destination, message, deliveryMode, priority, timeToLive);
	}
	
	@Override
	public void setDeliveryMode(int deliveryMode) throws JMSException
	{
		validateDeliveryMode(deliveryMode);
		
		try
		{
			configLock.writeLock().lock();
			defaultDeliveryMode = deliveryMode;
		}
		finally
		{
			configLock.writeLock().unlock();
		}
	}

	/**
	 * Sets whether message IDs are disabled.
	 * 
	 * <p>
	 * This implementation always generates message IDs.
	 * </p>
	 * 
	 * @param value
	 *          indicates if message IDs are disabled
	 */
	@Override
	public void setDisableMessageID(boolean value)
	{
		logger.debug("Application requested message ID generation to be " + (value ? "disabled" : "enabled") + ", ignoring");
	}

	/**
	 * Sets whether message timestamp generation is disabled.
	 * <p>
	 * This implementation always generates message timestamps.
	 * </p>
	 * 
	 * @param value
	 *          indicates if message timestamps are disabled
	 */
	@Override
	public void setDisableMessageTimestamp(boolean value)
	{
		logger.debug("Application requested message timestamp generation to be " + (value ? "disabled" : "enabled") + ", ignoring");
	}

	/**
	 * Sets the producer's default priority.
	 * 
	 * <p>
	 * The JMS API defines ten levels of priority value, with 0 as the lowest
	 * priority and 9 as the highest. Clients should consider priorities 0-4 as
	 * gradations of normal priority and priorities 5-9 as gradations of expedited
	 * priority. Priority is set to 4 by default.
	 * </p>
	 * 
	 * @param priority
	 *          the message priority for this message producer; must be a value
	 *          between 0 and 9
	 * @throws JMSException
	 *           if the JMS provider fails to set the priority due to some
	 *           internal error.
	 * @see #getPriority()
	 * @see Message#DEFAULT_PRIORITY
	 */
	@Override
	public void setPriority(int priority) throws JMSException
	{
		validatePriority(priority);
		
		try
		{
			configLock.writeLock().lock();
			defaultPriority = priority;
		}
		finally
		{
			configLock.writeLock().unlock();
		}
	}

	/**
	 * Sets the default length of time in milliseconds from its dispatch time that
	 * a produced message should be retained by the message system.
	 * 
	 * <p>
	 * Time to live is set to zero by default.
	 * </p>
	 * 
	 * @param timeToLive
	 *          the message time to live in milliseconds; zero is unlimited
	 * @throws JMSException
	 *           if the JMS provider fails to set the time to live due to some
	 *           internal error.
	 * @see #getTimeToLive()
	 * @see Message#DEFAULT_TIME_TO_LIVE
	 */
	@Override
	public void setTimeToLive(long timeToLive) throws JMSException
	{
		validateTimeToLive(timeToLive);
		
		try
		{
			configLock.writeLock().lock();
			defaultTimeToLive = timeToLive;
		}
		finally
		{
			configLock.writeLock().unlock();
		}
	}

	private void sendInternal(Destination destination, Message message, int deliveryMode, int priority, long timeToLive) throws JMSException
	{
		validateDestination(destination);
		validateDeliveryMode(deliveryMode);
		validatePriority(priority);
		validateTimeToLive(timeToLive);	

		TPJMSDestination jmsDestination = (TPJMSDestination) destination;
		jmsDestination.send(message);
	}
	
	private void validateDestination(Destination destination) throws InvalidDestinationException
	{
		if (destination == null)
		{
			throw new InvalidDestinationException("destination must be specified");
		}
		
		if (!(destination instanceof TPJMSDestination))
		{
			throw new InvalidDestinationException("Invalid destination: " + destination);
		}
	}

	private void validateDeliveryMode(int deliveryMode) throws JMSException
	{
		if (deliveryMode != DeliveryMode.PERSISTENT && deliveryMode != DeliveryMode.NON_PERSISTENT)
		{
			throw new JMSException("Invalid delivery mode: " + deliveryMode);
		}
	}
	
	private void validatePriority(int priority) throws JMSException
	{
		if (priority < 0 || priority > 9)
		{
			throw new JMSException("Invalid priority: " + priority);
		}
	}

	private void validateTimeToLive(long timeToLive) throws JMSException
	{
		if (timeToLive < 0L)
		{
			throw new JMSException("Invalid timeToLive: " + timeToLive);
		}
	}
}
