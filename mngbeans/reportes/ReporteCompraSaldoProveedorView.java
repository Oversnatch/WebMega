package com.americacg.cargavirtual.web.mngbeans.reportes;

import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
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
import com.americacg.cargavirtual.gestion.model.ArrayOfInformeCompraSaldoProv;
import com.americacg.cargavirtual.gestion.model.ArrayOfSaldoProveedor;
import com.americacg.cargavirtual.gestion.model.InformeCompraSaldoProv;
import com.americacg.cargavirtual.gestion.model.SaldoProveedor;
import com.americacg.cargavirtual.web.enums.CfgTimeout;
import com.americacg.cargavirtual.web.helpers.ACGFormatHelper;
import com.americacg.cargavirtual.web.helpers.GestionServiceHelper;
import com.americacg.cargavirtual.web.helpers.LogACGHelper;
import com.americacg.cargavirtual.web.model.Error;

@Named("reporteCompraSaldoProveedorView")
@Scope(org.springframework.web.context.WebApplicationContext.SCOPE_SESSION)
public class ReporteCompraSaldoProveedorView extends ReporteGeneral<InformeCompraSaldoProv> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8757685888045919465L;
	private List<SelectItem> proveedores;
	private Long idProveedor;
	private Long idCompraSaldoProv;
	private Float sumaInc = 0F;
	private String nroFactura;

	@Override
	public Error resetearReporte() {
		Error e = super.resetearReporte();
		this.idProveedor = null;
		this.idCompraSaldoProv = null;
		this.sumaInc = 0F;
		this.nroFactura = "";
		try {
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
					LogACGHelper.escribirLog(null, "No se pudo establecer la comunicación (GST-HNC): |" + ste.getMessage() + "|");
					e.setError("GST-TRW",
							"No se pudo establecer la comunicación (GST-HNC).\n Por favor intente nuevamente.");					
				} else {
					LogACGHelper.escribirLog(null,
							"Informe Compra Saldo Proveedor. Error ejecutando el WS de consulta: |" + ste.getMessage()
									+ "|");
					e.setError("GST-OTR",
							"No se pudo establecer la comunicación (GST-OTR).\n Por favor intente nuevamente.");
				}
			} else {
				LogACGHelper.escribirLog(null, "Informe Compra Saldo Proveedor. Error ejecutando el WS de consulta: |"
						+ ste.getMessage() + "|");
				e.setError("GST-OTR",
						"No se pudo establecer la comunicación (GST-OTR).\n Por favor intente nuevamente.");
			}
			FacesContext.getCurrentInstance().validationFailed();
		} catch (Exception ex) {
			LogACGHelper.escribirLog(null,
					"Informe Compra Saldo Proveedor. Excepcion ejecutando el WS de consulta: |" + ex.getMessage() + "|");
			e.setError("GST-OTR", "No se pudo establecer la comunicación (GST-OTR).\n Por favor intente nuevamente.");
		}
		return e;
	}

	public ReporteCompraSaldoProveedorView() {
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
			sumaInc = 0F;
			if (!validaFechas()) {
				return;
			}

			GregorianCalendar gcFechaHoraDesde = new GregorianCalendar();
			gcFechaHoraDesde.setTime(fechaHoraDesde);
			XMLGregorianCalendar xmlGCFechaHoraDesde = DatatypeFactory.newInstance()
					.newXMLGregorianCalendar(gcFechaHoraDesde);

			GregorianCalendar gcFechaHoraHasta = new GregorianCalendar();
			gcFechaHoraHasta.setTime(fechaHoraHasta);
			XMLGregorianCalendar xmlGCFechaHoraHasta = DatatypeFactory.newInstance()
					.newXMLGregorianCalendar(gcFechaHoraHasta);

			ArrayOfInformeCompraSaldoProv listaux = GestionServiceHelper.getGestionService(CfgTimeout.REPORTE)
					.informeCompraSaldoProv(this.getUsuario().getIdMayorista(), idProveedor, xmlGCFechaHoraDesde,
							xmlGCFechaHoraHasta, idCompraSaldoProv, nroFactura);

			
			// GENERO Y LIMPIO LAS VARIABLES PARA LA EXPORTACION
			// TODO implementar exportToCSV
			// this.exportToCSV.setExportText("");
			// this.exportToCSV.setFileName("");
			mostrarArchivoCSV = false;

			String csvSepCamp = this.getUsuario().getCsvSeparadorCampo();
			String csvSepDec = this.getUsuario().getCsvSeparadorDecimales();

			StringBuilder sb = new StringBuilder();

			if (listaux == null) {
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
						"La respuesta del informe de transacciones es null", null));
			/*} else if (listaux.getError().getValue().getHayError().getValue()) {
				// TODO Ver porque no entra en esta condicion. No reconoce el Error
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
						listaux.getError().getValue().getMsgError().getValue(), null)); */
			} else if (listaux.getInformeCompraSaldoProv() == null) {
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
						"No existe información para la consulta realizada.", null));
			} else {
				if (listaux.getInformeCompraSaldoProv().isEmpty()) {
					FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
							"No existe información para la consulta realizada.", null));
				} else {
					list = listaux.getInformeCompraSaldoProv();
					for (InformeCompraSaldoProv inf : list) {
						sumaInc += inf.getIncremento().getValue();
					}
					cantRegistros = list.size();
					Integer i = 0;

					// CREO HEADER DE EXPORTACION SI ES NECESARIO PARA EXPORTAR A CSV
					if (this.exportToExcel) {
						// Header
						sb.append((char) 34).append("Informe Compra Saldo a Proveedores").append((char) 34).append(csvSepCamp);
						sb.append((char) 13).append((char) 10);
						
						sb.append((char) 34).append("Id").append((char) 34).append(csvSepCamp);
						sb.append((char) 34).append("Fecha").append((char) 34).append(csvSepCamp);
						sb.append((char) 34).append("ID Proveedor").append((char) 34).append(csvSepCamp);
						sb.append((char) 34).append("Proveedor").append((char) 34).append(csvSepCamp);
						sb.append((char) 34).append("Id Usuario").append((char) 34).append(csvSepCamp);
						sb.append((char) 34).append("Usuario").append((char) 34).append(csvSepCamp);
						sb.append((char) 34).append("ID Tipo Mov.").append((char) 34).append(csvSepCamp);
						sb.append((char) 34).append("Tipo Mov.").append((char) 34).append(csvSepCamp);
						sb.append((char) 34).append("Saldo Inicial").append((char) 34).append(csvSepCamp);
						sb.append((char) 34).append("Incremento").append((char) 34).append(csvSepCamp);
						sb.append((char) 34).append("Saldo Final").append((char) 34).append(csvSepCamp);
						sb.append((char) 34).append("Fact.").append((char) 34).append(csvSepCamp);
						sb.append((char) 34).append("Coment.").append((char) 34).append(csvSepCamp);

						sb.append((char) 13).append((char) 10);

					}

					// DEFINO FORMATO DE FECHA PARA MOSTRAR EN EL REPORTE QUE SE EXPORTA
					SimpleDateFormat ff = new SimpleDateFormat("yyyy/MM/dd HH:mm");

					// RECORRO LA LISTA QUE HACER LAS SUMATORIAS Y PARA GENERAR EL REPORTE A
					// EXPORTAR EN CASO DE SER NECESARIO
					for (InformeCompraSaldoProv it : list) {


						// CREO ARCHIVO DE EXPORTACION SI ES NECESARIO PARA EXPORTAR A CSV
						if (this.exportToExcel) {

							if (i == 0) {
								mostrarArchivoCSV = true;
							}

							sb.append((char) 34).append(it.getIdCompraSaldoProv().getValue()).append((char) 34)
									.append(csvSepCamp);
							sb.append((char) 34).append(
									ff.format(it.getFecha().toGregorianCalendar().getTime()).toString())
									.append((char) 34).append(csvSepCamp);
							sb.append((char) 34).append(it.getIdProveedor().getValue()).append((char) 34)
									.append(csvSepCamp);
							sb.append((char) 34).append(it.getRazonSocialProveedor().getValue()).append((char) 34)
									.append(csvSepCamp);
							sb.append((char) 34).append(it.getIdUsuario().getValue()).append((char) 34)
									.append(csvSepCamp);
							sb.append((char) 34).append(it.getUsuario().getValue()).append((char) 34)
									.append(csvSepCamp);

							sb.append((char) 34).append(it.getIdTipoMovimiento().getValue()).append((char) 34)
									.append(csvSepCamp);
							sb.append((char) 34).append(it.getDescTipoMovimiento().getValue()).append((char) 34)
							.append(csvSepCamp);
							
							sb.append((char) 34).append("$")
							.append(ACGFormatHelper.format(it.getSaldoInicial().getValue(), csvSepDec))
							.append((char) 34).append(csvSepCamp);

							sb.append((char) 34).append("$")
							.append(ACGFormatHelper.format(it.getIncremento().getValue(), csvSepDec))
							.append((char) 34).append(csvSepCamp);							
							
							sb.append((char) 34).append("$")
							.append(ACGFormatHelper.format(it.getSaldoFinal().getValue(), csvSepDec))
							.append((char) 34).append(csvSepCamp);
							
							sb.append((char) 34).append(it.getNroFactura().getValue()).append((char) 34)
							.append(csvSepCamp);
							
							sb.append((char) 34).append(it.getComentario().getValue()).append((char) 34)
							.append(csvSepCamp);
							
							
						
							sb.append((char) 13).append((char) 10);

						}

						i++;

					}
					
					if (this.exportToExcel) {
						// Header
						sb.append((char) 34).append("").append((char) 34).append(csvSepCamp);
						sb.append((char) 34).append("").append((char) 34).append(csvSepCamp);
						sb.append((char) 34).append("").append((char) 34).append(csvSepCamp);
						sb.append((char) 34).append("").append((char) 34).append(csvSepCamp);
						sb.append((char) 34).append("").append((char) 34).append(csvSepCamp);
						sb.append((char) 34).append("").append((char) 34).append(csvSepCamp);
						sb.append((char) 34).append("").append((char) 34).append(csvSepCamp);
						sb.append((char) 34).append("").append((char) 34).append(csvSepCamp);
						sb.append((char) 34).append("").append((char) 34).append(csvSepCamp);
						sb.append((char) 34).append("$")
						.append(ACGFormatHelper.format(this.sumaInc, csvSepDec))
						.append((char) 34).append(csvSepCamp);
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
						ec.setResponseHeader("Content-Disposition",
								"attachment; filename=\"" + sdf.format(new Date()) + "_("
										+ this.getUsuario().getIdMayorista() + ")_" + "InformeCompraSaldoProv.csv"
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
			}
		} catch (WebServiceException ste) {
			if(ste != null && ste.getCause() != null && ste.getCause().getClass() != null) {
				if (ste.getCause().getClass().equals(ConnectException.class)) {
					FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "No se pudo establecer la comunicación (GST-TOC).\n Por favor intente nuevamente.", null));
				}else if (ste.getCause().getClass().equals(SocketTimeoutException.class)) {
					FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "No se pudo establecer la comunicación (GST-TRW).\n Por favor intente nuevamente.", null));
				} else if (ste.getCause().getClass().equals(UnknownHostException.class)) {
					LogACGHelper.escribirLog(null, "No se pudo establecer la comunicación (GST-HNC): |" + ste.getMessage() + "|");
					FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
							"No se pudo establecer la comunicación (GST-HNC).\n Por favor intente nuevamente.", null));					
				}else {
					LogACGHelper.escribirLog(null, "Informe Compra Saldo Proveedor. Error ejecutando el WS de consulta: |" + ste.getMessage() + "|");
					FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error ejecutando el WS de consulta: |" + ste.getMessage() + "|", null));
				}
			}else {
				LogACGHelper.escribirLog(null, "Informe Compra Saldo Proveedor. Error ejecutando el WS de consulta: |" + ste.getMessage() + "|");				
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error ejecutando el WS de consulta: |" + ste.getMessage() + "|", null));
			}
			FacesContext.getCurrentInstance().validationFailed();
			this.list = null;
		} catch (Exception e) {
			LogACGHelper.escribirLog(null, "Informe Compra Saldo Proveedor. Excepcion ejecutando el WS de consulta: |" + e.getMessage() + "|");	
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, 
					"Error ejecutando el WS de consulta de Compra Saldo Proveedor: |" + e.getMessage() + "|", null));
			FacesContext.getCurrentInstance().validationFailed();
			this.list = null;
		}
		PrimeFaces.current().executeScript("PF('blockPanelGral').hide()");
		return;			
	}	

	public Long getIdProveedor() {
		return idProveedor;
	}

	public void setIdProveedor(Long idProveedor) {
		this.idProveedor = idProveedor;
	}

	public Long getIdCompraSaldoProv() {
		return idCompraSaldoProv;
	}

	public void setIdCompraSaldoProv(Long idCompraSaldoProv) {
		this.idCompraSaldoProv = idCompraSaldoProv;
	}

	public Float getSumaInc() {
		return sumaInc;
	}

	public void setSumaInc(Float sumaInc) {
		this.sumaInc = sumaInc;
	}

	public List<SelectItem> getProveedores() {

		return proveedores;
	}

	public void setProveedores(List<SelectItem> proveedores) {
		this.proveedores = proveedores;
	}

	public String getNroFactura() {
		return nroFactura;
	}

	public void setNroFactura(String nroFactura) {
		this.nroFactura = nroFactura;
	}

}
