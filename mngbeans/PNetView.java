package com.americacg.cargavirtual.web.mngbeans;

import java.io.OutputStream;
import java.io.Serializable;
import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.xml.ws.WebServiceException;
import org.primefaces.PrimeFaces;
import org.springframework.beans.BeanUtils;
import org.springframework.context.annotation.Scope;
import com.americacg.cargavirtual.gestion.model.DatosPlataforma;
import com.americacg.cargavirtual.gestion.model.RespString;
import com.americacg.cargavirtual.pnet.message.respuesta.Ingreso;
import com.americacg.cargavirtual.pnet.model.Lote;
import com.americacg.cargavirtual.pnet.model.Lote.ListaFacturas;
import com.americacg.cargavirtual.web.enums.CfgTimeout;
import com.americacg.cargavirtual.web.helpers.GestionServiceHelper;
import com.americacg.cargavirtual.web.helpers.LogACGHelper;
import com.americacg.cargavirtual.web.helpers.PNetServiceHelper;
import com.americacg.cargavirtual.web.model.Factura;
import com.americacg.cargavirtual.web.shared.BasePage;
import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Font.FontFamily;
import com.itextpdf.text.pdf.PdfWriter;

@Named("pNetView")
@Scope(org.springframework.web.context.WebApplicationContext.SCOPE_SESSION)
public class PNetView extends BasePage implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5729352286486305129L;

	public enum MethodType {
		XCB, TRX, ANU, INF, EST, REI, RIT, INA, CIE, APE, CTP, CLS, EXP, GLC;
	}

	// @Inject
	// private HtmlToPDF exportToPDF;

	private boolean inicializadoPNet = false;

	private String contenido = "";
	private String menuLateralTop = "";
	private String menuLateralBottom = "";
	private String ticket = "";
	private Integer terminalAbierta = 0;

	private Integer mostrarPantallasDePNet = -1;

	private List<Factura> facturas = new ArrayList<Factura>();
	// private Lote lote = new lote();

	private Float totalAPagar = 0F;
	private Float totalPagado = 0F;
	private Float confirmarMonto = null;
	private Float confirmarVueltoAEntregar = 0F;
	private String confirmarVueltoAEntregarColor = "white";
	private String confirmarVueltoAEntregarBackgroundColor = "red";

	private Integer cantidadFacturas = 0;

	private String transaccionDatos = "";
	private String cerrarModal = "mpVisualizar";
	private String modalPanelTitulo = "";
	private Lote lote = null;
	private Boolean loteTotalmenteAprobado = false;

	private Factura factura = new Factura();
	private Long idTurnoCaja = null;

	private boolean confirmarHabilitado;
	private boolean borrarHabilitado;
	private boolean imprimirTodos;

	private boolean showBtnManual = false;
	private boolean showBtnBarra = false;
	private boolean showBtnInforme = false;
	private boolean showBtnApertura = false;
	private boolean showBtnCierre = false;
	
	private Factura selectedFactura;

	public void recalcularTotales() {
		totalAPagar = 0F;
		cantidadFacturas = 0;

		for (Factura factura : facturas) {
			if (factura.isIncluir()) {
				if(factura.getEstado() == null || factura.getEstado().getId() == 3 || factura.getEstado().getId() == 4) {
					totalAPagar = totalAPagar + factura.getImpAbonado();
					cantidadFacturas += 1;
				}
			}
		}
	}

	public void forzarInicializarPNet() {
		try {
			if (!inicializadoPNet) {
				invocarPNet("est", "", "", "");
			}
		} catch (Exception e) {
			LogACGHelper.escribirLog(null, e.getMessage());
			terminalAbierta = 0; // con error
		}
	}

	private void limpiarPantalla() {
		this.cerrarModal = "mpVisualizar";
		this.lote = new Lote();
		this.facturas.clear();
		this.cantidadFacturas = 0;
		this.totalAPagar = 0F;
		this.loteTotalmenteAprobado = false;
		this.confirmarMonto = null;
		this.confirmarVueltoAEntregar = 0f;
		this.confirmarVueltoAEntregarColor = "white";
		this.confirmarVueltoAEntregarBackgroundColor = "red";	
	}

	private void obtenerMenu() {

		menuLateralTop = "";
		menuLateralBottom = "";

		switch (terminalAbierta) {
		case 0:
			// menuLateralBottom += "<button type=\"button\"
			// onclick=\"callScript('est','','','');\" style=\"width: 180px;\">Reconsultar
			// Estado</button>";
			menuLateralBottom += "<button type=\"button\" class=\"ui-button ui-widget ui-state-default ui-corner-all ui-button-text-only\" onclick=\"callScript('est','','','');\" style=\"width: 180px;\">APERTURA</button>";
			this.showBtnApertura = true;
			this.showBtnCierre = false;
			break;
		case 1:
			menuLateralBottom += "<button type=\"button\" class=\"ui-button ui-widget ui-state-default ui-corner-all ui-button-text-only\" onclick=\"callScript('cie','','','');\" style=\"width: 180px;\">CIERRE</button>";
			this.showBtnApertura = false;
			this.showBtnCierre = true;
			break;
		case 2:
			menuLateralBottom += "<button type=\"button\" class=\"ui-button ui-widget ui-state-default ui-corner-all ui-button-text-only\" onclick=\"callScript('ape','','','');return false;\" style=\"width: 180px;\">APERTURA</button>";
			this.showBtnApertura = true;
			this.showBtnCierre = false;
			break;
		}
		switch (terminalAbierta) {
		case 1:
			this.showBtnManual = true;
			this.showBtnBarra = true;
			this.showBtnInforme = true;

			/*
			 * menuLateralTop +=
			 * "<button type=\"button\" class=\"ui-button ui-widget ui-state-default ui-corner-all ui-button-text-only\" name=\"btnManual\" id=\"btnManual\" onclick=\"callScript('trx','MANUAL','','');\" style=\"width: 180px;\">PAGO SIN FACTURA (F7)</button>"
			 * ; menuLateralTop +=
			 * "<button type=\"button\" class=\"ui-button ui-widget ui-state-default ui-corner-all ui-button-text-only\" name=\"btnBarra\" id=\"btnBarra\" onclick=\"callScript('xcb','','','');\" style=\"width: 180px;\">PAGO BARRA (F8)</button>"
			 * ; menuLateralTop +=
			 * "<button type=\"button\" class=\"ui-button ui-widget ui-state-default ui-corner-all ui-button-text-only\" name=\"btnInforme\" id=\"btnInforme\" onclick=\"PF('panelInfPNet').show();return false;\" style=\"width: 180px;\">INFORME</button>"
			 * ;
			 */
			break;
		default:
			this.showBtnManual = false;
			this.showBtnBarra = false;
			this.showBtnInforme = false;
		}

		switch (terminalAbierta) {
		case 1:
			// menu += "<button type=\"button\"
			// onclick=\"callScript('ina','','','');\"
			// style=\"width: 180px;\">Informe Analitico</button>";
			break;
		}

	}

	public String getContenido() {
		return contenido;
	}

	public void setContenido(String contenido) {
		this.contenido = contenido;
	}

	public void invocarPNet() {
		Map<String, String> params = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
		String parametro = params.get("parametro");
		String tipo = params.get("tipo");
		String valor = params.get("valor");
		String texto = params.get("texto");
		invocarPNet(parametro, tipo, valor, texto);
	}

	public void invocarPNet(String parametro, String tipo, String valor, String texto) {
		this.ticket = "";
		this.contenido = "";
		DatosPlataforma dp = null;
		boolean cerrarPantallas = false;
		boolean facturaRepetida = false;

		// TODO: Pasar dp a la inicializacion de PNet
		// Busco los DatosPlataforma
		try {
			dp = GestionServiceHelper.getGestionService(CfgTimeout.DEFAULT)
					.mostrarDatosPlataforma(this.getUsuario().getIdMayorista());
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
							"Invocar PNet. Error ejecutando el WS de datosPlataforma: |" + ste.getMessage() + "|");
					FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
							"Error ejecutando el WS de datosPlataforma: |" + ste.getMessage() + "|", null));
				}
			} else {
				LogACGHelper.escribirLog(null,
						"Invocar PNet. Error ejecutando el WS de consulta: |" + ste.getMessage() + "|");
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
						"Error ejecutando el WS de datosPlataforma: |" + ste.getMessage() + "|", null));
			}
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR, "No se encontraron datosPlataforma", null));
			return;
		} catch (Exception e) {
			LogACGHelper.escribirLog(null,
					"Invocar PNet. Excepcion ejecutando el WS de datosPlataforma: |" + e.getMessage() + "|");
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
					"Error ejecutando el WS de consulta de datosPlataforma: |" + e.getMessage() + "|", null));
			FacesContext.getCurrentInstance().validationFailed();
			return;
		}

		try {
			if (dp == null) {
				FacesContext.getCurrentInstance().addMessage(null,
						new FacesMessage(FacesMessage.SEVERITY_ERROR, "No se encontraron datosPlataforma", null));
				return;
			} else {

				String sClave = this.getUsuario().getPassword();
				String urlw = dp.getPnetTipoConexionSinSSL().getValue() + "://" + dp.getPnetServidorSinSSL().getValue()
						+ ":" + dp.getPnetPuertoSinSSL().getValue().toString() + dp.getPnetUrlSinSSL().getValue();

				switch (MethodType.valueOf(parametro.toUpperCase())) {
				case XCB:

					if ((this.loteTotalmenteAprobado) && (this.facturas.size() > 0)) {
						limpiarPantalla();
					}

					factura = new Factura();
					factura.setIncluir(true);
					factura.setSecuencia(0);
					factura.setListaParametros(new com.americacg.cargavirtual.pnet.model.Factura.ListaParametros());

					this.cerrarModal = "mpVisualizar";
					this.modalPanelTitulo = "PAGO CON CODIGO DE BARRAS";
					transaccionDatos = "<h2>Usted esta cobrando:</h2>";
					tipo = "BARRA";
					contenido = "<h4>Ingrese codigo de Barras</h4>";
					// contenido += "<p>Ingrese codigo de Barras</p>";
					//type=\"number\" GONZA
					contenido += "<input value=\"\" name=\"codigoDeBarra\" id=\"codigoDeBarra\"  style=\"border-radius: 5px;padding: 5px;width: 99%;margin-bottom: 5px;\" autofocus "
							+ " onkeydown='if(event.keyCode === 38 || event.keyCode === 40) {event.preventDefault();}'"
							+ " />";
					contenido += "<button type=\"button\" accesskey=\"f2\" class=\"ui-button ui-widget ui-state-default ui-corner-all ui-button-text-only\" name=\"btnCONTINUAR\" id=\"btnCONTINUAR\" onclick=\"callScript([{name:'parametro', value:'trx'}, {name:'tipo', value:'BARRA'}, {name:'valor', value: document.getElementById('codigoDeBarra').value}, {name:'texto', value: ''}])\"><span class=\"ui-button-text ui-c\">CONTINUAR (F2)</span></button>";
					// contenido += "<button type=\"button\"
					// onclick=\"callScript('','','','');\">DESCARTAR</button>";
					this.cerrarModal = "mpMostrar";
					break;
				case TRX:
					com.americacg.cargavirtual.pnet.message.solicitud.ValidarTransaccion stXCB = new com.americacg.cargavirtual.pnet.message.solicitud.ValidarTransaccion();
					if ("BARRA".equals(tipo.toUpperCase())) {
						if ((this.loteTotalmenteAprobado) && (this.facturas.size() > 0)) {
							limpiarPantalla();
						}
						factura = new Factura();
						factura.setIncluir(true);
						factura.setSecuencia(0);
						factura.setListaParametros(new com.americacg.cargavirtual.pnet.model.Factura.ListaParametros());
						this.modalPanelTitulo = "PAGO CON CODIGO DE BARRAS";
						transaccionDatos = "<h2>Usted esta cobrando:</h2>";
						this.cerrarModal = "mpMostrar";
					} else if ("MANUAL".equals(tipo.toUpperCase())) {
						if ((this.loteTotalmenteAprobado) && (this.facturas.size() > 0)) {
							limpiarPantalla();
						}
						factura = new Factura();
						factura.setIncluir(true);
						factura.setSecuencia(0);
						factura.setListaParametros(new com.americacg.cargavirtual.pnet.model.Factura.ListaParametros());
						this.modalPanelTitulo = "PAGO MANUAL SIN FACTURA";
						transaccionDatos = "<h2>Usted esta cobrando:</h2>";
						this.cerrarModal = "mpMostrar";
					} else if ("BOTON".equals(tipo.toUpperCase()) && ("DESC".equals(valor.toUpperCase()))) {
						factura = new Factura();
						factura.setIncluir(true);
						factura.setSecuencia(0);
						factura.setListaParametros(new com.americacg.cargavirtual.pnet.model.Factura.ListaParametros());
						transaccionDatos = "";
						this.cerrarModal = "mpVisualizar";
						obtenerMenu();
						break;
					}
					// REIMPRESION
					stXCB.setIdMayorista(this.getUsuario().getIdMayorista());
					stXCB.setUsuario(this.getUsuario().getUsername());
					stXCB.setPwd(sClave);
					stXCB.setSecuencia(String.valueOf(factura.getSecuencia()));
					stXCB.setIngresoTipo(tipo);
					stXCB.setIngresoValor(valor);
					stXCB.setIdTurnoCaja(this.idTurnoCaja);

					if (!"BOTON".equals(tipo.toUpperCase()) && !"DESC".equals(tipo.toUpperCase())) {
						com.americacg.cargavirtual.pnet.model.ParametroConsulta pc = new com.americacg.cargavirtual.pnet.model.ParametroConsulta();
						pc.setOrdinal(factura.getListaParametros().getParametro().size() + 1);
						pc.setDescripcion(tipo);
						pc.setValor(valor);
						factura.getListaParametros().getParametro().add(pc);
					}

					transaccionDatos += "<p>" + texto + "</p>";

					com.americacg.cargavirtual.pnet.message.respuesta.ValidarTransaccion rtXCB = PNetServiceHelper
							.getGatewayPNet(urlw).validarTransaccion(stXCB);
					if (rtXCB == null) {
						FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
								"La consulta de Facturas devolvio null", null));
					} else if (!"P0000".equals(rtXCB.getErrorRb().getCodigo())) {
						contenido = "<p>" + rtXCB.getErrorRb().getMensaje() + "</p>";
						// contenido += "<button type=\"button\"
						// onclick=\"callScript('','','','');\">DESCARTAR</button>";
					} else if ("P0000".equals(rtXCB.getErrorRb().getCodigo())) {
						contenido = "";
						if ("0".equals(rtXCB.getEstado())) {
							if ((rtXCB.getNumeroUnicoSiris() != null) && (!"".equals(rtXCB.getNumeroUnicoSiris()))) {
								factura.setNumeroUnicoSiris(Long.parseLong(rtXCB.getNumeroUnicoSiris()));
								for (com.americacg.cargavirtual.pnet.message.respuesta.Ingreso ing : rtXCB.getIngresos()
										.getIngreso()) {
									if ("PAGO".equals(ing.getTipo().toUpperCase())) {
										transaccionDatos = "";
										/*
										 * Factura f = new Factura(); f.setConfirmar(true);
										 * 
										 * f.setDescripcion(descripcionFactura);
										 * f.setImporte(Float.parseFloat(ing.getValor()));
										 */

										if (this.lote == null) {
											this.lote = new Lote();
										}
										
										factura.setImpAbonado(Float.parseFloat(ing.getValor()));
										
										facturaRepetida = false;
										
										for (Factura f : this.facturas) {
											if((f.getCodBarra1() != null && factura.getCodBarra1() != null && f.getCodBarra1().equals(factura.getCodBarra1() ))
													//&& (f.getCodBarra2() != null && factura.getCodBarra2() != null && f.getCodBarra2().equals(factura.getCodBarra2()))
													&& (f.getDescripcion() != null  && factura.getDescripcion() != null && f.getDescripcion().equals(factura.getDescripcion()))
													&& (f.getIdentificador1() != null && factura.getIdentificador1() != null && f.getIdentificador1().equals(factura.getIdentificador1()))
													&& (f.getIdentificador2() != null && factura.getIdentificador2() != null && f.getIdentificador2().equals(factura.getIdentificador2()))
													&& (f.getIdentificador3() != null && factura.getIdentificador3() != null && f.getIdentificador3().equals(factura.getIdentificador3()))
													&& (f.getIdentificadorServicio() != null && factura.getIdentificadorServicio() != null && f.getIdentificadorServicio().equals(factura.getIdentificadorServicio()))
													) {

											/*if((f.getCodBarra1() == null || (f.getCodBarra1() == null && factura.getCodBarra1() == null) || f.getCodBarra1().equals(factura.getCodBarra1() ))
													&& (f.getCodBarra2() != null  || (f.getCodBarra2() == null && factura.getCodBarra2() == null) ||  f.getCodBarra2().equals(factura.getCodBarra2()))
													&& (f.getDescripcion() != null  || (f.getDescripcion() == null && factura.getDescripcion() == null) ||  f.getDescripcion().equals(factura.getDescripcion()))
													&& (f.getIdentificador1() != null  || (f.getIdentificador1() == null && factura.getIdentificador1() == null) ||  f.getIdentificador1().equals(factura.getIdentificador1()))
													&& (f.getIdentificador2() != null  || (f.getIdentificador2() == null && factura.getIdentificador2() == null) ||  f.getIdentificador2().equals(factura.getIdentificador2()))
													&& (f.getIdentificador3() != null  || (f.getIdentificador3() == null && factura.getIdentificador3() == null) ||  f.getIdentificador3().equals(factura.getIdentificador3()))
													&& (f.getIdentificadorServicio() != null  || (f.getIdentificadorServicio() == null && factura.getIdentificadorServicio() == null) ||  f.getIdentificadorServicio().equals(factura.getIdentificadorServicio()))
													) {*/
												facturaRepetida = true;
												break;
											}
										}

										if(facturaRepetida) {
											FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
													"No se pemite agregar facturas repetidas.", null));
										}else {
											
											com.americacg.cargavirtual.pnet.message.solicitud.VerificarEstadoFactura stVEF = new com.americacg.cargavirtual.pnet.message.solicitud.VerificarEstadoFactura();
											stVEF.setIdMayorista(this.getUsuario().getIdMayorista());
											stVEF.setUsuario(this.getUsuario().getUsername());
											stVEF.setPwd(sClave);
											stVEF.setIdTurnoCaja(this.idTurnoCaja);
											stVEF.setFactura(factura);
											
											com.americacg.cargavirtual.pnet.message.respuesta.VerificarEstadoFactura rtVEF = PNetServiceHelper
													.getGatewayPNet(urlw).verificarEstadoFactura(stVEF);
											if (rtVEF == null) {
												FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
														"La verificación de estado de Facturas devolvio null", null));
											} else if (!"P0000".equals(rtVEF.getErrorRb().getCodigo())) {
												FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
														rtVEF.getErrorRb().getMensaje(), null));
											} else if ("P0000".equals(rtXCB.getErrorRb().getCodigo())) {
												//procede en modo normal
												totalAPagar = totalAPagar + factura.getImpAbonado();
												this.facturas.add(factura);

											}
										}										
										
										factura = new Factura();
										factura.setIncluir(true);
										factura.setSecuencia(0);
										factura.setListaParametros(
												new com.americacg.cargavirtual.pnet.model.Factura.ListaParametros());
										this.cerrarModal = "mpCerrar";
										this.cantidadFacturas = this.facturas.size();
										facturaRepetida = false;
									}
								}

								// contenido = "<p>Factura Pendiente de confirmación</p>";
								// contenido += "<button type=\"button\"
								// onclick=\"callScript('','','','');\">DESCARTAR</button>";
							} else {

								factura.setSecuencia(Integer.parseInt(rtXCB.getSecuencia()));

								boolean enLista = false;
								for (com.americacg.cargavirtual.pnet.message.respuesta.Ingreso ing : rtXCB.getIngresos()
										.getIngreso()) {
									if ("LISTA".equals(ing.getTipo().toUpperCase())) {
										if (!enLista) {
											contenido += "<h4>" + ing.getTexto() + "</h4>";
											contenido += "<select name=\"selectValor\" id=\"selectValor\" style=\"border-radius: 5px;padding: 5px;width: 99%;margin-bottom: 5px;\"  autofocus>";
											enLista = true;
										}
										contenido += "<option value=\"" + ing.getValor() + "\">" + ing.getTexto()
												+ "</option>";
									} else if ("BOTON".equals(ing.getTipo().toUpperCase())) {
										if (enLista) {
											contenido += "</select>";
											contenido += "<button type=\"button\" accesskey=\"f2\" class=\"ui-button ui-widget ui-state-default ui-corner-all ui-button-text-only ui-button ui-widget ui-state-default ui-corner-all ui-button-text-only\" name=\"btnCONTINUAR\" id=\"btnCONTINUAR\" onclick=\"callScript([{name:'parametro', value:'trx'}, {name:'tipo', value:'LISTA'}, {name:'valor', value:document.getElementById('selectValor').value}, {name:'texto', value: document.getElementById('selectValor').options[document.getElementById('selectValor').selectedIndex].text}])\"><span class=\"ui-button-text ui-c\">CONTINUAR (F2)</span></button>";
											enLista = false;
										}
										if ("DESC".equals(ing.getValor().toUpperCase())) {
										} else if ("CONF".equals(ing.getValor().toUpperCase())) {
											contenido += "<button type=\"button\" accesskey=\"f2\" class=\"ui-button ui-widget ui-state-default ui-corner-all ui-button-text-only ui-button ui-widget ui-state-default ui-corner-all ui-button-text-only\" name=\"btnCONTINUAR\" id=\"btnCONTINUAR\" onclick=\"callScript([{name:'parametro', value:'trx'}, {name:'tipo', value:'"
													+ ing.getTipo() + "'}, {name:'valor', value: '" + ing.getValor()
													+ "'}, {name:'texto', value: ''}])\"><span class=\"ui-button-text ui-c\">"
													+ ing.getTexto() + " (F2)</span></button>";
										} else {
											contenido += "<button type=\"button\"  class=\"ui-button ui-widget ui-state-default ui-corner-all ui-button-text-only ui-button ui-widget ui-state-default ui-corner-all ui-button-text-only\" onclick=\"callScript('trx', '"
													+ ing.getTipo() + "','" + ing.getValor()
													+ "','')\"><span class=\"ui-button-text ui-c\">" + ing.getTexto()
													+ "</span></button>";
										}
									} else if ("PAGO".equals(ing.getTipo().toUpperCase())) {
										if (enLista) {
											contenido += "</select>";
											contenido += "<button type=\"button\" accesskey=\"f2\" class=\"ui-button ui-widget ui-state-default ui-corner-all ui-button-text-only ui-button ui-widget ui-state-default ui-corner-all ui-button-text-only\" name=\"btnCONTINUAR\" id=\"btnCONTINUAR\" onclick=\"callScript([{name:'parametro', value:'trx'}, {name:'tipo', value:'LISTA'}, {name:'valor', value:document.getElementById('selectValor').value}, {name:'texto', value: document.getElementById('selectValor').options[document.getElementById('selectValor').selectedIndex].text}])\"><span class=\"ui-button-text ui-c\">CONTINUAR (F2)</span></button>";
											enLista = false;
										}
										// Factura f = new Factura();

										// f.setConfirmar(true);
										// f.setImporte(Float.parseFloat(ing.getValor()));
										if (this.lote == null) {
											this.lote = new Lote();
										}

										facturaRepetida = false;
										
										for (Factura f : this.facturas) {
											if((f.getCodBarra1() != null && factura.getCodBarra1() != null && f.getCodBarra1().equals(factura.getCodBarra1() ))
													//&& (f.getCodBarra2() != null && factura.getCodBarra2() != null && f.getCodBarra2().equals(factura.getCodBarra2()))
													&& (f.getDescripcion() != null  && factura.getDescripcion() != null && f.getDescripcion().equals(factura.getDescripcion()))
													&& (f.getIdentificador1() != null && factura.getIdentificador1() != null && f.getIdentificador1().equals(factura.getIdentificador1()))
													&& (f.getIdentificador2() != null && factura.getIdentificador2() != null && f.getIdentificador2().equals(factura.getIdentificador2()))
													&& (f.getIdentificador3() != null && factura.getIdentificador3() != null && f.getIdentificador3().equals(factura.getIdentificador3()))
													&& (f.getIdentificadorServicio() != null && factura.getIdentificadorServicio() != null && f.getIdentificadorServicio().equals(factura.getIdentificadorServicio()))
													) {

											/*if((f.getCodBarra1() == null || (f.getCodBarra1() == null && factura.getCodBarra1() == null) || f.getCodBarra1().equals(factura.getCodBarra1() ))
													&& (f.getCodBarra2() != null  || (f.getCodBarra2() == null && factura.getCodBarra2() == null) ||  f.getCodBarra2().equals(factura.getCodBarra2()))
													&& (f.getDescripcion() != null  || (f.getDescripcion() == null && factura.getDescripcion() == null) ||  f.getDescripcion().equals(factura.getDescripcion()))
													&& (f.getIdentificador1() != null  || (f.getIdentificador1() == null && factura.getIdentificador1() == null) ||  f.getIdentificador1().equals(factura.getIdentificador1()))
													&& (f.getIdentificador2() != null  || (f.getIdentificador2() == null && factura.getIdentificador2() == null) ||  f.getIdentificador2().equals(factura.getIdentificador2()))
													&& (f.getIdentificador3() != null  || (f.getIdentificador3() == null && factura.getIdentificador3() == null) ||  f.getIdentificador3().equals(factura.getIdentificador3()))
													&& (f.getIdentificadorServicio() != null  || (f.getIdentificadorServicio() == null && factura.getIdentificadorServicio() == null) ||  f.getIdentificadorServicio().equals(factura.getIdentificadorServicio()))
													) {*/
												facturaRepetida = true;
												break;
											}
										}

										if(facturaRepetida) {
											FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
													"No se pemite agregar facturas repetidas.", null));
										}else {
											
											com.americacg.cargavirtual.pnet.message.solicitud.VerificarEstadoFactura stVEF = new com.americacg.cargavirtual.pnet.message.solicitud.VerificarEstadoFactura();
											stVEF.setIdMayorista(this.getUsuario().getIdMayorista());
											stVEF.setUsuario(this.getUsuario().getUsername());
											stVEF.setPwd(sClave);
											stVEF.setIdTurnoCaja(this.idTurnoCaja);
											stVEF.setFactura(factura);
											
											com.americacg.cargavirtual.pnet.message.respuesta.VerificarEstadoFactura rtVEF = PNetServiceHelper
													.getGatewayPNet(urlw).verificarEstadoFactura(stVEF);
											if (rtVEF == null) {
												FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
														"La verificación de estado de Facturas devolvio null", null));
											} else if (!"P0000".equals(rtVEF.getErrorRb().getCodigo())) {
												FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
														rtVEF.getErrorRb().getMensaje(), null));
											} else if ("P0000".equals(rtXCB.getErrorRb().getCodigo())) {
												//procede en modo normal
												totalAPagar = totalAPagar + factura.getImpAbonado();
												this.facturas.add(factura);

											}
										}

										this.cantidadFacturas = this.facturas.size();
										contenido += "<p>Confirmar Pago desde la lista</p>";
									} else if ("BARRA".equals(ing.getTipo().toUpperCase())) {
										if (enLista) {
											contenido += "</select>";
											contenido += "<button type=\"button\" accesskey=\"f2\" class=\"buttonMenuContinuar\" name=\"btnCONTINUAR\" id=\"btnCONTINUAR\" onclick=\"callScript('trx', 'LISTA', document.getElementById('selectValor').value, document.getElementById('selectValor').options[document.getElementById('selectValor').selectedIndex].text)\"><span class=\"ui-button-text ui-c\">CONTINUAR (F2)</span></button>";
											enLista = false;
										}
										contenido += "<h4>" + ing.getTexto() + "</h4>";
										// contenido += "<p>" + ing.getTexto() + "</p>";
										//type=\"number\" GONZA
										contenido += "<input name=\"inputValor\" id=\"inputValor\" value=\""
												+ ("0".equals(ing.getValor()) ? "" : ing.getValor()) 
												+ "\"  style=\"border-radius: 5px;padding: 5px;width: 99%;margin-bottom: 5px;\" autofocus>";
										contenido += "<button type=\"button\" accesskey=\"f2\" class=\"buttonMenuContinuar\" name=\"btnCONTINUAR\" id=\"btnCONTINUAR\" onclick=\"callScript([{name:'parametro', value:'trx'}, {name:'tipo', value:'LISTA'}, {name:'valor', value:document.getElementById('inputValor').value}, {name:'texto', value: document.getElementById('inputValor').value}])\"><span class=\"ui-button-text ui-c\">CONTINUAR (F2)</span></button>";
									} else if ("NUMERO".equals(ing.getTipo().toUpperCase())) {
										if (enLista) {
											contenido += "</select>";
											contenido += "<button type=\"button\" accesskey=\"f2\" class=\"ui-button ui-widget ui-state-default ui-corner-all ui-button-text-only ui-button ui-widget ui-state-default ui-corner-all ui-button-text-only\" name=\"btnCONTINUAR\" id=\"btnCONTINUAR\" onclick=\"callScript([{name:'parametro', value:'trx'}, {name:'tipo', value:'LISTA'}, {name:'valor', value:document.getElementById('selectValor').value}, {name:'texto', value: document.getElementById('selectValor').options[document.getElementById('selectValor').selectedIndex].text}])\"><span class=\"ui-button-text ui-c\">CONTINUAR (F2)</span></button>";
											enLista = false;
										}
										contenido += "<h4>" + ing.getTexto() + "</h4>";
										// contenido += "<p>" + ing.getTexto() + "</p>";
										contenido += "<input type=\"number\" name=\"inputValor\" id=\"inputValor\" value=\""
												+ ("0".equals(ing.getValor()) ? "" : ing.getValor())
												+ "\"  style=\"border-radius: 5px;padding: 5px;width: 99%;margin-bottom: 5px;\" autofocus>";
										contenido += "<button type=\"button\"  accesskey=\"f2\" class=\"ui-button ui-widget ui-state-default ui-corner-all ui-button-text-only ui-button ui-widget ui-state-default ui-corner-all ui-button-text-only\" name=\"btnCONTINUAR\" id=\"btnCONTINUAR\" onclick=\"callScript([{name:'parametro', value:'trx'}, {name:'tipo', value:'LISTA'}, {name:'valor', value: document.getElementById('inputValor').value}, {name:'texto', value: document.getElementById('inputValor').value}])\"><span class=\"ui-button-text ui-c\">CONTINUAR (F2)</span></button>";

									} else if ("IMPORTE".equals(ing.getTipo().toUpperCase())) {
										if (enLista) {
											contenido += "</select>";
											contenido += "<button type=\"button\" accesskey=\"f2\" class=\"ui-button ui-widget ui-state-default ui-corner-all ui-button-text-only ui-button ui-widget ui-state-default ui-corner-all ui-button-text-only\" name=\"btnCONTINUAR\" id=\"btnCONTINUAR\"  onclick=\"callScript("
													+ "[{name:'parametro', value:'trx'}, {name:'tipo', value:'"
													+ ing.getTipo()
													+ "'}, {name:'valor', value: document.getElementById('selectValor').value}, {name:'texto', value: document.getElementById('selectValor').options[document.getElementById('selectValor').selectedIndex].text}])\"><span class=\"ui-button-text ui-c\">CONTINUAR (F2)</span></button>";
											enLista = false;
										}
										contenido += "<h4>" + ing.getTexto() + "</h4>";
										// contenido += "<p>" + ing.getTexto() + "</p>";
										factura.setImpMin(1F);
										factura.setImpMax(Float.parseFloat(ing.getValor().replace(",", "")));
										factura.setImpTot(Float.parseFloat(ing.getValor().replace(",", "")));
										contenido += "<input type=\"number\" name=\"inputValor\" id=\"inputValor\" value=\""
												+ ("0".equals(ing.getValor()) ? "" : ing.getValor().replace(",", ""))
												+ "\" style=\"border-radius: 5px;padding: 5px;width: 99%;margin-bottom: 5px;\" autofocus>";
										contenido += "<button type=\"button\" accesskey=\"f2\" class=\"ui-button ui-widget ui-state-default ui-corner-all ui-button-text-only ui-button ui-widget ui-state-default ui-corner-all ui-button-text-only\" name=\"btnCONTINUAR\" id=\"btnCONTINUAR\" onclick=\"callScript([{name:'parametro', value:'trx'}, {name:'tipo', value:'"
												+ ing.getTipo()
												+ "'}, {name:'valor', value: document.getElementById('inputValor').value}, {name:'texto', value: document.getElementById('inputValor').value}])\"><span class=\"ui-button-text ui-c\">CONTINUAR (F2)</span></button>";
									} else if ("TEXTO".equals(ing.getTipo().toUpperCase())) {
										if (enLista) {
											contenido += "</select>";
											contenido += "<button type=\"button\" accesskey=\"f2\" class=\"ui-button ui-widget ui-state-default ui-corner-all ui-button-text-only ui-button ui-widget ui-state-default ui-corner-all ui-button-text-only\" name=\"btnCONTINUAR\" id=\"btnCONTINUAR\"  onclick=\"callScript("
													+ "[{name:'parametro', value:'trx'}, {name:'tipo', value:'"
													+ ing.getTipo()
													+ "'}, {name:'valor', value: document.getElementById('selectValor').value}, {name:'texto', value: document.getElementById('selectValor').options[document.getElementById('selectValor').selectedIndex].text}])\"><span class=\"ui-button-text ui-c\">CONTINUAR (F2)</span></button>";
											enLista = false;
										}
										contenido += "<h4>" + ing.getTexto() + "</h4>";
										// contenido += "<p>" + ing.getTexto() + "</p>";
										contenido += "<input type=\"text\" name=\"inputValor\" id=\"inputValor\" minlength=\"" + ing.getLongMin() + "\" maxlength=\"" + ing.getLongMax() + "\" value=\""
												+ ing.getValor()
												+ "\" style=\"border-radius: 5px;padding: 5px;width: 99%;margin-bottom: 5px;\" autofocus>";
										contenido += "<button type=\"button\" accesskey=\"f2\" class=\"ui-button ui-widget ui-state-default ui-corner-all ui-button-text-only ui-button ui-widget ui-state-default ui-corner-all ui-button-text-only\" name=\"btnCONTINUAR\" id=\"btnCONTINUAR\" onclick=\"callScript([{name:'parametro', value:'trx'}, {name:'tipo', value:'"
												+ ing.getTipo()
												+ "'}, {name:'valor', value: document.getElementById('inputValor').value}, {name:'texto', value: document.getElementById('inputValor').value}])\"><span class=\"ui-button-text ui-c\">CONTINUAR (F2)</span></button>";
									} else if ("MSGTEXTO".equals(ing.getTipo().toUpperCase())) {
										if (enLista) {
											contenido += "</select>";
											contenido += "<button type=\"button\" accesskey=\"f2\" class=\"ui-button ui-widget ui-state-default ui-corner-all ui-button-text-only ui-button ui-widget ui-state-default ui-corner-all ui-button-text-only\" name=\"btnCONTINUAR\" id=\"btnCONTINUAR\" onclick=\"callScript([{name:'parametro', value:'trx'}, {name:'tipo', value:'LISTA'}, {name:'valor', value: document.getElementById('selectValor').value}, {name:'texto', value: document.getElementById('selectValor').options[document.getElementById('selectValor').selectedIndex].text}])\"><span class=\"ui-button-text ui-c\">CONTINUAR (F2)</span></button>";
											enLista = false;
										}
										transaccionDatos = "<h2>Usted esta cobrando:</h2>";
										contenido += "<p>" + ing.getTexto() + "</p>";
									} else {
										contenido += "<p> COMPONENTE NO DISPONIBLE </p>";
										// contenido += "<button type=\"button\"
										// onclick=\"callScript('','','','');\">DESCARTAR</button>";
									}
								}
								if (enLista) {
									contenido += "</select>";
									contenido += "<button type=\"button\" accesskey=\"f2\" class=\"ui-button ui-widget ui-state-default ui-corner-all ui-button-text-only ui-button ui-widget ui-state-default ui-corner-all ui-button-text-only\" name=\"btnCONTINUAR\" id=\"btnCONTINUAR\" onclick=\"callScript([{name:'parametro', value:'trx'}, {name:'tipo', value:'LISTA'}, {name:'valor', value: document.getElementById('selectValor').value}, {name:'texto', value: document.getElementById('selectValor').options[document.getElementById('selectValor').selectedIndex].text}])\"><span class=\"ui-button-text ui-c\">CONTINUAR (F2)</span></button>";
									enLista = false;
								}

							}
						} else {

							if (rtXCB.getEstadoDescripcion() != null
									&& !"".equals(rtXCB.getEstadoDescripcion().trim())) {
								contenido = "<p>" + rtXCB.getEstadoDescripcion() + "</p>";
							}

							if ("ACG1".equals(rtXCB.getEstado())) {
								contenido += "<a id=\"aError\" href=\"#\" onclick=\"document.getElementById('detallesError').style.display = 'block';document.getElementById('aError').style.display = 'none';return false;\"  style=\"text-decoration: none;\">Ver detalles...</p>";
								contenido += "<div id=\"detallesError\" style=\"display: none;border: 1px solid lightgray;padding-top: 10px;\">";
								contenido += "<a href=\"#\" onclick=\"document.getElementById('aError').style.display = 'block';document.getElementById('detallesError').style.display = 'none';return false;\" style=\"text-decoration: none;float: right;top: 0px;display: flex;padding-right: 5px;\">Ocultar</a>";
								contenido += "<h4 style=\"font-size: 14px;margin: 0px;\">Detalle error proveedor</h4>";
								if (rtXCB.getIngresos() != null) {
									for (Ingreso ing : rtXCB.getIngresos().getIngreso()) {
										if ("MSGTEXTO".equals(ing.getTipo().toUpperCase())) {
											contenido += "<p>" + ing.getTexto() + "</p>";
										}
									}
								}

								contenido += "</div>";
								contenido += "<button type=\"button\" class=\"buttonMenuContinuar\" name=\"btnCONTINUAR\" id=\"btnCONTINUAR\" onclick=\"PF('facturaPanelWV').hide();return false;\" style=\"margin-top: 10px;\">CERRAR</button>";
							}else if("9".equals(rtXCB.getEstado())) {
								if (rtXCB.getIngresos() != null) {
									for (Ingreso ing : rtXCB.getIngresos().getIngreso()) {
										if ("MSGTEXTO".equals(ing.getTipo().toUpperCase())) {
											contenido += "<p>" + ing.getTexto() + "</p>";
										}
									}
									contenido += "<button type=\"button\" class=\"buttonMenuContinuar\" name=\"btnCONTINUAR\" id=\"btnCONTINUAR\" onclick=\"PF('facturaPanelWV').hide();return false;\" style=\"margin-top: 10px;\">CERRAR</button>";
								}
							}
						}

						if (rtXCB.getDescripcion() != null) {
							factura.setDescripcion(rtXCB.getDescripcion());
						}
						if (rtXCB.getIdentificador() != null) {
							factura.setNumeroUnicoSiris(rtXCB.getIdentificador().getNumeroUnicoSiris());
							factura.setIdentificadorServicio(rtXCB.getIdentificador().getServicio());
							factura.setDescripcion(rtXCB.getIdentificador().getDescripcion());
							factura.setIdentificador1(rtXCB.getIdentificador().getIdentificador1());
							factura.setIdentificador2(rtXCB.getIdentificador().getIdentificador2());
							factura.setIdentificador3(rtXCB.getIdentificador().getIdentificador3());
							factura.setCodBarra1(rtXCB.getIdentificador().getBarra());
							factura.setIdentificadorMarca(rtXCB.getIdentificador().getMarca());
							factura.setIdentificadorBuscaRepetida(rtXCB.getIdentificador().getBuscaRepetida());
						}
					} else {
					}
					break;

				case ANU:
					// ANULACION
					break;
				case INF:
					// REIMPRESION
					break;
				case EST:
					// ESTADO DE TERMINAL
					com.americacg.cargavirtual.pnet.message.solicitud.ConsultarEstado stCE = new com.americacg.cargavirtual.pnet.message.solicitud.ConsultarEstado();
					stCE.setIdMayorista(this.getUsuario().getIdMayorista());
					stCE.setUsuario(this.getUsuario().getUsername());
					stCE.setPwd(sClave);

					com.americacg.cargavirtual.pnet.message.respuesta.ConsultarEstado rtCE = PNetServiceHelper
							.getGatewayPNet(urlw).consultarEstado(stCE);
					if (rtCE == null) {
						terminalAbierta = 0; // con error
						FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
								"La consulta de Facturas devolvio null", null));
					} else if (!"P0000".equals(rtCE.getErrorRb().getCodigo())) {
						terminalAbierta = 0; // con error
						FacesContext.getCurrentInstance().addMessage(null,
								new FacesMessage(FacesMessage.SEVERITY_ERROR, rtCE.getErrorRb().getMensaje(), null));
						cerrarModal = "mpCerrar";
					} else if ("P0000".equals(rtCE.getErrorRb().getCodigo())) {
						inicializadoPNet = true;
						if ("1".equals(rtCE.getEstado())) {
							/*
							 * TODO HACER ESTO DINAMICO
							 */
							// this.idTurnoCaja = rtCE.getIdTurnoCaja();
							this.idTurnoCaja = 1L;
							terminalAbierta = 1; // abierta
						} else if ("0".equals(rtCE.getEstado())) {
							terminalAbierta = 2; // cerrada
						} else if ("S/I".equals(rtCE.getEstado())) {
							terminalAbierta = 2; // estado apertura/cierre no informado, entonces toma como cerrado.
						} else {
							FacesContext.getCurrentInstance().addMessage(null,
									new FacesMessage(FacesMessage.SEVERITY_ERROR, rtCE.getEstadoDescripcion(), null));
							cerrarModal = "mpCerrar";
						}
					} else {
						terminalAbierta = 0; // con error
					}
					obtenerMenu();
					break;
				case REI:
					// REIMPRESION
					this.cerrarModal = "";
					this.ticket = "";
					com.americacg.cargavirtual.pnet.message.solicitud.ReimprimirFactura stRT = new com.americacg.cargavirtual.pnet.message.solicitud.ReimprimirFactura();
					stRT.setIdMayorista(this.getUsuario().getIdMayorista());
					stRT.setUsuario(this.getUsuario().getUsername());
					stRT.setPwd(sClave);
					com.americacg.cargavirtual.pnet.model.Factura t = new com.americacg.cargavirtual.pnet.model.Factura();
					t.setId(Long.parseLong(tipo));
					t.setNumeroUnicoSiris(Long.parseLong(valor));
					stRT.setFactura(t);

					com.americacg.cargavirtual.pnet.message.respuesta.ReimprimirFactura rtRT = PNetServiceHelper
							.getGatewayPNet(urlw).reimprimirFactura(stRT);
					if (rtRT == null) {
						FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
								"La consulta de Facturas devolvio null", null));
						cerrarModal = "mpCerrar";						
					} else if (!"P0000".equals(rtRT.getErrorRb().getCodigo())) {
						FacesContext.getCurrentInstance().addMessage(null,
								new FacesMessage(FacesMessage.SEVERITY_ERROR, rtRT.getErrorRb().getMensaje(), null));
						cerrarModal = "mpCerrar";						
					} else if ("P0000".equals(rtRT.getErrorRb().getCodigo())) {

						if (rtRT.getComprobanteFactura() != null) {
							StringBuilder sb = new StringBuilder();
							sb.append("<style> fieldset {text-align:center;width:450px;} </style>");
							sb.append("<fieldset>");
							if (rtRT.getComprobanteFactura().getLineas() != null) {
								for (String linea : rtRT.getComprobanteFactura().getLineas()) {
									sb.append("<p>").append(linea).append("</p>");
								}
							}
							sb.append("</fieldset>");
							this.ticket = sb.toString();
						} else {
							FacesContext.getCurrentInstance().addMessage(null,
									new FacesMessage(FacesMessage.SEVERITY_ERROR, "Transaccion sin datos.", null));
							cerrarModal = "mpCerrar";							
						}

					} else {
						FacesContext.getCurrentInstance().addMessage(null,
								new FacesMessage(FacesMessage.SEVERITY_ERROR, rtRT.getErrorRb().getMensaje(), null));
						cerrarModal = "mpCerrar";
					}

					break;

				case RIT:
					// REIMPRESION
					this.ticket = "";

					StringBuilder sb = new StringBuilder();
					for (Factura factura : facturas) {
						sb.append("<fieldset>");
						com.americacg.cargavirtual.pnet.message.solicitud.ReimprimirFactura stRIT = new com.americacg.cargavirtual.pnet.message.solicitud.ReimprimirFactura();
						stRIT.setIdMayorista(this.getUsuario().getIdMayorista());
						stRIT.setUsuario(this.getUsuario().getUsername());
						stRIT.setPwd(sClave);
						com.americacg.cargavirtual.pnet.model.Factura tRIT = new com.americacg.cargavirtual.pnet.model.Factura();
						tRIT.setId(factura.getId());
						stRIT.setFactura(tRIT);

						com.americacg.cargavirtual.pnet.message.respuesta.ReimprimirFactura rtRIT = PNetServiceHelper
								.getGatewayPNet(urlw).reimprimirFactura(stRIT);
						if (rtRIT == null) {
							FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(
									FacesMessage.SEVERITY_ERROR, "La consulta de Facturas devolvio null", null));
							cerrarModal = "mpCerrar";
						} else if (!"P0000".equals(rtRIT.getErrorRb().getCodigo())) {
							FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(
									FacesMessage.SEVERITY_ERROR, rtRIT.getErrorRb().getMensaje(), null));
							cerrarModal = "mpCerrar";
						} else if ("P0000".equals(rtRIT.getErrorRb().getCodigo())) {

							if (rtRIT.getComprobanteFactura() != null) {

								if (rtRIT.getComprobanteFactura().getLineas() != null) {
									for (String linea : rtRIT.getComprobanteFactura().getLineas()) {
										sb.append("<p>").append(linea).append("</p>");
									}
								}
							} else {
								FacesContext.getCurrentInstance().addMessage(null,
										new FacesMessage(FacesMessage.SEVERITY_ERROR, "Transaccion sin datos.", null));
								cerrarModal = "mpCerrar";
							}

						} else {
							FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(
									FacesMessage.SEVERITY_ERROR, rtRIT.getErrorRb().getMensaje(), null));
							cerrarModal = "mpCerrar";
						}
						sb.append("</fieldset>");
						sb.append("<br /><br /><br />");
					}
					this.ticket = sb.toString();
					break;

				case INA:
					break;
				case CIE:
					// CIERRE DE TERMINAL
					cerrarPantallas = false;
					com.americacg.cargavirtual.pnet.message.solicitud.CierreTerminal stCT = new com.americacg.cargavirtual.pnet.message.solicitud.CierreTerminal();
					stCT.setIdMayorista(this.getUsuario().getIdMayorista());
					stCT.setUsuario(this.getUsuario().getUsername());
					stCT.setPwd(sClave);

					com.americacg.cargavirtual.pnet.message.respuesta.CierreTerminal rtCT = PNetServiceHelper
							.getGatewayPNet(urlw).cierreTerminal(stCT);
					if (rtCT == null) {
						FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
								"La consulta de Facturas devolvio null", null));

						//TODO: Se pone vacio para que no ejecute ninguna accion al reconsultar estado, esto hay que optimizarlo 
						cerrarModal = "";
						invocarPNet("EST","","","");
						cerrarPantallas = true;
					} else if (!"P0000".equals(rtCT.getErrorRb().getCodigo())) {
						FacesContext.getCurrentInstance().addMessage(null,
								new FacesMessage(FacesMessage.SEVERITY_ERROR, rtCT.getErrorRb().getMensaje(), null));

						//TODO: Se pone vacio para que no ejecute ninguna accion al reconsultar estado, esto hay que optimizarlo 
						cerrarModal = "";
						invocarPNet("EST","","","");
						cerrarPantallas = true;
					} else if ("P0000".equals(rtCT.getErrorRb().getCodigo())) {
						// if ("2".equals(rtCT.getEstado())) {
						if ("0".equals(rtCT.getEstado())) {
							terminalAbierta = 2;
							this.ticket = "";
							this.idTurnoCaja = null;
							for (String linea : rtCT.getTicket().getListaLineas()) {
								this.ticket = this.ticket + linea + "<br />";
							}
						} else {
							FacesContext.getCurrentInstance().addMessage(null,
									new FacesMessage(FacesMessage.SEVERITY_ERROR, rtCT.getEstadoDescripcion(), null));

							//TODO: Se pone vacio para que no ejecute ninguna accion al reconsultar estado, esto hay que optimizarlo 
							cerrarModal = "";
							invocarPNet("EST","","","");
							cerrarPantallas = true;
						}
					} else {

						//TODO: Se pone vacio para que no ejecute ninguna accion al reconsultar estado, esto hay que optimizarlo 
						cerrarModal = "";
						invocarPNet("EST","","","");
						cerrarPantallas = true;
					}
					obtenerMenu();
					limpiarPantalla();
					if(cerrarPantallas) {
						cerrarModal = "mpCerrar";
					}
					break;
				case APE:
					// APERTURA DE TERMINAL
					this.cerrarModal = "mpCerrarAll";					
					cerrarPantallas = false;
					com.americacg.cargavirtual.pnet.message.solicitud.AperturaTerminal stAT = new com.americacg.cargavirtual.pnet.message.solicitud.AperturaTerminal();
					stAT.setIdMayorista(this.getUsuario().getIdMayorista());
					stAT.setUsuario(this.getUsuario().getUsername());
					stAT.setPwd(sClave);

					com.americacg.cargavirtual.pnet.message.respuesta.AperturaTerminal rtAT = PNetServiceHelper
							.getGatewayPNet(urlw).aperturaTerminal(stAT);
					if (rtAT == null) {
						FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
								"La consulta de Facturas devolvio null", null));
						cerrarPantallas = true;
					} else if (!"P0000".equals(rtAT.getErrorRb().getCodigo())) {
						FacesContext.getCurrentInstance().addMessage(null,
								new FacesMessage(FacesMessage.SEVERITY_ERROR, rtAT.getErrorRb().getMensaje(), null));
						cerrarPantallas = true;
					} else if ("P0000".equals(rtAT.getErrorRb().getCodigo())) {
						if ("0".equals(rtAT.getEstado())) {
							this.idTurnoCaja = rtAT.getIdTurnoCaja();
							terminalAbierta = 1;
						} else {
							FacesContext.getCurrentInstance().addMessage(null,
									new FacesMessage(FacesMessage.SEVERITY_ERROR, rtAT.getEstadoDescripcion(), null));
							cerrarPantallas = true;
						}
					} else {

					}
					obtenerMenu();
					limpiarPantalla();
					if(cerrarPantallas) {
						cerrarModal = "mpCerrar";
					}
					break;
				case CTP:
					// CONFIRMACION DE PAGO

					loteTotalmenteAprobado = false;

					com.americacg.cargavirtual.pnet.message.solicitud.Confirmar stC = new com.americacg.cargavirtual.pnet.message.solicitud.Confirmar();
					stC.setIdMayorista(this.getUsuario().getIdMayorista());
					stC.setUsuario(this.getUsuario().getUsername());
					stC.setPwd(sClave);
					stC.setIdTipoTerminal(1L);
					// stC.setLote(new Lote());
					// stC.setListaTransaccion(new ListaTransaccion());

					stC.setIdTurnoCaja(this.idTurnoCaja);
					stC.setLote(this.lote);
					stC.getLote().setListaFacturas(new ListaFacturas());

					for (Factura factura : facturas) {
						if (factura.isIncluir()) {
							stC.getLote().getListaFacturas().getFactura().add(factura);
						}
					}

					com.americacg.cargavirtual.pnet.message.respuesta.Confirmar rtC = PNetServiceHelper
							.getGatewayPNet(urlw).confirmar(stC);
					if (rtC == null) {
						FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
								"La confirmacion de pago devolvio null", null));
						cerrarModal = "mpcCerrar";
					} else {
						if (rtC.getLote() != null) {

							if (!"P0000".equals(rtC.getErrorRb().getCodigo())) {
								FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(
										FacesMessage.SEVERITY_ERROR, rtC.getErrorRb().getMensaje(), null));
								cerrarModal = "mpcCerrar";
							} else {
								if (rtC.getEstadoDescripcion() != null) {
									FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(
											FacesMessage.SEVERITY_ERROR, rtC.getEstadoDescripcion(), null));
									cerrarModal = "mpcCerrar";
								}
							}

							BeanUtils.copyProperties(rtC.getLote(), this.lote);
							if (rtC.getLote().getListaFacturas() != null) {
								totalAPagar = 0F;
								int cantidadAprobadas = 0;
								for (int i = 0; i < facturas.size(); i++) {
									for (com.americacg.cargavirtual.pnet.model.Factura trx : rtC.getLote()
											.getListaFacturas().getFactura()) {
										if (facturas.get(i).getNumeroUnicoSiris().equals(trx.getNumeroUnicoSiris())) {
											BeanUtils.copyProperties(trx, facturas.get(i));
											if (trx.getEstado() != null) {
												if ((trx.getEstado().getId() == 2L)
														|| (trx.getEstado().getId() == 5L)) {
													cantidadAprobadas += 1;
												}
											}
										}
									}
									totalAPagar += facturas.get(i).getImpAbonado();
								}
								loteTotalmenteAprobado = (cantidadAprobadas == facturas.size());
								if ("P0000".equals(rtC.getErrorRb().getCodigo())) {
									FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(
											FacesMessage.SEVERITY_ERROR, rtC.getErrorRb().getMensaje(), null));
									
									PrimeFaces.current().executeScript(
											"ACGSiteScriptsFCNS.cambiaFondoMsg('msgsProductos', '#212121', '#9ed5a0', '#a0d4b7', '"
													+ FacesContext.getCurrentInstance().getExternalContext().getRequestContextPath()
															.replace("/", "\\/")
													+ "\\/javax.faces.resource\\/images\\/24x24\\/operacionOK.png')");
									
									
									this.ticket = "";
									if (rtC.getTicket() != null) {
										if (rtC.getTicket().getListaLineas() != null) {
											for (String linea : rtC.getTicket().getListaLineas()) {
												this.ticket = this.ticket + linea + "<br />";
											}
										}
									}
								}
								obtenerMenu();
								this.cerrarModal = "mpcCerrar";
							} else {
								FacesContext.getCurrentInstance().addMessage(null,
										new FacesMessage(FacesMessage.SEVERITY_ERROR,
												"La lista de transacciones obtenidas es nula.", null));
								cerrarModal = "mpcCerrar";
							}
						} else {
							FacesContext.getCurrentInstance().addMessage(null,
									new FacesMessage(FacesMessage.SEVERITY_ERROR, "El lote obtenido es nulo.", null));
							cerrarModal = "mpcCerrar";
						}
					}
					
					this.confirmarMonto = null;
					this.confirmarVueltoAEntregar = 0f;
					this.confirmarVueltoAEntregarColor = "white";
					this.confirmarVueltoAEntregarBackgroundColor = "red";					

					break;
				case EXP:
					// REIMPRESION
					this.cerrarModal = "";
					String pdf = "";
					com.americacg.cargavirtual.pnet.message.solicitud.ReimprimirFactura stRF = new com.americacg.cargavirtual.pnet.message.solicitud.ReimprimirFactura();
					stRF.setIdMayorista(this.getUsuario().getIdMayorista());
					stRF.setUsuario(this.getUsuario().getUsername());
					stRF.setPwd(sClave);
					com.americacg.cargavirtual.pnet.model.Factura f = new com.americacg.cargavirtual.pnet.model.Factura();
					f.setId(Long.parseLong(tipo));
					f.setNumeroUnicoSiris(Long.parseLong(valor));
					stRF.setFactura(f);

					com.americacg.cargavirtual.pnet.message.respuesta.ReimprimirFactura rtRF = PNetServiceHelper
							.getGatewayPNet(urlw).reimprimirFactura(stRF);
					if (rtRF == null) {
						FacesContext.getCurrentInstance().addMessage("msgsProductos", new FacesMessage(FacesMessage.SEVERITY_ERROR,
								"La consulta de Facturas devolvio null", null));
						cerrarModal = "mpCerrar";
					} else if (!"P0000".equals(rtRF.getErrorRb().getCodigo())) {
						FacesContext.getCurrentInstance().addMessage("msgsProductos",
								new FacesMessage(FacesMessage.SEVERITY_ERROR, rtRF.getErrorRb().getMensaje(), null));
						cerrarModal = "mpCerrar";
					} else if ("P0000".equals(rtRF.getErrorRb().getCodigo())) {

						if (rtRF.getComprobanteFactura() != null) {
							StringBuilder sbRT = new StringBuilder();
							if (rtRF.getComprobanteFactura().getLineas() != null) {
								for (String linea : rtRF.getComprobanteFactura().getLineas()) {
									sbRT.append(linea).append("\r\n");
								}
							}
							pdf = sbRT.toString();

							try {

								FacesContext fc = FacesContext.getCurrentInstance();
								ExternalContext ec = fc.getExternalContext();

								ec.responseReset();
								ec.setResponseContentType("application/pdf");
								ec.setResponseHeader("Pragma", "no-cache");
								ec.setResponseHeader("Expires", "0");
								ec.setResponseHeader("Content-Disposition",
										"attachment; filename=\"" + tipo + ".pdf" + "\"");

								// Document doc = new Document(PageSize.A4, 50, 50, 50, 50);
								Document doc = new Document();
								OutputStream out = ec.getResponseOutputStream();
								PdfWriter writer = PdfWriter.getInstance(doc, out);
								doc.open();

								Font font = new Font(FontFamily.COURIER);
								Paragraph preface = new Paragraph(pdf, font);
								preface.setAlignment(Element.ALIGN_CENTER);
								doc.add(preface);

								doc.close();
								writer.close();

								sbRT.setLength(0);

								fc.responseComplete();

							} catch (Exception e) {
								//e.printStackTrace();
								LogACGHelper.escribirLog(null, "Error al realizar la exportacion de PNet : |" + e.getMessage() + "|");
							}

						} else {
							FacesContext.getCurrentInstance().addMessage("msgsProductos",
									new FacesMessage(FacesMessage.SEVERITY_ERROR, "Transaccion sin datos.", null));
							cerrarModal = "mpCerrar";
						}

					} else {
						FacesContext.getCurrentInstance().addMessage("msgsProductos",
								new FacesMessage(FacesMessage.SEVERITY_ERROR, rtRF.getErrorRb().getMensaje(), null));
						cerrarModal = "mpCerrar";
					}
					break;
				case GLC:	
					this.facturas.remove(this.selectedFactura);
					this.selectedFactura = null;
					recalcularTotales();
					break;
				case CLS:
					limpiarPantalla();
					break;
				default:
					obtenerMenu();
					break;
				}
			}
		} catch (WebServiceException ste) {
			if (ste != null && ste.getCause() != null && ste.getCause().getClass() != null) {
				if (ste.getCause().getClass().equals(ConnectException.class)) {
					FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
							"No se pudo establecer la comunicación (GPN-TOC).\n Por favor intente nuevamente.", null));
				} else if (ste.getCause().getClass().equals(SocketTimeoutException.class)) {
					FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
							"No se pudo establecer la comunicación (GPN-TRW).\n Por favor intente nuevamente.", null));
				} else if (ste.getCause().getClass().equals(UnknownHostException.class)) {
					LogACGHelper.escribirLog(null, "No se pudo establecer la comunicación (GPN-HNC): |" + ste.getMessage() + "|");
					FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
							"No se pudo establecer la comunicación (GPN-HNC).\n Por favor intente nuevamente.", null));					
				} else {
					LogACGHelper.escribirLog(null, "Invocar PNet. Error ejecutando el WS: |" + ste.getMessage() + "|");
					FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
							"Invocar PNet. Error ejecutando el WS: |" + ste.getMessage() + "|", null));
				}
			} else {
				LogACGHelper.escribirLog(null, "Invocar PNet. Error ejecutando el WS: |" + ste.getMessage() + "|");
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
						"Invocar PNet. Error ejecutando el WS: |" + ste.getMessage() + "|", null));
			}
			terminalAbierta = 0; // con error
			this.cerrarModal = "mpCerrarAll";
			obtenerMenu();
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR, "Ha ocurrido un error: " + e.getMessage(), null));
			// contenido = e.getMessage();
			terminalAbierta = 0; // con error
			this.cerrarModal = "mpCerrarAll";
			obtenerMenu();
		} finally {
			if ("mpMostrar".equals(cerrarModal)) {
				// System.out.println("Contenido: |" + contenido + "|");
				PrimeFaces.current().executeScript("PF('facturaPanelWV').show();");
			} else if ("mpCerrar".equals(cerrarModal)) {
				PrimeFaces.current().executeScript("PF('facturaPanelWV').hide();");
			} else if ("mpcCerrar".equals(cerrarModal) || "mpCerrarAll".equals(cerrarModal)) {
				PrimeFaces.current().executeScript("PF('confirmarPanel').hide();");
			}
			;
			if (!"".equals(ticket)) {
				this.mostrarTicket();
			}
		}
	}

	public void mostrarTicket() {
		String nombreTicketCompleto = "";

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

		nombreTicketCompleto = "/secure/shared/tickets/ticketPNet.xhtml";

		// --- Abre el dialogo para buscar articulos
		PrimeFaces.current().dialog().openDynamic(nombreTicketCompleto, options, paramMap);
		PrimeFaces.current().executeScript("PF('blockPanelGral').hide()");
	}
	
	public String getTicket() {
		return ticket;
	}

	public void setTicket(String ticket) {
		this.ticket = ticket;
	}

	public Integer getMostrarPantallasDePNet() {
		try {
			if (mostrarPantallasDePNet == -1) {

				mostrarPantallasDePNet = 0;

				// tipoproducto = 18 (Provincia Net Pagos)
				RespString rs = GestionServiceHelper.getGestionService(CfgTimeout.DEFAULT)
						.validarHabilitacionPorTipoProducto(this.getUsuario().getIdMayorista(), this.getUsuario().getIdCliente(), 18L);
				
				if ((rs != null) && (!rs.getError().getValue().getHayError().getValue())) {
					mostrarPantallasDePNet = 1;
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
					LogACGHelper.escribirLog(null, "Mostrar Pantalla de PNet: |" + ste.getMessage() + "|");
					FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
							"Error ejecutando el WS de consulta: |" + ste.getMessage() + "|", null));
				}
			} else {
				LogACGHelper.escribirLog(null, "Mostrar Pantalla de PNet: |" + ste.getMessage() + "|");
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
						"Error ejecutando el WS de consulta: |" + ste.getMessage() + "|", null));
			}
			FacesContext.getCurrentInstance().validationFailed();
		} catch (Exception e) {
			LogACGHelper.escribirLog(null, "Mostrar Pantalla de PNet. Excepcion: |" + e.getMessage() + "|");
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
					"Error ejecutando el WS de consulta: |" + e.getMessage() + "|", null));
			FacesContext.getCurrentInstance().validationFailed();
		}
		return mostrarPantallasDePNet;
	}

	public void setMostrarPantallasDePNet(Integer mostrarPantallasDePNet) {
		this.mostrarPantallasDePNet = mostrarPantallasDePNet;
	}

	public List<Factura> getFacturas() {
		return facturas;
	}

	public void setFacturas(List<Factura> facturas) {
		this.facturas = facturas;
	}

	public Float getTotalAPagar() {
		return totalAPagar;
	}

	public void setTotalAPagar(Float totalAPagar) {
		this.totalAPagar = totalAPagar;
	}

	public Float getTotalPagado() {
		return totalPagado;
	}

	public void setTotalPagado(Float totalPagado) {
		this.totalPagado = totalPagado;
	}

	public Integer getCantidadFacturas() {
		return cantidadFacturas;
	}

	public void setCantidadFacturas(Integer cantidadFacturas) {
		this.cantidadFacturas = cantidadFacturas;
	}

	public String getTransaccionDatos() {
		return transaccionDatos;
	}

	public void setTransaccionDatos(String transaccionDatos) {
		this.transaccionDatos = transaccionDatos;
	}

	public String getCerrarModal() {
		return cerrarModal;
	}

	public void setCerrarModal(String cerrarModal) {
		this.cerrarModal = cerrarModal;
	}

	public String getModalPanelTitulo() {
		return modalPanelTitulo;
	}

	public void setModalPanelTitulo(String modalPanelTitulo) {
		this.modalPanelTitulo = modalPanelTitulo;
	}

	public String getMenuLateralBottom() {
		return menuLateralBottom;
	}

	public void setMenuLateralBottom(String menuLateralBottom) {
		this.menuLateralBottom = menuLateralBottom;
	}

	public String getMenuLateralTop() {
		return menuLateralTop;
	}

	public void setMenuLateralTop(String menuLateralTop) {
		this.menuLateralTop = menuLateralTop;
	}

	public Boolean getLoteTotalmenteAprobado() {
		return loteTotalmenteAprobado;
	}

	public void setLoteTotalmenteAprobado(Boolean loteTotalmenteAprobado) {
		this.loteTotalmenteAprobado = loteTotalmenteAprobado;
	}

	public boolean isConfirmarHabilitado() {
		confirmarHabilitado = false;
		if (this.facturas != null) {
			for (Factura factura : facturas) {
				if (factura.isIncluir() && (factura.getResultado() == null
						|| (factura.getResultado() != null && !"0".equals(factura.getResultado())))) {
					if(factura.getEstado() == null || 
							(factura.getEstado() != null && factura.getEstado().getId() == 3)) {
						confirmarHabilitado = true;
						break;
					}
				}
			}
		}
		return confirmarHabilitado;
	}

	public void setConfirmarHabilitado(boolean confirmarHabilitado) {
		this.confirmarHabilitado = confirmarHabilitado;
	}

	public boolean isBorrarHabilitado() {
		borrarHabilitado = false;
		if (this.facturas != null) {
			borrarHabilitado = this.facturas.size() > 0;
		}
		return borrarHabilitado;
	}

	public void setBorrarHabilitado(boolean borrarHabilitado) {
		this.borrarHabilitado = borrarHabilitado;
	}

	public boolean isImprimirTodos() {
		imprimirTodos = false;
		if (this.facturas != null) {
			for (Factura factura : facturas) {
				if (factura.getEstado() != null) {
					if (factura.getEstado().getId() == 2 && "0".equals(factura.getResultado())) {
						imprimirTodos = true;
						break;
					}
				}
			}
		}
		return imprimirTodos;
	}
	
	public void calcular() {
		
		Float vuelto = 0f;
		try {
			vuelto = this.confirmarMonto - this.totalAPagar;
			if(vuelto < 0) {
				this.confirmarVueltoAEntregar = 0f;
			}else {
				this.confirmarVueltoAEntregar = vuelto;
			}
		} catch (Exception e) {
			confirmarVueltoAEntregar = 0f; 
		}
		
		if(vuelto < 0) {
			this.confirmarVueltoAEntregarColor = "white";
			this.confirmarVueltoAEntregarBackgroundColor = "red";
		}else {
			this.confirmarVueltoAEntregarColor = "black";
			this.confirmarVueltoAEntregarBackgroundColor = "white";			
		}
    }

	public void setImprimirTodos(boolean imprimirTodos) {
		this.imprimirTodos = imprimirTodos;
	}

	public boolean isShowBtnManual() {
		return showBtnManual;
	}

	public void setShowBtnManual(boolean showBtnManual) {
		this.showBtnManual = showBtnManual;
	}

	public boolean isShowBtnBarra() {
		return showBtnBarra;
	}

	public void setShowBtnBarra(boolean showBtnBarra) {
		this.showBtnBarra = showBtnBarra;
	}

	public boolean isShowBtnInforme() {
		return showBtnInforme;
	}

	public void setShowBtnInforme(boolean showBtnInforme) {
		this.showBtnInforme = showBtnInforme;
	}

	public boolean isShowBtnApertura() {
		return showBtnApertura;
	}

	public void setShowBtnApertura(boolean showBtnApertura) {
		this.showBtnApertura = showBtnApertura;
	}

	public boolean isShowBtnCierre() {
		return showBtnCierre;
	}

	public void setShowBtnCierre(boolean showBtnCierre) {
		this.showBtnCierre = showBtnCierre;
	}

	public boolean isInicializadoPNet() {
		return inicializadoPNet;
	}

	public void setInicializadoPNet(boolean inicializadoPNet) {
		this.inicializadoPNet = inicializadoPNet;
	}

	public Float getConfirmarMonto() {
		return confirmarMonto;
	}

	public void setConfirmarMonto(Float confirmarMonto) {
		this.confirmarMonto = confirmarMonto;
	}

	public Float getConfirmarVueltoAEntregar() {
		return confirmarVueltoAEntregar;
	}

	public void setConfirmarVueltoAEntregar(Float confirmarVueltoAEntregar) {
		this.confirmarVueltoAEntregar = confirmarVueltoAEntregar;
	}

	public String getConfirmarVueltoAEntregarColor() {
		return confirmarVueltoAEntregarColor;
	}

	public void setConfirmarVueltoAEntregarColor(String confirmarVueltoAEntregarColor) {
		this.confirmarVueltoAEntregarColor = confirmarVueltoAEntregarColor;
	}

	public String getConfirmarVueltoAEntregarBackgroundColor() {
		return confirmarVueltoAEntregarBackgroundColor;
	}

	public void setConfirmarVueltoAEntregarBackgroundColor(String confirmarVueltoAEntregarBackgroundColor) {
		this.confirmarVueltoAEntregarBackgroundColor = confirmarVueltoAEntregarBackgroundColor;
	}

	public Factura getSelectedFactura() {
		return selectedFactura;
	}

	public void setSelectedFactura(Factura selectedFactura) {
		this.selectedFactura = selectedFactura;
	}
}
