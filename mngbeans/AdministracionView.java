package com.americacg.cargavirtual.web.mngbeans;

import java.io.Serializable;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.List;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.event.ValueChangeEvent;
import javax.faces.model.SelectItem;

import javax.inject.Named;
import javax.xml.bind.JAXBElement;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import javax.xml.ws.WebServiceException;

import org.primefaces.PrimeFaces;
import org.primefaces.event.SelectEvent;
import org.primefaces.event.TabChangeEvent;
import org.primefaces.event.data.FilterEvent;
import org.primefaces.event.data.SortEvent;
import org.primefaces.model.DefaultTreeNode;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.TreeNode;
import org.springframework.context.annotation.Scope;

import com.americacg.cargavirtual.gestion.javax.faces.model.ArrayOfSelectItem;
import com.americacg.cargavirtual.gestion.model.ArrayOfCabeceraProducto;
import com.americacg.cargavirtual.gestion.model.ArrayOfCabeceraTablaComision;
import com.americacg.cargavirtual.gestion.model.ArrayOfClienteReducido;
import com.americacg.cargavirtual.gestion.model.ArrayOfComprobanteExterno;
import com.americacg.cargavirtual.gestion.model.ArrayOfDatoAcreditacionComisiones;
import com.americacg.cargavirtual.gestion.model.ArrayOfDatosDepAdel;
import com.americacg.cargavirtual.gestion.model.ArrayOfHistorialAsginacionListaComisiones;
import com.americacg.cargavirtual.gestion.model.ArrayOfIncrementoSaldo;
import com.americacg.cargavirtual.gestion.model.ArrayOfJerarquia;
import com.americacg.cargavirtual.gestion.model.ArrayOfProductoComision;
import com.americacg.cargavirtual.gestion.model.ArrayOfSaldoProveedor;
import com.americacg.cargavirtual.gestion.model.ArrayOfSubMovimientoCuentaCorriente;
import com.americacg.cargavirtual.gestion.model.Banco;
import com.americacg.cargavirtual.gestion.model.BancoContainer;
import com.americacg.cargavirtual.gestion.model.CabeceraProducto;
import com.americacg.cargavirtual.gestion.model.CabeceraTablaComision;
import com.americacg.cargavirtual.gestion.model.Cliente;
import com.americacg.cargavirtual.gestion.model.ClienteEstComisPlanaAdel;
import com.americacg.cargavirtual.gestion.model.ClienteReducido;
import com.americacg.cargavirtual.gestion.model.ComisionPlanaAdelantada;
import com.americacg.cargavirtual.gestion.model.ComprobanteExterno;
import com.americacg.cargavirtual.gestion.model.DatoAcreditacionComisiones;
import com.americacg.cargavirtual.gestion.model.DatosAcreditacionComisionesContainer;
import com.americacg.cargavirtual.gestion.model.DatosDepAdel;
import com.americacg.cargavirtual.gestion.model.DatosDepAdelContainer;
import com.americacg.cargavirtual.gestion.model.DatosReparto;
import com.americacg.cargavirtual.gestion.model.Descripcion;
import com.americacg.cargavirtual.gestion.model.DescripcionContainer;
import com.americacg.cargavirtual.gestion.model.FacturaList;
import com.americacg.cargavirtual.gestion.model.FloatHolder;
import com.americacg.cargavirtual.gestion.model.HistorialAsginacionListaComisiones;
import com.americacg.cargavirtual.gestion.model.IncrementoSaldo;
import com.americacg.cargavirtual.gestion.model.InformeComprobantesExternos;
import com.americacg.cargavirtual.gestion.model.ItemRespRepSaldo;
import com.americacg.cargavirtual.gestion.model.Jerarquia;
import com.americacg.cargavirtual.gestion.model.MonitorSaldos;
import com.americacg.cargavirtual.gestion.model.ObjectFactory;
import com.americacg.cargavirtual.gestion.model.ParametrosList;
import com.americacg.cargavirtual.gestion.model.ProductoComision;
import com.americacg.cargavirtual.gestion.model.RespAcreditComisiones;
import com.americacg.cargavirtual.gestion.model.RespAcreditComisionesContainer;
import com.americacg.cargavirtual.gestion.model.RespDepAdel;
import com.americacg.cargavirtual.gestion.model.RespDepAdelContainer;
import com.americacg.cargavirtual.gestion.model.RespIdString;
import com.americacg.cargavirtual.gestion.model.RespRepSaldo;
import com.americacg.cargavirtual.gestion.model.RespString;
import com.americacg.cargavirtual.gestion.model.ResultadoBase;
import com.americacg.cargavirtual.gestion.model.ResultadoBaseConIdyDesc;
import com.americacg.cargavirtual.gestion.model.SaldoCliente;
import com.americacg.cargavirtual.gestion.model.SaldoProveedor;
import com.americacg.cargavirtual.gestion.model.Saldos;
import com.americacg.cargavirtual.gestion.model.SubMovimientoCuentaCorriente;
import com.americacg.cargavirtual.gestion.model.Usuario;
import com.americacg.cargavirtual.gestion.model.WUTiporeparto;
import com.americacg.cargavirtual.gestion.model.WUTiporepartoContainer;
import com.americacg.cargavirtual.web.enums.CfgTimeout;
import com.americacg.cargavirtual.web.enums.OperacionEnEjecucion;
import com.americacg.cargavirtual.web.helpers.GestionServiceHelper;
import com.americacg.cargavirtual.web.lazymodels.LazyBusquedaClienteDataModel;
import com.americacg.cargavirtual.web.lazymodels.LazyCabeceraUsuarioDataModel;
import com.americacg.cargavirtual.web.lazymodels.LazyItemArbolDataModel;
import com.americacg.cargavirtual.web.mngbeans.reportes.ReporteAuditoriasView;
import com.americacg.cargavirtual.web.mngbeans.reportes.ReporteCCNView;
import com.americacg.cargavirtual.web.mngbeans.reportes.ReporteCargaMasivaClientesView;
import com.americacg.cargavirtual.web.mngbeans.reportes.ReporteCargaMasivaDepositosView;
import com.americacg.cargavirtual.web.mngbeans.reportes.ReporteCargaPinesView;
import com.americacg.cargavirtual.web.mngbeans.reportes.ReporteCargasAutomaticasView;
import com.americacg.cargavirtual.web.mngbeans.reportes.ReporteComisionesView;
import com.americacg.cargavirtual.web.mngbeans.reportes.ReporteComisionesVigentesView;
import com.americacg.cargavirtual.web.mngbeans.reportes.ReporteCompraSaldoProveedorView;
import com.americacg.cargavirtual.web.mngbeans.reportes.ReporteComprobantesExternosView;
import com.americacg.cargavirtual.web.mngbeans.reportes.ReporteConsSaldoProvProdView;
import com.americacg.cargavirtual.web.mngbeans.reportes.ReporteCuentaCorrienteView;
import com.americacg.cargavirtual.web.mngbeans.reportes.ReporteDepAdelView;
import com.americacg.cargavirtual.web.mngbeans.reportes.ReporteEstadoBancosView;
import com.americacg.cargavirtual.web.mngbeans.reportes.ReporteEstadoProveedoresView;
import com.americacg.cargavirtual.web.mngbeans.reportes.ReporteFacturasView;
import com.americacg.cargavirtual.web.mngbeans.reportes.ReporteIdsUnicosView;
import com.americacg.cargavirtual.web.mngbeans.reportes.ReporteIncrementoSaldoMayoristaView;
import com.americacg.cargavirtual.web.mngbeans.reportes.ReporteIncrementoSaldoMayoristaWUView;
import com.americacg.cargavirtual.web.mngbeans.reportes.ReporteInsertCargasAutomaticasView;
import com.americacg.cargavirtual.web.mngbeans.reportes.ReporteMASView;
import com.americacg.cargavirtual.web.mngbeans.reportes.ReportePINView;
import com.americacg.cargavirtual.web.mngbeans.reportes.ReportePNetACGView;
import com.americacg.cargavirtual.web.mngbeans.reportes.ReportePagoElectronicoLIPAView;
import com.americacg.cargavirtual.web.mngbeans.reportes.ReporteRentabilidadView;
import com.americacg.cargavirtual.web.mngbeans.reportes.ReporteRepartosView;
import com.americacg.cargavirtual.web.mngbeans.reportes.ReporteRepartosWUView;
import com.americacg.cargavirtual.web.mngbeans.reportes.ReporteSaldoClientesView;
import com.americacg.cargavirtual.web.mngbeans.reportes.ReporteSegurosView;
import com.americacg.cargavirtual.web.mngbeans.reportes.ReporteStockSimsView;
import com.americacg.cargavirtual.web.mngbeans.reportes.ReporteStockTerminalesView;
import com.americacg.cargavirtual.web.mngbeans.reportes.ReporteSubeLgsView;
import com.americacg.cargavirtual.web.mngbeans.reportes.ReporteSubeLotesView;
import com.americacg.cargavirtual.web.mngbeans.reportes.ReporteSubeTrxsView;
import com.americacg.cargavirtual.web.mngbeans.reportes.ReporteSwProvView;
import com.americacg.cargavirtual.web.mngbeans.reportes.ReporteTrxRedView;
import com.americacg.cargavirtual.web.mngbeans.reportes.ReporteTrxView;
import com.americacg.cargavirtual.web.mngbeans.reportes.ReporteWUView;
import com.americacg.cargavirtual.web.model.CabeceraUsuario;
import com.americacg.cargavirtual.web.model.ColumnModel;
import com.americacg.cargavirtual.web.model.IncrementoReparto;
import com.americacg.cargavirtual.web.model.ItemArbol;
import com.americacg.cargavirtual.gestion.model.Factura;
import com.americacg.cargavirtual.web.shared.AutoCompleteClienteView;
import com.americacg.cargavirtual.web.shared.BasePage;

