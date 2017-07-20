package lp.reactive.reactiverest;

import com.google.common.collect.ImmutableMap;
import com.google.common.eventbus.Subscribe;
import lp.reactive.reactiverest.api.CoordinatorAPI;
import lp.reactive.reactiverest.api.EventAPI;
import lp.reactive.reactiverest.model.EventResponse;
import lp.reactive.reactiverest.model.HttpMethod;
import lp.reactive.reactiverest.model.HttpRequest;

import java.util.Map;

/**
 * This class tests {@link EventAPI} and shows how use main methods exposed by the
 * tested class
 *
 * @author lucapompei
 */
public class TestEventAPI {

    /**
     * Mandatory data to use to call API
     */
    private static final String BASE_URL = "https://www.example.com/api/v2/";
    private static final String API_ENDPOINT = "getServices";
    private static final String EVENT_IDENTIFIER = "TEST_EVENT";

    /**
     * Optional data to use to call API
     */
    private static final HttpMethod HTTP_METHOD = HttpMethod.POST;
    private static final Map<String, String> HEADERS = ImmutableMap.of("AUTH_TOKEN", "1234567");
    private static final Map<String, String> QUERY_PARAMS = ImmutableMap.of("sort", "desc");
    private static final Map<String, String> BODY_PARAMS = ImmutableMap.of("code", "first");
    private static final int MAXIMUM_ATTEMPTS = 3;

    public static void main(String[] argv) {
        TestEventAPI.testBasicEventAPI();
        TestEventAPI.testBasicEventAPIWithRetry();
        TestEventAPI.testEventAPIWithOptionalParams();
    }

    private static TestEventAPI getInstance() {
    	// Used for test scope
        return new TestEventAPI();
    }

    public static void testBasicEventAPI() {
        System.out.println("Testing basic EventAPI call with http request");
        // prepare http request
        HttpRequest httpRequest = new HttpRequest.
                Builder(BASE_URL, API_ENDPOINT)
                .build();
        System.out.println(httpRequest.toString());
        // register class to event bus through the coordinator API
        CoordinatorAPI.getCoordinator().register(getInstance());
        // execute api call and getting http response
        EventAPI.call(httpRequest, EVENT_IDENTIFIER);
    }
    
    public static void testBasicEventAPIWithRetry() {
        System.out.println("Testing basic EventAPI call with http request with retry option");
        // prepare http request
        HttpRequest httpRequest = new HttpRequest.
                Builder(BASE_URL, API_ENDPOINT)
                .build();
        System.out.println(httpRequest.toString());
        // register class to event bus through the coordinator API
        CoordinatorAPI.getCoordinator().register(getInstance());
        // execute api call and getting http response
        EventAPI.call(httpRequest, EVENT_IDENTIFIER, MAXIMUM_ATTEMPTS);
    }

    public static void testEventAPIWithOptionalParams() {
        System.out.println("Testing EventAPI call with optional params");
        // prepare http request
        HttpRequest httpRequest = new HttpRequest.
                Builder(BASE_URL, API_ENDPOINT)
                .httpMethod(HTTP_METHOD)
                .headers(HEADERS)
                .queryParams(QUERY_PARAMS)
                .bodyParams(BODY_PARAMS)
                .build();
        System.out.println(httpRequest.toString());
        // register class to event bus through the coordinator API
        CoordinatorAPI.getCoordinator().register(getInstance());
        // execute api call and getting http response
        EventAPI.call(httpRequest, EVENT_IDENTIFIER);
    }


    @Subscribe
    public void onConsumeTestEvent(EventResponse eventResponse) {
        if (CoordinatorAPI.getCoordinator().isEventForMe(eventResponse, EVENT_IDENTIFIER)) {
            System.out.println("Received a new event for me");
            if (eventResponse.isSuccess()) {
                // use http response
                System.out.println(eventResponse.getEventResponse().toString());
            } else {
                // use http error
                System.out.println(eventResponse.getEventError());
            }
        }
    }

}
