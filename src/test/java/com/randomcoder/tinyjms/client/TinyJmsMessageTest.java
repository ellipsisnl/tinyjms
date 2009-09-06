package com.randomcoder.tinyjms.client;

import static org.junit.Assert.*;

import javax.jms.JMSException;

import org.junit.*;

public class TinyJmsMessageTest
{
	private TinyJmsMessage message;
	
	@Before
	public void setUp() throws Exception
	{
		message = new TinyJmsMessage();
	}

	@After
	public void tearDown() throws Exception
	{
		message = null;
	}

	@Test
	public void testAcknowledge()
	{
		fail("Not yet implemented");
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
		message.clearProperties();
		assertNull(message.getStringProperty("test"));
	}

	@Test
	public void testGetBooleanProperty()
	{
		fail("Not yet implemented");
	}

	@Test
	public void testGetByteProperty()
	{
		fail("Not yet implemented");
	}

	@Test
	public void testGetDoubleProperty()
	{
		fail("Not yet implemented");
	}

	@Test
	public void testGetFloatProperty()
	{
		fail("Not yet implemented");
	}

	@Test
	public void testGetIntProperty()
	{
		fail("Not yet implemented");
	}

	@Test
	public void testGetJMSCorrelationID()
	{
		fail("Not yet implemented");
	}

	@Test
	public void testGetJMSCorrelationIDAsBytes()
	{
		fail("Not yet implemented");
	}

	@Test
	public void testGetJMSDeliveryMode()
	{
		fail("Not yet implemented");
	}

	@Test
	public void testGetJMSDestination()
	{
		fail("Not yet implemented");
	}

	@Test
	public void testGetJMSExpiration()
	{
		fail("Not yet implemented");
	}

	@Test
	public void testGetJMSMessageID()
	{
		fail("Not yet implemented");
	}

	@Test
	public void testGetJMSPriority()
	{
		fail("Not yet implemented");
	}

	@Test
	public void testGetJMSRedelivered()
	{
		fail("Not yet implemented");
	}

	@Test
	public void testGetJMSReplyTo()
	{
		fail("Not yet implemented");
	}

	@Test
	public void testGetJMSTimestamp()
	{
		fail("Not yet implemented");
	}

	@Test
	public void testGetJMSType()
	{
		fail("Not yet implemented");
	}

	@Test
	public void testGetLongProperty()
	{
		fail("Not yet implemented");
	}

	@Test
	public void testGetObjectProperty()
	{
		fail("Not yet implemented");
	}

	@Test
	public void testGetPropertyNames()
	{
		fail("Not yet implemented");
	}

	@Test
	public void testGetShortProperty()
	{
		fail("Not yet implemented");
	}

	@Test
	public void testGetStringProperty()
	{
		fail("Not yet implemented");
	}

	@Test
	public void testPropertyExists()
	{
		fail("Not yet implemented");
	}

	@Test
	public void testSetBooleanProperty()
	{
		fail("Not yet implemented");
	}

	@Test
	public void testSetByteProperty()
	{
		fail("Not yet implemented");
	}

	@Test
	public void testSetDoubleProperty()
	{
		fail("Not yet implemented");
	}

	@Test
	public void testSetFloatProperty()
	{
		fail("Not yet implemented");
	}

	@Test
	public void testSetIntProperty()
	{
		fail("Not yet implemented");
	}

	@Test
	public void testSetJMSCorrelationID()
	{
		fail("Not yet implemented");
	}

	@Test
	public void testSetJMSCorrelationIDAsBytes()
	{
		fail("Not yet implemented");
	}

	@Test
	public void testSetJMSDeliveryMode()
	{
		fail("Not yet implemented");
	}

	@Test
	public void testSetJMSDestination()
	{
		fail("Not yet implemented");
	}

	@Test
	public void testSetJMSExpiration()
	{
		fail("Not yet implemented");
	}

	@Test
	public void testSetJMSMessageID()
	{
		fail("Not yet implemented");
	}

	@Test
	public void testSetJMSPriority()
	{
		fail("Not yet implemented");
	}

	@Test
	public void testSetJMSRedelivered()
	{
		fail("Not yet implemented");
	}

	@Test
	public void testSetJMSReplyTo()
	{
		fail("Not yet implemented");
	}

	@Test
	public void testSetJMSTimestamp()
	{
		fail("Not yet implemented");
	}

	@Test
	public void testSetJMSType()
	{
		fail("Not yet implemented");
	}

	@Test
	public void testSetLongProperty()
	{
		fail("Not yet implemented");
	}

	@Test
	public void testSetObjectProperty()
	{
		fail("Not yet implemented");
	}

	@Test
	public void testSetShortProperty()
	{
		fail("Not yet implemented");
	}

	@Test
	public void testSetStringProperty()
	{
		fail("Not yet implemented");
	}

}
