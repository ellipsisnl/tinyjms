package nl.ellipsis.tinyjms.provider.vm;

import java.util.UUID;

import nl.ellipsis.tinyjms.provider.TinyJmsConnectionContext;

public class VmConnectionContext implements TinyJmsConnectionContext
{
	private final String brokerName;
	private final String brokerId;
	private final String connectionId;
	
	/**
	 * Creates a new VM connection context.
	 * 
	 * @param brokerName
	 *          broker name
	 * @param brokerId
	 *          unique identifier
	 */
	public VmConnectionContext(String brokerName, String brokerId)
	{
		this.brokerName = brokerName;
		this.brokerId = brokerId;
		this.connectionId = UUID.randomUUID().toString();
	}

	/**
	 * Gets the broker name this connection is associated with.
	 * 
	 * @return broker name
	 */
	public String getBrokerName()
	{
		return brokerName;
	}

	/**
	 * Gets the broker ID this connection is associated with.
	 * 
	 * @return broker ID
	 */
	public String getBrokerId()
	{
		return brokerId;
	}

	/**
	 * Gets the ID of this connection.
	 * 
	 * @return connection ID
	 */
	public String getConnectionId()
	{
		return connectionId;
	}
	
	@Override
	public String toString()
	{
		return "vm:" + brokerName + ":" + connectionId;
	}
}
