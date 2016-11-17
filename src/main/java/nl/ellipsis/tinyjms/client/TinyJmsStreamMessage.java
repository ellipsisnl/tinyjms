package nl.ellipsis.tinyjms.client;

import java.io.*;

import javax.jms.*;

import nl.ellipsis.tinyjms.protocol.MarshallingSupport;
import nl.ellipsis.tinyjms.protocol.MarshallingSupport.ObjectType;

/**
 * TinyJms implementation of {@link StreamMessage}.
 */
public class TinyJmsStreamMessage extends TinyJmsMessage implements StreamMessage
{
	private boolean readOnly = false;
	private DataInputStream dis;
	private ByteArrayInputStream bis;
	private DataOutputStream dos;
	private ByteArrayOutputStream bos;

	private byte[] data;
	private byte[] byteField;
	private int byteFieldPos = 0;

	TinyJmsStreamMessage() throws JMSException
	{
		clearBody();
	}

	@Override
	byte[] getBody() throws JMSException
	{
		closeStreams();
		if (data == null)
		{
			return null;
		}
		
		byte[] copy = new byte[data.length];
		System.arraycopy(data, 0, copy, 0, data.length);
		return copy;
	}

	@Override
	void setBody(byte[] data) throws JMSException
	{
		closeStreams();
		if (data == null)
		{
			this.data = null;
		}
		else
		{
			this.data = new byte[data.length];
			System.arraycopy(data, 0, this.data, 0, data.length);
		}
		reset();
	}

	@Override
	public void clearBody() throws JMSException
	{
		closeStreams();
		data = null;
		byteField = null;
		byteFieldPos = 0;
		readOnly = false;
		bos = new ByteArrayOutputStream();
		dos = new DataOutputStream(bos);
	}

	/**
	 * Reads a <code>boolean</code> from the stream message.
	 * 
	 * @return the <code>boolean</code> value read
	 * @throws JMSException
	 *           if the JMS provider fails to read the message due to some
	 *           internal error.
	 * @throws MessageEOFException
	 *           if unexpected end of message stream has been reached.
	 * @throws MessageFormatException
	 *           if this type conversion is invalid.
	 * @throws MessageNotReadableException
	 *           if the message is in write-only mode.
	 */
	@Override
	public boolean readBoolean() throws JMSException
	{
		checkReadable();
		checkByteReadInProgress();
		Object value = deserializeObject();
		if (value instanceof Boolean)
		{
			return (Boolean) value;
		}

		if (value == null || value instanceof String)
		{
			return Boolean.valueOf((String) value);
		}

		throw new MessageFormatException("Unsupported conversion to boolean from type: " + value.getClass().getName());
	}

	@Override
	public byte readByte() throws JMSException
	{
		checkReadable();
		checkByteReadInProgress();
		Object value = deserializeObject();
		if (value instanceof Byte)
		{
			return (Byte) value;
		}

		if (value == null || value instanceof String)
		{
			return Byte.valueOf((String) value);
		}

		throw new MessageFormatException("Unsupported conversion to byte from type: " + value.getClass().getName());
	}

	/**
	 * Reads a byte array field from the stream message into the specified
	 * <code>byte[]</code> object (the read buffer).
	 * 
	 * <p>
	 * To read the field value, <code>readBytes</code> should be successively
	 * called until it returns a value less than the length of the read buffer.
	 * The value of the bytes in the buffer following the last byte read is
	 * undefined.
	 * </p>
	 * 
	 * <p>
	 * If <code>readBytes</code> returns a value equal to the length of the
	 * buffer, a subsequent <code>readBytes</code> call must be made. If there are
	 * no more bytes to be read, this call returns -1.
	 * </p>
	 * 
	 * <p>
	 * If the byte array field value is <code>null</code>, <code>readBytes</code>
	 * returns -1.
	 * </p>
	 * 
	 * <p>
	 * If the byte array field value is empty, <code>readBytes</code> returns 0.
	 * </p>
	 * 
	 * <p>
	 * Once the first <code>readBytes</code> call on a <code>byte[]</code> field
	 * value has been made, the full value of the field must be read before it is
	 * valid to read the next field. An attempt to read the next field before that
	 * has been done will throw a <code>MessageFormatException</code>.
	 * </p>
	 * 
	 * <p>
	 * To read the byte field value into a new <code>byte[]</code> object, use the
	 * readObject method.
	 * </p>
	 * 
	 * @param value
	 *          the buffer into which the data is read
	 * @return the total number of bytes read into the buffer, or -1 if there is
	 *         no more data because the end of the byte field has been reached
	 * @throws JMSException
	 *           if the JMS provider fails to read the message due to some
	 *           internal error.
	 * @throws MessageEOFException
	 *           if unexpected end of message stream has been reached.
	 * @throws MessageFormatException
	 *           if this type conversion is invalid.
	 * @throws MessageNotReadableException
	 *           if the message is in write-only mode.
	 * @see #readObject()
	 */
	@Override
	public int readBytes(byte[] value) throws JMSException
	{
		checkReadable();
		if (byteField == null)
		{
			Object nextValue = deserializeObject();
			if (nextValue != null && !(nextValue instanceof byte[]))
			{
				throw new MessageFormatException("Unsupported conversion to byte[] from type: " + value.getClass().getName());
			}
			byteField = (byte[]) nextValue;
			byteFieldPos = 0;
		}

		if (byteField == null)
		{
			return -1;
		}

		if (byteField.length == 0)
		{
			return 0;
		}

		if (byteFieldPos >= byteField.length)
		{
			byteField = null;
			byteFieldPos = 0;
			return -1;
		}

		int bytesToRead = Math.min(value.length, byteField.length - byteFieldPos);
		System.arraycopy(byteField, byteFieldPos, value, 0, bytesToRead);
		byteFieldPos += bytesToRead;

		return bytesToRead;
	}

