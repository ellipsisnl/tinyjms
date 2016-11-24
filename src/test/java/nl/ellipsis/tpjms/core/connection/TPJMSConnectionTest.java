package nl.ellipsis.tpjms.core.connection;

import static org.junit.Assert.*;

import javax.jms.*;
import javax.jms.IllegalStateException;

import nl.ellipsis.tpjms.core.connection.TPJMSConnection;
import nl.ellipsis.tpjms.core.connection.TPJMSConnectionFactory;
import nl.ellipsis.tpjms.core.connection.TPJMSConnectionMetaData;
import nl.ellipsis.tpjms.core.session.TPJMSSession;
import nl.ellipsis.tpjms.provider.vm.VmProvider;

import org.junit.*;

public class TPJMSConnectionTest {
	private TPJMSConnection con;

	@Before
	public void setUp() throws Exception {
		VmProvider.getInstance().removeBroker("test");

		TPJMSConnectionFactory factory = new TPJMSConnectionFactory(
				"vm://test?debug=true&test=false");
		con = (TPJMSConnection) factory.createConnection();
	}

	@After
	public void tearDown() throws Exception {
		con.close();
		con = null;
		VmProvider.getInstance().removeBroker("test");
	}

	@Test
	public void testClose() throws JMSException {
		con.close();
	}

	@Test(expected = JMSException.class)
	public void testCreateConnectionConsumer() throws JMSException {
		con.createConnectionConsumer((Destination) null, null, null, 0);
	}

	@Test(expected = JMSException.class)
	public void testCreateConnectionConsumerQueue() throws JMSException {
		con.createConnectionConsumer((Queue) null, null, null, 0);
	}

	@Test(expected = JMSException.class)
	public void testCreateConnectionConsumerTopic() throws JMSException {
		con.createConnectionConsumer((Topic) null, null, null, 0);
	}

	@Test(expected = JMSException.class)
	public void testCreateDurableConnectionConsumer() throws JMSException {
		con.createDurableConnectionConsumer(null, null, null, null, 0);
	}

	@Test
	public void testCreateSession() throws JMSException {
		Session session = con.createSession(false, Session.AUTO_ACKNOWLEDGE);
		assertNotNull(session);
		assertTrue(session instanceof TPJMSSession);
		assertFalse(session.getTransacted());
		assertEquals(Session.AUTO_ACKNOWLEDGE, session.getAcknowledgeMode());
	}

	@Test
	public void testCreateSessionTransacted() throws JMSException {
		Session session = con.createSession(true, Session.AUTO_ACKNOWLEDGE);
		assertNotNull(session);
		assertTrue(session instanceof TPJMSSession);
		assertTrue(session.getTransacted());
		assertEquals(Session.SESSION_TRANSACTED, session.getAcknowledgeMode());
	}

	@Test(expected = JMSException.class)
	public void testCreateSessionConflict() throws JMSException {
		con.createSession(false, Session.SESSION_TRANSACTED);
	}

	@Test(expected = JMSException.class)
	public void testCreateSessionInvalid() throws JMSException {
		con.createSession(false, Integer.MAX_VALUE);
	}

	@Test
	public void testGetMetaData() throws JMSException {
		ConnectionMetaData meta = con.getMetaData();
		assertNotNull(meta);
		assertTrue(meta instanceof TPJMSConnectionMetaData);
	}

	@Test
	public void testSetClientID() throws JMSException {
		con.setClientID("CLIENT-ID");
		assertEquals("CLIENT-ID", con.getClientID());
	}

	@Test(expected = IllegalStateException.class)
	public void testSetClientIDAlreadySet() throws JMSException {
		con.setClientID("CLIENT-ID");
		con.setClientID("CLIENT-ID-2");
	}

	@Test
	public void testSetExceptionListener() {
		ExceptionListener listener = new ExceptionListener() {
			@Override
			public void onException(JMSException exception) {
			}
		};

		con.setExceptionListener(listener);
		assertSame(listener, con.getExceptionListener());
	}

	@Test
	public void testStart() throws JMSException {
		con.start();
		assertTrue(con.isRunning());
	}

	@Test
	public void testStop() throws JMSException {
		con.stop();
		assertFalse(con.isRunning());
	}

	@Test
	public void testCreateQueueSession() throws JMSException {
		QueueSession session = con.createQueueSession(false,
				Session.AUTO_ACKNOWLEDGE);
		assertNotNull(session);
		assertTrue(session instanceof TPJMSSession);
		assertFalse(session.getTransacted());
		assertEquals(Session.AUTO_ACKNOWLEDGE, session.getAcknowledgeMode());
	}

	@Test
	public void testCreateTopicSession() throws JMSException {
		TopicSession session = con.createTopicSession(false,
				Session.AUTO_ACKNOWLEDGE);
		assertNotNull(session);
		assertTrue(session instanceof TPJMSSession);
		assertFalse(session.getTransacted());
		assertEquals(Session.AUTO_ACKNOWLEDGE, session.getAcknowledgeMode());
	}

}
