package nl.ellipsis.tpjms.core.session;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.jms.*;

import org.apache.logging.log4j.*;

import nl.ellipsis.tpjms.core.connection.TPJMSConnection;
import nl.ellipsis.tpjms.core.message.TPJMSBytesMessage;
import nl.ellipsis.tpjms.core.message.TPJMSMapMessage;
import nl.ellipsis.tpjms.core.message.TPJMSMessage;
import nl.ellipsis.tpjms.core.message.TPJMSObjectMessage;
import nl.ellipsis.tpjms.core.message.TPJMSStreamMessage;
import nl.ellipsis.tpjms.core.message.TPJMSTextMessage;
import nl.ellipsis.tpjms.provider.TPJMSProvider;

public class TPJMSSession implements Session, QueueSession, TopicSession {
	private static final Logger logger = LogManager.getLogger(TPJMSSession.class);

	private final TPJMSConnection connection;
	private final int acknowledgeMode;
	private boolean openedConnection = false;

	/**
	 * Creates a new JMS session.
	 * 
	 * @param transacted
	 *            indicates whether the session is transacted
	 * @param acknowledgeMode
	 *            indicates whether the consumer or the client will acknowledge
	 *            any messages it receives; ignored if the session is
	 *            transacted. Legal values are {@link Session#AUTO_ACKNOWLEDGE},
	 *            {@link Session#CLIENT_ACKNOWLEDGE}, and
	 *            {@link Session#DUPS_OK_ACKNOWLEDGE}.
	 * @throws JMSException
	 *             if the Connection object fails to create a session due to
	 *             some internal error or lack of support for the specific
	 *             transaction and acknowledgement mode.
	 * @since JMS 1.1
	 * @see TPJMSConnection#createSession(boolean, int)
	 * @see Session#AUTO_ACKNOWLEDGE
	 * @see Session#CLIENT_ACKNOWLEDGE
	 * @see Session#DUPS_OK_ACKNOWLEDGE
	 */
	public TPJMSSession(TPJMSConnection connection, boolean transacted, int acknowledgeMode) throws JMSException {
		if (transacted) {
			// per spec, ignore ack value if transacted
			acknowledgeMode = SESSION_TRANSACTED;
		} else if (acknowledgeMode == SESSION_TRANSACTED) {
			// conflicting modes
			throw new JMSException("SESSION_TRANSACTED requires a transacted Session.");
		}

		if (acknowledgeMode != AUTO_ACKNOWLEDGE && acknowledgeMode != CLIENT_ACKNOWLEDGE
				&& acknowledgeMode != DUPS_OK_ACKNOWLEDGE && acknowledgeMode != SESSION_TRANSACTED) {
			throw new JMSException("Invalid acknowledgeMode: " + acknowledgeMode);
		}

		this.acknowledgeMode = acknowledgeMode;
		this.connection = connection;
		this.openedConnection = true;
	}

	/**
	 * Closes the session.
	 * Since a provider may allocate some resources on behalf of a session outside the JVM, 
	 * clients should close the resources when they are not needed. 
	 * Relying on garbage collection to eventually reclaim these resources may not be timely enough.
	 * 
	 * There is no need to close the producers and consumers of a closed session.
	 * 
	 * This call will block until a receive call or message listener in progress has completed. 
	 * A blocked message consumer receive call returns null when this session is closed.
	 * 
	 * However if the close method is called from a message listener on its own Session, 
	 * then it will either fail and throw a javax.jms.IllegalStateException,
	 * or it will succeed and close the Session, blocking until any pending receive 
	 * call in progress has completed. If close succeeds and the acknowledge mode of the 
	 * Session is set to AUTO_ACKNOWLEDGE, the current message will still be acknowledged 
	 * automatically when the onMessage call completes.
	 * 
	 * Since two alternative behaviors are permitted in this case, applications should avoid calling close from a message listener on its own Session because this is not portable.
	 * 
	 * This method must not return until any incomplete asynchronous send operations for this Session have been completed and any CompletionListener callbacks have returned. Incomplete sends should be allowed to complete normally unless an error occurs.
	 * 
	 * For the avoidance of doubt, if an exception listener for this session's connection is running when close is invoked, there is no requirement for the close call to wait until the exception listener has returned before it may return.
	 * 
	 * Closing a transacted session must roll back the transaction in progress.
	 * 
	 * This method is the only Session method that can be called concurrently.
	 * 
	 * A CompletionListener callback method must not call close on its own Session. Doing so will cause an IllegalStateException to be thrown.
	 * 
	 * Invoking any other Session method on a closed session must throw a IllegalStateException. Closing a closed session must not throw an exception.
	 * 
	 * @see AutoCloseable.close()
	 * @throws IllegalStateException -
	 * this method has been called by a MessageListener on its own Session
	 * this method has been called by a CompletionListener callback method on its own Session
	 * @throws JMSException - if the JMS provider fails to close the session due to some internal error.
	 */
	@Override
	public void close() throws JMSException {
		if(!this.openedConnection) {
			throw new JMSException("Session is already closed");
		}
		this.openedConnection = false;
		connection.unregisterSession(this);
	}

