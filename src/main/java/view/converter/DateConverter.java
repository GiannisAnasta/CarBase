package view.converter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

@FacesConverter(forClass = Date.class)
public class DateConverter implements Converter {
    private static final SimpleDateFormat format = new SimpleDateFormat("yyyy:MM:dd HH:mm:ss");

    @Override
    public Date getAsObject(FacesContext context, UIComponent component, String value) {
        if (value == null) {
            return null;
        }
        try {
            Date parse = format.parse(value);
//            System.out.println("parsed "+parse.getTime());
            return parse;
        } catch (ParseException ex) {
            Logger.getLogger(DateConverter.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, Object value) {
        if (value == null) {
            return "";
        }
        Date date = (Date) value;    
//        System.out.println("-formatted "+ date.getTime());
        String formattedDate = format.format(date);
        System.out.println("+formatted "+ formattedDate);
        return formattedDate;
    }

}
