package lp.reactive.reactiverest.api;

import java.io.IOException;
import java.util.concurrent.ExecutionException;
import java.util.function.Consumer;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import lp.reactive.reactiverest.model.HttpRequest;
import lp.reactive.reactiverest.model.HttpResponse;
import lp.reactive.reactiverest.service.RestService;

/**
 * This class exposes the API to handle asynchronous REST communications
 *
 * @author lucapompei
 */
public class AsyncAPI {

	/**
	 * Logger
	 */
	private static final Logger LOGGER = LogManager.getFormatterLogger(AsyncAPI.class);

	/**
	 * Private constructor for an utility class, construct a new {@code AsyncAPI}
	 */
	private AsyncAPI() {
		// Empty implementation
	}

	/**
	 * This API is used to formulate an asynchronous api call on the base of the
	 * given parameters and return a {@link HttpResponse}
	 *
	 * @param httpRequest,
	 *            a prepared {@link HttpRequest} used for api call
	 * @param consumerOnSuccess,
	 *            the consumer of asynchronous executing
	 * @throws ExecutionException
	 *             if a problem occurred during the retrieving of REST client
	 * @throws IOException
	 *             if a problem occurred talking to the server
	 */
	public static void call(HttpRequest httpRequest, Consumer<HttpResponse> consumerOnSuccess)
			throws ExecutionException, IOException {
		handleRequest(httpRequest, consumerOnSuccess, null, 1);
	}

	/**
	 * This API is used to formulate an asynchronous api call on the base of the
	 * given parameters, specifying a maximum number of attempts to use if an error
	 * occurs, and return a {@link HttpResponse}
	 *
	 * @param httpRequest,
	 *            a prepared {@link HttpRequest} used for api call
	 * @param consumerOnSuccess,
	 *            the consumer of asynchronous executing
	 * @param attempts,
	 *            the number of attempts to test if an error occurs during the api
	 *            call
	 * @throws ExecutionException
	 *             if a problem occurred during the retrieving of REST client
	 * @throws IOException
	 *             if a problem occurred talking to the server
	 */
	public static void call(HttpRequest httpRequest, Consumer<HttpResponse> consumerOnSuccess, int attempts)
			throws ExecutionException, IOException {
		handleRequest(httpRequest, consumerOnSuccess, null, attempts);
	}

	/**
	 * This API is used to formulate an asynchronous api call on the base of the
	 * given parameters and return a {@link HttpResponse}
	 *
	 * @param httpRequest,
	 *            a prepared {@link HttpRequest} used for api call
	 * @param consumerOnSuccess,
	 *            the consumer of asynchronous executing
	 * @param consumerOnError,
	 *            the consumer of possible asynchronous errors
	 * @throws ExecutionException
	 *             if a problem occurred during the retrieving of REST client
	 * @throws IOException
	 *             if a problem occurred talking to the server
	 */
	public static void call(HttpRequest httpRequest, Consumer<HttpResponse> consumerOnSuccess,
			Consumer<Throwable> consumerOnError) throws ExecutionException, IOException {
		handleRequest(httpRequest, consumerOnSuccess, consumerOnError, 1);
	}

	/**
	 * This API is used to formulate an asynchronous api call on the base of the
	 * given parameters, specifying a maximum number of attempts to use if an error
	 * occurs, and return a {@link HttpResponse}
	 *
	 * @param httpRequest,
	 *            a prepared {@link HttpRequest} used for api call
	 * @param consumerOnSuccess,
	 *            the consumer of asynchronous executing
	 * @param consumerOnError,
	 *            the consumer of possible asynchronous errors
	 * @param attempts,
	 *            the number of attempts to test if an error occurs during the api
	 *            call
	 * @throws ExecutionException
	 *             if a problem occurred during the retrieving of REST client
	 * @throws IOException
	 *             if a problem occurred talking to the server
	 */
	public static void call(HttpRequest httpRequest, Consumer<HttpResponse> consumerOnSuccess,
			Consumer<Throwable> consumerOnError, int attempts) throws ExecutionException, IOException {
		handleRequest(httpRequest, consumerOnSuccess, consumerOnError, attempts);
	}

	/**
	 * This method handles all async requests on the base of the incoming parameters
	 *
	 * @param httpRequest,
	 *            a prepared {@link HttpRequest} used for api call
	 * @param consumerOnSuccess,
	 *            the consumer of asynchronous executing
	 * @param consumerOnError,
	 *            the consumer of possible asynchronous errors
	 * @param attempts,
	 *            the number of attempts to test if an error occurs during the api
	 *            call
	 * @throws ExecutionException
	 *             if a problem occurred during the retrieving of REST client
	 * @throws IOException
	 *             if a problem occurred talking to the server
	 */
	private static void handleRequest(HttpRequest httpRequest, Consumer<HttpResponse> consumerOnSuccess,
			Consumer<Throwable> consumerOnError, int attempts) throws ExecutionException, IOException {
		if (httpRequest != null) {
			LOGGER.debug("Asynchronous call to API with http request: " + httpRequest.toString());
			RestService.callAsync(httpRequest, consumerOnSuccess, consumerOnError, attempts);
		} else {
			LOGGER.error("HttpRequest must not be null");
		}
	}

}
