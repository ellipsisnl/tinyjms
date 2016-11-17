package nl.ellipsis.tpjms.client;

import static org.junit.Assert.*;

import javax.jms.*;

import nl.ellipsis.tpjms.client.TPJMSConnection;
import nl.ellipsis.tpjms.client.TPJMSConnectionFactory;
import nl.ellipsis.tpjms.client.TPJMSMessageProducer;
import nl.ellipsis.tpjms.client.TPJMSQueue;
import nl.ellipsis.tpjms.client.TPJMSSession;
import nl.ellipsis.tpjms.client.TPJMSTextMessage;
import nl.ellipsis.tpjms.provider.vm.*;

import org.junit.*;

public class TinyJmsMessageProducerTest
{
	private TPJMSConnection con;
	private TPJMSSession session;
	private TPJMSQueue queue;
	private TPJMSMessageProducer prod;
	private TPJMSTextMessage message;
	
	@Before
	public void setUp() throws Exception
	{
		VmProvider.getInstance().removeBroker("test");

		TPJMSConnectionFactory factory = new TPJMSConnectionFactory("vm://test");
		con = (TPJMSConnection) factory.createConnection();
		session = (TPJMSSession) con.createSession(false, Session.AUTO_ACKNOWLEDGE);
		queue = (TPJMSQueue) session.createQueue("test-queue");
		prod = (TPJMSMessageProducer) session.createProducer(queue);
		message = (TPJMSTextMessage) session.createTextMessage("BODY");
	}

	@After
	public void tearDown() throws Exception
	{
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
	public void testClose() throws JMSException
	{
		prod.close();
	}

	@Test
	public void testDeliveryMode() throws JMSException
	{
		assertEquals(Message.DEFAULT_DELIVERY_MODE, prod.getDeliveryMode());
		prod.setDeliveryMode(DeliveryMode.NON_PERSISTENT);
		assertEquals(DeliveryMode.NON_PERSISTENT, prod.getDeliveryMode());
		prod.setDeliveryMode(DeliveryMode.PERSISTENT);
		assertEquals(DeliveryMode.PERSISTENT, prod.getDeliveryMode());
	}

	@Test(expected=JMSException.class)
	public void testDeliveryModeInvalid() throws JMSException
	{
		prod.setDeliveryMode(Integer.MAX_VALUE);
	}

	@Test
	public void testGetDestination()
	{
		assertSame(queue, prod.getDestination());
	}

	@Test
	public void testDisableMessageID()
	{
		assertFalse(prod.getDisableMessageID());
		prod.setDisableMessageID(true);
		assertFalse(prod.getDisableMessageID());
	}

	@Test
	public void testDisableMessageTimestamp()
	{
		assertFalse(prod.getDisableMessageTimestamp());
		prod.setDisableMessageTimestamp(true);
		assertFalse(prod.getDisableMessageTimestamp());
	}

	@Test
	public void testPriority() throws JMSException
	{
		assertEquals(Message.DEFAULT_PRIORITY, prod.getPriority());
		prod.setPriority(9);
		assertEquals(9, prod.getPriority());
	}

	@Test(expected=JMSException.class)
	public void testPriorityLow() throws JMSException
	{
		prod.setPriority(-1);
	}

	@Test(expected=JMSException.class)
	public void testPriorityHigh() throws JMSException
	{
		prod.setPriority(10);
	}

	@Test
	public void testTimeToLive() throws JMSException
	{
		assertEquals(Message.DEFAULT_TIME_TO_LIVE, prod.getTimeToLive());
		prod.setTimeToLive(10000L);
		assertEquals(10000L, prod.getTimeToLive());
	}

	@Test(expected=JMSException.class)
	public void testTimeToLiveInvalid() throws JMSException
	{
		prod.setTimeToLive(-1L);
	}

	@Test
	public void testSendMessage() throws JMSException
	{
		prod.send(message);
		fail("Don't know how to validate sending");
	}

	@Test(expected=UnsupportedOperationException.class)
	public void testSendMessageWithDestinationUnsupported() throws JMSException
	{
		prod.send(queue, message);
	}

	@Test
	public void testSendMessageWithDestination() throws JMSException
	{
		prod.close();
		prod = (TPJMSMessageProducer) session.createProducer(null);
		prod.send(queue, message);
		fail("Don't know how to validate sending");
	}

	@Test
	public void testSendMessageFull() throws JMSException
	{
		prod.send(message, DeliveryMode.NON_PERSISTENT, 1, 60000L);
		fail("Don't know how to validate sending");
	}

	@Test(expected=UnsupportedOperationException.class)
	public void testSendMessageWithDestinationFullUnsupported() throws JMSException
	{
		prod.send(queue, message, DeliveryMode.NON_PERSISTENT, 1, 60000L);
	}

	@Test
	public void testSendMessageWithDestinationFull() throws JMSException
	{
		prod.close();
		prod = (TPJMSMessageProducer) session.createProducer(null);
		prod.send(queue, message, DeliveryMode.NON_PERSISTENT, 1, 60000L);
		fail("Don't know how to validate sending");
	}
	
}
