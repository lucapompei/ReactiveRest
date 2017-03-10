package lp.reactive.reactiverest.model;

import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;

/**
 * This enum represents the set of handled http method
 *
 * @author lucapompei
 */
public enum HttpMethod {

    /**
     * Reference to http {@link GET} method type
     */
    GET,

    /**
     * Reference to http {@link POST} method type
     */
    POST,

    /**
     * Reference to http {@link PUT} method type
     */
    PUT,

    /**
     * Reference to http {@link DELETE} method type
     */
    DELETE

}
