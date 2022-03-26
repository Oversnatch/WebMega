package com.americacg.cargavirtual.web.mngbeans;

import java.io.OutputStream;
import java.io.Serializable;
import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.xml.ws.WebServiceException;

import org.primefaces.PrimeFaces;
import org.springframework.context.annotation.Scope;

import com.americacg.cargavirtual.gcsi.entidad.Factura;
import com.americacg.cargavirtual.gcsi.entidad.FacturaGIRE;
import com.americacg.cargavirtual.gcsi.entidad.Lote;
import com.americacg.cargavirtual.gcsi.entidad.Lote.Facturas;
import com.americacg.cargavirtual.gcsi.entidad.TerminalGIRE;
import com.americacg.cargavirtual.gcsi.entidad.TurnoCaja;
import com.americacg.cargavirtual.gcsi.model.acg.request.AbrirTurnoRequest;
import com.americacg.cargavirtual.gcsi.model.acg.request.CerrarTurnoRequest;
import com.americacg.cargavirtual.gcsi.model.acg.request.ConfirmarRequestACG;
import com.americacg.cargavirtual.gcsi.model.acg.request.ConsultarBarraRequest;
import com.americacg.cargavirtual.gcsi.model.acg.request.ConsultarEmpresaRequestACG;
import com.americacg.cargavirtual.gcsi.model.acg.request.ConsultarTurnoCajaAsignadoRequest;
import com.americacg.cargavirtual.gcsi.model.acg.request.ObtenerFacturaSinSoportePapelGIRERequest;
import com.americacg.cargavirtual.gcsi.model.acg.request.ObtenerFacturaSinSoportePapelGIRERequest.DatosFormulario;
import com.americacg.cargavirtual.gcsi.model.acg.request.ObtenerParamFacSinSopPapelGIRERequest;
import com.americacg.cargavirtual.gcsi.model.acg.response.AbrirTurnoResponse;
import com.americacg.cargavirtual.gcsi.model.acg.response.CerrarTurnoResponse;
import com.americacg.cargavirtual.gcsi.model.acg.response.ConfirmarResponseACG;
import com.americacg.cargavirtual.gcsi.model.acg.response.ConsultarBarraResponse;
import com.americacg.cargavirtual.gcsi.model.acg.response.ConsultarEmpresaResponseACG;
import com.americacg.cargavirtual.gcsi.model.acg.response.ConsultarTurnoCajaAsignadoResponse;
import com.americacg.cargavirtual.gcsi.model.acg.response.ObtenerFacturaSinSoportePapelResponseACG;
import com.americacg.cargavirtual.gcsi.model.acg.response.ObtenerParamFacSinSopPapelGIREResponse;
import com.americacg.cargavirtual.gcsi.service.Campo;
import com.americacg.cargavirtual.gcsi.service.Campo.ListaValores.Entry;
import com.americacg.cargavirtual.gcsi.service.CodigoRetorno;
import com.americacg.cargavirtual.gcsi.service.EmpresaRapipago;
import com.americacg.cargavirtual.gcsi.service.FormasPago;
import com.americacg.cargavirtual.gcsi.service.Modalidad;
import com.americacg.cargavirtual.gestion.model.DatosPlataforma;
import com.americacg.cargavirtual.gestion.model.RespString;
import com.americacg.cargavirtual.web.enums.CfgTimeout;
import com.americacg.cargavirtual.web.helpers.GCSIServiceHelper;
import com.americacg.cargavirtual.web.helpers.GestionServiceHelper;
import com.americacg.cargavirtual.web.helpers.LogACGHelper;
import com.americacg.cargavirtual.web.model.FacturaGCSIWeb;
import com.americacg.cargavirtual.web.model.ItemGCSISinFac;
import com.americacg.cargavirtual.web.shared.BasePage;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Font.FontFamily;
import com.itextpdf.text.pdf.PdfWriter;

