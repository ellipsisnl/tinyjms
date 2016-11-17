package nl.ellipsis.tpjms.provider;

import java.net.URI;
import java.util.*;

import nl.ellipsis.tpjms.provider.vm.VmProvider;

/**
 * Registry of TinyJms providers.
 */
public final class ProviderRegistry
{
	private static final Map<String, TPJMSProvider> providerMap = new HashMap<String, TPJMSProvider>();

	static
	{
		registerProvider("vm", VmProvider.getInstance());
	}

	private ProviderRegistry()
	{}

	/**
	 * Retrieves a provider instance from the registry by URI.
	 * 
	 * @param uri
	 *          URI of provider
	 * @return TinyJms provider
	 * @throws InvalidUrlException
	 *           if url is malformed or a provider could not be found
	 */
	public static synchronized TPJMSProvider getProviderForUri(URI uri) throws InvalidUrlException
	{
		return getProviderForScheme(getScheme(uri));
	}

	/**
	 * Retrieves a provider instance from the registry by URI scheme.
	 * 
	 * @param scheme
	 *          URI scheme
	 * @return TinyJms provider
	 * @throws InvalidUrlException
	 *           if a provider could not be found
	 */
	public static synchronized TPJMSProvider getProviderForScheme(String scheme) throws InvalidUrlException
	{
		TPJMSProvider provider = providerMap.get(scheme);

		if (provider == null)
		{
			throw new InvalidUrlException("Unknown provider type: " + scheme);
		}

		return provider;
	}

	/**
	 * Registers a new provider.
	 * 
	 * @param scheme
	 *          URI scheme to register
	 * @param provider
	 *          JmsProvider implementation
	 * @throws IllegalArgumentException
	 *           if scheme or provider is null
	 */
	public static synchronized void registerProvider(String scheme, TPJMSProvider provider) throws IllegalArgumentException
	{
		if (scheme == null)
		{
			throw new IllegalArgumentException("Scheme cannot be null");
		}

		if (scheme.trim().length() == 0)
		{
			throw new IllegalArgumentException("Scheme must not be empty");
		}

		if (provider == null)
		{
			throw new IllegalArgumentException("Provider cannot be null");
		}

		providerMap.put(scheme.toLowerCase(Locale.US), provider);
	}

	/**
	 * Unregisters a provider.
	 * 
	 * @param scheme
	 *          URI scheme
	 */
	public static synchronized void unregisterProvider(String scheme)
	{
		if (scheme != null)
		{
			providerMap.remove(scheme.toLowerCase(Locale.US));
		}
	}

	private static String getScheme(URI uri) throws InvalidUrlException
	{
		if (uri == null)
		{
			throw new InvalidUrlException("Provider URI cannot be null");
		}

		return uri.getScheme().toLowerCase(Locale.US);
	}
}
