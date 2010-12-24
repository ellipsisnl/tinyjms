package com.randomcoder.tinyjms.client;

import javax.jms.*;

import org.apache.log4j.*;

public class TinyJmsTemporaryQueue extends TinyJmsQueue implements TemporaryQueue
{
	private static final Logger logger = LogManager.getLogger(TinyJmsTemporaryQueue.class);
	
	TinyJmsTemporaryQueue(String temporaryQueueName)
	{
		super(temporaryQueueName);
	}

	@Override
	public final boolean isTemporary()
	{
		return true;
	}

	/**
	 * Deletes this temporary queue. If there are existing receivers still using
	 * it, a <code>JMSException</code> will be thrown.
	 * 
	 * @throws JMSException
	 *           if the JMS provider fails to delete the temporary queue due to
	 *           some internal error.
	 */
	@Override
	public void delete() throws JMSException
	{
		// TODO Auto-generated method stub
		logger.warn("delete() not implemented yet");
	}

	@Override
	public String toString()
	{
		return "temp" + super.toString();
	}

}