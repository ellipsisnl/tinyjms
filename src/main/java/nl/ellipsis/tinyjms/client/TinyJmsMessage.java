package nl.ellipsis.tinyjms.client;

import java.nio.charset.Charset;
import java.util.*;

import javax.jms.*;
import org.apache.logging.log4j.*;

public class TinyJmsMessage implements Message
{
	private static final Logger logger = LogManager.getLogger(TinyJmsMessage.class);
	
	/* Properties */
	private final Map<String, Object> properties = new HashMap<String, Object>();

	/* Headers */
	private String messageID = null;
	private String correlationID = null;
	private int deliveryMode = Message.DEFAULT_DELIVERY_MODE;
	private int priority = Message.DEFAULT_PRIORITY;
	private long expiration = 0;
	private long timestamp = 0;
	private boolean redelivered = false;
	private String type = null;
	
	/* Destinations */
	private Destination destination;
	private Destination replyTo;
	
	/* State */
	private boolean propertiesReadOnly = false;
	
	TinyJmsMessage()
	{}

	@Override
	public void acknowledge() throws JMSException
	{
		// TODO Auto-generated method stub
		logger.warn("acknowledge() not implemented");
	}

	/**
	 * Clears out the message body. Clearing a message's body does not clear its
	 * header values or property entries.
	 * 
	 * <p>
	 * If this message body was read-only, calling this method leaves the message
	 * body in the same state as an empty body in a newly created message.
	 * </p>
	 * 
	 * @throws JMSException
	 *           if the JMS provider fails to clear the message body due to some
	 *           internal error.
	 */
	@Override
	public void clearBody() throws JMSException
	{}

	/**
	 * Sets the content of the body as a byte array. Subclasses should override
	 * this method to provide efficient deserialization of message bodies.
	 * 
	 * @param data
	 *          byte array (may be <code>null</code>) of data to set.
	 * @throws JMSException
	 *           if an error occurs
	 */
	void setBody(byte[] data) throws JMSException
	{}

	/**
	 * Gets the content of the body as a byte array. Subclasses should override
	 * this method to provide efficient serialization of message bodies.
	 * 
	 * @return byte array (may be <code>null</code>)
	 * @throws JMSException
	 *           if an error occurs
	 */
	byte[] getBody() throws JMSException
	{
		return null;
	}
	
	/**
	 * Clears a message's properties.
	 * 
	 * <p>
	 * The message's header fields and body are not cleared.
	 * </p>
	 * 
	 * @throws JMSException
	 *           if the JMS provider fails to clear the message properties due to
	 *           some internal error.
	 */
	@Override
	public void clearProperties() throws JMSException
	{
		properties.clear();
		propertiesReadOnly = false;
	}

	/**
	 * Returns the value of the boolean property with the specified name.
	 * 
	 * @param name
	 *          the name of the boolean property
	 * @return the boolean property value for the specified name
	 * @throws JMSException
	 *           if the JMS provider fails to get the property value due to some
	 *           internal error.
	 * @throws MessageFormatException
	 *           if this type conversion is invalid.
	 */
	@Override
	public boolean getBooleanProperty(String name) throws JMSException
	{
		Object value = properties.get(name);

		if (value == null || value instanceof String)
		{
			return Boolean.valueOf((String) value);
		}

		if (value instanceof Boolean)
		{
			return (Boolean) value;
		}
		
		throw new MessageFormatException("Unsupported conversion to boolean from type: " + value.getClass().getName());
	}

	/**
	 * Returns the value of the byte property with the specified name.
	 * 
	 * @param name
	 *          the name of the byte property
	 * @return the byte property value for the specified name
	 * @throws JMSException
	 *           if the JMS provider fails to get the property value due to some
	 *           internal error.
	 * @throws MessageFormatException
	 *           if this type conversion is invalid.
	 */
	@Override
	public byte getByteProperty(String name) throws JMSException
	{
		Object value = properties.get(name);
		if (value instanceof Byte)
		{
			return (Byte) value;
		}
		
		if (value == null || value instanceof String)
		{
			return Byte.valueOf((String) value);
		}

		throw new MessageFormatException("Unsupported conversion to byte from type: " + value.getClass().getName());
	}

	/**
	 * Returns the value of the double property with the specified name.
	 * 
	 * @param name
	 *          the name of the double property
	 * @return the double property value for the specified name
	 * @throws JMSException
	 *           if the JMS provider fails to get the property value due to some
	 *           internal error.
	 * @throws MessageFormatException
	 *           if this type conversion is invalid.
	 */
	@Override
	public double getDoubleProperty(String name) throws JMSException
	{
		Object value = properties.get(name);
		if (value instanceof Double)
		{
			return (Double) value;
		}
		
		if (value instanceof Float)
		{
			return (Float) value;
		}
		
		if (value == null || value instanceof String)
		{
			return Double.valueOf((String) value);
		}

		throw new MessageFormatException("Unsupported conversion to double from type: " + value.getClass().getName());
	}

