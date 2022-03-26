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

import javax.faces.application.FacesMessage;
import javax.inject.Named;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import javax.xml.ws.WebServiceException;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;

import org.primefaces.PrimeFaces;
import org.springframework.context.annotation.Scope;

import com.americacg.cargavirtual.gestion.model.InformeCuentaCorriente;
import com.americacg.cargavirtual.gestion.model.OperacionesCuentaCorriente;
import com.americacg.cargavirtual.web.enums.CfgTimeout;
import com.americacg.cargavirtual.web.helpers.ACGFormatHelper;
import com.americacg.cargavirtual.web.helpers.GestionServiceHelper;
import com.americacg.cargavirtual.web.helpers.LogACGHelper;
import com.americacg.cargavirtual.web.model.Error;

@Named("reporteCuentaCorrienteView")
@Scope(org.springframework.web.context.WebApplicationContext.SCOPE_SESSION)
public class ReporteCuentaCorrienteView extends ReporteGeneral<OperacionesCuentaCorriente> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4281090164388153738L;
	private InformeCuentaCorriente informeCC;
	private Boolean detalladoParaDistyMay;

	private String tipoClienteResp;
	private Boolean informeDetallado;
	
	protected Long idCliente;

	@Override
	public Error resetearReporte() {
		Error e = super.resetearReporte();
		this.informeCC = null;
		this.idCliente = this.getUsuario().getIdCliente();		
		this.list = null;
		this.detalladoParaDistyMay = false;

		this.tipoClienteResp = "";
		this.informeDetallado = false;
		return e;
	}

	public ReporteCuentaCorrienteView() {
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

			informeCC = GestionServiceHelper.getGestionService(CfgTimeout.REPORTE).informeCuentaCorriente(
					this.getUsuario().getIdMayorista(), this.idCliente,
					this.getUsuario().getNivelDistribuidorSuperior(), xmlGCFechaHoraDesde, xmlGCFechaHoraHasta,
					this.getUsuario().getIdCliente(), detalladoParaDistyMay);

			if (informeCC == null
					|| informeCC.getListOperacionesCuentaCorriente().getValue().getOperacionesCuentaCorriente() == null
					|| informeCC.getListOperacionesCuentaCorriente().getValue().getOperacionesCuentaCorriente()
							.isEmpty()) {
				this.list = null;
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
						"No existe información para la consulta realizada.", null));
			} else {
				this.list = informeCC.getListOperacionesCuentaCorriente().getValue().getOperacionesCuentaCorriente();

				tipoClienteResp = informeCC.getTipoCliente().getValue();
				informeDetallado = informeCC.getInformeDetallado().getValue();
				detalladoParaDistyMay = informeCC.getInformeDetallado().getValue();

				if (this.list != null) {
					cantRegistros = this.list.size();
				} else {
					cantRegistros = 0;
				}

				// CREO HEADER DE EXPORTACION SI ES NECESARIO PARA EXPORTAR A CSV
				if (this.exportToExcel) {

					mostrarArchivoCSV = false;

					String csvSepCamp = this.getUsuario().getCsvSeparadorCampo();
					String csvSepDec = this.getUsuario().getCsvSeparadorDecimales();

					StringBuilder sb = new StringBuilder();

					sb.append((char) 34).append("Informe de Cuenta Corriente").append((char) 34);
					sb.append((char) 13).append((char) 10);

					sb.append((char) 34).append("Fecha").append((char) 34).append(csvSepCamp);
					sb.append((char) 34).append("ID Cliente").append((char) 34).append(csvSepCamp);
					sb.append((char) 34).append("Cliente").append((char) 34).append(csvSepCamp);
					if (informeDetallado) {
						sb.append((char) 34).append("idRepartoSaldo").append((char) 34).append(csvSepCamp);
					}
					sb.append((char) 34).append("Operacion").append((char) 34).append(csvSepCamp);
					if ("D".equals(tipoClienteResp) || "M".equals(tipoClienteResp)) {
						sb.append((char) 34).append("Reparto a ID").append((char) 34).append(csvSepCamp);
						sb.append((char) 34).append("Reparto a").append((char) 34).append(csvSepCamp);

					}
					sb.append((char) 34).append("Debito").append((char) 34).append(csvSepCamp);
					sb.append((char) 34).append("Credito").append((char) 34).append(csvSepCamp);
					sb.append((char) 34).append("Saldo").append((char) 34).append(csvSepCamp);
					sb.append((char) 13).append((char) 10);

					// DEFINO FORMATO DE FECHA PARA MOSTRAR EN EL REPORTE QUE SE EXPORTA
					SimpleDateFormat ff = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
					SimpleDateFormat f = new SimpleDateFormat("dd/MM/yyyy");

					// RECORRO LA LISTA QUE HACER LAS SUMATORIAS Y PARA GENERAR EL REPORTE A
					// EXPORTAR EN CASO DE SER NECESARIO
					if (list != null) {
						for (OperacionesCuentaCorriente ic : list) {
							if (informeDetallado) {
								sb.append((char) 34).append(
										ff.format(ic.getFechaPeticion().toGregorianCalendar().getTime()).toString())
										.append((char) 34).append(csvSepCamp);
							} else {
								sb.append((char) 34).append(
										f.format(ic.getFechaPeticion().toGregorianCalendar().getTime()).toString())
										.append((char) 34).append(csvSepCamp);
							}
							sb.append((char) 34).append(ic.getIdCliente().getValue()).append((char) 34)
									.append(csvSepCamp);
							if (ic.getRazonSocial().getValue() != null && !"".equals(ic.getRazonSocial().getValue())) {
								sb.append((char) 34).append(ic.getRazonSocial().getValue()).append((char) 34)
										.append(csvSepCamp);
							} else {
								sb.append((char) 34).append((char) 34).append(csvSepCamp);
							}
							if (informeDetallado) {
								sb.append((char) 34).append(ic.getIdRepartoSaldo().getValue()).append((char) 34)
										.append(csvSepCamp);
							}
							sb.append((char) 34).append(ic.getOperacion().getValue()).append((char) 34)
									.append(csvSepCamp);
							if ("D".equals(tipoClienteResp) || "M".equals(tipoClienteResp)) {
								sb.append((char) 34);
								if(ic.getIdClienteInf() != null && ic.getIdClienteInf().getValue() != null) {
									sb.append(
										ic.getIdClienteInf().getValue().compareTo(0L) > 0 ? ic.getIdClienteInf() : "");
								}
								sb.append((char) 34).append(csvSepCamp);
								
								sb.append((char) 34);
								if(ic.getRazonSocialInf() != null && ic.getRazonSocialInf().getValue() != null) {
									sb.append(ic.getRazonSocialInf().getValue());
								}
								sb.append((char) 34).append(csvSepCamp);
							}

							
							if(ic.getDebito().getValue() != null) {
								sb.append((char) 34).append("$")
								.append(ACGFormatHelper.format(ic.getDebito().getValue(), csvSepDec))
								.append((char) 34).append(csvSepCamp);								
							}else {
								sb.append((char) 34).append("$")
								.append(ACGFormatHelper.format(0f, csvSepDec))
								.append((char) 34).append(csvSepCamp);
							}

							if(ic.getCredito().getValue() != null) {
								sb.append((char) 34).append("$")
								.append(ACGFormatHelper.format(ic.getCredito().getValue(), csvSepDec))
									.append((char) 34).append(csvSepCamp);
							}else {
								sb.append((char) 34).append("$")
								.append(ACGFormatHelper.format(0f, csvSepDec))
								.append((char) 34).append(csvSepCamp);
							}

							if (informeDetallado) {
								sb.append((char) 34);
								if(ic.getSaldo() != null && ic.getSaldo().getValue() != null) {
									sb.append("$").append(ACGFormatHelper.format(ic.getSaldo().getValue(), csvSepDec));
								}
								sb.append((char) 34).append(csvSepCamp);
							}
							
							sb.append((char) 13).append((char) 10);
						}
					}

					FacesContext fc = FacesContext.getCurrentInstance();
					ExternalContext ec = fc.getExternalContext();

					ec.responseReset();
					ec.setResponseContentType("text/plain");
					ec.setResponseContentLength(sb.toString().length());
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd_HHmmss");
					ec.setResponseHeader("Content-Disposition", "attachment; filename=\"" + sdf.format(new Date())
							+ "_(" + this.getUsuario().getIdMayorista() + ")_" + "InformeCuentaCorriente.csv" + "\"");

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
					LogACGHelper.escribirLog(null, "Informe Cuenta Corriente. Error ejecutando el WS de consulta: |" + ste.getMessage() + "|");
					FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error ejecutando el WS de consulta: |" + ste.getMessage() + "|", null));
				}
			}else {
				LogACGHelper.escribirLog(null, "Informe Cuenta Corriente. Error ejecutando el WS de consulta: |" + ste.getMessage() + "|");				
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error ejecutando el WS de consulta: |" + ste.getMessage() + "|", null));
			}
			FacesContext.getCurrentInstance().validationFailed();
			this.list = null;
		} catch (Exception e) {
			LogACGHelper.escribirLog(null, "Informe Cuenta Corriente. Excepcion ejecutando el WS de consulta: |" + e.getMessage() + "|");	
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, 
					"Error ejecutando el WS de consulta de Cuenta Corriente: |" + e.getMessage() + "|", null));
			FacesContext.getCurrentInstance().validationFailed();
			this.list = null;
		}
		PrimeFaces.current().executeScript("PF('blockPanelGral').hide()");
		return;			
	}

	public InformeCuentaCorriente getInformeCC() {
		return informeCC;
	}

	public void setInformeCC(InformeCuentaCorriente informeCC) {
		this.informeCC = informeCC;
	}

	public Boolean getDetalladoParaDistyMay() {
		return detalladoParaDistyMay;
	}

	public void setDetalladoParaDistyMay(Boolean detalladoParaDistyMay) {
		this.detalladoParaDistyMay = detalladoParaDistyMay;
	}

	public String getTipoClienteResp() {
		return tipoClienteResp;
	}

	public void setTipoClienteResp(String tipoClienteResp) {
		this.tipoClienteResp = tipoClienteResp;
	}

	public Boolean getInformeDetallado() {
		return informeDetallado;
	}

	public void setInformeDetallado(Boolean informeDetallado) {
		this.informeDetallado = informeDetallado;
	}

	public Long getIdCliente() {
		return idCliente;
	}

	public void setIdCliente(Long idCliente) {
		this.idCliente = idCliente;
	}

}
