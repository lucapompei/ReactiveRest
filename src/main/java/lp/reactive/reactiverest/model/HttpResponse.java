package lp.reactive.reactiverest.model;

import lp.reactive.reactiverest.utils.JsonUtils;
import okhttp3.ResponseBody;
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
	private Response<ResponseBody> rawResponse;

	/**
	 * Construct a {@link HttpResponse} using the raw response obtained from
	 * http request
	 *
	 * @param rawResponse,
	 *            the raw response obtained from http request
	 */
	public HttpResponse(Response<ResponseBody> rawResponse) {
		this.rawResponse = rawResponse;
	}

	/**
	 * Retrieve the raw response obtained from http request
	 *
	 * @return the raw response obtained from http request
	 */
	public Response<ResponseBody> getRawResponse() {
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
		return rawResponse.raw().code() + " " + rawResponse.raw().message();
	}

	/**
	 * Retrieve the json body of the raw response if no errors occur, otherwise
	 * return the json error body
	 *
	 * @return the json body of the raw response if no errors occur, otherwise
	 *         return the json error body
	 */
	public String getJsonBody() {
		if (rawResponse.isSuccessful()) {
			ResponseBody responseBody  = rawResponse.body();
			if (responseBody != null) {
				return responseBody.toString();
			} else {
				return JsonUtils.toJson(rawResponse.errorBody());
			}
		} else {
			return JsonUtils.toJson(rawResponse.errorBody());
		}
	}

	/**
	 * Return a boolean indicating if the http response has been successful or
	 * not
	 *
	 * @return a boolean indicating if the http response has been successful or
	 *         not
	 */
	public boolean isSuccessful() {
		return rawResponse.isSuccessful();
	}

	/**
	 * Return a prepared string to represent the obtained response
	 *
	 * @return a prepared string to represent the obtained response
	 */
	public String toString() {
		return "HttpResponse:\nUrl: " + getCalledUrl() + "\n" + getStatusCode() + "\n" + getJsonBody();
	}

}