@Named("cobranzaServImpView")
@Scope(org.springframework.web.context.WebApplicationContext.SCOPE_SESSION)
public class CobranzaServImpView extends BasePage implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8938640808404863450L;

	private String contenido = "";
	private String menuLateralTop = "";
	private String menuLateralBottom = "";
	private String ticket = "";
	private String confirmarVueltoAEntregarColor = "white";
	private String confirmarVueltoAEntregarBackgroundColor = "red";
	private String transaccionDatos = "";
	private String cerrarModal = "mpVisualizar";
	private String modalPanelTitulo = "";
	private String codEmpresaSeleccionada = "";
	private String operadora = "";
	private String idModSeleccionado = "";
	private String idFormaDePagoSeleccionado = "";
	private String facturaAnula = "";
	private String operacionAEjecutar = "";
	private Integer mostrarPantallasRapipago = -1;
	private Integer cantidadFacturas = 0;
	private List<FacturaGCSIWeb> facturas = new ArrayList<FacturaGCSIWeb>();
	private List<Factura> facturasColision = new ArrayList<Factura>();
	private List<EmpresaRapipago> empresas = new ArrayList<EmpresaRapipago>();
	private List<Modalidad> modalidadesCobranza = new ArrayList<Modalidad>();
	private List<FormasPago> formasDePago = new ArrayList<FormasPago>();
	private Float totalAPagar = 0F;
	private Float totalPagado = 0F;
	private Float confirmarMonto = null;
	private Float confirmarVueltoAEntregar = 0F;
	private Float facturaImpMinNeg = 0f;
	private Float facturaImpMaxNeg = 0f;
	private Float facturaImpMinPos = 0f;
	private Float facturaImpMaxPos = 0f;
	private Lote lote = null;
	private Factura facturaSeleccionada;
	private EmpresaRapipago empresaSeleccionada = null;
	private String idTemporalSeleccionado = null;
	private TurnoCaja oTurnoCaja = null;
	private DatosPlataforma oParamsPlat = null;
	private CodigoRetorno codigoRetornoAnterior = null;
	private Boolean loteTotalmenteAprobado = false;
	private boolean inicializadoGCSI = false;
	private boolean confirmarHabilitado;
	private boolean borrarHabilitado;
	private boolean imprimirTodos;
	private boolean showBtnManual = false;
	private boolean showBtnBarra = false;
	private boolean showBtnInforme = false;
	private boolean showBtnApertura = false;
	private boolean showBtnCierre = false;
	private boolean showCmbEmpresa = false;
	private boolean showCmbTipoCobranza = false;
	private boolean showCmbFormaDePago = false;
	private boolean showLstColisionFactura = false;
	private boolean showSetImporteFactura = false;

	public void recalcularTotales() {
		totalAPagar = 0F;
		cantidadFacturas = 0;

		for (FacturaGCSIWeb factura : facturas) {
			if (factura.isIncluir()) {
				if (factura.getFactura().getEstado() == null || factura.getFactura().getEstado().getId() == 3
						|| factura.getFactura().getEstado().getId() == 4) {
					totalAPagar = totalAPagar + factura.getFactura().getImporteAbonado();
					cantidadFacturas += 1;
				}
			}
		}
	}

	public void forzarInicializarGCSI(String operadora) {
		try {
			this.setOperadora(operadora);

			limpiarPantalla();

			if (!inicializadoGCSI) {
				try {
					oParamsPlat = GestionServiceHelper.getGestionService(CfgTimeout.DEFAULT)
							.mostrarDatosPlataforma(this.getUsuario().getIdMayorista());
				} catch (WebServiceException ste) {
					if (ste != null && ste.getCause() != null && ste.getCause().getClass() != null) {
						if (ste.getCause().getClass().equals(ConnectException.class)) {
							FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(
									FacesMessage.SEVERITY_ERROR,
									"No se pudo establecer la comunicación (GST-TOC).\n Por favor intente nuevamente.",
									null));
						} else if (ste.getCause().getClass().equals(SocketTimeoutException.class)) {
							FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(
									FacesMessage.SEVERITY_ERROR,
									"No se pudo establecer la comunicación (GST-TRW).\n Por favor intente nuevamente.",
									null));
						} else if (ste.getCause().getClass().equals(UnknownHostException.class)) {
							LogACGHelper.escribirLog(null,
									"No se pudo establecer la comunicación (GST-HNC): |" + ste.getMessage() + "|");
							FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(
									FacesMessage.SEVERITY_ERROR,
									"No se pudo establecer la comunicación (GST-HNC).\n Por favor intente nuevamente.",
									null));
						} else {
							LogACGHelper.escribirLog(null, "Invocar GCSI. Error ejecutando el WS de datosPlataforma: |"
									+ ste.getMessage() + "|");
							FacesContext.getCurrentInstance().addMessage(null,
									new FacesMessage(FacesMessage.SEVERITY_ERROR,
											"Error ejecutando el WS de datosPlataforma: |" + ste.getMessage() + "|",
											null));
						}
					} else {
						LogACGHelper.escribirLog(null,
								"Invocar GCSI. Error ejecutando el WS de consulta: |" + ste.getMessage() + "|");

						FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
								"Error ejecutando el WS de datosPlataforma: |" + ste.getMessage() + "|", null));
					}

					FacesContext.getCurrentInstance().addMessage(null,
							new FacesMessage(FacesMessage.SEVERITY_ERROR, "No se encontraron datosPlataforma", null));

					return;
				} catch (Exception e) {
					FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
							"Error ejecutando el WS de consulta de datosPlataforma: |" + e.getMessage() + "|", null));
					FacesContext.getCurrentInstance().validationFailed();

					LogACGHelper.escribirLog(null,
							"Invocar GCSI. Excepcion ejecutando el WS de datosPlataforma: |" + e.getMessage() + "|");

					return;
				}

				invocarGCSI("est", "", "", "");
			}
		} catch (Exception e) {
			LogACGHelper.escribirLog(null, e.getMessage());
		}
	}

	private void limpiarPantalla() {
		this.cerrarModal = "mpVisualizar";
		this.lote = null;
		this.facturas.clear();
		this.cantidadFacturas = 0;
		this.totalAPagar = 0F;
		this.loteTotalmenteAprobado = false;
		this.confirmarMonto = null;
		this.confirmarVueltoAEntregar = 0f;
		this.confirmarVueltoAEntregarColor = "white";
		this.confirmarVueltoAEntregarBackgroundColor = "red";
		this.codigoRetornoAnterior = CodigoRetorno.M_0000;
		/*
		 * HtmlInputText inp = new HtmlInputText();
		 * 
		 * FacesContext.getCurrentInstance().getApplication().createComponent(
		 * HtmlInputText.COMPONENT_TYPE);
		 */
	}

	private void obtenerMenu() {
		menuLateralTop = "";
		menuLateralBottom = "";

		if (oTurnoCaja != null) {
			menuLateralBottom += "<button type=\"button\" class=\"ui-button ui-widget ui-state-default ui-corner-all ui-button-text-only\" onclick=\"callScript('cie','','','');\" style=\"width: 180px;\">CIERRE</button>";
			this.showBtnApertura = false;
			this.showBtnCierre = true;
			this.showBtnManual = true;
			this.showBtnBarra = true;
			this.showBtnInforme = true;
		} else {
			menuLateralBottom += "<button type=\"button\" class=\"ui-button ui-widget ui-state-default ui-corner-all ui-button-text-only\" onclick=\"callScript('ape','','','');return false;\" style=\"width: 180px;\">APERTURA</button>";
			this.showBtnApertura = true;
			this.showBtnCierre = false;
			this.showBtnManual = false;
			this.showBtnBarra = false;
			this.showBtnInforme = false;
		}
	}

	private void verificarEstadoTerminal() {
		ConsultarTurnoCajaAsignadoRequest oReq = null;
		ConsultarTurnoCajaAsignadoResponse oResp = null;

		try {
			oReq = new ConsultarTurnoCajaAsignadoRequest();
			oReq.setCodMnemonicoOperador(this.getOperadora());
			oReq.setIdMayorista(this.getUsuario().getIdMayorista());
			oReq.setIdCliente(this.getUsuario().getIdCliente());
			oReq.setIdCanal("WEBINTRA");
			oReq.setUsuario(this.getUsuario().getUsername());
			oReq.setPwd(this.getUsuario().getPassword());

			oResp = GCSIServiceHelper.getGatewayGCSI(CfgTimeout.DEFAULT).consultarTurnoCajaAsignado(oReq);

			if (!CodigoRetorno.V_0002.equals(oResp.getCodigo()) && !CodigoRetorno.M_0000.equals(oResp.getCodigo()))
				inicializadoGCSI = false;
			else {
				if (CodigoRetorno.V_0002.equals(oResp.getCodigo()))
					oTurnoCaja = null;
				else
					oTurnoCaja = oResp.getTurnoCaja();

				inicializadoGCSI = true;
			}

			obtenerMenu();
		} catch (Exception e) {
			cerrarModal = "mpCerrar";

			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_FATAL, "Se produjo una excepcion.", null));

			LogACGHelper.escribirLog(null, "Excepcion en metodo verificarEstadoTerminal=|" + e.getMessage() + "|");
		}
	}

	private void realizarAperturaTerminal() {
		AbrirTurnoRequest oReq = null;
		AbrirTurnoResponse oResp = null;

		try {
			oReq = new AbrirTurnoRequest();
			oReq.setCodMnemonicoOperador(this.getOperadora());
			oReq.setIdMayorista(this.getUsuario().getIdMayorista());
			oReq.setIdCliente(this.getUsuario().getIdCliente());
			oReq.setIdCanal("WEBINTRA");
			oReq.setUsuario(this.getUsuario().getUsername());
			oReq.setPwd(this.getUsuario().getPassword());

			oResp = GCSIServiceHelper.getGatewayGCSI(CfgTimeout.DEFAULT).abrirTurno(oReq);

			if (!CodigoRetorno.M_0000.equals(oResp.getCodigo())) {
				oTurnoCaja = null;
				FacesContext.getCurrentInstance().addMessage(null,
						new FacesMessage(FacesMessage.SEVERITY_ERROR, oResp.getMensaje(), null));
			} else
				oTurnoCaja = oResp.getTurnoCaja();

			obtenerMenu();
		} catch (Exception e) {
			cerrarModal = "mpCerrar";

			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_FATAL, "Se produjo una excepcion.", null));

			LogACGHelper.escribirLog(null, "Excepcion en metodo realizarAperturaTerminal=|" + e.getMessage() + "|");
		}
	}

	private void realizarCierreTerminal() {
		CerrarTurnoRequest oReq = null;
		CerrarTurnoResponse oResp = null;

		try {
			oReq = new CerrarTurnoRequest();
			oReq.setCodMnemonicoOperador(this.getOperadora());
			oReq.setIdMayorista(this.getUsuario().getIdMayorista());
			oReq.setIdCliente(this.getUsuario().getIdCliente());
			oReq.setIdCanal("WEBINTRA");
			oReq.setUsuario(this.getUsuario().getUsername());
			oReq.setPwd(this.getUsuario().getPassword());
			oReq.setIdTerminal(oTurnoCaja.getTerminal().getId());
			
			oResp = GCSIServiceHelper.getGatewayGCSI(CfgTimeout.DEFAULT).cerrarTurno(oReq);

			if (!CodigoRetorno.M_0000.equals(oResp.getCodigo())) {
				if (CodigoRetorno.V_0003.equals(oResp.getCodigo()))
					oTurnoCaja = null;
				else
					FacesContext.getCurrentInstance().addMessage(null,
							new FacesMessage(FacesMessage.SEVERITY_ERROR, oResp.getMensaje(), null));
			} else
				oTurnoCaja = null;

			obtenerMenu();
		} catch (Exception e) {
			cerrarModal = "mpCerrar";

			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_FATAL, "Se produjo una excepcion.", null));

			LogACGHelper.escribirLog(null, "Excepcion en metodo realizarAperturaTerminal=|" + e.getMessage() + "|");
		}
	}

	private void dialogoCobranzaManual() {
		StringBuilder oSB = null;

		this.modalPanelTitulo = "PAGO MANUAL SIN FACTURA";
		transaccionDatos = "<h2>Ingrese descripcion de empresa:</h2>";
		showCmbEmpresa = false;
		showCmbTipoCobranza = false;
		showCmbFormaDePago = false;
		showLstColisionFactura = false;
		showSetImporteFactura = false;

		this.facturaAnula = "";
		this.facturaImpMinNeg = 0f;
		this.facturaImpMaxNeg = 0f;
		this.facturaImpMinPos = 0f;
		this.facturaImpMaxPos = 0f;
		this.codEmpresaSeleccionada = "";
		this.idModSeleccionado = "";
		this.operacionAEjecutar = "";

		this.cerrarModal = "mpMostrar";

		oSB = new StringBuilder();
		oSB.append("<input type=\"text\"");
		oSB.append("name=\"inputValor\" id=\"inputValor\" minlength=\"3\" maxlength=\"100\"");
		oSB.append(" value=\"\" style=\"border-radius: 5px;padding: 5px;width: 99%;margin-bottom: 5px;\" autofocus>");
		oSB.append("<button type=\"button\" accesskey=\"f2\"");
		oSB.append(
				" class=\"ui-button ui-widget ui-state-default ui-corner-all ui-button-text-only ui-button ui-widget ui-state-default ui-corner-all ui-button-text-only\"");
		oSB.append(" name=\"btnCONTINUAR\" id=\"btnCONTINUAR\"");
		oSB.append(
				" onclick=\"callScript([{name:'parametro', value:'cob'}, {name:'tipo', value:'getemp'}, {name:'valor', value: document.getElementById('inputValor').value}, {name:'texto', value:''}])\">");
		oSB.append("<span class=\"ui-button-text ui-c\">CONTINUAR (F2)</span></button>");
		contenido = oSB.toString();
	}

	private String armarHeadEstructuraCampo(Campo campoFormulario, Integer numCampo) {
		StringBuilder oCMBCampos = null;

		oCMBCampos = new StringBuilder();
		oCMBCampos.append("<div id=\"campo_".concat(numCampo.toString()).concat("\""));
		oCMBCampos.append(" class=\"camposGIRE\">");
		oCMBCampos.append("<div id=\"eti_".concat(campoFormulario.getNombre()).concat("\""));
		oCMBCampos.append(" class=\"etiquetaGIRE\">");
		oCMBCampos.append(campoFormulario.getEtiqueta());
		oCMBCampos.append("</div>");
		oCMBCampos.append("<div id=\"cam_".concat(campoFormulario.getNombre()).concat("\""));
		oCMBCampos.append(" class=\"nombreCampoGIRE\" valor=\"".concat(campoFormulario.getNombre()).concat("\""));
		oCMBCampos.append(" tipo=\"".concat(campoFormulario.getTipoComponenteVisual()).concat("\">"));

		return oCMBCampos.toString();
	}

	private String armarFootEstructuraCampo(Campo campoFormulario, Integer numCampo) {
		StringBuilder oCMBCampos = null;

		oCMBCampos = new StringBuilder();
		oCMBCampos.append("</div>");
		oCMBCampos.append("</div>");

		return oCMBCampos.toString();
	}

	private String armarComponenteInput(Campo campoFormulario, Integer numCampo) {
		StringBuilder oCMBCampos = null;

		oCMBCampos = new StringBuilder();
		oCMBCampos.append(armarHeadEstructuraCampo(campoFormulario, numCampo));
		oCMBCampos.append("<input id=\"".concat(campoFormulario.getNombre()).concat("\""));

		switch (campoFormulario.getTipo().trim().toUpperCase()) {
		case "ALF": {
			oCMBCampos.append(" type=\"text\"");
			break;
		}
		case "CBA": {
			oCMBCampos.append(" type=\"text\"");
			break;
		}
		case "IMP": {
			oCMBCampos.append(" type=\"number\"");
			break;
		}
		case "FEC": {
			oCMBCampos.append(" type=\"date\"");
			break;
		}
		case "LOG": {
			oCMBCampos.append(" type=\"checkbox\"");
			break;
		}
		default: {
			break;
		}
		}

		oCMBCampos.append(" maxlength=\"".concat(campoFormulario.getLongitud()).concat("\""));
		oCMBCampos.append("/ class=\"valorCampoGIRE\">");

		oCMBCampos.append(armarFootEstructuraCampo(campoFormulario, numCampo));

		return oCMBCampos.toString();
	}

	private String armarComponeneteComboEstatico(Campo campoFormulario, Integer numCampo) {
		StringBuilder oCMBCampos = null;

		oCMBCampos = new StringBuilder();

		if (campoFormulario.getListaValores() != null) {
			oCMBCampos.append(armarHeadEstructuraCampo(campoFormulario, numCampo));
			oCMBCampos.append("<select id=\"cmb_".concat(campoFormulario.getNombre()).concat("\""));
			oCMBCampos.append(" style=\"border-radius: 5px;padding: 5px;width: 99%;margin-bottom: 5px;\" autofocus");
			oCMBCampos.append(" class=\"valorCampoGIRE\">");

			for (Entry oEntryAux : campoFormulario.getListaValores().getEntry()) {
				oCMBCampos.append("<option value=\"".concat(oEntryAux.getKey()).concat("\">"));
				oCMBCampos.append(oEntryAux.getValue());
				oCMBCampos.append("</option>");
			}

			oCMBCampos.append("</select>");
			oCMBCampos.append(armarFootEstructuraCampo(campoFormulario, numCampo));
		}

		return oCMBCampos.toString();
	}

	// private String armarComponeneteComboDinamico(Campo campoFormulario) {}

	private String armarComponeneteLabel(Campo campoFormulario, Integer numCampo) {
		StringBuilder oCMBCampos = null;

		oCMBCampos = new StringBuilder();
		oCMBCampos.append(armarHeadEstructuraCampo(campoFormulario, numCampo));

		oCMBCampos.append("<span id=\"".concat(campoFormulario.getNombre()).concat("\">"));
		oCMBCampos.append(campoFormulario.getEtiqueta());
		oCMBCampos.append("</span>");
		oCMBCampos.append("</div>");

		return oCMBCampos.toString();
	}

	private void obtenerEmpresasCobManual(String filtroEmpresa) {
		ConsultarEmpresaRequestACG oReq = null;
		ConsultarEmpresaResponseACG oResp = null;
		this.setCodEmpresaSeleccionada("");

		try {
			this.modalPanelTitulo = "PAGO MANUAL SIN FACTURA";
			transaccionDatos = "<h2>Seleccione empresa:</h2>";
			this.cerrarModal = "mpActualizar";

			oReq = new ConsultarEmpresaRequestACG();
			oReq.setCodMnemonicoOperador(this.getOperadora());
			oReq.setIdMayorista(this.getUsuario().getIdMayorista());
			oReq.setIdCliente(this.getUsuario().getIdCliente());
			oReq.setIdCanal("WEBINTRA");
			oReq.setUsuario(this.getUsuario().getUsername());
			oReq.setPwd(this.getUsuario().getPassword());
			oReq.setCodPuesto(((TerminalGIRE) oTurnoCaja.getTerminal()).getCodPuesto());
			oReq.setIdTurnoCaja(oTurnoCaja.getId());
			oReq.setIdTerminal(oTurnoCaja.getTerminal().getId());
			oReq.setDescEmp(filtroEmpresa);
			oReq.getTiposCobranza().add("CSF");
			oReq.getTiposCobranza().add("SFM");

			oResp = GCSIServiceHelper.getGatewayGCSI(CfgTimeout.DEFAULT).consultarEmpresaConvenio(oReq);

			if (CodigoRetorno.M_0000.equals(oResp.getCodigo())) {
				this.setShowCmbEmpresa(true);
				this.empresas = new ArrayList<EmpresaRapipago>();

				for (EmpresaRapipago oEmpAux : oResp.getListaEmpresas())
					this.empresas.add(oEmpAux);

				this.operacionAEjecutar = "paramcobman";
			} else {
				this.cerrarModal = "mpCerrar";

				FacesContext.getCurrentInstance().addMessage(null,
						new FacesMessage(FacesMessage.SEVERITY_ERROR, oResp.getMensaje(), null));
			}
		} catch (Exception e) {
			this.cerrarModal = "mpCerrar";

			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_FATAL, "Se produjo una excepcion.", null));

			LogACGHelper.escribirLog(null, "Excepcion en metodo obtenerEmpresasCobManual=|" + e.getMessage() + "|");
		}
	}

	private void obtenerParametriaCobranzaManual() {
		try {
			this.modalPanelTitulo = "PAGO MANUAL SIN FACTURA";
			this.cerrarModal = "mpActualizar";

			if ("GIRE".equals(this.getOperadora())) {
				showCmbEmpresa = false;
				showCmbTipoCobranza = false;
				showCmbFormaDePago = false;
				showLstColisionFactura = false;
				this.operacionAEjecutar = "";
				contenido = obtenerParametriaCobranzaManualGIRE();
			}

			// OJO Completar para el resto de las operadoras

			if ("".equals(contenido) || contenido.isEmpty())
				this.cerrarModal = "mpCerrar";
		} catch (Exception e) {
			LogACGHelper.escribirLog(null, "Excepcion en metodo realizarCobranzaManual=|" + e.getMessage() + "|");

			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_FATAL, "Se produjo una excepcion.", null));

			this.cerrarModal = "mpCerrar";
		}
	}

	private String obtenerParametriaCobranzaManualGIRE() {
		ObtenerParamFacSinSopPapelGIRERequest oReq = null;
		ObtenerParamFacSinSopPapelGIREResponse oResp = null;
		StringBuilder oCMBCampos = null;
		Integer numCampo = 1;

		try {
			for (EmpresaRapipago oEmpAux : this.getEmpresas()) {
				if (this.getCodEmpresaSeleccionada().equals(oEmpAux.getCodEmp())) {
					transaccionDatos = "<h2>" + oEmpAux.getDescEmp() + "</h2>";

					try {
						facturaImpMinNeg = Float.parseFloat(oEmpAux.getTopes().getMinimoNegativo());
					} catch (Exception e) {
						facturaImpMinNeg = 0f;
					}

					try {
						facturaImpMaxNeg = Float.parseFloat(oEmpAux.getTopes().getMaximoNegativo());
					} catch (Exception e) {
						facturaImpMaxNeg = 0f;
					}

					try {
						facturaImpMinPos = Float.parseFloat(oEmpAux.getTopes().getMinimoPositivo());
					} catch (Exception e) {
						facturaImpMinPos = 0f;
					}

					try {
						facturaImpMaxPos = Float.parseFloat(oEmpAux.getTopes().getMaximoPositivo());
					} catch (Exception e) {
						facturaImpMaxPos = 0f;
					}

					break;
				}

				numCampo++;
			}

			for (Modalidad oModAux : this.getModalidadesCobranza()) {
				if (this.getIdModSeleccionado().equals(oModAux.getIdMod())) {
					transaccionDatos = transaccionDatos.concat("<h3>" + oModAux.getDescMod() + "</h3>");

					facturaAnula = oModAux.getAnula();
					break;
				}
			}

			this.cerrarModal = "mpActualizar";

			oReq = new ObtenerParamFacSinSopPapelGIRERequest();
			oReq.setCodMnemonicoOperador(this.getOperadora());
			oReq.setIdMayorista(this.getUsuario().getIdMayorista());
			oReq.setIdCliente(this.getUsuario().getIdCliente());
			oReq.setIdCanal("WEBINTRA");
			oReq.setUsuario(this.getUsuario().getUsername());
			oReq.setPwd(this.getUsuario().getPassword());
			oReq.setCodPuesto(((TerminalGIRE) oTurnoCaja.getTerminal()).getCodPuesto());
			oReq.setIdTurnoCaja(oTurnoCaja.getId());
			oReq.setIdTerminal(oTurnoCaja.getTerminal().getId());
			oReq.setIdMod(this.getIdModSeleccionado());
			oReq.setAnula(facturaAnula);
			oReq.setIdFormaDePago(this.getIdFormaDePagoSeleccionado());
			oReq.setImpMinNeg(facturaImpMinNeg);
			oReq.setImpMaxNeg(facturaImpMaxNeg);
			oReq.setImpMinPos(facturaImpMinPos);
			oReq.setImpMaxPos(facturaImpMaxPos);

			oResp = (ObtenerParamFacSinSopPapelGIREResponse) GCSIServiceHelper.getGatewayGCSI(CfgTimeout.DEFAULT)
					.obtenerParametriaFacturaSinSoportePapel(oReq);

			if (CodigoRetorno.M_0000.equals(oResp.getCodigo())) {
				oCMBCampos = new StringBuilder();
				oCMBCampos.append("<div id=\"contenedorCampos\">");

				for (Campo oCampAux : oResp.getCampos()) {
					switch (oCampAux.getTipoComponenteVisual().trim().toUpperCase()) {

					case "TEXT_FIELD": {
						oCMBCampos.append(armarComponenteInput(oCampAux, numCampo));
						break;
					}
					case "COMBO_BOX_ESTATICO": {
						oCMBCampos.append(armarComponeneteComboEstatico(oCampAux, numCampo));
						break;
					}
					case "COMBO_BOX_DINAMICO": {
						// oCMBCampos.append(armarComponeneteComboDinamico(oCampAux));
						break;
					}
					case "LABEL": {
						oCMBCampos.append(armarComponeneteLabel(oCampAux, numCampo));
						break;
					}
					case "CHECK_BOX": {
						oCMBCampos.append(armarComponenteInput(oCampAux, numCampo));
						break;
					}

					}
				}

				oCMBCampos.append("</div>");
				oCMBCampos.append(
						"<button type=\"button\" accesskey=\"f2\" class=\"ui-button ui-widget ui-state-default ui-corner-all ui-button-text-only ui-button ui-widget ui-state-default ui-corner-all ui-button-text-only\" name=\"btnCONTINUAR\" id=\"btnCONTINUAR\" onclick=\"gcsiScriptsFCNS.procesarSinFacGIRE()\"><span class=\"ui-button-text ui-c\">CONTINUAR (F2)</span></button>");

				return oCMBCampos.toString();

				// System.out.println("Contenido: |" + contenido + "|");
			} else {
				cerrarModal = "mpCerrar";

				FacesContext.getCurrentInstance().addMessage(null,
						new FacesMessage(FacesMessage.SEVERITY_ERROR, oResp.getMensaje(), null));
			}
		} catch (Exception e) {
			cerrarModal = "mpCerrar";

			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_FATAL, "Se produjo una excepcion.", null));

			LogACGHelper.escribirLog(null, "Excepcion en metodo realizarCobranzaManual=|" + e.getMessage() + "|");
		}

		return "";
	}

	private void obtenerFacturaSinSoportePapelGIRE(String jsonParametros) {
		ObtenerFacturaSinSoportePapelGIRERequest oReq = null;
		ObtenerFacturaSinSoportePapelResponseACG oResp = null;
		DatosFormulario oDtaForm = null;
		ObtenerFacturaSinSoportePapelGIRERequest.DatosFormulario.Entry oEntryDatForm = null;
		List<ItemGCSISinFac> oLstParams = null;
		List<String> oLstUpdt = null;
		ObjectMapper mapeadorJSON = null;
		JavaType jsonObjType = null;
		SimpleDateFormat oSDF = null;

		try {
			this.cerrarModal = "mpCerrar";
			oReq = new ObtenerFacturaSinSoportePapelGIRERequest();
			oReq.setCodMnemonicoOperador(this.getOperadora());
			oReq.setIdMayorista(this.getUsuario().getIdMayorista());
			oReq.setIdCliente(this.getUsuario().getIdCliente());
			oReq.setIdCanal("WEBINTRA");
			oReq.setUsuario(this.getUsuario().getUsername());
			oReq.setPwd(this.getUsuario().getPassword());
			oReq.setCodPuesto(((TerminalGIRE) oTurnoCaja.getTerminal()).getCodPuesto());
			oReq.setIdTurnoCaja(oTurnoCaja.getId());
			oReq.setIdTerminal(oTurnoCaja.getTerminal().getId());
			oReq.setCodEmp(Long.parseLong(this.getCodEmpresaSeleccionada()));
			oReq.setIdMod(this.getIdModSeleccionado());
			oReq.setAnula(this.facturaAnula);
			oReq.setIdFormaDePago(this.getIdFormaDePagoSeleccionado());
			oReq.setImpMinNeg(facturaImpMinNeg);
			oReq.setImpMaxNeg(facturaImpMaxNeg);
			oReq.setImpMinPos(facturaImpMinPos);
			oReq.setImpMaxPos(facturaImpMaxPos);

			mapeadorJSON = new ObjectMapper();
			mapeadorJSON.configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true);
			mapeadorJSON.configure(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT, true);
			mapeadorJSON.configure(DeserializationFeature.FAIL_ON_MISSING_CREATOR_PROPERTIES, false);
			mapeadorJSON.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
			mapeadorJSON.configure(DeserializationFeature.FAIL_ON_IGNORED_PROPERTIES, false);
			mapeadorJSON.configure(DeserializationFeature.FAIL_ON_MISSING_EXTERNAL_TYPE_ID_PROPERTY, false);

			jsonObjType = mapeadorJSON.getTypeFactory().constructType(new TypeReference<List<ItemGCSISinFac>>() {
			});

			oLstParams = mapeadorJSON.readValue(jsonParametros, jsonObjType);

			if (oLstParams != null) {
				oDtaForm = new DatosFormulario();

				for (ItemGCSISinFac oItmParamAux : oLstParams) {
					oEntryDatForm = new ObtenerFacturaSinSoportePapelGIRERequest.DatosFormulario.Entry();
					oEntryDatForm.setKey(oItmParamAux.getNombre());

					if ("FEC".equals(oItmParamAux.getTipo())) {
						oSDF = new SimpleDateFormat("dd/MM/yyyy");

						oEntryDatForm.setValue(oSDF.format(oSDF.parse(oItmParamAux.getValor())));
					} else
						oEntryDatForm.setValue(oItmParamAux.getValor());

					oDtaForm.getEntry().add(oEntryDatForm);
				}

				oReq.setDatosFormulario(oDtaForm);
			}

			oResp = GCSIServiceHelper.getGatewayGCSI(CfgTimeout.DEFAULT).obtenerFacturaSinSoportePapel(oReq);

			if (CodigoRetorno.M_0000.equals(oResp.getCodigo())) {
				showLstColisionFactura = false;

				if ("ESC".equals(((FacturaGIRE) oResp.getFactura()).getCodTI().toUpperCase().trim())
						|| ("CAC".equals(((FacturaGIRE) oResp.getFactura()).getCodTI().toUpperCase().trim()))
								&& oResp.getFactura().getImporteTotal().compareTo(0f) > 0) {
					oResp.getFactura().setImporteAbonado(oResp.getFactura().getImporteTotal());
					agregarFacturaAGrilla(oResp.getFactura());
					this.cerrarModal = "mpCerrar";
					this.operacionAEjecutar = "";

					recalcularTotales();

					oLstUpdt = new ArrayList<String>();
					oLstUpdt.add("operacionesActivas");
					oLstUpdt.add("total");
					oLstUpdt.add("contenedorBotonesInferiores");
					oLstUpdt.add("ConfirmarMontoACobrar");

					PrimeFaces.current().ajax().update(oLstUpdt);
				} else {
					this.setFacturaSeleccionada(oResp.getFactura());
					showSetImporteFactura = true;
					this.cerrarModal = "mpActualizar";
					this.operacionAEjecutar = "setfacselimp";

				}
			} else {
				cerrarModal = "mpCerrar";

				FacesContext.getCurrentInstance().addMessage(null,
						new FacesMessage(FacesMessage.SEVERITY_ERROR, oResp.getMensaje(), null));
			}
		} catch (Exception e) {
			cerrarModal = "mpCerrar";

			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_FATAL, "Se produjo una excepcion.", null));

			LogACGHelper.escribirLog(null,
					"Excepcion en metodo obtenerFacturaSinSoportePapelCobranzaManual=|" + e.getMessage() + "|");
		}
	}

	private void procesarCobranzaDeFactura() {
		ConfirmarRequestACG oReq = null;
		ConfirmarResponseACG oResp = null;
		boolean codBarra1Igual = false;
		boolean codBarra2Igual = false;
		boolean codEmpIgual = false;
		boolean codDiscriminadorIgual = false;
		StringBuilder oSBClave = null;
		SimpleDateFormat oFmtDate = null;
		Calendar oCal = null;
		Random oRnd = null;
		Integer cantFactAprob = 0;

		try {
			loteTotalmenteAprobado = false;
			
			oReq = new ConfirmarRequestACG();
			oReq.setCodMnemonicoOperador(this.getOperadora());
			oReq.setIdMayorista(this.getUsuario().getIdMayorista());
			oReq.setIdCliente(this.getUsuario().getIdCliente());
			oReq.setIdCanal("WEBINTRA");
			oReq.setUsuario(this.getUsuario().getUsername());
			oReq.setPwd(this.getUsuario().getPassword());
			oReq.setCodPuesto(((TerminalGIRE) oTurnoCaja.getTerminal()).getCodPuesto());
			oReq.setIdTurnoCaja(oTurnoCaja.getId());
			oReq.setIdTerminal(oTurnoCaja.getTerminal().getId());
			oReq.setCodRetornoAnterior(this.codigoRetornoAnterior);

			if (this.lote == null) {
				this.lote = new Lote();
				
				oSBClave = new StringBuilder();
				oCal = Calendar.getInstance();
				oRnd = new Random();
				oFmtDate = new SimpleDateFormat("yyyyMMddHHmmssSSS");

				oSBClave.append(oFmtDate.format(oCal.getTime()));
				oSBClave.append(String.format("%04d", this.getUsuario().getIdMayorista()));
				oSBClave.append(String.format("%07d", this.getUsuario().getIdCliente()));
				oSBClave.append(String.format("%04d", oRnd.nextInt(9999)));

				lote.setIdAutogeneradoFrontEnd(oSBClave.toString());

				codigoRetornoAnterior = CodigoRetorno.M_0000;
			}

			if (this.getFacturas().size() == 0) {
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
						"No se han seleccionado facturas para confirmar su pago.", null));

				return;
			}
			
			if (lote.getFacturas() == null) {
				lote.setFacturas(new Facturas());

				oSBClave = new StringBuilder();
				oCal = Calendar.getInstance();
				oRnd = new Random();
				oFmtDate = new SimpleDateFormat("yyyyMMddHHmmssSSS");

				oSBClave.append(oFmtDate.format(oCal.getTime()));
				oSBClave.append(String.format("%04d", this.getUsuario().getIdMayorista()));
				oSBClave.append(String.format("%07d", this.getUsuario().getIdCliente()));
				oSBClave.append(String.format("%04d", oRnd.nextInt(9999)));

				lote.setIdAutogeneradoFrontEnd(oSBClave.toString());

				codigoRetornoAnterior = CodigoRetorno.M_0000;
			}

			for (FacturaGCSIWeb oFAux : this.getFacturas()) {
				lote.getFacturas().getFacturaGIREOrFacturaPNETOrFacturaWU().clear();

				if (oFAux.isIncluir())
					lote.getFacturas().getFacturaGIREOrFacturaPNETOrFacturaWU().add(oFAux.getFactura());
			}

			if (lote.getFacturas().getFacturaGIREOrFacturaPNETOrFacturaWU().size() > 0) {
				oReq.setLote(lote);
				// oReq.setCodRetornoAnterior(value);gonza
				oResp = GCSIServiceHelper.getGatewayGCSI(CfgTimeout.DEFAULT).confirmar(oReq);

				this.codigoRetornoAnterior = oResp.getCodigo();

				if (oResp.getLote() != null)
					this.lote = oResp.getLote();

				//if (CodigoRetorno.M_0000.equals(oResp.getCodigo())) {
					for (FacturaGCSIWeb oFAuxWeb : this.getFacturas()) {
						codBarra1Igual = false;
						codBarra2Igual = false;
						codEmpIgual = false;
						codDiscriminadorIgual = false;

						for (Factura oFAux : this.lote.getFacturas().getFacturaGIREOrFacturaPNETOrFacturaWU()) {
							if (oFAuxWeb.getFactura().getCodBarra1() != null) {
								if (oFAux.getCodBarra1() != null) {
									if (oFAuxWeb.getFactura().getCodBarra1().equals(oFAux.getCodBarra1()))
										codBarra1Igual = true;
									else
										codBarra1Igual = false;
								} else
									codBarra1Igual = true;
							} else
								codBarra1Igual = true;

							if (oFAuxWeb.getFactura().getCodBarra2() != null) {
								if (oFAux.getCodBarra2() != null) {
									if (oFAuxWeb.getFactura().getCodBarra2().equals(oFAux.getCodBarra2()))
										codBarra2Igual = true;
									else
										codBarra2Igual = false;
								} else
									codBarra2Igual = true;
							} else
								codBarra2Igual = true;

							if (oFAuxWeb.getFactura().getCodEmp() != null) {
								if (oFAux.getCodEmp() != null) {
									if (oFAuxWeb.getFactura().getCodEmp().equals(oFAux.getCodEmp()))
										codEmpIgual = true;
									else
										codEmpIgual = false;
								} else
									codEmpIgual = true;
							} else
								codEmpIgual = true;

							if (oFAuxWeb.getFactura().getCodDiscriminador() != null) {
								if (oFAux.getCodDiscriminador() != null) {
									if (oFAuxWeb.getFactura().getCodDiscriminador()
											.equals(oFAux.getCodDiscriminador()))
										codDiscriminadorIgual = true;
									else
										codDiscriminadorIgual = false;
								} else
									codDiscriminadorIgual = true;
							} else
								codDiscriminadorIgual = true;

							if (codBarra1Igual && codBarra2Igual && codEmpIgual && codDiscriminadorIgual) {
								oFAuxWeb.setFactura(oFAux);

								if("0".equals(oFAux.getResultado())) 
									cantFactAprob++;
								
								break;
							}
						}
					}
				/*} else
					FacesContext.getCurrentInstance().addMessage(null,
							new FacesMessage(FacesMessage.SEVERITY_ERROR, oResp.getMensaje(), null));*/

				loteTotalmenteAprobado = (cantFactAprob == this.getFacturas().size() && CodigoRetorno.M_0000.equals(oResp.getCodigo()));
				
				this.confirmarMonto = null;
				this.confirmarVueltoAEntregar = 0f;
				this.confirmarVueltoAEntregarColor = "white";
				this.confirmarVueltoAEntregarBackgroundColor = "red";
				
				if (!CodigoRetorno.M_0000.equals(oResp.getCodigo())) {
					FacesContext.getCurrentInstance().addMessage(null,
							new FacesMessage(FacesMessage.SEVERITY_ERROR, oResp.getMensaje(), null));
				}
			} else {
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
						"No se han seleccionado facturas para confirmar su pago.", null));

			}
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_FATAL, "Se produjo una excepcion.", null));

			LogACGHelper.escribirLog(null, "Excepcion en metodo obtenerEmpresasCobManual=|" + e.getMessage() + "|");
		}

		cerrarModal = "mpcCerrar";
	}

	private void procesarImpresionTicketDeFactura(String idFactura) {
		Long idFacturaNum = 0L;

		try {
			if (idFactura == null || idFactura.isEmpty())
				FacesContext.getCurrentInstance().addMessage(null,
						new FacesMessage(FacesMessage.SEVERITY_ERROR, "No se ha informado una factura.", null));

			idFacturaNum = Long.parseLong(idFactura);

			for (Factura oFacAux : this.lote.getFacturas().getFacturaGIREOrFacturaPNETOrFacturaWU()) {
				if (idFacturaNum.compareTo(oFacAux.getId()) == 0) {
					if (oFacAux.getComprobantes() != null && oFacAux.getComprobantes().size() > 0)
						ticket = oFacAux.getComprobantes().get(0).getDetalleComprobante();
					else
						ticket = "";

					break;
				}
			}

			if (ticket != null && !ticket.isEmpty())
				this.mostrarTicket();
			else
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
						"La factura seleccionada no cuenta con comprobante.", null));
		} catch (Exception e) {
			cerrarModal = "mpCerrar";

			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR, "Se ha producido un error.", null));

			LogACGHelper.escribirLog(null, "Exception en procesarImpresionTicketDeFactura: |" + e.getMessage() + "|");
		}
	}

	private void procesarImpresionTicketsDeLote() {
		StringBuilder oSB = null;
		boolean existenComprobantes = false;

		try {
			if (this.lote == null)
				FacesContext.getCurrentInstance().addMessage(null,
						new FacesMessage(FacesMessage.SEVERITY_ERROR, "No existe lote para imprimir facturas.", null));
			else if (this.lote.getFacturas() == null
					|| this.lote.getFacturas().getFacturaGIREOrFacturaPNETOrFacturaWU() == null
					|| this.lote.getFacturas().getFacturaGIREOrFacturaPNETOrFacturaWU().size() == 0)
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
						"El lote no cuenta con facturas para imprimir.", null));

			oSB = new StringBuilder();
			oSB.append("<fieldset>");

			for (Factura oFacAux : this.lote.getFacturas().getFacturaGIREOrFacturaPNETOrFacturaWU()) {
				if ("RESPOPE".equals(oFacAux.getEstado().getCodMnemonico())
						&& "0".equals(oFacAux.getResultado().trim())
						&& "OK".equals(oFacAux.getResultadoMsg().trim().toUpperCase())) {
					if (oFacAux.getComprobantes() != null && oFacAux.getComprobantes().size() > 0) {
						oSB.append(oFacAux.getComprobantes().get(0).getDetalleComprobante());
						existenComprobantes = true;
					}
				}
			}

			if (!existenComprobantes) {
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
						"El lote no cuenta con facturas para imprimir.", null));

				this.ticket = "";
			} else {
				oSB.append("</fieldset>");
				oSB.append("<br /><br /><br />");

				this.ticket = oSB.toString();
				this.mostrarTicket();
			}
		} catch (Exception e) {
			cerrarModal = "mpCerrar";

			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR, "Se ha producido un error.", null));

			LogACGHelper.escribirLog(null, "Exception en procesarImpresionTicketDeFactura: |" + e.getMessage() + "|");
		}
	}

	private void procesarExportacionTicketDeFactura(String idFactura) {
		FacesContext oFacesCtx = null;
		ExternalContext oExtCtx = null;
		Document oPdfDoc = null;
		OutputStream oOutStream = null;
		PdfWriter oPdfWriter = null;
		Font oFont = null;
		Paragraph oParagraph = null;
		Long idFacturaNum = 0L;
		String oStrCompr = "";
		String oNomArch = "";

		try {
			if (idFactura == null || idFactura.isEmpty())
				FacesContext.getCurrentInstance().addMessage(null,
						new FacesMessage(FacesMessage.SEVERITY_ERROR, "No se ha informado una factura.", null));

			idFacturaNum = Long.parseLong(idFactura);

			for (Factura oFacAux : this.lote.getFacturas().getFacturaGIREOrFacturaPNETOrFacturaWU()) {
				if (idFacturaNum.compareTo(oFacAux.getId()) == 0) {
					if (oFacAux.getComprobantes() != null && oFacAux.getComprobantes().size() > 0) {
						oStrCompr = oFacAux.getComprobantes().get(0).getDetalleComprobante();
						oNomArch = "CompTX".concat(oFacAux.getIdTrxOperador());
					} else {
						oStrCompr = "";
						oNomArch = "";
					}

					break;
				}
			}

			if (oStrCompr == null || oStrCompr.isEmpty() || oNomArch == null || oNomArch.isEmpty())
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
						"La factura informada no cuenta con comprobante.", null));
			else {
				oFacesCtx = FacesContext.getCurrentInstance();
				oExtCtx = oFacesCtx.getExternalContext();

				oExtCtx.responseReset();
				oExtCtx.setResponseContentType("application/pdf");
				oExtCtx.setResponseHeader("Pragma", "no-cache");
				oExtCtx.setResponseHeader("Expires", "0");
				oExtCtx.setResponseHeader("Content-Disposition", "attachment; filename=\"" + oNomArch + ".pdf" + "\"");

				// Document doc = new Document(PageSize.A4, 50, 50, 50, 50);
				oPdfDoc = new Document();
				oOutStream = oExtCtx.getResponseOutputStream();
				oPdfWriter = PdfWriter.getInstance(oPdfDoc, oOutStream);
				oPdfDoc.open();

				oFont = new Font(FontFamily.COURIER);
				oParagraph = new Paragraph(oStrCompr, oFont);
				oParagraph.setAlignment(Element.ALIGN_CENTER);
				oPdfDoc.add(oParagraph);

				oPdfDoc.close();
				oPdfWriter.close();

				oFacesCtx.responseComplete();
			}
		} catch (Exception e) {
			cerrarModal = "mpCerrar";

			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL,
					"Se ha producido un error en la exportacion del comprobante.", null));

			LogACGHelper.escribirLog(null,
					"Se ha producido un error en la exportacion del comprobante: |" + e.getMessage() + "|");
		}
	}

	private void dialogoConfirmacionDePago() {
		confirmarMonto = null;
		cerrarModal = "";
		PrimeFaces.current().executeScript("PF('confirmarPanelWV').show();");
	}

	public void dialogoCobranzaCodigoDeBarra() {
		StringBuilder oSB = null;

		this.modalPanelTitulo = "PAGO CON CODIGO DE BARRA";
		this.cerrarModal = "mpMostrar";
		showCmbEmpresa = false;
		showCmbTipoCobranza = false;
		showCmbFormaDePago = false;
		showLstColisionFactura = false;
		showSetImporteFactura = false;

		transaccionDatos = "<h2>Usted esta cobrando:</h2>";
		contenido = "<h4>Ingrese codigo de Barras</h4>";

		oSB = new StringBuilder();
		oSB.append("<input value=\"\" name=\"codigoDeBarra\" id=\"codigoDeBarra\"");
		oSB.append(" style=\"border-radius: 5px;padding: 5px;width: 99%;margin-bottom: 5px;\" autofocus");
		oSB.append(" onkeydown='if(event.keyCode === 38 || event.keyCode === 40) {event.preventDefault();}'/>");
		oSB.append("<button type=\"button\" accesskey=\"f2\"");
		oSB.append(" class=\"ui-button ui-widget ui-state-default ui-corner-all ui-button-text-only\"");
		oSB.append(" name=\"btnCONTINUAR\" id=\"btnCONTINUAR\"");
		oSB.append(
				" onclick=\"callScript([{name:'parametro', value:'cob'}, {name:'tipo', value:'PROCCOBBARRA'}, {name:'valor', value: document.getElementById('codigoDeBarra').value}, {name:'texto', value: ''}])\">");
		oSB.append("<span class=\"ui-button-text ui-c\">CONTINUAR (F2)</span></button>");

		contenido = oSB.toString();
	}

	public void obtenerFacturaConCodigoDeBarra(String barra) {
		ConsultarBarraRequest oReq = null;
		ConsultarBarraResponse oResp = null;

		try {
			this.setFacturaSeleccionada(null);
			this.setIdModSeleccionado("");

			oReq = new ConsultarBarraRequest();
			oReq.setCodMnemonicoOperador(this.getOperadora());
			oReq.setIdMayorista(this.getUsuario().getIdMayorista());
			oReq.setIdCliente(this.getUsuario().getIdCliente());
			oReq.setIdCanal("WEBINTRA");
			oReq.setUsuario(this.getUsuario().getUsername());
			oReq.setPwd(this.getUsuario().getPassword());
			oReq.setCodPuesto(((TerminalGIRE) oTurnoCaja.getTerminal()).getCodPuesto());
			oReq.setIdTurnoCaja(oTurnoCaja.getId());
			oReq.setIdTerminal(oTurnoCaja.getTerminal().getId());
			oReq.setBarra(barra);

			oResp = GCSIServiceHelper.getGatewayGCSI(CfgTimeout.DEFAULT).consultarBarra(oReq);

			if (CodigoRetorno.M_0000.equals(oResp.getCodigo())) {
				if ("GIRE".equals(this.operadora)) {
					procesarRespBarraFacturaGIRE(oResp);
				}

				// OJO Agregar funciones para cuando se agreguen operadoras
			} else {
				cerrarModal = "mpCerrar";

				FacesContext.getCurrentInstance().addMessage(null,
						new FacesMessage(FacesMessage.SEVERITY_ERROR, oResp.getMensaje(), null));
			}
		} catch (Exception e) {
			cerrarModal = "mpCerrar";

			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_FATAL, "Se produjo una excepcion.", null));

			LogACGHelper.escribirLog(null,
					"Excepcion en metodo obtenerFacturaConCodigoDeBarra=|" + e.getMessage() + "|");
		}
	}

	private void procesarRespBarraFacturaGIRE(ConsultarBarraResponse response) {
		if (response.getListaFactura() != null && response.getListaFactura().size() > 0) {
			if (response.getListaFactura() != null && response.getListaFactura().size() > 1) {
				this.setFacturasColision(response.getListaFactura());
				this.cerrarModal = "mpActualizar";
				showLstColisionFactura = true;
				contenido = "";
				this.operacionAEjecutar = "selfaccolision";
			} else {
				showLstColisionFactura = false;

				if (((FacturaGIRE) response.getListaFactura().get(0)).getFormasDePagoHabilitadas().size() > 1) {
					this.setFormasDePago(new ArrayList<FormasPago>());
					this.setIdFormaDePagoSeleccionado("");
					this.setFormasDePago(
							((FacturaGIRE) response.getListaFactura().get(0)).getFormasDePagoHabilitadas());
					this.setFacturaSeleccionada(((FacturaGIRE) response.getListaFactura().get(0)));
					this.setShowCmbFormaDePago(true);
					this.cerrarModal = "mpActualizar";
					this.operacionAEjecutar = "setfacselformapago";
				} else {
					((FacturaGIRE) response.getListaFactura().get(0)).setFormaDePago(((FacturaGIRE) response.getListaFactura().get(0)).getFormasDePagoHabilitadas().get(0).getCodFP());
					setearFacutraGIRE(((FacturaGIRE) response.getListaFactura().get(0)));					
				}
			}
		} else {
			cerrarModal = "mpCerrar";

			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR, response.getMensaje(), null));
		}
	}

	private void setearFacutraGIRE(FacturaGIRE factura) {
		List<String> oLstUpdt = null;
		
		if ("ESC".equals(factura.getCodTI().toUpperCase().trim())
				|| ("CAC".equals(factura.getCodTI().toUpperCase().trim()))
						&& factura.getImporteTotal().compareTo(0f) > 0) {
			factura.setImporteAbonado(factura.getImporteTotal());
			agregarFacturaAGrilla(factura);
			this.cerrarModal = "mpCerrar";
			this.operacionAEjecutar = "";

			recalcularTotales();

			oLstUpdt = new ArrayList<String>();
			oLstUpdt.add("operacionesActivas");
			oLstUpdt.add("total");
			oLstUpdt.add("contenedorBotonesInferiores");
			oLstUpdt.add("ConfirmarMontoACobrar");

			PrimeFaces.current().ajax().update(oLstUpdt);
		} else {
			this.setFacturaSeleccionada(factura);
			showSetImporteFactura = true;
			this.cerrarModal = "mpActualizar";
			this.operacionAEjecutar = "setfacselimp";
		}
	}

	private void setearFacturaConIngresoDeImporte() {
		List<String> oLstUpdt = null;

		showLstColisionFactura = false;

		if (this.getFacturaSeleccionada() == null) {
			cerrarModal = "mpCerrar";

			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL,
					"No se ha seleccionado una factura para cobranza.", null));

			LogACGHelper.escribirLog(null, "No se ha seleccionado una factura para cobranza.");

			return;
		}

		showSetImporteFactura = false;
		cerrarModal = "mpCerrar";
		this.getFacturaSeleccionada().setImporteTotal(this.getFacturaSeleccionada().getImporteAbonado());
		agregarFacturaAGrilla(this.getFacturaSeleccionada());

		recalcularTotales();

		oLstUpdt = new ArrayList<String>();
		oLstUpdt.add("operacionesActivas");
		oLstUpdt.add("total");
		oLstUpdt.add("contenedorBotonesInferiores");
		oLstUpdt.add("ConfirmarMontoACobrar");

		PrimeFaces.current().ajax().update(oLstUpdt);
	}

	private void setearFacturaSeleccionadaConColision() {
		List<String> oLstUpdt = null;

		showLstColisionFactura = false;

		if (this.getFacturasColision() == null || this.getFacturasColision().size() == 0) {
			cerrarModal = "mpCerrar";

			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_FATAL, "No existen facturas para seleccionar.", null));

			LogACGHelper.escribirLog(null, "No existen facturas para seleccionar.");

			return;
		}

		for (Factura oFacAux : this.getFacturasColision()) {
			if (this.getIdTemporalSeleccionado().equals(oFacAux.getIdTemporal())) {
				this.setFacturaSeleccionada(oFacAux);
				break;
			}
		}

		if (this.getFacturaSeleccionada() == null) {
			cerrarModal = "mpCerrar";

			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL,
					"No se ha seleccionado una factura para cobranza.", null));

			LogACGHelper.escribirLog(null, "No se ha seleccionado una factura para cobranza.");

			return;
		}

		// Verifica cobranza rapida u otro tipo de cobranza con importe cerrado
		if ("CLR".equals(((FacturaGIRE) this.getFacturaSeleccionada()).getTipoCobranza().toUpperCase().trim())
				|| (!"CLR".equals(((FacturaGIRE) this.getFacturaSeleccionada()).getTipoCobranza().toUpperCase().trim())
						&& "ESC".equals(
								((FacturaGIRE) this.getFacturaSeleccionada()).getCodTI().toUpperCase().trim()))) {
			showSetImporteFactura = false;
			
			if (((FacturaGIRE) this.getFacturaSeleccionada()).getFormasDePagoHabilitadas().size() > 1) {
				this.setFormasDePago(new ArrayList<FormasPago>());
				this.setIdFormaDePagoSeleccionado("");
				this.setFormasDePago(((FacturaGIRE) this.getFacturaSeleccionada()).getFormasDePagoHabilitadas());
				this.setFacturaSeleccionada(((FacturaGIRE) this.getFacturaSeleccionada()));
				this.setShowCmbFormaDePago(true);
				this.cerrarModal = "mpActualizar";
				this.operacionAEjecutar = "setfacselformapago";
			} else {
				cerrarModal = "mpCerrar";

				((FacturaGIRE) this.getFacturaSeleccionada()).setFormaDePago(((FacturaGIRE) this.getFacturaSeleccionada()).getFormasDePagoHabilitadas().get(0).getCodFP());

				this.getFacturaSeleccionada().setImporteAbonado(this.getFacturaSeleccionada().getImporteTotal());
				
				agregarFacturaAGrilla(this.getFacturaSeleccionada());

				recalcularTotales();

				oLstUpdt = new ArrayList<String>();
				oLstUpdt.add("operacionesActivas");
				oLstUpdt.add("total");
				oLstUpdt.add("contenedorBotonesInferiores");
				oLstUpdt.add("ConfirmarMontoACobrar");

				PrimeFaces.current().ajax().update(oLstUpdt);
			}
		} else {
			showSetImporteFactura = true;
			cerrarModal = "mpActualizar";
		}
	}

	private void setearFacturaSeleccionadaConSeleccionFormaDePago() {
		if (this.getFacturaSeleccionada() == null) {
			cerrarModal = "mpCerrar";

			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL,
					"No se ha seleccionado una factura para cobranza.", null));

			LogACGHelper.escribirLog(null, "No se ha seleccionado una factura para cobranza.");

			return;
		}

		((FacturaGIRE)this.getFacturaSeleccionada()).setFormaDePago(this.getIdFormaDePagoSeleccionado());
		
		setearFacutraGIRE((FacturaGIRE)this.getFacturaSeleccionada());
	}

	private void agregarFacturaAGrilla(Factura factura) {
		FacturaGCSIWeb oFactura = null;
		boolean codBarra1Igual = false;
		boolean codBarra2Igual = false;
		boolean codEmpIgual = false;
		boolean codDiscriminadorIgual = false;

		oFactura = new FacturaGCSIWeb();
		oFactura.setFactura(factura);
		oFactura.setIncluir(true);

		// Verifica que la factuta a agregar no se encuentre en la lista
		for (FacturaGCSIWeb oFAuxWeb : this.getFacturas()) {
			if (oFAuxWeb.getFactura().getCodBarra1() != null) {
				if (oFactura.getFactura().getCodBarra1() != null) {
					if (oFAuxWeb.getFactura().getCodBarra1().equals(oFactura.getFactura().getCodBarra1()))
						codBarra1Igual = true;
				} else
					codBarra1Igual = true;
			} else
				codBarra1Igual = true;

			if (oFAuxWeb.getFactura().getCodBarra2() != null) {
				if (oFactura.getFactura().getCodBarra2() != null) {
					if (oFAuxWeb.getFactura().getCodBarra2().equals(oFactura.getFactura().getCodBarra2()))
						codBarra2Igual = true;
				} else
					codBarra2Igual = true;
			} else
				codBarra2Igual = true;

			if (oFAuxWeb.getFactura().getCodEmp() != null) {
				if (oFactura.getFactura().getCodEmp() != null) {
					if (oFAuxWeb.getFactura().getCodEmp().equals(oFactura.getFactura().getCodEmp()))
						codEmpIgual = true;
				} else
					codEmpIgual = true;
			} else
				codEmpIgual = true;

			if (oFAuxWeb.getFactura().getCodDiscriminador() != null) {
				if (oFactura.getFactura().getCodDiscriminador() != null) {
					if (oFAuxWeb.getFactura().getCodDiscriminador().equals(oFactura.getFactura().getCodDiscriminador()))
						codDiscriminadorIgual = true;
				} else
					codDiscriminadorIgual = true;
			} else
				codDiscriminadorIgual = true;
		}

		if (codBarra1Igual && codBarra2Igual && codEmpIgual && codDiscriminadorIgual) { // Misma factura
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN,
					"La factura que intenta agregar ya se encuentra en la grilla.", null));
		} else
			this.getFacturas().add(oFactura);		
	}

	public void obtenerFacturaSinSoportePapelCobranzaManual(String jsonParametros) {
		try {
			if ("GIRE".equals(this.getOperadora()))
				obtenerFacturaSinSoportePapelGIRE(jsonParametros);

			// OJO para el resto de las operadoras. Agregar logica
		} catch (Exception e) {
			cerrarModal = "mpCerrar";

			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_FATAL, "Se produjo una excepcion.", null));

			LogACGHelper.escribirLog(null,
					"Excepcion en metodo obtenerFacturaSinSoportePapelCobranzaManual=|" + e.getMessage() + "|");
		}
	}

	public void actualizarSeleccionDeEmpresa() {
		this.setModalidadesCobranza(new ArrayList<Modalidad>());
		this.setIdModSeleccionado("");

		for (EmpresaRapipago oEmpAux : this.empresas) {
			if (oEmpAux.getCodEmp().equals(this.getCodEmpresaSeleccionada())) {
				this.setEmpresaSeleccionada(oEmpAux);

				for (Modalidad oModAux : oEmpAux.getModalidades()) {
					this.getModalidadesCobranza().add(oModAux);
				}

				break;
			}
		}

		this.setShowCmbTipoCobranza(true);
	}

	public void actualizarSeleccionTipoCobranza() {
		this.setFormasDePago(new ArrayList<FormasPago>());
		this.setIdFormaDePagoSeleccionado("");
		this.setFormasDePago(this.getEmpresaSeleccionada().getFormasPago());
		this.setShowCmbFormaDePago(true);
	}

	public String getContenido() {
		return contenido;
	}

	public void setContenido(String contenido) {
		this.contenido = contenido;
	}

	public void invocarGCSI() {
		Map<String, String> params = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
		String parametro = params.get("parametro");
		String tipo = params.get("tipo");
		String valor = params.get("valor");
		String texto = params.get("texto");
		invocarGCSI(parametro, tipo, valor, texto);
	}

	public void invocarGCSI(String parametro, String tipo, String valor, String texto) {
		this.ticket = "";
		this.contenido = "";
		String sClave = "";
		String urlw = "";

		// TODO: Pasar dp a la inicializacion de GCSI
		// Busco los DatosPlataforma

		try {
			if (oParamsPlat == null) {
				FacesContext.getCurrentInstance().addMessage(null,
						new FacesMessage(FacesMessage.SEVERITY_ERROR, "No se encontraron datosPlataforma", null));
				return;
			} else {
				sClave = this.getUsuario().getPassword();
				urlw = oParamsPlat.getPnetTipoConexionSinSSL().getValue() + "://"
						+ oParamsPlat.getPnetServidorSinSSL().getValue() + ":"
						+ oParamsPlat.getPnetPuertoSinSSL().getValue().toString()
						+ oParamsPlat.getPnetUrlSinSSL().getValue();

				this.setShowCmbEmpresa(false);
				this.setShowCmbTipoCobranza(false);

				/*if ((this.loteTotalmenteAprobado) && (this.facturas.size() > 0)) {
					limpiarPantalla();
				}*/

				switch (parametro.toUpperCase().trim()) {
				// Estado de terminal
				case "EST": {
					verificarEstadoTerminal();
					break;
				}

				// Apertura de turno
				case "APE": {
					realizarAperturaTerminal();
					break;
				}

				// Cierre de turno
				case "CIE": {
					realizarCierreTerminal();
					break;
				}

				// Elimina Factura de Grilla
				case "EFG": {
					for (FacturaGCSIWeb oFWeb : this.getFacturas()) {
						if (oFWeb.getFactura().equals(this.facturaSeleccionada)) {
							this.facturas.remove(oFWeb);
							break;
						}
					}

					this.facturaSeleccionada = null;
					recalcularTotales();
					break;
				}

				// Elimina datos en grilla (Nueva cobranza)
				case "CLS":
					limpiarPantalla();
					break;

				// Cobranza
				case "COB": {
					// Apertura de dialogo para cobranza sin factura
					if ("DLGMANUAL".equals(tipo.toUpperCase().trim()))
						dialogoCobranzaManual();
					// Obtencion de empresas para cobranza sin factura
					else if ("GETEMP".equals(tipo.toUpperCase().trim()))
						obtenerEmpresasCobManual(valor);
					// Obtencion de parametria para empresa seleccionada
					else if ("PARAMCOBMAN".equals(tipo.toUpperCase().trim()))
						obtenerParametriaCobranzaManual();
					// Obtencion de la factura de acuerdo a los datos dinamicos cargados
					else if ("PROCCOBMAN".equals(tipo.toUpperCase().trim()))
						obtenerFacturaSinSoportePapelCobranzaManual(valor);
					// Apertura de dialogo para cobranza con codigo de barra
					else if ("DLGBARRA".equals(tipo.toUpperCase().trim()))
						dialogoCobranzaCodigoDeBarra();
					// Obtencion de factura de acuerdo al codigo de barra ingresado
					else if ("PROCCOBBARRA".equals(tipo.toUpperCase().trim()))
						obtenerFacturaConCodigoDeBarra(valor);
					else if ("SETFACSELFORMAPAGO".equals(tipo.toUpperCase().trim()))
						setearFacturaSeleccionadaConSeleccionFormaDePago();
					else if ("SELFACCOLISION".equals(tipo.toUpperCase().trim()))
						setearFacturaSeleccionadaConColision();
					else if ("SETFACSELIMP".equals(tipo.toUpperCase().trim()))
						setearFacturaConIngresoDeImporte();
					else
						FacesContext.getCurrentInstance().addMessage(null,
								new FacesMessage(FacesMessage.SEVERITY_ERROR, "Opcion de cobranza invalida", null));
					break;
				}
				case "PCF": { // Procesar Cobranza de Facturas
					procesarCobranzaDeFactura();
					break;
				}
				case "REI": { // IMPRESION de ticket
					procesarImpresionTicketDeFactura(valor);
					break;
				}
				case "RIT": { // IMPRESION de todos los ticket
					procesarImpresionTicketsDeLote();
					break;
				}
				case "EXP": { // Exportar ticket de factura
					procesarExportacionTicketDeFactura(valor);
				}
				case "DLGCDP": { // Dialogo confirmacion de pago
					dialogoConfirmacionDePago();
				}
				default: {
					obtenerMenu();
					break;
				}
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
					LogACGHelper.escribirLog(null,
							"No se pudo establecer la comunicación (GPN-HNC): |" + ste.getMessage() + "|");
					FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
							"No se pudo establecer la comunicación (GPN-HNC).\n Por favor intente nuevamente.", null));
				} else {
					LogACGHelper.escribirLog(null, "Invocar GCSI. Error ejecutando el WS: |" + ste.getMessage() + "|");
					FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
							"Invocar GCSI. Error ejecutando el WS: |" + ste.getMessage() + "|", null));
				}
			} else {
				LogACGHelper.escribirLog(null, "Invocar GCSI. Error ejecutando el WS: |" + ste.getMessage() + "|");
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
						"Invocar GCSI. Error ejecutando el WS: |" + ste.getMessage() + "|", null));
			}

			this.cerrarModal = "mpCerrarAll";
			obtenerMenu();
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR, "Ha ocurrido un error: " + e.getMessage(), null));

			this.cerrarModal = "mpCerrarAll";
			obtenerMenu();
		} finally {
			if ("mpMostrar".equals(cerrarModal)) {
				PrimeFaces.current().ajax().update("facturaPanel");
				PrimeFaces.current().executeScript("PF('facturaPanelWV').show();");
			} else if ("mpCerrar".equals(cerrarModal)) {
				PrimeFaces.current().executeScript("PF('facturaPanelWV').hide();");
			} else if ("mpcCerrar".equals(cerrarModal) || "mpCerrarAll".equals(cerrarModal)) {
				PrimeFaces.current().executeScript("PF('confirmarPanelWV').hide();");
			} else if ("mpActualizar".equals(cerrarModal)) {
				PrimeFaces.current().ajax().update("facturaPanelGroup");
				// PrimeFaces.current().executeScript("PF('facturaPanelWV').show();");
			}

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
		Map<String, List<String>> paramMap = new HashMap<String, List<String>>();

		nombreTicketCompleto = "/secure/shared/tickets/ticketGCSI.xhtml";

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

	public Integer getMostrarPantallasRapipago() {
		try {
			if (mostrarPantallasRapipago == -1) {

				mostrarPantallasRapipago = 0;

				// tipoproducto = 21 (Rapipago)
				RespString rs = GestionServiceHelper.getGestionService(CfgTimeout.DEFAULT)
						.validarHabilitacionPorTipoProducto(this.getUsuario().getIdMayorista(),
								this.getUsuario().getIdCliente(), 21L);

				if ((rs != null) && (!rs.getError().getValue().getHayError().getValue()))
					mostrarPantallasRapipago = 1;
				else
					LogACGHelper.escribirLog(null,
							"BUSQUEDA PRD RAPIPAGO-->>>>|" + rs.getError().getValue().getMsgError().getValue() + "|");

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
					LogACGHelper.escribirLog(null, "Mostrar Pantalla de GCSI: |" + ste.getMessage() + "|");
					FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
							"Error ejecutando el WS de consulta: |" + ste.getMessage() + "|", null));
				}
			} else {
				LogACGHelper.escribirLog(null, "Mostrar Pantalla de GCSI: |" + ste.getMessage() + "|");
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
						"Error ejecutando el WS de consulta: |" + ste.getMessage() + "|", null));
			}
			FacesContext.getCurrentInstance().validationFailed();
		} catch (Exception e) {
			LogACGHelper.escribirLog(null, "Mostrar Pantalla de GCSI. Excepcion: |" + e.getMessage() + "|");
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
					"Error ejecutando el WS de consulta: |" + e.getMessage() + "|", null));
			FacesContext.getCurrentInstance().validationFailed();
		}
		return mostrarPantallasRapipago;
	}

	public void setMostrarPantallasRapipago(Integer mostrarPantallasDeGCSI) {
		this.mostrarPantallasRapipago = mostrarPantallasDeGCSI;
	}

	public List<FacturaGCSIWeb> getFacturas() {
		return facturas;
	}

	public void setFacturas(List<FacturaGCSIWeb> facturas) {
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
			for (FacturaGCSIWeb factura : facturas) {
				if (factura.isIncluir() && 
					(factura.getFactura().getResultado() == null || (factura.getFactura().getResultado() != null && !"0".equals(factura.getFactura().getResultado())) ||
					(factura.getFactura().getEstado() == null || (factura.getFactura().getEstado() != null && ("ERRENVOPE".equals(factura.getFactura().getEstado().getCodMnemonico()) || 
								"TIMEOUTCONN".equals(factura.getFactura().getEstado().getCodMnemonico()) || 
								"TIMEOUTRW".equals(factura.getFactura().getEstado().getCodMnemonico()))))
					)) {
					/*if (factura.getFactura().getEstado() == null || (factura.getFactura().getEstado() != null
							&& ("ERRENVOPE".equals(factura.getFactura().getEstado().getCodMnemonico()) || 
								"TIMEOUTCONN".equals(factura.getFactura().getEstado().getCodMnemonico()) || 
								"TIMEOUTRW".equals(factura.getFactura().getEstado().getCodMnemonico()))
							)) {*/
						confirmarHabilitado = true;
						break;
					//}
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
			for (FacturaGCSIWeb factura : facturas) {
				if (factura.getFactura().getEstado() != null) {
					if (factura.getFactura().getEstado().getId() == 2
							&& "0".equals(factura.getFactura().getResultado())) {
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
			if (vuelto < 0) {
				this.confirmarVueltoAEntregar = 0f;
			} else {
				this.confirmarVueltoAEntregar = vuelto;
			}
		} catch (Exception e) {
			confirmarVueltoAEntregar = 0f;
		}

		if (vuelto < 0) {
			this.confirmarVueltoAEntregarColor = "white";
			this.confirmarVueltoAEntregarBackgroundColor = "red";
		} else {
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

	public boolean isInicializadoGCSI() {
		return inicializadoGCSI;
	}

	public void setInicializadoGCSI(boolean inicializadoGCSI) {
		this.inicializadoGCSI = inicializadoGCSI;
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

	public Factura getFacturaSeleccionada() {
		return facturaSeleccionada;
	}

	public void setFacturaSeleccionada(Factura facturaSeleccionada) {
		this.facturaSeleccionada = facturaSeleccionada;
	}

	public List<EmpresaRapipago> getEmpresas() {
		return empresas;
	}

	public void setEmpresas(List<EmpresaRapipago> empresas) {
		this.empresas = empresas;
	}

	public boolean isShowCmbEmpresa() {
		return showCmbEmpresa;
	}

	public void setShowCmbEmpresa(boolean showCmbEmpresa) {
		this.showCmbEmpresa = showCmbEmpresa;
	}

	public EmpresaRapipago getEmpresaSeleccionada() {
		return empresaSeleccionada;
	}

	public void setEmpresaSeleccionada(EmpresaRapipago empresaSeleccionada) {
		this.empresaSeleccionada = empresaSeleccionada;
	}

	public String getCodEmpresaSeleccionada() {
		return codEmpresaSeleccionada;
	}

	public void setCodEmpresaSeleccionada(String codEmpresaSeleccionada) {
		this.codEmpresaSeleccionada = codEmpresaSeleccionada;
	}

	public List<Modalidad> getModalidadesCobranza() {
		return modalidadesCobranza;
	}

	public void setModalidadesCobranza(List<Modalidad> modalidadesCobranza) {
		this.modalidadesCobranza = modalidadesCobranza;
	}

	public List<FormasPago> getFormasDePago() {
		return formasDePago;
	}

	public void setFormasDePago(List<FormasPago> formasDePago) {
		this.formasDePago = formasDePago;
	}

	public String getIdModSeleccionado() {
		return idModSeleccionado;
	}

	public void setIdModSeleccionado(String idModSeleccionado) {
		this.idModSeleccionado = idModSeleccionado;
	}

	public String getIdFormaDePagoSeleccionado() {
		return idFormaDePagoSeleccionado;
	}

	public void setIdFormaDePagoSeleccionado(String idFormaDePagoSeleccionado) {
		this.idFormaDePagoSeleccionado = idFormaDePagoSeleccionado;
	}

	public boolean isShowCmbTipoCobranza() {
		return showCmbTipoCobranza;
	}

	public void setShowCmbTipoCobranza(boolean showCmbTipoCobranza) {
		this.showCmbTipoCobranza = showCmbTipoCobranza;
	}

	public boolean isShowCmbFormaDePago() {
		return showCmbFormaDePago;
	}

	public void setShowCmbFormaDePago(boolean showCmbFormaDePago) {
		this.showCmbFormaDePago = showCmbFormaDePago;
	}

	public String getOperadora() {
		return operadora;
	}

	public void setOperadora(String operadora) {
		this.operadora = operadora;
	}

	public List<Factura> getFacturasColision() {
		return facturasColision;
	}

	public void setFacturasColision(List<Factura> facturasColision) {
		this.facturasColision = facturasColision;
	}

	public boolean isShowLstColisionFactura() {
		return showLstColisionFactura;
	}

	public void setShowLstColisionFactura(boolean showLstColisionFactura) {
		this.showLstColisionFactura = showLstColisionFactura;
	}

	public String getOperacionAEjecutar() {
		return operacionAEjecutar;
	}

	public void setOperacionAEjecutar(String operacionAEjecutar) {
		this.operacionAEjecutar = operacionAEjecutar;
	}

	public boolean isShowSetImporteFactura() {
		return showSetImporteFactura;
	}

	public void setShowSetImporteFactura(boolean showSetImporteFactura) {
		this.showSetImporteFactura = showSetImporteFactura;
	}

	public String getIdTemporalSeleccionado() {
		return idTemporalSeleccionado;
	}

	public void setIdTemporalSeleccionado(String idTemporalSeleccionado) {
		this.idTemporalSeleccionado = idTemporalSeleccionado;
	}
}
