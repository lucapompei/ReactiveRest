package lp.reactive.reactiverest;

import com.google.common.collect.ImmutableMap;
import io.reactivex.functions.Consumer;
import lp.reactive.reactiverest.api.ReactiveAPI;
import lp.reactive.reactiverest.model.HttpMethod;
import lp.reactive.reactiverest.model.HttpRequest;
import lp.reactive.reactiverest.model.HttpResponse;

import java.util.Map;

/**
 * This class tests {@link ReactiveAPI} and shows how use main methods exposed
 * by the tested class
 *
 * @author lucapompei
 */
public class TestReactiveAPI {

	/**
	 * Mandatory data to use to call API
	 */
	private static final String BASE_URL = "https://www.example.com/api/v2/";
	private static final String API_ENDPOINT = "getServices";

	/**
	 * Optional data to use to call API
	 */
	private static final HttpMethod HTTP_METHOD = HttpMethod.POST;
	private static final Map<String, String> HEADERS = ImmutableMap.of("AUTH_TOKEN", "1234567");
	private static final Map<String, String> QUERY_PARAMS = ImmutableMap.of("sort", "desc");
	private static final Map<String, String> BODY_PARAMS = ImmutableMap.of("code", "first");
	private static final int MAXIMUM_ATTEMPTS = 3;

	/**
	 * Consumers (on success and on error)
	 */
	private static Consumer<HttpResponse> consumerOnSuccess = httpResponse -> System.out
			.println(httpResponse.toString());
	private static Consumer<Throwable> consumerOnError = System.out::println;

	public static void main(String[] argv) {
		// testing basic reactive based api
		TestReactiveAPI.testBasicReactiveAPI();
		// testing basic reactive based api with retry option
		TestReactiveAPI.testBasicReactiveAPIWithRetry();
		// testing basic reactive based api handling errors
		TestReactiveAPI.testBasicReactiveAPIHandlingErrors();
		// testing basic reactive based api handling errors with retry option
		TestReactiveAPI.testBasicReactiveAPIHandlingErrorsWithRetry();
		// testing reactive based api with optional parameters
		TestReactiveAPI.testReactiveAPIWithOptionalParams();
		// testing reactive based api with optional parameters and handling errors
		TestReactiveAPI.testReactiveAPIWithOptionalParamsHandlingErrors();
	}

	public static void testBasicReactiveAPI() {
		System.out.println("Testing basic ReactiveAPI call");
		// prepare http request
		HttpRequest httpRequest = new HttpRequest.Builder(BASE_URL, API_ENDPOINT).build();
		System.out.println(httpRequest.toString());
		// execute api call for getting http response
		ReactiveAPI.call(httpRequest, consumerOnSuccess);
	}

	public static void testBasicReactiveAPIWithRetry() {
		System.out.println("Testing basic ReactiveAPI call with retry option");
		// prepare http request
		HttpRequest httpRequest = new HttpRequest.Builder(BASE_URL, API_ENDPOINT).build();
		System.out.println(httpRequest.toString());
		// execute api call for getting http response
		ReactiveAPI.call(httpRequest, consumerOnSuccess, MAXIMUM_ATTEMPTS);
	}

	public static void testBasicReactiveAPIHandlingErrors() {
		System.out.println("Testing basic ReactiveAPI call handling errors");
		// prepare http request
		HttpRequest httpRequest = new HttpRequest.Builder(BASE_URL, API_ENDPOINT).build();
		System.out.println(httpRequest.toString());
		// execute api call for getting http response
		ReactiveAPI.call(httpRequest, consumerOnSuccess, consumerOnError);
	}

	public static void testBasicReactiveAPIHandlingErrorsWithRetry() {
		System.out.println("Testing basic ReactiveAPI call handling errors with retry option");
		// prepare http request
		HttpRequest httpRequest = new HttpRequest.Builder(BASE_URL, API_ENDPOINT).build();
		System.out.println(httpRequest.toString());
		// execute api call for getting http response
		ReactiveAPI.call(httpRequest, consumerOnSuccess, consumerOnError, MAXIMUM_ATTEMPTS);
	}

	public static void testReactiveAPIWithOptionalParams() {
		System.out.println("Testing ReactiveAPI call with optional params");
		// prepare http request
		HttpRequest httpRequest = new HttpRequest.Builder(BASE_URL, API_ENDPOINT).httpMethod(HTTP_METHOD)
				.headers(HEADERS).queryParams(QUERY_PARAMS).bodyParams(BODY_PARAMS).build();
		System.out.println(httpRequest.toString());
		// execute api call for getting http response
		ReactiveAPI.call(httpRequest, consumerOnSuccess);
	}

	public static void testReactiveAPIWithOptionalParamsHandlingErrors() {
		System.out.println("Testing ReactiveAPI call with optional params handling errors");
		// prepare http request
		HttpRequest httpRequest = new HttpRequest.Builder(BASE_URL, API_ENDPOINT).httpMethod(HTTP_METHOD)
				.headers(HEADERS).queryParams(QUERY_PARAMS).bodyParams(BODY_PARAMS).build();
		System.out.println(httpRequest.toString());
		// execute api call for getting http response
		ReactiveAPI.call(httpRequest, consumerOnSuccess, consumerOnError);
	}

}
