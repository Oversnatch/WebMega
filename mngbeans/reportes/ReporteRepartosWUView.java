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
import javax.inject.Named;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import javax.xml.ws.WebServiceException;

import org.primefaces.PrimeFaces;
import org.springframework.context.annotation.Scope;

import com.americacg.cargavirtual.gestion.model.ArrayOfWUInformeRepartoSaldo;
import com.americacg.cargavirtual.gestion.model.WUInformeRepartoSaldo;
import com.americacg.cargavirtual.gestion.model.WUTiporeparto;
import com.americacg.cargavirtual.gestion.model.WUTiporepartoContainer;
import com.americacg.cargavirtual.web.enums.CfgTimeout;
import com.americacg.cargavirtual.web.helpers.ACGFormatHelper;
import com.americacg.cargavirtual.web.helpers.GestionServiceHelper;
import com.americacg.cargavirtual.web.helpers.LogACGHelper;
import com.americacg.cargavirtual.web.model.Error;

import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;

@Named("reporteRepartosWUView")
@Scope(org.springframework.web.context.WebApplicationContext.SCOPE_SESSION)
public class ReporteRepartosWUView extends ReporteGeneral<WUInformeRepartoSaldo> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2890971201769752280L;
	private Long idRepartoSaldo_wu;
	private Float importeTotal = 0F;
	private Long id_tipoReparto_wu;
	private List<SelectItem> wu_tiporepartos;

	private Integer tipoFiltroCliente;
	private Long idClienteFiltro;

	@Override
	public Error resetearReporte() {
		Error e = super.resetearReporte();
		this.idRepartoSaldo_wu = null;
		this.importeTotal = 0F;
		this.wu_tiporepartos = null;
		this.idClienteFiltro = null;
		this.tipoFiltroCliente = 3;
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
							"Informe Repartos WU. Error ejecutando el WS de consulta: |" + ste.getMessage() + "|");
					e.setError("GST-OTR",
							"No se pudo establecer la comunicación (GST-OTR).\n Por favor intente nuevamente.");
				}
			} else {
				LogACGHelper.escribirLog(null,
						"Informe Repartos WU. Error ejecutando el WS de consulta: |" + ste.getMessage() + "|");
				e.setError("GST-OTR",
						"No se pudo establecer la comunicación (GST-OTR).\n Por favor intente nuevamente.");
			}
			FacesContext.getCurrentInstance().validationFailed();
		} catch (Exception ex) {
			LogACGHelper.escribirLog(null,
					"Informe Repartos WU. Excepcion ejecutando el WS de consulta: |" + ex.getMessage() + "|");
			e.setError("GST-OTR", "No se pudo establecer la comunicación (GST-OTR).\n Por favor intente nuevamente.");
		}
		return e;
	}

	public ReporteRepartosWUView() {
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

			this.cantRegistros = 0;
			this.list = null;
			this.mostrarRegistros = true;
			this.importeTotal = 0F;

			if (!validaFechas()) {
				return;
			}

			if (this.getUsuario().getTipoCliente() != null) {
				if ("P".equals(this.getUsuario().getTipoCliente())) {
					// Para el caso del Punto de venta donde no tengo filtro de cliente
					// System.out.println("Inicializo tipoFiltroCliente porque estoy en punto de
					// venta");
					tipoFiltroCliente = 1;
					idClienteFiltro = this.getUsuario().getIdCliente();
				}
			} else {
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
						"El tipo de Cliente no es valido para la consulta", null));
				return;
			}

			

			Boolean filtroOK = false;
			if (tipoFiltroCliente == 1) { // Solo cliente
				if (idClienteFiltro != null) {
					filtroOK = true;
				} else {
					FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
							"El idCliente en el filtro no puede ser vacio", null));
				}

			} else {
				if (tipoFiltroCliente == 2) { // Todos los subclientes de
					if (idClienteFiltro != null) {
						filtroOK = true;
					} else {
						FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
								"El idCliente en el filtro no puede ser vacio", null));
					}
				} else {
					if (tipoFiltroCliente == 3) { // Todos los clientes que pertenecen al cliente logueado
						if (idClienteFiltro == null) {
							filtroOK = true;
						} else {
							FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(
									FacesMessage.SEVERITY_ERROR, "El idCliente en el filtro debe ser vacio", null));
						}
					} else {
						// tipo de Filtro Incorrecto
						FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
								"El tipo de filtro seleccionado es incorrecto", null));
					}
				}
			}

			if (filtroOK) {

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

				ArrayOfWUInformeRepartoSaldo oWuInf = GestionServiceHelper.getGestionService(CfgTimeout.REPORTE)
						.wuInformeRepartoSaldo(this.getUsuario().getIdMayorista(), idClienteFiltro,
								this.getUsuario().getNivelDistribuidorSuperior(), xmlGCFechaHoraDesde,
								xmlGCFechaHoraHasta, this.getUsuario().getIdCliente(), idRepartoSaldo_wu,
								id_tipoReparto_wu, tipoFiltroCliente, null);

				this.list = oWuInf.getWUInformeRepartoSaldo();

				if (list == null || list.isEmpty()) {
					FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
							"No existe información para la consulta realizada.", null));
				} else {
					for (WUInformeRepartoSaldo ir : list) {
						cantRegistros = list.size();
						// Sumo los importes repartidos
						importeTotal += ir.getIncremento().getValue();
					}

					// CREO HEADER DE EXPORTACION SI ES NECESARIO PARA EXPORTAR A CSV
					if (this.exportToExcel) {

						mostrarArchivoCSV = false;

						String csvSepCamp = this.getUsuario().getCsvSeparadorCampo();
						String csvSepDec = this.getUsuario().getCsvSeparadorDecimales();

						StringBuilder sb = new StringBuilder();

						sb.append((char) 34).append("Informe de Reparto de Saldo WU (").append(cantRegistros)
								.append(" Registros)").append((char) 34);
						sb.append((char) 13).append((char) 10);

						sb.append((char) 34).append("Id").append((char) 34).append(csvSepCamp);
						sb.append((char) 34).append("Fecha").append((char) 34).append(csvSepCamp);
						sb.append((char) 34).append("ID Distribuidor").append((char) 34).append(csvSepCamp);
						sb.append((char) 34).append("Distribuidor").append((char) 34).append(csvSepCamp);
						sb.append((char) 34).append("ID Cliente").append((char) 34).append(csvSepCamp);
						sb.append((char) 34).append("Cliente").append((char) 34).append(csvSepCamp);
						sb.append((char) 34).append("ID Usuario").append((char) 34).append(csvSepCamp);
						sb.append((char) 34).append("Usuario").append((char) 34).append(csvSepCamp);
						sb.append((char) 34).append("ID Tipo Rep WU").append((char) 34).append(csvSepCamp);
						sb.append((char) 34).append("Tipo Rep WU").append((char) 34).append(csvSepCamp);
						sb.append((char) 34).append("Credito Inicial").append((char) 34).append(csvSepCamp);
						sb.append((char) 34).append("Incremento").append((char) 34).append(csvSepCamp);
						sb.append((char) 34).append("Credito Final").append((char) 34).append(csvSepCamp);
						sb.append((char) 34).append("Comentario").append((char) 34).append(csvSepCamp);
						sb.append((char) 13).append((char) 10);

						// DEFINO FORMATO DE FECHA PARA MOSTRAR EN EL REPORTE QUE SE EXPORTA
						SimpleDateFormat ff = new SimpleDateFormat("dd/MM/yyyy HH:mm");

						// RECORRO LA LISTA QUE HACER LAS SUMATORIAS Y PARA GENERAR EL REPORTE A
						// EXPORTAR EN CASO DE SER NECESARIO
						if (list != null) {
							for (WUInformeRepartoSaldo ic : list) {

								sb.append((char) 34).append(ic.getIdRepartoSaldoWu().getValue()).append((char) 34)
										.append(csvSepCamp);
								sb.append((char) 34)
										.append(ff.format(ic.getFecha().toGregorianCalendar().getTime()).toString())
										.append((char) 34).append(csvSepCamp);
								sb.append((char) 34).append(ic.getIdClienteDist().getValue()).append((char) 34)
										.append(csvSepCamp);
								sb.append((char) 34).append(ic.getRazonSocialDist().getValue()).append((char) 34)
										.append(csvSepCamp);
								sb.append((char) 34).append(ic.getIdCliente().getValue()).append((char) 34)
										.append(csvSepCamp);
								sb.append((char) 34).append(ic.getRazonSocial().getValue()).append((char) 34)
										.append(csvSepCamp);
								sb.append((char) 34).append(ic.getIdUsuario().getValue()).append((char) 34)
										.append(csvSepCamp);
								sb.append((char) 34).append(ic.getUsuario().getValue()).append((char) 34)
										.append(csvSepCamp);

								sb.append((char) 34).append(ic.getIdTipoRepartoWu().getValue()).append((char) 34)
										.append(csvSepCamp);
								sb.append((char) 34).append(ic.getDescTipoWUrepartoSaldo().getValue()).append((char) 34)
										.append(csvSepCamp);
								sb.append((char) 34).append("$")
								.append(ACGFormatHelper.format(ic.getCreditoInicialWu().getValue(), csvSepDec))
									.append((char) 34).append(csvSepCamp);
								sb.append((char) 34).append("$")
								.append(ACGFormatHelper.format(ic.getIncremento().getValue(), csvSepDec))
									.append((char) 34).append(csvSepCamp);
								sb.append((char) 34).append("$")
								.append(ACGFormatHelper.format(ic.getCreditoFinalWu().getValue(), csvSepDec))
									.append((char) 34).append(csvSepCamp);
								sb.append((char) 34).append(ic.getComentario().getValue()).append((char) 34)
										.append(csvSepCamp);
								sb.append((char) 13).append((char) 10);
							}
						}

						sb.append((char) 34).append("-").append((char) 34).append(csvSepCamp);
						sb.append((char) 34).append("-").append((char) 34).append(csvSepCamp);
						sb.append((char) 34).append("-").append((char) 34).append(csvSepCamp);
						sb.append((char) 34).append("-").append((char) 34).append(csvSepCamp);
						sb.append((char) 34).append("-").append((char) 34).append(csvSepCamp);
						sb.append((char) 34).append("-").append((char) 34).append(csvSepCamp);
						sb.append((char) 34).append("-").append((char) 34).append(csvSepCamp);
						sb.append((char) 34).append("-").append((char) 34).append(csvSepCamp);
						sb.append((char) 34).append("-").append((char) 34).append(csvSepCamp);
						sb.append((char) 34).append("-").append((char) 34).append(csvSepCamp);
						sb.append((char) 34).append("-").append((char) 34).append(csvSepCamp);
						sb.append((char) 34).append("$")
						.append(ACGFormatHelper.format(importeTotal, csvSepDec))
								.append((char) 34).append(csvSepCamp);
						sb.append((char) 34).append("-").append((char) 34).append(csvSepCamp);
						sb.append((char) 34).append("-").append((char) 34).append(csvSepCamp);
						sb.append((char) 13).append((char) 10);

						FacesContext fc = FacesContext.getCurrentInstance();
						ExternalContext ec = fc.getExternalContext();

						ec.responseReset();
						ec.setResponseContentType("text/plain");
						ec.setResponseContentLength(sb.toString().length());
						SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd_HHmmss");
						ec.setResponseHeader("Content-Disposition", "attachment; filename=\"" + sdf.format(new Date())
								+ "_(" + this.getUsuario().getIdMayorista() + ")_" + "InformeRepartos.csv" + "\"");

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
							"Informe Reparto WU. Error ejecutando el WS de consulta: |" + ste.getMessage() + "|");
					FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
							"Error ejecutando el WS de consulta: |" + ste.getMessage() + "|", null));
				}
			} else {
				LogACGHelper.escribirLog(null,
						"Informe Reparto WU. Error ejecutando el WS de consulta: |" + ste.getMessage() + "|");
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
						"Error ejecutando el WS de consulta: |" + ste.getMessage() + "|", null));
			}
			FacesContext.getCurrentInstance().validationFailed();
		} catch (Exception e) {
			LogACGHelper.escribirLog(null,
					"Informe WU Reparto. Excepcion ejecutando el WS de consulta: |" + e.getMessage() + "|");
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
					"Error ejecutando el WS de Informe WU Reparto: |" + e.getMessage() + "|", null));
			FacesContext.getCurrentInstance().validationFailed();

		}
		PrimeFaces.current().executeScript("PF('blockPanelGral').hide()");
		return;
	}

	public Float getImporteTotal() {
		return importeTotal;
	}

	public void setImporteTotal(Float importeTotal) {
		this.importeTotal = importeTotal;
	}

	public Long getIdClienteFiltro() {
		return idClienteFiltro;
	}

	public void setIdClienteFiltro(Long idClienteFiltro) {
		this.idClienteFiltro = idClienteFiltro;
	}

	public Integer getTipoFiltroCliente() {
		return tipoFiltroCliente;
	}

	public void setTipoFiltroCliente(Integer tipoFiltroCliente) {
		this.tipoFiltroCliente = tipoFiltroCliente;
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

	public Long getIdRepartoSaldo_wu() {
		return idRepartoSaldo_wu;
	}

	public void setIdRepartoSaldo_wu(Long idRepartoSaldo_wu) {
		this.idRepartoSaldo_wu = idRepartoSaldo_wu;
	}
}