package org.randomcoder.tinyjms.client;

import java.io.*;

import javax.jms.*;

/**
 * TinyJms implementation of {@link ObjectMessage}.
 */
public class TinyJmsObjectMessage extends TinyJmsMessage implements ObjectMessage
{
	private byte[] data;
	private boolean readOnly = false;

	TinyJmsObjectMessage()
	{

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
	public void clearBody() throws JMSException
	{
		super.clearBody();
		data = null;
		readOnly = false;
	}

	@Override
	byte[] getBody() throws JMSException
	{
		if (data == null)
		{
			return null;
		}
		
		byte[] copy = new byte[data.length];
		System.arraycopy(data, 0, copy, 0, data.length);
		return copy;
	}
	
	@Override
	void setBody(byte[] bodyData) throws JMSException
	{
		if (bodyData == null)
		{
			this.data = null;
		}
		else
		{
			this.data = new byte[bodyData.length];
			System.arraycopy(bodyData, 0, this.data, 0, bodyData.length);
		}
	}
	
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
		if (data == null)
		{
			return null;
		}

		ByteArrayInputStream bis = null;
		ObjectInputStream ois = null;

		try
		{
			bis = new ByteArrayInputStream(data);
			ois = new ObjectInputStream(bis);
			return (Serializable) ois.readObject();
		}
		catch (Exception e)
		{
			throw new MessageFormatException("Unable to deserialize object: " + e.getMessage());
		}
		finally
		{
			if (ois != null)
				try
				{
					ois.close();
				}
				catch (Exception ignored)
				{}
			if (bis != null)
				try
				{
					ois.close();
				}
				catch (Exception ignored)
				{}
		}
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
		if (readOnly)
		{
			throw new MessageNotWriteableException("Message is read-only");
		}
		
		if (object == null)
		{
			data = null;
			return;
		}

		ByteArrayOutputStream bos = null;
		ObjectOutputStream oos = null;
		try
		{
			bos = new ByteArrayOutputStream();
			oos = new ObjectOutputStream(bos);
			oos.writeObject(object);
		}
		catch (Exception e)
		{
			throw new MessageFormatException("Unable to serialize object: " + e.getMessage());
		}
		finally
		{
			if (oos != null)
				try
				{
					oos.close();
				}
				catch (Exception ignored)
				{}
			if (bos != null)
				try
				{
					oos.close();
				}
				catch (Exception ignored)
				{}
		}

		this.data = bos.toByteArray();
	}

}
