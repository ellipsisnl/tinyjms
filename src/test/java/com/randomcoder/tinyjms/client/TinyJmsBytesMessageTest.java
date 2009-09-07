package com.randomcoder.tinyjms.client;

import static org.junit.Assert.*;

import javax.jms.*;

import org.junit.*;

public class TinyJmsBytesMessageTest
{
	private TinyJmsBytesMessage message;
	
	@Before
	public void setUp() throws Exception
	{
		message = new TinyJmsBytesMessage();
	}

	@After
	public void tearDown() throws Exception
	{
		message = null;
	}

	@Test
	public void testGetSetBody() throws JMSException
	{
		message.setBody(new byte[] { 1 });
		byte[] data = message.getBody();
		assertEquals(1, data.length);
		assertEquals((byte) 1, data[0]);
	}
	
	@Test
	public void testGetBodyLength() throws JMSException
	{
		message.reset(); // make read-only
		assertEquals(0L, message.getBodyLength());
	}

	@Test
	public void testReadWriteBoolean() throws JMSException
	{
		message.writeBoolean(true);
		message.writeBoolean(false);
		message.reset();
		assertTrue(message.readBoolean());
		assertFalse(message.readBoolean());
	}

	@Test
	public void testReadWriteByte() throws JMSException
	{
		message.writeByte(Byte.MIN_VALUE);
		message.writeByte(Byte.MAX_VALUE);
		message.reset();
		assertEquals(Byte.MIN_VALUE, message.readByte());
		assertEquals(Byte.MAX_VALUE, message.readByte());
	}

	@Test
	public void testReadWriteBytes() throws JMSException
	{
		byte[] out = { 0x01, 0x02, 0x03};
		byte[] in = new byte[3];
		
		message.writeBytes(out);
		message.reset();
		assertEquals(3, message.readBytes(in));
		assertEquals(0x01, in[0]);
		assertEquals(0x02, in[1]);
		assertEquals(0x03, in[2]);
		assertEquals(-1, message.readBytes(in));
	}

	@Test
	public void testReadWriteBytesPartial() throws JMSException
	{
		byte[] out = { 0x01, 0x02, 0x03};
		byte[] in = new byte[3];
		
		message.writeBytes(out, 0, 2);
		message.reset();
		assertEquals(2, message.readBytes(in, 3));
		assertEquals(0x01, in[0]);
		assertEquals(0x02, in[1]);
		assertEquals(-1, message.readBytes(in, 3));
	}

	@Test
	public void testReadWriteChar() throws JMSException
	{
		message.writeChar('A');
		message.writeChar('B');
		message.reset();
		assertEquals('A', message.readChar());
		assertEquals('B', message.readChar());
	}

	@Test
	public void testReadWriteDouble() throws JMSException
	{
		message.writeDouble(Double.MIN_VALUE);
		message.writeDouble(Double.MAX_VALUE);
		message.reset();
		assertEquals(Double.MIN_VALUE, message.readDouble(), 0.01d);
		assertEquals(Double.MAX_VALUE, message.readDouble(), 0.01d);
	}

	@Test
	public void testReadWriteFloat() throws JMSException
	{
		message.writeFloat(Float.MIN_VALUE);
		message.writeFloat(Float.MAX_VALUE);
		message.reset();
		assertEquals(Float.MIN_VALUE, message.readFloat(), 0.01f);
		assertEquals(Float.MAX_VALUE, message.readFloat(), 0.01f);
	}

	@Test
	public void testReadWriteInt() throws JMSException
	{
		message.writeInt(Integer.MIN_VALUE);
		message.writeInt(Integer.MAX_VALUE);
		message.reset();
		assertEquals(Integer.MIN_VALUE, message.readInt());
		assertEquals(Integer.MAX_VALUE, message.readInt());
	}

	@Test
	public void testReadWriteLong() throws JMSException
	{
		message.writeLong(Long.MIN_VALUE);
		message.writeLong(Long.MAX_VALUE);
		message.reset();
		assertEquals(Long.MIN_VALUE, message.readLong());
		assertEquals(Long.MAX_VALUE, message.readLong());
	}

	@Test
	public void testReadWriteShort() throws JMSException
	{
		message.writeShort(Short.MIN_VALUE);
		message.writeShort(Short.MAX_VALUE);
		message.reset();
		assertEquals(Short.MIN_VALUE, message.readShort());
		assertEquals(Short.MAX_VALUE, message.readShort());
	}

	@Test
	public void testReadWriteUTF() throws JMSException
	{
		message.writeUTF("Test");
		message.reset();
		assertEquals("Test", message.readUTF());
	}

	@Test
	public void testReadWriteUnsignedByte() throws JMSException
	{
		message.writeByte((byte) 255);
		message.reset();
		assertEquals(255, message.readUnsignedByte());
	}

	@Test
	public void testReadUnsignedShort() throws JMSException
	{
		message.writeShort((short) 65535);
		message.reset();
		assertEquals(65535, message.readUnsignedShort());
	}

	@Test
	public void testClearBody() throws JMSException
	{
		message.writeBoolean(true);
		message.clearBody();
		message.writeBoolean(true);
		message.reset();
		assertEquals(1, message.getBodyLength());
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
	public void testWriteObject() throws JMSException
	{
		message.writeObject(true);
		message.writeObject((byte) 1);
		message.writeObject('A');
		message.writeObject((short) 1);
		message.writeObject(1);
		message.writeObject(1L);
		message.writeObject(1.0f);
		message.writeObject(1.0d);
		message.writeObject("TEST");
		message.reset();
		assertTrue(message.readBoolean());
		assertEquals((byte) 1, message.readByte());
		assertEquals('A', message.readChar());
		assertEquals((short) 1, message.readShort());
		assertEquals(1, message.readInt());
		assertEquals(1L, message.readLong());
		assertEquals(1.0f, message.readFloat(), 0.01f);
		assertEquals(1.0d, message.readDouble(), 0.01d);
		assertEquals("TEST", message.readUTF());
	}
	
	@Test(expected=NullPointerException.class)
	public void testWriteObjectNull() throws JMSException
	{
		message.writeObject(null);
	}
	
	@Test(expected=MessageFormatException.class)
	public void testWriteObjectInvalid() throws JMSException
	{
		message.writeObject(new Object());
	}
}