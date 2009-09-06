package com.randomcoder.tinyjms.client;

import java.io.Serializable;

import javax.jms.*;

public class TinyJmsObjectMessage extends TinyJmsMessage implements ObjectMessage
{

	@Override
	public Serializable getObject() throws JMSException
	{
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException();
	}

	@Override
	public void setObject(Serializable object) throws JMSException
	{
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException();		
	}

}