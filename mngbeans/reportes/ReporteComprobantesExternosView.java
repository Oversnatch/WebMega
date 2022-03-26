package com.americacg.cargavirtual.web.mngbeans.reportes;

import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import javax.xml.ws.WebServiceException;
import org.primefaces.PrimeFaces;
import org.springframework.context.annotation.Scope;
import com.americacg.cargavirtual.gestion.model.ComprobanteExterno;
import com.americacg.cargavirtual.gestion.model.InformeComprobantesExternos;
import com.americacg.cargavirtual.web.enums.CfgTimeout;
import com.americacg.cargavirtual.web.helpers.ACGFormatHelper;
import com.americacg.cargavirtual.web.helpers.GestionServiceHelper;
import com.americacg.cargavirtual.web.helpers.LogACGHelper;
import com.americacg.cargavirtual.web.model.Error;

@Named("reporteComprobantesExternosView")
@Scope(org.springframework.web.context.WebApplicationContext.SCOPE_SESSION)
public class ReporteComprobantesExternosView extends ReporteGeneral<ComprobanteExterno> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6168942102047838101L;
	private Long movExtIdComprobante;
	private Integer movExtIdEstado;
	private String movExtTipoComp;
	private String movExtNumeroComp;
	private Boolean movExtIgnorarFecha;
	private String movExtIdClienteExt;
	private Float movExtImporte;
	private Long movExtIdCliente;
	private List<ComprobanteExterno> lce;

	@Override
	public Error resetearReporte() {
		Error e = super.resetearReporte();
		this.movExtIdComprobante = null;
		this.movExtIdEstado = null;
		this.movExtTipoComp = null;
		this.movExtNumeroComp = null;
		this.movExtIgnorarFecha = false;
		this.movExtIdClienteExt = null;
		this.movExtImporte = null;
		this.movExtIdCliente = null;
		return e;
	}

	public ReporteComprobantesExternosView() {
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

			this.mostrarRegistros = true;
			
			// Limpio la tabla
			list = null;

			if (!validaFechas()) {
				return;
			}

			// Indica la cantidad de registros que voy a usar en el limit de la query a la
			// base de datos
			Integer cantRegistrosFiltro = null;
			if (exportToExcel) {
				cantRegistrosFiltro = cantMaxRegistrosAexportar;
			} else {
				cantRegistrosFiltro = cantMaxRegistrosAmostrarPorPantalla;
			}

			GregorianCalendar gcFechaHoraDesde = new GregorianCalendar();
			gcFechaHoraDesde.setTime(fechaHoraDesde);
			XMLGregorianCalendar xmlGCFechaHoraDesde = DatatypeFactory.newInstance()
					.newXMLGregorianCalendar(gcFechaHoraDesde);

			GregorianCalendar gcFechaHoraHasta = new GregorianCalendar();
			gcFechaHoraHasta.setTime(fechaHoraHasta);
			XMLGregorianCalendar xmlGCFechaHoraHasta = DatatypeFactory.newInstance()
					.newXMLGregorianCalendar(gcFechaHoraHasta);

			InformeComprobantesExternos listaux = GestionServiceHelper.getGestionService(CfgTimeout.REPORTE)
					.informeComprobantesExternos(this.getUsuario().getIdMayorista(), movExtIdComprobante,
							xmlGCFechaHoraDesde, xmlGCFechaHoraHasta, movExtTipoComp, movExtNumeroComp,
							movExtIdClienteExt, movExtImporte, movExtIdEstado, movExtIdCliente, movExtIgnorarFecha,
							cantRegistrosFiltro);

			// GENERO Y LIMPIO LAS VARIABLES PARA LA EXPORTACION
			// this.exportToCSV.setExportText("");
			// this.exportToCSV.setFileName("");
			mostrarArchivoCSV = false;

			String csvSepCamp = this.getUsuario().getCsvSeparadorCampo();
			String csvSepDec = this.getUsuario().getCsvSeparadorDecimales();

			StringBuilder sb = new StringBuilder();

			if (listaux == null) {
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
						"La respuesta del informe de comprobantes externos es null", null));
			} else if (listaux.getError().getValue().getHayError().getValue()) {
				// TODO Ver porque no entra en esta condicion. No reconoce el Error
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
						listaux.getError().getValue().getMsgError().getValue(), null));
			} else {
				if (listaux.getLce() == null) {
					FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
							"No se ha encontrado ningun registro con ese criterio.", null));
				} else {
					if (listaux.getLce().getValue().getComprobanteExterno().isEmpty()) {
						FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
								"No se ha encontrado ningun registro con ese criterio.", null));
					} else {
						list = listaux.getLce().getValue().getComprobanteExterno();
						cantRegistros = list.size();
						Integer i = 0;

						// CREO HEADER DE EXPORTACION SI ES NECESARIO PARA EXPORTAR A CSV
						if (this.exportToExcel) {
							if (i == 0) {
								mostrarArchivoCSV = true;
							}
							// Header

							sb.append((char) 34).append("ID Comp.").append((char) 34).append(csvSepCamp);
							sb.append((char) 34).append("Fecha de Importacion").append((char) 34).append(csvSepCamp);
							sb.append((char) 34).append("Tipo Comprobante").append((char) 34).append(csvSepCamp);
							sb.append((char) 34).append("Nro Comprobante").append((char) 34).append(csvSepCamp);
							sb.append((char) 34).append("Fecha Emision").append((char) 34).append(csvSepCamp);
							sb.append((char) 34).append("ID Cli Ext").append((char) 34).append(csvSepCamp);
							sb.append((char) 34).append("Importe").append((char) 34).append(csvSepCamp);
							sb.append((char) 34).append("Tipo Producto").append((char) 34).append(csvSepCamp);
							sb.append((char) 34).append("Tipo Operacion").append((char) 34).append(csvSepCamp);
							sb.append((char) 34).append("ID Cliente").append((char) 34).append(csvSepCamp);
							sb.append((char) 34).append("Cliente").append((char) 34).append(csvSepCamp);
							sb.append((char) 34).append("ID Estado").append((char) 34).append(csvSepCamp);
							sb.append((char) 34).append("Estado").append((char) 34).append(csvSepCamp);
							sb.append((char) 34).append("ID Rep. Saldo").append((char) 34).append(csvSepCamp);
							sb.append((char) 34).append("Comentario").append((char) 34).append(csvSepCamp);
							sb.append((char) 13).append((char) 10);

							SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

							for (ComprobanteExterno ce : list) {
								// CREO ARCHIVO DE EXPORTACION SI ES NECESARIO PARA EXPORTAR A CSV

								sb.append((char) 34).append(ce.getIdComprobante().getValue()).append((char) 34).append(csvSepCamp);
								sb.append((char) 34)
										.append((ce.getFechaImportacion() == null
												|| "".equals(ce.getFechaImportacion().toString())) ? ""
														: format.format(ce.getFechaImportacion().toGregorianCalendar().getTime()))
										.append((char) 34).append(csvSepCamp);
								sb.append((char) 34).append(ce.getTipoComprobante().getValue()).append((char) 34)
										.append(csvSepCamp);
								sb.append((char) 34).append("'").append(ce.getNumeroComprobante().getValue()).append((char) 34)
										.append(csvSepCamp);
								sb.append((char) 34)
										.append((ce.getFechaEmision() == null
												|| "".equals(ce.getFechaEmision().toString())) ? ""
														: format.format(ce.getFechaEmision().toGregorianCalendar().getTime()))
										.append((char) 34).append(csvSepCamp);
								sb.append((char) 34).append(ce.getIdClienteExterno().getValue()).append((char) 34)
										.append(csvSepCamp);
								
								sb.append((char) 34).append("$")
									.append(ACGFormatHelper.format(ce.getImporte().getValue(), csvSepDec))
									.append((char) 34).append(csvSepCamp);
								
								sb.append((char) 34).append(ce.getTipoProducto().getValue()).append((char) 34).append(csvSepCamp);
								sb.append((char) 34).append(ce.getTipoOperacion().getValue()).append((char) 34).append(csvSepCamp);
								
								sb.append((char) 34);
								if(ce.getIdCliente() != null && ce.getIdCliente().getValue() != null) {
									sb.append(ce.getIdCliente().getValue());
								}
								sb.append((char) 34).append(csvSepCamp);
								
								sb.append((char) 34)
										.append(ce.getRazonSocialCliente().getValue() != null ? ce.getRazonSocialCliente().getValue() : "")
										.append((char) 34).append(csvSepCamp);
								sb.append((char) 34).append(ce.getEstado().getValue()).append((char) 34).append(csvSepCamp);
								sb.append((char) 34)
										.append(ce.getEstado().getValue() == 1 ? "Pendiente"
												: ce.getEstado().getValue() == 2 ? "Aprobado"
														: ce.getEstado().getValue() == 3 ? "Rechazado" : "")
										.append((char) 34).append(csvSepCamp);
								
								sb.append((char) 34);
								if(ce.getIdRepartoSaldo() != null && ce.getIdRepartoSaldo().getValue() != null) {
									sb.append(ce.getIdRepartoSaldo().getValue());
								}
								sb.append((char) 34).append(csvSepCamp);
								
								sb.append((char) 34).append(ce.getComentario().getValue()).append((char) 34).append(csvSepCamp);
								sb.append((char) 13).append((char) 10);

							}

							// GENERO ARCHIVO DE EXPORTACION SI ES NECESARIO PARA EXPORTAR A CSV
							//if (this.exportToExcel) {

							FacesContext fc = FacesContext.getCurrentInstance();
							ExternalContext ec = fc.getExternalContext();

							ec.responseReset();
							ec.setResponseContentType("text/plain");
							ec.setResponseContentLength(sb.toString().length());

							SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd_HHmmss");
							ec.setResponseHeader("Content-Disposition",
									"attachment; filename=\"" + sdf.format(new Date()) + "_("
											+ this.getUsuario().getIdMayorista() + ")_" + "InformeComprobantesExternos.csv"
											+ "\"");

							OutputStream os = ec.getResponseOutputStream();
							OutputStreamWriter osw = new OutputStreamWriter(os);
							PrintWriter writer = new PrintWriter(osw);
							writer.write(sb.toString());
							writer.flush();
							writer.close();
							sb.setLength(0);

							fc.responseComplete();

							//}
							
							// Limpio la lista para evitar que se dibuje en pantalla
							list = null;
						}
						PrimeFaces.current().executeScript("PF('panelFiltroWG').toggle();");
					}
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
					LogACGHelper.escribirLog(null, "Informe Comprobantes Externo. Error ejecutando el WS de consulta: |" + ste.getMessage() + "|");
					FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error ejecutando el WS de consulta: |" + ste.getMessage() + "|", null));
				}
			}else {
				LogACGHelper.escribirLog(null, "Informe Comprobantes Externo. Error ejecutando el WS de consulta: |" + ste.getMessage() + "|");				
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error ejecutando el WS de consulta: |" + ste.getMessage() + "|", null));
			}
			FacesContext.getCurrentInstance().validationFailed();
			this.list = null;
		} catch (Exception e) {
			LogACGHelper.escribirLog(null, "Informe Comprobantes Externo. Excepcion ejecutando el WS de consulta: |" + e.getMessage() + "|");	
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, 
					"Error ejecutando el WS de consulta de Comprobantes Externo: |" + e.getMessage() + "|", null));
			FacesContext.getCurrentInstance().validationFailed();
			this.list = null;
		}
		PrimeFaces.current().executeScript("PF('blockPanelGral').hide()");
		return;			
	}	

	public Integer getMovExtIdEstado() {
		return movExtIdEstado;
	}

	public void setMovExtIdEstado(Integer movExtIdEstado) {
		this.movExtIdEstado = movExtIdEstado;
	}

	public String getMovExtTipoComp() {
		return movExtTipoComp;
	}

	public void setMovExtTipoComp(String movExtTipoComp) {
		this.movExtTipoComp = movExtTipoComp;
	}

	public String getMovExtNumeroComp() {
		return movExtNumeroComp;
	}

	public void setMovExtNumeroComp(String movExtNumeroComp) {
		this.movExtNumeroComp = movExtNumeroComp;
	}

	public Boolean getMovExtIgnorarFecha() {
		return movExtIgnorarFecha;
	}

	public void setMovExtIgnorarFecha(Boolean movExtIgnorarFecha) {
		this.movExtIgnorarFecha = movExtIgnorarFecha;
	}

	public String getMovExtIdClienteExt() {
		return movExtIdClienteExt;
	}

	public void setMovExtIdClienteExt(String movExtIdClienteExt) {
		this.movExtIdClienteExt = movExtIdClienteExt;
	}

	public Float getMovExtImporte() {
		return movExtImporte;
	}

	public void setMovExtImporte(Float movExtImporte) {
		this.movExtImporte = movExtImporte;
	}

	public Long getMovExtIdCliente() {
		return movExtIdCliente;
	}

	public void setMovExtIdCliente(Long movExtIdCliente) {
		this.movExtIdCliente = movExtIdCliente;
	}

	public List<ComprobanteExterno> getLce() {
		return lce;
	}

	public void setLce(List<ComprobanteExterno> lce) {
		this.lce = lce;
	}

	public Long getMovExtIdComprobante() {
		return movExtIdComprobante;
	}

	public void setMovExtIdComprobante(Long movExtIdComprobante) {
		this.movExtIdComprobante = movExtIdComprobante;
	}

}