	/**
	 * Returns the value of the float property with the specified name.
	 * 
	 * @param name
	 *          the name of the float property
	 * @return the float property value for the specified name
	 * @throws JMSException
	 *           if the JMS provider fails to get the property value due to some
	 *           internal error.
	 * @throws MessageFormatException
	 *           if this type conversion is invalid.
	 */
	@Override
	public float getFloatProperty(String name) throws JMSException
	{
		Object value = properties.get(name);
		
		if (value instanceof Float)
		{
			return (Float) value;
		}
		
		if (value == null || value instanceof String)
		{
			return Float.valueOf((String) value);
		}

		throw new MessageFormatException("Unsupported conversion to float from type: " + value.getClass().getName());
	}

	/**
	 * Returns the value of the int property with the specified name.
	 * 
	 * @param name
	 *          the name of the int property
	 * @return the int property value for the specified name
	 * @throws JMSException
	 *           if the JMS provider fails to get the property value due to some
	 *           internal error.
	 * @throws MessageFormatException
	 *           if this type conversion is invalid.
	 */
	@Override
	public int getIntProperty(String name) throws JMSException
	{
		Object value = properties.get(name);
		
		if (value instanceof Integer)
		{
			return (Integer) value;
		}

		if (value instanceof Short)
		{
			return (Short) value;
		}

		if (value instanceof Byte)
		{
			return (Byte) value;
		}

		if (value == null || value instanceof String)
		{
			return Integer.valueOf((String) value);
		}

		throw new MessageFormatException("Unsupported conversion to int from type: " + value.getClass().getName());
	}

	/**
	 * Gets the correlation ID for the message.
	 * 
	 * <p>
	 * This method is used to return correlation ID values that are either
	 * provider-specific message IDs or application-specific <code>String</code>
	 * values.
	 * </p>
	 * 
	 * @return the correlation ID of a message as a String
	 * @throws JMSException
	 *           if the JMS provider fails to get the correlation ID due to some
	 *           internal error.
	 * @see #setJMSCorrelationID(String)
	 * @see #getJMSCorrelationIDAsBytes()
	 * @see #setJMSCorrelationIDAsBytes(byte[])
	 */
	@Override
	public String getJMSCorrelationID() throws JMSException
	{
		return correlationID;
	}

	/**
	 * Gets the correlation ID as an array of bytes for the message.
	 * 
	 * <p>
	 * The use of a byte[] value for JMSCorrelationID is non-portable.
	 * </p>
	 * 
	 * @return the correlation ID of a message as an array of bytes
	 * @throws JMSException
	 *           if the JMS provider fails to get the correlation ID due to some
	 *           internal error.
	 * @see #setJMSCorrelationID(String)
	 * @see #getJMSCorrelationID()
	 * @see #setJMSCorrelationIDAsBytes(byte[])
	 */
	@Override
	public byte[] getJMSCorrelationIDAsBytes() throws JMSException
	{
		return (correlationID == null) ? null : correlationID.getBytes(Charset.forName("UTF-8"));
	}

	/**
	 * Gets the DeliveryMode value specified for this message.
	 * 
	 * @return the delivery mode for this message
	 * @throws JMSException
	 *           if the JMS provider fails to get the delivery mode due to some
	 *           internal error.
	 * @see #setJMSDeliveryMode(int)
	 * @see DeliveryMode
	 */
	@Override
	public int getJMSDeliveryMode() throws JMSException
	{
		return deliveryMode;
	}

	/**
	 * Gets the Destination object for this message.
	 * 
	 * <p>
	 * The <code>JMSDestination</code> header field contains the destination to
	 * which the message is being sent.
	 * </p>
	 * 
	 * <p>
	 * When a message is sent, this field is ignored. After completion of the send
	 * or publish method, the field holds the destination specified by the method.
	 * </p>
	 * 
	 * <p>
	 * When a message is received, its <code>JMSDestination</code> value must be
	 * equivalent to the value assigned when it was sent.
	 * </p>
	 * 
	 * @return the destination of this message
	 * @throws JMSException
	 *           if the JMS provider fails to get the destination due to some
	 *           internal error.
	 * @see #setJMSDestination(Destination)
	 */
	@Override
	public Destination getJMSDestination() throws JMSException
	{
		return destination;
	}

