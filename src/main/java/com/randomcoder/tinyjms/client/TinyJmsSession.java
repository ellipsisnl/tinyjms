package com.randomcoder.tinyjms.client;

import java.io.Serializable;

import javax.jms.*;

public class TinyJmsSession implements Session, QueueSession, TopicSession
{
	private final int acknowledgeMode;
	
	/**
	 * Creates a new JMS session.
	 * 
	 * @param transacted
	 *          indicates whether the session is transacted
	 * @param acknowledgeMode
	 *          indicates whether the consumer or the client will acknowledge any
	 *          messages it receives; ignored if the session is transacted. Legal
	 *          values are {@link Session#AUTO_ACKNOWLEDGE},
	 *          {@link Session#CLIENT_ACKNOWLEDGE}, and
	 *          {@link Session#DUPS_OK_ACKNOWLEDGE}.
	 * @throws JMSException
	 *           if the Connection object fails to create a session due to some
	 *           internal error or lack of support for the specific transaction
	 *           and acknowledgement mode.
	 * @since JMS 1.1
	 * @see TinyJmsConnection#createSession(boolean, int)
	 * @see Session#AUTO_ACKNOWLEDGE
	 * @see Session#CLIENT_ACKNOWLEDGE
	 * @see Session#DUPS_OK_ACKNOWLEDGE
	 */
	TinyJmsSession(boolean transacted, int acknowledgeMode) throws JMSException
	{
		if (transacted)
		{
			// per spec, ignore ack value if transacted
			acknowledgeMode = SESSION_TRANSACTED;
		}
		else if (acknowledgeMode == SESSION_TRANSACTED)
		{
			// conflicting modes
			throw new JMSException("SESSION_TRANSACTED requires a transacted Session.");
		}
		
		if (acknowledgeMode != AUTO_ACKNOWLEDGE &&
				acknowledgeMode != CLIENT_ACKNOWLEDGE &&
				acknowledgeMode != DUPS_OK_ACKNOWLEDGE &&
				acknowledgeMode != SESSION_TRANSACTED)
		{
			throw new JMSException("Invalid acknowledgeMode: " + acknowledgeMode);
		}
		
		this.acknowledgeMode = acknowledgeMode;
	}
	
	@Override
	public void close() throws JMSException
	{
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException();
	}
	
	@Override
	public void commit() throws JMSException
	{
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException();
	}
	
	@Override
	public QueueBrowser createBrowser(Queue queue) throws JMSException
	{
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException();
	}
	
	@Override
	public QueueBrowser createBrowser(Queue queue, String messageSelector) throws JMSException
	{
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException();
	}
	
	@Override
	public BytesMessage createBytesMessage() throws JMSException
	{
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException();
	}
	
	@Override
	public MessageConsumer createConsumer(Destination destination) throws JMSException
	{
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException();
	}
	
	@Override
	public MessageConsumer createConsumer(Destination destination, String messageSelector) throws JMSException
	{
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException();
	}
	
	@Override
	public MessageConsumer createConsumer(Destination destination, String messageSelector, boolean NoLocal) throws JMSException
	{
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException();
	}
	
	@Override
	public TopicSubscriber createDurableSubscriber(Topic topic, String name) throws JMSException
	{
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException();
	}
	
	@Override
	public TopicSubscriber createDurableSubscriber(Topic topic, String name, String messageSelector, boolean noLocal) throws JMSException
	{
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException();
	}
	
	@Override
	public MapMessage createMapMessage() throws JMSException
	{
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException();
	}
	
	@Override
	public Message createMessage() throws JMSException
	{
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException();
	}
	
	@Override
	public ObjectMessage createObjectMessage() throws JMSException
	{
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException();
	}
	
	@Override
	public ObjectMessage createObjectMessage(Serializable object) throws JMSException
	{
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException();
	}
	
	@Override
	public MessageProducer createProducer(Destination destination) throws JMSException
	{
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException();
	}
	
	@Override
	public Queue createQueue(String queueName) throws JMSException
	{
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException();
	}
	
	@Override
	public StreamMessage createStreamMessage() throws JMSException
	{
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException();
	}
	
	@Override
	public TemporaryQueue createTemporaryQueue() throws JMSException
	{
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException();
	}
	
	@Override
	public TemporaryTopic createTemporaryTopic() throws JMSException
	{
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException();
	}
	
	@Override
	public TextMessage createTextMessage() throws JMSException
	{
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException();
	}
	
	@Override
	public TextMessage createTextMessage(String text) throws JMSException
	{
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException();
	}
	
	@Override
	public Topic createTopic(String topicName) throws JMSException
	{
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException();
	}
	
	/**
	 * Returns the acknowledgement mode of the session. The acknowledgement mode
	 * is set at the time that the session is created. If the session is
	 * transacted, the acknowledgement mode is ignored.
	 * 
	 * @return If the session is not transacted, returns the current
	 *         acknowledgement mode for the session. If the session is transacted,
	 *         returns {@link Session#SESSION_TRANSACTED}.
	 * @since JMS 1.1
	 * @see Connection#createSession(boolean, int)
	 */
	@Override
	public int getAcknowledgeMode()
	{
		return acknowledgeMode;
	}
	
	@Override
	public MessageListener getMessageListener() throws JMSException
	{
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException();
	}
	
	/**
	 * Indicates whether the session is in transacted mode.
	 * 
	 * @return <code>true</code> if the session is in transacted mode
	 */
	@Override
	public boolean getTransacted()
	{
		return acknowledgeMode == SESSION_TRANSACTED;
	}
	
	@Override
	public void recover() throws JMSException
	{
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException();
	}
	
	@Override
	public void rollback() throws JMSException
	{
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException();
	}
	
	@Override
	public void run()
	{
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException();
	}
	
	@Override
	public void setMessageListener(MessageListener listener) throws JMSException
	{
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException();
	}
	
	@Override
	public void unsubscribe(String name) throws JMSException
	{
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException();
	}

	@Override
	public TopicPublisher createPublisher(Topic topic) throws JMSException
	{
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException();
	}

	@Override
	public TopicSubscriber createSubscriber(Topic topic, String messageSelector, boolean noLocal) throws JMSException
	{
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException();
	}

	@Override
	public TopicSubscriber createSubscriber(Topic topic) throws JMSException
	{
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException();
	}

	@Override
	public QueueReceiver createReceiver(Queue queue, String messageSelector) throws JMSException
	{
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException();
	}

	@Override
	public QueueReceiver createReceiver(Queue queue) throws JMSException
	{
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException();
	}

	@Override
	public QueueSender createSender(Queue queue) throws JMSException
	{
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException();
	}	
	
}