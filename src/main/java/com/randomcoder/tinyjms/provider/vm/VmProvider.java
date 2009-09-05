package com.randomcoder.tinyjms.provider.vm;

import com.randomcoder.tinyjms.provider.TinyJmsProvider;

/**
 * In-memory provider.
 */
public class VmProvider implements TinyJmsProvider
{
	private static final VmProvider instance = new VmProvider();
	
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

	/**
	 * Removes the named broker from memory, freeing all resources.
	 *  
	 * @param brokerName
	 *          broker name
	 */
	public void removeBroker(String brokerName)
	{
		// TODO stub
	}
}