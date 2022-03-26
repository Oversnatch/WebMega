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
import javax.faces.event.ActionEvent;
import javax.inject.Named;
import javax.xml.ws.WebServiceException;
import org.primefaces.PrimeFaces;
import org.springframework.context.annotation.Scope;
import com.americacg.cargavirtual.gestion.javax.faces.model.ArrayOfSelectItem;
import com.americacg.cargavirtual.gestion.javax.faces.model.SelectItem;
import com.americacg.cargavirtual.gestion.model.ArrayOfInformeSaldoClientes;
import com.americacg.cargavirtual.gestion.model.ConfigGral;
import com.americacg.cargavirtual.gestion.model.Descripcion;
import com.americacg.cargavirtual.gestion.model.DescripcionContainer;
import com.americacg.cargavirtual.gestion.model.GeoCoordenadas;
import com.americacg.cargavirtual.gestion.model.InformeSaldoClientes;
import com.americacg.cargavirtual.gestion.javax.faces.model.ObjectFactory;
import com.americacg.cargavirtual.gestion.model.RespString;
import com.americacg.cargavirtual.gestion.model.ResultadoBase;
import com.americacg.cargavirtual.web.enums.CfgTimeout;
import com.americacg.cargavirtual.web.helpers.ACGFormatHelper;
import com.americacg.cargavirtual.web.helpers.GestionServiceHelper;
import com.americacg.cargavirtual.web.helpers.LogACGHelper;
import com.americacg.cargavirtual.web.model.Error;

