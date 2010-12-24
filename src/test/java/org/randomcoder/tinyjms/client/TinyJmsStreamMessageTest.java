package com.randomcoder.tinyjms.client;

import static org.junit.Assert.*;

import javax.jms.*;

import org.junit.*;

public class TinyJmsStreamMessageTest
{
	private TinyJmsStreamMessage message;
	
	@Before
	public void setUp() throws Exception
	{
		message = new TinyJmsStreamMessage();
	}

	@After
	public void tearDown() throws Exception
	{
		message = null;
	}
	
	@Test
	public void testReadBoolean() throws JMSException
	{
		message.writeBoolean(false);
		message.writeString("true");
		message.reset();
		assertFalse(message.readBoolean());
		assertTrue(message.readBoolean());
	}

	@Test(expected=MessageNotReadableException.class)
	public void testReadBooleanNotReadable() throws JMSException
	{
		message.clearBody();
		message.readBoolean();
	}

	@Test
	public void testReadBooleanNull() throws JMSException
	{
		message.writeObject(null);
		message.reset();
		assertFalse(message.readBoolean());
	}

	@Test
	public void testReadByte() throws JMSException
	{
		message.writeByte((byte) 123);
		message.writeString("123");
		message.reset();
		assertEquals(123, message.readByte());
		assertEquals(123, message.readByte());
	}

	@Test(expected=MessageNotReadableException.class)
	public void testReadByteNotReadable() throws JMSException
	{
		message.clearBody();
		message.readByte();
	}

	@Test(expected=NumberFormatException.class)
	public void testReadByteNull() throws JMSException
	{
		message.writeObject(null);
		message.reset();
		message.readByte();
	}

	@Test
	public void testReadBytes() throws JMSException
	{
		byte[] buf = new byte[] { 0, 1, 2, 3 };
		message.writeBytes(buf);
		message.reset();
		buf = new byte[2];
		assertEquals(2, message.readBytes(buf));
		assertEquals((byte) 0, buf[0]);
		assertEquals((byte) 1, buf[1]);
		assertEquals(2, message.readBytes(buf));
		assertEquals((byte) 2, buf[0]);
		assertEquals((byte) 3, buf[1]);
		assertEquals(-1, message.readBytes(buf));
	}

	@Test(expected=MessageNotReadableException.class)
	public void testReadBytesNotReadable() throws JMSException
	{
		message.clearBody();
		message.readBytes(new byte[1]);
	}

	@Test
	public void testReadBytesNull() throws JMSException
	{
		message.writeObject(null);
		message.reset();
		assertEquals(-1, message.readBytes(new byte[1]));
	}

	@Test
	public void testReadChar() throws JMSException
	{
		message.writeChar('A');
		message.reset();
		assertEquals('A', message.readChar());
	}

	@Test(expected=MessageNotReadableException.class)
	public void testReadCharNotReadable() throws JMSException
	{
		message.clearBody();
		message.readChar();
	}

	@Test(expected=NullPointerException.class)
	public void testReadCharNull() throws JMSException
	{
		message.writeObject(null);
		message.reset();
		message.readChar();
	}

	@Test
	public void testReadDouble() throws JMSException
	{
		message.writeDouble(1.0d);
		message.writeFloat(2.0f);
		message.writeString("3.0");
		message.reset();
		assertEquals(1.0d, message.readDouble(), 0.01d);
		assertEquals(2.0d, message.readDouble(), 0.01d);
		assertEquals(3.0d, message.readDouble(), 0.01d);
	}

	@Test(expected=MessageNotReadableException.class)
	public void testReadDoubleNotReadable() throws JMSException
	{
		message.clearBody();
		message.readDouble();
	}

	@Test(expected=NullPointerException.class)
	public void testReadDoubleNull() throws JMSException
	{
		message.writeObject(null);
		message.reset();
		message.readDouble();
	}

