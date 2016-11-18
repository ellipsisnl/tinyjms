package nl.ellipsis.tpjms.core.message;

import static org.junit.Assert.*;

import java.nio.charset.Charset;
import java.util.*;

import javax.jms.*;

import org.junit.*;

import nl.ellipsis.tpjms.core.destination.TPJMSQueue;
import nl.ellipsis.tpjms.core.message.TPJMSMessage;

public class TPJMSMessageTest
{
	private TPJMSMessage message;
	
	@Before
	public void setUp() throws Exception
	{
		message = new TPJMSMessage();
	}

	@After
	public void tearDown() throws Exception
	{
		message = null;
	}

	@Test
	public void testBody() throws JMSException
	{
		assertNull(message.getBody());
		message.setBody(new byte[] { (byte) 0 });
		assertNull(message.getBody());
	}

	@Test
	public void testAcknowledge() throws JMSException
	{
		message.acknowledge();
		fail("Not sure how to verify");
	}

	@Test
	public void testClearBody() throws JMSException
	{
		message.clearBody();
	}

	@Test
	public void testClearProperties() throws JMSException
	{
		message.setStringProperty("test", "value");
		message.setPropertiesReadOnly(true);
		message.clearProperties();
		assertNull(message.getStringProperty("test"));
		message.setStringProperty("test", "value");
		assertEquals("value", message.getStringProperty("test"));
	}

	@Test(expected=MessageNotWriteableException.class)
	public void testSetPropertiesReadOnly() throws JMSException
	{
		message.setPropertiesReadOnly(true);
		message.setStringProperty("test", "value");
	}
	
	@Test
	public void testBooleanProperty() throws JMSException
	{
		message.setBooleanProperty("test", true);
		assertTrue(message.getBooleanProperty("test"));
		
		message.setStringProperty("test2", "true");
		assertTrue(message.getBooleanProperty("test2"));
	}

	@Test
	public void testBooleanPropertyNull() throws JMSException
	{
		message.setStringProperty("test", null);
		assertFalse(message.getBooleanProperty("test"));
	}

	@Test(expected=MessageNotWriteableException.class)
	public void testBooleanPropertyReadOnly() throws JMSException
	{
		message.setPropertiesReadOnly(true);
		message.setBooleanProperty("test", Boolean.TRUE);
	}

	@Test
	public void testByteProperty() throws JMSException
	{
		message.setByteProperty("test", (byte) 123);
		assertEquals((byte) 123, message.getByteProperty("test"));
		
		message.setStringProperty("test2", "127");
		assertEquals((byte) 127, message.getByteProperty("test2"));

		message.setStringProperty("test3", "-128");
		assertEquals((byte) -128, message.getByteProperty("test3"));
	}

	@Test(expected=NumberFormatException.class)
	public void testBytePropertyNull() throws JMSException
	{
		message.setStringProperty("test", null);
		message.getByteProperty("test");
	}

	@Test(expected=MessageNotWriteableException.class)
	public void testBytePropertyReadOnly() throws JMSException
	{
		message.setPropertiesReadOnly(true);
		message.setByteProperty("test", Byte.MAX_VALUE);
	}

	@Test
	public void testDoubleProperty() throws JMSException
	{
		message.setDoubleProperty("test", 1.5d);
		assertEquals(1.5d, message.getDoubleProperty("test"), 0.01d);
		
		message.setFloatProperty("test2", 1.5f);
		assertEquals(1.5d, message.getDoubleProperty("test2"), 0.01d);

		message.setStringProperty("test3", "123.45");
		assertEquals(123.45d, message.getDoubleProperty("test3"), 0.01d);
	}

	@Test(expected=NullPointerException.class)
	public void testDoublePropertyNull() throws JMSException
	{
		message.setStringProperty("test", null);
		message.getDoubleProperty("test");
	}

	@Test(expected=MessageNotWriteableException.class)
	public void testDoublePropertyReadOnly() throws JMSException
	{
		message.setPropertiesReadOnly(true);
		message.setDoubleProperty("test", Double.MAX_VALUE);
	}

