package nl.ellipsis.tpjms.provider.vm;

import java.net.*;
import java.util.*;
import java.util.concurrent.locks.ReentrantLock;

import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.Queue;
import javax.jms.Topic;

import org.apache.logging.log4j.*;

import nl.ellipsis.tpjms.core.destination.TPJMSQueue;
import nl.ellipsis.tpjms.core.destination.TPJMSTopic;
import nl.ellipsis.tpjms.provider.*;

/**
 * In-memory provider.
 */
public class VmProvider implements TPJMSProvider {
	private static final Logger logger = LogManager.getLogger(VmProvider.class);

	private static final VmProvider instance = new VmProvider();

	private final Map<String, VmBroker> brokers = new HashMap<String, VmBroker>();
	private final ReentrantLock brokersLock = new ReentrantLock();

	private static List<Destination> destinations = new ArrayList<Destination>();
	private final Map<Destination, List<MessageConsumer>> destinationMessageConsumers = new HashMap<Destination, List<MessageConsumer>>();

	private VmProvider() {
	}

	/**
	 * Gets a singleton instance of the VmProvider.
	 * 
	 * @return VmProvider instance.
	 */
	public static VmProvider getInstance() {
		return instance;
	}

	private VmBroker getBroker(String brokerName, String brokerId) throws JMSException {
		try {
			brokersLock.lock();

			VmBroker broker = brokers.get(brokerName);
			if (broker == null) {
				broker = new VmBroker(brokerName);
				brokers.put(brokerName, broker);
			} else if (brokerId != null && !broker.getBrokerId().equals(brokerId)) {
				throw new JMSException("Broker ID mismatch. Is your connection closed?");
			}
			return broker;
		} finally {
			brokersLock.unlock();
		}
	}

	/**
	 * Removes the named broker from memory, freeing all resources.
	 * 
	 * @param brokerName
	 *            broker name
	 */
	public void removeBroker(String brokerName) {
		logger.debug("Removing broker: " + brokerName);
		try {
			brokersLock.lock();
			brokers.remove(brokerName);
		} finally {
			brokersLock.unlock();
		}
	}

	@Override
	public TPJMSConnectionContext connect(URI uri, String username, String password) throws JMSException {
		String brokerName = uri.getHost();
		if (brokerName == null || brokerName.trim().length() == 0) {
			throw new InvalidUrlException("Broker must be specified.");
		}

		VmBroker broker = getBroker(brokerName, null);
		return new VmConnectionContext(brokerName, broker.getBrokerId());
	}

	@Override
	public void close(TPJMSConnectionContext context) throws JMSException {
		logger.debug("Closing connection: " + context);

		VmConnectionContext vmc = (VmConnectionContext) context;

		VmBroker broker = getBroker(vmc.getBrokerName(), vmc.getBrokerId());

		logger.debug("Connection closed for broker " + broker);
	}

	@Override
	public Queue createQueue(String queueName) throws JMSException {
		Queue queue = getQueue(queueName);
		if(queue == null) {
			queue = new TPJMSQueue(queueName);
			destinations.add(queue);
		}
		return queue;
	}

	@Override
	public Topic createTopic(String topicName) throws JMSException {
		Topic topic = getTopic(topicName);
		if(topic == null) {
			topic = new TPJMSTopic(topicName);
			destinations.add(topic);
		}
		return topic;
	}

	@Override
	public TPJMSSessionContext createSession() throws JMSException {
		// TODO Auto-generated method stub
		return new VmSessionContext();
	}

	@Override
	public boolean registerMessageConsumer(Topic topic, MessageConsumer messageConsumer) throws JMSException {
		if(!isRegisteredTopic(topic)) {
			throw new JMSException("Topic "+topic.getTopicName()+" is not registered for this provider");
		}
		List<MessageConsumer> messageConsumers = destinationMessageConsumers.get(topic);
		if(messageConsumers == null) {
			messageConsumers = new ArrayList<MessageConsumer>();
			destinationMessageConsumers.put(topic, messageConsumers);
		}
		if(messageConsumers.contains(messageConsumer)) {
			throw new JMSException("MessageConsumer "+messageConsumer.toString()+" is already registered for topic "+topic.getTopicName());
		}
		return messageConsumers.add(messageConsumer);
	}
	
	@Override
	public void send(Destination destination, Message message) throws JMSException {
		if(!destinations.contains(destination)) {
			throw new JMSException("Destination "+destination.toString()+" is not registered for this provider");
		}
		List<MessageConsumer> messageConsumers = destinationMessageConsumers.get(destination);
		if(messageConsumers != null) {
			for(MessageConsumer messageConsumer : messageConsumers) {
				MessageListener messageListener = messageConsumer.getMessageListener();
				if(messageListener!=null) {
					messageListener.onMessage(message);
				}
			}
		}
	}

	private Queue getQueue(String queueName) throws JMSException {
		for (Destination destination : destinations) {
			if (destination instanceof Queue && ((Queue) destination).getQueueName().equalsIgnoreCase(queueName)) {
				return (Queue) destination;
			}
		}
		return null;
	}

	private Topic getTopic(String topicName) throws JMSException {
		for (Destination destination : destinations) {
			if (destination instanceof Topic && ((Topic) destination).getTopicName().equalsIgnoreCase(topicName)) {
				return (Topic) destination;
			}
		}
		return null;
	}



	private boolean isRegisteredTopic(Topic topic) throws JMSException {
		for (Destination destination : destinations) {
			if (destination instanceof Topic && ((Topic) destination).equals(topic)) {
				return true;
			}
		}
		return false;
	}


}