	@Test
	public void testReadFloat() throws JMSException
	{
		message.writeFloat(1.0f);
		message.writeString("2.0");
		message.reset();
		assertEquals(1.0f, message.readFloat(), 0.01f);
		assertEquals(2.0f, message.readFloat(), 0.01f);
	}

	@Test(expected=MessageNotReadableException.class)
	public void testReadFloatNotReadable() throws JMSException
	{
		message.clearBody();
		message.readFloat();
	}

	@Test(expected=NullPointerException.class)
	public void testReadFloatNull() throws JMSException
	{
		message.writeObject(null);
		message.reset();
		message.readFloat();
	}

	@Test
	public void testReadInt() throws JMSException
	{
		message.writeInt(1);
		message.writeByte((byte) 2);
		message.writeShort((short) 3);
		message.writeString("4");
		message.reset();
		assertEquals(1, message.readInt());
		assertEquals(2, message.readInt());
		assertEquals(3, message.readInt());
		assertEquals(4, message.readInt());
	}

	@Test(expected=MessageNotReadableException.class)
	public void testReadIntNotReadable() throws JMSException
	{
		message.clearBody();
		message.readInt();
	}

	@Test(expected=NumberFormatException.class)
	public void testReadIntNull() throws JMSException
	{
		message.writeObject(null);
		message.reset();
		message.readInt();
	}

	@Test
	public void testReadLong() throws JMSException
	{
		message.writeLong(1L);
		message.writeInt(2);
		message.writeByte((byte) 3);
		message.writeShort((short) 4);
		message.writeString("5");
		message.reset();
		assertEquals(1, message.readLong());
		assertEquals(2, message.readLong());
		assertEquals(3, message.readLong());
		assertEquals(4, message.readLong());
		assertEquals(5, message.readLong());
	}

	@Test(expected=MessageNotReadableException.class)
	public void testReadLongNotReadable() throws JMSException
	{
		message.clearBody();
		message.readLong();
	}

	@Test(expected=NumberFormatException.class)
	public void testReadLongNull() throws JMSException
	{
		message.writeObject(null);
		message.reset();
		message.readLong();
	}

	@Test
	public void testReadObject() throws JMSException
	{
		message.writeObject(null);
		message.writeBoolean(true);
		message.writeByte((byte) 1);
		message.writeChar('A');
		message.writeShort((short) 2);
		message.writeInt(3);
		message.writeLong(4L);
		message.writeFloat(5.0f);
		message.writeDouble(6.0d);
		message.writeString("TEST");
		message.writeBytes(new byte[] { 1, 2});
		message.reset();
		assertNull(message.readObject());
		assertTrue((Boolean) message.readObject());
		assertEquals(Byte.valueOf((byte) 1), message.readObject());
		assertEquals(Character.valueOf('A'), message.readObject());
		assertEquals(Short.valueOf((short) 2), message.readObject());
		assertEquals(Integer.valueOf(3), message.readObject());
		assertEquals(Long.valueOf(4L), message.readObject());
		assertEquals(Float.valueOf(5.0f), message.readObject());
		assertEquals(Double.valueOf(6.0d), message.readObject());
		assertEquals("TEST", message.readObject());
		byte[] bytes = (byte[]) message.readObject();
		assertNotNull(bytes);
		assertEquals(2, bytes.length);
		assertEquals(1, bytes[0]);
		assertEquals(2, bytes[1]);
	}

	@Test(expected=MessageNotReadableException.class)
	public void testReadObjectNotReadable() throws JMSException
	{
		message.clearBody();
		message.readObject();
	}

	@Test
	public void testReadShort() throws JMSException
	{
		message.writeByte((byte) 1);
		message.writeShort((short) 2);
		message.writeString("3");
		message.reset();
		assertEquals((short) 1, message.readShort());
		assertEquals((short) 2, message.readShort());
		assertEquals((short) 3, message.readShort());
	}