	/**
	 * Reads a Unicode character value from the stream message.
	 * 
	 * @return a Unicode character from the stream message
	 * @throws JMSException
	 *           if the JMS provider fails to read the message due to some
	 *           internal error.
	 * @throws MessageEOFException
	 *           if unexpected end of message stream has been reached.
	 * @throws MessageFormatException
	 *           if this type conversion is invalid
	 * @throws MessageNotReadableException
	 *           if the message is in write-only mode.
	 */
	@Override
	public char readChar() throws JMSException
	{
		checkReadable();
		checkByteReadInProgress();
		Object value = deserializeObject();
		if (value == null || value instanceof Character)
		{
			return (Character) value;
		}

		throw new MessageFormatException("Unsupported conversion to char from type: " + value.getClass().getName());
	}

	/**
	 * Reads a <code>double</code> from the stream message.
	 * 
	 * @return a <code>double</code> value from the stream message
	 * @throws JMSException
	 *           if the JMS provider fails to read the message due to some
	 *           internal error.
	 * @throws MessageEOFException
	 *           if unexpected end of message stream has been reached.
	 * @throws MessageFormatException
	 *           if this type conversion is invalid.
	 * @throws MessageNotReadableException
	 *           if the message is in write-only mode.
	 */
	@Override
	public double readDouble() throws JMSException
	{
		checkReadable();
		checkByteReadInProgress();
		Object value = deserializeObject();
		if (value instanceof Double)
		{
			return (Double) value;
		}

		if (value instanceof Float)
		{
			return (Float) value;
		}

		if (value == null || value instanceof String)
		{
			return Double.valueOf((String) value);
		}

		throw new MessageFormatException("Unsupported conversion to double from type: " + value.getClass().getName());
	}

	/**
	 * Reads a <code>float</code> from the stream message.
	 * 
	 * @return a <code>float</code> value from the stream message
	 * @throws JMSException
	 *           if the JMS provider fails to read the message due to some
	 *           internal error.
	 * @throws MessageEOFException
	 *           if unexpected end of message stream has been reached.
	 * @throws MessageFormatException
	 *           if this type conversion is invalid.
	 * @throws MessageNotReadableException
	 *           if the message is in write-only mode.
	 */
	@Override
	public float readFloat() throws JMSException
	{
		checkReadable();
		checkByteReadInProgress();
		Object value = deserializeObject();

		if (value instanceof Float)
		{
			return (Float) value;
		}

		if (value == null || value instanceof String)
		{
			return Float.valueOf((String) value);
		}

		throw new MessageFormatException("Unsupported conversion to float from type: " + value.getClass().getName());
	}

