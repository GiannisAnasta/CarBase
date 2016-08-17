package view.converter;

import java.util.Arrays;
import java.util.List;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;
import javax.faces.convert.FacesConverter;

@FacesConverter("ListStringConverter")
public class ListStringConverter implements Converter {

    @Override
    public Object getAsObject(FacesContext context, UIComponent component,
            String value) {

        String[] split = value.split("\n");

//           
//            if (!urlValidator.isValid(url.toString())) {
//
//                FacesMessage msg
//                        = new FacesMessage("URL Conversion error.",
//                                "Invalid URL format.");
//                msg.setSeverity(FacesMessage.SEVERITY_ERROR);
//                throw new ConverterException(msg);
//            }
        return Arrays.asList(split);
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component,
            Object value) {
        String toStringValue = "";

        List<String> obj = (List<String>) value;
        for (String item : obj) {
            toStringValue += item;
            toStringValue += '\n';
        }

        return toStringValue;

    }
}
