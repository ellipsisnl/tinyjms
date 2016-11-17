package nl.ellipsis.tinyjms.client;

import java.io.*;
import java.util.*;

import javax.jms.*;

import nl.ellipsis.tinyjms.protocol.MarshallingSupport;

/**
 * TinyJms implementation of {@link MapMessage}.
 */
public class TinyJmsMapMessage extends TinyJmsMessage implements MapMessage
{
	private final Map<String, Object> data = new HashMap<String, Object>();
	private boolean readOnly = false;

	@Override
	byte[] getBody() throws JMSException
	{
		ByteArrayOutputStream bos = null;
		DataOutputStream dos = null;
		
		try
		{
			bos = new ByteArrayOutputStream();
			dos = new DataOutputStream(bos);
			MarshallingSupport.marshalMap(dos, data);
		}
		finally
		{
			if (dos != null) try { dos.close(); } catch (Exception ignored) {}
			if (bos != null) try { bos.close(); } catch (Exception ignored) {}
		}
		
		return bos.toByteArray();
	}
	
	@Override
	void setBody(byte[] bodyData) throws JMSException
	{
		data.clear();
		if (bodyData == null)
		{
			return;
		}
		
		ByteArrayInputStream bis = null;
		DataInputStream dis = null;
		
		try
		{
			bis = new ByteArrayInputStream(bodyData);
			dis = new DataInputStream(bis);
			MarshallingSupport.unmarshalMap(dis, data);
		}
		finally
		{
			if (dis != null) try { dis.close(); } catch (Exception ignored) {}
			if (bis != null) try { bis.close(); } catch (Exception ignored) {}
		}
	}
	
