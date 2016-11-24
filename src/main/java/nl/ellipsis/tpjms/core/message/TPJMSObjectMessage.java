package nl.ellipsis.tpjms.core.message;

import java.io.*;

import javax.jms.*;

/**
 * TinTPJMSyJms implementation of {@link ObjectMessage}.
 */
public class TPJMSObjectMessage extends TPJMSMessage implements ObjectMessage {
	private byte[] body;
	private boolean readOnly = false;

	public TPJMSObjectMessage(Session session) {
		super(session);
	}

	/**
	 * Sets the read-only flag for this message.
	 * 
	 * @param readOnly
	 *            <code>true</code> if read-only
	 */
	void setReadOnly(boolean readOnly) {
		this.readOnly = readOnly;
	}

	@Override
	public void clearBody() throws JMSException {
		super.clearBody();
		body = null;
		readOnly = false;
	}

	@Override
	byte[] getBody() throws JMSException {
		if (body == null) {
			return null;
		}

		byte[] copy = new byte[body.length];
		System.arraycopy(body, 0, copy, 0, body.length);
		return copy;
	}

	@Override
	void setBody(byte[] bodyData) throws JMSException {
		if (bodyData == null) {
			this.body = null;
		} else {
			this.body = new byte[bodyData.length];
			System.arraycopy(bodyData, 0, this.body, 0, bodyData.length);
		}
	}

	/**
	 * Gets the serializable object containing this message's data. The default
	 * value is <code>null</code>.
	 * 
	 * @return the serializable object containing this message's data
	 * @throws JMSException
	 *             if the JMS provider fails to get the object due to some
	 *             internal error.
	 * @throws MessageFormatException
	 *             if object deserialization fails.
	 */
	@Override
	public Serializable getObject() throws JMSException {
		if(body == null) {
			return null;
		}
		return fromByteArray(body);
	}
	
	/**
	 * Sets the serializable object containing this message's data. It is
	 * important to note that an <code>ObjectMessage</code> contains a snapshot
	 * of the object at the time <code>setObject()</code> is called; subsequent
	 * modifications of the object will have no effect on the
	 * <code>ObjectMessage</code> body.
	 * 
	 * @param object
	 *            the message's data
	 * @throws JMSException
	 *             if the JMS provider fails to set the object due to some
	 *             internal error.
	 * @throws MessageFormatException
	 *             if object serialization fails.
	 * @throws MessageNotWriteableException
	 *             if the message is in read-only mode.
	 */
	@Override
	public void setObject(Serializable object) throws JMSException {
		if(readOnly) {
			throw new MessageNotWriteableException("Message is readonly");
		}
		this.body = toByteArray(object);
	}
	
	/**
	 * Object to byte array
	 * 
	 * @throws MessageFormatException 
	 */
	private static byte[] toByteArray(Serializable object) throws MessageFormatException {
		if(object == null) {
			return null;
		}
		byte[] bytes = null;
		ByteArrayOutputStream bos = null;
		ObjectOutputStream oos = null;
	    try {
            bos = new ByteArrayOutputStream(1024);
            oos = new ObjectOutputStream(bos);
            oos.writeObject(object);
            bytes = bos.toByteArray();
	    } catch (IOException e) {
	        throw new MessageFormatException("Cannot serialize object : "+e.toString());
		} finally {
			if (oos != null) {
				try {
					oos.close();
				} catch (Exception ignored) {}
			}
			if (bos != null) {
				try {
					oos.close();
				} catch (Exception ignored) {}
			}
		}
	    return bytes;
	}
	
	/**
	 * Byte array to object
	 * @throws MessageFormatException 
	 */
	private static Serializable fromByteArray(byte[] bytes) throws MessageFormatException {
		if(bytes == null) {
			return null;
		}

		ByteArrayInputStream bis = null;
		ObjectInputStream ois = null;

		try {
			bis = new ByteArrayInputStream(bytes);
			ois = new ObjectInputStream(bis);
			return (Serializable) ois.readObject();
		} catch (Exception e) {
			throw new MessageFormatException("Unable to deserialize object: " + e.getMessage());
		} finally {
			if (ois != null) {
				try {
					ois.close();
				} catch (Exception ignored) {}
			}
			if (bis != null) {
				try {
					ois.close();
				} catch (Exception ignored) {}
			}
		}
	}

}
