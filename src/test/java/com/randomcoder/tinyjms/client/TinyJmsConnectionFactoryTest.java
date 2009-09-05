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
	}
	
	@Test
	public void testCreateConnectionWithCredentials() throws JMSException
	{
		Connection con = factory.createConnection("user", "pass");
		assertNotNull(con);
		assertTrue(con instanceof TinyJmsConnection);
	}
	
}
