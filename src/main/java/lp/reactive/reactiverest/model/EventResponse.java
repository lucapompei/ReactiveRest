package lp.reactive.reactiverest.model;

/**
 * This class represents an event that incapsulates a {@link HttpResponse}
 * obtained from a http request
 *
 * @author lucapompei
 */
public class EventResponse {

    /**
     * The event identifier
     */
    private String identifier;

    /**
     * A variable used to indicate if the event based response is completed with success or not
     */
    private boolean isSuccess;

    /**
     * The http response obtained from http request
     */
    private HttpResponse httpResponse;

    /**
     * The possible catched error
     */
    private Throwable httpError;

    /**
     * Construct a new {@link EventResponse} on the base of the given
     * http response
     *
     * @param identifier,
     *         the event unique identifier
     * @param httpResponse,
     *         the response obtained from http request
     */
    public EventResponse(String identifier, HttpResponse httpResponse) {
        this.identifier = identifier;
        this.httpResponse = httpResponse;
        this.isSuccess = true;
    }

    /**
     * Construct a new {@link EventResponse} on the base of the given
     * http error
     *
     * @param identifier,
     *         the event unique identifier
     * @param httpError,
     *         the error obtained from http request
     */
    public EventResponse(String identifier, Throwable httpError) {
        this.identifier = identifier;
        this.httpError = httpError;
        this.isSuccess = false;
    }

    /**
     * Retrieve the event unique identifier
     *
     * @return the event unique identifier
     */
    public String getIdentifier() {
        return this.identifier;
    }

    /**
     * Retrieve the response obtained from http request
     *
     * @return the {@link HttpResponse} obtained from http request
     */
    public HttpResponse getEventResponse() {
        return httpResponse;
    }

    /**
     * Return a boolean variable used to indicate if the event based response is completed with success or not
     *
     * @return a variable used to indicate if the event based response is completed with success or not
     */
    public boolean isSuccess() {
        return isSuccess;
    }

    /**
     * Return the catched error
     *
     * @return the catched error
     */
    public Throwable getEventError() {
        return httpError;
    }
}