	/**
	 * Reads a <code>int</code> from the stream message.
	 * 
	 * @return a <code>int</code> value from the stream message
	 * @throws JMSException
	 *           if the JMS provider fails to read the message due to some
	 *           internal error.
	 * @throws MessageEOFException
	 *           if unexpected end of message stream has been reached.
	 * @throws MessageFormatException
	 *           if this type conversion is invalid.
	 * @throws MessageNotReadableException
	 *           if the message is in write-only mode.
	 */
	@Override
	public int readInt() throws JMSException
	{
		checkReadable();
		checkByteReadInProgress();
		Object value = deserializeObject();

		if (value instanceof Integer)
		{
			return (Integer) value;
		}

		if (value instanceof Short)
		{
			return (Short) value;
		}

		if (value instanceof Byte)
		{
			return (Byte) value;
		}

		if (value == null || value instanceof String)
		{
			return Integer.valueOf((String) value);
		}

		throw new MessageFormatException("Unsupported conversion to int from type: " + value.getClass().getName());
	}

	/**
	 * Reads a <code>long</code> from the stream message.
	 * 
	 * @return a <code>long</code> value from the stream message
	 * @throws JMSException
	 *           if the JMS provider fails to read the message due to some
	 *           internal error.
	 * @throws MessageEOFException
	 *           if unexpected end of message stream has been reached.
	 * @throws MessageFormatException
	 *           if this type conversion is invalid.
	 * @throws MessageNotReadableException
	 *           if the message is in write-only mode.
	 */
	@Override
	public long readLong() throws JMSException
	{
		checkReadable();
		checkByteReadInProgress();
		Object value = deserializeObject();

		if (value instanceof Long)
		{
			return (Long) value;
		}

		if (value instanceof Integer)
		{
			return (Integer) value;
		}

		if (value instanceof Short)
		{
			return (Short) value;
		}

		if (value instanceof Byte)
		{
			return (Byte) value;
		}

		if (value == null || value instanceof String)
		{
			return Long.valueOf((String) value);
		}

		throw new MessageFormatException("Unsupported conversion to long from type: " + value.getClass().getName());
	}

	/**
	 * Reads an object from the stream message.
	 * 
	 * <p>
	 * This method can be used to return, in objectified format, an object in the
	 * Java programming language ("Java object") that has been written to the
	 * stream with the equivalent <code>writeObject</code> method call, or its
	 * equivalent primitive writetype method.
	 * </p>
	 * 
	 * <p>
	 * Note that byte values are returned as <code>byte[]</code>, not
	 * <code>Byte[]</code>.
	 * </p>
	 * 
	 * <p>
	 * An attempt to call <code>readObject</code> to read a byte field value into
	 * a new <code>byte[]</code> object before the full value of the byte field
	 * has been read will throw a <code>MessageFormatException</code>.
	 * </p>
	 * 
	 * @return a Java object from the stream message, in objectified format (for
	 *         example, if the object was written as an <code>int</code>, an
	 *         <code>Integer</code> is returned)
	 * @throws JMSException
	 *           if the JMS provider fails to read the message due to some
	 *           internal error.
	 * @throws MessageEOFException
	 *           if unexpected end of message stream has been reached.
	 * @throws MessageFormatException
	 *           if this type conversion is invalid.
	 * @throws MessageNotReadableException
	 *           if the message is in write-only mode.
	 * @see #readBytes(byte[] value)
	 */
	@Override
	public Object readObject() throws JMSException
	{
		checkReadable();
		checkByteReadInProgress();
		return deserializeObject();
	}

	/**
	 * Reads a <code>short</code> from the stream message.
	 * 
	 * @return a <code>short</code> value from the stream message
	 * @throws JMSException
	 *           if the JMS provider fails to read the message due to some
	 *           internal error.
	 * @throws MessageEOFException
	 *           if unexpected end of message stream has been reached.
	 * @throws MessageFormatException
	 *           if this type conversion is invalid.
	 * @throws MessageNotReadableException
	 *           if the message is in write-only mode.
	 */
	@Override
	public short readShort() throws JMSException
	{
		checkReadable();
		checkByteReadInProgress();
		Object value = deserializeObject();

		if (value instanceof Short)
		{
			return (Short) value;
		}

		if (value instanceof Byte)
		{
			return (Byte) value;
		}

		if (value == null || value instanceof String)
		{
			return Short.valueOf((String) value);
		}

		throw new MessageFormatException("Unsupported conversion to short from type: " + value.getClass().getName());
	}

	/**
	 * Reads a <code>String</code> from the stream message.
	 * 
	 * @return a Unicode string from the stream message
	 * @throws JMSException
	 *           if the JMS provider fails to read the message due to some
	 *           internal error.
	 * @throws MessageEOFException
	 *           if unexpected end of message stream has been reached.
	 * @throws MessageFormatException
	 *           if this type conversion is invalid.
	 * @throws MessageNotReadableException
	 *           if the message is in write-only mode.
	 */
	@Override
	public String readString() throws JMSException
	{
		checkReadable();
		checkByteReadInProgress();
		
		Object value = deserializeObject();

		if (value == null)
		{
			return null;
		}
		
		if (value instanceof byte[])
		{
			throw new MessageFormatException("Unsupported conversion to String from type: " + value.getClass().getName());
		}
		
		return value.toString();
	}