	/**
	 * Sets the value of the read-only flag for this message.
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
		data.clear();
		readOnly = false;
	}

	/**
	 * Returns the <code>boolean</code> value with the specified name.
	 * 
	 * @param name
	 *          the name of the <code>boolean</code>
	 * @return the <code>boolean</code> value with the specified name
	 * @throws JMSException
	 *           if the JMS provider fails to read the message due to some
	 *           internal error.
	 * @throws MessageFormatException
	 *           if this type conversion is invalid.
	 */
	@Override
	public boolean getBoolean(String name) throws JMSException
	{
		Object value = data.get(name);

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

	/**
	 * Returns the <code>byte</code> value with the specified name.
	 * 
	 * @param name
	 *          the name of the <code>byte</code>
	 * @return the <code>byte</code> value with the specified name
	 * @throws JMSException
	 *           if the JMS provider fails to read the message due to some
	 *           internal error.
	 * @throws MessageFormatException
	 *           if this type conversion is invalid.
	 */
	@Override
	public byte getByte(String name) throws JMSException
	{
		Object value = data.get(name);

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
	 * Returns the byte array value with the specified name.
	 * 
	 * @param name
	 *          the name of the byte array
	 * @return a copy of the byte array value with the specified name; if there is
	 *         no item by this name, a <code>null</code> value is returned.
	 * @throws JMSException
	 *           if the JMS provider fails to read the message due to some
	 *           internal error.
	 * @throws MessageFormatException
	 *           if this type conversion is invalid.
	 */
	@Override
	public byte[] getBytes(String name) throws JMSException
	{
		Object value = data.get(name);

		if (value == null)
		{
			return null;
		}

		if (value instanceof byte[])
		{
			byte[] result = new byte[((byte[]) value).length];
			System.arraycopy(value, 0, result, 0, result.length);
			return result;
		}

		throw new MessageFormatException("Unsupported conversion to byte array from type: " + value.getClass().getName());
	}

	/**
	 * Returns the Unicode character value with the specified name.
	 * 
	 * @param name
	 *          the name of the Unicode character
	 * @return the Unicode character value with the specified name
	 * @throws JMSException
	 *           if the JMS provider fails to read the message due to some
	 *           internal error.
	 * @throws MessageFormatException
	 *           if this type conversion is invalid.
	 */
	@Override
	public char getChar(String name) throws JMSException
	{
		Object value = data.get(name);

		if (value == null)
		{
			throw new NullPointerException("Null value cannot be converted to char");
		}

		if (value instanceof Character)
		{
			return (Character) value;
		}

		throw new MessageFormatException("Unsupported conversion to char from type: " + value.getClass().getName());
	}

	/**
	 * Returns the <code>double</code> value with the specified name.
	 * 
	 * @param name
	 *          the name of the <code>double</code>
	 * @return the <code>double</code> value with the specified name
	 * @throws JMSException
	 *           if the JMS provider fails to read the message due to some
	 *           internal error.
	 * @throws MessageFormatException
	 *           if this type conversion is invalid.
	 */
	@Override
	public double getDouble(String name) throws JMSException
	{
		Object value = data.get(name);

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
	 * Returns the <code>float</code> value with the specified name.
	 * 
	 * @param name
	 *          the name of the <code>float</code>
	 * @return the <code>float</code> value with the specified name
	 * @throws JMSException
	 *           if the JMS provider fails to read the message due to some
	 *           internal error.
	 * @throws MessageFormatException
	 *           if this type conversion is invalid.
	 */
	@Override
	public float getFloat(String name) throws JMSException
	{
		Object value = data.get(name);

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
	 * Returns the <code>int</code> value with the specified name.
	 * 
	 * @param name
	 *          the name of the <code>int</code>
	 * @return the <code>int</code> value with the specified name
	 * @throws JMSException
	 *           if the JMS provider fails to read the message due to some
	 *           internal error.
	 * @throws MessageFormatException
	 *           if this type conversion is invalid.
	 */
	@Override
	public int getInt(String name) throws JMSException
	{
		Object value = data.get(name);

		if (value instanceof Integer)
		{
			return (Integer) value;
		}

		if (value instanceof Byte)
		{
			return (Byte) value;
		}

		if (value instanceof Short)
		{
			return (Short) value;
		}

		if (value == null || value instanceof String)
		{
			return Integer.valueOf((String) value);
		}

		throw new MessageFormatException("Unsupported conversion to int from type: " + value.getClass().getName());
	}

	/**
	 * Returns the <code>long</code> value with the specified name.
	 * 
	 * @param name
	 *          the name of the <code>long</code>
	 * @return the <code>long</code> value with the specified name
	 * @throws JMSException
	 *           if the JMS provider fails to read the message due to some
	 *           internal error.
	 * @throws MessageFormatException
	 *           if this type conversion is invalid.
	 */
	@Override
	public long getLong(String name) throws JMSException
	{
		Object value = data.get(name);

		if (value instanceof Long)
		{
			return (Long) value;
		}

		if (value instanceof Integer)
		{
			return (Integer) value;
		}

		if (value instanceof Byte)
		{
			return (Byte) value;
		}

		if (value instanceof Short)
		{
			return (Short) value;
		}

		if (value == null || value instanceof String)
		{
			return Long.valueOf((String) value);
		}

		throw new MessageFormatException("Unsupported conversion to long from type: " + value.getClass().getName());
	}

	/**
	 * Returns an <code>Enumeration</code> of all the names in the
	 * <code>MapMessage</code> object.
	 * 
	 * @return an enumeration of all the names in this <code>MapMessage</code>
	 * @throws JMSException
	 *           if the JMS provider fails to read the message due to some
	 *           internal error.
	 */
	@Override
	public Enumeration getMapNames() throws JMSException
	{
		return Collections.enumeration(data.keySet());
	}

	/**
	 * Returns the value of the object with the specified name.
	 * 
	 * <p>
	 * This method can be used to return, in objectified format, an object in the
	 * Java programming language ("Java object") that had been stored in the Map
	 * with the equivalent <code>setObject</code> method call, or its equivalent
	 * primitive settype method.
	 * </p>
	 * 
	 * <p>
	 * Note that byte values are returned as <code>byte[]</code>, not
	 * <code>Byte[]</code>.
	 * </p>
	 * 
	 * @param name
	 *          the name of the Java object
	 * @return a copy of the Java object value with the specified name, in
	 *         objectified format (for example, if the object was set as an
	 *         <code>int</code>, an <code>Integer</code> is returned); if there is
	 *         no item by this name, a <code>null</code> value is returned
	 * @throws JMSException
	 *           if the JMS provider fails to read the message due to some
	 *           internal error.
	 */
	@Override
	public Object getObject(String name) throws JMSException
	{
		Object value = data.get(name);
		
		if (value == null)
		{
			return null;
		}
		
		if (value instanceof Boolean ||
				value instanceof Byte ||
				value instanceof Character ||
				value instanceof Short ||
				value instanceof Integer ||
				value instanceof Long ||
				value instanceof Float ||
				value instanceof Double ||
				value instanceof String)
		{
			return value;
		}
		
		if (value instanceof byte[])
		{
			byte[] result = new byte[((byte[]) value).length];
			System.arraycopy(value, 0, result, 0, result.length);
			return result;
		}
		
		throw new JMSException("Invalid data type in map: " + value.getClass().getName());
	}

	/**
	 * Returns the <code>short</code> value with the specified name.
	 * 
	 * @param name
	 *          the name of the <code>short</code>
	 * @return the <code>short</code> value with the specified name
	 * @throws JMSException
	 *           if the JMS provider fails to read the message due to some
	 *           internal error.
	 * @throws MessageFormatException
	 *           if this type conversion is invalid.
	 */
	@Override
	public short getShort(String name) throws JMSException
	{
		Object value = data.get(name);

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
	 * Returns the <code>String</code> value with the specified name.
	 * 
	 * @param name
	 *          the name of the <code>String</code>
	 * @return the <code>String</code> value with the specified name; if there is
	 *         no item by this name, a <code>null</code> value is returned
	 * @throws JMSException
	 *           if the JMS provider fails to read the message due to some
	 *           internal error.
	 * @throws MessageFormatException
	 *           if this type conversion is invalid.
	 */
	@Override
	public String getString(String name) throws JMSException
	{
		Object value = data.get(name);
		
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
	 * Indicates whether an item exists in this <code>MapMessage</code> object.
	 * 
	 * @param name
	 *          the name of the item to test
	 * @return <code>true</code> if the item exists
	 * @throws JMSException
	 *           if the JMS provider fails to determine if the item exists due to
	 *           some internal error.
	 */
	@Override
	public boolean itemExists(String name) throws JMSException
	{
		return data.containsKey(name);
	}

	/**
	 * Sets a <code>boolean</code> value with the specified name into the Map.
	 * 
	 * @param name
	 *          the name of the <code>boolean</code>
	 * @param value
	 *          the <code>boolean</code> value to set in the Map
	 * @throws JMSException
	 *           if the JMS provider fails to write the message due to some
	 *           internal error.
	 * @throws IllegalArgumentException
	 *           if the name is <code>null</code> or if the name is an empty
	 *           string.
	 * @throws MessageNotWriteableException
	 *           if the message is in read-only mode.
	 */
	@Override
	public void setBoolean(String name, boolean value) throws JMSException
	{
		checkName(name);
		checkReadOnly();
		data.put(name, value);
	}

	/**
	 * Sets a <code>byte</code> value with the specified name into the Map.
	 * 
	 * @param name
	 *          the name of the <code>byte</code>
	 * @param value
	 *          the <code>byte</code> value to set in the Map
	 * @throws JMSException
	 *           if the JMS provider fails to write the message due to some
	 *           internal error.
	 * @throws IllegalArgumentException
	 *           if the name is <code>null</code> or if the name is an empty
	 *           string.
	 * @throws MessageNotWriteableException
	 *           if the message is in read-only mode.
	 */
	@Override
	public void setByte(String name, byte value) throws JMSException
	{
		checkName(name);
		checkReadOnly();
		data.put(name, value);
	}

	/**
	 * Sets a byte array value with the specified name into the Map.
	 * 
	 * @param name
	 *          the name of the byte array
	 * @param value
	 *          the byte array value to set in the Map; the array is copied so
	 *          that the value for name will not be altered by future
	 *          modifications
	 * @throws JMSException
	 *           if the JMS provider fails to write the message due to some
	 *           internal error.
	 * @throws IllegalArgumentException
	 *           if the name is <code>null</code>, or if the name is an empty
	 *           string. <strong>NOTE:</strong> This deviates from the JMS 1.1
	 *           specification which calls for <code>NullPointerException</code>
	 *           in order to make this consistent with all other setters in this
	 *           class.
	 * @throws MessageNotWriteableException
	 *           if the message is in read-only mode.
	 */
	@Override
	public void setBytes(String name, byte[] value) throws JMSException
	{
		checkName(name); // JMS 1.1 calls for NPE here, but IAE is more consistent
		checkReadOnly();
		byte[] copy = null;
		if (value != null)
		{
			copy = new byte[value.length];
			System.arraycopy(value, 0, copy, 0, value.length);
		}
		data.put(name, copy);
	}

	/**
	 * Sets a portion of the byte array value with the specified name into the
	 * Map.
	 * 
	 * @param name
	 *          the name of the byte array
	 * @param value
	 *          the byte array value to set in the Map
	 * @param offset
	 *          the initial offset within the byte array
	 * @param length
	 *          the number of bytes to use
	 * @throws JMSException
	 *           if the JMS provider fails to write the message due to some
	 *           internal error.
	 * @throws IllegalArgumentException
	 *           if the name is <code>null</code> or if the name is an empty
	 *           string.
	 * @throws MessageNotWriteableException
	 *           if the message is in read-only mode.
	 */
	@Override
	public void setBytes(String name, byte[] value, int offset, int length) throws JMSException
	{
		checkName(name);
		checkReadOnly();
		byte[] copy = null;
		if (value != null)
		{
			copy = new byte[length];
			System.arraycopy(value, offset, copy, 0, length);
		}
		data.put(name, copy);
	}

	/**
	 * Sets a Unicode character value with the specified name into the Map.
	 * 
	 * @param name
	 *          the name of the Unicode character
	 * @param value
	 *          the Unicode character value to set in the Map @throws JMSException
	 *          if the JMS provider fails to write the message due to some
	 *          internal error.
	 * @throws IllegalArgumentException
	 *           if the name is <code>null</code> or if the name is an empty
	 *           string.
	 * @throws MessageNotWriteableException
	 *           if the message is in read-only mode.
	 */
	@Override
	public void setChar(String name, char value) throws JMSException
	{
		checkName(name);
		checkReadOnly();
		data.put(name, value);
	}

	/**
	 * Sets a <code>double</code> value with the specified name into the Map.
	 * 
	 * @param name
	 *          the name of the <code>double</code>
	 * @param value
	 *          the <code>double</code> value to set in the Map
	 * @throws JMSException
	 *           if the JMS provider fails to write the message due to some
	 *           internal error.
	 * @throws IllegalArgumentException
	 *           if the name is <code>null</code> or if the name is an empty
	 *           string.
	 * @throws MessageNotWriteableException
	 *           if the message is in read-only mode.
	 */
	@Override
	public void setDouble(String name, double value) throws JMSException
	{
		checkName(name);
		checkReadOnly();
		data.put(name, value);
	}

	/**
	 * Sets a <code>float</code> value with the specified name into the Map.
	 * 
	 * @param name
	 *          the name of the <code>float</code>
	 * @param value
	 *          the <code>float</code> value to set in the Map
	 * @throws JMSException
	 *           if the JMS provider fails to write the message due to some
	 *           internal error.
	 * @throws IllegalArgumentException
	 *           if the name is <code>null</code> or if the name is an empty
	 *           string.
	 * @throws MessageNotWriteableException
	 *           if the message is in read-only mode.
	 */
	@Override
	public void setFloat(String name, float value) throws JMSException
	{
		checkName(name);
		checkReadOnly();
		data.put(name, value);
	}

	/**
	 * Sets a <code>int</code> value with the specified name into the Map.
	 * 
	 * @param name
	 *          the name of the <code>int</code>
	 * @param value
	 *          the <code>int</code> value to set in the Map
	 * @throws JMSException
	 *           if the JMS provider fails to write the message due to some
	 *           internal error.
	 * @throws IllegalArgumentException
	 *           if the name is <code>null</code> or if the name is an empty
	 *           string.
	 * @throws MessageNotWriteableException
	 *           if the message is in read-only mode.
	 */
	@Override
	public void setInt(String name, int value) throws JMSException
	{
		checkName(name);
		checkReadOnly();
		data.put(name, value);
	}

	/**
	 * Sets a <code>long</code> value with the specified name into the Map.
	 * 
	 * @param name
	 *          the name of the <code>long</code>
	 * @param value
	 *          the <code>long</code> value to set in the Map
	 * @throws JMSException
	 *           if the JMS provider fails to write the message due to some
	 *           internal error.
	 * @throws IllegalArgumentException
	 *           if the name is <code>null</code> or if the name is an empty
	 *           string.
	 * @throws MessageNotWriteableException
	 *           if the message is in read-only mode.
	 */
	@Override
	public void setLong(String name, long value) throws JMSException
	{
		checkName(name);
		checkReadOnly();
		data.put(name, value);
	}

	/**
	 * Sets an object value with the specified name into the Map.
	 * 
	 * <p>
	 * This method works only for the objectified primitive object types (
	 * <code>Integer</code>, <code>Double</code>, <code>Long</code> ...),
	 * <code>String</code> objects, and byte arrays.
	 * </p>
	 * 
	 * @param name
	 *          the name of the Java object
	 * @param value
	 *          the Java object value to set in the Map
	 * @throws JMSException
	 *           - if the JMS provider fails to write the message due to some
	 *           internal error.
	 * @throws IllegalArgumentException
	 *           if the name is <code>null</code> or if the name is an empty
	 *           string.
	 * @throws MessageFormatException
	 *           if the object is invalid.
	 * @throws MessageNotWriteableException
	 *           if the message is in read-only mode.
	 */
	@Override
	public void setObject(String name, Object value) throws JMSException
	{
		checkReadOnly();
		checkName(name);
		
		if (value == null ||
				value instanceof Boolean ||
				value instanceof Byte ||
				value instanceof Character ||
				value instanceof Short || 
				value instanceof Integer ||
				value instanceof Long ||
				value instanceof Float ||
				value instanceof Double ||
				value instanceof String)
		{
			data.put(name, value);
		}
		else if (value instanceof byte[])
		{
			byte[] copy = new byte[((byte[]) value).length];
			System.arraycopy(value, 0, copy, 0, copy.length);
			data.put(name, copy);
		}
		else
		{
			throw new MessageFormatException("Invalid object type: " + value.getClass().getName());
		}
	}

	/**
	 * Sets a <code>short</code> value with the specified name into the Map.
	 * 
	 * @param name
	 *          the name of the <code>short</code>
	 * @param value
	 *          the <code>short</code> value to set in the Map
	 * @throws JMSException
	 *           if the JMS provider fails to write the message due to some
	 *           internal error.
	 * @throws IllegalArgumentException
	 *           if the name is <code>null</code> or if the name is an empty
	 *           string.
	 * @throws MessageNotWriteableException
	 *           if the message is in read-only mode.
	 */
	@Override
	public void setShort(String name, short value) throws JMSException
	{
		checkName(name);
		checkReadOnly();
		data.put(name, value);
	}

	/**
	 * Sets a <code>String</code> value with the specified name into the Map.
	 * 
	 * @param name
	 *          the name of the <code>String</code>
	 * @param value
	 *          the <code>String</code> value to set in the Map
	 * @throws JMSException
	 *           if the JMS provider fails to write the message due to some
	 *           internal error.
	 * @throws IllegalArgumentException
	 *           if the name is <code>null</code> or if the name is an empty
	 *           string.
	 * @throws MessageNotWriteableException
	 *           if the message is in read-only mode.
	 */
	@Override
	public void setString(String name, String value) throws JMSException
	{
		checkName(name);
		checkReadOnly();
		data.put(name, value);
	}

	private void checkReadOnly() throws MessageNotWriteableException
	{
		if (readOnly)
		{
			throw new MessageNotWriteableException("Message is in read-only mode");
		}
	}

	private void checkName(String name) throws IllegalArgumentException
	{
		if (name == null || name.length() == 0)
		{
			throw new IllegalArgumentException("name must be specified");
		}
	}
}
