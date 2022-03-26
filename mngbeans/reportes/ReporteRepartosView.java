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

import com.americacg.cargavirtual.gestion.model.ArrayOfInformeRepartoSaldo;
import com.americacg.cargavirtual.gestion.model.Banco;
import com.americacg.cargavirtual.gestion.model.BancoContainer;
import com.americacg.cargavirtual.gestion.model.InformeRepartoSaldo;
import com.americacg.cargavirtual.gestion.model.TipoMovimiento;
import com.americacg.cargavirtual.gestion.model.TipoMovimientoContainer;
import com.americacg.cargavirtual.web.enums.CfgTimeout;
import com.americacg.cargavirtual.web.helpers.ACGFormatHelper;
import com.americacg.cargavirtual.web.helpers.GestionServiceHelper;
import com.americacg.cargavirtual.web.helpers.LogACGHelper;
import com.americacg.cargavirtual.web.model.Error;

import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;

@Named("reporteRepartosView")
@Scope(org.springframework.web.context.WebApplicationContext.SCOPE_SESSION)
public class ReporteRepartosView extends ReporteGeneral<InformeRepartoSaldo> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6990597076719044362L;
	private Float importeTotal = 0F;
	private Float incrementoNetoTotal = 0F;
	private Float comisionPlanaAdelTotal = 0F;

	private Long idTipoMovimiento;
	private List<SelectItem> tipoMovimientos;

	private Long idBanco;
	private List<SelectItem> bancos;

	private Integer tipoFiltroCliente;
	private Long idClienteFiltro;

	private Boolean infHistorico;

	private Long idRepartoSaldo;

	@Override
	public Error resetearReporte() {
		Error e = super.resetearReporte();
		this.importeTotal = 0F;
		this.incrementoNetoTotal = 0F;
		this.comisionPlanaAdelTotal = 0F;
		this.tipoMovimientos = null;
		this.idClienteFiltro = null;
		this.idBanco = null;
		this.tipoFiltroCliente = 3;
		this.idTipoMovimiento = null;
		this.cantRegistros = 0;
		this.infHistorico = null;
		this.idRepartoSaldo = null;

		try {

			this.bancos = new ArrayList<SelectItem>();
			// TODO Revisar pq se ejecuta la consulta de bancos cada vez que cambio de
			// solapa en PuntoDeVenta

			BancoContainer bc = GestionServiceHelper.getGestionService(CfgTimeout.REPORTE)
					.mostrarBanco(this.getUsuario().getIdMayorista(), null, null, false);

			if ((bc != null) && (bc.getListBanco() != null)) {
				if (bc.getListBanco().getValue() != null) {
					if (bc.getListBanco().getValue().getBanco().size() > 0) {
						for (Banco b : bc.getListBanco().getValue().getBanco()) {
							this.bancos.add(new SelectItem(b.getIdBanco().getValue(),
									b.getNombreBanco().getValue() + "|" + b.getSucursal().getValue() + "|"
											+ b.getCuenta().getValue() + "|" + b.getCBU().getValue() + "|"
											+ b.getDescripcion().getValue() + "|"));
						}
					}
				}
			}

			this.tipoMovimientos = new ArrayList<SelectItem>();
			// TODO Revisar pq se ejecuta la consulta de tipo de Movimientos cada vez que
			// cambio de solapa en PuntoDeVenta

			TipoMovimientoContainer tm = GestionServiceHelper.getGestionService(CfgTimeout.REPORTE)
					.mostrarTipoMovimiento(this.getUsuario().getIdMayorista(), null);
			for (TipoMovimiento t : tm.getListTipoMovimiento().getValue().getTipoMovimiento()) {
				this.tipoMovimientos
						.add(new SelectItem(t.getIdTipoMovimiento().getValue(), t.getDescripcion().getValue()));
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
							"Informe Repartos. Error ejecutando el WS de consulta: |" + ste.getMessage() + "|");
					e.setError("GST-OTR",
							"No se pudo establecer la comunicación (GST-OTR).\n Por favor intente nuevamente.");
				}
			} else {
				LogACGHelper.escribirLog(null,
						"Informe Repartos. Error ejecutando el WS de consulta: |" + ste.getMessage() + "|");
				e.setError("GST-OTR",
						"No se pudo establecer la comunicación (GST-OTR).\n Por favor intente nuevamente.");
			}
			FacesContext.getCurrentInstance().validationFailed();
		} catch (Exception ex) {
			LogACGHelper.escribirLog(null,
					"Informe Repartos. Excepcion ejecutando el WS de consulta: |" + ex.getMessage() + "|");
			e.setError("GST-OTR", "No se pudo establecer la comunicación (GST-OTR).\n Por favor intente nuevamente.");
		}
		return e;
	}

	public ReporteRepartosView() {
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
			this.mostrarRegistros = true;

			// Limpio la tabla
			list = null;

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

			importeTotal = 0F;
			incrementoNetoTotal = 0F;
			comisionPlanaAdelTotal = 0F;

			// Long id = idClienteFiltro != null? idClienteFiltro : idClienteSeleccionado;

			// Long id = idClienteFiltro != null? idClienteFiltro : null;

			// TODO validar filtros...
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

				// Indica la cantidad de registros que voy a usar en el limit de la query a la
				// base de datos
				Integer cantRegistrosFiltro = null;
				if (exportToExcel) {
					cantRegistrosFiltro = cantMaxRegistrosAexportar;
				} else {
					cantRegistrosFiltro = cantMaxRegistrosAmostrarPorPantalla;
				}

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

				ArrayOfInformeRepartoSaldo aoInfRepSal = GestionServiceHelper.getGestionService(CfgTimeout.REPORTE)
						.informeRepartoSaldo(this.getUsuario().getIdMayorista(), idClienteFiltro,
								this.getUsuario().getNivelDistribuidorSuperior(), xmlGCFechaHoraDesde,
								xmlGCFechaHoraHasta, this.getUsuario().getIdCliente(), idRepartoSaldo, idTipoMovimiento,
								idBanco, tipoFiltroCliente, cantRegistrosFiltro, infHistorico);

				list = aoInfRepSal.getInformeRepartoSaldo();
				// GENERO Y LIMPIO LAS VARIABLES PARA LA EXPORTACION
				mostrarArchivoCSV = false;

				String csvSepCamp = this.getUsuario().getCsvSeparadorCampo();
				String csvSepDec = this.getUsuario().getCsvSeparadorDecimales();

				StringBuilder sb = new StringBuilder();

				if (list == null || list.isEmpty()) {
					FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
							"No existe información para la consulta realizada.", null));
				} else {

					// CREO HEADER DE EXPORTACION SI ES NECESARIO PARA EXPORTAR A CSV
					if (this.exportToExcel) {
						// Header
						sb.append((char) 34).append("Id").append((char) 34).append(csvSepCamp);
						sb.append((char) 34).append("Fecha").append((char) 34).append(csvSepCamp);
						sb.append((char) 34).append("ID Distribuidor").append((char) 34).append(csvSepCamp);
						sb.append((char) 34).append("Distribuidor").append((char) 34).append(csvSepCamp);
						sb.append((char) 34).append("ID Cliente").append((char) 34).append(csvSepCamp);
						sb.append((char) 34).append("Cliente").append((char) 34).append(csvSepCamp);
						sb.append((char) 34).append("ID Usuario").append((char) 34).append(csvSepCamp);
						sb.append((char) 34).append("Usuario").append((char) 34).append(csvSepCamp);
						sb.append((char) 34).append("ID Tipo Mov.").append((char) 34).append(csvSepCamp);
						sb.append((char) 34).append("Tipo Mov.").append((char) 34).append(csvSepCamp);
						sb.append((char) 34).append("Credito Inicial").append((char) 34).append(csvSepCamp);
						sb.append((char) 34).append("Incremento").append((char) 34).append(csvSepCamp);

						if (this.getUsuario().getPermitirComisionPlanaAdelantada() == 1) {
							sb.append((char) 34).append("Incremento Neto").append((char) 34).append(csvSepCamp);
							sb.append((char) 34).append("Comision Plana Adelantada").append((char) 34)
									.append(csvSepCamp);
						}

						sb.append((char) 34).append("Credito Final").append((char) 34).append(csvSepCamp);
						sb.append((char) 34).append("Tipo Carga").append((char) 34).append(csvSepCamp); // Mostrar
																										// tambien
																										// idDepAdelanto
																										// en este campo
						sb.append((char) 34).append("Coment.").append((char) 34);

						sb.append((char) 13).append((char) 10);
					}

					// DEFINO FORMATO DE FECHA PARA MOSTRAR EN EL REPORTE QUE SE EXPORTA
					SimpleDateFormat ff = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

					// RECORRO LA LISTA QUE HACER LAS SUMATORIAS Y PARA GENERAR EL REPORTE A
					// EXPORTAR EN CASO DE SER NECESARIO
					Integer i = 0;
					for (InformeRepartoSaldo ir : list) {

						cantRegistros = list.size();
						// Sumo los importes repartidos
						importeTotal += ir.getIncremento().getValue();
						incrementoNetoTotal += ir.getIncrementoNeto().getValue();
						comisionPlanaAdelTotal += ir.getComisionPlanaAdel().getValue();

						// CREO ARCHIVO DE EXPORTACION SI ES NECESARIO PARA EXPORTAR A CSV
						if (this.exportToExcel) {

							if (i == 0) {
								mostrarArchivoCSV = true;
							}

							sb.append((char) 34).append(ir.getIdRepartoSaldo().getValue()).append((char) 34)
									.append(csvSepCamp);
							sb.append((char) 34)
									.append(ff.format(ir.getFecha().toGregorianCalendar().getTime()).toString())
									.append((char) 34).append(csvSepCamp);

							sb.append((char) 34).append(ir.getIdClienteDist().getValue()).append((char) 34)
									.append(csvSepCamp);
							sb.append((char) 34).append(ir.getRazonSocialDist().getValue()).append((char) 34)
									.append(csvSepCamp);
							sb.append((char) 34).append(ir.getIdCliente().getValue()).append((char) 34)
									.append(csvSepCamp);
							sb.append((char) 34).append(ir.getRazonSocial().getValue()).append((char) 34)
									.append(csvSepCamp);
							sb.append((char) 34).append(ir.getIdUsuario().getValue()).append((char) 34)
									.append(csvSepCamp);
							sb.append((char) 34).append(ir.getUsuario().getValue()).append((char) 34)
									.append(csvSepCamp);
							sb.append((char) 34).append(ir.getIdTipoMovimiento().getValue()).append((char) 34)
									.append(csvSepCamp);
							sb.append((char) 34).append(ir.getDescTipoMovimiento().getValue()).append((char) 34)
									.append(csvSepCamp);
							
							sb.append((char) 34).append("$")
							.append(ACGFormatHelper.format(ir.getCreditoInicial().getValue(), csvSepDec))
							.append((char) 34).append(csvSepCamp);

							sb.append((char) 34).append("$")
							.append(ACGFormatHelper.format(ir.getIncremento().getValue(), csvSepDec))
							.append((char) 34).append(csvSepCamp);

							if (this.getUsuario().getPermitirComisionPlanaAdelantada() == 1) {
								sb.append((char) 34).append("$")
								.append(ACGFormatHelper.format(ir.getIncrementoNeto().getValue(), csvSepDec))
								.append((char) 34).append(csvSepCamp);
								
								sb.append((char) 34).append("$")
								.append(ACGFormatHelper.format(ir.getComisionPlanaAdel().getValue(), csvSepDec))
								.append((char) 34).append(csvSepCamp);
								
							}

							sb.append((char) 34).append("$")
							.append(ACGFormatHelper.format(ir.getCreditoFinal().getValue(), csvSepDec))
							.append((char) 34).append(csvSepCamp);
							
							String idDepAdelanto = "";
							if (ir.getIdDepAdelanto().getValue() != null) {
								idDepAdelanto = " (" + ir.getIdDepAdelanto().getValue() + ")";
							}
							sb.append((char) 34).append(ir.getTipoCarga().getValue()).append(idDepAdelanto)
									.append((char) 34).append(csvSepCamp);

							sb.append((char) 34).append(ir.getComentario().getValue()).append((char) 34);
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
					LogACGHelper.escribirLog(null, "Informe Repartos. Error ejecutando el WS de consulta: |" + ste.getMessage() + "|");
					FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error ejecutando el WS de consulta: |" + ste.getMessage() + "|", null));
				}
			}else {
				LogACGHelper.escribirLog(null, "Informe Repartos. Error ejecutando el WS de consulta: |" + ste.getMessage() + "|");				
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error ejecutando el WS de consulta: |" + ste.getMessage() + "|", null));
			}
			FacesContext.getCurrentInstance().validationFailed();
			this.list = null;
		} catch (Exception e) {
			LogACGHelper.escribirLog(null, "Informe Repartos. Excepcion ejecutando el WS de consulta: |" + e.getMessage() + "|");	
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, 
					"Error ejecutando el WS de consulta de Repartos: |" + e.getMessage() + "|", null));
			FacesContext.getCurrentInstance().validationFailed();
			this.list = null;
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

	public Long getIdTipoMovimiento() {
		return idTipoMovimiento;
	}

	public void setIdTipoMovimiento(Long idTipoMovimiento) {
		this.idTipoMovimiento = idTipoMovimiento;
	}

	public List<SelectItem> getTipoMovimientos() {
		return tipoMovimientos;
	}

	public void setTipoMovimientos(List<SelectItem> tipoMovimientos) {
		this.tipoMovimientos = tipoMovimientos;
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

	public Integer getTipoFiltroCliente() {
		return tipoFiltroCliente;
	}

	public void setTipoFiltroCliente(Integer tipoFiltroCliente) {
		this.tipoFiltroCliente = tipoFiltroCliente;
	}

	public Float getIncrementoNetoTotal() {
		return incrementoNetoTotal;
	}

	public void setIncrementoNetoTotal(Float incrementoNetoTotal) {
		this.incrementoNetoTotal = incrementoNetoTotal;
	}

	public Float getComisionPlanaAdelTotal() {
		return comisionPlanaAdelTotal;
	}

	public void setComisionPlanaAdelTotal(Float comisionPlanaAdelTotal) {
		this.comisionPlanaAdelTotal = comisionPlanaAdelTotal;
	}

	public Boolean getInfHistorico() {
		return infHistorico;
	}

	public void setInfHistorico(Boolean infHistorico) {
		this.infHistorico = infHistorico;
	}

	public Long getIdRepartoSaldo() {
		return idRepartoSaldo;
	}

	public void setIdRepartoSaldo(Long idRepartoSaldo) {
		this.idRepartoSaldo = idRepartoSaldo;
	}
}