	@Override
	public void commit() throws JMSException {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException();
	}

	@Override
	public QueueBrowser createBrowser(Queue queue) throws JMSException {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException();
	}

	@Override
	public QueueBrowser createBrowser(Queue queue, String messageSelector) throws JMSException {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException();
	}

	@Override
	public BytesMessage createBytesMessage() throws JMSException {
		return new TPJMSBytesMessage(this);
	}

	@Override
	public MessageConsumer createConsumer(Destination destination) throws JMSException {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException();
	}

	@Override
	public MessageConsumer createConsumer(Destination destination, String messageSelector) throws JMSException {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException();
	}

	@Override
	public MessageConsumer createConsumer(Destination destination, String messageSelector, boolean NoLocal)
			throws JMSException {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException();
	}

	@Override
	public TopicSubscriber createDurableSubscriber(Topic topic, String name) throws JMSException {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException();
	}

	@Override
	public TopicSubscriber createDurableSubscriber(Topic topic, String name, String messageSelector, boolean noLocal) throws JMSException {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException();
	}

	@Override
	public MapMessage createMapMessage() throws JMSException {
		return new TPJMSMapMessage(this);
	}

	@Override
	public Message createMessage() throws JMSException {
		return new TPJMSMessage(this);
	}

	@Override
	public ObjectMessage createObjectMessage() throws JMSException {
		return new TPJMSObjectMessage(this);
	}

	@Override
	public ObjectMessage createObjectMessage(Serializable object) throws JMSException {
		ObjectMessage objectMessage = new TPJMSObjectMessage(this);
		objectMessage.setObject(object);
		return objectMessage;
	}

	/**
	 * Creates a <code>MessageProducer</code> to send messages to the specified
	 * destination.
	 * 
	 * <p>
	 * A client uses a <code>MessageProducer</code> object to send messages to a
	 * destination. Since <code>Queue</code> and <code>Topic</code> both inherit
	 * from <code>Destination</code>, they can be used in the destination
	 * parameter to create a <code>MessageProducer</code> object.
	 * </p>
	 * 
	 * @param destination
	 *            the <code>Destination</code> to send to, or <code>null</code>
	 *            if this is a producer which does not have a specified
	 *            destination.
	 * 
	 * @throws JMSException
	 *             if the session fails to create a MessageProducer due to some
	 *             internal error.
	 * @throws InvalidDestinationException
	 *             if an invalid destination is specified.
	 * @since JMS 1.1
	 */
	@Override
	public MessageProducer createProducer(Destination destination) throws JMSException {
		return new TPJMSMessageProducer(this,destination);
	}

	/**
	 * Creates a queue identity given a <code>Queue</code> name.
	 * 
	 * <p>
	 * This facility is provided for the rare cases where clients need to
	 * dynamically manipulate queue identity. It allows the creation of a queue
	 * identity with a provider-specific name. Clients that depend on this
	 * ability are not portable.
	 * </p>
	 * 
	 * <p>
	 * Note that this method is not for creating the physical queue. The
	 * physical creation of queues is an administrative task and is not to be
	 * initiated by the JMS API. The one exception is the creation of temporary
	 * queues, which is accomplished with the {@link #createTemporaryQueue()}
	 * method.
	 * </p>
	 * 
	 * @param queueName
	 *            the name of this <code>Queue</code>
	 * @return a <code>Queue</code> with the given name
	 * @throws JMSException
	 *             if the session fails to create a queue due to some internal
	 *             error.
	 * @since JMS 1.1
	 */
	@Override
	public Queue createQueue(String queueName) throws JMSException {
		return connection.getProvider().createQueue(queueName);
	}

