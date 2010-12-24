package org.randomcoder.tinyjms.provider.vm;

import java.net.*;
import java.util.*;
import java.util.concurrent.locks.ReentrantLock;

import javax.jms.JMSException;

import org.apache.log4j.*;

import org.randomcoder.tinyjms.provider.*;

/**
 * In-memory provider.
 */
public class VmProvider implements TinyJmsProvider
{
	private static final Logger logger = LogManager.getLogger(VmProvider.class);
	
	private static final VmProvider instance = new VmProvider();
	
	private final Map<String, VmBroker> brokers = new HashMap<String, VmBroker>();
	private final ReentrantLock brokersLock = new ReentrantLock();
	
	private VmProvider() {}

	/**
	 * Gets a singleton instance of the VmProvider.
	 * 
	 * @return VmProvider instance.
	 */
	public static VmProvider getInstance()
	{
		return instance;
	}

	private VmBroker getBroker(String brokerName, String brokerId) throws JMSException
	{
		try
		{
			brokersLock.lock();
			
			VmBroker broker = brokers.get(brokerName);
			if (broker == null)
			{
				broker = new VmBroker(brokerName);
				brokers.put(brokerName, broker);
			}
			else if (brokerId != null && !broker.getBrokerId().equals(brokerId))
			{
				throw new JMSException("Broker ID mismatch. Is your connection closed?");
			}
			return broker;
		}
		finally
		{
			brokersLock.unlock();
		}
	}
	
	/**
	 * Removes the named broker from memory, freeing all resources.
	 *  
	 * @param brokerName
	 *          broker name
	 */
	public void removeBroker(String brokerName)
	{
		logger.debug("Removing broker: " + brokerName);
		try
		{
			brokersLock.lock();
			brokers.remove(brokerName);
		}
		finally
		{
			brokersLock.unlock();
		}
	}

	@Override
	public TinyJmsConnectionContext connect(URI uri, String username, String password) throws JMSException
	{
		String brokerName = uri.getHost();
		if (brokerName == null || brokerName.trim().length() == 0)
		{
			throw new InvalidUrlException("Broker must be specified.");
		}

		VmBroker broker = getBroker(brokerName, null);		
		return new VmConnectionContext(brokerName, broker.getBrokerId());
	}

	@Override
	public void close(TinyJmsConnectionContext context) throws JMSException
	{
		logger.debug("Closing connection: " + context);
		
		VmConnectionContext vmc = (VmConnectionContext) context;
		
		VmBroker broker = getBroker(vmc.getBrokerName(), vmc.getBrokerId());
		
		logger.debug("Connection closed for broker " + broker);
	}

	@Override
	public TinyJmsSessionContext createSession() throws JMSException
	{
		// TODO Auto-generated method stub
		return new VmSessionContext();
	}
}
