package view.converter;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

@FacesConverter(forClass = String.class)
public class TrimStringConverter implements Converter {

    @Override
    public String getAsObject(FacesContext context, UIComponent component, String value) {
        if(value ==null){
            return "";
        }
        return value.trim();
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, Object value) {
        if(value ==null){
            return "";
        }
       return value.toString();
    }

}
