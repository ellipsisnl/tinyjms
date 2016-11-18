package nl.ellipsis.tpjms.core.session;

import static org.junit.Assert.*;

import java.io.Serializable;

import javax.jms.*;

import nl.ellipsis.tpjms.core.connection.TPJMSConnection;
import nl.ellipsis.tpjms.core.connection.TPJMSConnectionFactory;
import nl.ellipsis.tpjms.core.destination.TPJMSQueue;
import nl.ellipsis.tpjms.core.destination.TPJMSTopic;
import nl.ellipsis.tpjms.core.message.TPJMSBytesMessage;
import nl.ellipsis.tpjms.core.message.TPJMSMapMessage;
import nl.ellipsis.tpjms.core.message.TPJMSMessage;
import nl.ellipsis.tpjms.core.message.TPJMSObjectMessage;
import nl.ellipsis.tpjms.core.message.TPJMSStreamMessage;
import nl.ellipsis.tpjms.core.message.TPJMSTextMessage;
import nl.ellipsis.tpjms.core.session.TPJMSSession;
import nl.ellipsis.tpjms.core.session.TPJMSTemporaryQueue;
import nl.ellipsis.tpjms.core.session.TPJMSTemporaryTopic;
import nl.ellipsis.tpjms.provider.vm.VmProvider;

import org.junit.*;

public class TPJMSSessionTest
{
	private TPJMSConnection con;
	private TPJMSSession session;
	
	@Before
	public void setUp() throws Exception
	{
		VmProvider.getInstance().removeBroker("test");

		TPJMSConnectionFactory factory = new TPJMSConnectionFactory("vm://test");
		con = (TPJMSConnection) factory.createConnection();		
		session = (TPJMSSession) con.createSession(false, Session.AUTO_ACKNOWLEDGE);
	}
	
	@After
	public void tearDown() throws Exception
	{
		session.close();
		session = null;
		con.close();
		con = null;
		
		VmProvider.getInstance().removeBroker("test");
	}
	
	@Test
	public void testClose() throws JMSException
	{
		session.close();
	}
	
	@Test
	public void testCommit()
	{
		fail("Not yet implemented");
	}
	
	@Test
	public void testCreateBrowserQueue()
	{
		fail("Not yet implemented");
	}
	
	@Test
	public void testCreateBrowserQueueString()
	{
		fail("Not yet implemented");
	}
	
	@Test
	public void testCreateBytesMessage() throws JMSException
	{
		BytesMessage message = session.createBytesMessage();
		assertNotNull(message);
		assertTrue(message instanceof TPJMSBytesMessage);
	}
	
	@Test
	public void testCreateConsumerDestination()
	{
		fail("Not yet implemented");
	}
	
	@Test
	public void testCreateConsumerDestinationString()
	{
		fail("Not yet implemented");
	}
	
	@Test
	public void testCreateConsumerDestinationStringBoolean()
	{
		fail("Not yet implemented");
	}
	
	@Test
	public void testCreateDurableSubscriberTopicString()
	{
		fail("Not yet implemented");
	}
	
	@Test
	public void testCreateDurableSubscriberTopicStringStringBoolean()
	{
		fail("Not yet implemented");
	}
	
	@Test
	public void testCreateMapMessage() throws JMSException
	{
		MapMessage message = session.createMapMessage();
		assertNotNull(message);
		assertTrue(message instanceof TPJMSMapMessage);
	}
	
	@Test
	public void testCreateMessage() throws JMSException
	{
		Message message = session.createMessage();
		assertNotNull(message);
		assertTrue(message instanceof TPJMSMessage);
	}
	
	@Test
	public void testCreateObjectMessage() throws JMSException
	{
		ObjectMessage message = session.createObjectMessage();
		assertNotNull(message);
		assertTrue(message instanceof TPJMSObjectMessage);
		assertNull(message.getObject());
	}
	
	@Test
	public void testCreateObjectMessageWithData() throws JMSException
	{
		Serializable obj = new String("TEST");
		ObjectMessage message = session.createObjectMessage(obj);
		assertNotNull(message);
		assertTrue(message instanceof TPJMSObjectMessage);
		assertSame(obj, message.getObject());
	}
	
