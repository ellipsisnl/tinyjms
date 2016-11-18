package nl.ellipsis.tpjms.core.message;

import static org.junit.Assert.*;

import java.util.*;

import javax.jms.*;

import org.junit.*;

import nl.ellipsis.tpjms.core.message.TPJMSMapMessage;

public class TPJMSMapMessageTest
{
	private TPJMSMapMessage message;
	@Before
	public void setUp() throws Exception
	{
		message = new TPJMSMapMessage();
	}

	@After
	public void tearDown() throws Exception
	{
		message = null;
	}

	@Test
	public void testGetSetBody() throws JMSException
	{
		message.setObject("test", null);
		message.setObject("test2", true);
		message.setObject("test3", (byte) 3);
		message.setObject("test4", 'A');
		message.setObject("test5", (short) 5);
		message.setObject("test6", 6);
		message.setObject("test7", 7L);
		message.setObject("test8", 8.0f);
		message.setObject("test9", 9.0d);
		message.setObject("test10", new byte[] { 1 });
		
		byte[] ser = message.getBody();
		assertNotNull(ser);
		
		message.clearBody();
		message.setBody(ser);
		
		assertNull(message.getObject("test"));
		assertTrue(message.getBoolean("test2"));
		assertEquals((byte) 3, message.getByte("test3"));
		assertEquals('A', message.getChar("test4"));
		assertEquals((short) 5, message.getShort("test5"));
		assertEquals(6, message.getInt("test6"));
		assertEquals(7L, message.getLong("test7"));
		assertEquals(8.0f, message.getFloat("test8"), 0.01f);
		assertEquals(9.0d, message.getDouble("test9"), 0.01d);
		byte[] dst = message.getBytes("test10");
		assertNotNull(dst);
		assertEquals(1, dst.length);
		assertEquals(1, dst[0]);
	}
	
	@Test
	public void testClearBody() throws JMSException
	{
		message.setString("test", "value");
		assertEquals("value", message.getString("test"));
		message.clearBody();
		assertNull(message.getString("test"));
	}
	
	@Test
	public void testGetBoolean() throws JMSException
	{
		message.setBoolean("test", true);
		message.setString("test2", "true");
		assertTrue(message.getBoolean("test"));
		assertTrue(message.getBoolean("test2"));
		assertFalse(message.getBoolean("test3"));
	}

	@Test
	public void testGetByte() throws JMSException
	{
		message.setByte("test", Byte.MAX_VALUE);
		message.setString("test2", "123");
		assertEquals(Byte.MAX_VALUE, message.getByte("test"));
		assertEquals((byte) 123, message.getByte("test2"));
	}

	@Test(expected=NumberFormatException.class)
	public void testGetByteNull() throws JMSException
	{
		message.getByte("null");
	}

	@Test
	public void testGetBytes() throws JMSException
	{
		message.setBytes("test", new byte[] { 1 });
		byte[] result = message.getBytes("test");
		assertNotNull(result);
		assertEquals(1, result.length);
		assertEquals((byte) 1, result[0]);
		assertNull(message.getBytes("test2"));
	}

	@Test
	public void testGetChar() throws JMSException
	{
		message.setChar("test", 'A');
		assertEquals('A', message.getChar("test"));
	}

	@Test(expected=NullPointerException.class)
	public void testGetCharNull() throws JMSException
	{
		message.getChar("test");
	}

	@Test
	public void testGetDouble() throws JMSException
	{
		message.setFloat("test", 1.0f);
		message.setDouble("test2", 2.0d);
		message.setString("test3", "3");
		assertEquals(1.0d, message.getDouble("test"), 0.01d);
		assertEquals(2.0d, message.getDouble("test2"), 0.01d);
		assertEquals(3.0d, message.getDouble("test3"), 0.01d);
	}

	@Test(expected=NullPointerException.class)
	public void testGetDoubleNull() throws JMSException
	{
		message.getDouble("test");
	}

