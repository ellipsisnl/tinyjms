package com.randomcoder.tinyjms.client;

import javax.jms.*;

/**
 * TinyJms implementation of {@link Queue}.
 */
public class TinyJmsQueue extends TinyJmsDestination implements Queue
{
	TinyJmsQueue(String queueName)
	{
		super(queueName);
	}

	@Override
	public final boolean isQueue()
	{
		return true;
	}

	@Override
	public boolean isTopic()
	{
		return false;
	}

	@Override
	public boolean isTemporary()
	{
		return false;
	}

	/**
	 * Gets the name of this queue.
	 * 
	 * <p>
	 * Clients that depend upon the name are not portable.
	 * <p>
	 * 
	 * @return the queue name
	 */
	@Override
	public String getQueueName()
	{
		return super.getName();
	}

	/**
	 * Returns a string representation of this object.
	 * 
	 * @return the provider-specific identity values for this queue
	 */
	@Override
	public String toString()
	{
		return "queue:" + super.toString();
	}
}