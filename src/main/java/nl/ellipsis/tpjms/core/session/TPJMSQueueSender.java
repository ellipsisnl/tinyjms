package nl.ellipsis.tpjms.core.session;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Queue;
import javax.jms.QueueSender;

import nl.ellipsis.tpjms.core.connection.TPJMSConnection;

public class TPJMSQueueSender extends TPJMSMessageProducer implements QueueSender {

	public TPJMSQueueSender(TPJMSConnection connection, Queue queue) throws JMSException {
		super(connection,queue);
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
