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

public class TPJMSSessionTest {
	private TPJMSConnection con;
	private TPJMSSession session;
	private TPJMSQueue queue;
	private TPJMSTopic topic;
	
	private final static String QUEUE_NAME = "test-queue";
	
	class MyMessageListener implements MessageListener {

		@Override
		public void onMessage(Message message) {
			System.out.println(message.toString());	
		}
	}
	
	@Before
	public void setUp() throws Exception {
		VmProvider.getInstance().removeBroker("test");

		TPJMSConnectionFactory factory = new TPJMSConnectionFactory("vm://test");
		con = (TPJMSConnection) factory.createConnection();
		session = (TPJMSSession) con.createSession(false,Session.AUTO_ACKNOWLEDGE);
		queue = (TPJMSQueue) session.createQueue(QUEUE_NAME);
		topic = (TPJMSTopic) session.createTopic("test-topic");
	}

	@After
	public void tearDown() throws Exception {
		if(session.isOpen()) {
			session.close();
		}
		session = null;
		con.close();
		con = null;

		VmProvider.getInstance().removeBroker("test");
	}

	@Test(expected = JMSException.class)
	public void testClose() throws JMSException {
		// first time session will be closed
		try {
			session.close();
		} catch(Exception e) {
			assertTrue(e.getMessage(),false);
		}
		// second time error as session is already closed
		session.close();
	}

	@Test
	public void testCommit() {
		fail("Not yet implemented");
	}

	@Test
	public void testCreateBrowserQueue() throws JMSException {
		QueueBrowser browser = session.createBrowser(queue);
		assertNotNull(browser);
		assertTrue(browser instanceof TPJMSQueueBrowser);
	}

	@Test
	public void testCreateBrowserQueueString() throws JMSException {
		String messageSelector = "a-selector";
		QueueBrowser browser = session.createBrowser(queue,messageSelector);
		assertNotNull(browser);
		assertTrue(browser instanceof TPJMSQueueBrowser);
		assertEquals(browser.getMessageSelector(),messageSelector);
	}

	@Test
	public void testCreateBytesMessage() throws JMSException {
		BytesMessage message = session.createBytesMessage();
		assertNotNull(message);
		assertTrue(message instanceof TPJMSBytesMessage);
	}

	@Test
	public void testCreateConsumerDestination() throws JMSException {
		MessageConsumer consumer = session.createConsumer(queue);
		assertNotNull(consumer);
		assertTrue(consumer instanceof TPJMSMessageConsumer);
	}

	@Test
	public void testCreateConsumerDestinationString() throws JMSException {
		String messageSelector = "a-selector";
		MessageConsumer consumer = session.createConsumer(queue,messageSelector);
		assertNotNull(consumer);
		assertTrue(consumer instanceof TPJMSMessageConsumer);
		assertEquals(messageSelector,consumer.getMessageSelector());
	}

	@Test
	public void testCreateConsumerDestinationStringBoolean() throws JMSException {
		String messageSelector = "a-selector";
		boolean noLocal = false;
		MessageConsumer consumer = session.createConsumer(queue,messageSelector,noLocal);
		assertNotNull(consumer);
		assertTrue(consumer instanceof TPJMSMessageConsumer);
		assertEquals(messageSelector,consumer.getMessageSelector());
		assertEquals(noLocal,((TPJMSMessageConsumer)consumer).getNoLocal());
	}

	@Test
	public void testCreateDurableSubscriberTopicString() throws JMSException {
		String subscriberName = "a-subscriberName";
		TopicSubscriber subscriber = session.createDurableSubscriber(topic, subscriberName);
		assertEquals(subscriber.getTopic(),topic);
		assertNotNull(subscriber);
		assertTrue(subscriber instanceof TPJMSTopicSubscriber);
		assertEquals(subscriberName,((TPJMSTopicSubscriber) subscriber).getName());
	}

	@Test
	public void testCreateDurableSubscriberTopicStringStringBoolean() throws JMSException {
		String subscriberName = "a-subscriberName";
		String messageSelector = "a-selector";
		boolean noLocal = false;
		TopicSubscriber subscriber = session.createDurableSubscriber(topic,subscriberName,messageSelector,noLocal);
		assertEquals(subscriber.getTopic(),topic);
		assertNotNull(subscriber);
		assertEquals(messageSelector,subscriber.getMessageSelector());
		assertTrue(subscriber instanceof TPJMSTopicSubscriber);
		assertEquals(subscriberName,((TPJMSTopicSubscriber) subscriber).getName());
		assertEquals(noLocal,((TPJMSTopicSubscriber)subscriber).getNoLocal());
	}

	@Test
	public void testCreateMapMessage() throws JMSException {
		MapMessage message = session.createMapMessage();
		assertNotNull(message);
		assertTrue(message instanceof TPJMSMapMessage);
	}

	@Test
	public void testCreateMessage() throws JMSException {
		Message message = session.createMessage();
		assertNotNull(message);
		assertTrue(message instanceof TPJMSMessage);
	}

