package com.randomcoder.tinyjms.client;

import javax.jms.Destination;

abstract public class TinyJmsDestination implements Destination
{
	private final String name;

	TinyJmsDestination(String name)
	{
		this.name = name;
	}

	public String getName()
	{
		return name;
	}

	/**
	 * Determines if this destination is a queue.
	 * 
	 * @return <code>true</code> if a queu
	 */
	abstract public boolean isQueue();

	/**
	 * Determines if this destination is a topic.
	 * 
	 * @return <code>true</code> if a topic
	 */
	abstract public boolean isTopic();

	/**
	 * Determines if this destination is temporary.
	 * 
	 * @return <code>true</code> if temporary
	 */
	abstract public boolean isTemporary();
}
