package com.americacg.cargavirtual.web.converter;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.inject.Named;

import com.americacg.cargavirtual.gestion.model.Moneda;
import com.americacg.cargavirtual.gestion.model.ObjectFactory;

@Named
@FacesConverter(value = "monedaConverter")
public class MonedaConverter implements Converter {
	Moneda oMoneda = null;
	ObjectFactory oGestionObjFac = new ObjectFactory();
	
	@Override
	public Object getAsObject(FacesContext facesContext, UIComponent component,
			String value) {
		
		oMoneda = null;
		
		if(value != null){
			//Long id = null;
			//JAXBElement<Long> idXML = null;
			//ObjectFactory factory = new ObjectFactory(); 
			
			/*try{
				id = Long.parseLong(value);
			}
			catch(Exception ex){}*/

			//if(id != null){
				//idXML = factory.createBancoIdBanco(id);
				oMoneda = new Moneda();
				oMoneda.setIdMoneda(oGestionObjFac.createMonedaIdMoneda(value));
			//}
		}
		
		return oMoneda;
	}

	@Override
	public String getAsString(FacesContext facesContext, UIComponent component,
			Object o) {
		if(o instanceof Moneda && o != null){
			oMoneda = (Moneda) o;
			return oMoneda.getIdMoneda().getValue();
		}
		else
			return null;
	}
}