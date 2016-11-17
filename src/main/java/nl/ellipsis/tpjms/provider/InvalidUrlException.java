package nl.ellipsis.tpjms.provider;

import javax.jms.JMSException;

/**
 * Exception thrown when a provider URL is invalid.
 */
public class InvalidUrlException extends JMSException
{
	private static final long serialVersionUID = -520625396983765566L;

	public InvalidUrlException(String reason)
	{
		super(reason);
	}

	public InvalidUrlException(String reason, String errorCode)
	{
		super(reason, errorCode);
	}
}
