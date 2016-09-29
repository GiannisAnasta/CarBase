package util;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;
import org.apache.commons.lang3.StringUtils;

@Named
@ApplicationScoped
public class UrlConverterUtil {

    private static final String SEPARATOR_PROTOCOL = "://";

    private static final String HTTP = "http";
    private static final String HTTPS = "https";
    private static final String WWW = "www.";
    private static final String SLASH = "/";

    public static String normalize(String value) {
        if (StringUtils.isBlank(value)) {
            return "";
        }
        StringBuilder url = new StringBuilder();
        url.append(getProtocol(value));
        url.append(WWW);
        url.append(getNamedPart(value));
        return url.toString();
    }

    private static String getProtocol(String value) {
        StringBuilder protocolName = new StringBuilder();
        String[] split = value.split(SEPARATOR_PROTOCOL);

        if (HTTPS.equals(split[0])) {
            protocolName.append(HTTPS);
        } else {
            protocolName.append(HTTP);
        }
        protocolName.append(SEPARATOR_PROTOCOL);
        return protocolName.toString();
    }

    private static String getNamedPart(String value) {
        String[] split = value.split(SEPARATOR_PROTOCOL);
        String nameContainedPart = split.length == 1 ? split[0] : split[1];

        return filterLastSlash(nameContainedPart.startsWith(WWW)
                ? nameContainedPart.substring(WWW.length())
                : nameContainedPart);

    }

    private static String filterLastSlash(String value) {
        return value.endsWith(SLASH) ? value.substring(0, value.length() - SLASH.length()) : value;
    }
}
