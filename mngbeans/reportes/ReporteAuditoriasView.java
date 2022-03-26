package com.americacg.cargavirtual.web.mngbeans.reportes;

import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import javax.xml.ws.WebServiceException;
import org.primefaces.PrimeFaces;
import org.springframework.context.annotation.Scope;
import com.americacg.cargavirtual.gestion.model.ArrayOfInformeLogAuditoria;
import com.americacg.cargavirtual.gestion.model.InformeLogAuditoria;
import com.americacg.cargavirtual.web.enums.CfgTimeout;
import com.americacg.cargavirtual.web.helpers.GestionServiceHelper;
import com.americacg.cargavirtual.web.helpers.LogACGHelper;
import com.americacg.cargavirtual.web.model.Error;

@Named("reporteAuditoriasView")
@Scope(org.springframework.web.context.WebApplicationContext.SCOPE_SESSION)
public class ReporteAuditoriasView extends ReporteGeneral<InformeLogAuditoria> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4228806024279232711L;

	private Long idClienteSeleccionado;

	private String operativa;

	// Cliente o usuario que fue modificado
	private Long idClienteB;
	private Long idUsuarioB;
	private String usuarioB;

	// Cliente o usuario que realizo el cambio (El efector)
	private Long idClienteE;
	private Long idUsuarioE;
	private String usuarioE;

	@Override
	public Error resetearReporte() {
		Error e = super.resetearReporte();
		this.operativa = "login";
		this.idClienteB = null;
		this.idUsuarioB = null;
		this.usuarioB = null;
		this.idClienteE = null;
		this.idUsuarioE = null;
		this.usuarioE = null;
		this.operativa = "login";
		this.cantRegistros = 0;
		return e;
	}

	public ReporteAuditoriasView() {
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

			if (!validaFechas()) {
				return;
			}

			this.list = null;
			this.mostrarRegistros = true;
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

			ArrayOfInformeLogAuditoria listaux = GestionServiceHelper.getGestionService(CfgTimeout.REPORTE)
					.informeLogAuditoria(this.getUsuario().getIdMayorista(), this.getUsuario().getIdCliente(),
							this.getUsuario().getTipoCliente(), xmlGCFechaHoraDesde, xmlGCFechaHoraHasta, idUsuarioB,
							usuarioB, idClienteB, idClienteE, idUsuarioE, usuarioE, operativa);

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
			} else if (listaux.getInformeLogAuditoria() == null) {
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
						"No existe información para la consulta realizada.", null));
			} else {
				if (listaux.getInformeLogAuditoria().isEmpty()) {
					FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
							"No existe información para la consulta realizada.", null));
				} else {
					list = listaux.getInformeLogAuditoria();
					cantRegistros = list.size();
					Integer i = 0;

					// CREO HEADER DE EXPORTACION SI ES NECESARIO PARA EXPORTAR A CSV
					if (this.exportToExcel) {
						
						sb.append((char) 34).append("Informe de Auditorias ").append(this.cantRegistros).append(" Registros").append((char) 34).append(csvSepCamp);
						sb.append((char) 13).append((char) 10);
						
						// Header
						sb.append((char) 34).append("Id").append((char) 34).append(csvSepCamp);
						if ("M".equals(this.getUsuario().getTipoCliente())) {
							sb.append((char) 34).append("Mayorista").append((char) 34).append(csvSepCamp);
						}
						sb.append((char) 34).append("Fecha").append((char) 34).append(csvSepCamp);
						if ("M".equals(this.getUsuario().getTipoCliente())) {
							sb.append((char) 34).append("Fecha Fin").append((char) 34).append(csvSepCamp);
						}
						sb.append((char) 34).append("ID Cliente Efector").append((char) 34).append(csvSepCamp);
						sb.append((char) 34).append("Cliente Efector").append((char) 34).append(csvSepCamp);
						sb.append((char) 34).append("ID Usuario Efector").append((char) 34).append(csvSepCamp);
						sb.append((char) 34).append("Usuario Efector").append((char) 34).append(csvSepCamp);
						sb.append((char) 34).append("ID Cliente").append((char) 34).append(csvSepCamp);
						sb.append((char) 34).append("Cliente").append((char) 34).append(csvSepCamp);
						sb.append((char) 34).append("ID Usuario").append((char) 34).append(csvSepCamp);
						sb.append((char) 34).append("Usuario").append((char) 34).append(csvSepCamp);
						sb.append((char) 34).append("Operativa").append((char) 34).append(csvSepCamp);
						sb.append((char) 34).append("Estado").append((char) 34).append(csvSepCamp);
						sb.append((char) 34).append("DescEstado").append((char) 34).append(csvSepCamp);

						if ("M".equals(this.getUsuario().getTipoCliente())) {
							sb.append((char) 34).append("HayError").append((char) 34).append(csvSepCamp);
							sb.append((char) 34).append("ErrorCliente").append((char) 34).append(csvSepCamp);
							sb.append((char) 34).append("MsgError").append((char) 34).append(csvSepCamp);
						}

						sb.append((char) 34).append("Coment.").append((char) 34).append(csvSepCamp);
						

						sb.append((char) 13).append((char) 10);

					}

					// DEFINO FORMATO DE FECHA PARA MOSTRAR EN EL REPORTE QUE SE EXPORTA
					SimpleDateFormat ff = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");

					// RECORRO LA LISTA QUE HACER LAS SUMATORIAS Y PARA GENERAR EL REPORTE A
					// EXPORTAR EN CASO DE SER NECESARIO
					for (InformeLogAuditoria it : list) {

						// CREO ARCHIVO DE EXPORTACION SI ES NECESARIO PARA EXPORTAR A CSV
						if (this.exportToExcel) {

							if (i == 0) {
								mostrarArchivoCSV = true;
							}

							sb.append((char) 34).append(it.getIdLogAuditoria().getValue()).append((char) 34)
									.append(csvSepCamp);
							if ("M".equals(this.getUsuario().getTipoCliente())) {
								sb.append((char) 34).append(it.getIdMayorista().getValue()).append((char) 34).append(csvSepCamp);
							}
							sb.append((char) 34);
							if(it.getFecha() != null) {
								try {
									sb.append(ff.format(it.getFecha().toGregorianCalendar().getTime()).toString());	
								} catch (Exception e) {
								}
							}
							sb.append((char) 34).append(csvSepCamp);
							if ("M".equals(this.getUsuario().getTipoCliente())) {
								sb.append((char) 34);
								if(it.getFechaFin() != null) {
									try {
										sb.append(ff.format(it.getFechaFin().toGregorianCalendar().getTime()).toString());	
									} catch (Exception e) {
									}
								}
								sb.append((char) 34).append(csvSepCamp);
							}							
							sb.append((char) 34);
							if(it.getIdClienteQueRealizoCambio() != null && it.getIdClienteQueRealizoCambio().getValue() != null) {
								sb.append(it.getIdClienteQueRealizoCambio().getValue());
							}
							sb.append((char) 34).append(csvSepCamp);

							sb.append((char) 34);
							if(it.getRazSocClienteQueRealizoCambio() != null && it.getRazSocClienteQueRealizoCambio().getValue() != null) {
								sb.append(it.getRazSocClienteQueRealizoCambio().getValue());
							}
							sb.append((char) 34).append(csvSepCamp);
							
							sb.append((char) 34);
							if(it.getIdUsuarioQueRealizoCambio() != null && it.getIdUsuarioQueRealizoCambio().getValue() != null) {
								sb.append(it.getIdUsuarioQueRealizoCambio().getValue());
							}
							sb.append((char) 34).append(csvSepCamp);
							
							sb.append((char) 34);
							if(it.getUsuarioQueRealizoCambio() != null && it.getUsuarioQueRealizoCambio().getValue() != null) {
								sb.append(it.getUsuarioQueRealizoCambio().getValue());
							}
							sb.append((char) 34).append(csvSepCamp);

							sb.append((char) 34);
							if(it.getIdCliente() != null && it.getIdCliente().getValue() != null) {
								sb.append(it.getIdCliente().getValue());
							}
							sb.append((char) 34).append(csvSepCamp);
							
							sb.append((char) 34);
							if(it.getRazSocCliente() != null && it.getRazSocCliente().getValue() != null) {
								sb.append(it.getRazSocCliente().getValue());
							}
							sb.append((char) 34).append(csvSepCamp);
							
							sb.append((char) 34);
							if(it.getIdUsuario() != null && it.getIdUsuario().getValue() != null) {
								sb.append(it.getIdUsuario().getValue());
							}
							sb.append((char) 34).append(csvSepCamp);
							
							sb.append((char) 34);
							if(it.getUsuario() != null && it.getUsuario().getValue() != null) {
								sb.append(it.getUsuario().getValue());
							}
							sb.append((char) 34).append(csvSepCamp);
							
							sb.append((char) 34).append(it.getOperativa().getValue()).append((char) 34)
								.append(csvSepCamp);
							sb.append((char) 34).append(it.getEstado().getValue()).append((char) 34)
								.append(csvSepCamp);
							
							sb.append((char) 34);
							if(it.getDescEstado() != null && it.getDescEstado().getValue() != null) {
								sb.append(it.getDescEstado().getValue());
							}
							sb.append((char) 34).append(csvSepCamp);
							
							if ("M".equals(this.getUsuario().getTipoCliente())) {
								sb.append((char) 34);
								if(it.getHayError() != null && it.getHayError().getValue() != null) {
									sb.append(it.getHayError().getValue());
								}
								sb.append((char) 34).append(csvSepCamp);
								
								sb.append((char) 34);
								if(it.getErrorCliente() != null && it.getErrorCliente().getValue() != null) {
									sb.append(it.getErrorCliente().getValue());
								}
								sb.append((char) 34).append(csvSepCamp);
								
								sb.append((char) 34);
								if(it.getMsgError() != null && it.getMsgError().getValue() != null) {
									sb.append(it.getMsgError().getValue());
								}
								sb.append((char) 34).append(csvSepCamp);
							}

							sb.append((char) 34).append(it.getComentario().getValue()).append((char) 34)
									.append(csvSepCamp);
							
							sb.append((char) 13).append((char) 10);

						}

						i++;

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
										+ this.getUsuario().getIdMayorista() + ")_" + "InformeAudit.csv"
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
							"Informe Auditorias. Error ejecutando el WS de consulta: |" + ste.getMessage() + "|");
					FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
							"Error ejecutando el WS de consulta: |" + ste.getMessage() + "|", null));
				}
			} else {
				LogACGHelper.escribirLog(null,
						"Informe Auditorias. Error ejecutando el WS de consulta: |" + ste.getMessage() + "|");
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
						"Error ejecutando el WS de consulta: |" + ste.getMessage() + "|", null));
			}
			FacesContext.getCurrentInstance().validationFailed();
			this.list = null;
		} catch (Exception e) {
			LogACGHelper.escribirLog(null,
					"Informe Auditorias. Excepcion ejecutando el WS de consulta: |" + e.getMessage() + "|");
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
					"Error ejecutando el WS de consulta de Auditorias: |" + e.getMessage() + "|", null));
			FacesContext.getCurrentInstance().validationFailed();
			this.list = null;
		}
		PrimeFaces.current().executeScript("PF('blockPanelGral').hide()");
		return;
	}

	

	public Long getIdClienteSeleccionado() {
		return idClienteSeleccionado;
	}

	public void setIdClienteSeleccionado(Long idClienteSeleccionado) {
		this.idClienteSeleccionado = idClienteSeleccionado;
	}

	public String getOperativa() {
		return operativa;
	}

	public void setOperativa(String operativa) {
		this.operativa = operativa;
	}

	public Long getIdClienteB() {
		return idClienteB;
	}

	public void setIdClienteB(Long idClienteB) {
		this.idClienteB = idClienteB;
	}

	public Long getIdUsuarioB() {
		return idUsuarioB;
	}

	public void setIdUsuarioB(Long idUsuarioB) {
		this.idUsuarioB = idUsuarioB;
	}

	public String getUsuarioB() {
		return usuarioB;
	}

	public void setUsuarioB(String usuarioB) {
		this.usuarioB = usuarioB;
	}

	public Long getIdClienteE() {
		return idClienteE;
	}

	public void setIdClienteE(Long idClienteE) {
		this.idClienteE = idClienteE;
	}

	public Long getIdUsuarioE() {
		return idUsuarioE;
	}

	public void setIdUsuarioE(Long idUsuarioE) {
		this.idUsuarioE = idUsuarioE;
	}

	public String getUsuarioE() {
		return usuarioE;
	}

	public void setUsuarioE(String usuarioE) {
		this.usuarioE = usuarioE;
	}
}