	/**
	 * Puts the message body in read-only mode and repositions the stream to the
	 * beginning.
	 * 
	 * @throws JMSException
	 *           if the JMS provider fails to reset the message due to some
	 *           internal error.
	 * @throws MessageFormatException
	 *           if the message has an invalid format.
	 */
	@Override
	public void reset() throws JMSException
	{
		closeStreams();
		readOnly = true;
		if (data == null)
		{
			data = new byte[0];
		}
		byteField = null;
		byteFieldPos = 0;
		bis = new ByteArrayInputStream(data);
		dis = new DataInputStream(bis);
	}

	/**
	 * Writes a <code>boolean</code> to the stream message. The value
	 * <code>true</code> is written as the value (byte)1; the value
	 * <code>false</code> is written as the value (byte)0.
	 * 
	 * @param value
	 *          the <code>boolean</code> value to be written
	 * @throws JMSException
	 *           if the JMS provider fails to write the message due to some
	 *           internal error.
	 * @throws MessageNotWriteableException
	 *           if the message is in read-only mode.
	 */
	@Override
	public void writeBoolean(boolean value) throws JMSException
	{
		checkWritable();
		serializeObject(value);
	}

	/**
	 * Writes a <code>byte</code> to the stream message.
	 * 
	 * @param value
	 *          the <code>byte</code> value to be written
	 * @throws JMSException
	 *           if the JMS provider fails to write the message due to some
	 *           internal error.
	 * @throws MessageNotWriteableException
	 *           if the message is in read-only mode.
	 */
	@Override
	public void writeByte(byte value) throws JMSException
	{
		checkWritable();
		serializeObject(value);
	}

	/**
	 * Writes a byte array field to the stream message.
	 * 
	 * <p>
	 * The byte array <code>value</code> is written to the message as a byte array
	 * field. Consecutively written byte array fields are treated as two distinct
	 * fields when the fields are read.
	 * </p>
	 * 
	 * @param value
	 *          the byte array value to be written
	 * @throws JMSException
	 *           if the JMS provider fails to write the message due to some
	 *           internal error.
	 * @throws MessageNotWriteableException
	 *           if the message is in read-only mode.
	 */
	@Override
	public void writeBytes(byte[] value) throws JMSException
	{
		checkWritable();
		serializeObject(value);
	}

	/**
	 * Writes a portion of a byte array as a byte array field to the stream
	 * message.
	 * 
	 * <p>
	 * The a portion of the byte array <code>value</code> is written to the
	 * message as a byte array field. Consecutively written byte array fields are
	 * treated as two distinct fields when the fields are read.
	 * </p>
	 * 
	 * @param value
	 *          the byte array value to be written
	 * @param offset
	 *          the initial offset within the byte array
	 * @param length
	 *          the number of bytes to use
	 * @throws JMSException
	 *           if the JMS provider fails to write the message due to some
	 *           internal error.
	 * @throws MessageNotWriteableException
	 *           if the message is in read-only mode.
	 */
	@Override
	public void writeBytes(byte[] value, int offset, int length) throws JMSException
	{
		checkWritable();
		try
		{
			dos.writeByte(ObjectType.BYTE_ARRAY.ordinal());
			dos.writeInt(length);
			dos.write(value, offset, length);
		}
		catch (EOFException e)
		{
			throw new MessageEOFException("End of message: " + e.getMessage());
		}
		catch (IOException e)
		{
			throw new JMSException("Error reading data: " + e.getMessage());
		}
	}

	/**
	 * Writes a <code>char</code> to the stream message.
	 * 
	 * @param value
	 *          the char value to be written
	 * @throws JMSException
	 *           if the JMS provider fails to write the message due to some
	 *           internal error.
	 * @throws MessageNotWriteableException
	 *           if the message is in read-only mode.
	 */
	@Override
	public void writeChar(char value) throws JMSException
	{
		checkWritable();
		serializeObject(value);
	}