@Named("administracionView")
@Scope(org.springframework.web.context.WebApplicationContext.SCOPE_SESSION)
public class AdministracionView extends BasePage implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8062456850790162675L;

	private String work;

	private List<Jerarquia> clientes = null;
	private TreeNode clientesRoot;

	private Integer nivelesPrecargaArbol = 5;

	private String filtro;
	private String valorFiltro;
	private ArrayOfClienteReducido listCliRed;

	private String ordenamiento = "numerico";
	private String descArbol = "razonSocial";

	// Datos dependientes cliente seleccionado

	private Cliente clienteSeleccionado;
	private Jerarquia jerarquiaSeleccionado;

	private String campoOrdenamientoArbolN1 = "idCliente";
	private String campoOrdenamientoArbolN2 = "idCliente";
	private String campoOrdenamientoArbolN3 = "idCliente";
	private String campoOrdenamientoArbolN4 = "idCliente";
	private String campoOrdenamientoArbolN5 = "idCliente";

	private Saldos saldosClienteSeleccionado;
	private Float estadoCC_clienteSeleccionado;
	private DatosReparto datosReparto;
	private List<IncrementoSaldo> listIncSaldo;
	private List<IncrementoReparto> listIncReparto;
	private List<Boolean> listIncOpcion;
	private ArrayOfProductoComision listaProdCom;

	private ComisionPlanaAdelantada comisPlanaAdel;
	private ClienteEstComisPlanaAdel cliEstComPlanaAdelant;

	private DatosDepAdelContainer datosDepAdelC;
	private Integer depAdelMarcarTodo;

	private DatosAcreditacionComisionesContainer datosAcreditacionComisionesContainer;

	private Integer tipoFiltroCliente;
	private Long idCliente;
	private Boolean ignorarFecha;
	private Date fechaDesdeAcredCom = new Date();
	private Date fechaHastaAcredCom = new Date();
	private Integer acredCcomMarcarTodo;
	private Boolean mostrarDetalleAcreditComis;

	private List<SelectItem> bancosTodos;
	private Long idBancoAcredDepAdel;
	private Integer tipoFiltroClienteDepAdel;
	private Long idClienteDepAdel;
	private Boolean ignorarFechaDepAdel;
	private Boolean ordenarPorIdDepAdelantoDesc;
	private Date fechaDepAdelDesde = new Date();
	private Date fechaDepAdelHasta = new Date();
	private Long idEstadodepyadelanto;
	private Long tipoMovimientoADS;

	private Long totalTrxComisiones;
	private Float totalImporteComisiones;
	private Float totalComisionesSIVA;
	private Float totalComisionesCIVA;
	private Float totalComisionesSIVAaPagar;
	private Float totalComisionesCIVAaPagar;
	private Float totalDepAdel;
	private Long idConceptoCC;
	private Float importeCC;
	private String motivoCC;
	private Float estadoCC_cliente;
	private Float bolsaSaldoClienteEnEstadoCC;
	private Float wu_saldo;
	private Float cc_cliente;
	private Long wu_idDist;
	private String wu_RZdist;
	private Float wu_saldoDist;
	private Float wu_importeReparto;
	private Long id_tipoReparto_wu;
	private List<SelectItem> wu_tiporepartos;

	private Float ecc;
	private Float wu_saldo_header;

	private List<SelectItem> tipoCC;
	private Long idVentanaTrabajoCC = 1L;

	private Long idConceptoCCprov;
	private Long idProveedorCC;
	private Float estadoCC_proveedor;
	private Float bolsaSaldoProvEnEstadoCC;
	private Integer accionBolsaSaldoProv;
	private Integer accionIncrementoSaldoMayorista;
	private Float importeCCprov;
	private String nroFacturaCC;
	private String motivoCCprov;
	private Float valPendPagoFacturaCC;

	private Long idConceptoCCbanco;
	private Long idBancoCC;
	private ArrayOfSelectItem bancos;
	private Float estadoCC_banco;
	private Float importeCCbanco;
	private String motivoCCbanco;
	private Long id_ccbanco_coment;
	private List<SelectItem> cc_banco_comentarios;

	private Long idConceptoCCcaja;
	private Float estadoCC_caja;
	private Float importeCCcaja;
	private String motivoCCcaja;

	private int idRowItemSM_ccprov;
	private Integer listSubMovimientoCC_size_ccprov;
	// private List<SelectItem> subListaAnalitica_ccprov = new
	// ArrayList<SelectItem>();

	private String scrMensajeAlert = "";

	private ArrayOfSubMovimientoCuentaCorriente listSubMovimientoCC;

	private Long sel_cli_idListCom;
	private List<SelectItem> cabeceraTablasComisiones;

	private List<HistorialAsginacionListaComisiones> listHistAsigListCom;

	// Bindings a pantalla
	private boolean[] panels = { false, false, false, false, false, false, false, false, false, false, false, false,
			false };

	// Variables de formularios
	private Float saldo = 0F;
	private Long tipoMoviemiento;
	private String comentarios;
	private MonitorSaldos monitorSaldos;

	// Variables para Incremento de Saldo en la Bolsa de Recargas del Mayorista
	private Long tipoMovimientoISM;
	private List<SelectItem> productosISM;
	private List<SelectItem> proveedores;
	private Long idProductoISM;
	private Long idProveedorISM;

	// Variables para Incremento de Saldo en la Bolsa de Western Union del Mayorista
	private Float saldoMayWU;
	private Long id_tipoReparto_wu_may;
	private Float importe_inc_mwu;
	private String comentario_incSaldo_wu;

	private Boolean parametrosComisionesVigentesActualizados = false;
	private Boolean mostrarReporteComisionesVigentesD = false;
	private Date movExtFechaDesde;
	private Date movExtfechaHasta;
	private Integer movExtIdEstado;
	private String movExtTipoComp;
	private String movExtnumeroComp;
	private Boolean movExtIgnorarFecha;
	private String movExtIdClienteExt;
	private Float movExtImporte;
	private Long movExtIdCliente;
	private List<ComprobanteExterno> lce;

	// VARIABLES PARA ORDENAMIENTO DE LA LISTA DE CLIENTES PARA REPARTO MASIVO DE
	// SALDO
	private String ordenamientoAseleccionarRM = "numerico";
	private String ordenamientoRM = "numerico";
	private String descArbolRM = "razonSocial";
	private Integer scrollPageRM = 1;
	private String valorFiltroJerarquiaRM = null;
	private int ultimoPanel = -1;
	private String tabPanelSelectedTab = "areaDeTrabajo:tabAdmin";
	private String modalPanelInformesGenericosContenidoAIncluir = null;
	private String modalPanelInformesGenericosTitulo = null;
	private AutoCompleteClienteView autoCompleteCliente;
	private ArrayOfJerarquia aojClientes = null;
	// Listas del Arbol
	private LazyDataModel<ItemArbol> listClientesNivel1 = null;
	private LazyDataModel<ItemArbol> listClientesNivel2 = null;
	private LazyDataModel<ItemArbol> listClientesNivel3 = null;
	private LazyDataModel<ItemArbol> listClientesNivel4 = null;
	private LazyDataModel<ItemArbol> listClientesNivel5 = null;
	// Seleccion de niveles de Arbol
	private ItemArbol clienteSeleccionado1 = null;
	private ItemArbol clienteSeleccionado2 = null;
	private ItemArbol clienteSeleccionado3 = null;
	private ItemArbol clienteSeleccionado4 = null;
	private ItemArbol clienteSeleccionado5 = null;

	// Administracion de usuarios
	private LazyDataModel<CabeceraUsuario> listCabeceraUsuarios = null;
	private CabeceraUsuario cabeceraUsuario = null;

	// Listas para combos de busqueda puntual de cliente
	private ArrayList<SelectItem> provincias = null;
	private ArrayList<SelectItem> localidades = null;
	private ArrayList<SelectItem> tiposTerminal = null;
	private ArrayList<SelectItem> tiposCliente = null;
	private ArrayList<SelectItem> estadosCliente = null;
	private LazyDataModel<ItemArbol> listResuBusCli = null;

	// Filtros de busqueda puntual de cliente
	private Long provinciaFlt = null;
	private Long localidadFlt = null;
	private Long tipoTerminalFlt = null;
	private String tipoClienteFlt = null;
	private Long idClienteFlt = null;
	private Long idUsuarioFlt = null;
	private String idAuxFlt = null;
	private String idAux2Flt = null;
	private Long idEntidadFlt = null;
	private String razonSocialFlt = null;
	private String nombreFantasiaFlt = null;
	private String usuarioFlt = null;
	private String cuitFlt = null;

	private boolean muestraConsultaCliente = false;
	private Integer indiceMenu = null;
	private boolean muestraMnuIconos = false;
	private String mnuOpcIconos = "";
	private String opcAIncluir = "";
	private String auxiliarAIncluir = "";
	private List<ColumnModel> columnasNivel1;
	private List<ColumnModel> columnasNivel2;
	private List<ColumnModel> columnasNivel3;
	private List<ColumnModel> columnasNivel4;
	private List<ColumnModel> columnasNivel5;
	private boolean muestraArbol = true;
	private String tituloPanelProducto = "";
	private boolean muestraCRUDUsuario = false;
	private Usuario usuarioSeleccionado = null;
	private Integer usuarioRowIndex = null;
	private OperacionEnEjecucion operacion;

	private String origen = "";
	
	private Long prodCom_idTipoProducto;

	public void consultarSaldoDistribuidor(ActionEvent evt) {

		try {

			// CONSULTA DE SALDO PARA MOSTRAR EN distMainPage.xhtm (en barra)
			Float saldo = GestionServiceHelper.getGestionService(CfgTimeout.DEFAULT).obtenerSaldo(
					this.getUsuario().getIdMayorista(), this.getUsuario().getIdCliente(),
					this.getUsuario().getIdDistribuidor());
			this.getUsuario().setSaldoDisponible(saldo);

			// CONSULTA DE LIMITE DE DESCUBIERTO
			Float limDesc = GestionServiceHelper.getGestionService(CfgTimeout.DEFAULT).limDesc(
					this.getUsuario().getIdMayorista(), this.getUsuario().getIdCliente(),
					this.getUsuario().getPermitirLimiteDescubierto());
			this.getUsuario().setValorLimiteDescubierto(limDesc.intValue());

		} catch (Exception e) {
			this.getUsuario().setSaldoDisponible(null);
			// TODO: handle exception
		}

	}

	public void devolverHistorialAsignacionListaComisiones(ActionEvent a) {

		// System.out.println("devolverHistorialAsignacionListaComisiones");

		try {

			listHistAsigListCom = null;

			ArrayOfHistorialAsginacionListaComisiones r = GestionServiceHelper.getGestionService(CfgTimeout.DEFAULT)
					.devolverHistorialAsignacionListaComisiones(this.getUsuario().getIdMayorista(),
							clienteSeleccionado.getIdCliente().getValue(), 15);

			if (r == null) {
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
						"El metodo devolverHistorialAsignacionListaComisiones devolvio null", null));
			} else if (r.getHistorialAsginacionListaComisiones() == null) {
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
						"El metodo devolverHistorialAsignacionListaComisiones devolvio null", null));
				/*
				 * TODO: NO Hay manejo de error } else if (r.getError().get.getHayError()) {
				 * FacesContext.getCurrentInstance().addMessage(null, new
				 * FacesMessage(FacesMessage.SEVERITY_ERROR, "Error: " +
				 * r.getError().getMsgError(), null));
				 */
			} else if (r.getHistorialAsginacionListaComisiones().size() < 1) {
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
						"No se encontraron registros para el cliente seleccionado", null));

			} else {

				listHistAsigListCom = r.getHistorialAsginacionListaComisiones();

			}

		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
					"Error consultando el metodo devolverHistorialAsignacionListaComisiones", null));
		}

	}

	public void resetVariablesHistorialListaCom(ActionEvent a) {

		// System.out.println("resetVariablesHistorialListaCom");
		listHistAsigListCom = null;

	}

	public void onTabChange(TabChangeEvent event) {
		/*
		 * TODO comento estas lineas ya que este funcionamiento va a cambiar no se
		 * cargaria el bean dentro de este otro bean
		 * 
		 * if (event.getTab().getId().equals("tabUsu")) { UsuariosForm usrForm =
		 * (UsuariosForm) FacesUtils.getManagedBean("usrForm"); usrForm.reset();
		 * usrForm.setIdCliente(this.clienteSeleccionado.getIdCliente());
		 * usrForm.cargarUsuarios(); } else if
		 * ((event.getTab().getId().equals("tabJer"))) { ClienteForm cliForm =
		 * (ClienteForm) FacesUtils.getManagedBean("cliForm"); cliForm.reset();
		 * cliForm.setClienteDistribuidor(this.clienteSeleccionado);
		 * cliForm.cargarClientesYresetearFiltro(); } else if
		 * ((event.getTab().getId().equals("tabComisiones"))) { TablasComisionesForm
		 * tablasComisionesForm = (TablasComisionesForm) FacesUtils
		 * .getManagedBean("tablasComisionesForm"); //
		 * tablasComisionesForm.setClienteDistribuidor(this.clienteSeleccionado);
		 * tablasComisionesForm.cargarCabecerasDeTablasDeComisiones(); } else if
		 * ((event.getTab().getId().equals("tabMon"))) { MonitorTRXs monTRXs =
		 * (MonitorTRXs) FacesUtils.getManagedBean("monTRXs");
		 * monTRXs.inicializarMonitoreo(); }
		 */
	}

	public void seleccionCliente(Long idClienteSel) {
		// Long idClienteSel =
		// (Long.decode(FacesUtils.getRequestParameter("idCliente")));
		this.clienteSeleccionado = GestionServiceHelper.getGestionService(CfgTimeout.DEFAULT)
				.mostrarCliente(this.getUsuario().getIdMayorista(), idClienteSel);

		// Reseteo datos
		this.saldosClienteSeleccionado = null;
		this.estadoCC_clienteSeleccionado = null;
		this.datosReparto = null;
		this.listaProdCom = null;
		this.comisPlanaAdel = null;
		this.cliEstComPlanaAdelant = null;
		this.listIncSaldo = null;
		this.listIncReparto = null;
		this.listIncReparto = null;

		this.datosDepAdelC = null;

		// System.out.println("Panel previo: |" + this.tabPanelSelectedTab + "|");

		// Coloco el panel en la 1er solapa
		// tabPanel.setSelectedTab("tabAdmin"); //TODO ESTA LINEA TRAE PROBLEMA. Para
		// reproducirlo: ir a distribuidor 2, luego ir a jerarquia y luego hacer click
		// en Mayorista. Luego falla al seleccionar alguna de las pestanias (reparto
		// masivo de saldo por ej)

		// System.out.println("Panel posterior: |" + this.tabPanelSelectedTab + "|");

		// Reinicio UsrForm
		/*
		 * UsuariosForm usrForm = (UsuariosForm)FacesUtils.getManagedBean("usrForm");
		 * usrForm.reset(); usrForm.setIdCliente(idClienteSel);
		 * 
		 * if ("areaDeTrabajo:tabUsu".equals(this.tabPanelSelectedTab)){
		 * usrForm.cargarUsuarios(); }
		 * 
		 * 
		 * // Reinicio CliForm ClienteForm cliForm =
		 * (ClienteForm)FacesUtils.getManagedBean("cliForm"); cliForm.reset();
		 * cliForm.setClienteDistribuidor(this.clienteSeleccionado);
		 * 
		 * if ("areaDeTrabajo:tabJer".equals(this.tabPanelSelectedTab)){
		 * cliForm.cargarClientesYresetearFiltro(); }
		 */

		this.panels = null;
		ultimoPanel = -1;

		/*
		 * if ("tabAdmin".equals(this.tabPanelSelectedTab)){
		 * 
		 * refrescarPanels = true;
		 * 
		 * switch (ultimoPanel) { case 0: expandirContraerPanel0(); break; case 1:
		 * expandirContraerPanel1(); break; case 2: expandirContraerPanel2(); break;
		 * case 3: expandirContraerPanel3(); break; case 4: expandirContraerPanel4();
		 * break; case 5: expandirContraerPanel5(); break; case 6:
		 * expandirContraerPanel6(); break; case 7: expandirContraerPanel7(); break;
		 * case 8: expandirContraerPanel8(); break; case 9: expandirContraerPanel9();
		 * break; case 10: expandirContraerPanel10(); break; case 11:
		 * expandirContraerPanel11(); break; case 12: expandirContraerPanel12(); break;
		 * default: break; }
		 * 
		 * refrescarPanels = false;
		 * 
		 * }
		 */

	}

	private void cargarMonitoreoSaldos() {

		try {
			this.monitorSaldos = GestionServiceHelper.getGestionService(CfgTimeout.DEFAULT)
					.monitoreoSaldos(this.getUsuario().getIdMayorista(), clienteSeleccionado.getIdCliente().getValue());
		} catch (RuntimeException e) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
					"Error ejecutando SW de monitoreo de Saldos:" + e.getMessage(), null));
		}

	}

	public String modificarHabilitacionProdCom() {

		boolean huboErr = false;

		try {
			for (ProductoComision prodCom : listaProdCom.getProductoComision()) {

				ResultadoBase r = GestionServiceHelper.getGestionService(CfgTimeout.DEFAULT)
						.habilitarDeshabilitarProducto(this.getUsuario().getIdMayorista(),
								clienteSeleccionado.getIdCliente().getValue(), prodCom.getIdProducto().getValue(),
								prodCom.getHabilitado().getValue(), prodCom.getIdProveedor().getValue());

				if (r.getError().getValue().getHayError().getValue()) {
					FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
							r.getError().getValue().getMsgError().getValue(), null));
					huboErr = true;
				}

			}
		} catch (WebServiceException ste) {
			if (ste.getCause().getClass().equals(SocketTimeoutException.class)) {
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
						"No se pudo establecer la comunicación con GESTION.\n Por favor intente nuevamente.", null));
			} else {
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
						"Error realizando consulta contra GESTION: " + ste.getMessage(), null));
			}
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
					"Error modificando estado de Producto Comision: " + e.getMessage(), null));
			return null;
		}

		if (!huboErr) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
					"Se han actualizado correctamente las habilitaciones.", null));
		}

		return null;
	}

	public String modificarHabilitacionComPlana() {

		boolean huboErr = false;

		try {

			Integer comPlanHabilitada = 0;
			if (cliEstComPlanaAdelant.getHabilitada().getValue()) {
				comPlanHabilitada = 1;
			}

			ResultadoBase r = GestionServiceHelper.getGestionService(CfgTimeout.DEFAULT).modificarCondComPlanAdelCli(
					this.getUsuario().getIdMayorista(), clienteSeleccionado.getIdCliente().getValue(),
					comPlanHabilitada);

			if (r.getError().getValue().getHayError().getValue()) {
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
						r.getError().getValue().getMsgError().getValue(), null));
				huboErr = true;
				// Si hubo error deshabilito el prod comision
				cliEstComPlanaAdelant.getHabilitada().setValue(false);
			}

		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
					"Error modificando estado de Comision Plana: " + e.getMessage(), null));
			return null;
		}

		if (!huboErr) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
					"Se ha actualizado correctamente la habilitacion de la comision plana.", null));
		}

		return null;
	}

	public String cancelarHabilitacionProdCom() {
		cargarListaProductoComision(null);
		FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
				"Se ha restablecido correctamente las habilitaciones.", null));
		return null;
	}

	public String cancelarHabilitacionComPlana() {
		cargarDatosComisPlanaAdelantada(null);
		FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
				"Se ha restablecido correctamente la habilitacion de la Comision Plana.", null));
		return null;
	}

	public void modificarCabListCom(ActionEvent a) {

		scrMensajeAlert = "";

		// System.out.println("modificarCabListCom. sel_cli_idListCom: |" +
		// sel_cli_idListCom + "|");

		Long idListaSeleccionada = null;
		if (sel_cli_idListCom > 0) {
			idListaSeleccionada = sel_cli_idListCom;
		}

		ResultadoBase r = GestionServiceHelper.getGestionService(CfgTimeout.DEFAULT).modificarIdListaComisionEnCliente(
				this.getUsuario().getIdMayorista(), clienteSeleccionado.getIdCliente().getValue(), idListaSeleccionada,
				this.getUsuario().getIdCliente(), this.getUsuario().getUsername(), this.getUsuario().getPassword());

		if (r.getError().getValue().getHayError().getValue()) {
			scrMensajeAlert = "La modalidad de cambio de comisiones no fue modificada. "
					+ r.getError().getValue().getMsgError().getValue();

		} else if (idListaSeleccionada == null) {

			scrMensajeAlert = "Al cliente " + clienteSeleccionado.getRazonSocial().getValue()
					+ " se le desvinculo la lista de comisiones que tenia asignada."
					+ " Las comisiones se calcularan a traves de la tabla de comisiones por producto individual";

		} else {

			scrMensajeAlert = "El cliente " + clienteSeleccionado.getRazonSocial().getValue()
					+ " quedo relacionado a la lista de comisiones";

		}

		// Vuelvo a Cargar la Lista de Producto/Comision y leer la lista de comision que
		// tenia cargada el cliente
		cargarListaProductoComision(null);

	}

	// REPARTO DE SALDO AL PUNTO
	public void expandirContraerPanel0(ActionEvent evt) {

		if (realizarCargaDatosPanel(0)) {
			leerDatosParaRepartoAlPunto();
		}

	}

	public void leerDatosParaRepartoAlPunto() {

		this.saldo = 0F;
		this.tipoMoviemiento = 1L;
		this.comentarios = "";

		saldosClienteSeleccionado = GestionServiceHelper.getGestionService(CfgTimeout.DEFAULT)
				.obtenerSaldoDelNodoSuperiorEinferior(this.getUsuario().getIdMayorista(),
						clienteSeleccionado.getIdCliente().getValue(), 1,
						clienteSeleccionado.getIdDistribuidorSuperior().getValue());

		estadoCC_clienteSeleccionado = GestionServiceHelper.getGestionService(CfgTimeout.DEFAULT).ccEstado(1L,
				this.getUsuario().getIdMayorista(), clienteSeleccionado.getIdCliente().getValue());

		cargarMonitoreoSaldos();
	}

	private boolean realizarCargaDatosPanel(int nroPanel) {

		boolean r = false;

		try {

			if (ultimoPanel != nroPanel) {
				ultimoPanel = nroPanel;
				// Entonces lo esta abriendo

				if (this.clienteSeleccionado != null) {
					r = true;
				} else {
					// No selecciono ningun cliente
					panels[nroPanel] = false;
					FacesContext.getCurrentInstance().addMessage(null,
							new FacesMessage(FacesMessage.SEVERITY_ERROR, "Debe seleccionar un Cliente", null));
				}

				/*
				 * if((nroPanel == 3) && (!parametrosComisionesVigentesActualizados)){
				 * 
				 * ParametrosList lp =
				 * GestionServiceHelper.getGestionService(CfgTimeout.DEFAULT).mostrarParametros(
				 * this.getUsuario(). getIdMayorista(), "P",
				 * "mostrarReporteComisionesVigentesD"); if (lp != null &&
				 * lp.getListParametros() != null && lp.getListParametros().size() == 1){
				 * mostrarReporteComisionesVigentesD =
				 * ("1".equals(lp.getListParametros().get(0).getValor())); }
				 * parametrosComisionesVigentesActualizados = true; }
				 */

			} else {
				ultimoPanel = -1;
			}

		} catch (Exception e) {
			// System.out.println("Error en realizarCargaDatosPanel. " + e.getMessage() +
			// "|");
		}

		return r;
	}

	// REPARTO MASIVO DE SALDO A CLIENTES DEL DISTRIBUIDOR
	public void expandirContraerPanel1(ActionEvent evt) {
		try {
			if (realizarCargaDatosPanel(1)) {

				valorFiltroJerarquiaRM = null;
				scrollPageRM = 1;
				leerDatosParaRepartoMasivo();
			}

		} catch (Exception e) {

			// System.out.println("Excepcion en expandirContraerPanel1. " + e.getMessage());

			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
					"Error al tratar de leer datos para el reparto: |" + e.getMessage() + "|", null));
		}

	}

	public void leerDatosParaRepartoMasivoConAE(ActionEvent evt) {

		try {

			scrollPageRM = 1;

			leerDatosParaRepartoMasivo();

		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
					"Error al tratar de leer datos para el reparto con ActionEvent: |" + e.getMessage() + "|", null));
		}

	}

	public void leerDatosParaRepartoMasivo() {

		ordenamientoAseleccionarRM = "numerico";

		if (ordenamientoRM == "numerico") {
			ordenamientoAseleccionarRM = "numerico";

		} else if (ordenamientoRM.equals("alfabetico") && descArbolRM.equals("razonSocial")) {
			ordenamientoAseleccionarRM = "alfabeticoRazSoc";

		} else if (ordenamientoRM.equals("alfabetico") && descArbolRM.equals("nombreFantasia")) {
			ordenamientoAseleccionarRM = "alfabeticoFantasia";
		} else {
			ordenamientoAseleccionarRM = "numerico";
		}

		datosReparto = GestionServiceHelper.getGestionService(CfgTimeout.DEFAULT).leerDatosReparto(
				this.getUsuario().getIdUsuario(), this.getUsuario().getPassword(),
				clienteSeleccionado.getIdCliente().getValue(), this.getUsuario().getIdMayorista(), 1, "", true,
				ordenamientoAseleccionarRM, valorFiltroJerarquiaRM, null, null);
		cargarMonitoreoSaldos();

		if (datosReparto == null) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
					"Los datos devueltos para el raparto son null", null));
		} else {
			if (datosReparto.getError().getValue().getHayError().getValue()) {
				FacesContext.getCurrentInstance().addMessage(null,
						new FacesMessage(FacesMessage.SEVERITY_ERROR,
								"Error leyendo datos para el reparto: |"
										+ datosReparto.getError().getValue().getCodigoError().getValue() + ", "
										+ datosReparto.getError().getValue().getMsgError().getValue() + "|",
								null));
			} else {
				// Inicializo vector con IncrementoSaldo
				listIncSaldo = new ArrayList<IncrementoSaldo>();
				listIncReparto = new ArrayList<IncrementoReparto>();
				listIncOpcion = new ArrayList<Boolean>();
				for (SaldoCliente sc : datosReparto.getListSaldoCliente().getValue().getSaldoCliente()) {
					IncrementoSaldo is = new IncrementoSaldo();
					is.setIdCliente(sc.getIdCliente());
					// is.setIdDistribuidor(clienteSeleccionado.getIdCliente());
					is.getIdTipoMovimiento().setValue(1L);
					listIncSaldo.add(is);

					IncrementoReparto ir = new IncrementoReparto();
					ir.setIncremento(is);
					ir.setSaldo(sc);
					listIncReparto.add(ir);

					listIncOpcion.add(Boolean.FALSE);
				}
			}
		}

	}

	// PRODUCTOS Y COMISIONES
	public void expandirContraerPanel2(ActionEvent evt) {

		if (realizarCargaDatosPanel(2)) {
			cargarListaProductoComision(null);

			// Cargo La Lista de Comisiones Planas Adelantadas solo si esta habilitada para
			// la plataforma en la tabla configgral
			if (this.getUsuario().getPermitirComisionPlanaAdelantada() == 1) {
				cargarDatosComisPlanaAdelantada(null);
			}

		}

	}

	// INCREMENTO DE SALDO A MAYORISTA
	public void expandirContraerPanel4(ActionEvent evt) {

		// Reseteo variables de Incremento de Saldo Mayorista para la Bolsa de Recargas
		this.tipoMovimientoISM = null;
		this.productosISM = null;
		this.idProductoISM = 0L;
		this.proveedores = null;
		this.idProveedorISM = 0L;
		saldo = 0F;

		// Reseteo variables de Incremento de Saldo Mayorista para la Bolsa de Western
		// Union
		saldoMayWU = GestionServiceHelper.getGestionService(CfgTimeout.DEFAULT)
				.wuObtenerSaldo(this.getUsuario().getIdMayorista(), GestionServiceHelper
						.getGestionService(CfgTimeout.DEFAULT).buscarIdCliMayorista(this.getUsuario().getIdMayorista()))
				.getValor().getValue();
		id_tipoReparto_wu_may = null;
		importe_inc_mwu = 0F;
		comentario_incSaldo_wu = "";

		// Abro/Cierro Paneles
		if (realizarCargaDatosPanel(4)) {
		}

	}

	public void actualizarProductos(ActionEvent evt) {
		// No hace falta hacer nada
	}

	public void cambiarVentanaTrabajoCC(ActionEvent evt) {

		limpiarAltaCC();
		limpiarAltaCC_prov();
		limpiarAltaCC_banco();
		limpiarAltaCC_caja();

		listSubMovimientoCC = null;
		// this.subListaAnalitica_ccprov = null;
		// this.subListaAnalitica_ccprov = new ArrayList<SelectItem>();

		// System.out.println("idVentanaTrabajoCC: |" + idVentanaTrabajoCC + "|");
	}

	// INFORME DE DEPOSITOS Y SOLICITUD DE ADELANTOS
	public void expandirContraerPanel6(ActionEvent evt) {

		if (realizarCargaDatosPanel(6)) {
			// No hago nada, solo uso esto para expandir o contraer los paneles.
		}

	}

	// ACREDITACION DE DEPOSITOS O ADELANTOS DE SALDO
	public void expandirContraerPanel5(ActionEvent evt) {

		if (realizarCargaDatosPanel(5)) {
			// Limpio los datos de la solapa de Depositos y Adelantos
			idBancoAcredDepAdel = null;
			datosDepAdelC = null;
			tipoFiltroClienteDepAdel = 2;
			idClienteDepAdel = this.getUsuario().getIdCliente();
			ignorarFechaDepAdel = false;
			ordenarPorIdDepAdelantoDesc = false;
			fechaDepAdelDesde = new Date();
			fechaDepAdelHasta = new Date();
			depAdelMarcarTodo = 0;
			idEstadodepyadelanto = 1L;
			tipoMovimientoADS = null;
		}

	}

	public void leerDatosDepAdel() {

		try {

			// Limpio la lista
			this.datosDepAdelC = null;
			depAdelMarcarTodo = 0;
			totalDepAdel = 0F;

			// TODO validar filtros...
			Boolean filtroOK = false;
			if (tipoFiltroClienteDepAdel == 1) {
				if (idClienteDepAdel != null) {
					filtroOK = true;
				} else {
					FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
							"El idCliente en el filtro no puede ser vacio", null));
				}

			} else {
				if (tipoFiltroClienteDepAdel == 2) {
					if (idClienteDepAdel != null) {
						filtroOK = true;
					} else {
						FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
								"El idCliente en el filtro no puede ser vacio", null));
					}
				} else {
					if (tipoFiltroClienteDepAdel == 3) {
						if (idClienteDepAdel == null) {
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

			// TODO error en ignorarFecha

			if (filtroOK) {

				Integer igfe = 3;
				if (ignorarFechaDepAdel) {
					igfe = 1;
				}

				GregorianCalendar gcFechaHoraDesde = new GregorianCalendar();
				gcFechaHoraDesde.setTime(fechaDepAdelDesde);
				XMLGregorianCalendar xmlGCFechaHoraDesde = DatatypeFactory.newInstance()
						.newXMLGregorianCalendar(gcFechaHoraDesde);

				GregorianCalendar gcFechaHoraHasta = new GregorianCalendar();
				gcFechaHoraHasta.setTime(fechaDepAdelHasta);
				XMLGregorianCalendar xmlGCFechaHoraHasta = DatatypeFactory.newInstance()
						.newXMLGregorianCalendar(gcFechaHoraHasta);

				datosDepAdelC = GestionServiceHelper.getGestionService(CfgTimeout.DEFAULT).leerDatosDepAdel(
						this.getUsuario().getIdMayorista(), this.getUsuario().getIdCliente(),
						clienteSeleccionado.getIdCliente().getValue(),
						clienteSeleccionado.getNivelDistribuidorSuperior().getValue(), tipoMovimientoADS,
						idEstadodepyadelanto, igfe, xmlGCFechaHoraDesde, xmlGCFechaHoraHasta, idBancoAcredDepAdel,
						tipoFiltroClienteDepAdel, idClienteDepAdel, clienteSeleccionado.getIdCliente().getValue(),
						this.getUsuario().getUsername(), this.getUsuario().getPassword(), null, false, true, 0, false,
						ordenarPorIdDepAdelantoDesc);

				if (datosDepAdelC != null) {
					if (datosDepAdelC.getError().getValue().getHayError().getValue()) {
						FacesContext.getCurrentInstance().addMessage(null,
								new FacesMessage(FacesMessage.SEVERITY_ERROR,
										"Error: |" + datosDepAdelC.getError().getValue().getCodigoError().getValue()
												+ ", " + datosDepAdelC.getError().getValue().getMsgError().getValue()
												+ "|",
										null));
					} else {
						if (datosDepAdelC.getListDatosDepAdel().getValue().getDatosDepAdel().isEmpty()) {
							FacesContext.getCurrentInstance().addMessage(null,
									new FacesMessage(FacesMessage.SEVERITY_ERROR,
											"No se encontraron Depositos ni Solicitudes de adelanto pendientes", null));
						} else {
							for (DatosDepAdel dda : datosDepAdelC.getListDatosDepAdel().getValue().getDatosDepAdel()) {
								dda.getAccionDepAdel().setValue(0);
								totalDepAdel = totalDepAdel + dda.getSaldoPedido().getValue();
							}
						}
					}
				} else {
					FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
							"El WS de busqueda de Depositos y Adelantos devolvio null", null));
				}
			}
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
					"Error ejecutando el WS de lectura de Depositos y Adelantos Pendientes: |" + e.getMessage() + "|",
					null));
			return;
		}
	}

	public void marcDesmarcDepAdel(ActionEvent ev) {

		if (datosDepAdelC != null) {
			if (datosDepAdelC.getListDatosDepAdel() != null && datosDepAdelC.getListDatosDepAdel().getValue() != null
					&& datosDepAdelC.getListDatosDepAdel().getValue().getDatosDepAdel() != null
					&& !datosDepAdelC.getListDatosDepAdel().getValue().getDatosDepAdel().isEmpty()) {
				if (depAdelMarcarTodo == 0) {
					for (DatosDepAdel dda : datosDepAdelC.getListDatosDepAdel().getValue().getDatosDepAdel()) {
						// Accion = 0(Ignorar)
						dda.getAccionDepAdel().setValue(0);
					}
				} else if (depAdelMarcarTodo == 1) {
					for (DatosDepAdel dda : datosDepAdelC.getListDatosDepAdel().getValue().getDatosDepAdel()) {
						// Accion = 1(Aceptar)
						dda.getAccionDepAdel().setValue(1);
					}
				} else if (depAdelMarcarTodo == 2) {
					for (DatosDepAdel dda : datosDepAdelC.getListDatosDepAdel().getValue().getDatosDepAdel()) {
						// Accion = 2(Borrar)
						dda.getAccionDepAdel().setValue(2);
					}
				} else if (depAdelMarcarTodo == 3) {
					for (DatosDepAdel dda : datosDepAdelC.getListDatosDepAdel().getValue().getDatosDepAdel()) {

						if (dda.getIdTipoMovimiento().getValue() != 14) {
							// Accion =3(Acreditar Y Dejar Todo Pendiente de Pago)
							dda.getAccionDepAdel().setValue(3);
						} else {
							// Accion =0(Ignorar)
							dda.getAccionDepAdel().setValue(0);
						}
					}
				} else if (depAdelMarcarTodo == 4) {
					for (DatosDepAdel dda : datosDepAdelC.getListDatosDepAdel().getValue().getDatosDepAdel()) {

						if (dda.getIdEstado().getValue() == 4) {
							// Si estado = 4(Acreditado y Pendiente de Pago)
							// Accion = 4(Marcar como pagado un registro que estaba acreditado y pendiente
							// de pago)
							dda.getAccionDepAdel().setValue(4);
						} else {
							// Accion =0(Ignorar)
							dda.getAccionDepAdel().setValue(0);
						}
					}
				} else if (depAdelMarcarTodo == 5) {
					for (DatosDepAdel dda : datosDepAdelC.getListDatosDepAdel().getValue().getDatosDepAdel()) {

						if ((dda.getIdEstado().getValue() == 1) && (dda.getIdTipoMovimiento().getValue() != 14)) {
							// Si estado = 1(Pendiente)
							// Accion = 5(No Acreditar en la Bolsa y Aprobar Movimiento)
							dda.getAccionDepAdel().setValue(5);
						} else {
							// Accion =0(Ignorar)
							dda.getAccionDepAdel().setValue(0);
						}
					}
				} else if (depAdelMarcarTodo == 6) {
					for (DatosDepAdel dda : datosDepAdelC.getListDatosDepAdel().getValue().getDatosDepAdel()) {

						if (dda.getIdEstado().getValue() == 4) {
							// Si estado = 4(Acreditado y Pendiente de Pago)
							// Accion = 6(Marcar como NO pagado un registro que estaba acreditado y
							// pendiente de pago)
							dda.getAccionDepAdel().setValue(6);
						} else {
							// Accion =0(Ignorar)
							dda.getAccionDepAdel().setValue(0);
						}
					}
				}
			}
		}
	}

	public void cancelarDepAdel(ActionEvent ev) {

		try {
			this.datosDepAdelC = null;
			leerDatosDepAdel();
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_INFO, "Cancelacion OK", null));

		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR,
							"Error tratando de leer nuevamente los registros de Depositos y Adelantos: |"
									+ e.getMessage() + "|",
							null));
		}

	}

	public void aceptarDepAdel(ActionEvent ev) {

		try {

			Integer i = 1;
			Integer regsAprocesar = 0;

			ArrayOfDatosDepAdel listRegsAprocesar = new ArrayOfDatosDepAdel();
			listRegsAprocesar.getDatosDepAdel().clear();

			// RECORRO LA LISTA Y CREO UNA LISTA NUEVA SOLO CON LOS REGISTROS QUE SE DEBEN
			// PROCESAR
			for (DatosDepAdel dda : datosDepAdelC.getListDatosDepAdel().getValue().getDatosDepAdel()) {

				/*
				 * System.out.println("DepAdel (" + i + ") --> " + "idMayorista:|" +
				 * dda.getIdMayorista() + "|" + "idDepAdelanto:|" + dda.getIdDepAdelanto() + "|"
				 * + "idClienteDist:|" + dda.getIdClienteDist() + "|" + "razSocClienteDist:|" +
				 * dda.getClienteDist() + "|" + "idCliente: |" + dda.getIdCliente() + "|" +
				 * "razSosCliente: |" + dda.getCliente() + "|" + "idTipoMovimiento: |" +
				 * dda.getIdTipoMovimiento() + "|" + "tipoMovimiento: |" +
				 * dda.getTipoMovimiento() + "|" + "importe: |" + dda.getSaldoPedido() + "|" +
				 * "accion: |" + dda.getAccionDepAdel() + "|" + "comentarioAceptacion: |" +
				 * dda.getComentarioAceptacion() + "|" + "comentarioAcreditPago: |" +
				 * dda.getComentarioAcreditPago() + "|" );
				 */

				// accionDepAdel --> 0=Ignorar
				// 1=Aceptar (Acreditar y Marcar como Pagado)
				// 2=Borrar
				// 3=Acreditar Y Dejar Todo Pendiente de Pago
				// 4=Marcar como pagado un registro que estaba acreditado y pendiente de pago
				// 5=No Acreditar en la Bolsa y Aprobar Movimiento
				// 6=Marcar como NO pagado

				if ((dda.getAccionDepAdel().getValue() == 1) || (dda.getAccionDepAdel().getValue() == 2)
						|| (dda.getAccionDepAdel().getValue() == 3) || (dda.getAccionDepAdel().getValue() == 4)
						|| (dda.getAccionDepAdel().getValue() == 5) || (dda.getAccionDepAdel().getValue() == 6)) {
					regsAprocesar = regsAprocesar + 1;
					listRegsAprocesar.getDatosDepAdel().add(dda);
				}

				i = i + 1;
			}

			if (regsAprocesar >= 1) {

				// PROCESO LA LISTA DE DEPOSITOS Y ADELANTOS CONTRA GESTION
				RespDepAdelContainer res = GestionServiceHelper.getGestionService(CfgTimeout.DEFAULT).aceptarDepAdel(
						this.getUsuario().getIdMayorista(), this.getUsuario().getIdCliente(),
						this.getUsuario().getUsername(), this.getUsuario().getIdUsuario(),
						this.getUsuario().getPassword(), listRegsAprocesar);

				// MUESTRO POR PANTALLA EL RESULTADO DE LA EJECUCION
				if (res.getError().getValue().getHayError().getValue()) {
					FacesContext.getCurrentInstance().addMessage(null,
							new FacesMessage(FacesMessage.SEVERITY_ERROR,
									"Error: |" + res.getError().getValue().getCodigoError().getValue() + ", "
											+ res.getError().getValue().getMsgError().getValue(),
									null));
				} else {

					for (RespDepAdel r : res.getListRespDepAdel().getValue().getRespDepAdel()) {

						if (r.getError().getValue().getHayError().getValue()) {
							FacesContext.getCurrentInstance().addMessage(null,
									new FacesMessage(FacesMessage.SEVERITY_ERROR,
											r.getError().getValue().getCodigoError().getValue() + ", "
													+ r.getError().getValue().getMsgError().getValue(),
											null));

							// System.out.println(r.getError().getCodigoError() + ", " +
							// r.getError().getMsgError());

						} else {
							// Accion: --> 0=Ignorar 1=Aceptar 2=Borrar
							FacesContext.getCurrentInstance().addMessage(null,
									new FacesMessage(FacesMessage.SEVERITY_INFO, r.getDescProceso().getValue(), null));

						}
					}
				}

				// Limpio la lista luego de hacer las acreditaciones de saldo
				this.datosDepAdelC = null;
				leerDatosDepAdel();

			} else {
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
						"Debe seleccionar algun registro para Acreditar o Rechazar", null));
			}

		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
					"Error ejecutando el WS de Acreditacion de Depositos o Adelanto de Saldo: |" + e.getMessage() + "|",
					null));
		}

	}

	public void cargarListaProductoComision(ActionEvent ev) {

		try {
			sel_cli_idListCom = 0L;
			this.listaProdCom = null;

			// Verificar si el cliente tiene una lista de comisiones asignada

			RespIdString r = GestionServiceHelper.getGestionService(CfgTimeout.DEFAULT)
					.devolverIdListaComisionAsignadaAunCliente(this.getUsuario().getIdMayorista(),
							clienteSeleccionado.getIdCliente().getValue(), this.getUsuario().getIdCliente(),
							this.getUsuario().getUsername(), this.getUsuario().getPassword());

			if ((r != null) && (!r.getError().getValue().getHayError().getValue())) {

				sel_cli_idListCom = r.getId().getValue();

			}

			// Cargar lista de comisiones y habilitacion de productos por cliente
			this.listaProdCom = GestionServiceHelper.getGestionService(CfgTimeout.DEFAULT).mostrarProductoComision(
					this.getUsuario().getIdMayorista(), clienteSeleccionado.getIdCliente().getValue(),
					clienteSeleccionado.getIdDistribuidorSuperior().getValue(),
					clienteSeleccionado.getTipoCliente().getValue(), true, this.prodCom_idTipoProducto, 1 );

		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
					"Error tratando de obtener la lista de productos y comisiones para el cliente", null));
		}

	}

	public void cargarDatosComisPlanaAdelantada(ActionEvent ev) {
		this.comisPlanaAdel = GestionServiceHelper.getGestionService(CfgTimeout.DEFAULT).datosComisPlanaAdelantada(
				this.getUsuario().getIdMayorista(), clienteSeleccionado.getIdCliente().getValue());

		this.cliEstComPlanaAdelant = GestionServiceHelper.getGestionService(CfgTimeout.DEFAULT)
				.clienteEstComisPlanaAdel(this.getUsuario().getIdMayorista(),
						clienteSeleccionado.getIdCliente().getValue());

	}

	// ACREDITACION DE COMISIONES
	public void expandirContraerPanel7(ActionEvent evt) {

		if (realizarCargaDatosPanel(7)) {

			// Limpio los datos de la solapa de Acreditacion de Comisiones
			datosAcreditacionComisionesContainer = null;
			tipoFiltroCliente = 2;
			idCliente = this.getUsuario().getIdCliente();
			ignorarFecha = false;
			fechaDesdeAcredCom = new Date();
			fechaHastaAcredCom = new Date();
			acredCcomMarcarTodo = 0;
			mostrarDetalleAcreditComis = false;
		}

	}

	// CUENTA CORRIENTE
	public void expandirContraerPanel9(ActionEvent evt) {

		if (realizarCargaDatosPanel(9)) {
			try {
				limpiarAltaCC();
				limpiarAltaCC_prov();
				limpiarAltaCC_banco();
				limpiarAltaCC_caja();
				idVentanaTrabajoCC = 1L;
			} catch (Exception e) {
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
						"Error ejecutando Cuenta Corriente: " + e.getMessage(), null));
			}
		}
	}

	// REPARTO DE SALDO WESTERN UNION
	public void expandirContraerPanel10(ActionEvent evt) {

		if (realizarCargaDatosPanel(10)) {
			try {
				limpiarAltaCC();
			} catch (Exception e) {
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
						"Error ejecutando Reparto de Saldo Western Union: " + e.getMessage(), null));
			}
		}
	}

	private void limpiarAltaCC() {
		this.idConceptoCC = null;
		this.importeCC = null;
		this.motivoCC = null;
		this.autoCompleteCliente.setId(null);
		this.autoCompleteCliente.setDescripcion(null);
	}

	private void limpiarAltaCC_prov() {
		this.idConceptoCCprov = null;
		this.idProveedorCC = null;
		this.estadoCC_proveedor = null;
		this.bolsaSaldoProvEnEstadoCC = null;
		this.accionBolsaSaldoProv = 0;
		this.accionIncrementoSaldoMayorista = 0;
		this.listSubMovimientoCC = null;
		this.importeCCprov = null;
		this.nroFacturaCC = null;
		this.motivoCCprov = null;
		this.valPendPagoFacturaCC = null;
	}

	private void limpiarAltaCC_banco() {
		this.id_ccbanco_coment = null;
		this.idConceptoCCbanco = null;
		this.idBancoCC = null;
		this.estadoCC_banco = null;
		this.importeCCbanco = null;
		this.motivoCCbanco = null;

		this.idProveedorCC = null;
		this.nroFacturaCC = null;

		this.valPendPagoFacturaCC = null;
	}

	private void limpiarAltaCC_caja() {
		this.idConceptoCCcaja = null;
		this.estadoCC_caja = null;
		this.importeCCcaja = null;
		this.motivoCCcaja = null;

		this.idProveedorCC = null;
		this.nroFacturaCC = null;

		this.valPendPagoFacturaCC = null;
	}

	public void altaMovmientoCC(ActionEvent evt) {
		// Alta Movimiento CC de Cliente
		Boolean limpiarAltaCC = true;
		try {

			// Alta de Movimiento en Cuenta Corriente de Cliente (tipoCC = 1)
			ResultadoBaseConIdyDesc r = GestionServiceHelper.getGestionService(CfgTimeout.DEFAULT).altaMovimientoCC(
					this.getUsuario().getIdMayorista(), 1L, this.autoCompleteCliente.getId(),
					this.getUsuario().getIdCliente(), this.getUsuario().getIdUsuario(), this.getUsuario().getPassword(),
					this.idConceptoCC, null, null, null, null, null, this.importeCC, this.motivoCC, 0, null, null, null,
					null, null, null, 0);

			if (r == null) {
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
						"Error en alta de Movimiento CC de Cliente", null));
			} else {
				if (r.getError().getValue().getHayError().getValue()) {
					FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
							"Error: " + r.getError().getValue().getMsgError().getValue(), null));
					limpiarAltaCC = false;
				} else {
					FacesContext.getCurrentInstance().addMessage(null,
							new FacesMessage(FacesMessage.SEVERITY_INFO, r.getDescripcion().getValue(), null));
				}
			}
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
					"Error realizando altaMovmientoCC de Cliente: " + e.getMessage(), null));
		}
		if (limpiarAltaCC) {
			limpiarAltaCC();
		}
		return;
	}

	public void altaMovmientoCC_prov(ActionEvent evt) {
		Boolean limpiarAltaCC_prov = true;
		try {

			if ((listSubMovimientoCC != null) && (listSubMovimientoCC.getSubMovimientoCuentaCorriente().size() > 0)
					&& ((listSubMovimientoCC.getSubMovimientoCuentaCorriente()
							.get(listSubMovimientoCC.getSubMovimientoCuentaCorriente().size() - 1).getTipoCC()
							.getValue() == null)
							|| (listSubMovimientoCC.getSubMovimientoCuentaCorriente()
									.get(listSubMovimientoCC.getSubMovimientoCuentaCorriente().size() - 1).getTipoCC()
									.getValue() == 0))) {

				listSubMovimientoCC.getSubMovimientoCuentaCorriente()
						.remove(listSubMovimientoCC.getSubMovimientoCuentaCorriente().size() - 1);

			}

			// Alta de Movimiento en Cuenta Corriente de Proveedor (tipoCC = 2)
			ResultadoBaseConIdyDesc r = GestionServiceHelper.getGestionService(CfgTimeout.DEFAULT).altaMovimientoCC(
					this.getUsuario().getIdMayorista(), 2L, idProveedorCC, this.getUsuario().getIdCliente(),
					this.getUsuario().getIdUsuario(), this.getUsuario().getPassword(), this.idConceptoCCprov, null,
					null, null, null, null, this.importeCCprov, this.motivoCCprov, this.accionBolsaSaldoProv,
					this.listSubMovimientoCC, null, this.nroFacturaCC, null, null, null,
					accionIncrementoSaldoMayorista);

			if (r == null) {
				FacesContext.getCurrentInstance().addMessage(null,
						new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error en alta de Movimiento de CC_prov", null));
			} else {
				if (r.getError().getValue().getHayError().getValue()) {

					FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
							"Error: " + r.getError().getValue().getMsgError().getValue(), null));

					if ("E".equals(r.getError().getValue().getCodigoError().getValue())) {
						limpiarAltaCC_prov = false;
					}

				} else {

					FacesContext.getCurrentInstance().addMessage(null,
							new FacesMessage(FacesMessage.SEVERITY_INFO, r.getDescripcion().getValue(), null));
				}

				// Muestro el resultado de Cada Item
				if ((r.getLista() != null) && (r.getLista().getValue() != null)
						&& (r.getLista().getValue().getResultadoBaseConIdyDesc() != null)
						&& (!r.getLista().getValue().getResultadoBaseConIdyDesc().isEmpty())
						&& (r.getLista().getValue().getResultadoBaseConIdyDesc().size() > 0)) {

					for (ResultadoBaseConIdyDesc i : r.getLista().getValue().getResultadoBaseConIdyDesc()) {
						if (i != null) {
							if (i.getError().getValue().getHayError().getValue()) {
								FacesContext.getCurrentInstance().addMessage(null,
										new FacesMessage(FacesMessage.SEVERITY_ERROR,
												"      * " + i.getError().getValue().getMsgError().getValue(), null));
							} else {
								FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(
										FacesMessage.SEVERITY_INFO, "      * " + i.getDescripcion().getValue(), null));
							}
						}

					}
				}

			}
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
					"Error realizando altaMovmientoCC_prov: " + e.getMessage(), null));
		}
		if (limpiarAltaCC_prov) {
			limpiarAltaCC_prov();
		}
		return;
	}

	public void altaMovmientoCC_banco(ActionEvent evt) {
		Boolean limpiarAltaCC_banco = true;
		try {

			// Alta de Movimiento en Cuenta Corriente de Banco (tipoCC = 3)
			ResultadoBaseConIdyDesc r = GestionServiceHelper.getGestionService(CfgTimeout.DEFAULT).altaMovimientoCC(
					this.getUsuario().getIdMayorista(), 3L, idBancoCC, this.getUsuario().getIdCliente(),
					this.getUsuario().getIdUsuario(), this.getUsuario().getPassword(), this.idConceptoCCbanco, null,
					null, null, null, null, this.importeCCbanco, this.motivoCCbanco, 0, null, this.idProveedorCC,
					this.nroFacturaCC, null, id_ccbanco_coment, null, 0);

			if (r == null) {
				FacesContext.getCurrentInstance().addMessage(null,
						new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error en alta de Movimiento de CC_banco", null));
			} else {
				if (r.getError().getValue().getHayError().getValue()) {
					FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
							"Error: " + r.getError().getValue().getMsgError().getValue(), null));
					limpiarAltaCC_banco = false;
				} else {
					FacesContext.getCurrentInstance().addMessage(null,
							new FacesMessage(FacesMessage.SEVERITY_INFO, r.getDescripcion().getValue(), null));
				}
			}
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
					"Error realizando altaMovmientoCC_banco: " + e.getMessage(), null));
		}
		if (limpiarAltaCC_banco) {
			limpiarAltaCC_banco();
		}
		return;
	}

	public void altaMovmientoCC_caja(ActionEvent evt) {
		Boolean limpiarAltaCC_caja = true;
		try {

			// Alta de Movimiento en Cuenta Corriente de Caja (tipoCC = 4)
			ResultadoBaseConIdyDesc r = GestionServiceHelper.getGestionService(CfgTimeout.DEFAULT).altaMovimientoCC(
					this.getUsuario().getIdMayorista(), 4L, this.getUsuario().getIdCliente(),
					this.getUsuario().getIdCliente(), this.getUsuario().getIdUsuario(), this.getUsuario().getPassword(),
					this.idConceptoCCcaja, null, null, null, null, null, this.importeCCcaja, this.motivoCCcaja, 0, null,
					this.idProveedorCC, this.nroFacturaCC, null, null, null, 0);

			if (r == null) {
				FacesContext.getCurrentInstance().addMessage(null,
						new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error en alta de Movimiento de CC_caja", null));
			} else {
				if (r.getError().getValue().getHayError().getValue()) {
					FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
							"Error: " + r.getError().getValue().getMsgError().getValue(), null));
					limpiarAltaCC_caja = false;
				} else {
					FacesContext.getCurrentInstance().addMessage(null,
							new FacesMessage(FacesMessage.SEVERITY_INFO, r.getDescripcion().getValue(), null));
				}
			}
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
					"Error realizando altaMovmientoCC_caja: " + e.getMessage(), null));
		}
		if (limpiarAltaCC_caja) {
			limpiarAltaCC_caja();
		}
		return;
	}

	public void leerDatosAcredComis() {

		try {

			// Limpio la lista
			this.datosAcreditacionComisionesContainer = null;
			acredCcomMarcarTodo = 0;

			totalTrxComisiones = 0L;
			totalImporteComisiones = 0F;
			totalComisionesSIVA = 0F;
			totalComisionesCIVA = 0F;
			totalComisionesSIVAaPagar = 0F;
			totalComisionesCIVAaPagar = 0F;

			// TODO validar filtros...
			Boolean filtroOK = false;
			if (tipoFiltroCliente == 1) {
				if (idCliente != null) {
					filtroOK = true;
				} else {
					FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
							"El idCliente en el filtro no puede ser vacio", null));
				}

			} else {
				if (tipoFiltroCliente == 2) {
					if (idCliente != null) {
						filtroOK = true;
					} else {
						FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
								"El idCliente en el filtro no puede ser vacio", null));
					}
				} else {
					if (tipoFiltroCliente == 3) {
						if (idCliente == null) {
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

			// TODO error en ignorarFecha

			if (filtroOK) {

				Integer igfe = 0;
				if (ignorarFecha) {
					igfe = 1;
				}

				GregorianCalendar gcFechaHoraDesde = new GregorianCalendar();
				gcFechaHoraDesde.setTime(fechaDesdeAcredCom);
				XMLGregorianCalendar xmlGCFechaHoraDesde = DatatypeFactory.newInstance()
						.newXMLGregorianCalendar(gcFechaHoraDesde);

				GregorianCalendar gcFechaHoraHasta = new GregorianCalendar();
				gcFechaHoraHasta.setTime(fechaHastaAcredCom);
				XMLGregorianCalendar xmlGCFechaHoraHasta = DatatypeFactory.newInstance()
						.newXMLGregorianCalendar(gcFechaHoraHasta);

				datosAcreditacionComisionesContainer = GestionServiceHelper.getGestionService(CfgTimeout.DEFAULT)
						.leerDatosAcredComis(this.getUsuario().getIdMayorista(), this.getUsuario().getIdCliente(),
								clienteSeleccionado.getIdCliente().getValue(), tipoFiltroCliente, idCliente, -1L, igfe,
								xmlGCFechaHoraDesde, xmlGCFechaHoraHasta, this.getUsuario().getUsername(),
								this.getUsuario().getPassword());

				if (datosAcreditacionComisionesContainer != null) {
					if (datosAcreditacionComisionesContainer.getError().getValue().getHayError().getValue()) {
						FacesContext.getCurrentInstance().addMessage(null,
								new FacesMessage(FacesMessage.SEVERITY_ERROR,
										"Error: |"
												+ datosAcreditacionComisionesContainer.getError().getValue()
														.getCodigoError().getValue()
												+ ", " + datosAcreditacionComisionesContainer.getError().getValue()
														.getMsgError().getValue()
												+ "|",
										null));
					} else {
						if (datosAcreditacionComisionesContainer.getListDatosAcreditacionComisiones().getValue()
								.getDatoAcreditacionComisiones().isEmpty()) {
							FacesContext.getCurrentInstance().addMessage(null,
									new FacesMessage(FacesMessage.SEVERITY_INFO,
											"No se encontraron registros con el criterio seleccionado", null));
						} else {
							for (DatoAcreditacionComisiones dac : datosAcreditacionComisionesContainer
									.getListDatosAcreditacionComisiones().getValue().getDatoAcreditacionComisiones()) {
								dac.getAccionAcreditacion().setValue(0);

								totalTrxComisiones = totalTrxComisiones + dac.getCantTRX().getValue();
								totalImporteComisiones = totalImporteComisiones + dac.getImporteVendido().getValue();
								totalComisionesSIVA = totalComisionesSIVA + dac.getComisionCalculadaSinIVA().getValue();
								totalComisionesCIVA = totalComisionesCIVA + dac.getComisionCalculadaConIVA().getValue();

								// TOTALES PARA EL RESUMEN DE COMISIONES A PAGAR
								if (dac.getAcredComisionConIVA().getValue() == 0) {
									// TOTAL DE COMISION SIN IVA A PAGAR
									totalComisionesSIVAaPagar = totalComisionesSIVAaPagar
											+ dac.getComisionCalculadaSinIVA().getValue();
								} else {
									// TOTAL DE COMISION CON IVA A PAGAR
									totalComisionesCIVAaPagar = totalComisionesCIVAaPagar
											+ dac.getComisionCalculadaConIVA().getValue();
								}
							}
						}
					}
				} else {
					FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
							"Respuesta nula en la busqueda de registros para acreditar Comisiones", null));
				}
			}

		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR,
							"Error ejecutando el WS de lectura de datos para acreditacion de comisiones: |"
									+ e.getMessage() + "|",
							null));

		}
		return;

	}

	public void marcDesmarcAcredComis(ActionEvent ev) {

		if (datosAcreditacionComisionesContainer != null) {
			if (datosAcreditacionComisionesContainer != null
					&& datosAcreditacionComisionesContainer.getListDatosAcreditacionComisiones() != null
					&& datosAcreditacionComisionesContainer.getListDatosAcreditacionComisiones().getValue() != null
					&& datosAcreditacionComisionesContainer.getListDatosAcreditacionComisiones().getValue()
							.getDatoAcreditacionComisiones() != null
					&& !datosAcreditacionComisionesContainer.getListDatosAcreditacionComisiones().getValue()
							.getDatoAcreditacionComisiones().isEmpty()) {
				if (acredCcomMarcarTodo == 0) {
					// Desmarcar todas las comisiones
					for (DatoAcreditacionComisiones dac : datosAcreditacionComisionesContainer
							.getListDatosAcreditacionComisiones().getValue().getDatoAcreditacionComisiones()) {
						dac.getAccionAcreditacion().setValue(0);
					}
				} else if (acredCcomMarcarTodo == 1) {
					// Marcar todas las comisiones
					for (DatoAcreditacionComisiones dac : datosAcreditacionComisionesContainer
							.getListDatosAcreditacionComisiones().getValue().getDatoAcreditacionComisiones()) {
						dac.getAccionAcreditacion().setValue(1);
					}
				} else if (acredCcomMarcarTodo == 2) {
					// Rechazar todas las comisiones
					for (DatoAcreditacionComisiones dac : datosAcreditacionComisionesContainer
							.getListDatosAcreditacionComisiones().getValue().getDatoAcreditacionComisiones()) {
						dac.getAccionAcreditacion().setValue(2);
					}
				}
			}
		}
	}

	public void aceptarAcredComis(ActionEvent ev) {

		try {

			Integer i = 1;
			Integer regsAprocesar = 0;

			// Creo la lista con los registros que estan marcadas para ser acreditadas y
			// tienen importe mayor a cero
			ArrayOfDatoAcreditacionComisiones lac = new ArrayOfDatoAcreditacionComisiones();
			lac.getDatoAcreditacionComisiones().clear();

			if (datosAcreditacionComisionesContainer != null
					&& datosAcreditacionComisionesContainer.getListDatosAcreditacionComisiones() != null
					&& datosAcreditacionComisionesContainer.getListDatosAcreditacionComisiones().getValue() != null
					&& datosAcreditacionComisionesContainer.getListDatosAcreditacionComisiones().getValue()
							.getDatoAcreditacionComisiones() != null) {
				for (DatoAcreditacionComisiones dac : datosAcreditacionComisionesContainer
						.getListDatosAcreditacionComisiones().getValue().getDatoAcreditacionComisiones()) {
					/*
					 * System.out.println("Comision (" + i + ") --> " + "idConsecutivo:|" +
					 * dac.getIdConsecutivo() + "|" + "idMayorista:|" + dac.getIdMayorista() + "|" +
					 * "fecha:|" + dac.getFecha() + "|" + "idClienteDist:|" + dac.getIdClienteDist()
					 * + "|" + "razSocClienteDist:|" + dac.getClienteDist() + "|" + "idCliente: |" +
					 * dac.getIdCliente() + "|" + "razSosCliente: |" + dac.getCliente() + "|" +
					 * "cantTRX: |" + dac.getCantTRX() + "|" + "importeVendido: |" +
					 * dac.getImporteVendido() + "|" + "comisionCalculada: |" +
					 * dac.getComisionCalculada() + "|" + "detalle: |" + dac.getDetalle() + "|" +
					 * "saldoSuf: |" + dac.getSaldoSuficiente() + "|" + "accion: |" +
					 * dac.getAccionAcreditacion() + "|" + "comentarioAceptacion: |" +
					 * dac.getComentarioAceptacion() + "|" + "comisionAcreditada: |" +
					 * dac.getComisionAcreditada() + "|");
					 */
					if (((dac.getAccionAcreditacion().getValue() == 1)
							&& ((dac.getComisionCalculadaSinIVA().getValue() != 0)
									|| (dac.getComisionCalculadaConIVA().getValue() != 0)))
							|| (dac.getAccionAcreditacion().getValue() == 2)) {

						regsAprocesar = regsAprocesar + 1;
						lac.getDatoAcreditacionComisiones().add(dac);
					}

					i = i + 1;
				}
			}

			if (regsAprocesar >= 1) {

				RespAcreditComisionesContainer res = GestionServiceHelper.getGestionService(CfgTimeout.DEFAULT)
						.aceptarAcredComis(this.getUsuario().getIdMayorista(), this.getUsuario().getIdCliente(),
								this.getUsuario().getIdUsuario(), lac, this.getUsuario().getUsername(),
								this.getUsuario().getPassword());

				if (res.getError().getValue().getHayError().getValue()) {
					FacesContext.getCurrentInstance().addMessage(null,
							new FacesMessage(FacesMessage.SEVERITY_ERROR,
									"Error: |" + res.getError().getValue().getCodigoError().getValue() + ", "
											+ res.getError().getValue().getMsgError().getValue(),
									null));
				} else {

					if (mostrarDetalleAcreditComis) {
						// Pantalla donde muestro el detalle de las comisiones acreditadas
						if (res != null && res.getListRespAcreditComisiones() != null
								&& res.getListRespAcreditComisiones().getValue() != null
								&& res.getListRespAcreditComisiones().getValue().getRespAcreditComisiones() != null) {
							for (RespAcreditComisiones r : res.getListRespAcreditComisiones().getValue()
									.getRespAcreditComisiones()) {

								if (r.getError().getValue().getHayError().getValue()) {
									FacesContext.getCurrentInstance()
											.addMessage(null,
													new FacesMessage(FacesMessage.SEVERITY_ERROR,
															r.getError().getValue().getCodigoError().getValue() + ", "
																	+ r.getError().getValue().getMsgError().getValue(),
															null));

									// System.out.println(r.getError().getCodigoError() + ", " +
									// r.getError().getMsgError());

								} else {
									// Accion: --> 0=Ignorar 1=Aceptar
									FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(
											FacesMessage.SEVERITY_INFO, r.getDescProceso().getValue(), null));

								}
							}
						}
					} else {
						// Pantalla donde muestro el resumen de las comisiones acreditadas
						Integer cantComisAcreditadas = 0;
						Integer cantComisNOAcreditadas = 0;
						Integer cantComisRechazadas = 0;
						Integer cantComisQueNoSePudieronRechazar = 0;
						if (res != null && res.getListRespAcreditComisiones() != null
								&& res.getListRespAcreditComisiones().getValue() != null
								&& res.getListRespAcreditComisiones().getValue().getRespAcreditComisiones() != null)
							for (RespAcreditComisiones r : res.getListRespAcreditComisiones().getValue()
									.getRespAcreditComisiones()) {

								if (r.getAccion().getValue() == 1) {
									// ACREDITADA
									if (r.getError().getValue().getHayError().getValue()) {
										cantComisNOAcreditadas++;
									} else {
										cantComisAcreditadas++;
									}

								} else if (r.getAccion().getValue() == 2) {
									// RECHAZADA
									if (r.getError().getValue().getHayError().getValue()) {
										cantComisQueNoSePudieronRechazar++;
									} else {
										cantComisRechazadas++;
									}

								}
							}
						FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,
								"Cantidad de Comisiones Acreditadas: " + cantComisAcreditadas, null));
						FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,
								"Cantidad de Comisiones NO Acreditadas: " + cantComisNOAcreditadas, null));
						FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,
								"Cantidad de Comisiones Rechazadas: " + cantComisRechazadas, null));
						FacesContext.getCurrentInstance().addMessage(null,
								new FacesMessage(FacesMessage.SEVERITY_INFO,
										"Cantidad de Comisiones Que no se pudieron Rechazar: "
												+ cantComisQueNoSePudieronRechazar,
										null));
					}
				}

				// Para limpiar la pantalla luego de hacer las acreditaciones de comisiones
				this.datosAcreditacionComisionesContainer = null;
				leerDatosAcredComis();

			} else {
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,
						"Debe seleccionar algun registro para Acreditar y la comision a acreditar debe ser mayor a cero",
						null));
			}

		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
					"Error ejecutando el WS de Acreditacion de Comisiones: |" + e.getMessage() + "|", null));
		}

	}

	public void cancelarAcredComis(ActionEvent ev) {

		try {
			this.datosAcreditacionComisionesContainer = null;
			leerDatosAcredComis();
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_INFO, "Cancelacion OK", null));
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
					"Error tratando de leer nuevamente los registros de comisiones: |" + e.getMessage() + "|", null));
		}

	}

	// INFORMES
	public void expandirContraerPanel3(ActionEvent evt) {

		if (realizarCargaDatosPanel(3)) {

			if (!parametrosComisionesVigentesActualizados) {

				ParametrosList lp = GestionServiceHelper.getGestionService(CfgTimeout.DEFAULT).mostrarParametros(
						this.getUsuario().getIdMayorista(), "P", "mostrarReporteComisionesVigentesD");
				if (lp != null && lp.getListParametros() != null && lp.getListParametros().getValue() != null
						&& lp.getListParametros().getValue().getParametro() != null
						&& lp.getListParametros().getValue().getParametro().size() == 1) {
					mostrarReporteComisionesVigentesD = ("1"
							.equals(lp.getListParametros().getValue().getParametro().get(0).getValor().getValue()));
				}
				parametrosComisionesVigentesActualizados = true;
			}
		}

	}

	public void repartirSaldoMayo(ActionEvent evt) {

		try {

			RespIdString rs = GestionServiceHelper.getGestionService(CfgTimeout.DEFAULT).incrementoSaldoMayorista(
					this.getUsuario().getIdMayorista(), this.getUsuario().getIdCliente(),
					this.getUsuario().getUsername(), this.getUsuario().getPassword(), saldo, comentarios,
					tipoMovimientoISM, idProveedorISM, idProductoISM, "");

			if (rs == null) {
				FacesContext.getCurrentInstance().addMessage(null,
						new FacesMessage(FacesMessage.SEVERITY_ERROR, "El WS devolvio NULL", null));
			} else if (rs.getError() != null && rs.getError().getValue().getHayError().getValue()) {
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
						"Error cargando saldo: " + rs.getError().getValue().getMsgError().getValue(), null));
			} else {
				// OK!!!
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,
						"Carga realizada exitosamente. idIncSaldoMayorista: |" + rs.getId().getValue() + "|", null));

				// ACTUALIZO LOS SALDOS QUE MUESTRO EN PANTALLA
				this.getUsuario().setSaldoDisponible(this.getUsuario().getSaldoDisponible() + saldo);

				tipoMovimientoISM = null;
				idProveedorISM = null;
				idProductoISM = null;

			}

		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR,
							"Error realizando el Incremento de Saldo en la Bolsa de Saldo de Recargas del Mayorista: "
									+ e.getMessage(),
							null));
			return;
		}

		this.saldo = 0F;
		this.comentarios = "";

		return;
	}

	public void repartirSaldoMayoWU(ActionEvent evt) {
		try {

			Long idClienteMayorista = GestionServiceHelper.getGestionService(CfgTimeout.DEFAULT)
					.buscarIdCliMayorista(this.getUsuario().getIdMayorista());

			RespString r = GestionServiceHelper.getGestionService(CfgTimeout.DEFAULT).wuAceptarReparto(
					this.getUsuario().getIdMayorista(), this.getUsuario().getIdCliente(),
					this.getUsuario().getIdUsuario(), this.getUsuario().getPassword(), idClienteMayorista,
					id_tipoReparto_wu_may, importe_inc_mwu, "", comentario_incSaldo_wu);

			if (r == null) {
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
						"La respuesta del metodo de Incremento de Saldo de la Bolsa de Western Union fue null", null));
			} else {

				if (r.getError().getValue().getHayError().getValue()) {
					FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
							"Error: " + r.getError().getValue().getMsgError().getValue(), null));
				} else {
					saldoMayWU = GestionServiceHelper.getGestionService(CfgTimeout.DEFAULT)
							.wuObtenerSaldo(this.getUsuario().getIdMayorista(), idClienteMayorista).getValor()
							.getValue();
					id_tipoReparto_wu_may = null;
					importe_inc_mwu = 0F;
					comentario_incSaldo_wu = "";

					FacesContext.getCurrentInstance().addMessage(null,
							new FacesMessage(FacesMessage.SEVERITY_INFO, "Incremento OK: " + r.getRespuesta(), null));
				}
			}

		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
					"Error realizando el Incremento de Saldo en la Bolsa de Saldo de Western Union del Mayorista: "
							+ e.getMessage(),
					null));
			return;
		}

		return;
	}

	public void wu_repartirSaldo(ActionEvent evt) {

		try {

			RespString r = GestionServiceHelper.getGestionService(CfgTimeout.DEFAULT).wuAceptarReparto(
					this.getUsuario().getIdMayorista(), this.getUsuario().getIdCliente(),
					this.getUsuario().getIdUsuario(), this.getUsuario().getPassword(), this.autoCompleteCliente.getId(),
					id_tipoReparto_wu, wu_importeReparto, "", "");

			if (r == null) {
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
						"La respuesta del metodo de Reparto de Saldo de WU fue null", null));
			} else {

				if (r.getError().getValue().getHayError().getValue()) {
					FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
							"Error: " + r.getError().getValue().getMsgError().getValue(), null));
				} else {
					this.autoCompleteCliente.setId(null);
					this.autoCompleteCliente.setDescripcion(null);
					this.wu_saldo = null;
					this.cc_cliente = null;
					this.wu_idDist = null;
					this.wu_RZdist = null;
					this.wu_saldoDist = null;
					this.wu_importeReparto = null;
					this.id_tipoReparto_wu = null;

					FacesContext.getCurrentInstance().addMessage(null,
							new FacesMessage(FacesMessage.SEVERITY_INFO, "Reparto OK: " + r.getRespuesta(), null));
				}
			}

		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error en reparto de saldo de WU", null));
		}
		return;
	}

	public void repartirSaldoPunto(ActionEvent evt) {

		if (this.saldosClienteSeleccionado == null
				|| this.saldosClienteSeleccionado.getSaldoDisponibleDelCliente() == null) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
					"El Cliente seleccionado no tiene saldo inicializado.", null));
			return;
			/*
			 * } else if ( (saldo.compareTo(this.saldosClienteSeleccionado.
			 * getSaldoDisponibleDelDistribuidor()) > 0) &&
			 * (!this.getUsuario().getPermitirLimiteDescubierto())) {
			 * FacesContext.getCurrentInstance().addMessage(null, new
			 * FacesMessage(FacesMessage.SEVERITY_ERROR,
			 * "Saldo insuficiente para realizar el reparto", null); return;
			 */
		} else if (saldo == 0) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
					"El saldo a repartir debe ser distinto de cero.", null));
			return;
		}

		ArrayOfIncrementoSaldo l = new ArrayOfIncrementoSaldo();

		IncrementoSaldo is = new IncrementoSaldo();
		is.setIdCliente(clienteSeleccionado.getIdCliente());
		// is.setIdDistribuidor(clienteSeleccionado.getIdDistribuidorSuperior());
		is.getValorAincrementar().setValue(this.saldo);
		is.getIdTipoMovimiento().setValue(this.tipoMoviemiento);
		is.getComentario().setValue(this.comentarios);

		l.getIncrementoSaldo().add(is);

		// TODO REVISAR REPARTO AL PUNTO
		RespRepSaldo resul;
		try {

			resul = GestionServiceHelper.getGestionService(CfgTimeout.DEFAULT).aceptarReparto(
					this.getUsuario().getIdMayorista(), this.getUsuario().getIdUsuario(),
					this.getUsuario().getPassword(), clienteSeleccionado.getIdDistribuidorSuperior().getValue(), l, "");

			if (resul.getError().getValue().getHayError().getValue()) {
				FacesContext.getCurrentInstance().addMessage(null,
						new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error al realizar el reparto de saldo", null));
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
						resul.getError().getValue().getMsgError().getValue(), null));
				return;
			} else {
				// Recorrer el array de registros procesados y mostrarlo en la pantalla
				if (resul.getListItemRespRepSaldo() != null && resul.getListItemRespRepSaldo().getValue() != null
						&& resul.getListItemRespRepSaldo().getValue().getItemRespRepSaldo() != null) {
					for (ItemRespRepSaldo irrs : resul.getListItemRespRepSaldo().getValue().getItemRespRepSaldo()) {

						if (irrs.getError().getValue().getHayError().getValue()) {
							FacesContext.getCurrentInstance()
									.addMessage(null,
											new FacesMessage(FacesMessage.SEVERITY_ERROR,
													"* idCliente: " + irrs.getIdCliente() + ", "
															+ irrs.getError().getValue().getMsgError().getValue(),
													null));
						} else {
							FacesContext.getCurrentInstance().addMessage(null,
									new FacesMessage(FacesMessage.SEVERITY_INFO,
											"* El saldo al cliente (" + irrs.getIdCliente().getValue()
													+ ") se repartio exitosamente. idReparto: "
													+ irrs.getIdRepartoSaldo().getValue(),
											null));
						}
					}
				}
			}

		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
					"Error realizando el reparto al punto: " + e.getMessage(), null));
			return;
		}

		leerDatosParaRepartoAlPunto();

		return;
	}

	public void repartirSaldoMasivo(ActionEvent evt) {

		float sumaReparto = 0F;

		ArrayOfIncrementoSaldo l = new ArrayOfIncrementoSaldo();
		// Sumarizo
		int i = 0;
		int cantArepartir = 0;
		for (IncrementoSaldo is : listIncSaldo) {
			if (listIncOpcion.get(i) && (is.getValorAincrementar().getValue() != 0)) {
				sumaReparto += is.getValorAincrementar().getValue();
				l.getIncrementoSaldo().add(is);
				cantArepartir = cantArepartir + 1;
			}
			i++;
		}

		if (cantArepartir == 0) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
					"Debe seleccionar al menos un cliente para realizar el reparto y los repartos deben ser distintos de cero",
					null));
			return;
		}

		/*
		 * else if ((sumaReparto >
		 * datosReparto.getSaldoDisponibleParaRepartir().floatValue()) &&
		 * (!this.getUsuario().getPermitirLimiteDescubierto())) {
		 * FacesContext.getCurrentInstance().addMessage(null, new
		 * FacesMessage(FacesMessage.SEVERITY_ERROR,
		 * "Saldo insuficiente para realizar el reparto", null); return; }
		 */

		// SI LAS PREVALIDACIONES ESTAN OK --> REALIZO EL REPARTO MASIVO DE SALDO
		realizarRepartoMasivoDeSaldo(l);

		// Refresco la pantalla de Acreditacion masiva de saldo leyendo los datos de la
		// base de datos
		leerDatosParaRepartoMasivo();

		return;
	}

	private void realizarRepartoMasivoDeSaldo(ArrayOfIncrementoSaldo l) {

		// REPARTO MASIVO DE SALDO
		RespRepSaldo resul;
		try {

			resul = GestionServiceHelper.getGestionService(CfgTimeout.DEFAULT).aceptarReparto(
					this.getUsuario().getIdMayorista(), this.getUsuario().getIdUsuario(),
					this.getUsuario().getPassword(), clienteSeleccionado.getIdCliente().getValue(), l, "");

			if (resul.getError().getValue().getHayError().getValue()) {
				FacesContext.getCurrentInstance().addMessage(null,
						new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error al realizar el reparto masivo", null));
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
						resul.getError().getValue().getMsgError().getValue(), null));

				return;
			} else {

				// Recorrer el array y mostrarlo en la pantalla
				if (resul.getListItemRespRepSaldo() != null && resul.getListItemRespRepSaldo().getValue() != null
						&& resul.getListItemRespRepSaldo().getValue().getItemRespRepSaldo() != null) {
					for (ItemRespRepSaldo irrs : resul.getListItemRespRepSaldo().getValue().getItemRespRepSaldo()) {
						if (irrs.getError().getValue().getHayError().getValue()) {
							FacesContext.getCurrentInstance()
									.addMessage(null,
											new FacesMessage(FacesMessage.SEVERITY_ERROR,
													"* idCliente: " + irrs.getIdCliente() + ", "
															+ irrs.getError().getValue().getMsgError().getValue(),
													null));
						} else {
							FacesContext.getCurrentInstance().addMessage(null,
									new FacesMessage(FacesMessage.SEVERITY_INFO,
											"* El saldo al cliente (" + irrs.getIdCliente()
													+ ") se repartio exitosamente. idReparto: "
													+ irrs.getIdRepartoSaldo(),
											null));
						}
					}
				}

			}

		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
					"Error realizando el reparto masivo: " + e.getMessage(), null));
			return;
		}
	}

	public void autoIncrementarSaldo(ActionEvent evt) {

		try {

			if (this.getUsuario().getPermitirIncrementoAutomaticoDeSaldo()) {

				ordenamientoAseleccionarRM = "numerico";

				if (ordenamientoRM == "numerico") {
					ordenamientoAseleccionarRM = "numerico";

				} else if (ordenamientoRM.equals("alfabetico") && descArbolRM.equals("razonSocial")) {
					ordenamientoAseleccionarRM = "alfabeticoRazSoc";

				} else if (ordenamientoRM.equals("alfabetico") && descArbolRM.equals("nombreFantasia")) {
					ordenamientoAseleccionarRM = "alfabeticoFantasia";
				} else {
					ordenamientoAseleccionarRM = "numerico";
				}

				// Leo la lista de clientes a los que se le va a realizar el Incremento
				// Automatico de Saldo y Luego realizar el incremento

				DatosReparto datosRepartoParaAutoInc = GestionServiceHelper.getGestionService(CfgTimeout.DEFAULT)
						.leerDatosReparto(this.getUsuario().getIdUsuario(), this.getUsuario().getPassword(),
								clienteSeleccionado.getIdCliente().getValue(), this.getUsuario().getIdMayorista(), 1,
								"", true, ordenamientoAseleccionarRM, valorFiltroJerarquiaRM, null, null);

				// ARMO la lista para realizar autoincremento
				float sumaReparto = 0F;

				ArrayOfIncrementoSaldo l = new ArrayOfIncrementoSaldo();
				// Sumarizo
				int cantArepartir = 0;
				if (datosRepartoParaAutoInc != null && datosRepartoParaAutoInc.getListSaldoCliente() != null
						&& datosRepartoParaAutoInc.getListSaldoCliente().getValue() != null
						&& datosRepartoParaAutoInc.getListSaldoCliente().getValue().getSaldoCliente() != null) {
					for (SaldoCliente sc : datosRepartoParaAutoInc.getListSaldoCliente().getValue().getSaldoCliente()) {
						if ("P".equals(sc.getTipoCliente())) {
							if (sc.getValorParaIncrementoAutomaticoDeSaldo().getValue() > 0) {

								Float valorAincrementar = sc.getValorParaIncrementoAutomaticoDeSaldo().getValue()
										- sc.getSaldoDisponibleActual().getValue();

								if (valorAincrementar > 0) {
									sumaReparto += valorAincrementar;

									IncrementoSaldo is = new IncrementoSaldo();

									is.getIdCliente().setValue(sc.getIdCliente().getValue());
									is.getIdTipoMovimiento().setValue(19L);
									is.getValorAincrementar().setValue(valorAincrementar);
									is.getNroFactura().setValue("");
									is.getComentario().setValue("Usuario efector: " + this.getUsuario().getUsername());
									is.getHashGenerado().setValue("");

									l.getIncrementoSaldo().add(is);
									cantArepartir = cantArepartir + 1;
								}
							}
						}
					}
				}

				if (cantArepartir == 0) {
					FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
							"No hay clientes disponibles para realizar el incremento automatico de saldo y los incrementos deben ser mayores a cero",
							null));
					return;
				}

				/*
				 * else if ((sumaReparto >
				 * datosRepartoParaAutoInc.getSaldoDisponibleParaRepartir().floatValue()) &&
				 * (!this.getUsuario().getPermitirLimiteDescubierto())){
				 * FacesContext.getCurrentInstance().addMessage(null, new
				 * FacesMessage(FacesMessage.SEVERITY_ERROR,
				 * "Saldo insuficiente para realizar el incremento automatico", null); return; }
				 */

				// SI LAS PREVALIDACIONES ESTAN OK --> REALIZO EL REPARTO MASIVO DE SALDO
				realizarRepartoMasivoDeSaldo(l);

			} else {
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
						"La plataforma no esta habilitada para Auto Incremento de Saldo", null));
			}

		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
					"Error en autoIncrementarSaldo: " + e.getMessage(), null));
		}

		// Refresco la pantalla de Acreditacion masiva de saldo leyendo los datos de la
		// base de datos
		leerDatosParaRepartoMasivo();

		return;
	}

	private void loadJerarquia() {

		// Armo el 1er nodo con el Cliente Logueado
		this.clientes = new ArrayList<Jerarquia>(1);

		Jerarquia j = new Jerarquia();
		this.clientes.add(j);

		ClienteReducido cr = new ClienteReducido();

		j.getCliente().setValue(cr);

		this.clientesRoot = new DefaultTreeNode("R", new ClienteReducido(), null);

		try {
			cr.getIdCliente().setValue(this.getUsuario().getIdCliente());
			cr.getRazonSocial().setValue(this.getUsuario().getRazonSocial());
			cr.getNombreFantasia().setValue(this.getUsuario().getNombreFantasia());
			cr.getTipoCliente().setValue(this.getUsuario().getTipoCliente());
			cr.getNivelDistribuidorSuperior().setValue(this.getUsuario().getNivelDistribuidorSuperior());
			cr.getIconoTerminal().setValue("");

			DefaultTreeNode c0 = new DefaultTreeNode(
					(cr.getTipoCliente().getValue() == null ? "R" : cr.getTipoCliente().getValue()), cr,
					this.clientesRoot);

			String ordenamientoAseleccionar = "numerico";

			if (ordenamiento == "numerico") {
				ordenamientoAseleccionar = "numerico";

			} else if (ordenamiento.equals("alfabetico") && descArbol.equals("razonSocial")) {
				ordenamientoAseleccionar = "alfabeticoRazSoc";

			} else if (ordenamiento.equals("alfabetico") && descArbol.equals("nombreFantasia")) {
				ordenamientoAseleccionar = "alfabeticoFantasia";
			} else {
				ordenamientoAseleccionar = "numerico";
			}

			// Busco los subclientes
			j.getListaSubClientes()
					.setValue(GestionServiceHelper.getGestionService(CfgTimeout.DEFAULT).traerJerarquia(
							this.getUsuario().getIdCliente(), this.getUsuario().getIdMayorista(), nivelesPrecargaArbol,
							this.getUsuario().getNivelDistribuidorSuperior(), ordenamientoAseleccionar));

			if (j.getListaSubClientes() != null && j.getListaSubClientes() != null
					&& j.getListaSubClientes().getValue() != null
					&& j.getListaSubClientes().getValue().getJerarquia() != null) {
				for (Jerarquia jer : j.getListaSubClientes().getValue().getJerarquia()) {
					if (jer.getCliente().getValue().getIconoTerminal().getValue() != null) {
						jer.getCliente().getValue().getIconoTerminal().setValue(jer.getCliente().getValue()
								.getIconoTerminal().getValue().toLowerCase().replace("images/", ""));
						jer.getCliente().getValue().getIconoTerminal().setValue(jer.getCliente().getValue()
								.getIconoTerminal().getValue().toLowerCase().replace(".jpg", ""));
					}
					DefaultTreeNode c1 = new DefaultTreeNode(
							(jer.getCliente().getValue().getIconoTerminal().getValue() == null ? "R"
									: jer.getCliente().getValue().getTipoCliente().getValue()),
							jer.getCliente().getValue(), c0);

					obtenerSubclientes(c1, jer.getListaSubClientes().getValue());

				}
			}

			// Por default queda seleccionado el cliente logueado
			this.clienteSeleccionado = GestionServiceHelper.getGestionService(CfgTimeout.DEFAULT)
					.mostrarCliente(this.getUsuario().getIdMayorista(), this.getUsuario().getIdCliente());
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error en loadJerarquia", null));
		}

	}

	private DefaultTreeNode obtenerSubclientes(DefaultTreeNode dtn, ArrayOfJerarquia lj) {
		if (lj != null) {
			// recorre las nuevas listas de jerarquias y agrega nodos
			for (Jerarquia jerarquia : lj.getJerarquia()) {
				jerarquia.getCliente().getValue().getIconoTerminal().setValue(jerarquia.getCliente().getValue()
						.getIconoTerminal().getValue().toLowerCase().replace("images/", ""));
				jerarquia.getCliente().getValue().getIconoTerminal().setValue(jerarquia.getCliente().getValue()
						.getIconoTerminal().getValue().toLowerCase().replace(".jpg", ""));
				DefaultTreeNode c1 = new DefaultTreeNode(
						(jerarquia.getCliente().getValue().getTipoCliente() == null
								|| jerarquia.getCliente().getValue().getTipoCliente().getValue() == null ? "R"
										: jerarquia.getCliente().getValue().getTipoCliente().getValue()),
						jerarquia.getCliente(), dtn);
				obtenerSubclientes(c1, jerarquia.getListaSubClientes().getValue());
			}

		} else {
			// sale del ciclo recursivo
		}
		return dtn;
	}

	public void actualizarArbol(ActionEvent ae) {

		try {
			loadJerarquia();

		} catch (Exception e) {

		}
		return;
	}

	public void buscarClientes(ActionEvent ae) {
		Long idCli = null;
		String razSoc = null;
		Long idTipTer = null;
		String tipCli = null;
		Long idUsu = null;
		String usu = null;
		String idAu = null;
		String idAu2 = null;
		String idProvincia = null;
		String idLocalidad = null;
		Long idEntidad = null;
		String CUIT = null;

		try {
			if ("idCliente".equals(filtro)) {
				idCli = Long.parseLong(valorFiltro);
			}
			if ("razonSocial".equals(filtro)) {
				razSoc = valorFiltro;
			}
			if ("idTipoTerminal".equals(filtro)) {
				idTipTer = Long.parseLong(valorFiltro);
			}
			if ("tipoCliente".equals(filtro)) {
				tipCli = valorFiltro;
			}
			if ("idUsuario".equals(filtro)) {
				idUsu = Long.parseLong(valorFiltro);
			}
			if ("usuario".equals(filtro)) {
				usu = valorFiltro;
			}
			if ("idAux".equals(filtro)) {
				idAu = valorFiltro;
			}
			if ("idAux2".equals(filtro)) {
				idAu2 = valorFiltro;
			}
			if ("idProvincia".equals(filtro)) {
				idProvincia = valorFiltro;
			}
			if ("idLocalidad".equals(filtro)) {
				idLocalidad = valorFiltro;
			}
			if ("idEntidad".equals(filtro)) {
				idEntidad = Long.parseLong(valorFiltro);
			}
			if ("CUIT".equals(filtro)) {
				CUIT = valorFiltro;
			}

		} catch (Exception e) {
			addGrowlErrorMessage("El dato ingresado debe ser numerico");
			return;
		}

		try {

			listCliRed = GestionServiceHelper.getGestionService(CfgTimeout.DEFAULT).buscarCliente(
					this.getUsuario().getIdMayorista(), this.getUsuario().getIdCliente(), null, idCli, razSoc, idTipTer,
					tipCli, idUsu, usu, idAu, idAu2, idProvincia, idLocalidad, idEntidad, CUIT);

			if (listCliRed == null || listCliRed.getClienteReducido() == null
					|| listCliRed.getClienteReducido().isEmpty()) {
				addGrowlErrorMessage("No se ha encontrado ningun registro con ese criterio.");
				listCliRed = null;
			}
			return;

		} catch (Exception e) {
			addGrowlErrorMessage("Error realizando la consulta de Clientes.");
			return;
		}
	}

	public void limpiarBusquedaClientes(ActionEvent ae) {

		filtro = null;
		valorFiltro = null;
		listCliRed = null;
	}

	public void cambiarSubListaAnalitica_ccprov() {

		listSubMovimientoCC.getSubMovimientoCuentaCorriente().get(this.idRowItemSM_ccprov).getSubListaAnalitica()
				.setValue(null);
		listSubMovimientoCC.getSubMovimientoCuentaCorriente().get(this.idRowItemSM_ccprov).getSubListaAnalitica()
				.setValue(new ArrayOfSelectItem());

		listSubMovimientoCC.getSubMovimientoCuentaCorriente().get(this.idRowItemSM_ccprov).getImporte().setValue(0F);

		if (listSubMovimientoCC.getSubMovimientoCuentaCorriente().get(this.idRowItemSM_ccprov).getTipoCC()
				.getValue() == 3) {
			// Cargo la lista de Bancos
			listSubMovimientoCC.getSubMovimientoCuentaCorriente().get(this.idRowItemSM_ccprov).getSubListaAnalitica()
					.setValue(bancos);

		} else if (listSubMovimientoCC.getSubMovimientoCuentaCorriente().get(this.idRowItemSM_ccprov).getTipoCC()
				.getValue() == 4) {
			// Cargo el Cliente
			listSubMovimientoCC.getSubMovimientoCuentaCorriente().get(this.idRowItemSM_ccprov).getId()
					.setValue(this.getUsuario().getIdCliente());

			com.americacg.cargavirtual.gestion.javax.faces.model.SelectItem si = new com.americacg.cargavirtual.gestion.javax.faces.model.SelectItem();
			si.getValue().setValue(this.getUsuario().getIdCliente());
			si.getDescription().setValue(this.getUsuario().getRazonSocial());
			listSubMovimientoCC.getSubMovimientoCuentaCorriente().get(this.idRowItemSM_ccprov).getSubListaAnalitica()
					.getValue().getSelectItem().add(si);
		}

		sumarImportesDeLaListaCCprov_analitica();

	}

	public void sumarImportesDeLaListaCCprov_analitica() {
		this.importeCCprov = 0F;
		for (SubMovimientoCuentaCorriente s : listSubMovimientoCC.getSubMovimientoCuentaCorriente()) {
			if ((s.getImporte() != null) && (s.getImporte().getValue() > 0)) {
				this.importeCCprov = this.importeCCprov + s.getImporte().getValue();
			}
		}
	}

	public void eliminarSubMovimiento_ccprov() {
		scrMensajeAlert = "";
		try {

			listSubMovimientoCC.getSubMovimientoCuentaCorriente().remove(this.idRowItemSM_ccprov);

			sumarImportesDeLaListaCCprov_analitica();

		} catch (Exception e) {
			scrMensajeAlert = "No se pudo eliminar";
		}
	}

	public void agregarSubMovimiento_ccprov() {
		scrMensajeAlert = "";
		try {
			SubMovimientoCuentaCorriente smcc = new SubMovimientoCuentaCorriente();
			smcc.getTipoCC().setValue(0L);
			smcc.getId().setValue(0L);
			smcc.getImporte().setValue(0F);
			smcc.getSubListaAnalitica().setValue(new ArrayOfSelectItem());
			listSubMovimientoCC.getSubMovimientoCuentaCorriente().add(smcc);

			sumarImportesDeLaListaCCprov_analitica();

		} catch (Exception e) {
			scrMensajeAlert = "No se pudo agregar";
		}
	}

	public List<Jerarquia> getClientes() {
		if (this.clientes == null) {
			loadJerarquia();
		}
		return clientes;
	}

	public void setClientes(List<Jerarquia> clientes) {
		this.clientes = clientes;
	}

	public Integer getNivelesPrecargaArbol() {
		return nivelesPrecargaArbol;
	}

	public void setNivelesPrecargaArbol(Integer nivelesPrecargaArbol) {
		this.nivelesPrecargaArbol = nivelesPrecargaArbol;
	}

	public Cliente getClienteSeleccionado() {
		return clienteSeleccionado;
	}

	public void setClienteSeleccionado(Cliente clienteSeleccionado) {
		this.clienteSeleccionado = clienteSeleccionado;
	}

	public Saldos getSaldosClienteSeleccionado() {
		return saldosClienteSeleccionado;
	}

	public void setSaldosClienteSeleccionado(Saldos saldosClienteSeleccionado) {
		this.saldosClienteSeleccionado = saldosClienteSeleccionado;
	}

	public DatosReparto getDatosReparto() {
		return datosReparto;
	}

	public void setDatosReparto(DatosReparto datosReparto) {
		this.datosReparto = datosReparto;
	}

	public boolean[] getPanels() {
		return panels;
	}

	public void setPanels(boolean[] panels) {
		this.panels = panels;
	}

	public Float getSaldo() {
		return saldo;
	}

	public void setSaldo(Float saldo) {
		this.saldo = saldo;
	}

	public ArrayOfProductoComision getListaProdCom() {
		return listaProdCom;
	}

	public void setListaProdCom(ArrayOfProductoComision listaProdCom) {
		this.listaProdCom = listaProdCom;
	}

	public List<IncrementoReparto> getListIncReparto() {
		return listIncReparto;
	}

	public void setListIncReparto(List<IncrementoReparto> listIncReparto) {
		this.listIncReparto = listIncReparto;
	}

	public Long getTipoMoviemiento() {
		return tipoMoviemiento;
	}

	public void setTipoMoviemiento(Long tipoMoviemiento) {
		this.tipoMoviemiento = tipoMoviemiento;
	}

	public String getComentarios() {
		return comentarios;
	}

	public void setComentarios(String comentarios) {
		this.comentarios = comentarios;
	}

	public void refreshJerarquia() {
		loadJerarquia();
	}

	public List<Boolean> getListIncOpcion() {
		return listIncOpcion;
	}

	public void setListIncOpcion(List<Boolean> listIncOpcion) {
		this.listIncOpcion = listIncOpcion;
	}

	public MonitorSaldos getMonitorSaldos() {
		return monitorSaldos;
	}

	public void setMonitorSaldos(MonitorSaldos monitorSaldos) {
		this.monitorSaldos = monitorSaldos;
	}

	public String getFiltro() {
		return filtro;
	}

	public void setFiltro(String filtro) {
		this.filtro = filtro;
	}

	public String getValorFiltro() {
		return valorFiltro;
	}

	public void setValorFiltro(String valorFiltro) {
		this.valorFiltro = valorFiltro;
	}

	public ArrayOfClienteReducido getListCliRed() {
		return listCliRed;
	}

	public void setListCliRed(ArrayOfClienteReducido listCliRed) {
		this.listCliRed = listCliRed;
	}

	public DatosDepAdelContainer getDatosDepAdelC() {
		return datosDepAdelC;
	}

	public void setDatosDepAdelC(DatosDepAdelContainer datosDepAdelC) {
		this.datosDepAdelC = datosDepAdelC;
	}

	public Integer getTipoFiltroCliente() {
		return tipoFiltroCliente;
	}

	public void setTipoFiltroCliente(Integer tipoFiltroCliente) {
		this.tipoFiltroCliente = tipoFiltroCliente;
	}

	public Long getIdCliente() {
		return idCliente;
	}

	public void setIdCliente(Long idCliente) {
		this.idCliente = idCliente;
	}

	public DatosAcreditacionComisionesContainer getDatosAcreditacionComisionesContainer() {
		return datosAcreditacionComisionesContainer;
	}

	public void setDatosAcreditacionComisionesContainer(
			DatosAcreditacionComisionesContainer datosAcreditacionComisionesContainer) {
		this.datosAcreditacionComisionesContainer = datosAcreditacionComisionesContainer;
	}

	public void setIgnorarFecha(Boolean ignorarFecha) {
		this.ignorarFecha = ignorarFecha;
	}

	public Boolean getIgnorarFecha() {
		return ignorarFecha;
	}

	public Integer getAcredCcomMarcarTodo() {
		return acredCcomMarcarTodo;
	}

	public void setAcredCcomMarcarTodo(Integer acredCcomMarcarTodo) {
		this.acredCcomMarcarTodo = acredCcomMarcarTodo;
	}

	public Integer getDepAdelMarcarTodo() {
		return depAdelMarcarTodo;
	}

	public void setDepAdelMarcarTodo(Integer depAdelMarcarTodo) {
		this.depAdelMarcarTodo = depAdelMarcarTodo;
	}

	public Integer getTipoFiltroClienteDepAdel() {
		return tipoFiltroClienteDepAdel;
	}

	public void setTipoFiltroClienteDepAdel(Integer tipoFiltroClienteDepAdel) {
		this.tipoFiltroClienteDepAdel = tipoFiltroClienteDepAdel;
	}

	public Long getIdClienteDepAdel() {
		return idClienteDepAdel;
	}

	public void setIdClienteDepAdel(Long idClienteDepAdel) {
		this.idClienteDepAdel = idClienteDepAdel;
	}

	public Boolean getIgnorarFechaDepAdel() {
		return ignorarFechaDepAdel;
	}

	public void setIgnorarFechaDepAdel(Boolean ignorarFechaDepAdel) {
		this.ignorarFechaDepAdel = ignorarFechaDepAdel;
	}

	public String getOrdenamiento() {
		return ordenamiento;
	}

	public void setOrdenamiento(String ordenamiento) {
		this.ordenamiento = ordenamiento;
	}

	public Long getTotalTrxComisiones() {
		return totalTrxComisiones;
	}

	public void setTotalTrxComisiones(Long totalTrxComisiones) {
		this.totalTrxComisiones = totalTrxComisiones;
	}

	public Float getTotalImporteComisiones() {
		return totalImporteComisiones;
	}

	public void setTotalImporteComisiones(Float totalImporteComisiones) {
		this.totalImporteComisiones = totalImporteComisiones;
	}

	public Float getTotalComisionesSIVA() {
		return totalComisionesSIVA;
	}

	public void setTotalComisionesSIVA(Float totalComisionesSIVA) {
		this.totalComisionesSIVA = totalComisionesSIVA;
	}

	public Float getTotalComisionesCIVA() {
		return totalComisionesCIVA;
	}

	public void setTotalComisionesCIVA(Float totalComisionesCIVA) {
		this.totalComisionesCIVA = totalComisionesCIVA;
	}

	// ---

	public Long getTipoMovimientoISM() {
		return tipoMovimientoISM;
	}

	public void setTipoMovimientoISM(Long tipoMovimientoISM) {
		this.tipoMovimientoISM = tipoMovimientoISM;
	}

	public List<SelectItem> getProductosISM() {
		this.productosISM = new ArrayList<SelectItem>();

		try {
			ArrayOfCabeceraProducto cp = GestionServiceHelper.getGestionService(CfgTimeout.DEFAULT)
					.buscarProductosPorProveedor(this.getUsuario().getIdMayorista(), idProveedorISM, "", true);

			if (cp != null && cp.getCabeceraProducto() != null) {
				for (CabeceraProducto prod : cp.getCabeceraProducto()) {
					this.productosISM
							.add(new SelectItem(prod.getIdProducto().getValue(), prod.getDescProducto().getValue()));
				}
			}
		} catch (Exception e) {
			this.productosISM = new ArrayList<SelectItem>();
		}

		return productosISM;
	}

	public void setProductosISM(List<SelectItem> productosISM) {
		this.productosISM = productosISM;
	}

	public List<SelectItem> getProveedores() {

		try {
			if (this.proveedores == null) {
				this.proveedores = new ArrayList<SelectItem>();
				ArrayOfSaldoProveedor lp = GestionServiceHelper.getGestionService(CfgTimeout.DEFAULT)
						.obtenerSaldoProveedores(this.getUsuario().getIdMayorista(), null, true, false);
				for (SaldoProveedor sp : lp.getSaldoProveedor()) {
					this.proveedores
							.add(new SelectItem(sp.getIdProveedor().getValue(), sp.getRazonSocial().getValue()));
				}
			}

		} catch (Exception e) {
			this.proveedores = null;
		}

		return proveedores;
	}

	public void setProveedores(List<SelectItem> proveedores) {
		this.proveedores = proveedores;
	}

	public Long getIdProductoISM() {
		return idProductoISM;
	}

	public void setIdProductoISM(Long idProductoISM) {
		this.idProductoISM = idProductoISM;
	}

	public Long getIdProveedorISM() {
		return idProveedorISM;
	}

	public void setIdProveedorISM(Long idProveedorISM) {
		this.idProveedorISM = idProveedorISM;
	}

	public Float getTotalDepAdel() {
		return totalDepAdel;
	}

	public void setTotalDepAdel(Float totalDepAdel) {
		this.totalDepAdel = totalDepAdel;
	}

	public Date getFechaDepAdelDesde() {
		return fechaDepAdelDesde;
	}

	public void setFechaDepAdelDesde(Date fechaDepAdelDesde) {
		this.fechaDepAdelDesde = fechaDepAdelDesde;
	}

	public Date getFechaDepAdelHasta() {
		return fechaDepAdelHasta;
	}

	public void setFechaDepAdelHasta(Date fechaDepAdelHasta) {
		this.fechaDepAdelHasta = fechaDepAdelHasta;
	}

	public Date getFechaDesdeAcredCom() {
		return fechaDesdeAcredCom;
	}

	public void setFechaDesdeAcredCom(Date fechaDesdeAcredCom) {
		this.fechaDesdeAcredCom = fechaDesdeAcredCom;
	}

	public Date getFechaHastaAcredCom() {
		return fechaHastaAcredCom;
	}

	public void setFechaHastaAcredCom(Date fechaHastaAcredCom) {
		this.fechaHastaAcredCom = fechaHastaAcredCom;
	}

	public ComisionPlanaAdelantada getComisPlanaAdel() {
		return comisPlanaAdel;
	}

	public void setComisPlanaAdel(ComisionPlanaAdelantada comisPlanaAdel) {
		this.comisPlanaAdel = comisPlanaAdel;
	}

	public ClienteEstComisPlanaAdel getCliEstComPlanaAdelant() {
		return cliEstComPlanaAdelant;
	}

	public void setCliEstComPlanaAdelant(ClienteEstComisPlanaAdel cliEstComPlanaAdelant) {
		this.cliEstComPlanaAdelant = cliEstComPlanaAdelant;
	}

	public Long getIdEstadodepyadelanto() {
		return idEstadodepyadelanto;
	}

	public void setIdEstadodepyadelanto(Long idEstadodepyadelanto) {
		this.idEstadodepyadelanto = idEstadodepyadelanto;
	}

	public Boolean getMostrarDetalleAcreditComis() {
		return mostrarDetalleAcreditComis;
	}

	public void setMostrarDetalleAcreditComis(Boolean mostrarDetalleAcreditComis) {
		this.mostrarDetalleAcreditComis = mostrarDetalleAcreditComis;
	}

	public Float getTotalComisionesSIVAaPagar() {
		return totalComisionesSIVAaPagar;
	}

	public void setTotalComisionesSIVAaPagar(Float totalComisionesSIVAaPagar) {
		this.totalComisionesSIVAaPagar = totalComisionesSIVAaPagar;
	}

	public Float getTotalComisionesCIVAaPagar() {
		return totalComisionesCIVAaPagar;
	}

	public void setTotalComisionesCIVAaPagar(Float totalComisionesCIVAaPagar) {
		this.totalComisionesCIVAaPagar = totalComisionesCIVAaPagar;
	}

	public Long getIdConceptoCC() {
		return idConceptoCC;
	}

	public void setIdConceptoCC(Long idConceptoCC) {
		this.idConceptoCC = idConceptoCC;
	}

	public Float getImporteCC() {
		return importeCC;
	}

	public void setImporteCC(Float importeCC) {
		this.importeCC = importeCC;
	}

	public String getMotivoCC() {
		return motivoCC;
	}

	public void setMotivoCC(String motivoCC) {
		this.motivoCC = motivoCC;
	}

	public Float getEstadoCC_cliente() {

		try {
			if (this.autoCompleteCliente.getDescripcion().length() > 0) {
				this.estadoCC_cliente = GestionServiceHelper.getGestionService(CfgTimeout.DEFAULT).ccEstado(1L,
						this.getUsuario().getIdMayorista(), this.autoCompleteCliente.getId());
			} else {
				this.estadoCC_cliente = 0F;
			}
		} catch (Exception e) {
			this.estadoCC_cliente = 0F;
		}

		return estadoCC_cliente;
	}

	public void setEstadoCC_cliente(Float estadoCC_cliente) {
		this.estadoCC_cliente = estadoCC_cliente;
	}

	public Float getWu_saldo() {
		try {
			if ((this.autoCompleteCliente.getDescripcion().length() > 0)
					&& (this.autoCompleteCliente.getId().compareTo(this.getUsuario().getIdCliente()) != 0)) {

				FloatHolder fh = GestionServiceHelper.getGestionService(CfgTimeout.DEFAULT)
						.wuObtenerSaldo(this.getUsuario().getIdMayorista(), this.autoCompleteCliente.getId());
				this.wu_saldo = fh.getValor().getValue();

				this.wu_importeReparto = null;
				this.id_tipoReparto_wu = null;

			} else {
				this.wu_saldo = 0F;
			}

		} catch (Exception e) {
			this.wu_saldo = 0F;
		}
		return wu_saldo;
	}

	public void setWu_saldo(Float wu_saldo) {
		this.wu_saldo = wu_saldo;
	}

	public String getWu_RZdist() {
		try {
			if ((this.autoCompleteCliente.getDescripcion().length() > 0)
					&& (this.autoCompleteCliente.getId().compareTo(this.getUsuario().getIdCliente()) != 0)) {

				Cliente c = GestionServiceHelper.getGestionService(CfgTimeout.DEFAULT)
						.mostrarCliente(this.getUsuario().getIdMayorista(), this.autoCompleteCliente.getId());
				this.wu_RZdist = "(" + c.getIdDistribuidorSuperior() + ") " + c.getRazonSocialDistribuidorSuperior();
				this.wu_idDist = c.getIdDistribuidorSuperior().getValue();
			} else {
				this.wu_RZdist = "";
			}

		} catch (Exception e) {
			this.wu_RZdist = "";
		}
		return wu_RZdist;
	}

	public void setWu_RZdist(String wu_RZdist) {
		this.wu_RZdist = wu_RZdist;
	}

	public Float getWu_saldoDist() {
		try {
			if ((this.autoCompleteCliente.getDescripcion().length() > 0)
					&& (this.autoCompleteCliente.getId().compareTo(this.getUsuario().getIdCliente()) != 0)) {
				FloatHolder fhd = GestionServiceHelper.getGestionService(CfgTimeout.DEFAULT)
						.wuObtenerSaldo(this.getUsuario().getIdMayorista(), this.getWu_idDist());
				this.wu_saldoDist = fhd.getValor().getValue();

			} else {
				this.wu_saldoDist = 0F;
			}

		} catch (Exception e) {
			this.wu_saldoDist = 0F;
		}
		return wu_saldoDist;
	}

	public void setWu_saldoDist(Float wu_saldoDist) {
		this.wu_saldoDist = wu_saldoDist;
	}

	public Float getWu_importeReparto() {
		return wu_importeReparto;
	}

	public void setWu_importeReparto(Float wu_importeReparto) {
		this.wu_importeReparto = wu_importeReparto;
	}

	public Long getId_tipoReparto_wu() {
		return id_tipoReparto_wu;
	}

	public void setId_tipoReparto_wu(Long id_tipoReparto_wu) {
		this.id_tipoReparto_wu = id_tipoReparto_wu;
	}

	public List<SelectItem> getWu_tiporepartos() {

		try {
			if (this.wu_tiporepartos == null) {
				this.wu_tiporepartos = new ArrayList<SelectItem>();

				WUTiporepartoContainer c = GestionServiceHelper.getGestionService(CfgTimeout.DEFAULT)
						.mostrarWUTiporeparto(this.getUsuario().getIdMayorista(), null);
				if (c != null) {
					if (!c.getListWUtipoReparto().getValue().getWUTiporeparto().isEmpty()) {
						for (WUTiporeparto t : c.getListWUtipoReparto().getValue().getWUTiporeparto()) {
							this.wu_tiporepartos.add(
									new SelectItem(t.getIdTipoRepartoWu().getValue(), t.getDescripcion().getValue()));
						}
					}
				}
			}

		} catch (Exception e) {
			this.wu_tiporepartos = null;
		}

		return wu_tiporepartos;
	}

	public void setWu_tiporepartos(List<SelectItem> wu_tiporepartos) {
		this.wu_tiporepartos = wu_tiporepartos;
	}

	public Long getWu_idDist() {
		return wu_idDist;
	}

	public void setWu_idDist(Long wu_idDist) {
		this.wu_idDist = wu_idDist;
	}

	public Float getEcc() {
		// CONSULTA DE CC de Cliente
		try {
			ecc = GestionServiceHelper.getGestionService(CfgTimeout.DEFAULT).ccEstado(1L,
					this.getUsuario().getIdMayorista(), this.getUsuario().getIdCliente());
		} catch (Exception e) {
			ecc = 0F;
		}
		return ecc;
	}

	public void setEcc(Float ecc) {
		this.ecc = ecc;
	}

	public Long getIdVentanaTrabajoCC() {
		return idVentanaTrabajoCC;
	}

	public void setIdVentanaTrabajoCC(Long idVentanaTrabajoCC) {
		this.idVentanaTrabajoCC = idVentanaTrabajoCC;
	}

	public Long getIdConceptoCCprov() {
		return idConceptoCCprov;
	}

	public void setIdConceptoCCprov(Long idConceptoCCprov) {
		this.idConceptoCCprov = idConceptoCCprov;
	}

	public Long getIdProveedorCC() {
		return idProveedorCC;
	}

	public void setIdProveedorCC(Long idProveedorCC) {
		this.idProveedorCC = idProveedorCC;
	}

	public Float getImporteCCprov() {
		return importeCCprov;
	}

	public void setImporteCCprov(Float importeCCprov) {
		this.importeCCprov = importeCCprov;
	}

	public String getMotivoCCprov() {
		return motivoCCprov;
	}

	public void setMotivoCCprov(String motivoCCprov) {
		this.motivoCCprov = motivoCCprov;
	}

	public Float getEstadoCC_proveedor() {
		return estadoCC_proveedor;
	}

	public void setEstadoCC_proveedor(Float estadoCC_proveedor) {
		this.estadoCC_proveedor = estadoCC_proveedor;
	}

	public void actualizarEstadoCC_proveedor(ActionEvent evt) {
		// CONSULTA DE CC de Proveedor
		try {
			this.estadoCC_proveedor = GestionServiceHelper.getGestionService(CfgTimeout.DEFAULT).ccEstado(2L,
					this.getUsuario().getIdMayorista(), this.idProveedorCC);
			if (this.estadoCC_proveedor == null) {
				this.estadoCC_proveedor = 0F;
			}
		} catch (Exception e) {
			this.estadoCC_proveedor = 0F;
		}

		// CONSULTA DE BOLSA DE SALDO DEL PROVEEDOR
		try {
			this.bolsaSaldoProvEnEstadoCC = GestionServiceHelper.getGestionService(CfgTimeout.DEFAULT)
					.obtenerSaldoProv(this.getUsuario().getIdMayorista(), this.idProveedorCC);
			if (this.bolsaSaldoProvEnEstadoCC == null) {
				this.bolsaSaldoProvEnEstadoCC = 0F;
			}
		} catch (Exception e) {
			this.bolsaSaldoProvEnEstadoCC = 0F;
		}
	}

	public void actualizoNroFacturaCC_proveedor(ActionEvent evt) {

		// No hacer nada

	}

	public void actualizarConceptoCCprovCC_proveedor(ActionEvent evt) {
		// CONSULTA DE CC de Proveedor
		try {
			this.idProveedorCC = null;
			this.estadoCC_proveedor = null;
			this.bolsaSaldoProvEnEstadoCC = null;
			this.accionBolsaSaldoProv = 0;
			this.accionIncrementoSaldoMayorista = 0;
			this.listSubMovimientoCC = null;
			this.importeCCprov = null;
			this.motivoCCprov = null;
			this.valPendPagoFacturaCC = null;

		} catch (Exception e) {
			// No hacer nada
		}
	}

	public void actualizarConceptoCCbanco(ActionEvent evt) {
		// CONSULTA DE CC de Banco
		try {
			this.importeCCbanco = null;
			this.motivoCCbanco = null;

			this.idProveedorCC = null;
			this.nroFacturaCC = null;

			this.valPendPagoFacturaCC = null;

			this.id_ccbanco_coment = null;

		} catch (Exception e) {
			// No hacer nada
		}
	}

	public void actualizarConceptoCCcaja(ActionEvent evt) {
		// CONSULTA DE CC de Caja
		try {
			this.importeCCcaja = null;
			this.motivoCCcaja = null;

			this.idProveedorCC = null;
			this.nroFacturaCC = null;

			this.valPendPagoFacturaCC = null;

		} catch (Exception e) {
			// No hacer nada
		}
	}

	public void actualizarEstadoCC_banco(ActionEvent evt) {
		// CONSULTA DE CC de Banco
		try {
			this.estadoCC_banco = GestionServiceHelper.getGestionService(CfgTimeout.DEFAULT).ccEstado(3L,
					this.getUsuario().getIdMayorista(), this.idBancoCC);
		} catch (Exception e) {
			this.estadoCC_banco = 0F;
		}
	}

	public void actualizarEstadoCC_caja(ActionEvent evt) {
		actualizarEstadoCC_caja();
	}

	private void actualizarEstadoCC_caja() {
		// CONSULTA DE CC de Caja (Muestro la caja del cliente Logueado)
		try {
			this.estadoCC_caja = GestionServiceHelper.getGestionService(CfgTimeout.DEFAULT).ccEstado(4L,
					this.getUsuario().getIdMayorista(), this.getUsuario().getIdCliente());
		} catch (Exception e) {
			this.estadoCC_caja = 0F;
		}
	}

	// ACREDITACION COMPROBANTES EXTERNOS
	public void expandirContraerPanel11(ActionEvent evt) {

		try {
			if (realizarCargaDatosPanel(11)) {

				limpiarFiltrosDeMovimientosExternos();

			}

		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
					"Error al tratar de leer datos de Comprobantes Externos: |" + e.getMessage() + "|", null));
		}

	}

	// CREDITO PROVINCIA NET
	public void expandirContraerPanel12(ActionEvent evt) {

		try {
			if (realizarCargaDatosPanel(12)) {

			}

		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
					"Error al tratar de leer datos de CREDITO PROVINCIA NET: |" + e.getMessage() + "|", null));
		}

	}

	private void limpiarFiltrosDeMovimientosExternos() {
		// Limpio los datos de la solapa de Leer Comprobantes Externos
		movExtFechaDesde = new Date();
		movExtfechaHasta = new Date();
		movExtIdEstado = 1;
		movExtTipoComp = null;
		movExtnumeroComp = null;
		movExtIgnorarFecha = false;
		movExtIdClienteExt = null;
		movExtImporte = null;
		movExtIdCliente = null;
		lce = null;
	}

	public void leerComprobantesExternos() {

		try {

			InformeComprobantesExternos ice = null;
			lce = null;

			GregorianCalendar gcFechaHoraDesde = new GregorianCalendar();
			gcFechaHoraDesde.setTime(movExtFechaDesde);
			XMLGregorianCalendar xmlGCFechaHoraDesde = DatatypeFactory.newInstance()
					.newXMLGregorianCalendar(gcFechaHoraDesde);

			GregorianCalendar gcFechaHoraHasta = new GregorianCalendar();
			gcFechaHoraHasta.setTime(movExtfechaHasta);
			XMLGregorianCalendar xmlGCFechaHoraHasta = DatatypeFactory.newInstance()
					.newXMLGregorianCalendar(gcFechaHoraHasta);

			ice = GestionServiceHelper.getGestionService(CfgTimeout.DEFAULT).informeComprobantesExternos(
					this.getUsuario().getIdMayorista(), null, xmlGCFechaHoraDesde, xmlGCFechaHoraHasta, movExtTipoComp,
					movExtnumeroComp, movExtIdClienteExt, movExtImporte, 1, movExtIdCliente, movExtIgnorarFecha, null);

			if (ice == null) {
				FacesContext.getCurrentInstance().addMessage(null,
						new FacesMessage(FacesMessage.SEVERITY_ERROR, "InformeComprobantesExternos es null", null));

			} else if (ice.getLce() == null) {
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
						"La lista devuelta para leerComprobantesExternos es null", null));
			} else if (ice.getLce().getValue() == null) {
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
						"La lista devuelta para leerComprobantesExternos es null", null));
			} else if (ice.getLce().getValue().getComprobanteExterno() == null) {
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
						"La lista devuelta para leerComprobantesExternos es null", null));
			} else if (ice.getLce().getValue().getComprobanteExterno().isEmpty()) {
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
						"No se encontraron comprobantes externos que coincidan con la condicion de filtrado", null));

			} else {

				lce = ice.getLce().getValue().getComprobanteExterno();

			}

		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
					"Error ejecutando el WS de lectura de Comprobantes Externos: |" + e.getMessage() + "|", null));

		}
		return;
	}

	public void aceptarMovExt(ActionEvent ev) {

		try {

			// Creo una lista final solo con los movimientos que tengan "accion 1 y pin" o
			// "accion 2"
			ArrayOfComprobanteExterno lcef = new ArrayOfComprobanteExterno();

			for (Iterator iterator = lce.iterator(); iterator.hasNext();) {
				ComprobanteExterno ce = (ComprobanteExterno) iterator.next();

				if (ce != null && ((ce.getAccion().getValue() == 1 && ce.getPinComprobacionInput().getValue() != null
						& ce.getPinComprobacionInput().getValue().length() > 0) || ce.getAccion().getValue() == 2)) {
					lcef.getComprobanteExterno().add(ce);
				}

			}

			InformeComprobantesExternos iresp = GestionServiceHelper.getGestionService(CfgTimeout.DEFAULT)
					.aceptarMovExt(this.getUsuario().getIdMayorista(), this.getUsuario().getIdUsuario(),
							this.getUsuario().getPassword(), lcef);

			if (iresp == null) {
				FacesContext.getCurrentInstance().addMessage(null,
						new FacesMessage(FacesMessage.SEVERITY_ERROR, "La respuesta del metodo es null", null));

			} else if (iresp.getError().getValue().getHayError().getValue()) {
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
						iresp.getError().getValue().getMsgError().getValue(), null));

			} else if (iresp.getLce() == null || iresp.getLce().getValue() == null
					|| iresp.getLce().getValue().getComprobanteExterno() != null
					|| iresp.getLce().getValue().getComprobanteExterno().size() < 1) {
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
						"No se proceso ningun movimiento. Debe seleccionar una accion, en caso de una aprobacion tambien se debe cargar el PIN",
						null));

			} else {
				for (ComprobanteExterno cer : iresp.getLce().getValue().getComprobanteExterno()) {

					if (cer.getError().getValue().getHayError().getValue()) {
						FacesContext.getCurrentInstance().addMessage(null,
								new FacesMessage(FacesMessage.SEVERITY_ERROR,
										"idClienteExt: " + cer.getIdClienteExterno().getValue() + ", "
												+ cer.getTipoComprobante().getValue() + "-"
												+ cer.getNumeroComprobante().getValue() + ", importe: "
												+ cer.getImporte().getValue() + ". "
												+ cer.getError().getValue().getMsgError().getValue(),
										null));
					} else {
						FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,
								"idClienteExt: " + cer.getIdClienteExterno().getValue() + ", "
										+ cer.getTipoComprobante().getValue() + "-"
										+ cer.getNumeroComprobante().getValue() + ", importe: "
										+ cer.getImporte().getValue() + ". " + "Movimiento procesado OK",
								null));
					}

				}

			}

			// Limpio la lista luego de hacer las acreditaciones
			this.lce = null;
			leerComprobantesExternos();

		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
					"Error ejecutando el WS de Acreditacion de Movimientos Externos: |" + e.getMessage() + "|", null));
		}

	}

	public void cancelarMovExt(ActionEvent ev) {

		try {
			this.lce = null;
			leerComprobantesExternos();
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_INFO, "Cancelacion OK", null));

		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
					"Error tratando de leer nuevamente los Movimientos Externos: |" + e.getMessage() + "|", null));
		}

	}

	public void tabPanelClivalueChangeListener(ValueChangeEvent ev) {
		// No se esta usando. Ejecuta el metodo al cambiar de Tab
	}

	public void tabPanelvalueChangeListener(ValueChangeEvent ev) {
		// No se esta usando. Ejecuta el metodo al cambiar de Tab
	}

	public Long getIdConceptoCCbanco() {
		return idConceptoCCbanco;
	}

	public void setIdConceptoCCbanco(Long idConceptoCCbanco) {
		this.idConceptoCCbanco = idConceptoCCbanco;
	}

	public Long getIdConceptoCCcaja() {
		return idConceptoCCcaja;
	}

	public void setIdConceptoCCcaja(Long idConceptoCCcaja) {
		this.idConceptoCCcaja = idConceptoCCcaja;
	}

	public Long getIdBancoCC() {
		return idBancoCC;
	}

	public void setIdBancoCC(Long idBancoCC) {
		this.idBancoCC = idBancoCC;
	}

	public ArrayOfSelectItem getBancos() {

		try {

			if (this.bancos == null) {
				this.bancos = new ArrayOfSelectItem();
				BancoContainer bc = GestionServiceHelper.getGestionService(CfgTimeout.DEFAULT)
						.mostrarBanco(this.getUsuario().getIdMayorista(), null, "A", false);
				if ((bc != null) && (bc.getListBanco() != null) && (bc.getListBanco().getValue() != null)
						&& (bc.getListBanco().getValue().getBanco() != null)
						&& (bc.getListBanco().getValue().getBanco().size() > 0)) {
					for (Banco b : bc.getListBanco().getValue().getBanco()) {
						com.americacg.cargavirtual.gestion.javax.faces.model.SelectItem si = new com.americacg.cargavirtual.gestion.javax.faces.model.SelectItem();
						si.getValue().setValue(b.getIdBanco().getValue());
						si.getDescription()
								.setValue(b.getNombreBanco().getValue() + "|" + b.getSucursal().getValue() + "|"
										+ b.getCuenta().getValue() + "|" + b.getCBU().getValue() + "|"
										+ b.getDescripcion().getValue() + "|");
						this.bancos.getSelectItem().add(si);
					}
				}
			}

		} catch (Exception e) {
			this.bancos = new ArrayOfSelectItem();
		}

		return bancos;
	}

	public void setBancos(ArrayOfSelectItem bancos) {
		this.bancos = bancos;
	}

	public Float getEstadoCC_banco() {
		return estadoCC_banco;
	}

	public void setEstadoCC_banco(Float estadoCC_banco) {
		this.estadoCC_banco = estadoCC_banco;
	}

	public Float getEstadoCC_caja() {

		// Cargo el EstadoCC de la caja del cliente Logueado
		actualizarEstadoCC_caja();

		return estadoCC_caja;
	}

	public void setEstadoCC_caja(Float estadoCC_caja) {
		this.estadoCC_caja = estadoCC_caja;
	}

	public Float getImporteCCbanco() {
		return importeCCbanco;
	}

	public void setImporteCCbanco(Float importeCCbanco) {
		this.importeCCbanco = importeCCbanco;
	}

	public String getMotivoCCbanco() {
		return motivoCCbanco;
	}

	public void setMotivoCCbanco(String motivoCCbanco) {
		this.motivoCCbanco = motivoCCbanco;
	}

	public Float getImporteCCcaja() {
		return importeCCcaja;
	}

	public void setImporteCCcaja(Float importeCCcaja) {
		this.importeCCcaja = importeCCcaja;
	}

	public String getMotivoCCcaja() {
		return motivoCCcaja;
	}

	public void setMotivoCCcaja(String motivoCCcaja) {
		this.motivoCCcaja = motivoCCcaja;
	}

	public List<SelectItem> getTipoCC() {
		this.tipoCC = new ArrayList<SelectItem>();

		if (("M".equals(this.getUsuario().getTipoCliente())) || ("D".equals(this.getUsuario().getTipoCliente()))) {
			this.tipoCC.add(new SelectItem(1L, "CC Clientes"));
		}

		if ("M".equals(this.getUsuario().getTipoCliente())) {
			this.tipoCC.add(new SelectItem(2L, "CC Proveedores"));
			this.tipoCC.add(new SelectItem(3L, "CC Bancos"));
		}

		if (("M".equals(this.getUsuario().getTipoCliente())) || ("D".equals(this.getUsuario().getTipoCliente()))) {
			this.tipoCC.add(new SelectItem(4L, "Caja"));
		}

		return tipoCC;
	}

	public void setTipoCC(List<SelectItem> tipoCC) {
		this.tipoCC = tipoCC;
	}

	public Float getBolsaSaldoClienteEnEstadoCC() {
		try {
			if (this.autoCompleteCliente.getDescripcion().length() > 0) {
				this.bolsaSaldoClienteEnEstadoCC = GestionServiceHelper.getGestionService(CfgTimeout.DEFAULT)
						.obtenerSaldoCliente(this.getUsuario().getIdMayorista(), this.autoCompleteCliente.getId());
			} else {
				this.bolsaSaldoClienteEnEstadoCC = 0F;
			}
		} catch (Exception e) {
			this.bolsaSaldoClienteEnEstadoCC = 0F;
		}

		return bolsaSaldoClienteEnEstadoCC;
	}

	public void setBolsaSaldoClienteEnEstadoCC(Float bolsaSaldoClienteEnEstadoCC) {
		this.bolsaSaldoClienteEnEstadoCC = bolsaSaldoClienteEnEstadoCC;
	}

	public Float getBolsaSaldoProvEnEstadoCC() {
		return bolsaSaldoProvEnEstadoCC;
	}

	public void setBolsaSaldoProvEnEstadoCC(Float bolsaSaldoProvEnEstadoCC) {
		this.bolsaSaldoProvEnEstadoCC = bolsaSaldoProvEnEstadoCC;
	}

	public Float getEstadoCC_clienteSeleccionado() {
		return estadoCC_clienteSeleccionado;
	}

	public void setEstadoCC_clienteSeleccionado(Float estadoCC_clienteSeleccionado) {
		this.estadoCC_clienteSeleccionado = estadoCC_clienteSeleccionado;
	}

	public Float getCc_cliente() {
		try {
			if ((this.autoCompleteCliente.getDescripcion().length() > 0)
					&& (this.autoCompleteCliente.getId().compareTo(this.getUsuario().getIdCliente()) != 0)) {
				this.cc_cliente = GestionServiceHelper.getGestionService(CfgTimeout.DEFAULT).ccEstado(1L,
						this.getUsuario().getIdMayorista(), this.autoCompleteCliente.getId());
			} else {
				this.cc_cliente = 0F;
			}
		} catch (Exception e) {
			this.cc_cliente = 0F;
		}
		return cc_cliente;
	}

	public void setCc_cliente(Float cc_cliente) {
		this.cc_cliente = cc_cliente;
	}

	public Integer getAccionBolsaSaldoProv() {
		return accionBolsaSaldoProv;
	}

	public void setAccionBolsaSaldoProv(Integer accionBolsaSaldoProv) {
		this.accionBolsaSaldoProv = accionBolsaSaldoProv;
	}

	public ArrayOfSubMovimientoCuentaCorriente getListSubMovimientoCC() {
		if (listSubMovimientoCC == null || listSubMovimientoCC.getSubMovimientoCuentaCorriente().isEmpty()) {
			listSubMovimientoCC = new ArrayOfSubMovimientoCuentaCorriente();
			SubMovimientoCuentaCorriente smcc = new SubMovimientoCuentaCorriente();
			smcc.getTipoCC().setValue(0L);
			smcc.getId().setValue(0L);
			smcc.getImporte().setValue(0F);
			smcc.getSubListaAnalitica().setValue(new ArrayOfSelectItem());
			listSubMovimientoCC.getSubMovimientoCuentaCorriente().add(smcc);
		}
		return listSubMovimientoCC;
	}

	public void setListSubMovimientoCC(ArrayOfSubMovimientoCuentaCorriente listSubMovimientoCC) {
		this.listSubMovimientoCC = listSubMovimientoCC;
	}

	public int getIdRowItemSM_ccprov() {
		return idRowItemSM_ccprov;
	}

	public void setIdRowItemSM_ccprov(int idRowItemSM_ccprov) {
		this.idRowItemSM_ccprov = idRowItemSM_ccprov;
	}

	public String getScrMensajeAlert() {
		return scrMensajeAlert;
	}

	public void setScrMensajeAlert(String scrMensajeAlert) {
		this.scrMensajeAlert = scrMensajeAlert;
	}

	public Integer getListSubMovimientoCC_size_ccprov() {
		this.listSubMovimientoCC_size_ccprov = 0;

		if (this.listSubMovimientoCC != null && !this.listSubMovimientoCC.getSubMovimientoCuentaCorriente().isEmpty()) {
			this.listSubMovimientoCC_size_ccprov = this.listSubMovimientoCC.getSubMovimientoCuentaCorriente().size();
		}
		return listSubMovimientoCC_size_ccprov;
	}

	public void setListSubMovimientoCC_size_ccprov(Integer listSubMovimientoCC_size_ccprov) {
		this.listSubMovimientoCC_size_ccprov = listSubMovimientoCC_size_ccprov;
	}

	public String getNroFacturaCC() {
		return nroFacturaCC;
	}

	public void setNroFacturaCC(String nroFacturaCC) {
		this.nroFacturaCC = nroFacturaCC;
	}

	public Float getValPendPagoFacturaCC() {

		Float valRet = null;
		try {

			if ((this.idProveedorCC != null) && (this.idProveedorCC != 0) && (this.nroFacturaCC != null)
					&& (!"".equals(this.nroFacturaCC))) {
				FacturaList fl = GestionServiceHelper.getGestionService(CfgTimeout.DEFAULT).listaDeFacturas(
						this.getUsuario().getIdMayorista(), this.idProveedorCC, this.nroFacturaCC, null);

				if (!fl.getError().getValue().getHayError().getValue()) {
					List<Factura> lf = fl.getListFact().getValue().getFactura();

					if ((lf != null) && (!lf.isEmpty()) && (lf.size() == 1)) {

						valRet = lf.get(0).getValorPendienteDePago().getValue();
					}
				}
			}
		} catch (Exception e) {
			// No hago nada
		}
		return valRet;

	}

	public List<SelectItem> getCc_banco_comentarios() {
		try {

			this.cc_banco_comentarios = new ArrayList<SelectItem>();
			DescripcionContainer cd = GestionServiceHelper.getGestionService(CfgTimeout.DEFAULT)
					.mostrarCCbancosComentarios(this.getUsuario().getIdMayorista(), null);

			for (Descripcion d : cd.getListDescripcion().getValue().getDescripcion()) {
				this.cc_banco_comentarios.add(new SelectItem(d.getId().getValue(), d.getDescripcion().getValue()));
			}

		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
					"Error en lectura de Comentarios para Bancos: " + e.getMessage(), null));
		}
		return cc_banco_comentarios;
	}

	public Float getWu_saldo_header() {
		// CONSULTA de Saldo de WU del cliente logueado
		try {
			wu_saldo_header = GestionServiceHelper.getGestionService(CfgTimeout.DEFAULT)
					.wuObtenerSaldo(this.getUsuario().getIdMayorista(), this.getUsuario().getIdCliente()).getValor()
					.getValue();
		} catch (Exception e) {
			wu_saldo_header = 0F;
		}
		return wu_saldo_header;
	}

	public List<SelectItem> getCabeceraTablasComisiones() {

		try {

			this.cabeceraTablasComisiones = new ArrayList<SelectItem>();
			ArrayOfCabeceraTablaComision lctc = GestionServiceHelper.getGestionService(CfgTimeout.DEFAULT)
					.mostrarCabeceraTablasComisiones(this.getUsuario().getIdMayorista(), null,
							this.getUsuario().getIdCliente());

			if ((lctc != null) && (lctc.getCabeceraTablaComision() != null)) {

				// TODO: antes tenia el manejo de error y ahora no. Ver como solucionarlo
				// && (!lctc.getCabeceraTablaComision().
				// .getError().getValue().getHayError().getValue())) {

				for (CabeceraTablaComision ctc : lctc.getCabeceraTablaComision()) {

					this.cabeceraTablasComisiones
							.add(new SelectItem(ctc.getIdListaComision().getValue(), ctc.getDescripcion().getValue()));

				}

			}

		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
					"Error en lectura de CabeceraTablasComisiones: " + e.getMessage(), null));
		}

		return cabeceraTablasComisiones;
	}

	public List<SelectItem> getBancosTodos() {

		try {

			if (this.bancosTodos == null) {
				this.bancosTodos = new ArrayList<SelectItem>();
				BancoContainer bc = GestionServiceHelper.getGestionService(CfgTimeout.DEFAULT)
						.mostrarBanco(this.getUsuario().getIdMayorista(), null, null, false);

				if ((bc != null) && (bc.getListBanco() != null) && (bc.getListBanco().getValue() != null)
						&& (bc.getListBanco().getValue().getBanco() != null)
						&& (bc.getListBanco().getValue().getBanco().size() > 0)) {
					for (Banco b : bc.getListBanco().getValue().getBanco()) {
						this.bancosTodos.add(new SelectItem(b.getIdBanco().getValue(),
								b.getNombreBanco().getValue() + "|" + b.getSucursal().getValue() + "|"
										+ b.getCuenta().getValue() + "|" + b.getCBU().getValue() + "|"
										+ b.getDescripcion().getValue() + "|"));
					}
				}
			}

		} catch (Exception e) {
			this.bancosTodos = new ArrayList<SelectItem>();
		}

		return bancosTodos;
	}

	public void setValPendPagoFacturaCC(Float valPendPagoFacturaCC) {
		this.valPendPagoFacturaCC = valPendPagoFacturaCC;
	}

	public void setCc_banco_comentarios(List<SelectItem> cc_banco_comentarios) {
		this.cc_banco_comentarios = cc_banco_comentarios;
	}

	public Long getId_ccbanco_coment() {
		return id_ccbanco_coment;
	}

	public void setId_ccbanco_coment(Long id_ccbanco_coment) {
		this.id_ccbanco_coment = id_ccbanco_coment;
	}

	public Integer getAccionIncrementoSaldoMayorista() {
		return accionIncrementoSaldoMayorista;
	}

	public void setAccionIncrementoSaldoMayorista(Integer accionIncrementoSaldoMayorista) {
		this.accionIncrementoSaldoMayorista = accionIncrementoSaldoMayorista;
	}

	public Long getTipoMovimientoADS() {
		return tipoMovimientoADS;
	}

	public void setTipoMovimientoADS(Long tipoMovimientoADS) {
		this.tipoMovimientoADS = tipoMovimientoADS;
	}

	public Long getId_tipoReparto_wu_may() {
		return id_tipoReparto_wu_may;
	}

	public void setId_tipoReparto_wu_may(Long id_tipoReparto_wu_may) {
		this.id_tipoReparto_wu_may = id_tipoReparto_wu_may;
	}

	public Float getImporte_inc_mwu() {
		return importe_inc_mwu;
	}

	public void setImporte_inc_mwu(Float importe_inc_mwu) {
		this.importe_inc_mwu = importe_inc_mwu;
	}

	public Float getSaldoMayWU() {
		return saldoMayWU;
	}

	public void setSaldoMayWU(Float saldoMayWU) {
		this.saldoMayWU = saldoMayWU;
	}

	public String getComentario_incSaldo_wu() {
		return comentario_incSaldo_wu;
	}

	public void setComentario_incSaldo_wu(String comentario_incSaldo_wu) {
		this.comentario_incSaldo_wu = comentario_incSaldo_wu;
	}

	public void setWu_saldo_header(Float wu_saldo_header) {
		this.wu_saldo_header = wu_saldo_header;
	}

	public Long getSel_cli_idListCom() {
		return sel_cli_idListCom;
	}

	public void setSel_cli_idListCom(Long sel_cli_idListCom) {
		this.sel_cli_idListCom = sel_cli_idListCom;
	}

	public void setCabeceraTablasComisiones(List<SelectItem> cabeceraTablasComisiones) {
		this.cabeceraTablasComisiones = cabeceraTablasComisiones;
	}

	public List<HistorialAsginacionListaComisiones> getListHistAsigListCom() {
		return listHistAsigListCom;
	}

	public void setListHistAsigListCom(List<HistorialAsginacionListaComisiones> listHistAsigListCom) {
		this.listHistAsigListCom = listHistAsigListCom;
	}

	public String getWork() {
		return work;
	}

	public void setWork(String work) {
		this.work = work;
	}

	public String getDescArbol() {
		return descArbol;
	}

	public void setDescArbol(String descArbol) {
		this.descArbol = descArbol;
	}

	public Boolean getMostrarReporteComisionesVigentesD() {
		return mostrarReporteComisionesVigentesD;
	}

	public void setMostrarReporteComisionesVigentesD(Boolean mostrarReporteComisionesVigentesD) {
		this.mostrarReporteComisionesVigentesD = mostrarReporteComisionesVigentesD;
	}

	public Long getIdBancoAcredDepAdel() {
		return idBancoAcredDepAdel;
	}

	public void setIdBancoAcredDepAdel(Long idBancoAcredDepAdel) {
		this.idBancoAcredDepAdel = idBancoAcredDepAdel;
	}

	public void setBancosTodos(List<SelectItem> bancosTodos) {
		this.bancosTodos = bancosTodos;
	}

	public Boolean getOrdenarPorIdDepAdelantoDesc() {
		return ordenarPorIdDepAdelantoDesc;
	}

	public void setOrdenarPorIdDepAdelantoDesc(Boolean ordenarPorIdDepAdelantoDesc) {
		this.ordenarPorIdDepAdelantoDesc = ordenarPorIdDepAdelantoDesc;
	}

	public Date getMovExtFechaDesde() {
		return movExtFechaDesde;
	}

	public void setMovExtFechaDesde(Date movExtFechaDesde) {
		this.movExtFechaDesde = movExtFechaDesde;
	}

	public Date getMovExtfechaHasta() {
		return movExtfechaHasta;
	}

	public void setMovExtfechaHasta(Date movExtfechaHasta) {
		this.movExtfechaHasta = movExtfechaHasta;
	}

	public Integer getMovExtIdEstado() {
		return movExtIdEstado;
	}

	public void setMovExtIdEstado(Integer movExtIdEstado) {
		this.movExtIdEstado = movExtIdEstado;
	}

	public String getMovExtTipoComp() {
		return movExtTipoComp;
	}

	public void setMovExtTipoComp(String movExtTipoComp) {
		this.movExtTipoComp = movExtTipoComp;
	}

	public String getMovExtnumeroComp() {
		return movExtnumeroComp;
	}

	public void setMovExtnumeroComp(String movExtnumeroComp) {
		this.movExtnumeroComp = movExtnumeroComp;
	}

	public Boolean getMovExtIgnorarFecha() {
		return movExtIgnorarFecha;
	}

	public void setMovExtIgnorarFecha(Boolean movExtIgnorarFecha) {
		this.movExtIgnorarFecha = movExtIgnorarFecha;
	}

	public String getMovExtIdClienteExt() {
		return movExtIdClienteExt;
	}

	public void setMovExtIdClienteExt(String movExtIdClienteExt) {
		this.movExtIdClienteExt = movExtIdClienteExt;
	}

	public Float getMovExtImporte() {
		return movExtImporte;
	}

	public void setMovExtImporte(Float movExtImporte) {
		this.movExtImporte = movExtImporte;
	}

	public Long getMovExtIdCliente() {
		return movExtIdCliente;
	}

	public void setMovExtIdCliente(Long movExtIdCliente) {
		this.movExtIdCliente = movExtIdCliente;
	}

	public List<ComprobanteExterno> getLce() {
		return lce;
	}

	public void setLce(List<ComprobanteExterno> lce) {
		this.lce = lce;
	}

	public String getOrdenamientoRM() {
		return ordenamientoRM;
	}

	public void setOrdenamientoRM(String ordenamientoRM) {
		this.ordenamientoRM = ordenamientoRM;
	}

	public String getDescArbolRM() {
		return descArbolRM;
	}

	public void setDescArbolRM(String descArbolRM) {
		this.descArbolRM = descArbolRM;
	}

	public Integer getScrollPageRM() {
		return scrollPageRM;
	}

	public void setScrollPageRM(Integer scrollPageRM) {
		this.scrollPageRM = scrollPageRM;
	}

	public String getValorFiltroJerarquiaRM() {
		return valorFiltroJerarquiaRM;
	}

	public void setValorFiltroJerarquiaRM(String valorFiltroJerarquiaRM) {
		this.valorFiltroJerarquiaRM = valorFiltroJerarquiaRM;
	}

	public String getTabPanelSelectedTab() {
		return tabPanelSelectedTab;
	}

	public void setTabPanelSelectedTab(String tabPanelSelectedTab) {
		this.tabPanelSelectedTab = tabPanelSelectedTab;
	}

	public String getModalPanelInformesGenericosContenidoAIncluir() {
		return modalPanelInformesGenericosContenidoAIncluir;
	}

	public void setModalPanelInformesGenericosContenidoAIncluir(String modalPanelInformesGenericosContenidoAIncluir) {
		this.modalPanelInformesGenericosContenidoAIncluir = modalPanelInformesGenericosContenidoAIncluir;
	}

	public String getModalPanelInformesGenericosTitulo() {
		return modalPanelInformesGenericosTitulo;
	}

	public void setModalPanelInformesGenericosTitulo(String modalPanelInformesGenericosTitulo) {
		this.modalPanelInformesGenericosTitulo = modalPanelInformesGenericosTitulo;
	}

	public TreeNode getClientesRoot() {
		return clientesRoot;
	}

	public void setClientesRoot(TreeNode clientesRoot) {
		this.clientesRoot = clientesRoot;
	}

	public ArrayOfJerarquia getAojClientes() {

		if (aojClientes == null) {

			String ordenamientoAseleccionar = "numerico";

			// Busco los subclientes
			aojClientes = GestionServiceHelper.getGestionService(CfgTimeout.DEFAULT).traerJerarquia(
					this.getUsuario().getIdCliente(), this.getUsuario().getIdMayorista(), 1,
					this.getUsuario().getNivelDistribuidorSuperior(), ordenamientoAseleccionar);
		}

		return aojClientes;
	}

	public void obtenerJerarquiaCliente() {
		ArrayOfClienteReducido listCliRedTmp = null;
		ClienteReducido oCR = null;
		ItemArbol ia = null;
		ItemArbol iaSel = null;
		boolean cargoCliSel = false;

		// ------------------------------------------------------------------
		// Busca el cliente que se le dio doble click en la busqueda puntual
		seleccionCliente(this.getClienteSeleccionado1().getIdCliente());
		// ------------------------------------------------------------------

		if (this.getClienteSeleccionado() != null) {
			// ------------------------------------------------------------------
			// Inicializa las selecciones
			// ------------------------------------------------------------------
			inicializaListasYSeleccionesArbol();

			iaSel = new ItemArbol();
			iaSel.setIdCliente(this.getClienteSeleccionado().getIdCliente().getValue());
			iaSel.setIdMayorista(this.getUsuario().getIdMayorista());
			iaSel.setNivelDistribuidorSuperior(getClienteSeleccionado().getNivelDistribuidorSuperior().getValue());
			iaSel.setIdTipoTerminal(getClienteSeleccionado().getIdTipoTerminal().getValue());
			iaSel.setTipoCliente(getClienteSeleccionado().getTipoCliente().getValue());

			if (this.getClienteSeleccionado().getIdDistribuidor2().getValue().compareTo(0L) > 0) {
				listCliRedTmp = GestionServiceHelper.getGestionService(CfgTimeout.DEFAULT).buscarCliente(
						this.getUsuario().getIdMayorista(), this.getUsuario().getIdCliente(), null,
						this.getClienteSeleccionado().getIdDistribuidor2().getValue(), null, null, null, null, null,
						null, null, null, null, null, null);

				if (listCliRedTmp != null && listCliRedTmp.getClienteReducido() != null
						&& listCliRedTmp.getClienteReducido().size() > 0) {
					oCR = listCliRedTmp.getClienteReducido().get(0);
					ia = new ItemArbol();
					ia.setIdCliente(oCR.getIdCliente().getValue());
					ia.setIdMayorista(this.getUsuario().getIdMayorista());
					ia.setNivelDistribuidorSuperior(oCR.getNivelDistribuidorSuperior().getValue());
					ia.setIdTipoTerminal(oCR.getIdTipoTerminal().getValue());
					ia.setTipoCliente(oCR.getTipoCliente().getValue());

					if ("IDCLIENTE".equals(this.getCampoOrdenamientoArbolN1().toUpperCase().trim())
							|| "RAZONSOCIAL".equals(this.getCampoOrdenamientoArbolN1().toUpperCase().trim()))
						ia.setTitulo(oCR.getRazonSocial().getValue());
					else
						ia.setTitulo(oCR.getNombreFantasia().getValue());

					this.setClienteSeleccionado1(ia);
				}
			} else {
				if ("IDCLIENTE".equals(this.getCampoOrdenamientoArbolN1().toUpperCase().trim())
						|| "RAZONSOCIAL".equals(this.getCampoOrdenamientoArbolN1().toUpperCase().trim()))
					iaSel.setTitulo(getClienteSeleccionado().getRazonSocial().getValue());
				else
					iaSel.setTitulo(getClienteSeleccionado().getNombreFantasia().getValue());

				this.setClienteSeleccionado1(iaSel);
				cargoCliSel = true;
			}

			if (this.getClienteSeleccionado().getIdDistribuidor3().getValue().compareTo(0L) > 0) {
				listCliRedTmp = GestionServiceHelper.getGestionService(CfgTimeout.DEFAULT).buscarCliente(
						this.getUsuario().getIdMayorista(), this.getUsuario().getIdCliente(), null,
						this.getClienteSeleccionado().getIdDistribuidor3().getValue(), null, null, null, null, null,
						null, null, null, null, null, null);

				if (listCliRedTmp != null && listCliRedTmp.getClienteReducido() != null
						&& listCliRedTmp.getClienteReducido().size() > 0) {
					oCR = listCliRedTmp.getClienteReducido().get(0);
					ia = new ItemArbol();
					ia.setIdCliente(oCR.getIdCliente().getValue());
					ia.setIdMayorista(this.getUsuario().getIdMayorista());
					ia.setNivelDistribuidorSuperior(oCR.getNivelDistribuidorSuperior().getValue());
					ia.setIdTipoTerminal(oCR.getIdTipoTerminal().getValue());
					ia.setTipoCliente(oCR.getTipoCliente().getValue());

					if ("IDCLIENTE".equals(this.getCampoOrdenamientoArbolN2().toUpperCase().trim())
							|| "RAZONSOCIAL".equals(this.getCampoOrdenamientoArbolN2().toUpperCase().trim()))
						ia.setTitulo(oCR.getRazonSocial().getValue());
					else
						ia.setTitulo(oCR.getNombreFantasia().getValue());

					this.setClienteSeleccionado2(ia);
				}
			} else if (!cargoCliSel) {
				if ("IDCLIENTE".equals(this.getCampoOrdenamientoArbolN2().toUpperCase().trim())
						|| "RAZONSOCIAL".equals(this.getCampoOrdenamientoArbolN2().toUpperCase().trim()))
					iaSel.setTitulo(getClienteSeleccionado().getRazonSocial().getValue());
				else
					iaSel.setTitulo(getClienteSeleccionado().getNombreFantasia().getValue());

				this.setClienteSeleccionado2(iaSel);
				cargoCliSel = true;
			}

			if (this.getClienteSeleccionado().getIdDistribuidor4().getValue().compareTo(0L) > 0) {
				listCliRedTmp = GestionServiceHelper.getGestionService(CfgTimeout.DEFAULT).buscarCliente(
						this.getUsuario().getIdMayorista(), this.getUsuario().getIdCliente(), null,
						this.getClienteSeleccionado().getIdDistribuidor4().getValue(), null, null, null, null, null,
						null, null, null, null, null, null);

				if (listCliRedTmp != null && listCliRedTmp.getClienteReducido() != null
						&& listCliRedTmp.getClienteReducido().size() > 0) {
					oCR = listCliRedTmp.getClienteReducido().get(0);
					ia = new ItemArbol();
					ia.setIdCliente(oCR.getIdCliente().getValue());
					ia.setIdMayorista(this.getUsuario().getIdMayorista());
					ia.setNivelDistribuidorSuperior(oCR.getNivelDistribuidorSuperior().getValue());
					ia.setIdTipoTerminal(oCR.getIdTipoTerminal().getValue());
					ia.setTipoCliente(oCR.getTipoCliente().getValue());

					if ("IDCLIENTE".equals(this.getCampoOrdenamientoArbolN3().toUpperCase().trim())
							|| "RAZONSOCIAL".equals(this.getCampoOrdenamientoArbolN3().toUpperCase().trim()))
						ia.setTitulo(oCR.getRazonSocial().getValue());
					else
						ia.setTitulo(oCR.getNombreFantasia().getValue());

					this.setClienteSeleccionado3(ia);
				}
			} else if (!cargoCliSel) {
				if ("IDCLIENTE".equals(this.getCampoOrdenamientoArbolN3().toUpperCase().trim())
						|| "RAZONSOCIAL".equals(this.getCampoOrdenamientoArbolN3().toUpperCase().trim()))
					iaSel.setTitulo(getClienteSeleccionado().getRazonSocial().getValue());
				else
					iaSel.setTitulo(getClienteSeleccionado().getNombreFantasia().getValue());

				this.setClienteSeleccionado3(iaSel);
				cargoCliSel = true;
			}

			if (this.getClienteSeleccionado().getIdDistribuidor5().getValue().compareTo(0L) > 0) {
				listCliRedTmp = GestionServiceHelper.getGestionService(CfgTimeout.DEFAULT).buscarCliente(
						this.getUsuario().getIdMayorista(), this.getUsuario().getIdCliente(), null,
						this.getClienteSeleccionado().getIdDistribuidor5().getValue(), null, null, null, null, null,
						null, null, null, null, null, null);

				if (listCliRedTmp != null && listCliRedTmp.getClienteReducido() != null
						&& listCliRedTmp.getClienteReducido().size() > 0) {
					oCR = listCliRedTmp.getClienteReducido().get(0);
					ia = new ItemArbol();
					ia.setIdCliente(oCR.getIdCliente().getValue());
					ia.setIdMayorista(this.getUsuario().getIdMayorista());
					ia.setNivelDistribuidorSuperior(oCR.getNivelDistribuidorSuperior().getValue());
					ia.setIdTipoTerminal(oCR.getIdTipoTerminal().getValue());
					ia.setTipoCliente(oCR.getTipoCliente().getValue());

					if ("IDCLIENTE".equals(this.getCampoOrdenamientoArbolN4().toUpperCase().trim())
							|| "RAZONSOCIAL".equals(this.getCampoOrdenamientoArbolN4().toUpperCase().trim()))
						ia.setTitulo(oCR.getRazonSocial().getValue());
					else
						ia.setTitulo(oCR.getNombreFantasia().getValue());

					this.setClienteSeleccionado4(ia);
				}
			} else if (!cargoCliSel) {
				if ("IDCLIENTE".equals(this.getCampoOrdenamientoArbolN4().toUpperCase().trim())
						|| "RAZONSOCIAL".equals(this.getCampoOrdenamientoArbolN4().toUpperCase().trim()))
					iaSel.setTitulo(getClienteSeleccionado().getRazonSocial().getValue());
				else
					iaSel.setTitulo(getClienteSeleccionado().getNombreFantasia().getValue());

				this.setClienteSeleccionado4(iaSel);
				cargoCliSel = true;
			}

			if (this.getClienteSeleccionado1() != null && this.getClienteSeleccionado2() != null
					&& this.getClienteSeleccionado3() != null && this.getClienteSeleccionado4() != null
					&& !cargoCliSel) {
				if ("IDCLIENTE".equals(this.getCampoOrdenamientoArbolN5().toUpperCase().trim())
						|| "RAZONSOCIAL".equals(this.getCampoOrdenamientoArbolN5().toUpperCase().trim()))
					iaSel.setTitulo(getClienteSeleccionado().getRazonSocial().getValue());
				else
					iaSel.setTitulo(getClienteSeleccionado().getNombreFantasia().getValue());

				this.setClienteSeleccionado5(iaSel);
			}
		}
	}

	public ArrayOfJerarquia getAojClientes1(Long idMayorista, Long idCliente, int idNivelDistribuidorSuperior) {

		ArrayOfJerarquia aojC = null;

		if (aojC == null) {

			String ordenamientoAseleccionar = "numerico";

			// Busco los subclientes
			aojC = GestionServiceHelper.getGestionService(CfgTimeout.DEFAULT).traerJerarquia(idCliente, idMayorista, 1,
					idNivelDistribuidorSuperior, ordenamientoAseleccionar);
		}

		return aojC;
	}

	public String obtenerColor(int terminal) {

		switch (terminal) {
		case 1: // WEB images/i_web.JPG
			return "color: #00ACE8";
		case 2: // IVR images/i_ivr.JPG
			return "color: #FFE45F";
		case 3: // WAP images/i_wap.JPG
			return "color: #B87A65";
		case 4: // POS VERIFONE images/i_pos.JPG
			return "color: #F5965E";
		case 5: // INT. APLIC. images/i_ia.JPG
			return "color: #5CBF88";
		case 6: // JAVA images/i_java.JPG
			return "color: #C8DF73";
		case 7: // TERM. EXTERNA images/i_termExt.JPG
			return "color: #D482B3";
		case 8: // BPOS images/i_bpos.JPG
			return "color: #D6D4E9";
		case 9: // AUTOSERVICIO images/i_autoservicio.JPG
			return "color: #F3684B";
		case 10: // PC images/i_pc.JPG
			return "color: #56A4DC";
		case 11: // CARGAS AUTOMATICAS images/i_ca.JPG
			return "color: #00FFCC";
		case 12: // POS SUNSHARD images/i_multicab.JPG
			return "color: #DFFF62";
		case 13: // USSD images/i_ussd.jpg
			return "color: #FF55C8";
		case 14: // PINPOS images/i_pinpos.JPG
			return "color: #8C00E5";
		case 15: // ANDROID images/i_android.jpg
			return "color: #00F007";
		case 16: // CASTLE images/i_castle.jpg
			return "#04C1FF";
		default:
			return "color: #DDBA69";
		}
	}

	public void setAojClientes(ArrayOfJerarquia aojClientes) {
		this.aojClientes = aojClientes;
	}

	public Jerarquia getJerarquiaSeleccionado() {
		return jerarquiaSeleccionado;
	}

	public void setJerarquiaSeleccionado(Jerarquia jerarquiaSeleccionado) {
		this.jerarquiaSeleccionado = jerarquiaSeleccionado;
	}

	public LazyDataModel<ItemArbol> getListClientesNivel1() {
		if (listClientesNivel1 == null) {
			// Busco los subclientes
			listClientesNivel1 = new LazyItemArbolDataModel(this.getUsuario().getIdCliente(),
					this.getUsuario().getIdMayorista(), this.getUsuario().getNivelDistribuidorSuperior(),
					this.getCampoOrdenamientoArbolN1(), "ASC");

		}

		return listClientesNivel1;
	}

	public void setListClientesNivel1(LazyDataModel<ItemArbol> listClientesNivel1) {
		this.listClientesNivel1 = listClientesNivel1;
	}

	public LazyDataModel<ItemArbol> getListClientesNivel2() {
		if (listClientesNivel2 == null && this.getClienteSeleccionado1() != null) {
			// Busco los subclientes
			listClientesNivel2 = new LazyItemArbolDataModel(this.getClienteSeleccionado1().getIdCliente(),
					this.getClienteSeleccionado1().getIdMayorista(),
					this.getClienteSeleccionado1().getNivelDistribuidorSuperior(), this.getCampoOrdenamientoArbolN2(),
					"ASC");

		}
		return listClientesNivel2;
	}

	public void setListClientesNivel2(LazyDataModel<ItemArbol> listClientesNivel2) {
		this.listClientesNivel2 = listClientesNivel2;
	}

	public LazyDataModel<ItemArbol> getListClientesNivel3() {
		if (listClientesNivel3 == null && this.getClienteSeleccionado2() != null) {
			// Busco los subclientes
			listClientesNivel3 = new LazyItemArbolDataModel(this.getClienteSeleccionado2().getIdCliente(),
					this.getClienteSeleccionado2().getIdMayorista(),
					this.getClienteSeleccionado2().getNivelDistribuidorSuperior(), this.getCampoOrdenamientoArbolN3(),
					"ASC");

		}
		return listClientesNivel3;
	}

	public void setListClientesNivel3(LazyDataModel<ItemArbol> listClientesNivel3) {
		this.listClientesNivel3 = listClientesNivel3;
	}

	public LazyDataModel<ItemArbol> getListClientesNivel4() {
		if (listClientesNivel4 == null && this.getClienteSeleccionado3() != null) {
			// Busco los subclientes
			listClientesNivel4 = new LazyItemArbolDataModel(this.getClienteSeleccionado3().getIdCliente(),
					this.getClienteSeleccionado3().getIdMayorista(),
					this.getClienteSeleccionado3().getNivelDistribuidorSuperior(), this.getCampoOrdenamientoArbolN4(),
					"ASC");

		}
		return listClientesNivel4;
	}

	public void setListClientesNivel4(LazyDataModel<ItemArbol> listClientesNivel4) {
		this.listClientesNivel4 = listClientesNivel4;
	}

	public LazyDataModel<ItemArbol> getListClientesNivel5() {
		if (listClientesNivel5 == null && this.getClienteSeleccionado4() != null) {
			// Busco los subclientes
			listClientesNivel5 = new LazyItemArbolDataModel(this.getClienteSeleccionado4().getIdCliente(),
					this.getClienteSeleccionado4().getIdMayorista(),
					this.getClienteSeleccionado4().getNivelDistribuidorSuperior(), this.getCampoOrdenamientoArbolN5(),
					"ASC");

		}
		return listClientesNivel5;
	}

	public void setListClientesNivel5(LazyDataModel<ItemArbol> listClientesNivel5) {
		this.listClientesNivel5 = listClientesNivel5;
	}

	public ItemArbol getClienteSeleccionado1() {
		return clienteSeleccionado1;
	}

	public void setClienteSeleccionado1(ItemArbol clienteSeleccionado1) {
		Cliente oCli = null;

		if (clienteSeleccionado1 != null && (this.clienteSeleccionado1 == null
				|| this.clienteSeleccionado1.getIdCliente().compareTo(clienteSeleccionado1.getIdCliente()) != 0)) {

			this.clienteSeleccionado1 = clienteSeleccionado1;

			oCli = GestionServiceHelper.getGestionService(CfgTimeout.DEFAULT)
					.mostrarCliente(this.getUsuario().getIdMayorista(), this.clienteSeleccionado1.getIdCliente());

			if (oCli != null) {
				if ("IDCLIENTE".equals(this.getCampoOrdenamientoArbolN1().toUpperCase().trim())
						|| "RAZONSOCIAL".equals(this.getCampoOrdenamientoArbolN1().toUpperCase().trim()))
					this.clienteSeleccionado1.setTitulo(oCli.getRazonSocial().getValue());
				else
					this.clienteSeleccionado1.setTitulo(oCli.getNombreFantasia().getValue());
			}
		}

		this.clienteSeleccionado2 = null;
		this.clienteSeleccionado3 = null;
		this.clienteSeleccionado4 = null;
		this.clienteSeleccionado5 = null;
	}

	public ItemArbol getClienteSeleccionado2() {
		return clienteSeleccionado2;
	}

	public void setClienteSeleccionado2(ItemArbol clienteSeleccionado2) {
		Cliente oCli = null;

		if (clienteSeleccionado2 != null && (this.clienteSeleccionado2 == null
				|| this.clienteSeleccionado2.getIdCliente().compareTo(clienteSeleccionado2.getIdCliente()) != 0)) {

			this.clienteSeleccionado2 = clienteSeleccionado2;
			oCli = GestionServiceHelper.getGestionService(CfgTimeout.DEFAULT)
					.mostrarCliente(this.getUsuario().getIdMayorista(), this.clienteSeleccionado2.getIdCliente());

			if (oCli != null) {
				if ("IDCLIENTE".equals(this.getCampoOrdenamientoArbolN2().toUpperCase().trim())
						|| "RAZONSOCIAL".equals(this.getCampoOrdenamientoArbolN2().toUpperCase().trim()))
					this.clienteSeleccionado2.setTitulo(oCli.getRazonSocial().getValue());
				else
					this.clienteSeleccionado2.setTitulo(oCli.getNombreFantasia().getValue());
			}
		}

		this.clienteSeleccionado3 = null;
		this.clienteSeleccionado4 = null;
		this.clienteSeleccionado5 = null;
	}

	public ItemArbol getClienteSeleccionado3() {
		return clienteSeleccionado3;
	}

	public void setClienteSeleccionado3(ItemArbol clienteSeleccionado3) {
		Cliente oCli = null;

		if (clienteSeleccionado3 != null && (this.clienteSeleccionado3 == null
				|| this.clienteSeleccionado3.getIdCliente().compareTo(clienteSeleccionado3.getIdCliente()) != 0)) {

			this.clienteSeleccionado3 = clienteSeleccionado3;
			oCli = GestionServiceHelper.getGestionService(CfgTimeout.DEFAULT)
					.mostrarCliente(this.getUsuario().getIdMayorista(), this.clienteSeleccionado3.getIdCliente());

			if (oCli != null) {
				if ("IDCLIENTE".equals(this.getCampoOrdenamientoArbolN3().toUpperCase().trim())
						|| "RAZONSOCIAL".equals(this.getCampoOrdenamientoArbolN3().toUpperCase().trim()))
					this.clienteSeleccionado3.setTitulo(oCli.getRazonSocial().getValue());
				else
					this.clienteSeleccionado3.setTitulo(oCli.getNombreFantasia().getValue());
			}
		}

		this.clienteSeleccionado4 = null;
		this.clienteSeleccionado5 = null;
	}

	public ItemArbol getClienteSeleccionado4() {
		return clienteSeleccionado4;
	}

	public void setClienteSeleccionado4(ItemArbol clienteSeleccionado4) {
		Cliente oCli = null;

		if (clienteSeleccionado4 != null && (this.clienteSeleccionado4 == null
				|| this.clienteSeleccionado4.getIdCliente().compareTo(clienteSeleccionado4.getIdCliente()) != 0)) {

			this.clienteSeleccionado4 = clienteSeleccionado4;

			oCli = GestionServiceHelper.getGestionService(CfgTimeout.DEFAULT)
					.mostrarCliente(this.getUsuario().getIdMayorista(), this.clienteSeleccionado4.getIdCliente());

			if (oCli != null) {
				if ("IDCLIENTE".equals(this.getCampoOrdenamientoArbolN4().toUpperCase().trim())
						|| "RAZONSOCIAL".equals(this.getCampoOrdenamientoArbolN4().toUpperCase().trim()))
					this.clienteSeleccionado4.setTitulo(oCli.getRazonSocial().getValue());
				else
					this.clienteSeleccionado4.setTitulo(oCli.getNombreFantasia().getValue());
			}
		}

		this.clienteSeleccionado5 = null;
	}

	public ItemArbol getClienteSeleccionado5() {
		return clienteSeleccionado5;
	}

	public void setClienteSeleccionado5(ItemArbol clienteSeleccionado5) {
		Cliente oCli = null;

		if (clienteSeleccionado5 != null && (this.clienteSeleccionado5 == null
				|| this.clienteSeleccionado5.getIdCliente().compareTo(clienteSeleccionado5.getIdCliente()) != 0)) {

			this.clienteSeleccionado5 = clienteSeleccionado5;
			oCli = GestionServiceHelper.getGestionService(CfgTimeout.DEFAULT)
					.mostrarCliente(this.getUsuario().getIdMayorista(), this.clienteSeleccionado5.getIdCliente());

			if (oCli != null) {
				if ("IDCLIENTE".equals(this.getCampoOrdenamientoArbolN5().toUpperCase().trim())
						|| "RAZONSOCIAL".equals(this.getCampoOrdenamientoArbolN5().toUpperCase().trim()))
					this.clienteSeleccionado5.setTitulo(oCli.getRazonSocial().getValue());
				else
					this.clienteSeleccionado5.setTitulo(oCli.getNombreFantasia().getValue());
			}
		}
	}

	private void inicializarSeleccion(int nivel) {
		if (nivel <= 1) {
			this.listClientesNivel1 = null;
			this.clienteSeleccionado1 = null;
		}
		if (nivel <= 2) {
			this.listClientesNivel2 = null;
			this.clienteSeleccionado2 = null;
		}

		if (nivel <= 3) {
			this.listClientesNivel3 = null;
			this.clienteSeleccionado3 = null;
		}

		if (nivel <= 4) {

			this.listClientesNivel4 = null;
			this.clienteSeleccionado4 = null;
		}
		if (nivel <= 5) {
			this.listClientesNivel5 = null;
			this.clienteSeleccionado5 = null;
		}
	}

	public void onRowSelect1(SelectEvent event) {
		ArrayList<String> lstUpdt = new ArrayList<String>();
		inicializarSeleccion(2);

		// ------------------------------------------------------------------
		// Busca el cliente que se le dio doble click en la busqueda puntual
		seleccionCliente(this.getClienteSeleccionado1().getIdCliente());
		// ------------------------------------------------------------------

		this.cerrarIconosYPantallaProducto(lstUpdt);

		lstUpdt.add(":menuSeleccionCliente");

		PrimeFaces.current().ajax().update(":menuSeleccionCliente");
	}

	public void onRowSelect2(SelectEvent event) {
		ArrayList<String> lstUpdt = new ArrayList<String>();

		inicializarSeleccion(3);

		// ------------------------------------------------------------------
		// Busca el cliente que se le dio doble click en la busqueda puntual
		seleccionCliente(this.getClienteSeleccionado2().getIdCliente());
		// ------------------------------------------------------------------

		this.cerrarIconosYPantallaProducto(lstUpdt);
		lstUpdt.add(":menuSeleccionCliente");

		PrimeFaces.current().ajax().update(lstUpdt);
	}

	public void onRowSelect3(SelectEvent event) {
		ArrayList<String> lstUpdt = new ArrayList<String>();
		inicializarSeleccion(4);

		// ------------------------------------------------------------------
		// Busca el cliente que se le dio doble click en la busqueda puntual
		seleccionCliente(this.getClienteSeleccionado3().getIdCliente());
		// ------------------------------------------------------------------

		this.cerrarIconosYPantallaProducto(lstUpdt);
		lstUpdt.add(":menuSeleccionCliente");

		PrimeFaces.current().ajax().update(lstUpdt);
	}

	public void onRowSelect4(SelectEvent event) {
		ArrayList<String> lstUpdt = new ArrayList<String>();
		inicializarSeleccion(5);

		// ------------------------------------------------------------------
		// Busca el cliente que se le dio doble click en la busqueda puntual
		seleccionCliente(this.getClienteSeleccionado4().getIdCliente());
		// ------------------------------------------------------------------

		this.cerrarIconosYPantallaProducto(lstUpdt);
		lstUpdt.add(":menuSeleccionCliente");

		PrimeFaces.current().ajax().update(lstUpdt);
	}

	public void onRowSelect5(SelectEvent event) {
		ArrayList<String> lstUpdt = new ArrayList<String>();

		this.cerrarIconosYPantallaProducto(lstUpdt);
		lstUpdt.add(":menuSeleccionCliente");

		PrimeFaces.current().ajax().update(lstUpdt);

		// ------------------------------------------------------------------
		// Busca el cliente que se le dio doble click en la busqueda puntual
		seleccionCliente(this.getClienteSeleccionado5().getIdCliente());
		// ------------------------------------------------------------------
	}

	public void seleccionMenuUsuario(Integer nivel) {
		ArrayList<String> lstUpdt = new ArrayList<String>();

		switch (nivel) {
		case 1: {
			onRowSelect1(null);
			lstUpdt.add(":arbolNivel2");
			lstUpdt.add(":arbolNivel3");
			lstUpdt.add(":arbolNivel4");
			lstUpdt.add(":arbolNivel5");

			PrimeFaces.current().executeScript("ArbolFCNS.arbolResize(1);");
			this.cerrarIconosYPantallaProducto(lstUpdt);

			break;
		}
		case 2: {
			onRowSelect2(null);
			lstUpdt.add(":arbolNivel3");
			lstUpdt.add(":arbolNivel4");
			lstUpdt.add(":arbolNivel5");

			PrimeFaces.current().executeScript("ArbolFCNS.arbolResize(2);");
			this.cerrarIconosYPantallaProducto(lstUpdt);

			break;
		}
		case 3: {
			onRowSelect3(null);
			lstUpdt.add(":arbolNivel4");
			lstUpdt.add(":arbolNivel5");

			PrimeFaces.current().executeScript("ArbolFCNS.arbolResize(3);");
			this.cerrarIconosYPantallaProducto(lstUpdt);

			break;
		}
		case 4: {
			onRowSelect4(null);
			lstUpdt.add(":arbolNivel5");

			PrimeFaces.current().executeScript("ArbolFCNS.arbolResize(4);");
			this.cerrarIconosYPantallaProducto(lstUpdt);

			break;
		}
		}

		PrimeFaces.current().ajax().update(lstUpdt);
	}

	public void onFilterN1(FilterEvent filterEvent) {
		ArrayList<String> lstUpdt = new ArrayList<String>();

		this.clienteSeleccionado2 = null;
		this.listClientesNivel2 = null;

		this.clienteSeleccionado3 = null;
		this.listClientesNivel3 = null;

		this.clienteSeleccionado4 = null;
		this.listClientesNivel4 = null;

		this.clienteSeleccionado5 = null;
		this.listClientesNivel5 = null;

		lstUpdt.add("arbolNivel2");
		lstUpdt.add("arbolNivel3");
		lstUpdt.add("arbolNivel4");
		lstUpdt.add("arbolNivel5");

		this.cerrarIconosYPantallaProducto(lstUpdt);

		PrimeFaces.current().ajax().update(lstUpdt);
	}

	public void onFilterN2(FilterEvent filterEvent) {
		ArrayList<String> lstUpdt = new ArrayList<String>();

		this.clienteSeleccionado3 = null;
		this.listClientesNivel3 = null;

		this.clienteSeleccionado4 = null;
		this.listClientesNivel4 = null;

		this.clienteSeleccionado5 = null;
		this.listClientesNivel5 = null;

		lstUpdt.add("arbolNivel3");
		lstUpdt.add("arbolNivel4");
		lstUpdt.add("arbolNivel5");

		this.cerrarIconosYPantallaProducto(lstUpdt);

		PrimeFaces.current().ajax().update(lstUpdt);
	}

	public void onFilterN3(FilterEvent filterEvent) {
		ArrayList<String> lstUpdt = new ArrayList<String>();

		this.clienteSeleccionado4 = null;
		this.listClientesNivel4 = null;

		this.clienteSeleccionado5 = null;
		this.listClientesNivel5 = null;

		lstUpdt.add("arbolNivel4");
		lstUpdt.add("arbolNivel5");

		this.cerrarIconosYPantallaProducto(lstUpdt);

		PrimeFaces.current().ajax().update(lstUpdt);
	}

	public void onFilterN4(FilterEvent filterEvent) {
		ArrayList<String> lstUpdt = new ArrayList<String>();
		this.clienteSeleccionado5 = null;
		this.listClientesNivel5 = null;

		this.cerrarIconosYPantallaProducto(lstUpdt);

		PrimeFaces.current().ajax().update("arbolNivel5");
	}

	public void onSortN1(SortEvent sortEvent) {
		ArrayList<String> lstUpdt = new ArrayList<String>();

		this.clienteSeleccionado1 = null;
		this.clienteSeleccionado2 = null;
		this.listClientesNivel2 = null;

		this.clienteSeleccionado3 = null;
		this.listClientesNivel3 = null;

		this.clienteSeleccionado4 = null;
		this.listClientesNivel4 = null;

		this.clienteSeleccionado5 = null;
		this.listClientesNivel5 = null;

		lstUpdt.add("arbolNivel2");
		lstUpdt.add("arbolNivel3");
		lstUpdt.add("arbolNivel4");
		lstUpdt.add("arbolNivel5");

		this.cerrarIconosYPantallaProducto(lstUpdt);

		PrimeFaces.current().ajax().update(lstUpdt);
	}

	public void onSortN2(SortEvent sortEvent) {
		ArrayList<String> lstUpdt = new ArrayList<String>();

		this.clienteSeleccionado2 = null;
		this.clienteSeleccionado3 = null;
		this.listClientesNivel3 = null;

		this.clienteSeleccionado4 = null;
		this.listClientesNivel4 = null;

		this.clienteSeleccionado5 = null;
		this.listClientesNivel5 = null;

		lstUpdt.add("arbolNivel3");
		lstUpdt.add("arbolNivel4");
		lstUpdt.add("arbolNivel5");

		this.cerrarIconosYPantallaProducto(lstUpdt);

		PrimeFaces.current().ajax().update(lstUpdt);
	}

	public void onSortN3(SortEvent sortEvent) {
		ArrayList<String> lstUpdt = new ArrayList<String>();

		this.clienteSeleccionado3 = null;
		this.clienteSeleccionado4 = null;
		this.listClientesNivel4 = null;

		this.clienteSeleccionado5 = null;
		this.listClientesNivel5 = null;

		lstUpdt.add("arbolNivel4");
		lstUpdt.add("arbolNivel5");

		this.cerrarIconosYPantallaProducto(lstUpdt);

		PrimeFaces.current().ajax().update(lstUpdt);
	}

	public void onSortN4(SortEvent sortEvent) {
		ArrayList<String> lstUpdt = new ArrayList<String>();

		this.clienteSeleccionado4 = null;
		this.clienteSeleccionado5 = null;
		this.listClientesNivel5 = null;

		this.cerrarIconosYPantallaProducto(lstUpdt);
		lstUpdt.add("arbolNivel5");

		PrimeFaces.current().ajax().update(lstUpdt);
	}

	public String getCampoOrdenamientoArbolN1() {
		return campoOrdenamientoArbolN1;
	}

	public void setCampoOrdenamientoArbolN1(String campoOrdenamientoArbolN1) {
		this.campoOrdenamientoArbolN1 = campoOrdenamientoArbolN1;
	}

	public String getCampoOrdenamientoArbolN2() {
		return campoOrdenamientoArbolN2;
	}

	public void setCampoOrdenamientoArbolN2(String campoOrdenamientoArbolN2) {
		this.campoOrdenamientoArbolN2 = campoOrdenamientoArbolN2;
	}

	public String getCampoOrdenamientoArbolN3() {
		return campoOrdenamientoArbolN3;
	}

	public void setCampoOrdenamientoArbolN3(String campoOrdenamientoArbolN3) {
		this.campoOrdenamientoArbolN3 = campoOrdenamientoArbolN3;
	}

	public String getCampoOrdenamientoArbolN4() {
		return campoOrdenamientoArbolN4;
	}

	public void setCampoOrdenamientoArbolN4(String campoOrdenamientoArbolN4) {
		this.campoOrdenamientoArbolN4 = campoOrdenamientoArbolN4;
	}

	public String getCampoOrdenamientoArbolN5() {
		return campoOrdenamientoArbolN5;
	}

	public void setCampoOrdenamientoArbolN5(String campoOrdenamientoArbolN5) {
		this.campoOrdenamientoArbolN5 = campoOrdenamientoArbolN5;
	}

	public void inicializaListasYSeleccionesArbol() {
		listClientesNivel1 = null;
		listClientesNivel2 = null;
		listClientesNivel3 = null;
		listClientesNivel4 = null;
		listClientesNivel5 = null;

		this.clienteSeleccionado1 = null;
		this.clienteSeleccionado2 = null;
		this.clienteSeleccionado3 = null;
		this.clienteSeleccionado4 = null;
		this.clienteSeleccionado5 = null;
	}

	public List<SelectItem> getProvincias() {
		try {
			if (provincias == null) {
				this.provincias = new ArrayList<SelectItem>();
				this.localidades = null;

				if ((this.getUsuario().getIdPais() != null) && (this.getUsuario().getIdPais().compareTo(0L) > 0)) {
					DescripcionContainer cd = GestionServiceHelper.getGestionService(CfgTimeout.DEFAULT)
							.mostrarProvincias(this.getUsuario().getIdMayorista(), null, this.getUsuario().getIdPais());

					if ((cd.getListDescripcion() != null && cd.getListDescripcion().getValue() != null
							&& cd.getListDescripcion().getValue().getDescripcion() != null)
							&& (cd.getListDescripcion().getValue().getDescripcion().size() > 0)) {
						for (Descripcion d : cd.getListDescripcion().getValue().getDescripcion()) {
							this.provincias.add(new SelectItem(d.getId().getValue(), d.getDescripcion().getValue()));
						}
					}

				}
			}
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
					"Error en lectura de Provincias: " + e.getMessage(), null));
		}

		return provincias;
	}

	public List<SelectItem> getLocalidades() {
		try {
			if (this.getProvinciaFlt() != null && localidades == null) {
				this.localidades = new ArrayList<SelectItem>();

				if ((this.getProvinciaFlt() != null)
						&& (this.getProvinciaFlt() != null && !"0".equals(this.getProvinciaFlt()))) {
					DescripcionContainer cd = GestionServiceHelper.getGestionService(CfgTimeout.DEFAULT)
							.mostrarLocalidadesPorProv(this.getUsuario().getIdMayorista(), this.getProvinciaFlt(),
									null);

					if ((cd.getListDescripcion() != null && cd.getListDescripcion().getValue() != null
							&& cd.getListDescripcion().getValue().getDescripcion() != null)) {
						for (Descripcion d : cd.getListDescripcion().getValue().getDescripcion()) {
							this.localidades.add(new SelectItem(d.getId().getValue(), d.getDescripcion().getValue()));
						}
					}
				}
			}
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
					"Error en lectura de Localidades CF: " + e.getMessage(), null));
		}

		return localidades;
	}

	public List<SelectItem> getTiposTerminal() {

		try {
			if (this.tiposTerminal == null) {
				this.tiposTerminal = new ArrayList<SelectItem>();
				DescripcionContainer cd = GestionServiceHelper.getGestionService(CfgTimeout.DEFAULT)
						.mostrarTipoTerminales(this.getUsuario().getIdMayorista());

				if ((cd.getListDescripcion() != null && cd.getListDescripcion().getValue() != null
						&& cd.getListDescripcion().getValue().getDescripcion() != null)) {
					for (Descripcion d : cd.getListDescripcion().getValue().getDescripcion()) {
						this.tiposTerminal.add(new SelectItem(d.getId().getValue(), d.getDescripcion().getValue()));
					}
				}
			}
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
					"Error en lectura de Tipo de Terminales: " + e.getMessage(), null));
		}

		return tiposTerminal;
	}

	public ArrayList<SelectItem> getTiposCliente() {
		if (this.tiposCliente == null) {
			this.tiposCliente = new ArrayList<SelectItem>();
			// Tipo Cliente: P Punto de Venta, D Distribuidor, M Mayorista
			this.tiposCliente.add(new SelectItem("M", "Mayorista"));
			this.tiposCliente.add(new SelectItem("D", "Distribuidor"));
			this.tiposCliente.add(new SelectItem("P", "Punto de Venta"));
		}

		return tiposCliente;
	}

	public Long getProvinciaFlt() {
		return provinciaFlt;
	}

	public void setProvinciaFlt(Long provinciaFlt) {
		this.provinciaFlt = provinciaFlt;
	}

	public Long getLocalidadFlt() {
		return localidadFlt;
	}

	public void setLocalidadFlt(Long localidadFlt) {
		this.localidadFlt = localidadFlt;
	}

	public Long getTipoTerminalFlt() {
		return tipoTerminalFlt;
	}

	public void setTipoTerminalFlt(Long tipoTerminalFlt) {
		this.tipoTerminalFlt = tipoTerminalFlt;
	}

	public String getTipoClienteFlt() {
		return tipoClienteFlt;
	}

	public void setTipoClienteFlt(String tipoClienteFlt) {
		this.tipoClienteFlt = tipoClienteFlt;
	}

	public Long getIdClienteFlt() {
		return idClienteFlt;
	}

	public void setIdClienteFlt(Long idClienteFlt) {
		this.idClienteFlt = idClienteFlt;
	}

	public Long getIdUsuarioFlt() {
		return idUsuarioFlt;
	}

	public void setIdUsuarioFlt(Long idUsuarioFlt) {
		this.idUsuarioFlt = idUsuarioFlt;
	}

	public String getIdAuxFlt() {
		return idAuxFlt;
	}

	public void setIdAuxFlt(String idAuxFlt) {
		this.idAuxFlt = idAuxFlt;
	}

	public String getIdAux2Flt() {
		return idAux2Flt;
	}

	public void setIdAux2Flt(String idAux2Flt) {
		this.idAux2Flt = idAux2Flt;
	}

	public Long getIdEntidadFlt() {
		return idEntidadFlt;
	}

	public void setIdEntidadFlt(Long idEntidadFlt) {
		this.idEntidadFlt = idEntidadFlt;
	}

	public String getRazonSocialFlt() {
		return razonSocialFlt;
	}

	public void setRazonSocialFlt(String razonSocialFlt) {
		this.razonSocialFlt = razonSocialFlt;
	}

	public String getNombreFantasiaFlt() {
		return nombreFantasiaFlt;
	}

	public void setNombreFantasiaFlt(String nombreFantasiaFlt) {
		this.nombreFantasiaFlt = nombreFantasiaFlt;
	}

	public String getUsuarioFlt() {
		return usuarioFlt;
	}

	public void setUsuarioFlt(String usuarioFlt) {
		this.usuarioFlt = usuarioFlt;
	}

	public String getCuitFlt() {
		return cuitFlt;
	}

	public void setCuitFlt(String cuitFlt) {
		this.cuitFlt = cuitFlt;
	}

	public boolean isMuestraConsultaCliente() {
		return muestraConsultaCliente;
	}

	public void consultarCliente() {
		this.muestraConsultaCliente = true;
		this.provinciaFlt = null;
		this.localidadFlt = null;
		this.tipoTerminalFlt = null;
		this.tipoClienteFlt = null;
		this.idClienteFlt = null;
		this.idUsuarioFlt = null;
		this.idAuxFlt = null;
		this.idAux2Flt = null;
		this.idEntidadFlt = null;
		this.razonSocialFlt = null;
		this.nombreFantasiaFlt = null;
		this.usuarioFlt = null;
		this.cuitFlt = null;

		PrimeFaces.current().ajax().update("consultaClienteDialog");
		PrimeFaces.current().executeScript("PF('consultaClienteDialogWV').show()");
	}

	public void cancelarBusquedaCliente() {
		this.muestraConsultaCliente = false;
		idCliente = null;
		PrimeFaces.current().executeScript("PF('consultaClienteDialogWV').hide();");
	}

	public void actualizarProvincia() {
		this.localidades = null;
		this.localidadFlt = null;
	}

	public void procesarBusquedaCliente() {
		ArrayList<String> lstUpdt = new ArrayList<String>();

		listResuBusCli = null;

		this.muestraConsultaCliente = false;
		this.muestraArbol = false;
		this.clienteSeleccionado = null;
		this.clienteSeleccionado1 = null;
		this.clienteSeleccionado2 = null;
		this.clienteSeleccionado3 = null;
		this.clienteSeleccionado4 = null;
		this.clienteSeleccionado5 = null;
		this.listClientesNivel1 = null;
		this.listClientesNivel2 = null;
		this.listClientesNivel3 = null;
		this.listClientesNivel4 = null;
		this.listClientesNivel5 = null;
		lstUpdt.add("menuSeleccionCliente");
		lstUpdt.add("contSelUsr");

		PrimeFaces.current().executeScript("PF('consultaClienteDialogWV').hide();PF('panelSelUsrWV').expand();");
		PrimeFaces.current().ajax().update(lstUpdt);
		PrimeFaces.current().executeScript("ArbolFCNS.arbolResize(-1);");
	}

	public LazyDataModel<ItemArbol> getListResuBusCli() {
		if (listResuBusCli == null) {
			if (this.tipoClienteFlt != null && this.tipoClienteFlt.trim().length() == 0)
				this.tipoClienteFlt = null;

			if (this.idAuxFlt != null && this.idAuxFlt.trim().length() == 0)
				this.idAuxFlt = null;

			if (this.idAux2Flt != null && this.idAux2Flt.trim().length() == 0)
				this.idAux2Flt = null;

			if (this.razonSocialFlt != null && this.razonSocialFlt.trim().length() == 0)
				this.razonSocialFlt = null;

			if (this.nombreFantasiaFlt != null && this.nombreFantasiaFlt.trim().length() == 0)
				this.nombreFantasiaFlt = null;

			if (this.usuarioFlt != null && this.usuarioFlt.trim().length() == 0)
				this.usuarioFlt = null;

			if (this.cuitFlt != null && this.cuitFlt.trim().length() == 0)
				this.cuitFlt = null;

			// Busco los subclientes
			listResuBusCli = new LazyBusquedaClienteDataModel(this.getUsuario().getIdCliente(),
					this.getUsuario().getIdMayorista(),
					((this.getUsuario().getNivelDistribuidorSuperior().compareTo(0) == 0) ? null
							: this.getUsuario().getNivelDistribuidorSuperior()),
					this.getProvinciaFlt(), this.getLocalidadFlt(), this.getTipoTerminalFlt(), this.getTipoClienteFlt(),
					this.getIdClienteFlt(), this.getIdUsuarioFlt(), this.getIdAuxFlt(), this.getIdAux2Flt(),
					this.getIdEntidadFlt(), this.getRazonSocialFlt(), this.getNombreFantasiaFlt(), this.getUsuarioFlt(),
					this.getCuitFlt(), "idCliente", "ASC");

			// this.getCampoOrdenamientoArbol(), this.getTipoOrdenArbol()
		}

		return listResuBusCli;
	}

	public void setListResuBusCli(LazyDataModel<ItemArbol> listResuBusCli) {
		this.listResuBusCli = listResuBusCli;
	}

	public void clearSeleccionCliente0() {
		String ejecucionCli = "";
		ArrayList<String> lstUpdt = new ArrayList<String>();

		this.clienteSeleccionado = null;
		this.clienteSeleccionado1 = null;
		this.clienteSeleccionado2 = null;
		this.clienteSeleccionado3 = null;
		this.clienteSeleccionado4 = null;
		this.clienteSeleccionado5 = null;
		this.listClientesNivel1 = null;
		this.listClientesNivel2 = null;
		this.listClientesNivel3 = null;
		this.listClientesNivel4 = null;
		this.listClientesNivel5 = null;

		seleccionCliente(this.getUsuario().getIdCliente());

		ejecucionCli = "ArbolFCNS.clearFilters(0);";
		ejecucionCli = ejecucionCli.concat("ArbolFCNS.arbolResize(0);");
		ejecucionCli = ejecucionCli.concat("ACGSiteScriptsFCNS.deshabilitaSeleccionMenu();");
		PrimeFaces.current().executeScript(ejecucionCli);

		// ------------------------------------------------------------------------------------------------
		// Si estaba seleccionada la opcion de busqueda directa no se ejecutaba el clear
		// filter porque no
		// se estaba mostrando el arbol, pero al ejecutar clear hace que si comience a
		// mostrarse
		this.muestraArbol = true;
		// ------------------------------------------------------------------------------------------------

		// ------------------------------------------------------------------------------------------------
		// Oculta panel de iconos
		// ------------------------------------------------------------------------------------------------
		this.muestraMnuIconos = false;
		lstUpdt.add("formMnuIconos");
		// ------------------------------------------------------------------------------------------------

		lstUpdt.add("contSelUsr");
		lstUpdt.add("menuSeleccionCliente");

		PrimeFaces.current().ajax().update(lstUpdt);
	}

	public Integer getIndiceMenu() {
		return indiceMenu;
	}

	public void setIndiceMenu(Integer indiceMenu) {
		this.indiceMenu = indiceMenu;
	}

	public boolean isMuestraMnuIconos() {
		return muestraMnuIconos;
	}

	public String getMnuOpcIconos() {
		return mnuOpcIconos;
	}

	public void setMnuOpcIconos(String mnuOpcIconos) {
		this.mnuOpcIconos = mnuOpcIconos;
	}

	public void activaMenuIconos(Integer opcTab) {
		ArrayList<String> lstUpdate = new ArrayList<String>();

		switch (opcTab) {
		case 0: {
			this.setIndiceMenu(opcTab);
			muestraMnuIconos = true;
			mnuOpcIconos = "/secure/administracion/fcnsAdministracion/iconosOpcAdministracion.xhtml";
			break;
		}
		case 6: {
			this.setIndiceMenu(opcTab);
			muestraMnuIconos = true;
			mnuOpcIconos = "/secure/administracion/fcnsGestionActivos/iconosOpcGestionActivos.xhtml";
			break;
		}
		case 7: {
			this.setIndiceMenu(opcTab);
			muestraMnuIconos = true;
			mnuOpcIconos = "/secure/administracion/fcnsProveedores/iconosOpcProveedores.xhtml";
			break;
		}
		default: {
			this.setIndiceMenu(opcTab);
			muestraMnuIconos = false;
			mnuOpcIconos = "";
			break;
		}
		}

		lstUpdate.add("formMnuIconos");
		lstUpdate.add("formMenuPpal");

		PrimeFaces.current().ajax().update(lstUpdate);
		PrimeFaces.current().executeScript("PF('panelSelUsrWV').collapse();ACGSiteScriptsFCNS.cierraPanelProducto();");
	}

	public String getOpcAIncluir() {
		return opcAIncluir;
	}

	public void navegarMenuIconos(Integer opcTab) {
		origen = "";
		FacesMessage msg = null;
		ArrayList<String> lstUpdate = new ArrayList<String>();

		switch (opcTab) {
		case 1: {
			this.listCabeceraUsuarios = null;

			if (this.clienteSeleccionado == null) {
				msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "No existe ningun cliente seleccionado.", null);

				PrimeFaces.current().dialog().showMessageDynamic(msg);

				return;
			}

			opcAIncluir = "/secure/administracion/fcnsUsuario/listadoUsuario.xhtml";
			auxiliarAIncluir = "/secure/administracion/fcnsUsuario/includeAuxiliares.xhtml";
			tituloPanelProducto = "Administracion de Usuarios";
			break;
		}
		case 3: {
			opcAIncluir = "/secure/administracion/fcnsReportes/reportes.xhtml";
			auxiliarAIncluir = "";
			tituloPanelProducto = "Reportes";
			break;
		}
		case 4: {
			this.publicidad = null;
			opcAIncluir = "/secure/shared/publicidad.xhtml";
			auxiliarAIncluir = "";
			tituloPanelProducto = "Publicidad";
			break;
		}
		default: {
			opcAIncluir = "";
			auxiliarAIncluir = "";
			tituloPanelProducto = "";
		}
		}

		// Opcion no tiene iconos, se deshabilita la visualización.
		// Setea la opcion del TAB seleccionado.
		muestraMnuIconos = false;
		this.setIndiceMenu(opcTab);

		lstUpdate.add("formMnuIconos");
		lstUpdate.add("formMenuPpal");
		lstUpdate.add("opcAIncluir");

		PrimeFaces.current().ajax().update(lstUpdate);
		PrimeFaces.current().executeScript("PF('panelProductosWV').show();");
	}

	public void navegarFcnsAdministracion(Integer opcNav) {
		origen = "";
		switch (opcNav) {
		case 1: {
			// Incrementar Saldo Mayorista
			opcAIncluir = "/secure/administracion/fcnsAdministracion/incrementoSaldoMayorista.xhtml";
			tituloPanelProducto = "Incremento Saldo Mayorista";
			break;
		}
		case 2: {
			// Cuenta Corriente
			opcAIncluir = "/secure/administracion/fcnsAdministracion/ctaCte.xhtml";
			tituloPanelProducto = "Cuenta Corriente";
			break;
		}
		case 3: {
			// Reparto de Saldo Western Union
			opcAIncluir = "/secure/administracion/fcnsAdministracion/repartoSaldoWU.xhtml";
			tituloPanelProducto = "Reparto de Saldo Western Union";
			break;
		}
		case 4: {
			// Reparto de Saldo al Punto
			opcAIncluir = "/secure/administracion/fcnsAdministracion/repartoSaldoAlPto.xhtml";
			tituloPanelProducto = "Reparto de Saldo al Punto";
			break;
		}
		case 5: {
			// Reparto Masivo de Saldo
			opcAIncluir = "/secure/administracion/fcnsAdministracion/repartoMasivoDeSaldo.xhtml";
			tituloPanelProducto = "Reparto Masivo de Saldo";
			break;
		}
		case 6: {
			// Informar Depositos y Solicitud de Adelantos
			opcAIncluir = "/secure/administracion/fcnsAdministracion/informarDepYSolDeAdel.xhtml";
			tituloPanelProducto = "Informar Depositos y Solicitud de Adelantos";
			break;
		}
		case 7: {
			// Acreditar Depositos o Adelanto de Saldo
			opcAIncluir = "/secure/administracion/fcnsAdministracion/acreditarDepOAdelDeSaldo.xhtml";
			tituloPanelProducto = "Acreditar Depositos o Adelanto de Saldo";
			break;
		}
		case 8: {
			// Acreditar Movimientos Externos
			opcAIncluir = "/secure/administracion/fcnsAdministracion/acreditarMovExt.xhtml";
			tituloPanelProducto = "Acreditar Movimientos Externos";
			break;
		}
		case 9: {
			// Productos Y Comisiones
			opcAIncluir = "/secure/administracion/fcnsAdministracion/productosYComisiones.xhtml";
			tituloPanelProducto = "Productos y Comisiones";
			break;
		}
		case 10: {
			// Cargas Masivas
			opcAIncluir = "/secure/administracion/fcnsAdministracion/cargasMasivas.xhtml";
			tituloPanelProducto = "Cargas Masivas";
			break;
		}
		default: {
			opcAIncluir = "";
			auxiliarAIncluir = "";
			tituloPanelProducto = "";
		}
		}

		PrimeFaces.current().ajax().update("opcAIncluir");
	}

	public void navegarFcnsMensajes(Integer opcNav) {
		origen = "";
		switch (opcNav) {
		case 1: {
			// TODO: Esto es un parche, ver bien si esta publicidad se incluira en
			// administracion o no !!!!
			// Publicidad Web
			this.publicidad = null;
			opcAIncluir = "/secure/shared/publicidad.xhtml";
			auxiliarAIncluir = "";
			tituloPanelProducto = "Publicidad Web";
			break;
		}
		case 2: {
			break;
		}
		case 3: {
			break;
		}
		case 4: {
			break;
		}
		case 5: {
			break;
		}
		case 6: {
			break;
		}
		case 7: {
			break;
		}
		case 8: {
			break;
		}
		default: {
			opcAIncluir = "";
			auxiliarAIncluir = "";
			tituloPanelProducto = "";
		}
		}

		PrimeFaces.current().ajax().update("opcAIncluir");
	}

	public void navegarFcnsGestionActivos(Integer opcNav) {
		origen = "";
		switch (opcNav) {
		case 1: {
			// Gestión de Terminales
			opcAIncluir = "/secure/administracion/fcnsGestionActivos/gestionTerminales.xhtml";
			tituloPanelProducto = "Gestión de Terminales";
			break;
		}
		case 2: {
			// Gestion de LGs de SUBE
			opcAIncluir = "/secure/administracion/fcnsGestionActivos/gestionLGsSUBE.xhtml";
			tituloPanelProducto = "Gestion de LGs de SUBE";
			break;
		}
		case 3: {
			// Gestion de SIMS
			opcAIncluir = "/secure/administracion/fcnsGestionActivos/gestionSIMS.xhtml";
			tituloPanelProducto = "Gestion de SIMS";
			break;
		}
		default: {
			opcAIncluir = "";
			auxiliarAIncluir = "";
			tituloPanelProducto = "";
		}
		}

		PrimeFaces.current().ajax().update("opcAIncluir");
	}

	public void navegarFcnsProveedores(Integer opcNav) {
		origen = "";
		switch (opcNav) {
		case 1: {
			// Administración Proveedores/Productos y Alarmas de Saldo
			opcAIncluir = "/secure/administracion/fcnsProveedores/administracionProvProdYAlarmaSaldo.xhtml";
			tituloPanelProducto = "Administración Proveedores/Productos y Alarmas de Saldo";
			break;
		}
		case 2: {
			// Administración de Proveedores por Producto y Características
			opcAIncluir = "/secure/administracion/fcnsProveedores/administracionProvPorProdYCaracteristicas.xhtml";
			tituloPanelProducto = "Administración de Proveedores por Producto y Características";
			break;
		}
		case 3: {
			// Compra de Saldo a Proveedores
			opcAIncluir = "/secure/administracion/fcnsProveedores/compraSaldoAProveedores.xhtml";
			tituloPanelProducto = "Compra de Saldo a Proveedores";
			break;
		}
		case 4: {
			// Conciliación Proveedores
			opcAIncluir = "/secure/administracion/fcnsProveedores/conciliacionProveedores.xhtml";
			tituloPanelProducto = "Conciliación Proveedores";
			break;
		}
		default: {
			opcAIncluir = "";
			auxiliarAIncluir = "";
			tituloPanelProducto = "";
		}
		}

		PrimeFaces.current().ajax().update("opcAIncluir");
	}

	private void generarModeloColumnasN1() {
		ColumnModel oCol = null;

		columnasNivel1 = new ArrayList<ColumnModel>();

		if (this.getCampoOrdenamientoArbolN1() == null
				|| (this.getCampoOrdenamientoArbolN1() != null && this.getCampoOrdenamientoArbolN1().isEmpty()))
			oCol = new ColumnModel("", "idCliente", "N");
		else
			oCol = new ColumnModel("", this.getCampoOrdenamientoArbolN1(),
					("idCliente".equals(this.getCampoOrdenamientoArbolN1()) ? "N" : "S"));

		columnasNivel1.add(oCol);
	}

	public List<ColumnModel> getColumnasNivel1() {
		if (columnasNivel1 == null || (columnasNivel1 != null && columnasNivel1.isEmpty())) {
			generarModeloColumnasN1();
		}

		return columnasNivel1;
	}

	public void setColumnasNivel1(List<ColumnModel> columnasNivel1) {
		this.columnasNivel1 = columnasNivel1;
	}

	private void generarModeloColumnasN2() {
		ColumnModel oCol = null;

		columnasNivel2 = new ArrayList<ColumnModel>();

		if (this.getCampoOrdenamientoArbolN2() == null
				|| (this.getCampoOrdenamientoArbolN2() != null && this.getCampoOrdenamientoArbolN2().isEmpty()))
			oCol = new ColumnModel("", "idCliente", "N");
		else
			oCol = new ColumnModel("", this.getCampoOrdenamientoArbolN2(),
					("idCliente".equals(this.getCampoOrdenamientoArbolN2()) ? "N" : "S"));

		columnasNivel2.add(oCol);
	}

	public List<ColumnModel> getColumnasNivel2() {
		if (columnasNivel2 == null || (columnasNivel2 != null && columnasNivel2.isEmpty())) {
			generarModeloColumnasN2();
		}

		return columnasNivel2;
	}

	public void setColumnasNivel2(List<ColumnModel> columnasNivel2) {
		this.columnasNivel2 = columnasNivel2;
	}

	private void generarModeloColumnasN3() {
		ColumnModel oCol = null;

		columnasNivel3 = new ArrayList<ColumnModel>();

		if (this.getCampoOrdenamientoArbolN3() == null
				|| (this.getCampoOrdenamientoArbolN3() != null && this.getCampoOrdenamientoArbolN3().isEmpty()))
			oCol = new ColumnModel("", "idCliente", "N");
		else
			oCol = new ColumnModel("", this.getCampoOrdenamientoArbolN3(),
					("idCliente".equals(this.getCampoOrdenamientoArbolN3()) ? "N" : "S"));

		columnasNivel3.add(oCol);
	}

	public List<ColumnModel> getColumnasNivel3() {
		if (columnasNivel3 == null || (columnasNivel3 != null && columnasNivel3.isEmpty())) {
			generarModeloColumnasN3();
		}

		return columnasNivel3;
	}

	public void setColumnasNivel3(List<ColumnModel> columnasNivel3) {
		this.columnasNivel3 = columnasNivel3;
	}

	private void generarModeloColumnasN4() {
		ColumnModel oCol = null;

		columnasNivel4 = new ArrayList<ColumnModel>();

		if (this.getCampoOrdenamientoArbolN4() == null
				|| (this.getCampoOrdenamientoArbolN4() != null && this.getCampoOrdenamientoArbolN4().isEmpty()))
			oCol = new ColumnModel("", "idCliente", "N");
		else
			oCol = new ColumnModel("", this.getCampoOrdenamientoArbolN4(),
					("idCliente".equals(this.getCampoOrdenamientoArbolN4()) ? "N" : "S"));

		columnasNivel4.add(oCol);
	}

	public List<ColumnModel> getColumnasNivel4() {
		if (columnasNivel4 == null || (columnasNivel4 != null && columnasNivel4.isEmpty())) {
			generarModeloColumnasN4();
		}

		return columnasNivel4;
	}

	public void setColumnasNivel4(List<ColumnModel> columnasNivel4) {
		this.columnasNivel4 = columnasNivel4;
	}

	private void generarModeloColumnasN5() {
		ColumnModel oCol = null;

		columnasNivel5 = new ArrayList<ColumnModel>();

		if (this.getCampoOrdenamientoArbolN5() == null
				|| (this.getCampoOrdenamientoArbolN5() != null && this.getCampoOrdenamientoArbolN5().isEmpty()))
			oCol = new ColumnModel("", "idCliente", "N");
		else
			oCol = new ColumnModel("", this.getCampoOrdenamientoArbolN5(),
					("idCliente".equals(this.getCampoOrdenamientoArbolN5()) ? "N" : "S"));

		columnasNivel5.add(oCol);
	}

	public List<ColumnModel> getColumnasNivel5() {
		if (columnasNivel5 == null || (columnasNivel5 != null && columnasNivel5.isEmpty())) {
			generarModeloColumnasN5();
		}

		return columnasNivel5;
	}

	public void setColumnasNivel5(List<ColumnModel> columnasNivel5) {
		this.columnasNivel5 = columnasNivel5;
	}

	public void actualizarCampoOrdenamientoArbolN1(String columna) {
		inicializarSeleccion(1);

		this.setCampoOrdenamientoArbolN1(columna);
		generarModeloColumnasN1();
	}

	public void actualizarCampoOrdenamientoArbolN2(String columna) {
		inicializarSeleccion(2);

		this.setCampoOrdenamientoArbolN2(columna);
		generarModeloColumnasN2();
	}

	public void actualizarCampoOrdenamientoArbolN3(String columna) {
		inicializarSeleccion(3);

		this.setCampoOrdenamientoArbolN3(columna);
		generarModeloColumnasN3();
	}

	public void actualizarCampoOrdenamientoArbolN4(String columna) {
		inicializarSeleccion(4);

		this.setCampoOrdenamientoArbolN4(columna);
		generarModeloColumnasN4();
	}

	public void actualizarCampoOrdenamientoArbolN5(String columna) {
		inicializarSeleccion(6);

		this.setCampoOrdenamientoArbolN5(columna);
		generarModeloColumnasN5();
	}

	public String obtenerCampoOrdenamientoArbolTitle(Integer nivel, String tipoSalida) {
		String campoOrden = "";
		String textoOrden = "";

		textoOrden = ("R".equals(tipoSalida) ? "" : "Filtro y Ordenamiento: ");

		if (nivel == 1) {
			if ("IDCLIENTE".equals(this.getCampoOrdenamientoArbolN1().toUpperCase().trim())) {
				campoOrden = ("R".equals(tipoSalida) ? "Id: " : "Id.Cliente");
			} else if ("RAZONSOCIAL".equals(this.getCampoOrdenamientoArbolN1().toUpperCase().trim())) {
				campoOrden = ("R".equals(tipoSalida) ? "RS: " : "Razon Social");
			} else if ("NOMBREFANTASIA".equals(this.getCampoOrdenamientoArbolN1().toUpperCase().trim())) {
				campoOrden = ("R".equals(tipoSalida) ? "NF: " : "Nombre Fantasia");
			} else {
				campoOrden = ("R".equals(tipoSalida) ? "NA: " : "NO Asignado");
			}
		} else if (nivel == 2) {
			if ("IDCLIENTE".equals(this.getCampoOrdenamientoArbolN2().toUpperCase().trim())) {
				campoOrden = ("R".equals(tipoSalida) ? "Id: " : "Id.Cliente");
			} else if ("RAZONSOCIAL".equals(this.getCampoOrdenamientoArbolN2().toUpperCase().trim())) {
				campoOrden = ("R".equals(tipoSalida) ? "RS: " : "Razon Social");
			} else if ("NOMBREFANTASIA".equals(this.getCampoOrdenamientoArbolN2().toUpperCase().trim())) {
				campoOrden = ("R".equals(tipoSalida) ? "NF: " : "Nombre Fantasia");
			} else {
				campoOrden = ("R".equals(tipoSalida) ? "NA: " : "NO Asignado");
			}
		} else if (nivel == 3) {
			if ("IDCLIENTE".equals(this.getCampoOrdenamientoArbolN3().toUpperCase().trim())) {
				campoOrden = ("R".equals(tipoSalida) ? "Id: " : "Id.Cliente");
			} else if ("RAZONSOCIAL".equals(this.getCampoOrdenamientoArbolN3().toUpperCase().trim())) {
				campoOrden = ("R".equals(tipoSalida) ? "RS: " : "Razon Social");
			} else if ("NOMBREFANTASIA".equals(this.getCampoOrdenamientoArbolN3().toUpperCase().trim())) {
				campoOrden = ("R".equals(tipoSalida) ? "NF: " : "Nombre Fantasia");
			} else {
				campoOrden = ("R".equals(tipoSalida) ? "NA: " : "NO Asignado");
			}
		} else if (nivel == 4) {
			if ("IDCLIENTE".equals(this.getCampoOrdenamientoArbolN4().toUpperCase().trim())) {
				campoOrden = ("R".equals(tipoSalida) ? "Id: " : "Id.Cliente");
			} else if ("RAZONSOCIAL".equals(this.getCampoOrdenamientoArbolN4().toUpperCase().trim())) {
				campoOrden = ("R".equals(tipoSalida) ? "RS: " : "Razon Social");
			} else if ("NOMBREFANTASIA".equals(this.getCampoOrdenamientoArbolN4().toUpperCase().trim())) {
				campoOrden = ("R".equals(tipoSalida) ? "NF: " : "Nombre Fantasia");
			} else {
				campoOrden = ("R".equals(tipoSalida) ? "NA: " : "NO Asignado");
			}
		} else if (nivel == 5) {
			if ("IDCLIENTE".equals(this.getCampoOrdenamientoArbolN5().toUpperCase().trim())) {
				campoOrden = ("R".equals(tipoSalida) ? "Id: " : "Id.Cliente");
			} else if ("RAZONSOCIAL".equals(this.getCampoOrdenamientoArbolN5().toUpperCase().trim())) {
				campoOrden = ("R".equals(tipoSalida) ? "RS: " : "Razon Social");
			} else if ("NOMBREFANTASIA".equals(this.getCampoOrdenamientoArbolN5().toUpperCase().trim())) {
				campoOrden = ("R".equals(tipoSalida) ? "NF: " : "Nombre Fantasia");
			} else {
				campoOrden = ("R".equals(tipoSalida) ? "NA: " : "NO Asignado");
			}
		}

		return textoOrden.concat(campoOrden);
	}

	public boolean isMuestraArbol() {
		return muestraArbol;
	}

	public void setMuestraArbol(boolean muestraArbol) {
		this.muestraArbol = muestraArbol;
	}

	public String obtenerPatron(Integer nivel) {
		String campoOrdenamientoArbol = "";
		String patron = "/[\\d\\-]/";

		if (nivel == 1) {
			campoOrdenamientoArbol = this.getCampoOrdenamientoArbolN1().toUpperCase().trim();
		} else if (nivel == 2) {
			campoOrdenamientoArbol = this.getCampoOrdenamientoArbolN2().toUpperCase().trim();
		} else if (nivel == 3) {
			campoOrdenamientoArbol = this.getCampoOrdenamientoArbolN3().toUpperCase().trim();
		} else if (nivel == 4) {
			campoOrdenamientoArbol = this.getCampoOrdenamientoArbolN4().toUpperCase().trim();
		} else if (nivel == 5) {
			campoOrdenamientoArbol = this.getCampoOrdenamientoArbolN5().toUpperCase().trim();
		}

		if ("IDCLIENTE".equals(campoOrdenamientoArbol)) {
			patron = "/[\\d\\-]/";
		} else {
			patron = "/[a-zA-Z0-9_]/i";
		}
		return patron;

	}

	public String getTituloPanelProducto() {
		return tituloPanelProducto;
	}

	public LazyDataModel<CabeceraUsuario> getListCabeceraUsuarios() {
		if (listCabeceraUsuarios == null) {
			// Busco los subclientes
			listCabeceraUsuarios = new LazyCabeceraUsuarioDataModel(
					this.getClienteSeleccionado().getIdCliente().getValue(),
					this.getClienteSeleccionado().getIdMayorista().getValue(),
					this.getUsuario().getNivelDistribuidorSuperior(), "idUsuario", "ASC");

		}

		return listCabeceraUsuarios;
	}

	public void setListCabeceraUsuarios(LazyDataModel<CabeceraUsuario> listCabeceraUsuarios) {
		this.listCabeceraUsuarios = listCabeceraUsuarios;
	}

	public CabeceraUsuario getCabeceraUsuario() {
		return cabeceraUsuario;
	}

	public void setCabeceraUsuario(CabeceraUsuario cabeceraUsuario) {
		this.cabeceraUsuario = cabeceraUsuario;
	}

	public ArrayList<SelectItem> getEstadosCliente() {
		if (this.estadosCliente == null) {
			this.estadosCliente = new ArrayList<SelectItem>();
			// Estados Cliente: A Activo B Inactivo
			this.estadosCliente.add(new SelectItem("A", "Activo"));
			this.estadosCliente.add(new SelectItem("B", "Inactivo"));
		}

		return estadosCliente;
	}

	public void confirmarEliminaUsuario() {
		Long idUsuario = Long.parseLong(
				FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("idUsuario"));

		usuarioRowIndex = Integer.parseInt(
				FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("usuarioRowIndex"));

		Usuario oUsr = new Usuario();

		ObjectFactory objectFactory = new ObjectFactory();
		JAXBElement<Long> jaxbElement = objectFactory.createUsuarioIdUsuario(idUsuario);

		oUsr.setIdUsuario(jaxbElement);
		this.usuarioSeleccionado = oUsr;

		PrimeFaces.current().executeScript("PF('confirmaEliminacionDialogWV').show();");
	}

	public void eliminarUsuario() {
		ArrayList<String> lstUpdt = new ArrayList<String>();
		FacesMessage msg = null;

		if (GestionServiceHelper.getGestionService(CfgTimeout.DEFAULT).eliminarUsuario(
				this.getClienteSeleccionado().getIdMayorista().getValue(),
				this.getUsuarioSeleccionado().getIdUsuario().getValue())) {
			msg = new FacesMessage(FacesMessage.SEVERITY_INFO,
					"Usuario: |" + this.getUsuarioSeleccionado().getIdUsuario().getValue().toString()
							+ "| dado de baja exitosamente.",
					null);

			this.getListCabeceraUsuarios()
					.getRowData(this.getUsuarioSeleccionado().getIdUsuario().getValue().toString()).setEstado("B");

			lstUpdt.add("usuariosAdminDT:" + usuarioRowIndex + ":usuarioEstado");
			lstUpdt.add("usuariosAdminDT:" + usuarioRowIndex + ":usuarioEliminar");

			PrimeFaces.current().executeScript("PF('confirmaEliminacionDialogWV').hide();");
		} else {
			msg = new FacesMessage(FacesMessage.SEVERITY_ERROR,
					"Error dando de baja Usuario: |" + this.getCabeceraUsuario().getIdUsuario().toString() + "|", null);
		}

		PrimeFaces.current().dialog().showMessageDynamic(msg);
		PrimeFaces.current().ajax().update(lstUpdt);
	}

	public void abreDialogoNuevoUsuario() {
		this.muestraCRUDUsuario = true;

		// Seteo operacion para ALTA
		this.operacion = OperacionEnEjecucion.ALTA;

		ObjectFactory objectFactory = new ObjectFactory();

		usuarioSeleccionado = new Usuario();
		usuarioSeleccionado
				.setCambiarClaveEnProximoIngreso(objectFactory.createUsuarioCambiarClaveEnProximoIngreso(true));
		usuarioSeleccionado.setIdMayorista(this.getClienteSeleccionado().getIdMayorista());
		usuarioSeleccionado.setIdCliente(this.getClienteSeleccionado().getIdCliente());
		usuarioSeleccionado.setNombre(objectFactory.createUsuarioNombre(""));
		usuarioSeleccionado.setApellido(objectFactory.createUsuarioApellido(""));
		usuarioSeleccionado.setNumeroDocumento(objectFactory.createUsuarioNumeroDocumento(0));
		usuarioSeleccionado.setUsuario(objectFactory.createUsuarioUsuario(""));
		usuarioSeleccionado.setClave(objectFactory.createUsuarioClave(""));
		usuarioSeleccionado.setIdTipoDocumento(objectFactory.createUsuarioIdTipoDocumento(null));
		usuarioSeleccionado.setTelefono(objectFactory.createUsuarioTelefono(""));
		usuarioSeleccionado.setCelular(objectFactory.createUsuarioCelular(""));
		usuarioSeleccionado.setMail(objectFactory.createUsuarioMail(""));
		usuarioSeleccionado.setEstado(objectFactory.createUsuarioEstado(""));
		usuarioSeleccionado.setClaveBloqueada(objectFactory.createUsuarioClaveBloqueada(false));
		usuarioSeleccionado
				.setCambiarClaveEnProximoIngreso(objectFactory.createUsuarioCambiarClaveEnProximoIngreso(true));
		usuarioSeleccionado.setUsarTarjetaCoordenadas(objectFactory.createUsuarioUsarTarjetaCoordenadas(false));
		usuarioSeleccionado.setSoloInformes(objectFactory.createUsuarioSoloInformes(false));

		PrimeFaces.current().ajax().update("crudUsuarioDialog");
		PrimeFaces.current().executeScript("PF('crudUsuarioDialogWV').show();");
	}

	public void abreDialogoModificacionUsuario() {
		this.muestraCRUDUsuario = true;

		// Seteo operacion para EDICION
		this.operacion = OperacionEnEjecucion.EDICION;

		Long idUsuario = Long.parseLong(
				FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("idUsuario"));
		usuarioRowIndex = Integer.parseInt(
				FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("usuarioRowIndex"));
		// System.out.println("Cabecera Usuario: " + idUsuario);

		usuarioSeleccionado = GestionServiceHelper.getGestionService(CfgTimeout.DEFAULT)
				.mostrarUsuario(this.getClienteSeleccionado().getIdMayorista().getValue(), idUsuario);

		PrimeFaces.current().ajax().update("crudUsuarioDialog");
		PrimeFaces.current().executeScript("PF('crudUsuarioDialogWV').show();");
	}

	public void cierraDialogoModificacionUsuario() {
		this.muestraCRUDUsuario = false;
		this.setCabeceraUsuario(null);
		PrimeFaces.current().ajax().update("crudUsuarioDialog");
		PrimeFaces.current().executeScript("PF('crudUsuarioDialogWV').hide();");
	}

	public Usuario getUsuarioSeleccionado() {
		return usuarioSeleccionado;
	}

	public void setUsuarioSeleccionado(Usuario usuarioSeleccionado) {
		this.usuarioSeleccionado = usuarioSeleccionado;
	}

	public boolean isMuestraCRUDUsuario() {
		return muestraCRUDUsuario;
	}

	public String getAuxiliarAIncluir() {
		return auxiliarAIncluir;
	}

	public void generarTarjetaCoordenadas() {
		FacesMessage msg = null;

		try {
			ResultadoBase resp = GestionServiceHelper.getGestionService(CfgTimeout.DEFAULT).generarTarjetaCoordenadas(
					this.getUsuario().getIdMayorista(), this.getUsuarioSeleccionado().getUsuario().getValue(),
					this.getUsuario().getIdUsuario(), this.getUsuario().getPassword());

			if (resp.getError().getValue().getHayError().getValue()) {
				msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error generando tarjeta de coordenadas: |"
						+ resp.getError().getValue().getMsgError().getValue() + "|", null);
				FacesContext.getCurrentInstance().validationFailed();
			} else {
				msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Tarjeta de Coordenadas generada OK", null);
			}

		} catch (Exception e) {
			msg = new FacesMessage(FacesMessage.SEVERITY_FATAL, "Error generando Tarjeta de Coordenadas", null);
			FacesContext.getCurrentInstance().validationFailed();
		}

		PrimeFaces.current().dialog().showMessageDynamic(msg);
	}

	public void guardarModificacionUsuario() {
		FacesMessage msg = null;
		ResultadoBase resp = null;
		ArrayList<String> lstUpdt = new ArrayList<String>();

		try {
			/*
			 * ObjectFactory objectFactory = new ObjectFactory(); JAXBElement<Integer>
			 * jaxbElement = objectFactory.createUsuarioNumeroDocumento(99);
			 * 
			 * this.getUsuarioSeleccionado().setNumeroDocumento(jaxbElement);
			 */
			resp = GestionServiceHelper.getGestionService(CfgTimeout.DEFAULT).agregarModificarUsuario(
					this.getUsuario().getIdMayorista(), this.getUsuario().getIdCliente(),
					this.getUsuario().getIdUsuario(), this.getUsuario().getUsername(), this.getUsuarioSeleccionado(),
					false, true);

			if (this.getUsuario().getIdUsuario() != null) {

				this.getListCabeceraUsuarios()
						.getRowData(this.getUsuarioSeleccionado().getIdUsuario().getValue().toString())
						.setNombre(this.getUsuarioSeleccionado().getNombre().getValue());
				this.getListCabeceraUsuarios()
						.getRowData(this.getUsuarioSeleccionado().getIdUsuario().getValue().toString())
						.setApellido(this.getUsuarioSeleccionado().getApellido().getValue());
				this.getListCabeceraUsuarios()
						.getRowData(this.getUsuarioSeleccionado().getIdUsuario().getValue().toString())
						.setEstado(this.getUsuarioSeleccionado().getEstado().getValue());
				this.getListCabeceraUsuarios()
						.getRowData(this.getUsuarioSeleccionado().getIdUsuario().getValue().toString())
						.setSoloInformes(this.getUsuarioSeleccionado().getSoloInformes().getValue());

				// usuario a modificar
				lstUpdt.add("usuariosAdminDT:" + usuarioRowIndex + ":usuarioNombre");
				lstUpdt.add("usuariosAdminDT:" + usuarioRowIndex + ":usuarioApellido");
				lstUpdt.add("usuariosAdminDT:" + usuarioRowIndex + ":usuarioEstado");
				lstUpdt.add("usuariosAdminDT:" + usuarioRowIndex + ":usuarioInformes");
				lstUpdt.add("usuariosAdminDT:" + usuarioRowIndex + ":usuarioModificar");
				lstUpdt.add("usuariosAdminDT:" + usuarioRowIndex + ":usuarioEliminar");

			} else {
				// usuario a dar de alta
				lstUpdt.add("usuariosAdminDT");
			}

		} catch (Exception e) {
			msg = new FacesMessage(FacesMessage.SEVERITY_FATAL, e.getMessage(), null);

			PrimeFaces.current().dialog().showMessageDynamic(msg);
			FacesContext.getCurrentInstance().validationFailed();
			return;
		}

		if (!resp.getError().getValue().getHayError().getValue()) {
			msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Usuario guardado exitosamente.", null);

			/*
			 * if(this.getUsuarioSeleccionado().getIdUsuario() == null){ desactivarAceptar =
			 * Boolean.TRUE; }else{ desactivarAceptar = Boolean.FALSE; }
			 */

			if ((!"!%&)#?!=!¿¡!/(?".equals(this.getUsuarioSeleccionado().getClave().getValue())) && (this.getUsuario()
					.getUsername().equals(this.getUsuarioSeleccionado().getUsuario().getValue()))) {
				msg = new FacesMessage(FacesMessage.SEVERITY_INFO,
						"Por favor salga del sistema y vuelva a ingresar para poder operar", null);

				// login.setCambiarClaveEnProximoIngreso(0);
			}

			// desactivarUsuario = Boolean.TRUE;
		} else {
			msg = new FacesMessage(FacesMessage.SEVERITY_INFO,
					"Error guardando Usuario: " + resp.getError().getValue().getMsgError().getValue(), null);
			FacesContext.getCurrentInstance().validationFailed();
		}

		PrimeFaces.current().dialog().showMessageDynamic(msg);

		// Refresco listado
		// this.setListCabeceraUsuarios(null);

		// PrimeFaces.current().ajax().update("usuariosAdminDT");

		PrimeFaces.current().ajax().update(lstUpdt);
	}

	public void onFilterUsuarios(FilterEvent filterEvent) {
		System.out.println("Entro por filtrado");
	}

	private void cerrarIconosYPantallaProducto(ArrayList<String> lstUpdt) {
		boolean listaNoInformada = false;
		this.muestraMnuIconos = false;

		if (lstUpdt == null) {
			lstUpdt = new ArrayList<String>();
			listaNoInformada = true;
		}

		lstUpdt.add("formMnuIconos");

		if (listaNoInformada)
			PrimeFaces.current().ajax().update(lstUpdt);

		PrimeFaces.current().executeScript("ACGSiteScriptsFCNS.cierraPanelProducto();");
	}

	public void init() {
		Cliente oCli = null;
		ObjectFactory objectFactory = null;
		JAXBElement<Long> idLongJax = null;
		inicializaListasYSeleccionesArbol();
		this.cabeceraUsuario = null;
		this.usuarioSeleccionado = null;
		this.clienteSeleccionado = null;

		oCli = new Cliente();
		objectFactory = new ObjectFactory();
		idLongJax = objectFactory.createUsuarioIdUsuario(this.getUsuario().getIdCliente());

		oCli.setIdCliente(idLongJax);

		idLongJax = objectFactory.createUsuarioIdUsuario(this.getUsuario().getIdMayorista());

		oCli.setIdMayorista(idLongJax);

		this.setClienteSeleccionado(oCli);

		this.opcAIncluir = "";
		this.auxiliarAIncluir = "";
		this.tituloPanelProducto = "";
		this.muestraMnuIconos = false;
		this.mnuOpcIconos = "";
		this.indiceMenu = -1;
	}

	public OperacionEnEjecucion getOperacion() {
		return operacion;
	}

	public void navegarFcnsReportes(Integer opcNav) {

		origen = "REPORTES";

		auxiliarAIncluir = "/secure/shared/reportes/includeAuxiliaresReportes.xhtml";
		switch (opcNav) {
		case 1: {
			// Comisiones
			FacesContext facesContext = FacesContext.getCurrentInstance();
			ReporteComisionesView pv = (ReporteComisionesView) facesContext.getApplication().getELResolver()
					.getValue(facesContext.getELContext(), null, "reporteComisionesView");
			pv.resetearReporte();

			opcAIncluir = "/secure/shared/reportes/informeComisiones.xhtml";
			tituloPanelProducto = "Informe de Comisiones";

			break;
		}
		case 2: {

			// Transacciones
			FacesContext facesContext = FacesContext.getCurrentInstance();
			ReporteTrxView pv = (ReporteTrxView) facesContext.getApplication().getELResolver()
					.getValue(facesContext.getELContext(), null, "reporteTrxView");
			pv.resetearReporte();

			opcAIncluir = "/secure/shared/reportes/informeTrx.xhtml";
			tituloPanelProducto = "Informe de Transacciones";

			break;
		}
		case 3: {

			// Stock de Pines
			FacesContext facesContext = FacesContext.getCurrentInstance();
			ReportePINView pv = (ReportePINView) facesContext.getApplication().getELResolver()
					.getValue(facesContext.getELContext(), null, "reportePINView");
			pv.resetearReporte();

			opcAIncluir = "/secure/shared/reportes/informePines.xhtml";
			tituloPanelProducto = "Informe Stock de Pines";

			break;
		}
		case 4: {
			// Reparto de Saldos

			FacesContext facesContext = FacesContext.getCurrentInstance();
			ReporteRepartosView pv = (ReporteRepartosView) facesContext.getApplication().getELResolver()
					.getValue(facesContext.getELContext(), null, "reporteRepartosView");
			pv.resetearReporte();
			// pv.setIdClienteSeleccionado(this.usuariologin)
			opcAIncluir = "/secure/shared/reportes/informeRepartos.xhtml";
			tituloPanelProducto = "Informe de Repartos";

			break;
		}
		case 5: {
			// Compra Prov

			FacesContext facesContext = FacesContext.getCurrentInstance();
			ReporteCompraSaldoProveedorView pv = (ReporteCompraSaldoProveedorView) facesContext.getApplication()
					.getELResolver().getValue(facesContext.getELContext(), null, "reporteCompraSaldoProveedorView");
			pv.resetearReporte();
			opcAIncluir = "/secure/shared/reportes/informeCompraSaldoProveedor.xhtml";
			tituloPanelProducto = "Reporte Compra Saldo a Proveedor";

			break;
		}
		case 6: {
			// Resumen TRXs

			FacesContext facesContext = FacesContext.getCurrentInstance();
			ReporteTrxRedView pv = (ReporteTrxRedView) facesContext.getApplication().getELResolver()
					.getValue(facesContext.getELContext(), null, "reporteTrxRedView");
			pv.resetearReporte();

			opcAIncluir = "/secure/shared/reportes/informeTrxRed.xhtml";
			tituloPanelProducto = "Resumen Transacciones";

			break;
		}
		case 7: {
			// Depositos y Adelantos
			FacesContext facesContext = FacesContext.getCurrentInstance();
			ReporteDepAdelView pv = (ReporteDepAdelView) facesContext.getApplication().getELResolver()
					.getValue(facesContext.getELContext(), null, "reporteDepAdelView");
			pv.resetearReporte();

			opcAIncluir = "/secure/shared/reportes/informeDepAdel.xhtml";
			tituloPanelProducto = "Informe de Depositos y Adelantos";

			break;
		}
		case 8: {
			// Clientes, Saldos y GeoLocalizacion
			FacesContext facesContext = FacesContext.getCurrentInstance();
			ReporteSaldoClientesView pv = (ReporteSaldoClientesView) facesContext.getApplication().getELResolver()
					.getValue(facesContext.getELContext(), null, "reporteSaldoClientesView");
			pv.resetearReporte();

			opcAIncluir = "/secure/shared/reportes/informeSaldoClientes.xhtml";
			auxiliarAIncluir = "/secure/shared/reportes/includeAuxiliaresInformeSaldoClientes.xhtml";			
			tituloPanelProducto = "Reporte Saldo Clientes";

			break;
		}
		case 9: {
			// Inc. Saldo Mayorista
			FacesContext facesContext = FacesContext.getCurrentInstance();
			ReporteIncrementoSaldoMayoristaView pv = (ReporteIncrementoSaldoMayoristaView) facesContext.getApplication()
					.getELResolver().getValue(facesContext.getELContext(), null, "reporteIncrementoSaldoMayoristaView");
			pv.resetearReporte();

			opcAIncluir = "/secure/shared/reportes/informeIncrementoSaldoMayorista.xhtml";
			tituloPanelProducto = "Reporte Incremento Saldo Mayorista";

			break;
		}
		case 10: {
			// Movimientos

			FacesContext facesContext = FacesContext.getCurrentInstance();
			ReporteCuentaCorrienteView pv = (ReporteCuentaCorrienteView) facesContext.getApplication().getELResolver()
					.getValue(facesContext.getELContext(), null, "reporteCuentaCorrienteView");
			pv.resetearReporte();
			opcAIncluir = "/secure/shared/reportes/informeMovimientos.xhtml";
			tituloPanelProducto = "Informe de Movimientos";

			break;
		}
		case 11: {
			// Auditorias
			FacesContext facesContext = FacesContext.getCurrentInstance();
			ReporteAuditoriasView pv = (ReporteAuditoriasView) facesContext.getApplication().getELResolver()
					.getValue(facesContext.getELContext(), null, "reporteAuditoriasView");
			pv.resetearReporte();

			opcAIncluir = "/secure/shared/reportes/informeAuditorias.xhtml";
			tituloPanelProducto = "Reporte de Auditorias";

			break;
		}
		case 12: {
			// Cuenta Corriente
			FacesContext facesContext = FacesContext.getCurrentInstance();
			ReporteCCNView pv = (ReporteCCNView) facesContext.getApplication().getELResolver()
					.getValue(facesContext.getELContext(), null, "reporteCCNView");
			pv.resetearReporte();

			opcAIncluir = "/secure/shared/reportes/informeCCN.xhtml";
			tituloPanelProducto = "Informe de Cuenta Corriente";

			break;
		}
		case 13: {
			// Ids Puntos Venta
			FacesContext facesContext = FacesContext.getCurrentInstance();
			ReporteIdsUnicosView pv = (ReporteIdsUnicosView) facesContext.getApplication().getELResolver()
					.getValue(facesContext.getELContext(), null, "reporteIdsUnicosView");
			pv.resetearReporte();

			opcAIncluir = "/secure/shared/reportes/informeIdsUnicos.xhtml";
			tituloPanelProducto = "Reporte Ids Puntos de Venta";

			break;
		}
		case 14: {
			// Rentabilidad
			FacesContext facesContext = FacesContext.getCurrentInstance();
			ReporteRentabilidadView pv = (ReporteRentabilidadView) facesContext.getApplication().getELResolver()
					.getValue(facesContext.getELContext(), null, "reporteRentabilidadView");
			pv.resetearReporte();

			opcAIncluir = "/secure/shared/reportes/informeRentabilidad.xhtml";
			tituloPanelProducto = "Informe de Rentabilidad";

			break;
		}
		case 15: {
			// Cons. Saldo Autom. Proveedores
			FacesContext facesContext = FacesContext.getCurrentInstance();
			ReporteConsSaldoProvProdView pv = (ReporteConsSaldoProvProdView) facesContext.getApplication()
					.getELResolver().getValue(facesContext.getELContext(), null, "reporteConsSaldoProvProdView");
			pv.resetearReporte();

			opcAIncluir = "/secure/shared/reportes/informeCSPP.xhtml";
			tituloPanelProducto = "Reporte Saldo a Proveedores";

			break;
		}
		case 16: {
			// Switcheo proveedores
			FacesContext facesContext = FacesContext.getCurrentInstance();
			ReporteSwProvView pv = (ReporteSwProvView) facesContext.getApplication().getELResolver()
					.getValue(facesContext.getELContext(), null, "reporteSwProvView");
			pv.resetearReporte();

			opcAIncluir = "/secure/shared/reportes/informeSwProv.xhtml";
			tituloPanelProducto = "Reporte de Switcheo de Proveedores";

			break;
		}
		case 17: {
			// Carga de Pines
			FacesContext facesContext = FacesContext.getCurrentInstance();
			ReporteCargaPinesView pv = (ReporteCargaPinesView) facesContext.getApplication().getELResolver()
					.getValue(facesContext.getELContext(), null, "reporteCargaPinesView");
			pv.resetearReporte();

			opcAIncluir = "/secure/shared/reportes/informeCargaPines.xhtml";
			tituloPanelProducto = "Carga de Pines";

			break;
		}
		case 18: {
			// Stock de Terminales
			FacesContext facesContext = FacesContext.getCurrentInstance();
			ReporteStockTerminalesView pv = (ReporteStockTerminalesView) facesContext.getApplication().getELResolver()
					.getValue(facesContext.getELContext(), null, "reporteStockTerminalesView");
			pv.resetearReporte();

			opcAIncluir = "/secure/shared/reportes/informeStockTerminales.xhtml";
			tituloPanelProducto = "Reporte Stock de Terminales";

			break;
		}
		case 19: {
			// Alta Cargas Automaticas
			FacesContext facesContext = FacesContext.getCurrentInstance();
			ReporteInsertCargasAutomaticasView pv = (ReporteInsertCargasAutomaticasView) facesContext.getApplication()
					.getELResolver().getValue(facesContext.getELContext(), null, "reporteInsertCargasAutomaticasView");
			pv.resetearReporte();

			opcAIncluir = "/secure/shared/reportes/informeInsertCargasAutomaticas.xhtml";
			tituloPanelProducto = "Alta Cargas Automaticas";

			break;
		}
		case 20: {
			// Inf. Cargas Automaticas
			FacesContext facesContext = FacesContext.getCurrentInstance();
			ReporteCargasAutomaticasView pv = (ReporteCargasAutomaticasView) facesContext.getApplication()
					.getELResolver().getValue(facesContext.getELContext(), null, "reporteCargasAutomaticasView");
			pv.resetearReporte();

			opcAIncluir = "/secure/shared/reportes/informeCargasAutomaticas.xhtml";
			tituloPanelProducto = "Reporte Cargas Automaticas";

			break;
		}
		case 21: {
			// Carga Masiva Depositos
			FacesContext facesContext = FacesContext.getCurrentInstance();
			ReporteCargaMasivaDepositosView pv = (ReporteCargaMasivaDepositosView) facesContext.getApplication()
					.getELResolver().getValue(facesContext.getELContext(), null, "reporteCargaMasivaDepositosView");
			pv.resetearReporte();

			opcAIncluir = "/secure/shared/reportes/informeCargaMasivaDepositos.xhtml";
			tituloPanelProducto = "Carga Masiva Depositos";

			break;
		}
		case 22: {
			// Maquinas AutoServ.
			FacesContext facesContext = FacesContext.getCurrentInstance();
			ReporteMASView pv = (ReporteMASView) facesContext.getApplication().getELResolver()
					.getValue(facesContext.getELContext(), null, "reporteMASView");
			pv.resetearReporte();

			opcAIncluir = "/secure/shared/reportes/informeMAS.xhtml";
			tituloPanelProducto = "Reporte Maquinas Autoservicio";

			break;
		}
		case 23: {
			// Inf. Seguros
			FacesContext facesContext = FacesContext.getCurrentInstance();
			ReporteSegurosView pv = (ReporteSegurosView) facesContext.getApplication().getELResolver()
					.getValue(facesContext.getELContext(), null, "reporteSegurosView");
			pv.resetearReporte();

			opcAIncluir = "/secure/shared/reportes/informeSeguros.xhtml";
			tituloPanelProducto = "Informe de Seguros";

			break;
		}
		case 24: {
			// Estado Proveedores
			FacesContext facesContext = FacesContext.getCurrentInstance();
			ReporteEstadoProveedoresView pv = (ReporteEstadoProveedoresView) facesContext.getApplication()
					.getELResolver().getValue(facesContext.getELContext(), null, "reporteEstadoProveedoresView");
			pv.resetearReporte();

			opcAIncluir = "/secure/shared/reportes/informeEstProv.xhtml";
			tituloPanelProducto = "Reporte Estado de Proveedores";

			break;
		}
		case 25: {
			// Estado Bancos
			FacesContext facesContext = FacesContext.getCurrentInstance();
			ReporteEstadoBancosView pv = (ReporteEstadoBancosView) facesContext.getApplication().getELResolver()
					.getValue(facesContext.getELContext(), null, "reporteEstadoBancosView");
			pv.resetearReporte();

			opcAIncluir = "/secure/shared/reportes/informeEstBanco.xhtml";
			tituloPanelProducto = "Reporte Estado de Bancos";

			break;
		}
		case 26: {
			// Facturas
			FacesContext facesContext = FacesContext.getCurrentInstance();
			ReporteFacturasView pv = (ReporteFacturasView) facesContext.getApplication().getELResolver()
					.getValue(facesContext.getELContext(), null, "reporteFacturasView");
			pv.resetearReporte();

			opcAIncluir = "/secure/shared/reportes/informeFacturas.xhtml";
			tituloPanelProducto = "Reporte de Facturas";

			break;
		}
		case 27: {
			// Stock de SIMS
			FacesContext facesContext = FacesContext.getCurrentInstance();
			ReporteStockSimsView pv = (ReporteStockSimsView) facesContext.getApplication().getELResolver()
					.getValue(facesContext.getELContext(), null, "reporteStockSimsView");
			pv.resetearReporte();

			opcAIncluir = "/secure/shared/reportes/informeStockSims.xhtml";
			tituloPanelProducto = "Reporte Stock de SIMS";

			break;
		}
		case 28: {
			// Carga Masiva clientes
			FacesContext facesContext = FacesContext.getCurrentInstance();
			ReporteCargaMasivaClientesView pv = (ReporteCargaMasivaClientesView) facesContext.getApplication()
					.getELResolver().getValue(facesContext.getELContext(), null, "reporteCargaMasivaClientesView");
			pv.resetearReporte();

			opcAIncluir = "/secure/shared/reportes/informeCargaMasivaClientes.xhtml";
			tituloPanelProducto = "Carga Masiva Clientes";

			break;
		}
		case 29: {
			// Comisiones vigentes
			FacesContext facesContext = FacesContext.getCurrentInstance();
			ReporteComisionesVigentesView pv = (ReporteComisionesVigentesView) facesContext.getApplication()
					.getELResolver().getValue(facesContext.getELContext(), null, "reporteComisionesVigentesView");
			pv.resetearReporte();

			opcAIncluir = "/secure/shared/reportes/informeComisionesVigentes.xhtml";
			tituloPanelProducto = "Reporte de Comisiones Vigentes";

			break;
		}
		case 30: {
			// Comprobantes Externos
			FacesContext facesContext = FacesContext.getCurrentInstance();
			ReporteComprobantesExternosView pv = (ReporteComprobantesExternosView) facesContext.getApplication()
					.getELResolver().getValue(facesContext.getELContext(), null, "reporteComprobantesExternosView");
			pv.resetearReporte();

			opcAIncluir = "/secure/shared/reportes/informeComprobantesExternos.xhtml";
			tituloPanelProducto = "Reporte Movimientos Externos";

			break;
		}

		case 31: {
			// Stock LGs de SUBE
			FacesContext facesContext = FacesContext.getCurrentInstance();
			ReporteSubeLgsView pv = (ReporteSubeLgsView) facesContext.getApplication().getELResolver()
					.getValue(facesContext.getELContext(), null, "reporteSubeLgsView");
			pv.resetearReporte();

			opcAIncluir = "/secure/shared/reportes/informeSubeLgs.xhtml";
			tituloPanelProducto = "Reporte LGs de SUBE";

			break;
		}
		case 32: {
			// SUBE Transacciones
			FacesContext facesContext = FacesContext.getCurrentInstance();
			ReporteSubeTrxsView pv = (ReporteSubeTrxsView) facesContext.getApplication().getELResolver()
					.getValue(facesContext.getELContext(), null, "reporteSubeTrxsView");
			pv.resetearReporte();

			opcAIncluir = "/secure/shared/reportes/informeSubeTrxs.xhtml";
			tituloPanelProducto = "Informe de Transacciones de SUBE";

			break;
		}
		case 33: {
			// SUBE Lotes
			FacesContext facesContext = FacesContext.getCurrentInstance();
			ReporteSubeLotesView pv = (ReporteSubeLotesView) facesContext.getApplication().getELResolver()
					.getValue(facesContext.getELContext(), null, "reporteSubeLotesView");
			pv.resetearReporte();

			opcAIncluir = "/secure/shared/reportes/informeSubeLotes.xhtml";
			tituloPanelProducto = "Informe Lotes de SUBE";

			break;
		}
		case 34: {
			// Informe PNet
			FacesContext facesContext = FacesContext.getCurrentInstance();
			ReportePNetACGView pv = (ReportePNetACGView) facesContext.getApplication().getELResolver()
					.getValue(facesContext.getELContext(), null, "reportePNetACGView");
			pv.resetearReporte();

			opcAIncluir = "/secure/shared/reportes/informePNet.xhtml";
			auxiliarAIncluir = "/secure/shared/reportes/includeAuxiliaresReportesPNet.xhtml";
			tituloPanelProducto = "Informe de PNet";

			break;

		}
		case 35: {
			// Reparto Saldos WU

			FacesContext facesContext = FacesContext.getCurrentInstance();
			ReporteRepartosWUView pv = (ReporteRepartosWUView) facesContext.getApplication().getELResolver()
					.getValue(facesContext.getELContext(), null, "reporteRepartosWUView");
			pv.resetearReporte();

			opcAIncluir = "/secure/shared/reportes/informeRepartosWU.xhtml";
			tituloPanelProducto = "Informe de Repartos WU";

			break;
		}
		case 36: {
			// Informe WU

			FacesContext facesContext = FacesContext.getCurrentInstance();
			ReporteWUView pv = (ReporteWUView) facesContext.getApplication().getELResolver()
					.getValue(facesContext.getELContext(), null, "reporteWUView");
			pv.resetearReporte();

			opcAIncluir = "/secure/shared/reportes/informeWU.xhtml";
			tituloPanelProducto = "Reporte de WU";

			break;
		}
		case 37: {
			// Inc. Saldo Mayorista WU

			FacesContext facesContext = FacesContext.getCurrentInstance();
			ReporteIncrementoSaldoMayoristaWUView pv = (ReporteIncrementoSaldoMayoristaWUView) facesContext
					.getApplication().getELResolver()
					.getValue(facesContext.getELContext(), null, "reporteIncrementoSaldoMayoristaWUView");
			pv.resetearReporte();

			opcAIncluir = "/secure/shared/reportes/informeISMWU.xhtml";
			tituloPanelProducto = "Reporte Incremento Saldo Mayorista de Western Union";

			break;
		}
		case 38: {
			// Inc. Saldo Mayorista WU

			FacesContext facesContext = FacesContext.getCurrentInstance();
			ReportePagoElectronicoLIPAView pv = (ReportePagoElectronicoLIPAView) facesContext.getApplication()
					.getELResolver().getValue(facesContext.getELContext(), null, "reportePagoElectronicoLIPAView");
			pv.resetearReporte();

			opcAIncluir = "/secure/shared/reportes/informeLIPA.xhtml";
			tituloPanelProducto = "Listado de Cobros";

			break;
		}

		default: {
			opcAIncluir = "";
			tituloPanelProducto = "";
		}
		}

		PrimeFaces.current().ajax().update("opcAIncluir");
		PrimeFaces.current().executeScript("PF('panelProductosWV').show();");

	}

	public void onClose() {
		if ("REPORTES".equals(origen)) {
			navegarMenuIconos(3);
		}
	}

	public AutoCompleteClienteView getAutoCompleteCliente() {
		return autoCompleteCliente;
	}

	public void setAutoCompleteCliente(AutoCompleteClienteView autoCompleteCliente) {
		this.autoCompleteCliente = autoCompleteCliente;
	}

	public Long getProdCom_idTipoProducto() {
		return prodCom_idTipoProducto;
	}

	public void setProdCom_idTipoProducto(Long prodCom_idTipoProducto) {
		this.prodCom_idTipoProducto = prodCom_idTipoProducto;
	}
}