	@Test
	public void testFloatProperty() throws JMSException
	{
		message.setFloatProperty("test2", 1.5f);
		assertEquals(1.5f, message.getFloatProperty("test2"), 0.01f);

		message.setStringProperty("test3", "123.45");
		assertEquals(123.45f, message.getFloatProperty("test3"), 0.01f);
	}

	@Test(expected=NullPointerException.class)
	public void testFloatPropertyNull() throws JMSException
	{
		message.setStringProperty("test", null);
		message.getFloatProperty("test");
	}

	@Test(expected=MessageNotWriteableException.class)
	public void testFloatPropertyReadOnly() throws JMSException
	{
		message.setPropertiesReadOnly(true);
		message.setFloatProperty("test", Float.MAX_VALUE);
	}

	@Test
	public void testJMSCorrelationID() throws JMSException
	{
		assertNull(message.getJMSCorrelationID());
		message.setJMSCorrelationID("CORRELATION-ID");
		assertEquals("CORRELATION-ID", message.getJMSCorrelationID());
	}

	@Test
	public void testJMSCorrelationIDAsBytes() throws JMSException
	{
		assertNull(message.getJMSCorrelationIDAsBytes());
		message.setJMSCorrelationID("CORRELATION-ID");
		
		byte[] expected = "CORRELATION-ID".getBytes(Charset.forName("UTF-8"));
		byte[] actual = message.getJMSCorrelationIDAsBytes();
		
		assertEquals(expected.length, actual.length);
		for (int i = 0; i < expected.length; i++)
		{
			assertEquals(expected[i], actual[i]);
		}
	}

	@Test(expected=UnsupportedOperationException.class)
	public void testJMSCorrelationIDAsBytesUnsupported()
	{
		message.setJMSCorrelationIDAsBytes(new byte[] { (byte) 0 });
	}

	@Test
	public void testJMSDeliveryMode() throws JMSException
	{
		assertEquals(Message.DEFAULT_DELIVERY_MODE, message.getJMSDeliveryMode());
		message.setJMSDeliveryMode(DeliveryMode.NON_PERSISTENT);
		assertEquals(DeliveryMode.NON_PERSISTENT, message.getJMSDeliveryMode());
	}

	@Test(expected=JMSException.class)
	public void testJMSDeliveryModeInvalid() throws JMSException
	{
		message.setJMSDeliveryMode(Integer.MAX_VALUE);
	}

	@Test
	public void testJMSDestination() throws JMSException
	{
		assertNull(message.getJMSDestination());
		TPJMSQueue queue = new TPJMSQueue("TEST");
		message.setJMSDestination(queue);
		assertSame(queue, message.getJMSDestination());
		message.setJMSDestination(null);
		assertNull(message.getJMSDestination());
	}

	@Test
	public void testJMSExpiration() throws JMSException
	{
		long time = System.currentTimeMillis() + 60000L;
		message.setJMSExpiration(time);
		assertEquals(time, message.getJMSExpiration());
	}

	@Test
	public void testJMSMessageID() throws JMSException
	{
		assertNull(message.getJMSMessageID());
		message.setJMSMessageID("ID:TEST");
		assertEquals("ID:TEST", message.getJMSMessageID());
	}

	@Test(expected=JMSException.class)
	public void testJMSMessageIDInvalid() throws JMSException
	{
		message.setJMSMessageID("TEST");
	}

	@Test
	public void testJMSPriority() throws JMSException
	{
		assertEquals(Message.DEFAULT_PRIORITY, message.getJMSPriority());
		message.setJMSPriority(1);
		assertEquals(1, message.getJMSPriority());
	}

	@Test(expected=JMSException.class)
	public void testJMSPriorityInvalidLow() throws JMSException
	{
		message.setJMSPriority(-1);
	}

	@Test(expected=JMSException.class)
	public void testJMSPriorityInvalidHigh() throws JMSException
	{
		message.setJMSPriority(10);
	}

	@Test
	public void testJMSRedelivered() throws JMSException
	{
		assertFalse(message.getJMSRedelivered());
		message.setJMSRedelivered(true);
		assertTrue(message.getJMSRedelivered());
	}

	@Test
	public void testJMSReplyTo() throws JMSException
	{
		assertNull(message.getJMSReplyTo());
		TPJMSQueue queue = new TPJMSQueue("TEST");
		message.setJMSReplyTo(queue);
		assertSame(queue, message.getJMSReplyTo());
		message.setJMSReplyTo(null);
		assertNull(message.getJMSReplyTo());
	}

