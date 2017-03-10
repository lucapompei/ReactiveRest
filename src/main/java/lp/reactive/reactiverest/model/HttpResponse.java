package lp.reactive.reactiverest.model;

import lp.reactive.reactiverest.utils.JsonUtils;
import retrofit2.Response;

/**
 * This entity represents the response obtained after making an http request
 *
 * @author lucapompei
 */
public class HttpResponse {

    /**
     * The raw response obtained from http request
     */
    private Response<Object> rawResponse;

    /**
     * Construct a {@link HttpResponse} using the raw response obtained from
     * http request
     *
     * @param rawResponse,
     *         the raw response obtained from http request
     */
    public HttpResponse(Response<Object> rawResponse) {
        this.rawResponse = rawResponse;
    }

    /**
     * Retrieve the raw response obtained from http request
     *
     * @return the raw response obtained from http request
     */
    public Response<Object> getRawResponse() {
        return this.rawResponse;
    }

    /**
     * Retrieve the absolute url used for the http request
     *
     * @return the absolute url used for the http request
     */
    public String getCalledUrl() {
        return rawResponse.raw().request().url().toString();
    }

    /**
     * Retrieve the http status code of the raw response
     *
     * @return the http status code of the raw response
     */
    public String getStatusCode() {
        return rawResponse.code() + " " + rawResponse.message();
    }

    /**
     * Retrieve the json body of the raw response if no errors occur, otherwise
     * return the json error body
     *
     * @return the json body of the raw response if no errors occur, otherwise return the json error body
     */
    public String getJsonBody() {
        if (rawResponse.isSuccessful()) {
            return JsonUtils.toJson(rawResponse.body());
        } else {
            return JsonUtils.toJson(rawResponse.errorBody());
        }
    }

    /**
     * Return a prepared string to represent the obtained response
     *
     * @return a prepared string to represent the obtained response
     */
    public String toString() {
        return "HttpResponse:\nUrl: " + getCalledUrl() + "\n" +
                getStatusCode() + "\n" + getJsonBody();
    }

}