	/**
	 * Gets the message's expiration value.
	 * 
	 * <p>
	 * When a message is sent, the JMSExpiration header field is left unassigned.
	 * After completion of the send or publish method, it holds the expiration
	 * time of the message. This is the sum of the time-to-live value specified by
	 * the client and the GMT at the time of the send or publish.
	 * </p>
	 * 
	 * <p>
	 * If the time-to-live is specified as zero, JMSExpiration is set to zero to
	 * indicate that the message does not expire.
	 * </p>
	 * 
	 * <p>
	 * When a message's expiration time is reached, a provider should discard it.
	 * The JMS API does not define any form of notification of message expiration.
	 * </p>
	 * 
	 * <p>
	 * Clients should not receive messages that have expired; however, the JMS API
	 * does not guarantee that this will not happen.
	 * </p>
	 * 
	 * @return the time the message expires, which is the sum of the time-to-live
	 *         value specified by the client and the GMT at the time of the send
	 * @throws JMSException
	 *           if the JMS provider fails to get the message expiration due to
	 *           some internal error.
	 * @see #setJMSExpiration(long)
	 */
	@Override
	public long getJMSExpiration() throws JMSException
	{
		return expiration;
	}

	/**
	 * Gets the message ID.
	 * 
	 * <p>
	 * The JMSMessageID header field contains a value that uniquely identifies
	 * each message sent by a provider.
	 * </p>
	 * 
	 * <p>
	 * When a message is sent, JMSMessageID can be ignored. When the send or
	 * publish method returns, it contains a provider-assigned value.
	 * </p>
	 * 
	 * <p>
	 * A JMSMessageID is a String value that should function as a unique key for
	 * identifying messages in a historical repository. The exact scope of
	 * uniqueness is provider-defined. It should at least cover all messages for a
	 * specific installation of a provider, where an installation is some
	 * connected set of message routers.
	 * </p>
	 * 
	 * <p>
	 * All JMSMessageID values must start with the prefix 'ID:'. Uniqueness of
	 * message ID values across different providers is not required.
	 * </p>
	 * 
	 * <p>
	 * Since message IDs take some effort to create and increase a message's size,
	 * some JMS providers may be able to optimize message overhead if they are
	 * given a hint that the message ID is not used by an application. By calling
	 * the MessageProducer.setDisableMessageID method, a JMS client enables this
	 * potential optimization for all messages sent by that message producer. If
	 * the JMS provider accepts this hint, these messages must have the message ID
	 * set to null; if the provider ignores the hint, the message ID must be set
	 * to its normal unique value.
	 * </p>
	 * 
	 * @return the message ID
	 * @throws JMSException
	 *           if the JMS provider fails to get the message ID due to some
	 *           internal error.
	 * @see #setJMSMessageID(String)
	 * @see MessageProducer#setDisableMessageID(boolean)
	 */
	@Override
	public String getJMSMessageID() throws JMSException
	{
		return messageID;
	}

	/**
	 * Gets the message priority level.
	 * 
	 * <p>
	 * The JMS API defines ten levels of priority value, with 0 as the lowest
	 * priority and 9 as the highest. In addition, clients should consider
	 * priorities 0-4 as gradations of normal priority and priorities 5-9 as
	 * gradations of expedited priority.
	 * </p>
	 * 
	 * <p>
	 * The JMS API does not require that a provider strictly implement priority
	 * ordering of messages; however, it should do its best to deliver expedited
	 * messages ahead of normal messages.
	 * </p>
	 * 
	 * @return the default message priority
	 * @throws JMSException
	 *           if the JMS provider fails to get the message priority due to some
	 *           internal error.
	 * @see #setJMSPriority(int)
	 */
	@Override
	public int getJMSPriority() throws JMSException
	{
		return priority;
	}

	/**
	 * Gets an indication of whether this message is being redelivered.
	 * 
	 * <p>
	 * If a client receives a message with the JMSRedelivered field set, it is
	 * likely, but not guaranteed, that this message was delivered earlier but
	 * that its receipt was not acknowledged at that time.
	 * </p>
	 * 
	 * @return <code>true</code> if this message is being redelivered
	 * @throws JMSException
	 *           if the JMS provider fails to get the redelivered state due to
	 *           some internal error.
	 * @see #setJMSRedelivered(boolean)
	 */
	@Override
	public boolean getJMSRedelivered() throws JMSException
	{
		return redelivered;
	}

	/**
	 * Gets the <code>Destination</code> object to which a reply to this message
	 * should be sent.
	 * 
	 * @return <code>Destination</code> to which to send a response to this
	 *         message
	 * @throws JMSException
	 *           if the JMS provider fails to get the JMSReplyTo destination due
	 *           to some internal error.
	 * @see #setJMSReplyTo(Destination)
	 */
	@Override
	public Destination getJMSReplyTo() throws JMSException
	{
		return replyTo;
	}

