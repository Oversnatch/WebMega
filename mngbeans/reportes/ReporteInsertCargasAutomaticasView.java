package com.americacg.cargavirtual.web.mngbeans.reportes;

import java.util.ArrayList;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.model.SelectItem;
import javax.inject.Named;
import org.springframework.context.annotation.Scope;
import com.americacg.cargavirtual.gestion.model.ArrayOfCabeceraProducto;
import com.americacg.cargavirtual.gestion.model.CabeceraProducto;
import com.americacg.cargavirtual.gestion.model.RespString;
import com.americacg.cargavirtual.web.enums.CfgTimeout;
import com.americacg.cargavirtual.web.helpers.GestionServiceHelper;
import com.americacg.cargavirtual.web.model.Error;

@Named("reporteInsertCargasAutomaticasView")
@Scope(org.springframework.web.context.WebApplicationContext.SCOPE_SESSION)
public class ReporteInsertCargasAutomaticasView extends ReporteGeneral<Object> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1619646550400248023L;

	private Long idProducto;
	private List<SelectItem> productos;
	private String cargasAutomaticas;
	private Long idClienteCarga;
	private Long idUsuarioCarga;

	@Override
	public Error resetearReporte() {
		Error e = super.resetearReporte();

		this.idProducto = 0L;
		this.cargasAutomaticas = "";
		this.idClienteCarga = null;
		this.idUsuarioCarga = null;

		return e;
	}

	public void insertarCargasAutomaticas(ActionEvent ae) {

		try {

			RespString rs = GestionServiceHelper.getGestionService(CfgTimeout.DEFAULT).insertCargasAutomaticas(
					this.getUsuario().getIdMayorista(), idClienteCarga, idUsuarioCarga, idProducto,
					this.getUsuario().getIdCliente(), this.getUsuario().getIdUsuario(), this.getUsuario().getPassword(),
					cargasAutomaticas);

			if (rs != null) {
				if (rs.getError().getValue().getHayError().getValue()) {
					FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
							rs.getError().getValue().getMsgError().getValue(), null));
				} else {
					// Respuesta OK

					String r[] = rs.getRespuesta().getValue().split(Character.toString((char) 185), -1);

					if (r.length > 0) {
						FacesContext.getCurrentInstance().addMessage(null,
								new FacesMessage(FacesMessage.SEVERITY_ERROR, r[0], null));
					}

					if (r.length > 1) {
						String r1[] = r[1].split("\n", -1);

						for (int i = 0; i < r1.length; i++) {
							FacesContext.getCurrentInstance().addMessage(null,
									new FacesMessage(FacesMessage.SEVERITY_ERROR, r1[i], null));

						}
					}

					// Reseteo la variables pines para que se limpie la inputTextarea
					cargasAutomaticas = "";
				}
			} else {
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
						"La respuesta del metodo InsertCargasAutomaticas es null", null));
			}

		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
					"Error ejecutando el WS de InsertCargasAutomaticas: |" + e.getMessage() + "|", null));
		}

		return;
	}

	public Long getIdProducto() {
		return idProducto;
	}

	public void setIdProducto(Long idProducto) {
		this.idProducto = idProducto;
	}

	public List<SelectItem> getProductos() {
		try {
			this.productos = new ArrayList<SelectItem>();
			ArrayOfCabeceraProducto l = GestionServiceHelper.getGestionService(CfgTimeout.DEFAULT)
					.mostrarCabeceraProductos(this.getUsuario().getIdMayorista(), "ON", true);
			for (CabeceraProducto cp : l.getCabeceraProducto()) {
				this.productos.add(new SelectItem(cp.getIdProducto().getValue(), cp.getDescProducto().getValue()));
			}
		} catch (Exception e) {
			// Error cargando productos por proveedor
			// No hago nada
		}
		return productos;
	}

	public void setProductos(List<SelectItem> productos) {
		this.productos = productos;
	}

	public String getCargasAutomaticas() {
		return cargasAutomaticas;
	}

	public void setCargasAutomaticas(String cargasAutomaticas) {
		this.cargasAutomaticas = cargasAutomaticas;
	}

	public Long getIdClienteCarga() {
		return idClienteCarga;
	}

	public void setIdClienteCarga(Long idClienteCarga) {
		this.idClienteCarga = idClienteCarga;
	}

	public Long getIdUsuarioCarga() {
		return idUsuarioCarga;
	}

	public void setIdUsuarioCarga(Long idUsuarioCarga) {
		this.idUsuarioCarga = idUsuarioCarga;
	}
}
