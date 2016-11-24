package nl.ellipsis.tpjms.core.connection;

import java.net.*;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import javax.jms.*;

import nl.ellipsis.tpjms.provider.*;

/**
 * TPJMS implementation of {@link ConnectionFactory}.
 */
public class TPJMSConnectionFactory implements ConnectionFactory, QueueConnectionFactory, TopicConnectionFactory {
	
	/**
	 * Lock used to gate access to configuration variables.
	 */
	private final ReentrantReadWriteLock configLock = new ReentrantReadWriteLock();

	/**
	 * Currently configured client ID. Must use configLock for access.
	 */
	private String clientID;

	/**
	 * Currently configured uri. Must use configLock for access.
	 */
	private URI uri;

	/**
	 * Currently configured user name. Must use configLock for access.
	 */
	private String defaultUserName;

	/**
	 * Currently configured password. Must use configLock for access.
	 */
	private String defaultPassword;
	
	/**
	 * Provider used by the factory
	 */
	private TPJMSProvider provider; 

	/**
	 * Creates a new JMS connection factory.
	 * 
	 * By default, the factory is wired to a local in-memory provider.
	 * 
	 * @throws JMSException
	 *             if the connection factory could not be instantiated
	 */
	public TPJMSConnectionFactory() throws JMSException {
		this("vm://default");
	}

	/**
	 * Creates a new JMS connection factory with the given URL.
	 * 
	 * @param url
	 *            connection URL
	 * @throws JMSException
	 *             if the connection factory could not be instantiated
	 */
	public TPJMSConnectionFactory(String url) throws JMSException {
		setUrl(url);
	}

	/**
	 * Sets the connection URL to use for new connections.
	 * 
	 * @param url
	 *            connection URL
	 * @throws JMSException
	 *             if the URL is invalid or a provider could not be located
	 */
	public void setUrl(String url) throws JMSException {
		try {
			configLock.writeLock().lock();

			if (url == null) {
				throw new InvalidUrlException("URL cannot be null");
			}

			URI tempUri;
			try {
				tempUri = new URI(url);
			} catch (URISyntaxException e) {
				throw new InvalidUrlException("URL is invalid: " + e.getMessage());
			}

			provider = ProviderRegistry.getProviderForUri(tempUri); // validate the URI

			uri = tempUri;
		}

		finally {
			configLock.writeLock().unlock();
		}
	}

	/**
	 * Sets the Client ID.
	 * 
	 * @param clientID
	 *            client ID
	 * @see Connection#setClientID(String)
	 */
	public void setClientID(String clientID) {
		try {
			configLock.writeLock().lock();
			this.clientID = clientID;
		} finally {
			configLock.writeLock().unlock();
		}
	}

	/**
	 * Sets the default user name.
	 * 
	 * @param userName
	 *            user name
	 */
	public void setUserName(String userName) {
		try {
			configLock.writeLock().lock();
			defaultUserName = userName;
		} finally {
			configLock.writeLock().unlock();
		}
	}

	/**
	 * Sets the default password.
	 * 
	 * @param password
	 *            password
	 */
	public void setPassword(String password) {
		try {
			configLock.writeLock().lock();
			defaultPassword = password;
		} finally {
			configLock.writeLock().unlock();
		}
	}

	/**
	 * Gets the Client ID.
	 * 
	 * @return client ID
	 * @see #setClientID(String)
	 */
	public String getClientID() {
		try {
			configLock.readLock().lock();
			return clientID;
		} finally {
			configLock.readLock().unlock();
		}
	}

	/*
	 * ConnectionFactory implementation
	 */

	/**
	 * Creates a connection with the default user identity. The connection is
	 * created in stopped mode. No messages will be delivered until the
	 * {@link Connection#start()} method is explicitly called.
	 * 
	 * @return a newly created connection
	 * @throws JMSException
	 *             if the JMS provider fails to create the connection due to
	 *             some internal error.
	 * @throws JMSSecurityException
	 *             if client authentication fails due to an invalid user name or
	 *             password.
	 * @since JMS 1.1
	 */
	@Override
	public Connection createConnection() throws JMSException, JMSSecurityException {
		TPJMSProvider currProvider;
		String currClientID;
		String currUserName;
		String currPassword;
		URI currUri;
		try {
			configLock.readLock().lock();
			currClientID = clientID;
			currUserName = defaultUserName;
			currPassword = defaultPassword;
			currUri = uri;
			currProvider = provider;
		} finally {
			configLock.readLock().unlock();
		}

		return new TPJMSConnection(currProvider, currClientID, currUri, currUserName, currPassword);
	}

	/**
	 * Creates a connection with the specified user identity. The connection is
	 * created in stopped mode. No messages will be delivered until the
	 * {@link Connection#start()} method is explicitly called.
	 * 
	 * @param userName
	 *            the caller's user name
	 * @param password
	 *            the caller's password
	 * @return a newly created connection
	 * @throws JMSException
	 *             if the JMS provider fails to create the connection due to
	 *             some internal error.
	 * @throws JMSSecurityException
	 *             if client authentication fails due to an invalid user name or
	 *             password.
	 * @since JMS 1.1
	 */
	@Override
	public Connection createConnection(String userName, String password) throws JMSException, JMSSecurityException {
		String currClientID;
		URI currUri;
		TPJMSProvider currProvider;

		try {
			configLock.readLock().lock();
			currClientID = clientID;
			currUri = uri;
			currProvider = provider;
		} finally {
			configLock.readLock().unlock();
		}

		return new TPJMSConnection(currProvider, currClientID, currUri, userName, password);
	}

	/*
	 * TopicConnectionFactory implementation
	 */

	/**
	 * @see #createConnection()
	 */
	@Override
	public TopicConnection createTopicConnection() throws JMSException {
		return (TopicConnection) createConnection();
	}

	/**
	 * @see #createConnection(String, String)
	 */
	@Override
	public TopicConnection createTopicConnection(String userName, String password) throws JMSException {
		return (TopicConnection) createConnection(userName, password);
	}

	/*
	 * QueueConnectionFactory implementation
	 */

	/**
	 * @see #createConnection()
	 */
	@Override
	public QueueConnection createQueueConnection() throws JMSException {
		return (QueueConnection) createConnection();
	}

	/**
	 * @see #createConnection(String, String)
	 */
	@Override
	public QueueConnection createQueueConnection(String userName, String password) throws JMSException {
		return (QueueConnection) createConnection(userName, password);
	}

}