	/**
	 * Gets the message timestamp.
	 * 
	 * <p>
	 * The <code>JMSTimestamp</code> header field contains the time a message was
	 * handed off to a provider to be sent. It is not the time the message was
	 * actually transmitted, because the actual send may occur later due to
	 * transactions or other client-side queueing of messages.
	 * </p>
	 * 
	 * <p>
	 * When a message is sent, <code>JMSTimestamp</code> is ignored. When the send
	 * or publish method returns, it contains a time value somewhere in the
	 * interval between the call and the return. The value is in the format of a
	 * normal millis time value in the Java programming language.
	 * </p>
	 * 
	 * <p>
	 * Since timestamps take some effort to create and increase a message's size,
	 * some JMS providers may be able to optimize message overhead if they are
	 * given a hint that the timestamp is not used by an application. By calling
	 * the {@link MessageProducer#setDisableMessageTimestamp(boolean)} method, a
	 * JMS client enables this potential optimization for all messages sent by
	 * that message producer. If the JMS provider accepts this hint, these
	 * messages must have the timestamp set to zero; if the provider ignores the
	 * hint, the timestamp must be set to its normal value.
	 * </p>
	 * 
	 * @return the message timestamp
	 * @throws JMSException
	 *           if the JMS provider fails to get the timestamp due to some
	 *           internal error.
	 * @see #setJMSTimestamp(long)
	 * @see MessageProducer#setDisableMessageTimestamp(boolean)
	 */
	@Override
	public long getJMSTimestamp() throws JMSException
	{
		return timestamp;
	}

	/**
	 * Gets the message type identifier supplied by the client when the message
	 * was sent.
	 * 
	 * @return the message type
	 * @throws JMSException
	 *           if the JMS provider fails to get the message type due to some
	 *           internal error.
	 * @see #setJMSType(String)
	 */
	@Override
	public String getJMSType() throws JMSException
	{
		return type;
	}

	/**
	 * Returns the value of the long property with the specified name.
	 * 
	 * @param name
	 *          the name of the long property
	 * @return the long property value for the specified name
	 * @throws JMSException
	 *           if the JMS provider fails to get the property value due to some
	 *           internal error.
	 * @throws MessageFormatException
	 *           if this type conversion is invalid.
	 */
	@Override
	public long getLongProperty(String name) throws JMSException
	{
		Object value = properties.get(name);
		
		if (value instanceof Long)
		{
			return (Long) value;
		}

		if (value instanceof Integer)
		{
			return (Integer) value;
		}

		if (value instanceof Short)
		{
			return (Short) value;
		}

		if (value instanceof Byte)
		{
			return (Byte) value;
		}

		if (value == null || value instanceof String)
		{
			return Long.valueOf((String) value);
		}

		throw new MessageFormatException("Unsupported conversion to long from type: " + value.getClass().getName());
	}

	/**
	 * Returns the value of the Java object property with the specified name.
	 * 
	 * <p>
	 * This method can be used to return, in objectified format, an object that
	 * has been stored as a property in the message with the equivalent
	 * setObjectProperty method call, or its equivalent primitive settypeProperty
	 * method.
	 * </p>
	 * 
	 * @param name
	 *          the name of the Java object property
	 * @return the Java object property value with the specified name, in
	 *         objectified format (for example, if the property was set as an int,
	 *         an Integer is returned); if there is no property by this name, a
	 *         null value is returned
	 * @throws JMSException
	 *           if the JMS provider fails to get the property value due to some
	 *           internal error.
	 */
	@Override
	public Object getObjectProperty(String name) throws JMSException
	{
		Object value = properties.get(name);
		
		if (value == null)
		{
			return null;
		}
		
		if (value instanceof String)
		{
			return value;
		}
		
		if (value instanceof Double)
		{
			return value;
		}
		
		if (value instanceof Float)
		{
			return value;
		}
		
		if (value instanceof Long)
		{
			return value;
		}
		
		if (value instanceof Integer)
		{
			return value;
		}
		
		if (value instanceof Short)
		{
			return value;
		}
		
		if (value instanceof Byte)
		{
			return value;
		}
		
		if (value instanceof Boolean)
		{
			return value;
		}
		
		return null;
	}

	/**
	 * Returns an <code>Enumeration</code> of all the property names.
	 * 
	 * <p>
	 * Note that JMS standard header fields are not considered properties and are
	 * not returned in this enumeration.
	 * </p>
	 * 
	 * @return an enumeration of all the names of property values
	 * @throws JMSException
	 *           if the JMS provider fails to get the property names due to some
	 *           internal error.
	 */
	@Override
	public Enumeration getPropertyNames() throws JMSException
	{
		return Collections.enumeration(properties.keySet());
	}

