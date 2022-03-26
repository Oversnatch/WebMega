package com.americacg.cargavirtual.web.helpers;

import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;

@Named("selectOneMenuHelper")
public class SelectOneMenuHelper {

	protected String filterMatchModeContains = "contains";
	protected Boolean filter = false;

	public String getFilterMatchModeContains() {
		return filterMatchModeContains;
	}
	public void setFilterMatchModeContains(String filterMatchModeContains) {
		this.filterMatchModeContains = filterMatchModeContains;
	}
	public Boolean getFilter() {
		String origen = ((HttpServletRequest)FacesContext.getCurrentInstance().getExternalContext().getRequest()).getHeader("User-Agent");
		
		if(origen != null && !origen.toLowerCase().contains("mobi"))
			filter = true;
		else
			filter = false;

		return filter;
	}
	public void setFilter(Boolean filter) {
		this.filter = filter;
	}
	
	
}
