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
import java.util.List;
import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.inject.Named;
import javax.xml.ws.WebServiceException;
import org.primefaces.PrimeFaces;
import org.springframework.context.annotation.Scope;
import com.americacg.cargavirtual.gestion.model.ArrayOfSaldoProveedor;
import com.americacg.cargavirtual.gestion.model.SaldoProveedor;
import com.americacg.cargavirtual.web.enums.CfgTimeout;
import com.americacg.cargavirtual.web.helpers.ACGFormatHelper;
import com.americacg.cargavirtual.web.helpers.GestionServiceHelper;
import com.americacg.cargavirtual.web.helpers.LogACGHelper;
import com.americacg.cargavirtual.web.model.Error;

@Named("reporteEstadoProveedoresView")
@Scope(org.springframework.web.context.WebApplicationContext.SCOPE_SESSION)
public class ReporteEstadoProveedoresView extends ReporteGeneral<SaldoProveedor> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4394702004866819100L;
	private Long idProveedor;
	private List<SelectItem> proveedores;

	@Override
	public Error resetearReporte() {
		Error e = super.resetearReporte();
		this.list = null;
		this.idProveedor = null;
		this.cantRegistros = 0;

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
					LogACGHelper.escribirLog(null,
							"No se pudo establecer la comunicación (GST-HNC): |" + ste.getMessage() + "|");
					e.setError("GST-TRW",
							"No se pudo establecer la comunicación (GST-HNC).\n Por favor intente nuevamente.");
				} else {
					LogACGHelper.escribirLog(null, "Informe Estado Proveedores. Error ejecutando el WS de consulta: |"
							+ ste.getMessage() + "|");
					e.setError("GST-OTR",
							"No se pudo establecer la comunicación (GST-OTR).\n Por favor intente nuevamente.");
				}
			} else {
				LogACGHelper.escribirLog(null,
						"Informe Estado Proveedores. Error ejecutando el WS de consulta: |" + ste.getMessage() + "|");
				e.setError("GST-OTR",
						"No se pudo establecer la comunicación (GST-OTR).\n Por favor intente nuevamente.");
			}
			FacesContext.getCurrentInstance().validationFailed();
		} catch (Exception ex) {
			LogACGHelper.escribirLog(null,
					"Informe Estado Proveedores. Excepcion ejecutando el WS de consulta: |" + ex.getMessage() + "|");
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

		try {

			this.list = null;
			this.mostrarRegistros = true;
			
			if (!validaFechas()) {
				this.list = null;

			} else {

				ArrayOfSaldoProveedor i = GestionServiceHelper.getGestionService(CfgTimeout.REPORTE)
						.obtenerSaldoProveedores(this.getUsuario().getIdMayorista(), idProveedor, false, true);
				
				if (i != null) {
					this.list = i.getSaldoProveedor();
					if (this.list == null) {
						FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
								"La consulta de Saldo de Proveedores devolvio null", null));

					} else if (this.list.isEmpty()) {
						FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
								"No se ha encontrado ningun registro con ese criterio.", null));
						this.list = null;
					} else {
						this.cantRegistros = list.size();
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

						// CREO HEADER DE EXPORTACION SI ES NECESARIO PARA EXPORTAR A CSV
						if (this.exportToExcel) {
							// Header
							sb.append((char) 34).append("Informe Estado Proveedores (").append(this.cantRegistros)
									.append(" Registros)").append((char) 34).append(csvSepCamp);
							sb.append((char) 13).append((char) 10);

							sb.append((char) 34).append("ID Proveedor").append((char) 34).append(csvSepCamp);
							sb.append((char) 34).append("Proveedor").append((char) 34).append(csvSepCamp);
							sb.append((char) 34).append("Bolsa").append((char) 34).append(csvSepCamp);
							sb.append((char) 34).append("CC").append((char) 34).append(csvSepCamp);
							sb.append((char) 13).append((char) 10);
						}

						// RECORRO LA LISTA QUE HACER LAS SUMATORIAS Y PARA GENERAR EL REPORTE A
						// EXPORTAR EN CASO DE SER NECESARIO
						for (SaldoProveedor it : list) {

							// CREO ARCHIVO DE EXPORTACION SI ES NECESARIO PARA EXPORTAR A CSV
							if (this.exportToExcel) {

								if (!mostrarArchivoCSV) {
									mostrarArchivoCSV = true;
								}

								sb.append((char) 34).append(it.getIdProveedor().getValue()).append((char) 34)
										.append(csvSepCamp);
								sb.append((char) 34).append(it.getRazonSocial().getValue()).append((char) 34)
										.append(csvSepCamp);
								sb.append((char) 34).append("$")
										.append(ACGFormatHelper.format(it.getSaldoActual().getValue(), csvSepDec))
										.append((char) 34).append(csvSepCamp);
								sb.append((char) 34).append("$")
										.append(ACGFormatHelper
												.format(it.getEstadoCuentaCorrienteProveedor().getValue(), csvSepDec))
										.append((char) 34).append(csvSepCamp);
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
											+ this.getUsuario().getIdMayorista() + ")_" + "InformeEstProv.csv" + "\"");

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
					LogACGHelper.escribirLog(null,
							"No se pudo establecer la comunicación (GST-HNC): |" + ste.getMessage() + "|");
					FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
							"No se pudo establecer la comunicación (GST-HNC).\n Por favor intente nuevamente.", null));
				} else {
					LogACGHelper.escribirLog(null, "Informe Estado Proveedores. Error ejecutando el WS de consulta: |"
							+ ste.getMessage() + "|");
					FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
							"Error ejecutando el WS de consulta: |" + ste.getMessage() + "|", null));
				}
			} else {
				LogACGHelper.escribirLog(null,
						"Informe Estado Proveedores. Error ejecutando el WS de consulta: |" + ste.getMessage() + "|");
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
						"Error ejecutando el WS de consulta: |" + ste.getMessage() + "|", null));
			}
			FacesContext.getCurrentInstance().validationFailed();
			this.list = null;
		} catch (Exception e) {
			LogACGHelper.escribirLog(null,
					"Informe Estado Proveedores. Excepcion ejecutando el WS de consulta: |" + e.getMessage() + "|");
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
					"Error ejecutando el WS de consulta de Estado Proveedores: |" + e.getMessage() + "|", null));
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

	public List<SelectItem> getProveedores() {
		return proveedores;
	}

	public void setProveedores(List<SelectItem> proveedores) {
		this.proveedores = proveedores;
	}
}
