package com.americacg.cargavirtual.web.mngbeans.reportes;

import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.model.SelectItem;
import javax.inject.Named;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import javax.xml.ws.WebServiceException;

import org.springframework.context.annotation.Scope;

import com.americacg.cargavirtual.gestion.model.ArrayOfCabeceraProducto;
import com.americacg.cargavirtual.gestion.model.ArrayOfProveedorRed;
import com.americacg.cargavirtual.gestion.model.CabeceraProducto;
import com.americacg.cargavirtual.gestion.model.InformeComisionesVigentes;
import com.americacg.cargavirtual.gestion.model.ProveedorRed;
import com.americacg.cargavirtual.gestion.model.RespString;
import com.americacg.cargavirtual.web.enums.CfgTimeout;
import com.americacg.cargavirtual.web.helpers.GestionServiceHelper;
import com.americacg.cargavirtual.web.helpers.LogACGHelper;
import com.americacg.cargavirtual.web.model.Error;

@Named("reporteCargaPinesView")
@Scope(org.springframework.web.context.WebApplicationContext.SCOPE_SESSION)
public class ReporteCargaPinesView extends ReporteGeneral<InformeComisionesVigentes> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1900006137607608943L;
	private Date fechaVencimiento;
	private Long idProveedor;
	private Long idProducto;
	private List<SelectItem> productos;
	private List<SelectItem> proveedores;
	private String vigenciaMeses;
	private String pines;

	@Override
	public Error resetearReporte() {
		Error e = super.resetearReporte();
		this.idProveedor = 0L;
		this.idProducto = 0L;
		this.pines = "";
		this.vigenciaMeses = "12";

		try {

			this.productos = new ArrayList<SelectItem>();
			ArrayOfCabeceraProducto cp = GestionServiceHelper.getGestionService(CfgTimeout.REPORTE)
					.buscarProductosPorProveedor(this.getUsuario().getIdMayorista(), idProveedor, "OFF", true);

			for (CabeceraProducto prod : cp.getCabeceraProducto()) {
				this.productos.add(new SelectItem(prod.getIdProducto().getValue(), prod.getDescProducto().getValue()));
			}
			this.proveedores = new ArrayList<SelectItem>();
			ArrayOfProveedorRed lp = GestionServiceHelper.getGestionService(CfgTimeout.REPORTE)
					.buscarProveedores(this.getUsuario().getIdMayorista(), null, true);
			for (ProveedorRed pr : lp.getProveedorRed()) {
				this.proveedores.add(new SelectItem(pr.getIdProveedor().getValue(), pr.getRazonSocial().getValue()));
			}

		} catch (WebServiceException ste) {
			if (ste != null && ste.getCause() != null && ste.getCause().getClass() != null) {
				if (ste.getCause().getClass().equals(ConnectException.class)) {
					e.setError("GST-TOC",
							"No se pudo establecer la comunicación (GST-TOC).\n Por favor intente nuevamente.");
				} else if (ste.getCause().getClass().equals(SocketTimeoutException.class)) {
					e.setError("GST-TRW",
							"No se pudo establecer la comunicación (GST-TRW).\n Por favor intente nuevamente.");
				} else if (ste.getCause().getClass().equals(UnknownHostException.class)) {
					LogACGHelper.escribirLog(null,
							"No se pudo establecer la comunicación (GST-HNC): |" + ste.getMessage() + "|");
					e.setError("GST-TRW",
							"No se pudo establecer la comunicación (GST-HNC).\n Por favor intente nuevamente.");
				} else {
					LogACGHelper.escribirLog(null,
							"Informe Transacciones. Error ejecutando el WS de consulta: |" + ste.getMessage() + "|");
					e.setError("GST-OTR",
							"No se pudo establecer la comunicación (GST-OTR).\n Por favor intente nuevamente.");
				}
			} else {
				LogACGHelper.escribirLog(null,
						"Informe Transacciones. Error ejecutando el WS de consulta: |" + ste.getMessage() + "|");
				e.setError("GST-OTR",
						"No se pudo establecer la comunicación (GST-OTR).\n Por favor intente nuevamente.");
			}
			FacesContext.getCurrentInstance().validationFailed();
		} catch (Exception ex) {
			LogACGHelper.escribirLog(null,
					"Informe Transacciones. Excepcion ejecutando el WS de consulta: |" + ex.getMessage() + "|");
			e.setError("GST-OTR", "No se pudo establecer la comunicación (GST-OTR).\n Por favor intente nuevamente.");
		}
		return e;
	}

	public ReporteCargaPinesView() {
		super();
		resetearFecha();
		vigenciaMeses = "12";
	}

	private void resetearFecha() {
		Calendar c = Calendar.getInstance();
		c.add(Calendar.YEAR, 1); // le suma 1 anio
		c.set(Calendar.HOUR_OF_DAY, 0);
		c.set(Calendar.MINUTE, 0);
		c.set(Calendar.SECOND, 0);
		this.fechaVencimiento = c.getTime();
	}

	public void cargarPines(ActionEvent ae) {

		try {

			GregorianCalendar gcFechaVencimiento = new GregorianCalendar();
			gcFechaVencimiento.setTime(fechaVencimiento);
			XMLGregorianCalendar xmlGCFechaVencimiento = DatatypeFactory.newInstance()
					.newXMLGregorianCalendar(gcFechaVencimiento);

			RespString rs = GestionServiceHelper.getGestionService(CfgTimeout.REPORTE).cargarPines(
					this.getUsuario().getIdMayorista(), idProveedor, idProducto, xmlGCFechaVencimiento, vigenciaMeses,
					this.getUsuario().getIdCliente(), this.getUsuario().getIdUsuario(), pines);

			if (rs != null) {
				if (rs.getError().getValue().getHayError().getValue()) {
					FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
							rs.getError().getValue().getMsgError().getValue(), null));
				} else {
					// Respuesta OK

					String r[] = rs.getRespuesta().getValue().split(String.valueOf((char) 185), -1);

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
					pines = "";
				}
			} else {
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
						"La respuesta del metodo de Carga de Pines es null", null));
			}
		} catch (WebServiceException ste) {
			if (ste.getCause().getClass().equals(SocketTimeoutException.class)) {
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
						"No se pudo establecer la comunicación con GESTION.\n Por favor intente nuevamente.", null));
			} else {
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
						"Error realizando consulta contra GESTION: " + ste.getMessage(), null));
			}
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
					"Error ejecutando el WS de Carga de Pines: |" + e.getMessage() + "|", null));
		}

		return;
	}

	public void actualizarProductos() {
		try {
			this.productos = new ArrayList<SelectItem>();
			
			ArrayOfCabeceraProducto cp = GestionServiceHelper.getGestionService(CfgTimeout.REPORTE).buscarProductosPorProveedor(
					this.getUsuario().getIdMayorista(), idProveedor, "OFF", true);

			if (cp != null) {
				for (CabeceraProducto prod : cp.getCabeceraProducto()) {
					this.productos.add(new SelectItem(prod.getIdProducto().getValue(), prod.getDescProducto().getValue()));
				}
			}
		} catch (WebServiceException ste) {
			if (ste.getCause().getClass().equals(SocketTimeoutException.class)) {
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
						"No se pudo establecer la comunicación con GESTION.\n Por favor intente nuevamente.", null));
			} else {
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
						"Error realizando consulta contra GESTION: " + ste.getMessage(), null));
			}
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
					"Error ejecutando el WS de Carga de Pines: |" + e.getMessage() + "|", null));
		}

		return;
	}

	public Date getFechaVencimiento() {
		return fechaVencimiento;
	}

	public void setFechaVencimiento(Date fechaVencimiento) {
		this.fechaVencimiento = fechaVencimiento;
	}

	public Long getIdProveedor() {
		return idProveedor;
	}

	public void setIdProveedor(Long idProveedor) {
		this.idProveedor = idProveedor;
	}

	public Long getIdProducto() {
		return idProducto;
	}

	public void setIdProducto(Long idProducto) {
		this.idProducto = idProducto;
	}

	public List<SelectItem> getProductos() {
		return productos;
	}

	public void setProductos(List<SelectItem> productos) {
		this.productos = productos;
	}

	public List<SelectItem> getProveedores() {
		return proveedores;
	}

	public void setProveedores(List<SelectItem> proveedores) {
		this.proveedores = proveedores;
	}

	public String getPines() {
		return pines;
	}

	public void setPines(String pines) {
		this.pines = pines;
	}

	public String getVigenciaMeses() {
		return vigenciaMeses;
	}

	public void setVigenciaMeses(String vigenciaMeses) {
		this.vigenciaMeses = vigenciaMeses;
	}
}