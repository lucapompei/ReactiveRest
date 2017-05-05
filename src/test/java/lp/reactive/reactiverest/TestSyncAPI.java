package lp.reactive.reactiverest;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import com.google.common.collect.ImmutableMap;

import lp.reactive.reactiverest.api.SyncAPI;
import lp.reactive.reactiverest.model.HttpMethod;
import lp.reactive.reactiverest.model.HttpRequest;
import lp.reactive.reactiverest.model.HttpResponse;

/**
 * This class tests {@link SyncAPI} and shows how use main methods exposed by the
 * tested class
 *
 * @author lucapompei
 */
public class TestSyncAPI {

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

    public static void main(String[] argv) throws IOException, ExecutionException {
        TestSyncAPI.testBasicSyncAPI();
        TestSyncAPI.testSyncAPIWithOptionalParams();
    }

    public static void testBasicSyncAPI() throws ExecutionException, IOException {
        System.out.println("Testing basic SyncAPI call");
        // prepare http request
        HttpRequest httpRequest = new HttpRequest.
                Builder(BASE_URL, API_ENDPOINT)
                .build();
        System.out.println(httpRequest.toString());
        // execute api call and getting http response
        HttpResponse httpResponse = SyncAPI.call(httpRequest);
        if (httpRequest != null) {
        	System.out.println(httpResponse.toString());
        }
    }

    public static void testSyncAPIWithOptionalParams() throws ExecutionException, IOException {
        System.out.println("Testing SyncAPI call with optional params");
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
        HttpResponse httpResponse = SyncAPI.call(httpRequest);
        if (httpRequest != null) {
        	System.out.println(httpResponse.toString());
        }
    }

}