	@Override
	public StreamMessage createStreamMessage() throws JMSException {
		return new TPJMSStreamMessage(this);
	}

	@Override
	public TemporaryQueue createTemporaryQueue() throws JMSException {
		java.util.UUID uuid = UUID.randomUUID();
		return new TPJMSTemporaryQueue(uuid.toString());
	}

	@Override
	public TemporaryTopic createTemporaryTopic() throws JMSException {
		java.util.UUID uuid = UUID.randomUUID();
		return new TPJMSTemporaryTopic(uuid.toString());
	}

	@Override
	public TextMessage createTextMessage() throws JMSException {
		return new TPJMSTextMessage(this);
	}

	@Override
	public TextMessage createTextMessage(String text) throws JMSException {
		return new TPJMSTextMessage(this,text);
	}

	/**
	 * Creates a topic identity given a <code>Topic</code> name.
	 * 
	 * <p>
	 * This facility is provided for the rare cases where clients need to
	 * dynamically manipulate topic identity. This allows the creation of a
	 * topic identity with a provider-specific name. Clients that depend on this
	 * ability are not portable.
	 * </p>
	 * 
	 * <p>
	 * Note that this method is not for creating the physical topic. The
	 * physical creation of topics is an administrative task and is not to be
	 * initiated by the JMS API. The one exception is the creation of temporary
	 * topics, which is accomplished with the {@link #createTemporaryTopic()}
	 * method.
	 * </p>
	 * 
	 * @param topicName
	 *            the name of this <code>Topic</code>
	 * @return a <code>Topic</code> with the given name
	 * @throws JMSException
	 *             if the session fails to create a topic due to some internal
	 *             error.
	 * @since JMS 1.1
	 */
	@Override
	public Topic createTopic(String topicName) throws JMSException {
		return connection.getProvider().createTopic(topicName);
	}

	/**
	 * Returns the acknowledgement mode of the session. The acknowledgement mode
	 * is set at the time that the session is created. If the session is
	 * transacted, the acknowledgement mode is ignored.
	 * 
	 * @return If the session is not transacted, returns the current
	 *         acknowledgement mode for the session. If the session is
	 *         transacted, returns {@link Session#SESSION_TRANSACTED}.
	 * @since JMS 1.1
	 * @see Connection#createSession(boolean, int)
	 */
	@Override
	public int getAcknowledgeMode() {
		return acknowledgeMode;
	}

	@Override
	public MessageListener getMessageListener() throws JMSException {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException();
	}

	/**
	 * Indicates whether the session is in transacted mode.
	 * 
	 * @return <code>true</code> if the session is in transacted mode
	 */
	@Override
	public boolean getTransacted() {
		return acknowledgeMode == SESSION_TRANSACTED;
	}

	@Override
	public void recover() throws JMSException {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException();
	}

	@Override
	public void rollback() throws JMSException {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException();
	}

	@Override
	public void run() {
		logger.warn("run() not implemented");
	}

	@Override
	public void setMessageListener(MessageListener listener) throws JMSException {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException();
	}

	@Override
	public void unsubscribe(String name) throws JMSException {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException();
	}

	@Override
	public TopicPublisher createPublisher(Topic topic) throws JMSException {
		return new TPJMSTopicPublisher(this,topic);
	}

	@Override
	public TopicSubscriber createSubscriber(Topic topic, String messageSelector, boolean noLocal) throws JMSException {
		TPJMSTopicSubscriber topicSubscriber = new TPJMSTopicSubscriber(topic, messageSelector, noLocal);
		connection.getProvider().registerMessageConsumer(topic,topicSubscriber);
		return topicSubscriber;
	}

	@Override
	public TopicSubscriber createSubscriber(Topic topic) throws JMSException {
		return createSubscriber(topic, null, true);
	}

	@Override
	public QueueReceiver createReceiver(Queue queue, String messageSelector) throws JMSException {
		throw new UnsupportedOperationException();
	}

	@Override
	public QueueReceiver createReceiver(Queue queue) throws JMSException {
		return createReceiver(queue, null);
	}

	@Override
	public QueueSender createSender(Queue queue) throws JMSException {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException();
	}
	
	///// INTERNAL
	public TPJMSConnection getConnection() {
		return connection;
	}
	
	public TPJMSProvider getProvider() {
		return connection.getProvider();
	}
	
	public boolean isOpen() {
		return this.openedConnection;
	}
	

}
