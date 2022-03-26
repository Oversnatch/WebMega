package com.americacg.cargavirtual.web.converter;

import java.util.Date;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;

import javax.el.ELContext;
import javax.el.ExpressionFactory;
import javax.el.ValueExpression;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.DateTimeConverter;
import javax.faces.convert.FacesConverter;
import javax.xml.datatype.XMLGregorianCalendar;

@FacesConverter(value = "xMLGregorianCalConverter")
public class XMLGregorianCalConverter extends DateTimeConverter {
 private static final TimeZone DEFAULT_TIME_ZONE = TimeZone.getTimeZone("GMT");
 private String dateStyle = "default";
 private Locale locale = null;
 private String pattern = null;
 private String timeStyle = "default";
 private TimeZone timeZone = DEFAULT_TIME_ZONE;
 private String type = "date";

@Override
public Object getAsObject(FacesContext context, UIComponent component, String newValue) {
    return null;
}

@Override
public String getAsString(FacesContext context, UIComponent component, Object value) {
    Map<String, Object> attributes = component.getAttributes();
    if(attributes.containsKey("pattern")){
        pattern = (String) attributes.get("pattern");
    }
    setPattern(pattern);
    if(attributes.containsKey("locale")){
        locale = (Locale) attributes.get("locale");
    }
    setLocale(locale);
    
    /*
    if(attributes.containsKey("timeZone")){
        //timeZone = (TimeZone) attributes.get("timeZone");
    	timeZone = TimeZone.getTimeZone((String)attributes.get("timeZone")) ;
    }
    */
    
    //TODO: Ver como tomar un huso horario generico
    ExpressionFactory expressionFactory = context.getApplication().getExpressionFactory();
    ELContext elContext = context.getELContext();
    ValueExpression vex = expressionFactory.createValueExpression(elContext, "#{puntoVentaView.usuario.husoHorario}", String.class);
    timeZone = TimeZone.getTimeZone((String) vex.getValue(elContext));
        
    setTimeZone(timeZone);
    
    if(attributes.containsKey("dateStyle")){
        dateStyle = (String) attributes.get("dateStyle");
    }
    setDateStyle(dateStyle);
    if(attributes.containsKey("timeStyle")){
        timeStyle = (String) attributes.get("timeStyle");
    }
    setTimeStyle(timeStyle);
    if(attributes.containsKey("type")){
        type = (String) attributes.get("type");
    }
    setType(type);

    XMLGregorianCalendar xmlGregCal = (XMLGregorianCalendar) value;
    Date date =  null;
    if(xmlGregCal != null) {
    	date = xmlGregCal.toGregorianCalendar().getTime();
    }
    return super.getAsString(context, component, date);
}

}