package com.randomcoder.tinyjms.client;

import static org.junit.Assert.*;

import java.lang.reflect.Field;
import java.util.concurrent.atomic.AtomicReference;

import javax.jms.*;

import org.junit.*;

public class TinyJmsObjectMessageTest
{
	private TinyJmsObjectMessage message;
	
	@Before
	public void setUp()
	{
		message = new TinyJmsObjectMessage();
	}

	@After
	public void tearDown()
	{
		message = null;
	}

	@Test
	public void testNullSerialization() throws JMSException
	{
		message.setObject(null);
		assertNull(message.getObject());
	}

	@Test
	public void testObjectSerialization() throws JMSException
	{
		message.setObject("TEST");
		assertEquals("TEST", message.getObject());
	}
	
	@Test(expected=MessageNotWriteableException.class)
	public void testReadOnlySerialization() throws JMSException
	{
		message.setReadOnly(true);
		message.setObject("TEST");
	}

	@Test(expected=MessageFormatException.class)
	public void testInvalidSerialization() throws JMSException
	{
		message.setObject(new AtomicReference<Object>(new Object()));
	}

	@Test(expected=MessageFormatException.class)
	public void testInvalidDeserialization() throws Exception
	{
		Field data = message.getClass().getDeclaredField("data");
		data.setAccessible(true);
		data.set(message, new byte[] {});
		
		message.getObject();
	}

}