	@Test
	public void testGetFloat() throws JMSException
	{
		message.setFloat("test", 1.0f);
		message.setString("test2", "2.0");
		assertEquals(1.0f, message.getFloat("test"), 0.01f);
		assertEquals(2.0f, message.getFloat("test2"), 0.01f);
	}

	@Test(expected=NullPointerException.class)
	public void testGetFloatNull() throws JMSException
	{
		message.getFloat("test");
	}

	@Test
	public void testGetInt() throws JMSException
	{
		message.setInt("test", 1);
		message.setByte("test2", (byte) 2);
		message.setShort("test3", (short) 3);
		message.setString("test4", "4");
		assertEquals(1, message.getInt("test"));
		assertEquals(2, message.getInt("test2"));
		assertEquals(3, message.getInt("test3"));
		assertEquals(4, message.getInt("test4"));
	}

	@Test(expected=NumberFormatException.class)
	public void testGetIntNull() throws JMSException
	{
		message.getInt("test");
	}

	@Test
	public void testGetLong() throws JMSException
	{
		message.setLong("test", 1L);
		message.setInt("test2", 2);
		message.setByte("test3", (byte) 3);
		message.setShort("test4", (short) 4);
		message.setString("test5", "5");
		assertEquals(1, message.getLong("test"));
		assertEquals(2, message.getLong("test2"));
		assertEquals(3, message.getLong("test3"));
		assertEquals(4, message.getLong("test4"));
		assertEquals(5, message.getLong("test5"));
	}

	@Test(expected=NumberFormatException.class)
	public void testGetLongNull() throws JMSException
	{
		message.getLong("test");
	}

	@Test
	public void testGetMapNames() throws JMSException
	{
		message.setString("test", "value");
		message.setString("test2", "value2");
		
		Set<String> keys = new HashSet<String>();
		Enumeration<?> e = message.getMapNames();
		while (e.hasMoreElements())
		{
			keys.add((String) e.nextElement());
		}
		assertEquals(2, keys.size());
		assertTrue(keys.contains("test"));
		assertTrue(keys.contains("test2"));
	}

	@Test
	public void testGetObject() throws JMSException
	{
		byte[] src = new byte[] { 1 };
		message.setString("test", null);
		message.setBoolean("test2", true);
		message.setByte("test3", (byte) 3);
		message.setChar("test4", 'A');
		message.setShort("test5", (short) 5);
		message.setInt("test6", 6);
		message.setLong("test7", 7L);
		message.setFloat("test8", 8.0f);
		message.setDouble("test9", 9.0d);
		message.setBytes("test10", src);
		assertEquals(null, message.getObject("test"));
		assertEquals(Boolean.TRUE, message.getObject("test2"));
		assertEquals(Byte.valueOf((byte) 3), message.getObject("test3"));
		assertEquals(Character.valueOf('A'), message.getObject("test4"));
		assertEquals(Short.valueOf((short) 5), message.getObject("test5"));
		assertEquals(Integer.valueOf(6), message.getObject("test6"));
		assertEquals(Long.valueOf(7L), message.getObject("test7"));
		assertEquals(Float.valueOf(8.0f), message.getObject("test8"));
		assertEquals(Double.valueOf(9.0d), message.getObject("test9"));
		byte[] dst = (byte[]) message.getObject("test10");
		assertNotSame(src, dst);
		assertEquals(1, dst.length);
		assertEquals((byte) 1, dst[0]);
	}

	@Test
	public void testGetShort() throws JMSException
	{
		message.setByte("test", (byte) 1);
		message.setShort("test2", (short) 2);
		message.setString("test3", "3");
		assertEquals((short) 1, message.getShort("test"));
		assertEquals((short) 2, message.getShort("test2"));
		assertEquals((short) 3, message.getShort("test3"));
	}

	@Test(expected=NumberFormatException.class)
	public void testGetShortNull() throws JMSException
	{
		message.getShort("test");
	}

