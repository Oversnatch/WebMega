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

import com.americacg.cargavirtual.gestion.model.Descripcion;
import com.americacg.cargavirtual.gestion.model.DescripcionContainer;
import com.americacg.cargavirtual.gestion.model.InformeSubeTransaccion;
import com.americacg.cargavirtual.gestion.model.SubeTransaccion;
import com.americacg.cargavirtual.web.enums.CfgTimeout;
import com.americacg.cargavirtual.web.helpers.ACGFormatHelper;
import com.americacg.cargavirtual.web.helpers.GestionServiceHelper;
import com.americacg.cargavirtual.web.helpers.LogACGHelper;
import com.americacg.cargavirtual.web.model.Error;

import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;

@Named("reporteSubeTrxsView")
@Scope(org.springframework.web.context.WebApplicationContext.SCOPE_SESSION)
public class ReporteSubeTrxsView extends ReporteGeneral<SubeTransaccion> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3393478780397556008L;
	private Long idTransaccionSube;
	private String idTransaccionCliente;
	private Long idTipoTransaccionSube;
	private List<SelectItem> tipoTRXSsube;
	private Long idTerna;
	private String lgSerie;
	private String samId;
	private String posId;
	private String idUbicacionNacion;
	private Long idProveedorLG;
	private List<SelectItem> proveedoresLg;
	private String numeroTarjeta;
	private Float importe;
	private Long idUsuario;

	private Long idEstadoTrxSube;
	private List<SelectItem> estadosTRXSsube;

	private Integer tipoFiltroCliente;
	private Long idClienteInferior;

	private Integer cantRegistrosAprobados = 0;
	private Float importeTotalCargaTarjeta = 0F;

	private Boolean agrJerarquia;

	private Long idDistribuidorRed;
	private List<SelectItem> listDistribuidorRed;

	@Override
	public Error resetearReporte() {
		Error e = super.resetearReporte();

		this.idTransaccionSube = null;
		this.idTransaccionCliente = null;
		this.idTipoTransaccionSube = null;
		this.idTerna = null;
		this.lgSerie = null;
		this.samId = null;
		this.posId = null;
		this.idUbicacionNacion = null;
		this.idProveedorLG = null;
		this.numeroTarjeta = null;
		this.importe = null;
		this.idUsuario = null;
		this.idEstadoTrxSube = null;

		this.idClienteInferior = null;
		this.idTransaccionCliente = null;
		this.tipoFiltroCliente = 3;
		this.idUsuario = null;
		this.cantRegistros = 0;

		this.cantRegistrosAprobados = 0;
		this.importeTotalCargaTarjeta = 0F;

		this.agrJerarquia = null;

		this.idDistribuidorRed = null;

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
				this.estadosTRXSsube = new ArrayList<SelectItem>();
				DescripcionContainer et = GestionServiceHelper.getGestionService(CfgTimeout.REPORTE)
						.mostrarEstadoDeTrxsDeSube(this.getUsuario().getIdMayorista(), null);

				for (Descripcion d : et.getListDescripcion().getValue().getDescripcion()) {

					if (d.getDescripcion() != null) {
						if (d.getDescripcion().getValue().length() > 0) {
							this.estadosTRXSsube
									.add(new SelectItem(d.getId().getValue(), d.getDescripcion().getValue()));
						}
					}

				}
				this.tipoTRXSsube = new ArrayList<SelectItem>();
				DescripcionContainer tt = GestionServiceHelper.getGestionService(CfgTimeout.REPORTE)
						.mostrarTipoTransaccionDeSube(this.getUsuario().getIdMayorista(), null, 0);

				for (Descripcion d : tt.getListDescripcion().getValue().getDescripcion()) {

					if (d.getDescripcion() != null) {
						if (d.getDescripcion().getValue().length() > 0) {
							this.tipoTRXSsube.add(new SelectItem(d.getId().getValue(), d.getDescripcion().getValue()));
						}
					}

				}
				this.proveedoresLg = new ArrayList<SelectItem>();
				DescripcionContainer p = GestionServiceHelper.getGestionService(CfgTimeout.REPORTE)
						.mostrarProveedorLGdeSube(this.getUsuario().getIdMayorista(), null);

				for (Descripcion d : p.getListDescripcion().getValue().getDescripcion()) {

					if (d.getDescripcion() != null) {
						if (d.getDescripcion().getValue().length() > 0) {
							this.proveedoresLg.add(new SelectItem(d.getId().getValue(), d.getDescripcion().getValue()));
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
					LogACGHelper.escribirLog(null, "Informe Transacciones SUBE. Error ejecutando el WS de consulta: |"
							+ ste.getMessage() + "|");
					e.setError("GST-OTR",
							"No se pudo establecer la comunicación (GST-OTR).\n Por favor intente nuevamente.");
				}
			} else {
				LogACGHelper.escribirLog(null,
						"Informe Transacciones SUBE. Error ejecutando el WS de consulta: |" + ste.getMessage() + "|");
				e.setError("GST-OTR",
						"No se pudo establecer la comunicación (GST-OTR).\n Por favor intente nuevamente.");
			}
			FacesContext.getCurrentInstance().validationFailed();
		} catch (Exception ex) {
			LogACGHelper.escribirLog(null,
					"Informe Transacciones SUBE. Excepcion ejecutando el WS de consulta: |" + ex.getMessage() + "|");
			e.setError("GST-OTR", "No se pudo establecer la comunicación (GST-OTR).\n Por favor intente nuevamente.");
		}
		return e;
	}

	public ReporteSubeTrxsView() {
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

			if ("".equals(idTransaccionCliente)) {
				idTransaccionCliente = null;
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

			// Inicializo Contadores
			cantRegistrosAprobados = 0;
			importeTotalCargaTarjeta = 0F;

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
					idClienteInferior = this.getUsuario().getIdCliente();
				}
			} else {
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
						"El tipo de Cliente no es valido para la consulta", null));
				return;
			}

			// TODO validar filtros...
			Boolean filtroOK = false;
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

				cantRegistrosAprobados = 0;
				importeTotalCargaTarjeta = 0F;

				if ((idTransaccionSube != null) || (idTransaccionCliente != null)) {
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

				// Indica la cantidad de registros que voy a usar en el limit de la query a la
				// base de datos
				Integer cantRegistrosFiltro = null;
				if (exportToExcel) {
					cantRegistrosFiltro = cantMaxRegistrosAexportar;
				} else {
					cantRegistrosFiltro = cantMaxRegistrosAmostrarPorPantalla;
				}

				GregorianCalendar gcFechaHoraDesde = new GregorianCalendar();
				gcFechaHoraDesde.setTime(fechaHoraDesde);
				XMLGregorianCalendar xmlGCFechaHoraDesde = DatatypeFactory.newInstance()
						.newXMLGregorianCalendar(gcFechaHoraDesde);

				GregorianCalendar gcFechaHoraHasta = new GregorianCalendar();
				gcFechaHoraHasta.setTime(fechaHoraHasta);
				XMLGregorianCalendar xmlGCFechaHoraHasta = DatatypeFactory.newInstance()
						.newXMLGregorianCalendar(gcFechaHoraHasta);

				InformeSubeTransaccion ist = GestionServiceHelper.getGestionService(CfgTimeout.REPORTE)
						.informeSubeTransacciones(idTransaccionSube, xmlGCFechaHoraDesde, xmlGCFechaHoraHasta,
								this.getUsuario().getIdMayorista(), this.getUsuario().getIdCliente(), tipoFiltroCliente,
								idClienteInferior, idTerna, lgSerie, samId, posId, idUbicacionNacion,
								idTipoTransaccionSube, idTransaccionCliente, numeroTarjeta, importe,
								cantRegistrosFiltro, idUsuario, idProveedorLG, idEstadoTrxSube, 0, idDistribuidorRed);

				// GENERO Y LIMPIO LAS VARIABLES PARA LA EXPORTACION
				mostrarArchivoCSV = false;

				String csvSepCamp = this.getUsuario().getCsvSeparadorCampo();
				String csvSepDec = this.getUsuario().getCsvSeparadorDecimales();

				StringBuilder sb = new StringBuilder();

				if (ist == null) {
					FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
							"La respuesta del informe de Transacciones de SUBE es null", null));
				} else if (ist.getError().getValue().getHayError().getValue()) {
					// TODO Ver porque no entra en esta condicion. No reconoce el Error
					FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
							ist.getError().getValue().getMsgError().getValue(), null));
				} else {
					if (ist.getListSubeTransaccion() != null && ist.getListSubeTransaccion().getValue() != null
							&& ist.getListSubeTransaccion().getValue().getSubeTransaccion() != null
							&& !ist.getListSubeTransaccion().getValue().getSubeTransaccion().isEmpty()) {
						list = ist.getListSubeTransaccion().getValue().getSubeTransaccion();
						cantRegistros = list.size();
						Integer i = 0;

						// CREO HEADER DE EXPORTACION SI ES NECESARIO PARA EXPORTAR A CSV
						if (this.exportToExcel) {

							// Header
							sb.append((char) 34).append("Id").append((char) 34).append(csvSepCamp);
							sb.append((char) 34).append("Fecha").append((char) 34).append(csvSepCamp);
							sb.append((char) 34).append("ID Cliente").append((char) 34).append(csvSepCamp);
							sb.append((char) 34).append("Cliente").append((char) 34).append(csvSepCamp);

							if (agrJerarquia) {
								sb.append((char) 34).append("ID Distribuidor 1").append((char) 34).append(csvSepCamp);
								sb.append((char) 34).append("Distribuidor 1").append((char) 34).append(csvSepCamp);
								sb.append((char) 34).append("ID Distribuidor 2").append((char) 34).append(csvSepCamp);
								sb.append((char) 34).append("Distribuidor 2").append((char) 34).append(csvSepCamp);
								sb.append((char) 34).append("ID Distribuidor 3").append((char) 34).append(csvSepCamp);
								sb.append((char) 34).append("Distribuidor 3").append((char) 34).append(csvSepCamp);
								sb.append((char) 34).append("ID Distribuidor 4").append((char) 34).append(csvSepCamp);
								sb.append((char) 34).append("Distribuidor 4").append((char) 34).append(csvSepCamp);
								sb.append((char) 34).append("ID Distribuidor 5").append((char) 34).append(csvSepCamp);
								sb.append((char) 34).append("Distribuidor 5").append((char) 34).append(csvSepCamp);
							}

							sb.append((char) 34).append("Id Usuario").append((char) 34).append(csvSepCamp);
							sb.append((char) 34).append("Usuario").append((char) 34).append(csvSepCamp);

							sb.append((char) 34).append("ID Terna").append((char) 34).append(csvSepCamp);
							sb.append((char) 34).append("ID Distribuidor Red").append((char) 34).append(csvSepCamp);
							sb.append((char) 34).append("Distribuidor Red").append((char) 34).append(csvSepCamp);

							sb.append((char) 34).append("Nro. Serie").append((char) 34).append(csvSepCamp);
							sb.append((char) 34).append("Sam ID").append((char) 34).append(csvSepCamp);
							sb.append((char) 34).append("Sam Saldo").append((char) 34).append(csvSepCamp);
							sb.append((char) 34).append("POS ID").append((char) 34).append(csvSepCamp);

							sb.append((char) 34).append("ID Proveedor LG").append((char) 34).append(csvSepCamp);
							sb.append((char) 34).append("Proveedor LG").append((char) 34).append(csvSepCamp);

							sb.append((char) 34).append("ID Ubicacion Nacion").append((char) 34).append(csvSepCamp);

							sb.append((char) 34).append("ID Tipo de Trx").append((char) 34).append(csvSepCamp);
							sb.append((char) 34).append("Tipo de Trx").append((char) 34).append(csvSepCamp);

							sb.append((char) 34).append("ID Trx Cliente").append((char) 34).append(csvSepCamp);

							sb.append((char) 34).append("Fecha Cliente").append((char) 34).append(csvSepCamp);

							sb.append((char) 34).append("ID Tarjeta").append((char) 34).append(csvSepCamp);

							sb.append((char) 34).append("Nro de Tarjeta").append((char) 34).append(csvSepCamp);

							sb.append((char) 34).append("Importe Carga Tarjeta").append((char) 34).append(csvSepCamp);

							sb.append((char) 34).append("Nro de Ticket").append((char) 34).append(csvSepCamp);

							sb.append((char) 34).append("ID Trx descuento Bolsa").append((char) 34).append(csvSepCamp);

							sb.append((char) 34).append("Importe Descontado Bolsa").append((char) 34)
									.append(csvSepCamp);

							sb.append((char) 34).append("Importe Cargado SAM").append((char) 34).append(csvSepCamp);

							sb.append((char) 34).append("Importe Ajuste Carga SAM").append((char) 34)
									.append(csvSepCamp);

							sb.append((char) 34).append("Importe Recupero").append((char) 34).append(csvSepCamp);

							sb.append((char) 34).append("Importe Pendiente Recupero").append((char) 34)
									.append(csvSepCamp);

							sb.append((char) 34).append("Estado").append((char) 34).append(csvSepCamp);
							sb.append((char) 34).append("ID Estado").append((char) 34).append(csvSepCamp);
							sb.append((char) 34).append("Descripcion Adicional Estado").append((char) 34)
									.append(csvSepCamp);

							sb.append((char) 34).append("Datos Adicionales").append((char) 34);

							sb.append((char) 13).append((char) 10);
						}

						// DEFINO FORMATO DE FECHA PARA MOSTRAR EN EL REPORTE QUE SE EXPORTA
						SimpleDateFormat ff = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

						// RECORRO LA LISTA QUE HACER LAS SUMATORIAS Y PARA GENERAR EL REPORTE A
						// EXPORTAR EN CASO DE SER NECESARIO
						for (SubeTransaccion it : list) {

							// HAGO LAS SUMATORIAS PARA EL REPORTE

							// SUMA DEL VALOR DE LAS TRANSACCIONES APROBADAS
							if (it.getIdEstado().getValue() != null && it.getIdEstado().getValue() == 2) {

								cantRegistrosAprobados++;

								if (it.getImporteCargaTarjeta() != null
										&& it.getImporteCargaTarjeta().getValue() != null) {
									importeTotalCargaTarjeta += it.getImporteCargaTarjeta().getValue();
								}

							}

							// CREO ARCHIVO DE EXPORTACION SI ES NECESARIO PARA EXPORTAR A CSV

							if (this.exportToExcel) {

								if (i == 0) {
									mostrarArchivoCSV = true;
								}

								sb.append((char) 34).append(it.getIdTransaccionSube().getValue()).append((char) 34)
										.append(csvSepCamp);

								sb.append((char) 34).append(
										ff.format(it.getFechaServidor().toGregorianCalendar().getTime()).toString())
										.append((char) 34).append(csvSepCamp);

								sb.append((char) 34).append(it.getIdCliente().getValue()).append((char) 34)
										.append(csvSepCamp);
								sb.append((char) 34).append(it.getRazonSocialCliente().getValue()).append((char) 34)
										.append(csvSepCamp);

								if (agrJerarquia) {
									if (it.getIdDistribuidor1().getValue() != null
											&& it.getIdDistribuidor1().getValue() > 0) {
										sb.append((char) 34).append(it.getIdDistribuidor1().getValue())
												.append((char) 34).append(csvSepCamp);
									} else {
										sb.append((char) 34).append((char) 34).append(csvSepCamp);
									}
									if (it.getRazonSocialDist1().getValue() != null
											&& !"".equals(it.getRazonSocialDist1().getValue())) {
										sb.append((char) 34).append(it.getRazonSocialDist1().getValue())
												.append((char) 34).append(csvSepCamp);
									} else {
										sb.append((char) 34).append((char) 34).append(csvSepCamp);
									}

									if (it.getIdDistribuidor2().getValue() != null
											&& it.getIdDistribuidor2().getValue() > 0) {
										sb.append((char) 34).append(it.getIdDistribuidor2().getValue())
												.append((char) 34).append(csvSepCamp);
									} else {
										sb.append((char) 34).append((char) 34).append(csvSepCamp);
									}
									if (it.getRazonSocialDist2().getValue() != null
											&& !"".equals(it.getRazonSocialDist2().getValue())) {
										sb.append((char) 34).append(it.getRazonSocialDist2().getValue())
												.append((char) 34).append(csvSepCamp);
									} else {
										sb.append((char) 34).append((char) 34).append(csvSepCamp);
									}

									if (it.getIdDistribuidor3().getValue() != null
											&& it.getIdDistribuidor3().getValue() > 0) {
										sb.append((char) 34).append(it.getIdDistribuidor3().getValue())
												.append((char) 34).append(csvSepCamp);
									} else {
										sb.append((char) 34).append((char) 34).append(csvSepCamp);
									}
									if (it.getRazonSocialDist3().getValue() != null
											&& !"".equals(it.getRazonSocialDist3().getValue())) {
										sb.append((char) 34).append(it.getRazonSocialDist3().getValue())
												.append((char) 34).append(csvSepCamp);
									} else {
										sb.append((char) 34).append((char) 34).append(csvSepCamp);
									}

									if (it.getIdDistribuidor4().getValue() != null
											&& it.getIdDistribuidor4().getValue() > 0) {
										sb.append((char) 34).append(it.getIdDistribuidor4().getValue())
												.append((char) 34).append(csvSepCamp);
									} else {
										sb.append((char) 34).append((char) 34).append(csvSepCamp);
									}
									if (it.getRazonSocialDist4().getValue() != null
											&& !"".equals(it.getRazonSocialDist4().getValue())) {
										sb.append((char) 34).append(it.getRazonSocialDist4().getValue())
												.append((char) 34).append(csvSepCamp);
									} else {
										sb.append((char) 34).append((char) 34).append(csvSepCamp);
									}

									if (it.getIdDistribuidor5().getValue() != null
											&& it.getIdDistribuidor5().getValue() > 0) {
										sb.append((char) 34).append(it.getIdDistribuidor5().getValue())
												.append((char) 34).append(csvSepCamp);
									} else {
										sb.append((char) 34).append((char) 34).append(csvSepCamp);
									}
									if (it.getRazonSocialDist5().getValue() != null
											&& !"".equals(it.getRazonSocialDist5().getValue())) {
										sb.append((char) 34).append(it.getRazonSocialDist5().getValue())
												.append((char) 34).append(csvSepCamp);
									} else {
										sb.append((char) 34).append((char) 34).append(csvSepCamp);
									}
								}

								sb.append((char) 34).append(it.getIdUsuario().getValue()).append((char) 34)
										.append(csvSepCamp);
								sb.append((char) 34).append(it.getUsuario().getValue()).append((char) 34)
										.append(csvSepCamp);

								sb.append((char) 34).append(it.getIdTerna().getValue()).append((char) 34)
										.append(csvSepCamp);

								if (it.getIdDistribuidorRed().getValue() != null) {
									sb.append((char) 34).append(it.getIdDistribuidorRed().getValue()).append((char) 34)
											.append(csvSepCamp);
								} else {
									sb.append((char) 34).append((char) 34).append(csvSepCamp);
								}
								if (it.getDistribuidorRedDescripcion().getValue() != null
										&& !"".equals(it.getDistribuidorRedDescripcion().getValue())) {
									sb.append((char) 34).append(it.getDistribuidorRedDescripcion().getValue())
											.append((char) 34).append(csvSepCamp);
								} else {
									sb.append((char) 34).append((char) 34).append(csvSepCamp);
								}

								sb.append((char) 34).append("'").append(it.getLgSerie().getValue()).append((char) 34)
										.append(csvSepCamp);

								sb.append((char) 34).append("'").append(it.getSamId().getValue()).append((char) 34)
										.append(csvSepCamp);
								
								if(it.getSamSaldo().getValue() != null) {
									sb.append((char) 34).append("$")
									.append(ACGFormatHelper.format(it.getSamSaldo().getValue(), csvSepDec))
									.append((char) 34).append(csvSepCamp);
								}else {
									sb.append((char) 34).append("$")
									.append(ACGFormatHelper.format(0f, csvSepDec))
									.append((char) 34).append(csvSepCamp);
								}

								sb.append((char) 34).append("'").append(it.getPosId().getValue()).append((char) 34)
										.append(csvSepCamp);

								sb.append((char) 34).append("'").append(it.getIdProveedorLg().getValue())
										.append((char) 34).append(csvSepCamp);
								sb.append((char) 34).append("'").append(it.getDescProveedorLg().getValue())
										.append((char) 34).append(csvSepCamp);

								sb.append((char) 34).append("'").append(it.getIdUbicacionNacion().getValue())
										.append((char) 34).append(csvSepCamp);

								sb.append((char) 34).append("'").append(it.getIdTipoTransaccionSube().getValue())
										.append((char) 34).append(csvSepCamp);
								sb.append((char) 34).append("'").append(it.getDescTipoTransaccionSube().getValue())
										.append((char) 34).append(csvSepCamp);

								sb.append((char) 34).append("'").append(it.getIdTransaccionCliente().getValue())
										.append((char) 34).append(csvSepCamp);

								sb.append((char) 34).append((it.getFechaCliente() == null) ? ""
										: ff.format(it.getFechaCliente().toGregorianCalendar().getTime()).toString())
										.append((char) 34).append(csvSepCamp);

								if (it.getIdTarjeta().getValue() != null && !"".equals(it.getIdTarjeta().getValue())) {
									sb.append((char) 34).append(it.getIdTarjeta().getValue()).append((char) 34)
											.append(csvSepCamp);
								} else {
									sb.append((char) 34).append((char) 34).append(csvSepCamp);
								}

								if (it.getNumeroTarjeta().getValue() != null
										&& !"".equals(it.getNumeroTarjeta().getValue())) {
									sb.append((char) 34).append(it.getNumeroTarjeta().getValue()).append((char) 34)
											.append(csvSepCamp);
								} else {
									sb.append((char) 34).append((char) 34).append(csvSepCamp);
								}

								if(it.getImporteCargaTarjeta().getValue() != null) {
									sb.append((char) 34).append("$")
									.append(ACGFormatHelper.format(it.getImporteCargaTarjeta().getValue(), csvSepDec))
									.append((char) 34).append(csvSepCamp);
								}else {
									sb.append((char) 34).append("$")
									.append(ACGFormatHelper.format(0f, csvSepDec))
									.append((char) 34).append(csvSepCamp);
								}
								
								if (it.getNumeroTicket().getValue() != null
										&& !"".equals(it.getNumeroTicket().getValue())) {
									sb.append((char) 34).append(it.getNumeroTicket().getValue()).append((char) 34)
											.append(csvSepCamp);
								} else {
									sb.append((char) 34).append((char) 34).append(csvSepCamp);
								}

								if (it.getIdTrxDescuentoBolsa().getValue() != null) {
									sb.append((char) 34).append("'").append(it.getIdTrxDescuentoBolsa().getValue())
											.append((char) 34).append(csvSepCamp);
								} else {
									sb.append((char) 34).append((char) 34).append(csvSepCamp);
								}

								if(it.getImporteDescontadoBolsa().getValue() != null) {
									sb.append((char) 34).append("$")
									.append(ACGFormatHelper.format(it.getImporteDescontadoBolsa().getValue(), csvSepDec))
									.append((char) 34).append(csvSepCamp);
								}else {
									sb.append((char) 34).append("$")
									.append(ACGFormatHelper.format(0f, csvSepDec))
									.append((char) 34).append(csvSepCamp);
								}
								
								if(it.getImporteCargadoSAM().getValue() != null) {
									sb.append((char) 34).append("$")
									.append(ACGFormatHelper.format(it.getImporteCargadoSAM().getValue(), csvSepDec))
									.append((char) 34).append(csvSepCamp);
								}else {
									sb.append((char) 34).append("$")
									.append(ACGFormatHelper.format(0f, csvSepDec))
									.append((char) 34).append(csvSepCamp);
								}

								if(it.getImporteAjusteCargaSAM().getValue() != null) {
									sb.append((char) 34).append("$")
									.append(ACGFormatHelper.format(it.getImporteAjusteCargaSAM().getValue(), csvSepDec))
									.append((char) 34).append(csvSepCamp);
								}else {
									sb.append((char) 34).append("$")
									.append(ACGFormatHelper.format(0f, csvSepDec))
									.append((char) 34).append(csvSepCamp);
								}
								
								if(it.getImporteRecupero().getValue() != null) {
									sb.append((char) 34).append("$")
									.append(ACGFormatHelper.format(it.getImporteRecupero().getValue(), csvSepDec))
									.append((char) 34).append(csvSepCamp);
								}else {
									sb.append((char) 34).append("$")
									.append(ACGFormatHelper.format(0f, csvSepDec))
									.append((char) 34).append(csvSepCamp);
								}
								
								if(it.getImportePendienteRecupero().getValue() != null) {
									sb.append((char) 34).append("$")
									.append(ACGFormatHelper.format(it.getImportePendienteRecupero().getValue(), csvSepDec))
									.append((char) 34).append(csvSepCamp);
								}else {
									sb.append((char) 34).append("$")
									.append(ACGFormatHelper.format(0f, csvSepDec))
									.append((char) 34).append(csvSepCamp);
								}								
								
								sb.append((char) 34).append("'").append(it.getIdEstado().getValue()).append((char) 34)
										.append(csvSepCamp);
								sb.append((char) 34).append("'").append(it.getDescripcionEstado().getValue())
										.append((char) 34).append(csvSepCamp);
								sb.append((char) 34).append("'").append(it.getDescripcionAdicionalEstado().getValue())
										.append((char) 34).append(csvSepCamp);

								if (it.getDatosAdicionales().getValue() != null
										&& !"".equals(it.getDatosAdicionales().getValue())) {
									sb.append((char) 34).append("'").append(it.getDatosAdicionales().getValue())
											.append((char) 34).append(csvSepCamp);
								} else {
									sb.append((char) 34).append((char) 34).append(csvSepCamp);
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
											+ this.getUsuario().getIdMayorista() + ")_"
											+ "InformeTransaccionesDeSUBE.csv" + "\"");

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

					} else {
						FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
								"No existe información para la consulta realizada.", null));
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
					LogACGHelper.escribirLog(null, "Informe Transacciones SUBE. Error ejecutando el WS de consulta: |"
							+ ste.getMessage() + "|");
					FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
							"Error ejecutando el WS de consulta: |" + ste.getMessage() + "|", null));
				}
			} else {
				LogACGHelper.escribirLog(null,
						"Informe Transacciones SUBE. Error ejecutando el WS de consulta: |" + ste.getMessage() + "|");
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
						"Error ejecutando el WS de consulta: |" + ste.getMessage() + "|", null));
			}
			this.list = null;
			FacesContext.getCurrentInstance().validationFailed();
		} catch (Exception e) {
			LogACGHelper.escribirLog(null,
					"Informe Transacciones SUBE. Excepcion ejecutando el WS de consulta: |" + e.getMessage() + "|");
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
					"Error ejecutando el WS de Informe Transacciones SUBE: |" + e.getMessage() + "|", null));
			FacesContext.getCurrentInstance().validationFailed();
			this.list = null;
		}
		PrimeFaces.current().executeScript("PF('blockPanelGral').hide()");
		return;
	}

	public Long getIdClienteInferior() {
		return idClienteInferior;
	}

	public void setIdClienteInferior(Long idClienteInferior) {
		this.idClienteInferior = idClienteInferior;
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

	public String getIdTransaccionCliente() {
		return idTransaccionCliente;
	}

	public void setIdTransaccionCliente(String idTransaccionCliente) {
		this.idTransaccionCliente = idTransaccionCliente;
	}

	public Long getIdTransaccionSube() {
		return idTransaccionSube;
	}

	public void setIdTransaccionSube(Long idTransaccionSube) {
		this.idTransaccionSube = idTransaccionSube;
	}

	public Long getIdTipoTransaccionSube() {
		return idTipoTransaccionSube;
	}

	public void setIdTipoTransaccionSube(Long idTipoTransaccionSube) {
		this.idTipoTransaccionSube = idTipoTransaccionSube;
	}

	public List<SelectItem> getTipoTRXSsube() {
		return tipoTRXSsube;
	}

	public void setTipoTRXSsube(List<SelectItem> tipoTRXSsube) {
		this.tipoTRXSsube = tipoTRXSsube;
	}

	public Long getIdTerna() {
		return idTerna;
	}

	public void setIdTerna(Long idTerna) {
		this.idTerna = idTerna;
	}

	public String getLgSerie() {
		return lgSerie;
	}

	public void setLgSerie(String lgSerie) {
		this.lgSerie = lgSerie;
	}

	public String getSamId() {
		return samId;
	}

	public void setSamId(String samId) {
		this.samId = samId;
	}

	public String getPosId() {
		return posId;
	}

	public void setPosId(String posId) {
		this.posId = posId;
	}

	public String getIdUbicacionNacion() {
		return idUbicacionNacion;
	}

	public void setIdUbicacionNacion(String idUbicacionNacion) {
		this.idUbicacionNacion = idUbicacionNacion;
	}

	public Long getIdProveedorLG() {
		return idProveedorLG;
	}

	public void setIdProveedorLG(Long idProveedorLG) {
		this.idProveedorLG = idProveedorLG;
	}

	public List<SelectItem> getProveedoresLg() {
		return proveedoresLg;
	}

	public void setProveedoresLg(List<SelectItem> proveedoresLg) {
		this.proveedoresLg = proveedoresLg;
	}

	public String getNumeroTarjeta() {
		return numeroTarjeta;
	}

	public void setNumeroTarjeta(String numeroTarjeta) {
		this.numeroTarjeta = numeroTarjeta;
	}

	public Float getImporte() {
		return importe;
	}

	public void setImporte(Float importe) {
		this.importe = importe;
	}

	public Boolean getAgrJerarquia() {
		return agrJerarquia;
	}

	public void setAgrJerarquia(Boolean agrJerarquia) {
		this.agrJerarquia = agrJerarquia;
	}

	public Long getIdEstadoTrxSube() {
		return idEstadoTrxSube;
	}

	public void setIdEstadoTrxSube(Long idEstadoTrxSube) {
		this.idEstadoTrxSube = idEstadoTrxSube;
	}

	public List<SelectItem> getEstadosTRXSsube() {
		return estadosTRXSsube;
	}

	public void setEstadosTRXSsube(List<SelectItem> estadosTRXSsube) {
		this.estadosTRXSsube = estadosTRXSsube;
	}

	public Integer getCantRegistrosAprobados() {
		return cantRegistrosAprobados;
	}

	public void setCantRegistrosAprobados(Integer cantRegistrosAprobados) {
		this.cantRegistrosAprobados = cantRegistrosAprobados;
	}

	public Float getImporteTotalCargaTarjeta() {
		return importeTotalCargaTarjeta;
	}

	public void setImporteTotalCargaTarjeta(Float importeTotalCargaTarjeta) {
		this.importeTotalCargaTarjeta = importeTotalCargaTarjeta;
	}

	public Long getIdDistribuidorRed() {
		return idDistribuidorRed;
	}

	public void setIdDistribuidorRed(Long idDistribuidorRed) {
		this.idDistribuidorRed = idDistribuidorRed;
	}

	public List<SelectItem> getListDistribuidorRed() {
		return listDistribuidorRed;
	}

	public void setListDistribuidorRed(List<SelectItem> listDistribuidorRed) {
		this.listDistribuidorRed = listDistribuidorRed;
	}
}