package com.americacg.cargavirtual.web.mngbeans;

import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.inject.Named;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import org.primefaces.PrimeFaces;
import org.primefaces.event.DashboardReorderEvent;
import org.primefaces.event.ToggleEvent;
import org.primefaces.model.Visibility;
import org.primefaces.model.charts.ChartData;
import org.primefaces.model.charts.line.LineChartDataSet;
import org.primefaces.model.charts.line.LineChartModel;
import org.springframework.context.annotation.Scope;

import com.americacg.cargavirtual.gateway.pagoElectronico.model.MensajeOutboundGateway;
import com.americacg.cargavirtual.gateway.pagoElectronico.model.entidad.Pago;
import com.americacg.cargavirtual.gateway.pagoElectronico.model.parametros.funciones.output.DataOutputFcnCOPA;
import com.americacg.cargavirtual.gestion.model.Cliente;
import com.americacg.cargavirtual.gestion.model.FiltroListadoPlatAgendaDestinatarioCta;
import com.americacg.cargavirtual.gestion.model.FiltroListadoPlatArancel;
import com.americacg.cargavirtual.gestion.model.FiltroListadoPlatConsolidadoCuenta;
import com.americacg.cargavirtual.gestion.model.FiltroListadoPlatCuenta;
import com.americacg.cargavirtual.gestion.model.FiltroListadoPlatEntidadFinanciera;
import com.americacg.cargavirtual.gestion.model.FiltroListadoPlatEstadoTransferencia;
import com.americacg.cargavirtual.gestion.model.FiltroListadoPlatMoneda;
import com.americacg.cargavirtual.gestion.model.FiltroListadoPlatMovimientoCuenta;
import com.americacg.cargavirtual.gestion.model.FiltroListadoPlatTipoDocumento;
import com.americacg.cargavirtual.gestion.model.FiltroListadoPlatTipoIdentifCta;
import com.americacg.cargavirtual.gestion.model.HeaderSecur;
import com.americacg.cargavirtual.gestion.model.ObjectFactory;
import com.americacg.cargavirtual.gestion.model.PlatAgendaDestinatarioCta;
import com.americacg.cargavirtual.gestion.model.PlatAgendaDestinatarioCtaList;
import com.americacg.cargavirtual.gestion.model.PlatArancel;
import com.americacg.cargavirtual.gestion.model.PlatArancelList;
import com.americacg.cargavirtual.gestion.model.PlatCanal;
import com.americacg.cargavirtual.gestion.model.PlatClasificacionTransferencia;
import com.americacg.cargavirtual.gestion.model.PlatConsolidadoCuenta;
import com.americacg.cargavirtual.gestion.model.PlatConsolidadoCuentaList;
import com.americacg.cargavirtual.gestion.model.PlatCuenta;
import com.americacg.cargavirtual.gestion.model.PlatCuentaList;
import com.americacg.cargavirtual.gestion.model.PlatEntidadFinanciera;
import com.americacg.cargavirtual.gestion.model.PlatEntidadFinancieraList;
import com.americacg.cargavirtual.gestion.model.PlatEstadoAgendaDestCta;
import com.americacg.cargavirtual.gestion.model.PlatEstadoMovimientoCuenta;
import com.americacg.cargavirtual.gestion.model.PlatEstadoTransferencia;
import com.americacg.cargavirtual.gestion.model.PlatEstadoTransferenciaList;
import com.americacg.cargavirtual.gestion.model.PlatImpuesto;
import com.americacg.cargavirtual.gestion.model.PlatMoneda;
import com.americacg.cargavirtual.gestion.model.PlatMonedaList;
import com.americacg.cargavirtual.gestion.model.PlatMovimientoCuenta;
import com.americacg.cargavirtual.gestion.model.PlatMovimientoCuentaList;
import com.americacg.cargavirtual.gestion.model.PlatTipoDocumento;
import com.americacg.cargavirtual.gestion.model.PlatTipoDocumentoList;
import com.americacg.cargavirtual.gestion.model.PlatTipoIdentifCta;
import com.americacg.cargavirtual.gestion.model.PlatTipoIdentifCtaList;
import com.americacg.cargavirtual.gestion.model.PlatTipoMovimiento;
import com.americacg.cargavirtual.gestion.model.PlatTransferencia;
import com.americacg.cargavirtual.gestion.model.RespAcredPlatCta;
import com.americacg.cargavirtual.gestion.model.RespString;
import com.americacg.cargavirtual.gestion.model.ResultadoBase;
import com.americacg.cargavirtual.gestion.model.Usuario;
import com.americacg.cargavirtual.gestion.service.ArrayOfString;
import com.americacg.cargavirtual.web.enums.CfgTimeout;
import com.americacg.cargavirtual.web.handlers.HandledException;
import com.americacg.cargavirtual.web.helpers.GestionServiceHelper;
import com.americacg.cargavirtual.web.helpers.LogACGHelper;
import com.americacg.cargavirtual.web.helpers.PagoElectronicoHelper;
import com.americacg.cargavirtual.web.helpers.UsuarioHelper;
import com.americacg.cargavirtual.web.lazymodels.LazyAgendaDestinatarioDataModel;
import com.americacg.cargavirtual.web.lazymodels.LazyMovimientosDeCuentaDataModel;
import com.americacg.cargavirtual.web.lazymodels.LazyTransferenciasDataModel;
import com.americacg.cargavirtual.web.model.Modulo;
import com.americacg.cargavirtual.web.model.superPago.Cuenta;
import com.americacg.cargavirtual.web.model.superPago.ItemAgendaDestinatario;
import com.americacg.cargavirtual.web.model.superPago.ItemTransferencia;
import com.americacg.cargavirtual.web.model.superPago.MovimientoDeCuenta;
import com.americacg.cargavirtual.web.model.superPago.TipoIdentificacionCta;
import com.americacg.cargavirtual.web.model.superPago.TipoMovimiento;
import com.americacg.cargavirtual.web.shared.BasePage;

