package com.randomcoder.tinyjms.client;

import java.util.concurrent.atomic.AtomicReference;

import javax.jms.*;

/**
 * TinyJms implementation of {@link ConnectionFactory}.
 */
public class TinyJmsConnectionFactory implements ConnectionFactory, QueueConnectionFactory, TopicConnectionFactory
{
	private final AtomicReference<String> clientIDRef = new AtomicReference<String>();

	/**
	 * Sets the Client ID.
	 * 
	 * @param clientID
	 *          client ID
	 * @see Connection#setClientID(String)
	 */
	public void setClientID(String clientID)
	{
		this.clientIDRef.set(clientID);
	}
	
	public String getClientID()
	{
		return clientIDRef.get();
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
		// TODO Auto-generated method stub
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
		// TODO Auto-generated method stub
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