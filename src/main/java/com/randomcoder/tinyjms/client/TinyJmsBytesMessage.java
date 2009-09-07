package com.randomcoder.tinyjms.client;

import java.io.*;

import javax.jms.*;

/**
 * TinyJms implementation of {@link BytesMessage}.
 */
public class TinyJmsBytesMessage extends TinyJmsMessage implements BytesMessage
{
	private boolean readOnly = false;
	private DataInputStream dis;
	private ByteArrayInputStream bis;
	private DataOutputStream dos;
	private ByteArrayOutputStream bos;
	
	private byte[] data;
	
	TinyJmsBytesMessage() throws JMSException
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
		readOnly = false;
		bos = new ByteArrayOutputStream();
		dos = new DataOutputStream(bos);
	}
	
	/**
	 * Gets the number of bytes of the message body when the message is in
	 * read-only mode. The value returned can be used to allocate a byte array.
	 * The value returned is the entire length of the message body, regardless of
	 * where the pointer for reading the message is currently located.
	 * 
	 * @return number of bytes in the message
	 * @throws JMSException
	 *           if the JMS provider fails to read the message due to some
	 *           internal error.
	 * @throws MessageNotReadableException
	 *           if the message is in write-only mode.
	 * @since JMS 1.1
	 */
	@Override
	public long getBodyLength() throws JMSException
	{
		checkReadable();
		return (data == null) ? 0 : data.length;
	}

	/**
	 * Reads a <code>boolean</code> from the bytes message stream.
	 * 
	 * @return the <code>boolean</code> value read
	 * @throws JMSException
	 *           if the JMS provider fails to read the message due to some
	 *           internal error.
	 * @throws MessageEOFException
	 *           if unexpected end of bytes stream has been reached.
	 * @throws MessageNotReadableException
	 *           if the message is in write-only mode.
	 */
	@Override
	public boolean readBoolean() throws JMSException
	{
		checkReadable();
		try
		{
			return dis.readBoolean();
		}
		catch (EOFException e)
		{
			throw new MessageEOFException("Unable to read boolean: EOF");
		}
		catch (IOException e)
		{
			throw new JMSException("Unable to read boolean: " + e.getMessage());
		}
	}

	/**
	 * Reads a signed 8-bit value from the bytes message stream.
	 * 
	 * @return the next byte from the bytes message stream as a signed 8-bit
	 *         <code>byte</code>
	 * @throws JMSException
	 *           if the JMS provider fails to read the message due to some
	 *           internal error.
	 * @throws MessageEOFException
	 *           if unexpected end of bytes stream has been reached.
	 * @throws MessageNotReadableException
	 *           if the message is in write-only mode.
	 */
	@Override
	public byte readByte() throws JMSException
	{
		checkReadable();
		try
		{
			return dis.readByte();
		}
		catch (EOFException e)
		{
			throw new MessageEOFException("Unable to read byte: EOF");
		}
		catch (IOException e)
		{
			throw new JMSException("Unable to read byte: " + e.getMessage());
		}
	}

	/**
	 * Reads a byte array from the bytes message stream.
	 * 
	 * <p>
	 * If the length of array <code>value</code> is less than the number of bytes
	 * remaining to be read from the stream, the array should be filled. A
	 * subsequent call reads the next increment, and so on.
	 * </p>
	 * 
	 * <p>
	 * If the number of bytes remaining in the stream is less than the length of
	 * array value, the bytes should be read into the array. The return value of
	 * the total number of bytes read will be less than the length of the array,
	 * indicating that there are no more bytes left to be read from the stream.
	 * The next read of the stream returns -1.
	 * </p>
	 * 
	 * @param value
	 *          the buffer into which the data is read
	 * @return the total number of bytes read into the buffer, or -1 if there is
	 *         no more data because the end of the stream has been reached
	 * @throws JMSException
	 *           if the JMS provider fails to read the message due to some
	 *           internal error.
	 * @throws MessageNotReadableException
	 *           if the message is in write-only mode.
	 */
	@Override
	public int readBytes(byte[] value) throws JMSException
	{
		checkReadable();
		try
		{
			return dis.read(value);
		}
		catch (IOException e)
		{
			throw new JMSException("Unable to read byte array: " + e.getMessage());
		}
	}

	/**
	 * Reads a portion of the bytes message stream.
	 * 
	 * <p>
	 * If the length of array <code>value</code> is less than the number of bytes
	 * remaining to be read from the stream, the array should be filled. A
	 * subsequent call reads the next increment, and so on.
	 * </p>
	 * 
	 * <p>
	 * If the number of bytes remaining in the stream is less than the length of
	 * array <code>value</code>, the bytes should be read into the array. The
	 * return value of the total number of bytes read will be less than the length
	 * of the array, indicating that there are no more bytes left to be read from
	 * the stream. The next read of the stream returns -1.
	 * </p>
	 * 
	 * <p>
	 * If <code>length</code> is negative, or <code>length</code> is greater than
	 * the length of the array value, then an
	 * <code>IndexOutOfBoundsException</code> is thrown. No bytes will be read
	 * from the stream for this exception case.
	 * </p>
	 * 
	 * @param value
	 *          the buffer into which the data is read
	 * @param length
	 *          the number of bytes to read; must be less than or equal to
	 *          <code>value.length</code>
	 * @return the total number of bytes read into the buffer, or -1 if there is
	 *         no more data because the end of the stream has been reached
	 * @throws JMSException
	 *           if the JMS provider fails to read the message due to some
	 *           internal error.
	 * @throws MessageNotReadableException
	 *           if the message is in write-only mode.
	 */
	@Override
	public int readBytes(byte[] value, int length) throws JMSException
	{
		checkReadable();
		try
		{
			return dis.read(value, 0, length);
		}
		catch (IOException e)
		{
			throw new JMSException("Unable to read byte array: " + e.getMessage());
		}
	}

	/**
	 * Reads a Unicode character value from the bytes message stream.
	 * 
	 * @return the next two bytes from the bytes message stream as a Unicode
	 *         character
	 * @throws JMSException
	 *           if the JMS provider fails to read the message due to some
	 *           internal error.
	 * @throws MessageEOFException
	 *           if unexpected end of bytes stream has been reached.
	 * @throws MessageNotReadableException
	 *           if the message is in write-only mode.
	 */
	@Override
	public char readChar() throws JMSException
	{
		checkReadable();
		try
		{
			return dis.readChar();
		}
		catch (EOFException e)
		{
			throw new MessageEOFException("Unable to read char: EOF");
		}
		catch (IOException e)
		{
			throw new JMSException("Unable to read char: " + e.getMessage());
		}
	}

	/**
	 * Reads a <code>double</code> from the bytes message stream.
	 * 
	 * @return the next eight bytes from the bytes message stream, interpreted as
	 *         a double
	 * @throws JMSException
	 *           if the JMS provider fails to read the message due to some
	 *           internal error.
	 * @throws MessageEOFException
	 *           if unexpected end of bytes stream has been reached.
	 * @throws MessageNotReadableException
	 *           if the message is in write-only mode.
	 */
	@Override
	public double readDouble() throws JMSException
	{
		checkReadable();
		try
		{
			return dis.readDouble();
		}
		catch (EOFException e)
		{
			throw new MessageEOFException("Unable to read double: EOF");
		}
		catch (IOException e)
		{
			throw new JMSException("Unable to read double: " + e.getMessage());
		}
	}

	/**
	 * Reads a <code>float</code> from the bytes message stream.
	 * 
	 * @return the next four bytes from the bytes message stream, interpreted as a
	 *         float
	 * @throws JMSException
	 *           if the JMS provider fails to read the message due to some
	 *           internal error.
	 * @throws MessageEOFException
	 *           if unexpected end of bytes stream has been reached.
	 * @throws MessageNotReadableException
	 *           if the message is in write-only mode.
	 */
	@Override
	public float readFloat() throws JMSException
	{
		checkReadable();
		try
		{
			return dis.readFloat();
		}
		catch (EOFException e)
		{
			throw new MessageEOFException("Unable to read float: EOF");
		}
		catch (IOException e)
		{
			throw new JMSException("Unable to read float: " + e.getMessage());
		}
	}

	/**
	 * Reads a signed 32-bit integer from the bytes message stream.
	 * 
	 * @return the next four bytes from the bytes message stream, interpreted as
	 *         an int
	 * @throws JMSException
	 *           if the JMS provider fails to read the message due to some
	 *           internal error.
	 * @throws MessageEOFException
	 *           if unexpected end of bytes stream has been reached.
	 * @throws MessageNotReadableException
	 *           if the message is in write-only mode.
	 */
	@Override
	public int readInt() throws JMSException
	{
		checkReadable();
		try
		{
			return dis.readInt();
		}
		catch (EOFException e)
		{
			throw new MessageEOFException("Unable to read int: EOF");
		}
		catch (IOException e)
		{
			throw new JMSException("Unable to read int: " + e.getMessage());
		}
	}

	/**
	 * Reads a signed 64-bit integer from the bytes message stream.
	 * 
	 * @return the next eight bytes from the bytes message stream, interpreted as
	 *         a long
	 * @throws JMSException
	 *           if the JMS provider fails to read the message due to some
	 *           internal error.
	 * @throws MessageEOFException
	 *           if unexpected end of bytes stream has been reached.
	 * @throws MessageNotReadableException
	 *           if the message is in write-only mode.
	 */
	@Override
	public long readLong() throws JMSException
	{
		checkReadable();
		try
		{
			return dis.readLong();
		}
		catch (EOFException e)
		{
			throw new MessageEOFException("Unable to read long: EOF");
		}
		catch (IOException e)
		{
			throw new JMSException("Unable to read long: " + e.getMessage());
		}
	}

	/**
	 * Reads a signed 16-bit number from the bytes message stream.
	 * 
	 * @return the next two bytes from the bytes message stream, interpreted as a
	 *         signed 16-bit number
	 * @throws JMSException
	 *           if the JMS provider fails to read the message due to some
	 *           internal error.
	 * @throws MessageEOFException
	 *           if unexpected end of bytes stream has been reached.
	 * @thorws MessageNotReadableException if the message is in write-only mode.
	 */
	@Override
	public short readShort() throws JMSException
	{
		checkReadable();
		try
		{
			return dis.readShort();
		}
		catch (EOFException e)
		{
			throw new MessageEOFException("Unable to read short: EOF");
		}
		catch (IOException e)
		{
			throw new JMSException("Unable to read short: " + e.getMessage());
		}
	}

	/**
	 * Reads a string that has been encoded using a modified UTF-8 format from the
	 * bytes message stream.
	 * 
	 * <p>
	 * For more information on the UTF-8 format, see
	 * "File System Safe UCS Transformation Format (FSS_UTF)", X/Open Preliminary
	 * Specification, X/Open Company Ltd., Document Number: P316. This information
	 * also appears in ISO/IEC 10646, Annex P.
	 * </p>
	 * 
	 * @return a Unicode string from the bytes message stream
	 * @throws JMSException
	 *           if the JMS provider fails to read the message due to some
	 *           internal error.
	 * @throws MessageEOFException
	 *           if unexpected end of bytes stream has been reached.
	 * @throws MessageNotReadableException
	 *           if the message is in write-only mode.
	 */
	@Override
	public String readUTF() throws JMSException
	{
		checkReadable();
		try
		{
			return dis.readUTF();
		}
		catch (EOFException e)
		{
			throw new MessageEOFException("Unable to read UTF string: EOF");
		}
		catch (IOException e)
		{
			throw new JMSException("Unable to read UTF string: " + e.getMessage());
		}
	}

	/**
	 * Reads an unsigned 8-bit number from the bytes message stream.
	 * 
	 * @return the next byte from the bytes message stream, interpreted as an
	 *         unsigned 8-bit number
	 * @throws JMSException
	 *           if the JMS provider fails to read the message due to some
	 *           internal error.
	 * @throws MessageEOFException
	 *           if unexpected end of bytes stream has been reached.
	 * @throws MessageNotReadableException
	 *           if the message is in write-only mode.
	 */
	@Override
	public int readUnsignedByte() throws JMSException
	{
		checkReadable();
		try
		{
			return dis.readUnsignedByte();
		}
		catch (EOFException e)
		{
			throw new MessageEOFException("Unable to read unsigned byte: EOF");
		}
		catch (IOException e)
		{
			throw new JMSException("Unable to read unsigned byte: " + e.getMessage());
		}
	}

	/**
	 * Reads an unsigned 16-bit number from the bytes message stream.
	 * 
	 * @return the next two bytes from the bytes message stream, interpreted as an
	 *         unsigned 16-bit integer
	 * @throws JMSException
	 *           if the JMS provider fails to read the message due to some
	 *           internal error.
	 * @throws MessageEOFException
	 *           if unexpected end of bytes stream has been reached.
	 * @throws MessageNotReadableException
	 *           if the message is in write-only mode.
	 */
	@Override
	public int readUnsignedShort() throws JMSException
	{
		checkReadable();
		try
		{
			return dis.readUnsignedShort();
		}
		catch (EOFException e)
		{
			throw new MessageEOFException("Unable to read unsigned short: EOF");
		}
		catch (IOException e)
		{
			throw new JMSException("Unable to read unsigned short: " + e.getMessage());
		}
	}

	/**
	 * Puts the message body in read-only mode and repositions the stream of bytes
	 * to the beginning.
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
		bis = new ByteArrayInputStream(data);
		dis = new DataInputStream(bis);
	}

	/**
	 * Writes a <code>boolean</code> to the bytes message stream as a 1-byte
	 * value. The value <code>true</code> is written as the value (
	 * <code>byte</code>)1; the value <code>false</code> is written as the value (
	 * <code>byte</code>)0.
	 * 
	 * @param value
	 *          the boolean value to be written
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
		try
		{
			dos.writeBoolean(value);
		}
		catch (IOException e)
		{
			throw new JMSException("Unable to write boolean: " + e.getMessage());
		}
	}

	/**
	 * Writes a <code>byte</code> to the bytes message stream as a 1-byte value.
	 * 
	 * @param value
	 *          the byte value to be written
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
		try
		{
			dos.writeByte(value);
		}
		catch (IOException e)
		{
			throw new JMSException("Unable to write byte: " + e.getMessage());
		}
	}

	/**
	 * Writes a byte array to the bytes message stream.
	 * 
	 * @param value
	 *          the byte array to be written
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
		try
		{
			dos.write(value);
		}
		catch (IOException e)
		{
			throw new JMSException("Unable to write byte array: " + e.getMessage());
		}
	}

	/**
	 * Writes a portion of a byte array to the bytes message stream.
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
	 * @param MessageNotWriteableException
	 *          if the message is in read-only mode.
	 */
	@Override
	public void writeBytes(byte[] value, int offset, int length) throws JMSException
	{
		checkWritable();
		try
		{
			dos.write(value, offset, length);
		}
		catch (IOException e)
		{
			throw new JMSException("Unable to write byte array: " + e.getMessage());
		}
	}

	/**
	 * Writes a <code>char</code> to the bytes message stream as a 2-byte value,
	 * high byte first.
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
		try
		{
			dos.writeChar(value);
		}
		catch (IOException e)
		{
			throw new JMSException("Unable to write char: " + e.getMessage());
		}
	}

	/**
	 * Converts the <code>double</code> argument to a <code>long</code> using the
	 * <code>doubleToLongBits</code> method in class <code>Double</code>, and then
	 * writes that <code>long</code> value to the bytes message stream as an
	 * 8-byte quantity, high byte first.
	 * 
	 * @param value
	 *          the double value to be written
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
		try
		{
			dos.writeDouble(value);
		}
		catch (IOException e)
		{
			throw new JMSException("Unable to write double: " + e.getMessage());
		}
	}

	/**
	 * Converts the <code>float</code> argument to an <code>int</code> using the
	 * <code>floatToIntBits</code> method in class <code>Float</code>, and then
	 * writes that <code>int</code> value to the bytes message stream as a 4-byte
	 * quantity, high byte first.
	 * 
	 * @param value
	 *          the float value to be written
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
		try
		{
			dos.writeFloat(value);
		}
		catch (IOException e)
		{
			throw new JMSException("Unable to write float: " + e.getMessage());
		}
	}

	/**
	 * Writes an <code>int</code> to the bytes message stream as four bytes, high
	 * byte first.
	 * 
	 * @param value
	 *          the <code>int</code> to be written
	 * @throws JMSException
	 *           if the JMS provider fails to write the message due to some
	 *           internal error.
	 * @throws MessageNotWriteableException
	 *           if the message is in read-only mode.
	 */
	@Override
	public void writeInt(int value) throws JMSException
	{
		checkWritable();
		try
		{
			dos.writeInt(value);
		}
		catch (IOException e)
		{
			throw new JMSException("Unable to write int: " + e.getMessage());
		}
	}

	/**
	 * Writes a <code>long</code> to the bytes message stream as eight bytes, high
	 * byte first.
	 * 
	 * @param value
	 *          the <code>long</code> to be written
	 * @throws JMSException
	 *           if the JMS provider fails to write the message due to some
	 *           internal error.
	 * @throws MessageNotWriteableException
	 *           if the message is in read-only mode.
	 */
	@Override
	public void writeLong(long value) throws JMSException
	{
		checkWritable();
		try
		{
			dos.writeLong(value);
		}
		catch (IOException e)
		{
			throw new JMSException("Unable to write long: " + e.getMessage());
		}
	}

	/**
	 * Writes an object to the bytes message stream.
	 * 
	 * <p>
	 * This method works only for the objectified primitive object types (
	 * <code>Integer</code>, <code>Double</code>, <code>Long</code> ...),
	 * <code>String</code> objects, and byte arrays.
	 * 
	 * @param value
	 *          the object in the Java programming language ("Java object") to be
	 *          written; it must not be <code>null</code>
	 * @throws JMSException
	 *           if the JMS provider fails to write the message due to some
	 *           internal error.
	 * @throws MessageFormatException
	 *           if the object is of an invalid type.
	 * @throws MessageNotWriteableException
	 *           if the message is in read-only mode.
	 * @throws NullPointerException
	 *           if the parameter value is <code>null</code>.
	 */
	@Override
	public void writeObject(Object value) throws JMSException
	{
		checkWritable();
		
		if (value == null)
		{
			throw new NullPointerException("value cannot be null");
		}
		
		try
		{
			if (value instanceof String)
			{
				dos.writeUTF((String) value);
			}
			else if (value instanceof byte[])
			{
				dos.write((byte[]) value);
			}
			else if (value instanceof Boolean)
			{
				dos.writeBoolean((Boolean) value);
			}
			else if (value instanceof Byte)
			{
				dos.writeByte((Byte) value);
			}
			else if (value instanceof Character)
			{
				dos.writeChar((Character) value);
			}
			else if (value instanceof Short)
			{
				dos.writeShort((Short) value);
			}
			else if (value instanceof Integer)
			{
				dos.writeInt((Integer) value);
			}
			else if (value instanceof Long)
			{
				dos.writeLong((Long) value);
			}
			else if (value instanceof Float)
			{
				dos.writeFloat((Float) value);
			}
			else if (value instanceof Double)
			{
				dos.writeDouble((Double) value);
			}
			else
			{
				throw new MessageFormatException("Invalid type: " + value.getClass().getName());
			}
		}
		catch (IOException e)
		{
			throw new JMSException("Unable to write object: " + e.getMessage());
		}
	}

	/**
	 * Writes a <code>short</code> to the bytes message stream as two bytes, high
	 * byte first.
	 * 
	 * @param value
	 *          the short to be written
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
		try
		{
			dos.writeShort(value);
		}
		catch (IOException e)
		{
			throw new JMSException("Unable to write short: " + e.getMessage());
		}
	}

	/**
	 * Writes a string to the bytes message stream using UTF-8 encoding in a
	 * machine-independent manner.
	 * 
	 * <p>
	 * For more information on the UTF-8 format, see
	 * "File System Safe UCS Transformation Format (FSS_UTF)", X/Open Preliminary
	 * Specification, X/Open Company Ltd., Document Number: P316. This information
	 * also appears in ISO/IEC 10646, Annex P.
	 * </p>
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
	public void writeUTF(String value) throws JMSException
	{
		checkWritable();
		try
		{
			dos.writeUTF(value);
		}
		catch (IOException e)
		{
			throw new JMSException("Unable to write UTF: " + e.getMessage());
		}
	}
	
	private void checkReadable() throws MessageNotReadableException
	{
		if (!readOnly)
		{
			throw new MessageNotReadableException("Message is in write-only mode");
		}
	}
	
	private void checkWritable() throws MessageNotWriteableException
	{
		if (readOnly)
		{
			throw new MessageNotWriteableException("Message is in read-only mode");
		}
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
			try { c.close(); } catch (Exception ignored) {}
		}
	}
	
}