	@Test
	public void testCreateObjectMessage() throws JMSException {
		ObjectMessage message = session.createObjectMessage();
		assertNotNull(message);
		assertTrue(message instanceof TPJMSObjectMessage);
		assertNull(message.getObject());
	}

	@Test
	public void testCreateObjectMessageWithData() throws JMSException {
		Serializable obj = new String("TEST");
		ObjectMessage message = session.createObjectMessage(obj);
		assertNotNull(message);
		assertTrue(message instanceof TPJMSObjectMessage);
		assertEquals(obj, message.getObject());
	}

	@Test
	public void testCreateProducer() throws JMSException {
		Queue queue = session.createQueue(QUEUE_NAME);
		MessageProducer prod = null;

		try {
			prod = session.createProducer(queue);
			assertNotNull(prod);
			assertSame(queue, prod.getDestination());
		} finally {
			if (prod != null)
				prod.close();
		}
	}

	@Test
	public void testCreateProducerNoDestination() throws JMSException {
		MessageProducer prod = null;

		try {
			prod = session.createProducer(null);
			assertNotNull(prod);
			assertNull(prod.getDestination());
		} finally {
			if (prod != null)
				prod.close();
		}
	}

	@Test
	public void testCreateQueue() throws JMSException {
		Queue queue = session.createQueue(QUEUE_NAME);
		assertNotNull(queue);
		assertTrue(queue instanceof TPJMSQueue);
		assertEquals(QUEUE_NAME, queue.getQueueName());
	}

	@Test
	public void testCreateStreamMessage() throws JMSException {
		StreamMessage message = session.createStreamMessage();
		assertNotNull(message);
		assertTrue(message instanceof TPJMSStreamMessage);
	}

	@Test
	public void testCreateTemporaryQueue() throws JMSException {
		TemporaryQueue queue = null;
		try {
			queue = session.createTemporaryQueue();
			assertNotNull(queue);
			assertTrue(queue instanceof TPJMSTemporaryQueue);
			assertNotNull(queue.getQueueName());
		} finally {
			if (queue != null)
				queue.delete();
		}
	}

	@Test
	public void testCreateTemporaryTopic() throws JMSException {
		TemporaryTopic topic = null;
		try {
			topic = session.createTemporaryTopic();
			assertNotNull(topic);
			assertTrue(topic instanceof TPJMSTemporaryTopic);
			assertNotNull(topic.getTopicName());
		} finally {
			if (topic != null)
				topic.delete();
		}
	}

	@Test
	public void testCreateTextMessage() throws JMSException {
		TextMessage message = session.createTextMessage();
		assertNotNull(message);
		assertTrue(message instanceof TPJMSTextMessage);
		assertNull(message.getText());
	}

	@Test
	public void testCreateTextMessageWithBody() throws JMSException {
		TextMessage message = session.createTextMessage("TEXT");
		assertNotNull(message);
		assertTrue(message instanceof TPJMSTextMessage);
		assertEquals("TEXT", message.getText());
	}

	@Test
	public void testCreateTopic() throws JMSException {
		Topic topic = session.createTopic("TOPIC-NAME");
		assertNotNull(topic);
		assertTrue(topic instanceof TPJMSTopic);
		assertEquals("TOPIC-NAME", topic.getTopicName());
	}

	@Test
	public void testGetAcknowledgeMode() throws JMSException {
		assertEquals(Session.AUTO_ACKNOWLEDGE, session.getAcknowledgeMode());
		session.close();
		session = (TPJMSSession) con.createSession(true,
				Session.AUTO_ACKNOWLEDGE);
		assertEquals(Session.SESSION_TRANSACTED, session.getAcknowledgeMode());
	}

	@Test(expected = UnsupportedOperationException.class)
	public void testGetMessageListener() throws JMSException {
		assertNull(session.getMessageListener());
		MyMessageListener listener = new MyMessageListener();
		session.setMessageListener(listener);
		assertEquals(listener, session.getMessageListener());
	}

	@Test
	public void testGetTransacted() throws JMSException {
		assertFalse(session.getTransacted());
		session.close();
		session = (TPJMSSession) con.createSession(true,
				Session.SESSION_TRANSACTED);
		assertTrue(session.getTransacted());
	}

	@Test
	public void testRecover() {
		fail("Not yet implemented");
	}

	@Test
	public void testRollback() {
		fail("Not yet implemented");
	}

	@Test
	public void testRun() {
		session.run();
	}

	@Test(expected = UnsupportedOperationException.class)
	public void testSetMessageListener() throws JMSException {
		assertNull(session.getMessageListener());
		MyMessageListener listener = new MyMessageListener();
		session.setMessageListener(listener);
		assertEquals(listener, session.getMessageListener());
	}

	@Test
	public void testUnsubscribe() throws JMSException {
		fail("Not yet implemented");
	}

}
