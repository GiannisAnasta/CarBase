package util;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.validator.routines.UrlValidator;

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

    public boolean isValidScheme(String scheme) {
        String[] schemes = {"http", "https"};
        UrlValidator urlValidator = new UrlValidator(schemes);
        if (urlValidator.isValid("ftp://foo.bar.com/")) {
            System.out.println("url is valid");
        } else {
            System.out.println("url is invalid");
        }
        return true;
    }
}
