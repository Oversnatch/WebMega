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
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import javax.xml.ws.WebServiceException;
import org.primefaces.PrimeFaces;
import org.springframework.context.annotation.Scope;
import com.americacg.cargavirtual.gestion.model.ArrayOfDatosMAS;
import com.americacg.cargavirtual.gestion.model.DatosMAS;
import com.americacg.cargavirtual.web.enums.CfgTimeout;
import com.americacg.cargavirtual.web.helpers.ACGFormatHelper;
import com.americacg.cargavirtual.web.helpers.GestionServiceHelper;
import com.americacg.cargavirtual.web.helpers.LogACGHelper;
import com.americacg.cargavirtual.web.model.Error;

@Named("reporteMASView")
@Scope(org.springframework.web.context.WebApplicationContext.SCOPE_SESSION)
public class ReporteMASView extends ReporteGeneral<DatosMAS> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5410703603375965145L;
	private Long idClienteF;
	private String nombreF;
	private String razSocClienteF;

	private Integer hora;

	private Integer sumCantBilletes;
	private Float sumImporteTotal;
	private Float sumImporteVentas;
	private Float sumImporteDepositos;
	private Long idCliente = null;

	@Override
	public Error resetearReporte() {
		Error e = super.resetearReporte();
		this.list = null;
		this.idCliente = null;
		this.nombreF = null;
		this.razSocClienteF = null;
		this.hora = 0;
		this.sumCantBilletes = 0;
		this.sumImporteTotal = 0F;
		this.sumImporteVentas = 0F;
		this.sumImporteDepositos = 0F;
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
			this.cantRegistros = 0;
			
			// Reseteo los contadores
			this.sumCantBilletes = 0;
			this.sumImporteTotal = 0F;
			this.sumImporteVentas = 0F;
			this.sumImporteDepositos = 0F;

			// Validacion de fechas
			if (!validaFechas()) {
				this.list = null;

			} else {

				if (hora == -1) {
					hora = null;
				}

				GregorianCalendar gcFechaHoraDesde = new GregorianCalendar();
				gcFechaHoraDesde.setTime(fechaHoraDesde);
				XMLGregorianCalendar xmlGCFechaHoraDesde = DatatypeFactory.newInstance()
						.newXMLGregorianCalendar(gcFechaHoraDesde);

				GregorianCalendar gcFechaHoraHasta = new GregorianCalendar();
				gcFechaHoraHasta.setTime(fechaHoraHasta);
				XMLGregorianCalendar xmlGCFechaHoraHasta = DatatypeFactory.newInstance()
						.newXMLGregorianCalendar(gcFechaHoraHasta);

				ArrayOfDatosMAS i = GestionServiceHelper.getGestionService(CfgTimeout.REPORTE).mostrarReporteMAS(
						this.getUsuario().getIdMayorista(), nombreF, idClienteF, razSocClienteF, xmlGCFechaHoraDesde,
						xmlGCFechaHoraHasta, hora);

				if (i != null) {
					this.list = i.getDatosMAS();
					if (this.list == null) {
						FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
								"No se ha encontrado ningun registro con ese criterio.", null));

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
							sb.append((char) 34).append("Informe de Maquinas AutoServicio (").append(this.cantRegistros)
									.append(" Registros)").append((char) 34).append(csvSepCamp);
							sb.append((char) 13).append((char) 10);

							sb.append((char) 34).append("Fecha").append((char) 34).append(csvSepCamp);
							sb.append((char) 34).append("Nombre").append((char) 34).append(csvSepCamp);
							sb.append((char) 34).append("ID Cliente").append((char) 34).append(csvSepCamp);
							sb.append((char) 34).append("Cliente").append((char) 34).append(csvSepCamp);
							sb.append((char) 34).append("Fecha Extraccion Billetero").append((char) 34)
									.append(csvSepCamp);
							sb.append((char) 34).append("Cant Billetes Total").append((char) 34).append(csvSepCamp);
							sb.append((char) 34).append("Porcentaje Uso Billetero").append((char) 34)
									.append(csvSepCamp);
							sb.append((char) 34).append("Importe Total").append((char) 34).append(csvSepCamp);
							sb.append((char) 34).append("Importe Ventas").append((char) 34).append(csvSepCamp);
							sb.append((char) 34).append("Importe Depositos").append((char) 34).append(csvSepCamp);
							sb.append((char) 34).append("Cant Trxs Aprobadas").append((char) 34).append(csvSepCamp);
							sb.append((char) 34).append("Cant Trxs Rechazadas").append((char) 34).append(csvSepCamp);
							sb.append((char) 34).append("Cant Trxs Proceso").append((char) 34).append(csvSepCamp);
							sb.append((char) 34).append("Cant Trxs con Resp Null").append((char) 34).append(csvSepCamp);
							sb.append((char) 34).append("Cant Depositos Aprobados").append((char) 34)
									.append(csvSepCamp);
							sb.append((char) 34).append("Cant Depositos Rechazados").append((char) 34)
									.append(csvSepCamp);
							sb.append((char) 34).append("Comentario").append((char) 34).append(csvSepCamp);
							sb.append((char) 34).append("Fecha Inicio Aplicacion").append((char) 34).append(csvSepCamp);
							sb.append((char) 34).append("Error Grave").append((char) 34).append(csvSepCamp);
							sb.append((char) 34).append("Aux1").append((char) 34).append(csvSepCamp);
							sb.append((char) 34).append("Aux2").append((char) 34).append(csvSepCamp);
							sb.append((char) 34).append("Aux3").append((char) 34).append(csvSepCamp);

							sb.append((char) 13).append((char) 10);

						}

						// RECORRO LA LISTA QUE HACER LAS SUMATORIAS Y PARA GENERAR EL REPORTE A
						// EXPORTAR EN CASO DE SER NECESARIO
						for (DatosMAS it : list) {

							// Sumatoria de totales (Solo si selecciono una hora en particular)
							if (hora != null) {
								if ((hora >= 0) && (hora <= 23)) {

									// Sumo los importes repartidos
									sumCantBilletes += it.getCantBilletesTotal().getValue();
									sumImporteTotal += it.getImporteTotal().getValue();
									sumImporteVentas += it.getImporteVentas().getValue();
									sumImporteDepositos += it.getImporteDepositos().getValue();

								}
							}

							// CREO ARCHIVO DE EXPORTACION SI ES NECESARIO PARA EXPORTAR A CSV
							if (this.exportToExcel) {

								if (!mostrarArchivoCSV) {
									mostrarArchivoCSV = true;
								}

								sb.append((char) 34)
										.append(ff.format(it.getFecha().toGregorianCalendar().getTime()).toString())
										.append((char) 34).append(csvSepCamp);
								sb.append((char) 34).append(it.getNombre().getValue()).append((char) 34)
										.append(csvSepCamp);
								sb.append((char) 34).append(it.getIdCliente().getValue()).append((char) 34)
										.append(csvSepCamp);
								sb.append((char) 34).append(it.getRazonSocialCliente().getValue()).append((char) 34)
										.append(csvSepCamp);
								sb.append((char) 34)
										.append(ff.format(
												it.getFechaUltimoCambioBilletero().toGregorianCalendar().getTime())
												.toString())
										.append((char) 34).append(csvSepCamp);
								sb.append((char) 34).append(it.getCantBilletesTotal().getValue()).append((char) 34)
										.append(csvSepCamp);
								sb.append((char) 34).append(
										ACGFormatHelper.format(it.getPorcentajeUsoBilletero().getValue(), csvSepDec))
										.append((char) 34).append(csvSepCamp);
								sb.append((char) 34).append("$")
										.append(ACGFormatHelper.format(it.getImporteTotal().getValue(), csvSepDec))
										.append((char) 34).append(csvSepCamp);
								sb.append((char) 34).append("$")
										.append(ACGFormatHelper.format(it.getImporteVentas().getValue(), csvSepDec))
										.append((char) 34).append(csvSepCamp);
								sb.append((char) 34).append("$")
										.append(ACGFormatHelper.format(it.getImporteDepositos().getValue(), csvSepDec))
										.append((char) 34).append(csvSepCamp);

								sb.append((char) 34).append(it.getCantTrxsAprobadas().getValue()).append((char) 34)
										.append(csvSepCamp);
								sb.append((char) 34).append(it.getCantTrxsRechazadas().getValue()).append((char) 34)
										.append(csvSepCamp);
								sb.append((char) 34).append(it.getCantTrxsProceso().getValue()).append((char) 34)
										.append(csvSepCamp);
								sb.append((char) 34).append(it.getCantTrxsRespNull().getValue()).append((char) 34)
										.append(csvSepCamp);
								sb.append((char) 34).append(it.getCantDepositosAprobados().getValue()).append((char) 34)
										.append(csvSepCamp);
								sb.append((char) 34).append(it.getCantDepositosRechazados().getValue())
										.append((char) 34).append(csvSepCamp);
								sb.append((char) 34).append(it.getComentario().getValue()).append((char) 34)
										.append(csvSepCamp);
								sb.append((char) 34)
										.append(ff.format(it.getFechaInicioAplicacion().toGregorianCalendar().getTime())
												.toString())
										.append((char) 34).append(csvSepCamp);
								sb.append((char) 34).append(it.getErrorGrave().getValue()).append((char) 34)
										.append(csvSepCamp);
								sb.append((char) 34).append(it.getAux1().getValue()).append((char) 34)
										.append(csvSepCamp);
								sb.append((char) 34).append(it.getAux2().getValue()).append((char) 34)
										.append(csvSepCamp);
								sb.append((char) 34).append(it.getAux3().getValue()).append((char) 34)
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
							sb.append((char) 34).append(this.sumCantBilletes).append((char) 34).append(csvSepCamp);
							sb.append((char) 34).append("").append((char) 34).append(csvSepCamp);
							sb.append((char) 34).append(this.sumImporteTotal).append((char) 34).append(csvSepCamp);
							sb.append((char) 34).append(this.sumImporteVentas).append((char) 34).append(csvSepCamp);
							sb.append((char) 34).append(this.sumImporteDepositos).append((char) 34).append(csvSepCamp);
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
											+ this.getUsuario().getIdMayorista() + ")_" + "InformeMAS.csv" + "\"");

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
					LogACGHelper.escribirLog(null,
							"Informe MAS. Error ejecutando el WS de consulta: |" + ste.getMessage() + "|");
					FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
							"Error ejecutando el WS de consulta: |" + ste.getMessage() + "|", null));
				}
			} else {
				LogACGHelper.escribirLog(null,
						"Informe MAS. Error ejecutando el WS de consulta: |" + ste.getMessage() + "|");
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
						"Error ejecutando el WS de consulta: |" + ste.getMessage() + "|", null));
			}
			FacesContext.getCurrentInstance().validationFailed();
			this.list = null;
		} catch (Exception e) {
			LogACGHelper.escribirLog(null,
					"Informe MAS. Excepcion ejecutando el WS de consulta: |" + e.getMessage() + "|");
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
					"Error ejecutando el WS de consulta de MAS: |" + e.getMessage() + "|", null));
			FacesContext.getCurrentInstance().validationFailed();
			this.list = null;
		}
		PrimeFaces.current().executeScript("PF('blockPanelGral').hide()");
		return;
	}

	public Integer getHora() {
		Integer horaFiltro = null;
		try {
			SimpleDateFormat formatoHora = new SimpleDateFormat("HH");
			String horaF = formatoHora.format(new Date()).toString();
			horaFiltro = Integer.parseInt(horaF);
		} catch (Exception e) {
			horaFiltro = 0;
		}
		hora = horaFiltro;
		return hora;
	}

	public void setHora(Integer hora) {
		this.hora = hora;
	}

	public Long getIdClienteF() {
		return idClienteF;
	}

	public void setIdClienteF(Long idClienteF) {
		this.idClienteF = idClienteF;
	}

	public String getRazSocClienteF() {
		return razSocClienteF;
	}

	public void setRazSocClienteF(String razSocClienteF) {
		this.razSocClienteF = razSocClienteF;
	}

	public String getNombreF() {
		return nombreF;
	}

	public void setNombreF(String nombreF) {
		this.nombreF = nombreF;
	}

	public Float getSumImporteTotal() {
		return sumImporteTotal;
	}

	public void setSumImporteTotal(Float sumImporteTotal) {
		this.sumImporteTotal = sumImporteTotal;
	}

	public Float getSumImporteVentas() {
		return sumImporteVentas;
	}

	public void setSumImporteVentas(Float sumImporteVentas) {
		this.sumImporteVentas = sumImporteVentas;
	}

	public Float getSumImporteDepositos() {
		return sumImporteDepositos;
	}

	public void setSumImporteDepositos(Float sumImporteDepositos) {
		this.sumImporteDepositos = sumImporteDepositos;
	}

	public Integer getSumCantBilletes() {
		return sumCantBilletes;
	}

	public void setSumCantBilletes(Integer sumCantBilletes) {
		this.sumCantBilletes = sumCantBilletes;
	}

}
