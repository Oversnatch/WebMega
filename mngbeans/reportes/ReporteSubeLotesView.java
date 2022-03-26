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
import com.americacg.cargavirtual.gestion.model.InformeSubeLote;
import com.americacg.cargavirtual.gestion.model.SubeLote;
import com.americacg.cargavirtual.web.enums.CfgTimeout;
import com.americacg.cargavirtual.web.helpers.ACGFormatHelper;
import com.americacg.cargavirtual.web.helpers.GestionServiceHelper;
import com.americacg.cargavirtual.web.helpers.LogACGHelper;
import com.americacg.cargavirtual.web.model.Error;

import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;

@Named("reporteSubeLotesView")
@Scope(org.springframework.web.context.WebApplicationContext.SCOPE_SESSION)
public class ReporteSubeLotesView extends ReporteGeneral<SubeLote> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7776425843239621077L;
	private Long idLote;
	private Long idTipoTransaccionSube;
	private List<SelectItem> tipoTRXSsube;
	private Long idTerna;
	private String lgSerie;
	private String samId;
	private String posId;
	private String idUbicacionNacion;
	private Long idProveedorLG;
	private List<SelectItem> proveedoresLg;
	private Long idUsuario;

	private List<SelectItem> estadosLote;
	private Long idEstadoLote;

	private Integer tipoFiltroCliente;
	private Long idClienteInferior;

	private Integer cantRecargasSuma = 0;
	private Float importeRecargasTotal = 0F;
	private Integer cantRecargasAnuladasSuma = 0;
	private Float importeRecargasAnuladasTotal = 0F;
	private Integer cantRecargasNoAplicadasSuma = 0;
	private Float importeRecargasNoAplicadasTotal = 0F;
	private Integer cantVentasSuma = 0;
	private Float importeVentasTotal = 0F;
	private Integer cantVentasAnuladasSuma = 0;
	private Float importeVentasAnuladasTotal = 0F;
	private Integer cantMigracionesSuma = 0;
	private Float importeMigracionesTotal = 0F;
	private Integer cantMigracionesNoAplicadasSuma = 0;
	private Float importeMigracionesNoAplicadasTotal = 0F;
	private Integer cantMontosNoAplicadosSuma = 0;
	private Float importeMontosNoAplicadosTotal = 0F;
	private Integer cantRecargasDiferidasSuma = 0;
	private Float importeRecargasDiferidasTotal = 0F;
	private Integer cantRecargasDiferidasNoAplicadasSuma = 0;
	private Float importeRecargasDiferidasNoAplicadasTotal = 0F;

	private Boolean agrJerarquia;

	private Long idDistribuidorRed;
	private List<SelectItem> listDistribuidorRed;

	private Long estadoDevolucion;
	private List<SelectItem> listEstadosDevolucion;
	private Long idTransaccionDevolucion;

	@Override
	public Error resetearReporte() {
		Error e = super.resetearReporte();

		this.idLote = null;
		this.idTipoTransaccionSube = null;
		this.idTerna = null;
		this.lgSerie = null;
		this.samId = null;
		this.posId = null;
		this.idUbicacionNacion = null;
		this.idProveedorLG = null;
		this.idUsuario = null;
		this.idEstadoLote = null;

		this.cantRecargasSuma = null;
		this.importeRecargasTotal = null;
		this.cantRecargasAnuladasSuma = null;
		this.importeRecargasAnuladasTotal = null;
		this.cantRecargasNoAplicadasSuma = null;
		this.importeRecargasNoAplicadasTotal = null;
		this.cantVentasSuma = null;
		this.importeVentasTotal = null;
		this.cantVentasAnuladasSuma = null;
		this.importeVentasAnuladasTotal = null;
		this.cantMigracionesSuma = null;
		this.importeMigracionesTotal = null;
		this.cantMigracionesNoAplicadasSuma = null;
		this.importeMigracionesNoAplicadasTotal = null;
		this.cantMontosNoAplicadosSuma = null;
		this.importeMontosNoAplicadosTotal = null;
		this.cantRecargasDiferidasSuma = null;
		this.importeRecargasDiferidasTotal = null;
		this.cantRecargasDiferidasNoAplicadasSuma = null;
		this.importeRecargasDiferidasNoAplicadasTotal = null;
		this.idClienteInferior = null;
		this.tipoFiltroCliente = 3;
		this.idUsuario = null;
		this.cantRegistros = 0;
		this.agrJerarquia = null;
		this.idDistribuidorRed = null;
		this.estadoDevolucion = null;
		this.idTransaccionDevolucion = null;

		try {

			this.tipoTRXSsube = new ArrayList<SelectItem>();
			DescripcionContainer cd = GestionServiceHelper.getGestionService(CfgTimeout.REPORTE)
					.mostrarTipoTransaccionDeSube(this.getUsuario().getIdMayorista(), null, 0);

			for (Descripcion d : cd.getListDescripcion().getValue().getDescripcion()) {

				if (d.getDescripcion().getValue() != null) {
					if (d.getDescripcion().getValue().length() > 0) {
						this.tipoTRXSsube.add(new SelectItem(d.getId().getValue(), d.getDescripcion().getValue()));
					}
				}

			}

			this.proveedoresLg = new ArrayList<SelectItem>();
			DescripcionContainer p = GestionServiceHelper.getGestionService(CfgTimeout.REPORTE)
					.mostrarProveedorLGdeSube(this.getUsuario().getIdMayorista(), null);

			for (Descripcion d : p.getListDescripcion().getValue().getDescripcion()) {

				if (d.getDescripcion().getValue() != null) {
					if (d.getDescripcion().getValue().length() > 0) {
						this.proveedoresLg.add(new SelectItem(d.getId().getValue(), d.getDescripcion().getValue()));
					}
				}

			}

			this.estadosLote = new ArrayList<SelectItem>();
			DescripcionContainer el = GestionServiceHelper.getGestionService(CfgTimeout.REPORTE)
					.mostrarEstadoDeLotesSube(this.getUsuario().getIdMayorista(), null);

			for (Descripcion d : el.getListDescripcion().getValue().getDescripcion()) {

				if (d.getDescripcion().getValue() != null) {
					if (d.getDescripcion().getValue().length() > 0) {
						this.estadosLote.add(new SelectItem(d.getId().getValue(), d.getDescripcion().getValue()));
					}
				}

			}

			this.listDistribuidorRed = new ArrayList<SelectItem>();
			DescripcionContainer dr = GestionServiceHelper.getGestionService(CfgTimeout.REPORTE)
					.mostrarSubeDistribuidorRed(this.getUsuario().getIdMayorista());

			for (Descripcion d : dr.getListDescripcion().getValue().getDescripcion()) {

				if (d.getDescripcion().getValue() != null) {
					if (d.getDescripcion().getValue().length() > 0) {
						this.listDistribuidorRed
								.add(new SelectItem(d.getId().getValue(), d.getDescripcion().getValue()));
					}
				}

			}

			this.listEstadosDevolucion = new ArrayList<SelectItem>();
			DescripcionContainer ed = GestionServiceHelper.getGestionService(CfgTimeout.REPORTE)
					.mostrarEstadosDevolucionDeLotesSube(this.getUsuario().getIdMayorista(), null);

			for (Descripcion d : ed.getListDescripcion().getValue().getDescripcion()) {

				if (d.getDescripcion().getValue() != null) {
					if (d.getDescripcion().getValue().length() > 0) {
						this.listEstadosDevolucion
								.add(new SelectItem(d.getId().getValue(), d.getDescripcion().getValue()));
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
							"Informe Lotes SUBE. Error ejecutando el WS de consulta: |" + ste.getMessage() + "|");
					e.setError("GST-OTR",
							"No se pudo establecer la comunicación (GST-OTR).\n Por favor intente nuevamente.");
				}
			} else {
				LogACGHelper.escribirLog(null,
						"Informe Lotes SUBE. Error ejecutando el WS de consulta: |" + ste.getMessage() + "|");
				e.setError("GST-OTR",
						"No se pudo establecer la comunicación (GST-OTR).\n Por favor intente nuevamente.");
			}
			FacesContext.getCurrentInstance().validationFailed();
		} catch (Exception ex) {
			LogACGHelper.escribirLog(null,
					"Informe Lotes SUBE. Excepcion ejecutando el WS de consulta: |" + ex.getMessage() + "|");
			e.setError("GST-OTR", "No se pudo establecer la comunicación (GST-OTR).\n Por favor intente nuevamente.");
		}
		return e;
	}

	public ReporteSubeLotesView() {
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

			// Asigno 59 segundos a la fechaHoraHasta
			// Fecha Desde
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(fechaHoraDesde);
			calendar.set(Calendar.SECOND, 0);
			calendar.set(Calendar.MILLISECOND, 0);
			fechaHoraDesde = calendar.getTime();

			// Fecha Hasta
			calendar = Calendar.getInstance();
			calendar.setTime(fechaHoraHasta);
			calendar.set(Calendar.SECOND, 59);
			calendar.set(Calendar.MILLISECOND, 999);
			fechaHoraHasta = calendar.getTime();

			// Inicializo Contador
			cantRecargasSuma = 0;
			importeRecargasTotal = 0F;
			cantRecargasAnuladasSuma = 0;
			importeRecargasAnuladasTotal = 0F;
			cantRecargasNoAplicadasSuma = 0;
			importeRecargasNoAplicadasTotal = 0F;
			cantVentasSuma = 0;
			importeVentasTotal = 0F;
			cantVentasAnuladasSuma = 0;
			importeVentasAnuladasTotal = 0F;
			cantMigracionesSuma = 0;
			importeMigracionesTotal = 0F;
			cantMigracionesNoAplicadasSuma = 0;
			importeMigracionesNoAplicadasTotal = 0F;
			cantMontosNoAplicadosSuma = 0;
			importeMontosNoAplicadosTotal = 0F;
			cantRecargasDiferidasSuma = 0;
			importeRecargasDiferidasTotal = 0F;
			cantRecargasDiferidasNoAplicadasSuma = 0;
			importeRecargasDiferidasNoAplicadasTotal = 0F;
			this.cantRegistros = 0;

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

				cantRecargasSuma = 0;
				importeRecargasTotal = 0F;
				cantRecargasAnuladasSuma = 0;
				importeRecargasAnuladasTotal = 0F;
				cantRecargasNoAplicadasSuma = 0;
				importeRecargasNoAplicadasTotal = 0F;
				cantVentasSuma = 0;
				importeVentasTotal = 0F;
				cantVentasAnuladasSuma = 0;
				importeVentasAnuladasTotal = 0F;
				cantMigracionesSuma = 0;
				importeMigracionesTotal = 0F;
				cantMigracionesNoAplicadasSuma = 0;
				importeMigracionesNoAplicadasTotal = 0F;
				cantMontosNoAplicadosSuma = 0;
				importeMontosNoAplicadosTotal = 0F;
				cantRecargasDiferidasSuma = 0;
				importeRecargasDiferidasTotal = 0F;
				cantRecargasDiferidasNoAplicadasSuma = 0;
				importeRecargasDiferidasNoAplicadasTotal = 0F;

				if (idLote != null) {
					// Si IdTrx != null -> no toma en cuenta fechas
					// pero por WS no se puede enviar un date null
					// Fecha Desde
					calendar = Calendar.getInstance();
					calendar.setTime(new Date());
					calendar.set(Calendar.HOUR_OF_DAY, 00);
					calendar.set(Calendar.MINUTE, 00);
					calendar.set(Calendar.SECOND, 0);
					calendar.set(Calendar.MILLISECOND, 0);
					fechaHoraDesde = calendar.getTime();

					// Fecha Hasta
					calendar = Calendar.getInstance();
					calendar.setTime(new Date());
					calendar.set(Calendar.HOUR_OF_DAY, 23);
					calendar.set(Calendar.MINUTE, 59);
					calendar.set(Calendar.SECOND, 59);
					calendar.set(Calendar.MILLISECOND, 999);
					fechaHoraHasta = calendar.getTime();
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

				InformeSubeLote isl = GestionServiceHelper.getGestionService(CfgTimeout.REPORTE).informeSubeLotes(
						idLote, xmlGCFechaHoraDesde, xmlGCFechaHoraHasta, this.getUsuario().getIdMayorista(),
						this.getUsuario().getIdCliente(), tipoFiltroCliente, idClienteInferior, idTerna, lgSerie, samId,
						posId, idUbicacionNacion, cantRegistrosFiltro, idUsuario, idProveedorLG, idEstadoLote,
						idDistribuidorRed, estadoDevolucion, idTransaccionDevolucion);

				// GENERO Y LIMPIO LAS VARIABLES PARA LA EXPORTACION
				mostrarArchivoCSV = false;

				String csvSepCamp = this.getUsuario().getCsvSeparadorCampo();
				String csvSepDec = this.getUsuario().getCsvSeparadorDecimales();

				StringBuilder sb = new StringBuilder();

				if (isl == null) {
					FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
							"La respuesta del informe de Lotes de SUBE es null", null));
				} else if (isl.getError().getValue().getHayError().getValue()) {
					// TODO Ver porque no entra en esta condicion. No reconoce el Error
					FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
							isl.getError().getValue().getMsgError().getValue(), null));
				} else {
					if (isl.getListSubeLote() != null && isl.getListSubeLote().getValue() != null
							&& isl.getListSubeLote().getValue().getSubeLote() != null
							&& !isl.getListSubeLote().getValue().getSubeLote().isEmpty()) {

						list = isl.getListSubeLote().getValue().getSubeLote();
						cantRegistros = list.size();
						Integer i = 0;

						// CREO HEADER DE EXPORTACION SI ES NECESARIO PARA EXPORTAR A CSV
						if (this.exportToExcel) {
							// Header
							sb.append((char) 34).append("Id Lote").append((char) 34).append(csvSepCamp);
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
							sb.append((char) 34).append("POS ID").append((char) 34).append(csvSepCamp);

							sb.append((char) 34).append("ID Proveedor LG").append((char) 34).append(csvSepCamp);
							sb.append((char) 34).append("Proveedor LG").append((char) 34).append(csvSepCamp);

							sb.append((char) 34).append("ID Ubicacion Nacion").append((char) 34).append(csvSepCamp);

							sb.append((char) 34).append("Nro Lote").append((char) 34).append(csvSepCamp);

							sb.append((char) 34).append("Fecha Apertura").append((char) 34).append(csvSepCamp);

							sb.append((char) 34).append("Fecha Cierre").append((char) 34).append(csvSepCamp);

							sb.append((char) 34).append("Importe LG").append((char) 34).append(csvSepCamp);

							// ------------------------

							sb.append((char) 34).append("Cantidad Recargas").append((char) 34).append(csvSepCamp);
							sb.append((char) 34).append("Importe Recargas").append((char) 34).append(csvSepCamp);

							sb.append((char) 34).append("Cantidad Recargas Anuladas").append((char) 34)
									.append(csvSepCamp);
							sb.append((char) 34).append("Importe Recargas Anuladas").append((char) 34)
									.append(csvSepCamp);

							sb.append((char) 34).append("Cantidad Recargas No Aplicadas").append((char) 34)
									.append(csvSepCamp);
							sb.append((char) 34).append("Importe Recargas No Aplicadas").append((char) 34)
									.append(csvSepCamp);

							sb.append((char) 34).append("Cantidad Ventas").append((char) 34).append(csvSepCamp);
							sb.append((char) 34).append("Importe Ventas").append((char) 34).append(csvSepCamp);

							sb.append((char) 34).append("Cantidad Ventas Anuladas").append((char) 34)
									.append(csvSepCamp);
							sb.append((char) 34).append("Importe Ventas Anuladas").append((char) 34).append(csvSepCamp);

							sb.append((char) 34).append("Cantidad Migraciones").append((char) 34).append(csvSepCamp);
							sb.append((char) 34).append("Importe Migraciones").append((char) 34).append(csvSepCamp);

							sb.append((char) 34).append("Cantidad Migraciones No Aplicadas").append((char) 34)
									.append(csvSepCamp);
							sb.append((char) 34).append("Importe Migraciones No Aplicadas").append((char) 34)
									.append(csvSepCamp);

							sb.append((char) 34).append("Cantidad Montos No Aplicados").append((char) 34)
									.append(csvSepCamp);
							sb.append((char) 34).append("Importe Total Montos No Aplicados").append((char) 34)
									.append(csvSepCamp);

							sb.append((char) 34).append("Cantidad Recargas Diferidas").append((char) 34)
									.append(csvSepCamp);
							sb.append((char) 34).append("Importe Total Recargas Diferidas").append((char) 34)
									.append(csvSepCamp);

							sb.append((char) 34).append("Cantidad Recargas Diferidas No Aplicadas").append((char) 34)
									.append(csvSepCamp);
							sb.append((char) 34).append("Importe Total Recargas Diferidas No Aplicadas")
									.append((char) 34).append(csvSepCamp);

							// ------------------------

							sb.append((char) 34).append("Datos Adicionales").append((char) 34).append(csvSepCamp);

							sb.append((char) 34).append("ID Estado Lote").append((char) 34).append(csvSepCamp);
							sb.append((char) 34).append("Estado Lote").append((char) 34).append(csvSepCamp);

							sb.append((char) 34).append("ID Estado Devolucion").append((char) 34).append(csvSepCamp);
							sb.append((char) 34).append("Estado Devolucion").append((char) 34).append(csvSepCamp);

							sb.append((char) 34).append("ID Transaccion Devolucion").append((char) 34)
									.append(csvSepCamp);

							sb.append((char) 13).append((char) 10);
						}

						// DEFINO FORMATO DE FECHA PARA MOSTRAR EN EL REPORTE QUE SE EXPORTA
						SimpleDateFormat ff = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

						// RECORRO LA LISTA QUE HACER LAS SUMATORIAS Y PARA GENERAR EL REPORTE A
						// EXPORTAR EN CASO DE SER NECESARIO
						for (SubeLote it : list) {

							// HAGO LAS SUMATORIAS PARA EL REPORTE

							if (it.getCantRecargas() != null) {
								cantRecargasSuma += it.getCantRecargas().getValue();
							}

							if (it.getImporteRecargas() != null) {
								importeRecargasTotal += it.getImporteRecargas().getValue();
							}

							if (it.getCantRecargasAnuladas() != null) {
								cantRecargasAnuladasSuma += it.getCantRecargasAnuladas().getValue();
							}

							if (it.getImporteRecargasAnuladas() != null) {
								importeRecargasAnuladasTotal += it.getImporteRecargasAnuladas().getValue();
							}

							if (it.getCantRecargasNoAplicadas() != null) {
								cantRecargasNoAplicadasSuma += it.getCantRecargasNoAplicadas().getValue();
							}

							if (it.getImporteRecargasNoAplicadas() != null) {
								importeRecargasNoAplicadasTotal += it.getImporteRecargasNoAplicadas().getValue();
							}

							if (it.getCantVentas() != null) {
								cantVentasSuma += it.getCantVentas().getValue();
							}

							if (it.getImporteVentas() != null) {
								importeVentasTotal += it.getImporteVentas().getValue();
							}

							if (it.getCantVentasAnuladas() != null) {
								cantVentasAnuladasSuma += it.getCantVentasAnuladas().getValue();
							}

							if (it.getImporteVentasAnuladas() != null) {
								importeVentasAnuladasTotal += it.getImporteVentasAnuladas().getValue();
							}

							if (it.getCantMigraciones() != null) {
								cantMigracionesSuma += it.getCantMigraciones().getValue();
							}

							if (it.getImporteMigraciones() != null) {
								importeMigracionesTotal += it.getImporteMigraciones().getValue();
							}

							if (it.getCantMigracionesNoAplicadas() != null) {
								cantMigracionesNoAplicadasSuma += it.getCantMigracionesNoAplicadas().getValue();
							}

							if (it.getImporteMigracionesNoAplicadas() != null) {
								importeMigracionesNoAplicadasTotal += it.getImporteMigracionesNoAplicadas().getValue();
							}

							if (it.getCantMontosNoAplicados() != null) {
								cantMontosNoAplicadosSuma += it.getCantMontosNoAplicados().getValue();
							}

							if (it.getImporteMontosNoAplicados() != null) {
								importeMontosNoAplicadosTotal += it.getImporteMontosNoAplicados().getValue();
							}

							if (it.getCantRecargasDiferidas() != null) {
								cantRecargasDiferidasSuma += it.getCantRecargasDiferidas().getValue();
							}

							if (it.getImporteRecargasDiferidas() != null) {
								importeRecargasDiferidasTotal += it.getImporteRecargasDiferidas().getValue();
							}

							if (it.getCantRecargasDiferidasNoAplicadas() != null) {
								cantRecargasDiferidasNoAplicadasSuma += it.getCantRecargasDiferidasNoAplicadas()
										.getValue();
							}

							if (it.getImporteRecargasDiferidasNoAplicadas() != null) {
								importeRecargasDiferidasNoAplicadasTotal += it.getImporteRecargasDiferidasNoAplicadas()
										.getValue();
							}

							// CREO ARCHIVO DE EXPORTACION SI ES NECESARIO PARA EXPORTAR A CSV
							if (this.exportToExcel) {

								if (i == 0) {
									mostrarArchivoCSV = true;
								}

								sb.append((char) 34).append(it.getIdLote().getValue()).append((char) 34)
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

								sb.append((char) 34).append("'").append(it.getPosId().getValue()).append((char) 34)
										.append(csvSepCamp);

								sb.append((char) 34).append("'").append(it.getIdProveedorLg().getValue())
										.append((char) 34).append(csvSepCamp);
								sb.append((char) 34).append("'").append(it.getDescProveedorLg().getValue())
										.append((char) 34).append(csvSepCamp);

								sb.append((char) 34).append("'").append(it.getIdUbicacionNacion().getValue())
										.append((char) 34).append(csvSepCamp);

								sb.append((char) 34).append("'").append(it.getNumeroLote().getValue()).append((char) 34)
										.append(csvSepCamp);

								sb.append((char) 34)
										.append((it.getFechaAperturaLote() == null) ? ""
												: ff.format(it.getFechaAperturaLote().toGregorianCalendar().getTime())
														.toString())
										.append((char) 34).append(csvSepCamp);
								sb.append((char) 34).append((it.getFechaCierreLote() == null) ? ""
										: ff.format(it.getFechaCierreLote().toGregorianCalendar().getTime()).toString())
										.append((char) 34).append(csvSepCamp);

								if(it.getImporteLG().getValue() != null) {
									sb.append((char) 34).append("$")
									.append(ACGFormatHelper.format(it.getImporteLG().getValue(), csvSepDec))
									.append((char) 34).append(csvSepCamp);
								}else {
									sb.append((char) 34).append("$")
									.append(ACGFormatHelper.format(0f, csvSepDec))
									.append((char) 34).append(csvSepCamp);
								}

								// ------------------------
								sb.append((char) 34).append("'").append(it.getCantRecargas().getValue())
										.append((char) 34).append(csvSepCamp);
								
								
								if(it.getImporteRecargas().getValue() != null) {
									sb.append((char) 34).append("$")
									.append(ACGFormatHelper.format(it.getImporteRecargas().getValue(), csvSepDec))
									.append((char) 34).append(csvSepCamp);
								}else {
									sb.append((char) 34).append("$")
									.append(ACGFormatHelper.format(0f, csvSepDec))
									.append((char) 34).append(csvSepCamp);
								}
								
								sb.append((char) 34).append("'").append(it.getCantRecargasAnuladas().getValue())
										.append((char) 34).append(csvSepCamp);
								
								if(it.getImporteRecargasAnuladas().getValue() != null) {
									sb.append((char) 34).append("$")
									.append(ACGFormatHelper.format(it.getImporteRecargasAnuladas().getValue(), csvSepDec))
									.append((char) 34).append(csvSepCamp);
								}else {
									sb.append((char) 34).append("$")
									.append(ACGFormatHelper.format(0f, csvSepDec))
									.append((char) 34).append(csvSepCamp);
								}
								
								sb.append((char) 34).append("'").append(it.getCantRecargasNoAplicadas().getValue())
										.append((char) 34).append(csvSepCamp);
								
								if(it.getImporteRecargasNoAplicadas().getValue() != null) {
									sb.append((char) 34).append("$")
									.append(ACGFormatHelper.format(it.getImporteRecargasNoAplicadas().getValue(), csvSepDec))
									.append((char) 34).append(csvSepCamp);
								}else {
									sb.append((char) 34).append("$")
									.append(ACGFormatHelper.format(0f, csvSepDec))
									.append((char) 34).append(csvSepCamp);
								}
								
								sb.append((char) 34).append("'").append(it.getCantVentas().getValue()).append((char) 34)
										.append(csvSepCamp);
								
								if(it.getImporteVentas().getValue() != null) {
									sb.append((char) 34).append("$")
									.append(ACGFormatHelper.format(it.getImporteVentas().getValue(), csvSepDec))
									.append((char) 34).append(csvSepCamp);
								}else {
									sb.append((char) 34).append("$")
									.append(ACGFormatHelper.format(0f, csvSepDec))
									.append((char) 34).append(csvSepCamp);
								}
								
								sb.append((char) 34).append("'").append(it.getCantVentasAnuladas().getValue())
										.append((char) 34).append(csvSepCamp);
								
								if(it.getImporteVentasAnuladas().getValue() != null) {
									sb.append((char) 34).append("$")
									.append(ACGFormatHelper.format(it.getImporteVentasAnuladas().getValue(), csvSepDec))
									.append((char) 34).append(csvSepCamp);
								}else {
									sb.append((char) 34).append("$")
									.append(ACGFormatHelper.format(0f, csvSepDec))
									.append((char) 34).append(csvSepCamp);
								}

								sb.append((char) 34).append("'").append(it.getCantMigraciones().getValue())
										.append((char) 34).append(csvSepCamp);
								
								if(it.getImporteMigraciones().getValue() != null) {
									sb.append((char) 34).append("$")
									.append(ACGFormatHelper.format(it.getImporteMigraciones().getValue(), csvSepDec))
									.append((char) 34).append(csvSepCamp);
								}else {
									sb.append((char) 34).append("$")
									.append(ACGFormatHelper.format(0f, csvSepDec))
									.append((char) 34).append(csvSepCamp);
								}
								
								sb.append((char) 34).append("'").append(it.getCantMigracionesNoAplicadas().getValue())
										.append((char) 34).append(csvSepCamp);
								
								if(it.getImporteMigracionesNoAplicadas().getValue() != null) {
									sb.append((char) 34).append("$")
									.append(ACGFormatHelper.format(it.getImporteMigracionesNoAplicadas().getValue(), csvSepDec))
									.append((char) 34).append(csvSepCamp);
								}else {
									sb.append((char) 34).append("$")
									.append(ACGFormatHelper.format(0f, csvSepDec))
									.append((char) 34).append(csvSepCamp);
								}
								
								sb.append((char) 34).append("'").append(it.getCantMontosNoAplicados().getValue())
										.append((char) 34).append(csvSepCamp);
								
								if(it.getImporteMontosNoAplicados().getValue() != null) {
									sb.append((char) 34).append("$")
									.append(ACGFormatHelper.format(it.getImporteMontosNoAplicados().getValue(), csvSepDec))
									.append((char) 34).append(csvSepCamp);
								}else {
									sb.append((char) 34).append("$")
									.append(ACGFormatHelper.format(0f, csvSepDec))
									.append((char) 34).append(csvSepCamp);
								}
								
								sb.append((char) 34).append("'").append(it.getCantRecargasDiferidas().getValue())
										.append((char) 34).append(csvSepCamp);
								
								if(it.getImporteRecargasDiferidas().getValue() != null) {
									sb.append((char) 34).append("$")
									.append(ACGFormatHelper.format(it.getImporteRecargasDiferidas().getValue(), csvSepDec))
									.append((char) 34).append(csvSepCamp);
								}else {
									sb.append((char) 34).append("$")
									.append(ACGFormatHelper.format(0f, csvSepDec))
									.append((char) 34).append(csvSepCamp);
								}
								
								sb.append((char) 34).append("'")
										.append(it.getCantRecargasDiferidasNoAplicadas().getValue()).append((char) 34)
										.append(csvSepCamp);
								
								if(it.getImporteRecargasDiferidasNoAplicadas().getValue() != null) {
									sb.append((char) 34).append("$")
									.append(ACGFormatHelper.format(it.getImporteRecargasDiferidasNoAplicadas().getValue(), csvSepDec))
									.append((char) 34).append(csvSepCamp);
								}else {
									sb.append((char) 34).append("$")
									.append(ACGFormatHelper.format(0f, csvSepDec))
									.append((char) 34).append(csvSepCamp);
								}
								
								// ------------------------

								if (it.getDatosAdicionales().getValue() != null
										&& !"".equals(it.getDatosAdicionales().getValue())) {
									sb.append((char) 34).append(it.getDatosAdicionales().getValue()).append((char) 34)
											.append(csvSepCamp);
								} else {
									sb.append((char) 34).append((char) 34).append(csvSepCamp);
								}

								sb.append((char) 34).append("'").append(it.getIdEstado().getValue()).append((char) 34)
										.append(csvSepCamp);
								sb.append((char) 34).append("'").append(it.getDescEstadoLote().getValue())
										.append((char) 34).append(csvSepCamp);

								sb.append((char) 34).append("'").append(it.getEstadoDevolucion().getValue())
										.append((char) 34).append(csvSepCamp);
								sb.append((char) 34).append("'").append(it.getDescripcionEstadoDevolucion().getValue())
										.append((char) 34).append(csvSepCamp);

								if (it.getIdTransaccionDevolucion().getValue() != null) {
									sb.append((char) 34).append(it.getIdTransaccionDevolucion().getValue())
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
											+ this.getUsuario().getIdMayorista() + ")_" + "InformeLotesDeSUBE.csv"
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
					LogACGHelper.escribirLog(null,
							"Informe Lotes SUBE. Error ejecutando el WS de consulta: |" + ste.getMessage() + "|");
					FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
							"Error ejecutando el WS de consulta: |" + ste.getMessage() + "|", null));
				}
			} else {
				LogACGHelper.escribirLog(null,
						"Informe Lotes SUBE. Error ejecutando el WS de consulta: |" + ste.getMessage() + "|");
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
						"Error ejecutando el WS de consulta: |" + ste.getMessage() + "|", null));
			}
			this.list = null;
			FacesContext.getCurrentInstance().validationFailed();
		} catch (Exception e) {
			LogACGHelper.escribirLog(null,
					"Informe Lotes SUBE. Excepcion ejecutando el WS de consulta: |" + e.getMessage() + "|");
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
					"Error ejecutando el WS de Informe Lotes SUBE: |" + e.getMessage() + "|", null));
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

	public Long getIdLote() {
		return idLote;
	}

	public void setIdLote(Long idLote) {
		this.idLote = idLote;
	}

	public Integer getCantRecargasSuma() {
		return cantRecargasSuma;
	}

	public void setCantRecargasSuma(Integer cantRecargasSuma) {
		this.cantRecargasSuma = cantRecargasSuma;
	}

	public Float getImporteRecargasTotal() {
		return importeRecargasTotal;
	}

	public void setImporteRecargasTotal(Float importeRecargasTotal) {
		this.importeRecargasTotal = importeRecargasTotal;
	}

	public Integer getCantRecargasAnuladasSuma() {
		return cantRecargasAnuladasSuma;
	}

	public void setCantRecargasAnuladasSuma(Integer cantRecargasAnuladasSuma) {
		this.cantRecargasAnuladasSuma = cantRecargasAnuladasSuma;
	}

	public Float getImporteRecargasAnuladasTotal() {
		return importeRecargasAnuladasTotal;
	}

	public void setImporteRecargasAnuladasTotal(Float importeRecargasAnuladasTotal) {
		this.importeRecargasAnuladasTotal = importeRecargasAnuladasTotal;
	}

	public Integer getCantRecargasNoAplicadasSuma() {
		return cantRecargasNoAplicadasSuma;
	}

	public void setCantRecargasNoAplicadasSuma(Integer cantRecargasNoAplicadasSuma) {
		this.cantRecargasNoAplicadasSuma = cantRecargasNoAplicadasSuma;
	}

	public Float getImporteRecargasNoAplicadasTotal() {
		return importeRecargasNoAplicadasTotal;
	}

	public void setImporteRecargasNoAplicadasTotal(Float importeRecargasNoAplicadasTotal) {
		this.importeRecargasNoAplicadasTotal = importeRecargasNoAplicadasTotal;
	}

	public Integer getCantVentasSuma() {
		return cantVentasSuma;
	}

	public void setCantVentasSuma(Integer cantVentasSuma) {
		this.cantVentasSuma = cantVentasSuma;
	}

	public Float getImporteVentasTotal() {
		return importeVentasTotal;
	}

	public void setImporteVentasTotal(Float importeVentasTotal) {
		this.importeVentasTotal = importeVentasTotal;
	}

	public Integer getCantVentasAnuladasSuma() {
		return cantVentasAnuladasSuma;
	}

	public void setCantVentasAnuladasSuma(Integer cantVentasAnuladasSuma) {
		this.cantVentasAnuladasSuma = cantVentasAnuladasSuma;
	}

	public Float getImporteVentasAnuladasTotal() {
		return importeVentasAnuladasTotal;
	}

	public void setImporteVentasAnuladasTotal(Float importeVentasAnuladasTotal) {
		this.importeVentasAnuladasTotal = importeVentasAnuladasTotal;
	}

	public Integer getCantMigracionesSuma() {
		return cantMigracionesSuma;
	}

	public void setCantMigracionesSuma(Integer cantMigracionesSuma) {
		this.cantMigracionesSuma = cantMigracionesSuma;
	}

	public Float getImporteMigracionesTotal() {
		return importeMigracionesTotal;
	}

	public void setImporteMigracionesTotal(Float importeMigracionesTotal) {
		this.importeMigracionesTotal = importeMigracionesTotal;
	}

	public Integer getCantMigracionesNoAplicadasSuma() {
		return cantMigracionesNoAplicadasSuma;
	}

	public void setCantMigracionesNoAplicadasSuma(Integer cantMigracionesNoAplicadasSuma) {
		this.cantMigracionesNoAplicadasSuma = cantMigracionesNoAplicadasSuma;
	}

	public Float getImporteMigracionesNoAplicadasTotal() {
		return importeMigracionesNoAplicadasTotal;
	}

	public void setImporteMigracionesNoAplicadasTotal(Float importeMigracionesNoAplicadasTotal) {
		this.importeMigracionesNoAplicadasTotal = importeMigracionesNoAplicadasTotal;
	}

	public Integer getCantMontosNoAplicadosSuma() {
		return cantMontosNoAplicadosSuma;
	}

	public void setCantMontosNoAplicadosSuma(Integer cantMontosNoAplicadosSuma) {
		this.cantMontosNoAplicadosSuma = cantMontosNoAplicadosSuma;
	}

	public Integer getCantRecargasDiferidasSuma() {
		return cantRecargasDiferidasSuma;
	}

	public void setCantRecargasDiferidasSuma(Integer cantRecargasDiferidasSuma) {
		this.cantRecargasDiferidasSuma = cantRecargasDiferidasSuma;
	}

	public Boolean getAgrJerarquia() {
		return agrJerarquia;
	}

	public void setAgrJerarquia(Boolean agrJerarquia) {
		this.agrJerarquia = agrJerarquia;
	}

	public List<SelectItem> getEstadosLote() {
		return estadosLote;
	}

	public void setEstadosLote(List<SelectItem> estadosLote) {
		this.estadosLote = estadosLote;
	}

	public Long getIdEstadoLote() {
		return idEstadoLote;
	}

	public void setIdEstadoLote(Long idEstadoLote) {
		this.idEstadoLote = idEstadoLote;
	}

	public Integer getCantRecargasDiferidasNoAplicadasSuma() {
		return cantRecargasDiferidasNoAplicadasSuma;
	}

	public void setCantRecargasDiferidasNoAplicadasSuma(Integer cantRecargasDiferidasNoAplicadasSuma) {
		this.cantRecargasDiferidasNoAplicadasSuma = cantRecargasDiferidasNoAplicadasSuma;
	}

	public Float getImporteRecargasDiferidasNoAplicadasTotal() {
		return importeRecargasDiferidasNoAplicadasTotal;
	}

	public void setImporteRecargasDiferidasNoAplicadasTotal(Float importeRecargasDiferidasNoAplicadasTotal) {
		this.importeRecargasDiferidasNoAplicadasTotal = importeRecargasDiferidasNoAplicadasTotal;
	}

	public Float getImporteMontosNoAplicadosTotal() {
		return importeMontosNoAplicadosTotal;
	}

	public void setImporteMontosNoAplicadosTotal(Float importeMontosNoAplicadosTotal) {
		this.importeMontosNoAplicadosTotal = importeMontosNoAplicadosTotal;
	}

	public Float getImporteRecargasDiferidasTotal() {
		return importeRecargasDiferidasTotal;
	}

	public void setImporteRecargasDiferidasTotal(Float importeRecargasDiferidasTotal) {
		this.importeRecargasDiferidasTotal = importeRecargasDiferidasTotal;
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

	public Long getEstadoDevolucion() {
		return estadoDevolucion;
	}

	public void setEstadoDevolucion(Long estadoDevolucion) {
		this.estadoDevolucion = estadoDevolucion;
	}

	public List<SelectItem> getListEstadosDevolucion() {
		return listEstadosDevolucion;
	}

	public void setListEstadosDevolucion(List<SelectItem> listEstadosDevolucion) {
		this.listEstadosDevolucion = listEstadosDevolucion;
	}

	public Long getIdTransaccionDevolucion() {
		return idTransaccionDevolucion;
	}

	public void setIdTransaccionDevolucion(Long idTransaccionDevolucion) {
		this.idTransaccionDevolucion = idTransaccionDevolucion;
	}
}
