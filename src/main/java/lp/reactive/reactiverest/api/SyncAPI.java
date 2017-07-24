package lp.reactive.reactiverest.api;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import lp.reactive.reactiverest.model.HttpRequest;
import lp.reactive.reactiverest.model.HttpResponse;
import lp.reactive.reactiverest.service.RestService;

/**
 * This class exposes the API to handle synchronous REST communications.
 *
 * @author lucapompei
 */
public class SyncAPI {

	/**
	 * Logger
	 */
	private static final Logger LOGGER = LogManager.getFormatterLogger(SyncAPI.class);

	/**
	 * Private constructor for an utility class, construct a new {@code SyncAPI}
	 */
	private SyncAPI() {
		// Empty implementation
	}

	/**
	 * This API is used to formulate a synchronous api call on the base of the given
	 * http request and return a {@link HttpResponse}
	 *
	 * @param httpRequest,
	 *            a prepared {@link HttpRequest} used for api call
	 * @return the http response encapsulated into a {@link HttpResponse} or
	 *         {@code null} is some error occurs
	 * @throws ExecutionException
	 *             if a problem occurred during the retrieving of REST client
	 * @throws IOException
	 *             if a problem occurred talking to the server
	 */
	public static HttpResponse call(HttpRequest httpRequest) throws ExecutionException, IOException {
		if (httpRequest != null) {
			LOGGER.debug("Synchronous call to API with http request: " + httpRequest.toString());
			return RestService.callSync(httpRequest, 1);
		} else {
			LOGGER.error("HttpRequest must not be null");
			return null;
		}
	}

	/**
	 * This API is used to formulate a synchronous api call on the base of the given
	 * http request, specifying a maximum number of attempts to use if an error
	 * occurs, and return a {@link HttpResponse}
	 *
	 * @param httpRequest,
	 *            a prepared {@link HttpRequest} used for api call
	 * @param attempts,
	 *            the number of attempts to test if an error occurs during the api
	 *            call
	 * @return the http response encapsulated into a {@link HttpResponse} or
	 *         {@code null} is some error occurs
	 * @throws ExecutionException
	 *             if a problem occurred during the retrieving of REST client
	 * @throws IOException
	 *             if a problem occurred talking to the server
	 */
	public static HttpResponse call(HttpRequest httpRequest, int attempts) throws ExecutionException, IOException {
		if (httpRequest != null) {
			LOGGER.debug("Synchronous call to API with http request: " + httpRequest.toString());
			return RestService.callSync(httpRequest, attempts);
		} else {
			LOGGER.error("HttpRequest must not be null");
			return null;
		}
	}

}
