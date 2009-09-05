package com.randomcoder.tinyjms.client;

import javax.jms.*;

/**
 * TinyJms implementation of Connection.
 */
public class TinyJmsConnection implements Connection
{
	TinyJmsConnection()
	{
	}
	
	@Override
	public void close() throws JMSException
	{
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException();
	}
	
	@Override
	public ConnectionConsumer createConnectionConsumer(Destination destination, String messageSelector, ServerSessionPool sessionPool, int maxMessages)
			throws JMSException
	{
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException();
	}
	
	@Override
	public ConnectionConsumer createDurableConnectionConsumer(Topic topic, String subscriptionName, String messageSelector, ServerSessionPool sessionPool,
			int maxMessages) throws JMSException
	{
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException();
	}
	
	/**
	 * Creates a {@link Session} object.
	 * 
	 * @param transacted
	 *          indicates whether the session is transacted
	 * @param acknowledgeMode
	 *          indicates whether the consumer or the client will acknowledge any
	 *          messages it receives; ignored if the session is transacted. Legal
	 *          values are {@link Session#AUTO_ACKNOWLEDGE},
	 *          {@link Session#CLIENT_ACKNOWLEDGE}, and
	 *          {@link Session#DUPS_OK_ACKNOWLEDGE}.
	 * @return a newly created session
	 * @throws JMSException
	 *           if the Connection object fails to create a session due to some
	 *           internal error or lack of support for the specific transaction
	 *           and acknowledgement mode.
	 * @since JMS 1.1
	 * @see Session#AUTO_ACKNOWLEDGE
	 * @see Session#CLIENT_ACKNOWLEDGE
	 * @see Session#DUPS_OK_ACKNOWLEDGE
	 */
	@Override
	public Session createSession(boolean transacted, int acknowledgeMode) throws JMSException
	{
		return new TinyJmsSession(transacted, acknowledgeMode);
	}
	
	@Override
	public String getClientID() throws JMSException
	{
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException();
	}
	
	@Override
	public ExceptionListener getExceptionListener() throws JMSException
	{
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException();
	}
	
	@Override
	public ConnectionMetaData getMetaData() throws JMSException
	{
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException();
	}
	
	@Override
	public void setClientID(String clientID) throws JMSException
	{
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException();
	}
	
	@Override
	public void setExceptionListener(ExceptionListener listener) throws JMSException
	{
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException();
	}
	
	@Override
	public void start() throws JMSException
	{
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException();
	}
	
	@Override
	public void stop() throws JMSException
	{
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException();
	}
	
}
