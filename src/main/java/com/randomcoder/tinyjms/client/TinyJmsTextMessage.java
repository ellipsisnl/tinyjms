package com.randomcoder.tinyjms.client;

import javax.jms.*;

public class TinyJmsTextMessage extends TinyJmsMessage implements TextMessage
{

	@Override
	public String getText() throws JMSException
	{
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException();
	}

	@Override
	public void setText(String string) throws JMSException
	{
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException();		
	}
}