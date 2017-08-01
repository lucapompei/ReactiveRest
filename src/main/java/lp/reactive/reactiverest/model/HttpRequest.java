package lp.reactive.reactiverest.model;

import java.util.Map;

/**
 * This entity represents the http request used to make api call
 *
 * @author lucapompei
 */
public class HttpRequest {

	/**
	 * The base url used to compose the api call
	 */
	private final String baseUrl;

	/**
	 * The relative path used to specify the api endpoint and
	 *            formulate the complete api call
	 */
	private final String apiEndpoint;

	/**
	 * The set of headers used for api call
	 */
	private final Map<String, String> headers;

	/**
	 * The query string used to compose the api call expressed as a
	 *            map
	 */
	private final Map<String, String> queryParams;

	/**
	 * The query string used to compose the api call expressed as a
	 *            single string
	 */
	private final String queryString;

	/**
	 * The http method used to compose the api call
	 */
	private final HttpMethod httpMethod;

	/**
	 * The body parameters used to compose the api call
	 */
	private final Map<String, String> bodyParams;

	/**
	 * Constructor a new {@link HttpRequest} starting from the given
	 * {@param builder}
	 */
	private HttpRequest(Builder builder) {
		this.baseUrl = builder.baseUrl;
		this.apiEndpoint = builder.apiEndpoint;
		this.httpMethod = builder.httpMethod;
		this.headers = builder.headers;
		this.queryParams = builder.queryParams;
		this.queryString = builder.queryString;
		this.bodyParams = builder.bodyParams;
	}

	/**
	 * Getter method for retrieve the base url
	 *
	 * @return the base url
	 */
	public String getBaseUrl() {
		return baseUrl;
	}

	/**
	 * Getter method for retrieve the api endpoint
	 *
	 * @return the api endpoint
	 */
	public String getApiEndpoint() {
		return apiEndpoint;
	}

	/**
	 * Getter method for retrieve the headers
	 *
	 * @return the headers
	 */
	public Map<String, String> getHeaders() {
		return headers;
	}

	/**
	 * Getter method for retrieve the query string expressed as a map
	 *
	 * @return the query string
	 */
	public Map<String, String> getQueryParams() {
		return queryParams;
	}

	/**
	 * Getter method for retrieve the query string expressed as a map
	 *
	 * @return the query string
	 */
	public String getQueryString() {
		return queryString;
	}

	/**
	 * Getter method for retrieve the http method
	 *
	 * @return the http method
	 */
	public HttpMethod getHttpMethod() {
		return httpMethod;
	}

	/**
	 * Getter method for retrieve the body parameters
	 *
	 * @return the body parameters
	 */
	public Map<String, String> getBodyParams() {
		return bodyParams;
	}

	/**
	 * Return a prepared string to represent this object
	 *
	 * @return a prepared string to represent this object
	 */
	public String toString() {
		return "HttpRequest:\n" + "Url: " + getBaseUrl() + getApiEndpoint() + "\n" + "Method: " + getHttpMethod() + "\n"
				+ "Headers: " + getHeaders() + "\n" + "Query param: " + getQueryParams() + "\nQuery string: "
				+ getQueryString() + "\n" + "Body params:" + " " + getBodyParams();
	}

	/**
	 * Static builder to build and configure a new {@link HttpRequest}
	 */
	public static class Builder {

		/**
		 * The base url used to compose the api call
		 */
		private String baseUrl;

		/**
		 * The relative path used to specify the api endpoint and
		 *            formulate the complete api call
		 */
		private String apiEndpoint;

		/**
		 * The set of headers used for api call
		 */
		private Map<String, String> headers;

		/**
		 * The query string used to compose the api call
		 */
		private Map<String, String> queryParams;

		/**
		 * The query string used to compose the api call
		 */
		private String queryString;

		/**
		 * The http method used to compose the api call
		 */
		private HttpMethod httpMethod;

		/**
		 * The body parameters used to compose the api call
		 */
		private Map<String, String> bodyParams;

		/**
		 * Constructor of {@link Builder} based on mandatory parameters
		 *
		 * @param baseUrl,
		 *            the base url used to compose the api call
		 * @param apiEndpoint,
		 *            the relative path used to specify the api endpoint and
		 *            formulate the complete api call
		 */
		public Builder(String baseUrl, String apiEndpoint) {
			this.baseUrl = baseUrl;
			this.apiEndpoint = apiEndpoint;
		}

		/**
		 * Setter method for the headers
		 *
		 * @param httpMethod,
		 *            the http method used for api call
		 * @return a {@link Builder} with http method set
		 */
		public Builder httpMethod(HttpMethod httpMethod) {
			this.httpMethod = httpMethod;
			return this;
		}

		/**
		 * Setter method for the headers
		 *
		 * @param headers,
		 *            a set of headers used for api call
		 * @return a {@link Builder} with headers set
		 */
		public Builder headers(Map<String, String> headers) {
			this.headers = headers;
			return this;
		}

		/**
		 * Setter method for the query string
		 *
		 * @param queryParams,
		 *            the query string used to compose the api call
		 * @return a {@link Builder} with a query string parameters set
		 */
		public Builder queryParams(Map<String, String> queryParams) {
			this.queryParams = queryParams;
			return this;
		}

		/**
		 * Setter method for the query string
		 *
		 * @param queryString,
		 *            the query string used to compose the api call
		 * @return a {@link Builder} with a query string parameters set
		 */
		public Builder queryString(String queryString) {
			this.queryString = queryString;
			return this;
		}

		/**
		 * Setter method for the body parameters
		 *
		 * @param bodyParams,
		 *            the body parameters used to compose the api call
		 * @return a {@link Builder} with a body parameters set
		 */
		public Builder bodyParams(Map<String, String> bodyParams) {
			this.bodyParams = bodyParams;
			return this;
		}

		/**
		 * Build a new {@link HttpRequest} based on constructed and configured
		 * {@link Builder}
		 *
		 * @return a {@link HttpRequest}
		 */
		public HttpRequest build() {
			return new HttpRequest(this);
		}

	}

}
