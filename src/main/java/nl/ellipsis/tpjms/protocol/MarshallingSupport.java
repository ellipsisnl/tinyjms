package nl.ellipsis.tpjms.protocol;

import java.io.*;
import java.util.Map;

import javax.jms.*;

/**
 * Marshalling support for TinyJms objects.
 */
public final class MarshallingSupport
{
	private MarshallingSupport() {}
	
	/**
	 * Enumeration of supported types for marshalling.
	 */
	public static enum ObjectType
	{
		NULL, BOOLEAN, BYTE, CHAR, SHORT, INT, LONG, FLOAT, DOUBLE, STRING, BYTE_ARRAY;

		/**
		 * Gets an enum constant by ordinal.
		 * 
		 * @param ordinal
		 *          ordinal value to lookup
		 * @return object type, or <code>null</code> if not found
		 */
		public static ObjectType getByOrdinal(int ordinal)
		{
			if (ordinal < 0 || ordinal >= values().length)
				return null;

			return values()[ordinal];
		}
	}
	
	public static void marshalMap(DataOutputStream dos, Map<String, Object> map) throws JMSException
	{
		try
		{
			dos.writeInt(map.size()); // send count for easier deserialization			
			for (Map.Entry<String, Object> entry : map.entrySet())
			{
				String key = entry.getKey();
				if (key == null)
				{
					throw new MessageFormatException("name cannot be null");
				}
				dos.writeUTF(key);
				marshalObject(dos, entry.getValue());
			}
		}
		catch (IOException e)
		{
			throw new JMSException("Unable to serialize map: " + e.getMessage());
		}
	}
	
	public static void unmarshalMap(DataInputStream dis, Map<String, Object> map) throws JMSException
	{
		try
		{
			int size = dis.readInt();
			if (size < 0)
			{
				throw new MessageFormatException("Invalid map size: " + size);
			}
			
			for (int i = 0; i < size; i++)
			{
				String key = dis.readUTF();
				Object value = unmarshalObject(dis);
				map.put(key, value);
			}
		}
		catch (EOFException e)
		{
			throw new MessageEOFException("End of message reached: " + e.getMessage());
		}
		catch (IOException e)
		{
			throw new JMSException("Error deserializing map: " + e.getMessage());
		}
	}
	
	public static Object unmarshalObject(DataInputStream dis) throws JMSException
	{
		try
		{
			// read ordinal
			int ordinal = dis.readByte();
			ObjectType type = ObjectType.getByOrdinal(ordinal);
			if (type == null)
			{
				throw new MessageFormatException("Invalid type read: " + ordinal);
			}

			switch (type)
			{
				case NULL:
					return null;
				case BOOLEAN:
					return dis.readBoolean();
				case BYTE:
					return dis.readByte();
				case CHAR:
					return dis.readChar();
				case SHORT:
					return dis.readShort();
				case INT:
					return dis.readInt();
				case LONG:
					return dis.readLong();
				case FLOAT:
					return dis.readFloat();
				case DOUBLE:
					return dis.readDouble();
				case STRING:
					return dis.readUTF();
				case BYTE_ARRAY:
					return unmarshalBytes(dis);
				default:
					throw new JMSException("Unknown data type reeived: " + type);
			}
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
	
	public static void marshalObject(DataOutputStream dos, Object value) throws JMSException
	{
		try
		{
			if (value == null)
			{
				dos.writeByte(ObjectType.NULL.ordinal());
			}
			else if (value instanceof Boolean)
			{
				dos.writeByte(ObjectType.BOOLEAN.ordinal());
				dos.writeBoolean((Boolean) value);
			}
			else if (value instanceof Byte)
			{
				dos.writeByte(ObjectType.BYTE.ordinal());
				dos.writeByte((Byte) value);
			}
			else if (value instanceof Character)
			{
				dos.writeByte(ObjectType.CHAR.ordinal());
				dos.writeChar((Character) value);
			}
			else if (value instanceof Short)
			{
				dos.writeByte(ObjectType.SHORT.ordinal());
				dos.writeShort((Short) value);
			}
			else if (value instanceof Integer)
			{
				dos.writeByte(ObjectType.INT.ordinal());
				dos.writeInt((Integer) value);
			}
			else if (value instanceof Long)
			{
				dos.writeByte(ObjectType.LONG.ordinal());
				dos.writeLong((Long) value);
			}
			else if (value instanceof Float)
			{
				dos.writeByte(ObjectType.FLOAT.ordinal());
				dos.writeFloat((Float) value);
			}
			else if (value instanceof Double)
			{
				dos.writeByte(ObjectType.DOUBLE.ordinal());
				dos.writeDouble((Double) value);
			}
			else if (value instanceof String)
			{
				dos.writeByte(ObjectType.STRING.ordinal());
				dos.writeUTF((String) value);
			}
			else if (value instanceof byte[])
			{
				dos.writeByte(ObjectType.BYTE_ARRAY.ordinal());
				dos.writeInt(((byte[]) value).length);
				dos.write((byte[]) value);
			}
			else
			{
				throw new MessageFormatException("Invalid type: " + value.getClass().getName());
			}
		}
		catch (EOFException e)
		{
			throw new MessageEOFException("End of message: " + e.getMessage());
		}
		catch (IOException e)
		{
			throw new JMSException("Error writing data: " + e.getMessage());
		}
	}
	
	private static byte[] unmarshalBytes(DataInputStream dis) throws IOException
	{
		int length = dis.readInt();
		byte[] buf = new byte[length];
		dis.readFully(buf);
		return buf;
	}
}