	/**
	 * Writes a <code>double</code> to the stream message.
	 * 
	 * @param value
	 *          the <code>double</code> value to be written
	 * @throws JMSException
	 *           if the JMS provider fails to write the message due to some
	 *           internal error.
	 * @throws MessageNotWriteableException
	 *           if the message is in read-only mode.
	 */
	@Override
	public void writeDouble(double value) throws JMSException
	{
		checkWritable();
		serializeObject(value);
	}

	/**
	 * Writes a <code>float</code> to the stream message.
	 * 
	 * @param value
	 *          the <code>float</code> value to be written
	 * @throws JMSException
	 *           if the JMS provider fails to write the message due to some
	 *           internal error.
	 * @throws MessageNotWriteableException
	 *           if the message is in read-only mode.
	 */
	@Override
	public void writeFloat(float value) throws JMSException
	{
		checkWritable();
		serializeObject(value);
	}

	/**
	 * Writes an <code>int</code> to the stream message.
	 * 
	 * @param value
	 *          the <code>int</code> value to be written
	 * @throwsJMSException if the JMS provider fails to write the message due to
	 *                     some internal error.
	 * @throws MessageNotWriteableException
	 *           if the message is in read-only mode.
	 */
	@Override
	public void writeInt(int value) throws JMSException
	{
		checkWritable();
		serializeObject(value);
	}

	/**
	 * Writes an <code>long</code> to the stream message.
	 * 
	 * @param value
	 *          the <code>long</code> value to be written
	 * @throwsJMSException if the JMS provider fails to write the message due to
	 *                     some internal error.
	 * @throws MessageNotWriteableException
	 *           if the message is in read-only mode.
	 */
	@Override
	public void writeLong(long value) throws JMSException
	{
		checkWritable();
		serializeObject(value);
	}

	/**
	 * Writes an object to the stream message.
	 * 
	 * <p>
	 * This method works only for the objectified primitive object types (
	 * <code>Integer</code>, <code>Double</code>, <code>Long</code> ...),
	 * <code>String</code> objects, and byte arrays.
	 * </p>
	 * 
	 * @param value
	 *          the Java object to be written
	 * @throws JMSException
	 *           if the JMS provider fails to write the message due to some
	 *           internal error.
	 * @throws MessageFormatException
	 *           if the object is invalid.
	 * @throws MessageNotWriteableException
	 *           if the message is in read-only mode.
	 */
	@Override
	public void writeObject(Object value) throws JMSException
	{
		checkWritable();
		serializeObject(value);
	}

	/**
	 * Writes a <code>short</code> to the stream message.
	 * 
	 * @param value
	 *          the <code>short</code> value to be written
	 * @throws JMSException
	 *           if the JMS provider fails to write the message due to some
	 *           internal error.
	 * @throws MessageNotWriteableException
	 *           if the message is in read-only mode.
	 */
	@Override
	public void writeShort(short value) throws JMSException
	{
		checkWritable();
		serializeObject(value);
	}

	/**
	 * Writes a <code>String</code> to the stream message.
	 * 
	 * @param value
	 *          the <code>String</code> value to be written
	 * @throws JMSException
	 *           if the JMS provider fails to write the message due to some
	 *           internal error.
	 * @throws MessageNotWriteableException
	 *           if the message is in read-only mode.
	 */
	@Override
	public void writeString(String value) throws JMSException
	{
		checkWritable();
		serializeObject(value);
	}

	private void checkReadable() throws MessageNotReadableException
	{
		if (!readOnly)
		{
			throw new MessageNotReadableException("Message is in write-only mode");
		}
	}

	private void checkByteReadInProgress() throws MessageFormatException
	{
		if (byteField != null)
		{
			throw new MessageFormatException("Previous read of byte array is not complete");
		}
	}

	private void checkWritable() throws MessageNotWriteableException
	{
		if (readOnly)
		{
			throw new MessageNotWriteableException("Message is in read-only mode");
		}
	}

	private void serializeObject(Object value) throws JMSException
	{
		MarshallingSupport.marshalObject(dos, value);
	}

	private Object deserializeObject() throws JMSException
	{
		return MarshallingSupport.unmarshalObject(dis);
	}

	private void closeStreams()
	{
		closeStream(dis);
		closeStream(bis);
		closeStream(dos);
		closeStream(bos);

		// get byte array from stream
		if (bos != null)
		{
			data = bos.toByteArray();
		}

		dis = null;
		bis = null;
		dos = null;
		bos = null;
	}

	private void closeStream(Closeable c)
	{
		if (c != null)
		{
			try
			{
				c.close();
			}
			catch (Exception ignored)
			{}
		}
	}

}
