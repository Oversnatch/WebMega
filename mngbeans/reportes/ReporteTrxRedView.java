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

import com.americacg.cargavirtual.gestion.model.ArrayOfCabeceraProducto;
import com.americacg.cargavirtual.gestion.model.ArrayOfSaldoProveedor;
import com.americacg.cargavirtual.gestion.model.CabeceraProducto;
import com.americacg.cargavirtual.gestion.model.Descripcion;
import com.americacg.cargavirtual.gestion.model.DescripcionContainer;
import com.americacg.cargavirtual.gestion.model.InformeResumenTransacciones;
import com.americacg.cargavirtual.gestion.model.RespResumenTrxsContainer;
import com.americacg.cargavirtual.gestion.model.SaldoProveedor;
import com.americacg.cargavirtual.web.enums.CfgTimeout;
import com.americacg.cargavirtual.web.helpers.GestionServiceHelper;
import com.americacg.cargavirtual.web.helpers.LogACGHelper;
import com.americacg.cargavirtual.web.model.Error;

import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.model.SelectItem;

@Named("reporteTrxRedView")
@Scope(org.springframework.web.context.WebApplicationContext.SCOPE_SESSION)
public class ReporteTrxRedView extends ReporteGeneral<InformeResumenTransacciones> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6216664193927846349L;
	private String estadoTransacciones;
	private Long idClienteInferior;
	private Long idProducto;
	private Long idProveedor;
	private Long idUsuario;
	private Long idLote;
	private Integer caracteristicaTelefono;
	private String identifTerminal;
	private String imeiTerminal;

	private Integer tipoFiltroCliente;

	private String tipoTRX;
	private Long idTipoTerminal;

	private Float importeTotal = 0F;
	private Integer cantRegistros = 0;
	private Integer cantidadOperaciones = 0;
	private Float promedioTotal = 0F;

	private List<SelectItem> productos;
	private List<SelectItem> proveedores;

	private List<SelectItem> nivelesJerarquia;

	private String fechaHoraDesdeMostrar;
	private String fechaHoraHastaMostrar;

	private Boolean agrupar;
	private Boolean agrUsu;
	private Boolean agrProv;
	private Boolean agrFec;
	private Boolean agrTerm;
	private Boolean agrCarTel;
	private Boolean agrProd;
	private Boolean agrTipoTRX;
	private Boolean infHistorico;
	private Boolean agrLote;
	private Boolean agrIdentifTerminal;
	private Boolean agrImeiTerminal;
	private Integer nivelJerarquia;
	private Boolean agrModTerm;

	private List<SelectItem> tipoTerminales;

	private Long idPais;
	private List<SelectItem> paises;
	private Long idProvincia;
	private List<SelectItem> provincias;
	private Long idLocalidad;
	private List<SelectItem> localidades;
	private Long idZona;
	private List<SelectItem> zonas;
	private Boolean agrProvincia = false;
	private Boolean agrLocalidad = false;
	private Boolean agrPais = false;
	private Boolean agrZona = false;
	private Boolean agrSube_DistribuidorRed = false;

	private Long sube_idDistribuidorRed;
	private List<SelectItem> listDistribuidorRed;

	private RespResumenTrxsContainer rt;

	@Override
	public Error resetearReporte() {
		Error e = super.resetearReporte();
		this.importeTotal = 0F;
		this.cantRegistros = 0;
		this.cantidadOperaciones = 0;
		this.promedioTotal = 0F;
		this.idClienteInferior = null;
		this.idUsuario = null;
		this.idLote = null;
		this.identifTerminal = null;
		this.imeiTerminal = null;
		this.caracteristicaTelefono = null;
		this.estadoTransacciones = null;
		this.idProducto = null;
		this.idProveedor = null;
		this.tipoTRX = null;
		this.idTipoTerminal = null;
		this.tipoFiltroCliente = 3;
		this.fechaHoraDesdeMostrar = null;
		this.fechaHoraHastaMostrar = null;
		this.agrupar = false;
		this.agrUsu = false;
		this.agrProv = false;
		this.agrFec = false;
		this.agrTerm = false;
		this.agrCarTel = false;
		this.agrProd = null;
		this.agrTipoTRX = null;
		this.infHistorico = null;
		this.agrLote = null;
		this.agrModTerm = null;
		this.agrIdentifTerminal = null;
		this.agrImeiTerminal = null;
		this.nivelJerarquia = 0;
		this.idPais = null;
		this.idProvincia = null;
		this.idLocalidad = null;
		this.idZona = null;
		this.agrPais = false;
		this.agrProvincia = false;
		this.agrLocalidad = false;
		this.agrZona = false;
		this.sube_idDistribuidorRed = null;
		this.agrSube_DistribuidorRed = false;

		try {
			this.listDistribuidorRed = new ArrayList<SelectItem>();
			DescripcionContainer cd = GestionServiceHelper.getGestionService(CfgTimeout.REPORTE)
					.mostrarSubeDistribuidorRed(this.getUsuario().getIdMayorista());

			for (Descripcion d : cd.getListDescripcion().getValue().getDescripcion()) {

				if (d.getDescripcion().getValue() != null) {
					if (d.getDescripcion().getValue().length() > 0) {
						this.listDistribuidorRed
								.add(new SelectItem(d.getId().getValue(), d.getDescripcion().getValue()));
					}
				}

			}
			this.provincias = new ArrayList<SelectItem>();
			this.localidades = new ArrayList<SelectItem>();

			this.paises = new ArrayList<SelectItem>();
			DescripcionContainer p = GestionServiceHelper.getGestionService(CfgTimeout.REPORTE)
					.mostrarPaises(this.getUsuario().getIdMayorista(), null);

			if ((p != null) && (!p.getListDescripcion().getValue().getDescripcion().isEmpty())) {
				for (Descripcion d : p.getListDescripcion().getValue().getDescripcion()) {
					this.paises.add(new SelectItem(d.getId().getValue(), d.getDescripcion().getValue()));
				}
			}

			this.zonas = new ArrayList<SelectItem>();
			DescripcionContainer z = GestionServiceHelper.getGestionService(CfgTimeout.REPORTE)
					.mostrarZonas(this.getUsuario().getIdMayorista(), null);

			for (Descripcion d : z.getListDescripcion().getValue().getDescripcion()) {
				this.zonas.add(new SelectItem(d.getId().getValue(), d.getDescripcion().getValue()));
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
			this.nivelesJerarquia = new ArrayList<SelectItem>();

			int ini = 0;

			if (this.getUsuario().getNivelDistribuidorSuperior() == null) {
				ini = 0;
			} else {
				ini = this.getUsuario().getNivelDistribuidorSuperior();
			}

			Boolean primerIngreso = true;

			for (int i = ini; i < 6; i++) {

				if (primerIngreso) {
					this.nivelesJerarquia.add(new SelectItem(0, i + ""));
				} else {
					this.nivelesJerarquia.add(new SelectItem(i, i + ""));
				}

				primerIngreso = false;

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
							"Informe Transacciones Resumido. Error ejecutando el WS de consulta: |" + ste.getMessage()
									+ "|");
					e.setError("GST-OTR",
							"No se pudo establecer la comunicación (GST-OTR).\n Por favor intente nuevamente.");
				}
			} else {
				LogACGHelper.escribirLog(null, "Informe Transacciones Resumido. Error ejecutando el WS de consulta: |"
						+ ste.getMessage() + "|");
				e.setError("GST-OTR",
						"No se pudo establecer la comunicación (GST-OTR).\n Por favor intente nuevamente.");
			}
			FacesContext.getCurrentInstance().validationFailed();
		} catch (Exception ex) {
			LogACGHelper.escribirLog(null,
					"Informe Transacciones Resumido. Excepcion ejecutando el WS de consulta: |" + ex.getMessage() + "|");
			e.setError("GST-OTR", "No se pudo establecer la comunicación (GST-OTR).\n Por favor intente nuevamente.");
		}
		return e;
	}

	public ReporteTrxRedView() {
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

			// Limpio la lista
			list = null;

			// Inicializo contadores
			importeTotal = 0F;
			cantRegistros = 0;
			cantidadOperaciones = 0;
			promedioTotal = 0F;

			if (!validaFechas()) {
				return;
			}

			if (agrProv == null) {
				agrProv = false;
			}
			if (nivelJerarquia == null) {
				nivelJerarquia = 0;
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

				importeTotal = 0F;
				cantRegistros = 0;
				cantidadOperaciones = 0;
				promedioTotal = 0F;

				LogACGHelper.escribirLog(null, "Informe Reducido Transacciones --> idMayorista: |"
						+ this.getUsuario().getIdMayorista() + "| idCliente: |" + this.getUsuario().getIdCliente()
						+ "| nivelDistribuidorSuperior: |" + this.getUsuario().getNivelDistribuidorSuperior()
						+ "| fechaHoraDesde: |" + fechaHoraDesde + "| fechaHoraHasta: |" + fechaHoraHasta
						+ "| estadoTransacciones: |" + estadoTransacciones + "| idClienteInferior: |"
						+ idClienteInferior + "| idUsuario: |" + idUsuario + "| idProducto: |" + idProducto
						+ "| idProveedor: |" + idProveedor + "| tipoFiltroCliente: |" + tipoFiltroCliente
						+ "| agrupar: |" + agrupar + "| agrUsu: |" + agrUsu + "| agrProv: |" + agrProv + "| agrFec: |"
						+ agrFec + "| agrTerm: |" + agrTerm + "| caracteristicaTelefono: |" + caracteristicaTelefono
						+ "| agrCarTel: |" + agrCarTel + "| agrProd: |" + agrProd + "| agrTipoTRX: |" + agrTipoTRX
						+ "| infHistorico: |" + infHistorico + "| agrLote: |" + agrLote + "| idLote: |" + idLote
						+ "| agrIdentifTerminal: |" + agrIdentifTerminal + "| identifTerminal: |" + identifTerminal
						+ "| agrImeiTerminal: |" + agrImeiTerminal + "| imeiTerminal: |" + imeiTerminal
						+ "| nivelJerarquia: |" + nivelJerarquia + "| agrModTerm: |" + agrModTerm + "|");

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

				rt = GestionServiceHelper.getGestionService(CfgTimeout.REPORTE).informeReducidoTransacciones(
						this.getUsuario().getIdMayorista(), this.getUsuario().getIdCliente(),
						this.getUsuario().getNivelDistribuidorSuperior(), xmlGCFechaHoraDesde, xmlGCFechaHoraHasta,
						estadoTransacciones, idProducto, tipoTRX, idTipoTerminal, idProveedor, idClienteInferior,
						tipoFiltroCliente, agrupar, idUsuario, agrUsu, agrTerm, agrProv, agrFec, agrCarTel,
						caracteristicaTelefono, agrProd, agrTipoTRX, infHistorico, agrLote, idLote, agrIdentifTerminal,
						identifTerminal, nivelJerarquia, agrModTerm, agrImeiTerminal, imeiTerminal, cantRegistrosFiltro,
						idPais, idProvincia, idLocalidad, idZona, agrPais, agrProvincia, agrLocalidad, agrZona,
						sube_idDistribuidorRed, agrSube_DistribuidorRed);

				// GENERO Y LIMPIO LAS VARIABLES PARA LA EXPORTACION
				mostrarArchivoCSV = false;

				String csvSepCamp = this.getUsuario().getCsvSeparadorCampo();
				String csvSepDec = this.getUsuario().getCsvSeparadorDecimales();

				StringBuilder sb = new StringBuilder();

				if (rt == null) {
					FacesContext.getCurrentInstance().addMessage(null,
							new FacesMessage(FacesMessage.SEVERITY_ERROR, "La consulta devolvio null", null));
				} else {
					if (rt.getError().getValue().getHayError().getValue()) {
						FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
								rt.getError().getValue().getMsgError().getValue(), null));
					} else {
						// Asigno la lista recibida a la variable "list"
						list = rt.getListResumenTrxs().getValue().getInformeResumenTransacciones();

						if (list == null) {
							FacesContext.getCurrentInstance().addMessage(null,
									new FacesMessage(FacesMessage.SEVERITY_ERROR,
											"No existe información para la consulta realizada.", null));
						} else {
							if (list.isEmpty()) {
								FacesContext.getCurrentInstance().addMessage(null,
										new FacesMessage(FacesMessage.SEVERITY_ERROR,
												"No existe información para la consulta realizada.", null));
							} else {

								// CREO HEADER DE EXPORTACION SI ES NECESARIO PARA EXPORTAR A CSV
								if (this.exportToExcel) {
									// Header

									if (agrFec) {
										sb.append((char) 34).append("Fecha").append((char) 34).append(csvSepCamp);
									}

									if (agrProv) {
										sb.append((char) 34).append("ID Proveedor").append((char) 34)
												.append(csvSepCamp);
										sb.append((char) 34).append("Proveedor").append((char) 34).append(csvSepCamp);
									}

									if (agrupar != null && agrupar) {
										sb.append((char) 34).append("ID Cliente").append((char) 34).append(csvSepCamp);
										sb.append((char) 34).append("Cliente").append((char) 34).append(csvSepCamp);
									}

									if (nivelJerarquia >= 1 && this.getUsuario().getNivelDistribuidorSuperior() < 1) {
										sb.append((char) 34).append("ID Distribuidor 1").append((char) 34)
												.append(csvSepCamp);
										sb.append((char) 34).append("Distribuidor 1").append((char) 34)
												.append(csvSepCamp);
									}

									if (nivelJerarquia >= 2 && this.getUsuario().getNivelDistribuidorSuperior() < 2) {
										sb.append((char) 34).append("ID Distribuidor 2").append((char) 34)
												.append(csvSepCamp);
										sb.append((char) 34).append("Distribuidor 2").append((char) 34)
												.append(csvSepCamp);
									}

									if (nivelJerarquia >= 3 && this.getUsuario().getNivelDistribuidorSuperior() < 3) {
										sb.append((char) 34).append("ID Distribuidor 3").append((char) 34)
												.append(csvSepCamp);
										sb.append((char) 34).append("Distribuidor 3").append((char) 34)
												.append(csvSepCamp);
									}

									if (nivelJerarquia >= 4 && this.getUsuario().getNivelDistribuidorSuperior() < 4) {
										sb.append((char) 34).append("ID Distribuidor 4").append((char) 34)
												.append(csvSepCamp);
										sb.append((char) 34).append("Distribuidor 4").append((char) 34)
												.append(csvSepCamp);
									}

									if (nivelJerarquia >= 5 && this.getUsuario().getNivelDistribuidorSuperior() < 5) {
										sb.append((char) 34).append("ID Distribuidor 5").append((char) 34)
												.append(csvSepCamp);
										sb.append((char) 34).append("Distribuidor 5").append((char) 34)
												.append(csvSepCamp);
									}

									if (agrUsu) {
										sb.append((char) 34).append("ID Usuario").append((char) 34).append(csvSepCamp);
										sb.append((char) 34).append("Usuario").append((char) 34).append(csvSepCamp);
									}

									if (agrProd) {
										sb.append((char) 34).append("ID Producto").append((char) 34).append(csvSepCamp);
										sb.append((char) 34).append("Producto").append((char) 34).append(csvSepCamp);
									}

									if (agrCarTel) {
										sb.append((char) 34).append("CarTel").append((char) 34).append(csvSepCamp);
									}
									if (agrPais) {
										sb.append((char) 34).append("ID Pais").append((char) 34).append(csvSepCamp);
										sb.append((char) 34).append("Pais").append((char) 34).append(csvSepCamp);
									}
									if (agrProvincia) {
										sb.append((char) 34).append("ID Provincia").append((char) 34)
												.append(csvSepCamp);
										sb.append((char) 34).append("Provincia").append((char) 34).append(csvSepCamp);
									}
									if (agrLocalidad) {
										sb.append((char) 34).append("ID Localidad").append((char) 34)
												.append(csvSepCamp);
										sb.append((char) 34).append("Localidad").append((char) 34).append(csvSepCamp);
									}
									if (agrZona) {
										sb.append((char) 34).append("ID Zona").append((char) 34).append(csvSepCamp);
										sb.append((char) 34).append("Zona").append((char) 34).append(csvSepCamp);
										sb.append((char) 34).append("Alias de Zona").append((char) 34)
												.append(csvSepCamp);
									}

									if (this.getUsuario().getMostrarPantallasDeSube().compareTo(1) >= 0) {
										if (agrSube_DistribuidorRed) {
											sb.append((char) 34).append("SUBE ID Distribuidor Red").append((char) 34)
													.append(csvSepCamp);
											sb.append((char) 34).append("SUBE Distribuidor Red").append((char) 34)
													.append(csvSepCamp);
										}
									}

									sb.append((char) 34).append("Importe").append((char) 34).append(csvSepCamp);
									sb.append((char) 34).append("Cant.TRXS").append((char) 34).append(csvSepCamp);
									sb.append((char) 34).append("Trx.Prom").append((char) 34).append(csvSepCamp);

									if (agrTerm) {
										sb.append((char) 34).append("ID Terminal").append((char) 34).append(csvSepCamp);
										sb.append((char) 34).append("Terminal").append((char) 34).append(csvSepCamp);
									}

									if (agrModTerm) {
										sb.append((char) 34).append("Modelo de Terminal").append((char) 34)
												.append(csvSepCamp);
									}

									if (agrTipoTRX) {
										sb.append((char) 34).append("Tipo TRX").append((char) 34).append(csvSepCamp);
									}

									sb.append((char) 34).append("Estado").append((char) 34).append(csvSepCamp);

									if (agrLote) {
										sb.append((char) 34).append("ID Lote").append((char) 34).append(csvSepCamp);
									}

									if (agrIdentifTerminal) {
										sb.append((char) 34).append("Identif. Terminal").append((char) 34)
												.append(csvSepCamp);
									}

									if (agrImeiTerminal) {
										sb.append((char) 34).append("Imei Terminal").append((char) 34)
												.append(csvSepCamp);
									}

									sb.append((char) 13).append((char) 10);
								}

								for (InformeResumenTransacciones it : list) {

									// Sumo solo los que el estado = OK
									if ("Aprobada".equals(it.getDescripcionEstado().getValue())) {
										importeTotal += it.getImporte().getValue();
										cantidadOperaciones += it.getCantTRXs().getValue().intValue();
									}
									if (cantidadOperaciones > 0) {
										promedioTotal = (importeTotal / cantidadOperaciones);
									}

									// CREO ARCHIVO DE EXPORTACION SI ES NECESARIO PARA EXPORTAR A CSV
									if (this.exportToExcel) {

										if (cantRegistros == 0) {
											mostrarArchivoCSV = true;
										}

										if (agrFec) {
											sb.append((char) 34)
													.append((it.getFecha().getValue() == null
															|| "".equals(it.getFecha().getValue())) ? ""
																	: it.getFecha().getValue())
													.append((char) 34).append(csvSepCamp);
										}

										if (agrProv) {
											sb.append((char) 34);
											if(it.getIdProveedor().getValue() != null && it.getIdProveedor().getValue() != null) {
												sb.append(it.getIdProveedor().getValue());
											}
											sb.append((char) 34).append(csvSepCamp);
											sb.append((char) 34);
											if(it.getRazSocialProveedor().getValue() != null && it.getRazSocialProveedor().getValue() != null) {
												sb.append(it.getRazSocialProveedor().getValue());
											}
											sb.append((char) 34).append(csvSepCamp);
										}

										if (agrupar != null && agrupar) {
											sb.append((char) 34).append(it.getIdCliente().getValue()).append((char) 34)
													.append(csvSepCamp);
											sb.append((char) 34).append(it.getRazonSocialCliente().getValue())
													.append((char) 34).append(csvSepCamp);
										}

										if (nivelJerarquia >= 1
												&& this.getUsuario().getNivelDistribuidorSuperior() < 1) {
											sb.append((char) 34)
													.append((it.getIdDist1().getValue() != null
															&& it.getIdDist1().getValue() != 0) ? it.getIdDist1() : "")
													.append((char) 34).append(csvSepCamp);
											sb.append((char) 34)
													.append((it.getDescDist1().getValue() != null)
															? it.getDescDist1().getValue()
															: "")
													.append((char) 34).append(csvSepCamp);
										}

										if (nivelJerarquia >= 2
												&& this.getUsuario().getNivelDistribuidorSuperior() < 2) {
											sb.append((char) 34)
													.append((it.getIdDist2().getValue() != null
															&& it.getIdDist2().getValue() != 0)
																	? it.getIdDist2().getValue()
																	: "")
													.append((char) 34).append(csvSepCamp);
											sb.append((char) 34)
													.append((it.getDescDist2().getValue() != null)
															? it.getDescDist2().getValue()
															: "")
													.append((char) 34).append(csvSepCamp);
										}

										if (nivelJerarquia >= 3
												&& this.getUsuario().getNivelDistribuidorSuperior() < 3) {
											sb.append((char) 34)
													.append((it.getIdDist3().getValue() != null
															&& it.getIdDist3().getValue() != 0)
																	? it.getIdDist3().getValue()
																	: "")
													.append((char) 34).append(csvSepCamp);
											sb.append((char) 34)
													.append((it.getDescDist3().getValue() != null)
															? it.getDescDist3().getValue()
															: "")
													.append((char) 34).append(csvSepCamp);
										}

										if (nivelJerarquia >= 4
												&& this.getUsuario().getNivelDistribuidorSuperior() < 4) {
											sb.append((char) 34)
													.append((it.getIdDist4().getValue() != null
															&& it.getIdDist4().getValue() != 0)
																	? it.getIdDist4().getValue()
																	: "")
													.append((char) 34).append(csvSepCamp);
											sb.append((char) 34)
													.append((it.getDescDist4().getValue() != null)
															? it.getDescDist4().getValue()
															: "")
													.append((char) 34).append(csvSepCamp);
										}

										if (nivelJerarquia >= 5
												&& this.getUsuario().getNivelDistribuidorSuperior() < 5) {
											sb.append((char) 34)
													.append((it.getIdDist5().getValue() != null
															&& it.getIdDist5().getValue() != 0)
																	? it.getIdDist5().getValue()
																	: "")
													.append((char) 34).append(csvSepCamp);
											sb.append((char) 34).append(
													(it.getDescDist5() != null) ? it.getDescDist5().getValue() : "")
													.append((char) 34).append(csvSepCamp);
										}

										if (agrUsu) {

											if (it.getIdUsuario().getValue() != null) {
												sb.append((char) 34).append(it.getIdUsuario().getValue())
														.append((char) 34).append(csvSepCamp);
											} else {
												sb.append((char) 34).append((char) 34).append(csvSepCamp);
											}
											if (it.getUsuario().getValue() != null
													&& !"".equals(it.getUsuario().getValue())) {
												sb.append((char) 34).append(it.getUsuario().getValue())
														.append((char) 34).append(csvSepCamp);
											} else {
												sb.append((char) 34).append((char) 34).append(csvSepCamp);
											}

										}

										if (agrProd) {
											if (it.getIdProducto().getValue() != null) {
												sb.append((char) 34).append(it.getIdProducto().getValue())
														.append((char) 34).append(csvSepCamp);
											} else {
												sb.append((char) 34).append((char) 34).append(csvSepCamp);
											}
											if (it.getDescripcionProducto().getValue() != null
													&& !"".equals(it.getDescripcionProducto().getValue())) {
												sb.append((char) 34).append(it.getDescripcionProducto().getValue())
														.append((char) 34).append(csvSepCamp);
											} else {
												sb.append((char) 34).append((char) 34).append(csvSepCamp);
											}
										}

										if (agrCarTel) {
											sb.append((char) 34).append(it.getCaracteristicaTelefono().getValue())
													.append((char) 34).append(csvSepCamp);
										}

										if (agrPais) {
											sb.append((char) 34)
													.append((it.getIdPais().getValue() != null
															&& it.getIdPais().getValue() != 0)
																	? it.getIdPais().getValue()
																	: "")
													.append((char) 34).append(csvSepCamp);
											sb.append((char) 34)
													.append((it.getDescripcionPais().getValue() != null)
															? it.getDescripcionPais().getValue()
															: "")
													.append((char) 34).append(csvSepCamp);
										}

										if (agrProvincia) {
											sb.append((char) 34)
													.append((it.getIdProvincia().getValue() != null
															&& it.getIdProvincia().getValue() != 0)
																	? it.getIdProvincia().getValue()
																	: "")
													.append((char) 34).append(csvSepCamp);
											sb.append((char) 34)
													.append((it.getDescripcionProvincia().getValue() != null)
															? it.getDescripcionProvincia().getValue()
															: "")
													.append((char) 34).append(csvSepCamp);
										}

										if (agrLocalidad) {
											sb.append((char) 34)
													.append((it.getIdLocalidad().getValue() != null
															&& it.getIdLocalidad().getValue() != 0)
																	? it.getIdLocalidad().getValue()
																	: "")
													.append((char) 34).append(csvSepCamp);
											sb.append((char) 34)
													.append((it.getDescripcionLocalidad().getValue() != null)
															? it.getDescripcionLocalidad().getValue()
															: "")
													.append((char) 34).append(csvSepCamp);
										}

										if (agrZona) {
											sb.append((char) 34)
													.append((it.getIdZona().getValue() != null
															&& it.getIdZona().getValue() != 0)
																	? it.getIdZona().getValue()
																	: "")
													.append((char) 34).append(csvSepCamp);
											sb.append((char) 34)
													.append((it.getDescripcionZona().getValue() != null)
															? it.getDescripcionZona().getValue()
															: "")
													.append((char) 34).append(csvSepCamp);
											sb.append((char) 34)
													.append((it.getAliasZona().getValue() != null)
															? it.getAliasZona().getValue()
															: "")
													.append((char) 34).append(csvSepCamp);
										}

										if (this.getUsuario().getMostrarPantallasDeSube().compareTo(1) >= 0) {
											if (agrSube_DistribuidorRed) {
												if (it.getSubeIdDistribuidorRed().getValue() != null) {
													sb.append((char) 34)
															.append(it.getSubeIdDistribuidorRed().getValue())
															.append((char) 34).append(csvSepCamp);
												} else {
													sb.append((char) 34).append((char) 34).append(csvSepCamp);
												}
												if (it.getSubeDistribuidorRedDescripcion().getValue() != null && !""
														.equals(it.getSubeDistribuidorRedDescripcion().getValue())) {
													sb.append((char) 34)
															.append(it.getSubeDistribuidorRedDescripcion().getValue())
															.append((char) 34).append(csvSepCamp);
												} else {
													sb.append((char) 34).append((char) 34).append(csvSepCamp);
												}
											}
										}
										sb.append((char) 34).append(it.getImporte().getValue().toString()
												.replace(".", csvSepDec).replace(",", csvSepDec)).append((char) 34)
												.append(csvSepCamp);
										sb.append((char) 34).append(it.getCantTRXs().getValue()).append((char) 34)
												.append(csvSepCamp);
										sb.append((char) 34)
												.append(it.getValorPromedio().getValue().toString()
														.replace(".", csvSepDec).replace(",", csvSepDec))
												.append((char) 34).append(csvSepCamp);

										if (agrTerm) {
											if (it.getIdTipoTerminal().getValue() != null) {
												sb.append((char) 34).append(it.getIdTipoTerminal().getValue())
														.append((char) 34).append(csvSepCamp);
											} else {
												sb.append((char) 34).append((char) 34).append(csvSepCamp);
											}
											if (it.getDescTipoTerminal().getValue() != null
													&& !"".equals(it.getDescTipoTerminal().getValue())) {
												sb.append((char) 34).append(it.getDescTipoTerminal().getValue())
														.append((char) 34).append(csvSepCamp);
											} else {
												sb.append((char) 34).append((char) 34).append(csvSepCamp);
											}
										}

										if (agrModTerm) {
											if (it.getModeloDeTerminal().getValue() != null
													&& !"".equals(it.getModeloDeTerminal().getValue())) {
												sb.append((char) 34).append(it.getModeloDeTerminal().getValue())
														.append((char) 34).append(csvSepCamp);
											} else {
												sb.append((char) 34).append((char) 34).append(csvSepCamp);
											}
										}

										if (agrTipoTRX) {
											sb.append((char) 34).append(it.getTipoTRX().getValue()).append((char) 34)
													.append(csvSepCamp);
										}

										sb.append((char) 34).append(it.getDescripcionEstado().getValue())
												.append((char) 34).append(csvSepCamp);

										if (agrLote) {
											sb.append((char) 34).append(it.getIdLote().getValue()).append((char) 34)
													.append(csvSepCamp);
										}

										if (agrIdentifTerminal) {
											sb.append((char) 34).append(it.getIdentifTerminal().getValue())
													.append((char) 34).append(csvSepCamp);
										}

										if (agrImeiTerminal) {
											if (it.getImeiTerminal().getValue() != null
													&& !"".equals(it.getImeiTerminal().getValue())) {
												sb.append((char) 34).append(it.getImeiTerminal().getValue())
														.append((char) 34).append(csvSepCamp);
											} else {
												sb.append((char) 34).append((char) 34).append(csvSepCamp);
											}
										}

										sb.append((char) 13).append((char) 10);
									}

									// Cantidad total de Registros devueltos
									cantRegistros += 1;

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
													+ this.getUsuario().getIdMayorista() + ")_" + "ResumenTRXS.csv"
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
				}
			}

			String DATE_FORMAT = "dd/MM/yyyy HH:mm:ss";
			SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);
			fechaHoraDesdeMostrar = sdf.format(fechaHoraDesde);
			fechaHoraHastaMostrar = sdf.format(fechaHoraHasta);

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
							"Informe Transacciones Resumido. Error ejecutando el WS de consulta: |" + ste.getMessage()
									+ "|");
					FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
							"Error ejecutando el WS de consulta: |" + ste.getMessage() + "|", null));
				}
			} else {
				LogACGHelper.escribirLog(null, "Informe Transacciones Resumido. Error ejecutando el WS de consulta: |"
						+ ste.getMessage() + "|");
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
						"Error ejecutando el WS de consulta: |" + ste.getMessage() + "|", null));
			}
			FacesContext.getCurrentInstance().validationFailed();
			this.list = null;
		} catch (Exception e) {
			LogACGHelper.escribirLog(null,
					"Informe Transacciones Resumido. Excepcion ejecutando el WS de consulta: |" + e.getMessage() + "|");
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
					"Error ejecutando el WS de consulta de Transacciones: |" + e.getMessage() + "|", null));
			FacesContext.getCurrentInstance().validationFailed();
			this.list = null;
		}
		PrimeFaces.current().executeScript("PF('blockPanelGral').hide()");
		return;
	}

	public List<SelectItem> getProvincias() {
		try {
			if (this.provincias == null) {
				this.provincias = new ArrayList<SelectItem>();
				this.localidades = new ArrayList<SelectItem>();

				if ((idPais != null) && (idPais > 0)) {
					DescripcionContainer cd = GestionServiceHelper.getGestionService(CfgTimeout.REPORTE)
							.mostrarProvincias(this.getUsuario().getIdMayorista(), null, idPais);
					if ((cd != null) && (!cd.getListDescripcion().getValue().getDescripcion().isEmpty())) {
						for (Descripcion d : cd.getListDescripcion().getValue().getDescripcion()) {
							this.provincias.add(new SelectItem(d.getId().getValue(), d.getDescripcion().getValue()));
						}
					}
				}
			}
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
					"Error en lectura de Provincias RSC: " + e.getMessage(), null));
		}
		return provincias;
	}

	public void setProvincias(List<SelectItem> provincias) {
		this.provincias = provincias;
	}

	public List<SelectItem> getLocalidades() {

		try {
			this.localidades = new ArrayList<SelectItem>();
			if ((idProvincia != null) && (idProvincia > 0)) {
				DescripcionContainer cd = GestionServiceHelper.getGestionService(CfgTimeout.REPORTE)
						.mostrarLocalidadesPorProv(this.getUsuario().getIdMayorista(), idProvincia, null);

				if ((cd != null) && (!cd.getListDescripcion().getValue().getDescripcion().isEmpty())) {
					for (Descripcion d : cd.getListDescripcion().getValue().getDescripcion()) {
						this.localidades.add(new SelectItem(d.getId().getValue(), d.getDescripcion().getValue()));
					}
				}
			}
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
					"Error en lectura de Localidades RSC: " + e.getMessage(), null));
		}

		return localidades;
	}

	public void setLocalidades(List<SelectItem> localidades) {
		this.localidades = localidades;
	}

	public List<SelectItem> getZonas() {
		return zonas;
	}

	public void setZonas(List<SelectItem> zonas) {
		this.zonas = zonas;
	}

	public Boolean getAgrZona() {
		return agrZona;
	}

	public void setAgrZona(Boolean agrZona) {
		this.agrZona = agrZona;
	}

	public Boolean getAgrPais() {
		return agrPais;
	}

	public void setAgrPais(Boolean agrPais) {
		this.agrPais = agrPais;
	}

	public Long getIdPais() {
		return idPais;
	}

	public void setIdPais(Long idPais) {
		this.idPais = idPais;
	}

	public List<SelectItem> getPaises() {
		return paises;
	}

	public void setPaises(List<SelectItem> paises) {
		this.paises = paises;
	}

	public void onChangePais(ActionEvent evt) {
		idProvincia = null;
		idLocalidad = null;
	}

	public void onChangeProvincia(ActionEvent evt) {
		idLocalidad = null;
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

	public Boolean getAgrupar() {
		return agrupar;
	}

	public void setAgrupar(Boolean agrupar) {
		this.agrupar = agrupar;
	}

	public Float getPromedioTotal() {
		return promedioTotal;
	}

	public void setPromedioTotal(Float promedioTotal) {
		this.promedioTotal = promedioTotal;
	}

	public Long getIdUsuario() {
		return idUsuario;
	}

	public void setIdUsuario(Long idUsuario) {
		this.idUsuario = idUsuario;
	}

	public Boolean getAgrUsu() {
		return agrUsu;
	}

	public void setAgrUsu(Boolean agrUsu) {
		this.agrUsu = agrUsu;
	}

	public Boolean getAgrProv() {
		return agrProv;
	}

	public void setAgrProv(Boolean agrProv) {
		this.agrProv = agrProv;
	}

	public Boolean getAgrFec() {
		return agrFec;
	}

	public void setAgrFec(Boolean agrFec) {
		this.agrFec = agrFec;
	}

	public Integer getCaracteristicaTelefono() {
		return caracteristicaTelefono;
	}

	public void setCaracteristicaTelefono(Integer caracteristicaTelefono) {
		this.caracteristicaTelefono = caracteristicaTelefono;
	}

	public Boolean getAgrCarTel() {
		return agrCarTel;
	}

	public void setAgrCarTel(Boolean agrCarTel) {
		this.agrCarTel = agrCarTel;
	}

	public Boolean getAgrProd() {
		return agrProd;
	}

	public void setAgrProd(Boolean agrProd) {
		this.agrProd = agrProd;
	}

	public Boolean getAgrTipoTRX() {
		return agrTipoTRX;
	}

	public void setAgrTipoTRX(Boolean agrTipoTRX) {
		this.agrTipoTRX = agrTipoTRX;
	}

	public Boolean getAgrTerm() {
		return agrTerm;
	}

	public void setAgrTerm(Boolean agrTerm) {
		this.agrTerm = agrTerm;
	}

	public List<SelectItem> getTipoTerminales() {
		return tipoTerminales;
	}

	public void setTipoTerminales(List<SelectItem> tipoTerminales) {
		this.tipoTerminales = tipoTerminales;
	}

	public Boolean getInfHistorico() {
		return infHistorico;
	}

	public void setInfHistorico(Boolean infHistorico) {
		this.infHistorico = infHistorico;
	}

	public Integer getCantidadOperaciones() {
		return cantidadOperaciones;
	}

	public void setCantidadOperaciones(Integer cantidadOperaciones) {
		this.cantidadOperaciones = cantidadOperaciones;
	}

	public Integer getCantRegistros() {
		return cantRegistros;
	}

	public void setCantRegistros(Integer cantRegistros) {
		this.cantRegistros = cantRegistros;
	}

	public Long getIdLote() {
		return idLote;
	}

	public void setIdLote(Long idLote) {
		this.idLote = idLote;
	}

	public Boolean getAgrLote() {
		return agrLote;
	}

	public void setAgrLote(Boolean agrLote) {
		this.agrLote = agrLote;
	}

	public String getIdentifTerminal() {
		return identifTerminal;
	}

	public void setIdentifTerminal(String identifTerminal) {
		this.identifTerminal = identifTerminal;
	}

	public Boolean getAgrIdentifTerminal() {
		return agrIdentifTerminal;
	}

	public void setAgrIdentifTerminal(Boolean agrIdentifTerminal) {
		this.agrIdentifTerminal = agrIdentifTerminal;
	}

	public Integer getNivelJerarquia() {
		return nivelJerarquia;
	}

	public void setNivelJerarquia(Integer nivelJerarquia) {
		this.nivelJerarquia = nivelJerarquia;
	}

	public Boolean getAgrModTerm() {
		return agrModTerm;
	}

	public void setAgrModTerm(Boolean agrModTerm) {
		this.agrModTerm = agrModTerm;
	}

	public String getImeiTerminal() {
		return imeiTerminal;
	}

	public void setImeiTerminal(String imeiTerminal) {
		this.imeiTerminal = imeiTerminal;
	}

	public Boolean getAgrImeiTerminal() {
		return agrImeiTerminal;
	}

	public void setAgrImeiTerminal(Boolean agrImeiTerminal) {
		this.agrImeiTerminal = agrImeiTerminal;
	}

	public List<SelectItem> getNivelesJerarquia() {
		return nivelesJerarquia;
	}

	public void setNivelesJerarquia(List<SelectItem> nivelesJerarquia) {
		this.nivelesJerarquia = nivelesJerarquia;
	}

	public Long getIdProvincia() {
		return idProvincia;
	}

	public void setIdProvincia(Long idProvincia) {
		this.idProvincia = idProvincia;
	}

	public Long getIdLocalidad() {
		return idLocalidad;
	}

	public void setIdLocalidad(Long idLocalidad) {
		this.idLocalidad = idLocalidad;
	}

	public Long getIdZona() {
		return idZona;
	}

	public void setIdZona(Long idZona) {
		this.idZona = idZona;
	}

	public Boolean getAgrProvincia() {
		return agrProvincia;
	}

	public void setAgrProvincia(Boolean agrProvincia) {
		this.agrProvincia = agrProvincia;
	}

	public Boolean getAgrLocalidad() {
		return agrLocalidad;
	}

	public void setAgrLocalidad(Boolean agrLocalidad) {
		this.agrLocalidad = agrLocalidad;
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

	public Boolean getAgrSube_DistribuidorRed() {
		return agrSube_DistribuidorRed;
	}

	public void setAgrSube_DistribuidorRed(Boolean agrSube_DistribuidorRed) {
		this.agrSube_DistribuidorRed = agrSube_DistribuidorRed;
	}

}
