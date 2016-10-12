package view.converter;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;
import view.FormatOfData;

@Named
@ApplicationScoped
public class LocalizationKeyConvertor {

    public String toLocalizationKey(FormatOfData fd) {
        switch (fd) {
            case EMPTY_LINE_SEPARATED:
                return "emptyLineSeparated";
            case NEW_LINE_SEPARATED:
                return "newLineSeparated";
            default:
                return "unknown key";
        }
    }

}
