package lp.reactive.reactiverest.api;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import io.reactivex.functions.Consumer;
import lp.reactive.reactiverest.model.HttpRequest;
import lp.reactive.reactiverest.model.HttpResponse;
import lp.reactive.reactiverest.service.RestService;

/**
 * This class exposes the API to handle reactive REST communications.
 *
 * @author lucapompei
 */
public class ReactiveAPI {

	/**
	 * Logger
	 */
	private static final Logger LOGGER = LogManager.getFormatterLogger(ReactiveAPI.class);

	/**
	 * Private constructor for an utility class, construct a new {@code ReactiveAPI}
	 */
	private ReactiveAPI() {
		// Empty implementation
	}

	/**
	 * This API is used to formulate a reactive api call on the base of the given
	 * parameters and return a {@link HttpResponse}
	 *
	 * @param httpRequest,
	 *            a prepared {@link HttpRequest} used for api call
	 * @param consumerOnSuccess,
	 *            the provided consumer that react to http response
	 */
	public static void call(HttpRequest httpRequest, Consumer<HttpResponse> consumerOnSuccess) {
		handleRequest(httpRequest, consumerOnSuccess, null, 1);
	}

	/**
	 * This API is used to formulate a reactive api call on the base of the given
	 * parameters, specifying a maximum number of attempts to use if an error
	 * occurs, and return a {@link HttpResponse}
	 *
	 * @param httpRequest,
	 *            a prepared {@link HttpRequest} used for api call
	 * @param consumerOnSuccess,
	 *            the provided consumer that react to http response
	 * @param attempts,
	 *            the number of attempts to test if an error occurs during the api
	 *            call
	 */
	public static void call(HttpRequest httpRequest, Consumer<HttpResponse> consumerOnSuccess, int attempts) {
		handleRequest(httpRequest, consumerOnSuccess, null, attempts);
	}

	/**
	 * This API is used to formulate a reactive api call on the base of the given
	 * parameters and return a {@link HttpResponse}
	 *
	 * @param httpRequest,
	 *            a prepared {@link HttpRequest} used for api call
	 * @param consumerOnSuccess,
	 *            the provided consumer that react to http response
	 * @param consumerOnError,
	 *            the provided consumer that react to http error
	 */
	public static void call(HttpRequest httpRequest, Consumer<HttpResponse> consumerOnSuccess,
			Consumer<Throwable> consumerOnError) {
		handleRequest(httpRequest, consumerOnSuccess, consumerOnError, 1);
	}

	/**
	 * This API is used to formulate a reactive api call on the base of the given
	 * parameters, specifying a maximum number of attempts to use if an error
	 * occurs, and return a {@link HttpResponse}
	 *
	 * @param httpRequest,
	 *            a prepared {@link HttpRequest} used for api call
	 * @param consumerOnSuccess,
	 *            the provided consumer that react to http response
	 * @param consumerOnError,
	 *            the provided consumer that react to http error
	 * @param attempts,
	 *            the number of attempts to test if an error occurs during the api
	 *            call
	 */
	public static void call(HttpRequest httpRequest, Consumer<HttpResponse> consumerOnSuccess,
			Consumer<Throwable> consumerOnError, int attempts) {
		handleRequest(httpRequest, consumerOnSuccess, consumerOnError, attempts);
	}

	/**
	 * This method handles all reactive requests on the base of the incoming
	 * parameters
	 *
	 * @param httpRequest,
	 *            a prepared {@link HttpRequest} used for api call
	 * @param consumerOnSuccess,
	 *            the provided consumer that react to http response
	 * @param consumerOnError,
	 *            the provided consumer that react to http error
	 * @param attempts,
	 *            the number of attempts to test if an error occurs during the api
	 *            call
	 */
	private static void handleRequest(HttpRequest httpRequest, Consumer<HttpResponse> consumerOnSuccess,
			Consumer<Throwable> consumerOnError, int attempts) {
		if (httpRequest != null) {
			LOGGER.debug("Reactive call to API with http request: " + httpRequest.toString());
			RestService.callReact(httpRequest, consumerOnSuccess, consumerOnError, attempts);
		} else {
			LOGGER.error("HttpRequest must not be null");
		}
	}

}
