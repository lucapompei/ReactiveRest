package lp.reactive.reactiverest.service;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.function.Consumer;

import lp.reactive.reactiverest.model.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import io.reactivex.Flowable;
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
	 * The seconds to wait before start a new api call when an error occurs
	 */
	private static final long SECONDS_TO_WAIT_BEFORE_RETRY = 2;

	/**
	 * Private constructor for an utility class, construct a new {@code RestService}
	 */
	private RestService() {
		// Empty implementation
	}

	/**
	 * This method is used to formulate a synchronous api call on the base of the
	 * given parameters and return a {@code HttpResponse}
	 *
	 * @param httpRequest,
	 *            a prepared {@code HttpRequest} used for api call
	 * @param attempts,
	 *            the number of attempts to test if an error occurs during the api
	 *            call
	 * @return the http response encapsulated into a {@code HttpResponse} or
	 *         {@code null} is some error occurs
	 * @throws ExecutionException
	 *             if a problem occurred during the retrieving of REST client
	 * @throws IOException
	 *             if a problem occurred talking to the server
	 */
	public static HttpResponse callSync(HttpRequest httpRequest, int attempts) throws ExecutionException, IOException {
		Date startTime = new Date();
		// prepare the call
		Call<ResponseBody> call = prepareCall(httpRequest);
		if (call == null) {
			LOGGER.error("Error during preparing call");
			return null;
		}
		// handle synchronous api call
		HttpResponse response = executeCall(call, attempts);
		Date endTime = new Date();
		LOGGER.info("Returned response in " + String.format("%s ms", endTime.getTime() - startTime.getTime())
				+ " milliseconds");
		return response;
	}

	/**
	 * Executes a sync api call on the base of the prepared call and the
	 * maximum number of attempts
	 * 
	 * @param call,
	 *            the call to synchronously execute
	 * @param attempts,
	 *            the maximum number of attempts
	 * @return the received httpResponse
	 * @throws IOException, if the call execution fails
	 */
	private static HttpResponse executeCall(Call<ResponseBody> call, int attempts) throws IOException {
		int remainingAttempts = attempts;
		// preparing response
		Response<ResponseBody> rawResponse = null;
		try {
			remainingAttempts--;
			// make synchronous http request and get http response
			rawResponse = call.clone().execute();
		} catch (IOException ex) {
			LOGGER.error("Received " + ex.getMessage());
			if (remainingAttempts < 1) {
				// no remaining attempts, throw the exception
				throw ex;
			}
		}
		if (remainingAttempts > 0 && (rawResponse == null || !rawResponse.isSuccessful())) {
			// handling not successful response
			LOGGER.error("Waiting " + SECONDS_TO_WAIT_BEFORE_RETRY + " seconds before retry... (remaining "
					+ remainingAttempts + " attempts)");
			try {
				Thread.sleep(SECONDS_TO_WAIT_BEFORE_RETRY * 1000);
			} catch (InterruptedException e) {
				// Unhandled exception
				Thread.currentThread().interrupt();
			}
			return executeCall(call, remainingAttempts);
		} else {
			return prepareHttpResponse(rawResponse);
		}
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
	 */
	public static void callAsync(HttpRequest httpRequest, Consumer<HttpResponse> consumerOnSuccess,
			Consumer<Throwable> consumerOnError, int attempts) throws ExecutionException {
		// prepare the call
		Call<ResponseBody> call = prepareCall(httpRequest);
		if (call == null) {
			LOGGER.error("Call cannot be null");
			return;
		}
		if (consumerOnSuccess == null) {
			LOGGER.error("Async consumer on success cannot be null");
			return;
		}
		Date startTime = new Date();
		// make asynchronous http request and get http response
		enqueueCall(call, consumerOnSuccess, consumerOnError, attempts, startTime);
	}

	/**
	 * This method starts a new asynchronous api call based on an already prepared
	 * call and return a {@link HttpResponse}
	 *
	 * @param call,
	 *            a prepared {@link Call} used for api call
	 * @param consumerOnSuccess,
	 *            the consumer used to handle success response
	 * @param consumerOnError,
	 *            the consumer used to handle error response
	 * @param attempts,
	 *            the number of attempts to test if an error occurs during the api
	 *            call
	 * @param startTime,
	 *            the start time of async api call
	 */
	private static void enqueueCall(Call<ResponseBody> call, Consumer<HttpResponse> consumerOnSuccess,
			Consumer<Throwable> consumerOnError, int attempts, Date startTime) {
		// make asynchronous http request and get http response
		call.clone().enqueue(new Callback<ResponseBody>() {
			@Override
			public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
				HttpResponse httpResponse = prepareHttpResponse(response);
				if ((httpResponse != null && httpResponse.isSuccessful()) || attempts <= 1) {
					Date endTime = new Date();
					LOGGER.info("Returned response in "
							+ String.format("%s ms", endTime.getTime() - startTime.getTime()) + " milliseconds");
					consumerOnSuccess.accept(httpResponse);
				} else {
					onFailure(call, new Throwable(
							httpResponse == null ? "HttpResponse is null" : httpResponse.getStatusCode()));
				}
			}

			@Override
			public void onFailure(Call<ResponseBody> call, Throwable t) {
				LOGGER.error("Error during executing asynchronous api call, received " + t.getMessage());
				if (attempts > 1) {
					int remainingAttempts = attempts - 1;
					LOGGER.error("Waiting " + SECONDS_TO_WAIT_BEFORE_RETRY + " seconds for retry... (remaining "
							+ remainingAttempts + " attempts)");
					try {
						Thread.sleep(SECONDS_TO_WAIT_BEFORE_RETRY * 1000);
					} catch (InterruptedException e) {
						// Unhandled exception
						Thread.currentThread().interrupt();
					}
					enqueueCall(call, consumerOnSuccess, consumerOnError, remainingAttempts, startTime);
				} else if (consumerOnError != null) {
					consumerOnError.accept(t);
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
	 */
	private static Call<ResponseBody> prepareCall(HttpRequest httpRequest) throws ExecutionException {
		if (httpRequest == null) {
			LOGGER.error("HttpRequest cannot be null");
			return null;
		}
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
		HttpMethod httpMethod = httpRequest.getHttpMethod() == null ? HttpMethod.GET : httpRequest.getHttpMethod();
		String apiEndpoint = httpRequest.getApiEndpoint();
		Map<String, String> headers = httpRequest.getHeaders() == null ? new HashMap<>() : httpRequest.getHeaders();
		Map<String, String> queryParams = httpRequest.getQueryParams() == null ? new HashMap<>()
				: httpRequest.getQueryParams();
		String queryString = httpRequest.getQueryString();
		if (queryString != null) {
			String[] queryStringSplitted = queryString.split("&");
			int index = 0;
			int sizeList = queryStringSplitted.length;
			for (; index < sizeList; index++) {
				String[] stringValues = queryStringSplitted[index].split("=");
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
			call = null;
			break;
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
	 * This method prepares and dispatch a {@code EventResponse} event on event bus
	 * using the given {@code eventIdentifier} through the
	 * {@link CoordinatorService}
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
		if (coordinatorService == null) {
			LOGGER.error("Coordinator service cannot be null");
			return;
		}
		new Thread(() -> {
			try {
				// obtaining http response
				HttpResponse httpResponse = callSync(httpRequest, attempts);
				if (httpRequest == null) {
					throw new ExecutionException("HttpResponse is null", null);
				} else {
					// preparing the event response based on http response
					EventResponse eventResponse = new EventResponse(eventIdentifier, httpResponse);
					// dispatching event response on event bus
					LOGGER.debug("Posting event response on event bus identifier by " + eventIdentifier);
					coordinatorService.post(eventResponse);
				}
			} catch (ExecutionException | IOException ex) {
				// preparing the event response based on http error
				EventResponse eventResponse = new EventResponse(eventIdentifier, ex);
				// dispatching event response on event bus
				LOGGER.error("Error during preparing event response for event " + eventIdentifier, ex.getMessage());
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
		if (consumerOnSuccess == null) {
			LOGGER.error("Reactive consumer on success cannot be null");
			return;
		}
		// prepare flowable to handle async response
		Flowable<HttpResponse> flowable = Flowable.fromCallable(() -> RestService.callSync(httpRequest, attempts));
		new Thread(() -> {
			if (consumerOnError == null) {
				LOGGER.debug("No consumer on error specified, using a default one");
				flowable.subscribe(consumerOnSuccess, err -> {
                    // unhandled event
                    LOGGER.error("Default consumer onError has received: " + err.getMessage());
                });
			} else {
				flowable.subscribe(consumerOnSuccess, consumerOnError);
			}
			flowable.publish();
		}).start();
	}

}
