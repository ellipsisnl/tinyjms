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

public class TPJMSSession implements Session {
	private static final Logger logger = LogManager.getLogger(TPJMSSession.class);

	protected final TPJMSConnection connection;
	private final int acknowledgeMode;
	private boolean openedConnection = false;
	
	private Destination defaultDestination;

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
		return new TPJMSQueueBrowser(this,queue);
	}

	@Override
	public QueueBrowser createBrowser(Queue queue, String messageSelector) throws JMSException {
		return new TPJMSQueueBrowser(this,queue,messageSelector);
	}

	@Override
	public BytesMessage createBytesMessage() throws JMSException {
		return new TPJMSBytesMessage(this,defaultDestination);
	}

	@Override
	public MessageConsumer createConsumer(Destination destination) throws JMSException {
		return new TPJMSMessageConsumer(this,destination);
	}

	@Override
	public MessageConsumer createConsumer(Destination destination, String messageSelector) throws JMSException {
		return new TPJMSMessageConsumer(this,destination,messageSelector);
	}

	@Override
	public MessageConsumer createConsumer(Destination destination, String messageSelector, boolean noLocal)  throws JMSException {
		return new TPJMSMessageConsumer(this,destination,messageSelector,noLocal);
	}
	
	/**
	Creates an unshared durable subscription on the specified topic (if one does not already exist), specifying a message selector and the noLocal parameter, and creates a consumer on that durable subscription.
	A durable subscription is used by an application which needs to receive all the messages published on a topic, including the ones published when there is no active consumer associated with it. The JMS provider retains a record of this durable subscription and ensures that all messages from the topic's publishers are retained until they are delivered to, and acknowledged by, a consumer on this durable subscription or until they have expired.

	A durable subscription will continue to accumulate messages until it is deleted using the unsubscribe method.

	This method may only be used with unshared durable subscriptions. Any durable subscription created using this method will be unshared. This means that only one active (i.e. not closed) consumer on the subscription may exist at a time. The term "consumer" here means a TopicSubscriber, MessageConsumer or JMSConsumer object in any client.

	An unshared durable subscription is identified by a name specified by the client and by the client identifier, which must be set. An application which subsequently wishes to create a consumer on that unshared durable subscription must use the same client identifier.

	If an unshared durable subscription already exists with the same name and client identifier, and the same topic, message selector and noLocal value has been specified, and there is no consumer already active (i.e. not closed) on the durable subscription then this method creates a MessageConsumer on the existing durable subscription.

	If an unshared durable subscription already exists with the same name and client identifier, and there is a consumer already active (i.e. not closed) on the durable subscription, then a JMSException will be thrown.

	If an unshared durable subscription already exists with the same name and client identifier but a different topic, message selector or noLocal value has been specified, and there is no consumer already active (i.e. not closed) on the durable subscription then this is equivalent to unsubscribing (deleting) the old one and creating a new one.

	If noLocal is set to true then any messages published to the topic using this session's connection, or any other connection with the same client identifier, will not be added to the durable subscription.

	A shared durable subscription and an unshared durable subscription may not have the same name and client identifier. If a shared durable subscription already exists with the same name and client identifier then a JMSException is thrown.

	There is no restriction on durable subscriptions and shared non-durable subscriptions having the same name and clientId. Such subscriptions would be completely separate.

	This method is identical to the corresponding createDurableSubscriber method except that it returns a MessageConsumer rather than a TopicSubscriber to represent the consumer.


	@param topic - the non-temporary Topic to subscribe to
	@param name - the name used to identify this subscription
	@param messageSelector - only messages with properties matching the message selector expression are added to the durable subscription. A value of null or an empty string indicates that there is no message selector for the durable subscription.
	@param noLocal - if true then any messages published to the topic using this session's connection, or any other connection with the same client identifier, will not be added to the durable subscription.
	
	@throws InvalidDestinationException - if an invalid topic is specified.
	@throws InvalidSelectorException - if the message selector is invalid.
	@throws IllegalStateException - if the client identifier is unset
	@throws JMSException -
	if the session fails to create the unshared durable subscription and MessageConsumer due to some internal error
	if an unshared durable subscription already exists with the same name and client identifier, and there is a consumer already active
	if a shared durable subscription already exists with the same name and client identifier
	
	@since JMS 2.0
	MessageConsumer createDurableConsumer(Topic topic,
            String name,
            String messageSelector,
            boolean noLocal)
     throws JMSException {
		return null;
	}
	*/

	/**
	Creates an unshared durable subscription on the specified topic (if one does not already exist) and creates a consumer on that durable subscription. This method creates the durable subscription without a message selector and with a noLocal value of false.
	A durable subscription is used by an application which needs to receive all the messages published on a topic, including the ones published when there is no active consumer associated with it. The JMS provider retains a record of this durable subscription and ensures that all messages from the topic's publishers are retained until they are delivered to, and acknowledged by, a consumer on this durable subscription or until they have expired.

	A durable subscription will continue to accumulate messages until it is deleted using the unsubscribe method.

	This method may only be used with unshared durable subscriptions. Any durable subscription created using this method will be unshared. This means that only one active (i.e. not closed) consumer on the subscription may exist at a time. The term "consumer" here means a TopicSubscriber, MessageConsumer or JMSConsumer object in any client.

	An unshared durable subscription is identified by a name specified by the client and by the client identifier, which must be set. An application which subsequently wishes to create a consumer on that unshared durable subscription must use the same client identifier.

	If an unshared durable subscription already exists with the same name and client identifier, and the same topic, message selector and noLocal value has been specified, and there is no consumer already active (i.e. not closed) on the durable subscription then this method creates a MessageConsumer on the existing durable subscription.

	If an unshared durable subscription already exists with the same name and client identifier, and there is a consumer already active (i.e. not closed) on the durable subscription, then a JMSException will be thrown.

	If an unshared durable subscription already exists with the same name and client identifier but a different topic, message selector or noLocal value has been specified, and there is no consumer already active (i.e. not closed) on the durable subscription then this is equivalent to unsubscribing (deleting) the old one and creating a new one.

	A shared durable subscription and an unshared durable subscription may not have the same name and client identifier. If a shared durable subscription already exists with the same name and client identifier then a JMSException is thrown.

	There is no restriction on durable subscriptions and shared non-durable subscriptions having the same name and clientId. Such subscriptions would be completely separate.

	This method is identical to the corresponding createDurableSubscriber method except that it returns a MessageConsumer rather than a TopicSubscriber to represent the consumer.


	@param topic - the non-temporary Topic to subscribe to
	@param name - the name used to identify this subscription
	
	@throws InvalidDestinationException - if an invalid topic is specified.
	@throws IllegalStateException - if the client identifier is unset
	@throws JMSException -
	if the session fails to create the unshared durable subscription and MessageConsumer due to some internal error
	if an unshared durable subscription already exists with the same name and client identifier, and there is a consumer already active
	if a shared durable subscription already exists with the same name and client identifier
	
	@since JMS 2.0
	MessageConsumer createDurableConsumer(Topic topic,
                                      String name)
                               throws JMSException {
    	return null;
    }                           
	*/
	
	/**
	Creates an unshared durable subscription on the specified topic (if one does not already exist) and creates a consumer on that durable subscription. This method creates the durable subscription without a message selector and with a noLocal value of false.
	A durable subscription is used by an application which needs to receive all the messages published on a topic, including the ones published when there is no active consumer associated with it. The JMS provider retains a record of this durable subscription and ensures that all messages from the topic's publishers are retained until they are delivered to, and acknowledged by, a consumer on this durable subscription or until they have expired.

	A durable subscription will continue to accumulate messages until it is deleted using the unsubscribe method.

	This method may only be used with unshared durable subscriptions. Any durable subscription created using this method will be unshared. This means that only one active (i.e. not closed) consumer on the subscription may exist at a time. The term "consumer" here means a TopicSubscriber, MessageConsumer or JMSConsumer object in any client.

	An unshared durable subscription is identified by a name specified by the client and by the client identifier, which must be set. An application which subsequently wishes to create a consumer on that unshared durable subscription must use the same client identifier.

	If an unshared durable subscription already exists with the same name and client identifier, and the same topic, message selector and noLocal value has been specified, and there is no consumer already active (i.e. not closed) on the durable subscription then this method creates a TopicSubscriber on the existing durable subscription.

	If an unshared durable subscription already exists with the same name and client identifier, and there is a consumer already active (i.e. not closed) on the durable subscription, then a JMSException will be thrown.

	If an unshared durable subscription already exists with the same name and client identifier but a different topic, message selector or noLocal value has been specified, and there is no consumer already active (i.e. not closed) on the durable subscription then this is equivalent to unsubscribing (deleting) the old one and creating a new one.

	A shared durable subscription and an unshared durable subscription may not have the same name and client identifier. If a shared durable subscription already exists with the same name and client identifier then a JMSException is thrown.

	There is no restriction on durable subscriptions and shared non-durable subscriptions having the same name and clientId. Such subscriptions would be completely separate.

	This method is identical to the corresponding createDurableConsumer method except that it returns a TopicSubscriber rather than a MessageConsumer to represent the consumer.

	@param topic - the non-temporary Topic to subscribe to
	@param name - the name used to identify this subscription
	
	@throws InvalidDestinationException - if an invalid topic is specified.
	@throws IllegalStateException - if the client identifier is unset
	@throws JMSException -
	if the session fails to create the unshared durable subscription and TopicSubscriber due to some internal error
	if an unshared durable subscription already exists with the same name and client identifier, and there is a consumer already active
	if a shared durable subscription already exists with the same name and client identifier
	
	@since JMS 1.1
	*/
	@Override
	public TopicSubscriber createDurableSubscriber(Topic topic, String name) throws JMSException {
		return new TPJMSTopicSubscriber(this,topic,name);
	}

	/**
	Creates an unshared durable subscription on the specified topic (if one does not already exist), specifying a message selector and the noLocal parameter, and creates a consumer on that durable subscription.
	A durable subscription is used by an application which needs to receive all the messages published on a topic, including the ones published when there is no active consumer associated with it. The JMS provider retains a record of this durable subscription and ensures that all messages from the topic's publishers are retained until they are delivered to, and acknowledged by, a consumer on this durable subscription or until they have expired.

	A durable subscription will continue to accumulate messages until it is deleted using the unsubscribe method.

	This method may only be used with unshared durable subscriptions. Any durable subscription created using this method will be unshared. This means that only one active (i.e. not closed) consumer on the subscription may exist at a time. The term "consumer" here means a TopicSubscriber, MessageConsumer or JMSConsumer object in any client.

	An unshared durable subscription is identified by a name specified by the client and by the client identifier, which must be set. An application which subsequently wishes to create a consumer on that unshared durable subscription must use the same client identifier.

	If an unshared durable subscription already exists with the same name and client identifier, and the same topic, message selector and noLocal value has been specified, and there is no consumer already active (i.e. not closed) on the durable subscription then this method creates a TopicSubscriber on the existing durable subscription.

	If an unshared durable subscription already exists with the same name and client identifier, and there is a consumer already active (i.e. not closed) on the durable subscription, then a JMSException will be thrown.

	If an unshared durable subscription already exists with the same name and client identifier but a different topic, message selector or noLocal value has been specified, and there is no consumer already active (i.e. not closed) on the durable subscription then this is equivalent to unsubscribing (deleting) the old one and creating a new one.

	If noLocal is set to true then any messages published to the topic using this session's connection, or any other connection with the same client identifier, will not be added to the durable subscription.

	A shared durable subscription and an unshared durable subscription may not have the same name and client identifier. If a shared durable subscription already exists with the same name and client identifier then a JMSException is thrown.

	There is no restriction on durable subscriptions and shared non-durable subscriptions having the same name and clientId. Such subscriptions would be completely separate.

	This method is identical to the corresponding createDurableConsumer method except that it returns a TopicSubscriber rather than a MessageConsumer to represent the consumer.


	@param topic - the non-temporary Topic to subscribe to
	@param name - the name used to identify this subscription
	@param messageSelector - only messages with properties matching the message selector expression are added to the durable subscription. A value of null or an empty string indicates that there is no message selector for the durable subscription.
	@param noLocal - if true then any messages published to the topic using this session's connection, or any other connection with the same client identifier, will not be added to the durable subscription.
	
	@throws InvalidDestinationException - if an invalid topic is specified.
	@throws InvalidSelectorException - if the message selector is invalid.
	@throws IllegalStateException - if the client identifier is unset
	@throws JMSException -
	if the session fails to create the unshared durable subscription and TopicSubscriber due to some internal error
	if an unshared durable subscription already exists with the same name and client identifier, and there is a consumer already active
	if a shared durable subscription already exists with the same name and client identifier
	
	@since JMS 1.1
	*/
	@Override
	public TopicSubscriber createDurableSubscriber(Topic topic, String name, String messageSelector, boolean noLocal) throws JMSException {
		return new TPJMSTopicSubscriber(this,topic,name,messageSelector,noLocal);
	}
	
	@Override
	public MapMessage createMapMessage() throws JMSException {
		return new TPJMSMapMessage(this,defaultDestination);
	}

	@Override
	public Message createMessage() throws JMSException {
		return new TPJMSMessage(this,defaultDestination);
	}

	@Override
	public ObjectMessage createObjectMessage() throws JMSException {
		return new TPJMSObjectMessage(this,defaultDestination);
	}

	@Override
	public ObjectMessage createObjectMessage(Serializable object) throws JMSException {
		ObjectMessage objectMessage = new TPJMSObjectMessage(this,defaultDestination);
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
		return new TPJMSStreamMessage(this,defaultDestination);
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
		return new TPJMSTextMessage(this,defaultDestination);
	}

	@Override
	public TextMessage createTextMessage(String text) throws JMSException {
		return new TPJMSTextMessage(this,defaultDestination,text);
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

	/**
	 * Returns the session's distinguished message listener (optional).
	 * 
	 * @returns the message listener associated with this session
	 * @throws JMSException
	 *             - if the JMS provider fails to get the message listener due to an internal error.
	 * @see setMessageListener(javax.jms.MessageListener)
	 * @see ServerSessionPool
	 * @see ServerSession
	 */
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

	/**
	 * Sets the session's distinguished message listener (optional). When the
	 * distinguished message listener is set, no other form of message receipt
	 * in the session can be used; however, all forms of sending messages are
	 * still supported.
	 * 
	 * This is an expert facility not used by regular JMS clients.
	 * 
	 * @param listener
	 *            - the message listener to associate with this session
	 * @throws JMSException
	 *             - if the JMS provider fails to set the message listener due to an internal error.
	 * @see getMessageListener(), ServerSessionPool, ServerSession
	 */
	@Override
	public void setMessageListener(MessageListener listener) throws JMSException {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException();
	}

	/**
	 * Unsubscribes a durable subscription that has been created by a client.
	 * This method deletes the state being maintained on behalf of the
	 * subscriber by its provider.
	 * 
	 * It is erroneous for a client to delete a durable subscription while there
	 * is an active MessageConsumer or TopicSubscriber for the subscription, or
	 * while a consumed message is part of a pending transaction or has not been
	 * acknowledged in the session.
	 * 
	 * @param name
	 *            - the name used to identify this subscription
	 * @throws JMSException
	 *             - if the session fails to unsubscribe to the durable
	 *             subscription due to some internal error.
	 * @throws InvalidDestinationException
	 *             - if an invalid subscription name is specified.
	 * @since 1.1
	 */
	@Override
	public void unsubscribe(String name) throws JMSException {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException();
	}

	///// INTERNAL
	public TPJMSConnection getConnection() {
		return this.connection;
	}
	
	public Destination getDefaultDestination() {
		return this.defaultDestination;
	}

	public TPJMSProvider getProvider() {
		return connection.getProvider();
	}
	
	public boolean isOpen() {
		return this.openedConnection;
	}
	
	public void setDefaultDestination(Destination destination) {
		this.defaultDestination = destination;
	}
	

}