	@Test
	public void testJMSTimestamp() throws JMSException
	{
		assertEquals(0L, message.getJMSTimestamp());
		long timestamp = System.currentTimeMillis();
		message.setJMSTimestamp(timestamp);
		assertEquals(timestamp, message.getJMSTimestamp());
	}

	@Test
	public void testJMSType() throws JMSException
	{
		assertNull(message.getJMSType());
		message.setJMSType("TEST");
		assertEquals("TEST", message.getJMSType());
	}

	@Test
	public void testLongProperty() throws JMSException
	{
		message.setLongProperty("test", 123L);
		assertEquals(123L, message.getLongProperty("test"));
		
		message.setIntProperty("test2", 123);
		assertEquals(123L, message.getLongProperty("test2"));
		
		message.setShortProperty("test3", (short) 123);
		assertEquals(123L, message.getLongProperty("test3"));
		
		message.setByteProperty("test4", (byte) 123);
		assertEquals(123L, message.getLongProperty("test4"));
		
		message.setStringProperty("test5", "123");
		assertEquals(123L, message.getLongProperty("test5"));
	}

	@Test(expected=NumberFormatException.class)
	public void testLongPropertyNull() throws JMSException
	{
		message.setStringProperty("test", null);
		message.getLongProperty("test");
	}

	@Test(expected=MessageNotWriteableException.class)
	public void testLongPropertyReadOnly() throws JMSException
	{
		message.setPropertiesReadOnly(true);
		message.setLongProperty("test", Long.MAX_VALUE);
	}

	@Test
	public void testIntProperty() throws JMSException
	{
		message.setIntProperty("test", 123);
		assertEquals(123, message.getIntProperty("test"));
		
		message.setShortProperty("test2", (short) 123);
		assertEquals(123, message.getIntProperty("test2"));
		
		message.setByteProperty("test3", (byte) 123);
		assertEquals(123, message.getIntProperty("test3"));
		
		message.setStringProperty("test4", "123");
		assertEquals(123, message.getIntProperty("test4"));
	}

	@Test(expected=NumberFormatException.class)
	public void testIntPropertyNull() throws JMSException
	{
		message.setStringProperty("test", null);
		message.getIntProperty("test");
	}

	@Test(expected=MessageNotWriteableException.class)
	public void testIntPropertyReadOnly() throws JMSException
	{
		message.setPropertiesReadOnly(true);
		message.setIntProperty("test", Integer.MAX_VALUE);
	}

	@Test
	public void testGetObjectProperty() throws JMSException
	{
		assertNull(message.getObjectProperty("test"));
		
		message.setStringProperty("test", "value");
		assertEquals("value", message.getObjectProperty("test"));
		
		message.setBooleanProperty("test", true);
		assertEquals(Boolean.TRUE, message.getObjectProperty("test"));
		
		message.setByteProperty("test", (byte) 123);
		assertEquals(Byte.valueOf((byte) 123), message.getObjectProperty("test"));
		
		message.setShortProperty("test", (short) 123);
		assertEquals(Short.valueOf((short) 123), message.getObjectProperty("test"));
		
		message.setIntProperty("test", 123);
		assertEquals(Integer.valueOf(123), message.getObjectProperty("test"));
		
		message.setLongProperty("test", 123L);
		assertEquals(Long.valueOf(123L), message.getObjectProperty("test"));

		message.setFloatProperty("test", 123.45f);
		assertEquals(Float.valueOf(123.45f), message.getObjectProperty("test"));

		message.setDoubleProperty("test", 123.45d);
		assertEquals(Double.valueOf(123.45d), message.getObjectProperty("test"));
	}

