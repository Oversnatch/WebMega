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
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;

import org.primefaces.PrimeFaces;
import org.springframework.context.annotation.Scope;

import com.americacg.cargavirtual.gestion.model.ArrayOfCabeceraProducto;
import com.americacg.cargavirtual.gestion.model.ArrayOfSaldoProveedor;
import com.americacg.cargavirtual.gestion.model.CabeceraProducto;
import com.americacg.cargavirtual.gestion.model.Descripcion;
import com.americacg.cargavirtual.gestion.model.DescripcionContainer;
import com.americacg.cargavirtual.gestion.model.InformeTransacciones;
import com.americacg.cargavirtual.gestion.model.InformeTransaccionesContainer;
import com.americacg.cargavirtual.gestion.model.SaldoProveedor;
import com.americacg.cargavirtual.web.enums.CfgTimeout;
import com.americacg.cargavirtual.web.helpers.ACGFormatHelper;
import com.americacg.cargavirtual.web.helpers.GestionServiceHelper;
import com.americacg.cargavirtual.web.helpers.LogACGHelper;

import javax.inject.Named;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import javax.xml.ws.WebServiceException;

import com.americacg.cargavirtual.web.model.Error;

@Named("reporteTrxView")
@Scope(org.springframework.web.context.WebApplicationContext.SCOPE_SESSION)
public class ReporteTrxView extends ReporteGeneral<InformeTransacciones> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5922405803173586124L;
	private String estadoTransacciones;
	private Long idClienteInferior;
	private Long idTransaccion;
	private String idTransaccionCliente;
	private String idClienteExt;
	private Long idUnicoCliente;
	private Long idProducto;
	private Long idProveedor;
	private Long idUsuario;
	private String mostDatoPin;
	private String identifTerminal;
	private String imeiTerminal;
	private String prodIdentif;
	private String telefonoCompletoOTarjeta;

	private Integer tipoFiltroCliente;

	private Long telefono;
	private String tipoTRX;
	private Long idTipoTerminal;

	private Float importeTotal = 0F;
	private Integer cantAprobadas = 0;

	private List<SelectItem> productos;
	private List<SelectItem> proveedores;

	private List<SelectItem> tipoTerminales;

	private Boolean infHistorico;

	private Long sube_idDistribuidorRed;
	private List<SelectItem> listDistribuidorRed;

	@Override
	public Error resetearReporte() {
		Error e = super.resetearReporte();
		this.importeTotal = 0F;
		this.cantAprobadas = 0;
		this.idClienteInferior = null;
		this.estadoTransacciones = null;
		this.idProducto = null;
		this.idProveedor = null;
		this.idTransaccion = null;
		this.idTransaccionCliente = null;
		this.idClienteExt = null;
		this.idUnicoCliente = null;
		this.identifTerminal = null;
		this.imeiTerminal = null;
		this.prodIdentif = null;
		this.telefonoCompletoOTarjeta = null;
		this.telefono = null;
		this.tipoTRX = null;
		this.idTipoTerminal = null;
		this.tipoFiltroCliente = 3;
		this.idUsuario = null;
		this.mostDatoPin = "N";
		this.cantRegistros = 0;
		this.infHistorico = null;

		this.sube_idDistribuidorRed = null;

		try {

			this.listDistribuidorRed = new ArrayList<SelectItem>();
			DescripcionContainer cd = GestionServiceHelper.getGestionService(CfgTimeout.REPORTE)
					.mostrarSubeDistribuidorRed(this.getUsuario().getIdMayorista());

			for (Descripcion d : cd.getListDescripcion().getValue().getDescripcion()) {

				if (d.getDescripcion() != null) {
					if (d.getDescripcion().getValue().length() > 0) {
						this.listDistribuidorRed
								.add(new SelectItem(d.getId().getValue(), d.getDescripcion().getValue()));
					}
				}

			}
			this.productos = new ArrayList<SelectItem>();
			ArrayOfCabeceraProducto l = GestionServiceHelper.getGestionService(CfgTimeout.REPORTE)
					.mostrarCabeceraProductos(this.getUsuario().getIdMayorista(), null, false);
			for (CabeceraProducto cp : l.getCabeceraProducto()) {
				this.productos.add(new SelectItem(cp.getIdProducto().getValue(), cp.getDescProducto().getValue()));
			}
			this.proveedores = new ArrayList<SelectItem>();
			ArrayOfSaldoProveedor lp = GestionServiceHelper.getGestionService(CfgTimeout.REPORTE)
					.obtenerSaldoProveedores(this.getUsuario().getIdMayorista(), null, false, false);
			for (SaldoProveedor sp : lp.getSaldoProveedor()) {
				this.proveedores.add(new SelectItem(sp.getIdProveedor().getValue(), sp.getRazonSocial().getValue()));
			}
			this.tipoTerminales = new ArrayList<SelectItem>();
			DescripcionContainer tt = GestionServiceHelper.getGestionService(CfgTimeout.REPORTE)
					.mostrarTipoTerminales(this.getUsuario().getIdMayorista());

			for (Descripcion d : tt.getListDescripcion().getValue().getDescripcion()) {
				this.tipoTerminales.add(new SelectItem(d.getId().getValue(), d.getDescripcion().getValue()));
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
							"Informe Transacciones. Error ejecutando el WS de consulta: |" + ste.getMessage() + "|");
					e.setError("GST-OTR",
							"No se pudo establecer la comunicación (GST-OTR).\n Por favor intente nuevamente.");
				}
			} else {
				LogACGHelper.escribirLog(null,
						"Informe Transacciones. Error ejecutando el WS de consulta: |" + ste.getMessage() + "|");
				e.setError("GST-OTR",
						"No se pudo establecer la comunicación (GST-OTR).\n Por favor intente nuevamente.");
			}
			FacesContext.getCurrentInstance().validationFailed();
		} catch (Exception ex) {
			LogACGHelper.escribirLog(null,
					"Informe Transacciones. Excepcion ejecutando el WS de consulta: |" + ex.getMessage() + "|");
			e.setError("GST-OTR", "No se pudo establecer la comunicación (GST-OTR).\n Por favor intente nuevamente.");
		}
		return e;
	}

	public ReporteTrxView() {
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

			// Inicializo Contador
			this.importeTotal = 0F;
			this.cantRegistros = 0;
			this.cantAprobadas = 0;
			this.mostrarRegistros = true;
			// Limpio la tabla
			list = null;
			mostDatoPin = "N";
			// TODO validar filtros...
			Boolean filtroOK = false;	
			// Indica la cantidad de registros que voy a usar en el limit de la query a la
			// base de datos
			Integer cantRegistrosFiltro = null;			
			
			

			if ("".equals(idTransaccionCliente)) {
				idTransaccionCliente = null;
			}
			if ("".equals(idClienteExt)) {
				idClienteExt = null;
			}

			// Fecha Desde
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(fechaHoraDesde);
			calendar.set(Calendar.SECOND, 0);
			calendar.set(Calendar.MILLISECOND, 0);
			fechaHoraDesde = calendar.getTime();

			// Asigno 59 segundos a la fechaHoraHasta
			// Fecha Hasta
			calendar = Calendar.getInstance();
			calendar.setTime(fechaHoraHasta);
			calendar.set(Calendar.SECOND, 59);
			calendar.set(Calendar.MILLISECOND, 999);
			fechaHoraHasta = calendar.getTime();



			if (!validaFechas()) {
				return;
			}

			if (this.getUsuario().getTipoCliente() != null) {
				if ("P".equals(this.getUsuario().getTipoCliente())) {
					// Para el caso del Punto de venta donde no tengo filtro de cliente
					// System.out.println("Inicializo tipoFiltroCliente porque estoy en punto de
					// venta");
					tipoFiltroCliente = 1;
					idClienteInferior = this.getUsuario().getIdCliente();
				}
			} else {
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
						"El tipo de Cliente no es valido para la consulta", null));
				return;
			}

			if (tipoFiltroCliente == 1) { // Solo cliente
				if (idClienteInferior != null) {
					filtroOK = true;
				} else {
					FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
							"El idCliente en el filtro no puede ser vacio", null));
				}

			} else {
				if (tipoFiltroCliente == 2) { // Todos los subclientes de
					if (idClienteInferior != null) {
						filtroOK = true;
					} else {
						FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
								"El idCliente en el filtro no puede ser vacio", null));
					}
				} else {
					if (tipoFiltroCliente == 3) { // Todos los clientes que pertenecen al cliente logueado
						if (idClienteInferior == null) {
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

				if ((idTransaccion != null) || (idTransaccionCliente != null)) {
					// Si IdTrx != null -> no toma en cuenta fechas
					// pero por WS no se puede enviar un date null
					// Fecha Desde
					calendar = Calendar.getInstance();
					calendar.setTime(new Date());
					calendar.set(Calendar.HOUR_OF_DAY, 00);
					calendar.set(Calendar.MINUTE, 00);
					calendar.set(Calendar.SECOND, 0);
					calendar.set(Calendar.MILLISECOND, 0);
					this.fechaHoraDesde = calendar.getTime();

					// Fecha Hasta
					calendar = Calendar.getInstance();
					calendar.setTime(new Date());
					calendar.set(Calendar.HOUR_OF_DAY, 23);
					calendar.set(Calendar.MINUTE, 59);
					calendar.set(Calendar.SECOND, 59);
					calendar.set(Calendar.MILLISECOND, 999);
					this.fechaHoraHasta = calendar.getTime();
				}

				if (exportToExcel) {
					cantRegistrosFiltro = cantMaxRegistrosAexportar;
				} else {
					cantRegistrosFiltro = cantMaxRegistrosAmostrarPorPantalla;
				}

				/*
				 * escribirLog(null, "Informe Transacciones --> idMayorista: |" + idMayorista +
				 * "|, idCliente: |" + idCliente + "|, nivelDistribuidorSuperior: |" +
				 * nivelDistribuidorSuperior + "|, fechaHoraDesde: |" + fechaHoraDesde +
				 * "|, fechaHoraHasta: |" + fechaHoraHasta + "|, estadoTransacciones: |" +
				 * estadoTransacciones + "|, idClienteInferior: |" + idClienteInferior +
				 * "|, idTransaccion: |" + idTransaccion + "|, idTransaccionCliente: |" +
				 * idTransaccionCliente + "|, idClienteExt: |" + idClienteExt +
				 * "|, idUnicoCliente: |" + idUnicoCliente + "|, identifTerminal: |" +
				 * identifTerminal + "|, imeiTerminal: |" + imeiTerminal + "|, idProducto: |" +
				 * idProducto + "|, telefono: |" + telefono + "|, idProveedor: |" + idProveedor
				 * + "|, tipoFiltroCliente: |" + tipoFiltroCliente + "|, prodIdentif: |" +
				 * prodIdentif + "|, telefonoCompletoOTarjeta: |" + telefonoCompletoOTarjeta +
				 * "|, cantRegistrosFiltro: |" + cantRegistrosFiltro + "|");
				 */

				GregorianCalendar gcFechaHoraDesde = new GregorianCalendar();
				gcFechaHoraDesde.setTime(fechaHoraDesde);
				XMLGregorianCalendar xmlGCFechaHoraDesde = DatatypeFactory.newInstance()
						.newXMLGregorianCalendar(gcFechaHoraDesde);

				GregorianCalendar gcFechaHoraHasta = new GregorianCalendar();
				gcFechaHoraHasta.setTime(fechaHoraHasta);
				XMLGregorianCalendar xmlGCFechaHoraHasta = DatatypeFactory.newInstance()
						.newXMLGregorianCalendar(gcFechaHoraHasta);

				InformeTransaccionesContainer listaux = GestionServiceHelper.getGestionService(CfgTimeout.REPORTE)
						.informeTransacciones(this.getUsuario().getIdMayorista(), this.getUsuario().getIdCliente(),
								this.getUsuario().getNivelDistribuidorSuperior(), xmlGCFechaHoraDesde,
								xmlGCFechaHoraHasta, estadoTransacciones, idClienteInferior, idTransaccion, idProducto,
								telefono, tipoTRX, idTipoTerminal, idProveedor, tipoFiltroCliente, idUsuario,
								idTransaccionCliente, idClienteExt, idUnicoCliente, identifTerminal, prodIdentif, "",
								cantRegistrosFiltro, imeiTerminal, telefonoCompletoOTarjeta, infHistorico,
								sube_idDistribuidorRed, null);

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
				} else if (listaux.getError().getValue().getHayError().getValue()) {
					// TODO Ver porque no entra en esta condicion. No reconoce el Error
					FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
							listaux.getError().getValue().getMsgError().getValue(), null));
				} else if (listaux.getList() == null) {
					FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
							"No existe información para la consulta realizada.", null));
				} else {
					if (listaux.getList().getValue().getInformeTransacciones().isEmpty()) {
						FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
								"No existe información para la consulta realizada.", null));
					} else {
						list = listaux.getList().getValue().getInformeTransacciones();
						cantRegistros = list.size();
						Integer i = 0;

						// CREO HEADER DE EXPORTACION SI ES NECESARIO PARA EXPORTAR A CSV
						if (this.exportToExcel) {
							// Header
							sb.append((char) 34).append("Id").append((char) 34).append(csvSepCamp);
							sb.append((char) 34).append("Fecha").append((char) 34).append(csvSepCamp);
							sb.append((char) 34).append("ID Cliente").append((char) 34).append(csvSepCamp);
							sb.append((char) 34).append("Cliente").append((char) 34).append(csvSepCamp);
							sb.append((char) 34).append("Id Usuario").append((char) 34).append(csvSepCamp);
							sb.append((char) 34).append("Usuario").append((char) 34).append(csvSepCamp);

							sb.append((char) 34).append("tipoTRX").append((char) 34).append(csvSepCamp);

							if ("M".equals(this.getUsuario().getTipoCliente())) {
								sb.append((char) 34).append("ID Proveedor").append((char) 34).append(csvSepCamp);
								sb.append((char) 34).append("Proveedor").append((char) 34).append(csvSepCamp);
							}

							sb.append((char) 34).append("ID Producto").append((char) 34).append(csvSepCamp);
							sb.append((char) 34).append("Producto").append((char) 34).append(csvSepCamp);
							sb.append((char) 34).append("Tel").append((char) 34).append(csvSepCamp);

							sb.append((char) 34).append("Telefono Completo o Tarjeta").append((char) 34)
									.append(csvSepCamp);

							sb.append((char) 34).append("Importe").append((char) 34).append(csvSepCamp);
							sb.append((char) 34).append("ID Terminal").append((char) 34).append(csvSepCamp);
							sb.append((char) 34).append("Terminal").append((char) 34).append(csvSepCamp);
							sb.append((char) 34).append("ID Estado").append((char) 34).append(csvSepCamp);
							sb.append((char) 34).append("Estado").append((char) 34).append(csvSepCamp);
							sb.append((char) 34).append("Motivo").append((char) 34).append(csvSepCamp);
							sb.append((char) 34).append("ID Estado Prov").append((char) 34).append(csvSepCamp);
							sb.append((char) 34).append("Descripcion Estado Prov").append((char) 34).append(csvSepCamp);
							sb.append((char) 34).append("Id Proveedor").append((char) 34).append(csvSepCamp);
							sb.append((char) 34).append("Id Operador").append((char) 34).append(csvSepCamp);
							sb.append((char) 34).append("Id Transaccion Cliente").append((char) 34).append(csvSepCamp);

							if ("M".equals(this.getUsuario().getTipoCliente())) {
								sb.append((char) 34).append("Id Cliente Ext").append((char) 34).append(csvSepCamp);
								sb.append((char) 34).append("Id Unico Cliente").append((char) 34).append(csvSepCamp);
							}

							sb.append((char) 34).append("Identif. Terminal").append((char) 34).append(csvSepCamp);
							sb.append((char) 34).append("Imei Terminal").append((char) 34).append(csvSepCamp);
							sb.append((char) 34).append("Identif. Prod").append((char) 34).append(csvSepCamp);

							if ("M".equals(this.getUsuario().getTipoCliente())) {
								sb.append((char) 34).append("Datos Adic. Prod").append((char) 34).append(csvSepCamp);
							}

							sb.append((char) 34).append("ID Lote").append((char) 34).append(csvSepCamp);
							sb.append((char) 34).append("Modelo de Terminal").append((char) 34).append(csvSepCamp);
							sb.append((char) 34).append("IMEI del SIM").append((char) 34);

							if (this.getUsuario().getMostrarPantallasDeSube().compareTo(1) >= 0) {
								sb.append(csvSepCamp).append((char) 34).append("SUBE ID Distribuidor Red")
										.append((char) 34).append(csvSepCamp);
								sb.append((char) 34).append("SUBE Distribuidor Red").append((char) 34);
							}

							sb.append((char) 13).append((char) 10);

						}

						// DEFINO FORMATO DE FECHA PARA MOSTRAR EN EL REPORTE QUE SE EXPORTA
						SimpleDateFormat ff = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

						// RECORRO LA LISTA QUE HACER LAS SUMATORIAS Y PARA GENERAR EL REPORTE A
						// EXPORTAR EN CASO DE SER NECESARIO
						for (InformeTransacciones it : list) {
							if (i == 0) {
								if (("SI".equals(it.getMostrarDatosPin().getValue()))
										&& (("M".equals(this.getUsuario().getTipoCliente()))
												|| ("D".equals(this.getUsuario().getTipoCliente())))
										&& ("OFF".equals(it.getTipoTRX().getValue()))) {
									mostDatoPin = "S";
								}
							}

							// Sumo solo los que el estado = OK
							if ("M0000".equals(it.getIdEstado().getValue())) {
								importeTotal += it.getImporte().getValue();
								cantAprobadas++;
							}

							// CREO ARCHIVO DE EXPORTACION SI ES NECESARIO PARA EXPORTAR A CSV
							if (this.exportToExcel) {

								if (i == 0) {
									mostrarArchivoCSV = true;
								}

								sb.append((char) 34).append(it.getIdTransaccion().getValue()).append((char) 34)
										.append(csvSepCamp);
								sb.append((char) 34).append(
										ff.format(it.getFechaPeticion().toGregorianCalendar().getTime()).toString())
										.append((char) 34).append(csvSepCamp);
								sb.append((char) 34).append(it.getIdCliente().getValue()).append((char) 34)
										.append(csvSepCamp);
								sb.append((char) 34).append(it.getRazonSocial().getValue()).append((char) 34)
										.append(csvSepCamp);
								sb.append((char) 34).append(it.getIdUsuario().getValue()).append((char) 34)
										.append(csvSepCamp);
								sb.append((char) 34).append(it.getUsuario().getValue()).append((char) 34)
										.append(csvSepCamp);

								sb.append((char) 34).append(it.getTipoTRX().getValue()).append((char) 34)
										.append(csvSepCamp);

								if ("M".equals(this.getUsuario().getTipoCliente())) {
									sb.append((char) 34).append(it.getIdProveedor().getValue()).append((char) 34)
											.append(csvSepCamp);
									sb.append((char) 34).append(it.getRazSocialProveedor().getValue()).append((char) 34)
											.append(csvSepCamp);
								}
								sb.append((char) 34).append(it.getIdProducto().getValue()).append((char) 34)
										.append(csvSepCamp);
								sb.append((char) 34)
										.append(it.getDescripcionProducto().getValue() != null
												? it.getDescripcionProducto().getValue()
												: "")
										.append((char) 34).append(csvSepCamp);
								sb.append((char) 34).append(it.getTelefono().getValue()).append((char) 34)
										.append(csvSepCamp);

								sb.append((char) 34).append(it.getTelefonoCompletoOTarjeta().getValue())
										.append((char) 34).append(csvSepCamp);

								sb.append((char) 34).append("$")
									.append(ACGFormatHelper.format(it.getImporte().getValue(), csvSepDec))
									.append((char) 34).append(csvSepCamp);
								
								sb.append((char) 34).append(it.getIdTipoTerminal().getValue()).append((char) 34)
										.append(csvSepCamp);
								sb.append((char) 34)
										.append(it.getDescTipoTerminal().getValue() != null
												&& !"".equals(it.getDescTipoTerminal().getValue())
														? it.getDescTipoTerminal().getValue()
														: "")
										.append((char) 34).append(csvSepCamp);
								sb.append((char) 34).append(it.getIdEstado().getValue()).append((char) 34)
										.append(csvSepCamp);
								sb.append((char) 34).append(it.getDescripcionEstado().getValue()).append((char) 34)
										.append(csvSepCamp);
								sb.append((char) 34)
										.append(it.getDescEstadoInterno().getValue() != null
												&& !"".equals(it.getDescEstadoInterno().getValue())
														? it.getDescEstadoInterno().getValue()
														: "")
										.append((char) 34).append(csvSepCamp);
								sb.append((char) 34)
										.append(it.getIdEstadoOperador().getValue() != null
												&& !"".equals(it.getIdEstadoOperador().getValue())
														? it.getIdEstadoOperador().getValue()
														: "")
										.append((char) 34).append(csvSepCamp);

								String descEstadoOperador = "";
								if (it.getDescEstadoOperador().getValue() != null) {
									if (!"".equals(it.getDescEstadoOperador().getValue())) {
										descEstadoOperador = it.getDescEstadoOperador().getValue().replace("\r", "")
												.replace("\n", "").replace(";", "").replace(",", "").replace("#", "");
									}
								}
								sb.append((char) 34).append(descEstadoOperador).append((char) 34).append(csvSepCamp);

								sb.append((char) 34)
										.append(it.getIdTransaccionProveedor().getValue() != null
												&& !"".equals(it.getIdTransaccionProveedor().getValue())
														? it.getIdTransaccionProveedor().getValue()
														: "")
										.append((char) 34).append(csvSepCamp);
								sb.append((char) 34)
										.append(it.getIdTransaccionOperador().getValue() != null
												&& !"".equals(it.getIdTransaccionOperador().getValue())
														? it.getIdTransaccionOperador().getValue()
														: "")
										.append((char) 34).append(csvSepCamp);
								sb.append((char) 34).append(it.getIdTransaccionCliente().getValue()).append((char) 34)
										.append(csvSepCamp);

								if ("M".equals(this.getUsuario().getTipoCliente())) {
									sb.append((char) 34).append(it.getIdClienteExt().getValue()).append((char) 34)
											.append(csvSepCamp);
									sb.append((char) 34).append(it.getIdUnicoCliente().getValue()).append((char) 34)
											.append(csvSepCamp);
								}

								sb.append((char) 34).append(it.getIdentifTerminal().getValue()).append((char) 34)
										.append(csvSepCamp);

								if (it.getImeiTerminal().getValue() != null
										&& !"".equals(it.getImeiTerminal().getValue())) {
									sb.append((char) 34).append(it.getImeiTerminal().getValue()).append((char) 34);
								} else {
									sb.append((char) 34).append((char) 34);
								}
								sb.append(csvSepCamp);

								sb.append((char) 34)
										.append(it.getProdIdentif().getValue() != null
												&& !"".equals(it.getProdIdentif().getValue())
														? it.getProdIdentif().getValue()
														: "")
										.append((char) 34).append(csvSepCamp);

								if ("M".equals(this.getUsuario().getTipoCliente())) {
									sb.append((char) 34).append(it.getProdDatosAdic().getValue()).append((char) 34)
											.append(csvSepCamp);
								}

								if (it.getIdLote().getValue() != null) {
									sb.append((char) 34).append(it.getIdLote().getValue()).append((char) 34);
								}

								sb.append(csvSepCamp);

								if (it.getModeloDeTerminal().getValue() != null
										&& !"".equals(it.getModeloDeTerminal().getValue())) {
									sb.append((char) 34).append(it.getModeloDeTerminal().getValue()).append((char) 34);
								} else {
									sb.append((char) 34).append((char) 34);
								}
								sb.append(csvSepCamp);

								sb.append((char) 34).append(
										it.getImeiDeSIM().getValue() != null && !"".equals(it.getImeiDeSIM().getValue())
												? it.getImeiDeSIM().getValue()
												: "")
										.append((char) 34);

								if (this.getUsuario().getMostrarPantallasDeSube().compareTo(1) >= 0) {
									if (it.getSubeIdDistribuidorRed().getValue() != null
											&& it.getSubeIdDistribuidorRed().getValue().compareTo(0L) > 0) {
										sb.append(csvSepCamp).append((char) 34)
												.append(it.getSubeIdDistribuidorRed().getValue()).append((char) 34)
												.append(csvSepCamp);
									} else {
										sb.append(csvSepCamp).append((char) 34).append((char) 34).append(csvSepCamp);
									}
									if (it.getSubeDistribuidorRedDescripcion().getValue() != null
											&& !"".equals(it.getSubeDistribuidorRedDescripcion().getValue())) {
										sb.append((char) 34).append(it.getSubeDistribuidorRedDescripcion().getValue())
												.append((char) 34);
									} else {
										sb.append((char) 34).append((char) 34);
									}
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

							SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd_HHmmss");
							ec.setResponseHeader("Content-Disposition",
									"attachment; filename=\"" + sdf.format(new Date()) + "_("
											+ this.getUsuario().getIdMayorista() + ")_" + "InformeTransacciones.csv"
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
							"Informe Transacciones. Error ejecutando el WS de consulta: |" + ste.getMessage() + "|");
					FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
							"Error ejecutando el WS de consulta: |" + ste.getMessage() + "|", null));
				}
			} else {
				LogACGHelper.escribirLog(null,
						"Informe Transacciones. Error ejecutando el WS de consulta: |" + ste.getMessage() + "|");
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
						"Error ejecutando el WS de consulta: |" + ste.getMessage() + "|", null));
			}
			FacesContext.getCurrentInstance().validationFailed();
			this.list = null;
		} catch (Exception e) {
			LogACGHelper.escribirLog(null,
					"Informe Transacciones. Excepcion ejecutando el WS de consulta: |" + e.getMessage() + "|");
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
					"Error ejecutando el WS de Informe Transacciones: |" + e.getMessage() + "|", null));
			FacesContext.getCurrentInstance().validationFailed();
			this.list = null;
		}
		return;
	}

	public String getEstadoTransacciones() {
		return estadoTransacciones;
	}

	public void setEstadoTransacciones(String estadoTransacciones) {
		this.estadoTransacciones = estadoTransacciones;
	}

	public Long getIdClienteInferior() {
		return idClienteInferior;
	}

	public void setIdClienteInferior(Long idClienteInferior) {
		this.idClienteInferior = idClienteInferior;
	}

	public Long getIdTransaccion() {
		return idTransaccion;
	}

	public void setIdTransaccion(Long idTransaccion) {
		this.idTransaccion = idTransaccion;
	}

	public Long getIdProducto() {
		return idProducto;
	}

	public void setIdProducto(Long idProducto) {
		this.idProducto = idProducto;
	}

	public Float getImporteTotal() {
		return importeTotal;
	}

	public void setImporteTotal(Float importeTotal) {
		this.importeTotal = importeTotal;
	}

	public Long getTelefono() {
		return telefono;
	}

	public void setTelefono(Long telefono) {
		this.telefono = telefono;
	}

	public String getTipoTRX() {
		return tipoTRX;
	}

	public void setTipoTRX(String tipoTRX) {
		this.tipoTRX = tipoTRX;
	}

	public Long getIdTipoTerminal() {
		return idTipoTerminal;
	}

	public void setIdTipoTerminal(Long idTipoTerminal) {
		this.idTipoTerminal = idTipoTerminal;
	}

	public List<SelectItem> getProductos() {
		return productos;
	}

	public void setProductos(List<SelectItem> productos) {
		this.productos = productos;
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

	public Integer getTipoFiltroCliente() {
		return tipoFiltroCliente;
	}

	public void setTipoFiltroCliente(Integer tipoFiltroCliente) {
		this.tipoFiltroCliente = tipoFiltroCliente;
	}

	public Long getIdUsuario() {
		return idUsuario;
	}

	public void setIdUsuario(Long idUsuario) {
		this.idUsuario = idUsuario;
	}

	public String getMostDatoPin() {
		return mostDatoPin;
	}

	public void setMostDatoPin(String mostDatoPin) {
		this.mostDatoPin = mostDatoPin;
	}

	public List<SelectItem> getTipoTerminales() {
		return tipoTerminales;
	}

	public void setTipoTerminales(List<SelectItem> tipoTerminales) {
		this.tipoTerminales = tipoTerminales;
	}

	public String getIdTransaccionCliente() {
		return idTransaccionCliente;
	}

	public void setIdTransaccionCliente(String idTransaccionCliente) {
		this.idTransaccionCliente = idTransaccionCliente;
	}

	public String getIdClienteExt() {
		return idClienteExt;
	}

	public void setIdClienteExt(String idClienteExt) {
		this.idClienteExt = idClienteExt;
	}

	public Long getIdUnicoCliente() {
		return idUnicoCliente;
	}

	public void setIdUnicoCliente(Long idUnicoCliente) {
		this.idUnicoCliente = idUnicoCliente;
	}

	public String getIdentifTerminal() {
		return identifTerminal;
	}

	public void setIdentifTerminal(String identifTerminal) {
		this.identifTerminal = identifTerminal;
	}

	public Integer getCantAprobadas() {
		return cantAprobadas;
	}

	public void setCantAprobadas(Integer cantAprobadas) {
		this.cantAprobadas = cantAprobadas;
	}

	public String getProdIdentif() {
		return prodIdentif;
	}

	public void setProdIdentif(String prodIdentif) {
		this.prodIdentif = prodIdentif;
	}

	public String getImeiTerminal() {
		return imeiTerminal;
	}

	public void setImeiTerminal(String imeiTerminal) {
		this.imeiTerminal = imeiTerminal;
	}

	public String getTelefonoCompletoOTarjeta() {
		return telefonoCompletoOTarjeta;
	}

	public void setTelefonoCompletoOTarjeta(String telefonoCompletoOTarjeta) {
		this.telefonoCompletoOTarjeta = telefonoCompletoOTarjeta;
	}

	public Boolean getInfHistorico() {
		return infHistorico;
	}

	public void setInfHistorico(Boolean infHistorico) {
		this.infHistorico = infHistorico;
	}

	public Long getSube_idDistribuidorRed() {
		return sube_idDistribuidorRed;
	}

	public void setSube_idDistribuidorRed(Long sube_idDistribuidorRed) {
		this.sube_idDistribuidorRed = sube_idDistribuidorRed;
	}

	public List<SelectItem> getListDistribuidorRed() {
		return listDistribuidorRed;
	}

	public void setListDistribuidorRed(List<SelectItem> listDistribuidorRed) {
		this.listDistribuidorRed = listDistribuidorRed;
	}
}
