package nl.ellipsis.tpjms.core.session;

import javax.jms.JMSException;
import javax.jms.Queue;
import javax.jms.QueueBrowser;
import javax.jms.QueueReceiver;
import javax.jms.QueueSender;
import javax.jms.QueueSession;
import javax.jms.TemporaryQueue;

import nl.ellipsis.tpjms.core.connection.TPJMSConnection;

public class TPJMSQueueSession extends TPJMSSession implements QueueSession {

	public TPJMSQueueSession(TPJMSConnection connection, boolean transacted, int acknowledgeMode) throws JMSException {
		super(connection, transacted, acknowledgeMode);
	}
	
	@Override
	public QueueBrowser createBrowser(Queue queue) throws JMSException {
		return super.createBrowser(queue);
	}

	@Override
	public QueueBrowser createBrowser(Queue queue, String messageSelector) throws JMSException {
		return super.createBrowser(queue,messageSelector);
	}
	
	@Override
	public Queue createQueue(String queueName) throws JMSException {
		return super.createQueue(queueName);
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
	
	@Override
	public TemporaryQueue createTemporaryQueue() throws JMSException {
		return super.createTemporaryQueue();
	}

}
