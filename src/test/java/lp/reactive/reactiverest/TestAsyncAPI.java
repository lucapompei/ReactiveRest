package lp.reactive.reactiverest;

import com.google.common.collect.ImmutableMap;
import lp.reactive.reactiverest.api.AsyncAPI;
import lp.reactive.reactiverest.model.HttpMethod;
import lp.reactive.reactiverest.model.HttpRequest;
import lp.reactive.reactiverest.model.HttpResponse;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.function.Consumer;

/**
 * This class tests {@link AsyncAPI} and shows how use main methods exposed by the
 * tested class
 *
 * @author lucapompei
 */
public class TestAsyncAPI {

    /**
     * Mandatory data to use to call API
     */
    private static final String BASE_URL = "https://www.example.com/api/v2/";
    private static final String API_ENDPOINT = "getServices";

    /**
     * Optional data to use to call API
     */
    private static final HttpMethod HTTP_METHOD = HttpMethod.GET;
    private static final Map<String, String> HEADERS = ImmutableMap.of("AUTH_TOKEN", "1234567");
    private static final Map<String, String> QUERY_PARAMS = ImmutableMap.of("sort", "desc");
    private static final Map<String, String> BODY_PARAMS = ImmutableMap.of("code", "first");
    private static final int MAXIMUM_ATTEMPTS = 3;

    public static void main(String[] argv) throws IOException, ExecutionException {
        TestAsyncAPI.testBasicAsyncAPI();
        TestAsyncAPI.testBasicAsyncAPIWithRetry();
        TestAsyncAPI.testBasicAsyncAPIHandlingErrors();
        TestAsyncAPI.testBasicAsyncAPIHandlingErrorsWithRetry();
        TestAsyncAPI.testAsyncAPIWithOptionalParams();
        TestAsyncAPI.testAsyncAPIWithOptionalParamsHandlingErrors();
    }

    /**
     * Consumers
     */
    private static Consumer<HttpResponse> consumerOnSuccess = httpResponse ->
            System.out.println(httpResponse.toString());
    private static Consumer<Throwable> consumerOnError = System.out::println;


    public static void testBasicAsyncAPI() throws ExecutionException, IOException {
        System.out.println("Testing basic AsyncAPI call");
        // prepare http request
        HttpRequest httpRequest = new HttpRequest.
                Builder(BASE_URL, API_ENDPOINT)
                .build();
        System.out.println(httpRequest.toString());
        // execute api call and getting http response
        AsyncAPI.call(httpRequest, consumerOnSuccess);
    }
    
    public static void testBasicAsyncAPIWithRetry() throws ExecutionException, IOException {
        System.out.println("Testing basic AsyncAPI call with retry option");
        // prepare http request
        HttpRequest httpRequest = new HttpRequest.
                Builder(BASE_URL, API_ENDPOINT)
                .build();
        System.out.println(httpRequest.toString());
        // execute api call and getting http response
        AsyncAPI.call(httpRequest, consumerOnSuccess, MAXIMUM_ATTEMPTS);
    }

    public static void testBasicAsyncAPIHandlingErrors() throws ExecutionException, IOException {
        System.out.println("Testing basic AsyncAPI call handling errors");
        // prepare http request
        HttpRequest httpRequest = new HttpRequest.
                Builder(BASE_URL, API_ENDPOINT)
                .build();
        System.out.println(httpRequest.toString());
        // execute api call and getting http response
        AsyncAPI.call(httpRequest, consumerOnSuccess, consumerOnError);
    }
    
    public static void testBasicAsyncAPIHandlingErrorsWithRetry() throws ExecutionException, IOException {
        System.out.println("Testing basic AsyncAPI call handling errors with retry option");
        // prepare http request
        HttpRequest httpRequest = new HttpRequest.
                Builder(BASE_URL, API_ENDPOINT)
                .build();
        System.out.println(httpRequest.toString());
        // execute api call and getting http response
        AsyncAPI.call(httpRequest, consumerOnSuccess, consumerOnError, MAXIMUM_ATTEMPTS);
    }

    public static void testAsyncAPIWithOptionalParams() throws ExecutionException, IOException {
        System.out.println("Testing AsyncAPI call with optional params");
        // prepare http request
        HttpRequest httpRequest = new HttpRequest.
                Builder(BASE_URL, API_ENDPOINT)
                .httpMethod(HTTP_METHOD)
                .headers(HEADERS)
                .queryParams(QUERY_PARAMS)
                .bodyParams(BODY_PARAMS)
                .build();
        System.out.println(httpRequest.toString());
        // execute api call and getting http response
        AsyncAPI.call(httpRequest, consumerOnSuccess);
    }

    public static void testAsyncAPIWithOptionalParamsHandlingErrors() throws ExecutionException, IOException {
        System.out.println("Testing AsyncAPI call with optional params handling errors");
        // prepare http request
        HttpRequest httpRequest = new HttpRequest.
                Builder(BASE_URL, API_ENDPOINT)
                .httpMethod(HTTP_METHOD)
                .headers(HEADERS)
                .queryParams(QUERY_PARAMS)
                .bodyParams(BODY_PARAMS)
                .build();
        System.out.println(httpRequest.toString());
        // execute api call and getting http response
        AsyncAPI.call(httpRequest, consumerOnSuccess, consumerOnError);
    }

}
