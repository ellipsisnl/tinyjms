package com.randomcoder.tinyjms.client;

import javax.jms.*;

/**
 * TinyJms implementation of ConnectionFactory.
 */
public class TinyJmsConnectionFactory implements ConnectionFactory
{
	
	@Override
	public Connection createConnection() throws JMSException
	{
		// TODO Auto-generated method stub
		return new TinyJmsConnection();
	}

	@Override
	public Connection createConnection(String userName, String password) throws JMSException
	{
		// TODO Auto-generated method stub
		return new TinyJmsConnection();
	}
}