	@Test
	public void testCreateProducer() throws JMSException
	{
		Queue queue = session.createQueue("TEST-QUEUE");
		MessageProducer prod = null;

		try
		{
			prod = session.createProducer(queue);
			assertNotNull(prod);
			assertSame(queue, prod.getDestination());
		}
		finally
		{
			if (prod != null)
				prod.close();
		}
	}

	@Test
	public void testCreateProducerNoDestination() throws JMSException
	{
		MessageProducer prod = null;

		try
		{
			prod = session.createProducer(null);
			assertNotNull(prod);
			assertNull(prod.getDestination());
		}
		finally
		{
			if (prod != null)
				prod.close();
		}
	}

	@Test
	public void testCreateQueue() throws JMSException
	{
		Queue queue = session.createQueue("TEST-QUEUE");
		assertNotNull(queue);
		assertTrue(queue instanceof TPJMSQueue);
		assertEquals("TEST-QUEUE", queue.getQueueName());
	}
	
	@Test
	public void testCreateStreamMessage() throws JMSException
	{
		StreamMessage message = session.createStreamMessage();
		assertNotNull(message);
		assertTrue(message instanceof TPJMSStreamMessage);
	}
	
	@Test
	public void testCreateTemporaryQueue() throws JMSException
	{
		TemporaryQueue queue = null;
		try
		{
			queue = session.createTemporaryQueue();
			assertNotNull(queue);
			assertTrue(queue instanceof TPJMSTemporaryQueue);
			assertNotNull(queue.getQueueName());
		}
		finally
		{
			if (queue != null)
				queue.delete();
		}
	}
	
	@Test
	public void testCreateTemporaryTopic() throws JMSException
	{
		TemporaryTopic topic = null;
		try
		{
			topic = session.createTemporaryTopic();
			assertNotNull(topic);
			assertTrue(topic instanceof TPJMSTemporaryTopic);
			assertNotNull(topic.getTopicName());
		}
		finally
		{
			if (topic != null)
				topic.delete();
		}
	}
	
	@Test
	public void testCreateTextMessage() throws JMSException
	{
		TextMessage message = session.createTextMessage();
		assertNotNull(message);
		assertTrue(message instanceof TPJMSTextMessage);
		assertNull(message.getText());
	}
	
	@Test
	public void testCreateTextMessageWithBody() throws JMSException
	{
		TextMessage message = session.createTextMessage("TEXT");
		assertNotNull(message);
		assertTrue(message instanceof TPJMSTextMessage);
		assertEquals("TEXT", message.getText());
	}
	
	@Test
	public void testCreateTopic() throws JMSException
	{
		Topic topic = session.createTopic("TOPIC-NAME");
		assertNotNull(topic);
		assertTrue(topic instanceof TPJMSTopic);
		assertEquals("TOPIC-NAME", topic.getTopicName());
	}
	
	@Test
	public void testGetAcknowledgeMode() throws JMSException
	{
		assertEquals(Session.AUTO_ACKNOWLEDGE, session.getAcknowledgeMode());
		session.close();
		session = (TPJMSSession) con.createSession(true, Session.AUTO_ACKNOWLEDGE);
		assertEquals(Session.SESSION_TRANSACTED, session.getAcknowledgeMode());
	}
	
	@Test
	public void testGetMessageListener()
	{
		fail("Not yet implemented");
	}
	
	@Test
	public void testGetTransacted() throws JMSException
	{
		assertFalse(session.getTransacted());		
		session.close();
		session = (TPJMSSession) con.createSession(true, Session.SESSION_TRANSACTED);
		assertTrue(session.getTransacted());
	}
	
	@Test
	public void testRecover()
	{
		fail("Not yet implemented");
	}
	
	@Test
	public void testRollback()
	{
		fail("Not yet implemented");
	}
	
	@Test
	public void testRun()
	{
		session.run();
	}
	
	@Test
	public void testSetMessageListener()
	{
		fail("Not yet implemented");
	}
	
	@Test
	public void testUnsubscribe()
	{
		fail("Not yet implemented");
	}
	
}
