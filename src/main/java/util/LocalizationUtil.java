package util;

import java.util.ResourceBundle;
import javax.faces.context.FacesContext;

public final class LocalizationUtil {

    public static String getMessage(String key) {
        ResourceBundle boundle = ResourceBundle.getBundle("locale", FacesContext.getCurrentInstance().getViewRoot().getLocale());
        String value = null;
        try {
            value = boundle.getString(key);
        } catch (ClassCastException ex) {
            value = "???" + key + "???";
        }
        return value;
    }

}
