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

import com.americacg.cargavirtual.gestion.model.Banco;
import com.americacg.cargavirtual.gestion.model.BancoContainer;
import com.americacg.cargavirtual.gestion.model.DatosDepAdel;
import com.americacg.cargavirtual.gestion.model.DatosDepAdelContainer;
import com.americacg.cargavirtual.gestion.model.Descripcion;
import com.americacg.cargavirtual.gestion.model.DescripcionContainer;
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

@Named("reporteDepAdelView")
@Scope(org.springframework.web.context.WebApplicationContext.SCOPE_SESSION)
public class ReporteDepAdelView extends ReporteGeneral<DatosDepAdelContainer> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4549983853284991152L;

	private DatosDepAdelContainer listda;

	private Float importeTotal = 0F;

	private Long idBanco;
	private Long idTipoMovimiento;
	private Long idEstadodepyadelanto;
	private Long idCobrador;

	private List<SelectItem> bancos;
	private List<SelectItem> tipoMovimientos;
	private List<SelectItem> estadosdepyadelanto;
	private List<SelectItem> cobradores;

	private Integer tipoFiltroCliente;
	private Long idClienteFiltro;

	private Boolean agrPorCli;
	private String fechaHoraDesdeMostrar;
	private String fechaHoraHastaMostrar;

	private Boolean infHistorico;

	@Override
	public Error resetearReporte() {
		Error e = super.resetearReporte();
		listda = null;
		this.importeTotal = 0F;
		this.idBanco = null;
		this.idCobrador = null;
		this.idTipoMovimiento = null;
		this.idEstadodepyadelanto = null;

		this.tipoFiltroCliente = 3;
		this.idClienteFiltro = null;
		this.cantRegistros = 0;
		this.agrPorCli = false;
		this.fechaHoraDesdeMostrar = null;
		this.fechaHoraHastaMostrar = null;
		this.infHistorico = null;

		try {

			this.bancos = new ArrayList<SelectItem>();

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

			TipoMovimientoContainer tm = GestionServiceHelper.getGestionService(CfgTimeout.REPORTE)
					.mostrarTipoMovimiento(this.getUsuario().getIdMayorista(), null);
			for (TipoMovimiento m : tm.getListTipoMovimiento().getValue().getTipoMovimiento()) {
				this.tipoMovimientos
						.add(new SelectItem(m.getIdTipoMovimiento().getValue(), m.getDescripcion().getValue()));
			}
			this.estadosdepyadelanto = new ArrayList<SelectItem>();

			DescripcionContainer ceda = GestionServiceHelper.getGestionService(CfgTimeout.REPORTE)
					.mostrarEstadosdepyadelanto(null);
			for (Descripcion d : ceda.getListDescripcion().getValue().getDescripcion()) {
				this.estadosdepyadelanto.add(new SelectItem(d.getId().getValue(), d.getDescripcion().getValue()));
			}
			this.cobradores = new ArrayList<SelectItem>();

			DescripcionContainer dc = GestionServiceHelper.getGestionService(CfgTimeout.REPORTE)
					.mostrarCobradores(this.getUsuario().getIdMayorista(), null);
			for (Descripcion d : dc.getListDescripcion().getValue().getDescripcion()) {
				this.cobradores.add(new SelectItem(d.getId().getValue(), d.getDescripcion().getValue()));
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
							"Informe Depositos y Adelantos. Error ejecutando el WS de consulta: |" + ste.getMessage()
									+ "|");
					e.setError("GST-OTR",
							"No se pudo establecer la comunicación (GST-OTR).\n Por favor intente nuevamente.");
				}
			} else {
				LogACGHelper.escribirLog(null,
						"Informe Depositos y Adelantos. Error ejecutando el WS de consulta: |" + ste.getMessage() + "|");
				e.setError("GST-OTR",
						"No se pudo establecer la comunicación (GST-OTR).\n Por favor intente nuevamente.");
			}
			FacesContext.getCurrentInstance().validationFailed();
		} catch (Exception ex) {
			LogACGHelper.escribirLog(null,
					"Informe Depositos y Adelantos. Excepcion ejecutando el WS de consulta: |" + ex.getMessage() + "|");
			e.setError("GST-OTR", "No se pudo establecer la comunicación (GST-OTR).\n Por favor intente nuevamente.");
		}
		return e;
	}

	public ReporteDepAdelView() {
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
			this.cantRegistros = 0;

			importeTotal = 0F;

			if (!"P".equals(this.getUsuario().getTipoCliente())) {
				if (((tipoFiltroCliente == 1) || (tipoFiltroCliente == 2)) && (idClienteFiltro == null)) {
					FacesContext.getCurrentInstance().addMessage(null,
							new FacesMessage(FacesMessage.SEVERITY_ERROR, "Debe ingresar un numero de cliente", null));
					return;
				}

				if ((tipoFiltroCliente == 3) && (idClienteFiltro != null)) {
					FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
							"Para seleccionar todos los clientes debe borrar el numero de cliente en el filtro", null));
					return;
				}

				if ((tipoFiltroCliente == 4) && (!"D".equals(this.getUsuario().getTipoCliente()))) {
					FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
							"La Opcion Mis Depositos solo se encuentra disponible para los Distribuidores", null));
					return;
				}

				if ((tipoFiltroCliente == 4) && (idClienteFiltro != null)) {
					FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
							"Para ver su Depositos/Adelantos debe borrar el numero de cliente en el filtro", null));
					return;
				}

			}

			Long idCliLog = null;
			Long idCliDist = null;

			if ("P".equals(this.getUsuario().getTipoCliente())) {
				idCliLog = null;
				idCliDist = this.getUsuario().getIdCliente();
				tipoFiltroCliente = null;
				idClienteFiltro = null;
			} else {
				if ("D".equals(this.getUsuario().getTipoCliente())) {
					if ((tipoFiltroCliente == 2) || (tipoFiltroCliente == 3)) {
						idCliLog = this.getUsuario().getIdCliente();
						idCliDist = this.getUsuario().getIdDistribuidor();
					} else {
						if (tipoFiltroCliente == 1) {
							idCliLog = this.getUsuario().getIdCliente();
							idCliDist = null;
						} else {
							if (tipoFiltroCliente == 4) {
								idCliLog = null;
								idCliDist = this.getUsuario().getIdCliente();
								tipoFiltroCliente = null;
								idClienteFiltro = null;
								tipoFiltroCliente = null;
							} else {
								FacesContext.getCurrentInstance().addMessage(null,
										new FacesMessage(FacesMessage.SEVERITY_ERROR,
												"El tipoFiltroCliente no es valido para la consulta.", null));
								return;
							}
						}
					}

				} else {
					if ("M".equals(this.getUsuario().getTipoCliente())) {
						idCliLog = this.getUsuario().getIdCliente();
						idCliDist = this.getUsuario().getIdDistribuidor();
					} else {
						FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
								"El tipo de cliente no es valido para la consulta.", null));
						return;
					}
				}
			}

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

			listda = GestionServiceHelper.getGestionService(CfgTimeout.REPORTE).leerDatosDepAdel(
					this.getUsuario().getIdMayorista(), idCliLog, idCliDist,
					this.getUsuario().getNivelDistribuidorSuperior(), idTipoMovimiento, idEstadodepyadelanto, 3,
					xmlGCFechaHoraDesde, xmlGCFechaHoraHasta, idBanco, tipoFiltroCliente, idClienteFiltro, null,
					this.getUsuario().getUsername(), this.getUsuario().getPassword(), idCobrador, agrPorCli, false,
					cantRegistrosFiltro, infHistorico, false);

			// GENERO Y LIMPIO LAS VARIABLES PARA LA EXPORTACION
			// TODO resolver exportToCSV
			// this.exportToCSV.setExportText("");
			// this.exportToCSV.setFileName("");
			mostrarArchivoCSV = false;

			StringBuilder sb = new StringBuilder();

			String csvSepCamp = this.getUsuario().getCsvSeparadorCampo();
			String csvSepDec = this.getUsuario().getCsvSeparadorDecimales();

			// Armo los valores de Fecha y Hora (Desde/Hasta) para mostrar en el encabezado
			// del reporte
			String DATE_FORMAT = "dd/MM/yyyy";
			SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);
			fechaHoraDesdeMostrar = sdf.format(fechaHoraDesde).concat(" 00:00:00");
			fechaHoraHastaMostrar = sdf.format(fechaHoraHasta).concat(" 23:59:59");

			if (listda == null) {
				FacesContext.getCurrentInstance().addMessage(null,
						new FacesMessage(FacesMessage.SEVERITY_ERROR, "Por favor reintente", null));
				return;
			} else {

				if (listda.getError().getValue().getHayError().getValue()) {
					/*
					 * FacesContext.getCurrentInstance().addMessage(null, new
					 * FacesMessage(FacesMessage.SEVERITY_ERROR,
					 * "Error en la lectura de Depositos y Adelantos: |" +
					 * listda.getError().getValue().getCodigoError().getValue() + "|" +
					 * listda.getError().getValue().getMsgError().getValue() + "|", null));
					 */
					FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
							listda.getError().getValue().getMsgError().getValue(), null));

					return;
				} else if (listda.getListDatosDepAdel().getValue().getDatosDepAdel() == null
						|| listda.getListDatosDepAdel().getValue().getDatosDepAdel().isEmpty()) {
					FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
							"No existe información para la consulta realizada.", null));
					return;
				} else {
					cantRegistros = listda.getListDatosDepAdel().getValue().getDatosDepAdel().size();

					// CREO HEADER DE EXPORTACION SI ES NECESARIO PARA EXPORTAR A CSV
					if (this.exportToExcel) {

						if (!agrPorCli) {
							sb.append((char) 34).append("ID").append((char) 34).append(csvSepCamp);
							sb.append((char) 34).append("Fecha").append((char) 34).append(csvSepCamp);
						}
						sb.append((char) 34).append("ID Distribuidor").append((char) 34).append(csvSepCamp);
						sb.append((char) 34).append("Distribuidor").append((char) 34).append(csvSepCamp);
						sb.append((char) 34).append("ID Cliente").append((char) 34).append(csvSepCamp);
						sb.append((char) 34).append("Cliente").append((char) 34).append(csvSepCamp);

						if (!agrPorCli) {
							sb.append((char) 34).append("ID Usuario Que Solicito").append((char) 34).append(csvSepCamp);
							sb.append((char) 34).append("Usuario Que Solicito").append((char) 34).append(csvSepCamp);
						}

						sb.append((char) 34).append("ID Tipo Mov").append((char) 34).append(csvSepCamp);
						sb.append((char) 34).append("Tipo Mov").append((char) 34).append(csvSepCamp);

						if (!agrPorCli) {
							sb.append((char) 34).append("ID Banco").append((char) 34).append(csvSepCamp);
							sb.append((char) 34).append("Banco").append((char) 34).append(csvSepCamp);
						}

						sb.append((char) 34).append("Importe").append((char) 34).append(csvSepCamp);
						sb.append((char) 34).append("ID Estado").append((char) 34).append(csvSepCamp);
						sb.append((char) 34).append("Estado").append((char) 34).append(csvSepCamp);

						if (!agrPorCli) {
							sb.append((char) 34).append("Comentario Cli.").append((char) 34).append(csvSepCamp);
							sb.append((char) 34).append("Fecha Autoriz.").append((char) 34).append(csvSepCamp);
							sb.append((char) 34).append("ID Cliente Que Autoriza").append((char) 34).append(csvSepCamp);
							sb.append((char) 34).append("Cliente Que Autoriza").append((char) 34).append(csvSepCamp);
							sb.append((char) 34).append("ID Usuario Que Autorizo").append((char) 34).append(csvSepCamp);
							sb.append((char) 34).append("Usuario Que Autorizo").append((char) 34).append(csvSepCamp);
							sb.append((char) 34).append("Comentario Autoriz.").append((char) 34).append(csvSepCamp);
							sb.append((char) 34).append("Fecha Acredit. Pago").append((char) 34).append(csvSepCamp);
							sb.append((char) 34).append("ID Cliente Que Acredito Pago").append((char) 34)
									.append(csvSepCamp);
							sb.append((char) 34).append("Cliente Que Acredito Pago").append((char) 34)
									.append(csvSepCamp);
							sb.append((char) 34).append("ID Usuario Que Acredito Pago").append((char) 34)
									.append(csvSepCamp);
							sb.append((char) 34).append("Usuario Que Acredito Pago").append((char) 34)
									.append(csvSepCamp);
							sb.append((char) 34).append("Comentario Acredit. Pago").append((char) 34)
									.append(csvSepCamp);
							sb.append((char) 34).append("Fecha Deposito").append((char) 34).append(csvSepCamp);
							sb.append((char) 34).append("Nro Ticet/Ref").append((char) 34).append(csvSepCamp);
							sb.append((char) 34).append("Nro. Sucursal").append((char) 34).append(csvSepCamp);
							sb.append((char) 34).append("Ventanilla/Buzon?").append((char) 34).append(csvSepCamp);
							sb.append((char) 34).append("Es Tarjeta?").append((char) 34).append(csvSepCamp);
							sb.append((char) 34).append("Nro. Tarjeta").append((char) 34).append(csvSepCamp);
							sb.append((char) 34).append("Nro. Secuencia").append((char) 34).append(csvSepCamp);
							sb.append((char) 34).append("Es Cheque?").append((char) 34).append(csvSepCamp);
							sb.append((char) 34).append("Nro. Cheque").append((char) 34).append(csvSepCamp);
							sb.append((char) 34).append("ID Cobrador").append((char) 34).append(csvSepCamp);
							sb.append((char) 34).append("Cobrador").append((char) 34).append(csvSepCamp);
						}

						if (("M".equals(this.getUsuario().getTipoCliente())) && (!agrPorCli)) {
							sb.append((char) 34).append("Es Cuenta de Terceros").append((char) 34).append(csvSepCamp);
							sb.append((char) 34).append("ID Proveedor").append((char) 34).append(csvSepCamp);
							sb.append((char) 34).append("Proveedor").append((char) 34);
						}

						sb.append((char) 13).append((char) 10);
					}

					// DEFINO FORMATO DE FECHA PARA MOSTRAR EN EL REPORTE QUE SE EXPORTA
					SimpleDateFormat ff = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

					// RECORRO LA LISTA QUE HACER LAS SUMATORIAS Y PARA GENERAR EL REPORTE A
					// EXPORTAR EN CASO DE SER NECESARIO
					Integer i = 0;
					for (DatosDepAdel id : listda.getListDatosDepAdel().getValue().getDatosDepAdel()) {

						// Sumo los importes
						importeTotal += id.getSaldoPedido().getValue();

						// CREO ARCHIVO DE EXPORTACION SI ES NECESARIO PARA EXPORTAR A CSV
						if (this.exportToExcel) {

							if (i == 0) {
								mostrarArchivoCSV = true;
							}

							if (!agrPorCli) {
								sb.append((char) 34).append(id.getIdDepAdelanto().getValue()).append((char) 34)
										.append(csvSepCamp);
								sb.append((char) 34).append((id.getFechaSolicitud() == null) ? ""
										: ff.format(id.getFechaSolicitud().toGregorianCalendar().getTime()).toString())
										.append((char) 34).append(csvSepCamp);
							}

							sb.append((char) 34).append(id.getIdClienteDist().getValue()).append((char) 34)
									.append(csvSepCamp);
							sb.append((char) 34).append(id.getClienteDist().getValue()).append((char) 34)
									.append(csvSepCamp);
							if (id.getIdCliente().getValue() != null) {
								sb.append((char) 34).append(id.getIdCliente().getValue()).append((char) 34)
										.append(csvSepCamp);
							} else {
								sb.append((char) 34).append((char) 34).append(csvSepCamp);
							}
							if (id.getCliente().getValue() != null && !"".equals(id.getCliente().getValue())) {
								sb.append((char) 34).append(id.getCliente().getValue()).append((char) 34)
										.append(csvSepCamp);
							} else {
								sb.append((char) 34).append((char) 34).append(csvSepCamp);
							}

							if (!agrPorCli) {
								sb.append((char) 34).append(id.getIdUsuarioSolicito().getValue()).append((char) 34)
										.append(csvSepCamp);
								sb.append((char) 34).append(id.getUsuarioSolicito().getValue()).append((char) 34)
										.append(csvSepCamp);
							}

							sb.append((char) 34).append(id.getIdTipoMovimiento().getValue()).append((char) 34)
									.append(csvSepCamp);
							sb.append((char) 34).append(id.getTipoMovimiento().getValue()).append((char) 34)
									.append(csvSepCamp);

							if (!agrPorCli) {
								if (id.getIdBanco().getValue() != null) {
									sb.append((char) 34).append(id.getIdBanco().getValue()).append((char) 34)
											.append(csvSepCamp);
								} else {
									sb.append((char) 34).append((char) 34).append(csvSepCamp);
								}
								if (id.getBanco().getValue() != null && !"".equals(id.getBanco().getValue())) {
									sb.append((char) 34).append(id.getBanco().getValue()).append((char) 34)
											.append(csvSepCamp);
								} else {
									sb.append((char) 34).append((char) 34).append(csvSepCamp);
								}
							}

							sb.append((char) 34).append("$")
							.append(ACGFormatHelper.format(id.getSaldoPedido().getValue(), csvSepDec))
							.append((char) 34).append(csvSepCamp);
							
							sb.append((char) 34).append(id.getIdEstado().getValue()).append((char) 34)
									.append(csvSepCamp);
							sb.append((char) 34).append(id.getDescEstado().getValue()).append((char) 34)
									.append(csvSepCamp);

							if (!agrPorCli) {
								sb.append((char) 34).append(id.getComentarioCliente().getValue()).append((char) 34)
										.append(csvSepCamp);
								sb.append((char) 34)
										.append((id.getFechaAutorizacion() == null) ? ""
												: ff.format(id.getFechaAutorizacion().toGregorianCalendar().getTime())
														.toString())
										.append((char) 34).append(csvSepCamp);

								if (id.getIdClienteAutorizo().getValue() != null) {
									sb.append((char) 34).append(id.getIdClienteAutorizo().getValue()).append((char) 34)
											.append(csvSepCamp);
								} else {
									sb.append((char) 34).append((char) 34).append(csvSepCamp);
								}

								if (id.getClienteAutorizo().getValue() != null
										&& !"".equals(id.getClienteAutorizo().getValue())) {
									sb.append((char) 34).append(id.getClienteAutorizo().getValue()).append((char) 34)
											.append(csvSepCamp);
								} else {
									sb.append((char) 34).append((char) 34).append(csvSepCamp);
								}

								if (id.getIdUsuarioAutorizo().getValue() != null) {
									sb.append((char) 34).append(id.getIdUsuarioAutorizo().getValue()).append((char) 34)
											.append(csvSepCamp);
								} else {
									sb.append((char) 34).append((char) 34).append(csvSepCamp);
								}

								if (id.getUsuarioAutorizo().getValue() != null
										&& !"".equals(id.getUsuarioAutorizo().getValue())) {
									sb.append((char) 34).append(id.getUsuarioAutorizo().getValue()).append((char) 34)
											.append(csvSepCamp);
								} else {
									sb.append((char) 34).append((char) 34).append(csvSepCamp);
								}

								if (id.getComentarioAceptacion().getValue() != null
										&& !"".equals(id.getComentarioAceptacion().getValue())) {
									sb.append((char) 34).append(id.getComentarioAceptacion().getValue())
											.append((char) 34).append(csvSepCamp);
								} else {
									sb.append((char) 34).append((char) 34).append(csvSepCamp);
								}

								sb.append((char) 34)
										.append((id.getFechaAcreditPago() == null) ? ""
												: ff.format(id.getFechaAcreditPago().toGregorianCalendar().getTime())
														.toString())
										.append((char) 34).append(csvSepCamp);

								if (id.getIdClienteQueAcreditoPago().getValue() != null) {
									sb.append((char) 34).append(id.getIdClienteQueAcreditoPago().getValue())
											.append((char) 34).append(csvSepCamp);
								} else {
									sb.append((char) 34).append((char) 34).append(csvSepCamp);
								}
								if (id.getClienteQueAcreditoPago().getValue() != null
										&& !"".equals(id.getClienteQueAcreditoPago().getValue())) {
									sb.append((char) 34).append(id.getClienteQueAcreditoPago().getValue())
											.append((char) 34).append(csvSepCamp);
								} else {
									sb.append((char) 34).append((char) 34).append(csvSepCamp);
								}
								if (id.getIdUsuarioQueAcreditoPago().getValue() != null) {
									sb.append((char) 34).append(id.getIdUsuarioQueAcreditoPago().getValue())
											.append((char) 34).append(csvSepCamp);
								} else {
									sb.append((char) 34).append((char) 34).append(csvSepCamp);
								}
								if (id.getUsuarioQueAcreditoPago().getValue() != null
										&& !"".equals(id.getUsuarioQueAcreditoPago().getValue())) {
									sb.append((char) 34).append(id.getUsuarioQueAcreditoPago().getValue())
											.append((char) 34).append(csvSepCamp);
								} else {
									sb.append((char) 34).append((char) 34).append(csvSepCamp);
								}
								if (id.getComentarioAcreditPago().getValue() != null
										&& !"".equals(id.getComentarioAcreditPago().getValue())) {
									sb.append((char) 34).append(id.getComentarioAcreditPago().getValue())
											.append((char) 34).append(csvSepCamp);
								} else {
									sb.append((char) 34).append((char) 34).append(csvSepCamp);
								}
								sb.append((char) 34).append((id.getDepFechaHora() == null) ? ""
										: ff.format(id.getDepFechaHora().toGregorianCalendar().getTime()).toString())
										.append((char) 34).append(csvSepCamp);
								sb.append((char) 34).append(id.getDepNroTicketRef().getValue()).append((char) 34)
										.append(csvSepCamp);
								sb.append((char) 34).append(id.getDepNumSucursal().getValue()).append((char) 34)
										.append(csvSepCamp);

								sb.append((char) 34)
										.append((id.getIdTipoMovimiento().getValue() != null
												&& id.getIdTipoMovimiento().getValue() == 20)
														? ((id.getDepVentanillaBuzon().getValue() != null
																&& id.getDepVentanillaBuzon().getValue()) ? "Ventanilla"
																		: "Buzon")
														: "")
										.append((char) 34).append(csvSepCamp);
								sb.append((char) 34)
										.append((id.getIdTipoMovimiento().getValue() != null
												&& id.getIdTipoMovimiento().getValue() == 20)
														? ((id.getDepEsTarjeta().getValue() != null
																&& id.getDepEsTarjeta().getValue()) ? "SI" : "NO")
														: "")
										.append((char) 34).append(csvSepCamp);
								sb.append((char) 34).append(id.getDepNroTarjeta().getValue()).append((char) 34)
										.append(csvSepCamp);
								sb.append((char) 34).append(id.getDepSecuencia().getValue()).append((char) 34)
										.append(csvSepCamp);
								sb.append((char) 34)
										.append((id.getDepEsCheque().getValue() != null
												&& id.getDepEsCheque().getValue()) ? "SI" : "NO")
										.append((char) 34).append(csvSepCamp);

								if (id.getDepNroCheque().getValue() != null
										&& !"".equals(id.getDepNroCheque().getValue())) {
									sb.append((char) 34).append(id.getDepNroCheque().getValue()).append((char) 34)
											.append(csvSepCamp);
								} else {
									sb.append((char) 34).append((char) 34).append(csvSepCamp);
								}

								if (id.getIdCobrador().getValue() != null) {
									sb.append((char) 34).append(id.getIdCobrador().getValue()).append((char) 34)
											.append(csvSepCamp);
								} else {
									sb.append((char) 34).append((char) 34).append(csvSepCamp);
								}

								if (id.getNombreCobrador().getValue() != null
										&& !"".equals(id.getNombreCobrador().getValue())) {
									sb.append((char) 34).append(id.getNombreCobrador().getValue()).append((char) 34)
											.append(csvSepCamp);
								} else {
									sb.append((char) 34).append((char) 34).append(csvSepCamp);
								}

							}

							if (("M".equals(this.getUsuario().getTipoCliente())) && (!agrPorCli)) {
								sb.append((char) 34)
										.append((id.getCuentaDeTercero().getValue() != null
												&& id.getCuentaDeTercero().getValue()) ? "SI" : "NO")
										.append((char) 34).append(csvSepCamp);
								sb.append((char) 34);
								if(id.getIdProveedor() != null && id.getIdProveedor().getValue() != null ) {
									sb.append(id.getIdProveedor().getValue());
								}
								sb.append((char) 34).append(csvSepCamp);
								sb.append((char) 34);
								if(id.getRazonSocialProveedor() != null && id.getRazonSocialProveedor().getValue() != null ) {
									sb.append(id.getRazonSocialProveedor().getValue());
								}
								sb.append((char) 34);
							}

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

						sdf = new SimpleDateFormat("yyyy-MM-dd_HHmmss");
						ec.setResponseHeader("Content-Disposition", "attachment; filename=\"" + sdf.format(new Date())
								+ "_(" + this.getUsuario().getIdMayorista() + ")_" + "InformeDepAdel.csv" + "\"");

						OutputStream os = ec.getResponseOutputStream();
						OutputStreamWriter osw = new OutputStreamWriter(os);
						PrintWriter writer = new PrintWriter(osw);
						writer.write(sb.toString());
						writer.flush();
						writer.close();
						sb.setLength(0);

						fc.responseComplete();

						// Limpio la lista para evitar que se dibuje en pantalla
						listda = null;
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
					LogACGHelper.escribirLog(null, "Informe Deposito Adelanto. Error ejecutando el WS de consulta: |" + ste.getMessage() + "|");
					FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error ejecutando el WS de consulta: |" + ste.getMessage() + "|", null));
				}
			}else {
				LogACGHelper.escribirLog(null, "Informe Deposito Adelanto. Error ejecutando el WS de consulta: |" + ste.getMessage() + "|");				
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error ejecutando el WS de consulta: |" + ste.getMessage() + "|", null));
			}
			FacesContext.getCurrentInstance().validationFailed();
			this.list = null;
		} catch (Exception e) {
			LogACGHelper.escribirLog(null, "Informe Deposito Adelanto. Excepcion ejecutando el WS de consulta: |" + e.getMessage() + "|");	
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, 
					"Error ejecutando el WS de consulta de Deposito Adelanto: |" + e.getMessage() + "|", null));
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

	public Long getIdTipoMovimiento() {
		return idTipoMovimiento;
	}

	public void setIdTipoMovimiento(Long idTipoMovimiento) {
		this.idTipoMovimiento = idTipoMovimiento;
	}

	public List<SelectItem> getBancos() {
		return bancos;
	}

	public void setBancos(List<SelectItem> bancos) {
		this.bancos = bancos;
	}

	public List<SelectItem> getTipoMovimientos() {
		return tipoMovimientos;
	}

	public void setTipoMovimientos(List<SelectItem> tipoMovimientos) {
		this.tipoMovimientos = tipoMovimientos;
	}

	public DatosDepAdelContainer getListda() {
		return listda;
	}

	public void setListda(DatosDepAdelContainer listda) {
		this.listda = listda;
	}

	public Integer getTipoFiltroCliente() {
		return tipoFiltroCliente;
	}

	public void setTipoFiltroCliente(Integer tipoFiltroCliente) {
		this.tipoFiltroCliente = tipoFiltroCliente;
	}

	public Long getIdClienteFiltro() {
		return idClienteFiltro;
	}

	public void setIdClienteFiltro(Long idClienteFiltro) {
		this.idClienteFiltro = idClienteFiltro;
	}

	public Long getIdEstadodepyadelanto() {
		return idEstadodepyadelanto;
	}

	public void setIdEstadodepyadelanto(Long idEstadodepyadelanto) {
		this.idEstadodepyadelanto = idEstadodepyadelanto;
	}

	public List<SelectItem> getEstadosdepyadelanto() {
		return estadosdepyadelanto;
	}

	public void setEstadosdepyadelanto(List<SelectItem> estadosdepyadelanto) {
		this.estadosdepyadelanto = estadosdepyadelanto;
	}

	public Float getImporteTotal() {
		return importeTotal;
	}

	public void setImporteTotal(Float importeTotal) {
		this.importeTotal = importeTotal;
	}

	public Long getIdCobrador() {
		return idCobrador;
	}

	public void setIdCobrador(Long idCobrador) {
		this.idCobrador = idCobrador;
	}

	public List<SelectItem> getCobradores() {
		return cobradores;
	}

	public void setCobradores(List<SelectItem> cobradores) {
		this.cobradores = cobradores;
	}

	public Boolean getAgrPorCli() {
		return agrPorCli;
	}

	public void setAgrPorCli(Boolean agrPorCli) {
		this.agrPorCli = agrPorCli;
	}

	public String getFechaHoraDesdeMostrar() {
		return fechaHoraDesdeMostrar;
	}

	public void setFechaHoraDesdeMostrar(String fechaHoraDesdeMostrar) {
		this.fechaHoraDesdeMostrar = fechaHoraDesdeMostrar;
	}

	public String getFechaHoraHastaMostrar() {
		return fechaHoraHastaMostrar;
	}

	public void setFechaHoraHastaMostrar(String fechaHoraHastaMostrar) {
		this.fechaHoraHastaMostrar = fechaHoraHastaMostrar;
	}

	public Boolean getInfHistorico() {
		return infHistorico;
	}

	public void setInfHistorico(Boolean infHistorico) {
		this.infHistorico = infHistorico;
	}

}
