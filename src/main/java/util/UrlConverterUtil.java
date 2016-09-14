package util;

import java.net.URI;
import java.net.URISyntaxException;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;
import org.apache.commons.lang3.StringUtils;

@Named
@ApplicationScoped
public class UrlConverterUtil {

    public String normalize(String value) {
        if (StringUtils.isBlank(value)) {
            return "";
        }
        StringBuilder url = new StringBuilder();

        if (!value.startsWith("https://", 0)) {
            if (!value.startsWith("http://", 0)) {
                url.append("http://");
                if (!value.startsWith("www.", 0)) {
                    url.append("www.");
                }
            }
        }
        url.append(value);

        return url.toString();
    }

    public boolean isCorrect(String value) {

        if (StringUtils.isBlank(value)) {
            return false;
        }
//        StringBuilder url = new StringBuilder();
//
//        if (!value.startsWith("https://", 0)) {
//            if (!value.startsWith("http://", 0)) {
//                url.append("http://");
//                if (!value.startsWith("www.", 0)) {
//                    url.append("www.");
//                }
//            }
//        }
//        url.append(value);
        String url = normalize(value);

        try {
            new URI(url.toString());
        } catch (URISyntaxException e) {
            return false;
        }
        return true;
    }
}
