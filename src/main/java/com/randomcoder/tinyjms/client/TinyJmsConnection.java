package com.randomcoder.tinyjms.client;

import java.util.concurrent.locks.ReentrantReadWriteLock;

import javax.jms.*;
import javax.jms.IllegalStateException;

/**
 * TinyJms implementation of Connection.
 */
public class TinyJmsConnection implements Connection, QueueConnection, TopicConnection
{
	private final ReentrantReadWriteLock clientIDLock = new ReentrantReadWriteLock();	

	private String clientID;

	TinyJmsConnection(String clientID)
	{
		this.clientID = clientID;
	}

	/*
	 * Connection implementation
	 */

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

	/**
	 * Gets the client identifier for this connection.
	 * 
	 * <p>
	 * This value is specific to the JMS provider. It is either preconfigured by
	 * an administrator in a {@link ConnectionFactory} object or assigned
	 * dynamically by the application by calling the {@link #setClientID(String)}
	 * method.
	 * </p>
	 * 
	 * @return the unique client identifier
	 * @throw JMSException if the JMS provider fails to return the client ID for
	 *        this connection due to some internal error.
	 */
	@Override
	public String getClientID() throws JMSException
	{
		try
		{
			clientIDLock.readLock().lock();			
			return clientID;
		}
		finally
		{
			clientIDLock.readLock().unlock();
		}
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

	/**
	 * Sets the client identifier for this connection.
	 * 
	 * <p>
	 * The preferred way to assign a JMS client's client identifier is for it to
	 * be configured in a client-specific {@link ConnectionFactory} object and
	 * transparently assigned to the Connection object it creates.
	 * </p>
	 * 
	 * <p>
	 * Alternatively, a client can set a connection's client identifier using a
	 * provider-specific value. The facility to set a connection's client
	 * identifier explicitly is not a mechanism for overriding the identifier that
	 * has been administratively configured. It is provided for the case where no
	 * administratively specified identifier exists. If one does exist, an attempt
	 * to change it by setting it must throw an {@link IllegalStateException}. If
	 * a client sets the client identifier explicitly, it must do so immediately
	 * after it creates the connection and before any other action on the
	 * connection is taken. After this point, setting the client identifier is a
	 * programming error that should throw an {@link IllegalStateException}.
	 * </p>
	 * 
	 * <p>
	 * The purpose of the client identifier is to associate a connection and its
	 * objects with a state maintained on behalf of the client by a provider. The
	 * only such state identified by the JMS API is that required to support
	 * durable subscriptions.
	 * </p>
	 * 
	 *<p>
	 * If another connection with the same clientID is already running when this
	 * method is called, the JMS provider should detect the duplicate ID and throw
	 * an InvalidClientIDException.
	 * <p>
	 * 
	 * @param clientID
	 *          the unique client identifier
	 * @throws JMSException
	 *           if the JMS provider fails to set the client ID for this
	 *           connection due to some internal error.
	 * @throws InvalidClientIDException
	 *           if the JMS client specifies an invalid or duplicate client ID.
	 * @throws IllegalStateException
	 *           if the JMS client attempts to set a connection's client ID at the
	 *           wrong time or when it has been administratively configured.
	 */
	@Override
	public void setClientID(String clientID) throws JMSException, InvalidClientIDException, IllegalStateException
	{
		try
		{
			clientIDLock.writeLock().lock();
			
			if (this.clientID != null)
			{
				throw new IllegalStateException("Client ID is already set: " + clientID);
			}
			
			// TODO verify with server that client ID is not already taken
			
			this.clientID = clientID;
		}
		finally
		{
			clientIDLock.writeLock().unlock();
		}
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

	/*
	 * TopicConnection implementation
	 */

	/**
	 * @see #createConnectionConsumer(Destination, String, ServerSessionPool, int)
	 */
	@Override
	public ConnectionConsumer createConnectionConsumer(Topic topic, String messageSelector, ServerSessionPool sessionPool, int maxMessages) throws JMSException
	{
		return createConnectionConsumer((Destination) topic, messageSelector, sessionPool, maxMessages);
	}

	/**
	 * @see #createSession(boolean, int)
	 */
	@Override
	public TopicSession createTopicSession(boolean transacted, int acknowledgeMode) throws JMSException
	{
		return (TopicSession) createSession(transacted, acknowledgeMode);
	}

	/*
	 * QueueConnection implementation
	 */

	/**
	 * @see #createConnectionConsumer(Destination, String, ServerSessionPool, int)
	 */
	@Override
	public ConnectionConsumer createConnectionConsumer(Queue queue, String messageSelector, ServerSessionPool sessionPool, int maxMessages) throws JMSException
	{
		return createConnectionConsumer((Destination) queue, messageSelector, sessionPool, maxMessages);
	}

	/**
	 * @see #createSession(boolean, int)
	 */
	@Override
	public QueueSession createQueueSession(boolean transacted, int acknowledgeMode) throws JMSException
	{
		return (QueueSession) createSession(transacted, acknowledgeMode);
	}
}