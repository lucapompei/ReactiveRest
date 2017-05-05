package lp.reactive.reactiverest.api;

import lp.reactive.reactiverest.model.HttpRequest;
import lp.reactive.reactiverest.model.HttpResponse;
import lp.reactive.reactiverest.service.RestService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

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
     * This API is used to formulate a synchronous api call on the base of the
     * given http request and return a {@link HttpResponse}
     *
     * @param httpRequest,
     *         a prepared {@link HttpRequest} used for api call
     * @return the http response encapsulated into a {@link HttpResponse} or {@code null} is some error occurs
     * @throws ExecutionException
     *         if a problem occurred during the retrieving of REST client
     * @throws IOException
     *         if a problem occurred talking to the server
     */
    public static HttpResponse call(HttpRequest httpRequest) throws ExecutionException, IOException {
    	if (httpRequest != null) {
    		LOGGER.debug("Synchronous call to API with http request: " + httpRequest.toString());
    		return RestService.callSync(httpRequest);
    	} else {
    		LOGGER.error("HttpRequest must not be null");
    		return null;
    	}
    }

}
