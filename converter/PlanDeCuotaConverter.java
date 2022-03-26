package com.americacg.cargavirtual.web.converter;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.inject.Named;

import com.americacg.cargavirtual.gateway.pagoElectronico.model.entidad.PlanDeCuota;

@Named
@FacesConverter(value = "planDeCuotaConverter")
public class PlanDeCuotaConverter implements Converter {
	PlanDeCuota oPlanDeCuota = null;
	
	@Override
	public Object getAsObject(FacesContext facesContext, UIComponent component,
			String value) {
		
		oPlanDeCuota = null;
		
		if(value != null){
			Long id = null;
			//JAXBElement<Long> idXML = null;
			//ObjectFactory factory = new ObjectFactory(); 
			
			try{
				id = Long.parseLong(value);
			}
			catch(Exception ex){}

			if(id != null){
				//idXML = factory.createPlanDeCuotaIdPlanDeCuota(id);
				oPlanDeCuota = new PlanDeCuota();
				oPlanDeCuota.setId(id);
			}
		}
		
		return oPlanDeCuota;
	}

	@Override
	public String getAsString(FacesContext facesContext, UIComponent component,
			Object o) {
		if(o instanceof PlanDeCuota && o != null){
			oPlanDeCuota = (PlanDeCuota) o;
			return oPlanDeCuota.getId().toString();
		}
		else
			return null;
	}
}
