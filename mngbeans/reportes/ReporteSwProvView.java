package com.americacg.cargavirtual.web.mngbeans.reportes;

import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.inject.Named;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import javax.xml.ws.WebServiceException;
import org.primefaces.PrimeFaces;
import org.springframework.context.annotation.Scope;
import com.americacg.cargavirtual.gestion.model.ArrayOfCabeceraProducto;
import com.americacg.cargavirtual.gestion.model.ArrayOfInformeCambioPrioridades;
import com.americacg.cargavirtual.gestion.model.ArrayOfSaldoProveedor;
import com.americacg.cargavirtual.gestion.model.CabeceraProducto;
import com.americacg.cargavirtual.gestion.model.InformeCambioPrioridades;
import com.americacg.cargavirtual.gestion.model.SaldoProveedor;
import com.americacg.cargavirtual.web.enums.CfgTimeout;
import com.americacg.cargavirtual.web.helpers.GestionServiceHelper;
import com.americacg.cargavirtual.web.helpers.LogACGHelper;
import com.americacg.cargavirtual.web.model.Error;

@Named("reporteSwProvView")
@Scope(org.springframework.web.context.WebApplicationContext.SCOPE_SESSION)
public class ReporteSwProvView extends ReporteGeneral<InformeCambioPrioridades> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6189928010784534109L;

	private Long idClienteSeleccionado;

	private Integer factorAplicado;

	private Long idProducto;
	private Long idProveedor;

	private List<SelectItem> productos;
	private List<SelectItem> proveedores;

	@Override
	public Error resetearReporte() {
		Error e = super.resetearReporte();
		this.factorAplicado = null;
		this.idProducto = null;
		this.idProveedor = null;
		this.list = null;

		try {
			this.proveedores = new ArrayList<SelectItem>();
			ArrayOfSaldoProveedor lp = GestionServiceHelper.getGestionService(CfgTimeout.REPORTE)
					.obtenerSaldoProveedores(this.getUsuario().getIdMayorista(), null, false, false);
			for (SaldoProveedor sp : lp.getSaldoProveedor()) {
				this.proveedores.add(new SelectItem(sp.getIdProveedor().getValue(), sp.getRazonSocial().getValue()));
			}
			this.productos = new ArrayList<SelectItem>();
			ArrayOfCabeceraProducto l = GestionServiceHelper.getGestionService(CfgTimeout.REPORTE)
					.mostrarCabeceraProductos(this.getUsuario().getIdMayorista(), null, false);
			for (CabeceraProducto cp : l.getCabeceraProducto()) {
				this.productos.add(new SelectItem(cp.getIdProducto().getValue(), cp.getDescProducto().getValue()));
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
					LogACGHelper.escribirLog(null, "No se pudo establecer la comunicación (GST-HNC): |" + ste.getMessage() + "|");
					e.setError("GST-TRW",
							"No se pudo establecer la comunicación (GST-HNC).\n Por favor intente nuevamente.");					
				} else {
					LogACGHelper.escribirLog(null, "Informe Switch Proveedores. Error ejecutando el WS de consulta: |"
							+ ste.getMessage() + "|");
					e.setError("GST-OTR",
							"No se pudo establecer la comunicación (GST-OTR).\n Por favor intente nuevamente.");
				}
			} else {
				LogACGHelper.escribirLog(null,
						"Informe Switch Proveedores. Error ejecutando el WS de consulta: |" + ste.getMessage() + "|");
				e.setError("GST-OTR",
						"No se pudo establecer la comunicación (GST-OTR).\n Por favor intente nuevamente.");
			}
			FacesContext.getCurrentInstance().validationFailed();
		} catch (Exception ex) {
			LogACGHelper.escribirLog(null,
					"Informe Switch Proveedores. Excepcion ejecutando el WS de consulta: |" + ex.getMessage() + "|");
			e.setError("GST-OTR", "No se pudo establecer la comunicación (GST-OTR).\n Por favor intente nuevamente.");
		}
		return e;
	}

	public ReporteSwProvView() {
		super();
	}

	public void realizarInforme() {
		this.exportToExcel = false;
		informe();
	}

	public void exportarInforme() {
		this.exportToExcel = true;
		informe();
	}

	public void informe() {

		try {
			this.cantRegistros = 0;
			this.list = null;
			this.mostrarRegistros = true;
			
			if (!validaFechas()) {
				return;
			}

			// Fecha Desde
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(fechaHoraDesde);
			calendar.set(Calendar.HOUR_OF_DAY, 00);
			calendar.set(Calendar.MINUTE, 00);
			calendar.set(Calendar.SECOND, 0);
			calendar.set(Calendar.MILLISECOND, 0);
			fechaHoraDesde = calendar.getTime();

			// Fecha Hasta
			calendar = Calendar.getInstance();
			calendar.setTime(fechaHoraHasta);
			calendar.set(Calendar.HOUR_OF_DAY, 23);
			calendar.set(Calendar.MINUTE, 59);
			calendar.set(Calendar.SECOND, 59);
			calendar.set(Calendar.MILLISECOND, 999);
			fechaHoraHasta = calendar.getTime();

			GregorianCalendar gcFechaHoraDesde = new GregorianCalendar();
			gcFechaHoraDesde.setTime(fechaHoraDesde);
			XMLGregorianCalendar xmlGCFechaHoraDesde = DatatypeFactory.newInstance()
					.newXMLGregorianCalendar(gcFechaHoraDesde);

			GregorianCalendar gcFechaHoraHasta = new GregorianCalendar();
			gcFechaHoraHasta.setTime(fechaHoraHasta);
			XMLGregorianCalendar xmlGCFechaHoraHasta = DatatypeFactory.newInstance()
					.newXMLGregorianCalendar(gcFechaHoraHasta);

			ArrayOfInformeCambioPrioridades i = GestionServiceHelper.getGestionService(CfgTimeout.REPORTE)
					.informeCambioPrioridades(this.getUsuario().getIdMayorista(), this.getUsuario().getIdCliente(),
							xmlGCFechaHoraDesde, xmlGCFechaHoraHasta, idProducto, idProveedor, factorAplicado);

			if (i == null || i.getInformeCambioPrioridades().isEmpty()) {
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
						"No se ha encontrado ningun registro con ese criterio.", null));
			}else {
				this.list = i.getInformeCambioPrioridades();
				if(this.list != null) {
					this.cantRegistros = this.list.size();
				}
			
				// GENERO Y LIMPIO LAS VARIABLES PARA LA EXPORTACION
				// TODO implementar exportToCSV
				// this.exportToCSV.setExportText("");
				// this.exportToCSV.setFileName("");
				mostrarArchivoCSV = false;
	
				String csvSepCamp = this.getUsuario().getCsvSeparadorCampo();
				String csvSepDec = this.getUsuario().getCsvSeparadorDecimales();
	
				StringBuilder sb = new StringBuilder();
				// DEFINO FORMATO DE FECHA PARA MOSTRAR EN EL REPORTE QUE SE EXPORTA
				SimpleDateFormat ff = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");


				//CREO HEADER DE EXPORTACION SI ES NECESARIO PARA EXPORTAR A CSV
				if (this.exportToExcel) {
					// Header
					sb.append((char) 34).append("Informe de Switcheo de Proveedores").append((char) 34).append(csvSepCamp);
					sb.append((char) 13).append((char) 10);
					
					sb.append((char) 34).append("Id").append((char) 34).append(csvSepCamp);
					sb.append((char) 34).append("Fecha").append((char) 34).append(csvSepCamp);
					sb.append((char) 34).append("ID Proveedor").append((char) 34).append(csvSepCamp);
					sb.append((char) 34).append("Proveedor").append((char) 34).append(csvSepCamp);
					sb.append((char) 34).append("ID Conector").append((char) 34).append(csvSepCamp);
					sb.append((char) 34).append("Conector").append((char) 34).append(csvSepCamp);
					sb.append((char) 34).append("ID Producto").append((char) 34).append(csvSepCamp);
					sb.append((char) 34).append("Producto").append((char) 34).append(csvSepCamp);
					sb.append((char) 34).append("Prioridad Anterior").append((char) 34).append(csvSepCamp);
					sb.append((char) 34).append("Factor Aplicado").append((char) 34).append(csvSepCamp);
					sb.append((char) 34).append("ID Estado").append((char) 34).append(csvSepCamp);
					sb.append((char) 34).append("Desc. Estado").append((char) 34).append(csvSepCamp);
					sb.append((char) 34).append("Id Transaccion").append((char) 34).append(csvSepCamp);

					sb.append((char) 13).append((char) 10);

				}
			
				// RECORRO LA LISTA QUE HACER LAS SUMATORIAS Y PARA GENERAR EL REPORTE A
				// EXPORTAR EN CASO DE SER NECESARIO
				for (InformeCambioPrioridades it : list) {


					// CREO ARCHIVO DE EXPORTACION SI ES NECESARIO PARA EXPORTAR A CSV
					if (this.exportToExcel) {

						if (!mostrarArchivoCSV) {
							mostrarArchivoCSV = true;
						}

						sb.append((char) 34).append(it.getIdCambioPrioridad().getValue()).append((char) 34)
								.append(csvSepCamp);
						sb.append((char) 34).append(
								ff.format(it.getFecha().toGregorianCalendar().getTime()).toString())
								.append((char) 34).append(csvSepCamp);
						sb.append((char) 34).append(it.getIdProveedor().getValue()).append((char) 34)
								.append(csvSepCamp);
						sb.append((char) 34).append(it.getRazonSocial().getValue()).append((char) 34)
								.append(csvSepCamp);
						sb.append((char) 34).append(it.getIdConector().getValue()).append((char) 34)
								.append(csvSepCamp);
						sb.append((char) 34).append(it.getDescConector().getValue()).append((char) 34)
								.append(csvSepCamp);

						sb.append((char) 34).append(it.getIdProducto().getValue()).append((char) 34)
								.append(csvSepCamp);
						sb.append((char) 34).append(it.getDescProducto().getValue()).append((char) 34)
						.append(csvSepCamp);
						sb.append((char) 34).append(it.getPrioridadAnterior().getValue()).append((char) 34)
						.append(csvSepCamp);
						sb.append((char) 34).append(it.getFactorAplicado().getValue()).append((char) 34)
						.append(csvSepCamp);						
						sb.append((char) 34).append(it.getIdEstadoOperador().getValue()).append((char) 34)
						.append(csvSepCamp);				
						sb.append((char) 34).append(it.getDescEstadoOperador().getValue()).append((char) 34)
						.append(csvSepCamp);					
						sb.append((char) 34).append(it.getIdTransaccion().getValue()).append((char) 34)
						.append(csvSepCamp);						

						sb.append((char) 13).append((char) 10);

					}

				}

				// GENERO ARCHIVO DE EXPORTACION SI ES NECESARIO PARA EXPORTAR A CSV
				if (this.exportToExcel) {

					FacesContext fc = FacesContext.getCurrentInstance();
					ExternalContext ec = fc.getExternalContext();

					ec.responseReset();
					ec.setResponseContentType("text/plain");
					ec.setResponseContentLength(sb.toString().length());

					SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd_HHmmss");
					ec.setResponseHeader("Content-Disposition",
							"attachment; filename=\"" + sdf.format(new Date()) + "_("
									+ this.getUsuario().getIdMayorista() + ")_" + "InformeSP.csv"
									+ "\"");

					OutputStream os = ec.getResponseOutputStream();
					OutputStreamWriter osw = new OutputStreamWriter(os);
					PrintWriter writer = new PrintWriter(osw);
					writer.write(sb.toString());
					writer.flush();
					writer.close();
					sb.setLength(0);

					fc.responseComplete();

				}

				PrimeFaces.current().executeScript("PF('panelFiltroWG').toggle();");
			}
			
		} catch (WebServiceException ste) {
			if (ste != null && ste.getCause() != null && ste.getCause().getClass() != null) {
				if (ste.getCause().getClass().equals(ConnectException.class)) {
					FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
							"No se pudo establecer la comunicación (GST-TOC).\n Por favor intente nuevamente.", null));
				} else if (ste.getCause().getClass().equals(SocketTimeoutException.class)) {
					FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
							"No se pudo establecer la comunicación (GST-TRW).\n Por favor intente nuevamente.", null));
				} else if (ste.getCause().getClass().equals(UnknownHostException.class)) {
					LogACGHelper.escribirLog(null, "No se pudo establecer la comunicación (GST-HNC): |" + ste.getMessage() + "|");
					FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
							"No se pudo establecer la comunicación (GST-HNC).\n Por favor intente nuevamente.", null));					
				} else {
					LogACGHelper.escribirLog(null,
							"Informe de Switcheo de Proveedores. Error ejecutando el WS de consulta: |"
									+ ste.getMessage() + "|");
					FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
							"Error ejecutando el WS de consulta: |" + ste.getMessage() + "|", null));
				}
			} else {
				LogACGHelper.escribirLog(null,
						"Informe de Switcheo de Proveedores. Error ejecutando el WS de consulta: |" + ste.getMessage()
								+ "|");
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
						"Error ejecutando el WS de consulta: |" + ste.getMessage() + "|", null));
			}
			this.list = null;
			FacesContext.getCurrentInstance().validationFailed();
		} catch (Exception e) {
			LogACGHelper.escribirLog(null,
					"Informe de Switcheo de Proveedores. Excepcion ejecutando el WS de consulta: |" + e.getMessage()
							+ "|");
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
					"Error ejecutando el WS de Informe de Switcheo de Proveedores: |" + e.getMessage() + "|", null));
			FacesContext.getCurrentInstance().validationFailed();
			this.list = null;
		}
		return;
	}



	public Long getIdClienteSeleccionado() {
		return idClienteSeleccionado;
	}

	public void setIdClienteSeleccionado(Long idClienteSeleccionado) {
		this.idClienteSeleccionado = idClienteSeleccionado;
	}

	public List<SelectItem> getProductos() {
		return productos;
	}

	public void setProductos(List<SelectItem> productos) {
		this.productos = productos;
	}

	public Long getIdProveedor() {
		return idProveedor;
	}

	public void setIdProveedor(Long idProveedor) {
		this.idProveedor = idProveedor;
	}

	public List<SelectItem> getProveedores() {
		return proveedores;
	}

	public void setProveedores(List<SelectItem> proveedores) {
		this.proveedores = proveedores;
	}

	public Integer getFactorAplicado() {
		return factorAplicado;
	}

	public void setFactorAplicado(Integer factorAplicado) {
		this.factorAplicado = factorAplicado;
	}

	public Long getIdProducto() {
		return idProducto;
	}

	public void setIdProducto(Long idProducto) {
		this.idProducto = idProducto;
	}
}