	@Test
	public void testGetString() throws JMSException
	{
		message.setString("test", null);
		message.setBoolean("test2", true);
		message.setByte("test3", (byte) 3);
		message.setChar("test4", 'A');
		message.setShort("test5", (short) 5);
		message.setInt("test6", 6);
		message.setLong("test7", 7L);
		message.setFloat("test8", 8.0f);
		message.setDouble("test9", 9.0d);
		assertEquals(null, message.getString("test"));
		assertEquals("true", message.getString("test2"));
		assertEquals("3", message.getString("test3"));
		assertEquals("A", message.getString("test4"));
		assertEquals("5", message.getString("test5"));
		assertEquals("6", message.getString("test6"));
		assertEquals("7", message.getString("test7"));
		assertEquals("8.0", message.getString("test8"));
		assertEquals("9.0", message.getString("test9"));
	}

	@Test(expected=MessageFormatException.class)
	public void testGetStringInvalid() throws JMSException
	{
		message.setBytes("test", new byte[] { 1 });
		message.getString("test");
	}
	
	@Test
	public void testItemExists() throws JMSException
	{
		message.setString("test", "value");
		assertTrue(message.itemExists("test"));
		assertFalse(message.itemExists("test2"));
	}

	@Test
	public void testSetBoolean() throws JMSException
	{
		message.setBoolean("test", true);
		assertTrue(message.getBoolean("test"));
	}

	@Test(expected=MessageNotWriteableException.class)
	public void testSetBooleanReadOnly() throws JMSException
	{
		message.setReadOnly(true);
		message.setBoolean("test", true);
	}

	@Test(expected=IllegalArgumentException.class)
	public void testSetBooleanNullName() throws JMSException
	{
		message.setBoolean(null, true);
	}

	@Test(expected=IllegalArgumentException.class)
	public void testSetBooleanEmptyName() throws JMSException
	{
		message.setBoolean("", true);
	}

	@Test
	public void testSetByte() throws JMSException
	{
		message.setByte("test", Byte.MAX_VALUE);
		assertEquals(Byte.MAX_VALUE, message.getByte("test"));
	}

	@Test(expected=MessageNotWriteableException.class)
	public void testSetByteReadOnly() throws JMSException
	{
		message.setReadOnly(true);
		message.setByte("test", Byte.MAX_VALUE);
	}

	@Test(expected=IllegalArgumentException.class)
	public void testSetByteNullName() throws JMSException
	{
		message.setByte(null, Byte.MAX_VALUE);
	}

	@Test(expected=IllegalArgumentException.class)
	public void testSetByteEmptyName() throws JMSException
	{
		message.setByte("", Byte.MAX_VALUE);
	}

	@Test
	public void testSetBytes() throws JMSException
	{
		message.setBytes("test", new byte[] { 1, 2 });
		byte[] result = message.getBytes("test");
		assertNotNull(result);
		assertEquals(2, result.length);
		assertEquals(1, result[0]);
		assertEquals(2, result[1]);
	}

	@Test(expected=MessageNotWriteableException.class)
	public void testSetBytesReadOnly() throws JMSException
	{
		message.setReadOnly(true);
		message.setBytes("test", new byte[] { 1, 2 });
	}

	@Test
	public void testSetBytesPartial() throws JMSException
	{
		message.setBytes("test", new byte[] { 1, 2 }, 1, 1);
		byte[] result = message.getBytes("test");
		assertNotNull(result);
		assertEquals(1, result.length);
		assertEquals(2, result[0]);
	}

	@Test(expected=MessageNotWriteableException.class)
	public void testSetBytesPartialReadOnly() throws JMSException
	{
		message.setReadOnly(true);
		message.setBytes("test", new byte[] { 1, 2 }, 1, 1);
	}

	@Test
	public void testSetChar() throws JMSException
	{
		message.setChar("test", 'A');
		assertEquals('A', message.getChar("test"));
	}

