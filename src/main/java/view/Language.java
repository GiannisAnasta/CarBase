package view;

import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;

import javax.faces.context.FacesContext;
import javax.inject.Named;

@Named(value = "language")
@SessionScoped
public class Language implements Serializable {

    private static final long serialVersionUID = 1L;

    private String localeCode;

    private static final Map<String, Object> countries;

    static {
        countries = new LinkedHashMap<String, Object>();
        Locale eng = Locale.ENGLISH;
        countries.put(eng.getLanguage(), eng);
        Locale russ = new Locale("ru");
        countries.put(russ.getLanguage(), russ);
        Locale bul = new Locale("bg");
        countries.put(bul.getLanguage(), bul);
    }

    @PostConstruct
    private void init() {
        localeCode = FacesContext.getCurrentInstance()
                .getViewRoot().getLocale().getLanguage();
    }

    public Map<String, Object> getCountriesInMap() {
        return countries;
    }

    public String getLocaleCode() {
        return localeCode;
    }

    public void setLocaleCode(String localeCode) {
        this.localeCode = localeCode;
    }

    public void countryLocaleCodeChanged() {

        //loop a map to compare the locale code
        for (Map.Entry<String, Object> entry : countries.entrySet()) {

            if (entry.getValue().toString().equals(localeCode)) {

                FacesContext.getCurrentInstance()
                        .getViewRoot().setLocale((Locale) entry.getValue());

            }
        }

    }

}
