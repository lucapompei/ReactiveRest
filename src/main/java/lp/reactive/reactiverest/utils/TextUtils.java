package lp.reactive.reactiverest.utils;

/**
 * This class exposes utils to handle generic text operations
 *
 * @author lucapompei
 */
public class TextUtils {

	/**
	 * Private constructor for an utility class, construct a new {@code TextUtils}
	 */
	private TextUtils() {
		// Empty implementation
	}

	/**
	 * This method checks if the passed value is {@code null} or empty
	 *
	 * @param value,
	 *            the string value to check
	 * @return a {@code boolean} indicating if the checked string value is
	 *         {@code null} or empty or not
	 */
	public static boolean isNullOrEmpty(String value) {
		return value == null || value.isEmpty();
	}

	/**
	 * This method add missing http protocol to the given base url
	 *
	 * @param baseUrl,
	 *            the base url to check
	 * @return a formatted base url with a default http protocol
	 */
	public static String configHttpProtocolForBaseUrl(String baseUrl) {
		String regexForHttpProtocol = "^(http|https)://.*";
		if (baseUrl != null && !baseUrl.matches(regexForHttpProtocol)) {
			return "http://" + baseUrl;
		} else {
			return baseUrl;
		}
	}

}
