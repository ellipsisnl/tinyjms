package nl.ellipsis.tinyjms.provider;

import java.net.URI;

import javax.jms.JMSException;

/**
 * Interface for Tiny JMS providers.
 */
public interface TinyJmsProvider
{
	/**
	 * Connects to a provider.
	 * 
	 * @param uri
	 *          connection URI
	 * @param username
	 *          user name
	 * @param password
	 *          password
	 * @return connection context
	 * @throws JMSException
	 *           if connection fails
	 */
	public TinyJmsConnectionContext connect(URI uri, String username, String password) throws JMSException;

	/**
	 * Closes a connection to a provider.
	 * 
	 * @param context
	 *          connection context
	 * @throws JMSException
	 *           if close fails
	 */
	public void close(TinyJmsConnectionContext context) throws JMSException;
	
	public TinyJmsSessionContext createSession() throws JMSException;
}