	@Test
	public void testSetObjectProperty() throws JMSException
	{
		message.setObjectProperty("test", "value");
		assertEquals("value", message.getStringProperty("test"));
		
		message.setObjectProperty("test", Boolean.TRUE);
		assertTrue(message.getBooleanProperty("test"));
		
		message.setObjectProperty("test", Byte.MAX_VALUE);
		assertEquals(Byte.MAX_VALUE, message.getByteProperty("test"));
		
		message.setObjectProperty("test", Short.MAX_VALUE);
		assertEquals(Short.MAX_VALUE, message.getShortProperty("test"));
		
		message.setObjectProperty("test", Integer.MAX_VALUE);
		assertEquals(Integer.MAX_VALUE, message.getIntProperty("test"));
		
		message.setObjectProperty("test", Long.MAX_VALUE);
		assertEquals(Long.MAX_VALUE, message.getLongProperty("test"));
		
		message.setObjectProperty("test", Double.MAX_VALUE);
		assertEquals(Double.MAX_VALUE, message.getDoubleProperty("test"), 0.01d);

		message.setObjectProperty("test", Float.MAX_VALUE);
		assertEquals(Float.MAX_VALUE, message.getFloatProperty("test"), 0.01f);
		
		message.setObjectProperty("test", null);
		assertNull(message.getStringProperty("test"));
	}

	@Test(expected=MessageNotWriteableException.class)
	public void testObjectPropertyReadOnly() throws JMSException
	{
		message.setPropertiesReadOnly(true);
		message.setObjectProperty("test", "value");
	}

	@Test(expected=MessageFormatException.class)
	public void testSetObjectPropertyInvalid() throws JMSException
	{
		message.setObjectProperty("test", 'X');
	}

	@Test
	public void testGetPropertyNames() throws JMSException
	{
		message.setStringProperty("test1", "value1");
		message.setStringProperty("test2", "value2");
		
		Set<String> keys = new HashSet<String>();
		Enumeration<?> e = message.getPropertyNames();
		while (e.hasMoreElements())
		{
			keys.add((String) e.nextElement());
		}
		
		assertEquals(2, keys.size());
		assertTrue(keys.contains("test1"));
		assertTrue(keys.contains("test2"));
	}

	@Test
	public void testShortProperty() throws JMSException
	{
		message.setShortProperty("test", (short) 123);
		assertEquals((short) 123, message.getShortProperty("test"));
		
		message.setByteProperty("test2", (byte) 123);
		assertEquals((short) 123, message.getShortProperty("test2"));
		
		message.setStringProperty("test3", "123");
		assertEquals((short) 123, message.getShortProperty("test3"));
	}

	@Test(expected=NumberFormatException.class)
	public void testShortPropertyNull() throws JMSException
	{
		message.setStringProperty("test", null);
		message.getShortProperty("test");
	}

	@Test(expected=MessageNotWriteableException.class)
	public void testShortPropertyReadOnly() throws JMSException
	{
		message.setPropertiesReadOnly(true);
		message.setShortProperty("test", Short.MAX_VALUE);
	}

	@Test
	public void testGetStringProperty() throws JMSException
	{
		assertNull(message.getStringProperty("test"));
		
		message.setStringProperty("test", "value");
		assertEquals("value", message.getStringProperty("test"));
		
		message.setBooleanProperty("test", true);
		assertEquals("true", message.getStringProperty("test"));
		
		message.setByteProperty("test", (byte) 123);
		assertEquals("123", message.getStringProperty("test"));
		
		message.setShortProperty("test", (short) 123);
		assertEquals("123", message.getStringProperty("test"));
		
		message.setIntProperty("test", 123);
		assertEquals("123", message.getStringProperty("test"));
		
		message.setLongProperty("test", 123L);
		assertEquals("123", message.getStringProperty("test"));

		message.setFloatProperty("test", 123.45f);
		assertEquals("123.45", message.getStringProperty("test"));

		message.setDoubleProperty("test", 123.45d);
		assertEquals("123.45", message.getStringProperty("test"));
	}

	@Test
	public void testPropertyExists() throws JMSException
	{
		assertFalse(message.propertyExists("test"));
		message.setStringProperty("test", "value");
		assertTrue(message.propertyExists("test"));
	}

	@Test(expected=MessageNotWriteableException.class)
	public void testStringPropertyReadOnly() throws JMSException
	{
		message.setPropertiesReadOnly(true);
		message.setStringProperty("test", "value");
	}

	@Test
	public void testSetStringProperty() throws JMSException
	{
		message.setStringProperty("test", "value");
		assertEquals("value", message.getStringProperty("test"));
	}

}
