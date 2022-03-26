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
import com.americacg.cargavirtual.gestion.model.Banco;
import com.americacg.cargavirtual.gestion.model.BancoContainer;
import com.americacg.cargavirtual.web.enums.CfgTimeout;
import com.americacg.cargavirtual.web.helpers.ACGFormatHelper;
import com.americacg.cargavirtual.web.helpers.GestionServiceHelper;
import com.americacg.cargavirtual.web.helpers.LogACGHelper;
import com.americacg.cargavirtual.web.model.Error;

@Named("reporteEstadoBancosView")
@Scope(org.springframework.web.context.WebApplicationContext.SCOPE_SESSION)
public class ReporteEstadoBancosView extends ReporteGeneral<Banco> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2255245745148854984L;
	private Long idBanco;
	private List<SelectItem> bancos;

	@Override
	public Error resetearReporte() {
		Error e = super.resetearReporte();
		this.list = null;
		this.idBanco = null;
		this.cantRegistros = 0;

		try {
			this.bancos = new ArrayList<SelectItem>();
			BancoContainer bc = GestionServiceHelper.getGestionService(CfgTimeout.REPORTE)
					.mostrarBanco(this.getUsuario().getIdMayorista(), null, null, false);
			if ((bc != null) && (bc.getListBanco() != null) && (bc.getListBanco().getValue() != null)
					&& (bc.getListBanco().getValue().getBanco() != null)
					&& (bc.getListBanco().getValue().getBanco().size() > 0)) {
				for (Banco b : bc.getListBanco().getValue().getBanco()) {
					this.bancos.add(new SelectItem(b.getIdBanco().getValue(), b.getNombreBanco().getValue() + " | " + b.getDescInterna().getValue()));
				}
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
							"Informe Estado Bancos. Error ejecutando el WS de consulta: |" + ste.getMessage() + "|");
					e.setError("GST-OTR",
							"No se pudo establecer la comunicación (GST-OTR).\n Por favor intente nuevamente.");
				}
			} else {
				LogACGHelper.escribirLog(null,
						"Informe Estado Bancos. Error ejecutando el WS de consulta: |" + ste.getMessage() + "|");
				e.setError("GST-OTR",
						"No se pudo establecer la comunicación (GST-OTR).\n Por favor intente nuevamente.");
			}
			FacesContext.getCurrentInstance().validationFailed();
		} catch (Exception ex) {
			LogACGHelper.escribirLog(null,
					"Informe Estado Bancos. Excepcion ejecutando el WS de consulta: |" + ex.getMessage() + "|");
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

				BancoContainer bc = GestionServiceHelper.getGestionService(CfgTimeout.REPORTE)
						.mostrarBanco(this.getUsuario().getIdMayorista(), idBanco, null, true);
				this.list = null;

				if (bc == null) {
					FacesContext.getCurrentInstance().addMessage(null,
							new FacesMessage(FacesMessage.SEVERITY_ERROR, "La consulta de Bancos devolvio null", null));
				} else if (bc.getError().getValue().getHayError().getValue()) {
					FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
							"Error: " + bc.getError().getValue().getMsgError().getValue(), null));
				} else if (bc.getListBanco() == null) {
					FacesContext.getCurrentInstance().addMessage(null,
							new FacesMessage(FacesMessage.SEVERITY_ERROR, "La lista de bancos es null", null));
				} else if (bc.getListBanco().getValue().getBanco().isEmpty()) {
					FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
							"No hay resultado de bancos con el criterio de busqueda aplicado", null));
				} else {
					this.list = bc.getListBanco().getValue().getBanco();
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


					//CREO HEADER DE EXPORTACION SI ES NECESARIO PARA EXPORTAR A CSV
					if (this.exportToExcel) {
						// Header
						sb.append((char) 34).append("Informe Estado Bancos (").append(this.cantRegistros).append(" Registros)").append((char) 34).append(csvSepCamp);
						sb.append((char) 13).append((char) 10);
						
						sb.append((char) 34).append("ID Banco").append((char) 34).append(csvSepCamp);
						sb.append((char) 34).append("Banco").append((char) 34).append(csvSepCamp);
						sb.append((char) 34).append("Sucursal").append((char) 34).append(csvSepCamp);
						sb.append((char) 34).append("Cuenta").append((char) 34).append(csvSepCamp);
						sb.append((char) 34).append("CBU").append((char) 34).append(csvSepCamp);
						sb.append((char) 34).append("Descripcion").append((char) 34).append(csvSepCamp);
						sb.append((char) 34).append("Estado").append((char) 34).append(csvSepCamp);
						sb.append((char) 34).append("Es Cuenta de Tercero?").append((char) 34).append(csvSepCamp);
						sb.append((char) 34).append("idProveedor de la cuenta de Tercero").append((char) 34).append(csvSepCamp);
						sb.append((char) 34).append("Proveedor de la cuenta de Tercero").append((char) 34).append(csvSepCamp);
						sb.append((char) 34).append("Desc. Interna").append((char) 34).append(csvSepCamp);
						sb.append((char) 34).append("CC").append((char) 34).append(csvSepCamp);
						sb.append((char) 13).append((char) 10);

					}
				
					// RECORRO LA LISTA QUE HACER LAS SUMATORIAS Y PARA GENERAR EL REPORTE A
					// EXPORTAR EN CASO DE SER NECESARIO
					for (Banco it : list) {


						// CREO ARCHIVO DE EXPORTACION SI ES NECESARIO PARA EXPORTAR A CSV
						if (this.exportToExcel) {

							if (!mostrarArchivoCSV) {
								mostrarArchivoCSV = true;
							}

							sb.append((char) 34).append(it.getIdBanco().getValue()).append((char) 34)
								.append(csvSepCamp);
							sb.append((char) 34).append(it.getNombreBanco().getValue()).append((char) 34)
								.append(csvSepCamp);
							sb.append((char) 34).append(it.getSucursal().getValue()).append((char) 34)
								.append(csvSepCamp);
							sb.append((char) 34).append(it.getCuenta().getValue()).append((char) 34)
								.append(csvSepCamp);
							sb.append((char) 34).append(it.getCBU().getValue()).append((char) 34)
								.append(csvSepCamp);
							sb.append((char) 34).append(it.getDescripcion().getValue()).append((char) 34)
								.append(csvSepCamp);
							sb.append((char) 34).append(it.getEstado().getValue()).append((char) 34)
								.append(csvSepCamp);
							sb.append((char) 34).append(it.getCuentaDeTercero().getValue() ? "SI" : "NO").append((char) 34)
								.append(csvSepCamp);
							sb.append((char) 34);
							if(it.getIdProveedorDeLaCuentaDeTercero() != null && it.getIdProveedorDeLaCuentaDeTercero().getValue() != null ) {
								sb.append(it.getIdProveedorDeLaCuentaDeTercero().getValue());
							}
							sb.append((char) 34).append(csvSepCamp);
							sb.append((char) 34);
							if(it.getRazSocProveedorDeLaCuentaDeTercero() != null && it.getRazSocProveedorDeLaCuentaDeTercero().getValue() != null ) {
								sb.append(it.getRazSocProveedorDeLaCuentaDeTercero().getValue());
							}
							sb.append((char) 34).append(csvSepCamp);
							
							sb.append((char) 34).append(it.getDescInterna().getValue()).append((char) 34)
								.append(csvSepCamp);
							sb.append((char) 34).append("$")
								.append(ACGFormatHelper.format(it.getEstadoCuentaCorrienteBanco().getValue(), csvSepDec))
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
										+ this.getUsuario().getIdMayorista() + ")_" + "InformeEstBanco.csv"
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
					LogACGHelper.escribirLog(null, "Informe Estado Bancos. Error ejecutando el WS de consulta: |" + ste.getMessage() + "|");
					FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error ejecutando el WS de consulta: |" + ste.getMessage() + "|", null));
				}
			}else {
				LogACGHelper.escribirLog(null, "Informe Estado Bancos. Error ejecutando el WS de consulta: |" + ste.getMessage() + "|");				
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error ejecutando el WS de consulta: |" + ste.getMessage() + "|", null));
			}
			FacesContext.getCurrentInstance().validationFailed();
			this.list = null;
		} catch (Exception e) {
			LogACGHelper.escribirLog(null, "Informe Estado Bancos. Excepcion ejecutando el WS de consulta: |" + e.getMessage() + "|");	
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, 
					"Error ejecutando el WS de consulta de Estado Bancos: |" + e.getMessage() + "|", null));
			FacesContext.getCurrentInstance().validationFailed();
			this.list = null;
		}
		PrimeFaces.current().executeScript("PF('blockPanelGral').hide()");
		return;			
	}

	public Long getIdBanco() {
		return idBanco;
	}

	public void setIdBanco(Long idBanco) {
		this.idBanco = idBanco;
	}

	public List<SelectItem> getBancos() {
		return bancos;
	}

	public void setBancos(List<SelectItem> bancos) {
		this.bancos = bancos;
	}

}
