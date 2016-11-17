package nl.ellipsis.tpjms.client;

import static org.junit.Assert.*;

import java.util.Enumeration;

import org.junit.*;

import nl.ellipsis.tpjms.client.TPJMSConnectionMetaData;

public class TinyJmsConnectionMetaDataTest
{
	private TPJMSConnectionMetaData meta;

	@Before
	public void setUp() throws Exception
	{
		meta = new TPJMSConnectionMetaData();
	}

	@After
	public void tearDown()
	{
		meta = null;
	}

	@Test
	public void testGetJMSMajorVersion()
	{
		assertEquals(1, meta.getJMSMajorVersion());
	}

	@Test
	public void testGetJMSMinorVersion()
	{
		assertEquals(1, meta.getJMSMinorVersion());
	}

	@Test
	public void testGetJMSProviderName()
	{
		assertEquals("TinyJms", meta.getJMSProviderName());
	}

	@Test
	public void testGetJMSVersion()
	{
		assertEquals("1.1.1", meta.getJMSVersion());
	}

	@Test
	public void testGetJMSXPropertyNames()
	{
		Enumeration props = meta.getJMSXPropertyNames();
		assertFalse(props.hasMoreElements());
	}

	@Test
	public void testGetProviderMajorVersion()
	{
		assertEquals(1, meta.getProviderMajorVersion());
	}

	@Test
	public void testGetProviderMinorVersion()
	{
		assertEquals(0, meta.getProviderMinorVersion());
	}

	@Test
	public void testGetProviderVersion()
	{
		assertEquals("1.0-SNAPSHOT", meta.getProviderVersion());
	}
}
