package com.randomcoder.tinyjms.client;

import java.util.concurrent.locks.ReentrantReadWriteLock;

import javax.jms.*;

import com.randomcoder.tinyjms.provider.*;

/**
 * TinyJms implementation of {@link ConnectionFactory}.
 */
public class TinyJmsConnectionFactory implements ConnectionFactory, QueueConnectionFactory, TopicConnectionFactory
{
	/**
	 * Lock used to gate access to configuration variables.
	 */
	private final ReentrantReadWriteLock configLock = new ReentrantReadWriteLock();

	/**
	 * Currently configured client ID. Must use configLock for access.
	 */
	private String clientID;
	
	/**
	 * Currently configured provider. Must use configLock for access.
	 */
	private TinyJmsProvider provider;
	
	/**
	 * Currently configured url. Must use configLock for access.
	 */
	private String url;
	
	/**
	 * Creates a new JMS connection factory.
	 * 
	 * By default, the factory is wired to a local in-memory provider.
	 * 
	 * @throws JMSException
	 *           if the connection factory could not be instantiated
	 */
	public TinyJmsConnectionFactory() throws JMSException
	{
		this("vm:default");
	}

	/**
	 * Creates a new JMS connection factory with the given URL.
	 * 
	 * @param url
	 *          connection URL
	 * @throws JMSException
	 *           if the connection factory could not be instantiated
	 */
	public TinyJmsConnectionFactory(String url) throws JMSException
	{
		setUrl(url);
	}

	/**
	 * Sets the connection URL to use for new connections.
	 * 
	 * @param url
	 *          connection URL
	 * @throws JMSException
	 *           if the URL is invalid or a provider could not be located
	 */
	public void setUrl(String url) throws JMSException
	{
		try
		{
			configLock.writeLock().lock();
			this.provider = ProviderRegistry.getProviderForUrl(url);
			this.url = url;
		}
		finally
		{
			configLock.writeLock().unlock();
		}
	}

	/**
	 * Sets the Client ID.
	 * 
	 * @param clientID
	 *          client ID
	 * @see Connection#setClientID(String)
	 */
	public void setClientID(String clientID)
	{
		try
		{
			configLock.writeLock().lock();
			this.clientID = clientID;
		}
		finally
		{
			configLock.writeLock().unlock();
		}
	}

	public String getClientID()
	{
		try
		{
			configLock.readLock().lock();
			return clientID;
		}
		finally
		{
			configLock.readLock().unlock();
		}
	}

	/*
	 * ConnectionFactory implementation
	 */

	/**
	 * Creates a connection with the default user identity. The connection is
	 * created in stopped mode. No messages will be delivered until the
	 * {@link Connection#start()} method is explicitly called.
	 * 
	 * @return a newly created connection
	 * @throws JMSException
	 *           if the JMS provider fails to create the connection due to some
	 *           internal error.
	 * @throws JMSSecurityException
	 *           if client authentication fails due to an invalid user name or
	 *           password.
	 * @since JMS 1.1
	 */
	@Override
	public Connection createConnection() throws JMSException, JMSSecurityException
	{
		return new TinyJmsConnection(getClientID());
	}

	/**
	 * Creates a connection with the specified user identity. The connection is
	 * created in stopped mode. No messages will be delivered until the
	 * {@link Connection#start()} method is explicitly called.
	 * 
	 * @param userName
	 *          the caller's user name
	 * @param password
	 *          the caller's password
	 * @return a newly created connection
	 * @throws JMSException
	 *           if the JMS provider fails to create the connection due to some
	 *           internal error.
	 * @throws JMSSecurityException
	 *           if client authentication fails due to an invalid user name or
	 *           password.
	 * @since JMS 1.1
	 */
	@Override
	public Connection createConnection(String userName, String password) throws JMSException, JMSSecurityException
	{
		return new TinyJmsConnection(getClientID());
	}

	/*
	 * TopicConnectionFactory implementation
	 */

	/**
	 * @see #createConnection()
	 */
	@Override
	public TopicConnection createTopicConnection() throws JMSException
	{
		return (TopicConnection) createConnection();
	}

	/**
	 * @see #createConnection(String, String)
	 */
	@Override
	public TopicConnection createTopicConnection(String userName, String password) throws JMSException
	{
		return (TopicConnection) createConnection(userName, password);
	}

	/*
	 * QueueConnectionFactory implementation
	 */

	/**
	 * @see #createConnection()
	 */
	@Override
	public QueueConnection createQueueConnection() throws JMSException
	{
		return (QueueConnection) createConnection();
	}

	/**
	 * @see #createConnection(String, String)
	 */
	@Override
	public QueueConnection createQueueConnection(String userName, String password) throws JMSException
	{
		return (QueueConnection) createConnection(userName, password);
	}

}