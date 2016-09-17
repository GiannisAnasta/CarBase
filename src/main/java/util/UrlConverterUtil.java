package util;

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

}
