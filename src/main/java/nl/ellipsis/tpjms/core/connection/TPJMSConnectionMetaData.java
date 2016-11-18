package nl.ellipsis.tpjms.core.connection;

import java.io.Serializable;
import java.util.*;

import javax.jms.ConnectionMetaData;

/**
 * TinyJms implementation of {@link ConnectionMetaData}.
 */
public class TPJMSConnectionMetaData implements ConnectionMetaData, Serializable
{
	private static final long serialVersionUID = -470922197998456034L;

	protected TPJMSConnectionMetaData()
	{}

	/**
	 * Gets the JMS major version number.
	 * 
	 * @return the JMS API major version number
	 */
	@Override
	public int getJMSMajorVersion()
	{
		return 1;
	}

	/**
	 * Gets the JMS minor version number.
	 * 
	 * @return the JMS API minor version number
	 */
	@Override
	public int getJMSMinorVersion()
	{
		return 1;
	}

	/**
	 * Gets the JMS provider name.
	 * 
	 * @return the JMS provider name
	 */
	@Override
	public String getJMSProviderName()
	{
		return "TinyJms";
	}

	/**
	 * Gets the JMS API version.
	 * 
	 * @return the JMS API version
	 */
	@Override
	public String getJMSVersion()
	{
		return "1.1.1";
	}

	/**
	 * Gets an enumeration of the JMSX property names.
	 * 
	 * @return an Enumeration of JMSX property names
	 */
	@Override
	public Enumeration getJMSXPropertyNames()
	{
		return Collections.enumeration(Collections.emptyList());
	}

	/**
	 * Gets the JMS provider major version number.
	 * 
	 * @return the JMS provider major version number
	 */
	@Override
	public int getProviderMajorVersion()
	{
		return 1;
	}

	/**
	 * Gets the JMS provider minor version number.
	 * 
	 * @return the JMS provider minor version number
	 */
	@Override
	public int getProviderMinorVersion()
	{
		return 0;
	}

	/**
	 * Gets the JMS provider version.
	 * 
	 * @return the JMS provider version
	 */
	@Override
	public String getProviderVersion()
	{
		return "1.0-SNAPSHOT";
	}
}
