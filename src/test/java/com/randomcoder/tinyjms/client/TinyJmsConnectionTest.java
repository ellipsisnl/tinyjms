package com.randomcoder.tinyjms.client;

import static org.junit.Assert.*;

import javax.jms.*;
import javax.jms.IllegalStateException;

import org.junit.*;

public class TinyJmsConnectionTest
{
	private TinyJmsConnection con;

	@Before
	public void setUp() throws Exception
	{
		con = (TinyJmsConnection) new TinyJmsConnectionFactory().createConnection();
	}

	@After
	public void tearDown()
	{
		con = null;
	}

	@Test
	public void testClose()
	{
		fail("Not yet implemented");
	}

	@Test
	public void testCreateConnectionConsumer()
	{
		fail("Not yet implemented");
	}

	@Test
	public void testCreateDurableConnectionConsumer()
	{
		fail("Not yet implemented");
	}

	@Test
	public void testCreateSession() throws JMSException
	{
		Session session = con.createSession(false, Session.AUTO_ACKNOWLEDGE);
		assertNotNull(session);
		assertTrue(session instanceof TinyJmsSession);
		assertFalse(session.getTransacted());
		assertEquals(Session.AUTO_ACKNOWLEDGE, session.getAcknowledgeMode());
	}

	@Test
	public void testCreateSessionTransacted() throws JMSException
	{
		Session session = con.createSession(true, Session.AUTO_ACKNOWLEDGE);
		assertNotNull(session);
		assertTrue(session instanceof TinyJmsSession);
		assertTrue(session.getTransacted());
		assertEquals(Session.SESSION_TRANSACTED, session.getAcknowledgeMode());
	}

	@Test(expected = JMSException.class)
	public void testCreateSessionConflict() throws JMSException
	{
		con.createSession(false, Session.SESSION_TRANSACTED);
	}

	@Test(expected = JMSException.class)
	public void testCreateSessionInvalid() throws JMSException
	{
		con.createSession(false, Integer.MAX_VALUE);
	}

	@Test
	public void testGetExceptionListener()
	{
		fail("Not yet implemented");
	}

	@Test
	public void testGetMetaData() throws JMSException
	{
		ConnectionMetaData meta = con.getMetaData();
		assertNotNull(meta);
		assertTrue(meta instanceof TinyJmsConnectionMetaData);
	}

	@Test
	public void testSetClientID() throws JMSException
	{
		con.setClientID("CLIENT-ID");
		assertEquals("CLIENT-ID", con.getClientID());
	}

	@Test(expected = IllegalStateException.class)
	public void testSetClientIDAlreadySet() throws JMSException
	{
		con.setClientID("CLIENT-ID");
		con.setClientID("CLIENT-ID-2");
	}

	@Test
	public void testSetExceptionListener()
	{
		fail("Not yet implemented");
	}

	@Test
	public void testStart()
	{
		fail("Not yet implemented");
	}

	@Test
	public void testStop()
	{
		fail("Not yet implemented");
	}

	@Test
	public void testCreateQueueSession() throws JMSException
	{
		QueueSession session = con.createQueueSession(false, Session.AUTO_ACKNOWLEDGE);
		assertNotNull(session);
		assertTrue(session instanceof TinyJmsSession);
		assertFalse(session.getTransacted());
		assertEquals(Session.AUTO_ACKNOWLEDGE, session.getAcknowledgeMode());
	}

	@Test
	public void testCreateTopicSession() throws JMSException
	{
		TopicSession session = con.createTopicSession(false, Session.AUTO_ACKNOWLEDGE);
		assertNotNull(session);
		assertTrue(session instanceof TinyJmsSession);
		assertFalse(session.getTransacted());
		assertEquals(Session.AUTO_ACKNOWLEDGE, session.getAcknowledgeMode());
	}

}
