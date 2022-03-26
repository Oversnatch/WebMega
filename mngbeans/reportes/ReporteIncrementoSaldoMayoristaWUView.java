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
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import javax.xml.ws.WebServiceException;
import org.primefaces.PrimeFaces;
import org.springframework.context.annotation.Scope;
import com.americacg.cargavirtual.gestion.model.ArrayOfInformeIncrementoSaldoMayoristaWU;
import com.americacg.cargavirtual.gestion.model.InformeIncrementoSaldoMayoristaWU;
import com.americacg.cargavirtual.gestion.model.WUTiporeparto;
import com.americacg.cargavirtual.gestion.model.WUTiporepartoContainer;
import com.americacg.cargavirtual.web.enums.CfgTimeout;
import com.americacg.cargavirtual.web.helpers.ACGFormatHelper;
import com.americacg.cargavirtual.web.helpers.GestionServiceHelper;
import com.americacg.cargavirtual.web.helpers.LogACGHelper;
import com.americacg.cargavirtual.web.model.Error;

@Named("reporteIncrementoSaldoMayoristaWUView")
@Scope(org.springframework.web.context.WebApplicationContext.SCOPE_SESSION)
public class ReporteIncrementoSaldoMayoristaWUView extends ReporteGeneral<InformeIncrementoSaldoMayoristaWU> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3741309069242739459L;
	private Long idCliente;
	private Long wu_idIncSaldoMayorista;
	private Float sumaInc = 0F;

	private Long id_tipoReparto_wu;
	private List<SelectItem> wu_tiporepartos;

	@Override
	public Error resetearReporte() {
		Error e = super.resetearReporte();
		this.idCliente = null;
		this.wu_idIncSaldoMayorista = null;
		this.sumaInc = 0F;

		this.wu_tiporepartos = null;
		this.id_tipoReparto_wu = null;
		this.cantRegistros = 0;
		try {

			this.wu_tiporepartos = new ArrayList<SelectItem>();

			WUTiporepartoContainer c = GestionServiceHelper.getGestionService(CfgTimeout.REPORTE)
					.mostrarWUTiporeparto(this.getUsuario().getIdMayorista(), null);
			if (c != null) {
				if (!c.getListWUtipoReparto().getValue().getWUTiporeparto().isEmpty()) {
					for (WUTiporeparto t : c.getListWUtipoReparto().getValue().getWUTiporeparto()) {
						this.wu_tiporepartos
								.add(new SelectItem(t.getIdTipoRepartoWu().getValue(), t.getDescripcion().getValue()));
					}
				}
			}
		} catch (WebServiceException ste) {
			this.wu_tiporepartos = null;
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
					LogACGHelper.escribirLog(null, "Informe Saldo Mayorista WU. Error ejecutando el WS de consulta: |"
							+ ste.getMessage() + "|");
					e.setError("GST-OTR",
							"No se pudo establecer la comunicación (GST-OTR).\n Por favor intente nuevamente.");
				}
			} else {
				LogACGHelper.escribirLog(null,
						"Informe Saldo Mayorista WU. Error ejecutando el WS de consulta: |" + ste.getMessage() + "|");
				e.setError("GST-OTR",
						"No se pudo establecer la comunicación (GST-OTR).\n Por favor intente nuevamente.");
			}
			FacesContext.getCurrentInstance().validationFailed();
		} catch (Exception ex) {
			this.wu_tiporepartos = null;
			LogACGHelper.escribirLog(null,
					"Informe Saldo Mayorista WU. Excepcion ejecutando el WS de consulta: |" + ex.getMessage() + "|");
			e.setError("GST-OTR", "No se pudo establecer la comunicación (GST-OTR).\n Por favor intente nuevamente.");
		}
		return e;
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

		sumaInc = 0F;
		try {
			
			this.list = null;
			this.mostrarRegistros = true;
			
			if (!validaFechas()) {
				return;
			}

			GregorianCalendar gcFechaHoraDesde = new GregorianCalendar();
			gcFechaHoraDesde.setTime(fechaHoraDesde);
			XMLGregorianCalendar xmlGCFechaHoraDesde = null;
			try {
				xmlGCFechaHoraDesde = DatatypeFactory.newInstance().newXMLGregorianCalendar(gcFechaHoraDesde);
			} catch (DatatypeConfigurationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			GregorianCalendar gcFechaHoraHasta = new GregorianCalendar();
			gcFechaHoraHasta.setTime(fechaHoraHasta);
			XMLGregorianCalendar xmlGCFechaHoraHasta = null;

			try {
				xmlGCFechaHoraHasta = DatatypeFactory.newInstance().newXMLGregorianCalendar(gcFechaHoraHasta);
			} catch (DatatypeConfigurationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			ArrayOfInformeIncrementoSaldoMayoristaWU i = GestionServiceHelper.getGestionService(CfgTimeout.REPORTE)
					.wuInformeIncrementoSaldoMayorista(this.getUsuario().getIdMayorista(), xmlGCFechaHoraDesde,
							xmlGCFechaHoraHasta, idCliente, wu_idIncSaldoMayorista, id_tipoReparto_wu);

			if (i != null) {
				this.list = i.getInformeIncrementoSaldoMayoristaWU();
				if (list.isEmpty()) {
					FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
							"No se ha encontrado ningun registro con ese criterio.", null));
				} else {
					cantRegistros = list.size();

					// GENERO Y LIMPIO LAS VARIABLES PARA LA EXPORTACION
					// TODO implementar exportToCSV
					// this.exportToCSV.setExportText("");
					// this.exportToCSV.setFileName("");
					mostrarArchivoCSV = false;

					String csvSepCamp = this.getUsuario().getCsvSeparadorCampo();
					String csvSepDec = this.getUsuario().getCsvSeparadorDecimales();

					// GENERO Y LIMPIO LAS VARIABLES PARA LA EXPORTACION
					// TODO implementar exportToCSV
					// this.exportToCSV.setExportText("");
					// this.exportToCSV.setFileName("");
					mostrarArchivoCSV = false;
		
					StringBuilder sb = new StringBuilder();
					// DEFINO FORMATO DE FECHA PARA MOSTRAR EN EL REPORTE QUE SE EXPORTA
					SimpleDateFormat ff = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");


					//CREO HEADER DE EXPORTACION SI ES NECESARIO PARA EXPORTAR A CSV
					if (this.exportToExcel) {
						// Header
						sb.append((char) 34).append("Informe Incremento Saldo Mayorista de Western Union (").append(this.cantRegistros).append(" Registros)").append((char) 34).append(csvSepCamp);
						sb.append((char) 13).append((char) 10);
						
						sb.append((char) 34).append("Id").append((char) 34).append(csvSepCamp);
						sb.append((char) 34).append("Fecha").append((char) 34).append(csvSepCamp);
						sb.append((char) 34).append("ID Mayorista").append((char) 34).append(csvSepCamp);
						sb.append((char) 34).append("Mayorista").append((char) 34).append(csvSepCamp);
						sb.append((char) 34).append("ID Usuario").append((char) 34).append(csvSepCamp);
						sb.append((char) 34).append("Usuario").append((char) 34).append(csvSepCamp);
						sb.append((char) 34).append("Saldo Inicial").append((char) 34).append(csvSepCamp);
						sb.append((char) 34).append("Incremento").append((char) 34).append(csvSepCamp);
						sb.append((char) 34).append("Saldo Final").append((char) 34).append(csvSepCamp);
						sb.append((char) 34).append("ID Tipo Reparto").append((char) 34).append(csvSepCamp);
						sb.append((char) 34).append("Tipo Mov.").append((char) 34).append(csvSepCamp);
						sb.append((char) 34).append("Coment.").append((char) 34).append(csvSepCamp);
						sb.append((char) 13).append((char) 10);

					}
				
					// RECORRO LA LISTA QUE HACER LAS SUMATORIAS Y PARA GENERAR EL REPORTE A
					// EXPORTAR EN CASO DE SER NECESARIO
					for (InformeIncrementoSaldoMayoristaWU it : list) {
						sumaInc += it.getIncremento().getValue();

						// CREO ARCHIVO DE EXPORTACION SI ES NECESARIO PARA EXPORTAR A CSV
						if (this.exportToExcel) {

							if (!mostrarArchivoCSV) {
								mostrarArchivoCSV = true;
							}

							sb.append((char) 34).append(it.getWuIdIncSaldoMayorista().getValue()).append((char) 34)
									.append(csvSepCamp);
							sb.append((char) 34).append(
									ff.format(it.getFecha().toGregorianCalendar().getTime()).toString())
									.append((char) 34).append(csvSepCamp);
							sb.append((char) 34).append(it.getIdMayorista().getValue()).append((char) 34)
									.append(csvSepCamp);
							sb.append((char) 34).append(it.getRazonSocial().getValue()).append((char) 34)
									.append(csvSepCamp);
							sb.append((char) 34).append(it.getIdUsuario().getValue()).append((char) 34)
									.append(csvSepCamp);
							sb.append((char) 34).append(it.getUsuario().getValue()).append((char) 34)
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
							sb.append((char) 34).append(it.getIdTipoRepartoWu().getValue()).append((char) 34)
								.append(csvSepCamp);
							sb.append((char) 34).append(it.getDescTipoReparto().getValue()).append((char) 34)
								.append(csvSepCamp);
							sb.append((char) 34).append(it.getComentario().getValue()).append((char) 34)
								.append(csvSepCamp);							
							sb.append((char) 13).append((char) 10);

						}

					}
					
					if (this.exportToExcel) {
						// Footer
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
										+ this.getUsuario().getIdMayorista() + ")_" + "InformeIncrementoSaldoMayWU.csv"
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
					LogACGHelper.escribirLog(null, "Informe Incrementos Saldo Mayorista. Error ejecutando el WS de consulta: |"
							+ ste.getMessage() + "|");
					FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
							"Error ejecutando el WS de consulta: |" + ste.getMessage() + "|", null));
				}
			} else {
				LogACGHelper.escribirLog(null,
						"Informe Incrementos Saldo Mayorista. Error ejecutando el WS de consulta: |" + ste.getMessage() + "|");
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
						"Error ejecutando el WS de consulta: |" + ste.getMessage() + "|", null));
			}
			FacesContext.getCurrentInstance().validationFailed();
			this.list = null;
		} catch (Exception e) {
			LogACGHelper.escribirLog(null,
					"Informe Incrementos Saldo Mayorista. Excepcion ejecutando el WS de consulta: |" + e.getMessage() + "|");
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
					"Error ejecutando el WS de consulta de Incrementos Saldo Mayorista: |" + e.getMessage() + "|", null));
			FacesContext.getCurrentInstance().validationFailed();
			this.list = null;
		}
		PrimeFaces.current().executeScript("PF('blockPanelGral').hide()");
		return;
	}

	public Float getSumaInc() {
		return sumaInc;
	}

	public void setSumaInc(Float sumaInc) {
		this.sumaInc = sumaInc;
	}

	public Long getIdCliente() {
		return idCliente;
	}

	public void setIdCliente(Long idCliente) {
		this.idCliente = idCliente;
	}

	public Long getWu_idIncSaldoMayorista() {
		return wu_idIncSaldoMayorista;
	}

	public void setWu_idIncSaldoMayorista(Long wu_idIncSaldoMayorista) {
		this.wu_idIncSaldoMayorista = wu_idIncSaldoMayorista;
	}

	public Long getId_tipoReparto_wu() {
		return id_tipoReparto_wu;
	}

	public void setId_tipoReparto_wu(Long id_tipoReparto_wu) {
		this.id_tipoReparto_wu = id_tipoReparto_wu;
	}

	public List<SelectItem> getWu_tiporepartos() {
		return wu_tiporepartos;
	}

	public void setWu_tiporepartos(List<SelectItem> wu_tiporepartos) {
		this.wu_tiporepartos = wu_tiporepartos;
	}

}
