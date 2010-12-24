package com.randomcoder.tinyjms.client;

import static org.junit.Assert.*;

import javax.jms.*;

import org.junit.*;

public class TinyJmsConnectionFactoryTest
{
	private TinyJmsConnectionFactory factory;
	
	@Before
	public void setUp() throws Exception
	{
		factory = new TinyJmsConnectionFactory();
		factory.setClientID("CLIENT-ID");
	}
	
	@After
	public void tearDown() throws Exception
	{
		factory = null;
	}
	
	@Test
	public void testCreateConnection() throws JMSException
	{
		Connection con = factory.createConnection();
		assertNotNull(con);
		assertTrue(con instanceof TinyJmsConnection);
		assertEquals("CLIENT-ID", con.getClientID());
	}
	
	@Test
	public void testCreateConnectionWithCredentials() throws JMSException
	{
		Connection con = factory.createConnection("user", "pass");
		assertNotNull(con);
		assertTrue(con instanceof TinyJmsConnection);
		assertEquals("CLIENT-ID", con.getClientID());
	}
	
	@Test
	public void testCreateQueueConnection() throws JMSException
	{
		QueueConnection con = factory.createQueueConnection();
		assertNotNull(con);
		assertTrue(con instanceof TinyJmsConnection);
		assertEquals("CLIENT-ID", con.getClientID());
	}
	
	@Test
	public void testCreateQueueConnectionWithCredentials() throws JMSException
	{
		QueueConnection con = factory.createQueueConnection("user", "pass");
		assertNotNull(con);
		assertTrue(con instanceof TinyJmsConnection);
		assertEquals("CLIENT-ID", con.getClientID());
	}
	
	@Test
	public void testCreateTopicConnection() throws JMSException
	{
		TopicConnection con = factory.createTopicConnection();
		assertNotNull(con);
		assertTrue(con instanceof TinyJmsConnection);
		assertEquals("CLIENT-ID", con.getClientID());
	}
	
	@Test
	public void testCreateTopicConnectionWithCredentials() throws JMSException
	{
		TopicConnection con = factory.createTopicConnection("user", "pass");
		assertNotNull(con);
		assertTrue(con instanceof TinyJmsConnection);
		assertEquals("CLIENT-ID", con.getClientID());
	}
	
}