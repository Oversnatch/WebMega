package com.americacg.cargavirtual.web.converter;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.inject.Named;
import com.americacg.cargavirtual.web.model.superPago.Cuenta;

@Named
@FacesConverter(value = "cuentaConverter")
public class CuentaConverter implements Converter {
	Cuenta oCuenta = null;
	
	@Override
	public Object getAsObject(FacesContext facesContext, UIComponent component,
			String value) {
		
		oCuenta = null;
		
		if(value != null){
			Long id = null;
			//JAXBElement<Long> idXML = null;
			//ObjectFactory factory = new ObjectFactory(); 
			
			try{
				id = Long.parseLong(value);
			}
			catch(Exception ex){}

			if(id != null){
				//idXML = factory.createCuentaIdCuenta(id);
				oCuenta = new Cuenta();
				oCuenta.setIdCuenta(id);
			}
		}
		
		return oCuenta;
	}

	@Override
	public String getAsString(FacesContext facesContext, UIComponent component,
			Object o) {
		if(o instanceof Cuenta && o != null){
			oCuenta = (Cuenta) o;
			return oCuenta.getIdCuenta().toString();
		}
		else
			return null;
	}
}