	/**
	 * Returns the value of the short property with the specified name.
	 * 
	 * @param name
	 *          the name of the short property
	 * @return the short property value for the specified name
	 * @throws JMSException
	 *           if the JMS provider fails to get the property value due to some
	 *           internal error.
	 * @throws MessageFormatException
	 *           if this type conversion is invalid.
	 */
	@Override
	public short getShortProperty(String name) throws JMSException
	{
		Object value = properties.get(name);
		
		if (value instanceof Short)
		{
			return (Short) value;
		}

		if (value instanceof Byte)
		{
			return (Byte) value;
		}

		if (value == null || value instanceof String)
		{
			return Short.valueOf((String) value);
		}

		throw new MessageFormatException("Unsupported conversion to short from type: " + value.getClass().getName());
	}

	/**
	 * Returns the value of the <code>String</code> property with the specified
	 * name.
	 * 
	 * @param name
	 *          the name of the <code>String</code> property
	 * @return the String property value for the specified name; if there is no
	 *         property by this name, a <code>null</code> value is returned
	 * @throws JMSException
	 *           if the JMS provider fails to get the property value due to some
	 *           internal error.
	 * @throws MessageFormatException
	 *           if this type conversion is invalid.
	 */
	@Override
	public String getStringProperty(String name) throws JMSException
	{
		Object value = properties.get(name);
		
		if (value == null)
		{
			return null;
		}
		
		if (value instanceof String)
		{
			return (String) value;
		}
		
		if (value instanceof Double)
		{
			return Double.toString((Double) value);
		}
		
		if (value instanceof Float)
		{
			return Float.toString((Float) value);
		}
		
		if (value instanceof Long)
		{
			return Long.toString((Long) value);
		}
		
		if (value instanceof Integer)
		{
			return Integer.toString((Integer) value);
		}
		
		if (value instanceof Short)
		{
			return Short.toString((Short) value);
		}
		
		if (value instanceof Byte)
		{
			return Byte.toString((Byte) value);
		}
		
		if (value instanceof Boolean)
		{
			return Boolean.toString((Boolean) value);
		}
		
		throw new MessageFormatException("Unsupported conversion to String from type: " + value.getClass().getName());
	}

	/**
	 * Indicates whether a property value exists.
	 * 
	 * @param name
	 *          the name of the property to test
	 * @return <code>true</code> if the property exists
	 * @throws JMSException
	 *           if the JMS provider fails to determine if the property exists due
	 *           to some internal error.
	 */
	@Override
	public boolean propertyExists(String name) throws JMSException
	{
		return properties.containsKey(name);
	}

	/**
	 * Sets a boolean property value with the specified name into the message.
	 * 
	 * @param name
	 *          the name of the boolean property
	 * @param value
	 *          the boolean property value to set
	 * @throws JMSException
	 *           if the JMS provider fails to set the property due to some
	 *           internal error.
	 * @param IllegalArgumentException
	 *          if the name is <code>null</code> or if the name is an empty
	 *          string.
	 * @param MessageNotWriteableException
	 *          if properties are read-only
	 */
	@Override
	public void setBooleanProperty(String name, boolean value) throws JMSException
	{
		setObjectProperty(name, value);
	}

	/**
	 * Sets a byte property value with the specified name into the message.
	 * 
	 * @param name
	 *          the name of the byte property
	 * @param value
	 *          the byte property value to set
	 * @throws JMSException
	 *           if the JMS provider fails to set the property due to some
	 *           internal error.
	 * @param IllegalArgumentException
	 *          if the name is <code>null</code> or if the name is an empty
	 *          string.
	 * @param MessageNotWriteableException
	 *          if properties are read-only
	 */
	@Override
	public void setByteProperty(String name, byte value) throws JMSException
	{
		setObjectProperty(name, value);
	}

	/**
	 * Sets a double property value with the specified name into the message.
	 * 
	 * @param name
	 *          the name of the double property
	 * @param value
	 *          the double property value to set
	 * @throws JMSException
	 *           if the JMS provider fails to set the property due to some
	 *           internal error.
	 * @param IllegalArgumentException
	 *          if the name is <code>null</code> or if the name is an empty
	 *          string.
	 * @param MessageNotWriteableException
	 *          if properties are read-only
	 */
	@Override
	public void setDoubleProperty(String name, double value) throws JMSException
	{
		setObjectProperty(name, value);
	}

	/**
	 * Sets a float property value with the specified name into the message.
	 * 
	 * @param name
	 *          the name of the float property
	 * @param value
	 *          the float property value to set
	 * @throws JMSException
	 *           if the JMS provider fails to set the property due to some
	 *           internal error.
	 * @param IllegalArgumentException
	 *          if the name is <code>null</code> or if the name is an empty
	 *          string.
	 * @param MessageNotWriteableException
	 *          if properties are read-only
	 */
	@Override
	public void setFloatProperty(String name, float value) throws JMSException
	{
		setObjectProperty(name, value);
	}

