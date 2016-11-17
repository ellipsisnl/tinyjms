package nl.ellipsis.tinyjms.client;

import javax.jms.JMSException;
import javax.jms.Topic;
import javax.jms.TopicSubscriber;

public class TinyJmsTopicSubscriber extends TinyJmsMessageConsumer implements TopicSubscriber {

	private boolean noLocal = true;
	
	public TinyJmsTopicSubscriber(Topic topic) throws JMSException {
		super(topic);
	}
	
	public TinyJmsTopicSubscriber(Topic topic, String messageSelector, boolean noLocal) throws JMSException {
		super(topic,messageSelector);
		this.noLocal = noLocal;
	}
	
	public boolean getNoLocal() throws JMSException {
		return noLocal;
	}

	public Topic getTopic() throws JMSException {
		return (Topic) super.getDestination();
	}


}
