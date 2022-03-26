package com.americacg.cargavirtual.web.converter;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.inject.Named;
import com.americacg.cargavirtual.web.model.superPago.ItemAgendaDestinatario;

@Named
@FacesConverter(value = "itemAgDestConverter")
public class ItemAgDestConverter implements Converter {
	ItemAgendaDestinatario oItemAgendaDestinatario = null;
	
	@Override
	public Object getAsObject(FacesContext facesContext, UIComponent component,
			String value) {
		
		oItemAgendaDestinatario = null;
		
		if(value != null){
			Long id = null;
			//JAXBElement<Long> idXML = null;
			//ObjectFactory factory = new ObjectFactory(); 
			
			try{
				id = Long.parseLong(value);
			}
			catch(Exception ex){}

			if(id != null){
				//idXML = factory.createItemAgendaDestinatarioIdItemAgendaDestinatario(id);
				oItemAgendaDestinatario = new ItemAgendaDestinatario();
				oItemAgendaDestinatario.setIdAgendaDestinatarioCta(id);
			}
		}
		
		return oItemAgendaDestinatario;
	}

	@Override
	public String getAsString(FacesContext facesContext, UIComponent component,
			Object o) {
		if(o instanceof ItemAgendaDestinatario && o != null){
			oItemAgendaDestinatario = (ItemAgendaDestinatario) o;
			return oItemAgendaDestinatario.getIdAgendaDestinatarioCta().toString();
		}
		else
			return null;
	}
}
