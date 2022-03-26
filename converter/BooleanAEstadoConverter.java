package com.americacg.cargavirtual.web.converter;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;
import javax.faces.convert.FacesConverter;

@FacesConverter(value = "booleanAEstadoConverter")
public class BooleanAEstadoConverter implements Converter {

	public Object getAsObject(FacesContext arg0, UIComponent arg1, String param)
	throws ConverterException {

		try {

			String ret;
			
			if ("true".equalsIgnoreCase(param)) {
				ret = "A";
			} else {
				ret = "D";
			}
			
			return ret;
		}
		catch (Exception exception) {
			throw new ConverterException(exception);
		}		

	}

	public String getAsString(FacesContext arg0, UIComponent arg1, Object arg2)	throws ConverterException {
		
			String state = (String)arg2;
			String ret;
			
			if ("A".equalsIgnoreCase(state)) {
				ret = "true";
			} else {
				ret = "false";
			}
		
			return ret;
	}

}
