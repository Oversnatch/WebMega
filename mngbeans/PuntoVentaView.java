package com.americacg.cargavirtual.web.mngbeans;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Serializable;
import java.math.BigInteger;
import java.net.ConnectException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Random;
import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.PBEParameterSpec;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.inject.Named;
import javax.net.SocketFactory;
import javax.net.ssl.SSLSocketFactory;
import javax.servlet.http.Cookie;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import javax.xml.ws.WebServiceException;

import org.json.JSONObject;
import org.primefaces.PrimeFaces;
import org.springframework.context.annotation.Scope;

import com.americacg.cargavirtual.core.model.Ticket;
import com.americacg.cargavirtual.gestion.model.ArrayOfCabeceraProducto;
import com.americacg.cargavirtual.gestion.model.Banner;
import com.americacg.cargavirtual.gestion.model.CabeceraProducto;
import com.americacg.cargavirtual.gestion.model.CierreLote;
import com.americacg.cargavirtual.gestion.model.ConfigGral;
import com.americacg.cargavirtual.gestion.model.ObjectFactory;
import com.americacg.cargavirtual.gestion.model.Parametro;
import com.americacg.cargavirtual.gestion.model.ParametrosList;
import com.americacg.cargavirtual.gestion.model.RespPrestamo;
import com.americacg.cargavirtual.sube.enums.FuncionName;
import com.americacg.cargavirtual.sube.model.HeaderInGateway;
import com.americacg.cargavirtual.sube.model.MensajeOutboundGateway;
import com.americacg.cargavirtual.sube.model.funciones.FuncionGCAT;
import com.americacg.cargavirtual.sube.model.parametros.SeparadorTrama;
import com.americacg.cargavirtual.web.enums.CfgTimeout;
import com.americacg.cargavirtual.web.helpers.CookieHelper;
import com.americacg.cargavirtual.web.helpers.CoreServiceHelper;
import com.americacg.cargavirtual.web.helpers.GestionServiceHelper;
import com.americacg.cargavirtual.web.helpers.LogACGHelper;
import com.americacg.cargavirtual.web.mngbeans.reportes.ReporteCCNView;
import com.americacg.cargavirtual.web.mngbeans.reportes.ReporteComisionesView;
import com.americacg.cargavirtual.web.mngbeans.reportes.ReporteComisionesVigentesView;
import com.americacg.cargavirtual.web.mngbeans.reportes.ReporteCuentaCorrienteView;
import com.americacg.cargavirtual.web.mngbeans.reportes.ReporteDepAdelView;
import com.americacg.cargavirtual.web.mngbeans.reportes.ReporteGCSIACGView;
import com.americacg.cargavirtual.web.mngbeans.reportes.ReportePNetACGView;
import com.americacg.cargavirtual.web.mngbeans.reportes.ReportePagoElectronicoLIAPView;
import com.americacg.cargavirtual.web.mngbeans.reportes.ReportePagoElectronicoLIPAView;
import com.americacg.cargavirtual.web.mngbeans.reportes.ReporteRentabilidadView;
import com.americacg.cargavirtual.web.mngbeans.reportes.ReporteRepartosView;
import com.americacg.cargavirtual.web.mngbeans.reportes.ReporteRepartosWUView;
import com.americacg.cargavirtual.web.mngbeans.reportes.ReporteSegurosView;
import com.americacg.cargavirtual.web.mngbeans.reportes.ReporteSubeLotesView;
import com.americacg.cargavirtual.web.mngbeans.reportes.ReporteSubeTrxsView;
import com.americacg.cargavirtual.web.mngbeans.reportes.ReporteSuperPagoOperacionesView;
import com.americacg.cargavirtual.web.mngbeans.reportes.ReporteTrxRedView;
import com.americacg.cargavirtual.web.mngbeans.reportes.ReporteTrxView;
import com.americacg.cargavirtual.web.model.superPago.Cuenta;
import com.americacg.cargavirtual.web.shared.BasePage;