@Named("reporteSaldoClientesView")
@Scope(org.springframework.web.context.WebApplicationContext.SCOPE_SESSION)
public class ReporteSaldoClientesView extends ReporteGeneral<InformeSaldoClientes> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3263006276047720471L;
	private Long idClienteInferior;

	private Integer tipoFiltroCliente;

	private Float saldoMinimo;
	private Long idTipoTerminal;
	private Long idProvincia;
	private List<javax.faces.model.SelectItem> provincias;
	private Long idLocalidad;
	private List<javax.faces.model.SelectItem> localidades;
	private Long idPais;
	private List<javax.faces.model.SelectItem> paises;
	private Long idRubro;
	private List<javax.faces.model.SelectItem> rubros;
	private Long idZona;
	private List<javax.faces.model.SelectItem> zonas;
	private Boolean agrTerminal = false;
	private Boolean agrProvincia = false;
	private Boolean agrLocalidad = false;
	private Boolean agrPais = false;
	private Boolean agrRubro = false;
	private Boolean agrDetalles = false;
	private Boolean agrZonas = false;
	private Boolean agrJerarquia = false;
	private Boolean agrFechaAlta = false;
	private String estado;
	private String tipoCliente;
	private Boolean validarDatosCliente = false;
	private Boolean geolocalizarTodo = false;
	private Integer cantRegAvalidar;

	private Long idClienteAmodificar;
	private String modifOK = "";

	private List<javax.faces.model.SelectItem> tipoTerminales;

	private String direccionGeolocalizacion = "";
	private String clienteGeolocalizacion = "";
	private String tipoClienteGeolocalizacion = "";
	private Double geo_latitud_delCliente = 0D;
	private Double geo_longitud_delCliente = 0D;

	private Boolean agregarEstadoCuentaCorrienteDelCliente;
	private Boolean agregarEstadoCuentaCorrienteDeCajaParaElCliente;
	private Boolean agregarSaldoWU;

	private String pathLecturaImages;

	private String idClienteExterno;

	// TODO: ver que pasa con este idCliente si hay que tomarlo del usuario
	// lo agregue para poder compilar
	// private Long idCliente = null;

	private InformeSaldoClientes informeSaldoClientesSeleccionado;
	private String informeSaldoClientesSeleccionadoImagenDelLocal = "/images/pixel_blanco.jpg";

	@Override
	public Error resetearReporte() {
		Error e = super.resetearReporte();
		this.idClienteInferior = null;
		this.idTipoTerminal = null;
		this.tipoFiltroCliente = 3;
		this.saldoMinimo = null;
		this.idProvincia = null;
		this.idLocalidad = null;
		this.idPais = null;
		this.idRubro = null;
		this.idZona = null;
		this.agrTerminal = false;
		this.agrProvincia = false;
		this.agrLocalidad = false;
		this.agrPais = false;
		this.estado = null;
		this.tipoCliente = null;
		this.list = null;
		this.agrRubro = false;
		this.agrDetalles = false;
		this.agrZonas = false;
		this.agrFechaAlta = false;
		this.agrJerarquia = false;
		this.validarDatosCliente = null;
		this.geolocalizarTodo = false;
		this.cantRegistros = 0;
		this.cantRegAvalidar = 0;
		this.agregarEstadoCuentaCorrienteDelCliente = false;
		this.agregarEstadoCuentaCorrienteDeCajaParaElCliente = false;
		this.agregarSaldoWU = false;
		this.idClienteExterno = null;

		try {

			this.tipoTerminales = new ArrayList<javax.faces.model.SelectItem>();
			DescripcionContainer cd = GestionServiceHelper.getGestionService(CfgTimeout.REPORTE)
					.mostrarTipoTerminales(this.getUsuario().getIdMayorista());

			for (Descripcion d : cd.getListDescripcion().getValue().getDescripcion()) {
				this.tipoTerminales
						.add(new javax.faces.model.SelectItem(d.getId().getValue(), d.getDescripcion().getValue()));
			}
			this.rubros = new ArrayList<javax.faces.model.SelectItem>();
			DescripcionContainer r = GestionServiceHelper.getGestionService(CfgTimeout.REPORTE)
					.mostrarRubros(this.getUsuario().getIdMayorista(), null);

			for (Descripcion d : r.getListDescripcion().getValue().getDescripcion()) {
				this.rubros.add(new javax.faces.model.SelectItem(d.getId().getValue(), d.getDescripcion().getValue()));
			}
			this.provincias = new ArrayList<javax.faces.model.SelectItem>();
			this.localidades = new ArrayList<javax.faces.model.SelectItem>();

			this.paises = new ArrayList<javax.faces.model.SelectItem>();
			DescripcionContainer p = GestionServiceHelper.getGestionService(CfgTimeout.REPORTE)
					.mostrarPaises(this.getUsuario().getIdMayorista(), null);

			if ((p != null) && (!p.getListDescripcion().getValue().getDescripcion().isEmpty())) {
				for (Descripcion d : p.getListDescripcion().getValue().getDescripcion()) {
					this.paises
							.add(new javax.faces.model.SelectItem(d.getId().getValue(), d.getDescripcion().getValue()));
				}
			}

			this.zonas = new ArrayList<javax.faces.model.SelectItem>();
			DescripcionContainer z = GestionServiceHelper.getGestionService(CfgTimeout.REPORTE)
					.mostrarZonas(this.getUsuario().getIdMayorista(), null);

			for (Descripcion d : z.getListDescripcion().getValue().getDescripcion()) {
				this.zonas.add(new javax.faces.model.SelectItem(d.getId().getValue(), d.getDescripcion().getValue()));
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
					LogACGHelper.escribirLog(null,
							"No se pudo establecer la comunicación (GST-HNC): |" + ste.getMessage() + "|");
					e.setError("GST-TRW",
							"No se pudo establecer la comunicación (GST-HNC).\n Por favor intente nuevamente.");
				} else {
					LogACGHelper.escribirLog(null,
							"Informe Saldo Clientes. Error ejecutando el WS de consulta: |" + ste.getMessage() + "|");
					e.setError("GST-OTR",
							"No se pudo establecer la comunicación (GST-OTR).\n Por favor intente nuevamente.");
				}
			} else {
				LogACGHelper.escribirLog(null,
						"Informe Saldo Clientes. Error ejecutando el WS de consulta: |" + ste.getMessage() + "|");
				e.setError("GST-OTR",
						"No se pudo establecer la comunicación (GST-OTR).\n Por favor intente nuevamente.");
			}
			FacesContext.getCurrentInstance().validationFailed();
		} catch (Exception ex) {
			LogACGHelper.escribirLog(null,
					"Informe Saldo Clientes. Excepcion ejecutando el WS de consulta: |" + ex.getMessage() + "|");
			e.setError("GST-OTR", "No se pudo establecer la comunicación (GST-OTR).\n Por favor intente nuevamente.");
		}
		return e;
	}

	public ReporteSaldoClientesView() {
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

			cantRegistros = 0;
			cantRegAvalidar = 0;

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

				Boolean log;

				/*
				 * log = escribirLog(null, "Informe SaldoClientes --> idMayorista: |" +
				 * idMayorista + "| idCliente: |" + idCliente + "| nivelDistribuidorSuperior: |"
				 * + nivelDistribuidorSuperior + "| idClienteInferior: |" + idClienteInferior +
				 * "| saldoMinimo: |" + saldoMinimo + "|tipoFiltroCliente: |" +
				 * tipoFiltroCliente +"|");
				 */

				// Busco el path de las imagenes
				ConfigGral cg = GestionServiceHelper.getGestionService(CfgTimeout.REPORTE)
						.mostrarConfigGral(this.getUsuario().getIdMayorista());
				pathLecturaImages = cg.getPathLecturaImages().getValue();

				// Listo los clientes con sus saldos y todos sus datos
				ArrayOfInformeSaldoClientes listsc = GestionServiceHelper.getGestionService(CfgTimeout.REPORTE)
						.informeSaldoClientes(this.getUsuario().getIdMayorista(), this.getUsuario().getIdCliente(),
								this.getUsuario().getNivelDistribuidorSuperior(), idClienteInferior, idTipoTerminal,
								tipoFiltroCliente, saldoMinimo, idProvincia, idLocalidad, agrTerminal, agrProvincia,
								agrLocalidad, estado, tipoCliente, agrRubro, idRubro, idZona, validarDatosCliente,
								agrPais, idPais, agregarEstadoCuentaCorrienteDelCliente,
								agregarEstadoCuentaCorrienteDeCajaParaElCliente, agregarSaldoWU, idClienteExterno);

				// GENERO Y LIMPIO LAS VARIABLES PARA LA EXPORTACION
				// this.exportToCSV.setExportText("");
				// this.exportToCSV.setFileName("");
				mostrarArchivoCSV = false;

				String csvSepCamp = this.getUsuario().getCsvSeparadorCampo();
				String csvSepDec = this.getUsuario().getCsvSeparadorDecimales();

				StringBuilder sb = new StringBuilder();

				if (listsc == null || listsc.getInformeSaldoClientes() == null
						|| listsc.getInformeSaldoClientes().isEmpty()) {
					FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
							"No se ha encontrado ningun registro con ese criterio.", null));
				} else {
					cantRegistros = listsc.getInformeSaldoClientes().size();
					this.list = listsc.getInformeSaldoClientes();
					Integer i = 0;

					if ((validarDatosCliente != null) && (validarDatosCliente)) {
						// SI ESTOY VALIDANDO CLIENTE, RECORRO LA LISTA Y CARGO LA LISTA DE PROVINCIAS Y
						// LOCALIDADES PARA LA PROVINCIA DE CADA CLIENTE

						ObjectFactory objectFactory = new ObjectFactory();

						for (InformeSaldoClientes iss : listsc.getInformeSaldoClientes()) {

							if (iss.getIdClienteValid().getValue() != null) {
								if (iss.getIdClienteValid().getValue() > 0) {
									cantRegAvalidar++;

									// Provincias
									ArrayOfSelectItem provinciasAux = new ArrayOfSelectItem();
									DescripcionContainer cdprov = GestionServiceHelper
											.getGestionService(CfgTimeout.REPORTE)
											.mostrarProvincias(this.getUsuario().getIdMayorista(), null,
													iss.getIdPaisValid().getValue());

									for (Descripcion d : cdprov.getListDescripcion().getValue().getDescripcion()) {
										SelectItem si = new SelectItem();
										si.setValue(objectFactory.createSelectItemValue(d.getId().getValue()));
										si.setLabel(objectFactory.createSelectItemLabel(d.getDescripcion().getValue()));
										provinciasAux.getSelectItem().add(si);
									}

									iss.getListProvinciasValid().setValue(provinciasAux);

									// Localidades
									ArrayOfSelectItem localidadesAux = new ArrayOfSelectItem();
									DescripcionContainer cdloc = GestionServiceHelper
											.getGestionService(CfgTimeout.REPORTE)
											.mostrarLocalidadesPorProv(this.getUsuario().getIdMayorista(),
													iss.getIdProvinciaValid().getValue(), null);

									for (Descripcion d : cdloc.getListDescripcion().getValue().getDescripcion()) {
										SelectItem si = new SelectItem();
										si.setValue(objectFactory.createSelectItemValue(d.getId().getValue()));
										si.setLabel(objectFactory.createSelectItemLabel(d.getDescripcion().getValue()));
										localidadesAux.getSelectItem().add(si);
									}

									iss.getListLocalidadesValid().setValue(localidadesAux);
								}
							}
						}

						if (cantRegAvalidar <= 0) {
							FacesContext.getCurrentInstance().addMessage(null,
									new FacesMessage(FacesMessage.SEVERITY_ERROR,
											"No se encontraron clientes pendiente de validacion", null));
						}

					} else {

						// CREO HEADER DE EXPORTACION SI ES NECESARIO PARA EXPORTAR A CSV
						if (this.exportToExcel) {
							// Header
							sb.append((char) 34).append("Fecha Actual").append((char) 34).append(csvSepCamp);

							if (agrFechaAlta) {
								sb.append((char) 34).append("Fecha Alta").append((char) 34).append(csvSepCamp);
							}

							sb.append((char) 34).append("ID Cliente").append((char) 34).append(csvSepCamp);
							sb.append((char) 34).append("Cliente").append((char) 34).append(csvSepCamp);

							sb.append((char) 34).append("Nro Contrato").append((char) 34).append(csvSepCamp);

							if (agrDetalles) {
								sb.append((char) 34).append("CUIT").append((char) 34).append(csvSepCamp);
								sb.append((char) 34).append("Calle").append((char) 34).append(csvSepCamp);
								sb.append((char) 34).append("Altura").append((char) 34).append(csvSepCamp);
								sb.append((char) 34).append("Latitud").append((char) 34).append(csvSepCamp);
								sb.append((char) 34).append("Longitud").append((char) 34).append(csvSepCamp);
								sb.append((char) 34).append("Telefono 1").append((char) 34).append(csvSepCamp);
								sb.append((char) 34).append("Telefono 2").append((char) 34).append(csvSepCamp);
								sb.append((char) 34).append("Telefono 3").append((char) 34).append(csvSepCamp);
								sb.append((char) 34).append("Mail").append((char) 34).append(csvSepCamp);
								sb.append((char) 34).append("Fax").append((char) 34).append(csvSepCamp);
								sb.append((char) 34).append("Partido").append((char) 34).append(csvSepCamp);
								sb.append((char) 34).append("Barrio").append((char) 34).append(csvSepCamp);
								sb.append((char) 34).append("Piso").append((char) 34).append(csvSepCamp);
								sb.append((char) 34).append("Depto").append((char) 34).append(csvSepCamp);
								sb.append((char) 34).append("Entre Calle 1").append((char) 34).append(csvSepCamp);
								sb.append((char) 34).append("Entre Calle 2").append((char) 34).append(csvSepCamp);
								sb.append((char) 34).append("Local").append((char) 34).append(csvSepCamp);
								sb.append((char) 34).append("Tipo De Centro").append((char) 34).append(csvSepCamp);
								sb.append((char) 34).append("Ubicacion").append((char) 34).append(csvSepCamp);
								sb.append((char) 34).append("Tipo de Ubicacion").append((char) 34).append(csvSepCamp);
								sb.append((char) 34).append("Comuna").append((char) 34).append(csvSepCamp);
								sb.append((char) 34).append("Jurisdiccion").append((char) 34).append(csvSepCamp);
								sb.append((char) 34).append("Casa").append((char) 34).append(csvSepCamp);
								sb.append((char) 34).append("Lote").append((char) 34).append(csvSepCamp);
								sb.append((char) 34).append("Manzana").append((char) 34).append(csvSepCamp);
								sb.append((char) 34).append("SN Sube Carga Dif").append((char) 34).append(csvSepCamp);
								sb.append((char) 34).append("ID Unico Cliente").append((char) 34).append(csvSepCamp);
								sb.append((char) 34).append("ID Movistar").append((char) 34).append(csvSepCamp);
								sb.append((char) 34).append("ID Claro").append((char) 34).append(csvSepCamp);
								sb.append((char) 34).append("ID Personal").append((char) 34).append(csvSepCamp);
								sb.append((char) 34).append("ID Nextel").append((char) 34).append(csvSepCamp);
								//sb.append((char) 34).append("SUBE CD LocationID").append((char) 34).append(csvSepCamp);
								//sb.append((char) 34).append("SUBE CD PosID").append((char) 34).append(csvSepCamp);
								sb.append((char) 34).append("IDs UnifCli SUBE CD LocationID").append((char) 34).append(csvSepCamp);
								sb.append((char) 34).append("IDs UnifCli SUBE CD PosID").append((char) 34).append(csvSepCamp);
								sb.append((char) 34).append("IDs UnifCli SUBE CD TerminalID").append((char) 34).append(csvSepCamp);
							}

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

							if (agrRubro) {
								sb.append((char) 34).append("ID Rubro").append((char) 34).append(csvSepCamp);
								sb.append((char) 34).append("Rubro").append((char) 34).append(csvSepCamp);
							}

							sb.append((char) 34).append("Tipo Cliente").append((char) 34).append(csvSepCamp);
							sb.append((char) 34).append("ID Cliente Externo").append((char) 34).append(csvSepCamp);
							sb.append((char) 34).append("ID Zona").append((char) 34).append(csvSepCamp);
							sb.append((char) 34).append("Zona").append((char) 34).append(csvSepCamp);

							if (agrPais) {
								sb.append((char) 34).append("ID Pais").append((char) 34).append(csvSepCamp);
								sb.append((char) 34).append("Pais").append((char) 34).append(csvSepCamp);
							}

							if (agrProvincia) {
								sb.append((char) 34).append("ID Provincia").append((char) 34).append(csvSepCamp);
								sb.append((char) 34).append("Provincia").append((char) 34).append(csvSepCamp);
							}

							if (agrLocalidad) {
								sb.append((char) 34).append("ID Localidad").append((char) 34).append(csvSepCamp);
								sb.append((char) 34).append("Localidad").append((char) 34).append(csvSepCamp);
								sb.append((char) 34).append("CP").append((char) 34).append(csvSepCamp);
							}

							if (agrTerminal) {
								sb.append((char) 34).append("ID Terminal").append((char) 34).append(csvSepCamp);
								sb.append((char) 34).append("Terminal").append((char) 34).append(csvSepCamp);
							}

							sb.append((char) 34).append("Estado").append((char) 34).append(csvSepCamp);
							sb.append((char) 34).append("Bolsa").append((char) 34).append(csvSepCamp);

							if (this.getUsuario().getHabilitarWU() && agregarSaldoWU) {
								sb.append((char) 34).append("Bolsa WU").append((char) 34).append(csvSepCamp);
							}

							if (agregarEstadoCuentaCorrienteDelCliente) {
								sb.append((char) 34).append("Estado CC Cliente").append((char) 34).append(csvSepCamp);
							}

							if (agregarEstadoCuentaCorrienteDeCajaParaElCliente) {
								sb.append((char) 34).append("Estado CC Caja").append((char) 34).append(csvSepCamp);
							}

							sb.append((char) 34).append("ID Lista de Comision Asignada").append((char) 34)
									.append(csvSepCamp);
							sb.append((char) 34).append("Lista de Comision Asignada").append((char) 34)
									.append(csvSepCamp);

							if ("M".equals(this.getUsuario().getTipoCliente())) {
								sb.append((char) 34).append("Calc. Autom. Comisiones").append((char) 34)
										.append(csvSepCamp);
								sb.append((char) 34).append("Acreditar Comision con IVA").append((char) 34)
										.append(csvSepCamp);
								sb.append((char) 34).append("Cliente Validado OK").append((char) 34).append(csvSepCamp);
							}

							sb.append((char) 34).append("Permitir Ventas Por Web").append((char) 34).append(csvSepCamp);

							if (this.getUsuario().getPermitirIncrementoAutomaticoDeSaldo()) {
								sb.append((char) 34).append("Valor AutoIncremento de Saldo").append((char) 34)
										.append(csvSepCamp);
							}

							if (this.getUsuario().getPermitirLimiteDescubierto()) {
								sb.append((char) 34).append("Valor Limite Descubierto").append((char) 34)
										.append(csvSepCamp);
							}

							// ---
							sb.append((char) 13).append((char) 10);
						}

						// DEFINO FORMATO DE FECHA PARA MOSTRAR EN EL REPORTE QUE SE EXPORTA
						SimpleDateFormat ff = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

						// CREO ARCHIVO DE EXPORTACION SI ES NECESARIO PARA EXPORTAR A CSV
						if (this.exportToExcel) {
							// RECORRO LA LISTA QUE HACER LAS SUMATORIAS Y PARA GENERAR EL REPORTE A
							// EXPORTAR EN CASO DE SER NECESARIO
							for (InformeSaldoClientes isc : listsc.getInformeSaldoClientes()) {

								if (i == 0) {
									mostrarArchivoCSV = true;
								}

								sb.append((char) 34).append(
										(isc.getFechaActual() == null || "".equals(isc.getFechaActual().toString()))
												? ""
												: ff.format(isc.getFechaActual().toGregorianCalendar().getTime()))
										.append((char) 34).append(csvSepCamp);

								if (agrFechaAlta) {
									sb.append((char) 34).append(
											(isc.getFechaAlta() == null || "".equals(isc.getFechaAlta().toString()))
													? ""
													: ff.format(isc.getFechaAlta().toGregorianCalendar().getTime()))
											.append((char) 34).append(csvSepCamp);
								}

								sb.append((char) 34).append(isc.getIdCliente().getValue()).append((char) 34)
										.append(csvSepCamp);
								sb.append((char) 34).append(isc.getRazonSocial().getValue()).append((char) 34)
										.append(csvSepCamp);

								sb.append((char) 34).append(isc.getNroContrato().getValue()).append((char) 34)
										.append(csvSepCamp);

								if (agrDetalles) {
									sb.append((char) 34).append("'").append(isc.getCuitcliente().getValue()).append((char) 34)
											.append(csvSepCamp);
									sb.append((char) 34).append(isc.getCalleCliente().getValue()).append((char) 34)
											.append(csvSepCamp);
									sb.append((char) 34).append(isc.getAlturaCliente().getValue()).append((char) 34)
											.append(csvSepCamp);
									sb.append((char) 34).append(isc.getGeoLatitud().getValue().toString()
											.replace(".", csvSepDec).replace(",", csvSepDec)).append((char) 34)
											.append(csvSepCamp);
									sb.append((char) 34).append(isc.getGeoLongitud().getValue().toString()
											.replace(".", csvSepDec).replace(",", csvSepDec)).append((char) 34)
											.append(csvSepCamp);
									sb.append((char) 34);
									if (isc.getTelefono1Cliente() != null && isc.getTelefono1Cliente().getValue() != null) {
										sb.append(isc.getTelefono1Cliente().getValue());
									}
									sb.append((char) 34).append(csvSepCamp);
									sb.append((char) 34);
									if (isc.getTelefono2Cliente() != null && isc.getTelefono2Cliente().getValue() != null) {
										sb.append(isc.getTelefono2Cliente().getValue());
									}
									sb.append((char) 34).append(csvSepCamp);									
									sb.append((char) 34);
									if (isc.getTelefono3Cliente() != null && isc.getTelefono3Cliente().getValue() != null) {
										sb.append(isc.getTelefono3Cliente().getValue());
									}
									sb.append((char) 34).append(csvSepCamp);
									sb.append((char) 34);
									if (isc.getMailCliente() != null && isc.getMailCliente().getValue() != null) {
										sb.append(isc.getMailCliente().getValue());
									}
									sb.append((char) 34).append(csvSepCamp);
									sb.append((char) 34);
									if (isc.getFaxCliente() != null && isc.getFaxCliente().getValue() != null) {
										sb.append(isc.getFaxCliente().getValue());
									}
									sb.append((char) 34).append(csvSepCamp);									
									sb.append((char) 34);
									if (isc.getPartido() != null && isc.getPartido().getValue() != null) {
										sb.append(isc.getPartido().getValue());
									}
									sb.append((char) 34).append(csvSepCamp);									
									sb.append((char) 34);
									if (isc.getBarrio() != null && isc.getBarrio().getValue() != null) {
										sb.append(isc.getBarrio().getValue());
									}
									sb.append((char) 34).append(csvSepCamp);									
									sb.append((char) 34);
									if (isc.getPiso() != null && isc.getPiso().getValue() != null) {
										sb.append(isc.getPiso().getValue());
									}
									sb.append((char) 34).append(csvSepCamp);									
									sb.append((char) 34);
									if (isc.getDepto() != null && isc.getDepto().getValue() != null) {
										sb.append(isc.getDepto().getValue());
									}
									sb.append((char) 34).append(csvSepCamp);									
									sb.append((char) 34);
									if (isc.getEntreCalle1() != null && isc.getEntreCalle1().getValue() != null) {
										sb.append(isc.getEntreCalle1().getValue());
									}
									sb.append((char) 34).append(csvSepCamp);									
									sb.append((char) 34);
									if (isc.getEntreCalle2() != null && isc.getEntreCalle2().getValue() != null) {
										sb.append(isc.getEntreCalle2().getValue());
									}
									sb.append((char) 34).append(csvSepCamp);									
									sb.append((char) 34);
									if (isc.getLocal() != null && isc.getLocal().getValue() != null) {
										sb.append(isc.getLocal().getValue());
									}
									sb.append((char) 34).append(csvSepCamp);									
									sb.append((char) 34);
									if (isc.getTipoDeCentro() != null && isc.getTipoDeCentro().getValue() != null) {
										sb.append(isc.getTipoDeCentro().getValue());
									}
									sb.append((char) 34).append(csvSepCamp);									
									sb.append((char) 34);
									if (isc.getUbicacion() != null && isc.getUbicacion().getValue() != null) {
										sb.append(isc.getUbicacion().getValue());
									}
									sb.append((char) 34).append(csvSepCamp);									
									sb.append((char) 34);
									if (isc.getTipoUbicacion() != null && isc.getTipoUbicacion().getValue() != null) {
										sb.append(isc.getTipoUbicacion().getValue());
									}
									sb.append((char) 34).append(csvSepCamp);									
									sb.append((char) 34);
									if (isc.getComuna() != null && isc.getComuna().getValue() != null) {
										sb.append(isc.getComuna().getValue());
									}
									sb.append((char) 34).append(csvSepCamp);									
									sb.append((char) 34);
									if (isc.getJurisdiccion() != null && isc.getJurisdiccion().getValue() != null) {
										sb.append(isc.getJurisdiccion().getValue());
									}
									sb.append((char) 34).append(csvSepCamp);									
									sb.append((char) 34);
									if (isc.getCasa() != null && isc.getCasa().getValue() != null) {
										sb.append(isc.getCasa().getValue());
									}
									sb.append((char) 34).append(csvSepCamp);									
									sb.append((char) 34);
									if (isc.getLote() != null && isc.getLote().getValue() != null) {
										sb.append(isc.getLote().getValue());
									}
									sb.append((char) 34).append(csvSepCamp);									
									sb.append((char) 34);
									if (isc.getManzana() != null && isc.getManzana().getValue() != null) {
										sb.append(isc.getManzana().getValue());
									}
									sb.append((char) 34).append(csvSepCamp);									
									sb.append((char) 34);
									if (isc.getSnSUBEcargaDif() != null && isc.getSnSUBEcargaDif().getValue() != null) {
										sb.append(isc.getSnSUBEcargaDif().getValue());
									}
									sb.append((char) 34).append(csvSepCamp);									
									sb.append((char) 34).append(isc.getIdUnicoCliente().getValue()).append((char) 34)
											.append(csvSepCamp);
									sb.append((char) 34);
									if (isc.getIdMovistar() != null && isc.getIdMovistar().getValue() != null) {
										sb.append(isc.getIdMovistar().getValue());
									}
									sb.append((char) 34).append(csvSepCamp);									
									sb.append((char) 34);
									if (isc.getIdClaro() != null && isc.getIdClaro().getValue() != null) {
										sb.append(isc.getIdClaro().getValue());
									}
									sb.append((char) 34).append(csvSepCamp);									
									sb.append((char) 34);
									if (isc.getIdPersonal() != null && isc.getIdPersonal().getValue() != null) {
										sb.append(isc.getIdPersonal().getValue());
									}
									sb.append((char) 34).append(csvSepCamp);									
									sb.append((char) 34);
									if (isc.getIdNextel() != null && isc.getIdNextel().getValue() != null) {
										sb.append(isc.getIdNextel().getValue());
									}
									sb.append((char) 34).append(csvSepCamp);									
									//sb.append((char) 34).append(isc.getSubeCdLocationid().getValue()).append((char) 34).append(csvSepCamp);
									//sb.append((char) 34).append(isc.getSubeCdPosid().getValue()).append((char) 34).append(csvSepCamp);
									sb.append((char) 34);
									if (isc.getIdsUnifCliSubeCdLocationid() != null && isc.getIdsUnifCliSubeCdLocationid().getValue() != null) {
										sb.append(isc.getIdsUnifCliSubeCdLocationid().getValue());
									}
									sb.append((char) 34).append(csvSepCamp);									
									sb.append((char) 34);
									if (isc.getIdsUnifCliSubeCdPosid() != null && isc.getIdsUnifCliSubeCdPosid().getValue() != null) {
										sb.append(isc.getIdsUnifCliSubeCdPosid().getValue());
									}
									sb.append((char) 34).append(csvSepCamp);
									sb.append((char) 34);
									if (isc.getIdsUnifCliSubeCdTerminalid() != null && isc.getIdsUnifCliSubeCdTerminalid().getValue() != null) {
										sb.append(isc.getIdsUnifCliSubeCdTerminalid().getValue());
									}
									sb.append((char) 34).append(csvSepCamp);
								}

								if (agrJerarquia) {
									sb.append((char) 34)
											.append((isc.getIdDist1() != null && isc.getIdDist1().getValue() != 0)
													? isc.getIdDist1().getValue()
													: "")
											.append((char) 34).append(csvSepCamp);
									sb.append((char) 34).append(
											(isc.getDescDist1() != null && isc.getDescDist1().getValue() != null)
													? isc.getDescDist1().getValue()
													: "")
											.append((char) 34).append(csvSepCamp);

									sb.append((char) 34)
											.append((isc.getIdDist2() != null && isc.getIdDist2().getValue() != 0)
													? isc.getIdDist2().getValue()
													: "")
											.append((char) 34).append(csvSepCamp);
									sb.append((char) 34).append(
											(isc.getDescDist2() != null && isc.getDescDist2().getValue() != null)
													? isc.getDescDist2().getValue()
													: "")
											.append((char) 34).append(csvSepCamp);

									sb.append((char) 34)
											.append((isc.getIdDist3() != null && isc.getIdDist3().getValue() != 0)
													? isc.getIdDist3().getValue()
													: "")
											.append((char) 34).append(csvSepCamp);
									sb.append((char) 34).append(
											(isc.getDescDist3() != null && isc.getDescDist3().getValue() != null)
													? isc.getDescDist3().getValue()
													: "")
											.append((char) 34).append(csvSepCamp);

									sb.append((char) 34)
											.append((isc.getIdDist4() != null && isc.getIdDist4().getValue() != 0)
													? isc.getIdDist4().getValue()
													: "")
											.append((char) 34).append(csvSepCamp);
									sb.append((char) 34).append(
											(isc.getDescDist4() != null && isc.getDescDist4().getValue() != null)
													? isc.getDescDist4().getValue()
													: "")
											.append((char) 34).append(csvSepCamp);

									sb.append((char) 34)
											.append((isc.getIdDist5() != null && isc.getIdDist5().getValue() != 0)
													? isc.getIdDist5().getValue()
													: "")
											.append((char) 34).append(csvSepCamp);
									sb.append((char) 34).append(
											(isc.getDescDist5() != null && isc.getDescDist5().getValue() != null)
													? isc.getDescDist5().getValue()
													: "")
											.append((char) 34).append(csvSepCamp);
								}

								if (agrRubro) {
									sb.append((char) 34).append(isc.getIdRubro().getValue()).append((char) 34)
											.append(csvSepCamp);
									sb.append((char) 34).append(isc.getDescripcionRubro().getValue()).append((char) 34)
											.append(csvSepCamp);
								}

								sb.append((char) 34).append(isc.getTipoCliente().getValue()).append((char) 34)
										.append(csvSepCamp);

								sb.append((char) 34);
								if (isc.getIdClienteExterno() != null && isc.getIdClienteExterno().getValue() != null) {
									sb.append(isc.getIdClienteExterno().getValue());
								}
								sb.append((char) 34).append(csvSepCamp);
								sb.append((char) 34).append(isc.getIdZonaCliente().getValue()).append((char) 34)
										.append(csvSepCamp);
								sb.append((char) 34).append(isc.getDescRegionCliente().getValue()).append((char) 34)
										.append(csvSepCamp);

								if (agrPais) {
									sb.append((char) 34).append(isc.getIdPaisCliente().getValue()).append((char) 34)
											.append(csvSepCamp);
									sb.append((char) 34).append(isc.getPaisCliente().getValue()).append((char) 34)
											.append(csvSepCamp);
								}

								if (agrProvincia) {
									sb.append((char) 34).append(isc.getIdProvincia().getValue()).append((char) 34)
											.append(csvSepCamp);
									sb.append((char) 34).append(isc.getDescripcionProvincia().getValue())
											.append((char) 34).append(csvSepCamp);
								}

								if (agrLocalidad) {
									sb.append((char) 34).append(isc.getIdLocalidad().getValue()).append((char) 34)
											.append(csvSepCamp);
									sb.append((char) 34).append(isc.getDescripcionLocalidad().getValue())
											.append((char) 34).append(csvSepCamp);
									sb.append((char) 34).append(isc.getCp().getValue()).append((char) 34)
											.append(csvSepCamp);
								}

								if (agrTerminal) {
									sb.append((char) 34).append(isc.getIdTipoTerminal().getValue()).append((char) 34)
											.append(csvSepCamp);
									sb.append((char) 34).append(isc.getDescTipoTerminal().getValue()).append((char) 34)
											.append(csvSepCamp);
								}

								sb.append((char) 34).append(isc.getEstado().getValue()).append((char) 34)
										.append(csvSepCamp);
								sb.append((char) 34).append("$")
										.append(ACGFormatHelper.format(isc.getSaldoActual().getValue(), csvSepDec))
										.append((char) 34).append(csvSepCamp);

								if (this.getUsuario().getHabilitarWU() && agregarSaldoWU) {
									sb.append((char) 34).append("$")
											.append(ACGFormatHelper.format(isc.getSaldoWU().getValue(), csvSepDec))
											.append((char) 34).append(csvSepCamp);
								}

								if (agregarEstadoCuentaCorrienteDelCliente) {
									sb.append((char) 34).append("$")
											.append(ACGFormatHelper.format(
													isc.getEstadoCuentaCorrienteCliente().getValue(), csvSepDec))
											.append((char) 34).append(csvSepCamp);
								}

								if (agregarEstadoCuentaCorrienteDeCajaParaElCliente) {
									sb.append((char) 34).append("$")
											.append(ACGFormatHelper.format(
													isc.getEstadoCuentaCorrienteDeCajaParaElCliente().getValue(),
													csvSepDec))
											.append((char) 34).append(csvSepCamp);
								}

								sb.append((char) 34);
								if (isc.getIdListaComision() != null && isc.getIdListaComision().getValue() != null) {
									sb.append(isc.getIdListaComision().getValue());
								}
								sb.append((char) 34).append(csvSepCamp);

								sb.append((char) 34);
								if (isc.getDescListaComision() != null
										&& isc.getDescListaComision().getValue() != null) {
									sb.append(isc.getDescListaComision().getValue());
								}
								sb.append((char) 34).append(csvSepCamp);

								if ("M".equals(this.getUsuario().getTipoCliente())) {
									sb.append((char) 34)
											.append((isc.getCalculoAutomComisiones().getValue() != null
													&& isc.getCalculoAutomComisiones().getValue()) ? "SI" : "NO")
											.append((char) 34).append(csvSepCamp);
									sb.append((char) 34)
											.append((isc.getAcredComisionConIVA().getValue() != null
													&& isc.getAcredComisionConIVA().getValue()) ? "SI" : "NO")
											.append((char) 34).append(csvSepCamp);
									sb.append((char) 34)
											.append((isc.getClienteValidadoOK().getValue() != null
													&& isc.getClienteValidadoOK().getValue()) ? "SI" : "NO")
											.append((char) 34).append(csvSepCamp);
								}

								sb.append((char) 34)
										.append((isc.getPermitirVentasPorWeb().getValue() != null
												&& isc.getPermitirVentasPorWeb().getValue()) ? "SI" : "NO")
										.append((char) 34).append(csvSepCamp);

								if (this.getUsuario().getPermitirIncrementoAutomaticoDeSaldo()) {
									sb.append((char) 34)
											.append(isc.getValorParaIncrementoAutomaticoDeSaldo().getValue())
											.append((char) 34).append(csvSepCamp);
								}

								if (this.getUsuario().getPermitirLimiteDescubierto()) {
									sb.append((char) 34).append(isc.getValorLimiteDescubierto().getValue())
											.append((char) 34);
								}

								// ---
								sb.append((char) 13).append((char) 10);

								i++;

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
											+ this.getUsuario().getIdMayorista() + ")_" + "InformeSaldoClientes.csv"
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
			cantRegistros = 0;
			cantRegAvalidar = 0;
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
							"Informe Saldo Clientes. Error ejecutando el WS de consulta: |" + ste.getMessage() + "|");
					FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
							"Error ejecutando el WS de consulta: |" + ste.getMessage() + "|", null));
				}
			} else {
				LogACGHelper.escribirLog(null,
						"Informe Saldo Clientes. Error ejecutando el WS de consulta: |" + ste.getMessage() + "|");
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
						"Error ejecutando el WS de consulta: |" + ste.getMessage() + "|", null));
			}
			FacesContext.getCurrentInstance().validationFailed();
			this.list = null;
		} catch (Exception e) {
			cantRegistros = 0;
			cantRegAvalidar = 0;
			LogACGHelper.escribirLog(null,
					"Informe Saldo Clientes. Excepcion ejecutando el WS de consulta: |" + e.getMessage() + "|");
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
					"Error ejecutando el WS de consulta de Transacciones: |" + e.getMessage() + "|", null));
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

	public void aceptarValidCli(Long idCliModif) {

		try {
			modifOK = "";

			for (InformeSaldoClientes iss : list) {

				if (iss.getIdCliente().getValue().compareTo(idCliModif) == 0) {

					Double geo_latitud = 0D;
					Double geo_longitud = 0D;
					// Calculo Latitud y Longitud
					try {
						DescripcionContainer dcl = GestionServiceHelper.getGestionService(CfgTimeout.REPORTE)
								.mostrarLocalidadesPorProv(this.getUsuario().getIdMayorista(),
										iss.getIdProvinciaValid().getValue(), iss.getIdProvinciaValid().getValue());
						DescripcionContainer dcp = GestionServiceHelper.getGestionService(CfgTimeout.REPORTE)
								.mostrarProvincias(this.getUsuario().getIdMayorista(),
										iss.getIdProvinciaValid().getValue(), idPais);
						DescripcionContainer dcpais = GestionServiceHelper.getGestionService(CfgTimeout.REPORTE)
								.mostrarPaises(this.getUsuario().getIdMayorista(), iss.getIdPaisValid().getValue());

						String localidad = dcl.getListDescripcion().getValue().getDescripcion().get(0).getDescripcion()
								.getValue();
						String provincia = dcp.getListDescripcion().getValue().getDescripcion().get(0).getDescripcion()
								.getValue();
						String pais = dcpais.getListDescripcion().getValue().getDescripcion().get(0).getDescripcion()
								.getValue();

						GeoCoordenadas gc = GestionServiceHelper.getGestionService(CfgTimeout.REPORTE)
								.obtenerGeoCoordenadas(this.getUsuario().getIdMayorista(),
										iss.getCalleValid().getValue(), iss.getAlturaValid().getValue(), localidad,
										provincia, pais);

						if ((gc != null) && (!gc.getError().getValue().getHayError().getValue())) {
							geo_latitud = gc.getLatitud().getValue();
							geo_longitud = gc.getLongitud().getValue();
						}
					} catch (Exception e1) {
						geo_latitud = 0D;
						geo_longitud = 0D;
					}
					// FIN Calculo Latitud y Longitud

					ResultadoBase r = GestionServiceHelper.getGestionService(CfgTimeout.REPORTE)
							.actualizarDatosValidCli(this.getUsuario().getIdMayorista(), idCliModif,
									iss.getIdPaisValid().getValue(), iss.getIdProvinciaValid().getValue(),
									iss.getIdProvinciaValid().getValue(),
									iss.getIdProvinciaValid().getValue().toString(), iss.getCuitValid().getValue(),
									iss.getTelefono1Valid().getValue(), iss.getCelularValid().getValue(),
									iss.getMailValid().getValue(), iss.getCalleValid().getValue(),
									iss.getAlturaValid().getValue(), iss.getIdRubroValid().getValue(), geo_latitud,
									geo_longitud);

					if (r.getError().getValue().getHayError().getValue()) {
						modifOK = r.getError().getValue().getMsgError().getValue();
					}
					break;
				}
			}

			// Refrescar la lista
			realizarInforme();

			if (!"".equals(this.modifOK)) {
				FacesContext.getCurrentInstance().addMessage(null,
						new FacesMessage(FacesMessage.SEVERITY_ERROR, this.modifOK, null));
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
							"Informe Saldo Clientes. Error ejecutando el WS de consulta: |" + ste.getMessage() + "|");
					FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
							"Error ejecutando el WS de consulta: |" + ste.getMessage() + "|", null));
				}
			} else {
				LogACGHelper.escribirLog(null,
						"Informe Saldo Clientes. Error ejecutando el WS de consulta: |" + ste.getMessage() + "|");
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
						"Error ejecutando el WS de consulta: |" + ste.getMessage() + "|", null));
			}
			FacesContext.getCurrentInstance().validationFailed();
			this.list = null;
		} catch (Exception e) {
			cantRegistros = 0;
			cantRegAvalidar = 0;
			LogACGHelper.escribirLog(null,
					"Informe Saldo Clientes. Excepcion ejecutando el WS de consulta: |" + e.getMessage() + "|");
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
					"Error ejecutando el WS de consulta de Transacciones: |" + e.getMessage() + "|", null));
			FacesContext.getCurrentInstance().validationFailed();
			this.list = null;
		}
		PrimeFaces.current().executeScript("PF('blockPanelGral').hide()");
		return;
	}

	public void descartarValidCli(Long idCliModif) {
		try {

			ResultadoBase r = GestionServiceHelper.getGestionService(CfgTimeout.REPORTE)
					.descartarValidacionCliente(this.getUsuario().getIdMayorista(), idCliModif, true);

			// Refrescar la lista
			realizarInforme();

		} catch (WebServiceException ste) {
			cantRegistros = 0;
			cantRegAvalidar = 0;
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
							"Informe Saldo Clientes. Error ejecutando el WS de consulta: |" + ste.getMessage() + "|");
					FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
							"Error ejecutando el WS de consulta: |" + ste.getMessage() + "|", null));
				}
			} else {
				LogACGHelper.escribirLog(null,
						"Informe Saldo Clientes. Error ejecutando el WS de consulta: |" + ste.getMessage() + "|");
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
						"Error ejecutando el WS de consulta: |" + ste.getMessage() + "|", null));
			}
			FacesContext.getCurrentInstance().validationFailed();
			this.list = null;
		} catch (Exception e) {
			cantRegistros = 0;
			cantRegAvalidar = 0;
			LogACGHelper.escribirLog(null,
					"Informe Saldo Clientes. Excepcion ejecutando el WS de consulta: |" + e.getMessage() + "|");
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
					"Error ejecutando el WS de consulta de Transacciones: |" + e.getMessage() + "|", null));
			FacesContext.getCurrentInstance().validationFailed();
			this.list = null;
		}
		PrimeFaces.current().executeScript("PF('blockPanelGral').hide()");
		return;
	}

	public void onChangePais(ActionEvent evt) {
		idProvincia = null;
		idLocalidad = null;
	}

	public void onChangeProvincia(ActionEvent evt) {
		idLocalidad = null;
	}

	public void actualizarProvinciasParaValidCli(Long onchangepais_idcli, Long onchangepais_idpais) {
		try {

			for (InformeSaldoClientes iss : list) {

				if (iss.getIdCliente().getValue().compareTo(onchangepais_idcli) == 0) {
					// Vuelvo a cargar la lista de provincias

					ObjectFactory objectFactory = new ObjectFactory();

					DescripcionContainer cd = GestionServiceHelper.getGestionService(CfgTimeout.REPORTE)
							.mostrarProvincias(this.getUsuario().getIdMayorista(), null, onchangepais_idpais);

					for (Descripcion d : cd.getListDescripcion().getValue().getDescripcion()) {
						SelectItem si = new SelectItem();
						si.setValue(objectFactory.createSelectItemValue(d.getId().getValue()));
						si.setLabel(objectFactory.createSelectItemDescription(d.getDescripcion().getValue()));
						iss.getListProvinciasValid().getValue().getSelectItem().add(si);
					}

					if (!iss.getListProvinciasValid().getValue().getSelectItem().isEmpty()) {

						iss.getIdProvinciaValid().setValue((Long) iss.getListProvinciasValid().getValue()
								.getSelectItem().get(0).getValue().getValue());
						actualizarLocalidadesParaValidCliAux(onchangepais_idcli, (Long) iss.getListProvinciasValid()
								.getValue().getSelectItem().get(0).getValue().getValue());
					} else {
						iss.setIdProvinciaValid(null);
						iss.getListLocalidadesValid().getValue().getSelectItem().clear();
					}

					break;
				}

			}
		} catch (WebServiceException ste) {
			if (ste != null && ste.getCause() != null && ste.getCause().getClass() != null) {
				if (ste.getCause().getClass().equals(ConnectException.class)) {
					FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
							"No se pudo establecer la comunicación (GST-TOC).\n No se reconsultaron las provincias. Por favor intente nuevamente.",
							null));
				} else if (ste.getCause().getClass().equals(SocketTimeoutException.class)) {
					FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
							"No se pudo establecer la comunicación (GST-TRW).\n No se reconsultaron las provincias. Por favor intente nuevamente.",
							null));
				} else if (ste.getCause().getClass().equals(UnknownHostException.class)) {
					LogACGHelper.escribirLog(null,
							"No se pudo establecer la comunicación (GST-HNC): |" + ste.getMessage() + "|");
					FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
							"No se pudo establecer la comunicación (GST-HNC).\n Por favor intente nuevamente.", null));
				} else {
					LogACGHelper.escribirLog(null,
							"Informe Saldo Clientes. Error ejecutando el WS de consulta: |" + ste.getMessage() + "|");
					FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
							"No se pudo establecer la comunicación (GST-OTR).\n No se reconsultaron las provincias. Por favor intente nuevamente.",
							null));
				}
			} else {
				LogACGHelper.escribirLog(null,
						"Informe Saldo Clientes. Error ejecutando el WS de consulta: |" + ste.getMessage() + "|");
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
						"No se pudo establecer la comunicación (GST-OTR).\n No se reconsultaron las provincias. Por favor intente nuevamente.",
						null));
			}
			FacesContext.getCurrentInstance().validationFailed();
		} catch (Exception e) {
			LogACGHelper.escribirLog(null,
					"Informe Saldo Clientes. Excepcion ejecutando el WS de consulta: |" + e.getMessage() + "|");
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
					"No se pudo establecer la comunicación (GST-OTR).\n No se reconsultaron las provincias. Por favor intente nuevamente.",
					null));
			FacesContext.getCurrentInstance().validationFailed();

		}
	}

	private void actualizarLocalidadesParaValidCliAux(Long onchangeprov_idcli, Long onchangeprov_idprov) {
		try {

			for (InformeSaldoClientes iss : list) {

				if (iss.getIdCliente().getValue().compareTo(onchangeprov_idcli) == 0) {
					// Vuelvo a cargar la lista de localidades

					ObjectFactory objectFactory = new ObjectFactory();
					ArrayOfSelectItem localidadesAux = new ArrayOfSelectItem();
					DescripcionContainer cd = GestionServiceHelper.getGestionService(CfgTimeout.REPORTE)
							.mostrarLocalidadesPorProv(this.getUsuario().getIdMayorista(), onchangeprov_idprov, null);

					for (Descripcion d : cd.getListDescripcion().getValue().getDescripcion()) {
						SelectItem si = new SelectItem();
						si.setValue(objectFactory.createSelectItemValue(d.getId().getValue()));
						si.setLabel(objectFactory.createSelectItemDescription(d.getDescripcion().getValue()));
						iss.getListLocalidadesValid().getValue().getSelectItem().add(si);
					}

					if (!iss.getListLocalidadesValid().getValue().getSelectItem().isEmpty()) {

						iss.getIdlocalidadValid().setValue((Long) iss.getListLocalidadesValid().getValue()
								.getSelectItem().get(0).getValue().getValue());
					} else {
						iss.setIdlocalidadValid(null);
					}

					break;

				}
			}
		} catch (WebServiceException ste) {
			if (ste != null && ste.getCause() != null && ste.getCause().getClass() != null) {
				if (ste.getCause().getClass().equals(ConnectException.class)) {
					FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
							"No se pudo establecer la comunicación (GST-TOC).\n No se reconsultaron las localidades. Por favor intente nuevamente.",
							null));
				} else if (ste.getCause().getClass().equals(SocketTimeoutException.class)) {
					FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
							"No se pudo establecer la comunicación (GST-TRW).\n No se reconsultaron las localidades. Por favor intente nuevamente.",
							null));
				} else if (ste.getCause().getClass().equals(UnknownHostException.class)) {
					LogACGHelper.escribirLog(null,
							"No se pudo establecer la comunicación (GST-HNC): |" + ste.getMessage() + "|");
					FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
							"No se pudo establecer la comunicación (GST-HNC).\n Por favor intente nuevamente.", null));
				} else {
					LogACGHelper.escribirLog(null,
							"Informe Saldo Clientes. Error ejecutando el WS de consulta: |" + ste.getMessage() + "|");
					FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
							"No se pudo establecer la comunicación (GST-OTR).\n No se reconsultaron las localidades. Por favor intente nuevamente.",
							null));
				}
			} else {
				LogACGHelper.escribirLog(null,
						"Informe Saldo Clientes. Error ejecutando el WS de consulta: |" + ste.getMessage() + "|");
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
						"No se pudo establecer la comunicación (GST-OTR).\n No se reconsultaron las localidades. Por favor intente nuevamente.",
						null));
			}
			FacesContext.getCurrentInstance().validationFailed();
		} catch (Exception e) {
			LogACGHelper.escribirLog(null,
					"Informe Saldo Clientes. Excepcion ejecutando el WS de consulta: |" + e.getMessage() + "|");
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
					"No se pudo establecer la comunicación (GST-OTR).\n No se reconsultaron las localidades. Por favor intente nuevamente.",
					null));
			FacesContext.getCurrentInstance().validationFailed();

		}
	}

	public void actualizarLocalidadesParaValidCli(Long onchangeprov_idcli, Long onchangeprov_idprov) {

		actualizarLocalidadesParaValidCliAux(onchangeprov_idcli, onchangeprov_idprov);

	}

	public void habilitDeshabilitExpExcel(ActionEvent evt) {
		if (exportToExcel) {
			validarDatosCliente = false;
			geolocalizarTodo = false;
		}
	}

	public void habilitDeshabilitValidDatosCli() {
		if (validarDatosCliente) {
			exportToExcel = false;
			geolocalizarTodo = false;
		}
	}

	public void geolocalizarTodoAction() {
		if (geolocalizarTodo) {
			validarDatosCliente = false;
			exportToExcel = false;
		}
	}

	public Long getIdTipoTerminal() {
		return idTipoTerminal;
	}

	public void setIdTipoTerminal(Long idTipoTerminal) {
		this.idTipoTerminal = idTipoTerminal;
	}

	public Integer getTipoFiltroCliente() {
		return tipoFiltroCliente;
	}

	public void setTipoFiltroCliente(Integer tipoFiltroCliente) {
		this.tipoFiltroCliente = tipoFiltroCliente;
	}

	public Float getSaldoMinimo() {
		return saldoMinimo;
	}

	public void setSaldoMinimo(Float saldoMinimo) {
		this.saldoMinimo = saldoMinimo;
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

	public Boolean getAgrTerminal() {
		return agrTerminal;
	}

	public void setAgrTerminal(Boolean agrTerminal) {
		this.agrTerminal = agrTerminal;
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

	public String getEstado() {
		return estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}

	public String getTipoCliente() {
		return tipoCliente;
	}

	public void setTipoCliente(String tipoCliente) {
		this.tipoCliente = tipoCliente;
	}

	public List<javax.faces.model.SelectItem> getProvincias() {

		try {

			this.provincias = new ArrayList<javax.faces.model.SelectItem>();
			this.localidades = new ArrayList<javax.faces.model.SelectItem>();

			if ((idPais != null) && (idPais > 0)) {
				DescripcionContainer cd = GestionServiceHelper.getGestionService(CfgTimeout.REPORTE)
						.mostrarProvincias(this.getUsuario().getIdMayorista(), null, idPais);
				if ((cd != null) && (!cd.getListDescripcion().getValue().getDescripcion().isEmpty())) {
					for (Descripcion d : cd.getListDescripcion().getValue().getDescripcion()) {

						this.provincias.add(
								new javax.faces.model.SelectItem(d.getId().getValue(), d.getDescripcion().getValue()));
					}
				}
			}

		} catch (WebServiceException ste) {
			if (ste != null && ste.getCause() != null && ste.getCause().getClass() != null) {
				if (ste.getCause().getClass().equals(ConnectException.class)) {
					FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
							"No se pudo establecer la comunicación (GST-TOC).\n No se reconsultaron las provincias. Por favor intente nuevamente.",
							null));
				} else if (ste.getCause().getClass().equals(SocketTimeoutException.class)) {
					FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
							"No se pudo establecer la comunicación (GST-TRW).\n No se reconsultaron las provincias. Por favor intente nuevamente.",
							null));
				} else if (ste.getCause().getClass().equals(UnknownHostException.class)) {
					LogACGHelper.escribirLog(null,
							"No se pudo establecer la comunicación (GST-HNC): |" + ste.getMessage() + "|");
					FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
							"No se pudo establecer la comunicación (GST-HNC).\n Por favor intente nuevamente.", null));
				} else {
					LogACGHelper.escribirLog(null,
							"Informe Saldo Clientes. Error ejecutando el WS de consulta: |" + ste.getMessage() + "|");
					FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
							"No se pudo establecer la comunicación (GST-OTR).\n No se reconsultaron las provincias. Por favor intente nuevamente.",
							null));
				}
			} else {
				LogACGHelper.escribirLog(null,
						"Informe Saldo Clientes. Error ejecutando el WS de consulta: |" + ste.getMessage() + "|");
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
						"No se pudo establecer la comunicación (GST-OTR).\n No se reconsultaron las provincias. Por favor intente nuevamente.",
						null));
			}
			FacesContext.getCurrentInstance().validationFailed();
		} catch (Exception e) {
			LogACGHelper.escribirLog(null,
					"Informe Saldo Clientes. Excepcion ejecutando el WS de consulta: |" + e.getMessage() + "|");
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
					"No se pudo establecer la comunicación (GST-OTR).\n No se reconsultaron los modelos. Por favor intente nuevamente.",
					null));
			FacesContext.getCurrentInstance().validationFailed();

		}

		return provincias;
	}

	public void setProvincias(List<javax.faces.model.SelectItem> provincias) {
		this.provincias = provincias;
	}

	public List<javax.faces.model.SelectItem> getLocalidades() {

		try {
			this.localidades = new ArrayList<javax.faces.model.SelectItem>();
			if ((idProvincia != null) && (idProvincia > 0)) {
				DescripcionContainer cd = GestionServiceHelper.getGestionService(CfgTimeout.REPORTE)
						.mostrarLocalidadesPorProv(this.getUsuario().getIdMayorista(), idProvincia, null);

				if ((cd != null) && (!cd.getListDescripcion().getValue().getDescripcion().isEmpty())) {
					for (Descripcion d : cd.getListDescripcion().getValue().getDescripcion()) {
						this.localidades.add(
								new javax.faces.model.SelectItem(d.getId().getValue(), d.getDescripcion().getValue()));
					}
				}
			}
		} catch (WebServiceException ste) {
			if (ste != null && ste.getCause() != null && ste.getCause().getClass() != null) {
				if (ste.getCause().getClass().equals(ConnectException.class)) {
					FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
							"No se pudo establecer la comunicación (GST-TOC).\n No se reconsultaron las localidades. Por favor intente nuevamente.",
							null));
				} else if (ste.getCause().getClass().equals(SocketTimeoutException.class)) {
					FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
							"No se pudo establecer la comunicación (GST-TRW).\n No se reconsultaron las localidades. Por favor intente nuevamente.",
							null));
				} else if (ste.getCause().getClass().equals(UnknownHostException.class)) {
					LogACGHelper.escribirLog(null,
							"No se pudo establecer la comunicación (GST-HNC): |" + ste.getMessage() + "|");
					FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
							"No se pudo establecer la comunicación (GST-HNC).\n Por favor intente nuevamente.", null));
				} else {
					LogACGHelper.escribirLog(null,
							"Informe Saldo Clientes. Error ejecutando el WS de consulta: |" + ste.getMessage() + "|");
					FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
							"No se pudo establecer la comunicación (GST-OTR).\n No se reconsultaron las localidades. Por favor intente nuevamente.",
							null));
				}
			} else {
				LogACGHelper.escribirLog(null,
						"Informe Saldo Clientes. Error ejecutando el WS de consulta: |" + ste.getMessage() + "|");
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
						"No se pudo establecer la comunicación (GST-OTR).\n No se reconsultaron las localidades. Por favor intente nuevamente.",
						null));
			}
			FacesContext.getCurrentInstance().validationFailed();
		} catch (Exception e) {
			LogACGHelper.escribirLog(null,
					"Informe Saldo Clientes. Excepcion ejecutando el WS de consulta: |" + e.getMessage() + "|");
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
					"No se pudo establecer la comunicación (GST-OTR).\n No se reconsultaron los modelos. Por favor intente nuevamente.",
					null));
			FacesContext.getCurrentInstance().validationFailed();

		}

		return localidades;
	}

	// ERA DE PANELMOSTRARIMAGEN

	public void preCargarImagen(ActionEvent evt) {

		// PreCargo las imagenes en caso de que el Punto de venta tenga una asignada

		// System.out.println("!!!!!!!!!!!! preCargarImagen en
		// MostrarImagen!!!!!!!!!!!!");

		informeSaldoClientesSeleccionadoImagenDelLocal = "/images/pixel_blanco.jpg";

		RespString resp = GestionServiceHelper.getGestionService(CfgTimeout.REPORTE).devolverImagenLocal(
				this.informeSaldoClientesSeleccionado.getIdMayorista().getValue(),
				this.informeSaldoClientesSeleccionado.getIdCliente().getValue());

		if (resp != null) {
			if (!resp.getError().getValue().getHayError().getValue()) {

				String imagenDelLocal = resp.getRespuesta().getValue();

				if (!"".equals(imagenDelLocal)) {

					try {

						cargarPathParaArchivos();

						// Leo la imagen desde la URL
						String imagenAleer = pathLecturaImages + imagenDelLocal;
						// System.out.println("imagenAleer: |" + imagenAleer + "|");

						// Asigno el nombre de la imagen en asoc_imagenDelLocal para luego usarla en el
						// xthml
						informeSaldoClientesSeleccionadoImagenDelLocal = imagenAleer;

					} catch (Exception e) {
						// System.out.println("Error en Precargando Imagen: |" + e.getMessage() + "|");
					}

				} else {
					// El nombre de la imagen es vacio
				}

			}
		}

	}

	private void cargarPathParaArchivos() {

		try {

			if (pathLecturaImages == null || "".equals(pathLecturaImages)) {
				ConfigGral c = GestionServiceHelper.getGestionService(CfgTimeout.REPORTE)
						.mostrarConfigGral(this.getUsuario().getIdMayorista());

				if (c != null) {
					if (!c.getError().getValue().getHayError().getValue()) {
						pathLecturaImages = c.getPathLecturaImages().getValue();

					}

				}
			}

		} catch (Exception e) {
			// System.out.println("Error buscando los path de Lectura y Escritua para las
			// imagenes");
		}

	}

	// ERA DE PANELMOSTRARIMAGEN - FIN

	public void setLocalidades(List<javax.faces.model.SelectItem> localidades) {
		this.localidades = localidades;
	}

	public List<javax.faces.model.SelectItem> getTipoTerminales() {
		return tipoTerminales;
	}

	public void setTipoTerminales(List<javax.faces.model.SelectItem> tipoTerminales) {
		this.tipoTerminales = tipoTerminales;
	}

	public Long getIdRubro() {
		return idRubro;
	}

	public void setIdRubro(Long idRubro) {
		this.idRubro = idRubro;
	}

	public List<javax.faces.model.SelectItem> getRubros() {
		return rubros;
	}

	public void setRubros(List<javax.faces.model.SelectItem> rubros) {
		this.rubros = rubros;
	}

	public Boolean getAgrRubro() {
		return agrRubro;
	}

	public void setAgrRubro(Boolean agrRubro) {
		this.agrRubro = agrRubro;
	}

	public Boolean getAgrFechaAlta() {
		return agrFechaAlta;
	}

	public void setAgrFechaAlta(Boolean agrFechaAlta) {
		this.agrFechaAlta = agrFechaAlta;
	}

	public Long getIdZona() {
		return idZona;
	}

	public void setIdZona(Long idZona) {
		this.idZona = idZona;
	}

	public List<javax.faces.model.SelectItem> getZonas() {
		return zonas;
	}

	public void setZonas(List<javax.faces.model.SelectItem> zonas) {
		this.zonas = zonas;
	}

	public Boolean getAgrZonas() {
		return agrZonas;
	}

	public void setAgrZonas(Boolean agrZonas) {
		this.agrZonas = agrZonas;
	}

	public Boolean getAgrJerarquia() {
		return agrJerarquia;
	}

	public void setAgrJerarquia(Boolean agrJerarquia) {
		this.agrJerarquia = agrJerarquia;
	}

	public Boolean getAgrDetalles() {
		return agrDetalles;
	}

	public void setAgrDetalles(Boolean agrDetalles) {
		this.agrDetalles = agrDetalles;
	}

	public String getDireccionGeolocalizacion() {
		return direccionGeolocalizacion;
	}

	public void setDireccionGeolocalizacion(String direccionGeolocalizacion) {
		this.direccionGeolocalizacion = direccionGeolocalizacion;
	}

	public String getClienteGeolocalizacion() {
		return clienteGeolocalizacion;
	}

	public void setClienteGeolocalizacion(String clienteGeolocalizacion) {
		this.clienteGeolocalizacion = clienteGeolocalizacion;
	}

	public Boolean getValidarDatosCliente() {
		return validarDatosCliente;
	}

	public void setValidarDatosCliente(Boolean validarDatosCliente) {
		this.validarDatosCliente = validarDatosCliente;
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

	public List<javax.faces.model.SelectItem> getPaises() {
		return paises;
	}

	public void setPaises(List<javax.faces.model.SelectItem> paises) {
		this.paises = paises;
	}

	public Long getIdClienteAmodificar() {
		return idClienteAmodificar;
	}

	public void setIdClienteAmodificar(Long idClienteAmodificar) {
		this.idClienteAmodificar = idClienteAmodificar;
	}

	public Integer getCantRegAvalidar() {
		return cantRegAvalidar;
	}

	public void setCantRegAvalidar(Integer cantRegAvalidar) {
		this.cantRegAvalidar = cantRegAvalidar;
	}

	public String getModifOK() {
		return modifOK;
	}

	public void setModifOK(String modifOK) {
		this.modifOK = modifOK;
	}

	public Boolean getAgregarEstadoCuentaCorrienteDelCliente() {
		return agregarEstadoCuentaCorrienteDelCliente;
	}

	public void setAgregarEstadoCuentaCorrienteDelCliente(Boolean agregarEstadoCuentaCorrienteDelCliente) {
		this.agregarEstadoCuentaCorrienteDelCliente = agregarEstadoCuentaCorrienteDelCliente;
	}

	public Boolean getAgregarEstadoCuentaCorrienteDeCajaParaElCliente() {
		return agregarEstadoCuentaCorrienteDeCajaParaElCliente;
	}

	public void setAgregarEstadoCuentaCorrienteDeCajaParaElCliente(
			Boolean agregarEstadoCuentaCorrienteDeCajaParaElCliente) {
		this.agregarEstadoCuentaCorrienteDeCajaParaElCliente = agregarEstadoCuentaCorrienteDeCajaParaElCliente;
	}

	public Boolean getAgregarSaldoWU() {
		return agregarSaldoWU;
	}

	public void setAgregarSaldoWU(Boolean agregarSaldoWU) {
		this.agregarSaldoWU = agregarSaldoWU;
	}

	public Boolean getGeolocalizarTodo() {
		return geolocalizarTodo;
	}

	public void setGeolocalizarTodo(Boolean geolocalizarTodo) {
		this.geolocalizarTodo = geolocalizarTodo;
	}

	public String getTipoClienteGeolocalizacion() {
		return tipoClienteGeolocalizacion;
	}

	public void setTipoClienteGeolocalizacion(String tipoClienteGeolocalizacion) {
		this.tipoClienteGeolocalizacion = tipoClienteGeolocalizacion;
	}

	public Double getGeo_latitud_delCliente() {
		return geo_latitud_delCliente;
	}

	public void setGeo_latitud_delCliente(Double geo_latitud_delCliente) {
		this.geo_latitud_delCliente = geo_latitud_delCliente;
	}

	public Double getGeo_longitud_delCliente() {
		return geo_longitud_delCliente;
	}

	public void setGeo_longitud_delCliente(Double geo_longitud_delCliente) {
		this.geo_longitud_delCliente = geo_longitud_delCliente;
	}

	public String getPathLecturaImages() {
		return pathLecturaImages;
	}

	public void setPathLecturaImages(String pathLecturaImages) {
		this.pathLecturaImages = pathLecturaImages;
	}

	public String getIdClienteExterno() {
		return idClienteExterno;
	}

	public void setIdClienteExterno(String idClienteExterno) {
		this.idClienteExterno = idClienteExterno;
	}

	public String getInformeSaldoClientesSeleccionadoImagenDelLocal() {
		return informeSaldoClientesSeleccionadoImagenDelLocal;
	}

	public void setInformeSaldoClientesSeleccionadoImagenDelLocal(
			String informeSaldoClientesSeleccionadoImagenDelLocal) {
		this.informeSaldoClientesSeleccionadoImagenDelLocal = informeSaldoClientesSeleccionadoImagenDelLocal;
	}

	public InformeSaldoClientes getInformeSaldoClientesSeleccionado() {
		return informeSaldoClientesSeleccionado;
	}

	public void setInformeSaldoClientesSeleccionado(InformeSaldoClientes informeSaldoClientesSeleccionado) {
		this.informeSaldoClientesSeleccionado = informeSaldoClientesSeleccionado;
	}
}
