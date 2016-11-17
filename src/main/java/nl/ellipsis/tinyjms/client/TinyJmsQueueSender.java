package nl.ellipsis.tinyjms.client;

import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Queue;
import javax.jms.QueueSender;

public class TinyJmsQueueSender extends TinyJmsMessageProducer implements QueueSender {

	public TinyJmsQueueSender(Queue queue) throws JMSException {
		super(queue);
	}

	public Queue getQueue() throws JMSException {
		return (Queue) super.getDestination();
	}

	public void send(Queue queue, Message message) throws JMSException {
		send(queue,message);
	}

	public void send(Queue queue, Message message, int deliveryMode, int priority, long timeToLive) throws JMSException {
		send(queue,message,deliveryMode,priority,timeToLive);
	}

}
