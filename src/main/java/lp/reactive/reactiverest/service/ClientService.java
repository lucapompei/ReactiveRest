package lp.reactive.reactiverest.service;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;

import lp.reactive.reactiverest.utils.JsonUtils;
import lp.reactive.reactiverest.utils.TextUtils;
import retrofit2.Retrofit;

/**
 * This service represents the retrofit client used for REST communications
 *
 * @author lucapompei
 */
public class ClientService {

	/**
	 * Logger
	 */
	private static final Logger LOGGER = LogManager.getFormatterLogger(ClientService.class);

	/**
	 * The maximum number of REST clients that cache can contains simultaneously
	 */
	private static final int CACHE_REST_CLIENTS_SIZE = 10;

	/**
	 * A cache object used to handle multiple {@link Retrofit} REST client based on
	 * different base url. If a cached object exists, the cache retrieves it,
	 * otherwise the cache creates, caches and returns a new one. The objects stored
	 * in cache expire after 1 hour spent by its last usage. The cache stores up to
	 * {@value CACHE_REST_CLIENTS_SIZE} elements simultaneously.
	 */
	private static final LoadingCache<String, Retrofit> CACHE_REST_CLIENT = CacheBuilder.newBuilder()
			.maximumSize(CACHE_REST_CLIENTS_SIZE).expireAfterAccess(1, TimeUnit.HOURS)
			.build(new CacheLoader<String, Retrofit>() {
				@Override
				public Retrofit load(String baseUrl) {
					// a prior check was already performed
					if (TextUtils.isNullOrEmpty(baseUrl)) {
						LOGGER.error(
								"No valid base url specified for REST client, it cannot be null or empty: " + baseUrl);
						return null;
					} else {
						return new Retrofit.Builder().baseUrl(TextUtils.configHttpProtocolForBaseUrl(baseUrl))
								.addConverterFactory(JsonUtils.getConverterInstance()).build();
					}
				}
			});

	/**
	 * Private constructor for an utility class, construct a new
	 * {@code ClientService}
	 */
	private ClientService() {
		// Empty implementation
	}

	/**
	 * Using caching mechanisms, it retrieves a {@link Retrofit} REST client,
	 * configuring it on the base of the given base url
	 *
	 * @param baseUrl,
	 *            the base url to use
	 * @return a {@link Retrofit} REST client based on the given base url or
	 *         {@code null} if an incorrect base url was indicated
	 * @throws ExecutionException
	 *             if an error was thrown while loading the value
	 */
	public static Retrofit getRestClient(String baseUrl) throws ExecutionException {
		if (TextUtils.isNullOrEmpty(baseUrl)) {
			LOGGER.error("No valid base url for REST client, it cannot be null or empty: " + baseUrl);
			return null;
		} else {
			return CACHE_REST_CLIENT.get(baseUrl);
		}
	}

}
