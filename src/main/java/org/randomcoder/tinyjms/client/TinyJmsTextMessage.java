package com.randomcoder.tinyjms.client;

import java.nio.charset.Charset;

import javax.jms.*;

/**
 * TinyJms implementation of {@link TextMessage}.
 */
public class TinyJmsTextMessage extends TinyJmsMessage implements TextMessage
{
	private String text;
	private boolean readOnly = false;
	
	TinyJmsTextMessage()
	{
		text = null;
	}
	
	TinyJmsTextMessage(String text)
	{
		this.text = text; 
	}

	/**
	 * Sets the read-only flag for this message.
	 * 
	 * @param readOnly
	 *          <code>true</code> if read-only
	 */
	void setReadOnly(boolean readOnly)
	{
		this.readOnly = readOnly;
	}
	
	
	@Override
	byte[] getBody() throws JMSException
	{
		try
		{
			return (text == null) ? null : text.getBytes(Charset.forName("UTF-8"));
		}
		catch (Exception e)
		{
			throw new JMSException("Unable to serialize body: " + e.getMessage());
		}
	}

	@Override
	void setBody(byte[] data) throws JMSException
	{
		try
		{
			text = (data == null) ? null : new String(data, Charset.forName("UTF-8"));
		}
		catch (Exception e)
		{
			throw new JMSException("Unable to deserialize body: " + e.getMessage());
		}
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

	@Override
	public void clearBody() throws JMSException
	{
		super.clearBody();
		text = null;
		readOnly = false;
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
		if (readOnly)
			throw new MessageNotWriteableException("Message is read-only");
		
		this.text = text;
	}
	
}