@Named("superPagoView")
@Scope(org.springframework.web.context.WebApplicationContext.SCOPE_SESSION)
public class SuperPagoView extends BasePage implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4632176115821243509L;
	private List<Cuenta> cuentas;
	private List<SelectItem> arancelesDebito = null;
	private List<SelectItem> arancelesCredito = null;
	private List<SelectItem> arancelesDebitoExtra = null;
	private List<SelectItem> arancelesCreditoExtra = null;
	private List<SelectItem> monedas = null;;
	private List<SelectItem> cuentasCfgAranceles = null;
	private List<SelectItem> entidadesFinancierasOrigen = null;
	private List<SelectItem> entidadesFinancierasDestino = null;
	private List<SelectItem> entidadesFinancierasAgDest = null;
	private List<SelectItem> tiposIdentificacionTributaria = null;
	private List<SelectItem> estadosTransferencia = null;
	private ArrayList<TipoIdentificacionCta> tiposIdentificacionCuenta = null;
	private ArrayList<Cuenta> cuentasOrigen = null;
	private ArrayList<ItemAgendaDestinatario> itemsAgendaDestinatarios = null;
	private ArrayList<ItemAgendaDestinatario> itemsAgendaDestinatariosPublicos = null;
	private ArrayList<MovimientoDeCuenta> aranceles = null;
	private LazyAgendaDestinatarioDataModel listaDeAgendaDestinatarios = null;
	private ItemAgendaDestinatario itemAgendaDestinatario = null;
	private LazyTransferenciasDataModel listaDeTransferencias = null;
	private ItemTransferencia itemTransferencia = null;
	private LazyMovimientosDeCuentaDataModel movimientosDeCuenta = null;
	private MovimientoDeCuenta itemMovimientoDeCuenta = null;
	private Cuenta cuentaDestino = null;
	private Cuenta cuentaOrigen = null;
	private Cuenta cuentaAdministradora = null;
	private Cliente cliente = null;
	private PlatEntidadFinanciera entidadFinanciera = null;
	private PlatTipoIdentifCta tipoIdentificacionCta = null;
	private Date fechaDesde = null;
	private Date fechaHasta = null;
	private Date fechaHoy = null;
	private Date fechaAhora = null;
	private String seleccionArancelDebito = "";
	private String seleccionArancelCredito = "";
	private String seleccionIdMoneda = "";
	private String seleccionIdTipoIdentifTributaria = "";
	private String operacion = "";
	private String tipoTrf = "";
	private String comentarios = "";
	private Pago detallePagoMEP = null;
	private ItemTransferencia detalleTransferencia = null;
	private TipoIdentificacionCta tipoIdentificacionCuenta = null;
	private Long cuitCliente = null;
	private Float importeTotalOperacion = null;
	private Float importeTotalAranceles = null;
	private boolean solicitudDeDineroPropia = false;
	private boolean muestraCRUDAdmDestCta = false;
	private boolean muestraCRUDTransferencia = false;
	private boolean muestraCRUDSolicituDeDinero = false;
	private boolean validaCBU = false;
	private boolean muestraAplicacionAranceles = false;
	private LineChartModel cartesianLinerModel = new LineChartModel();
	private ObjectFactory oGestionObjFac = new ObjectFactory();

	public void inicializar() {
		ObjectFactory oGestionObjFac = new ObjectFactory();

		FiltroListadoPlatCuenta flpc = new FiltroListadoPlatCuenta();

		this.limpiarPantalla();

		this.cuentas = new ArrayList<Cuenta>();

		PlatCuenta oCta = new PlatCuenta();
		Cliente oCli = new Cliente();
		oCli.setIdCliente(oGestionObjFac.createClienteIdCliente(this.getUsuario().getIdCliente()));
		
		oCta.setIdMayorista(oGestionObjFac.createPlatCuentaIdMayorista(this.getUsuario().getIdMayorista()));
		oCta.setCliente(oGestionObjFac.createPlatCuentaCliente(oCli));

		flpc.setCuenta(oGestionObjFac.createFiltroListadoPlatCuentaCuenta(oCta));
		flpc.setPage(oGestionObjFac.createFiltroListadoPlatCuentaPage(0));
		flpc.setPageSize(oGestionObjFac.createFiltroListadoPlatCuentaPageSize(0));

		try {

			GregorianCalendar gcFechaSaldo = new GregorianCalendar();
			gcFechaSaldo.setTime(new Date());
			XMLGregorianCalendar xmlGCFechaSaldo;

			xmlGCFechaSaldo = DatatypeFactory.newInstance().newXMLGregorianCalendar(gcFechaSaldo);
			flpc.setFechaSaldo(xmlGCFechaSaldo);
		} catch (DatatypeConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		PlatCuentaList aoPC = GestionServiceHelper.getGestionService(CfgTimeout.DEFAULT).listarPlatCuenta(flpc);
		
		
		if (aoPC != null && aoPC.getRegistros().getValue() != null
				&& aoPC.getRegistros().getValue().getPlatCuenta() != null) {
			for (PlatCuenta itemPC : aoPC.getRegistros().getValue().getPlatCuenta()) {
				Cuenta oCtaAux = new Cuenta();
				oCtaAux.setIdMayorista(itemPC.getIdMayorista().getValue());
				oCtaAux.setIdCuenta(itemPC.getIdCuenta().getValue());
				oCtaAux.setAlias(itemPC.getAlias().getValue());
				oCtaAux.setCVU(itemPC.getCVU().getValue());
				oCtaAux.setIdProducto((itemPC.getProducto().getValue() != null
						? itemPC.getProducto().getValue().getIdProducto().getValue()
						: null));
				oCtaAux.setNumeroCuenta(itemPC.getNumeroCuenta().getValue());
				oCtaAux.setIdMoneda(
						(itemPC.getMoneda().getValue() != null ? itemPC.getMoneda().getValue().getIdMoneda().getValue()
								: null));
				oCtaAux.setDescripcionMoneda((itemPC.getMoneda().getValue() != null
						? itemPC.getMoneda().getValue().getDescripcion().getValue()
						: null));
				oCtaAux.setSimboloMoneda((itemPC.getMoneda().getValue() != null
						? itemPC.getMoneda().getValue().getTxtSimbolo().getValue()
						: null));

				oCtaAux.setSaldo(itemPC.getSaldo().getValue());
				oCtaAux.setSaldoPendiente(itemPC.getSaldoPendiente().getValue());

				oCtaAux.setDescripcionEstado(itemPC.getEstado().getValue().getDescripcion().getValue());
				oCtaAux.setCodMnemonicoEstado(itemPC.getEstado().getValue().getCodMnemonico().getValue());
				oCtaAux.setProductoDescripcion(itemPC.getProducto().getValue().getDescProducto().getValue());

				if (itemPC.getLogo().getValue() != null) {
					oCtaAux.setLogo(itemPC.getLogo().getValue());
				} else {
					oCtaAux.setLogo(null);
				}
				
				oCtaAux.setIdTipoCuenta(itemPC.getTipoCuenta().getValue().getIdTipoCuenta().getValue());
				oCtaAux.setCodMnemonicoTipoCuenta(itemPC.getTipoCuenta().getValue().getCodMnemonico().getValue());
				oCtaAux.setDescripcionTipoCuenta(itemPC.getTipoCuenta().getValue().getDescripcion().getValue());
				
				cuentas.add(oCtaAux);
			}
		}

	}
	public void inicializarMovimientosDeCuenta(Cuenta cuentaAdministradora) {
		ArrayList<String> exclusiones = new ArrayList<String>();
		exclusiones.add("ANUL");
		exclusiones.add("PENDACRE");

		this.movimientosDeCuenta = new LazyMovimientosDeCuentaDataModel(this.getUsuario().getIdMayorista(),
				this.getUsuario().getIdCliente(), cuentaAdministradora, null, exclusiones, "fechaAcreditacion", "DESC",
				this.getFechaDesde(), this.getFechaHasta(), (this.getUsuario().getPermisosReducidosEnModuloDeCuentas() ? this.getUsuario().getIdUsuario() : null));
		this.cuentaAdministradora = cuentaAdministradora;
		this.itemMovimientoDeCuenta = null;

		createCartesianLinerModel();
		obtenerDatosComercioMEP();
	}

	public void inicializarMovimientosDeCuentaPendientes(Cuenta cuentaAdministradora) {
		ArrayList<String> inclusiones = new ArrayList<String>();
		inclusiones.add("PENDACRE");

		this.movimientosDeCuenta = new LazyMovimientosDeCuentaDataModel(this.getUsuario().getIdMayorista(),
				this.getUsuario().getIdCliente(), cuentaAdministradora, inclusiones, null, "fechaMovimiento", "DESC",
				null, null, (this.getUsuario().getPermisosReducidosEnModuloDeCuentas() ? this.getUsuario().getIdUsuario() : null));

		this.cuentaAdministradora = cuentaAdministradora;
		this.itemMovimientoDeCuenta = null;
		obtenerDatosComercioMEP();
	}

	public void inicializarAgendaDestinatarios() {
		this.listaDeAgendaDestinatarios = new LazyAgendaDestinatarioDataModel(this.getUsuario().getIdCliente(),
				this.getUsuario().getIdMayorista(), "", "");
		itemAgendaDestinatario = null;
	}

	public void inicializarDepositoEnCta(Cuenta cuentaAdministradora) {
		this.listaDeAgendaDestinatarios = new LazyAgendaDestinatarioDataModel(this.getUsuario().getIdCliente(),
				this.getUsuario().getIdMayorista(), "", "");

		itemAgendaDestinatario = null;
		itemTransferencia = null;
		this.setCuentaDestino(cuentaAdministradora);

		ArrayList<String> inclusiones = new ArrayList<String>();
		inclusiones.add("DEPD");

		this.listaDeTransferencias = new LazyTransferenciasDataModel(this.getUsuario().getIdCliente(),
				this.getUsuario().getIdMayorista(), cuentaAdministradora, null, null, inclusiones, null, "", "",
				this.getFechaDesde(), this.getFechaHasta(), (this.getUsuario().getPermisosReducidosEnModuloDeCuentas() ? this.getUsuario().getIdUsuario() : null));

		this.cuentaAdministradora = cuentaAdministradora;
	}

	public void inicializarTransferencias(Cuenta cuentaAdministradora) {
		ArrayList<String> inclusiones = new ArrayList<String>();

		try {
			inclusiones.add("TENT");
			inclusiones.add("TSAL");

			this.listaDeTransferencias = new LazyTransferenciasDataModel(this.getUsuario().getIdCliente(),
					this.getUsuario().getIdMayorista(), cuentaAdministradora, null, null, inclusiones, null, "", "",
					this.getFechaDesde(), this.getFechaHasta(), (this.getUsuario().getPermisosReducidosEnModuloDeCuentas() ? this.getUsuario().getIdUsuario() : null));

			this.setCuentaOrigen(this.obtenerCuentaSuperPago(cuentaAdministradora));

			this.cuentaAdministradora = cuentaAdministradora;
		} catch (Exception e) {
			LogACGHelper.escribirLog(null, "grabarItmTransferencia. |" + e.getMessage() + "|");

			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
					"Se produjo un error al obtener la cuenta origen. Intente la operacion nuevamente", null));

			PrimeFaces.current().ajax().update("msgsProductos");
		}

	}

	public void inicializarRequerimientoDeDinero() {
		ArrayList<String> inclusiones = new ArrayList<String>();
		inclusiones.add("SDIN");
		solicitudDeDineroPropia = false;
		this.listaDeTransferencias = new LazyTransferenciasDataModel(null, this.getUsuario().getIdMayorista(), null,
				this.getUsuario().getIdCliente(), null, inclusiones, null, "", "", this.getFechaDesde(),
				this.getFechaHasta(), (this.getUsuario().getPermisosReducidosEnModuloDeCuentas() ? this.getUsuario().getIdUsuario() : null));
	}

	public void inicializarSolicitudDeDinero(Cuenta cuentaAdministradora) {
		ArrayList<String> inclusiones = new ArrayList<String>();
		inclusiones.add("SDIN");
		solicitudDeDineroPropia = true;

		this.listaDeTransferencias = new LazyTransferenciasDataModel(null, this.getUsuario().getIdMayorista(),
				cuentaAdministradora, null, this.getUsuario().getIdCliente(), inclusiones, null, "", "",
				this.getFechaDesde(), this.getFechaHasta(), (this.getUsuario().getPermisosReducidosEnModuloDeCuentas() ? this.getUsuario().getIdUsuario() : null));

		this.setCuentaDestino(cuentaAdministradora);

		this.cuentaAdministradora = cuentaAdministradora;
	}

	public void inicializarGenerarSolicitudDeDinero() {
		this.itemTransferencia = new ItemTransferencia();
		this.setCuentaDestino(this.getUsuario().getSuperPagoInstance().getCuentaSeleccionada());
	}

	public void obtenerDatosComercioMEP() {
		Integer estado = 0;
		Cuenta oCta = null;

		try {
			if (this.getUsuario().getPagoElectronicoInstance() == null
					|| this.getUsuario().getPagoElectronicoInstance().getIdCuentaAcreditacion() == null
					|| this.getUsuario().getPagoElectronicoInstance().getCodMnemonicoArancelCredito() == null
					|| this.getUsuario().getPagoElectronicoInstance().getCodMnemonicoArancelCredito().isEmpty()
					|| this.getUsuario().getPagoElectronicoInstance().getCodMnemonicoArancelDebito() == null
					|| this.getUsuario().getPagoElectronicoInstance().getCodMnemonicoArancelDebito().isEmpty()) {
				PagoElectronicoHelper.getInstance();
				estado = PagoElectronicoHelper.ejecutarFuncionGCOM();
			} else
				estado = 1;

			if (estado.compareTo(1) != 0) {
				LogACGHelper.escribirLog(null, "Inicializar Pago Electronico. Comercio deshabilitado");
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
						"El comercio no se encuentra habilitado para operar MEP.", null));
				return;
			}

			seleccionArancelCredito = this.getUsuario().getPagoElectronicoInstance().getCodMnemonicoArancelCredito();
			
			if(seleccionArancelCredito == null || seleccionArancelCredito.isEmpty())
				FacesContext.getCurrentInstance().addMessage(null,
						new FacesMessage(FacesMessage.SEVERITY_INFO, "Debe seleccionar un arancel de credito.", null));

			seleccionArancelDebito = this.getUsuario().getPagoElectronicoInstance().getCodMnemonicoArancelDebito();

			if(seleccionArancelDebito == null || seleccionArancelDebito.isEmpty())
				FacesContext.getCurrentInstance().addMessage(null,
						new FacesMessage(FacesMessage.SEVERITY_INFO, "Debe seleccionar un arancel de debito.", null));

			// seleccionIdCuenta =
			// this.getUsuario().getPagoElectronicoInstance().getIdCuentaAcreditacion();

			if(this.getUsuario().getPagoElectronicoInstance().getIdCuentaAcreditacion() != null) {
				oCta = new Cuenta();
				oCta.setIdCuenta(this.getUsuario().getPagoElectronicoInstance().getIdCuentaAcreditacion());
	
				oCta = this.obtenerCuentaSuperPago(oCta);
	
				if (oCta == null || oCta.getIdCuenta() == null) {
					LogACGHelper.escribirLog(null,
							"Inicializar Pago Electronico. |Error al obtener la cuenta del cliente.|");
					FacesContext.getCurrentInstance().addMessage(null,
							new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error al obtener la cuenta del cliente.", null));
				} else
					this.setCuentaOrigen(oCta);
			}
			else {
				FacesContext.getCurrentInstance().addMessage(null,
						new FacesMessage(FacesMessage.SEVERITY_INFO, "Debe seleccionar una cuenta de acreditacion.", null));
			}

			this.setCuentasOrigen(null);
		} catch (Exception e) {
			LogACGHelper.escribirLog(null, "Inicializar Pago Electronico. |" + e.getMessage() + "|");
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
					"El comercio no se encuentra habilitado para operar.", null));
			return;
		}
	}
	public void transferir() {
		addMessage("Transferir!!");
	}

	public void resetear() {

	}

	public void accion(Integer accion) {
		this.limpiarPantalla();
	}

	public void limpiarPantalla() {

	}

	public void mostrarTicket() {
		String nombreTicketCompleto = "";
		String nombreTicket = this.getUsuario().getTicket();

		Map<String, Object> options = new HashMap<String, Object>();
		options.put("modal", true);
		options.put("draggable", true);
		options.put("resizable", false);
		options.put("closable", true);
		options.put("contentWidth", this.getUsuario().getAnchoTicket() + 45);
		options.put("position", "center center");
		options.put("includeViewParams", true);
		options.put("fitViewport", true);

		Map<String, List<String>> paramMap = new HashMap<String, List<String>>();

		nombreTicketCompleto = "/secure/shared/tickets/" + nombreTicket + "PagoElectronico";

		// --- Abre el dialogo para buscar articulos
		PrimeFaces.current().dialog().openDynamic(nombreTicketCompleto, options, paramMap);
		PrimeFaces.current().executeScript("PF('blockPanelGral').hide()");
	}

	public void superPagoCBU(Cuenta cuenta) {

		// UsuarioHelper.usuarioSession().getSuperPagoInstance().setCuentaSeleccionada(cuenta);

		/*
		 * RespFloat rf = null; XMLGregorianCalendar xmlGCFechaSaldo = null;
		 * GregorianCalendar gcFechaSaldo = null;
		 * 
		 * try { gcFechaSaldo = new GregorianCalendar(); gcFechaSaldo.setTime(new
		 * Date()); xmlGCFechaSaldo =
		 * DatatypeFactory.newInstance().newXMLGregorianCalendar(gcFechaSaldo);
		 * 
		 * } catch (Exception e) { // TODO: handle exception }
		 * 
		 * PlatCuenta pc =
		 * GestionServiceHelper.getGestionService(CfgTimeout.DEFAULT).obtenerPlatCuenta(
		 * idCuenta, xmlGCFechaSaldo);
		 * 
		 * if (pc != null) {
		 * cuentaSeleccionada.setIdCuenta(pc.getIdCuenta().getValue());
		 * cuentaSeleccionada.setIdMayorista(pc.getIdMayorista().getValue());
		 * cuentaSeleccionada.setProductoDescripcion(pc.getProductoDescripcion().
		 * getValue());
		 * cuentaSeleccionada.setNumeroCuenta(pc.getNumeroCuenta().getValue());
		 * 
		 * rf =
		 * GestionServiceHelper.getGestionService(CfgTimeout.DEFAULT).obtenerSaldoActual
		 * (null, this.getUsuario().getIdMayorista(), pc.getIdCuenta().getValue()); if
		 * (rf != null) { cuentaSeleccionada.setSaldo(rf.getValor().getValue()); } else
		 * { cuentaSeleccionada.setSaldo(0F); } //
		 * cuentaSeleccionada.setSaldo(pc.getSaldo().getValue());
		 * cuentaSeleccionada.setIdMoneda(pc.getIdMoneda().getValue());
		 * cuentaSeleccionada.setCVU(pc.getCVU().getValue());
		 * cuentaSeleccionada.setAlias(pc.getAlias().getValue()); }
		 */
	}

	public void actualizarAranceles() {
		MensajeOutboundGateway oMsgOut = null;
		Integer estado = 0;

		try {
			if(this.getCuentaOrigen() == null) {
				LogACGHelper.escribirLog(null, "ActualizarAranceles. Debe seleccionar una cuenta de acreditacion");
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
						"Debe seleccionar una cuenta de acreditacion.", null));
				return;
			}
			
			PagoElectronicoHelper.getInstance();
			oMsgOut = PagoElectronicoHelper.ejecutarFuncionMCOM(this.getSeleccionArancelDebito(),
					this.getSeleccionArancelCredito(), (this.getCuentaOrigen() != null ? this.getCuentaOrigen().getIdCuenta() : null));

			if (oMsgOut != null && oMsgOut.getDataOutputFcn() != null
					&& "M0000".equals(oMsgOut.getDataOutputFcn().getCodigoRetorno())) {
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,
						"La Modificacion de plazos y tasas se ha realizado correctamente. ", null));

				estado = PagoElectronicoHelper.ejecutarFuncionGCOM();

				if (estado.compareTo(1) != 0) {
					LogACGHelper.escribirLog(null, "Inicializar Pago Electronico. Comercio deshabilitado");
					FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
							"El comercio no se encuentra habilitado para operar MEP.", null));
					return;
				}
			} else {
				LogACGHelper.escribirLog(null, "actualizarAranceles. |" + oMsgOut.getDataOutputFcn().getCodigoRetorno()
						+ " - " + oMsgOut.getDataOutputFcn().getMensajeRetorno() + "|");

				FacesContext.getCurrentInstance().addMessage(null,
						new FacesMessage(FacesMessage.SEVERITY_ERROR,
								"Modificacion de plazos y tasas. " + oMsgOut.getDataOutputFcn().getCodigoRetorno() + " - "
										+ oMsgOut.getDataOutputFcn().getMensajeRetorno(),
								null));
			}
		} catch (HandledException e) {
			LogACGHelper.escribirLog(null, "actualizarAranceles. |" + e.getMessage() + "|");
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL,
					"Modificacion de plazos y tasas. Excepcion gral.", null));
		}
	}

	public void superPagoAranceles(Cuenta cuenta) {

		// TODO: ver que hacer con los aranceles de cliente o con idCliente == null

		arancelesDebito = new ArrayList<SelectItem>();
		arancelesCredito = new ArrayList<SelectItem>();
		arancelesDebitoExtra = new ArrayList<SelectItem>();
		arancelesCreditoExtra = new ArrayList<SelectItem>();
		GregorianCalendar gcFechaVigencia = null;
		XMLGregorianCalendar xmlGCFechaVigencia = null;
		SelectItem oSelItm = null;
		PlatArancelList oLstAran = null;
		PlatArancel oAran = null;
		PlatTipoMovimiento oTipMov = null;
		FiltroListadoPlatArancel oFltLstArancel = null;

		// DEBITOS
		oFltLstArancel = new FiltroListadoPlatArancel();
		oFltLstArancel.setPageSize(oGestionObjFac.createFiltroListadoPlatArancelPageSize(0));
		oFltLstArancel.setPage(oGestionObjFac.createFiltroListadoPlatArancelPage(0));

		oAran = new PlatArancel();
		oTipMov = new PlatTipoMovimiento();
		oTipMov.setCodMnemonico(oGestionObjFac.createPlatTipoMovimientoCodMnemonico("CMEPD"));
		// --->>>oAran.setIdCliente(oGestionObjFac.createPlatArancelIdCliente(this.getUsuario().getIdCliente()));
		// REVISAR !!!!
		// oAran.setFlgArancelExclusivo(oGestionObjFac.createPlatArancelFlgArancelExclusivo(1));
		oAran.setEstado(oGestionObjFac.createPlatArancelEstado(1)); // Activos
		oAran.setTipoMovimiento(oGestionObjFac.createPlatArancelTipoMovimiento(oTipMov));

		try {
			gcFechaVigencia = new GregorianCalendar();
			gcFechaVigencia.setTime(new Date());

			xmlGCFechaVigencia = DatatypeFactory.newInstance().newXMLGregorianCalendar(gcFechaVigencia);
		} catch (DatatypeConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		oAran.setFechaInicioVigencia(xmlGCFechaVigencia);

		oFltLstArancel.setArancel(oGestionObjFac.createFiltroListadoPlatArancelArancel(oAran));

		oLstAran = GestionServiceHelper.getGestionService(CfgTimeout.DEFAULT).listarPlatArancel(oFltLstArancel);
		if (oLstAran != null && oLstAran.getRegistros() != null && oLstAran.getRegistros().getValue() != null
				&& oLstAran.getRegistros().getValue().getPlatArancel() != null) {
			for (PlatArancel itemPA : oLstAran.getRegistros().getValue().getPlatArancel()) {
				oSelItm = new SelectItem(itemPA, itemPA.getEtiqueta().getValue());

				if (itemPA.getArancelExclusivo().getValue()) {
					arancelesDebitoExtra.add(oSelItm);
				} else {
					arancelesDebito.add(oSelItm);
				}
			}
		}

		// CREDITOS
		oFltLstArancel = new FiltroListadoPlatArancel();
		oFltLstArancel.setPageSize(oGestionObjFac.createFiltroListadoPlatArancelPageSize(0));
		oFltLstArancel.setPage(oGestionObjFac.createFiltroListadoPlatArancelPage(0));

		oAran = new PlatArancel();
		oTipMov = new PlatTipoMovimiento();
		oTipMov.setCodMnemonico(oGestionObjFac.createPlatTipoMovimientoCodMnemonico("CMEPC"));
		// ---->>>oAran.setIdCliente(oGestionObjFac.createPlatArancelIdCliente(this.getUsuario().getIdCliente()));
		// REVISAR !!!!
		// oAran.setFlgArancelExclusivo(oGestionObjFac.createPlatArancelFlgArancelExclusivo(1));
		oAran.setEstado(oGestionObjFac.createPlatArancelEstado(1)); // Activos
		oAran.setTipoMovimiento(oGestionObjFac.createPlatArancelTipoMovimiento(oTipMov));

		try {
			gcFechaVigencia = new GregorianCalendar();
			gcFechaVigencia.setTime(new Date());

			xmlGCFechaVigencia = DatatypeFactory.newInstance().newXMLGregorianCalendar(gcFechaVigencia);
		} catch (DatatypeConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		oAran.setFechaInicioVigencia(xmlGCFechaVigencia);

		oFltLstArancel.setArancel(oGestionObjFac.createFiltroListadoPlatArancelArancel(oAran));

		oLstAran = GestionServiceHelper.getGestionService(CfgTimeout.DEFAULT).listarPlatArancel(oFltLstArancel);
		if (oLstAran != null && oLstAran.getRegistros() != null && oLstAran.getRegistros().getValue() != null
				&& oLstAran.getRegistros().getValue().getPlatArancel() != null) {
			for (PlatArancel itemPA : oLstAran.getRegistros().getValue().getPlatArancel()) {

				oSelItm = new SelectItem(itemPA, itemPA.getEtiqueta().getValue());

				if (itemPA.getArancelExclusivo().getValue()) {
					arancelesCreditoExtra.add(oSelItm);
				} else {
					arancelesCredito.add(oSelItm);
				}

			}
		}

	}

	public void handleReorder(DashboardReorderEvent event) {
		FacesMessage message = new FacesMessage();
		message.setSeverity(FacesMessage.SEVERITY_INFO);
		message.setSummary("Reordered: " + event.getWidgetId());
		message.setDetail("Item index: " + event.getItemIndex() + ", Column index: " + event.getColumnIndex()
				+ ", Sender index: " + event.getSenderColumnIndex());

		addMessage(message);
	}

	private void addMessage(FacesMessage message) {
		FacesContext.getCurrentInstance().addMessage(null, message);
	}

	public List<Cuenta> getCuentas() {
		FiltroListadoPlatCuenta oFltCta = new FiltroListadoPlatCuenta();
		PlatCuenta oPlatCta = null;
		Cliente oCli = null;
		Cuenta oCtaAux = null;
		GregorianCalendar gcFechaSaldo = null;
		XMLGregorianCalendar xmlGCFechaSaldo = null;

		if (cuentas == null) {
			this.cuentas = new ArrayList<Cuenta>();

			oPlatCta = new PlatCuenta();

			oPlatCta.setIdMayorista(oGestionObjFac.createPlatCuentaIdMayorista(this.getUsuario().getIdMayorista()));
			
			oCli = new Cliente();
			oCli.setIdCliente(oGestionObjFac.createClienteIdCliente(this.getUsuario().getIdCliente()));
			oPlatCta.setCliente(oGestionObjFac.createPlatCuentaCliente(oCli));

			oFltCta.setCuenta(oGestionObjFac.createFiltroListadoPlatCuentaCuenta(oPlatCta));
			oFltCta.setPage(oGestionObjFac.createFiltroListadoPlatCuentaPage(0));
			oFltCta.setPageSize(oGestionObjFac.createFiltroListadoPlatCuentaPageSize(0));

			try {
				gcFechaSaldo = new GregorianCalendar();
				gcFechaSaldo.setTime(new Date());

				xmlGCFechaSaldo = DatatypeFactory.newInstance().newXMLGregorianCalendar(gcFechaSaldo);
				oFltCta.setFechaSaldo(xmlGCFechaSaldo);
			} catch (DatatypeConfigurationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			PlatCuentaList aoPC = GestionServiceHelper.getGestionService(CfgTimeout.DEFAULT).listarPlatCuenta(oFltCta);
			if (aoPC != null && aoPC.getRegistros() != null && aoPC.getRegistros().getValue() != null
					&& aoPC.getRegistros().getValue().getPlatCuenta() != null) {
				for (PlatCuenta itemPC : aoPC.getRegistros().getValue().getPlatCuenta()) {
					oCtaAux = new Cuenta();
					oCtaAux.setIdMayorista(itemPC.getIdMayorista().getValue());
					oCtaAux.setIdCuenta(itemPC.getIdCuenta().getValue());
					oCtaAux.setAlias(itemPC.getAlias().getValue());
					oCtaAux.setCVU(itemPC.getCVU().getValue());
					oCtaAux.setIdProducto(itemPC.getProducto().getValue().getIdProducto().getValue());
					oCtaAux.setNumeroCuenta(itemPC.getNumeroCuenta().getValue());
					oCtaAux.setIdMoneda(itemPC.getMoneda().getValue().getIdMoneda().getValue());
					oCtaAux.setDescripcionMoneda(itemPC.getMoneda().getValue().getDescripcion().getValue());
					oCtaAux.setSimboloMoneda(itemPC.getMoneda().getValue().getTxtSimbolo().getValue());
					oCtaAux.setSaldo(itemPC.getSaldo().getValue());
					oCtaAux.setSaldoPendiente(itemPC.getSaldoPendiente().getValue());
					oCtaAux.setIdTipoCuenta(itemPC.getTipoCuenta().getValue().getIdTipoCuenta().getValue());
					oCtaAux.setCodMnemonicoTipoCuenta(itemPC.getTipoCuenta().getValue().getCodMnemonico().getValue());
					oCtaAux.setDescripcionTipoCuenta(itemPC.getTipoCuenta().getValue().getDescripcion().getValue());

					/*
					 * rf = null; rf =
					 * GestionServiceHelper.getGestionService(CfgTimeout.DEFAULT).obtenerSaldoActual
					 * (null, this.getUsuario().getIdMayorista(), itemPC.getIdCuenta().getValue());
					 * if (rf != null) { oCtaAux.setSaldo(rf.getValor().getValue()); } else {
					 * oCtaAux.setSaldo(0F); }
					 */
					oCtaAux.setProductoDescripcion(itemPC.getProducto().getValue().getDescProducto().getValue());
					if (itemPC.getLogo().getValue() != null) {
						oCtaAux.setLogo(itemPC.getLogo().getValue());
					} else {
						oCtaAux.setLogo(null);
					}
					cuentas.add(oCtaAux);
				}
			}
		}

		return cuentas;
	}

	public void setCuentas(List<Cuenta> cuentas) {
		this.cuentas = cuentas;
	}

	public List<SelectItem> getArancelesDebito() {
		StringBuilder oDescAra = null;

		if (arancelesDebito == null) {
			FiltroListadoPlatArancel oFlt = new FiltroListadoPlatArancel();
			PlatArancel oAraFlt = new PlatArancel();
			PlatTipoMovimiento oTipMov = new PlatTipoMovimiento();
			oTipMov.setCodMnemonico(oGestionObjFac.createPlatTipoMovimientoCodMnemonico("CMEPD"));
			boolean aplicaArancel = false;
			GregorianCalendar gcFechaVigencia = new GregorianCalendar();
			gcFechaVigencia.setTime(new Date());
			XMLGregorianCalendar xmlGCFechaVigencia;

			try {
				xmlGCFechaVigencia = DatatypeFactory.newInstance().newXMLGregorianCalendar(gcFechaVigencia);

				// --->>>oAraFlt.setIdCliente(oGestionObjFac.createPlatArancelIdCliente(this.getUsuario().getIdCliente()));
				oAraFlt.setArancelExclusivo(oGestionObjFac.createPlatArancelArancelExclusivo(true));
				oAraFlt.setEstado(oGestionObjFac.createPlatArancelEstado(1)); // Activos
				oAraFlt.setTipoMovimiento(oGestionObjFac.createPlatArancelTipoMovimiento(oTipMov));
				oAraFlt.setFechaInicioVigencia(xmlGCFechaVigencia);

				oFlt.setArancel(oGestionObjFac.createFiltroListadoPlatArancelArancel(oAraFlt));
				oFlt.setPage(oGestionObjFac.createFiltroListadoPlatArancelPage(0));
				oFlt.setPageSize(oGestionObjFac.createFiltroListadoPlatArancelPageSize(0));

				PlatArancelList oLstArancel = GestionServiceHelper.getGestionService(CfgTimeout.DEFAULT)
						.listarPlatArancel(oFlt);

				if (oLstArancel != null && oLstArancel.getTotalRegistros().getValue().compareTo(0) > 0) {
					arancelesDebito = new ArrayList<SelectItem>();

					for (PlatArancel oAran : oLstArancel.getRegistros().getValue().getPlatArancel()) {
						oDescAra = new StringBuilder();

						oDescAra.append(oAran.getDescripcion().getValue());
						oDescAra.append(" (");

						if ("POR".equals(oAran.getTipoCalculoArancel().getValue())) {
							if (oAran.getPorcArancel().getValue().compareTo(0f) > 0) {
								oDescAra.append(oAran.getPorcArancel().getValue());
								oDescAra.append("%");

								for(PlatImpuesto oImp : oAran.getImpuestos().getValue().getPlatImpuesto()) {
									if ("IVA".equals(oImp.getCodMnemonicoGrupo().getValue())) {
										oDescAra.append(" + " + oImp.getPorAlicuota().getValue() + "% IVA");
										break;
									}
								}
								
								aplicaArancel = true;
							}

							if (oAran.getImpBaseArancelMinimo().getValue().compareTo(0f) > 0) {
								oDescAra.append(" [Minimo fijo de $");
								oDescAra.append(oAran.getImpBaseArancelMinimo().getValue());

								for(PlatImpuesto oImp : oAran.getImpuestos().getValue().getPlatImpuesto()) {
									if ("IVA".equals(oImp.getCodMnemonicoGrupo().getValue())) {
										oDescAra.append(" + " + oImp.getPorAlicuota().getValue() + "% IVA");
										break;
									}
								}

								oDescAra.append("]");
								
								aplicaArancel = true;
							}
						} else if ("IMFI".equals(oAran.getTipoCalculoArancel().getValue())) {
							if (oAran.getImpArancel().getValue().compareTo(0f) > 0) {
								oDescAra.append(oAran.getImpArancel().getValue());
								oDescAra.append("Fijo $");
								oDescAra.append(oAran.getImpArancel().getValue());

								for(PlatImpuesto oImp : oAran.getImpuestos().getValue().getPlatImpuesto()) {
									if ("IVA".equals(oImp.getCodMnemonicoGrupo().getValue())) {
										oDescAra.append(" + " + oImp.getPorAlicuota().getValue() + "% IVA");
										break;
									}
								}

								aplicaArancel = true;
							}

							if (oAran.getImpBaseArancelMinimo().getValue().compareTo(0f) > 0) {
								oDescAra.append(" [Minimo fijo de $");
								oDescAra.append(oAran.getImpBaseArancelMinimo().getValue());

								for(PlatImpuesto oImp : oAran.getImpuestos().getValue().getPlatImpuesto()) {
									if ("IVA".equals(oImp.getCodMnemonicoGrupo().getValue())) {
										oDescAra.append(" + " + oImp.getPorAlicuota().getValue() + "% IVA");
										break;
									}
								}

								oDescAra.append("]");

								aplicaArancel = true;
							}
						} else if ("MIX".equals(oAran.getTipoCalculoArancel().getValue())) {
							if (oAran.getPorcArancel().getValue().compareTo(0f) > 0) {
								oDescAra.append(oAran.getPorcArancel().getValue());
								oDescAra.append("%");
								aplicaArancel = true;
							}

							if (oAran.getImpArancel().getValue().compareTo(0f) > 0) {
								oDescAra.append(" Adic. Fijo $");
								oDescAra.append(oAran.getImpArancel().getValue());
								aplicaArancel = true;
							}

							for(PlatImpuesto oImp : oAran.getImpuestos().getValue().getPlatImpuesto()) {
								if ("IVA".equals(oImp.getCodMnemonicoGrupo().getValue())) {
									oDescAra.append(" + " + oImp.getPorAlicuota().getValue() + "% IVA");
									break;
								}
							}

							if (oAran.getImpBaseArancelMinimo().getValue().compareTo(0f) > 0) {
								oDescAra.append(" [Minimo fijo de $");
								oDescAra.append(oAran.getImpBaseArancelMinimo().getValue());
								oDescAra.append("]");

								for(PlatImpuesto oImp : oAran.getImpuestos().getValue().getPlatImpuesto()) {
									if ("IVA".equals(oImp.getCodMnemonicoGrupo().getValue())) {
										oDescAra.append(" + " + oImp.getPorAlicuota().getValue() + "% IVA");
										break;
									}
								}

								aplicaArancel = true;
							}
						}

						oDescAra.append(")");

						if (!aplicaArancel)
							oDescAra.delete(oDescAra.length() - 2, oDescAra.length());

						arancelesDebito.add(new SelectItem(oAran.getCodMnemonico().getValue(), oDescAra.toString()));
					}
				}
			} catch (DatatypeConfigurationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		return arancelesDebito;
	}

	public void setArancelesDebito(List<SelectItem> arancelesDebito) {
		this.arancelesDebito = arancelesDebito;
	}

	public List<SelectItem> getArancelesCredito() {
		if (arancelesCredito == null) {
			FiltroListadoPlatArancel oFlt = new FiltroListadoPlatArancel();
			PlatArancel oAraFlt = new PlatArancel();
			PlatTipoMovimiento oTipMov = new PlatTipoMovimiento();
			oTipMov.setCodMnemonico(oGestionObjFac.createPlatTipoMovimientoCodMnemonico("CMEPC"));
			StringBuilder oDescAra = null;
			boolean aplicaArancel = false;
			GregorianCalendar gcFechaVigencia = new GregorianCalendar();
			gcFechaVigencia.setTime(new Date());
			XMLGregorianCalendar xmlGCFechaVigencia;

			try {
				xmlGCFechaVigencia = DatatypeFactory.newInstance().newXMLGregorianCalendar(gcFechaVigencia);

				// -->>>oAraFlt.setIdCliente(oGestionObjFac.createPlatArancelIdCliente(this.getUsuario().getIdCliente()));
				oAraFlt.setArancelExclusivo(oGestionObjFac.createPlatArancelArancelExclusivo(true));
				oAraFlt.setEstado(oGestionObjFac.createPlatArancelEstado(1)); // Activos
				oAraFlt.setTipoMovimiento(oGestionObjFac.createPlatArancelTipoMovimiento(oTipMov));
				oAraFlt.setFechaInicioVigencia(xmlGCFechaVigencia);

				oFlt.setArancel(oGestionObjFac.createFiltroListadoPlatArancelArancel(oAraFlt));
				oFlt.setPage(oGestionObjFac.createFiltroListadoPlatArancelPage(0));
				oFlt.setPageSize(oGestionObjFac.createFiltroListadoPlatArancelPageSize(0));

				PlatArancelList oLstArancel = GestionServiceHelper.getGestionService(CfgTimeout.DEFAULT)
						.listarPlatArancel(oFlt);

				if (oLstArancel != null && oLstArancel.getTotalRegistros().getValue().compareTo(0) > 0) {
					arancelesCredito = new ArrayList<SelectItem>();

					for (PlatArancel oAran : oLstArancel.getRegistros().getValue().getPlatArancel()) {
						oDescAra = new StringBuilder();

						oDescAra.append(oAran.getDescripcion().getValue());
						oDescAra.append(" (");

						if ("POR".equals(oAran.getTipoCalculoArancel().getValue())) {
							if (oAran.getPorcArancel().getValue().compareTo(0f) > 0) {
								oDescAra.append(oAran.getPorcArancel().getValue());
								oDescAra.append("%");

								for(PlatImpuesto oImp : oAran.getImpuestos().getValue().getPlatImpuesto()) {
									if ("IVA".equals(oImp.getCodMnemonicoGrupo().getValue())) {
										oDescAra.append(" + " + oImp.getPorAlicuota().getValue() + "% IVA");
										break;
									}
								}

								aplicaArancel = true;
							}

							if (oAran.getImpBaseArancelMinimo().getValue().compareTo(0f) > 0) {
								oDescAra.append(" [Minimo fijo de $");
								oDescAra.append(oAran.getImpBaseArancelMinimo().getValue());

								for(PlatImpuesto oImp : oAran.getImpuestos().getValue().getPlatImpuesto()) {
									if ("IVA".equals(oImp.getCodMnemonicoGrupo().getValue())) {
										oDescAra.append(" + " + oImp.getPorAlicuota().getValue() + "% IVA");
										break;
									}
								}

								oDescAra.append("]");

								aplicaArancel = true;
							}
						} else if ("IMFI".equals(oAran.getTipoCalculoArancel().getValue())) {
							if (oAran.getImpArancel().getValue().compareTo(0f) > 0) {
								oDescAra.append(oAran.getImpArancel().getValue());
								oDescAra.append("Fijo $");
								oDescAra.append(oAran.getImpArancel().getValue());

								for(PlatImpuesto oImp : oAran.getImpuestos().getValue().getPlatImpuesto()) {
									if ("IVA".equals(oImp.getCodMnemonicoGrupo().getValue())) {
										oDescAra.append(" + " + oImp.getPorAlicuota().getValue() + "% IVA");
										break;
									}
								}

								aplicaArancel = true;
							}

							if (oAran.getImpBaseArancelMinimo().getValue().compareTo(0f) > 0) {
								oDescAra.append(" [Minimo fijo de $");
								oDescAra.append(oAran.getImpBaseArancelMinimo().getValue());

								for(PlatImpuesto oImp : oAran.getImpuestos().getValue().getPlatImpuesto()) {
									if ("IVA".equals(oImp.getCodMnemonicoGrupo().getValue())) {
										oDescAra.append(" + " + oImp.getPorAlicuota().getValue() + "% IVA");
										break;
									}
								}

								oDescAra.append("]");

								aplicaArancel = true;
							}
						} else if ("MIX".equals(oAran.getTipoCalculoArancel().getValue())) {
							if (oAran.getPorcArancel().getValue().compareTo(0f) > 0) {
								oDescAra.append(oAran.getPorcArancel().getValue());
								oDescAra.append("%");
								aplicaArancel = true;
							}

							if (oAran.getImpArancel().getValue().compareTo(0f) > 0) {
								oDescAra.append(" Adic. Fijo $");
								oDescAra.append(oAran.getImpArancel().getValue());
								aplicaArancel = true;
							}

							for(PlatImpuesto oImp : oAran.getImpuestos().getValue().getPlatImpuesto()) {
								if ("IVA".equals(oImp.getCodMnemonicoGrupo().getValue())) {
									oDescAra.append(" + " + oImp.getPorAlicuota().getValue() + "% IVA");
									break;
								}
							}

							if (oAran.getImpBaseArancelMinimo().getValue().compareTo(0f) > 0) {
								oDescAra.append(" [Minimo fijo de $");
								oDescAra.append(oAran.getImpBaseArancelMinimo().getValue());
								oDescAra.append("]");

								for(PlatImpuesto oImp : oAran.getImpuestos().getValue().getPlatImpuesto()) {
									if ("IVA".equals(oImp.getCodMnemonicoGrupo().getValue())) {
										oDescAra.append(" + " + oImp.getPorAlicuota().getValue() + "% IVA");
										break;
									}
								}

								aplicaArancel = true;
							}
						}

						oDescAra.append(")");

						if (!aplicaArancel)
							oDescAra.delete(oDescAra.length() - 2, oDescAra.length());

						arancelesCredito.add(new SelectItem(oAran.getCodMnemonico().getValue(), oDescAra.toString()));
					}
				}
			} catch (DatatypeConfigurationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		return arancelesCredito;
	}

	public void setArancelesCredito(List<SelectItem> arancelesCredito) {
		this.arancelesCredito = arancelesCredito;
	}

	public List<SelectItem> getArancelesDebitoExtra() {
		return arancelesDebitoExtra;
	}

	public void setArancelesDebitoExtra(List<SelectItem> arancelesDebitoExtra) {
		this.arancelesDebitoExtra = arancelesDebitoExtra;
	}

	public List<SelectItem> getArancelesCreditoExtra() {
		return arancelesCreditoExtra;
	}

	public void setArancelesCreditoExtra(List<SelectItem> arancelesCreditoExtra) {
		this.arancelesCreditoExtra = arancelesCreditoExtra;
	}

	public List<SelectItem> getMonedas() {
		if (monedas == null) {
			FiltroListadoPlatMoneda oFlt = new FiltroListadoPlatMoneda();
			oFlt.setPage(oGestionObjFac.createFiltroListadoMonedaPage(0));
			oFlt.setPageSize(oGestionObjFac.createFiltroListadoMonedaPageSize(0));

			PlatMonedaList oLstMon = GestionServiceHelper.getGestionService(CfgTimeout.DEFAULT).listarPlatMoneda(oFlt);

			if (oLstMon != null && oLstMon.getTotalRegistros().getValue().compareTo(0) > 0) {
				monedas = new ArrayList<SelectItem>();

				for (PlatMoneda oMon : oLstMon.getRegistros().getValue().getPlatMoneda())
					monedas.add(new SelectItem(oMon.getIdMoneda().getValue(), oMon.getDescripcion().getValue()));
			}
		}

		return monedas;
	}

	public void setMonedas(List<SelectItem> monedas) {
		this.monedas = monedas;
	}

	public List<SelectItem> getCuentasCfgAranceles() {
		return cuentasCfgAranceles;
	}

	public void setCuentasCfgAranceles(List<SelectItem> cuentasCfgAranceles) {
		this.cuentasCfgAranceles = cuentasCfgAranceles;
	}

	public String getSeleccionArancelDebito() {
		return seleccionArancelDebito;
	}

	public void setSeleccionArancelDebito(String seleccionArancelDebito) {
		this.seleccionArancelDebito = seleccionArancelDebito;
	}

	public String getSeleccionArancelCredito() {
		return seleccionArancelCredito;
	}

	public void setSeleccionArancelCredito(String seleccionArancelCredito) {
		this.seleccionArancelCredito = seleccionArancelCredito;
	}

	public String getSeleccionIdMoneda() {
		return seleccionIdMoneda;
	}

	public void setSeleccionIdMoneda(String seleccionIdMoneda) {
		this.seleccionIdMoneda = seleccionIdMoneda;
	}

	public List<SelectItem> getEntidadesFinancierasOrigen() {
		if (entidadesFinancierasOrigen == null) {
			FiltroListadoPlatEntidadFinanciera oFlt = new FiltroListadoPlatEntidadFinanciera();
			// Se filtra solo la moneda ARS por el momento
			PlatEntidadFinanciera oEntFin = new PlatEntidadFinanciera();
			oEntFin.setEstado(oGestionObjFac.createPlatEntidadFinancieraEstado(1));

			oFlt.setEntidadFinanciera(
					oGestionObjFac.createFiltroListadoPlatEntidadFinancieraEntidadFinanciera(oEntFin));
			oFlt.setPage(oGestionObjFac.createFiltroListadoPlatEntidadFinancieraPage(0));
			oFlt.setPageSize(oGestionObjFac.createFiltroListadoPlatEntidadFinancieraPageSize(0));

			PlatEntidadFinancieraList oLstEFO = GestionServiceHelper.getGestionService(CfgTimeout.DEFAULT)
					.listarPlatEntidadFinanciera(oFlt);

			if (oLstEFO != null && oLstEFO.getTotalRegistros().getValue().compareTo(0) > 0) {
				entidadesFinancierasOrigen = new ArrayList<SelectItem>();

				for (PlatEntidadFinanciera oEF : oLstEFO.getRegistros().getValue().getPlatEntidadFinanciera())
					if (!"0".equals(oEF.getIdBancoBCRA().getValue()))
						entidadesFinancierasOrigen
								.add(new SelectItem(oEF.getId().getValue(), oEF.getDenominacion().getValue()));
			}
		}

		return entidadesFinancierasOrigen;
	}

	public void setEntidadesFinancierasOrigen(List<SelectItem> entidadesFinancierasOrigen) {
		this.entidadesFinancierasOrigen = entidadesFinancierasOrigen;
	}

	public List<SelectItem> getEntidadesFinancierasDestino() {
		if (entidadesFinancierasDestino == null) {
			FiltroListadoPlatEntidadFinanciera oFlt = new FiltroListadoPlatEntidadFinanciera();
			// Se filtra solo la moneda ARS por el momento
			PlatEntidadFinanciera oEntFin = new PlatEntidadFinanciera();
			oEntFin.setEstado(oGestionObjFac.createPlatEntidadFinancieraEstado(1));

			oFlt.setEntidadFinanciera(
					oGestionObjFac.createFiltroListadoPlatEntidadFinancieraEntidadFinanciera(oEntFin));
			oFlt.setPage(oGestionObjFac.createFiltroListadoPlatEntidadFinancieraPage(0));
			oFlt.setPageSize(oGestionObjFac.createFiltroListadoPlatEntidadFinancieraPageSize(0));

			PlatEntidadFinancieraList oLstEFO = GestionServiceHelper.getGestionService(CfgTimeout.DEFAULT)
					.listarPlatEntidadFinanciera(oFlt);

			if (oLstEFO != null && oLstEFO.getTotalRegistros().getValue().compareTo(0) > 0) {
				entidadesFinancierasDestino = new ArrayList<SelectItem>();

				for (PlatEntidadFinanciera oEF : oLstEFO.getRegistros().getValue().getPlatEntidadFinanciera())
					entidadesFinancierasDestino
							.add(new SelectItem(oEF.getId().getValue(), oEF.getDenominacion().getValue()));
			}

		}

		return entidadesFinancierasDestino;
	}

	public void setEntidadesFinancierasDestino(List<SelectItem> entidadesFinancierasDestino) {
		this.entidadesFinancierasDestino = entidadesFinancierasDestino;
	}

	public List<SelectItem> getEntidadesFinancierasAgDest() {
		if (entidadesFinancierasAgDest == null && itemAgendaDestinatario != null
				&& itemAgendaDestinatario.getIdTipoIdentifCta() != null) {
			FiltroListadoPlatEntidadFinanciera oFlt = new FiltroListadoPlatEntidadFinanciera();
			Integer nroAux = 0;

			PlatEntidadFinanciera oEntFin = new PlatEntidadFinanciera();

			if (this.operacion != "DETALLE") {
				if (this.getTipoIdentificacionCuenta() != null
						&& "CTASP".equals(this.getTipoIdentificacionCuenta().getCodMnemonico())) {
					oEntFin.setCodMnemonico(oGestionObjFac.createPlatEntidadFinancieraCodMnemonico("SUPERPAGO"));
				} else if (this.getTipoIdentificacionCuenta() != null
						&& "CBU".equals(this.getTipoIdentificacionCuenta().getCodMnemonico())
						&& (this.itemAgendaDestinatario.getCodigoIdentifCta() == null
								|| this.itemAgendaDestinatario.getCodigoIdentifCta().isEmpty())) {
					entidadesFinancierasAgDest = null;

					return null;
				} else if (this.getTipoIdentificacionCuenta() != null
						&& "CBU".equals(this.getTipoIdentificacionCuenta().getCodMnemonico())) {
					try {
						nroAux = Integer.parseInt(itemAgendaDestinatario.getCodigoIdentifCta().substring(0, 3));
						oEntFin.setIdBancoBCRA(
								oGestionObjFac.createPlatEntidadFinancieraIdBancoBCRA(nroAux.toString()));
					} catch (Exception e) {
						FacesContext.getCurrentInstance().validationFailed();
						FacesContext.getCurrentInstance().addMessage("msgsProductos",
								new FacesMessage(FacesMessage.SEVERITY_INFO, "El CBU ingresado es invalido.", null));
						PrimeFaces.current().ajax().update("msgsProductos");

						entidadesFinancierasAgDest = null;

						return entidadesFinancierasAgDest;
					}
				}
			}

			oEntFin.setEstado(oGestionObjFac.createPlatEntidadFinancieraEstado(1));

			oFlt.setEntidadFinanciera(
					oGestionObjFac.createFiltroListadoPlatEntidadFinancieraEntidadFinanciera(oEntFin));
			oFlt.setPage(oGestionObjFac.createFiltroListadoPlatEntidadFinancieraPage(0));
			oFlt.setPageSize(oGestionObjFac.createFiltroListadoPlatEntidadFinancieraPageSize(0));

			PlatEntidadFinancieraList oLstEFO = GestionServiceHelper.getGestionService(CfgTimeout.DEFAULT)
					.listarPlatEntidadFinanciera(oFlt);

			if (oLstEFO != null && oLstEFO.getTotalRegistros().getValue().compareTo(0) > 0) {
				entidadesFinancierasAgDest = new ArrayList<SelectItem>();

				for (PlatEntidadFinanciera oEF : oLstEFO.getRegistros().getValue().getPlatEntidadFinanciera())
					if (!"0".equals(oEF.getIdBancoBCRA().getValue()))
						entidadesFinancierasAgDest
								.add(new SelectItem(oEF.getId().getValue(), oEF.getDenominacion().getValue()));
			} else {
				if (this.getTipoIdentificacionCuenta() != null
						&& "CBU".equals(this.getTipoIdentificacionCuenta().getCodMnemonico())) {
					FacesContext.getCurrentInstance().validationFailed();
					FacesContext.getCurrentInstance().addMessage("msgsProductos",
							new FacesMessage(FacesMessage.SEVERITY_INFO,
									"No se encuentra configurado el Banco para el CBU informado.", null));
					PrimeFaces.current().ajax().update("msgsProductos");

					entidadesFinancierasAgDest = null;

					return entidadesFinancierasAgDest;
				}
			}
		}

		return entidadesFinancierasAgDest;
	}

	public void setEntidadesFinancierasAgDest(List<SelectItem> entidadesFinancierasAgDest) {
		this.entidadesFinancierasAgDest = entidadesFinancierasAgDest;
	}

	public ArrayList<TipoIdentificacionCta> getTiposIdentificacionCuenta() {
		if (tiposIdentificacionCuenta == null) {
			TipoIdentificacionCta oTipIdCta = null;
			FiltroListadoPlatTipoIdentifCta oFlt = new FiltroListadoPlatTipoIdentifCta();
			// Se filtra solo la moneda ARS por el momento
			PlatTipoIdentifCta oTipIdenCta = new PlatTipoIdentifCta();
			oTipIdenCta.setEstado(oGestionObjFac.createPlatTipoIdentifCtaEstado(1));

			oFlt.setPlatTipoIdentifCta(
					oGestionObjFac.createFiltroListadoPlatTipoIdentifCtaPlatTipoIdentifCta(oTipIdenCta));
			oFlt.setPage(oGestionObjFac.createFiltroListadoPlatTipoIdentifCtaPage(0));
			oFlt.setPageSize(oGestionObjFac.createFiltroListadoPlatTipoIdentifCtaPageSize(0));

			PlatTipoIdentifCtaList oLstTidCta = GestionServiceHelper.getGestionService(CfgTimeout.DEFAULT)
					.listarPlatTipoIdentifCta(oFlt);

			if (oLstTidCta != null && oLstTidCta.getTotalRegistros().getValue().compareTo(0) > 0) {
				tiposIdentificacionCuenta = new ArrayList<TipoIdentificacionCta>();

				for (PlatTipoIdentifCta oTIC : oLstTidCta.getRegistros().getValue().getPlatTipoIdentifCta()) {
					oTipIdCta = new TipoIdentificacionCta();
					oTipIdCta.setIdTipoIdentifCta(oTIC.getIdTipoIdentifCta().getValue());
					oTipIdCta.setCodMnemonico(oTIC.getCodMnemonico().getValue());
					oTipIdCta.setDescripcion(oTIC.getDescripcion().getValue());
					oTipIdCta.setEstado(oTIC.getEstado().getValue());
					tiposIdentificacionCuenta.add(oTipIdCta);
				}
			}
		}

		return tiposIdentificacionCuenta;
	}

	public void setTiposIdentificacionCuenta(ArrayList<TipoIdentificacionCta> tiposIdentificacionCuenta) {
		this.tiposIdentificacionCuenta = tiposIdentificacionCuenta;
	}

	public List<SelectItem> getTiposIdentificacionTributaria() {
		if (tiposIdentificacionTributaria == null) {
			FiltroListadoPlatTipoDocumento oFlt = null;
			PlatTipoDocumentoList oLstTD = null;
			ArrayOfString codMnemIncl = null;

			oFlt = new FiltroListadoPlatTipoDocumento();
			oFlt.setPage(oGestionObjFac.createFiltroListadoPlatTipoDocumentoPage(0));
			oFlt.setPageSize(oGestionObjFac.createFiltroListadoPlatTipoDocumentoPageSize(Integer.MAX_VALUE));

			codMnemIncl = new ArrayOfString();
			codMnemIncl.getString().add("CUIT");
			codMnemIncl.getString().add("CUIL");
			oFlt.setCodMnemonicosIncluTipoDoc(
					oGestionObjFac.createFiltroListadoPlatTipoDocumentoCodMnemonicosIncluTipoDoc(codMnemIncl));

			oLstTD = GestionServiceHelper.getGestionService(CfgTimeout.DEFAULT).listarPlatTipoDocumento(oFlt);

			if (oLstTD != null && oLstTD.getTotalRegistros().getValue().compareTo(0) > 0) {
				tiposIdentificacionTributaria = new ArrayList<SelectItem>();

				for (PlatTipoDocumento oTD : oLstTD.getRegistros().getValue().getPlatTipoDocumento())
					tiposIdentificacionTributaria
							.add(new SelectItem(oTD.getIdTipoDocumento().getValue(), oTD.getDescripcion().getValue()));
			}

		}

		return tiposIdentificacionTributaria;
	}

	public void setTiposIdentificacionTributaria(List<SelectItem> tiposIdentificacionTributaria) {
		this.tiposIdentificacionTributaria = tiposIdentificacionTributaria;
	}

	public ObjectFactory getoGestionObjFac() {
		return oGestionObjFac;
	}

	public void setoGestionObjFac(ObjectFactory oGestionObjFac) {
		this.oGestionObjFac = oGestionObjFac;
	}

	public String getSeleccionIdTipoIdentifTributaria() {
		return seleccionIdTipoIdentifTributaria;
	}

	public void setSeleccionIdTipoIdentifTributaria(String seleccionIdTipoIdentifTributaria) {
		this.seleccionIdTipoIdentifTributaria = seleccionIdTipoIdentifTributaria;
	}

	public LazyAgendaDestinatarioDataModel getListaDeAgendaDestinatarios() {
		return listaDeAgendaDestinatarios;
	}

	public void setListaDeAgendaDestinatarios(LazyAgendaDestinatarioDataModel listaDeAgendaDestinatarios) {
		this.listaDeAgendaDestinatarios = listaDeAgendaDestinatarios;
	}

	// INICIO Metodos de Agenda Destinatarios
	public void detalleDestinatario() {
		if (itemAgendaDestinatario != null) {
			operacion = "DETALLE";
			this.muestraCRUDAdmDestCta = true;
			TipoIdentificacionCta oTIC = new TipoIdentificacionCta(itemAgendaDestinatario.getIdTipoIdentifCta());
			// Carga el objeto completo de la coleccion
			this.setTipoIdentificacionCuenta(
					this.getTiposIdentificacionCuenta().get(this.getTiposIdentificacionCuenta().indexOf(oTIC)));

			PrimeFaces.current().ajax().update("admDestCtaDialog");
			PrimeFaces.current().executeScript("PF('admDestCtaDialogWV').show()");
		} else {
			FacesContext.getCurrentInstance().addMessage("msgsProductos", new FacesMessage(FacesMessage.SEVERITY_WARN,
					"Debe seleccionar un destinatario para realizar la operacion.", null));
			PrimeFaces.current().ajax().update("msgsProductos");
		}
	}

	public void altaDestinatario() {
		operacion = "ALTA";
		itemAgendaDestinatario = new ItemAgendaDestinatario();
		this.muestraCRUDAdmDestCta = true;
		this.entidadesFinancierasAgDest = null;
		this.tipoIdentificacionCuenta = null;
		PrimeFaces.current().ajax().update("admDestCtaDialog");
		PrimeFaces.current().executeScript("PF('admDestCtaDialogWV').show()");
	}

	public void modificacionDestinatario() {
		if (itemAgendaDestinatario != null) {
			operacion = "MODIFICACION";
			this.muestraCRUDAdmDestCta = true;
			TipoIdentificacionCta oTIC = new TipoIdentificacionCta(itemAgendaDestinatario.getIdTipoIdentifCta());
			// Carga el objeto completo de la coleccion
			this.setTipoIdentificacionCuenta(
					this.getTiposIdentificacionCuenta().get(this.getTiposIdentificacionCuenta().indexOf(oTIC)));
			this.setEntidadesFinancierasAgDest(null);
			PrimeFaces.current().ajax().update("admDestCtaDialog");
			PrimeFaces.current().executeScript("PF('admDestCtaDialogWV').show()");
		} else {
			FacesContext.getCurrentInstance().addMessage("msgsProductos", new FacesMessage(FacesMessage.SEVERITY_WARN,
					"Debe seleccionar un destinatario para realizar la operacion.", null));
			PrimeFaces.current().ajax().update("msgsProductos");
		}
	}

	public void eliminacionDestinatario() {
		if (itemAgendaDestinatario != null) {
			this.muestraCRUDAdmDestCta = false;
			PrimeFaces.current().ajax().update("confirmaDelDestinatarioDialog");
			PrimeFaces.current().executeScript("PF('confirmaDelDestinatarioDialogWV').show()");
		} else {
			FacesContext.getCurrentInstance().addMessage("msgsProductos", new FacesMessage(FacesMessage.SEVERITY_WARN,
					"Debe seleccionar un destinatario para realizar la operacion.", null));
			PrimeFaces.current().ajax().update("msgsProductos");
		}

	}

	public void cancelarDialogoDestinatario() {
		operacion = "";
		this.muestraCRUDAdmDestCta = false;
		this.entidadesFinancierasAgDest = null;
		this.tipoIdentificacionCuenta = null;
		this.setItemAgendaDestinatario(null);
		PrimeFaces.current().executeScript("PF('admDestCtaDialogWV').hide();");
	}

	public void grabarItmAgendaDestinatario() {
		PlatAgendaDestinatarioCta oAGDest = null;
		PlatEntidadFinanciera oEnFi = null;
		PlatTipoIdentifCta oTiIdeCta = null;
		PlatTipoDocumento oTiIdenTrib = null;
		PlatEstadoAgendaDestCta oEstAgDes = null;
		Cliente oCli = null;
		RespString oRTA = null;
		ArrayList<String> lstUpdate = new ArrayList<String>();

		oAGDest = new PlatAgendaDestinatarioCta();

		oAGDest.setIdAgendaDestinatarioCta(oGestionObjFac.createPlatAgendaDestinatarioCtaIdAgendaDestinatarioCta(
				itemAgendaDestinatario.getIdAgendaDestinatarioCta()));
		oAGDest.setCodigoIdentifCta(oGestionObjFac
				.createPlatAgendaDestinatarioCtaCodigoIdentifCta(itemAgendaDestinatario.getCodigoIdentifCta()));
		oAGDest.setEmailNotifTransfer(oGestionObjFac
				.createPlatAgendaDestinatarioCtaEmailNotifTransfer(itemAgendaDestinatario.getEmailNotifTransfer()));

		oEnFi = new PlatEntidadFinanciera();
		oEnFi.setId(oGestionObjFac.createPlatEntidadFinancieraId(itemAgendaDestinatario.getIdEntidadFinanciera()));
		oAGDest.setEntidadFinanciera(oGestionObjFac.createPlatAgendaDestinatarioCtaEntidadFinanciera(oEnFi));

		oAGDest.setEsCtaCte(
				oGestionObjFac.createPlatAgendaDestinatarioCtaEsCtaCte(itemAgendaDestinatario.getEsCtaCte()));
		oAGDest.setEsCtaPropia(oGestionObjFac
				.createPlatAgendaDestinatarioCtaEsCtaPropia(itemAgendaDestinatario.getEsCtaPropia()));
		oCli = new Cliente();
		oCli.setIdCliente(oGestionObjFac.createClienteIdCliente(this.getUsuario().getIdCliente()));
		oAGDest.setCliente(oGestionObjFac.createPlatAgendaDestinatarioCtaCliente(oCli));
		oAGDest.setIdMayorista(
				oGestionObjFac.createPlatAgendaDestinatarioCtaIdMayorista(this.getUsuario().getIdMayorista()));
		oAGDest.setIdUsuario(oGestionObjFac.createPlatAgendaDestinatarioCtaIdUsuario(this.getUsuario().getIdUsuario()));

		oAGDest.setLimiteTransferencia(oGestionObjFac.createPlatAgendaDestinatarioCtaLimiteTransferencia(
				(itemAgendaDestinatario.getLimiteTransferencia() != null
						? itemAgendaDestinatario.getLimiteTransferencia()
						: 0f)));

		oTiIdenTrib = new PlatTipoDocumento();
		oTiIdenTrib.setIdTipoDocumento(
				oGestionObjFac.createPlatTipoDocumentoIdTipoDocumento(itemAgendaDestinatario.getIdTipoIdenTrib()));
		oAGDest.setTipoIdenTrib(oGestionObjFac.createPlatAgendaDestinatarioCtaTipoIdenTrib(oTiIdenTrib));

		oAGDest.setNroIdenTrib(
				oGestionObjFac.createPlatAgendaDestinatarioCtaNroIdenTrib(itemAgendaDestinatario.getNroIdenTrib()));
		oAGDest.setReferencia(
				oGestionObjFac.createPlatAgendaDestinatarioCtaReferencia(itemAgendaDestinatario.getReferencia()));

		oTiIdeCta = new PlatTipoIdentifCta();
		oTiIdeCta.setIdTipoIdentifCta(
				oGestionObjFac.createPlatTipoIdentifCtaIdTipoIdentifCta(itemAgendaDestinatario.getIdTipoIdentifCta()));
		oAGDest.setTipoIdentifCta(oGestionObjFac.createPlatAgendaDestinatarioCtaTipoIdentifCta(oTiIdeCta));
		oAGDest.setTitular(oGestionObjFac.createPlatAgendaDestinatarioCtaTitular(itemAgendaDestinatario.getTitular()));
		oAGDest.setVistaPublica(oGestionObjFac.createPlatAgendaDestinatarioCtaVistaPublica(false));

		oEstAgDes = new PlatEstadoAgendaDestCta();
		oEstAgDes.setIdEstadoAgendaDestCta(oGestionObjFac
				.createPlatEstadoAgendaDestCtaIdEstadoAgendaDestCta(itemAgendaDestinatario.getIdEstado()));
		oAGDest.setEstado(oGestionObjFac.createPlatAgendaDestinatarioCtaEstado(oEstAgDes));

		if ("ALTA".equals(this.getOperacion()))
			oRTA = GestionServiceHelper.getGestionService(CfgTimeout.DEFAULT).agregarPlatAgendaDestinatarioCta(this.getHeaderSeguridad(), oAGDest);
		else
			oRTA = GestionServiceHelper.getGestionService(CfgTimeout.DEFAULT).modificarPlatAgendaDestinatarioCta(this.getHeaderSeguridad(), oAGDest);

		if (oRTA != null && oRTA.getError() != null
				&& oRTA.getError().getValue().getHayError().getValue().booleanValue()) {
			LogACGHelper.escribirLog(null,
					"grabarItmAgendaDestinatario. |" + oRTA.getError().getValue().getCodigoError().getValue() + " - "
							+ oRTA.getError().getValue().getMsgError().getValue() + "|");

			if ("ALTA".equals(this.getOperacion()))
				FacesContext.getCurrentInstance().addMessage(null,
						new FacesMessage(FacesMessage.SEVERITY_ERROR,
								"Alta de destinatario. " + oRTA.getError().getValue().getCodigoError().getValue()
										+ " - " + oRTA.getError().getValue().getMsgError().getValue(),
								null));
			else
				FacesContext.getCurrentInstance().addMessage(null,
						new FacesMessage(FacesMessage.SEVERITY_ERROR,
								"Modificacion de destinatario. "
										+ oRTA.getError().getValue().getCodigoError().getValue() + " - "
										+ oRTA.getError().getValue().getMsgError().getValue(),
								null));

			FacesContext.getCurrentInstance().validationFailed();
		} else {
			if ("ALTA".equals(this.getOperacion())) {
				FacesContext.getCurrentInstance().addMessage("msgsProductos", new FacesMessage(
						FacesMessage.SEVERITY_INFO, "Alta de destinatario realizada con exito.", null));
			} else
				FacesContext.getCurrentInstance().addMessage("msgsProductos", new FacesMessage(
						FacesMessage.SEVERITY_INFO, "Modificacion de destinatario realizada con exito.", null));

			lstUpdate.add("msgsProductos");
			lstUpdate.add("agendaDestDT");
			this.itemAgendaDestinatario = null;
			this.muestraCRUDAdmDestCta = false;
			this.itemsAgendaDestinatarios = null;
			this.itemsAgendaDestinatariosPublicos = null;
			inicializarAgendaDestinatarios();
			PrimeFaces.current().ajax().update(lstUpdate);
		}
	}

	public void configrmarEliminacionDestinatario() {
		PlatAgendaDestinatarioCta oAGDest = null;
		RespString oRTA = null;
		HeaderSecur oHeadSec = null;
		Cliente oCli = null;
		
		if (itemAgendaDestinatario != null) {
			if (!itemAgendaDestinatario.getVistaPublica()) {
				oAGDest = new PlatAgendaDestinatarioCta();
				oCli = new Cliente();
				oCli.setIdCliente(oGestionObjFac.createClienteIdCliente(this.getUsuario().getIdCliente()));
				oAGDest.setCliente(
						oGestionObjFac.createPlatAgendaDestinatarioCtaCliente(oCli));
				oAGDest.setIdMayorista(
						oGestionObjFac.createPlatAgendaDestinatarioCtaIdMayorista(this.getUsuario().getIdMayorista()));
				oAGDest.setIdAgendaDestinatarioCta(
						oGestionObjFac.createPlatAgendaDestinatarioCtaIdAgendaDestinatarioCta(
								itemAgendaDestinatario.getIdAgendaDestinatarioCta()));
				
				oHeadSec = new HeaderSecur();
				oHeadSec.setIdMayorista(oGestionObjFac.createHeaderSecurIdMayorista(this.getUsuario().getIdMayorista()));
				oHeadSec.setIdCliente(oGestionObjFac.createHeaderSecurIdCliente(this.getUsuario().getIdCliente()));
				oHeadSec.setIdUsuario(oGestionObjFac.createHeaderSecurIdUsuario(this.getUsuario().getIdUsuario()));
				oHeadSec.setUsuario(oGestionObjFac.createHeaderSecurUsuario(this.getUsuario().getUsername()));
				oHeadSec.setPassword(oGestionObjFac.createHeaderSecurPassword(this.getUsuario().getPassword()));

				oRTA = GestionServiceHelper.getGestionService(CfgTimeout.DEFAULT).eliminarPlatAgendaDestinatarioCta(oHeadSec, oAGDest);

				if (oRTA != null && oRTA.getError() != null && oRTA.getError().getValue().getHayError().getValue()) {
					LogACGHelper.escribirLog(null,
							"eliminarDestinatario. |" + oRTA.getError().getValue().getCodigoError().getValue() + " - "
									+ oRTA.getError().getValue().getMsgError().getValue() + "|");

					FacesContext.getCurrentInstance().addMessage("msgsProductos",
							new FacesMessage(FacesMessage.SEVERITY_ERROR,
									"Eliminacion de destinatario. "
											+ oRTA.getError().getValue().getCodigoError().getValue() + " - "
											+ oRTA.getError().getValue().getMsgError().getValue(),
									null));

					FacesContext.getCurrentInstance().validationFailed();
				} else {
					FacesContext.getCurrentInstance().addMessage("msgsProductos", new FacesMessage(
							FacesMessage.SEVERITY_INFO, "Eliminacion de destinatario realizada con exito.", null));

					itemAgendaDestinatario = null;
					inicializarAgendaDestinatarios();
				}
			} else {
				FacesContext.getCurrentInstance().addMessage("msgsProductos",
						new FacesMessage(FacesMessage.SEVERITY_WARN,
								"Debe seleccionar un destinatario para realizar la operacion.", null));
			}
		} else {
			FacesContext.getCurrentInstance().addMessage("msgsProductos", new FacesMessage(FacesMessage.SEVERITY_INFO,
					"El destinatario seleccionado no puede eliminarse.", null));
		}
	}

	// FIN Metodos de Agenda Destinatarios

	public ItemAgendaDestinatario getItemAgendaDestinatario() {
		return itemAgendaDestinatario;
	}

	public void setItemAgendaDestinatario(ItemAgendaDestinatario itemAgendaDestinatario) {
		this.itemAgendaDestinatario = itemAgendaDestinatario;
	}

	public String getOperacion() {
		return operacion;
	}

	public void setOperacion(String operacionAgendaDestinatario) {
		this.operacion = operacionAgendaDestinatario;
	}

	public boolean isMuestraCRUDAdmDestCta() {
		return muestraCRUDAdmDestCta;
	}

	public void setMuestraCRUDAdmDestCta(boolean muestraCRUDAdmDestCta) {
		this.muestraCRUDAdmDestCta = muestraCRUDAdmDestCta;
	}

	public boolean isMuestraCRUDSolicituDeDinero() {
		return muestraCRUDSolicituDeDinero;
	}

	public void setMuestraCRUDSolicituDeDinero(boolean muestraCRUDSolicituDeDinero) {
		this.muestraCRUDSolicituDeDinero = muestraCRUDSolicituDeDinero;
	}

	public List<SelectItem> getEstadosTransferencia() {
		if (estadosTransferencia == null) {
			FiltroListadoPlatEstadoTransferencia oFlt = new FiltroListadoPlatEstadoTransferencia();
			// Se filtra solo la moneda ARS por el momento
			PlatEstadoTransferencia oEstTrf = new PlatEstadoTransferencia();
			oEstTrf.setEstado(oGestionObjFac.createPlatEstadoTransferenciaEstado(1));

			oFlt.setEstadoTransferencia(
					oGestionObjFac.createFiltroListadoPlatEstadoTransferenciaEstadoTransferencia(oEstTrf));
			oFlt.setPage(oGestionObjFac.createFiltroListadoPlatEntidadFinancieraPage(0));
			oFlt.setPageSize(oGestionObjFac.createFiltroListadoPlatEntidadFinancieraPageSize(0));

			PlatEstadoTransferenciaList oLstET = GestionServiceHelper.getGestionService(CfgTimeout.DEFAULT)
					.listarPlatEstadoTransferencia(oFlt);

			if (oLstET != null && oLstET.getTotalRegistros().getValue().compareTo(0) > 0) {
				estadosTransferencia = new ArrayList<SelectItem>();

				for (PlatEstadoTransferencia oEF : oLstET.getRegistros().getValue().getPlatEstadoTransferencia())
					estadosTransferencia.add(
							new SelectItem(oEF.getIdEstadoTransferencia().getValue(), oEF.getDescripcion().getValue()));
			}

		}

		return estadosTransferencia;
	}

	public void setEstadosTransferencia(List<SelectItem> estadosTransferencia) {
		this.estadosTransferencia = estadosTransferencia;
	}

	public LazyTransferenciasDataModel getListaDeTransferencias() {
		return listaDeTransferencias;
	}

	public void setListaDeTransferencias(LazyTransferenciasDataModel listaDeTransferencias) {
		this.listaDeTransferencias = listaDeTransferencias;
	}

	public ItemTransferencia getItemTransferencia() {
		return itemTransferencia;
	}

	public void setItemTransferencia(ItemTransferencia itemTransferencia) {
		this.itemTransferencia = itemTransferencia;
	}

	public LazyMovimientosDeCuentaDataModel getMovimientosDeCuenta() {
		return movimientosDeCuenta;
	}

	public void setMovimientosDeCuenta(LazyMovimientosDeCuentaDataModel movimientosDeCuenta) {
		this.movimientosDeCuenta = movimientosDeCuenta;
	}

	public MovimientoDeCuenta getItemMovimientoDeCuenta() {
		return itemMovimientoDeCuenta;
	}

	public void setItemMovimientoDeCuenta(MovimientoDeCuenta itemMovimientoDeCuenta) {
		this.itemMovimientoDeCuenta = itemMovimientoDeCuenta;
	}

	public boolean isMuestraCRUDTransferencia() {
		return muestraCRUDTransferencia;
	}

	public void setMuestraCRUDTransferencia(boolean muestraCRUDTransferencia) {
		this.muestraCRUDTransferencia = muestraCRUDTransferencia;
	}

	public boolean isSolicitudDeDineroPropia() {
		return solicitudDeDineroPropia;
	}

	public void setSolicitudDeDineroPropia(boolean solicitudDeDineroPropia) {
		this.solicitudDeDineroPropia = solicitudDeDineroPropia;
	}

	// INICIO Metodos de Transferencias
	public void detalleTransferencia() {
		if (itemTransferencia != null) {
			try {
				operacion = "DETALLE";
				this.muestraCRUDTransferencia = true;
				/*
				 * this.setSeleccionIdCuenta(
				 * this.getUsuario().getSuperPagoInstance().getCuentaSeleccionada().getIdCuenta(
				 * ));
				 */

				if ("SUPERPAGO".equals(itemTransferencia.getCodMnemonicoEntOrigen())) {
					this.cuentaOrigen = new Cuenta();
					this.cuentaOrigen.setIdMayorista(this.getUsuario().getIdMayorista());
					this.cuentaOrigen.setIdCliente(this.getUsuario().getIdCliente());
					this.cuentaOrigen.setNumeroCuenta(itemTransferencia.getCodigoIdentifOrigen());
					this.cuentaOrigen = obtenerCuentaSuperPago(this.cuentaOrigen);
				}

				if ("SUPERPAGO".equals(itemTransferencia.getCodMnemonicoEntDestino())) {
					this.cuentaDestino = new Cuenta();
					this.cuentaDestino.setNumeroCuenta(itemTransferencia.getCodigoIdentifDestino());
					this.cuentaDestino = obtenerCuentaSuperPago(this.cuentaDestino);
				}

				/*----------*/
				//Agregar para que se visualicen los aranceles
				/*FiltroListadoPlatMovimientoCuenta oFlt = new FiltroListadoPlatMovimientoCuenta();
				PlatMovimientoCuenta oMovCtaFlt = new PlatMovimientoCuenta();
				PlatCuenta oCtaAdmin = null;

				oMovCtaFlt.setIdMayorista(oGestionObjFac.createPlatTransferenciaIdMayorista(this.getUsuario().getIdMayorista()));
				oMovCtaFlt.setIdCliente(oGestionObjFac.createPlatTransferenciaIdCliente(this.getUsuario().getIdCliente()));
				oMovCtaFlt.setIdMovimientoCuenta(oGestionObjFac.createPlatMovimientoCuentaIdMovimientoCuenta(this.getItemTransferencia().getIdTransferencia()));
				oMovCtaFlt.setAdmiteDetalleOrigen(oGestionObjFac.createPlatMovimientoCuentaAdmiteDetalleOrigen(false));
				oFlt.setMovimientoCuenta(oGestionObjFac.createFiltroListadoPlatMovimientoCuentaMovimientoCuenta(oMovCtaFlt));

				oCtaAdmin = new PlatCuenta();
				oCtaAdmin.setIdCuenta(oGestionObjFac.createPlatCuentaIdCuenta(cuentaAdministradora.getIdCuenta()));
				oCtaAdmin.setAlias(oGestionObjFac.createPlatCuentaAlias(cuentaAdministradora.getAlias()));
				oCtaAdmin.setNumeroCuenta(oGestionObjFac.createPlatCuentaNumeroCuenta(cuentaAdministradora.getNumeroCuenta()));
				oMovCtaFlt.setCuenta(oGestionObjFac.createPlatMovimientoCuentaCuenta(oCtaAdmin));
				oFlt.setPage(oGestionObjFac.createFiltroListadoPlatTransferenciaPage(0));
				oFlt.setPageSize(oGestionObjFac.createFiltroListadoPlatTransferenciaPageSize(Integer.MAX_VALUE));
				oFlt.setCampoOrden(oGestionObjFac.createFiltroListadoPlatTransferenciaCampoOrden("fechaAcreditacion"));
				oFlt.setTipoOrden(oGestionObjFac.createFiltroListadoPlatTransferenciaTipoOrden("ASC"));

				
				PlatMovimientoCuentaList oRTA = GestionServiceHelper.getGestionService(CfgTimeout.DEFAULT).listarPlatMovimientoCuenta(oFlt);*/

				/*--------------------*/
				
				PrimeFaces.current().ajax().update("admTrfDialog");
				PrimeFaces.current().executeScript("PF('admTrfDialogWV').show()");
			} catch (Exception e) {
				LogACGHelper.escribirLog(null,
						"detalleTransferencia. Se produjo un error al obtener el detalle de la transferencia. - MsgErr: |"
								+ e.getMessage() + "|");

				FacesContext.getCurrentInstance().addMessage("msgsProductos",
						new FacesMessage(FacesMessage.SEVERITY_WARN,
								"Se produjo un error al obtener el detalle de la transferencia.", null));
				PrimeFaces.current().ajax().update("msgsProductos");
			}
		} else {
			FacesContext.getCurrentInstance().addMessage("msgsProductos", new FacesMessage(FacesMessage.SEVERITY_WARN,
					"Debe seleccionar una transferencia para realizar la operacion.", null));
			PrimeFaces.current().ajax().update("msgsProductos");
		}
	}

	public void altaTransferencia() {
		operacion = "ALTA";
		// this.cuentaOrigen = null;
		this.cuentaDestino = null;
		this.itemAgendaDestinatario = null;
		itemTransferencia = new ItemTransferencia();
		itemTransferencia.setCodMnemonicoEntOrigen("SUPERPAGO");
		this.muestraCRUDTransferencia = true;
		this.muestraAplicacionAranceles = false;
		this.aranceles = null;
		tipoTrf = "CTASP";
		PrimeFaces.current().ajax().update("admTrfDialog");
		PrimeFaces.current().executeScript("PF('admTrfDialogWV').show()");
	}

	public void anulacionTransferencia() {
		if (itemTransferencia != null) {
			this.muestraCRUDTransferencia = false;
			PrimeFaces.current().ajax().update("confirmaDelTransferenciaDialog");
			PrimeFaces.current().executeScript("PF('confirmaDelTransferenciaDialogWV').show()");
		} else {
			FacesContext.getCurrentInstance().addMessage("msgsProductos", new FacesMessage(FacesMessage.SEVERITY_WARN,
					"Debe seleccionar una transferencia para realizar la operacion.", null));
			PrimeFaces.current().ajax().update("msgsProductos");
		}

	}

	public void cancelarDialogoTransferencia() {
		operacion = "";
		this.muestraCRUDTransferencia = false;
		this.muestraAplicacionAranceles = false;
		this.aranceles = null;
		this.setItemTransferencia(null);
		PrimeFaces.current().executeScript("PF('admTrfDialogWV').hide();ACGSiteScriptsFCNS.resultadoResize();");
	}

	public void procesarItmTransferencia() {
		PlatTransferencia oTrf = null;
		PlatTipoIdentifCta oTiIdeOri = null;
		PlatTipoIdentifCta oTiIdeDest = null;
		PlatTipoDocumento oTiIdenTribDes = null;
		PlatClasificacionTransferencia oClaTrf = null;
		PlatCanal oCanal = null;
		PlatMoneda oMoneda = null;
		PlatEntidadFinanciera oEntFiOri = null;
		PlatEntidadFinanciera oEntFiDest = null;
		Usuario oUsr = null;
		MovimientoDeCuenta oMovCta = null;
		RespAcredPlatCta oRTA = null;
		ArrayList<String> lstUpdate = new ArrayList<String>();
		GregorianCalendar gcFechaSaldo = null;
		XMLGregorianCalendar xmlGCFechaSaldo = null;

		try {
			oTrf = new PlatTransferencia();

			oTrf.setIdMayorista(oGestionObjFac.createPlatTransferenciaIdMayorista(this.getUsuario().getIdMayorista()));
			oTrf.setIdCliente(oGestionObjFac.createPlatTransferenciaIdCliente(this.getUsuario().getIdCliente()));
			oUsr = oGestionObjFac.createUsuario();
			oUsr.setIdUsuario(oGestionObjFac.createUsuarioIdUsuario(this.getUsuario().getIdUsuario()));
			oTrf.setUsuario(oGestionObjFac.createPlatTransferenciaUsuario(oUsr));

			oCanal = new PlatCanal();
			oCanal.setIdCanal(oGestionObjFac.createPlatCanalIdCanal("WEBINTRA"));
			oTrf.setCanal(oGestionObjFac.createPlatTransferenciaCanal(oCanal));

			oClaTrf = new PlatClasificacionTransferencia();
			oClaTrf.setCodMnemonico(oGestionObjFac.createPlatClasificacionTransferenciaCodMnemonico("TSAL"));
			oTrf.setClasificacion(oGestionObjFac.createPlatTransferenciaClasificacion(oClaTrf));

			gcFechaSaldo = new GregorianCalendar();
			gcFechaSaldo.setTime(new Date());

			xmlGCFechaSaldo = DatatypeFactory.newInstance().newXMLGregorianCalendar(gcFechaSaldo);

			oTrf.setFechaTransferencia(xmlGCFechaSaldo);

			oEntFiOri = new PlatEntidadFinanciera();
			oEntFiOri.setCodMnemonico(oGestionObjFac.createPlatEntidadFinancieraCodMnemonico("SUPERPAGO"));
			oTrf.setEntidadOrigen(oGestionObjFac.createPlatTransferenciaEntidadOrigen(oEntFiOri));

			oTiIdeOri = new PlatTipoIdentifCta();
			oTiIdeOri.setCodMnemonico(oGestionObjFac.createPlatTipoIdentifCtaCodMnemonico("CTASP"));
			oTrf.setTipoIdentifOrigen(oGestionObjFac.createPlatTransferenciaTipoIdentifOrigen(oTiIdeOri));

			// Carga el objeto completo de la coleccion
			this.setCuentaOrigen(this.getCuentasOrigen().get(this.cuentasOrigen.indexOf(this.getCuentaOrigen())));

			oTrf.setCodigoIdentifOrigen(oGestionObjFac
					.createPlatTransferenciaCodigoIdentifOrigen(this.getCuentaOrigen().getNumeroCuenta()));

			// Carga el objeto completo de la coleccion
			if(this.getItemAgendaDestinatario() != null) {
				this.setItemAgendaDestinatario(this.itemsAgendaDestinatarios.get(this.itemsAgendaDestinatarios.indexOf(this.getItemAgendaDestinatario())));

				oEntFiDest = new PlatEntidadFinanciera();
				oEntFiDest.setId(oGestionObjFac.createPlatEntidadFinancieraId(this.getItemAgendaDestinatario().getIdEntidadFinanciera()));
				oTrf.setEntidadDestino(oGestionObjFac.createPlatTransferenciaEntidadDestino(oEntFiDest));

				oTiIdeDest = new PlatTipoIdentifCta();
				oTiIdeDest.setIdTipoIdentifCta(oGestionObjFac
						.createPlatTipoIdentifCtaIdTipoIdentifCta(this.getItemAgendaDestinatario().getIdTipoIdentifCta()));

				oTiIdenTribDes = new PlatTipoDocumento();
				oTiIdenTribDes.setIdTipoDocumento(oGestionObjFac
						.createPlatTipoDocumentoIdTipoDocumento(this.getItemAgendaDestinatario().getIdTipoIdenTrib()));
				oTrf.setTipoIdentTribDestino(oGestionObjFac.createPlatTransferenciaTipoIdentTribDestino(oTiIdenTribDes));
				oTrf.setNroIdenTribDestino(oGestionObjFac
						.createPlatTransferenciaNroIdenTribDestino(this.getItemAgendaDestinatario().getNroIdenTrib()));

				oTrf.setTipoIdentifDestino(oGestionObjFac.createPlatTransferenciaTipoIdentifDestino(oTiIdeDest));
				oTrf.setCodigoIdentifDestino(oGestionObjFac.createPlatTransferenciaCodigoIdentifDestino(
						this.getItemAgendaDestinatario().getCodigoIdentifCta()));

				oTrf.setDestinoEsCtaPropia(oGestionObjFac.createPlatTransferenciaDestinoEsCtaPropia(
						this.getItemAgendaDestinatario().getEsCtaPropia().booleanValue()));

				oTrf.setDestinoEsCtaCte(oGestionObjFac.createPlatTransferenciaDestinoEsCtaCte(
						this.getItemAgendaDestinatario().getEsCtaCte().booleanValue()));
				
				oTrf.setTitularDestino(oGestionObjFac.createPlatTransferenciaTitularDestino(this.getItemAgendaDestinatario().getTitular()));
			}

			oTrf.setConcepto(oGestionObjFac.createPlatTransferenciaConcepto(this.getItemTransferencia().getConcepto()));
			oTrf.setImporte(oGestionObjFac.createPlatTransferenciaImporte(this.getItemTransferencia().getImporte()));

			oMoneda = new PlatMoneda();
			oMoneda.setIdMoneda(oGestionObjFac.createPlatMonedaIdMoneda(this.getCuentaOrigen().getIdMoneda()));
			oTrf.setMoneda(oGestionObjFac.createPlatTransferenciaMoneda(oMoneda));

			// oTrf.setNroComprobanteOrigen(value);
			oTrf.setObs(oGestionObjFac.createPlatTransferenciaObs(this.getItemTransferencia().getObs()));
			oTrf.setReferencia(
					oGestionObjFac.createPlatTransferenciaReferencia(this.getItemTransferencia().getReferencia()));

			oRTA = GestionServiceHelper.getGestionService(CfgTimeout.DEFAULT).obtenerArancelesPlatTransferencia(this.getHeaderSeguridad(), oTrf);

			if (oRTA != null && oRTA.getError() != null
					&& oRTA.getError().getValue().getHayError().getValue().booleanValue()
					&& !"EMI00".equals(oRTA.getError().getValue().getCodigoError().getValue())) {
				LogACGHelper.escribirLog(null,
						"procesarItmTransferencia. |" + oRTA.getError().getValue().getCodigoError().getValue() + " - "
								+ oRTA.getError().getValue().getMsgError().getValue() + "|");

				FacesContext.getCurrentInstance().addMessage(null,
						new FacesMessage(FacesMessage.SEVERITY_ERROR,
								"Nueva transferencia. " + oRTA.getError().getValue().getCodigoError().getValue() + " - "
										+ oRTA.getError().getValue().getMsgError().getValue(),
								null));

				FacesContext.getCurrentInstance().validationFailed();
			} else {
				if (oRTA.getListaAranceles().getValue() != null
						&& oRTA.getListaAranceles().getValue().getPlatMovimientoCuenta().size() > 0) {
					this.aranceles = new ArrayList<MovimientoDeCuenta>();
					for (PlatMovimientoCuenta oPMC : oRTA.getListaAranceles().getValue().getPlatMovimientoCuenta()) {
						oMovCta = new MovimientoDeCuenta();
						oMovCta.setConcepto(oPMC.getConcepto().getValue());
						oMovCta.setImpCredito(oPMC.getImpCredito().getValue());
						oMovCta.setImpDebito(oPMC.getImpDebito().getValue());
						oMovCta.setImporte(Math.abs(oPMC.getImpCredito().getValue() - oPMC.getImpDebito().getValue()));

						aranceles.add(oMovCta);
					}

					this.setImporteTotalAranceles(oRTA.getImporteTotAranceles().getValue());
					this.setImporteTotalOperacion(oRTA.getImporteTotOperacion().getValue());
					muestraAplicacionAranceles = true;
				} else {
					this.aranceles = null;
					muestraAplicacionAranceles = false;
				}

				if (oRTA != null && oRTA.getError() != null
						&& oRTA.getError().getValue().getHayError().getValue().booleanValue()
						&& "EMI00".equals(oRTA.getError().getValue().getCodigoError().getValue())) {
					FacesContext.getCurrentInstance().addMessage(null,
							new FacesMessage(FacesMessage.SEVERITY_ERROR,
									"Nueva transferencia. " + oRTA.getError().getValue().getCodigoError().getValue()
											+ " - " + oRTA.getError().getValue().getMsgError().getValue(),
									null));

					FacesContext.getCurrentInstance().validationFailed();
				}

				this.operacion = "CONFIRMACION";
				lstUpdate.add("formAdmTrfDialog");
			}
		} catch (Exception e) {
			LogACGHelper.escribirLog(null, "procesarItmTransferencia. |" + e.getMessage() + "|");

			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
					"Se produjo un error al dar de alta la transferencia. Intente la operacion nuevamente", null));

			lstUpdate.add("msgsProductos");
		}

		PrimeFaces.current().ajax().update(lstUpdate);
	}

	public void grabarItmTransferencia() {
		PlatTransferencia oTrf = null;
		PlatTipoIdentifCta oTiIdeOri = null;
		PlatTipoIdentifCta oTiIdeDest = null;
		PlatTipoDocumento oTiIdenTribDes = null;
		PlatClasificacionTransferencia oClaTrf = null;
		PlatCanal oCanal = null;
		PlatMoneda oMoneda = null;
		PlatEntidadFinanciera oEntFiOri = null;
		PlatEntidadFinanciera oEntFiDest = null;
		RespAcredPlatCta oRTA = null;
		ArrayList<String> lstUpdate = new ArrayList<String>();
		GregorianCalendar gcFechaSaldo = null;
		XMLGregorianCalendar xmlGCFechaSaldo = null;
		Usuario oUsr = null;
		
		try {
			oTrf = new PlatTransferencia();

			oTrf.setIdMayorista(oGestionObjFac.createPlatTransferenciaIdMayorista(this.getUsuario().getIdMayorista()));
			oTrf.setIdCliente(oGestionObjFac.createPlatTransferenciaIdCliente(this.getUsuario().getIdCliente()));
			oUsr = oGestionObjFac.createUsuario();
			oUsr.setIdUsuario(oGestionObjFac.createUsuarioIdUsuario(this.getUsuario().getIdUsuario()));
			oTrf.setUsuario(oGestionObjFac.createPlatTransferenciaUsuario(oUsr));

			oCanal = new PlatCanal();
			oCanal.setIdCanal(oGestionObjFac.createPlatCanalIdCanal("WEBINTRA"));
			oTrf.setCanal(oGestionObjFac.createPlatTransferenciaCanal(oCanal));

			oClaTrf = new PlatClasificacionTransferencia();
			oClaTrf.setCodMnemonico(oGestionObjFac.createPlatClasificacionTransferenciaCodMnemonico("TSAL"));
			oTrf.setClasificacion(oGestionObjFac.createPlatTransferenciaClasificacion(oClaTrf));

			gcFechaSaldo = new GregorianCalendar();
			gcFechaSaldo.setTime(new Date());

			xmlGCFechaSaldo = DatatypeFactory.newInstance().newXMLGregorianCalendar(gcFechaSaldo);

			oTrf.setFechaTransferencia(xmlGCFechaSaldo);

			oEntFiOri = new PlatEntidadFinanciera();
			oEntFiOri.setCodMnemonico(oGestionObjFac.createPlatEntidadFinancieraCodMnemonico("SUPERPAGO"));
			oTrf.setEntidadOrigen(oGestionObjFac.createPlatTransferenciaEntidadOrigen(oEntFiOri));

			oTiIdeOri = new PlatTipoIdentifCta();
			oTiIdeOri.setCodMnemonico(oGestionObjFac.createPlatTipoIdentifCtaCodMnemonico("CTASP"));
			oTrf.setTipoIdentifOrigen(oGestionObjFac.createPlatTransferenciaTipoIdentifOrigen(oTiIdeOri));

			// Carga el objeto completo de la coleccion
			this.setCuentaOrigen(this.cuentasOrigen.get(this.cuentasOrigen.indexOf(this.getCuentaOrigen())));

			oTrf.setCodigoIdentifOrigen(oGestionObjFac
					.createPlatTransferenciaCodigoIdentifOrigen(this.getCuentaOrigen().getNumeroCuenta()));

			// Carga el objeto completo de la coleccion
			this.setItemAgendaDestinatario(this.itemsAgendaDestinatarios
					.get(this.itemsAgendaDestinatarios.indexOf(this.getItemAgendaDestinatario())));

			oEntFiDest = new PlatEntidadFinanciera();
			oEntFiDest.setId(oGestionObjFac
					.createPlatEntidadFinancieraId(this.getItemAgendaDestinatario().getIdEntidadFinanciera()));
			oTrf.setEntidadDestino(oGestionObjFac.createPlatTransferenciaEntidadDestino(oEntFiDest));

			oTiIdeDest = new PlatTipoIdentifCta();
			oTiIdeDest.setIdTipoIdentifCta(oGestionObjFac
					.createPlatTipoIdentifCtaIdTipoIdentifCta(this.getItemAgendaDestinatario().getIdTipoIdentifCta()));
			oTrf.setTipoIdentifDestino(oGestionObjFac.createPlatTransferenciaTipoIdentifDestino(oTiIdeDest));
			oTrf.setCodigoIdentifDestino(oGestionObjFac.createPlatTransferenciaCodigoIdentifDestino(
					this.getItemAgendaDestinatario().getCodigoIdentifCta()));

			oTrf.setConcepto(oGestionObjFac.createPlatTransferenciaConcepto(this.getItemTransferencia().getConcepto()));
			oTrf.setImporte(oGestionObjFac.createPlatTransferenciaImporte(this.getItemTransferencia().getImporte()));

			oTrf.setDestinoEsCtaPropia(oGestionObjFac.createPlatTransferenciaDestinoEsCtaPropia(
					this.getItemAgendaDestinatario().getEsCtaPropia().booleanValue()));
			oTrf.setDestinoEsCtaCte(oGestionObjFac.createPlatTransferenciaDestinoEsCtaCte(
					this.getItemAgendaDestinatario().getEsCtaCte().booleanValue()));

			oTiIdenTribDes = new PlatTipoDocumento();
			oTiIdenTribDes.setIdTipoDocumento(oGestionObjFac
					.createPlatTipoDocumentoIdTipoDocumento(this.getItemAgendaDestinatario().getIdTipoIdenTrib()));
			oTrf.setTipoIdentTribDestino(oGestionObjFac.createPlatTransferenciaTipoIdentTribDestino(oTiIdenTribDes));
			oTrf.setNroIdenTribDestino(oGestionObjFac
					.createPlatTransferenciaNroIdenTribDestino(this.getItemAgendaDestinatario().getNroIdenTrib()));

			oTrf.setTitularDestino(oGestionObjFac.createPlatTransferenciaTitularDestino(this.getItemAgendaDestinatario().getTitular()));
			
			oMoneda = new PlatMoneda();
			oMoneda.setIdMoneda(oGestionObjFac.createPlatMonedaIdMoneda(this.getCuentaOrigen().getIdMoneda()));
			oTrf.setMoneda(oGestionObjFac.createPlatTransferenciaMoneda(oMoneda));

			// oTrf.setNroComprobanteOrigen(value);
			oTrf.setObs(oGestionObjFac.createPlatTransferenciaObs(this.getItemTransferencia().getObs()));
			oTrf.setReferencia(
					oGestionObjFac.createPlatTransferenciaReferencia(this.getItemTransferencia().getReferencia()));

			if ("CONFIRMACION".equals(this.getOperacion()))
				oRTA = GestionServiceHelper.getGestionService(CfgTimeout.DEFAULT).agregarPlatTransferencia(this.getHeaderSeguridad(), oTrf);

			if (oRTA != null && oRTA.getError() != null
					&& oRTA.getError().getValue().getHayError().getValue().booleanValue()) {
				LogACGHelper.escribirLog(null,
						"grabarItmTransferencia. |" + oRTA.getError().getValue().getCodigoError().getValue() + " - "
								+ oRTA.getError().getValue().getMsgError().getValue() + "|");

				FacesContext.getCurrentInstance().addMessage(null,
						new FacesMessage(FacesMessage.SEVERITY_ERROR,
								"Nueva transferencia. " + oRTA.getError().getValue().getCodigoError().getValue() + " - "
										+ oRTA.getError().getValue().getMsgError().getValue(),
								null));

				FacesContext.getCurrentInstance().validationFailed();
			} else {
				FacesContext.getCurrentInstance().addMessage("msgsProductos",
						new FacesMessage(FacesMessage.SEVERITY_INFO, "Nueva transferencia realizada con exito.", null));

				this.itemTransferencia = null;
				lstUpdate.add("msgsProductos");
				lstUpdate.add("transfersDT");
				this.muestraCRUDTransferencia = false;
				this.muestraAplicacionAranceles = false;
				this.aranceles = null;
				PrimeFaces.current().executeScript("ACGSiteScriptsFCNS.resultadoResize();");
			}
		} catch (Exception e) {
			LogACGHelper.escribirLog(null, "grabarItmTransferencia. |" + e.getMessage() + "|");

			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
					"Se produjo un error al dar de alta la transferencia. Intente la operacion nuevamente", null));

			lstUpdate.add("msgsProductos");
		}

		PrimeFaces.current().ajax().update(lstUpdate);
	}

	public void configrmarAnulacionTransferencia() {
		PlatTransferencia oTRF = null;
		RespString oRTA = null;
		
		if (itemTransferencia != null) {
			oTRF = new PlatTransferencia();
			oTRF.setIdMayorista(oGestionObjFac.createPlatTransferenciaIdMayorista(this.getUsuario().getIdMayorista()));
			oTRF.setIdCliente(oGestionObjFac.createPlatTransferenciaIdCliente(this.getUsuario().getIdCliente()));
			oTRF.setIdTransferencia(
					oGestionObjFac.createPlatTransferenciaIdTransferencia(itemTransferencia.getIdTransferencia()));

			oRTA = GestionServiceHelper.getGestionService(CfgTimeout.DEFAULT).eliminarPlatTransferencia(this.getHeaderSeguridad(), oTRF);

			if (oRTA != null && oRTA.getError() != null && oRTA.getError().getValue().getHayError().getValue()) {
				LogACGHelper.escribirLog(null,
						"eliminarTransferencia. |" + oRTA.getError().getValue().getCodigoError().getValue() + " - "
								+ oRTA.getError().getValue().getMsgError().getValue() + "|");

				FacesContext.getCurrentInstance().addMessage("msgsProductos",
						new FacesMessage(FacesMessage.SEVERITY_ERROR,
								"Eliminacion de transferencia. "
										+ oRTA.getError().getValue().getCodigoError().getValue() + " - "
										+ oRTA.getError().getValue().getMsgError().getValue(),
								null));

				FacesContext.getCurrentInstance().validationFailed();
			} else {
				FacesContext.getCurrentInstance().addMessage("msgsProductos",
						new FacesMessage(FacesMessage.SEVERITY_WARN,
								"La transferencia fue eliminada con exito.", null));
			}
		} else {
			FacesContext.getCurrentInstance().addMessage("msgsProductos", new FacesMessage(FacesMessage.SEVERITY_INFO,
					"La transferencia seleccionada no puede eliminarse.", null));
		}
	}

	public void actualizarDatosCuentaOrigenSeleccionada() {
		// Carga el objeto completo de la coleccion
		if (this.getCuentaOrigen() != null)
			this.setCuentaOrigen(this.cuentasOrigen.get(this.cuentasOrigen.indexOf(this.getCuentaOrigen())));
	}

	public void actualizarDatosCuentaDestinoSeleccionada() {
		// Carga el objeto completo de la coleccion
		if (this.getItemAgendaDestinatario() != null)
			this.setItemAgendaDestinatario(this.itemsAgendaDestinatarios
					.get(this.itemsAgendaDestinatarios.indexOf(this.getItemAgendaDestinatario())));
	}

	private Cuenta obtenerCuentaSuperPago(Cuenta cuenta) throws Exception {
		PlatCuenta oPlatCta = null;
		GregorianCalendar gcFechaAux = null;
		XMLGregorianCalendar xmlGCFechaAux = null;

		gcFechaAux = new GregorianCalendar();
		gcFechaAux.setTime(new Date());

		xmlGCFechaAux = DatatypeFactory.newInstance().newXMLGregorianCalendar(gcFechaAux);

		oPlatCta = new PlatCuenta();
		oPlatCta.setIdCuenta(oGestionObjFac.createPlatCuentaIdCuenta(cuenta.getIdCuenta()));
		oPlatCta.setNumeroCuenta(oGestionObjFac.createPlatCuentaNumeroCuenta(cuenta.getNumeroCuenta()));
		oPlatCta = GestionServiceHelper.getGestionService(CfgTimeout.DEFAULT).obtenerPlatCuenta(oPlatCta,
				xmlGCFechaAux);

		if (oPlatCta != null) {
			cuenta.setAlias(oPlatCta.getAlias().getValue());
			cuenta.setCVU(oPlatCta.getCVU().getValue());
			cuenta.setFechaAlta(oPlatCta.getFechaAlta().toGregorianCalendar().getTime());
			cuenta.setIdCliente(oPlatCta.getCliente().getValue().getIdCliente().getValue());
			cuenta.setIdCuenta(oPlatCta.getIdCuenta().getValue());
			cuenta.setIdMayorista(oPlatCta.getIdMayorista().getValue());
			cuenta.setIdMoneda(oPlatCta.getMoneda().getValue().getIdMoneda().getValue());
			cuenta.setDescripcionMoneda(oPlatCta.getMoneda().getValue().getDescripcion().getValue());
			cuenta.setSimboloMoneda(oPlatCta.getMoneda().getValue().getTxtSimbolo().getValue());
			cuenta.setIdProducto((oPlatCta != null && oPlatCta.getProducto() != null
					&& oPlatCta.getProducto().getValue().getIdProducto() != null
							? oPlatCta.getProducto().getValue().getIdProducto().getValue()
							: null));
			cuenta.setNumeroCuenta(oPlatCta.getNumeroCuenta().getValue());
			cuenta.setProductoDescripcion((oPlatCta != null && oPlatCta.getProducto() != null
					&& oPlatCta.getProducto().getValue().getDescProducto() != null
							? oPlatCta.getProducto().getValue().getDescProducto().getValue()
							: null));
			cuenta.setSaldo(oPlatCta.getSaldo().getValue());
			cuenta.setSaldoPendiente(oPlatCta.getSaldoPendiente().getValue());
			cuenta.setIdCuenta(oPlatCta.getIdCuenta().getValue());
			
			cuenta.setDescripcionEstado(oPlatCta.getEstado().getValue().getDescripcion().getValue());
			cuenta.setCodMnemonicoEstado(oPlatCta.getEstado().getValue().getCodMnemonico().getValue());
			cuenta.setIdTipoCuenta(oPlatCta.getTipoCuenta().getValue().getIdTipoCuenta().getValue());
			cuenta.setCodMnemonicoTipoCuenta(oPlatCta.getTipoCuenta().getValue().getCodMnemonico().getValue());
			cuenta.setDescripcionTipoCuenta(oPlatCta.getTipoCuenta().getValue().getDescripcion().getValue());
		} else
			cuenta = null;

		return cuenta;
	}

	// FIN Metodos de Transferencias

	public Cuenta getCuentaDestino() {
		return cuentaDestino;
	}

	public Cuenta getCuentaOrigen() {
		return cuentaOrigen;
	}

	public void setCuentaOrigen(Cuenta cuentaOrigen) {
		this.cuentaOrigen = cuentaOrigen;
	}

	public void setCuentaDestino(Cuenta cuentaDestino) {
		/*
		 * if(cuentaDestino != null && cuentaDestino.getIdCuenta() != null &&
		 * cuentaDestino.getIdCuenta().compareTo(0L) >0) // Carga el objeto completo de
		 * la coleccion
		 * this.setCuentaDestino(this.cuentasOrigen.get(this.cuentasOrigen.indexOf(this.
		 * getCuentaDestino()))); else
		 */
		this.cuentaDestino = cuentaDestino;
	}

	public ArrayList<Cuenta> getCuentasOrigen() {
		if (cuentasOrigen == null) {
			try {
				FiltroListadoPlatCuenta oFlt = new FiltroListadoPlatCuenta();
				GregorianCalendar gcFechaSaldo = null;
				XMLGregorianCalendar xmlGCFechaSaldo = null;
				
				gcFechaSaldo = new GregorianCalendar();
				gcFechaSaldo.setTime(new Date());

				xmlGCFechaSaldo = DatatypeFactory.newInstance().newXMLGregorianCalendar(gcFechaSaldo);

				// Se filtra solo la moneda ARS por el momento
				Cuenta oCuenta = null;
				PlatCuenta oCta = new PlatCuenta();
				PlatMoneda oMonFlt = new PlatMoneda();
				Cliente oCli = new Cliente();
				// oMonFlt.setIdMoneda(oGestionObjFac.createMonedaIdMoneda(this.getSeleccionIdMoneda()));
				oMonFlt.setCodISO4217A3(oGestionObjFac.createPlatMonedaCodISO4217A3("ARS"));

				oCta.setIdMayorista(oGestionObjFac.createPlatCuentaIdMayorista(this.getUsuario().getIdMayorista()));
				oCta.setMoneda(oGestionObjFac.createPlatCuentaMoneda(oMonFlt));
				oCli.setIdCliente(oGestionObjFac.createClienteIdCliente(this.getUsuario().getIdCliente()));
				oCta.setCliente(oGestionObjFac.createPlatCuentaCliente(oCli));

				oFlt.setFechaSaldo(xmlGCFechaSaldo);
				oFlt.setCuenta(oGestionObjFac.createFiltroListadoPlatCuentaCuenta(oCta));
				oFlt.setPage(oGestionObjFac.createFiltroListadoPlatCuentaPage(0));
				oFlt.setPageSize(oGestionObjFac.createFiltroListadoPlatCuentaPageSize(0));

				PlatCuentaList oLstCta = GestionServiceHelper.getGestionService(CfgTimeout.DEFAULT)
						.listarPlatCuenta(oFlt);

				if (oLstCta != null && oLstCta.getTotalRegistros().getValue().compareTo(0) > 0) {
					cuentasOrigen = new ArrayList<Cuenta>();

					// cuentasCfgAranceles = new ArrayList<SelectItem>();

					for (PlatCuenta oPlatCta : oLstCta.getRegistros().getValue().getPlatCuenta()) {
						oCuenta = new Cuenta();
						oCuenta.setAlias(oPlatCta.getAlias().getValue());
						oCuenta.setCVU(oPlatCta.getCVU().getValue());
						oCuenta.setFechaAlta(oPlatCta.getFechaAlta().toGregorianCalendar().getTime());
						oCuenta.setIdCliente(oPlatCta.getCliente().getValue().getIdCliente().getValue());
						oCuenta.setIdCuenta(oPlatCta.getIdCuenta().getValue());
						oCuenta.setIdMayorista(oPlatCta.getIdMayorista().getValue());
						oCuenta.setIdMoneda(oPlatCta.getMoneda().getValue().getIdMoneda().getValue());
						oCuenta.setDescripcionMoneda(oPlatCta.getMoneda().getValue().getDescripcion().getValue());
						oCuenta.setSimboloMoneda(oPlatCta.getMoneda().getValue().getTxtSimbolo().getValue());
						oCuenta.setIdProducto((oPlatCta != null && oPlatCta.getProducto() != null
								&& oPlatCta.getProducto().getValue().getIdProducto() != null
										? oPlatCta.getProducto().getValue().getIdProducto().getValue()
										: null));
						oCuenta.setNumeroCuenta(oPlatCta.getNumeroCuenta().getValue());
						oCuenta.setProductoDescripcion((oPlatCta != null && oPlatCta.getProducto() != null
								&& oPlatCta.getProducto().getValue().getDescProducto() != null
										? oPlatCta.getProducto().getValue().getDescProducto().getValue()
										: null));

						// GestionServiceHelper.getGestionService(CfgTimeout.DEFAULT).obtenerPlat
						oCuenta.setSaldo(oPlatCta.getSaldo().getValue());
						oCuenta.setSaldoPendiente(oPlatCta.getSaldoPendiente().getValue());
						oCuenta.setIdCuenta(oPlatCta.getIdCuenta().getValue());

						oCuenta.setDescripcionEstado(oPlatCta.getEstado().getValue().getDescripcion().getValue());
						oCuenta.setCodMnemonicoEstado(oPlatCta.getEstado().getValue().getCodMnemonico().getValue());

						oCuenta.setIdTipoCuenta(oPlatCta.getTipoCuenta().getValue().getIdTipoCuenta().getValue());
						oCuenta.setCodMnemonicoTipoCuenta(oPlatCta.getTipoCuenta().getValue().getCodMnemonico().getValue());
						oCuenta.setDescripcionTipoCuenta(oPlatCta.getTipoCuenta().getValue().getDescripcion().getValue());

						cuentasOrigen.add(oCuenta);
					}
				}
			} catch (Exception e) {
				FacesContext.getCurrentInstance().addMessage("msgsProductos",
						new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error obteniendo las cuentas origen.", null));
				PrimeFaces.current().ajax().update("msgsProductos");
			}
		}

		return cuentasOrigen;
	}

	public void setCuentasOrigen(ArrayList<Cuenta> cuentasOrigen) {
		this.cuentasOrigen = cuentasOrigen;
	}

	public String obtenerTipoTransferencia(ItemTransferencia itmTrf) {
		if ("SUPERPAGO".equals(itmTrf.getCodMnemonicoEntOrigen())
				&& "SUPERPAGO".equals(itmTrf.getCodMnemonicoEntDestino()))
			return "Entre ctas. Superpago";
		else if ("SUPERPAGO".equals(itmTrf.getCodMnemonicoEntOrigen())
				&& !"SUPERPAGO".equals(itmTrf.getCodMnemonicoEntDestino()))
			return "Hacia otras entidades";
		else if (!"SUPERPAGO".equals(itmTrf.getCodMnemonicoEntOrigen())
				&& "SUPERPAGO".equals(itmTrf.getCodMnemonicoEntDestino()))
			return "Desde otras entidades";
		else
			return "";
	}

	public String getTipoTrf() {
		return tipoTrf;
	}

	public void setTipoTrf(String tipoTrf) {
		this.tipoTrf = tipoTrf;
	}

	public ArrayList<ItemAgendaDestinatario> getItemsAgendaDestinatariosPublicos() {
		FiltroListadoPlatAgendaDestinatarioCta oFlt = null;
		PlatAgendaDestinatarioCta oAgCtaFlt = null;
		PlatEntidadFinanciera oEF = null;
		Cliente oCli = null;
		Long idCliMayorista = null;
		
		if (itemsAgendaDestinatariosPublicos == null) {
			oFlt = new FiltroListadoPlatAgendaDestinatarioCta();
			oAgCtaFlt = new PlatAgendaDestinatarioCta();

			oAgCtaFlt.setIdMayorista(
					oGestionObjFac.createPlatAgendaDestinatarioCtaIdMayorista(this.getUsuario().getIdMayorista()));
			
			idCliMayorista = GestionServiceHelper.getGestionService(CfgTimeout.DEFAULT).buscarIdCliMayorista(this.getUsuario().getIdMayorista());
			
			if(idCliMayorista == null)
				return null;
			
			oCli = new Cliente();
			oCli.setIdCliente(oGestionObjFac.createClienteIdCliente(idCliMayorista));
			
			
			oAgCtaFlt.setCliente(oGestionObjFac.createPlatAgendaDestinatarioCtaCliente(oCli)); // SUPERPAGO
			oAgCtaFlt.setVistaPublica(oGestionObjFac.createPlatAgendaDestinatarioCtaVistaPublica(true));

			oFlt.setAgendaDestinatario(
					oGestionObjFac.createFiltroListadoPlatAgendaDestinatarioCtaAgendaDestinatario(oAgCtaFlt));

			oFlt.setPage(oGestionObjFac.createFiltroListadoPlatAgendaDestinatarioCtaPage(1));
			oFlt.setPageSize(oGestionObjFac.createFiltroListadoPlatAgendaDestinatarioCtaPageSize(Integer.MAX_VALUE));
			oFlt.setCampoOrden(oGestionObjFac.createFiltroListadoPlatAgendaDestinatarioCtaCampoOrden(""));
			oFlt.setTipoOrden(oGestionObjFac.createFiltroListadoPlatAgendaDestinatarioCtaTipoOrden(""));

			PlatAgendaDestinatarioCtaList oLstAgDes = GestionServiceHelper.getGestionService(CfgTimeout.DEFAULT)
					.listarPlatAgendaDestinatarioCta(oFlt);

			if (oLstAgDes != null && oLstAgDes.getRegistros() != null && oLstAgDes.getRegistros().getValue() != null
					&& oLstAgDes.getRegistros().getValue().getPlatAgendaDestinatarioCta() != null) {
				itemsAgendaDestinatariosPublicos = new ArrayList<ItemAgendaDestinatario>();

				for (PlatAgendaDestinatarioCta agDes : oLstAgDes.getRegistros().getValue()
						.getPlatAgendaDestinatarioCta()) {
					ItemAgendaDestinatario oItmAgeDes = new ItemAgendaDestinatario();
					oItmAgeDes.setIdAgendaDestinatarioCta(agDes.getIdAgendaDestinatarioCta().getValue());
					oItmAgeDes.setIdMayorista(agDes.getIdMayorista().getValue());
					oItmAgeDes.setIdUsuario(agDes.getIdUsuario().getValue());
					oItmAgeDes.setFechaAlta(
							(agDes.getFechaAlta() != null ? agDes.getFechaAlta().toGregorianCalendar().getTime()
									: null));
					oItmAgeDes.setReferencia(agDes.getReferencia().getValue());
					oItmAgeDes.setTitular(agDes.getTitular().getValue());
					oItmAgeDes.setIdEntidadFinanciera((agDes.getEntidadFinanciera() != null
							&& agDes.getEntidadFinanciera().getValue().getId() != null
									? agDes.getEntidadFinanciera().getValue().getId().getValue()
									: null));
					oItmAgeDes.setDescEntidadFinanciera((agDes.getEntidadFinanciera() != null
							&& agDes.getEntidadFinanciera().getValue().getDenominacion() != null
									? agDes.getEntidadFinanciera().getValue().getDenominacion().getValue()
									: null));
					oItmAgeDes.setEsCtaPropia(agDes.getEsCtaPropia().getValue());
					oItmAgeDes.setIdTipoIdentifCta((agDes.getTipoIdentifCta() != null
							&& agDes.getTipoIdentifCta().getValue().getIdTipoIdentifCta() != null
									? agDes.getTipoIdentifCta().getValue().getIdTipoIdentifCta().getValue()
									: null));
					oItmAgeDes.setDescTipoIdentifCta((agDes.getTipoIdentifCta() != null
							&& agDes.getTipoIdentifCta().getValue().getDescripcion() != null
									? agDes.getTipoIdentifCta().getValue().getDescripcion().getValue()
									: null));
					oItmAgeDes.setCodigoIdentifCta(agDes.getCodigoIdentifCta().getValue());
					oItmAgeDes.setEsCtaCte(agDes.getEsCtaCte().getValue());
					oItmAgeDes.setIdTipoIdenTrib((agDes.getTipoIdenTrib().getValue() != null
							? agDes.getTipoIdenTrib().getValue().getIdTipoDocumento().getValue()
							: null));
					oItmAgeDes.setCodMnemonicoTipoIdenTrib((agDes.getTipoIdenTrib().getValue() != null
							? agDes.getTipoIdenTrib().getValue().getCodMnemonico().getValue()
							: null));
					oItmAgeDes.setDescripcionTipoIdenTrib((agDes.getTipoIdenTrib().getValue() != null
							? agDes.getTipoIdenTrib().getValue().getDescripcion().getValue()
							: null));
					oItmAgeDes.setNroIdenTrib(agDes.getNroIdenTrib().getValue());
					oItmAgeDes.setEmailNotifTransfer(agDes.getEmailNotifTransfer().getValue());
					oItmAgeDes.setIdEstado((agDes.getEstado() != null
							&& agDes.getEstado().getValue().getIdEstadoAgendaDestCta() != null
									? agDes.getEstado().getValue().getIdEstadoAgendaDestCta().getValue()
									: null));
					oItmAgeDes.setLimiteTransferencia(agDes.getLimiteTransferencia().getValue());
					oItmAgeDes.setDescEstado(
							(agDes.getEstado() != null && agDes.getEstado().getValue().getDescripcion() != null
									? agDes.getEstado().getValue().getDescripcion().getValue()
									: null));
					oItmAgeDes.setVistaPublica(agDes.getVistaPublica().getValue());

					itemsAgendaDestinatariosPublicos.add(oItmAgeDes);
				}
			}
		}

		return itemsAgendaDestinatariosPublicos;
	}

	public void setItemsAgendaDestinatariosPublicos(
			ArrayList<ItemAgendaDestinatario> itemsAgendaDestinatariosPublicos) {
		this.itemsAgendaDestinatariosPublicos = itemsAgendaDestinatariosPublicos;
	}

	public ArrayList<ItemAgendaDestinatario> getItemsAgendaDestinatarios() {
		FiltroListadoPlatAgendaDestinatarioCta oFlt = null;
		PlatAgendaDestinatarioCta oAgCtaFlt = null;
		PlatEntidadFinanciera oEF = null;
		Cliente oCli = null;
		
		if (itemsAgendaDestinatarios == null) {
			oFlt = new FiltroListadoPlatAgendaDestinatarioCta();
			oAgCtaFlt = new PlatAgendaDestinatarioCta();

			oAgCtaFlt.setIdMayorista(
					oGestionObjFac.createPlatAgendaDestinatarioCtaIdMayorista(this.getUsuario().getIdMayorista()));
			
			oCli = new Cliente();
			oCli.setIdCliente(oGestionObjFac.createClienteIdCliente(this.getUsuario().getIdCliente()));
			
			oAgCtaFlt.setCliente(
					oGestionObjFac.createPlatAgendaDestinatarioCtaCliente(oCli));

			oFlt.setAgendaDestinatario(
					oGestionObjFac.createFiltroListadoPlatAgendaDestinatarioCtaAgendaDestinatario(oAgCtaFlt));

			oFlt.setPage(oGestionObjFac.createFiltroListadoPlatAgendaDestinatarioCtaPage(1));
			oFlt.setPageSize(oGestionObjFac.createFiltroListadoPlatAgendaDestinatarioCtaPageSize(Integer.MAX_VALUE));
			oFlt.setCampoOrden(oGestionObjFac.createFiltroListadoPlatAgendaDestinatarioCtaCampoOrden(""));
			oFlt.setTipoOrden(oGestionObjFac.createFiltroListadoPlatAgendaDestinatarioCtaTipoOrden(""));

			PlatAgendaDestinatarioCtaList oLstAgDes = GestionServiceHelper.getGestionService(CfgTimeout.DEFAULT)
					.listarPlatAgendaDestinatarioCta(oFlt);

			if (oLstAgDes != null && oLstAgDes.getRegistros() != null && oLstAgDes.getRegistros().getValue() != null
					&& oLstAgDes.getRegistros().getValue().getPlatAgendaDestinatarioCta() != null) {
				itemsAgendaDestinatarios = new ArrayList<ItemAgendaDestinatario>();

				for (PlatAgendaDestinatarioCta agDes : oLstAgDes.getRegistros().getValue()
						.getPlatAgendaDestinatarioCta()) {
					ItemAgendaDestinatario oItmAgeDes = new ItemAgendaDestinatario();
					oItmAgeDes.setIdAgendaDestinatarioCta(agDes.getIdAgendaDestinatarioCta().getValue());
					oItmAgeDes.setIdMayorista(agDes.getIdMayorista().getValue());
					oItmAgeDes.setIdUsuario(agDes.getIdUsuario().getValue());
					oItmAgeDes.setFechaAlta(
							(agDes.getFechaAlta() != null ? agDes.getFechaAlta().toGregorianCalendar().getTime()
									: null));
					oItmAgeDes.setReferencia(agDes.getReferencia().getValue());
					oItmAgeDes.setTitular(agDes.getTitular().getValue());
					oItmAgeDes.setIdEntidadFinanciera((agDes.getEntidadFinanciera() != null
							&& agDes.getEntidadFinanciera().getValue().getId() != null
									? agDes.getEntidadFinanciera().getValue().getId().getValue()
									: null));
					oItmAgeDes.setDescEntidadFinanciera((agDes.getEntidadFinanciera() != null
							&& agDes.getEntidadFinanciera().getValue().getDenominacion() != null
									? agDes.getEntidadFinanciera().getValue().getDenominacion().getValue()
									: null));
					oItmAgeDes.setEsCtaPropia(agDes.getEsCtaPropia().getValue());
					oItmAgeDes.setIdTipoIdentifCta((agDes.getTipoIdentifCta() != null
							&& agDes.getTipoIdentifCta().getValue().getIdTipoIdentifCta() != null
									? agDes.getTipoIdentifCta().getValue().getIdTipoIdentifCta().getValue()
									: null));
					oItmAgeDes.setDescTipoIdentifCta((agDes.getTipoIdentifCta() != null
							&& agDes.getTipoIdentifCta().getValue().getDescripcion() != null
									? agDes.getTipoIdentifCta().getValue().getDescripcion().getValue()
									: null));
					oItmAgeDes.setCodigoIdentifCta(agDes.getCodigoIdentifCta().getValue());
					oItmAgeDes
							.setEsCtaCte(agDes.getEsCtaCte().getValue());
					oItmAgeDes.setIdTipoIdenTrib((agDes.getTipoIdenTrib().getValue() != null
							? agDes.getTipoIdenTrib().getValue().getIdTipoDocumento().getValue()
							: null));
					oItmAgeDes.setCodMnemonicoTipoIdenTrib((agDes.getTipoIdenTrib().getValue() != null
							? agDes.getTipoIdenTrib().getValue().getCodMnemonico().getValue()
							: null));
					oItmAgeDes.setDescripcionTipoIdenTrib((agDes.getTipoIdenTrib().getValue() != null
							? agDes.getTipoIdenTrib().getValue().getDescripcion().getValue()
							: null));
					oItmAgeDes.setNroIdenTrib(agDes.getNroIdenTrib().getValue());
					oItmAgeDes.setEmailNotifTransfer(agDes.getEmailNotifTransfer().getValue());
					oItmAgeDes.setIdEstado((agDes.getEstado() != null
							&& agDes.getEstado().getValue().getIdEstadoAgendaDestCta() != null
									? agDes.getEstado().getValue().getIdEstadoAgendaDestCta().getValue()
									: null));
					oItmAgeDes.setLimiteTransferencia(agDes.getLimiteTransferencia().getValue());
					oItmAgeDes.setDescEstado(
							(agDes.getEstado() != null && agDes.getEstado().getValue().getDescripcion() != null
									? agDes.getEstado().getValue().getDescripcion().getValue()
									: null));
					oItmAgeDes.setVistaPublica(agDes.getVistaPublica().getValue());

					itemsAgendaDestinatarios.add(oItmAgeDes);
				}
			}
		}

		return itemsAgendaDestinatarios;
	}

	public void setItemsAgendaDestinatarios(ArrayList<ItemAgendaDestinatario> itemsAgendaDestinatarios) {
		this.itemsAgendaDestinatarios = itemsAgendaDestinatarios;
	}

	// INICIO Metodos de Solicitud de dinero
	public void detalleSolicitudDeDinero() {
		if (itemTransferencia != null) {
			try {
				operacion = "DETALLE";
				this.muestraCRUDSolicituDeDinero = true;

				this.cuentaOrigen = new Cuenta();
				this.cuentaOrigen.setIdMayorista(this.getUsuario().getIdMayorista());
				this.cuentaOrigen.setIdCliente(this.getUsuario().getIdCliente());
				this.cuentaOrigen.setNumeroCuenta(itemTransferencia.getCodigoIdentifOrigen());

				this.cuentaDestino = new Cuenta();
				this.cuentaDestino.setNumeroCuenta(itemTransferencia.getCodigoIdentifDestino());
				this.cuentaDestino = obtenerCuentaSuperPago(this.cuentaDestino);

				PrimeFaces.current().ajax().update("admSolDineroDialog");
				PrimeFaces.current().executeScript("PF('admSolDineroDialogWV').show()");
			} catch (Exception e) {
				LogACGHelper.escribirLog(null,
						"detalleSolicitudDeDinero. Se produjo un error al obtener el detalle de la solicitud de dinero. - MsgErr: |"
								+ e.getMessage() + "|");

				FacesContext.getCurrentInstance().addMessage("msgsProductos",
						new FacesMessage(FacesMessage.SEVERITY_WARN,
								"Se produjo un error al obtener el detalle de la solicitud de dinero.", null));
			}
		} else {
			FacesContext.getCurrentInstance().addMessage("msgsProductos", new FacesMessage(FacesMessage.SEVERITY_WARN,
					"Debe seleccionar una solicitud de dinero para realizar la operacion.", null));
		}

		PrimeFaces.current().ajax().update("msgsProductos");
	}

	public void altaSolicitudDeDinero() {
		try {
			this.setCuentaDestino(this.obtenerCuentaSuperPago(this.getCuentaDestino()));

			operacion = "ALTA";
			itemTransferencia = new ItemTransferencia();
			this.muestraCRUDSolicituDeDinero = true;
			this.setCuitCliente(null);
			this.setCliente(null);
			tipoTrf = "";
			PrimeFaces.current().ajax().update("admSolDineroDialog");
			PrimeFaces.current().executeScript("PF('admSolDineroDialogWV').show()");
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage("msgsProductos",
					new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error obteniendo la cuenta origen.", null));
			PrimeFaces.current().ajax().update("msgsProductos");
		}
	}

	public void confirmarSolicitudDeDinero() {
		if (itemTransferencia != null) {
			if ("PENDAUTMAN".equals(itemTransferencia.getCodMnemonicoEstado())) {
				operacion = "CONFIRMAR";
				this.muestraCRUDSolicituDeDinero = true;
				this.comentarios = "";
				this.setCuentaOrigen(null);
				
				try {
					this.cuentaDestino = new Cuenta();
					this.cuentaDestino.setNumeroCuenta(this.getItemTransferencia().getCodigoIdentifDestino());

					this.cuentaDestino = obtenerCuentaSuperPago(this.cuentaDestino);

					PrimeFaces.current().ajax().update("admSolDineroDialog");
					PrimeFaces.current().executeScript("PF('admSolDineroDialogWV').show()");
				} catch (Exception e) {
					FacesContext.getCurrentInstance().addMessage("msgsProductos", new FacesMessage(
							FacesMessage.SEVERITY_WARN, "Se produjo un error al obtener la cuenta destino.", null));
					PrimeFaces.current().ajax().update("msgsProductos");
				}
			} else {
				FacesContext.getCurrentInstance().addMessage("msgsProductos", new FacesMessage(
						FacesMessage.SEVERITY_WARN, "El estado de la solicitud de dinero no permite la accion.", null));
				PrimeFaces.current().ajax().update("msgsProductos");
			}
		} else {
			FacesContext.getCurrentInstance().addMessage("msgsProductos", new FacesMessage(FacesMessage.SEVERITY_WARN,
					"Debe seleccionar una solicitud de dinero para realizar la operacion.", null));
			PrimeFaces.current().ajax().update("msgsProductos");
		}
	}

	public void rechazarSolicitudDeDinero() {
		if (itemTransferencia != null) {
			this.muestraCRUDTransferencia = false;
			PrimeFaces.current().ajax().update("confirmaDelDestinatarioDialog");
			PrimeFaces.current().executeScript("PF('confirmaDelDestinatarioDialogWV').show()");
		} else {
			FacesContext.getCurrentInstance().addMessage("msgsProductos", new FacesMessage(FacesMessage.SEVERITY_WARN,
					"Debe seleccionar una transferencia para realizar la operacion.", null));
			PrimeFaces.current().ajax().update("msgsProductos");
		}

	}

	public void cancelarDialogoSolicitudDeDinero() {
		operacion = "";
		this.muestraCRUDSolicituDeDinero = false;
		this.setItemTransferencia(null);
		PrimeFaces.current().ajax().update(":solicitudesDeDineroDT");
		PrimeFaces.current().executeScript("PF('admSolDineroDialogWV').hide();ACGSiteScriptsFCNS.resultadoResize();");
	}

	public void confirmarAceptacionSolicitudDeDinero() {
		ResultadoBase oRTA = null;
		ArrayList<String> lstUpdate = new ArrayList<String>();
		
		try {
			if (this.getCuentaOrigen() == null) {
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
						"Cuenta origen no asignada. No es posible procesar la operacion", null));

				FacesContext.getCurrentInstance().validationFailed();
				PrimeFaces.current().ajax().update("msgsProductos");
				return;
			}

			// Carga el objeto completo de la coleccion
			this.setCuentaOrigen(this.cuentasOrigen.get(this.cuentasOrigen.indexOf(this.getCuentaOrigen())));

			oRTA = GestionServiceHelper.getGestionService(CfgTimeout.DEFAULT).procesarSolicitudDeDinero(this.getHeaderSeguridad(),
					itemTransferencia.getIdTransferencia(), "ACEPTADA", this.getComentarios(),
					this.getCuentaOrigen().getNumeroCuenta());

			if (oRTA != null && oRTA.getError() != null
					&& oRTA.getError().getValue().getHayError().getValue().booleanValue()) {
				LogACGHelper.escribirLog(null,
						"confirmarAceptacionSolicitudDeDinero. |"
								+ oRTA.getError().getValue().getCodigoError().getValue() + " - "
								+ oRTA.getError().getValue().getMsgError().getValue() + "|");

				FacesContext.getCurrentInstance().addMessage(null,
						new FacesMessage(FacesMessage.SEVERITY_ERROR,
								"Aceptacion de Solicitud de dinero. "
										+ oRTA.getError().getValue().getCodigoError().getValue() + " - "
										+ oRTA.getError().getValue().getMsgError().getValue(),
								null));

				FacesContext.getCurrentInstance().validationFailed();
				lstUpdate.add("msgsProductos");
			} else {
				FacesContext.getCurrentInstance().addMessage("msgsProductos",
						new FacesMessage(FacesMessage.SEVERITY_INFO,
								"La solicitud de dinero fue aceptada y procesada con exito.", null));

				this.itemTransferencia = null;
				lstUpdate.add("msgsProductos");
				lstUpdate.add("solicitudesDeDineroDT");
				this.muestraCRUDSolicituDeDinero = false;

				PrimeFaces.current().executeScript("ACGSiteScriptsFCNS.resultadoResize();");
			}
		} catch (Exception e) {
			LogACGHelper.escribirLog(null, "confirmarAceptacionSolicitudDeDinero. |" + e.getMessage() + "|");

			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
					"Se produjo un error al aceptar la solicitud de dinero. Intente la operacion nuevamente", null));
			
			lstUpdate.add("msgsProductos");
			FacesContext.getCurrentInstance().validationFailed();
		}

		PrimeFaces.current().ajax().update(lstUpdate);
	}

	public void configrmarRechazoSolicitudDeDinero() {
		ResultadoBase oRTA = null;
		ArrayList<String> lstUpdate = new ArrayList<String>();
		
		try {
			oRTA = GestionServiceHelper.getGestionService(CfgTimeout.DEFAULT).procesarSolicitudDeDinero(this.getHeaderSeguridad(), 
					itemTransferencia.getIdTransferencia(), "RECTRF", this.getComentarios(), null);

			if (oRTA != null && oRTA.getError().getValue() != null
					&& oRTA.getError().getValue().getHayError().getValue().booleanValue()) {
				LogACGHelper.escribirLog(null,
						"configrmarRechazoSolicitudDeDinero. |" + oRTA.getError().getValue().getCodigoError().getValue()
								+ " - " + oRTA.getError().getValue().getMsgError().getValue() + "|");

				FacesContext.getCurrentInstance().addMessage(null,
						new FacesMessage(FacesMessage.SEVERITY_ERROR,
								"Rechazo de Solicitud de dinero. "
										+ oRTA.getError().getValue().getCodigoError().getValue() + " - "
										+ oRTA.getError().getValue().getMsgError().getValue(),
								null));

				FacesContext.getCurrentInstance().validationFailed();
			} else {
				FacesContext.getCurrentInstance().addMessage("msgsProductos", new FacesMessage(
						FacesMessage.SEVERITY_INFO, "La solicitud de dinero fue rechazada con exito.", null));

				this.itemTransferencia = null;
				lstUpdate.add("msgsProductos");
				lstUpdate.add("solicitudesDeDineroDT");
				this.muestraCRUDSolicituDeDinero = false;
				PrimeFaces.current().executeScript("ACGSiteScriptsFCNS.resultadoResize();");
			}
		} catch (Exception e) {
			LogACGHelper.escribirLog(null, "confirmarAceptacionSolicitudDeDinero. |" + e.getMessage() + "|");

			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
					"Se produjo un error al aceptar la solicitud de dinero. Intente la operacion nuevamente", null));
			lstUpdate.add("msgsProductos");
		}

		PrimeFaces.current().ajax().update(lstUpdate);
	}

	public void procesarSolicitudDinero() {
		PlatTransferencia oTrf = null;
		PlatTipoIdentifCta oTiIdeOri = null;
		PlatTipoIdentifCta oTiIdeDest = null;
		PlatClasificacionTransferencia oClaTrf = null;
		PlatCanal oCanal = null;
		PlatMoneda oMoneda = null;
		PlatEntidadFinanciera oEntFiOri = null;
		PlatEntidadFinanciera oEntFiDest = null;
		PlatTipoDocumento oTiIdenTribDes = null;
		Cliente oCli = null;
		RespAcredPlatCta oRTA = null;
		ArrayList<String> lstUpdate = new ArrayList<String>();
		GregorianCalendar gcFechaSaldo = null;
		XMLGregorianCalendar xmlGCFechaSaldo = null;
		Usuario oUsr = null;

		try {
			oTrf = new PlatTransferencia();

			oTrf.setIdMayorista(oGestionObjFac.createPlatTransferenciaIdMayorista(this.getUsuario().getIdMayorista()));
			oTrf.setIdCliente(oGestionObjFac.createPlatTransferenciaIdCliente(this.getUsuario().getIdCliente()));
			oUsr = oGestionObjFac.createUsuario();
			oUsr.setIdUsuario(oGestionObjFac.createUsuarioIdUsuario(this.getUsuario().getIdUsuario()));
			oTrf.setUsuario(oGestionObjFac.createPlatTransferenciaUsuario(oUsr));


			oCanal = new PlatCanal();
			oCanal.setIdCanal(oGestionObjFac.createPlatCanalIdCanal("WEBINTRA"));
			oTrf.setCanal(oGestionObjFac.createPlatTransferenciaCanal(oCanal));

			oClaTrf = new PlatClasificacionTransferencia();
			oClaTrf.setCodMnemonico(oGestionObjFac.createPlatClasificacionTransferenciaCodMnemonico("SDIN"));
			oTrf.setClasificacion(oGestionObjFac.createPlatTransferenciaClasificacion(oClaTrf));

			gcFechaSaldo = new GregorianCalendar();
			gcFechaSaldo.setTime(new Date());

			xmlGCFechaSaldo = DatatypeFactory.newInstance().newXMLGregorianCalendar(gcFechaSaldo);

			oTrf.setFechaTransferencia(xmlGCFechaSaldo);

			oEntFiOri = new PlatEntidadFinanciera();
			oEntFiOri.setCodMnemonico(oGestionObjFac.createPlatEntidadFinancieraCodMnemonico("SUPERPAGO"));
			oTrf.setEntidadOrigen(oGestionObjFac.createPlatTransferenciaEntidadOrigen(oEntFiOri));

			oTrf.setClienteOrigen(oGestionObjFac.createPlatTransferenciaClienteOrigen(this.getCliente()));

			oEntFiDest = new PlatEntidadFinanciera();
			oEntFiDest.setCodMnemonico(oGestionObjFac.createPlatEntidadFinancieraCodMnemonico("SUPERPAGO"));
			oTrf.setEntidadDestino(oGestionObjFac.createPlatTransferenciaEntidadDestino(oEntFiDest));

			oTiIdeDest = new PlatTipoIdentifCta();
			oTiIdeDest.setCodMnemonico(oGestionObjFac.createPlatTipoIdentifCtaCodMnemonico("CTASP"));
			oTrf.setTipoIdentifDestino(oGestionObjFac.createPlatTransferenciaTipoIdentifDestino(oTiIdeDest));
			oTrf.setCodigoIdentifDestino(
					oGestionObjFac.createPlatTransferenciaCodigoIdentifDestino(cuentaDestino.getNumeroCuenta()));

			oTrf.setConcepto(oGestionObjFac.createPlatTransferenciaConcepto(this.getItemTransferencia().getConcepto()));
			oTrf.setImporte(oGestionObjFac.createPlatTransferenciaImporte(this.getItemTransferencia().getImporte()));

			oTrf.setDestinoEsCtaPropia(oGestionObjFac.createPlatTransferenciaDestinoEsCtaPropia(true));
			// TODO Ojo si en algun momento se habilitan cuentas corrientes el valor deberia
			// venir de parametro de BBDD
			oTrf.setDestinoEsCtaCte(oGestionObjFac.createPlatTransferenciaDestinoEsCtaCte(false));

			oCli = new Cliente();
			oCli.setIdMayorista(oGestionObjFac.createClienteIdMayorista(this.getUsuario().getIdMayorista()));
			oCli.setIdCliente(oGestionObjFac.createClienteIdCliente(this.getUsuario().getIdCliente()));
			oCli = GestionServiceHelper.getGestionService(CfgTimeout.DEFAULT).mostrarClienteObj(oCli);

			if (oCli == null || oCli.getCUIT().getValue() == null || oCli.getCUIT().getValue().compareTo(0L) == 0) {
				LogACGHelper.escribirLog(null, "procesarSolicitudDinero. |No se pudo obtener el cliente.|");

				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
						"Solicitud de dinero. No se pudo obtener cliente destino.", null));

				FacesContext.getCurrentInstance().validationFailed();

				return;
			}

			// TODO Se pone hardcode CUIT porque en la platadorma no hay campo para
			// diferenciar entre CUIT y CUIL
			oTiIdenTribDes = new PlatTipoDocumento();
			oTiIdenTribDes.setCodMnemonico(oGestionObjFac.createPlatTipoDocumentoCodMnemonico("CUIT"));
			oTiIdenTribDes = GestionServiceHelper.getGestionService(CfgTimeout.DEFAULT)
					.obtenerPlatTipoDocumento(oTiIdenTribDes);

			if (oTiIdenTribDes == null || oTiIdenTribDes.getIdTipoDocumento().getValue() == null) {
				LogACGHelper.escribirLog(null,
						"procesarSolicitudDinero. |No se pudo obtener el tipo de identificacion tributaria del cliente.|");

				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
						"Solicitud de dinero. No se pudo obtener el tipo de identificacion tributaria del cliente.",
						null));

				FacesContext.getCurrentInstance().validationFailed();

				return;
			}

			oTrf.setTipoIdentTribDestino(oGestionObjFac.createPlatTransferenciaTipoIdentTribDestino(oTiIdenTribDes));
			oTrf.setNroIdenTribDestino(
					oGestionObjFac.createPlatTransferenciaNroIdenTribDestino(oCli.getCUIT().getValue().toString()));

			oMoneda = new PlatMoneda();
			oMoneda.setIdMoneda(oGestionObjFac.createPlatMonedaIdMoneda(this.getCuentaDestino().getIdMoneda()));
			oTrf.setMoneda(oGestionObjFac.createPlatTransferenciaMoneda(oMoneda));

			// oTrf.setNroComprobanteOrigen(value);
			oTrf.setObs(oGestionObjFac.createPlatTransferenciaObs(this.getItemTransferencia().getObs()));
			// oTrf.setReferencia(oGestionObjFac.createPlatTransferenciaReferencia(this.getItemTransferencia().getReferencia()));

			oRTA = GestionServiceHelper.getGestionService(CfgTimeout.DEFAULT).agregarPlatTransferencia(this.getHeaderSeguridad(), oTrf);

			if (oRTA != null && oRTA.getError().getValue() != null
					&& oRTA.getError().getValue().getHayError().getValue().booleanValue()) {
				LogACGHelper.escribirLog(null,
						"grabarItmTransferencia. |" + oRTA.getError().getValue().getCodigoError().getValue() + " - "
								+ oRTA.getError().getValue().getMsgError().getValue() + "|");

				FacesContext.getCurrentInstance().addMessage(null,
						new FacesMessage(FacesMessage.SEVERITY_ERROR,
								"Solicitud de dinero. " + oRTA.getError().getValue().getCodigoError().getValue() + " - "
										+ oRTA.getError().getValue().getMsgError().getValue(),
								null));

				FacesContext.getCurrentInstance().validationFailed();
			} else {
				FacesContext.getCurrentInstance().addMessage("msgsProductos",
						new FacesMessage(FacesMessage.SEVERITY_INFO, "Solicitud de dinero realizada con exito.", null));

				this.itemTransferencia = null;
				lstUpdate.add("msgsProductos");
				lstUpdate.add("solicitudesDeDineroDT");
				this.muestraCRUDSolicituDeDinero = false;
			}
		} catch (Exception e) {
			LogACGHelper.escribirLog(null, "grabarItmTransferencia. |" + e.getMessage() + "|");

			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
					"Se produjo un error al realizar la solicitud de dinero. Intente la operacion nuevamente", null));
			lstUpdate.add("msgsGenDepEnCta");
			FacesContext.getCurrentInstance().validationFailed();
		}

		PrimeFaces.current().ajax().update(lstUpdate);
		PrimeFaces.current().executeScript("ACGSiteScriptsFCNS.resultadoResize();");
	}

	public void eliminarSolicitudDeDinero() {
		if (itemTransferencia != null) {
			PrimeFaces.current().ajax().update("confirmaDelSolDinDialog");
			PrimeFaces.current().executeScript("PF('confirmaDelSolDinDialogWV').show()");
		} else {
			FacesContext.getCurrentInstance().addMessage("msgsProductos", new FacesMessage(FacesMessage.SEVERITY_WARN,
					"Debe seleccionar una operacion para realizar la anulacion.", null));
			PrimeFaces.current().ajax().update("msgsProductos");
		}

	}

	public void configrmarEliminacionSolicitudDeDinero() {
		PlatTransferencia oTrf = null;
		RespString oRTA = null;
		
		if (itemTransferencia != null) {
			oTrf = new PlatTransferencia();

			oTrf.setIdCliente(oGestionObjFac.createPlatTransferenciaIdCliente(this.getUsuario().getIdCliente()));
			oTrf.setIdMayorista(oGestionObjFac.createPlatTransferenciaIdMayorista(this.getUsuario().getIdMayorista()));
			oTrf.setIdTransferencia(
					oGestionObjFac.createPlatTransferenciaIdTransferencia(itemTransferencia.getIdTransferencia()));
			
			oRTA = GestionServiceHelper.getGestionService(CfgTimeout.DEFAULT).eliminarPlatTransferencia(this.getHeaderSeguridad(), oTrf);

			if (oRTA != null && oRTA.getError() != null && oRTA.getError().getValue().getHayError().getValue()) {
				LogACGHelper.escribirLog(null,
						"eliminarDestinatario. |" + oRTA.getError().getValue().getCodigoError().getValue() + " - "
								+ oRTA.getError().getValue().getMsgError().getValue() + "|");

				FacesContext.getCurrentInstance().addMessage("msgsProductos",
						new FacesMessage(FacesMessage.SEVERITY_ERROR,
								"Eliminacion de solicitud de dinero. "
										+ oRTA.getError().getValue().getCodigoError().getValue() + " - "
										+ oRTA.getError().getValue().getMsgError().getValue(),
								null));

				FacesContext.getCurrentInstance().validationFailed();
			} else {
				FacesContext.getCurrentInstance().addMessage("msgsProductos", new FacesMessage(
						FacesMessage.SEVERITY_INFO, "Eliminacion de solicitud de dinero realizada con exito.", null));

				itemTransferencia = null;
			}
		} else {
			FacesContext.getCurrentInstance().addMessage("msgsProductos", new FacesMessage(FacesMessage.SEVERITY_WARN,
					"Debe seleccionar una solicitud de dinero para realizar la operacion.", null));
		}
	}

	// FIN Metodos de Solicitud de dinero

	// INICIO Metodos de Deposito en cuenta
	public void detalleDeposito() {
		PlatEntidadFinanciera oEntFi = null;
		PlatTipoIdentifCta oTipIdenCta = null;

		if (itemTransferencia != null) {
			try {
				operacion = "DETALLE";
				this.muestraCRUDTransferencia = true;

				oEntFi = new PlatEntidadFinanciera();
				oEntFi.setId(oGestionObjFac.createPlatEntidadFinancieraId(itemTransferencia.getIdEntidadOrigen()));
				this.entidadFinanciera = this.obtenerEntidadFinanciera(oEntFi);

				oTipIdenCta = new PlatTipoIdentifCta();
				oTipIdenCta.setIdTipoIdentifCta(oGestionObjFac
						.createPlatTipoIdentifCtaIdTipoIdentifCta(itemTransferencia.getIdTipoIdentifOrigen()));
				this.tipoIdentificacionCta = this.obtenerTipoIdentificacionCta(oTipIdenCta);

				this.cuentaDestino = new Cuenta();
				this.cuentaDestino.setNumeroCuenta(itemTransferencia.getCodigoIdentifDestino());
				this.cuentaDestino = obtenerCuentaSuperPago(this.cuentaDestino);

				PrimeFaces.current().ajax().update("admDepoDialog");
				PrimeFaces.current().executeScript("PF('admDepoDialogWV').show()");
			} catch (Exception e) {
				LogACGHelper.escribirLog(null,
						"detalleTransferencia. Se produjo un error al obtener el detalle del deposito. - MsgErr: |"
								+ e.getMessage() + "|");

				FacesContext.getCurrentInstance().addMessage("msgsProductos", new FacesMessage(
						FacesMessage.SEVERITY_WARN, "Se produjo un error al obtener el detalle del deposito.", null));
				PrimeFaces.current().ajax().update("msgsProductos");
			}
		} else {
			FacesContext.getCurrentInstance().addMessage("msgsProductos", new FacesMessage(FacesMessage.SEVERITY_WARN,
					"Debe seleccionar un deposito para realizar la operacion.", null));
			PrimeFaces.current().ajax().update("msgsProductos");
		}
	}

	public void altaDeposito() {
		operacion = "ALTA";
		itemTransferencia = new ItemTransferencia();
		itemAgendaDestinatario = null;
		this.setCuitCliente(null);
		this.muestraCRUDTransferencia = true;
		PrimeFaces.current().ajax().update("admDepoDialog");
		PrimeFaces.current().executeScript("PF('admDepoDialogWV').show()");
	}

	public void procesarDepositoEnCuenta() {
		PlatTransferencia oTrf = null;
		PlatTipoIdentifCta oTiIdeOri = null;
		PlatTipoIdentifCta oTiIdeDest = null;
		PlatClasificacionTransferencia oClaTrf = null;
		PlatCanal oCanal = null;
		PlatMoneda oMoneda = null;
		PlatEntidadFinanciera oEntFiOri = null;
		PlatEntidadFinanciera oEntFiDest = null;
		PlatTipoDocumento oTiIdenTribDes = null;
		Cliente oCli = null;
		RespAcredPlatCta oRTA = null;
		ArrayList<String> lstUpdate = new ArrayList<String>();
		GregorianCalendar gcFechaSaldo = null;
		XMLGregorianCalendar xmlGCFechaSaldo = null;
		Usuario oUsr = null;
		
		try {
			oTrf = new PlatTransferencia();

			oTrf.setIdMayorista(oGestionObjFac.createPlatTransferenciaIdMayorista(this.getUsuario().getIdMayorista()));
			oTrf.setIdCliente(oGestionObjFac.createPlatTransferenciaIdCliente(this.getUsuario().getIdCliente()));
			oUsr = oGestionObjFac.createUsuario();
			oUsr.setIdUsuario(oGestionObjFac.createUsuarioIdUsuario(this.getUsuario().getIdUsuario()));
			oTrf.setUsuario(oGestionObjFac.createPlatTransferenciaUsuario(oUsr));

			oCanal = new PlatCanal();
			oCanal.setIdCanal(oGestionObjFac.createPlatCanalIdCanal("WEBINTRA"));
			oTrf.setCanal(oGestionObjFac.createPlatTransferenciaCanal(oCanal));

			oClaTrf = new PlatClasificacionTransferencia();
			oClaTrf.setCodMnemonico(oGestionObjFac.createPlatClasificacionTransferenciaCodMnemonico("DEPD"));
			oTrf.setClasificacion(oGestionObjFac.createPlatTransferenciaClasificacion(oClaTrf));

			gcFechaSaldo = new GregorianCalendar();
			gcFechaSaldo.setTime(new Date());

			xmlGCFechaSaldo = DatatypeFactory.newInstance().newXMLGregorianCalendar(gcFechaSaldo);

			oTrf.setFechaTransferencia(xmlGCFechaSaldo);

			// Carga el objeto completo de la coleccion
			if(this.getItemAgendaDestinatario() != null) {
				this.setItemAgendaDestinatario(this.getItemsAgendaDestinatariosPublicos()
						.get(this.getItemsAgendaDestinatariosPublicos().indexOf(this.getItemAgendaDestinatario())));
	
				oEntFiOri = new PlatEntidadFinanciera();
				oEntFiOri.setId(
						oGestionObjFac.createPlatEntidadFinancieraId(itemAgendaDestinatario.getIdEntidadFinanciera()));
				oTrf.setEntidadOrigen(oGestionObjFac.createPlatTransferenciaEntidadOrigen(oEntFiOri));
	
				oTiIdeOri = new PlatTipoIdentifCta();
				oTiIdeOri.setIdTipoIdentifCta(oGestionObjFac
						.createPlatTipoIdentifCtaIdTipoIdentifCta(itemAgendaDestinatario.getIdTipoIdentifCta()));
				oTrf.setTipoIdentifOrigen(oGestionObjFac.createPlatTransferenciaTipoIdentifOrigen(oTiIdeOri));
				oTrf.setCodigoIdentifOrigen(oGestionObjFac
						.createPlatTransferenciaCodigoIdentifOrigen(itemAgendaDestinatario.getCodigoIdentifCta()));
			}
			
			oEntFiDest = new PlatEntidadFinanciera();
			oEntFiDest.setCodMnemonico(oGestionObjFac.createPlatEntidadFinancieraCodMnemonico("SUPERPAGO"));
			oTrf.setEntidadDestino(oGestionObjFac.createPlatTransferenciaEntidadDestino(oEntFiDest));

			oTiIdeDest = new PlatTipoIdentifCta();
			oTiIdeDest.setCodMnemonico(oGestionObjFac.createPlatTipoIdentifCtaCodMnemonico("CTASP"));
			oTrf.setTipoIdentifDestino(oGestionObjFac.createPlatTransferenciaTipoIdentifDestino(oTiIdeDest));
			oTrf.setCodigoIdentifDestino(
					oGestionObjFac.createPlatTransferenciaCodigoIdentifDestino(cuentaDestino.getNumeroCuenta()));

			oTrf.setConcepto(oGestionObjFac.createPlatTransferenciaConcepto(this.getItemTransferencia().getConcepto()));
			oTrf.setReferencia(
					oGestionObjFac.createPlatTransferenciaReferencia(this.getItemTransferencia().getReferencia()));
			oTrf.setNroComprobanteOrigen(oGestionObjFac.createPlatTransferenciaNroComprobanteOrigen(
					this.getItemTransferencia().getNroComprobanteOrigen()));
			oTrf.setImporte(oGestionObjFac.createPlatTransferenciaImporte(this.getItemTransferencia().getImporte()));
			oTrf.setObs(oGestionObjFac.createPlatTransferenciaObs(this.getItemTransferencia().getObs()));

			oTrf.setDestinoEsCtaPropia(oGestionObjFac.createPlatTransferenciaDestinoEsCtaPropia(true));
			oTrf.setDestinoEsCtaCte(oGestionObjFac.createPlatTransferenciaDestinoEsCtaCte(false));

			oCli = new Cliente();
			oCli.setIdMayorista(oGestionObjFac.createClienteIdMayorista(this.getUsuario().getIdMayorista()));
			oCli.setIdCliente(oGestionObjFac.createClienteIdCliente(this.getUsuario().getIdCliente()));
			oCli = GestionServiceHelper.getGestionService(CfgTimeout.DEFAULT).mostrarClienteObj(oCli);

			if (oCli == null || oCli.getCUIT().getValue() == null || oCli.getCUIT().getValue().compareTo(0L) == 0) {
				LogACGHelper.escribirLog(null, "procesarDepositoEnCuenta. |No se pudo obtener el cliente.|");

				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
						"Deposito en cuenta. No se pudo obtener cliente destino.", null));

				FacesContext.getCurrentInstance().validationFailed();

				return;
			}

			// TODO Se pone hardcode CUIT porque en la platadorma no hay campo para
			// diferenciar entre CUIT y CUIL
			oTiIdenTribDes = new PlatTipoDocumento();
			oTiIdenTribDes.setCodMnemonico(oGestionObjFac.createPlatTipoDocumentoCodMnemonico("CUIT"));
			oTiIdenTribDes = GestionServiceHelper.getGestionService(CfgTimeout.DEFAULT)
					.obtenerPlatTipoDocumento(oTiIdenTribDes);

			if (oTiIdenTribDes == null || oTiIdenTribDes.getIdTipoDocumento().getValue() == null) {
				LogACGHelper.escribirLog(null,
						"procesarDepositoEnCuenta. |No se pudo obtener el tipo de identificacion tributaria del cliente.|");

				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
						"Deposito en cuenta. No se pudo obtener el tipo de identificacion tributaria del cliente.",
						null));

				FacesContext.getCurrentInstance().validationFailed();

				return;
			}

			oTrf.setTipoIdentTribDestino(oGestionObjFac.createPlatTransferenciaTipoIdentTribDestino(oTiIdenTribDes));
			oTrf.setNroIdenTribDestino(
					oGestionObjFac.createPlatTransferenciaNroIdenTribDestino(oCli.getCUIT().getValue().toString()));

			oMoneda = new PlatMoneda();
			oMoneda.setIdMoneda(oGestionObjFac.createPlatMonedaIdMoneda(this.getCuentaDestino().getIdMoneda()));
			oTrf.setMoneda(oGestionObjFac.createPlatTransferenciaMoneda(oMoneda));

			oRTA = GestionServiceHelper.getGestionService(CfgTimeout.DEFAULT).agregarPlatTransferencia(this.getHeaderSeguridad(), oTrf);

			if (oRTA != null && oRTA.getError() != null
					&& oRTA.getError().getValue().getHayError().getValue().booleanValue()) {
				LogACGHelper.escribirLog(null,
						"grabarItmTransferencia. |" + oRTA.getError().getValue().getCodigoError().getValue() + " - "
								+ oRTA.getError().getValue().getMsgError().getValue() + "|");

				FacesContext.getCurrentInstance().addMessage(null,
						new FacesMessage(FacesMessage.SEVERITY_ERROR,
								"Deposito en cuenta. " + oRTA.getError().getValue().getCodigoError().getValue() + " - "
										+ oRTA.getError().getValue().getMsgError().getValue(),
								null));
				
				lstUpdate.add("msgsProductos");
				FacesContext.getCurrentInstance().validationFailed();
			} else {
				FacesContext.getCurrentInstance().addMessage("msgsProductos",
						new FacesMessage(FacesMessage.SEVERITY_INFO, "Deposito en cuenta realizado con exito.", null));

				this.itemTransferencia = null;
				lstUpdate.add("msgsProductos");
				lstUpdate.add("depositosDT");
				this.muestraCRUDTransferencia = false;
				PrimeFaces.current().executeScript("ACGSiteScriptsFCNS.resultadoResize();");
			}
		} catch (Exception e) {
			LogACGHelper.escribirLog(null, "grabarItmTransferencia. |" + e.getMessage() + "|");

			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
					"Se produjo un error al realizar el deposito en cuenta. Intente la operacion nuevamente", null));

			lstUpdate.add("msgsProductos");
			FacesContext.getCurrentInstance().validationFailed();
		}

		PrimeFaces.current().ajax().update(lstUpdate);

	}

	public void cancelarDialogoDepositoEnCuenta() {
		operacion = "";
		this.muestraCRUDTransferencia = false;
		this.setItemTransferencia(null);
		PrimeFaces.current().executeScript("PF('admDepoDialogWV').hide();");
	}

	// FIN Metodos de Deposito en cuenta
	public void buscarCliente() {
		ArrayList<String> lstUpdate = new ArrayList<String>();
		Cliente oCli = null;

		try {
			oCli = new Cliente();
			oCli.setIdMayorista(oGestionObjFac.createClienteIdMayorista(this.getUsuario().getIdMayorista()));
			oCli.setCUIT(oGestionObjFac.createClienteCUIT(getCuitCliente()));

			this.setCliente(GestionServiceHelper.getGestionService(CfgTimeout.DEFAULT).mostrarClienteObj(oCli));

			if (this.getCliente() == null || this.getCliente().getIdMayorista().getValue() == null
					|| oCli.getIdCliente().getValue() == null) {
				FacesContext.getCurrentInstance().addMessage(null,
						new FacesMessage(FacesMessage.SEVERITY_INFO, "El idCliente informado no existe", null));

				lstUpdate.add("msgsAdminSolDin");
				PrimeFaces.current().ajax().update(lstUpdate);
				FacesContext.getCurrentInstance().validationFailed();
				return;
			}

			if (this.getUsuario().getIdCliente().compareTo(oCli.getIdCliente().getValue()) == 0) {
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,
						"El idCliente informado es el mismo que el usuario logueado", null));

				lstUpdate.add("msgsAdminSolDin");
				PrimeFaces.current().ajax().update(lstUpdate);
				FacesContext.getCurrentInstance().validationFailed();
				return;
			}

			lstUpdate.add("descCli");
		} catch (Exception e) {
			LogACGHelper.escribirLog(null, "buscarCliente. |" + e.getMessage() + "|");

			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
					"Se produjo un error en la busqueda del cliente", null));
			lstUpdate.add("msgsGenSolDin");
		}

		PrimeFaces.current().ajax().update(lstUpdate);

	}

	public Cliente getCliente() {
		return cliente;
	}

	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
	}

	public PlatEntidadFinanciera getEntidadFinanciera() {
		return entidadFinanciera;
	}

	public void setEntidadFinanciera(PlatEntidadFinanciera entidadFinanciera) {
		this.entidadFinanciera = entidadFinanciera;
	}

	public PlatTipoIdentifCta getTipoIdentificacionCta() {
		return tipoIdentificacionCta;
	}

	public void setTipoIdentificacionCta(PlatTipoIdentifCta tipoIdentificacionCta) {
		this.tipoIdentificacionCta = tipoIdentificacionCta;
	}

	private PlatEntidadFinanciera obtenerEntidadFinanciera(PlatEntidadFinanciera entidadFinanciera) throws Exception {
		return GestionServiceHelper.getGestionService(CfgTimeout.DEFAULT)
				.obtenerPlatEntidadFinanciera(entidadFinanciera);
	}

	private PlatTipoIdentifCta obtenerTipoIdentificacionCta(PlatTipoIdentifCta tipoIdentifCta) throws Exception {
		return GestionServiceHelper.getGestionService(CfgTimeout.DEFAULT).obtenerPlatTipoIdentifCta(tipoIdentifCta);
	}

	public Long getCuitCliente() {
		return cuitCliente;
	}

	public void setCuitCliente(Long cuitCliente) {
		this.cuitCliente = cuitCliente;
	}

	public String getComentarios() {
		return comentarios;
	}

	public void setComentarios(String comentarios) {
		this.comentarios = comentarios;
	}

	public Date getFechaDesde() {
		if (fechaDesde == null) {
			Calendar oCal = Calendar.getInstance();
			oCal.add(Calendar.MONTH, -6);
			oCal.set(Calendar.HOUR_OF_DAY, 0);
			oCal.set(Calendar.MINUTE, 0);
			oCal.set(Calendar.SECOND, 0);
			oCal.set(Calendar.MILLISECOND, 0);

			fechaDesde = oCal.getTime();
		}

		return fechaDesde;
	}

	public void setFechaDesde(Date fechaDesde) {
		this.fechaDesde = fechaDesde;
	}

	public Date getFechaHasta() {
		if (fechaHasta == null) {
			Calendar oCal = Calendar.getInstance();
			oCal.set(Calendar.HOUR_OF_DAY, 23);
			oCal.set(Calendar.MINUTE, 59);
			oCal.set(Calendar.SECOND, 59);
			oCal.set(Calendar.MILLISECOND, 59);

			fechaHasta = oCal.getTime();
		}

		return fechaHasta;
	}

	public void setFechaHasta(Date fechaHasta) {
		this.fechaHasta = fechaHasta;
	}

	public Date getFechaHoy() {
		if (fechaHoy == null) {
			Calendar oCal = Calendar.getInstance();
			oCal.set(Calendar.HOUR_OF_DAY, 23);
			oCal.set(Calendar.MINUTE, 59);
			oCal.set(Calendar.SECOND, 59);
			oCal.set(Calendar.MILLISECOND, 59);

			fechaHoy = oCal.getTime();
		}

		return fechaHoy;
	}

	public void setFechaHoy(Date fechaHoy) {
		this.fechaHoy = fechaHoy;
	}

	public Date getFechaAhora() {
		if (fechaAhora == null)
			fechaAhora = new Date();

		return fechaAhora;
	}

	public void setFechaAhora(Date fechaAhora) {
		this.fechaAhora = fechaAhora;
	}

	public void borrarFiltrosFecha(String pantallaInicializacion) {
		this.setFechaDesde(null);
		this.setFechaHasta(null);
		this.setFechaAhora(fechaAhora);
		
		switch(pantallaInicializacion) {
			case "MOVCTA":
				filtrarMovimientosDeCuenta();
				break;
			case "MOVCTAPEN":
				filtrarMovimientosDeCtaPendientes();
				break;
			case "TRF":
				filtrarTransferencias();
				break;
			case "SDIN":
				filtrarSolicitudesDeDinero();
				break;
			case "RDIN":
				filtrarRequerimientosDeDinero();
				break;
			case "DEPD":
				filtrarDepositosEnCuenta();
				break;
		}
	}

	public void filtrarTransferencias() {
		ArrayList<String> inclusiones = new ArrayList<String>();

		inclusiones.add("TENT");
		inclusiones.add("TSAL");

		this.listaDeTransferencias = new LazyTransferenciasDataModel(this.getUsuario().getIdCliente(),
				this.getUsuario().getIdMayorista(), cuentaAdministradora, null, null, inclusiones, null, "", "",
				this.getFechaDesde(), this.getFechaHasta(), (this.getUsuario().getPermisosReducidosEnModuloDeCuentas() ? this.getUsuario().getIdUsuario() : null));
	}

	public void filtrarSolicitudesDeDinero() {
		ArrayList<String> inclusiones = new ArrayList<String>();
		inclusiones.add("SDIN");

		this.listaDeTransferencias = new LazyTransferenciasDataModel(null, this.getUsuario().getIdMayorista(),
				cuentaAdministradora, null, this.getUsuario().getIdCliente(), inclusiones, null, "", "",
				this.getFechaDesde(), this.getFechaHasta(), (this.getUsuario().getPermisosReducidosEnModuloDeCuentas() ? this.getUsuario().getIdUsuario() : null));
	}

	public void filtrarRequerimientosDeDinero() {
		ArrayList<String> inclusiones = new ArrayList<String>();
		inclusiones.add("SDIN");

		this.listaDeTransferencias = new LazyTransferenciasDataModel(null, this.getUsuario().getIdMayorista(), null,
				this.getUsuario().getIdCliente(), null, inclusiones, null, "", "", this.getFechaDesde(),
				this.getFechaHasta(), (this.getUsuario().getPermisosReducidosEnModuloDeCuentas() ? this.getUsuario().getIdUsuario() : null));
	}

	public void filtrarDepositosEnCuenta() {
		ArrayList<String> inclusiones = new ArrayList<String>();
		inclusiones.add("DEPD");

		this.listaDeTransferencias = new LazyTransferenciasDataModel(this.getUsuario().getIdCliente(),
				this.getUsuario().getIdMayorista(), cuentaAdministradora, null, null, inclusiones, null, "", "",
				this.getFechaDesde(), this.getFechaHasta(), (this.getUsuario().getPermisosReducidosEnModuloDeCuentas() ? this.getUsuario().getIdUsuario() : null));
	}

	public void createCartesianLinerModel() {
		cartesianLinerModel = new LineChartModel();
		ChartData data = new ChartData();

		FiltroListadoPlatConsolidadoCuenta flpcc = new FiltroListadoPlatConsolidadoCuenta();

		ObjectFactory factory = new ObjectFactory();

		flpcc.setIdMayorista(factory.createFiltroListadoPlatConsolidadoCuentaIdMayorista(
				UsuarioHelper.usuarioSession().getSuperPagoInstance().getCuentaSeleccionada().getIdMayorista()));
		flpcc.setIdCuenta(factory.createFiltroListadoPlatConsolidadoCuentaIdCuenta(
				UsuarioHelper.usuarioSession().getSuperPagoInstance().getCuentaSeleccionada().getIdCuenta()));
		flpcc.setCampoOrden(oGestionObjFac.createFiltroListadoPlatConsolidadoCuentaCampoOrden("fecha"));
		flpcc.setTipoOrden(oGestionObjFac.createFiltroListadoPlatConsolidadoCuentaTipoOrden("ASC"));
		flpcc.setPage(factory.createFiltroListadoPlatConsolidadoCuentaPage(1));
		flpcc.setPageSize(factory.createFiltroListadoPlatConsolidadoCuentaPageSize(Integer.MAX_VALUE));

		try {

			Date referenceDate = new Date();
			Calendar c = Calendar.getInstance();
			c.setTime(referenceDate);
			c.add(Calendar.MONTH, -6);
			c.set(Calendar.DAY_OF_MONTH, 1);
			c.set(Calendar.HOUR_OF_DAY, 0);
			c.set(Calendar.MINUTE, 0);
			c.set(Calendar.SECOND, 0);

			GregorianCalendar gcFechaHoraDesde = new GregorianCalendar();
			gcFechaHoraDesde.setTime(c.getTime());
			XMLGregorianCalendar xmlGCFechaHoraDesde;
			xmlGCFechaHoraDesde = DatatypeFactory.newInstance().newXMLGregorianCalendar(gcFechaHoraDesde);
			flpcc.setFechaDesde(xmlGCFechaHoraDesde);

			c = Calendar.getInstance();
			c.setTime(referenceDate);
			c.set(Calendar.DAY_OF_MONTH, 1);
			c.set(Calendar.HOUR_OF_DAY, 0);
			c.set(Calendar.MINUTE, 0);
			c.set(Calendar.SECOND, 0);

			GregorianCalendar gcFechaHoraHasta = new GregorianCalendar();
			gcFechaHoraHasta.setTime(c.getTime());
			XMLGregorianCalendar xmlGCFechaHoraHasta;
			xmlGCFechaHoraHasta = DatatypeFactory.newInstance().newXMLGregorianCalendar(gcFechaHoraHasta);
			flpcc.setFechaHasta(xmlGCFechaHoraHasta);
		} catch (DatatypeConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		flpcc.setAgruparPor(factory.createFiltroListadoPlatConsolidadoCuentaAgruparPor("MONTH"));

		PlatConsolidadoCuentaList pccl = GestionServiceHelper.getGestionService(CfgTimeout.DEFAULT)
				.listarPlatConsolidadoCuentaTotalizado(flpcc);

		LineChartDataSet dataSet = new LineChartDataSet();
		List<Object> values = new ArrayList<>();

		if (pccl != null && pccl.getRegistros() != null && pccl.getRegistros().getValue() != null
				&& pccl.getRegistros().getValue().getPlatConsolidadoCuenta() != null) {
			for (PlatConsolidadoCuenta pmc : pccl.getRegistros().getValue().getPlatConsolidadoCuenta()) {
				values.add(pmc.getSaldo().getValue());
			}
		}

		dataSet.setData(values);
		dataSet.setLabel("Saldo Real por Mes");
		data.addChartDataSet(dataSet);

		List<String> labels = new ArrayList<>();
		SimpleDateFormat ff = new SimpleDateFormat("MM-yyyy");
		if (pccl != null && pccl.getRegistros() != null && pccl.getRegistros().getValue() != null
				&& pccl.getRegistros().getValue().getPlatConsolidadoCuenta() != null) {
			for (PlatConsolidadoCuenta pmc : pccl.getRegistros().getValue().getPlatConsolidadoCuenta()) {
				labels.add(ff.format(pmc.getFecha().toGregorianCalendar().getTime()).toString());
			}
		}

		data.setLabels(labels);
		cartesianLinerModel.setData(data);

	}

	public void exportarMovimientosDeCta() {
		PlatEstadoMovimientoCuenta oEstMovCta = null;
		PlatMovimientoCuentaList oLstMovCta = null;
		PlatCuenta oCta = null;
		PlatMovimientoCuenta oPlatMovCta = null;
		Cuenta oCtaAux = null;
		FiltroListadoPlatMovimientoCuenta oFltMovCta = null;
		List<MovimientoDeCuenta> oLst = null;
		Integer cantRegistros;
		Date referenceDate = null;
		Calendar oCal = null;
		GregorianCalendar gcFechaHoraDesde = null;
		XMLGregorianCalendar xmlGCFechaHoraDesde = null;
		String csvSepCamp = "";
		String csvSepDec = "";
		StringBuilder oSB = null;
		SimpleDateFormat oSDF = null;
		OutputStream oOutStream = null;
		OutputStreamWriter oOutStramWriter = null;
		PrintWriter oWriter = null;
		FacesContext oFacesCtx = null;
		ExternalContext oExtrCtx = null;
		// Operacion oOper = null;
		MovimientoDeCuenta oMovCtaTmp = null;
		Modulo oMod = null;
		TipoMovimiento oTipMov = null;

		try {
			oLst = new ArrayList<MovimientoDeCuenta>();
			cantRegistros = 0;

			oFltMovCta = new FiltroListadoPlatMovimientoCuenta();
			oFltMovCta.setIdMayorista(oGestionObjFac.createFiltroListadoPlatMovimientoCuentaIdMayorista(
					UsuarioHelper.usuarioSession().getSuperPagoInstance().getCuentaSeleccionada().getIdMayorista()));

			oCta = oGestionObjFac.createPlatCuenta();
			oCta.setIdCuenta(oGestionObjFac.createPlatCuentaIdCuenta(
					UsuarioHelper.usuarioSession().getSuperPagoInstance().getCuentaSeleccionada().getIdCuenta()));

			oPlatMovCta = new PlatMovimientoCuenta();
			oPlatMovCta.setCuenta(oGestionObjFac.createPlatMovimientoCuentaCuenta(oCta));

			oFltMovCta.setPage(oGestionObjFac.createFiltroListadoPlatMovimientoCuentaPage(1));
			oFltMovCta.setPageSize(oGestionObjFac.createFiltroListadoPlatMovimientoCuentaPageSize(Integer.MAX_VALUE));

			if (UsuarioHelper.usuarioSession().getSuperPagoInstance().getOperacionesPendientes()) {
				// OPERACIONES PENDIENTES
				oFltMovCta.setCampoOrden(oGestionObjFac
						.createFiltroListadoPlatMovimientoCuentaCampoOrden("fechaMovimiento DESC, idOrigen, nroOrden"));
			} else {
				oFltMovCta.setCampoOrden(oGestionObjFac.createFiltroListadoPlatMovimientoCuentaCampoOrden(
						"fechaAcreditacion DESC, idOrigen, nroOrden"));
			}
			oFltMovCta.setTipoOrden(oGestionObjFac.createFiltroListadoPlatMovimientoCuentaTipoOrden(""));

			referenceDate = new Date();
			oCal = Calendar.getInstance();
			oCal.setTime(referenceDate);
			oCal.add(Calendar.MONTH, -6);
			oCal.set(Calendar.DAY_OF_MONTH, 1);
			oCal.set(Calendar.HOUR_OF_DAY, 0);
			oCal.set(Calendar.MINUTE, 0);
			oCal.set(Calendar.SECOND, 0);
			// c.set(c.YEAR, c.MONTH, 1, 0, 0, 0);

			gcFechaHoraDesde = new GregorianCalendar();
			gcFechaHoraDesde.setTime(oCal.getTime());

			xmlGCFechaHoraDesde = DatatypeFactory.newInstance().newXMLGregorianCalendar(gcFechaHoraDesde);
			oPlatMovCta.setFechaMovimiento(xmlGCFechaHoraDesde);

			if (UsuarioHelper.usuarioSession().getSuperPagoInstance().getOperacionesPendientes()) {
				// OPERACIONES PENDIENTES
				oEstMovCta = new PlatEstadoMovimientoCuenta();
				oEstMovCta.setCodMnemonico(oGestionObjFac.createPlatEstadoMovimientoCuentaCodMnemonico("PENDACRE"));

				oPlatMovCta.setEstado(oGestionObjFac.createPlatMovimientoCuentaEstado(oEstMovCta));

				// flpmc.setCodMnemonicoEstado(oGestionObjFac.createFiltroListadoPlatMovimientoCuentaCodMnemonicoEstado("PENDACRE"));
				oFltMovCta.setMovimientoCuenta(
						oGestionObjFac.createFiltroListadoPlatMovimientoCuentaMovimientoCuenta(oPlatMovCta));
				oLstMovCta = GestionServiceHelper.getGestionService(CfgTimeout.DEFAULT)
						.listarPlatMovimientoCuenta(oFltMovCta);
			} else {
				// OPERACIONES ACREDITADAS
				oEstMovCta = new PlatEstadoMovimientoCuenta();
				oEstMovCta.setCodMnemonico(oGestionObjFac.createPlatEstadoMovimientoCuentaCodMnemonico("ACRE"));

				oPlatMovCta.setEstado(oGestionObjFac.createPlatMovimientoCuentaEstado(oEstMovCta));

				// flpmc.setCodMnemonicoEstado(oGestionObjFac.createFiltroListadoPlatMovimientoCuentaCodMnemonicoEstado("ACRE"));
				oFltMovCta.setMovimientoCuenta(
						oGestionObjFac.createFiltroListadoPlatMovimientoCuentaMovimientoCuenta(oPlatMovCta));
				oLstMovCta = GestionServiceHelper.getGestionService(CfgTimeout.DEFAULT)
						.listarPlatMovimientoCuenta(oFltMovCta);
			}

			// Obtengo el saldo en caso de ser necesario
			if (!UsuarioHelper.usuarioSession().getSuperPagoInstance().getOperacionesPendientes()) {
				oCtaAux = this.obtenerCuentaSuperPago(UsuarioHelper.usuarioSession().getSuperPagoInstance().getCuentaSeleccionada());
				
				if(oCtaAux != null) {
					UsuarioHelper.usuarioSession().getSuperPagoInstance().getCuentaSeleccionada().setSaldo(oCtaAux.getSaldo());
				} else {
					UsuarioHelper.usuarioSession().getSuperPagoInstance().getCuentaSeleccionada().setSaldo(0F);
				}				
			}

			// GENERO Y LIMPIO LAS VARIABLES PARA LA EXPORTACION
			csvSepCamp = this.getUsuario().getCsvSeparadorCampo();
			csvSepDec = this.getUsuario().getCsvSeparadorDecimales();

			oSB = new StringBuilder();

			if (oLstMovCta == null) {
				FacesContext.getCurrentInstance().addMessage(null,
						new FacesMessage(FacesMessage.SEVERITY_ERROR, "No se obtuvieron resultados.", null));
			} else {
				if (oLstMovCta.getError().getValue().getHayError().getValue()) {
					FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
							oLstMovCta.getError().getValue().getMsgError().getValue(), null));
				} else {
					if (oLstMovCta.getRegistros() != null && oLstMovCta.getRegistros().getValue() != null
							&& oLstMovCta.getRegistros().getValue().getPlatMovimientoCuenta() != null) {
						for (PlatMovimientoCuenta oMovCtaAux : oLstMovCta.getRegistros().getValue()
								.getPlatMovimientoCuenta()) {
							// oOper = new Operacion();
							oMovCtaTmp = new MovimientoDeCuenta();
							oMovCtaTmp.setFechaMovimiento(
									oMovCtaAux.getFechaMovimiento().toGregorianCalendar().getTime());
							oTipMov = new TipoMovimiento();
							oTipMov.setIdTipoMovimiento(
									oMovCtaAux.getTipoMovimiento().getValue().getIdPlatTipoMovimiento().getValue());
							oTipMov.setDescripcion(
									oMovCtaAux.getTipoMovimiento().getValue().getDescripcion().getValue());

							oMovCtaTmp.setTipoMovimiento(oTipMov);
							/*
							 * oOper.setTipoMovimientoDescripcion(
							 * oMovCtaAux.getTipoMovimiento().getValue().getDescripcion().getValue());
							 */

							if (oMovCtaAux.getFechaAcreditacion() != null) {
								oMovCtaTmp.setFechaAcreditacion(
										oMovCtaAux.getFechaAcreditacion().toGregorianCalendar().getTime());
							}

							oMovCtaTmp.setConcepto(oMovCtaAux.getConcepto().getValue());
							oMovCtaTmp.setImpCredito(oMovCtaAux.getImpCredito().getValue());
							oMovCtaTmp.setImpDebito(oMovCtaAux.getImpDebito().getValue());
							oMovCtaTmp.setImporte(oMovCtaAux.getImporte().getValue());
							oMod = new Modulo();
							oMod.setIdModulo(oMovCtaAux.getModuloOrigen().getValue().getIdModulo().getValue());
							oMod.setCodigoModulo(oMovCtaAux.getModuloOrigen().getValue().getCodigoModulo().getValue());
							oMovCtaTmp.setModuloOrigen(oMod);
							// oOper.setIdModuloOrigen(oMovCtaAux.getModuloOrigen().getValue().getIdModulo().getValue());
							// oOper.setCodMnemonicoModOrigen(oMovCtaAux.getModuloOrigen().getValue().getCodigoModulo().getValue());
							oMovCtaTmp.setIdOrigen(oMovCtaAux.getIdOrigen().getValue());
							oMovCtaTmp.setNroOrden(oMovCtaAux.getNroOrden().getValue());
							// oOper.setOrden(oMovCtaAux.getNroOrden().getValue());
							//oMovCtaTmp.setAdmiteDetalleOrigen(oMovCtaAux.getAdmiteDetalleOrigen().getValue());
							oMovCtaTmp.setIdClasificacion(oMovCtaAux.getClasificacion().getValue().getIdClasificacionMovimientoCuenta().getValue());
							oMovCtaTmp.setCodMnemonicoClasificacion(oMovCtaAux.getClasificacion().getValue().getCodMnemonico().getValue());
							oMovCtaTmp.setDescClasificacion(oMovCtaAux.getClasificacion().getValue().getDescripcion().getValue());
							oLst.add(oMovCtaTmp);
							
							oMovCtaTmp.setIdCliente(oMovCtaAux.getCliente().getValue().getIdCliente().getValue());
							oMovCtaTmp.setIdUsuario(oMovCtaAux.getUsuario().getValue().getIdUsuario().getValue());
							oMovCtaTmp.setUsuario(oMovCtaAux.getUsuario().getValue().getUsuario().getValue());
						}

						if (oLst == null || oLst.size() == 0) {
							FacesContext.getCurrentInstance().addMessage(null,
									new FacesMessage(FacesMessage.SEVERITY_ERROR,
											"No existe informacion para la consulta realizada.", null));
						} else {
							// CREO HEADER DE EXPORTACION SI ES NECESARIO PARA EXPORTAR A CSV
							// Header

							oSB.append((char) 34).append("Cuenta Nro.: ").append((char) 34).append(csvSepCamp);
							oSB.append((char) 34).append((char) 39).append(this.cuentaAdministradora.getNumeroCuenta())
									.append((char) 34).append(csvSepCamp);
							oSB.append((char) 13).append((char) 10);

							oSB.append((char) 34).append("Fecha").append((char) 34).append(csvSepCamp);
							oSB.append((char) 34).append("Id. Usuario").append((char) 34).append(csvSepCamp);
							oSB.append((char) 34).append("Ultimos Movimientos").append((char) 34).append(csvSepCamp);

							if (UsuarioHelper.usuarioSession().getSuperPagoInstance().isMostrarDebeHaber()) {
								oSB.append((char) 34).append("Acreditaciones").append((char) 34).append(csvSepCamp);
								oSB.append((char) 34).append("Debitos").append((char) 34).append(csvSepCamp);
							} else {
								oSB.append((char) 34).append("Importe").append((char) 34).append(csvSepCamp);
							}

							oSB.append((char) 13).append((char) 10);

							oSDF = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

							for (MovimientoDeCuenta oOperAuxArchivo : oLst) {
								// CREO ARCHIVO DE EXPORTACION SI ES NECESARIO PARA EXPORTAR A CSV
								if (UsuarioHelper.usuarioSession().getSuperPagoInstance().getOperacionesPendientes()) {
									// OPERACIONES PENDIENTES

									oSB.append((char) 34)
											.append(oOperAuxArchivo.getFechaMovimiento() == null ? ""
													: oSDF.format(oOperAuxArchivo.getFechaMovimiento()))
											.append((char) 34).append(csvSepCamp);
								} else {
									// OPERACIONES ACREDITADAS

									oSB.append((char) 34)
											.append(oOperAuxArchivo.getFechaAcreditacion() == null ? ""
													: oSDF.format(oOperAuxArchivo.getFechaAcreditacion()))
											.append((char) 34).append(csvSepCamp);
								}

								oSB.append((char) 34).append(oOperAuxArchivo.getIdUsuario()).append((char) 34)
								.append(csvSepCamp);

								oSB.append((char) 34).append(oOperAuxArchivo.getConcepto()).append((char) 34)
										.append(csvSepCamp);

								if (UsuarioHelper.usuarioSession().getSuperPagoInstance().isMostrarDebeHaber()) {
									oSB.append((char) 34).append(oOperAuxArchivo.getImpCredito().toString()
											.replace(".", csvSepDec).replace(",", csvSepDec)).append((char) 34)
											.append(csvSepCamp);
									oSB.append((char) 34).append(oOperAuxArchivo.getImpDebito().toString()
											.replace(".", csvSepDec).replace(",", csvSepDec)).append((char) 34)
											.append(csvSepCamp);
								} else {
									oSB.append((char) 34).append(oOperAuxArchivo.getImporte().toString()
											.replace(".", csvSepDec).replace(",", csvSepDec)).append((char) 34)
											.append(csvSepCamp);
								}

								oSB.append((char) 13).append((char) 10);

								// Cantidad total de Registros devueltos
								cantRegistros += 1;
							}

							// GENERO ARCHIVO DE EXPORTACION SI ES NECESARIO PARA EXPORTAR A CSV
							oFacesCtx = FacesContext.getCurrentInstance();
							oExtrCtx = oFacesCtx.getExternalContext();

							oExtrCtx.responseReset();
							oExtrCtx.setResponseContentType("text/plain");
							oExtrCtx.setResponseContentLength(oSB.toString().length());
							oSDF = new SimpleDateFormat("yyyy-MM-dd_HHmmss");

							if (!UsuarioHelper.usuarioSession().getSuperPagoInstance().getOperacionesPendientes()) {
								oExtrCtx.setResponseHeader("Content-Disposition",
										"attachment; filename=\"" + oSDF.format(new Date()) + "_("
												+ this.getUsuario().getIdMayorista() + ")_" + "MovimientosDeCta_CtaNro_"
												+ this.cuentaAdministradora.getNumeroCuenta() + ".csv" + "\"");
							} else {
								oExtrCtx.setResponseHeader("Content-Disposition",
										"attachment; filename=\"" + oSDF.format(new Date()) + "_("
												+ this.getUsuario().getIdMayorista() + ")_"
												+ "MovimientosDeCtaPendientes_CtaNro_"
												+ this.cuentaAdministradora.getNumeroCuenta() + ".csv" + "\"");
							}

							oOutStream = oExtrCtx.getResponseOutputStream();
							oOutStramWriter = new OutputStreamWriter(oOutStream);
							oWriter = new PrintWriter(oOutStramWriter);
							oWriter.write(oSB.toString());
							oWriter.flush();
							oWriter.close();
							oSB.setLength(0);

							oFacesCtx.responseComplete();
						}
					}
				}
			}
		} catch (Exception e) {
			LogACGHelper.escribirLog(null,
					"Informe Super Pago Operaciones. Excepcion ejecutando el WS de consulta: |" + e.getMessage() + "|");
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
					"Error ejecutando el WS de consulta de Transacciones: |" + e.getMessage() + "|", null));
			FacesContext.getCurrentInstance().validationFailed();
			oLst = null;
		}

		PrimeFaces.current().executeScript("PF('blockPanelGral').hide()");
		return;

	}

	public void filtrarMovimientosDeCuenta() {
		ArrayList<String> exclusiones = new ArrayList<String>();
		exclusiones.add("ANUL");
		exclusiones.add("PENDACRE");

		this.movimientosDeCuenta = new LazyMovimientosDeCuentaDataModel(this.getUsuario().getIdMayorista(),
				this.getUsuario().getIdCliente(), cuentaAdministradora, null, exclusiones, "fechaAcreditacion", "DESC",
				this.getFechaDesde(), this.getFechaHasta(), (this.getUsuario().getPermisosReducidosEnModuloDeCuentas() ? this.getUsuario().getIdUsuario() : null));
		
		refrescarCuenta();
	}

	public void filtrarMovimientosDeCtaPendientes() {
		ArrayList<String> inclusiones = new ArrayList<String>();
		inclusiones.add("PENDACRE");

		this.movimientosDeCuenta = new LazyMovimientosDeCuentaDataModel(this.getUsuario().getIdMayorista(),
				this.getUsuario().getIdCliente(), cuentaAdministradora, inclusiones, null, "fechaAcreditacion", "DESC",
				null, null, (this.getUsuario().getPermisosReducidosEnModuloDeCuentas() ? this.getUsuario().getIdUsuario() : null));

		refrescarCuenta();
	}

	public LineChartModel getCartesianLinerModel() {
		return cartesianLinerModel;
	}

	public void setCartesianLinerModel(LineChartModel cartesianLinerModel) {
		this.cartesianLinerModel = cartesianLinerModel;
	}

	public void verDetalleMovCta(ToggleEvent event) {
		MovimientoDeCuenta movimientoDeCuenta = null;
		MensajeOutboundGateway msgOutBound = null;
		DataOutputFcnCOPA oDataOutCOPA = null;
		PlatTransferencia oTransferencia = null;

		if (event.getVisibility() == Visibility.VISIBLE) {
			try {
				movimientoDeCuenta = (MovimientoDeCuenta) event.getData();
				if ("GPE".equals(movimientoDeCuenta.getModuloOrigen().getCodigoModulo())) {
					this.getUsuario().getPagoElectronicoInstance();
					msgOutBound = PagoElectronicoHelper.ejecutarFuncionCOPA("",
							movimientoDeCuenta.getIdOrigen().toString(), 1, 1, 1);

					if (msgOutBound != null) {
						if (msgOutBound.getDataOutputFcn() != null) {
							if ("M0000".equals(msgOutBound.getDataOutputFcn().getCodigoRetorno())) {
								oDataOutCOPA = (DataOutputFcnCOPA) msgOutBound.getDataOutputFcn();
								this.detallePagoMEP = oDataOutCOPA.getPago();
							} else {
								LogACGHelper.escribirLog(null,
										"Consulta de Cobro. Error: |"
												+ msgOutBound.getDataOutputFcn().getCodigoRetorno() + " "
												+ msgOutBound.getDataOutputFcn().getMensajeRetorno() + "|");

								FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(
										FacesMessage.SEVERITY_ERROR, "Error al obtener el detalle del MEP.", null));

								PrimeFaces.current().ajax().update("msgsProductos");
							}
						}
					}
				} else if ("TRFELEC".equals(movimientoDeCuenta.getModuloOrigen().getCodigoModulo())) {
					oTransferencia = new PlatTransferencia();
					oTransferencia.setIdMayorista(
							oGestionObjFac.createPlatTransferenciaIdMayorista(this.getUsuario().getIdMayorista()));
					oTransferencia.setIdTransferencia(
							oGestionObjFac.createPlatTransferenciaIdTransferencia(movimientoDeCuenta.getIdOrigen()));
					oTransferencia = GestionServiceHelper.getGestionService(CfgTimeout.DEFAULT)
							.obtenerPlatTransferencia(oTransferencia);

					if (oTransferencia == null) {
						LogACGHelper.escribirLog(null,
								"No se pudo obtener el detalle del movimiento en el modulo origen");

						FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
								"No se pudo obtener el detalle del movimiento en el modulo origen", null));

						PrimeFaces.current().ajax().update("msgsProductos");
					} else {
						this.detalleTransferencia = new ItemTransferencia();
						detalleTransferencia.setIdTransferencia(oTransferencia.getIdTransferencia().getValue());
						detalleTransferencia.setIdMayorista(oTransferencia.getIdMayorista().getValue());
						detalleTransferencia.setIdUsuario(oTransferencia.getUsuario().getValue().getIdUsuario().getValue());
						detalleTransferencia.setUsuario(oTransferencia.getUsuario().getValue().getUsuario().getValue());
						detalleTransferencia.setIdCliente(oTransferencia.getIdCliente().getValue());
						detalleTransferencia.setFechaAlta((oTransferencia.getFechaAlta() != null
								? oTransferencia.getFechaAlta().toGregorianCalendar().getTime()
								: null));
						detalleTransferencia.setReferencia(oTransferencia.getReferencia().getValue());
						detalleTransferencia.setIdCanal((oTransferencia.getCanal().getValue() != null
								? oTransferencia.getCanal().getValue().getIdCanal().getValue()
								: null));
						detalleTransferencia.setDescCanal((oTransferencia.getCanal().getValue() != null
								? oTransferencia.getCanal().getValue().getDescripcion().getValue()
								: null));
						detalleTransferencia.setFechaTransferencia(
								oTransferencia.getFechaTransferencia().toGregorianCalendar().getTime());
						detalleTransferencia.setIdClasificacion((oTransferencia.getClasificacion().getValue() != null
								? oTransferencia.getClasificacion().getValue().getIdClasificacionTransferencia()
										.getValue()
								: null));
						detalleTransferencia.setDescClasificacion((oTransferencia.getClasificacion().getValue() != null
								? oTransferencia.getClasificacion().getValue().getDescripcion().getValue()
								: null));
						detalleTransferencia
								.setCodMnemonicoClasificacion((oTransferencia.getClasificacion().getValue() != null
										? oTransferencia.getClasificacion().getValue().getCodMnemonico().getValue()
										: null));
						detalleTransferencia.setIdMoneda((oTransferencia.getMoneda().getValue() != null
								? oTransferencia.getMoneda().getValue().getIdMoneda().getValue()
								: null));
						detalleTransferencia.setDescripcionMoneda((oTransferencia.getMoneda().getValue() != null
								? oTransferencia.getMoneda().getValue().getDescripcion().getValue()
								: null));
						detalleTransferencia.setSimboloMoneda((oTransferencia.getMoneda().getValue() != null
								? oTransferencia.getMoneda().getValue().getTxtSimbolo().getValue()
								: null));
						detalleTransferencia.setImporte(oTransferencia.getImporte().getValue());
						detalleTransferencia.setIdEntidadOrigen((oTransferencia.getEntidadOrigen().getValue() != null
								? oTransferencia.getEntidadOrigen().getValue().getId().getValue()
								: null));
						detalleTransferencia.setDescEntidadOrigen((oTransferencia.getEntidadOrigen().getValue() != null
								? oTransferencia.getEntidadOrigen().getValue().getDenominacion().getValue()
								: null));
						detalleTransferencia
								.setCodMnemonicoEntOrigen((oTransferencia.getEntidadOrigen().getValue() != null
										? oTransferencia.getEntidadOrigen().getValue().getCodMnemonico().getValue()
										: null));
						detalleTransferencia
								.setIdTipoIdentifOrigen((oTransferencia.getTipoIdentifOrigen().getValue() != null
										? oTransferencia.getTipoIdentifOrigen().getValue().getIdTipoIdentifCta()
												.getValue()
										: null));
						detalleTransferencia
								.setDescTipoIdentifOrigen((oTransferencia.getTipoIdentifOrigen().getValue() != null
										? oTransferencia.getTipoIdentifOrigen().getValue().getDescripcion().getValue()
										: null));
						detalleTransferencia.setCodigoIdentifOrigen(oTransferencia.getCodigoIdentifOrigen().getValue());
						detalleTransferencia
								.setIdTipoIdentifDestino((oTransferencia.getTipoIdentifDestino().getValue() != null
										? oTransferencia.getTipoIdentifDestino().getValue().getIdTipoIdentifCta()
												.getValue()
										: null));
						detalleTransferencia
								.setDescTipoIdentifDestino((oTransferencia.getTipoIdentifDestino().getValue() != null
										? oTransferencia.getTipoIdentifDestino().getValue().getDescripcion().getValue()
										: null));
						detalleTransferencia
								.setCodigoIdentifDestino(oTransferencia.getCodigoIdentifDestino().getValue());
						detalleTransferencia
								.setNroComprobanteOrigen(oTransferencia.getNroComprobanteOrigen().getValue());
						detalleTransferencia.setIdEntidadDestino((oTransferencia.getEntidadDestino().getValue() != null
								? oTransferencia.getEntidadDestino().getValue().getId().getValue()
								: null));
						detalleTransferencia
								.setDescEntidadDestino((oTransferencia.getEntidadDestino().getValue() != null
										? oTransferencia.getEntidadDestino().getValue().getDenominacion().getValue()
										: null));
						detalleTransferencia
								.setCodMnemonicoEntDestino((oTransferencia.getEntidadDestino().getValue() != null
										? oTransferencia.getEntidadDestino().getValue().getCodMnemonico().getValue()
										: null));
						detalleTransferencia.setConcepto(oTransferencia.getConcepto().getValue());
						detalleTransferencia
								.setFecUltMod(oTransferencia.getFecUltMod().toGregorianCalendar().getTime());
						detalleTransferencia.setUsuUltMod(oTransferencia.getUsuUltMod().getValue());
						detalleTransferencia.setObs(oTransferencia.getObs().getValue());
						detalleTransferencia.setFechaAcreditacionCuentaOrigen(
								(oTransferencia.getFechaAcreditacionCuentaOrigen() != null
										? oTransferencia.getFechaAcreditacionCuentaOrigen().toGregorianCalendar()
												.getTime()
										: null));
						detalleTransferencia.setIdMovimientoDeCuentaOrigen(
								oTransferencia.getIdMovimientoDeCuentaOrigen().getValue());
						detalleTransferencia.setFechaAcreditacionCuentaDestino(
								(oTransferencia.getFechaAcreditacionCuentaDestino() != null
										? oTransferencia.getFechaAcreditacionCuentaDestino().toGregorianCalendar()
												.getTime()
										: null));
						detalleTransferencia.setIdMovimientoDeCuentaDestino(
								oTransferencia.getIdMovimientoDeCuentaDestino().getValue());
						detalleTransferencia.setIdEstado((oTransferencia.getEstado().getValue() != null
								&& oTransferencia.getEstado().getValue().getIdEstadoTransferencia() != null
										? oTransferencia.getEstado().getValue().getIdEstadoTransferencia().getValue()
										: null));
						detalleTransferencia.setDescEstado((oTransferencia.getEstado().getValue() != null
								&& oTransferencia.getEstado().getValue().getDescripcion() != null
										? oTransferencia.getEstado().getValue().getDescripcion().getValue()
										: null));
						detalleTransferencia.setCodMnemonicoEstado((oTransferencia.getEstado().getValue() != null
								&& oTransferencia.getEstado().getValue().getCodMnemonico() != null
										? oTransferencia.getEstado().getValue().getCodMnemonico().getValue()
										: null));

						detalleTransferencia.setIdClienteOrigen((oTransferencia.getClienteOrigen().getValue() != null
								? oTransferencia.getClienteOrigen().getValue().getIdCliente().getValue()
								: null));
						detalleTransferencia.setCuitClienteOrigen((oTransferencia.getClienteOrigen().getValue() != null
								? oTransferencia.getClienteOrigen().getValue().getCUIT().getValue()
								: null));
						detalleTransferencia
								.setApellidoClienteOrigen((oTransferencia.getClienteOrigen().getValue() != null
										? oTransferencia.getClienteOrigen().getValue().getApellido().getValue()
										: null));
						detalleTransferencia
								.setNombreClienteOrigen((oTransferencia.getClienteOrigen().getValue() != null
										? oTransferencia.getClienteOrigen().getValue().getNombre().getValue()
										: null));
						detalleTransferencia
								.setNombreFantasiaClienteOrigen((oTransferencia.getClienteOrigen().getValue() != null
										? oTransferencia.getClienteOrigen().getValue().getNombreFantasia().getValue()
										: null));
						detalleTransferencia
								.setRazonSocialClienteOrigen((oTransferencia.getClienteOrigen().getValue() != null
										? oTransferencia.getClienteOrigen().getValue().getRazonSocial().getValue()
										: null));

						detalleTransferencia.setIdClienteDestino((oTransferencia.getClienteDestino().getValue() != null
								? oTransferencia.getClienteDestino().getValue().getIdCliente().getValue()
								: null));
						detalleTransferencia
								.setCuitClienteDestino((oTransferencia.getClienteDestino().getValue() != null
										? oTransferencia.getClienteDestino().getValue().getCUIT().getValue()
										: null));
						detalleTransferencia
								.setApellidoClienteDestino((oTransferencia.getClienteDestino().getValue() != null
										? oTransferencia.getClienteDestino().getValue().getApellido().getValue()
										: null));
						detalleTransferencia
								.setNombreClienteDestino((oTransferencia.getClienteDestino().getValue() != null
										? oTransferencia.getClienteDestino().getValue().getNombre().getValue()
										: null));
						detalleTransferencia
								.setNombreFantasiaClienteDestino((oTransferencia.getClienteDestino().getValue() != null
										? oTransferencia.getClienteDestino().getValue().getNombreFantasia().getValue()
										: null));
						detalleTransferencia
								.setRazonSocialClienteDestino((oTransferencia.getClienteDestino().getValue() != null
										? oTransferencia.getClienteDestino().getValue().getRazonSocial().getValue()
										: null));
					}
				}
			} catch (Exception e) {
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL,
						"Error al obtener el detalle del movimiento de cuenta.", null));

				PrimeFaces.current().ajax().update("msgsProductos");
			}
		}
	}

	public Pago getDetallePagoMEP() {
		return detallePagoMEP;
	}

	public void setDetallePagoMEP(Pago detallePagoMEP) {
		this.detallePagoMEP = detallePagoMEP;
	}

	public ItemTransferencia getDetalleTransferencia() {
		return detalleTransferencia;
	}

	public void setDetalleTransferencia(ItemTransferencia detalleTransferencia) {
		this.detalleTransferencia = detalleTransferencia;
	}

	public boolean isValidaCBU() {
		return validaCBU;
	}

	public void setValidaCBU(boolean validaCBU) {
		this.validaCBU = validaCBU;
	}

	public void actualizarTipoIdenCta() {
		if (this.getTipoIdentificacionCuenta() != null) {
			// Carga el objeto completo de la coleccion
			this.setTipoIdentificacionCuenta(this.getTiposIdentificacionCuenta()
					.get(this.getTiposIdentificacionCuenta().indexOf(this.getTipoIdentificacionCuenta())));

			if ("CBU".equals(this.getTipoIdentificacionCuenta().getCodMnemonico()))
				this.setValidaCBU(true);
			else
				this.setValidaCBU(false);

			if (itemAgendaDestinatario != null)
				itemAgendaDestinatario.setIdTipoIdentifCta(this.getTipoIdentificacionCuenta().getIdTipoIdentifCta());

			this.setEntidadesFinancierasAgDest(null);
		}
	}

	public TipoIdentificacionCta getTipoIdentificacionCuenta() {
		return tipoIdentificacionCuenta;
	}

	public void setTipoIdentificacionCuenta(TipoIdentificacionCta tipoIdentificacionCuenta) {
		this.tipoIdentificacionCuenta = tipoIdentificacionCuenta;
	}

	public void validarNumeroCBU() {
		if (this.validaCBU) {
			this.entidadesFinancierasAgDest = null;
			if (!GestionServiceHelper.getGestionService(CfgTimeout.DEFAULT)
					.verifyCBU(this.itemAgendaDestinatario.getCodigoIdentifCta())) {
				FacesContext.getCurrentInstance().validationFailed();
				FacesContext.getCurrentInstance().addMessage("msgsProductos",
						new FacesMessage(FacesMessage.SEVERITY_INFO, "El CBU ingresado es invalido.", null));
				PrimeFaces.current().ajax().update("msgsProductos");
			}
		}
	}

	public ArrayList<MovimientoDeCuenta> getAranceles() {
		return aranceles;
	}

	public void setAranceles(ArrayList<MovimientoDeCuenta> aranceles) {
		this.aranceles = aranceles;
	}

	public boolean isMuestraAplicacionAranceles() {
		return muestraAplicacionAranceles;
	}

	public void setMuestraAplicacionAranceles(boolean muestraAplicacionAranceles) {
		this.muestraAplicacionAranceles = muestraAplicacionAranceles;
	}

	public Float getImporteTotalOperacion() {
		return importeTotalOperacion;
	}

	public void setImporteTotalOperacion(Float importeTotalOperacion) {
		this.importeTotalOperacion = importeTotalOperacion;
	}

	public Float getImporteTotalAranceles() {
		return importeTotalAranceles;
	}

	public void setImporteTotalAranceles(Float importeTotalAranceles) {
		this.importeTotalAranceles = importeTotalAranceles;
	}
	
	public void refrescarCuenta() {
		Cuenta oCta = null;
		oCta = this.getUsuario().getSuperPagoInstance().getCuentaSeleccionada();
		
		if(oCta != null) {
			try {
				oCta = obtenerCuentaSuperPago(oCta);

				if(oCta != null)
					this.getUsuario().getSuperPagoInstance().setCuentaSeleccionada(oCta);
			} catch (Exception e) {
				FacesContext.getCurrentInstance().validationFailed();
				FacesContext.getCurrentInstance().addMessage("msgsProductos",
						new FacesMessage(FacesMessage.SEVERITY_INFO, "Error al refrescar los datos de la cuenta.", null));
				PrimeFaces.current().ajax().update("msgsProductos");
			}
		}
	}
}