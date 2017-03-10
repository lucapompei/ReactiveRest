package lp.reactive.reactiverest.api;

import lp.reactive.reactiverest.model.HttpRequest;
import lp.reactive.reactiverest.model.HttpResponse;
import lp.reactive.reactiverest.service.RestService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.util.concurrent.ExecutionException;
import java.util.function.Consumer;

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
     * This API is used to formulate an asynchronous api call on the base of the
     * given parameters and return a {@link HttpResponse}
     *
     * @param httpRequest,
     *         a prepared {@link HttpRequest} used for api call
     * @param consumerOnSuccess,
     *         the consumer of asynchronous executing
     * @throws ExecutionException,
     *         if a problem occurred during the retrieving of REST client
     * @throws IOException,
     *         if a problem occurred talking to the server
     */
    public static void call(HttpRequest httpRequest, Consumer<HttpResponse> consumerOnSuccess) throws
            ExecutionException,
            IOException {
        LOGGER.debug("Asynchronous call to API with http request: " + httpRequest.toString());
        RestService.callAsync(httpRequest, consumerOnSuccess, null);
    }

    /**
     * This API is used to formulate an asynchronous api call on the base of the
     * given parameters and return a {@link HttpResponse}
     *
     * @param httpRequest,
     *         a prepared {@link HttpRequest} used for api call
     * @param consumerOnSuccess,
     *         the consumer of asynchronous executing
     * @param consumerOnError,
     *         the consumer of possible asynchronous errors
     * @throws ExecutionException,
     *         if a problem occurred during the retrieving of REST client
     * @throws IOException,
     *         if a problem occurred talking to the server
     */
    public static void call(HttpRequest httpRequest, Consumer<HttpResponse> consumerOnSuccess, Consumer<Throwable>
            consumerOnError) throws ExecutionException,
            IOException {
        LOGGER.debug("Asynchronous call to API with http request: " + httpRequest.toString());
        RestService.callAsync(httpRequest, consumerOnSuccess, consumerOnError);
    }

}