	/**
	 * Sets an int property value with the specified name into the message.
	 * 
	 * @param name
	 *          the name of the int property
	 * @param value
	 *          the int property value to set
	 * @throws JMSException
	 *           if the JMS provider fails to set the property due to some
	 *           internal error.
	 * @param IllegalArgumentException
	 *          if the name is <code>null</code> or if the name is an empty
	 *          string.
	 * @param MessageNotWriteableException
	 *          if properties are read-only
	 */
	@Override
	public void setIntProperty(String name, int value) throws JMSException
	{
		setObjectProperty(name, value);
	}

	/**
	 * Sets the correlation ID for the message.
	 * 
	 * <p>
	 * A client can use the <code>JMSCorrelationID</code> header field to link one
	 * message with another. A typical use is to link a response message with its
	 * request message.
	 * </p>
	 * 
	 * <p>
	 * <code>JMSCorrelationID</code> can hold one of the following:
	 * </p>
	 * 
	 * <ul>
	 * <li>A provider-specific message ID</li>
	 * <li>An application-specific String </il>
	 * <li>A provider-native byte[] value</li>
	 * </ul>
	 * 
	 * <p>
	 * Since each message sent by a JMS provider is assigned a message ID value,
	 * it is convenient to link messages via message ID. All message ID values
	 * must start with the 'ID:' prefix.
	 * </p>
	 * 
	 * <p>
	 * In some cases, an application (made up of several clients) needs to use an
	 * application-specific value for linking messages. For instance, an
	 * application may use <code>JMSCorrelationID</code> to hold a value
	 * referencing some external information. Application-specified values must
	 * not start with the 'ID:' prefix; this is reserved for provider-generated
	 * message ID values.
	 * </p>
	 * 
	 * <p>
	 * If a provider supports the native concept of correlation ID, a JMS client
	 * may need to assign specific <code>JMSCorrelationID</code> values to match
	 * those expected by clients that do not use the JMS API. A byte[] value is
	 * used for this purpose. JMS providers without native correlation ID values
	 * are not required to support byte[] values. The use of a byte[] value for
	 * <code>JMSCorrelationID</code> is non-portable.
	 * </p>
	 * 
	 * @param correlationID
	 *          the message ID of a message being referred to
	 * @throws JMSException
	 *           if the JMS provider fails to set the correlation ID due to some
	 *           internal error.
	 * @see #getJMSCorrelationID()
	 * @see #getJMSCorrelationIDAsBytes()
	 * @see #setJMSCorrelationIDAsBytes(byte[])
	 */
	@Override
	public void setJMSCorrelationID(String correlationID) throws JMSException
	{
		this.correlationID = correlationID;
	}

	/**
	 * Sets the correlation ID as an array of bytes for the message.
	 * 
	 * <p>
	 * This method is not supported by this provider.
	 * </p>
	 * 
	 * @param correlationID
	 *          the correlation ID value as an array of bytes
	 * @throws UnsupportedOperationException
	 *           always
	 * @see #setJMSCorrelationID(String)
	 * @see #getJMSCorrelationID()
	 * @see #getJMSCorrelationIDAsBytes()
	 */
	@Override
	public void setJMSCorrelationIDAsBytes(byte[] correlationID) throws UnsupportedOperationException
	{
		throw new UnsupportedOperationException();
	}

	/**
	 * Sets the <code>DeliveryMode</code> value for this message.
	 * 
	 * <p>
	 * JMS providers set this field when a message is sent. This method can be
	 * used to change the value for a message that has been received.
	 * </p>
	 * 
	 * @param deliveryMode
	 *          the delivery mode for this message
	 * @throws JMSException
	 *           if the JMS provider fails to set the delivery mode due to some
	 *           internal error.
	 * @see #getJMSDeliveryMode()
	 * @see DeliveryMode
	 */
	@Override
	public void setJMSDeliveryMode(int deliveryMode) throws JMSException
	{
		if (deliveryMode != DeliveryMode.PERSISTENT &&
				deliveryMode != DeliveryMode.NON_PERSISTENT)
		{
			throw new JMSException("Invalid deliveryMode: " + deliveryMode);
		}
		
		this.deliveryMode = deliveryMode;
	}

	/**
	 * Sets the <code>Destination</code> object for this message.
	 * 
	 * <p>
	 * JMS providers set this field when a message is sent. This method can be
	 * used to change the value for a message that has been received.
	 * </p>
	 * 
	 * @param destination
	 *          the destination for this message
	 * @throws JMSException
	 *           if the JMS provider fails to set the destination due to some
	 *           internal error
	 * @see #getJMSDestination()
	 */
	@Override
	public void setJMSDestination(Destination destination) throws JMSException
	{
		this.destination = destination;
	}