	@Test
	public void testReadString() throws JMSException
	{
		message.writeObject(null);
		message.writeBoolean(true);
		message.writeByte((byte) 1);
		message.writeChar('A');
		message.writeShort((short) 2);
		message.writeInt(3);
		message.writeLong(4L);
		message.writeFloat(5.0f);
		message.writeDouble(6.0d);
		message.writeString("TEST");
		message.reset();
		assertNull(message.readString());
		assertEquals("true", message.readString());
		assertEquals("1", message.readString());
		assertEquals("A", message.readString());
		assertEquals("2", message.readString());
		assertEquals("3", message.readString());
		assertEquals("4", message.readString());
		assertEquals("5.0", message.readString());
		assertEquals("6.0", message.readString());
		assertEquals("TEST", message.readString());
	}

	@Test(expected=MessageNotReadableException.class)
	public void testReadStringNotReadable() throws JMSException
	{
		message.clearBody();
		message.readString();
	}

	@Test(expected=MessageNotReadableException.class)
	public void testReadShortNotReadable() throws JMSException
	{
		message.clearBody();
		message.readShort();
	}

	@Test(expected=NumberFormatException.class)
	public void testReadShortNull() throws JMSException
	{
		message.writeObject(null);
		message.reset();
		message.readShort();
	}

	@Test
	public void testReset() throws JMSException
	{
		message.writeInt(1);
		message.reset();
		assertEquals(1, message.readInt());
		message.reset();
		assertEquals(1, message.readInt());
	}

	@Test
	public void testWriteBoolean() throws JMSException
	{
		message.writeBoolean(true);
		message.reset();
		assertTrue(message.readBoolean());
	}

	@Test(expected=MessageNotWriteableException.class)
	public void testWriteBooleanNotWritable() throws JMSException
	{
		message.reset();
		message.writeBoolean(true);
	}
	
	@Test
	public void testWriteByte() throws JMSException
	{
		message.writeByte(Byte.MAX_VALUE);
		message.reset();
		assertEquals(Byte.MAX_VALUE, message.readByte());
	}

	@Test(expected=MessageNotWriteableException.class)
	public void testWriteByteNotWritable() throws JMSException
	{
		message.reset();
		message.writeByte(Byte.MIN_VALUE);
	}

	@Test
	public void testWriteBytes() throws JMSException
	{
		byte[] bytes = new byte[] { 1, 2 };
		message.writeBytes(bytes);
		message.reset();
		bytes = (byte[]) message.readObject();
		assertNotNull(bytes);
		assertEquals(2, bytes.length);
		assertEquals(1, bytes[0]);
		assertEquals(2, bytes[1]);
	}

	@Test(expected=MessageNotWriteableException.class)
	public void testWriteBytesNotWritable() throws JMSException
	{
		message.reset();
		message.writeBytes(new byte[] { 1 });
	}

	@Test
	public void testWriteBytesPartial() throws JMSException
	{
		byte[] bytes = new byte[] { 1, 2 };
		message.writeBytes(bytes, 1, 1);
		message.reset();
		bytes = (byte[]) message.readObject();
		assertNotNull(bytes);
		assertEquals(1, bytes.length);
		assertEquals(2, bytes[0]);
	}

	@Test(expected=MessageNotWriteableException.class)
	public void testWriteBytesPartialNotWritable() throws JMSException
	{
		message.reset();
		message.writeBytes(new byte[] { 1, 2 }, 0, 1);
	}

	@Test
	public void testWriteChar() throws JMSException
	{
		message.writeChar('A');
		message.reset();
		assertEquals('A', message.readChar());
	}

	@Test(expected=MessageNotWriteableException.class)
	public void testWriteCharNotWritable() throws JMSException
	{
		message.reset();
		message.writeChar('A');
	}

	@Test
	public void testWriteDouble() throws JMSException
	{
		message.writeDouble(1.0d);
		message.reset();
		assertEquals(1.0d, message.readDouble(), 0.01d);
	}

