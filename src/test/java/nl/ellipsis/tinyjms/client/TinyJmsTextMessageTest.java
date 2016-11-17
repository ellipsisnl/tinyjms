package nl.ellipsis.tinyjms.client;

import static org.junit.Assert.*;

import java.nio.charset.Charset;

import javax.jms.*;

import nl.ellipsis.tinyjms.client.TinyJmsTextMessage;

import org.junit.*;

public class TinyJmsTextMessageTest
{
	private TinyJmsTextMessage message;
	
	@Before
	public void setUp() throws Exception
	{
		message = new TinyJmsTextMessage();
	}

	@After
	public void tearDown() throws Exception
	{
		message = null;
	}

	@Test
	public void testNullText() throws JMSException
	{
		assertNull(message.getText());
		message.setText(null);
		assertNull(message.getText());
	}

	@Test
	public void testText() throws JMSException
	{
		message.setText("TEST");
		assertEquals("TEST", message.getText());
	}

	@Test(expected=MessageNotWriteableException.class)
	public void testReadOnly() throws JMSException
	{
		message.setReadOnly(true);
		message.setText("TEST");
	}

	@Test
	public void testClearBody() throws JMSException
	{
		message.setReadOnly(false);
		message.setText("TEST");
		message.clearBody();
		assertNull(message.getText());
	}

	@Test
	public void testBody() throws JMSException
	{
		assertNull(message.getBody());
		message.setBody("TEST".getBytes(Charset.forName("UTF-8")));
		assertEquals("TEST", new String(message.getBody(), Charset.forName("UTF-8")));
	}

	@Test
	public void testClearBodyReadOnly() throws JMSException
	{
		message.setReadOnly(true);
		message.clearBody();
		message.setText("TEST");
		assertEquals("TEST", message.getText());
	}
}
