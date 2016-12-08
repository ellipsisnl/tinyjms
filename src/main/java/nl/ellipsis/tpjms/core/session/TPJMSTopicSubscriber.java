package nl.ellipsis.tpjms.core.session;

import javax.jms.JMSException;
import javax.jms.Topic;
import javax.jms.TopicSubscriber;

public class TPJMSTopicSubscriber extends TPJMSMessageConsumer implements TopicSubscriber {
	private final String name;

	public TPJMSTopicSubscriber(TPJMSSession session, Topic topic) throws JMSException {
		super(session,topic);
		this.name = null;
	}
	
	public TPJMSTopicSubscriber(TPJMSSession session, Topic topic, String name) throws JMSException {
		super(session,topic);
		this.name = name;
	}
	
	public TPJMSTopicSubscriber(TPJMSSession session, Topic topic, String messageSelector, boolean noLocal) throws JMSException {
		super(session,topic,messageSelector,noLocal);
		this.name = null;
	}
	
	public TPJMSTopicSubscriber(TPJMSSession session, Topic topic, String name, String messageSelector, boolean noLocal) throws JMSException {
		super(session,topic,messageSelector,noLocal);
		this.name = name;
	}
	
	/**
	 * Gets the NoLocal attribute for this subscriber. The default value for
	 * this attribute is false.
	 * 
	 * @return true if locally published messages are being inhibited
	 * 
	 * @throws JMSException
	 *             - if the JMS provider fails to get the NoLocal attribute for
	 *             this topic subscriber due to some internal error.
	 */
	public boolean getNoLocal() throws JMSException {
		return super.getNoLocal();
	}

	/**
	 * Gets the Topic associated with this subscriber.
	 * 
	 * @return this subscriber's Topic
	 * 
	 * @throws JMSException
	 *             - if the JMS provider fails to get the topic for this topic
	 *             subscriber due to some internal error.
	 */
	public Topic getTopic() throws JMSException {
		return (Topic) super.getDestination();
	}
	
	public String getName() {
		return name;
	}


}
