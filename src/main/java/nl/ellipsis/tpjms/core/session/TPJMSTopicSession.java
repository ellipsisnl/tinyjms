package nl.ellipsis.tpjms.core.session;

import java.util.UUID;

import javax.jms.InvalidDestinationException;
import javax.jms.InvalidSelectorException;
import javax.jms.JMSException;
import javax.jms.TemporaryTopic;
import javax.jms.Topic;
import javax.jms.TopicPublisher;
import javax.jms.TopicSession;
import javax.jms.TopicSubscriber;

import nl.ellipsis.tpjms.core.connection.TPJMSConnection;

/**
 * A TopicSession object provides methods for creating TopicPublisher,
 * TopicSubscriber, and TemporaryTopic objects. It also provides a method for
 * deleting its client's durable subscribers.
 * 
 * A TopicSession is used for creating Pub/Sub specific objects. In general, use
 * the Session object, and use TopicSession only to support existing code. Using
 * the Session object simplifies the programming model, and allows transactions
 * to be used across the two messaging domains.
 * 
 * A TopicSession cannot be used to create objects specific to the
 * point-to-point domain. The following methods inherit from Session, but must
 * throw an IllegalStateException if used from TopicSession:
 * 
 * createBrowser createQueue createTemporaryQueue
 */
public class TPJMSTopicSession extends TPJMSSession implements TopicSession {

	public TPJMSTopicSession(TPJMSConnection connection, boolean transacted, int acknowledgeMode) throws JMSException {
		super(connection, transacted, acknowledgeMode);
	}
	
	/**
	 * @see nl.ellipsis.tpjms.core.session.TPJMSSession#createDurableSubscriber(Topic, String)
	 */
	@Override
	public TopicSubscriber createDurableSubscriber(Topic topic, String name) throws JMSException {
		return super.createDurableSubscriber(topic,name);
	}
	
	/**
	 * @see nl.ellipsis.tpjms.core.session.TPJMSSession#createDurableSubscriber(Topic, String, String, boolean)
	 */
	@Override
	public TopicPublisher createPublisher(Topic topic) throws JMSException {
		return new TPJMSTopicPublisher(this,topic);
	}

	/**
	 * Creates a nondurable subscriber to the specified topic. A client uses a
	 * TopicSubscriber object to receive messages that have been published to a
	 * topic.
	 * 
	 * Regular TopicSubscriber objects are not durable. They receive only
	 * messages that are published while they are active.
	 * 
	 * In some cases, a connection may both publish and subscribe to a topic.
	 * The subscriber NoLocal attribute allows a subscriber to inhibit the
	 * delivery of messages published by its own connection. The default value
	 * for this attribute is false.
	 * 
	 * @param topic
	 *            - the Topic to subscribe to
	 * @throws JMSException
	 *             - if the session fails to create a subscriber due to some
	 *             internal error.
	 * @throws InvalidDestinationException
	 *             - if an invalid topic is specified.
	 */
	@Override
	public TopicSubscriber createSubscriber(Topic topic) throws JMSException {
		return createSubscriber(topic, null, true);
	}

	/**
	 * Creates a nondurable subscriber to the specified topic, using a message
	 * selector or specifying whether messages published by its own connection
	 * should be delivered to it. A client uses a TopicSubscriber object to
	 * receive messages that have been published to a topic.
	 * 
	 * Regular TopicSubscriber objects are not durable. They receive only
	 * messages that are published while they are active.
	 * 
	 * Messages filtered out by a subscriber's message selector will never be
	 * delivered to the subscriber. From the subscriber's perspective, they do
	 * not exist.
	 * 
	 * In some cases, a connection may both publish and subscribe to a topic.
	 * The subscriber NoLocal attribute allows a subscriber to inhibit the
	 * delivery of messages published by its own connection. The default value
	 * for this attribute is false.
	 * 
	 * @param topic
	 *            - the Topic to subscribe to
	 * @param messageSelector
	 *            - only messages with properties matching the message selector
	 *            expression are delivered. A value of null or an empty string
	 *            indicates that there is no message selector for the message
	 *            consumer.
	 * @param noLocal
	 *            - if set, inhibits the delivery of messages published by its
	 *            own connection
	 * @throws JMSException
	 *             - if the session fails to create a subscriber due to some
	 *             internal error.
	 * @throws InvalidDestinationException
	 *             - if an invalid topic is specified.
	 * @throws InvalidSelectorException
	 *             - if the message selector is invalid
	 */
	@Override
	public TopicSubscriber createSubscriber(Topic topic, String messageSelector, boolean noLocal) throws JMSException {
		TPJMSTopicSubscriber topicSubscriber = new TPJMSTopicSubscriber(this,topic,messageSelector,noLocal);
		connection.getProvider().registerMessageConsumer(topic,topicSubscriber);
		return topicSubscriber;
	}
	
	/**
	 * @see nl.ellipsis.tpjms.core.session.TPJMSSession#createTemporaryTopic()
	 */
	@Override
	public TemporaryTopic createTemporaryTopic() throws JMSException {
		return super.createTemporaryTopic();
	}
	
	/**
	 * @see nl.ellipsis.tpjms.core.session.TPJMSSession#createTopic(String)
	 */
	@Override
	public Topic createTopic(String topicName) throws JMSException {
		return super.createTopic(topicName);
	}
	
	/**
	 * @see nl.ellipsis.tpjms.core.session.TPJMSSession#unsubscribe(String)
	 */
	@Override
	public void unsubscribe(String name) throws JMSException {
		super.unsubscribe(name);
	}

}
