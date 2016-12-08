package nl.ellipsis.tpjms.core.session;

import static org.junit.Assert.*;

import javax.jms.*;

import nl.ellipsis.tpjms.core.connection.TPJMSConnection;
import nl.ellipsis.tpjms.core.connection.TPJMSConnectionFactory;
import nl.ellipsis.tpjms.core.destination.TPJMSDestination;
import nl.ellipsis.tpjms.core.destination.TPJMSQueue;
import nl.ellipsis.tpjms.core.message.TPJMSTextMessage;
import nl.ellipsis.tpjms.core.session.TPJMSMessageProducer;
import nl.ellipsis.tpjms.core.session.TPJMSSession;
import nl.ellipsis.tpjms.provider.vm.*;
import nl.ellipsis.tpjms.util.AcknowledgeCallback;

import org.junit.*;

public class TPJMSMessageProducerTest {
	private TPJMSConnection con;
	private TPJMSSession session;
	private TPJMSQueue queue;
	private TPJMSMessageProducer prod;
	private TPJMSTextMessage message;
	
	private final static String QUEUE_NAME = "test-queue";
	
	private class AcknowledgeCallbackImpl implements AcknowledgeCallback {
		private boolean acknowledged = false;
		private MessageProducer producer;
		
		public AcknowledgeCallbackImpl(MessageProducer producer) {
			this.producer = producer;
			this.acknowledged = true;
		}
		
		@Override
		public void acknowledge(String messageID) throws JMSException {
			String producerID = null;
			String destinationID = null;
			if (producer!=null && producer instanceof TPJMSMessageProducer) {
				TPJMSMessageProducer jmsProducer = (TPJMSMessageProducer) producer;
				producerID = jmsProducer.getJMSMessageProducerID();
				Destination destination = jmsProducer.getDestination();
				destinationID = (destination instanceof TPJMSDestination ? ((TPJMSDestination) destination).getName() : null);
			}
			System.out.println("Message "+messageID+" from producer "+producerID+" arrived at destination "+destinationID);
			this.acknowledged = true;
		}
		
		public boolean isAcknowledged() {
			return acknowledged;
		}
		
		public void clear() {
			this.acknowledged = false;
		}
	}
	
	private AcknowledgeCallbackImpl acknowledgeCallback;

	@Before
	public void setUp() throws Exception {
		VmProvider.getInstance().removeBroker("test");

		TPJMSConnectionFactory factory = new TPJMSConnectionFactory("vm://test");
		con = (TPJMSConnection) factory.createConnection();
		session = (TPJMSSession) con.createSession(false, Session.AUTO_ACKNOWLEDGE);
		queue = (TPJMSQueue) session.createQueue(QUEUE_NAME);
		prod = (TPJMSMessageProducer) session.createProducer(queue);
		message = (TPJMSTextMessage) session.createTextMessage("BODY");
		
		acknowledgeCallback = new AcknowledgeCallbackImpl(prod);
		message.setAcknowledgeCallback(acknowledgeCallback);
	}

	@After
	public void tearDown() throws Exception {
		message = null;
		prod.close();
		prod = null;
		session.close();
		session = null;
		con.close();
		con = null;

		VmProvider.getInstance().removeBroker("test");
	}

	@Test
	public void testClose() throws JMSException {
		prod.close();
	}

	@Test
	public void testDeliveryMode() throws JMSException {
		assertEquals(Message.DEFAULT_DELIVERY_MODE, prod.getDeliveryMode());
		prod.setDeliveryMode(DeliveryMode.NON_PERSISTENT);
		assertEquals(DeliveryMode.NON_PERSISTENT, prod.getDeliveryMode());
		prod.setDeliveryMode(DeliveryMode.PERSISTENT);
		assertEquals(DeliveryMode.PERSISTENT, prod.getDeliveryMode());
	}

	@Test(expected = JMSException.class)
	public void testDeliveryModeInvalid() throws JMSException {
		prod.setDeliveryMode(Integer.MAX_VALUE);
	}

	@Test
	public void testGetDestination() {
		assertSame(queue, prod.getDestination());
	}

	@Test
	public void testDisableMessageID() {
		assertFalse(prod.getDisableMessageID());
		prod.setDisableMessageID(true);
		assertFalse(prod.getDisableMessageID());
	}

	@Test
	public void testDisableMessageTimestamp() {
		assertFalse(prod.getDisableMessageTimestamp());
		prod.setDisableMessageTimestamp(true);
		assertFalse(prod.getDisableMessageTimestamp());
	}

	@Test
	public void testPriority() throws JMSException {
		assertEquals(Message.DEFAULT_PRIORITY, prod.getPriority());
		prod.setPriority(9);
		assertEquals(9, prod.getPriority());
	}

	@Test(expected = JMSException.class)
	public void testPriorityLow() throws JMSException {
		prod.setPriority(-1);
	}

	@Test(expected = JMSException.class)
	public void testPriorityHigh() throws JMSException {
		prod.setPriority(10);
	}

	@Test
	public void testTimeToLive() throws JMSException {
		assertEquals(Message.DEFAULT_TIME_TO_LIVE, prod.getTimeToLive());
		prod.setTimeToLive(10000L);
		assertEquals(10000L, prod.getTimeToLive());
	}

	@Test(expected = JMSException.class)
	public void testTimeToLiveInvalid() throws JMSException {
		prod.setTimeToLive(-1L);
	}

	@Test
	public void testSendMessage() throws JMSException {
		acknowledgeCallback.clear();
		prod.send(message);
		// As Session.AUTO_ACKNOWLEDGE, producer should be notified through the callback 
		assertTrue(acknowledgeCallback.isAcknowledged());
	}

	@Test(expected = UnsupportedOperationException.class)
	public void testSendMessageWithDestinationUnsupported() throws JMSException {
		acknowledgeCallback.clear();
		prod.send(queue, message);
		assertFalse(acknowledgeCallback.isAcknowledged());
	}

	@Test
	public void testSendMessageWithDestination() throws JMSException {
		acknowledgeCallback.clear();
		prod.close();
		prod = (TPJMSMessageProducer) session.createProducer(null);
		prod.send(queue, message);
		// As Session.AUTO_ACKNOWLEDGE, producer should be notified through the callback 
		assertTrue(acknowledgeCallback.isAcknowledged());
	}

	@Test
	public void testSendMessageFull() throws JMSException {
		acknowledgeCallback.clear();
		prod.send(message, DeliveryMode.NON_PERSISTENT, 1, 60000L);
		// As Session.AUTO_ACKNOWLEDGE, producer should be notified through the callback 
		assertTrue(acknowledgeCallback.isAcknowledged());
	}

	@Test(expected = UnsupportedOperationException.class)
	public void testSendMessageWithDestinationFullUnsupported()
			throws JMSException {
		prod.send(queue, message, DeliveryMode.NON_PERSISTENT, 1, 60000L);
	}

	@Test
	public void testSendMessageWithDestinationFull() throws JMSException {
		acknowledgeCallback.clear();
		prod.close();
		prod = (TPJMSMessageProducer) session.createProducer(null);
		prod.send(queue, message, DeliveryMode.NON_PERSISTENT, 1, 60000L);
		// As Session.AUTO_ACKNOWLEDGE, producer should be notified through the callback 
		assertTrue(acknowledgeCallback.isAcknowledged());
	}

}
