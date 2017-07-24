package lp.reactive.reactiverest.service;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.function.Consumer;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import io.reactivex.Flowable;
import lp.reactive.reactiverest.model.EventResponse;
import lp.reactive.reactiverest.model.HttpCall;
import lp.reactive.reactiverest.model.HttpMethod;
import lp.reactive.reactiverest.model.HttpRequest;
import lp.reactive.reactiverest.model.HttpResponse;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

/**
 * This service handles REST communications using a REST client
 *
 * @author lucapompei
 */
public class RestService {

	/**
	 * Logger
	 */
	private static final Logger LOGGER = LogManager.getFormatterLogger(RestService.class);

	/**
	 * Private constructor for an utility class, construct a new {@code RestService}
	 */
	private RestService() {
		// Empty implementation
	}

	/**
	 * This method is used to formulate a synchronous api call on the base of the
	 * given parameters and return a {@link HttpResponse}
	 *
	 * @param httpRequest,
	 *            a prepared {@link HttpRequest} used for api call
	 * @param attempts,
	 *            the number of attempts to test if an error occurs during the api
	 *            call
	 * @return the http response encapsulated into a {@link HttpResponse} or
	 *         {@code null} is some error occurs
	 * @throws ExecutionException
	 *             if a problem occurred during the retrieving of REST client
	 * @throws IOException
	 *             if a problem occurred talking to the server
	 */
	public static HttpResponse callSync(HttpRequest httpRequest, int attempts) throws ExecutionException, IOException {
		// prepare the call
		Call<ResponseBody> call = prepareCall(httpRequest);
		if (call == null) {
			LOGGER.error("Error during preparing call");
			return null;
		}
		// make synchronous http request and get http response
		Response<ResponseBody> rawResponse = call.execute();
		while (attempts > 1 && !rawResponse.isSuccessful()) {
			attempts--;
			LOGGER.error("Received " + rawResponse.code() + ", waiting 2 seconds for retry... (remaining " + attempts
					+ " attempts)");
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				// Unhandled exception
			}
			rawResponse = call.clone().execute();
		}
		return prepareHttpResponse(rawResponse);
	}

	/**
	 * This method is used to formulate an asynchronous api call on the base of the
	 * given parameters and return a {@link HttpResponse}
	 *
	 * @param httpRequest,
	 *            a prepared {@link HttpRequest} used for api call
	 * @param consumerOnSuccess,
	 *            the consumer used to handle success response
	 * @param consumerOnError,
	 *            the consumer used to handle error response
	 * @param attempts,
	 *            the number of attempts to test if an error occurs during the api
	 *            call
	 * @throws ExecutionException
	 *             if a problem occurred during the retrieving of REST client
	 * @throws IOException
	 *             if a problem occurred talking to the server
	 */
	public static void callAsync(HttpRequest httpRequest, Consumer<HttpResponse> consumerOnSuccess,
			Consumer<Throwable> consumerOnError, int attempts) throws ExecutionException, IOException {
		// prepare the call
		Call<ResponseBody> call = prepareCall(httpRequest);
		if (call == null) {
			LOGGER.error("Error during preparing call");
			return;
		}
		// make asynchronous http request and get http response
		enqueueCall(call, consumerOnSuccess, consumerOnError, attempts);
	}

	/**
	 * This method starts a new asynchronous api call based on an already prepared
	 * call and return a {@link HttpResponse}
	 *
	 * @param httpRequest,
	 *            a prepared {@link HttpRequest} used for api call
	 * @param consumerOnSuccess,
	 *            the consumer used to handle success response
	 * @param consumerOnError,
	 *            the consumer used to handle error response
	 * @param attempts,
	 *            the number of attempts to test if an error occurs during the api
	 *            call
	 * @throws ExecutionException
	 *             if a problem occurred during the retrieving of REST client
	 * @throws IOException
	 *             if a problem occurred talking to the server
	 */
	private static void enqueueCall(Call<ResponseBody> call, Consumer<HttpResponse> consumerOnSuccess,
			Consumer<Throwable> consumerOnError, int attempts) {
		// make asynchronous http request and get http response
		call.clone().enqueue(new Callback<ResponseBody>() {
			@Override
			public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
				HttpResponse httpResponse = prepareHttpResponse(response);
				if (httpResponse != null) {
					consumerOnSuccess.accept(httpResponse);
				}
			}

			@Override
			public void onFailure(Call<ResponseBody> call, Throwable t) {
				LOGGER.error("Error during executing asynchronous api call", t);
				if (attempts > 1) {
					int newAttempts = attempts - 1;
					LOGGER.error("Received " + t.getMessage() + ", waiting 2 seconds for retry... (remaining "
							+ attempts + " attempts)");
					try {
						Thread.sleep(2000);
					} catch (InterruptedException e) {
						// Unhandled exception
					}
					enqueueCall(call, consumerOnSuccess, consumerOnError, newAttempts);
				} else {
					if (consumerOnError != null) {
						consumerOnError.accept(t);
					}
				}
			}
		});
	}

	/**
	 * This method prepares the {@link Call<ResponseBody>} object used to execute
	 * the REST communication
	 *
	 * @param httpRequest,
	 *            a prepared {@link HttpRequest} used for api call
	 * @return the {@link Call<ResponseBody>} object used for execute REST
	 *         communications or {@code null} is some error occurs
	 * @throws ExecutionException
	 *             if a problem occurred during the retrieving of REST client
	 * @throws IOException
	 *             if a problem occurred talking to the server
	 */
	private static Call<ResponseBody> prepareCall(HttpRequest httpRequest) throws ExecutionException, IOException {
		// obtain a REST client
		Retrofit restClient = ClientService.getRestClient(httpRequest.getBaseUrl());
		if (restClient == null) {
			String errorMessage = "Unable to initialize the REST client with the given base url: "
					+ httpRequest.getBaseUrl();
			LOGGER.error(errorMessage);
			throw new InternalError(errorMessage);
		}
		// initialize the http request using the previous obtained REST client
		HttpCall httpCall = restClient.create(HttpCall.class);
		// handle possible null values for query string and body parameters
		String apiEndpoint = httpRequest.getApiEndpoint();
		HttpMethod httpMethod = httpRequest.getHttpMethod() == null ? HttpMethod.GET : httpRequest.getHttpMethod();
		Map<String, String> headers = httpRequest.getHeaders() == null ? new HashMap<>() : httpRequest.getHeaders();
		Map<String, String> queryParams = httpRequest.getQueryParams() == null ? new HashMap<>()
				: httpRequest.getQueryParams();
		String queryString = httpRequest.getQueryString() == null ? null : httpRequest.getQueryString();
		if (queryString != null) {
			String queryStringSplitted[] = queryString.split("&");
			int index = 0;
			int sizeList = queryStringSplitted.length;
			for (; index < sizeList; index++) {
				String stringValues[] = queryStringSplitted[index].split("=");
				if (stringValues.length == 2) {
					queryParams.put(stringValues[0], stringValues[1]);
				}
			}
		}
		Map<String, String> bodyParams = httpRequest.getBodyParams() == null ? new HashMap<>()
				: httpRequest.getBodyParams();
		// initialize the call
		Call<ResponseBody> call;
		// make the http request with respect to the indicated http method
		// as default will be considered a GET http method
		switch (httpMethod) {
		case GET:
			call = httpCall.makeGET(apiEndpoint, headers, queryParams);
			break;
		case POST:
			call = httpCall.makePOST(apiEndpoint, headers, queryParams, bodyParams);
			break;
		case PUT:
			call = httpCall.makePUT(apiEndpoint, headers, queryParams, bodyParams);
			break;
		case DELETE:
			call = httpCall.makeDELETE(apiEndpoint, headers, queryParams, bodyParams);
			break;
		default:
			LOGGER.error("Not valid http method used for api call");
			return null;
		}
		return call;
	}

	/**
	 * Convert the obtained {@link Response<ResponseBody>} rawResponse into a
	 * {@link HttpResponse}
	 *
	 * @param rawResponse,
	 *            the raw response obtained from http request
	 * @return a {@link HttpResponse}
	 */
	private static HttpResponse prepareHttpResponse(Response<ResponseBody> rawResponse) {
		if (rawResponse == null) {
			LOGGER.debug("The raw response is null");
			return null;
		} else {
			HttpResponse httpResponse = new HttpResponse(rawResponse);
			LOGGER.debug("Response obtained with http status code: " + httpResponse.getStatusCode());
			return httpResponse;
		}
	}

	/**
	 * This method prepares and executes an event based REST communication
	 *
	 * @param httpRequest,
	 *            a prepared {@link HttpRequest} used for api call
	 * @param eventIdentifier,
	 *            the unique identifier to recognize the response event on event bus
	 *            when it is emitted
	 * @param coordinatorService,
	 *            the coordinator service used to post new event on event bus
	 * @param attempts,
	 *            the number of attempts to test if an error occurs during the api
	 *            call
	 */
	public static void callEvent(HttpRequest httpRequest, String eventIdentifier, CoordinatorService coordinatorService,
			int attempts) {
		// prepare and dispatch event response on event bus
		RestService.prepareAndDispatchEvent(httpRequest, eventIdentifier, coordinatorService, attempts);
	}

	/**
	 * Prepare and dispatch a {@code EventResponse} event on event bus using the
	 * given {@param eventIdentifier} through the {@link CoordinatorService}
	 *
	 * @param httpRequest,
	 *            a prepared {@link HttpRequest} used for api call
	 * @param eventIdentifier,
	 *            the identify used by subscribers to recognize the event on event
	 *            bus
	 * @param coordinatorService,
	 *            the coordinator service used to post new event on event bus
	 * @param attempts,
	 *            the number of attempts to test if an error occurs during the api
	 *            call
	 */
	private static void prepareAndDispatchEvent(HttpRequest httpRequest, String eventIdentifier,
			CoordinatorService coordinatorService, int attempts) {
		// prepare and asynchronously send event response
		new Thread(() -> {
			try {
				// obtaining http response
				HttpResponse httpResponse = callSync(httpRequest, attempts);
				if (httpRequest == null) {
					throw new Exception("HttpResponse is null");
				} else {
					// preparing the event response based on http response
					EventResponse eventResponse = new EventResponse(eventIdentifier, httpResponse);
					// dispatching event response on event bus
					LOGGER.debug("Posting event response on event bus identifier by " + eventIdentifier);
					coordinatorService.post(eventResponse);
				}
			} catch (Exception ex) {
				// preparing the event response based on http error
				EventResponse eventResponse = new EventResponse(eventIdentifier, ex);
				// dispatching event response on event bus
				LOGGER.error("Error during preparing event response for event " + eventIdentifier, ex);
				coordinatorService.post(eventResponse);
			}
		}).start();
	}

	/**
	 * This method prepares and executes a reactive based REST communication
	 *
	 * @param httpRequest,
	 *            a prepared {@link HttpRequest} used for api call
	 * @param consumerOnSuccess,
	 *            the provided consumer that react to http response
	 * @param consumerOnError,
	 *            the provided consumer that react to http error
	 * @param attempts,
	 *            the number of attempts to test if an error occurs during the api
	 *            call
	 */
	public static void callReact(HttpRequest httpRequest,
			io.reactivex.functions.Consumer<HttpResponse> consumerOnSuccess,
			io.reactivex.functions.Consumer<Throwable> consumerOnError, int attempts) {
		// prepare flowable to handle async response
		Flowable<HttpResponse> flowable = Flowable.fromCallable(() -> RestService.callSync(httpRequest, attempts));
		new Thread(() -> {
			if (consumerOnError == null) {
				flowable.subscribe(consumerOnSuccess);
			} else {
				flowable.subscribe(consumerOnSuccess, consumerOnError);
			}
			flowable.publish();
		}).start();
	}

}
