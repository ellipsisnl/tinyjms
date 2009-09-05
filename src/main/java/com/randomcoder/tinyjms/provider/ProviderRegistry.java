package com.randomcoder.tinyjms.provider;

import java.util.*;

import com.randomcoder.tinyjms.provider.vm.VmProvider;

/**
 * Registry of TinyJms providers.
 */
public final class ProviderRegistry
{
	private static final Map<String, TinyJmsProvider> providerMap = new HashMap<String, TinyJmsProvider>();

	static
	{
		registerProvider("vm", VmProvider.getInstance());
	}

	private ProviderRegistry()
	{}

	/**
	 * Retrieves a provider instance from the registry by URL.
	 * 
	 * @param url
	 *          URL to provider
	 * @return TinyJms provider
	 * @throws InvalidUrlException
	 *           if url is malformed or a provider could not be found
	 */
	public static synchronized TinyJmsProvider getProviderForUrl(String url) throws InvalidUrlException
	{
		return getProviderForScheme(getScheme(url));
	}

	/**
	 * Retrieves a provider instance from the registry by URL scheme.
	 * 
	 * @param scheme
	 *          URL scheme
	 * @return TinyJms provider
	 * @throws InvalidUrlException
	 *           if a provider could not be found
	 */
	public static synchronized TinyJmsProvider getProviderForScheme(String scheme) throws InvalidUrlException
	{
		TinyJmsProvider provider = providerMap.get(scheme);

		if (provider == null)
		{
			throw new InvalidUrlException("Unknown provider type: " + scheme);
		}

		return provider;
	}

	/**
	 * Registers a new
	 * 
	 * @param scheme
	 *          URL scheme to register
	 * @param provider
	 *          JmsProvider implementation
	 * @throws IllegalArgumentException
	 *           if scheme or provider is null
	 */
	public static synchronized void registerProvider(String scheme, TinyJmsProvider provider) throws IllegalArgumentException
	{
		if (scheme == null)
		{
			throw new IllegalArgumentException("Scheme cannot be null");
		}

		if (scheme.length() == 0)
		{
			throw new IllegalArgumentException("Scheme must not be empty");
		}

		for (char c : scheme.toCharArray())
		{
			if (c <= 'a' || c >= 'z')
			{
				throw new IllegalArgumentException("Invalid scheme: " + scheme);
			}
		}

		if (provider == null)
		{
			throw new IllegalArgumentException("Provider cannot be null");
		}

		providerMap.put(scheme, provider);
	}

	public static synchronized void unregisterProvider(String scheme)
	{
		providerMap.remove(scheme);
	}

	private static String getScheme(String url) throws InvalidUrlException
	{
		if (url == null)
		{
			throw new InvalidUrlException("Provider URL cannot be null");
		}

		int separatorIndex = url.indexOf(':');
		if (separatorIndex < 0)
		{
			throw new InvalidUrlException("Invalid provider URL: " + url);
		}

		return url.substring(0, separatorIndex).toLowerCase(Locale.US);
	}

}