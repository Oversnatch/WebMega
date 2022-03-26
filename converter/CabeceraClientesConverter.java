package com.americacg.cargavirtual.web.converter;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.inject.Named;

import com.americacg.cargavirtual.gestion.model.CabeceraClientes;

@Named
@FacesConverter(value = "cabeceraClientesConverter")
public class CabeceraClientesConverter implements Converter {
	CabeceraClientes oCabeceraClientes = null;
	
	@Override
	public Object getAsObject(FacesContext facesContext, UIComponent component,
			String value) {
		
		oCabeceraClientes = null;
		
		if(value != null){
			Long id = null;
			//JAXBElement<Long> idXML = null;
			//ObjectFactory factory = new ObjectFactory(); 
			
			try{
				id = Long.parseLong(value);
			}
			catch(Exception ex){}

			if(id != null){
				//idXML = factory.createBancoIdBanco(id);
				oCabeceraClientes = new CabeceraClientes();
				oCabeceraClientes.getIdCliente().setValue(id);
			}
		}
		
		return oCabeceraClientes;
	}

	@Override
	public String getAsString(FacesContext facesContext, UIComponent component,
			Object o) {
		if(o instanceof CabeceraClientes && o != null){
			oCabeceraClientes = (CabeceraClientes) o;
			return oCabeceraClientes.getIdCliente().toString();
		}
		else
			return null;
	}
}