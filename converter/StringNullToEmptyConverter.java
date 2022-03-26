package com.americacg.cargavirtual.web.converter;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;
import javax.faces.convert.FacesConverter;

import org.springframework.util.StringUtils;


@FacesConverter(value = "stringNullToEmptyConverter")
public class StringNullToEmptyConverter implements Converter {

	
	public Object getAsObject(FacesContext context, UIComponent component,
			String value) throws ConverterException {
		
		if (StringUtils.isEmpty(value)) {
			value = "";
		}
		
		return value;
	}

	public String getAsString(FacesContext context, UIComponent component,
			Object value) throws ConverterException {
		
		String ret = (String)value;

		if (StringUtils.isEmpty(ret)) {
			value = "";
		}
		
		
		return ret;
	}

}
