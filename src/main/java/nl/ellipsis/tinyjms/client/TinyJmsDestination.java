package nl.ellipsis.tinyjms.client;

import java.util.ArrayList;
import java.util.List;

import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;

/**
 * The Destination object encapsulates provider-specific addresses since JMS does not define a standard address syntax, Although a standard address syntax was considered, it was decided that the differences in address semantics between existing MOM products was too wide to bridge with a single syntax.
Since Destination is an administered object it may also contain provider-specific configuration information in addition to its address.
JMS also supports a client's use of provider-specific address names.
Destination objects support concurrent use.
A Destination is a JMS administered object.
JMS administered objects are objects containing JMS configuration information that are created by a JMS administrator and later used by JMS clients. They make it practical to administer JMS in the enterprise.
Although the interfaces for administered objects do not explicitly depend on JNDI, JMS establishes the convention that JMS clients find them by looking them up in a JNDI namespace.
An administrator can place an administered object anywhere in a namespace. JMS does not define a naming policy.
It is expected that JMS providers will provide the tools an administrator needs to create and configure administered objects in a JNDI namespace. JMS provider implementations of administered objects should be both javax.jndi.Referenceable and java.io.Serializable so that they can be stored in all JNDI naming contexts. In addition, it is recommended that these implementations follow the JavaBeans(TM) design patterns.
This strategy provides several benefits.

It hides provider-specific details from JMS clients.
It abstracts JMS administrative information into Java objects that are easily organized and administrated from a common management console.
Since there will be JNDI providers for all popular naming services, this means JMS providers can deliver one implementation of administered objects that will run everywhere.
An administered object should not hold on to any remote resources. Its lookup should not use remote resources other than those used by JNDI itself.

Clients should think of administered objects as local Java objects. Looking them up should not have any hidden side affects or use surprising amounts of local resources.

 * @see javax.jms.Queue
 * @see javax.jms.Topic
 */

abstract public class TinyJmsDestination implements Destination {
	private final String name;
	
	private List<MessageConsumer> messageConsumers = new ArrayList<MessageConsumer>(); 

	TinyJmsDestination(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	/**
	 * Determines if this destination is a queue.
	 * 
	 * @return <code>true</code> if a queu
	 */
	abstract public boolean isQueue();

	/**
	 * Determines if this destination is a topic.
	 * 
	 * @return <code>true</code> if a topic
	 */
	abstract public boolean isTopic();

	/**
	 * Determines if this destination is temporary.
	 * 
	 * @return <code>true</code> if temporary
	 */
	abstract public boolean isTemporary();
	
	/////// INTERNAL
	
	protected void setMessageConsumer(MessageConsumer messageConsumer) throws JMSException {
		if(messageConsumers.contains(messageConsumer)) {
			throw new JMSException("MessageConsumer "+messageConsumer.toString() +" is already registered");
		}
		messageConsumers.add(messageConsumer);
	}
	
	protected void send(Message message) throws JMSException {
		for(MessageConsumer messageConsumer : messageConsumers) {
			if(messageConsumer.getMessageListener()!=null) {
				messageConsumer.getMessageListener().onMessage(message);
			}
		}
	}
	
}
