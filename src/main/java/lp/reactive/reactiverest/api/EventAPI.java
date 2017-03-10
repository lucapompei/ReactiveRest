package lp.reactive.reactiverest.api;

import lp.reactive.reactiverest.model.HttpRequest;
import lp.reactive.reactiverest.model.HttpResponse;
import lp.reactive.reactiverest.service.CoordinatorService;
import lp.reactive.reactiverest.service.RestService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * This class exposes the API to handle event based REST communications using a
 * Google implementation of event bus. Using
 * this type of API you must handle registration and unregistration of object
 * from/to event bus through the {@link CoordinatorService}
 *
 * @author lucapompei
 */
public class EventAPI {

    /**
     * Logger
     */
    private static final Logger LOGGER = LogManager.getFormatterLogger(EventAPI.class);

    /**
     * This API is used to formulate a reactive api call on the base of the
     * given parameters and return a {@link HttpResponse}
     *
     * @param httpRequest,
     *         a prepared {@link HttpRequest} used for api call
     * @param eventIdentifier,
     *         the unique identifier to recognize the response event on event bus when it is emitted
     */
    public static void call(HttpRequest httpRequest, String eventIdentifier) {
        LOGGER.debug("Reactive call to API with http request: " + httpRequest.toString());
        RestService.callEvent(httpRequest, eventIdentifier, CoordinatorAPI.getCoordinator());
    }

}
