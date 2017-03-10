package lp.reactive.reactiverest.model;

import io.reactivex.annotations.Nullable;
import retrofit2.Call;
import retrofit2.http.*;

import java.util.Map;

/**
 * This generic interface is to use to require to implement the most general
 * http request method to make REST communications
 *
 * @author lucapompei
 */
public interface HttpCall {

    /**
     * Execute a generic http {@link GET} request
     *
     * @param apiEndpoint,
     *         the relative path to be appended to the REST client's base url to compose the api url and make the http
     *         {@link GET} request
     * @param headers,
     *         a set of headers used for api call
     * @param queryParams,
     *         a map of params used to compose the query string
     * @return the obtained response from the http {@link GET} request
     */
    @GET(value = "{apiEndpoint}")
    Call<Object> makeGET(@Path(value = "apiEndpoint", encoded = true) String apiEndpoint, @Nullable @HeaderMap
            Map<String, String> headers, @Nullable @QueryMap Map<String, String> queryParams);

    /**
     * Execute a generic http {@link POST} request
     *
     * @param apiEndpoint,
     *         the relative path to be appended to the REST client's base url to compose the api url and make the http
     *         {@link POST} request
     * @param headers,
     *         a set of headers used for api call
     * @param queryParams,
     *         a map of params used to compose the query string
     * @param bodyParams,
     *         a map of params used to set the body of the http {@link POST} request
     * @return the obtained response from the http {@link POST} request
     */
    @POST(value = "{apiEndpoint}")
    Call<Object> makePOST(@Path(value = "apiEndpoint", encoded = true) String apiEndpoint, @Nullable @HeaderMap
            Map<String, String> headers, @Nullable @QueryMap Map<String, String> queryParams, @Nullable @Body
                                  Map<String, String> bodyParams);

    /**
     * Execute a generic http {@link PUT} request
     *
     * @param apiEndpoint,
     *         the relative path to be appended to the REST client's base url to compose the api url and make the http
     *         {@link PUT} request
     * @param headers,
     *         a set of headers used for api call
     * @param queryParams,
     *         a map of params used to compose the query string
     * @param bodyParams,
     *         a map of params used to set the body of the http {@link PUT} request
     * @return the obtained response from the http {@link PUT} request
     */
    @PUT(value = "{apiEndpoint}")
    Call<Object> makePUT(@Path(value = "apiEndpoint", encoded = true) String apiEndpoint, @Nullable @HeaderMap
            Map<String, String> headers, @Nullable @QueryMap Map<String, String> queryParams, @Nullable @Body
                                 Map<String, String> bodyParams);

    /**
     * Execute a generic http {@link DELETE} request
     *
     * @param apiEndpoint,
     *         the relative path to be appended to the REST client's base url to compose the api url and make the http
     *         {@link DELETE} request
     * @param headers,
     *         a set of headers used for api call
     * @param queryParams,
     *         a map of params used to compose the query string
     * @param bodyParams,
     *         a map of params used to set the body of the http {@code DELETE} request
     * @return the obtained response from the http {@link DELETE} request
     */
    @DELETE(value = "{apiEndpoint}")
    Call<Object> makeDELETE(@Path(value = "apiEndpoint", encoded = true) String apiEndpoint, @Nullable @HeaderMap
            Map<String, String> headers, @Nullable @QueryMap Map<String, String> queryParams, @Nullable @Body
                                    Map<String, String> bodyParams);

}
