package nl.ellipsis.tpjms.core.session;

import javax.jms.*;
import org.apache.logging.log4j.*;

import nl.ellipsis.tpjms.core.destination.TPJMSTopic;

public class TPJMSTemporaryTopic extends TPJMSTopic implements TemporaryTopic
{
	private static final Logger logger = LogManager.getLogger(TPJMSTemporaryTopic.class);

	TPJMSTemporaryTopic(String temporaryTopicName)
	{
		super(temporaryTopicName);
	}

	@Override
	public final boolean isTemporary()
	{
		return true;
	}

	/**
	 * Deletes this temporary topic. If there are existing receivers still using
	 * it, a <code>JMSException</code> will be thrown.
	 * 
	 * @throws JMSException
	 *           if the JMS provider fails to delete the temporary topic due to
	 *           some internal error.
	 */
	@Override
	public void delete() throws JMSException
	{
		// TODO Auto-generated method stub
		logger.warn("delete() not implemented yet");
	}

}