	/**
	 * Sets the message's expiration value.
	 * 
	 * <p>
	 * JMS providers set this field when a message is sent. This method can be
	 * used to change the value for a message that has been received.
	 * </p>
	 * 
	 * @param expiration
	 *          the message's expiration time
	 * @throws JMSException
	 *           if the JMS provider fails to set the message expiration due to
	 *           some internal error.
	 * @see #getJMSExpiration()
	 */
	@Override
	public void setJMSExpiration(long expiration) throws JMSException
	{
		this.expiration = expiration;
	}

	/**
	 * Sets the message ID.
	 * 
	 * <p>
	 * JMS providers set this field when a message is sent. This method can be
	 * used to change the value for a message that has been received.
	 * </p>
	 * 
	 * @param id
	 *          the ID of the message
	 * @throws JMSException
	 *           if the JMS provider fails to set the message ID due to some
	 *           internal error.
	 * @see #getJMSMessageID()
	 */
	@Override
	public void setJMSMessageID(String id) throws JMSException
	{
		if (id != null && !id.startsWith("ID:"))
		{
			throw new JMSException("Invalid message ID: " + id);
		}
		
		this.messageID = id;
	}

	/**
	 * Sets the priority level for this message.
	 * 
	 * <p>
	 * JMS providers set this field when a message is sent. This method can be
	 * used to change the value for a message that has been received.
	 * </p>
	 * 
	 * @param priority
	 *          the priority of this message
	 * @throws JMSException
	 *           if the JMS provider fails to set the message priority due to some
	 *           internal error.
	 * @see #getJMSPriority()
	 */
	@Override
	public void setJMSPriority(int priority) throws JMSException
	{
		if (priority < 0 || priority > 9)
		{
			throw new JMSException("Invalid priority: " + priority);
		}
		
		this.priority = priority;
	}

	/**
	 * Specifies whether this message is being redelivered.
	 * 
	 * <p>
	 * This field is set at the time the message is delivered. This method can be
	 * used to change the value for a message that has been received.
	 * </p>
	 * 
	 * @param redelivered
	 *          an indication of whether this message is being redelivered
	 * @throws JMSException
	 *           if the JMS provider fails to set the redelivered state due to
	 *           some internal error
	 * @see #getJMSRedelivered()
	 */
	@Override
	public void setJMSRedelivered(boolean redelivered) throws JMSException
	{
		this.redelivered = redelivered;
	}

	/**
	 * Sets the <code>Destination</code> object to which a reply to this message
	 * should be sent.
	 * 
	 * <p>
	 * The <code>JMSReplyTo</code> header field contains the destination where a
	 * reply to the current message should be sent. If it is <code>null</code>, no
	 * reply is expected. The destination may be either a <code>Queue</code>
	 * object or a <code>Topic</code> object.
	 * </p>
	 * 
	 * <p>
	 * Messages sent with a <code>null</code> <code>JMSReplyTo</code> value may be
	 * a notification of some event, or they may just be some data the sender
	 * thinks is of interest.
	 * </p>
	 * 
	 * <p>
	 * Messages with a <code>JMSReplyTo</code> value typically expect a response.
	 * A response is optional; it is up to the client to decide. These messages
	 * are called requests. A message sent in response to a request is called a
	 * reply.
	 * </p>
	 * 
	 * <p>
	 * In some cases a client may wish to match a request it sent earlier with a
	 * reply it has just received. The client can use the
	 * <code>JMSCorrelationID</code> header field for this purpose.
	 * </p>
	 * 
	 * @param replyTo
	 *          Destination to which to send a response to this message
	 * @throws JMSException
	 *           if the JMS provider fails to set the <code>JMSReplyTo</code>
	 *           destination due to some internal error.
	 * @see #getJMSReplyTo()
	 */
	@Override
	public void setJMSReplyTo(Destination replyTo) throws JMSException
	{
		this.replyTo = replyTo;
	}

	/**
	 * Sets the message timestamp.
	 * 
	 * <p>
	 * JMS providers set this field when a message is sent. This method can be
	 * used to change the value for a message that has been received.
	 * </p>
	 * 
	 * @param timestamp
	 *          the timestamp for this message
	 * @throws JMSException
	 *           if the JMS provider fails to set the timestamp due to some
	 *           internal error.
	 * @see #getJMSTimestamp()
	 */
	@Override
	public void setJMSTimestamp(long timestamp) throws JMSException
	{
		this.timestamp = timestamp;
	}

