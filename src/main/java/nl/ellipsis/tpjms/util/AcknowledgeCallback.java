package nl.ellipsis.tpjms.util;

import javax.jms.JMSException;

public interface AcknowledgeCallback {
	void acknowledge(String messageID) throws JMSException;
}
