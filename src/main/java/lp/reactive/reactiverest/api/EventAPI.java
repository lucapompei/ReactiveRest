package lp.reactive.reactiverest.api;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import lp.reactive.reactiverest.model.HttpRequest;
import lp.reactive.reactiverest.model.HttpResponse;
import lp.reactive.reactiverest.service.CoordinatorService;
import lp.reactive.reactiverest.service.RestService;
import lp.reactive.reactiverest.utils.TextUtils;

/**
 * This class exposes the API to handle event based REST communications using a
 * Google implementation of event bus. Using this type of API you must handle
 * registration and unregistration of object from/to event bus through the
 * {@link CoordinatorService}
 *
 * @author lucapompei
 */
public class EventAPI {

	/**
	 * Logger
	 */
	private static final Logger LOGGER = LogManager.getFormatterLogger(EventAPI.class);

	/**
	 * Private constructor for an utility class, construct a new {@code EventAPI}
	 */
	private EventAPI() {
		// Empty implementation
	}

	/**
	 * This API is used to formulate a reactive api call on the base of the given
	 * parameters and return a {@link HttpResponse}
	 *
	 * @param httpRequest,
	 *            a prepared {@link HttpRequest} used for api call
	 * @param eventIdentifier,
	 *            the unique identifier to recognize the response event on event bus
	 *            when it is emitted
	 */
	public static void call(HttpRequest httpRequest, String eventIdentifier) {
		handleRequest(httpRequest, eventIdentifier, 1);
	}

	/**
	 * This API is used to formulate a reactive api call on the base of the given
	 * parameters, specifying a maximum number of attempts to use if an error
	 * occurs, and return a {@link HttpResponse}
	 *
	 * @param httpRequest,
	 *            a prepared {@link HttpRequest} used for api call
	 * @param eventIdentifier,
	 *            the unique identifier to recognize the response event on event bus
	 *            when it is emitted
	 * @param attempts,
	 *            the number of attempts to test if an error occurs during the api
	 *            call
	 */
	public static void call(HttpRequest httpRequest, String eventIdentifier, int attempts) {
		handleRequest(httpRequest, eventIdentifier, attempts);
	}

	/**
	 * This method handles all event based requests on the base of the incoming
	 * parameters
	 *
	 * @param httpRequest,
	 *            a prepared {@link HttpRequest} used for api call
	 * @param eventIdentifier,
	 *            the unique identifier to recognize the response event on event bus
	 *            when it is emitted
	 * @param attempts,
	 *            the number of attempts to test if an error occurs during the api
	 *            call
	 */
	private static void handleRequest(HttpRequest httpRequest, String eventIdentifier, int attempts) {
		if (httpRequest != null) {
			if (TextUtils.isNullOrEmpty(eventIdentifier)) {
				LOGGER.error("EventIdentifier must be not null");
			} else {
				LOGGER.debug("Reactive call to API with http request: " + httpRequest.toString());
				RestService.callEvent(httpRequest, eventIdentifier, CoordinatorAPI.getCoordinator(), attempts);
			}
		} else {
			LOGGER.error("HttpRequest must not be null");
		}
	}

}
