package com.randomcoder.tinyjms.client;

import java.io.Serializable;

import javax.jms.*;

/**
 * TinyJms implementation of {@link ObjectMessage}.
 */
public class TinyJmsObjectMessage extends TinyJmsMessage implements ObjectMessage
{
	private Serializable object;

	/**
	 * Gets the serializable object containing this message's data. The default
	 * value is <code>null</code>.
	 * 
	 * @return the serializable object containing this message's data
	 * @throws JMSException
	 *           if the JMS provider fails to get the object due to some internal
	 *           error.
	 * @throws MessageFormatException
	 *           if object deserialization fails.
	 */
	@Override
	public Serializable getObject() throws JMSException
	{
		// TODO deserialize
		return object;
	}

	/**
	 * Sets the serializable object containing this message's data. It is
	 * important to note that an <code>ObjectMessage</code> contains a snapshot of
	 * the object at the time <code>setObject()</code> is called; subsequent
	 * modifications of the object will have no effect on the
	 * <code>ObjectMessage</code> body.
	 * 
	 * @param object
	 *          the message's data
	 * @throws JMSException
	 *           if the JMS provider fails to set the object due to some internal
	 *           error.
	 * @throws MessageFormatException
	 *           if object serialization fails.
	 * @throws MessageNotWriteableException
	 *           if the message is in read-only mode.
	 */
	@Override
	public void setObject(Serializable object) throws JMSException
	{
		// TODO serialize and verify that message is writable
		this.object = object;
	}

}