package com.americacg.cargavirtual.web.mngbeans.reportes;

import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import javax.faces.application.FacesMessage;
import javax.inject.Named;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.model.SelectItem;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.namespace.QName;
import javax.xml.ws.BindingProvider;
import javax.xml.ws.WebServiceException;
import org.primefaces.PrimeFaces;
import org.springframework.context.annotation.Scope;
import com.americacg.cargavirtual.gestion.model.DatosPlataforma;
import com.americacg.cargavirtual.gestion.model.DescripcionContainerString;
import com.americacg.cargavirtual.gestion.model.DescripcionString;
import com.americacg.cargavirtual.gestion.model.ListaDeStrings;
import com.americacg.cargavirtual.pnet.gateway.ICargaVirtualGatewayPNet;
import com.americacg.cargavirtual.web.model.Error;
import com.americacg.cargavirtual.web.model.Factura;
import com.americacg.cargavirtual.web.enums.CfgTimeout;
import com.americacg.cargavirtual.web.helpers.GestionServiceHelper;
import com.americacg.cargavirtual.web.helpers.LogACGHelper;
import com.americacg.cargavirtual.web.lazymodels.LazyFacturaDataModel;

@Named("reportePNetACGView")
@Scope(org.springframework.web.context.WebApplicationContext.SCOPE_SESSION)
public class ReportePNetACGView extends ReporteGeneral<Factura> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2326266592185648986L;

	// idUsuario es el id del usuario logueado, que se lo pase desde el faces.xml
	private Long idUsuario;

	private String idEstadoOperador;
	private List<SelectItem> pnet_estados = null;

	private String estado_lote;
	private String estado_trx;
	private Long idLotePNet;
	private Long idFacturaPNet;

	private Integer tipoFiltroCliente = 3;

	private String codBarra1;
	private String codBarra2;
	private String descProducto;

	private Integer cantFacturas = 0;
	private BigDecimal sumFacturas = BigDecimal.ZERO;

	private Integer cantLotes = 0;
	private BigDecimal sumLoteConfirmados = BigDecimal.ZERO;
	private Integer cantLotesConFacturasAceptadas = 0;
	private Integer cantFacturasAceptadas = 0;
	private BigDecimal sumLoteRechazadas = BigDecimal.ZERO;
	private BigDecimal sumLoteAnuladas = BigDecimal.ZERO;

	private Long idLoteACG;

	private String numeroUnicoSiris;
	private String idSiris;

	private Long idFacturaParaTicket;
	private Long idClienteParaTicket;
	private String ticket;

	/*
	 * TODO pasar estas propiedades a ReporteGeneral
	 */
	protected int paginadorPaginasTotal = 0;
	protected Long idClienteFiltro = null;

	private LazyFacturaDataModel lazyModel;

	@Override
	public Error resetearReporte() {
		Error e = super.resetearReporte();
		this.tipoFiltroCliente = 3;
		this.idClienteFiltro = null;
		this.idLotePNet = null;
		this.idEstadoOperador = null;
		this.idFacturaPNet = null;
		this.idSiris = null;
		this.numeroUnicoSiris = null;
		this.descProducto = null;
		this.codBarra1 = null;
		this.codBarra2 = null;
		this.paginadorPaginasTotal = 0;

		try {

			this.pnet_estados = new ArrayList<SelectItem>();
			DescripcionContainerString  ceda = GestionServiceHelper.getGestionService(CfgTimeout.REPORTE).mostrarPNetEstadosOperador(null);
			for (DescripcionString d : ceda.getListDescripcionString().getValue().getDescripcionString()) {
				this.pnet_estados.add(new SelectItem(d.getId().getValue(), d.getDescripcion().getValue()));
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
							"Informe PNet. Error ejecutando el WS de consulta: |" + ste.getMessage() + "|");
					e.setError("GST-OTR",
							"No se pudo establecer la comunicación (GST-OTR).\n Por favor intente nuevamente.");
				}
			} else {
				LogACGHelper.escribirLog(null,
						"Informe PNet. Error ejecutando el WS de consulta: |" + ste.getMessage() + "|");
				e.setError("GST-OTR",
						"No se pudo establecer la comunicación (GST-OTR).\n Por favor intente nuevamente.");
			}
			FacesContext.getCurrentInstance().validationFailed();
		} catch (Exception ex) {
			LogACGHelper.escribirLog(null,
					"Informe PNet. Excepcion ejecutando el WS de consulta: |" + ex.getMessage() + "|");
			e.setError("GST-OTR", "No se pudo establecer la comunicación (GST-OTR).\n Por favor intente nuevamente.");
		}
		return e;
	}

	public ReportePNetACGView() {
		super();
	}

	public void obtenerDatos() {

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

			if (this.getUsuario().getTipoCliente() != null) {
				if ("P".equals(this.getUsuario().getTipoCliente())) {
					// Para el caso del Punto de venta donde no tengo filtro de cliente
					tipoFiltroCliente = 1;
					idClienteFiltro = this.getUsuario().getIdCliente();
				}
			} else {
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
						"El tipo de Cliente no es valido para la consulta", null));
				return;
			}

			// Prevalido las condificiones de filtro por cliente antes de Buscar la lista de
			// Clientes a consultar
			if ("P".equals(this.getUsuario().getTipoCliente())) {
				tipoFiltroCliente = 1;
				idClienteFiltro = this.getUsuario().getIdCliente();

			} else if (tipoFiltroCliente == null) {
				FacesContext.getCurrentInstance().addMessage(null,
						new FacesMessage(FacesMessage.SEVERITY_ERROR, "El tipoFiltroCliente es null", null));
				return;

			} else if (tipoFiltroCliente == 3) {
				if (idClienteFiltro != null) {
					FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
							"El cliente del filtro debe ser vacio", null));
					return;
				} else {
					idClienteFiltro = this.getUsuario().getIdCliente();
				}

			} else if ((tipoFiltroCliente == 1) || (tipoFiltroCliente == 2)) {
				if ((idClienteFiltro == null) || (idClienteFiltro <= 0)) {
					FacesContext.getCurrentInstance().addMessage(null,
							new FacesMessage(FacesMessage.SEVERITY_ERROR, "Debe cargar un cliente para filtrar", null));
					return;
				}

			} else {
				FacesContext.getCurrentInstance().addMessage(null,
						new FacesMessage(FacesMessage.SEVERITY_ERROR, "El tipoCliente de Filtro no es valido", null));
				return;
			}

			ListaDeStrings ls = GestionServiceHelper.getGestionService(CfgTimeout.REPORTE).listaDeSubClientes(
					this.getUsuario().getIdMayorista(), this.getUsuario().getIdCliente(), tipoFiltroCliente,
					idClienteFiltro, false);
			if (ls == null) {
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
						"No se encontraron subclientes para consultar. La respuesta de la consulta fue null", null));
			} else if (ls.getError().getValue().getHayError().getValue()) {
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
						ls.getError().getValue().getMsgError().getValue(), null));
			} else if ((ls.getL().getValue().getString() == null) || (ls.getL().getValue().getString().size() <= 0)) {
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
						"No existe información para la consulta realizada.", null));
			} else {

				// Busco los DatosPlataforma
				DatosPlataforma dp = GestionServiceHelper.getGestionService(CfgTimeout.REPORTE)
						.mostrarDatosPlataforma(this.getUsuario().getIdMayorista());
				if (dp == null) {
					FacesContext.getCurrentInstance().addMessage(null,
							new FacesMessage(FacesMessage.SEVERITY_ERROR, "No se encontraron datosPlataforma", null));
					return;
				} else {

					String urlw = dp.getPnetTipoConexionSinSSL().getValue() + "://"
							+ dp.getPnetServidorSinSSL().getValue() + ":"
							+ dp.getPnetPuertoSinSSL().getValue().toString() + dp.getPnetUrlSinSSL().getValue();
					com.americacg.cargavirtual.pnet.service.CargaVirtualGatewayPNet cargaVirtualGatewayPNet = null;
					URL url;

					url = new URL(
							com.americacg.cargavirtual.pnet.service.CargaVirtualGatewayPNet.class.getResource("."),
							urlw);

					cargaVirtualGatewayPNet = new com.americacg.cargavirtual.pnet.service.CargaVirtualGatewayPNet(url,
							new QName("http://service.pnet.cargavirtual.americacg.com/", "CargaVirtualGatewayPNet"));
					ICargaVirtualGatewayPNet gsp;
					gsp = cargaVirtualGatewayPNet.getCargaVirtualGatewayPNetPort();
					// SETEO EL TIME OUT DE CONEXION (ms)
					((BindingProvider) gsp).getRequestContext().put("javax.xml.ws.client.connectionTimeout", "6000");

					// SETEO EL TIME DE LECTURA (ms)
					((BindingProvider) gsp).getRequestContext().put("javax.xml.ws.client.receiveTimeout", "120000");

					// Configuro el Parametro set-jaxb-validation-event-handler para
					// que no pinche el WS si cambia la definicion del WSDL
					((BindingProvider) gsp).getRequestContext().put("set-jaxb-validation-event-handler", "false");

					// CIERRE DE TERMINAL
					com.americacg.cargavirtual.pnet.message.solicitud.ReporteACG stRACG = new com.americacg.cargavirtual.pnet.message.solicitud.ReporteACG();
					com.americacg.cargavirtual.pnet.message.solicitud.DescargaReporteACG stDRACG = new com.americacg.cargavirtual.pnet.message.solicitud.DescargaReporteACG();
					if (!this.exportToExcel) {
						stRACG.setIdMayorista(this.getUsuario().getIdMayorista());
						stRACG.setUsuario(this.getUsuario().getUsername());
						stRACG.setPwd(this.getUsuario().getPassword());
					} else {
						stDRACG.setIdMayorista(this.getUsuario().getIdMayorista());
						stDRACG.setUsuario(this.getUsuario().getUsername());
						stDRACG.setPwd(this.getUsuario().getPassword());
						stDRACG.setSeparadorCampo(this.getUsuario().getCsvSeparadorCampo());
						stDRACG.setSeparadorDecimales(this.getUsuario().getCsvSeparadorDecimales());
					}

					com.americacg.cargavirtual.pnet.model.FiltroReporte clFR = new com.americacg.cargavirtual.pnet.model.FiltroReporte();

					clFR.setIdLote(this.idLoteACG);

					if (this.fechaHoraDesde != null) {
						GregorianCalendar gcd = new GregorianCalendar();
						gcd.setTimeInMillis(this.fechaHoraDesde.getTime());
						DatatypeFactory dfd = DatatypeFactory.newInstance();
						clFR.setFechaDde(dfd.newXMLGregorianCalendar(gcd));
					}

					if (this.fechaHoraHasta != null) {
						GregorianCalendar gch = new GregorianCalendar();
						gch.setTimeInMillis(this.fechaHoraHasta.getTime());
						DatatypeFactory dfh = DatatypeFactory.newInstance();
						clFR.setFechaHta(dfh.newXMLGregorianCalendar(gch));
					}

					clFR.setIdLote(this.idLotePNet);
					clFR.setIdEstadoFactura("".equals(this.idEstadoOperador) ? null : this.idEstadoOperador);
					clFR.setIdFactura(this.idFacturaPNet);

					if ((this.idSiris != null) && (!"".equals(this.idSiris))) {
						clFR.setIdSiris(this.idSiris);
					}

					if ((this.numeroUnicoSiris != null) && (!"".equals(this.numeroUnicoSiris))) {
						clFR.setNumeroUnicoSiris(this.numeroUnicoSiris);
					}
					if ((this.descProducto != null) && (!"".equals(this.descProducto))) {
						clFR.setEmpresaFactura(this.descProducto);
					}

					/*
					 * clFR.setEstadoTransaccion(value); clFR.setEmpresaFactura(value);
					 * clFR.setIdEstadoFactura(value);
					 */
					if (this.codBarra1 != null && this.codBarra1.trim() != "") {
						clFR.setCodBarra1(this.codBarra1);
					}
					if (this.codBarra2 != null && this.codBarra2.trim() != "") {
						clFR.setCodBarra2(this.codBarra2);
					}

					if ((this.numeroUnicoSiris != null) && (!"".equals(this.numeroUnicoSiris))) {
						clFR.setNumeroUnicoSiris(this.numeroUnicoSiris);
					}
					if ((this.descProducto != null) && (!"".equals(this.descProducto))) {
						clFR.setEmpresaFactura(this.descProducto);
					}

					/*
					 * clFR.setEstadoTransaccion(value); clFR.setEmpresaFactura(value);
					 * clFR.setIdEstadoFactura(value);
					 */
					if (this.codBarra1 != null && this.codBarra1.trim() != "") {
						clFR.setCodBarra1(this.codBarra1);
					}
					if (this.codBarra2 != null && this.codBarra2.trim() != "") {
						clFR.setCodBarra2(this.codBarra2);
					}

					clFR.setIdFactura(this.idFacturaPNet);

					clFR.getListaIdCliente().addAll(ls.getL().getValue().getString());

					if (!this.exportToExcel) {
						stRACG.setFiltroReporte(clFR);
						lazyModel = new LazyFacturaDataModel(gsp, stRACG);
					} else {

						clFR.setPagina(1);
						clFR.setCantRegXPagina(-1);
						clFR.setCampoOrden("FAC_IDTRX");
						clFR.setTipoOrden("DESC");
						stDRACG.setFiltroReporte(clFR);

						byte[] rtRACG = gsp.descargaReporteACG(stDRACG);

						FacesContext fc = FacesContext.getCurrentInstance();
						ExternalContext ec = fc.getExternalContext();

						ec.responseReset();
						ec.setResponseContentType("text/plain");
						ec.setResponseContentLength(rtRACG.length);

						SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd_HHmmss");
						ec.setResponseHeader("Content-Disposition", "attachment; filename=\"" + sdf.format(new Date())
								+ "_(" + this.getUsuario().getIdMayorista() + ")_" + "InformePNet.csv" + "\"");

						OutputStream os = ec.getResponseOutputStream();
						OutputStreamWriter osw = new OutputStreamWriter(os);
						PrintWriter writer = new PrintWriter(osw);
						writer.write(new String(rtRACG));
						writer.flush();
						writer.close();

						fc.responseComplete();

					}
					PrimeFaces.current().executeScript("PF('panelFiltroWG').toggle();");
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
					LogACGHelper.escribirLog(null,
							"Informe PNet. Error ejecutando el WS de consulta: |" + ste.getMessage() + "|");
					FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
							"Error ejecutando el WS de consulta: |" + ste.getMessage() + "|", null));
				}
			} else {
				LogACGHelper.escribirLog(null,
						"Informe PNet. Error ejecutando el WS de consulta: |" + ste.getMessage() + "|");
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
						"Error ejecutando el WS de consulta: |" + ste.getMessage() + "|", null));
			}
			FacesContext.getCurrentInstance().validationFailed();
			this.list = null;
		} catch (Exception e) {
			LogACGHelper.escribirLog(null,
					"Informe PNet. Excepcion ejecutando el WS de consulta: |" + e.getMessage() + "|");
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
					"Error ejecutando el WS de consulta de PNet: |" + e.getMessage() + "|", null));
			FacesContext.getCurrentInstance().validationFailed();
			this.list = null;
		}
		PrimeFaces.current().executeScript("PF('blockPanelGral').hide()");
		return;
	}

	public Long getIdUsuario() {
		return idUsuario;
	}

	public void setIdUsuario(Long idUsuario) {
		this.idUsuario = idUsuario;
	}

	public String getIdEstadoOperador() {
		return idEstadoOperador;
	}

	public void setIdEstadoOperador(String idEstadoOperador) {
		this.idEstadoOperador = idEstadoOperador;
	}

	public List<SelectItem> getPnet_estados() {
		return pnet_estados;
	}

	public void setPnet_estados(List<SelectItem> pnet_estados) {
		this.pnet_estados = pnet_estados;
	}

	public String getEstado_lote() {
		return estado_lote;
	}

	public void setEstado_lote(String estado_lote) {
		this.estado_lote = estado_lote;
	}

	public String getEstado_trx() {
		return estado_trx;
	}

	public void setEstado_trx(String estado_trx) {
		this.estado_trx = estado_trx;
	}

	public Long getIdLotePNet() {
		return idLotePNet;
	}

	public void setIdLotePNet(Long idLotePNet) {
		this.idLotePNet = idLotePNet;
	}

	public Long getIdFacturaPNet() {
		return idFacturaPNet;
	}

	public void setIdFacturaPNet(Long idFacturaPNet) {
		this.idFacturaPNet = idFacturaPNet;
	}

	public Integer getTipoFiltroCliente() {
		return tipoFiltroCliente;
	}

	public void setTipoFiltroCliente(Integer tipoFiltroCliente) {
		this.tipoFiltroCliente = tipoFiltroCliente;
	}

	public String getCodBarra1() {
		return codBarra1;
	}

	public void setCodBarra1(String codBarra1) {
		this.codBarra1 = codBarra1;
	}

	public String getCodBarra2() {
		return codBarra2;
	}

	public void setCodBarra2(String codBarra2) {
		this.codBarra2 = codBarra2;
	}

	public String getDescProducto() {
		return descProducto;
	}

	public void setDescProducto(String descProducto) {
		this.descProducto = descProducto;
	}

	public Integer getCantFacturas() {
		return cantFacturas;
	}

	public void setCantFacturas(Integer cantFacturas) {
		this.cantFacturas = cantFacturas;
	}

	public BigDecimal getSumFacturas() {
		return sumFacturas;
	}

	public void setSumFacturas(BigDecimal sumFacturas) {
		this.sumFacturas = sumFacturas;
	}

	public Integer getCantLotes() {
		return cantLotes;
	}

	public void setCantLotes(Integer cantLotes) {
		this.cantLotes = cantLotes;
	}

	public BigDecimal getSumLoteConfirmados() {
		return sumLoteConfirmados;
	}

	public void setSumLoteConfirmados(BigDecimal sumLoteConfirmados) {
		this.sumLoteConfirmados = sumLoteConfirmados;
	}

	public Integer getCantLotesConFacturasAceptadas() {
		return cantLotesConFacturasAceptadas;
	}

	public void setCantLotesConFacturasAceptadas(Integer cantLotesConFacturasAceptadas) {
		this.cantLotesConFacturasAceptadas = cantLotesConFacturasAceptadas;
	}

	public Integer getCantFacturasAceptadas() {
		return cantFacturasAceptadas;
	}

	public void setCantFacturasAceptadas(Integer cantFacturasAceptadas) {
		this.cantFacturasAceptadas = cantFacturasAceptadas;
	}

	public BigDecimal getSumLoteRechazadas() {
		return sumLoteRechazadas;
	}

	public void setSumLoteRechazadas(BigDecimal sumLoteRechazadas) {
		this.sumLoteRechazadas = sumLoteRechazadas;
	}

	public BigDecimal getSumLoteAnuladas() {
		return sumLoteAnuladas;
	}

	public void setSumLoteAnuladas(BigDecimal sumLoteAnuladas) {
		this.sumLoteAnuladas = sumLoteAnuladas;
	}

	public Long getIdLoteACG() {
		return idLoteACG;
	}

	public void setIdLoteACG(Long idLoteACG) {
		this.idLoteACG = idLoteACG;
	}

	public Long getIdFacturaParaTicket() {
		return idFacturaParaTicket;
	}

	public void setIdFacturaParaTicket(Long idFacturaParaTicket) {
		this.idFacturaParaTicket = idFacturaParaTicket;
	}

	public Long getIdClienteParaTicket() {
		return idClienteParaTicket;
	}

	public void setIdClienteParaTicket(Long idClienteParaTicket) {
		this.idClienteParaTicket = idClienteParaTicket;
	}

	public String getTicket() {
		return ticket;
	}

	public void setTicket(String ticket) {
		this.ticket = ticket;
	}

	public void paginar(ActionEvent ae) {
		obtenerDatos();
	}

	public Long getIdClienteFiltro() {
		return idClienteFiltro;
	}

	public void setIdClienteFiltro(Long idClienteFiltro) {
		this.idClienteFiltro = idClienteFiltro;
	}

	public String getNumeroUnicoSiris() {
		return numeroUnicoSiris;
	}

	public void setNumeroUnicoSiris(String numeroUnicoSiris) {
		this.numeroUnicoSiris = numeroUnicoSiris;
	}

	public String getIdSiris() {
		return idSiris;
	}

	public void setIdSiris(String idSiris) {
		this.idSiris = idSiris;
	}

	public int getPaginadorPaginasTotal() {
		return paginadorPaginasTotal;
	}

	public void setPaginadorPaginasTotal(int paginadorPaginasTotal) {
		this.paginadorPaginasTotal = paginadorPaginasTotal;
	}

	public LazyFacturaDataModel getLazyModel() {
		return lazyModel;
	}

	public void setLazyModel(LazyFacturaDataModel lazyModel) {
		this.lazyModel = lazyModel;
	}

}