	/**
	 * Sets the message type.
	 * 
	 * <p>
	 * Some JMS providers use a message repository that contains the definitions
	 * of messages sent by applications. The <code>JMSType</code> header field may
	 * reference a message's definition in the provider's repository.
	 * </p>
	 * 
	 * <p>
	 * The JMS API does not define a standard message definition repository, nor
	 * does it define a naming policy for the definitions it contains.
	 * </p>
	 * 
	 * <p>
	 * Some messaging systems require that a message type definition for each
	 * application message be created and that each message specify its type. In
	 * order to work with such JMS providers, JMS clients should assign a value to
	 * <code>JMSType</code>, whether the application makes use of it or not. This
	 * ensures that the field is properly set for those providers that require it.
	 * </p>
	 * 
	 * <p>
	 * To ensure portability, JMS clients should use symbolic values for
	 * <code>JMSType</code> that can be configured at installation time to the
	 * values defined in the current provider's message repository. If string
	 * literals are used, they may not be valid type names for some JMS providers.
	 * </p>
	 * 
	 * @param type
	 *          the message type
	 * @throws JMSException
	 *           if the JMS provider fails to set the message type due to some
	 *           internal error.
	 * @see #getJMSType()
	 */
	@Override
	public void setJMSType(String type) throws JMSException
	{
		this.type = type;
	}

	/**
	 * Sets a long property value with the specified name into the message.
	 * 
	 * @param name
	 *          the name of the long property
	 * @param value
	 *          the long property value to set
	 * @throws JMSException
	 *           if the JMS provider fails to set the property due to some
	 *           internal error.
	 * @param IllegalArgumentException
	 *          if the name is <code>null</code> or if the name is an empty
	 *          string.
	 * @param MessageNotWriteableException
	 *          if properties are read-only
	 */
	@Override
	public void setLongProperty(String name, long value) throws JMSException
	{
		setObjectProperty(name, value);
	}

	/**
	 * Sets a Java object property value with the specified name into the message.
	 * 
	 * <p>
	 * Note that this method works only for the objectified primitive object types
	 * (Integer, Double, Long ...) and String objects.
	 * </p>
	 * 
	 * @param name
	 *          the name of the Java object property
	 * @param value
	 *          the Java object property value to set
	 * @throws JMSException
	 *           if the JMS provider fails to set the property due to some
	 *           internal error.
	 * @throws IllegalArgumentException
	 *           if the name is <code>null</code> or if the name is an empty
	 *           string.
	 * @throws MessageFormatException
	 *           if the object is invalid
	 * @throws MessageNotWriteableException
	 *           if properties are read-only
	 */
	@Override
	public void setObjectProperty(String name, Object value) throws JMSException
	{
		if (name == null || name.length() == 0)
		{
			throw new IllegalArgumentException("name is required");
		}
		
		if (propertiesReadOnly)
		{
			throw new MessageNotWriteableException("Message properties are read-only");
		}

		if (value != null &&
				!(value instanceof Boolean) &&
				!(value instanceof Byte) &&
				!(value instanceof Short) &&
				!(value instanceof Integer) &&
				!(value instanceof Long) &&
				!(value instanceof Float) &&
				!(value instanceof Double) &&
				!(value instanceof String))
		{
			throw new MessageFormatException("Illegal property type: " + value.getClass().getName()); 
		}
		
		properties.put(name, value);
	}

	/**
	 * Sets a short property value with the specified name into the message.
	 * 
	 * @param name
	 *          the name of the short property
	 * @param value
	 *          the short property value to set
	 * @throws JMSException
	 *           if the JMS provider fails to set the property due to some
	 *           internal error.
	 * @param IllegalArgumentException
	 *          if the name is <code>null</code> or if the name is an empty
	 *          string.
	 * @param MessageNotWriteableException
	 *          if properties are read-only
	 */
	@Override
	public void setShortProperty(String name, short value) throws JMSException
	{
		setObjectProperty(name, value);
	}

	/**
	 * Sets a String property value with the specified name into the message.
	 * 
	 * @param name
	 *          the name of the String property
	 * @param value
	 *          the String property value to set
	 * @throws JMSException
	 *           if the JMS provider fails to set the property due to some
	 *           internal error.
	 * @param IllegalArgumentException
	 *          if the name is <code>null</code> or if the name is an empty
	 *          string.
	 * @param MessageNotWriteableException
	 *          if properties are read-only
	 */
	@Override
	public void setStringProperty(String name, String value) throws JMSException
	{
		setObjectProperty(name, value);
	}

	/**
	 * Sets the read-only flag for this message's properties.
	 * 
	 * @param value
	 *          <code>true</code> if properties are read-only
	 */
	void setPropertiesReadOnly(boolean value)
	{
		this.propertiesReadOnly = value;
	}
	
}
