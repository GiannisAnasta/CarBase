package view.converter;

import java.util.ArrayList;
import java.util.List;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

@FacesConverter("ListStringConverter")
public class ListStringConverter implements Converter {

    @Override
    public Object getAsObject(
            FacesContext context,
            UIComponent component,
            String value) {

        String[] split = value.split("\n");
        List<String> asList = new ArrayList<>();
        for (String item : split) {
            if (!item.trim().isEmpty()) {
                asList.add(item.trim());
            }
        }
//        System.out.println("getAsObject");
        return asList;
    }

    @Override
    public String getAsString(
            FacesContext context,
            UIComponent component,
            Object value) {
        String toStringValue = "";

        List<String> obj = (List<String>) value;
        for (String item : obj) {
            toStringValue += item;
            toStringValue += '\n';
        }
//        System.out.println("getAsString");
        return toStringValue;

    }
}
