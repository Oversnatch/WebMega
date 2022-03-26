package com.americacg.cargavirtual.web.mngbeans.reportes;

import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.inject.Named;
import javax.xml.ws.WebServiceException;

import org.primefaces.PrimeFaces;
import org.springframework.context.annotation.Scope;

import com.americacg.cargavirtual.gestion.model.ArrayOfDescripcion;
import com.americacg.cargavirtual.gestion.model.Descripcion;
import com.americacg.cargavirtual.web.enums.CfgTimeout;
import com.americacg.cargavirtual.web.helpers.GestionServiceHelper;
import com.americacg.cargavirtual.web.helpers.LogACGHelper;
import com.americacg.cargavirtual.web.model.Error;

@Named("reporteIdsUnicosView")
@Scope(org.springframework.web.context.WebApplicationContext.SCOPE_SESSION)
public class ReporteIdsUnicosView extends ReporteGeneral<Descripcion> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2520267710565524335L;
	private Long idUnicoCliente;
	private String idMay_idCli_idCliExt;

	@Override
	public Error resetearReporte() {
		Error e = super.resetearReporte();
		this.list = null;
		this.idUnicoCliente = null;
		this.idMay_idCli_idCliExt = null;
		this.cantRegistros = 0;
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
		try {
			
			this.list = null;
			this.mostrarRegistros = true;
			
			ArrayOfDescripcion i = GestionServiceHelper.getGestionService(CfgTimeout.REPORTE)
					.mostrarListadoIdsunificacionclientes(idUnicoCliente, idMay_idCli_idCliExt);

			if (i == null || i.getDescripcion().isEmpty()) {
			} else {
				this.list = i.getDescripcion();
				if (this.list == null) {
					FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
							"No se ha encontrado ningun registro con ese criterio.", null));

				} else if (this.list.isEmpty()) {
					FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
							"No se ha encontrado ningun registro con ese criterio.", null));
					this.list = null;
				} else {
					cantRegistros = this.list.size();
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
						sb.append((char) 34).append("Informe Ids Puntos de Venta (").append(this.cantRegistros).append(" Registros)").append((char) 34).append(csvSepCamp);
						sb.append((char) 13).append((char) 10);
						
						sb.append((char) 34).append("Id Unico Cliente").append((char) 34).append(csvSepCamp);
						sb.append((char) 34).append("idMay/idCli/idCliExt").append((char) 34).append(csvSepCamp);

						sb.append((char) 13).append((char) 10);
					}
				
					// RECORRO LA LISTA QUE HACER LAS SUMATORIAS Y PARA GENERAR EL REPORTE A
					// EXPORTAR EN CASO DE SER NECESARIO
					for (Descripcion it : list) {


						// CREO ARCHIVO DE EXPORTACION SI ES NECESARIO PARA EXPORTAR A CSV
						if (this.exportToExcel) {

							if (!mostrarArchivoCSV) {
								mostrarArchivoCSV = true;
							}

							sb.append((char) 34).append(it.getId().getValue()).append((char) 34)
									.append(csvSepCamp);
							sb.append((char) 34).append(it.getDescripcion().getValue()).append((char) 34)
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
										+ this.getUsuario().getIdMayorista() + ")_" + "InformeIdsUnicos.csv"
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
					LogACGHelper.escribirLog(null, "Informe IDs Unicos. Error ejecutando el WS de consulta: |"
							+ ste.getMessage() + "|");
					FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
							"Error ejecutando el WS de consulta: |" + ste.getMessage() + "|", null));
				}
			} else {
				LogACGHelper.escribirLog(null,
						"Informe IDs Unicos. Error ejecutando el WS de consulta: |" + ste.getMessage() + "|");
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
						"Error ejecutando el WS de consulta: |" + ste.getMessage() + "|", null));
			}
			FacesContext.getCurrentInstance().validationFailed();
			this.list = null;
		} catch (Exception e) {
			LogACGHelper.escribirLog(null,
					"Informe IDs Unicos. Excepcion ejecutando el WS de consulta: |" + e.getMessage() + "|");
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
					"Error ejecutando el WS de consulta de IDs Unicos: |" + e.getMessage() + "|", null));
			FacesContext.getCurrentInstance().validationFailed();
			this.list = null;
		}
		PrimeFaces.current().executeScript("PF('blockPanelGral').hide()");
		return;
	}

	public Long getIdUnicoCliente() {
		return idUnicoCliente;
	}

	public void setIdUnicoCliente(Long idUnicoCliente) {
		this.idUnicoCliente = idUnicoCliente;
	}

	public String getIdMay_idCli_idCliExt() {
		return idMay_idCli_idCliExt;
	}

	public void setIdMay_idCli_idCliExt(String idMay_idCli_idCliExt) {
		this.idMay_idCli_idCliExt = idMay_idCli_idCliExt;
	}

}