	@Test(expected=MessageNotWriteableException.class)
	public void testSetCharReadOnly() throws JMSException
	{
		message.setReadOnly(true);
		message.setChar("test", 'A');
	}

	@Test
	public void testSetDouble() throws JMSException
	{
		message.setDouble("test", 1.0d);
		assertEquals(1.0d, message.getDouble("test"), 0.01d);
	}

	@Test(expected=MessageNotWriteableException.class)
	public void testSetDoubleReadOnly() throws JMSException
	{
		message.setReadOnly(true);
		message.setDouble("test", 1.0d);
	}

	@Test
	public void testSetFloat() throws JMSException
	{
		message.setFloat("test", 1.0f);
		assertEquals(1.0f, message.getFloat("test"), 0.01f);
	}

	@Test(expected=MessageNotWriteableException.class)
	public void testSetFloatReadOnly() throws JMSException
	{
		message.setReadOnly(true);
		message.setFloat("test", 1.0f);
	}

	@Test
	public void testSetInt() throws JMSException
	{
		message.setInt("test", 1);
		assertEquals(1, message.getInt("test"));
	}

	@Test(expected=MessageNotWriteableException.class)
	public void testSetIntReadOnly() throws JMSException
	{
		message.setReadOnly(true);
		message.setInt("test", 1);
	}

	@Test
	public void testSetLong() throws JMSException
	{
		message.setLong("test", 1L);
		assertEquals(1L, message.getLong("test"));
	}

	@Test(expected=MessageNotWriteableException.class)
	public void testSetLongReadOnly() throws JMSException
	{
		message.setReadOnly(true);
		message.setLong("test", 1L);
	}

	@Test
	public void testSetObject() throws JMSException
	{
		message.setObject("test", null);
		message.setObject("test2", true);
		message.setObject("test3", (byte) 3);
		message.setObject("test4", 'A');
		message.setObject("test5", (short) 5);
		message.setObject("test6", 6);
		message.setObject("test7", 7L);
		message.setObject("test8", 8.0f);
		message.setObject("test9", 9.0d);
		message.setObject("test10", new byte[] { 1 });
		assertNull(message.getObject("test"));
		assertTrue(message.getBoolean("test2"));
		assertEquals((byte) 3, message.getByte("test3"));
		assertEquals('A', message.getChar("test4"));
		assertEquals((short) 5, message.getShort("test5"));
		assertEquals(6, message.getInt("test6"));
		assertEquals(7L, message.getLong("test7"));
		assertEquals(8.0f, message.getFloat("test8"), 0.01f);
		assertEquals(9.0d, message.getDouble("test9"), 0.01d);
		byte[] dst = message.getBytes("test10");
		assertNotNull(dst);
		assertEquals(1, dst.length);
		assertEquals(1, dst[0]);
	}

	@Test(expected=MessageFormatException.class)
	public void testSetObjectInvalid() throws JMSException
	{
		message.setObject("test", new Object());
	}

	@Test(expected=MessageNotWriteableException.class)
	public void testSetObjectReadOnly() throws JMSException
	{
		message.setReadOnly(true);
		message.setObject("test", "value");
	}

	@Test
	public void testSetShort() throws JMSException
	{
		message.setShort("test", (short) 1);
		assertEquals((short) 1, message.getShort("test"));
	}

	@Test(expected=MessageNotWriteableException.class)
	public void testSetShortReadOnly() throws JMSException
	{
		message.setReadOnly(true);
		message.setShort("test", (short) 1);
	}

	@Test
	public void testSetString() throws JMSException
	{
		message.setString("test", "value");
		assertEquals("value", message.getString("test"));
	}

	@Test(expected=MessageNotWriteableException.class)
	public void testSetStringReadOnly() throws JMSException
	{
		message.setReadOnly(true);
		message.setString("test", "value");
	}

}
