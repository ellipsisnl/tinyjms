package nl.ellipsis.tpjms.core.session;

import javax.jms.JMSException;
import javax.jms.Topic;
import javax.jms.TopicSubscriber;

public class TPJMSTopicSubscriber extends TPJMSMessageConsumer implements TopicSubscriber {

	private boolean noLocal = true;
	
	public TPJMSTopicSubscriber(Topic topic) throws JMSException {
		super(topic);
	}
	
	public TPJMSTopicSubscriber(Topic topic, String messageSelector, boolean noLocal) throws JMSException {
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
