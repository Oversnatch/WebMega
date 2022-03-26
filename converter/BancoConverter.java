package com.americacg.cargavirtual.web.converter;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.inject.Named;
import com.americacg.cargavirtual.web.model.Banco;

@Named
@FacesConverter(value = "bancoConverter")
public class BancoConverter implements Converter {
	Banco oBanco = null;
	
	@Override
	public Object getAsObject(FacesContext facesContext, UIComponent component,
			String value) {
		
		oBanco = null;
		
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
				oBanco = new Banco();
				oBanco.setIdBanco(id);
			}
		}
		
		return oBanco;
	}

	@Override
	public String getAsString(FacesContext facesContext, UIComponent component,
			Object o) {
		if(o instanceof Banco && o != null){
			oBanco = (Banco) o;
			return oBanco.getIdBanco().toString();
		}
		else
			return null;
	}
}
