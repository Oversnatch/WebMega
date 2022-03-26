package com.americacg.cargavirtual.web.shared;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.event.ValueChangeEvent;
import javax.inject.Named;

import org.primefaces.PrimeFaces;
import org.primefaces.event.SelectEvent;
import org.springframework.context.annotation.Scope;

import com.americacg.cargavirtual.gestion.model.ArrayOfCabeceraClientes;
import com.americacg.cargavirtual.gestion.model.CabeceraClientes;
import com.americacg.cargavirtual.web.enums.CfgTimeout;
import com.americacg.cargavirtual.web.helpers.GestionServiceHelper;

@Named("autoCompleteClienteView")
@Scope(org.springframework.web.context.WebApplicationContext.SCOPE_SESSION)
public class AutoCompleteClienteView extends BasePage implements Serializable {

	private static final long serialVersionUID = -7102647559155338637L;
	protected Boolean incluirMayorista = false;
	private String campoBusqueda = "";
	private boolean busquedaPorNumero = false;
	private Long id = null;
	private String descripcion = null;
	ArrayOfCabeceraClientes clientes = new ArrayOfCabeceraClientes();

	public List<CabeceraClientes> completeClientes(String query) {
		List<CabeceraClientes> results = new ArrayList<CabeceraClientes>();

		if (clientes != null && clientes.getCabeceraClientes() != null) {
			for (CabeceraClientes c : clientes.getCabeceraClientes()) {
				results.add(c);
			}
		}
		return results;
	}

	public void onItemSelect(SelectEvent<String> event) {
		obtenerCliente();
		if (this.id != null) {
			this.campoBusqueda = this.id.toString();
		}
	}

	public void onQuery() {
		this.clientes = null;
		if (this.busquedaPorNumero) {

		} else {
			this.clientes = GestionServiceHelper.getGestionService(CfgTimeout.CONSULTA).clientesListar(
					this.getUsuario().getIdMayorista(), this.getUsuario().getIdCliente(), this.campoBusqueda,
					incluirMayorista);
		}
	}

	public void onClear() {
		this.campoBusqueda = "";
		this.busquedaPorNumero = false;
		this.id = null;
		this.descripcion = null;
		this.clientes = new ArrayOfCabeceraClientes();
		PrimeFaces.current().ajax().update("autoCompleteClienteImagenOK");
	}

	public void validator(FacesContext facesContext, UIComponent uiComponent, Object object) {
		obtenerCliente();
		PrimeFaces.current().ajax().update("autoCompleteClienteImagenOK");
	}

	private void obtenerCliente() {
		this.clientes = GestionServiceHelper.getGestionService(CfgTimeout.CONSULTA).clientesListar(
				this.getUsuario().getIdMayorista(), this.getUsuario().getIdCliente(), this.campoBusqueda,
				incluirMayorista);

		if (this.clientes != null && this.clientes.getCabeceraClientes() != null) {
			if (this.clientes.getCabeceraClientes().size() == 1) {
				this.id = this.clientes.getCabeceraClientes().get(0).getIdCliente().getValue();
				this.descripcion = this.clientes.getCabeceraClientes().get(0).getRazonSocial().getValue();
			} else {
				this.id = null;
				this.descripcion = null;
			}
		} else {
			this.id = null;
			this.descripcion = null;
		}
	}

	public void valueChangeListener(ValueChangeEvent event) {
		campoBusqueda = "";

		if (event != null && event.getNewValue() != null) {
			campoBusqueda = event.getNewValue().toString();
		}

		busquedaPorNumero = false;
		try {
			Long.parseLong(campoBusqueda);
			busquedaPorNumero = true;
		} catch (Exception e) {
			// TODO: handle exception
			busquedaPorNumero = false;
		}
	}

	public Boolean getIncluirMayorista() {
		return incluirMayorista;
	}

	public void setIncluirMayorista(Boolean incluirMayorista) {
		this.incluirMayorista = incluirMayorista;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public String getCampoBusqueda() {
		return campoBusqueda;
	}

	public void setCampoBusqueda(String campoBusqueda) {
		this.campoBusqueda = campoBusqueda;
	}

}