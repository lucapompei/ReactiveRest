package lp.reactive.reactiverest.utils;

/**
 * This class exposes utils to handle generic text operations
 *
 * @author lucapompei
 */
public class TextUtils {

    /**
     * This method checks if the passed {@param value} is {@code null} or empty
     *
     * @param value,
     *         the string value to check
     * @return a {@code boolean} indicating if the checked string value is {@code null} or empty or not
     */
    public static boolean isNullOrEmpty(String value) {
        return value == null || value.isEmpty();
    }

    /**
     * This method add missing http protocol to the given {@param baseUrl}
     *
     * @param baseUrl,
     *         the base url to check
     * @return a formatted base url with a default http protocol
     */
    public static String configHttpProtocolForBaseUrl(String baseUrl) {
        String regexForHttpProtocol = "^(http|https)://.*";
        if (!baseUrl.matches(regexForHttpProtocol)) {
            return "http://" + baseUrl;
        } else {
            return baseUrl;
        }
    }

}
