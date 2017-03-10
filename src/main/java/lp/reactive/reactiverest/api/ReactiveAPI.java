package lp.reactive.reactiverest.api;

import io.reactivex.functions.Consumer;
import lp.reactive.reactiverest.model.HttpRequest;
import lp.reactive.reactiverest.model.HttpResponse;
import lp.reactive.reactiverest.service.RestService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

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
     * This API is used to formulate a reactive api call on the base of the
     * given parameters and return a {@link HttpResponse}
     *
     * @param httpRequest,
     *         a prepared {@link HttpRequest} used for api call
     * @param consumerOnSuccess,
     *         the provided consumer that react to http response
     */
    public static void call(HttpRequest httpRequest, Consumer<HttpResponse> consumerOnSuccess) {
        LOGGER.debug("Reactive call to API with http request: " + httpRequest.toString());
        RestService.callReact(httpRequest, consumerOnSuccess, null);
    }

    /**
     * This API is used to formulate a reactive api call on the base of the
     * given parameters and return a {@link HttpResponse}
     *
     * @param httpRequest,
     *         a prepared {@link HttpRequest} used for api call
     * @param consumerOnSuccess,
     *         the provided consumer that react to http response
     * @param consumerOnError,
     *         the provided consumer that react to http error
     */
    public static void call(HttpRequest httpRequest, Consumer<HttpResponse> consumerOnSuccess, Consumer<Throwable>
            consumerOnError) {
        LOGGER.debug("Reactive call to API with http request: " + httpRequest.toString());
        RestService.callReact(httpRequest, consumerOnSuccess, consumerOnError);
    }

}
