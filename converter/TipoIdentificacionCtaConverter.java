package com.americacg.cargavirtual.web.converter;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.inject.Named;
import com.americacg.cargavirtual.web.model.superPago.TipoIdentificacionCta;

@Named
@FacesConverter(value = "tipoIdentificacionCtaConverter")
public class TipoIdentificacionCtaConverter implements Converter {
	TipoIdentificacionCta oTipoIdentificacionCta = null;
	
	@Override
	public Object getAsObject(FacesContext facesContext, UIComponent component,
			String value) {
		
		oTipoIdentificacionCta = null;
		
		if(value != null){
			Long id = null;
			//JAXBElement<Long> idXML = null;
			//ObjectFactory factory = new ObjectFactory(); 
			
			try{
				id = Long.parseLong(value);
			}
			catch(Exception ex){}

			if(id != null){
				//idXML = factory.createTipoIdentificacionCtaIdTipoIdentificacionCta(id);
				oTipoIdentificacionCta = new TipoIdentificacionCta();
				oTipoIdentificacionCta.setIdTipoIdentifCta(id);
			}
		}
		
		return oTipoIdentificacionCta;
	}

	@Override
	public String getAsString(FacesContext facesContext, UIComponent component,
			Object o) {
		if(o instanceof TipoIdentificacionCta && o != null){
			oTipoIdentificacionCta = (TipoIdentificacionCta) o;
			return oTipoIdentificacionCta.getIdTipoIdentifCta().toString();
		}
		else
			return null;
	}
}
