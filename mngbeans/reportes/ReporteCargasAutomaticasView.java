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
import com.americacg.cargavirtual.gestion.model.ArrayOfSaldoProveedor;
import com.americacg.cargavirtual.gestion.model.CabeceraProducto;
import com.americacg.cargavirtual.gestion.model.CargasAutomaticas;
import com.americacg.cargavirtual.gestion.model.InformeCargasAutomaticas;
import com.americacg.cargavirtual.gestion.model.SaldoProveedor;
import com.americacg.cargavirtual.web.enums.CfgTimeout;
import com.americacg.cargavirtual.web.helpers.ACGFormatHelper;
import com.americacg.cargavirtual.web.helpers.GestionServiceHelper;
import com.americacg.cargavirtual.web.helpers.LogACGHelper;
import com.americacg.cargavirtual.web.model.Error;

@Named("reporteCargasAutomaticasView")
@Scope(org.springframework.web.context.WebApplicationContext.SCOPE_SESSION)
public class ReporteCargasAutomaticasView extends ReporteGeneral<CargasAutomaticas> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 9172567482388801747L;
	private Long idLote;
	private Long idClienteCarga;
	private Long idProducto;
	private Long idProveedor;
	private String tipoTRX;
	private Integer caracteristicaTelefono;
	private Integer numeroTelefono;
	private Float importe;
	private String estadoProceso;
	private String idEstado;

	private Float importeTotal = 0F;
	private Integer cantAprobadas = 0;

	private List<SelectItem> productos;
	private List<SelectItem> proveedores;

	@Override
	public Error resetearReporte() {
		Error e = super.resetearReporte();
		this.idLote = null;
		this.idProducto = null;
		this.idProveedor = null;
		this.tipoTRX = null;
		this.caracteristicaTelefono = null;
		this.numeroTelefono = null;
		this.importe = null;
		this.estadoProceso = null;
		this.idEstado = null;

		this.importeTotal = 0F;
		this.cantAprobadas = 0;
		this.cantRegistros = 0;

		try {

			this.productos = new ArrayList<SelectItem>();
			ArrayOfCabeceraProducto l = GestionServiceHelper.getGestionService(CfgTimeout.REPORTE)
					.mostrarCabeceraProductos(this.getUsuario().getIdMayorista(), null, true);
			for (CabeceraProducto cp : l.getCabeceraProducto()) {
				this.productos.add(new SelectItem(cp.getIdProducto().getValue(), cp.getDescProducto().getValue()));
			}
			this.proveedores = new ArrayList<SelectItem>();
			ArrayOfSaldoProveedor lp = GestionServiceHelper.getGestionService(CfgTimeout.REPORTE)
					.obtenerSaldoProveedores(this.getUsuario().getIdMayorista(), null, false, false);
			for (SaldoProveedor sp : lp.getSaldoProveedor()) {
				this.proveedores.add(new SelectItem(sp.getIdProveedor().getValue(), sp.getRazonSocial().getValue()));
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
					LogACGHelper.escribirLog(null, "Informe Cargas Automaticas. Error ejecutando el WS de consulta: |"
							+ ste.getMessage() + "|");
					e.setError("GST-OTR",
							"No se pudo establecer la comunicación (GST-OTR).\n Por favor intente nuevamente.");
				}
			} else {
				LogACGHelper.escribirLog(null,
						"Informe Cargas Automaticas. Error ejecutando el WS de consulta: |" + ste.getMessage() + "|");
				e.setError("GST-OTR",
						"No se pudo establecer la comunicación (GST-OTR).\n Por favor intente nuevamente.");
			}
			FacesContext.getCurrentInstance().validationFailed();
		} catch (Exception ex) {
			LogACGHelper.escribirLog(null,
					"Informe Cargas Automaticas. Excepcion ejecutando el WS de consulta: |" + ex.getMessage() + "|");
			e.setError("GST-OTR", "No se pudo establecer la comunicación (GST-OTR).\n Por favor intente nuevamente.");
		}
		return e;
	}

	public ReporteCargasAutomaticasView() {
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
			
			this.list = null;
			this.mostrarRegistros = true;

			// Fecha Desde
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(fechaHoraDesde);
			calendar.set(Calendar.SECOND, 0);
			calendar.set(Calendar.MILLISECOND, 0);
			fechaHoraDesde = calendar.getTime();

			// Asigno 59 segundos a la fechaHoraHasta
			// Fecha Hasta
			calendar = Calendar.getInstance();
			calendar.setTime(fechaHoraHasta);
			calendar.set(Calendar.SECOND, 59);
			calendar.set(Calendar.MILLISECOND, 999);
			fechaHoraHasta = calendar.getTime();

			// Inicializo Contador
			importeTotal = 0F;
			cantAprobadas = 0;

			// Limpio la tabla
			list = null;

			importeTotal = 0F;
			cantAprobadas = 0;

			// Integer cantRegistros = null;

			GregorianCalendar gcFechaHoraDesde = new GregorianCalendar();
			gcFechaHoraDesde.setTime(fechaHoraDesde);
			XMLGregorianCalendar xmlGCFechaHoraDesde = DatatypeFactory.newInstance()
					.newXMLGregorianCalendar(gcFechaHoraDesde);

			GregorianCalendar gcFechaHoraHasta = new GregorianCalendar();
			gcFechaHoraHasta.setTime(fechaHoraHasta);
			XMLGregorianCalendar xmlGCFechaHoraHasta = DatatypeFactory.newInstance()
					.newXMLGregorianCalendar(gcFechaHoraHasta);

			InformeCargasAutomaticas ica = GestionServiceHelper.getGestionService(CfgTimeout.REPORTE)
					.informeCargasAutomaticas(this.getUsuario().getIdMayorista(), idLote, idClienteCarga, idProducto,
							idProveedor, tipoTRX, caracteristicaTelefono, numeroTelefono, importe, estadoProceso,
							idEstado, xmlGCFechaHoraDesde, xmlGCFechaHoraHasta);

			if (ica == null) {
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
						"La respuesta del informe de CargasAutomaticas es null", null));
			} else if (ica.getError().getValue().getHayError().getValue()) {
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
						ica.getError().getValue().getMsgError().getValue(), null));
			} else {

				List<CargasAutomaticas> listaux = ica.getLca().getValue().getCargasAutomaticas();
				if (listaux.isEmpty()) {
					FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
							"La consulta devolvio cero registos o supero el limite de registros permitidos. Por favor reintente",
							null));
				} else {
					list = listaux;
					cantRegistros = list.size();
					for (CargasAutomaticas ca : list) {
						// Sumo solo los que el estado = OK
						if ("M0000".equals(ca.getIdEstado().getValue())) {
							importeTotal += ca.getImporte().getValue();
							cantAprobadas++;
						}
					}

					Integer i = 0;
					String csvSepCamp = this.getUsuario().getCsvSeparadorCampo();
					String csvSepDec = this.getUsuario().getCsvSeparadorDecimales();
					StringBuilder sb = new StringBuilder();

					// CREO HEADER DE EXPORTACION SI ES NECESARIO PARA EXPORTAR A CSV
					if (this.exportToExcel) {
						sb.append((char) 34).append("Informe de Cargas Automaticas ").append(this.cantRegistros).append(" Registros), Aprobadas: ").append(this.cantAprobadas).append((char) 34).append(csvSepCamp);
						sb.append((char) 13).append((char) 10);
								 
						// Header
						sb.append((char) 34).append("Id Lote").append((char) 34).append(csvSepCamp);
						sb.append((char) 34).append("Fecha Alta").append((char) 34).append(csvSepCamp);
						sb.append((char) 34).append("Fecha de Processamiento").append((char) 34).append(csvSepCamp);
						sb.append((char) 34).append("ID Cliente").append((char) 34).append(csvSepCamp);
						sb.append((char) 34).append("Cliente").append((char) 34).append(csvSepCamp);
						sb.append((char) 34).append("ID Usuario").append((char) 34).append(csvSepCamp);
						sb.append((char) 34).append("Usuario").append((char) 34).append(csvSepCamp);
						sb.append((char) 34).append("ID Proveedor").append((char) 34).append(csvSepCamp);
						sb.append((char) 34).append("Proveedor").append((char) 34).append(csvSepCamp);
						sb.append((char) 34).append("ID Producto").append((char) 34).append(csvSepCamp);
						sb.append((char) 34).append("Producto").append((char) 34).append(csvSepCamp);
						sb.append((char) 34).append("Tipo TRX").append((char) 34).append(csvSepCamp);
						sb.append((char) 34).append("Telefono").append((char) 34).append(csvSepCamp);
						sb.append((char) 34).append("Importe").append((char) 34).append(csvSepCamp);
						sb.append((char) 34).append("ID Transaccion").append((char) 34).append(csvSepCamp);
						sb.append((char) 34).append("Estado Proceso").append((char) 34).append(csvSepCamp);
						sb.append((char) 34).append("ID Estado").append((char) 34).append(csvSepCamp);
						sb.append((char) 34).append("Estado").append((char) 34).append(csvSepCamp);
						sb.append((char) 34).append("ID Cliente Alta").append((char) 34).append(csvSepCamp);						
						sb.append((char) 34).append("Cliente Alta").append((char) 34).append(csvSepCamp);
						sb.append((char) 34).append("ID Usuario Alta").append((char) 34).append(csvSepCamp);						
						sb.append((char) 34).append("Usuario Alta").append((char) 34).append(csvSepCamp);
						sb.append((char) 34).append("Comentario 1").append((char) 34).append(csvSepCamp);
						sb.append((char) 34).append("Comentario 2").append((char) 34).append(csvSepCamp);
						sb.append((char) 34).append("Comentario 3").append((char) 34).append(csvSepCamp);

						sb.append((char) 13).append((char) 10);

					}

					// DEFINO FORMATO DE FECHA PARA MOSTRAR EN EL REPORTE QUE SE EXPORTA
					SimpleDateFormat ff = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

					// RECORRO LA LISTA QUE HACER LAS SUMATORIAS Y PARA GENERAR EL REPORTE A
					// EXPORTAR EN CASO DE SER NECESARIO
					for (CargasAutomaticas ca : list) {

						// CREO ARCHIVO DE EXPORTACION SI ES NECESARIO PARA EXPORTAR A CSV
						if (this.exportToExcel) {

							if (i == 0) {
								mostrarArchivoCSV = true;
							}

							sb.append((char) 34).append(ca.getIdLote().getValue()).append((char) 34).append(csvSepCamp);
							sb.append((char) 34)
									.append(ff.format(ca.getFechaAlta().toGregorianCalendar().getTime()).toString())
									.append((char) 34).append(csvSepCamp);
							sb.append((char) 34).append(
									ff.format(ca.getFechaProcesamiento().toGregorianCalendar().getTime()).toString())
									.append((char) 34).append(csvSepCamp);
							sb.append((char) 34).append(ca.getIdClienteCarga().getValue()).append((char) 34)
									.append(csvSepCamp);
							sb.append((char) 34).append(ca.getRazonSocialClienteCarga().getValue()).append((char) 34)
									.append(csvSepCamp);
							sb.append((char) 34).append(ca.getIdUsuarioCarga().getValue()).append((char) 34)
									.append(csvSepCamp);
							sb.append((char) 34).append(ca.getUsuarioCarga().getValue()).append((char) 34)
									.append(csvSepCamp);
							sb.append((char) 34);
							if(ca.getIdProveedor() != null && ca.getIdProveedor().getValue() != null) {
								sb.append(ca.getIdProveedor().getValue());
							}
							sb.append((char) 34).append(csvSepCamp);
							sb.append((char) 34);
							if(ca.getRazonSocialProveedor() != null && ca.getRazonSocialProveedor().getValue() != null) {
								sb.append(ca.getRazonSocialProveedor().getValue());
							}
							sb.append((char) 34).append(csvSepCamp);							
							sb.append((char) 34).append(ca.getIdProducto().getValue()).append((char) 34)
									.append(csvSepCamp);
							sb.append((char) 34).append(ca.getDescProducto().getValue()).append((char) 34)
									.append(csvSepCamp);
							sb.append((char) 34).append(ca.getTipoTRX().getValue()).append((char) 34)
									.append(csvSepCamp);
							sb.append((char) 34).append(ca.getCaracteristicaTelefono().getValue()).append(" ")
									.append(ca.getNumeroTelefono().getValue()).append((char) 34).append(csvSepCamp);
							sb.append((char) 34).append(ACGFormatHelper.format(ca.getImporte().getValue(), csvSepDec))
									.append((char) 34).append(csvSepCamp);
							sb.append((char) 34).append(ca.getIdTransaccion().getValue()).append((char) 34)
									.append(csvSepCamp);
							sb.append((char) 34).append(ca.getEstadoProceso().getValue()).append((char) 34)
									.append(csvSepCamp);
							sb.append((char) 34).append(ca.getIdEstado().getValue()).append((char) 34)
									.append(csvSepCamp);
							sb.append((char) 34).append(ca.getDescEstado().getValue()).append((char) 34)
									.append(csvSepCamp);
							sb.append((char) 34).append(ca.getIdClienteAlta().getValue()).append((char) 34)
							.append(csvSepCamp);
							sb.append((char) 34).append(ca.getRazonSocialClienteAlta().getValue()).append((char) 34)
									.append(csvSepCamp);
							sb.append((char) 34).append(ca.getIdUsuarioAlta().getValue()).append((char) 34)
									.append(csvSepCamp);
							sb.append((char) 34).append(ca.getUsuarioAlta().getValue()).append((char) 34)
									.append(csvSepCamp);
							sb.append((char) 34);
							if(ca.getComentario1() != null && ca.getComentario1().getValue() != null) {
								sb.append(ca.getComentario1().getValue());
							}
							sb.append((char) 34).append(csvSepCamp);
							sb.append((char) 34);
							if(ca.getComentario2() != null && ca.getComentario2().getValue() != null) {
								sb.append(ca.getComentario2().getValue());
							}
							sb.append((char) 34).append(csvSepCamp);
							sb.append((char) 34);
							if(ca.getComentario3() != null && ca.getComentario3().getValue() != null) {
								sb.append(ca.getComentario3().getValue());
							}
							sb.append((char) 34).append(csvSepCamp);

							sb.append((char) 13).append((char) 10);

						}

						i++;

					}

					// CREO HEADER DE EXPORTACION SI ES NECESARIO PARA EXPORTAR A CSV
					if (this.exportToExcel) {
						// Header
						sb.append((char) 34).append("Total").append((char) 34).append(csvSepCamp);
						sb.append((char) 34).append("").append((char) 34).append(csvSepCamp);
						sb.append((char) 34).append("").append((char) 34).append(csvSepCamp);
						sb.append((char) 34).append("").append((char) 34).append(csvSepCamp);
						sb.append((char) 34).append("").append((char) 34).append(csvSepCamp);
						sb.append((char) 34).append("").append((char) 34).append(csvSepCamp);
						sb.append((char) 34).append("").append((char) 34).append(csvSepCamp);
						sb.append((char) 34).append("").append((char) 34).append(csvSepCamp);
						sb.append((char) 34).append("").append((char) 34).append(csvSepCamp);
						sb.append((char) 34).append("").append((char) 34).append(csvSepCamp);
						sb.append((char) 34).append("").append((char) 34).append(csvSepCamp);
						sb.append((char) 34).append("").append((char) 34).append(csvSepCamp);
						sb.append((char) 34).append("").append((char) 34).append(csvSepCamp);
						sb.append((char) 34).append(ACGFormatHelper.format(this.importeTotal, csvSepDec))
								.append((char) 34).append(csvSepCamp);
						sb.append((char) 34).append("").append((char) 34).append(csvSepCamp);
						sb.append((char) 34).append("").append((char) 34).append(csvSepCamp);
						sb.append((char) 34).append("").append((char) 34).append(csvSepCamp);
						sb.append((char) 34).append("").append((char) 34).append(csvSepCamp);
						sb.append((char) 34).append("").append((char) 34).append(csvSepCamp);
						sb.append((char) 34).append("").append((char) 34).append(csvSepCamp);
						sb.append((char) 34).append("").append((char) 34).append(csvSepCamp);
						sb.append((char) 34).append("").append((char) 34).append(csvSepCamp);
						sb.append((char) 34).append("").append((char) 34).append(csvSepCamp);
						sb.append((char) 34).append("").append((char) 34).append(csvSepCamp);
						sb.append((char) 34).append("").append((char) 34).append(csvSepCamp);
						
						sb.append((char) 13).append((char) 10);

					}

					// GENERO ARCHIVO DE EXPORTACION SI ES NECESARIO PARA EXPORTAR A CSV
					if (this.exportToExcel) {

						FacesContext fc = FacesContext.getCurrentInstance();
						ExternalContext ec = fc.getExternalContext();

						ec.responseReset();
						ec.setResponseContentType("text/plain");
						ec.setResponseContentLength(sb.toString().length());

						SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd_HHmmss");
						ec.setResponseHeader("Content-Disposition", "attachment; filename=\"" + sdf.format(new Date())
								+ "_(" + this.getUsuario().getIdMayorista() + ")_" + "InformeCargasAutomaticas.csv" + "\"");

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
					"Error ejecutando el WS de consulta de CargasAutomaticas: |" + e.getMessage() + "|", null));
		}
		return;
	}

	public Long getIdProducto() {
		return idProducto;
	}

	public void setIdProducto(Long idProducto) {
		this.idProducto = idProducto;
	}

	public Float getImporteTotal() {
		return importeTotal;
	}

	public void setImporteTotal(Float importeTotal) {
		this.importeTotal = importeTotal;
	}

	public String getTipoTRX() {
		return tipoTRX;
	}

	public void setTipoTRX(String tipoTRX) {
		this.tipoTRX = tipoTRX;
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

	public Integer getCantAprobadas() {
		return cantAprobadas;
	}

	public void setCantAprobadas(Integer cantAprobadas) {
		this.cantAprobadas = cantAprobadas;
	}

	public Long getIdLote() {
		return idLote;
	}

	public void setIdLote(Long idLote) {
		this.idLote = idLote;
	}

	public Long getIdClienteCarga() {
		return idClienteCarga;
	}

	public void setIdClienteCarga(Long idClienteCarga) {
		this.idClienteCarga = idClienteCarga;
	}

	public Integer getCaracteristicaTelefono() {
		return caracteristicaTelefono;
	}

	public void setCaracteristicaTelefono(Integer caracteristicaTelefono) {
		this.caracteristicaTelefono = caracteristicaTelefono;
	}

	public Integer getNumeroTelefono() {
		return numeroTelefono;
	}

	public void setNumeroTelefono(Integer numeroTelefono) {
		this.numeroTelefono = numeroTelefono;
	}

	public Float getImporte() {
		return importe;
	}

	public void setImporte(Float importe) {
		this.importe = importe;
	}

	public String getEstadoProceso() {
		return estadoProceso;
	}

	public void setEstadoProceso(String estadoProceso) {
		this.estadoProceso = estadoProceso;
	}

	public String getIdEstado() {
		return idEstado;
	}

	public void setIdEstado(String idEstado) {
		this.idEstado = idEstado;
	}

}
