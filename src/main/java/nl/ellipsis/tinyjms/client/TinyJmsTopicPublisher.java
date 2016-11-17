package nl.ellipsis.tinyjms.client;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Topic;
import javax.jms.TopicPublisher;

public class TinyJmsTopicPublisher extends TinyJmsMessageProducer implements
		TopicPublisher {

	public TinyJmsTopicPublisher(Topic topic) throws JMSException {
		super(topic);
	}

	public Topic getTopic() throws JMSException {
		return (Topic) super.getDestination();
	}

	public void publish(Message message) throws JMSException {
		send(message);
	}

	public void publish(Topic topic, Message message) throws JMSException {
		send(topic, message);
	}

	public void publish(Message message, int deliveryMode, int priority, long timeToLive) throws JMSException {
		send(message, deliveryMode, priority, timeToLive);
	}

	public void publish(Topic topic, Message message, int deliveryMode, int priority, long timeToLive) throws JMSException {
		send(topic, message, deliveryMode, priority, timeToLive);
	}

}
