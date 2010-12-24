package com.randomcoder.tinyjms.client;

import javax.jms.*;

public class TinyJmsTopic extends TinyJmsDestination implements Topic
{
	TinyJmsTopic(String topicName)
	{
		super(topicName);
	}

	@Override
	public boolean isQueue()
	{
		return false;
	}

	@Override
	public final boolean isTopic()
	{
		return true;
	}

	@Override
	public boolean isTemporary()
	{
		return false;
	}

	@Override
	public String getTopicName() throws JMSException
	{
		return super.getName();
	}

	/**
	 * Returns a string representation of this object.
	 * 
	 * @return the provider-specific identity values for this topic
	 */
	@Override
	public String toString()
	{
		return "topic:" + super.toString();
	}
}