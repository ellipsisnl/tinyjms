package com.randomcoder.tinyjms.client;

import javax.jms.*;

/**
 * TinyJms implementation of {@link TextMessage}.
 */
public class TinyJmsTextMessage extends TinyJmsMessage implements TextMessage
{
	private String text;
	
	TinyJmsTextMessage()
	{
		text = null;
	}
	
	TinyJmsTextMessage(String text)
	{
		this.text = text; 
	}

	/**
	 * Gets the string containing this message's data. The default value is
	 * <code>null</code>.
	 * 
	 * @return the <code>String</code> containing the message's data
	 * @throws JMSException
	 *           if the JMS provider fails to get the text due to some internal
	 *           error.
	 */
	@Override
	public String getText() throws JMSException
	{
		return text;
	}

	/**
	 * Sets the string containing this message's data.
	 * 
	 * @param text
	 *          the <code>String</code> containing the message's data
	 * 
	 * @throws JMSException
	 *           if the JMS provider fails to set the text due to some internal
	 *           error.
	 * @throws MessageNotWriteableException
	 *           if the message is in read-only mode.
	 */
	@Override
	public void setText(String text) throws JMSException
	{
		if (super.isReadOnly())
			throw new MessageNotWriteableException("Message is read-only");
		
		this.text = text;
	}
	
}