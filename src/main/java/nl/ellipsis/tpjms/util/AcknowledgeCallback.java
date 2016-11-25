package nl.ellipsis.tpjms.util;

import javax.jms.JMSException;

public interface AcknowledgeCallback {
	void notify(String messageId, String destinationName) throws JMSException;
}