@Named("puntoVentaView")
@Scope(org.springframework.web.context.WebApplicationContext.SCOPE_SESSION)
public class PuntoVentaView extends BasePage implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4237583370507494658L;
	private Hashtable<String, String> arrayAnchos = new Hashtable<String, String>();  
	private Hashtable<String, String> arrayValorNumerico = new Hashtable<String, String>();  
	private Hashtable<String, String> arrayMaxLength = new Hashtable<String, String>();  
	private Hashtable<String, Boolean> arraySolicitarImporte = new Hashtable<String, Boolean>();  
	private Hashtable<String, Boolean> arrayBotonAceptar = new Hashtable<String, Boolean>();  

	private Integer anchoValorIn1;
	private Integer anchoValorIn2;
	private Integer anchoValorIn3;

	private Boolean valorNumericoIn1;
	private Boolean valorNumericoIn2;
	private Boolean valorNumericoIn3;

	private Integer maxLengthValorIn1;
	private Integer maxLengthValorIn2;
	private Integer maxLengthValorIn3;

	private Boolean solicitarImporte;
	private Boolean botonAceptar;

	private String resAdicional1ParaConfirmarVenta = "";
	private String resAdicional2ParaConfirmarVenta = "";

	private String valorString;

	// Productos ONLINE
	private Long idProducto;
	private String idMoneda;
	private Float importe;

	private String valorIn1;
	private String valorIn2;
	private String valorIn3;

	private List<SelectItem> productos;
	private ArrayOfCabeceraProducto cabeceraProdsOnline;
	private Ticket ticket;
	private String logoOn;
	private String leyenda;
	private Integer celular;
	private Long idTipoProducto;
	private String idEstadoTransaccion;
	private List<SelectItem> importesDisponiblesON;
	private String comentariosOn;
	private String codMnemonicoTipoProductoOn;
	private String codMnemonicoProductoOn;

	// Productos OFFLINE
	private Long idProductoOff;
	private String idMonedaOff;
	private Float importeOff;
	private List<SelectItem> productosOff;

	private Boolean mostrarComboImporteEnProdOff;
	private List<SelectItem> valoresProductosOff;

	private List<CabeceraProducto> cabeceraProdsOffline;
	private Ticket ticketOff;
	private String logoOff;
	private String idEstadoTransaccionOff;
	private String comentariosOff;
	private String codMnemonicoTipoProductoOff;
	private String codMnemonicoProductoOff;
	
	// Reimpresion de Ticket
	private Long idTRXreimpRec;
	private Long idTRXreimpPIN;

	// Prestamos
	private Boolean consultaPrestamo = Boolean.FALSE;
	private String idBeneficiarioPrestamo;
	private Long importePrestamo;
	private String caractTelFijoPrestamo;
	private String numeroTelFijoPrestamo;
	private String caractCelularPrestamo;
	private String numeroCelularPrestamo;
	private String mailPrestamo;
	private String idEstadoPrestamo;
	private Integer rbTipoBeneficiario = 0;

	private RespPrestamo ticketPrestamo;

	private Float ecc = null;

	private String resultadoMensaje = "";
	private String tituloTicket = "";
	private Date fechaActualCierreLote;
	private CierreLote cl;
	private Long idLoteParaConsultar;

	private Boolean parametrosComisionesVigentesActualizados = false;
	private Boolean mostrarReporteComisionesVigentesPV = false;

	private int number = 1;

	private Integer indiceMenu = null;
	private boolean muestraMnuIconos = false;
	private String mnuOpcIconos = "";

	private String opcAIncluir = "";
	private String auxiliarAIncluir = "";
	private String tituloPanelProducto = "";

	private String icono_menu_cuentas = "";
	
	/*
	 * SUBE
	 */

	private boolean forzarInicializarSube = true;

	private static final char CHR_INICIO_TRAMA = (char) 2;
	private static final char CHR_SEPARADOR_TRAMA = (char) 4;
	private static final char CHR_FIN_TRAMA = (char) 3;
	private static final char CHR_SEPARADOR_HEADER_DATA = (char) 5;
	/*
	 * SUBE - FIN
	 */

	private String origen = "";
	
	private Integer impresionTickets = null;

	public void inicializarValores() {
		/*
		 * if (!FacesContext.getCurrentInstance().isPostback() &&
		 * !FacesContext.getCurrentInstance().isValidationFailed()) {
		 */
		this.ticket = null;
		this.idProducto = null;
		this.idMoneda = "";
		this.importe = null;
		this.valorIn1 = "";
		this.valorIn2 = "";
		this.valorIn3 = "";
		this.leyenda = "Telefono";
		this.celular = null;
		this.idTipoProducto = null;
		this.idEstadoTransaccion = "";
		this.comentariosOn = "";
		this.codMnemonicoTipoProductoOn = "";
		this.codMnemonicoProductoOn = "";		
		this.idProductoOff = null;
		this.idMonedaOff = "";
		this.importeOff = null;
		this.ticketOff = null;
		this.idEstadoTransaccionOff = "";
		this.comentariosOff = "";
		this.codMnemonicoTipoProductoOff = "";
		this.codMnemonicoProductoOff = "";
		this.idTRXreimpRec = null;
		this.idTRXreimpPIN = null;
		this.idBeneficiarioPrestamo = "";
		this.importePrestamo = null;
		this.caractTelFijoPrestamo = "";
		this.numeroTelFijoPrestamo = "";
		this.caractCelularPrestamo = "";
		this.numeroCelularPrestamo = "";
		this.mailPrestamo = "";
		this.idEstadoPrestamo = "";
		this.rbTipoBeneficiario = null;
		this.ticketPrestamo = null;
		this.ecc = null;
		this.resultadoMensaje = "";
		this.tituloTicket = "";
		this.fechaActualCierreLote = null;
		this.cl = null;
		this.idLoteParaConsultar = null;
	}

	private boolean muestraConsultaLote = false;

	public boolean isMuestraConsultaLote() {
		return muestraConsultaLote;
	}

	public void setMuestraConsultaLote(boolean muestraConsultaLote) {
		this.muestraConsultaLote = muestraConsultaLote;
	}

	public int getNumber() {
		return number;
	}

	public void increment() {
		System.out.println("increment");
		number++;
	}

	public PuntoVentaView() {
		// Constructor
		// Defino el ancho de las cajas de texto de valorIn1, valorIn2 y valorIn3

		arrayAnchos.put("1", "54;120;0"); // Celular
		arrayValorNumerico.put("1", "true;true;false");
		arrayMaxLength.put("1", "4;8;0");
		arraySolicitarImporte.put("1", true);
		arrayBotonAceptar.put("1", true);

		arrayAnchos.put("2", "0;0;221"); // DirectTV
		arrayValorNumerico.put("2", "false;false;true");
		arrayMaxLength.put("2", "0;0;20");
		arraySolicitarImporte.put("2", true);
		arrayBotonAceptar.put("2", true);

		arrayAnchos.put("3", "54;163;0"); // GranDT
		arrayValorNumerico.put("3", "true;true;false");
		arrayMaxLength.put("3", "4;9;0");
		arraySolicitarImporte.put("3", true);
		arrayBotonAceptar.put("3", true);

		arrayAnchos.put("4", "0;0;0"); // Sube
		arrayValorNumerico.put("4", "false;false;false");
		arrayMaxLength.put("4", "0;0;0");
		arraySolicitarImporte.put("4", false);
		arrayBotonAceptar.put("4", false);

		arrayAnchos.put("5", "104;40;70"); // Estacionamiento EP
		arrayValorNumerico.put("5", "false;true;false");
		arrayMaxLength.put("5", "9;3;6");
		arraySolicitarImporte.put("5", false);
		arrayBotonAceptar.put("5", false);

		arrayAnchos.put("6", "220;0;0"); // Multa Elinpar
		arrayValorNumerico.put("6", "true;false;false");
		arrayMaxLength.put("6", "12;0;0");
		arraySolicitarImporte.put("6", false);
		arrayBotonAceptar.put("6", false);

		arrayAnchos.put("7", "220;0;0"); // Rec. Tarjeta Mifare Elinpar
		arrayValorNumerico.put("7", "true;false;false");
		arrayMaxLength.put("7", "12;0;0");
		arraySolicitarImporte.put("7", true);
		arrayBotonAceptar.put("7", false);

		arrayAnchos.put("8", "220;0;0"); // Rec. Saldo Telefono Elinpar
		arrayValorNumerico.put("8", "true;false;false");
		arrayMaxLength.put("8", "12;0;0");
		arraySolicitarImporte.put("8", true);
		arrayBotonAceptar.put("8", false);

		arrayAnchos.put("9", "130;100;0"); // Venta Tarjeta Mifare Elinpar
		arrayValorNumerico.put("9", "true;false;false");
		arrayMaxLength.put("9", "12;9;0");
		arraySolicitarImporte.put("9", false);
		arrayBotonAceptar.put("9", false);

		arrayAnchos.put("10", "0;0;221"); // 1milcosas
		arrayValorNumerico.put("10", "false;false;true");
		arrayMaxLength.put("10", "0;0;12");
		arraySolicitarImporte.put("10", true);
		arrayBotonAceptar.put("10", true);

		arrayAnchos.put("11", "0;0;221"); // Estacionamiento_BO_propio
		arrayValorNumerico.put("11", "false;false;false");
		arrayMaxLength.put("11", "0;0;12");
		arraySolicitarImporte.put("11", true);
		arrayBotonAceptar.put("11", true);

		arrayAnchos.put("14", "0;0;221"); // Edenor Prepago
		arrayValorNumerico.put("14", "false;false;false");
		arrayMaxLength.put("14", "0;0;20");
		arraySolicitarImporte.put("14", true);
		arrayBotonAceptar.put("14", true);

		arrayAnchos.put("15", "0;0;173"); // SUBE C.Dif
		arrayValorNumerico.put("15", "false;false;false");
		arrayMaxLength.put("15", "0;0;20");
		arraySolicitarImporte.put("15", true);
		arrayBotonAceptar.put("15", true);

		arrayAnchos.put("16", "0;0;221"); // SUBE CashIN
		arrayValorNumerico.put("16", "false;false;true");
		arrayMaxLength.put("16", "0;0;20");
		arraySolicitarImporte.put("16", true);
		arrayBotonAceptar.put("16", true);

		arrayAnchos.put("17", "221;221;221"); // SUBE CashOut
		arrayValorNumerico.put("17", "true;true;true");
		arrayMaxLength.put("17", "20;20;20");
		arraySolicitarImporte.put("17", true);
		arrayBotonAceptar.put("17", true);

		arrayAnchos.put("23SEMMUR", "221;0;0"); // Estacionamiento SEMM - SEMMUR
		arrayValorNumerico.put("23SEMMUR", "true;false;false");
		arrayMaxLength.put("23SEMMUR", "10;0;0");
		arraySolicitarImporte.put("23SEMMUR", true);
		arrayBotonAceptar.put("23SEMMUR", true);

		arrayAnchos.put("23SEMMUNR", "100;100;0"); // Estacionamiento SEMM - SEMMUNR
		arrayValorNumerico.put("23SEMMUNR", "true;false;false");
		arrayMaxLength.put("23SEMMUNR", "10;10;0");
		arraySolicitarImporte.put("23SEMMUNR", true);
		arrayBotonAceptar.put("23SEMMUNR", true);
		
	}

	private Boolean habilitarRefreshPVdesdePVPjava;

	public void refrescarSaldo() {
		try {

			// CONSULTA DE SALDO PARA MOSTRAR EN puntoVentaMainPage.xhtm (en barra)
			Float saldo = GestionServiceHelper.getGestionService(CfgTimeout.DEFAULT).obtenerSaldo(
					this.getUsuario().getIdMayorista(), this.getUsuario().getIdCliente(),
					this.getUsuario().getIdDistribuidor());
			this.getUsuario().setSaldoDisponible(saldo);

			// CONSULTA DE LIMITE DE DESCUBIERTO
			Float limDesc = GestionServiceHelper.getGestionService(CfgTimeout.DEFAULT).limDesc(
					this.getUsuario().getIdMayorista(), this.getUsuario().getIdCliente(),
					this.getUsuario().getPermitirLimiteDescubierto());
			this.getUsuario().setValorLimiteDescubierto(limDesc.intValue());

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
					LogACGHelper.escribirLog(null, "Refresco de Saldo. Error: |" + ste.getMessage() + "|");
					FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
							"Refresco de Saldo. Error: |" + ste.getMessage() + "|", null));
				}
			} else {
				LogACGHelper.escribirLog(null, "Refresco de Saldo. Error: |" + ste.getMessage() + "|");
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
						"Refresco de Saldo. Error: |" + ste.getMessage() + "|", null));
			}
			FacesContext.getCurrentInstance().validationFailed();
		} catch (Exception e) {
			LogACGHelper.escribirLog(null, "Refresco de Saldo. Error: |" + e.getMessage() + "|");
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
					"Refresco de Saldo. Error: |" + e.getMessage() + "|", null));
			FacesContext.getCurrentInstance().validationFailed();
		}
	}

	public void refrescarBanner() {
		try {
			Banner banner = GestionServiceHelper.getGestionService(CfgTimeout.DEFAULT).refreshBanner(
					this.getUsuario().getIdMayorista(), this.getUsuario().getTipoCliente(),
					this.getUsuario().getIdTipoTerminal(), this.getUsuario().getIdDistribuidor(),
					this.getUsuario().getIdCliente());

			if (banner != null) {
				this.getUsuario().setBanner(banner.getTextoBanner().getValue());
				this.getUsuario().setColorBanner(banner.getColor().getValue());
			} else {
				// NO SE ENCONTRO EL BANNER
				// System.out.println("No se encontro Banner");
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
					LogACGHelper.escribirLog(null, "Refresco de Banner. Error: |" + ste.getMessage() + "|");
					FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
							"Refresco de Banner. Error: |" + ste.getMessage() + "|", null));
				}
			} else {
				LogACGHelper.escribirLog(null, "Refresco de Banner. Error: |" + ste.getMessage() + "|");
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
						"Refresco de Banner. Error: |" + ste.getMessage() + "|", null));
			}
			FacesContext.getCurrentInstance().validationFailed();
		} catch (Exception e) {
			LogACGHelper.escribirLog(null, "Refresco de Banner. Error: |" + e.getMessage() + "|");
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
					"Refresco de Banner. Error: |" + e.getMessage() + "|", null));
			FacesContext.getCurrentInstance().validationFailed();
		}
	}

	public void cambiarLogoOn() {
		try {
			logoOn = "../../../images/telefono.JPG";
			leyenda = "Telefono";
			celular = 1;
			idTipoProducto = 1L;
			comentariosOn = "";

			importesDisponiblesON = null;

			for (CabeceraProducto cp : cabeceraProdsOnline.getCabeceraProducto()) {
				if (cp.getIdProducto().getValue().equals(idProducto)) {
					logoOn = cp.getLogo().getValue();
					leyenda = cp.getLeyenda().getValue();
					celular = cp.getCelular().getValue();
					idTipoProducto = cp.getIdTipoProducto().getValue();

					if (cp.getComentarios() != null) {
						comentariosOn = cp.getComentarios().getValue().replace("\r\n", "<br/>").replace("\n\r", "<br/>")
								.replace("\r", "<br/>").replace("\n", "<br/>");
					} else {
						comentariosOn = "";
					}

					// Cargo ImportesDisponibles
					if (cp.getImportesDisponiblesTablaProducto() != null
							&& cp.getImportesDisponiblesTablaProducto().getValue().getFloat().size() > 0) {

						importesDisponiblesON = new ArrayList<SelectItem>();

						for (Float f : cp.getImportesDisponiblesTablaProducto().getValue().getFloat()) {
							importesDisponiblesON.add(new SelectItem(f, f.toString()));
						}
					}
					
					if (cp.getCodMnemonicoTipoProducto() != null) {
						this.codMnemonicoTipoProductoOn = cp.getCodMnemonicoTipoProducto().getValue();
					} else {
						this.codMnemonicoTipoProductoOn = "";
					}
					
					if (cp.getCodMnemonicoProducto() != null) {
						this.codMnemonicoProductoOn = cp.getCodMnemonicoProducto().getValue();
					} else {
						this.codMnemonicoProductoOn = "";
					}


				}
			}

			// Cuando cambio de producto, limpio todos los campos con ajax.
			valorIn1 = null;
			valorIn2 = null;
			valorIn3 = null;
			idMoneda = "$";
			importe = null;
			resAdicional1ParaConfirmarVenta = "";
			resAdicional2ParaConfirmarVenta = "";
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
					"Error cambiando logoOn/leyenda/celular: " + e.getMessage(), null));
		}

		PrimeFaces.current().executeScript("PF('blockPanelGral').hide()");
	}

	public void cambiarLogoOff() {
		try {
			logoOff = "../../../images/tarjeta.JPG";
			comentariosOff = "";
			for (CabeceraProducto cp : cabeceraProdsOffline) {
				if (cp.getIdProducto().getValue().equals(idProductoOff)) {
					logoOff = cp.getLogo().getValue();

					if (cp.getComentarios() != null) {
						comentariosOff = cp.getComentarios().getValue().replace("\r\n", "<br/>")
								.replace("\n\r", "<br/>").replace("\r", "<br/>").replace("\n", "<br/>");
					} else {
						comentariosOff = "";
					}
					
					if (cp.getCodMnemonicoTipoProducto() != null) {
						this.codMnemonicoTipoProductoOff = cp.getCodMnemonicoTipoProducto().getValue();
					} else {
						this.codMnemonicoTipoProductoOff = "";
					}
					
					if (cp.getCodMnemonicoProducto() != null) {
						this.codMnemonicoProductoOff = cp.getCodMnemonicoProducto().getValue();
					} else {
						this.codMnemonicoProductoOff = "";
					}
				}
			}
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error cambiando logoOff: " + e.getMessage(), null));
		}

		PrimeFaces.current().executeScript("PF('blockPanelGral').hide()");
	}

	// Reimpresion Ticket

	public void expandirContraerReimpresionTicket() {

		idTRXreimpRec = null;
		idTRXreimpPIN = null;
		ticket = null;
		ticketOff = null;
	}

	// --------------------------------------------------------------------------------------------------
	// Prestamos
	// --------------------------------------------------------------------------------------------------

	public void solicPrestamoConsulta() {

		Boolean limpiarPantalla = false;
		tituloTicket = "PRESTAMO";

		try {

			if (idBeneficiarioPrestamo == null) {
				FacesContext.getCurrentInstance().addMessage(null,
						new FacesMessage(FacesMessage.SEVERITY_ERROR, "Debe cargar un ID para el Beneficiario", null));
				PrimeFaces.current().executeScript("PF('blockPanelGral').hide()");
				FacesContext.getCurrentInstance().validationFailed();
				return;
			}
			if ("".equals(idBeneficiarioPrestamo)) {
				FacesContext.getCurrentInstance().addMessage(null,
						new FacesMessage(FacesMessage.SEVERITY_ERROR, "Debe cargar un ID para el Beneficiario", null));
				PrimeFaces.current().executeScript("PF('blockPanelGral').hide()");
				FacesContext.getCurrentInstance().validationFailed();
				return;
			}

			idEstadoPrestamo = "";
			String usuario = this.getUsuario().getUsername();

			String codOperacion = "";
			if (consultaPrestamo) {
				codOperacion = "02";
			} else {
				codOperacion = "01";
			}

			String tipoBeneficiario = "";
			if (rbTipoBeneficiario == 0) {
				tipoBeneficiario = "NB";
			}
			if (rbTipoBeneficiario == 1) {
				tipoBeneficiario = "DN";
			}
			if (rbTipoBeneficiario == 2) {
				tipoBeneficiario = "LE";
			}
			if (rbTipoBeneficiario == 3) {
				tipoBeneficiario = "LC";
			}

			if ("".equals(tipoBeneficiario)) {
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
						"Debe seleccionar un tipo de Beneficiario", null));
				PrimeFaces.current().executeScript("PF('blockPanelGral').hide()");
				FacesContext.getCurrentInstance().validationFailed();
				return;
			}

			limpiarPantalla = true;

			GregorianCalendar gcFechaHoy = new GregorianCalendar();
			gcFechaHoy.setTime(new Date());
			XMLGregorianCalendar xmlGCFechaHoy = DatatypeFactory.newInstance().newXMLGregorianCalendar(gcFechaHoy);

			this.ticketPrestamo = GestionServiceHelper.getGestionService(CfgTimeout.DEFAULT).solicConsPrestamo(
					this.getUsuario().getIdMayorista(), this.getUsuario().getIdCliente(),
					this.getUsuario().getIdUsuario(), usuario, xmlGCFechaHoy, codOperacion, tipoBeneficiario,
					idBeneficiarioPrestamo, importePrestamo, caractCelularPrestamo, numeroCelularPrestamo,
					caractTelFijoPrestamo, numeroTelFijoPrestamo, mailPrestamo, "W");

			if (ticketPrestamo.getError().getValue().getHayError().getValue()) {
				FacesContext.getCurrentInstance().addMessage(null,
						new FacesMessage(FacesMessage.SEVERITY_ERROR,
								"Error en ejecucion de proceso de prestamo: |"
										+ ticketPrestamo.getError().getValue().getCodigoError().getValue() + " - "
										+ ticketPrestamo.getError().getValue().getMsgError().getValue() + "|",
								null));
				limpiarPantalla = ticketPrestamo.getLimpiarPantalla().getValue();
			} else {

				if ("03".equals(ticketPrestamo.getIdEstado().getValue())) {
					// Prestamo Aprobado

					FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,
							ticketPrestamo.getDescOperacion().getValue(), null));
					FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,
							"ID Beneficiario: " + ticketPrestamo.getIdBeneficiario().getValue(), null));
					FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,
							"Importe: " + ticketPrestamo.getImporte().getValue(), null));
					FacesContext.getCurrentInstance().addMessage(null,
							new FacesMessage(FacesMessage.SEVERITY_INFO,
									"Estado: " + ticketPrestamo.getIdEstado().getValue() + " - "
											+ ticketPrestamo.getDescEstado().getValue(),
									null));
					FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,
							"Numero de operacion NUS: |" + ticketPrestamo.getNUS() + "|", null));
					FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,
							"Numero de operacion PLATAFORMA: |" + ticketPrestamo.getIdTransaccionACG().getValue() + "|",
							null));
					if ((!"".equals(ticketPrestamo.getNombreBeneficiario().getValue()))
							|| (!"".equals(ticketPrestamo.getApellidoBeneficiario().getValue()))) {
						FacesContext.getCurrentInstance().addMessage(null,
								new FacesMessage(FacesMessage.SEVERITY_INFO,
										"Beneficiario: |" + ticketPrestamo.getNombreBeneficiario().getValue() + " "
												+ ticketPrestamo.getApellidoBeneficiario().getValue() + "|",
										null));
					}
					FacesContext.getCurrentInstance().addMessage(null,
							new FacesMessage(FacesMessage.SEVERITY_INFO, ticketPrestamo.getMensaje().getValue(), null));

					this.mostrarTicket("PRESTAMOS");

					PrimeFaces.current().executeScript(
							"ACGSiteScriptsFCNS.cambiaFondoMsg('msgsProductos', '#212121', '#9ed5a0', '#a0d4b7', '"
									+ FacesContext.getCurrentInstance().getExternalContext().getRequestContextPath()
											.replace("/", "\\/")
									+ "\\/javax.faces.resource\\/images\\/24x24\\/operacionOK.png')");
				} else {
					FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
							ticketPrestamo.getDescOperacion().getValue(), null));
					FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
							"ID Beneficiario: " + ticketPrestamo.getIdBeneficiario(), null));
					FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
							"Importe: " + ticketPrestamo.getImporte(), null));
					FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
							"Estado: " + ticketPrestamo.getIdEstado() + " - " + ticketPrestamo.getDescEstado(), null));
					FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
							"Numero de operacion NUS: |" + ticketPrestamo.getNUS() + "|", null));
					FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
							"Numero de operacion PLATAFORMA: |" + ticketPrestamo.getIdTransaccionACG() + "|", null));
					if ((!"".equals(ticketPrestamo.getNombreBeneficiario().getValue()))
							|| (!"".equals(ticketPrestamo.getApellidoBeneficiario().getValue()))) {
						FacesContext.getCurrentInstance().addMessage(null,
								new FacesMessage(FacesMessage.SEVERITY_ERROR,
										"Beneficiario: |" + ticketPrestamo.getNombreBeneficiario().getValue() + " "
												+ ticketPrestamo.getApellidoBeneficiario().getValue() + "|",
										null));
					}
					FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
							ticketPrestamo.getMensaje().getValue(), null));

					// TODO Verificar si se muestra el ticket cuando esta rechazado o pendiente.
					// this.mostrarTicket("PRESTAMOS");
				}
			}

			// Estado igual a "03" --> Aprobada
			// Estado igual a "04" --> Pendiente o en Proceso
			// Estado distinto de "03" y distinto de "04" --> Rechazo

			idEstadoPrestamo = ticketPrestamo.getIdEstado().getValue();
		} catch (WebServiceException ste) {
			if (ste != null && ste.getCause() != null && ste.getCause().getClass() != null) {
				if (ste.getCause().getClass().equals(ConnectException.class)) {
					FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
							"No se pudo establecer la comunicación (GST-TOC).\n Por favor reconsulte la operacion.",
							null));
				} else if (ste.getCause().getClass().equals(SocketTimeoutException.class)) {
					FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
							"No se pudo establecer la comunicación (GST-TRW).\n Por favor reconsulte la operacion.",
							null));
				} else if (ste.getCause().getClass().equals(UnknownHostException.class)) {
					LogACGHelper.escribirLog(null,
							"No se pudo establecer la comunicación (GST-HNC): |" + ste.getMessage() + "|");
					FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
							"No se pudo establecer la comunicación (GST-HNC).\n Por favor intente nuevamente.", null));
				} else {
					LogACGHelper.escribirLog(null,
							"Error ejecutando Solicitud o Consulta de Prestamo. Por favor reconsulte la operacion: |"
									+ ste.getMessage() + "|");
					FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
							"Error ejecutando Solicitud o Consulta de Prestamo. Por favor reconsulte la operacion: |"
									+ ste.getMessage() + "|",
							null));
				}
			} else {
				LogACGHelper.escribirLog(null,
						"Error ejecutando Solicitud o Consulta de Prestamo. Por favor reconsulte la operacion: |"
								+ ste.getMessage() + "|");
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
						"Error ejecutando Solicitud o Consulta de Prestamo. Por favor reconsulte la operacion: |"
								+ ste.getMessage() + "|",
						null));
			}
			FacesContext.getCurrentInstance().validationFailed();
		} catch (Exception e) {
			LogACGHelper.escribirLog(null,
					"Error ejecutando Solicitud o Consulta de Prestamo. Por favor reconsulte la operacion: |"
							+ e.getMessage() + "|");
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR,
							"Error ejecutando Solicitud o Consulta de Prestamo. Por favor reconsulte la operacion: |"
									+ e.getMessage() + "|",
							null));
			FacesContext.getCurrentInstance().validationFailed();
		} finally {

			if (limpiarPantalla) {
				// Reseteo campos
				idBeneficiarioPrestamo = null;
				consultaPrestamo = false;
				importePrestamo = null;
				caractTelFijoPrestamo = null;
				numeroTelFijoPrestamo = null;
				caractCelularPrestamo = null;
				numeroCelularPrestamo = null;
				mailPrestamo = null;
				rbTipoBeneficiario = 0;
			}
		}

		PrimeFaces.current().executeScript("PF('blockPanelGral').hide()");

		return;
	}
	// --------------------------------------------------------------------------------------------------
	// FIN Prestamos
	// --------------------------------------------------------------------------------------------------

	public void realizarVenta() {

		Boolean finOn = true;
		idEstadoTransaccion = "";
		tituloTicket = "VENTA ONLINE";
		this.ticket = null;

		Boolean debug = false;

		try {
			if (!((this.getUsuario().getIdTipoTerminal() == 1) || (this.getUsuario().getPermitirVentasPorWeb()))) {
				finOn = true;
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
						"El tipo de terminal es incorrecto para operar", null));
				FacesContext.getCurrentInstance().validationFailed();
				PrimeFaces.current().executeScript("PF('blockPanelGral').hide()");
				return;
			}

			if (!"P".equals(this.getUsuario().getTipoCliente())) {
				finOn = true;
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
						"El tipo de cliente es incorrecto para operar", null));
				FacesContext.getCurrentInstance().validationFailed();
				PrimeFaces.current().executeScript("PF('blockPanelGral').hide()");
				return;
			}

			if (this.getUsuario().getSoloInformes() != 0) {
				finOn = true;
				FacesContext.getCurrentInstance().addMessage(null,
						new FacesMessage(FacesMessage.SEVERITY_ERROR, "El usuario solo puede generar informes", null));
				FacesContext.getCurrentInstance().validationFailed();
				PrimeFaces.current().executeScript("PF('blockPanelGral').hide()");
				return;
			}

			if ((idProducto == null) || "".equals(idProducto)) {
				finOn = false;
				FacesContext.getCurrentInstance().addMessage(null,
						new FacesMessage(FacesMessage.SEVERITY_ERROR, "El producto es requerido", null));
				FacesContext.getCurrentInstance().validationFailed();
				PrimeFaces.current().executeScript("PF('blockPanelGral').hide()");
				return;
			}

			String monedaAdic = "";
			// MONEDA +
			// ID_CLIENTE_EXTERNO +
			// IDENTIFTERMINAL +
			// SALDO_DE_SUBE_ANTES_DE_REALIZAR_UNA_VENTA (ESTE PARAMETRO NO SE USA MAS, PERO
			// NO LO PUEDO SACAR PORQUE SE DESPLAZARIAN TODOS LOS CAMPOS) +
			// TELEFONO_COMPLETO_O_TARJETA +
			// IDENTIFICADOR DEL PRODUCTO (PATENTE DE UN AUTO, NRO DE ACTA PARA MULTA,ETC...
			// ETC +
			// DATOS ADICIONALES DEL PRODUCTO

			if (idTipoProducto == 1 || idTipoProducto == 3) {
				// Se esta realizando una transaccion de telefonia celular (RECARGA O VENTA DE
				// PIN)

				if ((valorIn1 == null) || ("".equals(valorIn1))) {
					finOn = false;
					FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
							"La caracteristica del telefono es requerida", null));
					FacesContext.getCurrentInstance().validationFailed();
					PrimeFaces.current().executeScript("PF('blockPanelGral').hide()");
					return;
				}
				if ((valorIn2 == null) || ("".equals(valorIn2))) {
					finOn = false;
					FacesContext.getCurrentInstance().addMessage(null,
							new FacesMessage(FacesMessage.SEVERITY_ERROR, "El numero de telefono es requerido", null));
					FacesContext.getCurrentInstance().validationFailed();
					PrimeFaces.current().executeScript("PF('blockPanelGral').hide()");
					return;
				}

				try {
					Long.parseLong(valorIn1);
				} catch (Exception e) {
					finOn = false;
					FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
							"La caracteristica de telefono debe ser numerica", null));
					FacesContext.getCurrentInstance().validationFailed();
					PrimeFaces.current().executeScript("PF('blockPanelGral').hide()");
					return;
				}

				try {
					Long.parseLong(valorIn2);
				} catch (Exception e) {
					finOn = false;
					FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
							"El numero de telefono debe ser numerico", null));
					FacesContext.getCurrentInstance().validationFailed();
					PrimeFaces.current().executeScript("PF('blockPanelGral').hide()");
					return;
				}

				monedaAdic = "####" + valorIn1 + valorIn2 + "##";

			} else if (idTipoProducto == 2 || idTipoProducto == 10) {
				// Se esta realizando la carga de un producto por identificador (por ejemplo
				// 1Milcosas)

				// ANALIZAR POR IDTIPOPRODUCTO

				if ((valorIn3 == null) || ("".equals(valorIn3))) {
					finOn = false;
					FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
							"El identificador para la carga del producto es requerido", null));
					FacesContext.getCurrentInstance().validationFailed();
					PrimeFaces.current().executeScript("PF('blockPanelGral').hide()");
					return;
				} else {

					valorIn1 = "0";
					valorIn2 = "0";

					if (debug) {
						System.out.println("valorIn1: |" + valorIn1 + "|");
					}
					if (debug) {
						System.out.println("valorIn2: |" + valorIn2 + "|");
					}
					if (debug) {
						System.out.println("valorIn3 (telefonoCompletoOTarjeta): |" + valorIn3 + "|");
					}

					monedaAdic = "####" + valorIn3 + "##";
				}

			} else if (idTipoProducto == 5) {
				// Estacionamiento Elinpar

				if (debug) {
					System.out.println("resAdicional1ParaConfirmarVenta: " + resAdicional1ParaConfirmarVenta);
				}
				if (debug) {
					System.out.println("resAdicional2ParaConfirmarVenta: " + resAdicional2ParaConfirmarVenta);
				}

				String ra2[] = resAdicional2ParaConfirmarVenta.split("@", -1);

				valorIn1 = "0";
				valorIn2 = "0";
				monedaAdic = "#####" + resAdicional1ParaConfirmarVenta + "#" + resAdicional2ParaConfirmarVenta;
				importe = Float.parseFloat(ra2[5]);

			} else if (idTipoProducto == 6) {
				// Multa Elinpar

				if (debug) {
					System.out.println("resAdicional1ParaConfirmarVenta: " + resAdicional1ParaConfirmarVenta);
				}
				if (debug) {
					System.out.println("resAdicional2ParaConfirmarVenta: " + resAdicional2ParaConfirmarVenta);
				}

				String ra2[] = resAdicional2ParaConfirmarVenta.split("@", -1);

				valorIn1 = "0";
				valorIn2 = "0";
				monedaAdic = "#####" + resAdicional1ParaConfirmarVenta + "#" + resAdicional2ParaConfirmarVenta;
				importe = Float.parseFloat(ra2[6]);

			} else if (idTipoProducto == 7) {
				// Rec. Tarjeta Mifare Elinpar

				if (debug) {
					System.out.println("resAdicional1ParaConfirmarVenta: " + resAdicional1ParaConfirmarVenta);
				}
				if (debug) {
					System.out.println("resAdicional2ParaConfirmarVenta: " + resAdicional2ParaConfirmarVenta);
				}

				String ra2[] = resAdicional2ParaConfirmarVenta.split("@", -1);

				valorIn1 = "0";
				valorIn2 = "0";

				monedaAdic = "#####" + resAdicional1ParaConfirmarVenta + "#" + resAdicional2ParaConfirmarVenta;
				importe = Float.parseFloat(ra2[1]);

			} else if (idTipoProducto == 8) {
				// Rec. Saldo Telefono Elinpar

				if (debug) {
					System.out.println("resAdicional1ParaConfirmarVenta: " + resAdicional1ParaConfirmarVenta);
				}
				if (debug) {
					System.out.println("resAdicional2ParaConfirmarVenta: " + resAdicional2ParaConfirmarVenta);
				}

				String ra2[] = resAdicional2ParaConfirmarVenta.split("@", -1);

				valorIn1 = "0";
				valorIn2 = "0";

				monedaAdic = "#####" + resAdicional1ParaConfirmarVenta + "#" + resAdicional2ParaConfirmarVenta;
				importe = Float.parseFloat(ra2[1]);

			} else if (idTipoProducto == 9) {
				// Venta Tarjeta Mifare Elinpar

				if (debug) {
					System.out.println("resAdicional1ParaConfirmarVenta: " + resAdicional1ParaConfirmarVenta);
				}
				if (debug) {
					System.out.println("resAdicional2ParaConfirmarVenta: " + resAdicional2ParaConfirmarVenta);
				}

				valorIn1 = "0";
				valorIn2 = "0";

				String ra2[] = resAdicional2ParaConfirmarVenta.split("@", -1);

				monedaAdic = "#####" + resAdicional1ParaConfirmarVenta + "#" + resAdicional2ParaConfirmarVenta;

				importe = Float.parseFloat(ra2[2]);

			} else if (idTipoProducto == 11) {
				// Estacionamiento_BO_propio

				if ((valorIn3 == null) || ("".equals(valorIn3))) {
					finOn = false;
					FacesContext.getCurrentInstance().addMessage(null,
							new FacesMessage(FacesMessage.SEVERITY_ERROR, "La Patente es requerida", null));
					FacesContext.getCurrentInstance().validationFailed();
					PrimeFaces.current().executeScript("PF('blockPanelGral').hide()");
					return;
				} else {

					valorIn1 = "0";
					valorIn2 = "0";

					if (debug) {
						System.out.println("Patente: |" + valorIn3 + "|");
					}

					monedaAdic = "#####" + valorIn3 + "#";
				}
			} else if (idTipoProducto == 14) {
				// 14 - Edenor Prepago

				if ((valorIn3 == null) || ("".equals(valorIn3))) {
					finOn = false;
					FacesContext.getCurrentInstance().addMessage(null,
							new FacesMessage(FacesMessage.SEVERITY_ERROR, "El Identificador es requerido", null));
					FacesContext.getCurrentInstance().validationFailed();
					PrimeFaces.current().executeScript("PF('blockPanelGral').hide()");
					return;
				} else {

					valorIn1 = "0";
					valorIn2 = "0";

					if (debug) {
						System.out.println("Identificador: |" + valorIn3 + "|");
					}

					monedaAdic = "####" + valorIn3 + "#########";

				}

			} else if (idTipoProducto == 15) {
				// 15 - SUBE C.Dif

				if ((valorIn3 == null) || ("".equals(valorIn3))) {
					finOn = false;
					FacesContext.getCurrentInstance().addMessage(null,
							new FacesMessage(FacesMessage.SEVERITY_ERROR, "El Identificador es requerido", null));
					FacesContext.getCurrentInstance().validationFailed();
					PrimeFaces.current().executeScript("PF('blockPanelGral').hide()");
					return;
				} else {

					valorIn1 = "0";
					valorIn2 = "0";

					if (debug) {
						System.out.println("Identificador: |" + valorIn3 + "|");
					}

					monedaAdic = "####" + "606126" + valorIn3 + "#########";

				}

			} else if (idTipoProducto == 16) {
				// SUBE CashIN

				if ((valorIn3 == null) || ("".equals(valorIn3))) {
					finOn = false;
					FacesContext.getCurrentInstance().addMessage(null,
							new FacesMessage(FacesMessage.SEVERITY_ERROR, "El Identificador es requerido", null));
					FacesContext.getCurrentInstance().validationFailed();
					PrimeFaces.current().executeScript("PF('blockPanelGral').hide()");
					return;
				} else {

					valorIn1 = "0";
					valorIn2 = "0";

					if (debug) {
						System.out.println("Identificador: |" + valorIn3 + "|");
					}

					monedaAdic = "####" + valorIn3 + "#########";

				}

			} else if (idTipoProducto == 17) {
				// SUBE CashOut

				if ((valorIn1 == null) || ("".equals(valorIn1)) || (valorIn2 == null) || ("".equals(valorIn2))
						|| (valorIn3 == null) || ("".equals(valorIn3))) {
					finOn = false;
					FacesContext.getCurrentInstance().addMessage(null,
							new FacesMessage(FacesMessage.SEVERITY_ERROR, "Todos los campos son requeridos", null));
					FacesContext.getCurrentInstance().validationFailed();
					PrimeFaces.current().executeScript("PF('blockPanelGral').hide()");
					return;
				} else {

					if (debug) {
						System.out.println("Identificador: |" + valorIn1 + "@" + valorIn2 + "@" + valorIn3 + "|");
					}

					monedaAdic = "####" + valorIn1 + "@" + valorIn2 + "@" + valorIn3 + "#########";

					valorIn1 = "0";
					valorIn2 = "0";

				}
			} else if (idTipoProducto == 23) {
				// Estacionamiento SEMM

				if("SEMMUR".equals(this.getCodMnemonicoProductoOn().toUpperCase()) ) {
					if ((valorIn1 == null) || ("".equals(valorIn1))) {
						finOn = false;
						FacesContext.getCurrentInstance().addMessage(null,
								new FacesMessage(FacesMessage.SEVERITY_ERROR, "El Celular es requeridos", null));
						FacesContext.getCurrentInstance().validationFailed();
						PrimeFaces.current().executeScript("PF('blockPanelGral').hide()");
						return;
					} else {

						if (debug) {
							System.out.println("Identificador: |" + valorIn1 + "@" + valorIn2 + "@" + valorIn3 + "|");
						}

						monedaAdic = "####" + valorIn1 + "#########";

						valorIn1 = "0";
						valorIn2 = "0";

					}
					
				}else if("SEMMUNR".equals(this.getCodMnemonicoProductoOn().toUpperCase()) ) {
					if ((valorIn1 == null) || ("".equals(valorIn1)) || (valorIn2 == null) || ("".equals(valorIn2))) {
						finOn = false;
						FacesContext.getCurrentInstance().addMessage(null,
								new FacesMessage(FacesMessage.SEVERITY_ERROR, "Celular y Patente son requeridos", null));
						FacesContext.getCurrentInstance().validationFailed();
						PrimeFaces.current().executeScript("PF('blockPanelGral').hide()");
						return;
					} else {

						if (debug) {
							System.out.println("Identificador: |" + valorIn1 + "@" + valorIn2 + "@" + valorIn3 + "|");
						}

						monedaAdic = "####" + valorIn1 + "*" + valorIn2 + "#########";

						valorIn1 = "0";
						valorIn2 = "0";

					}
					
				}
				

			} else {
				finOn = true;
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
						"El Tipo de Producto del Producto seleccionado, no esta soportado", null));
				FacesContext.getCurrentInstance().validationFailed();
				PrimeFaces.current().executeScript("PF('blockPanelGral').hide()");
				return;
			}

			if (solicitarImporte) {
				if ((importe == null) || "".equals(importe)) {
					finOn = false;
					FacesContext.getCurrentInstance().addMessage(null,
							new FacesMessage(FacesMessage.SEVERITY_ERROR, "El importe es requerido", null));
					FacesContext.getCurrentInstance().validationFailed();
					PrimeFaces.current().executeScript("PF('blockPanelGral').hide()");
					return;
				}

				if ((idMoneda == null) || "".equals(idMoneda)) {
					finOn = false;
					FacesContext.getCurrentInstance().addMessage(null,
							new FacesMessage(FacesMessage.SEVERITY_ERROR, "La moneda es requerido", null));
					FacesContext.getCurrentInstance().validationFailed();
					PrimeFaces.current().executeScript("PF('blockPanelGral').hide()");
					return;
				}
			}

			String usuario = this.getUsuario().getUsername();
			String clave = this.getUsuario().getPassword();

			String idTransaccionCliente = crearIdTransaccionCliente();

			Integer ct;
			Integer nt;
			try {
				ct = Integer.parseInt(valorIn1);
				nt = Integer.parseInt(valorIn2);
			} catch (Exception e) {
				finOn = true;
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
						"Caracteristica o Numero de Telefono deben ser numericos", null));
				FacesContext.getCurrentInstance().validationFailed();
				PrimeFaces.current().executeScript("PF('blockPanelGral').hide()");
				return;
			}

			/*
			 * moneda + "####".concat(caracteristicaTelefono).concat(numeroTelefono)
			 * .concat("#").concat(prodIdentif).concat("#").concat(prodDatosAdic)
			 */

			GregorianCalendar gcFechaHoy = new GregorianCalendar();
			gcFechaHoy.setTime(new Date());
			XMLGregorianCalendar xmlGCFechaHoy = DatatypeFactory.newInstance().newXMLGregorianCalendar(gcFechaHoy);

			this.ticket = CoreServiceHelper.getCoreService(CfgTimeout.VENTA).realizarVenta(
					this.getUsuario().getIdMayorista(), this.getUsuario().getIdCliente(), usuario, clave, xmlGCFechaHoy,
					idTransaccionCliente, idProducto, importe, ct, nt, null, "ON", idMoneda.concat(monedaAdic));

			// PARA CARGA DIFERIDA DE SUBE CAMBIO LA LEYENDA "idTransaccionProveedor:" por
			// "idTransaccionSUBE:"
			if (idTipoProducto == 15) {
				// 15 - SUBE C.Dif)
				if (this.ticket.getDescEstadoTransaccion() != null
						&& this.ticket.getDescEstadoTransaccion().getValue() != null) {
					if (this.ticket.getDescEstadoTransaccion() != null) {
						this.ticket.getDescEstadoTransaccion().setValue(this.ticket.getDescEstadoTransaccion()
								.getValue().replaceAll("OK - Solicitud procesada correctamente.", ""));

						this.ticket.getDescEstadoTransaccion().setValue(this.ticket.getDescEstadoTransaccion()
								.getValue().replaceAll("OK - Limite de COM alcanzado pero continua operando.", ""));

						this.ticket.getDescEstadoTransaccion().setValue(this.ticket.getDescEstadoTransaccion()
								.getValue().replaceAll("idTransaccionProveedor:", "idTransaccionSUBE:"));

						this.ticket.getDescEstadoTransaccion()
								.setValue(this.ticket.getDescEstadoTransaccion().getValue().replaceAll(
										"Carga en Proceso. Por favor consulte el estado de esta carga con su proveedor",
										"Carga en Proceso. Por favor consulte el estado de esta carga"));
					}
				}
			}

			// Reemplazo \\r por \n para mostrar en el ticket
			if ("M0000".equals(ticket.getIdEstadoTransaccion().getValue())
					&& (ticket.getDescEstadoTransaccion() != null)) {
				// System.out.println("AAA: " + this.ticket.getDescEstadoTransaccion());
				this.ticket.getDescEstadoTransaccion()
						.setValue(ticket.getDescEstadoTransaccion().getValue().replaceAll("\\\\r", "\r"));
				// this.ticket.setDescEstadoTransaccion(ticket.getDescEstadoTransaccion().replaceAll("\\\\r",
				// ""));
				// this.ticket.setDescEstadoTransaccion(ticket.getDescEstadoTransaccion().replaceAll("\\",
				// ""));
				this.ticket.getDescEstadoTransaccion()
						.setValue(ticket.getDescEstadoTransaccion().getValue().replaceAll("\\.", "\n\r"));
				// System.out.println("BBB: " + this.ticket.getDescEstadoTransaccion());
			}

			// Reemplazo los 2 puntos y el punto por enter en DescEstadoTransaccion
			// Para que las lineas se hagan mas cortas y el ticket salga mas angosto
			if (this.ticket.getDescEstadoTransaccion() != null
					&& this.ticket.getDescEstadoTransaccion().getValue() != null) {
				ticket.getDescEstadoTransaccion().setValue(
						ticket.getDescEstadoTransaccion().getValue().replace(":", ":\n\r").replace(".", ".\n\r"));
			}

			if (ticket.getIdEstadoTransaccion() != null && ticket.getIdEstadoTransaccion().getValue() != null) {
				idEstadoTransaccion = ticket.getIdEstadoTransaccion().getValue();
			}

			if ("M0000".equals(ticket.getIdEstadoTransaccion().getValue())) {
				FacesContext.getCurrentInstance().addMessage(null,
						new FacesMessage(FacesMessage.SEVERITY_INFO, "La venta se realizo con exito.", null));
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,
						"Id Transaccion: " + ticket.getIdTransaccion().getValue(), null));

				if (this.impresionTickets.compareTo(2) != 0) {
					this.mostrarTicket("ONLINE");
				}

				PrimeFaces.current().executeScript(
						"ACGSiteScriptsFCNS.cambiaFondoMsg('msgsProductos', '#212121', '#9ed5a0', '#a0d4b7', '"
								+ FacesContext.getCurrentInstance().getExternalContext().getRequestContextPath()
										.replace("/", "\\/")
								+ "\\/javax.faces.resource\\/images\\/24x24\\/operacionOK.png')");
			} else {
				if ("M0001".equals(ticket.getIdEstadoTransaccion().getValue())) {
					if (idTipoProducto != 15) {
						// 15 - SUBE C.Dif)
						FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
								"VENTA EN PROCESO. Por favor consulte el estado antes de devolver el dinero.", null));
						PrimeFaces.current().executeScript(
								"ACGSiteScriptsFCNS.cambiaFondoMsg('msgsProductos', '#212121', '#E4B34E', '#a0d4b7', '"
										+ FacesContext.getCurrentInstance().getExternalContext().getRequestContextPath()
												.replace("/", "\\/")
										+ "\\/javax.faces.resource\\/images\\/24x24\\/enProceso.png')");
					}

				} else {
					FacesContext.getCurrentInstance().addMessage(null,
							new FacesMessage(FacesMessage.SEVERITY_ERROR, "Venta Rechazada!", null));
				}
				if (ticket != null) {
					if (ticket.getIdTransaccion() != null) {
						if (ticket.getIdTransaccion().getValue() != null) {
							FacesContext.getCurrentInstance().addMessage(null,
									new FacesMessage(FacesMessage.SEVERITY_ERROR,
											"Id Transaccion: " + ticket.getIdTransaccion().getValue(), null));
						}
					}
					if (ticket.getError().getValue().getMsgError().getValue() != null) {
						FacesContext.getCurrentInstance()
								.addMessage(null,
										new FacesMessage(FacesMessage.SEVERITY_ERROR,
												"Error: " + ticket.getError().getValue().getCodigoError().getValue()
														+ " - " + ticket.getError().getValue().getMsgError().getValue(),
												null));
					}
					if (ticket.getDescEstadoTransaccion().getValue() != null) {
						if ("M0001".equals(ticket.getDescEstadoTransaccion().getValue())) {
							FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
									"Estado Transaccion: " + ticket.getIdEstadoTransaccion().getValue() + " - "
											+ ticket.getDescEstadoTransaccion().getValue(), null));
							PrimeFaces.current().executeScript(
									"ACGSiteScriptsFCNS.cambiaFondoMsg('msgsProductos', '#212121', '#E4B34E', '#a0d4b7', '"
											+ FacesContext.getCurrentInstance().getExternalContext().getRequestContextPath()
													.replace("/", "\\/")
											+ "\\/javax.faces.resource\\/images\\/24x24\\/enProceso.png')");
						}else {
							FacesContext.getCurrentInstance().addMessage(null,
									new FacesMessage(FacesMessage.SEVERITY_ERROR,
											"Estado Transaccion: " + ticket.getIdEstadoTransaccion().getValue() + " - "
													+ ticket.getDescEstadoTransaccion().getValue(),
											null));
						}
					}
				}
			}

			FacesContext.getCurrentInstance().validationFailed();
			PrimeFaces.current().executeScript("PF('blockPanelGral').hide();refreshSaldo();");

		} catch (WebServiceException ste) {
			if (ste != null && ste.getCause() != null && ste.getCause().getClass() != null) {
				if (ste.getCause().getClass().equals(ConnectException.class)) {
					FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
							"VENTA EN PROCESO (CRE-TOC). Por favor consulte el estado antes de devolver el dinero.",
							null));
					PrimeFaces.current().executeScript(
							"ACGSiteScriptsFCNS.cambiaFondoMsg('msgsProductos', '#212121', '#E4B34E', '#a0d4b7', '"
									+ FacesContext.getCurrentInstance().getExternalContext().getRequestContextPath()
											.replace("/", "\\/")
									+ "\\/javax.faces.resource\\/images\\/24x24\\/enProceso.png')");
				} else if (ste.getCause().getClass().equals(SocketTimeoutException.class)) {
					FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
							"VENTA EN PROCESO (CRE-TORW). Por favor consulte el estado antes de devolver el dinero.",
							null));
					PrimeFaces.current().executeScript(
							"ACGSiteScriptsFCNS.cambiaFondoMsg('msgsProductos', '#212121', '#E4B34E', '#a0d4b7', '"
									+ FacesContext.getCurrentInstance().getExternalContext().getRequestContextPath()
											.replace("/", "\\/")
									+ "\\/javax.faces.resource\\/images\\/24x24\\/enProceso.png')");
				} else if (ste.getCause().getClass().equals(UnknownHostException.class)) {
					LogACGHelper.escribirLog(null,
							"No se pudo establecer la comunicación (CRE-HNC): |" + ste.getMessage() + "|");
					FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
							"VENTA EN PROCESO (CRE-HNC). Por favor consulte el estado antes de devolver el dinero.",
							null));
					PrimeFaces.current().executeScript(
							"ACGSiteScriptsFCNS.cambiaFondoMsg('msgsProductos', '#212121', '#E4B34E', '#a0d4b7', '"
									+ FacesContext.getCurrentInstance().getExternalContext().getRequestContextPath()
											.replace("/", "\\/")
									+ "\\/javax.faces.resource\\/images\\/24x24\\/enProceso.png')");
				} else {
					FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
							"Error realizando venta contra el CORE: " + ste.getMessage(), null));
				}
			} else {
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
						"Error realizando venta contra el CORE: " + ste.getMessage(), null));
			}
			
			FacesContext.getCurrentInstance().validationFailed();
			PrimeFaces.current().executeScript("PF('blockPanelGral').hide()");
			
			return;
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error realizando venta: " + e.getMessage(), null));
			FacesContext.getCurrentInstance().validationFailed();
			PrimeFaces.current().executeScript("PF('blockPanelGral').hide()");
			return;
		} finally {
			if (finOn) {
				// Reseteo campos
				idProducto = null;
				idMoneda = "$";
				importe = null;
				importesDisponiblesON = null;
				valorIn1 = null;
				valorIn2 = null;
				valorIn3 = null;
				logoOn = "../../../images/telefono.JPG";
				leyenda = "Telefono";
				celular = 1;
				idTipoProducto = 1L;
				resAdicional1ParaConfirmarVenta = "";
				resAdicional2ParaConfirmarVenta = "";
				// idEstadoTransaccion = "";
				comentariosOn = "";
				this.codMnemonicoTipoProductoOn = "";
				this.codMnemonicoProductoOn = "";				

			}
		}

	}

	public void cerrarLote() {

		tituloTicket = "CIERRE DE LOTE";
		resultadoMensaje = "";
		fechaActualCierreLote = new Date();

		try {

			String clave = this.getUsuario().getPassword();
			cl = GestionServiceHelper.getGestionService(CfgTimeout.DEFAULT).cerrarLote(
					this.getUsuario().getIdMayorista(), this.getUsuario().getIdCliente(),
					this.getUsuario().getIdUsuario(), clave);

			if (cl == null) {
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
						"El resultado del cierre de lote fue null.", null));
				PrimeFaces.current().executeScript("PF('blockPanelGral').hide()");
				FacesContext.getCurrentInstance().validationFailed();
				return;
			} else if (cl.getError().getValue().getHayError().getValue()) {
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
						"Error: " + cl.getError().getValue().getMsgError().getValue(), null));
				PrimeFaces.current().executeScript("PF('blockPanelGral').hide()");
				FacesContext.getCurrentInstance().validationFailed();
				return;
			} else {
				Map<String, Object> options = new HashMap<String, Object>();
				options.put("modal", true);
				options.put("draggable", true);
				options.put("resizable", false);
				options.put("closable", true);
				options.put("contentWidth", 480);
				options.put("position", "center center");
				options.put("includeViewParams", true);

				List<String> paramList = new ArrayList<String>();
				// paramList.add("ENTIDAD");
				Map<String, List<String>> paramMap = new HashMap<String, List<String>>();
				// paramMap.put("ORIGEN", paramList);

				// --- Abre el dialogo para buscar articulos
				PrimeFaces.current().dialog().openDynamic("/secure/shared/tickets/ticketCierreLote", options, paramMap);
				PrimeFaces.current().executeScript("PF('blockPanelGral').hide()");
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
					LogACGHelper.escribirLog(null, "Cierre de lote. Error: |" + ste.getMessage() + "|");
					FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
							"Cierre de lote. Error: |" + ste.getMessage() + "|", null));
				}
			} else {
				LogACGHelper.escribirLog(null, "Cierre de lote. Error: |" + ste.getMessage() + "|");
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
						"Cierre de lote. Error: |" + ste.getMessage() + "|", null));
			}
			FacesContext.getCurrentInstance().validationFailed();
		} catch (Exception e) {
			LogACGHelper.escribirLog(null, "Cierre de lote. Error: |" + e.getMessage() + "|");
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
					"Cierre de lote. Error: |" + e.getMessage() + "|", null));
			FacesContext.getCurrentInstance().validationFailed();
		}
		PrimeFaces.current().executeScript("PF('blockPanelGral').hide()");
	}

	public void consultarLote() {
		this.muestraConsultaLote = true;

		PrimeFaces.current().ajax().update("consultaLoteDialog");
		PrimeFaces.current().executeScript("PF('consultaLoteDialogWV').show()");
	}

	public void cancelarConsultarLote() {
		this.muestraConsultaLote = false;
		idLoteParaConsultar = null;
		PrimeFaces.current().executeScript("PF('consultaLoteDialogWV').hide();");
	}

	public void procesarConsultaLote() {

		tituloTicket = "CONSULTA DE LOTE";
		resultadoMensaje = "No procesada";
		fechaActualCierreLote = new Date();
		Boolean buscarUltimoLoteGenerado = false;
		long idLcons = 0L;

		try {
			try {
				idLcons = idLoteParaConsultar;
			} catch (Exception e) {
				// resultadoMensaje = "El numero de lote a consultar debe ser numerico";
				idLoteParaConsultar = null;
				FacesContext.getCurrentInstance().addMessage("nroLote", new FacesMessage(FacesMessage.SEVERITY_INFO,
						"El numero de lote a consultar debe ser numerico", null));

				FacesContext.getCurrentInstance().validationFailed();
				PrimeFaces.current().executeScript("PF('blockPanelGral').hide()");
				return;
			}

			idLoteParaConsultar = null;

			if (idLcons < 0) {
				// resultadoMensaje = "El lote a buscar no puede ser menor a cero";

				FacesContext.getCurrentInstance().addMessage("nroLote", new FacesMessage(FacesMessage.SEVERITY_INFO,
						"El lote a buscar no puede ser menor a cero", null));

				FacesContext.getCurrentInstance().validationFailed();
				PrimeFaces.current().executeScript("PF('blockPanelGral').hide()");
				return;
			}

			if (idLcons == 0) {
				buscarUltimoLoteGenerado = true;
			}

			cl = GestionServiceHelper.getGestionService(CfgTimeout.DEFAULT).buscarLoteGenerado(
					this.getUsuario().getIdMayorista(), this.getUsuario().getIdCliente(), idLcons,
					buscarUltimoLoteGenerado);

			if (cl == null) {
				FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO,
						"El resultado de la consultar de lote fue null.", null);

				PrimeFaces.current().dialog().showMessageDynamic(msg);
				FacesContext.getCurrentInstance().validationFailed();
				// resultadoMensaje = "El resultado de la consultar de lote fue null";
			} else if (cl.getError().getValue().getHayError().getValue()) {
				FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR,
						"Error: " + cl.getError().getValue().getMsgError().getValue(), null);

				PrimeFaces.current().dialog().showMessageDynamic(msg);
				FacesContext.getCurrentInstance().validationFailed();
				// resultadoMensaje = "Error: " +
				// cl.getError().getValue().getMsgError().getValue();
			} else {
				// Mostrar Ticket OK
				PrimeFaces.current().executeScript("PF('consultaLoteDialogWV').hide()");

				// var winp =
				// window.open('ticketCierreLote.html','ticketCierreLote','height=500,width=480,left=100,top=100,resizable=yes,scrollbars=yes,toolbar=no,status=no');

				Map<String, Object> options = new HashMap<String, Object>();
				options.put("modal", true);
				options.put("draggable", true);
				options.put("resizable", false);
				options.put("closable", true);
				options.put("contentWidth", 480);
				options.put("position", "center center");
				options.put("includeViewParams", true);

				List<String> paramList = new ArrayList<String>();
				// paramList.add("ENTIDAD");
				Map<String, List<String>> paramMap = new HashMap<String, List<String>>();
				// paramMap.put("ORIGEN", paramList);

				// --- Abre el dialogo para buscar articulos
				PrimeFaces.current().dialog().openDynamic("/secure/shared/tickets/ticketCierreLote", options, paramMap);
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
					LogACGHelper.escribirLog(null, "Consulta de lote. Error: |" + ste.getMessage() + "|");
					FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
							"Cierre de lote. Error: |" + ste.getMessage() + "|", null));
				}
			} else {
				LogACGHelper.escribirLog(null, "Consulta de lote. Error: |" + ste.getMessage() + "|");
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
						"Cierre de lote. Error: |" + ste.getMessage() + "|", null));
			}
			FacesContext.getCurrentInstance().validationFailed();
		} catch (Exception e) {
			LogACGHelper.escribirLog(null, "Consulta de lote. Error: |" + e.getMessage() + "|");
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
					"Consulta de lote. Error: |" + e.getMessage() + "|", null));
			FacesContext.getCurrentInstance().validationFailed();
		}
		PrimeFaces.current().executeScript("PF('blockPanelGral').hide()");
	}

	public void mostrarTicket(String tipoTicket) {
		String nombreTicketCompleto = "";
		String nombreTicket = this.getUsuario().getTicket();

		if (!"PRESTAMOS".equals(tipoTicket) && (nombreTicket == null || nombreTicket.isEmpty())) {
			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Ticket no configurado para el usuario.",
					null);

			PrimeFaces.current().dialog().showMessageDynamic(msg);
			PrimeFaces.current().executeScript("PF('blockPanelGral').hide()");
			FacesContext.getCurrentInstance().validationFailed();
			return;
		}

		Map<String, Object> options = new HashMap<String, Object>();
		options.put("modal", true);
		options.put("draggable", true);
		options.put("resizable", false);
		options.put("closable", true);
		options.put("contentWidth", this.getUsuario().getAnchoTicket() + 45);
		options.put("position", "center center");
		options.put("includeViewParams", true);
		options.put("fitViewport", true);

		List<String> paramList = new ArrayList<String>();
		// paramList.add("ENTIDAD");
		Map<String, List<String>> paramMap = new HashMap<String, List<String>>();
		// paramMap.put("ORIGEN", paramList);

		if ("ONLINE".equals(tipoTicket))
			nombreTicketCompleto = "/secure/shared/tickets/" + nombreTicket;
		else if ("OFFLINE".equals(tipoTicket)) {
			nombreTicketCompleto = "/secure/shared/tickets/" + nombreTicket + "Off";
		} else if ("PRESTAMOS".equals(tipoTicket)) {
			nombreTicketCompleto = "/secure/shared/tickets/ticketPrestamo";
		} else {
			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Tipo de Ticket invalido.", null);

			PrimeFaces.current().dialog().showMessageDynamic(msg);
			PrimeFaces.current().executeScript("PF('blockPanelGral').hide()");
			FacesContext.getCurrentInstance().validationFailed();
			return;
		}

		// --- Abre el dialogo para buscar articulos
		PrimeFaces.current().dialog().openDynamic(nombreTicketCompleto, options, paramMap);
		PrimeFaces.current().executeScript("PF('blockPanelGral').hide()");
	}

	public void consultaPreviaVenta() {

		resAdicional1ParaConfirmarVenta = "";
		resAdicional2ParaConfirmarVenta = "";
		valorString = "E" + "-|-" + "Consulta no Realizada";

		try {
			ConfigGral cg = GestionServiceHelper.getGestionService(CfgTimeout.DEFAULT)
					.mostrarConfigGral(this.getUsuario().getIdMayorista());

			if (cg == null) {
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
						"Error leyendo configuracion de la plataforma", null));
				PrimeFaces.current().executeScript("PF('blockPanelGral').hide()");
				FacesContext.getCurrentInstance().validationFailed();
				return;
			}

			String usuario = this.getUsuario().getUsername();
			String clave = this.getUsuario().getPassword();

			SimpleDateFormat formatoFecha = new SimpleDateFormat("yyyyMMddHHmmss");
			String fecha = formatoFecha.format(new Date()).toString();

			String idTransaccionCliente = crearIdTransaccionCliente();

			String caracteristicaTelefono = "0";
			String numeroTelefono = "0";

			String tipoProducto = "";
			String petAdicional1 = "";
			String petAdicional2 = "";

			if (idTipoProducto == 5) {
				// Estacionamiento Elinpar
				tipoProducto = "Z";
				petAdicional1 = valorIn1;
				petAdicional2 = valorIn3 + "@" + valorIn2;

			} else if (idTipoProducto == 6) {
				// Multa Elinpar
				tipoProducto = "H";
				petAdicional1 = valorIn1;
				petAdicional2 = "";

			} else if (idTipoProducto == 7) {
				// Rec. Tarjeta Mifare Elinpar
				tipoProducto = "N";
				petAdicional1 = valorIn1;
				petAdicional2 = "";

			} else if (idTipoProducto == 8) {
				// Rec. Saldo Telefono Elinpar
				tipoProducto = "Q";
				petAdicional1 = valorIn1;
				;
				petAdicional2 = "";

			} else if (idTipoProducto == 9) {
				// Venta Tarjeta Mifare Elinpar
				tipoProducto = "U";
				petAdicional1 = valorIn1;
				petAdicional2 = valorIn2;

			} else {
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
						"El Tipo de Producto del Producto seleccionado, no esta soportado para la Consulta", null));
				PrimeFaces.current().executeScript("PF('blockPanelGral').hide()");
				FacesContext.getCurrentInstance().validationFailed();
				return;
			}

			if (!solicitarImporte) {
				importe = 0F;
			} else {
				if ((importe == null) || "".equals(importe)) {
					FacesContext.getCurrentInstance().addMessage(null,
							new FacesMessage(FacesMessage.SEVERITY_ERROR, "El importe es requerido", null));
					PrimeFaces.current().executeScript("PF('blockPanelGral').hide()");
					FacesContext.getCurrentInstance().validationFailed();
					return;
				}

				if ((idMoneda == null) || "".equals(idMoneda)) {
					FacesContext.getCurrentInstance().addMessage(null,
							new FacesMessage(FacesMessage.SEVERITY_ERROR, "La moneda es requerido", null));
					PrimeFaces.current().executeScript("PF('blockPanelGral').hide()");
					FacesContext.getCurrentInstance().validationFailed();
					return;
				}
			}

			String trama = "WebACG" + tipoProducto + "" + this.getUsuario().getIdMayorista() + ""
					+ this.getUsuario().getIdCliente() + "" + this.getUsuario().getIdDistribuidor() + "" + usuario
					+ "" + clave + "" + fecha + "" + idTransaccionCliente + "" + idProducto + "" + importe + ""
					+ caracteristicaTelefono + "" + numeroTelefono + "certON$false" + petAdicional1 + ""
					+ petAdicional2 + ".";

			String idEstadoTransaccion = "";
			String descEstadoTransaccion = "";
			String resAdicional1 = "";
			String resAdicional2 = "";

			Boolean debugGatewayCon = false;

			try {
				if (debugGatewayCon) {
					System.out.println("TRAMA A ENVIAR: |" + trama + "|");
				}

				Date tiempo1 = new Date();
				Socket sock;

				if (cg.getGatewayUsarSSL().getValue() == 1) {
					// Create socket SSL
					if (debugGatewayCon) {
						System.out.println("..... USO SOCKET CON SSL .....");
					}
					SocketFactory socketFactory = SSLSocketFactory.getDefault();
					sock = socketFactory.createSocket();
				} else {
					// Create socket sin SSL
					if (debugGatewayCon) {
						System.out.println("..... USO SOCKET SIN SSL .....");
					}
					sock = new Socket();
				}

				// Bind to a local ephemeral port
				sock.bind(null);

				if (debugGatewayCon) {
					System.out.println("Socket Creado y Bindeado");
				}

				// Connect to hostname on port especificado with a timeout especificado de
				// conexion
				if (debugGatewayCon) {
					System.out.println("Conectando a Socket: |" + cg.getGatewayIP() + ":" + cg.getGatewayPort()
							+ "| con un TimeOutDeConexion de: |" + cg.getGatewayTOconMS() + "| ms.....");
				}
				sock.connect(new InetSocketAddress(cg.getGatewayIP().getValue(), cg.getGatewayPort().getValue()),
						cg.getGatewayTOconMS().getValue());

				if (debugGatewayCon) {
					System.out.println("Conexion Establecida");
				}

				// Seteo el timeOut de lectura
				if (debugGatewayCon) {
					System.out.println("Configuro el TimeOut de Lectura en: |" + cg.getGatewayTOreadMS() + "| ms.");
				}
				sock.setSoTimeout(cg.getGatewayTOreadMS().getValue());

				// Buffer de Lectura
				BufferedWriter wr = new BufferedWriter(new OutputStreamWriter(sock.getOutputStream(), "UTF-8"));

				if (debugGatewayCon) {
					System.out.println("Enviando Trama y Esperando Respuesta");
				}

				// Send Data
				wr.write(trama);
				wr.write("\r\n");
				wr.flush();

				// Response
				BufferedReader rd = new BufferedReader(new InputStreamReader(sock.getInputStream()));
				Thread.sleep(500);
				String result = "";

				// LECTURA
				int leido = 0;
				while (leido != -1) {
					leido = rd.read();
					if (leido != -1) {
						char c = (char) leido;
						result = result + c;
					}
				}

				if (debugGatewayCon) {
					System.out.println("##############################################");
				}
				if (debugGatewayCon) {
					System.out.println("RESPUESTA COMPLETA: #### |" + result + "| ####");
				}
				if (debugGatewayCon) {
					System.out.println("##############################################");
				}

				if (debugGatewayCon) {
					System.out.println("############################");
				}
				if (debugGatewayCon) {
					System.out.println("#### SPLIT DE RESPUESTA ####");
				}
				if (debugGatewayCon) {
					System.out.println("############################");
				}

				char iniTrama = (char) 2; // Inicio de trama
				char finTrama = (char) 3; // Fin de trama
				char spTrama = (char) 4; // Separador de datos

				String[] r = result.replace(iniTrama + "", "").replace(finTrama + "", "").split(spTrama + "", -1);

				for (int i = 0; i < r.length; i++) {
					String c = r[i];

					switch (i) {
					case 0:
						if (debugGatewayCon) {
							System.out.println((i + 1) + ") tipoTransaccion: |" + c + "|");
						}
						break;
					case 1:
						if (debugGatewayCon) {
							System.out.println((i + 1) + ") encabezado: |" + c + "|");
						}
						break;
					case 2:
						if (debugGatewayCon) {
							System.out.println((i + 1) + ") Fecha: |" + c + "|");
						}
						break;
					case 3:
						if (debugGatewayCon) {
							System.out.println((i + 1) + ") idCliente: |" + c + "|");
						}
						break;
					case 4:
						if (debugGatewayCon) {
							System.out.println((i + 1) + ") idUsuario: |" + c + "|");
						}
						break;
					case 5:
						if (debugGatewayCon) {
							System.out.println((i + 1) + ") tipoCliente: |" + c + "|");
						}
						break;
					case 6:
						if (debugGatewayCon) {
							System.out.println((i + 1) + ") idTipoTerminal: |" + c + "|");
						}
						break;
					case 7:
						if (debugGatewayCon) {
							System.out.println((i + 1) + ") razonSocialCliente: |" + c + "|");
						}
						break;
					case 8:
						if (debugGatewayCon) {
							System.out.println((i + 1) + ") saldoDisponibleDelCliente: |" + c + "|");
						}
						break;
					case 9:
						if (debugGatewayCon) {
							System.out.println((i + 1) + ") usuario: |" + c + "|");
						}
						break;
					case 10:
						if (debugGatewayCon) {
							System.out.println((i + 1) + ") tipoOperacion: |" + c + "|");
						}
						break;
					case 11:
						if (debugGatewayCon) {
							System.out.println((i + 1) + ") idTransaccion: |" + c + "|");
						}
						break;
					case 12:
						if (debugGatewayCon) {
							System.out.println((i + 1) + ") idProducto: |" + c + "|");
						}
						break;
					case 13:
						if (debugGatewayCon) {
							System.out.println((i + 1) + ") descripcionProducto: |" + c + "|");
						}
						break;
					case 14:
						if (debugGatewayCon) {
							System.out.println((i + 1) + ") moneda: |" + c + "|");
						}
						break;
					case 15:
						if (debugGatewayCon) {
							System.out.println((i + 1) + ") importe: |" + c + "|");
						}
						break;
					case 16:
						if (debugGatewayCon) {
							System.out.println((i + 1) + ") caracteristicaTelefono: |" + c + "|");
						}
						break;
					case 17:
						if (debugGatewayCon) {
							System.out.println((i + 1) + ") numeroTelefono: |" + c + "|");
						}
						break;
					case 18:
						if (debugGatewayCon) {
							System.out.println((i + 1) + ") pin: |" + c + "|");
						}
						break;
					case 19:
						if (debugGatewayCon) {
							System.out.println((i + 1) + ") serie: |" + c + "|");
						}
						break;
					case 20:
						if (debugGatewayCon) {
							System.out.println((i + 1) + ") cat: |" + c + "|");
						}
						break;
					case 21:
						if (debugGatewayCon) {
							System.out.println((i + 1) + ") idLlotePin: |" + c + "|");
						}
						break;
					case 22:
						if (debugGatewayCon) {
							System.out.println((i + 1) + ") vigenciaPin: |" + c + "|");
						}
						break;
					case 23:
						if (debugGatewayCon) {
							System.out.println((i + 1) + ") idEstadoTransaccion: |" + c + "|");
						}
						idEstadoTransaccion = c;
						break;
					case 24:
						if (debugGatewayCon) {
							System.out.println((i + 1) + ") descEstadoTransaccion: |" + c + "|");
						}
						descEstadoTransaccion = c;
						break;
					case 25:
						if (debugGatewayCon) {
							System.out.println((i + 1) + ") instrucciones: |" + c + "|");
						}
						break;
					case 26:
						if (debugGatewayCon) {
							System.out.println((i + 1) + ") callcenter1: |" + c + "|");
						}
						break;
					case 27:
						if (debugGatewayCon) {
							System.out.println((i + 1) + ") callcenter2: |" + c + "|");
						}
						break;
					case 28:
						if (debugGatewayCon) {
							System.out.println((i + 1) + ") pie1: |" + c + "|");
						}
						break;
					case 29:
						if (debugGatewayCon) {
							System.out.println((i + 1) + ") pie2: |" + c + "|");
						}
						break;
					case 30:
						if (debugGatewayCon) {
							System.out.println((i + 1) + ") pie3: |" + c + "|");
						}
						break;
					case 31:
						if (debugGatewayCon) {
							System.out.println((i + 1) + ") pie4: |" + c + "|");
						}
						break;
					case 32:
						if (debugGatewayCon) {
							System.out.println((i + 1) + ") finTicket: |" + c + "|");
						}
						break;
					case 33:
						if (debugGatewayCon) {
							System.out.println((i + 1) + ") productos: |" + c + "|");
						}
						break;
					case 34:
						if (debugGatewayCon) {
							System.out.println((i + 1) + ") resAdicional1: |" + c + "|");
						}
						resAdicional1 = c;
						resAdicional1ParaConfirmarVenta = c;
						break;
					case 35:
						if (debugGatewayCon) {
							System.out.println((i + 1) + ") resAdicional2: |" + c + "|");
						}
						resAdicional2 = c;
						resAdicional2ParaConfirmarVenta = c;
						break;
					case 36:
						if (debugGatewayCon) {
							System.out.println((i + 1) + ") resAdicional3: |" + c + "|");
						}
						break;

					default:
						break;
					}
				}

				if (debugGatewayCon) {
					System.out.println("################################");
				}
				if (debugGatewayCon) {
					System.out.println("#### FIN SPLIT DE RESPUESTA ####");
				}
				if (debugGatewayCon) {
					System.out.println("################################");
				}

				if (debugGatewayCon) {
					System.out.println("Cierro Conexiones");
				}
				wr.close();
				rd.close();
				sock.close();

				Date tiempo2 = new Date();
				Long timempoTotalResp = tiempo2.getTime() - tiempo1.getTime();
				if (debugGatewayCon) {
					System.out.println("Tiempo total de respuesta en milisegundos: |" + timempoTotalResp + "|");
				}

			} catch (Exception exception) {
				if (debugGatewayCon) {
					System.out
							.println("Error en Conexion, envio o recepcion de trama: |" + exception.getMessage() + "|");
				}
				descEstadoTransaccion = descEstadoTransaccion
						+ "\nError de conexion con el Gateway. Por favor reintente";
			}

			if ("M0000".equals(idEstadoTransaccion)) {
				if (idTipoProducto == 5) {
					// Estacionamiento Elinpar

					String ra2[] = resAdicional2.split("@", -1);
					descEstadoTransaccion = descEstadoTransaccion + "\n";
					descEstadoTransaccion = descEstadoTransaccion + "\nImporte: " + ra2[5];
					descEstadoTransaccion = descEstadoTransaccion + "\nHora de Fin: " + ra2[2];
					descEstadoTransaccion = descEstadoTransaccion + "\n";
					descEstadoTransaccion = descEstadoTransaccion + "\nPatente: " + resAdicional1;
					descEstadoTransaccion = descEstadoTransaccion + "\nDuracion del Estacionamiento: " + ra2[0];
					descEstadoTransaccion = descEstadoTransaccion + "\nHora de Inicio: " + ra2[1];
					descEstadoTransaccion = descEstadoTransaccion + "\nSubZona: " + ra2[3];

				} else if (idTipoProducto == 6) {
					// Multa Elinpar
					String ra2[] = resAdicional2.split("@", -1);

					descEstadoTransaccion = descEstadoTransaccion + "\n";
					descEstadoTransaccion = descEstadoTransaccion + "\nImporte: " + ra2[6];
					descEstadoTransaccion = descEstadoTransaccion + "\n";
					descEstadoTransaccion = descEstadoTransaccion + "\nN. Acta: " + resAdicional1;
					descEstadoTransaccion = descEstadoTransaccion + "\nTipo de Infraccion: " + ra2[1];
					descEstadoTransaccion = descEstadoTransaccion + "\nHora del Acta: " + ra2[2];
					descEstadoTransaccion = descEstadoTransaccion + "\nPatente: " + ra2[3];
					descEstadoTransaccion = descEstadoTransaccion + "\nSubZona: " + ra2[4];
					descEstadoTransaccion = descEstadoTransaccion + "\nMarca: " + ra2[7];
					descEstadoTransaccion = descEstadoTransaccion + "\nColor: " + ra2[8];

				} else if (idTipoProducto == 7) {
					// Rec. Tarjeta Mifare Elinpar
					descEstadoTransaccion = descEstadoTransaccion + "\n";
					descEstadoTransaccion = descEstadoTransaccion + "\nN. Tarjeta: " + resAdicional1;
					descEstadoTransaccion = descEstadoTransaccion + "\nImporte: " + importe;

				} else if (idTipoProducto == 8) {
					// Rec. Saldo Telefono Elinpar
					descEstadoTransaccion = descEstadoTransaccion + "\n";
					descEstadoTransaccion = descEstadoTransaccion + "\nN. Telefono: " + resAdicional1;
					descEstadoTransaccion = descEstadoTransaccion + "\nImporte: " + importe;

				} else if (idTipoProducto == 9) {
					// Venta Tarjeta Mifare Elinpar
					String ra2[] = resAdicional2.split("@", -1);

					descEstadoTransaccion = descEstadoTransaccion + "\n";
					descEstadoTransaccion = descEstadoTransaccion + "\nImporte: " + ra2[2];
					descEstadoTransaccion = descEstadoTransaccion + "\n";
					descEstadoTransaccion = descEstadoTransaccion + "\nN. Tarjeta: " + resAdicional1;
					descEstadoTransaccion = descEstadoTransaccion + "\nPatente: " + ra2[0];

				}
			} else {
				if (debugGatewayCon) {
					System.out.println("La consulta salio Rechazada: |" + idEstadoTransaccion + "|, |"
							+ descEstadoTransaccion + "|");
				}
			}

			// TODO eliminar la variable valorString con sus propiedades

			if (!"M0000".equals(idEstadoTransaccion)) {
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
						"No se puede realizar la Operacion " + descEstadoTransaccion, null));
				FacesContext.getCurrentInstance().validationFailed();
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
					LogACGHelper.escribirLog(null, "Consulta de previa de Venta. Error: |" + ste.getMessage() + "|");
					FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
							"Consulta de previa de Venta. Error: |" + ste.getMessage() + "|", null));
				}
			} else {
				LogACGHelper.escribirLog(null, "Consulta de previa de Venta. Error: |" + ste.getMessage() + "|");
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
						"Consulta de previa de Venta. Error: |" + ste.getMessage() + "|", null));
			}
			FacesContext.getCurrentInstance().validationFailed();
		} catch (Exception e) {
			LogACGHelper.escribirLog(null, "Consulta de previa de Venta. Error: |" + e.getMessage() + "|");
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
					"Consulta de previa de Venta. Error: |" + e.getMessage() + "|", null));
			FacesContext.getCurrentInstance().validationFailed();
		}
		PrimeFaces.current().executeScript("PF('blockPanelGral').hide()");
	}

	public void realizarVentaOff() {

		Boolean finOff = true;
		tituloTicket = "VENTA OFFLINE";

		try {
			idEstadoTransaccionOff = "";
			if ((idProductoOff == null) || idProductoOff.equals("")) {
				finOff = false;
				FacesContext.getCurrentInstance().addMessage(null,
						new FacesMessage(FacesMessage.SEVERITY_ERROR, "El producto es un campo requerido", null));
				PrimeFaces.current().executeScript("PF('blockPanelGral').hide()");
				FacesContext.getCurrentInstance().validationFailed();
				return;
			}

			if ((importeOff == null) || "".equals(importeOff)) {
				finOff = false;
				FacesContext.getCurrentInstance().addMessage(null,
						new FacesMessage(FacesMessage.SEVERITY_ERROR, "El importe es requerido", null));
				PrimeFaces.current().executeScript("PF('blockPanelGral').hide()");
				FacesContext.getCurrentInstance().validationFailed();
				return;
			}

			if ((idMonedaOff == null) || "".equals(idMonedaOff)) {
				finOff = false;
				FacesContext.getCurrentInstance().addMessage(null,
						new FacesMessage(FacesMessage.SEVERITY_ERROR, "La moneda es requerido", null));
				PrimeFaces.current().executeScript("PF('blockPanelGral').hide()");
				FacesContext.getCurrentInstance().validationFailed();
				return;
			}

			if (!((this.getUsuario().getIdTipoTerminal() == 1) || (this.getUsuario().getPermitirVentasPorWeb()))) {
				finOff = true;
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
						"El tipo de terminal es incorrecto para operar", null));
				PrimeFaces.current().executeScript("PF('blockPanelGral').hide()");
				FacesContext.getCurrentInstance().validationFailed();
				return;
			}

			if (!"P".equals(this.getUsuario().getTipoCliente())) {
				finOff = true;
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
						"El tipo de cliente es incorrecto para operar", null));
				PrimeFaces.current().executeScript("PF('blockPanelGral').hide()");
				FacesContext.getCurrentInstance().validationFailed();
				return;
			}

			String usuario = this.getUsuario().getUsername();
			String clave = this.getUsuario().getPassword();

			String idTransaccionCliente = crearIdTransaccionCliente();

			GregorianCalendar gcFechaHoy = new GregorianCalendar();
			gcFechaHoy.setTime(new Date());
			XMLGregorianCalendar xmlGCFechaHoy = DatatypeFactory.newInstance().newXMLGregorianCalendar(gcFechaHoy);

			this.ticketOff = CoreServiceHelper.getCoreService(CfgTimeout.VENTA).realizarVenta(
					this.getUsuario().getIdMayorista(), this.getUsuario().getIdCliente(), usuario, clave, xmlGCFechaHoy,
					idTransaccionCliente, idProductoOff, importeOff, null, null, null, "OFF", idMonedaOff);

			// Reemplazo los 2 puntos y el punto por enter en DescEstadoTransaccion
			// Para que las lineas se hagan mas cortas y el ticket salga mas angosto
			try {
				ticketOff.getDescEstadoTransaccion().setValue(
						ticketOff.getDescEstadoTransaccion().getValue().replace(":", ":\n\r").replace(".", ".\n\r"));
			} catch (Exception e) {
				// No hago nada
			}

			idEstadoTransaccionOff = ticketOff.getIdEstadoTransaccion().getValue();

			if ("M0000".equals(ticketOff.getIdEstadoTransaccion().getValue())) {
				FacesContext.getCurrentInstance().addMessage(null,
						new FacesMessage(FacesMessage.SEVERITY_INFO, "La venta se realizo con exito.", null));

				if (ticketOff != null && ticketOff.getIdTransaccion() != null
						&& ticketOff.getIdTransaccion().getValue() != null) {
					FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,
							"Id Transaccion: " + ticketOff.getIdTransaccion().getValue(), null));
				}

				this.mostrarTicket("OFFLINE");

				PrimeFaces.current().executeScript(
						"ACGSiteScriptsFCNS.cambiaFondoMsg('msgsProductos', '#212121', '#9ed5a0', '#a0d4b7', '"
								+ FacesContext.getCurrentInstance().getExternalContext().getRequestContextPath()
										.replace("/", "\\/")
								+ "\\/javax.faces.resource\\/images\\/24x24\\/operacionOK.png')");
			} else {
				FacesContext.getCurrentInstance().addMessage(null,
						new FacesMessage(FacesMessage.SEVERITY_ERROR, "Venta Rechazada!", null));
				if (ticketOff != null) {
					if (ticketOff.getIdTransaccion() != null) {
						FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
								"Id Transaccion: " + ticketOff.getIdTransaccion().getValue(), null));
					}
					if (ticketOff.getError().getValue().getMsgError().getValue() != null) {
						FacesContext.getCurrentInstance().addMessage(null,
								new FacesMessage(FacesMessage.SEVERITY_ERROR,
										"Error: " + ticketOff.getError().getValue().getCodigoError().getValue() + " - "
												+ ticketOff.getError().getValue().getMsgError().getValue(),
										null));
					}
					if (ticketOff.getDescEstadoTransaccion() != null) {
						if (ticketOff.getDescEstadoTransaccion().getValue() != null) {
							if ("M0001".equals(ticket.getDescEstadoTransaccion().getValue())) {
								FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
										"Estado Transaccion: " + ticketOff.getIdEstadoTransaccion().getValue()
										+ " - " + ticketOff.getDescEstadoTransaccion().getValue() , null));
								PrimeFaces.current().executeScript(
										"ACGSiteScriptsFCNS.cambiaFondoMsg('msgsProductos', '#212121', '#E4B34E', '#a0d4b7', '"
												+ FacesContext.getCurrentInstance().getExternalContext().getRequestContextPath()
														.replace("/", "\\/")
												+ "\\/javax.faces.resource\\/images\\/24x24\\/enProceso.png')");
							}else {
								FacesContext.getCurrentInstance().addMessage(null,
										new FacesMessage(FacesMessage.SEVERITY_ERROR,
												"Estado Transaccion: " + ticketOff.getIdEstadoTransaccion().getValue()
												+ " - " + ticketOff.getDescEstadoTransaccion().getValue(),
												null));
							}
						}
					}
				}

				FacesContext.getCurrentInstance().validationFailed();
				PrimeFaces.current().executeScript("PF('blockPanelGral').hide()");
			}
		} catch (WebServiceException ste) {
			if (ste != null && ste.getCause() != null && ste.getCause().getClass() != null) {
				if (ste.getCause().getClass().equals(ConnectException.class)) {
					FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
							"VENTA EN PROCESO (CRE-TOC). Por favor consulte el estado antes de devolver el dinero.",
							null));
					PrimeFaces.current().executeScript(
							"ACGSiteScriptsFCNS.cambiaFondoMsg('msgsProductos', '#212121', '#E4B34E', '#a0d4b7', '"
									+ FacesContext.getCurrentInstance().getExternalContext().getRequestContextPath()
											.replace("/", "\\/")
									+ "\\/javax.faces.resource\\/images\\/24x24\\/enProceso.png')");
				} else if (ste.getCause().getClass().equals(SocketTimeoutException.class)) {
					FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
							"VENTA EN PROCESO (CRE-TORW). Por favor consulte el estado antes de devolver el dinero.",
							null));
					PrimeFaces.current().executeScript(
							"ACGSiteScriptsFCNS.cambiaFondoMsg('msgsProductos', '#212121', '#E4B34E', '#a0d4b7', '"
									+ FacesContext.getCurrentInstance().getExternalContext().getRequestContextPath()
											.replace("/", "\\/")
									+ "\\/javax.faces.resource\\/images\\/24x24\\/enProceso.png')");
				} else if (ste.getCause().getClass().equals(UnknownHostException.class)) {
					LogACGHelper.escribirLog(null,
							"No se pudo establecer la comunicación (CRE-HNC): |" + ste.getMessage() + "|");
					FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
							"VENTA EN PROCESO (CRE-HNC). Por favor consulte el estado antes de devolver el dinero.",
							null));
					PrimeFaces.current().executeScript(
							"ACGSiteScriptsFCNS.cambiaFondoMsg('msgsProductos', '#212121', '#E4B34E', '#a0d4b7', '"
									+ FacesContext.getCurrentInstance().getExternalContext().getRequestContextPath()
											.replace("/", "\\/")
									+ "\\/javax.faces.resource\\/images\\/24x24\\/enProceso.png')");
				} else {
					FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
							"Error realizando venta contra el CORE: " + ste.getMessage(), null));
				}
			} else {
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
						"Error realizando venta contra el CORE: " + ste.getMessage(), null));
			}

			FacesContext.getCurrentInstance().validationFailed();
			PrimeFaces.current().executeScript("PF('blockPanelGral').hide()");
			return;
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error realizando venta: " + e.getMessage(), null));
			FacesContext.getCurrentInstance().validationFailed();
			PrimeFaces.current().executeScript("PF('blockPanelGral').hide()");
			return;
		} finally {
			if (finOff) {
				// Reseteo campos
				idProductoOff = null;
				idMonedaOff = "$";
				importeOff = null;
				logoOff = "../../../images/tarjeta.JPG";
				// idEstadoTransaccionOff = "";
				comentariosOff = "";
				this.codMnemonicoTipoProductoOff = "";
				this.codMnemonicoProductoOff = "";

			}
		}

	}

	// REIMPRESION DE TICKET ONLINE
	public void reimprimirTicketON() {

		Long idTRXreimp = 0L;

		try {
			if ((idTRXreimpRec == null) || (idTRXreimpRec == 0)) {
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,
						"Debe ingresar un numero de transaccion distinto de cero para reimprimir el ticket", null));

				FacesContext.getCurrentInstance().validationFailed();
				PrimeFaces.current().executeScript("PF('blockPanelGral').hide()");
			} else {
				if (null != idTRXreimpRec) {
					if (0L != idTRXreimpRec) {
						idTRXreimp = idTRXreimpRec;
					}
				}

				// CONSULTAR TRANSACCION ON
				String usuario = this.getUsuario().getUsername();
				String clave = this.getUsuario().getPassword();

				GregorianCalendar gcFechaHoy = new GregorianCalendar();
				gcFechaHoy.setTime(new Date());
				XMLGregorianCalendar xmlGCFechaHoy = DatatypeFactory.newInstance().newXMLGregorianCalendar(gcFechaHoy);

				this.ticket = CoreServiceHelper.getCoreService(CfgTimeout.CONSULTA).consultarTransaccion(
						this.getUsuario().getIdMayorista(), this.getUsuario().getIdCliente(), 0L, clave, xmlGCFechaHoy,
						idTRXreimp, null, 0L, 0F, 0, 0, null, usuario, false);

				if (ticket == null) {
					FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
							"No se encontro la transaccion solicitada", null));

					FacesContext.getCurrentInstance().validationFailed();
					PrimeFaces.current().executeScript("PF('blockPanelGral').hide()");

					return;
				} else {
					if (ticket.getIdTransaccion().getValue() == null) {
						FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
								"No se encontro la transaccion solicitada", null));

						FacesContext.getCurrentInstance().validationFailed();
						PrimeFaces.current().executeScript("PF('blockPanelGral').hide()");
					} else {

						// Reemplazo los 2 puntos y el punto por enter en DescEstadoTransaccion
						// Para que las lineas se hagan mas cortas y el ticket salga mas angosto
						try {
							ticket.getDescEstadoTransaccion().setValue(ticket.getDescEstadoTransaccion().getValue()
									.replace(":", ":\n\r").replace(".", ".\n\r"));
						} catch (Exception e) {
							// No hago nada
						}

						if ("ON".equals(ticket.getTipoOperacion().getValue())) {
							FacesContext.getCurrentInstance().addMessage(null,
									new FacesMessage(FacesMessage.SEVERITY_INFO,
											"Transaccion Encontrada: |" + ticket.getIdTransaccion().getValue() + "|",
											null));
							FacesContext.getCurrentInstance().addMessage(null,
									new FacesMessage(FacesMessage.SEVERITY_INFO,
											"Tipo de Transaccion: |" + ticket.getTipoOperacion().getValue() + "|",
											null));
							FacesContext.getCurrentInstance().addMessage(null,
									new FacesMessage(FacesMessage.SEVERITY_INFO,
											"Estado: |" + ticket.getDescEstadoTransaccion().getValue() + "|", null));

							PrimeFaces.current().executeScript(
									"ACGSiteScriptsFCNS.cambiaFondoMsg('msgsProductos', '#212121', '#9ed5a0', '#a0d4b7', '"
											+ FacesContext.getCurrentInstance().getExternalContext()
													.getRequestContextPath().replace("/", "\\/")
											+ "\\/javax.faces.resource\\/images\\/24x24\\\\/operacionOK.png')");

							PrimeFaces.current().executeScript("PF('blockPanelGral').hide()");
							this.mostrarTicket("ONLINE");
						} else {
							FacesContext.getCurrentInstance().addMessage(null,
									new FacesMessage(FacesMessage.SEVERITY_ERROR,
											"EL tipo de Transaccion encontrado es: |"
													+ ticket.getTipoOperacion().getValue() + "|",
											null));
							FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(
									FacesMessage.SEVERITY_ERROR, "Debe utilizar la Reimpresion de PIN", null));

							FacesContext.getCurrentInstance().validationFailed();
							PrimeFaces.current().executeScript("PF('blockPanelGral').hide()");
						}
					}
				}
			}
		} catch (WebServiceException ste) {
			if (ste != null && ste.getCause() != null && ste.getCause().getClass() != null) {
				if (ste.getCause().getClass().equals(ConnectException.class)) {
					FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
							"CONSULTA EN PROCESO (CRE-TOC). Por favor intente nuevamente.", null));
					PrimeFaces.current().executeScript(
							"ACGSiteScriptsFCNS.cambiaFondoMsg('msgsProductos', '#212121', '#E4B34E', '#a0d4b7', '"
									+ FacesContext.getCurrentInstance().getExternalContext().getRequestContextPath()
											.replace("/", "\\/")
									+ "\\/javax.faces.resource\\/images\\/24x24\\/enProceso.png')");
				} else if (ste.getCause().getClass().equals(SocketTimeoutException.class)) {
					FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
							"CONSULTA EN PROCESO (CRE-TORW). Por favor intente nuevamente.", null));
					PrimeFaces.current().executeScript(
							"ACGSiteScriptsFCNS.cambiaFondoMsg('msgsProductos', '#212121', '#E4B34E', '#a0d4b7', '"
									+ FacesContext.getCurrentInstance().getExternalContext().getRequestContextPath()
											.replace("/", "\\/")
									+ "\\/javax.faces.resource\\/images\\/24x24\\/enProceso.png')");
				} else if (ste.getCause().getClass().equals(UnknownHostException.class)) {
					LogACGHelper.escribirLog(null,
							"No se pudo establecer la comunicación (CRE-HNC): |" + ste.getMessage() + "|");
					FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
							"CONSULTA EN PROCESO (CRE-HNC). Por favor consulte el estado antes de devolver el dinero.",
							null));
					PrimeFaces.current().executeScript(
							"ACGSiteScriptsFCNS.cambiaFondoMsg('msgsProductos', '#212121', '#E4B34E', '#a0d4b7', '"
									+ FacesContext.getCurrentInstance().getExternalContext().getRequestContextPath()
											.replace("/", "\\/")
									+ "\\/javax.faces.resource\\/images\\/24x24\\/enProceso.png')");
				} else {
					FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
							"Error realizando reimpresion de Ticket: " + ste.getMessage(), null));
				}
			} else {
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
						"Error realizando reimpresion de Ticket: " + ste.getMessage(), null));
			}
			FacesContext.getCurrentInstance().validationFailed();
			PrimeFaces.current().executeScript("PF('blockPanelGral').hide()");
			return;
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
					"Error realizando reimpresion de Ticket: " + e.getMessage(), null));

			FacesContext.getCurrentInstance().validationFailed();
			PrimeFaces.current().executeScript("PF('blockPanelGral').hide()");
		} finally {
			idTRXreimpRec = null;
		}
		return;
	}

	// REIMPRESION DE TICKET OFFLINE
	public void reimprimirTicketOFF() {
		Long idTRXreimp = 0L;

		try {
			if ((idTRXreimpPIN == null) || (idTRXreimpPIN == 0)) {
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,
						"Debe ingresar un numero de transaccion distinto de cero para reimprimir el ticket", null));

				FacesContext.getCurrentInstance().validationFailed();
				PrimeFaces.current().executeScript("PF('blockPanelGral').hide()");

				return;
			} else {
				if (null != idTRXreimpPIN) {
					if (0L != idTRXreimpPIN) {
						idTRXreimp = idTRXreimpPIN;
					}
				}

				// CONSULTAR TRANSACCION OFF
				String usuario = this.getUsuario().getUsername();
				String clave = this.getUsuario().getPassword();

				GregorianCalendar gcFechaHoy = new GregorianCalendar();
				gcFechaHoy.setTime(new Date());
				XMLGregorianCalendar xmlGCFechaHoy = DatatypeFactory.newInstance().newXMLGregorianCalendar(gcFechaHoy);

				this.ticketOff = CoreServiceHelper.getCoreService(CfgTimeout.CONSULTA).consultarTransaccion(
						this.getUsuario().getIdMayorista(), this.getUsuario().getIdCliente(), 0L, clave, xmlGCFechaHoy,
						idTRXreimp, null, 0L, 0F, 0, 0, null, usuario, false);

				if (ticketOff == null) {
					FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
							"No se encontro la transaccion solicitada", null));

					FacesContext.getCurrentInstance().validationFailed();
					PrimeFaces.current().executeScript("PF('blockPanelGral').hide()");

					return;
				} else {
					if (ticketOff.getIdTransaccion().getValue() == null) {
						FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
								"No se encontro la transaccion solicitada", null));

						FacesContext.getCurrentInstance().validationFailed();
						PrimeFaces.current().executeScript("PF('blockPanelGral').hide()");

						return;
					} else {

						// Reemplazo los 2 puntos y el punto por enter en DescEstadoTransaccion
						// Para que las lineas se hagan mas cortas y el ticket salga mas angosto
						try {
							ticketOff.getDescEstadoTransaccion().setValue(ticketOff.getDescEstadoTransaccion()
									.getValue().replace(":", ":\n\r").replace(".", ".\n\r"));
						} catch (Exception e) {
							// No hago nada
						}

						if ("OFF".equals(ticketOff.getTipoOperacion().getValue())) {
							FacesContext.getCurrentInstance().addMessage(null,
									new FacesMessage(FacesMessage.SEVERITY_INFO,
											"Transaccion Encontrada: |" + ticketOff.getIdTransaccion().getValue() + "|",
											null));
							FacesContext.getCurrentInstance().addMessage(null,
									new FacesMessage(FacesMessage.SEVERITY_INFO,
											"Tipo de Transaccion: |" + ticketOff.getTipoOperacion().getValue() + "|",
											null));
							FacesContext.getCurrentInstance().addMessage(null,
									new FacesMessage(FacesMessage.SEVERITY_INFO,
											"Estado: |" + ticketOff.getDescEstadoTransaccion().getValue() + "|", null));

							PrimeFaces.current().executeScript(
									"ACGSiteScriptsFCNS.cambiaFondoMsg('msgsProductos', '#212121', '#9ed5a0', '#a0d4b7', '"
											+ FacesContext.getCurrentInstance().getExternalContext()
													.getRequestContextPath().replace("/", "\\/")
											+ "\\/javax.faces.resource\\/images\\/24x24\\/operacionOK.png')");

							PrimeFaces.current().executeScript("PF('blockPanelGral').hide()");
							this.mostrarTicket("OFFLINE");
						} else {
							FacesContext.getCurrentInstance().addMessage(null,
									new FacesMessage(FacesMessage.SEVERITY_ERROR,
											"EL tipo de Transaccion encontrado es: |"
													+ ticketOff.getTipoOperacion().getValue() + "|",
											null));
							FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(
									FacesMessage.SEVERITY_ERROR, "Debe utilizar la Reimpresion de Recarga", null));

							FacesContext.getCurrentInstance().validationFailed();
							PrimeFaces.current().executeScript("PF('blockPanelGral').hide()");
							return;
						}
					}
				}
			}
		} catch (WebServiceException ste) {
			if (ste != null && ste.getCause() != null && ste.getCause().getClass() != null) {
				if (ste.getCause().getClass().equals(ConnectException.class)) {
					FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
							"CONSULTA EN PROCESO (CRE-TOC). Por favor intente nuevamente.", null));
					PrimeFaces.current().executeScript(
							"ACGSiteScriptsFCNS.cambiaFondoMsg('msgsProductos', '#212121', '#E4B34E', '#a0d4b7', '"
									+ FacesContext.getCurrentInstance().getExternalContext().getRequestContextPath()
											.replace("/", "\\/")
									+ "\\/javax.faces.resource\\/images\\/24x24\\/enProceso.png')");
				} else if (ste.getCause().getClass().equals(SocketTimeoutException.class)) {
					FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
							"CONSULTA EN PROCESO (CRE-TORW). Por favor intente nuevamente.", null));
					PrimeFaces.current().executeScript(
							"ACGSiteScriptsFCNS.cambiaFondoMsg('msgsProductos', '#212121', '#E4B34E', '#a0d4b7', '"
									+ FacesContext.getCurrentInstance().getExternalContext().getRequestContextPath()
											.replace("/", "\\/")
									+ "\\/javax.faces.resource\\/images\\/24x24\\/enProceso.png')");
				} else if (ste.getCause().getClass().equals(UnknownHostException.class)) {
					LogACGHelper.escribirLog(null,
							"No se pudo establecer la comunicación (CRE-HNC): |" + ste.getMessage() + "|");
					FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
							"CONSULTA EN PROCESO (CRE-HNC). Por favor consulte el estado antes de devolver el dinero.",
							null));
					PrimeFaces.current().executeScript(
							"ACGSiteScriptsFCNS.cambiaFondoMsg('msgsProductos', '#212121', '#E4B34E', '#a0d4b7', '"
									+ FacesContext.getCurrentInstance().getExternalContext().getRequestContextPath()
											.replace("/", "\\/")
									+ "\\/javax.faces.resource\\/images\\/24x24\\/enProceso.png')");
				} else {
					FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
							"Error realizando reimpresion de Ticket: " + ste.getMessage(), null));
				}
			} else {
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
						"Error realizando reimpresion de Ticket: " + ste.getMessage(), null));
			}
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
					"Error realizando reimpresion de Ticket: " + e.getMessage(), null));

			FacesContext.getCurrentInstance().validationFailed();
			PrimeFaces.current().executeScript("PF('blockPanelGral').hide()");
		} finally {
			idTRXreimpPIN = null;
		}

		return;
	}

	private String crearIdTransaccionCliente() {
		// -----------------------------------------------------------------------------
		// Creo idTransaccionCliente (La fecha en Milisegundos concatenado con un random
		// -----------------------------------------------------------------------------
		Random rnd = new Random();
		Long nAleatorio = rnd.nextLong();
		String nAleatorioS = nAleatorio.toString();
		Long fechaMS = new Date().getTime();
		String fechaMSS = fechaMS.toString();
		String idTransaccionCliente = (fechaMSS + nAleatorioS).substring(0, 20);
		// ---------------------------------------------------------------------------------
		// FIN Creo idTransaccionCliente (La fecha en Milisegundos concatenado con un
		// random
		// ---------------------------------------------------------------------------------
		return idTransaccionCliente;
	}

	// Getters and Setters

	public Long getIdProducto() {
		return idProducto;
	}

	public void setIdProducto(Long idProducto) {
		this.idProducto = idProducto;
	}

	public String getIdMoneda() {
		return idMoneda;
	}

	public void setIdMoneda(String idMoneda) {
		this.idMoneda = idMoneda;
	}

	public Float getImporte() {
		return importe;
	}

	public void setImporte(Float importe) {
		this.importe = importe;
	}

	public Ticket getTicket() {
		return ticket;
	}

	public void setTicket(Ticket ticket) {
		this.ticket = ticket;
	}

	public List<SelectItem> getProductos() {

		if (idProducto == null) {
			logoOn = "../../../images/telefono.JPG";
		}

		this.productos = null;

		if (leyenda == null) {
			leyenda = "Telefono";
		}

		if (celular == null) {
			celular = 1;
			idTipoProducto = 1L;
		}

		try {
			if (this.productos == null) {
				this.productos = new ArrayList<SelectItem>();
				ArrayOfCabeceraProducto l = GestionServiceHelper.getGestionService(CfgTimeout.DEFAULT)
						.obtenerProductosHabilitadosParaOperar(this.getUsuario().getIdMayorista(),
								this.getUsuario().getIdCliente(), "ON");
				this.cabeceraProdsOnline = new ArrayOfCabeceraProducto();
				this.cabeceraProdsOnline.getCabeceraProducto().clear();
				for (CabeceraProducto cp : l.getCabeceraProducto()) {
					this.productos.add(new SelectItem(cp.getIdProducto().getValue(), cp.getDescProducto().getValue()));
					this.cabeceraProdsOnline.getCabeceraProducto().add(cp);
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
					LogACGHelper.escribirLog(null,
							"No se pudo establecer la comunicación (GST-HNC): |" + ste.getMessage() + "|");
					FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
							"No se pudo establecer la comunicación (GST-HNC).\n Por favor intente nuevamente.", null));
				} else {
					LogACGHelper.escribirLog(null, "Error leyendo ProductosOn: |" + ste.getMessage() + "|");
					FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
							"Error leyendo ProductosOn: |" + ste.getMessage() + "|", null));
				}
			} else {
				LogACGHelper.escribirLog(null, "Error leyendo ProductosOn: |" + ste.getMessage() + "|");
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
						"Error leyendo ProductosOn: |" + ste.getMessage() + "|", null));
			}
		} catch (Exception e) {
			LogACGHelper.escribirLog(null, "Error leyendo ProductosOn: |" + e.getMessage() + "|");
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
					"Error leyendo ProductosOn: |" + e.getMessage() + "|", null));
			FacesContext.getCurrentInstance().validationFailed();
		}
		return productos;
	}

	public void setProductos(List<SelectItem> productos) {
		this.productos = productos;
	}

	public Long getIdProductoOff() {
		return idProductoOff;
	}

	public void setIdProductoOff(Long idProductoOff) {
		this.idProductoOff = idProductoOff;
	}

	public String getIdMonedaOff() {
		return idMonedaOff;
	}

	public void setIdMonedaOff(String idMonedaOff) {
		this.idMonedaOff = idMonedaOff;
	}

	public Float getImporteOff() {
		return importeOff;
	}

	public void setImporteOff(Float importeOff) {
		this.importeOff = importeOff;
	}

	public List<SelectItem> getProductosOff() {

		if (idProductoOff == null) {
			logoOff = "../../../images/tarjeta.JPG";
		}

		this.productosOff = null;
		try {
			if (this.productosOff == null) {
				this.productosOff = new ArrayList<SelectItem>();
				ArrayOfCabeceraProducto l = GestionServiceHelper.getGestionService(CfgTimeout.DEFAULT)
						.obtenerProductosHabilitadosParaOperar(this.getUsuario().getIdMayorista(),
								this.getUsuario().getIdCliente(), "OFF");
				this.cabeceraProdsOffline = new ArrayList<CabeceraProducto>();
				this.cabeceraProdsOffline.clear();
				for (CabeceraProducto cp : l.getCabeceraProducto()) {

					if (cp.getIdTipoProducto().getValue() == 1) {
						// Muestro solo los productos de tipo (1 - Celular).
						// El resto de los productos OFFLINE, no los muestro
						this.productosOff
								.add(new SelectItem(cp.getIdProducto().getValue(), cp.getDescProducto().getValue()));
						this.cabeceraProdsOffline.add(cp);
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
					LogACGHelper.escribirLog(null,
							"No se pudo establecer la comunicación (GST-HNC): |" + ste.getMessage() + "|");
					FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
							"No se pudo establecer la comunicación (GST-HNC).\n Por favor intente nuevamente.", null));
				} else {
					LogACGHelper.escribirLog(null, "Error leyendo ProductosOff: |" + ste.getMessage() + "|");
					FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
							"Error leyendo ProductosOff: |" + ste.getMessage() + "|", null));
				}
			} else {
				LogACGHelper.escribirLog(null, "Error leyendo ProductosOff: |" + ste.getMessage() + "|");
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
						"Error leyendo ProductosOff: |" + ste.getMessage() + "|", null));
			}
		} catch (Exception e) {
			LogACGHelper.escribirLog(null, "Error leyendo ProductosOff: |" + e.getMessage() + "|");
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
					"Error leyendo ProductosOff: |" + e.getMessage() + "|", null));
			FacesContext.getCurrentInstance().validationFailed();
		}
		return productosOff;
	}

	public void setProductosOff(List<SelectItem> productosOff) {
		this.productosOff = productosOff;
	}

	public Ticket getTicketOff() {
		return ticketOff;
	}

	public void setTicketOff(Ticket ticketOff) {
		this.ticketOff = ticketOff;
	}

	public String getLogoOn() {
		return logoOn;
	}

	public void setLogoOn(String logoOn) {
		this.logoOn = logoOn;
	}

	public ArrayOfCabeceraProducto getCabeceraProdsOnline() {
		return cabeceraProdsOnline;
	}

	public void setCabeceraProdsOnline(ArrayOfCabeceraProducto cabeceraProdsOnline) {
		this.cabeceraProdsOnline = cabeceraProdsOnline;
	}

	public List<CabeceraProducto> getCabeceraProdsOffline() {
		return cabeceraProdsOffline;
	}

	public void setCabeceraProdsOffline(List<CabeceraProducto> cabeceraProdsOffline) {
		this.cabeceraProdsOffline = cabeceraProdsOffline;
	}

	public String getLogoOff() {
		return logoOff;
	}

	public void setLogoOff(String logoOff) {
		this.logoOff = logoOff;
	}

	public String getIdEstadoTransaccion() {
		return idEstadoTransaccion;
	}

	public void setIdEstadoTransaccion(String idEstadoTransaccion) {
		this.idEstadoTransaccion = idEstadoTransaccion;
	}

	public String getIdEstadoTransaccionOff() {
		return idEstadoTransaccionOff;
	}

	public void setIdEstadoTransaccionOff(String idEstadoTransaccionOff) {
		this.idEstadoTransaccionOff = idEstadoTransaccionOff;
	}

	public Long getIdTRXreimpRec() {
		return idTRXreimpRec;
	}

	public void setIdTRXreimpRec(Long idTRXreimpRec) {
		this.idTRXreimpRec = idTRXreimpRec;
	}

	public Long getIdTRXreimpPIN() {
		return idTRXreimpPIN;
	}

	public void setIdTRXreimpPIN(Long idTRXreimpPIN) {
		this.idTRXreimpPIN = idTRXreimpPIN;
	}

	public Boolean getHabilitarRefreshPVdesdePVPjava() {
		this.habilitarRefreshPVdesdePVPjava = false;
		if (null != this.getUsuario().getRefreshAutomMonTRXsPV()) {
			if (1 == this.getUsuario().getRefreshAutomMonTRXsPV()) {
				this.habilitarRefreshPVdesdePVPjava = true;
			}
		}

		return habilitarRefreshPVdesdePVPjava;
	}

	public void setHabilitarRefreshPVdesdePVPjava(Boolean habilitarRefreshPVdesdePVPjava) {
		this.habilitarRefreshPVdesdePVPjava = habilitarRefreshPVdesdePVPjava;
	}

	public Boolean getConsultaPrestamo() {
		return consultaPrestamo;
	}

	public void setConsultaPrestamo(Boolean consultaPrestamo) {
		this.consultaPrestamo = consultaPrestamo;
	}

	public String getIdBeneficiarioPrestamo() {
		return idBeneficiarioPrestamo;
	}

	public void setIdBeneficiarioPrestamo(String idBeneficiarioPrestamo) {
		this.idBeneficiarioPrestamo = idBeneficiarioPrestamo;
	}

	public String getCaractTelFijoPrestamo() {
		return caractTelFijoPrestamo;
	}

	public void setCaractTelFijoPrestamo(String caractTelFijoPrestamo) {
		this.caractTelFijoPrestamo = caractTelFijoPrestamo;
	}

	public String getNumeroTelFijoPrestamo() {
		return numeroTelFijoPrestamo;
	}

	public void setNumeroTelFijoPrestamo(String numeroTelFijoPrestamo) {
		this.numeroTelFijoPrestamo = numeroTelFijoPrestamo;
	}

	public String getCaractCelularPrestamo() {
		return caractCelularPrestamo;
	}

	public void setCaractCelularPrestamo(String caractCelularPrestamo) {
		this.caractCelularPrestamo = caractCelularPrestamo;
	}

	public String getNumeroCelularPrestamo() {
		return numeroCelularPrestamo;
	}

	public void setNumeroCelularPrestamo(String numeroCelularPrestamo) {
		this.numeroCelularPrestamo = numeroCelularPrestamo;
	}

	public String getMailPrestamo() {
		return mailPrestamo;
	}

	public void setMailPrestamo(String mailPrestamo) {
		this.mailPrestamo = mailPrestamo;
	}

	public RespPrestamo getTicketPrestamo() {
		return ticketPrestamo;
	}

	public void setTicketPrestamo(RespPrestamo ticketPrestamo) {
		this.ticketPrestamo = ticketPrestamo;
	}

	public Long getImportePrestamo() {
		return importePrestamo;
	}

	public void setImportePrestamo(Long importePrestamo) {
		this.importePrestamo = importePrestamo;
	}

	public String getIdEstadoPrestamo() {
		return idEstadoPrestamo;
	}

	public void setIdEstadoPrestamo(String idEstadoPrestamo) {
		this.idEstadoPrestamo = idEstadoPrestamo;
	}

	public String getLeyenda() {

		leyenda = leyenda.replaceAll("#", "<br /><br />");

		return leyenda;
	}

	public void setLeyenda(String leyenda) {
		this.leyenda = leyenda;
	}

	public Integer getCelular() {
		return celular;
	}

	public void setCelular(Integer celular) {
		this.celular = celular;
	}

	public Integer getRbTipoBeneficiario() {
		return rbTipoBeneficiario;
	}

	public void setRbTipoBeneficiario(Integer rbTipoBeneficiario) {
		this.rbTipoBeneficiario = rbTipoBeneficiario;
	}

	public Long getIdTipoProducto() {
		return idTipoProducto;
	}

	public void setIdTipoProducto(Long idTipoProducto) {
		this.idTipoProducto = idTipoProducto;
	}

	public String getValorIn1() {
		return valorIn1;
	}

	public void setValorIn1(String valorIn1) {
		this.valorIn1 = valorIn1;
	}

	public String getValorIn2() {
		return valorIn2;
	}

	public void setValorIn2(String valorIn2) {
		this.valorIn2 = valorIn2;
	}

	public String getValorIn3() {
		return valorIn3;
	}

	public void setValorIn3(String valorIn3) {
		this.valorIn3 = valorIn3;
	}

	public Integer getAnchoValorIn1() {
		// Ancho por defecto
		//anchoValorIn1 = 60;
		anchoValorIn1 = 0;
		try {
			String ancho[] = null;
			String valor = "";
			
			
			if(arrayAnchos.containsKey(idTipoProducto.toString().concat(this.codMnemonicoProductoOn))) {
				valor = arrayAnchos.get(idTipoProducto.toString().concat(this.codMnemonicoProductoOn));	
			}else {
				if(arrayAnchos.containsKey(idTipoProducto.toString())) {
					valor = arrayAnchos.get(idTipoProducto.toString());
				}
			}
			
			ancho = valor.split(";", -1);	
			anchoValorIn1 = Integer.parseInt(ancho[0]);
		} catch (Exception e) {
			// No va nada
		}
		return anchoValorIn1;
	}

	public void setAnchoValorIn1(Integer anchoValorIn1) {
		this.anchoValorIn1 = anchoValorIn1;
	}

	public Integer getAnchoValorIn2() {
		// Ancho por defecto
		//anchoValorIn2 = 120;
		anchoValorIn2 = 0;		
		try {
			String ancho[] = null;
			String valor = "";
			
			
			if(arrayAnchos.containsKey(idTipoProducto.toString().concat(this.codMnemonicoProductoOn))) {
				valor = arrayAnchos.get(idTipoProducto.toString().concat(this.codMnemonicoProductoOn));	
			}else {
				if(arrayAnchos.containsKey(idTipoProducto.toString())) {
					valor = arrayAnchos.get(idTipoProducto.toString());
				}
			}
			
			ancho = valor.split(";", -1);	
			
			anchoValorIn2 = Integer.parseInt(ancho[1]);
		} catch (Exception e) {
			// No va nada
		}
		return anchoValorIn2;
	}

	public void setAnchoValorIn2(Integer anchoValorIn2) {
		this.anchoValorIn2 = anchoValorIn2;
	}

	public Integer getAnchoValorIn3() {
		// Ancho por defecto
		//anchoValorIn3 = 220;
		anchoValorIn3 = 0;
		try {
			String ancho[] = null;
			String valor = "";
			
			
			if(arrayAnchos.containsKey(idTipoProducto.toString().concat(this.codMnemonicoProductoOn))) {
				valor = arrayAnchos.get(idTipoProducto.toString().concat(this.codMnemonicoProductoOn));	
			}else {
				if(arrayAnchos.containsKey(idTipoProducto.toString())) {
					valor = arrayAnchos.get(idTipoProducto.toString());
				}
			}
			
			ancho = valor.split(";", -1);	
			
			anchoValorIn3 = Integer.parseInt(ancho[2]);
		} catch (Exception e) {
			// No va nada
		}
		return anchoValorIn3;
	}

	public void setAnchoValorIn3(Integer anchoValorIn3) {
		this.anchoValorIn3 = anchoValorIn3;
	}

	public Integer getMaxLengthValorIn1() {
		// MaxLength por defecto
		maxLengthValorIn1 = 4;
		try {
			String ml[] = null;
			String valor = "";
			
			
			if(arrayMaxLength.containsKey(idTipoProducto.toString().concat(this.codMnemonicoProductoOn))) {
				valor = arrayMaxLength.get(idTipoProducto.toString().concat(this.codMnemonicoProductoOn));	
			}else {
				if(arrayMaxLength.containsKey(idTipoProducto.toString())) {
					valor = arrayMaxLength.get(idTipoProducto.toString());
				}
			}
			
			ml = valor.split(";", -1);	
			
			maxLengthValorIn1 = Integer.parseInt(ml[0]);
		} catch (Exception e) {
			// No va nada
		}
		return maxLengthValorIn1;
	}

	public void setMaxLengthValorIn1(Integer maxLengthValorIn1) {
		this.maxLengthValorIn1 = maxLengthValorIn1;
	}

	public Integer getMaxLengthValorIn2() {
		// MaxLength por defecto
		maxLengthValorIn2 = 8;
		try {

			String ml[] = null;
			String valor = "";
			
			
			if(arrayMaxLength.containsKey(idTipoProducto.toString().concat(this.codMnemonicoProductoOn))) {
				valor = arrayMaxLength.get(idTipoProducto.toString().concat(this.codMnemonicoProductoOn));	
			}else {
				if(arrayMaxLength.containsKey(idTipoProducto.toString())) {
					valor = arrayMaxLength.get(idTipoProducto.toString());
				}
			}
			
			ml = valor.split(";", -1);	
			
			maxLengthValorIn2 = Integer.parseInt(ml[1]);
		} catch (Exception e) {
			// No va nada
		}
		return maxLengthValorIn2;
	}

	public void setMaxLengthValorIn2(Integer maxLengthValorIn2) {
		this.maxLengthValorIn2 = maxLengthValorIn2;
	}

	public Integer getMaxLengthValorIn3() {
		// MaxLength por defecto
		maxLengthValorIn3 = 18;
		try {

			String ml[] = null;
			String valor = "";
			
			
			if(arrayMaxLength.containsKey(idTipoProducto.toString().concat(this.codMnemonicoProductoOn))) {
				valor = arrayMaxLength.get(idTipoProducto.toString().concat(this.codMnemonicoProductoOn));	
			}else {
				if(arrayMaxLength.containsKey(idTipoProducto.toString())) {
					valor = arrayMaxLength.get(idTipoProducto.toString());
				}
			}
			
			ml = valor.split(";", -1);	
			
			maxLengthValorIn3 = Integer.parseInt(ml[2]);
		} catch (Exception e) {
			// No va nada
		}
		return maxLengthValorIn3;
	}

	public void setMaxLengthValorIn3(Integer maxLengthValorIn3) {
		this.maxLengthValorIn3 = maxLengthValorIn3;
	}

	public Boolean getSolicitarImporte() {
		// Valor por defecto (no solicitar importe)
		solicitarImporte = false;
		try {
			
			if(arraySolicitarImporte.containsKey(idTipoProducto.toString().concat(this.codMnemonicoProductoOn))) {
				solicitarImporte = arraySolicitarImporte.get(idTipoProducto.toString().concat(this.codMnemonicoProductoOn));	
			}else {
				if(arraySolicitarImporte.containsKey(idTipoProducto.toString())) {
					solicitarImporte = arraySolicitarImporte.get(idTipoProducto.toString());
				}
			}
			
		} catch (Exception e) {
			// No va nada
		}
		return solicitarImporte;
	}

	public void setSolicitarImporte(Boolean solicitarImporte) {
		this.solicitarImporte = solicitarImporte;
	}

	public Boolean getBotonAceptar() {
		// Valor por defecto (mostrar boton aceptar)
		botonAceptar = true;
		try {
			
			if(arrayBotonAceptar.containsKey(idTipoProducto.toString().concat(this.codMnemonicoProductoOn))) {
				botonAceptar = arrayBotonAceptar.get(idTipoProducto.toString().concat(this.codMnemonicoProductoOn));	
			}else {
				if(arrayBotonAceptar.containsKey(idTipoProducto.toString())) {
					botonAceptar = arrayBotonAceptar.get(idTipoProducto.toString());
				}
			}
						
		} catch (Exception e) {
			// No va nada
		}
		return botonAceptar;
	}

	public void setBotonAceptar(Boolean botonAceptar) {
		this.botonAceptar = botonAceptar;
	}

	public String getValorString() {
		return valorString;
	}

	public void setValorString(String valorString) {
		this.valorString = valorString;
	}

	public Boolean getValorNumericoIn1() {
		// Tipo de valor por defecto (false permite valores alfanumericos)
		valorNumericoIn1 = false;
		try {
			String valNum[] = null;
			String valor = "";
			
			
			if(arrayValorNumerico.containsKey(idTipoProducto.toString().concat(this.codMnemonicoProductoOn))) {
				valor = arrayValorNumerico.get(idTipoProducto.toString().concat(this.codMnemonicoProductoOn));	
			}else {
				if(arrayValorNumerico.containsKey(idTipoProducto.toString())) {
					valor = arrayValorNumerico.get(idTipoProducto.toString());
				}
			}
			
			valNum = valor.split(";", -1);	
			valorNumericoIn1 = Boolean.parseBoolean(valNum[0]);
		} catch (Exception e) {
			// No va nada
		}
		return valorNumericoIn1;
	}

	public void setValorNumericoIn1(Boolean valorNumericoIn1) {
		this.valorNumericoIn1 = valorNumericoIn1;
	}

	public Boolean getValorNumericoIn2() {
		// Tipo de valor por defecto (false permite valores alfanumericos)
		valorNumericoIn2 = false;
		try {
			String valNum[] = null;
			String valor = "";
			
			
			if(arrayValorNumerico.containsKey(idTipoProducto.toString().concat(this.codMnemonicoProductoOn))) {
				valor = arrayValorNumerico.get(idTipoProducto.toString().concat(this.codMnemonicoProductoOn));	
			}else {
				if(arrayValorNumerico.containsKey(idTipoProducto.toString())) {
					valor = arrayValorNumerico.get(idTipoProducto.toString());
				}
			}
			
			valNum = valor.split(";", -1);	
			
			valorNumericoIn2 = Boolean.parseBoolean(valNum[1]);
		} catch (Exception e) {
			// No va nada
		}
		return valorNumericoIn2;
	}

	public void setValorNumericoIn2(Boolean valorNumericoIn2) {
		this.valorNumericoIn2 = valorNumericoIn2;
	}

	public Boolean getValorNumericoIn3() {
		// Tipo de valor por defecto (false permite valores alfanumericos)
		valorNumericoIn3 = false;
		try {
			String valNum[] = null;
			String valor = "";
			
			
			if(arrayValorNumerico.containsKey(idTipoProducto.toString().concat(this.codMnemonicoProductoOn))) {
				valor = arrayValorNumerico.get(idTipoProducto.toString().concat(this.codMnemonicoProductoOn));	
			}else {
				if(arrayValorNumerico.containsKey(idTipoProducto.toString())) {
					valor = arrayValorNumerico.get(idTipoProducto.toString());
				}
			}
			
			valNum = valor.split(";", -1);				
			valorNumericoIn3 = Boolean.parseBoolean(valNum[2]);
		} catch (Exception e) {
			// No va nada
		}
		return valorNumericoIn3;
	}

	public void setValorNumericoIn3(Boolean valorNumericoIn3) {
		this.valorNumericoIn3 = valorNumericoIn3;
	}

	public List<SelectItem> getValoresProductosOff() {
		this.valoresProductosOff = null;
		try {
			this.valoresProductosOff = new ArrayList<SelectItem>();
			for (CabeceraProducto cp : cabeceraProdsOffline) {
				if ((idProductoOff != null) && (idProductoOff.equals(cp.getIdProducto().getValue()))) {
					for (Float v : cp.getImportePinesDisponibles().getValue().getFloat()) {
						this.valoresProductosOff.add(new SelectItem(v, v.toString()));
					}
				}
			}
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
					"Error Cargando los valores de los productos OFFLINE: " + e.getMessage(), null));
		}
		return valoresProductosOff;
	}

	public void setValoresProductosOff(List<SelectItem> valoresProductosOff) {
		this.valoresProductosOff = valoresProductosOff;
	}

	public Boolean getMostrarComboImporteEnProdOff() {
		this.mostrarComboImporteEnProdOff = false;
		for (CabeceraProducto cp : cabeceraProdsOffline) {
			if ((idProductoOff != null) && (idProductoOff.equals(cp.getIdProducto().getValue()))) {
				if ((cp.getImportePinesDisponibles() != null) && (cp.getImportePinesDisponibles().getValue() != null)
						&& (cp.getImportePinesDisponibles().getValue().getFloat() != null)
						&& (cp.getImportePinesDisponibles().getValue().getFloat().size() > 0)) {
					mostrarComboImporteEnProdOff = true;
				}
			}
		}
		return mostrarComboImporteEnProdOff;
	}

	public void setMostrarComboImporteEnProdOff(Boolean mostrarComboImporteEnProdOff) {
		this.mostrarComboImporteEnProdOff = mostrarComboImporteEnProdOff;
	}

	public Float getEcc() {
		// CONSULTA DE CC
		try {
			ecc = GestionServiceHelper.getGestionService(CfgTimeout.DEFAULT).ccEstado(1L,
					this.getUsuario().getIdMayorista(), this.getUsuario().getIdCliente());
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
					LogACGHelper.escribirLog(null, "Obtener ECC. Error: |" + ste.getMessage() + "|");
					FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
							"Obtener ECC. Error: |" + ste.getMessage() + "|", null));
				}
			} else {
				LogACGHelper.escribirLog(null, "Obtener ECC. Error: |" + ste.getMessage() + "|");
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
						"Obtener ECC. Error: |" + ste.getMessage() + "|", null));
			}
			ecc = null;
		} catch (Exception e) {
			LogACGHelper.escribirLog(null, "Obtener ECC. Error: |" + e.getMessage() + "|");
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
					"Obtener ECC. Error: |" + e.getMessage() + "|", null));
			FacesContext.getCurrentInstance().validationFailed();
			ecc = null;
		}
		return ecc;
	}

	public void setEcc(Float ecc) {
		this.ecc = ecc;
	}

	public String getResultadoMensaje() {
		return resultadoMensaje;
	}

	public void setResultadoMensaje(String resultadoMensaje) {
		this.resultadoMensaje = resultadoMensaje;
	}

	public CierreLote getCl() {
		return cl;
	}

	public void setCl(CierreLote cl) {
		this.cl = cl;
	}

	public Date getFechaActualCierreLote() {
		return fechaActualCierreLote;
	}

	public void setFechaActualCierreLote(Date fechaActualCierreLote) {
		this.fechaActualCierreLote = fechaActualCierreLote;
	}

	public Long getIdLoteParaConsultar() {
		return idLoteParaConsultar;
	}

	public void setIdLoteParaConsultar(Long idLoteParaConsultar) {
		this.idLoteParaConsultar = idLoteParaConsultar;
	}

	public String getTituloTicket() {
		return tituloTicket;
	}

	public void setTituloTicket(String tituloTicket) {
		this.tituloTicket = tituloTicket;
	}

	public Boolean getMostrarReporteComisionesVigentesPV() {
		try {
			if (!parametrosComisionesVigentesActualizados) {
				ParametrosList lp = GestionServiceHelper.getGestionService(CfgTimeout.DEFAULT).mostrarParametros(
						this.getUsuario().getIdMayorista(), "P", "mostrarReporteComisionesVigentesPV");
				if (lp != null && lp.getListParametros() != null
						&& lp.getListParametros().getValue().getParametro().size() == 1) {
					mostrarReporteComisionesVigentesPV = ("1"
							.equals(lp.getListParametros().getValue().getParametro().get(0).getValor().getValue()));
				}
				parametrosComisionesVigentesActualizados = true;
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
							"Mostrar ReporteComisiones Vigentes. Error: |" + ste.getMessage() + "|");
					FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
							"Mostrar ReporteComisiones Vigentes. Error: |" + ste.getMessage() + "|", null));
				}
			} else {
				LogACGHelper.escribirLog(null, "Mostrar ReporteComisiones Vigentes. Error: |" + ste.getMessage() + "|");
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
						"Mostrar ReporteComisiones Vigentes. Error: |" + ste.getMessage() + "|", null));
			}
		} catch (Exception e) {
			LogACGHelper.escribirLog(null, "Mostrar ReporteComisiones Vigentes. Error: |" + e.getMessage() + "|");
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
					"Mostrar ReporteComisiones Vigentes. Error: |" + e.getMessage() + "|", null));
			FacesContext.getCurrentInstance().validationFailed();
		}
		return mostrarReporteComisionesVigentesPV;
	}

	public void setMostrarReporteComisionesVigentesPV(Boolean mostrarReporteComisionesVigentesPV) {
		this.mostrarReporteComisionesVigentesPV = mostrarReporteComisionesVigentesPV;
	}

	public List<SelectItem> getImportesDisponiblesON() {
		return importesDisponiblesON;
	}

	public void setImportesDisponiblesON(List<SelectItem> importesDisponiblesON) {
		this.importesDisponiblesON = importesDisponiblesON;
	}

	public String getComentariosOn() {
		return comentariosOn;
	}

	public void setComentariosOn(String comentariosOn) {
		this.comentariosOn = comentariosOn;
	}

	public String getComentariosOff() {
		return comentariosOff;
	}

	public void setComentariosOff(String comentariosOff) {
		this.comentariosOff = comentariosOff;
	}

	public void activaMenuIconos(Integer opcTab) {
		origen = "";
		ArrayList<String> lstUpdate = new ArrayList<String>();

		switch (opcTab) {
		case 0: {
			this.setIndiceMenu(opcTab);
			muestraMnuIconos = true;
			mnuOpcIconos = "/secure/puntoDeVenta/fcnsProductos/iconosOpcProductos.xhtml";
			break;
		}
		case 1: {
			this.setIndiceMenu(opcTab);
			muestraMnuIconos = true;
			mnuOpcIconos = "/secure/puntoDeVenta/fcnsAdministracion/iconosOpcAdministracion.xhtml";
			break;
		}
		case 2: {
			this.setIndiceMenu(opcTab);
			muestraMnuIconos = true;
			mnuOpcIconos = "/secure/puntoDeVenta/fcnsReportes/iconosOpcReportes.xhtml";
			break;
		}

		case 3: {
			this.setIndiceMenu(opcTab);
			muestraMnuIconos = true;
			// mnuOpcIconos =
			// "/secure/puntoDeVenta/fcnsPublicidad/iconosOpcPublicidad.xhtml";
			mnuOpcIconos = "";
			break;
		}
		case 4: {
			this.setIndiceMenu(opcTab);
			muestraMnuIconos = true;
			mnuOpcIconos = "/secure/puntoDeVenta/fcnsReimpresiones/iconosOpcReimpresiones.xhtml";
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
		PrimeFaces.current().executeScript("ACGSiteScriptsFCNS.cierraPanelProducto();");
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

	public void setMuestraMnuIconos(boolean muestraMnuIconos) {
		this.muestraMnuIconos = muestraMnuIconos;
	}

	public String getMnuOpcIconos() {
		return mnuOpcIconos;
	}

	public void setMnuOpcIconos(String mnuOpcIconos) {
		this.mnuOpcIconos = mnuOpcIconos;
	}

	public void navegarFcnsProductos(Integer opcNav) {
		origen = "";
		switch (opcNav) {
		case 1: {
			inicializarValores();
			// Recargas
			opcAIncluir = "/secure/puntoDeVenta/fcnsProductos/productosOnline.xhtml";
			auxiliarAIncluir = "/secure/puntoDeVenta/fcnsProductos/includeAuxiliaresProductosOnline.xhtml";
			tituloPanelProducto = "Recargas";
			break;
		}
		case 2: {
			inicializarValores();
			// Productos Offline
			opcAIncluir = "/secure/puntoDeVenta/fcnsProductos/productosOffline.xhtml";
			tituloPanelProducto = "Productos Offline";
			break;
		}
		case 3: {
			inicializarValores();
			// Prestamos
			opcAIncluir = "/secure/puntoDeVenta/fcnsProductos/prestamos.xhtml";
			tituloPanelProducto = "Prestamos";
			break;
		}
		case 4: {
			// SUBE
			actualizarValoresSUBE();
			opcAIncluir = "/secure/puntoDeVenta/fcnsProductos/sube.xhtml";
			auxiliarAIncluir = "/secure/puntoDeVenta/fcnsProductos/includeAuxiliaresSube.xhtml";
			tituloPanelProducto = "SUBE";
			PrimeFaces.current().executeScript("init();");
			break;
		}
		case 5: {
			FacesContext facesContext = FacesContext.getCurrentInstance();
			PNetView pv = (PNetView) facesContext.getApplication().getELResolver().getValue(facesContext.getELContext(),
					null, "pNetView");
			pv.forzarInicializarPNet();
			// Provincia Net
			opcAIncluir = "/secure/puntoDeVenta/fcnsProductos/pnet.xhtml";
			auxiliarAIncluir = "/secure/puntoDeVenta/fcnsProductos/includeAuxiliaresPNet.xhtml";
			tituloPanelProducto = "Provincia Net";
			break;
		}
		case 6: {
			FacesContext facesContext = FacesContext.getCurrentInstance();
			SegurosView s = (SegurosView) facesContext.getApplication().getELResolver()
					.getValue(facesContext.getELContext(), null, "segurosView");
			s.limpiarPantalla();

			// Seguros
			opcAIncluir = "/secure/puntoDeVenta/fcnsProductos/seguros.xhtml";
			tituloPanelProducto = "Seguros";
			break;
		}
		case 7: {
			FacesContext facesContext = FacesContext.getCurrentInstance();
			PagoElectronicoView s = (PagoElectronicoView) facesContext.getApplication().getELResolver()
					.getValue(facesContext.getELContext(), null, "pagoElectronicoView");
			s.inicializar();
			
			// Pagos Electronicos
			opcAIncluir = "/secure/puntoDeVenta/fcnsProductos/pagoElectronico.xhtml";
			auxiliarAIncluir = "/secure/puntoDeVenta/fcnsProductos/includeAuxiliaresPagoElectronico.xhtml";
			tituloPanelProducto = "Pagos Electronicos";
			break;
		}
		case 8: {
			FacesContext facesContext = FacesContext.getCurrentInstance();
			SuperPagoView s = (SuperPagoView) facesContext.getApplication().getELResolver()
					.getValue(facesContext.getELContext(), null, "superPagoView");
			s.inicializar();
			
			// Super Pagos
			opcAIncluir = "/secure/puntoDeVenta/fcnsProductos/superPago.xhtml";
			tituloPanelProducto = "Cuentas";
			break;
		}		
		case 9: {
			FacesContext facesContext = FacesContext.getCurrentInstance();
			CobranzaServImpView s = (CobranzaServImpView) facesContext.getApplication().getELResolver()
					.getValue(facesContext.getELContext(), null, "cobranzaServImpView");
			s.forzarInicializarGCSI("GIRE");
			
			// Rapipago
			opcAIncluir = "/secure/puntoDeVenta/fcnsProductos/cobranzaServiciosEImpuestos.xhtml";
			auxiliarAIncluir = "/secure/puntoDeVenta/fcnsProductos/includeAuxiliaresCobServEImp.xhtml";
			tituloPanelProducto = "Rapipago";
			
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

	public void navegarFcnsAdministracion(Integer opcNav) {
		com.americacg.cargavirtual.web.model.Error e = new com.americacg.cargavirtual.web.model.Error();
		origen = "";
		switch (opcNav) {
		case 1: {
			opcAIncluir = "/secure/shared/administracion/cambioDeClave.xhtml";
			tituloPanelProducto = "Cambio de Clave";
			break;
		}
		case 2: {
			FacesContext facesContext = FacesContext.getCurrentInstance();
			InfDepSolicAdelView pv = (InfDepSolicAdelView) facesContext.getApplication().getELResolver()
					.getValue(facesContext.getELContext(), null, "infDepSolicAdelView");
			e = pv.onLoadInfDepYAdel();

			// Inf. Dep. y Sol. Adelantos
			opcAIncluir = "/secure/shared/administracion/infDepYAdel.xhtml";
			tituloPanelProducto = "Inf. Dep. y Sol. Adelantos";
			break;
		}
		case 3: {
			FacesContext facesContext = FacesContext.getCurrentInstance();
			AcreditSolicitudesBMRView pv = (AcreditSolicitudesBMRView) facesContext.getApplication().getELResolver()
					.getValue(facesContext.getELContext(), null, "acreditSolicitudesBMRView");
			pv.resetear();

			// Acredita Sol. BMR.
			opcAIncluir = "/secure/puntoDeVenta/fcnsAdministracion/acreditSolicitudesBMR.xhtml";
			tituloPanelProducto = "Acreditacion de Solicitudes BMR";
			auxiliarAIncluir = "";
			break;
		}
		case 4: {
			FacesContext facesContext = FacesContext.getCurrentInstance();
			SuperPagoView pv = (SuperPagoView) facesContext.getApplication().getELResolver()
					.getValue(facesContext.getELContext(), null, "superPagoView");
			pv.obtenerDatosComercioMEP();

			// Acredita Sol. BMR.
			opcAIncluir = "/secure/puntoDeVenta/fcnsAdministracion/superPagoAranceles.xhtml";
			tituloPanelProducto = "Seleccion de Aranceles y Cuentas";
			auxiliarAIncluir = "";
			break;
		}		
		default: {
			opcAIncluir = "";
			tituloPanelProducto = "";
		}
		}

		if (e.getHayError()) {
			opcAIncluir = "";
			auxiliarAIncluir = "";
			tituloPanelProducto = "";
			PrimeFaces.current().dialog()
					.showMessageDynamic(new FacesMessage(FacesMessage.SEVERITY_ERROR, "Reportes", e.getMsgError()));
		}
		PrimeFaces.current().ajax().update("opcAIncluir");
		PrimeFaces.current().executeScript("PF('panelProductosWV').show();");
	}

	public void navegarFcnsReimpresiones(Integer opcNav) {
		origen = "";
		switch (opcNav) {
		case 1: {
			// Recargas
			inicializarValores();

			opcAIncluir = "/secure/puntoDeVenta/fcnsReimpresiones/reimpresionRecargas.xhtml";
			tituloPanelProducto = "Reimpresion Productos Online";

			break;
		}
		case 2: {
			// Pines
			inicializarValores();

			opcAIncluir = "/secure/puntoDeVenta/fcnsReimpresiones/reimpresionPines.xhtml";
			tituloPanelProducto = "Reimpresion Productos Offline";
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

	public void navegarFcnsReportes(Integer opcNav) {

		origen = "REPORTES";
		auxiliarAIncluir = "/secure/shared/reportes/includeAuxiliaresReportes.xhtml";

		com.americacg.cargavirtual.web.model.Error e = new com.americacg.cargavirtual.web.model.Error();
		switch (opcNav) {
		case 1: {

			// Transacciones
			FacesContext facesContext = FacesContext.getCurrentInstance();
			ReporteTrxView pv = (ReporteTrxView) facesContext.getApplication().getELResolver()
					.getValue(facesContext.getELContext(), null, "reporteTrxView");

			e = pv.resetearReporte();
			opcAIncluir = "/secure/shared/reportes/informeTrx.xhtml";
			tituloPanelProducto = "Informe de Transacciones";

			break;
		}
		case 2: {
			// Movimientos

			FacesContext facesContext = FacesContext.getCurrentInstance();
			ReporteCuentaCorrienteView pv = (ReporteCuentaCorrienteView) facesContext.getApplication().getELResolver()
					.getValue(facesContext.getELContext(), null, "reporteCuentaCorrienteView");
			e = pv.resetearReporte();
			opcAIncluir = "/secure/shared/reportes/informeMovimientos.xhtml";
			tituloPanelProducto = "Informe de Movimientos";

			break;
		}
		case 3: {
			// Reparto de Saldos

			FacesContext facesContext = FacesContext.getCurrentInstance();
			ReporteRepartosView pv = (ReporteRepartosView) facesContext.getApplication().getELResolver()
					.getValue(facesContext.getELContext(), null, "reporteRepartosView");
			e = pv.resetearReporte();

			opcAIncluir = "/secure/shared/reportes/informeRepartos.xhtml";
			tituloPanelProducto = "Informe de Repartos";

			break;
		}
		case 4: {
			// Resumen TRXs

			FacesContext facesContext = FacesContext.getCurrentInstance();
			ReporteTrxRedView pv = (ReporteTrxRedView) facesContext.getApplication().getELResolver()
					.getValue(facesContext.getELContext(), null, "reporteTrxRedView");
			e = pv.resetearReporte();

			opcAIncluir = "/secure/shared/reportes/informeTrxRed.xhtml";
			tituloPanelProducto = "Resumen Transacciones";

			break;
		}
		case 5: {
			// Comisiones
			FacesContext facesContext = FacesContext.getCurrentInstance();
			ReporteComisionesView pv = (ReporteComisionesView) facesContext.getApplication().getELResolver()
					.getValue(facesContext.getELContext(), null, "reporteComisionesView");
			e = pv.resetearReporte();

			opcAIncluir = "/secure/shared/reportes/informeComisiones.xhtml";
			tituloPanelProducto = "Informe de Comisiones";

			break;
		}
		case 6: {
			// Depositos y Adelantos
			FacesContext facesContext = FacesContext.getCurrentInstance();
			ReporteDepAdelView pv = (ReporteDepAdelView) facesContext.getApplication().getELResolver()
					.getValue(facesContext.getELContext(), null, "reporteDepAdelView");
			e = pv.resetearReporte();

			opcAIncluir = "/secure/shared/reportes/informeDepAdel.xhtml";
			tituloPanelProducto = "Informe de Depositos y Adelantos";

			break;
		}
		case 7: {
			// Cuenta Corriente
			FacesContext facesContext = FacesContext.getCurrentInstance();
			ReporteCCNView pv = (ReporteCCNView) facesContext.getApplication().getELResolver()
					.getValue(facesContext.getELContext(), null, "reporteCCNView");
			e = pv.resetearReporte();

			opcAIncluir = "/secure/shared/reportes/informeCCN.xhtml";
			tituloPanelProducto = "Informe de Cuenta Corriente";

			break;
		}
		case 8: {
			// Rentabilidad
			FacesContext facesContext = FacesContext.getCurrentInstance();
			ReporteRentabilidadView pv = (ReporteRentabilidadView) facesContext.getApplication().getELResolver()
					.getValue(facesContext.getELContext(), null, "reporteRentabilidadView");
			e = pv.resetearReporte();

			opcAIncluir = "/secure/shared/reportes/informeRentabilidad.xhtml";
			tituloPanelProducto = "Informe de Rentabilidad";

			break;
		}
		case 9: {
			// Inf. Seguros
			FacesContext facesContext = FacesContext.getCurrentInstance();
			ReporteSegurosView pv = (ReporteSegurosView) facesContext.getApplication().getELResolver()
					.getValue(facesContext.getELContext(), null, "reporteSegurosView");
			e = pv.resetearReporte();

			opcAIncluir = "/secure/shared/reportes/informeSeguros.xhtml";
			tituloPanelProducto = "Informe de Seguros";

			break;
		}
		case 10: {
			// Comisiones vigentes
			FacesContext facesContext = FacesContext.getCurrentInstance();
			ReporteComisionesVigentesView pv = (ReporteComisionesVigentesView) facesContext.getApplication()
					.getELResolver().getValue(facesContext.getELContext(), null, "reporteComisionesVigentesView");
			e = pv.resetearReporte();

			opcAIncluir = "/secure/shared/reportes/informeComisionesVigentes.xhtml";
			tituloPanelProducto = "Informe de Comisiones Vigentes";

			break;
		}
		case 11: {
			// SUBE Transacciones
			FacesContext facesContext = FacesContext.getCurrentInstance();
			ReporteSubeTrxsView pv = (ReporteSubeTrxsView) facesContext.getApplication().getELResolver()
					.getValue(facesContext.getELContext(), null, "reporteSubeTrxsView");
			e = pv.resetearReporte();

			opcAIncluir = "/secure/shared/reportes/informeSubeTrxs.xhtml";
			tituloPanelProducto = "Informe de Transacciones de SUBE";

			break;
		}
		case 12: {
			// SUBE Lotes
			FacesContext facesContext = FacesContext.getCurrentInstance();
			ReporteSubeLotesView pv = (ReporteSubeLotesView) facesContext.getApplication().getELResolver()
					.getValue(facesContext.getELContext(), null, "reporteSubeLotesView");
			e = pv.resetearReporte();

			opcAIncluir = "/secure/shared/reportes/informeSubeLotes.xhtml";
			tituloPanelProducto = "Informe Lotes de SUBE";

			break;
		}
		case 13: {
			// Reparto Saldos WU

			FacesContext facesContext = FacesContext.getCurrentInstance();
			ReporteRepartosWUView pv = (ReporteRepartosWUView) facesContext.getApplication().getELResolver()
					.getValue(facesContext.getELContext(), null, "reporteRepartosWUView");
			e = pv.resetearReporte();

			opcAIncluir = "/secure/shared/reportes/informeRepartosWU.xhtml";
			tituloPanelProducto = "Informe de Repartos WU";

			break;
		}
		case 14: {
			// Informe PNet
			FacesContext facesContext = FacesContext.getCurrentInstance();
			ReportePNetACGView pv = (ReportePNetACGView) facesContext.getApplication().getELResolver()
					.getValue(facesContext.getELContext(), null, "reportePNetACGView");
			e = pv.resetearReporte();

			opcAIncluir = "/secure/shared/reportes/informePNet.xhtml";
			auxiliarAIncluir = "/secure/shared/reportes/includeAuxiliaresReportesPNet.xhtml";
			tituloPanelProducto = "Informe de PNet";

			break;

		}
		case 15: {
			// Listado de Pagos
			FacesContext facesContext = FacesContext.getCurrentInstance();
			ReportePagoElectronicoLIPAView pv = (ReportePagoElectronicoLIPAView) facesContext.getApplication()
					.getELResolver().getValue(facesContext.getELContext(), null, "reportePagoElectronicoLIPAView");
			e = pv.resetearReporte();

			opcAIncluir = "/secure/shared/reportes/informeLIPA.xhtml";
			//TODO: revisar a nivel de mayorista y distribuidor
			//auxiliarAIncluir = "/secure/shared/reportes/includeAuxiliaresReportesPNet.xhtml";
			tituloPanelProducto = "Listado de Cobros";

			break;

		}
		case 16: {
			// Listado de Anulaciones de un Pago
			FacesContext facesContext = FacesContext.getCurrentInstance();
			ReportePagoElectronicoLIAPView pv = (ReportePagoElectronicoLIAPView) facesContext.getApplication()
					.getELResolver().getValue(facesContext.getELContext(), null, "reportePagoElectronicoLIAPView");
			e = pv.resetearReporte();
			opcAIncluir = "/secure/shared/reportes/informeLIAP.xhtml";
			auxiliarAIncluir = "";
			tituloPanelProducto = "Listado de Anulaciones de un Pago";

			break;

		}		
		case 17: {
			// Informe GCSI - GIRE
			FacesContext facesContext = FacesContext.getCurrentInstance();
			ReporteGCSIACGView pv = (ReporteGCSIACGView) facesContext.getApplication().getELResolver()
					.getValue(facesContext.getELContext(), null, "reporteGCSIACGView");
			pv.inicializarReporte("GIRE");

			opcAIncluir = "/secure/shared/reportes/informeGCSI.xhtml";
			auxiliarAIncluir = "/secure/shared/reportes/includeAuxiliaresReportesGCSI.xhtml";
			tituloPanelProducto = "Informe de GCSI";

			break;

		}
		default: {
			opcAIncluir = "";
			tituloPanelProducto = "";
		}
		}

		if (e.getHayError()) {
			opcAIncluir = "/secure/puntoDeVenta/fcnsReportes/reportes.xhtml";
			auxiliarAIncluir = "/secure/shared/reportes/includeAuxiliaresReportes.xhtml";
			tituloPanelProducto = "";
			PrimeFaces.current().dialog()
					.showMessageDynamic(new FacesMessage(FacesMessage.SEVERITY_ERROR, "Reportes", e.getMsgError()));
		}
		PrimeFaces.current().ajax().update("opcAIncluir");
		PrimeFaces.current().executeScript("PF('panelProductosWV').show();");
	}
	
	public void navegarFcnsSuperPago(Integer opcNav, Cuenta cuenta) {

		origen = "SUPERPAGO";
		auxiliarAIncluir = "";

		com.americacg.cargavirtual.web.model.Error e = new com.americacg.cargavirtual.web.model.Error();
		switch (opcNav) {
		case 1: {

			// Operaciones
			
			this.getUsuario().getSuperPagoInstance().setCuentaSeleccionada(cuenta);
			this.getUsuario().getSuperPagoInstance().setOperacionesPendientes(false);
			
			FacesContext facesContext = FacesContext.getCurrentInstance();
			/*ReporteSuperPagoOperacionesView rspov = (ReporteSuperPagoOperacionesView) facesContext.getApplication().getELResolver()
					.getValue(facesContext.getELContext(), null, "reporteSuperPagoOperacionesView");*/
			SuperPagoView pv = (SuperPagoView) facesContext.getApplication().getELResolver()
					.getValue(facesContext.getELContext(), null, "superPagoView");

			pv.inicializarMovimientosDeCuenta(cuenta);
			opcAIncluir = "/secure/puntoDeVenta/fcnsProductos/superPagoMovimientosDeCuenta.xhtml";
			auxiliarAIncluir = "/secure/puntoDeVenta/fcnsProductos/includeAuxiliaresSuperPagoMovimientoDeCuenta.xhtml";
			tituloPanelProducto = "Movimientos de Cuenta";

			break;
		}
		case 2: {

			// CVU y Alias
			FacesContext facesContext = FacesContext.getCurrentInstance();
			SuperPagoView pv = (SuperPagoView) facesContext.getApplication().getELResolver()
					.getValue(facesContext.getELContext(), null, "superPagoView");
			pv.superPagoCBU(cuenta);
			this.getUsuario().getSuperPagoInstance().setCuentaSeleccionada(cuenta);
			opcAIncluir = "/secure/puntoDeVenta/fcnsProductos/superPagoCVUAlias.xhtml";
			tituloPanelProducto = "CVU y Alias";

			break;
		}
		case 3: {

			// Operaciones Pendientes de Acreditación
			
			this.getUsuario().getSuperPagoInstance().setCuentaSeleccionada(cuenta);
			this.getUsuario().getSuperPagoInstance().setOperacionesPendientes(true);
			
			FacesContext facesContext = FacesContext.getCurrentInstance();
			/*ReporteSuperPagoOperacionesView rspov = (ReporteSuperPagoOperacionesView) facesContext.getApplication().getELResolver()
					.getValue(facesContext.getELContext(), null, "reporteSuperPagoOperacionesView");
			
			rspov.realizarInforme();
			
			opcAIncluir = "/secure/puntoDeVenta/fcnsProductos/superPagoOperaciones.xhtml";
			auxiliarAIncluir = "/secure/shared/reportes/includeAuxiliaresReportes.xhtml";*/
			SuperPagoView pv = (SuperPagoView) facesContext.getApplication().getELResolver()
					.getValue(facesContext.getELContext(), null, "superPagoView");

			pv.inicializarMovimientosDeCuentaPendientes(cuenta);
			opcAIncluir = "/secure/puntoDeVenta/fcnsProductos/superPagoMovimientosDeCuenta.xhtml";
			auxiliarAIncluir = "/secure/puntoDeVenta/fcnsProductos/includeAuxiliaresSuperPagoMovimientoDeCuenta.xhtml";

			tituloPanelProducto = "Movimientos de Cuenta Pendientes de Acreditacion";

			break;
		}
		case 4: {

			// Aranceles
			FacesContext facesContext = FacesContext.getCurrentInstance();
			SuperPagoView pv = (SuperPagoView) facesContext.getApplication().getELResolver()
					.getValue(facesContext.getELContext(), null, "superPagoView");
			//pv.superPagoAranceles(cuenta);
			pv.obtenerDatosComercioMEP();
			this.getUsuario().getSuperPagoInstance().setCuentaSeleccionada(null);
			opcAIncluir = "/secure/puntoDeVenta/fcnsProductos/superPagoAranceles.xhtml";
			tituloPanelProducto = "Aranceles";

			break;
		}
		case 5: {

			// Agenda Destinatarios
			FacesContext facesContext = FacesContext.getCurrentInstance();
			SuperPagoView pv = (SuperPagoView) facesContext.getApplication().getELResolver()
					.getValue(facesContext.getELContext(), null, "superPagoView");
			pv.inicializarAgendaDestinatarios();
			this.getUsuario().getSuperPagoInstance().setCuentaSeleccionada(null);
			opcAIncluir = "/secure/puntoDeVenta/fcnsProductos/superPagoAgendaDestinatario.xhtml";
			auxiliarAIncluir = "/secure/puntoDeVenta/fcnsProductos/includeAuxiliaresSuperPagoAgendaDestinatario.xhtml";
			tituloPanelProducto = "Agenda de Cuentas";

			break;
		}
		case 6: {

			// Transferencias
			FacesContext facesContext = FacesContext.getCurrentInstance();
			SuperPagoView pv = (SuperPagoView) facesContext.getApplication().getELResolver()
					.getValue(facesContext.getELContext(), null, "superPagoView");
			this.getUsuario().getSuperPagoInstance().setCuentaSeleccionada(cuenta);
			pv.inicializarTransferencias(cuenta);
			opcAIncluir = "/secure/puntoDeVenta/fcnsProductos/superPagoTransferencias.xhtml";
			auxiliarAIncluir = "/secure/puntoDeVenta/fcnsProductos/includeAuxiliaresSuperPagoTransferencia.xhtml";
			tituloPanelProducto = "Transferencias";

			break;
		}
		case 7: {

			// Pedidos de dinero
			FacesContext facesContext = FacesContext.getCurrentInstance();
			SuperPagoView pv = (SuperPagoView) facesContext.getApplication().getELResolver()
					.getValue(facesContext.getELContext(), null, "superPagoView");
			this.getUsuario().getSuperPagoInstance().setCuentaSeleccionada(null);
			pv.inicializarRequerimientoDeDinero();
			opcAIncluir = "/secure/puntoDeVenta/fcnsProductos/superPagoSolicitudDeDinero.xhtml";
			auxiliarAIncluir = "/secure/puntoDeVenta/fcnsProductos/includeAuxiliaresSuperPagoSolicitudDeDinero.xhtml";
			tituloPanelProducto = "Solicitudes de dinero de clientes";

			break;
		}
		case 8: {

			// Solicitar dinero
			FacesContext facesContext = FacesContext.getCurrentInstance();
			SuperPagoView pv = (SuperPagoView) facesContext.getApplication().getELResolver()
					.getValue(facesContext.getELContext(), null, "superPagoView");
			this.getUsuario().getSuperPagoInstance().setCuentaSeleccionada(null);
			pv.inicializarSolicitudDeDinero(cuenta);
			opcAIncluir = "/secure/puntoDeVenta/fcnsProductos/superPagoSolicitudDeDinero.xhtml";
			auxiliarAIncluir = "/secure/puntoDeVenta/fcnsProductos/includeAuxiliaresSuperPagoSolicitudDeDinero.xhtml";
			tituloPanelProducto = "Solicitudes de dinero a clientes";

			break;
		}
		case 9: {
			// Deposito en cuenta 
			FacesContext facesContext = FacesContext.getCurrentInstance();
			SuperPagoView pv = (SuperPagoView) facesContext.getApplication().getELResolver()
					.getValue(facesContext.getELContext(), null, "superPagoView");
			this.getUsuario().getSuperPagoInstance().setCuentaSeleccionada(cuenta);
			pv.inicializarDepositoEnCta(cuenta);
			opcAIncluir = "/secure/puntoDeVenta/fcnsProductos/superPagoDepositoEnCuenta.xhtml";
			auxiliarAIncluir = "/secure/puntoDeVenta/fcnsProductos/includeAuxiliaresSuperPagoDepositoEnCuenta.xhtml";
			tituloPanelProducto = "Depositos en cuenta";
			
			break;
		}

		default: {
			opcAIncluir = "";
			tituloPanelProducto = "";
		}
		}

		if (e.getHayError()) {
			opcAIncluir = "/secure/puntoDeVenta/fcnsReportes/reportes.xhtml";
			auxiliarAIncluir = "/secure/shared/reportes/includeAuxiliaresReportes.xhtml";
			tituloPanelProducto = "";
			PrimeFaces.current().dialog()
					.showMessageDynamic(new FacesMessage(FacesMessage.SEVERITY_ERROR, "Reportes", e.getMsgError()));
		}
		PrimeFaces.current().ajax().update("opcAIncluir");
		PrimeFaces.current().executeScript("PF('panelProductosWV').show();");
	}

	public String getOpcAIncluir() {
		return opcAIncluir;
	}

	public void setOpcAIncluir(String opcAIncluir) {
		this.opcAIncluir = opcAIncluir;
	}

	public String getTituloPanelProducto() {
		return tituloPanelProducto;
	}

	public void setTituloPanelProducto(String tituloPanelProducto) {
		this.tituloPanelProducto = tituloPanelProducto;
	}

	public void actualizarValoresSUBE() {
		try {
			if ("P".equals(this.getUsuario().getTipoCliente())) {
				if (this.getUsuario().getMostrarPantallasDeSube().compareTo(0) > 0) {

					ObjectFactory factory = new ObjectFactory();

					String password = "";
					// TODO: obetener password de SUBE gateway. Ejemplo
					// String password =
					// "71a2a69d8f06b24ffba61bb78a08246a80308b2079cbade404f07e235145d72ad6be597f047757920dc6f4246023bfac";

					Long idProceso = 1L;

					this.getUsuario().setParametros(new ParametrosList());
					this.getUsuario().getParametros().setListParametros(
							factory.createParametrosListListParametros(factory.createArrayOfParametro()));

					JSONObject obj = new JSONObject();

					obj.put("idMayorista", String.valueOf(this.getUsuario().getIdMayorista()));
					obj.put("marca", "STRWeb");
					obj.put("idCliente", String.valueOf(this.getUsuario().getIdCliente()));
					obj.put("idUsuario", String.valueOf(this.getUsuario().getIdUsuario()));
					obj.put("usuario", this.getUsuario().getUsername());
					obj.put("tipoCliente", String.valueOf(this.getUsuario().getTipoCliente()));
					obj.put("clave", this.getUsuario().getPassword());

					ParametrosList lp = GestionServiceHelper.getGestionService(CfgTimeout.DEFAULT)
							.mostrarParametros(this.getUsuario().getIdMayorista(), "S", "gatewaySube_ip");
					if (lp != null && lp.getListParametros().getValue().getParametro() != null
							&& lp.getListParametros().getValue().getParametro().size() == 1) {
						obj.put("gatewaySube_ip",
								lp.getListParametros().getValue().getParametro().get(0).getValor().getValue());
					}

					lp = GestionServiceHelper.getGestionService(CfgTimeout.DEFAULT)
							.mostrarParametros(this.getUsuario().getIdMayorista(), "S", "gatewaySube_puerto");
					if (lp != null && lp.getListParametros().getValue().getParametro() != null
							&& lp.getListParametros().getValue().getParametro().size() == 1) {
						obj.put("gatewaySube_puerto",
								lp.getListParametros().getValue().getParametro().get(0).getValor().getValue());
					}

					lp = GestionServiceHelper.getGestionService(CfgTimeout.DEFAULT).mostrarParametros(
							this.getUsuario().getIdMayorista(), "S", "gatewaySube_timeOut_conexion_ms");
					if (lp != null && lp.getListParametros().getValue().getParametro() != null
							&& lp.getListParametros().getValue().getParametro().size() == 1) {
						obj.put("gatewaySube_timeOut_conexion_ms",
								lp.getListParametros().getValue().getParametro().get(0).getValor().getValue());
					}

					lp = GestionServiceHelper.getGestionService(CfgTimeout.DEFAULT)
							.mostrarParametros(this.getUsuario().getIdMayorista(), "S", "gatewaySube_timeOut_rw_ms");
					if (lp != null && lp.getListParametros().getValue().getParametro() != null
							&& lp.getListParametros().getValue().getParametro().size() == 1) {
						obj.put("gatewaySube_timeOut_rw_ms",
								lp.getListParametros().getValue().getParametro().get(0).getValor().getValue());
					}

					lp = GestionServiceHelper.getGestionService(CfgTimeout.DEFAULT)
							.mostrarParametros(this.getUsuario().getIdMayorista(), "S", "gatewaySube_dns");
					if (lp != null && lp.getListParametros().getValue().getParametro() != null
							&& lp.getListParametros().getValue().getParametro().size() == 1) {
						obj.put("gatewaySube_dns",
								lp.getListParametros().getValue().getParametro().get(0).getValor().getValue());
					}

					Parametro p = new Parametro();
					p.setVariable(factory.createParametroVariable("strWebURL"));

					lp = GestionServiceHelper.getGestionService(CfgTimeout.DEFAULT)
							.mostrarParametros(this.getUsuario().getIdMayorista(), "P", "strWebURL");
					if (lp != null && lp.getListParametros().getValue().getParametro() != null
							&& lp.getListParametros().getValue().getParametro().size() == 1) {
						obj.put("strWebURL",
								lp.getListParametros().getValue().getParametro().get(0).getValor().getValue());
						p.setValor(factory.createParametroValor(
								lp.getListParametros().getValue().getParametro().get(0).getValor().getValue()));
					}

					this.getUsuario().getParametros().getListParametros().getValue().getParametro().add(p);

					p = new Parametro();
					p.setVariable(factory.createParametroVariable("strWeb_ult_version_de_jnlp"));
					lp = GestionServiceHelper.getGestionService(CfgTimeout.DEFAULT)
							.mostrarParametros(this.getUsuario().getIdMayorista(), "P", "strWeb_ult_version_de_jnlp");
					if (lp != null && lp.getListParametros().getValue().getParametro() != null
							&& lp.getListParametros().getValue().getParametro().size() == 1) {
						obj.put("strWeb_ult_version_de_jnlp",
								lp.getListParametros().getValue().getParametro().get(0).getValor().getValue());
						p.setValor(factory.createParametroValor(
								lp.getListParametros().getValue().getParametro().get(0).getValor().getValue()));
					}
					this.getUsuario().getParametros().getListParametros().getValue().getParametro().add(p);

					p = new Parametro();
					p.setVariable(factory.createParametroVariable("debug"));
					/* USADO PARA HABILITAR LOG */
					obj.put("debug", (this.getUsuario().getHabilitarLog().compareTo(3) == 0 ? "true" : "false"));
					p.setValor(factory.createParametroValor(
							(this.getUsuario().getHabilitarLog().compareTo(3) == 0 ? "true" : "false")));
					this.getUsuario().getParametros().getListParametros().getValue().getParametro().add(p);

					ArrayOfCabeceraProducto rlCP = GestionServiceHelper.getGestionService(CfgTimeout.DEFAULT)
							.obtenerProductosHabilitadosParaOperar(this.getUsuario().getIdMayorista(),
									this.getUsuario().getIdCliente(), "ON");

					if ((rlCP != null) && (rlCP.getCabeceraProducto().size() > 0)) {

						for (CabeceraProducto cabeceraProducto : rlCP.getCabeceraProducto()) {

							if (cabeceraProducto.getIdTipoProducto().getValue().equals(4L)) {

								obj.put("IdProductoSUBEPlataforma",
										String.valueOf(cabeceraProducto.getIdProducto().getValue()));

								p = new Parametro();
								p.setVariable(factory.createParametroVariable("IdProductoSUBEPlataforma"));
								p.setValor(factory.createParametroValor(
										String.valueOf(cabeceraProducto.getIdProducto().getValue())));
								this.getUsuario().getParametros().getListParametros().getValue().getParametro().add(p);
							}
						}
					}

					HeaderInGateway headerIn = new HeaderInGateway();

					SeparadorTrama separadorTrama = new SeparadorTrama(CHR_INICIO_TRAMA, CHR_FIN_TRAMA,
							CHR_SEPARADOR_TRAMA, CHR_SEPARADOR_HEADER_DATA);

					String data = null;

					Date myDate = new Date();
					SimpleDateFormat mdyFormat = new SimpleDateFormat("YYYYMMDDHHMMSS");

					Random rand = new Random();

					headerIn.setCanal("WEBINTRA");
					headerIn.setClave(this.getUsuario().getPassword());
					headerIn.setFechaCliente(new Date());
					headerIn.setFuncion(FuncionName.GCAT);
					headerIn.setHashSecurity(".");
					headerIn.setIdCliente(this.getUsuario().getIdCliente());
					headerIn.setIdMayorista(this.getUsuario().getIdMayorista());
					headerIn.setIdTransaccionCliente(mdyFormat.format(myDate) + rand.nextInt(1000));
					headerIn.setIdUsuario(this.getUsuario().getIdUsuario());
					headerIn.setImeiPOS("");
					headerIn.setImeiSIM("");
					headerIn.setMarcaPOS("BROWSER");
					headerIn.setMedioConexion("L");
					headerIn.setModelo("BROWSER");
					// se pone fijo el valor 999-999-999 ya que no se puede identificar el Nro de
					// Serie del POS
					headerIn.setNroSeriePos("999-999-999");
					headerIn.setNroTelefonoDelSIM("");
					headerIn.setProveedor("ACG");
					headerIn.setTipoCliente(String.valueOf(this.getUsuario().getTipoCliente()));
					headerIn.setUsuario(this.getUsuario().getUsername());
					headerIn.setVersion("V1.0"); // version de la web o GENERAR
													// PARAMETRO NUEVO EN TABLA
													// DE CONFIGURACION ES LA
													// VERSION DE LA DLL
													// consultar
					headerIn.setVersionPOS("V1.0");

					FuncionGCAT fGCAT;
					try {

						fGCAT = new FuncionGCAT(idProceso, headerIn, separadorTrama, data);

						fGCAT.setIpGatewaySUBE("");
						lp = GestionServiceHelper.getGestionService(CfgTimeout.DEFAULT)
								.mostrarParametros(this.getUsuario().getIdMayorista(), "S", "gatewaySube_ip_intra");
						if (lp != null && lp.getListParametros().getValue().getParametro() != null
								&& lp.getListParametros().getValue().getParametro().size() == 1) {
							fGCAT.setIpGatewaySUBE(
									lp.getListParametros().getValue().getParametro().get(0).getValor().getValue());
						}

						fGCAT.setDnsGatewaySUBE("");
						lp = GestionServiceHelper.getGestionService(CfgTimeout.DEFAULT)
								.mostrarParametros(this.getUsuario().getIdMayorista(), "S", "gatewaySube_dns_intra");
						if (lp != null && lp.getListParametros().getValue().getParametro() != null
								&& lp.getListParametros().getValue().getParametro().size() == 1) {
							fGCAT.setDnsGatewaySUBE(
									lp.getListParametros().getValue().getParametro().get(0).getValor().getValue());
						}

						fGCAT.setPuertoGatewaySUBE(0);
						lp = GestionServiceHelper.getGestionService(CfgTimeout.DEFAULT)
								.mostrarParametros(this.getUsuario().getIdMayorista(), "S", "gatewaySube_puerto_intra");
						if (lp != null && lp.getListParametros().getValue().getParametro() != null
								&& lp.getListParametros().getValue().getParametro().size() == 1) {
							fGCAT.setPuertoGatewaySUBE(Integer.parseInt((String) lp.getListParametros().getValue()
									.getParametro().get(0).getValor().getValue()));
						}

						fGCAT.setTimeoutConnGatewaySUBE(
								Integer.parseInt((String) obj.get("gatewaySube_timeOut_conexion_ms")));
						fGCAT.setTimeoutRWGatewaySUBE(Integer.parseInt((String) obj.get("gatewaySube_timeOut_rw_ms")));
						MensajeOutboundGateway mo = fGCAT.ejecutar();

						// TODO: hacer que controle el error

						password = mo.getDataOutGatewaySUBE();
					} catch (WebServiceException ste) {
						if (ste != null && ste.getCause() != null && ste.getCause().getClass() != null) {
							if (ste.getCause().getClass().equals(ConnectException.class)) {
								LogACGHelper.escribirLog(null,
										"actualizarValoresSUBE: No se pudo establecer la comunicación (GST-TOC).");
							} else if (ste.getCause().getClass().equals(SocketTimeoutException.class)) {
								LogACGHelper.escribirLog(null,
										"actualizarValoresSUBE: No se pudo establecer la comunicación (GST-TRW).");
							} else if (ste.getCause().getClass().equals(UnknownHostException.class)) {
								LogACGHelper.escribirLog(null,
										"actualizarValoresSUBE: Error GCAT: |" + ste.getMessage() + "|");
							} else {
								LogACGHelper.escribirLog(null,
										"actualizarValoresSUBE: Error GCAT: |" + ste.getMessage() + "|");
							}
							password = "";
						} else {
							LogACGHelper.escribirLog(null,
									"actualizarValoresSUBE: Error GCAT:|" + ste.getMessage() + "|");
							password = "";
						}
					} catch (Exception e) {
						LogACGHelper.escribirLog(null, "actualizarValoresSUBE: Error GCAT: |" + e.getMessage() + "|");
						password = "";
					}
					obj.put("password", password);
					if (!"".equals(password)) {

						// seria como una clave privada
						byte[] baSalt = "'~7&amp;03~/.".getBytes();
						PBEParameterSpec pbeParamSpec = new PBEParameterSpec(baSalt, 128);
						PBEKeySpec keySpec = new PBEKeySpec(password.toCharArray(), baSalt, 2048, 128);
						SecretKeyFactory keyFac = SecretKeyFactory.getInstance("PBEWithSHA1AndDESede");
						SecretKey key = keyFac.generateSecret(keySpec);
						Cipher cipher = Cipher.getInstance("PBEWithSHA1AndDESede");
						cipher.init(Cipher.ENCRYPT_MODE, key, pbeParamSpec);
						byte[] encrypted = cipher.doFinal(obj.toString().getBytes("UTF-8"));
						p = new Parametro();
						p.setVariable(factory.createParametroVariable("token"));
						p.setValor(factory.createParametroValor(password.substring(0, 80)
								+ String.format("%02x", new BigInteger(1, encrypted)) + password.substring(80)));
						this.getUsuario().getParametros().getListParametros().getValue().getParametro().add(p);
					} else {
						p = new Parametro();
						p.setVariable(factory.createParametroVariable("token"));
						p.setValor(factory.createParametroValor(""));
						this.getUsuario().getParametros().getListParametros().getValue().getParametro().add(p);
					}
				}
			}
		} catch (WebServiceException ste) {
			if (ste != null && ste.getCause() != null && ste.getCause().getClass() != null) {
				if (ste.getCause().getClass().equals(ConnectException.class)) {
					LogACGHelper.escribirLog(null,
							"actualizarValoresSUBE: Error: |No se pudo establecer la comunicación (GST-TOC).\n Por favor intente nuevamente.|");
				} else if (ste.getCause().getClass().equals(SocketTimeoutException.class)) {
					LogACGHelper.escribirLog(null,
							"actualizarValoresSUBE: Error: |No se pudo establecer la comunicación (GST-TRW).\n Por favor intente nuevamente.|");
				} else if (ste.getCause().getClass().equals(UnknownHostException.class)) {
					LogACGHelper.escribirLog(null, "actualizarValoresSUBE: Error: |" + ste.getMessage() + "|");
				} else {
					LogACGHelper.escribirLog(null, "actualizarValoresSUBE: Error: |" + ste.getMessage() + "|");
				}
			} else {
				LogACGHelper.escribirLog(null, "actualizarValoresSUBE: Error: |" + ste.getMessage() + "|");
			}
		} catch (Exception e) {
			LogACGHelper.escribirLog(null, "actualizarValoresSUBE: Error: |" + e.getMessage() + "|");
		}
	}

	public void navegarFcnsPuntoDeVenta(Integer opcTab) {
		origen = "";
		FacesMessage msg = null;
		ArrayList<String> lstUpdate = new ArrayList<String>();

		switch (opcTab) {
		case 2: {
			opcAIncluir = "/secure/puntoDeVenta/fcnsReportes/reportes.xhtml";
			auxiliarAIncluir = "";
			tituloPanelProducto = "Reportes";
			break;
		}
		case 3: {
			this.publicidad = null;
			opcAIncluir = "/secure/shared/publicidad.xhtml";
			auxiliarAIncluir = "";
			tituloPanelProducto = "Publicidad";
			break;
		}
		default: {
			opcAIncluir = "";
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
	
	public boolean isForzarInicializarSube() {
		return forzarInicializarSube;
	}

	public void setForzarInicializarSube(boolean forzarInicializarSube) {
		this.forzarInicializarSube = forzarInicializarSube;
	}

	public void inicializadoOK() {
		forzarInicializarSube = false;
	}

	public String getAuxiliarAIncluir() {
		return auxiliarAIncluir;
	}

	public void setAuxiliarAIncluir(String auxiliarAIncluir) {
		this.auxiliarAIncluir = auxiliarAIncluir;
	}

	public void init() {
		this.opcAIncluir = "";
		this.auxiliarAIncluir = "";
		this.tituloPanelProducto = "";
		this.muestraMnuIconos = false;
		this.mnuOpcIconos = "";
		this.indiceMenu = -1;
		//inicializarValores();
		obtenerMenuPorDefault(this.getUsuario().getPvDefTab());
	}

	private void obtenerMenuPorDefault(String opcion) {
		origen = "";
		switch (opcion) {
		case "tabVentas":
			// TODO: OPTIMIZACION: Reemplazar este codigo a futuro, tiene que estar en la
			// parte de seguridad
			if (this.getUsuario().getHabilitarVirtual() == 1) {
				activaMenuIconos(0);

				// TODO: OPTIMIZACION: Reemplazar este codigo a futuro, tiene que estar en la
				// parte de seguridad
				if (this.getUsuario().getPermisosDeComponentes() != null && this.getUsuario().getPermisosDeComponentes().size() > 0 &&  this.getUsuario().getPermisosDeComponentes().containsKey("3admTabMenumiProductos") && this.getUsuario().getPermisosDeComponentes().get("3admTabMenumiProductos")
						&& this.getUsuario().getPermisosDeComponentes().get("3mnuIconoscbRecargas")
						&& this.getUsuario().getSoloInformes() == 0 && ((this.getUsuario().getIdTipoTerminal() == 1)
								|| (this.getUsuario().getPermitirVentasPorWeb()))) {
					navegarFcnsProductos(1);
				}
			}
			break;
		case "tabSube":
			// TODO: OPTIMIZACION: Reemplazar este codigo a futuro, tiene que estar en la
			// parte de seguridad
			if (this.getUsuario().getPermisosDeComponentes() != null && this.getUsuario().getPermisosDeComponentes().size() > 0  &&  this.getUsuario().getPermisosDeComponentes().containsKey("3admTabMenumiProductos") &&  this.getUsuario().getPermisosDeComponentes().get("3admTabMenumiProductos")
					&& this.getUsuario().getPermisosDeComponentes().get("3mnuIconoscbSube")
					&& this.getUsuario().getHabilitarVirtual() == 1) {
				activaMenuIconos(0);
				// TODO: OPTIMIZACION: Reemplazar este codigo a futuro, tiene que estar en la
				// parte de seguridad
				if (this.getUsuario().getMostrarPantallasDeSube() >= 1) {
					navegarFcnsProductos(4);
				}
			}
			break;
		case "tabPNet":
			// TODO: OPTIMIZACION: Reemplazar este codigo a futuro, tiene que estar en la
			// parte de seguridad
			if (this.getUsuario().getPermisosDeComponentes() != null && this.getUsuario().getPermisosDeComponentes().size() > 0  &&  this.getUsuario().getPermisosDeComponentes().containsKey("3admTabMenumiProductos") &&  this.getUsuario().getPermisosDeComponentes().get("3admTabMenumiProductos")
					&& this.getUsuario().getPermisosDeComponentes().get("3mnuIconoscbProvinciaNet")
					&& this.getUsuario().getHabilitarVirtual() == 1) {
				activaMenuIconos(0);
				// TODO: OPTIMIZACION: Reemplazar este codigo a futuro, tiene que estar en la
				// parte de seguridad

				FacesContext facesContext = FacesContext.getCurrentInstance();
				Object view = facesContext.getApplication().getELResolver().getValue(facesContext.getELContext(), null,
						"pNetView");
				if (view != null) {
					PNetView pNetView = (PNetView) view;
					if (pNetView.getMostrarPantallasDePNet() == 1) {
						navegarFcnsProductos(5);
					}
				}

			}
			break;

		/*
		 * //TODO: Ocultado por ahora hasta que se aplique en el sistema case
		 * "tabSeguros": //TODO: OPTIMIZACION: Reemplazar este codigo a futuro, tiene
		 * que estar en la parte de seguridad if(this.getUsuario().getHabilitarVirtual()
		 * == 1 ) { activaMenuIconos(0); //TODO: OPTIMIZACION: Reemplazar este codigo a
		 * futuro, tiene que estar en la parte de seguridad
		 * if(this.getUsuario().getHabilitarSeguros()) { navegarFcnsProductos(6); } }
		 * break;
		 */
		case "tabGestion":
			if (this.getUsuario().getPermisosDeComponentes() != null && this.getUsuario().getPermisosDeComponentes().size() > 0  &&  this.getUsuario().getPermisosDeComponentes().containsKey("3admTabMenumiGestion") &&  this.getUsuario().getPermisosDeComponentes().get("3admTabMenumiGestion")) {
				activaMenuIconos(1);
			}
			break;
		case "tabInformes":
			// TODO: OPTIMIZACION: Reemplazar este codigo a futuro, tiene que estar en la
			// parte de seguridad
			if (this.getUsuario().getPermisosDeComponentes() != null && this.getUsuario().getPermisosDeComponentes().size() > 0  &&  this.getUsuario().getPermisosDeComponentes().containsKey("3admTabMenumiReportes") &&  this.getUsuario().getPermisosDeComponentes().get("3admTabMenumiReportes")
					&& this.getUsuario().getHabilitarVirtual() == 1) {
				navegarFcnsPuntoDeVenta(2);
			}
			break;
		case "tabPublicidad":
			if (this.getUsuario().getPermisosDeComponentes() != null && this.getUsuario().getPermisosDeComponentes().size() > 0  &&  this.getUsuario().getPermisosDeComponentes().containsKey("3admTabMenumiPublicidad") &&  this.getUsuario().getPermisosDeComponentes().get("3admTabMenumiPublicidad")){
					//&& this.getUsuario().getHabilitarVirtual() == 1) {
				navegarFcnsPuntoDeVenta(3);
			}
			break;
		case "tabReimpresion":
			if (this.getUsuario().getPermisosDeComponentes() != null && this.getUsuario().getPermisosDeComponentes().size() > 0  &&  this.getUsuario().getPermisosDeComponentes().containsKey("3admTabMenumiReimpresionDeTickets") &&  this.getUsuario().getPermisosDeComponentes().get("3admTabMenumiReimpresionDeTickets")
					&& this.getUsuario().getHabilitarVirtual() == 1) {
				activaMenuIconos(4);
			}
			break;
		case "tabCuentas":
			// TODO: OPTIMIZACION: Reemplazar este codigo a futuro, tiene que estar en la
			// parte de seguridad
			//if (this.getUsuario().getHabilitarVirtual() == 1) {
				activaMenuIconos(0);
	
				if (this.getUsuario().getPermisosDeComponentes() != null && this.getUsuario().getPermisosDeComponentes().size() > 0 &&  this.getUsuario().getPermisosDeComponentes().containsKey("3admTabMenumiProductos") && this.getUsuario().getPermisosDeComponentes().get("3admTabMenumiProductos")
						&& this.getUsuario().getPermisosDeComponentes().get("3mnuIconoscbSuperPago")
						&& this.getUsuario().getSoloInformes() == 0 && ((this.getUsuario().getIdTipoTerminal() == 1)
								|| (this.getUsuario().getPermitirVentasPorWeb()))) {
					activaMenuIconos(0);
					navegarFcnsProductos(8);
				}
			//}
			break;
		}
	}

	public void onClose() {
		if ("REPORTES".equals(origen)) {
			navegarFcnsPuntoDeVenta(2);
		}else if ("SUPERPAGO".equals(origen)) {
			activaMenuIconos(0);
			navegarFcnsProductos(8);
			//PrimeFaces.current().executeScript("console.log('1a');ACGSiteScriptsFCNS.cierraPanelProducto();console.log('2a');");
			//PrimeFaces.current().executeScript("console.log('1');ACGSiteScriptsFCNS.productoResize();console.log('2');");
		}
	}

	public Integer getImpresionTickets() {
		try {
			if (this.impresionTickets == null) {
				Cookie c = CookieHelper.getCookie("ImpresionTicketOnline");
				if (c != null && c.getValue() != null) {
					this.impresionTickets = Integer.parseInt(c.getValue());
				} else {
					this.impresionTickets = 1;
				}
			}
		} catch (Exception e) {
			LogACGHelper.escribirLog(null, "Error getImpresionTickets: |" + e.getMessage() + "|");
			cambiarImpresionTickets(1);
		}

		return impresionTickets;
	}

	public void setImpresionTickets(Integer impresionTickets) {
		this.impresionTickets = impresionTickets;
	}

	public void cambiarImpresionTickets(Integer valor) {
		try {
			int tiempo = 60 * 60 * 24 * 365;
			this.impresionTickets = valor;
			CookieHelper.setCookie("ImpresionTicketOnline", valor.toString(), tiempo);

		} catch (Exception e) {
			LogACGHelper.escribirLog(null, "Error cambiarImpresionTickets: |" + e.getMessage() + "|");
		}

		PrimeFaces.current().executeScript("PF('blockPanelGral').hide()");
	}

	public String getCodMnemonicoTipoProductoOn() {
		return codMnemonicoTipoProductoOn;
	}

	public void setCodMnemonicoTipoProductoOn(String codMnemonicoTipoProductoOn) {
		this.codMnemonicoTipoProductoOn = codMnemonicoTipoProductoOn;
	}

	public String getCodMnemonicoProductoOn() {
		return codMnemonicoProductoOn;
	}

	public void setCodMnemonicoProductoOn(String codMnemonicoProductoOn) {
		this.codMnemonicoProductoOn = codMnemonicoProductoOn;
	}

	public String getCodMnemonicoTipoProductoOff() {
		return codMnemonicoTipoProductoOff;
	}

	public void setCodMnemonicoTipoProductoOff(String codMnemonicoTipoProductoOff) {
		this.codMnemonicoTipoProductoOff = codMnemonicoTipoProductoOff;
	}

	public String getCodMnemonicoProductoOff() {
		return codMnemonicoProductoOff;
	}

	public void setCodMnemonicoProductoOff(String codMnemonicoProductoOff) {
		this.codMnemonicoProductoOff = codMnemonicoProductoOff;
	}

	public String getIcono_menu_cuentas() {
		
		if (icono_menu_cuentas == null || "".equals(icono_menu_cuentas)){
			ParametrosList lp = GestionServiceHelper.getGestionService(CfgTimeout.DEFAULT).mostrarParametros(this.getUsuario().getIdMayorista(), "P", "icono_menu_cuentas");
			if (lp != null && lp.getListParametros() != null && lp.getListParametros().getValue().getParametro().size() == 1) {
				icono_menu_cuentas = lp.getListParametros().getValue().getParametro().get(0).getValor().getValue();
			}
		}

		return icono_menu_cuentas;
	}

	public void setIcono_menu_cuentas(String icono_menu_cuentas) {
		this.icono_menu_cuentas = icono_menu_cuentas;
	}
}
