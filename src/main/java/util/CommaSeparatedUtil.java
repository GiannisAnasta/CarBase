package util;

import java.util.List;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;
import org.apache.commons.lang3.StringUtils;

@Named
@ApplicationScoped
public class CommaSeparatedUtil {

    public static String getAsCommaSeparated(List<String> values) {
        return StringUtils.join(values, ';');
    }
}
