package org.randomcoder.tinyjms.provider.vm;

import java.util.UUID;

public class VmBroker
{
	private final String brokerId;
	
	public VmBroker(String brokerName)
	{
		brokerId = "vm:" + brokerName + ":" + UUID.randomUUID().toString();
	}
	
	public String getBrokerId()
	{
		return brokerId;
	}
	
	@Override
	public String toString()
	{
		return brokerId;
	}
}