	@Test(expected=MessageNotWriteableException.class)
	public void testWriteDoubleNotWritable() throws JMSException
	{
		message.reset();
		message.writeDouble(1.0d);
	}

	@Test
	public void testWriteFloat() throws JMSException
	{
		message.writeFloat(1.0f);
		message.reset();
		assertEquals(1.0f, message.readFloat(), 0.01f);
	}

	@Test(expected=MessageNotWriteableException.class)
	public void testWriteFloatNotWritable() throws JMSException
	{
		message.reset();
		message.writeFloat(1.0f);
	}

	@Test
	public void testWriteInt() throws JMSException
	{
		message.writeInt(1);
		message.reset();
		assertEquals(1, message.readInt());
	}

	@Test(expected=MessageNotWriteableException.class)
	public void testWriteIntNotWritable() throws JMSException
	{
		message.reset();
		message.writeInt(1);
	}

	@Test
	public void testWriteLong() throws JMSException
	{
		message.writeLong(1L);
		message.reset();
		assertEquals(1L, message.readLong());
	}

	@Test(expected=MessageNotWriteableException.class)
	public void testWriteLongNotWritable() throws JMSException
	{
		message.reset();
		message.writeLong(1L);
	}

	@Test
	public void testWriteObject() throws JMSException
	{
		message.writeObject(null);
		message.writeObject(true);
		message.writeObject((byte) 1);
		message.writeObject('A');
		message.writeObject((short) 2);
		message.writeObject(3);
		message.writeObject(4L);
		message.writeObject(5.0f);
		message.writeObject(6.0d);
		message.writeObject("TEST");
		message.writeObject(new byte[] { 1, 2 });
		message.reset();
		assertNull(message.readString());
		assertTrue(message.readBoolean());
		assertEquals((byte) 1, message.readByte());
		assertEquals('A', message.readChar());
		assertEquals((short) 2, message.readShort());
		assertEquals(3, message.readInt());
		assertEquals(4L, message.readLong());
		assertEquals(5.0f, message.readFloat(), 0.01f);
		assertEquals(6.0d, message.readDouble(), 0.01d);
		assertEquals("TEST", message.readString());
		byte[] data = (byte[]) message.readObject();
		assertNotNull(data);
		assertEquals(2, data.length);
		assertEquals(1, data[0]);
		assertEquals(2, data[1]);
	}

	@Test(expected=MessageNotWriteableException.class)
	public void testWriteObjectNotWritable() throws JMSException
	{
		message.reset();
		message.writeObject(null);
	}
	
	@Test
	public void testWriteShort() throws JMSException
	{
		message.writeShort((short) 1);
		message.reset();
		assertEquals((short) 1, message.readShort());
	}

	@Test(expected=MessageNotWriteableException.class)
	public void testWriteShortNotWritable() throws JMSException
	{
		message.reset();
		message.writeShort((short) 1);
	}

	@Test
	public void testWriteString() throws JMSException
	{
		message.writeString(null);
		message.writeString("true");
		message.writeString("1");
		message.writeString("2");
		message.writeString("3");
		message.writeString("4");
		message.writeString("5");
		message.writeString("6");
		message.writeString("TEST");
		message.reset();
		assertNull(message.readString());
		assertTrue(message.readBoolean());
		assertEquals((byte) 1, message.readByte());
		assertEquals((short) 2, message.readShort());
		assertEquals(3, message.readInt());
		assertEquals(4L, message.readLong());
		assertEquals(5.0f, message.readFloat(), 0.01f);
		assertEquals(6.0d, message.readDouble(), 0.01d);
		assertEquals("TEST", message.readString());
	}

	@Test(expected=MessageNotWriteableException.class)
	public void testWriteStringNotWritable() throws JMSException
	{
		message.reset();
		message.writeString(null);
	}

}
