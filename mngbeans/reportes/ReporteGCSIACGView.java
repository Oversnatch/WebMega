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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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

import com.americacg.cargavirtual.gcsi.entidad.Estado;
import com.americacg.cargavirtual.gcsi.entidad.Factura;
import com.americacg.cargavirtual.gcsi.entidad.FacturaGIRE;
import com.americacg.cargavirtual.gcsi.entidad.Lote;
import com.americacg.cargavirtual.gcsi.entidad.Lote.Facturas;
import com.americacg.cargavirtual.gcsi.entidad.Mayorista;
import com.americacg.cargavirtual.gcsi.entidad.Operador;
import com.americacg.cargavirtual.gcsi.model.acg.miscelaneo.OrdenBBDD;
import com.americacg.cargavirtual.gcsi.model.acg.request.ConsultarOperacionesRequest;
import com.americacg.cargavirtual.gcsi.model.acg.request.DescargaReporteACG;
import com.americacg.cargavirtual.gcsi.model.acg.request.FiltroListadoOperacionesRequest;
import com.americacg.cargavirtual.gcsi.model.acg.request.ObtenerEstadosRequest;
import com.americacg.cargavirtual.gcsi.model.acg.response.ConsultarOperacionesResponse;
import com.americacg.cargavirtual.gcsi.model.acg.response.ObtenerEstadosResponse;
import com.americacg.cargavirtual.gcsi.model.acg.response.ObtenerParamFacSinSopPapelGIREResponse;
import com.americacg.cargavirtual.gcsi.service.CodigoRetorno;
import com.americacg.cargavirtual.gcsi.service.EstadoGCSI;
import com.americacg.cargavirtual.gestion.model.DatosPlataforma;
import com.americacg.cargavirtual.gestion.model.DescripcionContainerString;
import com.americacg.cargavirtual.gestion.model.DescripcionString;
import com.americacg.cargavirtual.gestion.model.ListaDeStrings;
import com.americacg.cargavirtual.web.model.Error;
import com.americacg.cargavirtual.web.model.FacturaGCSIWeb;
import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Font.FontFamily;
import com.itextpdf.text.pdf.PdfWriter;
import com.americacg.cargavirtual.web.enums.CfgTimeout;
import com.americacg.cargavirtual.web.helpers.GCSIServiceHelper;
import com.americacg.cargavirtual.web.helpers.GestionServiceHelper;
import com.americacg.cargavirtual.web.helpers.LogACGHelper;
import com.americacg.cargavirtual.web.lazymodels.LazyFacturaDataModel;
import com.americacg.cargavirtual.web.lazymodels.LazyOperacionGCSIDataModel;

@Named("reporteGCSIACGView")
@Scope(org.springframework.web.context.WebApplicationContext.SCOPE_SESSION)
public class ReporteGCSIACGView extends ReporteGeneral<Factura> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 208823722883190376L;

	// idUsuario es el id del usuario logueado, que se lo pase desde el faces.xml
	private Long idUsuario;

	private String idEstadoOperador;
	private List<SelectItem> gcsi_estados = null;

	private Long idLoteGCSI;
	private Long idFacturaGCSI;
	private Long idLoteACG;
	private Long idFacturaParaTicket;
	private Long idClienteParaTicket;
	private String estado_lote;
	private String estado_trx;
	private String numeroUnicoSiris;
	private String idTrxOperador;
	private String ticket;

	private Integer tipoFiltroCliente = 3;

	private String codBarra1;
	private String codBarra2;
	private String descEmpresa;

	private Integer cantFacturas = 0;
	private BigDecimal sumFacturas = BigDecimal.ZERO;

	private Integer cantLotes = 0;
	private BigDecimal sumLoteConfirmados = BigDecimal.ZERO;
	private Integer cantLotesConFacturasAceptadas = 0;
	private Integer cantFacturasAceptadas = 0;
	private BigDecimal sumLoteRechazadas = BigDecimal.ZERO;
	private BigDecimal sumLoteAnuladas = BigDecimal.ZERO;
	private String operadora = "";
	private String estadoGCSI = "";
	
	/*
	 * TODO pasar estas propiedades a ReporteGeneral
	 */
	protected int paginadorPaginasTotal = 0;
	protected Long idClienteFiltro = null;

	private LazyOperacionGCSIDataModel listaOperacionesGCSI = null;

	public void inicializarReporte(String operadora) {
		this.operadora = operadora;
		this.resetearReporte();
	}
	
	@Override
	public Error resetearReporte() {
		Error e = super.resetearReporte();
		ObtenerEstadosRequest oObtEstReq = null;
		ObtenerEstadosResponse oResp = null;
		this.tipoFiltroCliente = 3;
		this.idClienteFiltro = null;
		this.idLoteGCSI = null;
		this.idEstadoOperador = null;
		this.idFacturaGCSI = null;
		this.idTrxOperador = null;
		this.numeroUnicoSiris = null;
		this.descEmpresa = null;
		this.codBarra1 = null;
		this.codBarra2 = null;
		this.paginadorPaginasTotal = 0;
		this.estadoGCSI = "";

		try {
			this.gcsi_estados = new ArrayList<SelectItem>();
			
			oObtEstReq = new ObtenerEstadosRequest();
			oObtEstReq.setCodMnemonicoOperador(this.getOperadora());
			oObtEstReq.setIdMayorista(this.getUsuario().getIdMayorista());
			oObtEstReq.setIdCliente(this.getUsuario().getIdCliente());
			oObtEstReq.setIdCanal("WEBINTRA");
			oObtEstReq.setUsuario(this.getUsuario().getUsername());
			oObtEstReq.setPwd(this.getUsuario().getPassword());

			oResp = (ObtenerEstadosResponse) GCSIServiceHelper.getGatewayGCSI(CfgTimeout.DEFAULT).obtenerEstados(oObtEstReq);

			if(CodigoRetorno.M_0000.equals(oResp.getCodigo())) {
				for (Estado oEstAux : oResp.getEstados()) {
					this.gcsi_estados.add(new SelectItem(oEstAux.getCodMnemonico(), oEstAux.getDescripcion()));
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
							"Informe GCSI. Error ejecutando el WS de consulta: |" + ste.getMessage() + "|");
					e.setError("GST-OTR",
							"No se pudo establecer la comunicación (GST-OTR).\n Por favor intente nuevamente.");
				}
			} else {
				LogACGHelper.escribirLog(null,
						"Informe GCSI. Error ejecutando el WS de consulta: |" + ste.getMessage() + "|");
				e.setError("GST-OTR",
						"No se pudo establecer la comunicación (GST-OTR).\n Por favor intente nuevamente.");
			}
			FacesContext.getCurrentInstance().validationFailed();
		} catch (Exception ex) {
			LogACGHelper.escribirLog(null,
					"Informe GCSI. Excepcion ejecutando el WS de consulta: |" + ex.getMessage() + "|");
			e.setError("GST-OTR", "No se pudo establecer la comunicación (GST-OTR).\n Por favor intente nuevamente.");
		}
		return e;
	}

	public ReporteGCSIACGView() {
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
		ConsultarOperacionesRequest oConOperReq = null;
		DescargaReporteACG oDescRepReq = null;
		FiltroListadoOperacionesRequest oFltOpers = null;
		Factura oFac = null;
		Lote oLote = null;
		Estado oEstTrx = null;
		Operador oOpe = null;
		Mayorista oMayo = null;
		OrdenBBDD oOrdenBBDD = null;
		byte[] oDescRepResp = null;  
		ArrayList<Long> oLstIdCliente = null;
		ListaDeStrings oLstCliRetGestion = null;
		
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
			
			oLstCliRetGestion = GestionServiceHelper.getGestionService(CfgTimeout.REPORTE).listaDeSubClientes(
					this.getUsuario().getIdMayorista(), this.getUsuario().getIdCliente(), tipoFiltroCliente,
					idClienteFiltro, false);
			if (oLstCliRetGestion == null) {
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
						"No se encontraron subclientes para consultar. La respuesta de la consulta fue null", null));
			} else if (oLstCliRetGestion.getError().getValue().getHayError().getValue()) {
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
						oLstCliRetGestion.getError().getValue().getMsgError().getValue(), null));
			} else if ((oLstCliRetGestion.getL().getValue().getString() == null) || (oLstCliRetGestion.getL().getValue().getString().size() <= 0)) {
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
					oOpe = new Operador();
					oOpe.setCodMnemonico(this.getOperadora());
					oMayo = new Mayorista();
					oMayo.setId(this.getUsuario().getIdMayorista());

					oLote = new Lote();
					oLote.setOperador(oOpe);
					oLote.setMayorista(oMayo);
					
					//-------------------------------------------
					//Tener en cuenta el resto de las operadoras.
					//-------------------------------------------
					if("GIRE".equals(this.getOperadora()))
						oFac = new FacturaGIRE();
					//-------------------------------------------

					if(this.idLoteACG != null && this.idLoteACG.compareTo(0L) > 0) 
						oLote.setId(this.idLoteACG);

					oFltOpers = new FiltroListadoOperacionesRequest();
					
					if(oLstCliRetGestion != null && oLstCliRetGestion.getL().getValue() != null) {
						oLstIdCliente = (ArrayList<Long>) oLstCliRetGestion.getL().getValue().getString().stream().map(Long::parseLong).collect(Collectors.toList());
						
						oFltOpers.getListaIdsCliente().addAll(oLstIdCliente);
					}
					
					if (this.fechaHoraDesde != null) {
						GregorianCalendar gcd = new GregorianCalendar();
						gcd.setTimeInMillis(this.fechaHoraDesde.getTime());
						DatatypeFactory dfd = DatatypeFactory.newInstance();
						oFltOpers.setFechaDesde(dfd.newXMLGregorianCalendar(gcd));
					}

					if (this.fechaHoraHasta != null) {
						GregorianCalendar gch = new GregorianCalendar();
						gch.setTimeInMillis(this.fechaHoraHasta.getTime());
						DatatypeFactory dfh = DatatypeFactory.newInstance();
						oFltOpers.setFechaHasta(dfh.newXMLGregorianCalendar(gch));
					}

					//oFltOpers.setIdLote(this.idLoteGCSI);
					//oFltOpers.setIdEstadoFactura("".equals(this.idEstadoOperador) ? null : this.idEstadoOperador);
					//oFltOpers.setIdFactura(this.idFacturaGCSI);

					/*if ((this.numeroUnicoSiris != null) && (!"".equals(this.numeroUnicoSiris))) {
						oFltOpers.setNumeroUnicoSiris(this.numeroUnicoSiris);
					}*/
					
					if ((this.idTrxOperador != null) && (!"".equals(this.idTrxOperador))) {
						oFac.setIdTrxOperador(idTrxOperador);
					}

					if ((this.descEmpresa != null) && (!"".equals(this.descEmpresa))) {
						oFac.setDescEmpresa(descEmpresa);
					}

					if(this.getEstadoGCSI() != null && !this.getEstadoGCSI().isEmpty()) {
						oFltOpers.setEstadoOperacionGCSI(EstadoGCSI.fromValue(this.getEstadoGCSI()));
					}
					
					/*if(estado_trx != null) {
						oEstTrx = new Estado();
						oEstTrx.setCodMnemonico(estado_trx);
						oEstTrx.setOperador(oOpe);
								
						oFac.setEstado(oEstTrx);
					}*/
					/*
					 * oFltOpers.setEstadoTransaccion(value); oFltOpers.setEmpresaFactura(value);
					 * oFltOpers.setIdEstadoFactura(value);
					 */
					
					if (this.codBarra1 != null && this.codBarra1.trim() != "") {
						oFac.setCodBarra1(codBarra1);
					}
					
					if (this.codBarra2 != null && this.codBarra2.trim() != "") {
						oFac.setCodBarra2(codBarra2);
					}

					if(this.idFacturaGCSI != null && this.idFacturaGCSI.compareTo(0L) > 0)
						oFac.setId(idFacturaGCSI);

					if(oLote.getFacturas() == null)
						oLote.setFacturas(new Facturas());
					
					oLote.getFacturas().getFacturaGIREOrFacturaPNETOrFacturaWU().add(oFac);
					
					oOrdenBBDD = new OrdenBBDD();
					oOrdenBBDD.setCampoOrden("factura.fechaAlta"); //Del lote
					oOrdenBBDD.setTipoOrden("ASC");
					oFltOpers.getOrdenBBDD().add(oOrdenBBDD); 
					oFltOpers.setLote(oLote);
										
					if (!this.exportToExcel) {
						oConOperReq = new ConsultarOperacionesRequest();
						oConOperReq.setCodMnemonicoOperador(this.getOperadora());
						oConOperReq.setIdMayorista(this.getUsuario().getIdMayorista());
						oConOperReq.setIdCliente(this.getUsuario().getIdCliente());
						oConOperReq.setIdCanal("WEBINTRA");
						oConOperReq.setUsuario(this.getUsuario().getUsername());
						oConOperReq.setPwd(this.getUsuario().getPassword());
						oConOperReq.setFiltro(oFltOpers);

						listaOperacionesGCSI = new LazyOperacionGCSIDataModel(oConOperReq);
					} else {
						oDescRepReq = new DescargaReporteACG();
						oDescRepReq.setCodMnemonicoOperador(this.getOperadora());
						oDescRepReq.setIdMayorista(this.getUsuario().getIdMayorista());
						oDescRepReq.setIdCliente(this.getUsuario().getIdCliente());
						oDescRepReq.setIdCanal("WEBINTRA");
						oDescRepReq.setUsuario(this.getUsuario().getUsername());
						oDescRepReq.setPwd(this.getUsuario().getPassword());
						oFltOpers.setOffset(0);
						oFltOpers.setPageSize(this.getCantMaxRegistrosAexportar());
						oDescRepReq.setFiltroReporte(oFltOpers);
						oDescRepReq.setSeparadorCampo(this.getUsuario().getCsvSeparadorCampo());
						oDescRepReq.setSeparadorDecimales(this.getUsuario().getCsvSeparadorDecimales());
						
						oDescRepResp = GCSIServiceHelper.getGatewayGCSI(CfgTimeout.DEFAULT).descargaReporteACG(oDescRepReq);

						FacesContext fc = FacesContext.getCurrentInstance();
						ExternalContext ec = fc.getExternalContext();

						ec.responseReset();
						ec.setResponseContentType("text/plain");

						SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd_HHmmss");
						ec.setResponseHeader("Content-Disposition", "attachment; filename=\"" + sdf.format(new Date())
								+ "_(" + this.getUsuario().getIdMayorista() + ")_" + "Informe_" + this.getOperadora() + ".csv" + "\"");

						OutputStream os = ec.getResponseOutputStream();
						OutputStreamWriter osw = new OutputStreamWriter(os);
						PrintWriter writer = new PrintWriter(osw);
						writer.write(new String(oDescRepResp));
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
							"Informe GCSI. Error ejecutando el WS de consulta: |" + ste.getMessage() + "|");
					FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
							"Error ejecutando el WS de consulta: |" + ste.getMessage() + "|", null));
				}
			} else {
				LogACGHelper.escribirLog(null,
						"Informe GCSI. Error ejecutando el WS de consulta: |" + ste.getMessage() + "|");
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
						"Error ejecutando el WS de consulta: |" + ste.getMessage() + "|", null));
			}
			FacesContext.getCurrentInstance().validationFailed();
			this.list = null;
		} catch (Exception e) {
			LogACGHelper.escribirLog(null,
					"Informe GCSI. Excepcion ejecutando el WS de consulta: |" + e.getMessage() + "|");
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
					"Error ejecutando el WS de consulta de GCSI: |" + e.getMessage() + "|", null));
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

	public List<SelectItem> getGCSI_estados() {
		return gcsi_estados;
	}

	public void setGCSI_estados(List<SelectItem> gcsi_estados) {
		this.gcsi_estados = gcsi_estados;
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

	public Long getIdLoteGCSI() {
		return idLoteGCSI;
	}

	public void setIdLoteGCSI(Long idLoteGCSI) {
		this.idLoteGCSI = idLoteGCSI;
	}

	public Long getIdFacturaGCSI() {
		return idFacturaGCSI;
	}

	public void setIdFacturaGCSI(Long idFacturaGCSI) {
		this.idFacturaGCSI = idFacturaGCSI;
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

	public String getDescEmpresa() {
		return descEmpresa;
	}

	public void setDescEmpresa(String descEmpresa) {
		this.descEmpresa = descEmpresa;
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

	public String getIdTrxOperador() {
		return idTrxOperador;
	}

	public void setIdTrxOperador(String idTrxOperador) {
		this.idTrxOperador = idTrxOperador;
	}

	public int getPaginadorPaginasTotal() {
		return paginadorPaginasTotal;
	}

	public void setPaginadorPaginasTotal(int paginadorPaginasTotal) {
		this.paginadorPaginasTotal = paginadorPaginasTotal;
	}

	public String getOperadora() {
		return operadora;
	}

	public void setOperadora(String operadora) {
		this.operadora = operadora;
	}

	public List<SelectItem> getGcsi_estados() {
		return gcsi_estados;
	}

	public void setGcsi_estados(List<SelectItem> gcsi_estados) {
		this.gcsi_estados = gcsi_estados;
	}

	public LazyOperacionGCSIDataModel getListaOperacionesGCSI() {
		return listaOperacionesGCSI;
	}

	public void setListaOperacionesGCSI(LazyOperacionGCSIDataModel listaOperacionesGCSI) {
		this.listaOperacionesGCSI = listaOperacionesGCSI;
	}

	public void mostrarTicket(Long id) {
		String nombreTicketCompleto = "";
		FacturaGCSIWeb oFac = null;
		
		oFac = listaOperacionesGCSI.getRowData(id.toString());
		
		if (oFac.getFactura().getComprobantes() != null && oFac.getFactura().getComprobantes().size() > 0)
			ticket = oFac.getFactura().getComprobantes().get(0).getDetalleComprobante();
		else
			ticket = "";

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

		nombreTicketCompleto = "/secure/shared/tickets/ticketGCSIReporte.xhtml";

		// --- Abre el dialogo para buscar articulos
		PrimeFaces.current().dialog().openDynamic(nombreTicketCompleto, options, paramMap);
		PrimeFaces.current().executeScript("PF('blockPanelGral').hide()");
	}

	public void procesarExportacionTicketDeFactura(Long idFactura) {
		FacesContext oFacesCtx = null;
		ExternalContext oExtCtx = null;
		Document oPdfDoc = null;
		OutputStream oOutStream = null;
		PdfWriter oPdfWriter = null;
		Font oFont = null;
		Paragraph oParagraph = null;
		String oStrCompr = "";
		String oNomArch = "";
		FacturaGCSIWeb oFac = null;

		try {
			if (idFactura == null || idFactura.compareTo(0L) == 0)
				FacesContext.getCurrentInstance().addMessage(null,
						new FacesMessage(FacesMessage.SEVERITY_ERROR, "No se ha informado una factura.", null));

			oFac = listaOperacionesGCSI.getRowData(idFactura.toString());
			
			if (oFac.getFactura().getComprobantes() == null || oFac.getFactura().getComprobantes().size() == 0)
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
						"La factura informada no cuenta con comprobante.", null));
			else {
				oStrCompr = oFac.getFactura().getComprobantes().get(0).getDetalleComprobante();
				oNomArch = "CompTX".concat(oFac.getFactura().getIdTrxOperador());

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
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL,
					"Se ha producido un error en la exportacion del comprobante.", null));

			LogACGHelper.escribirLog(null,
					"Se ha producido un error en la exportacion del comprobante: |" + e.getMessage() + "|");
		}
	}

	public String getEstadoGCSI() {
		return estadoGCSI;
	}

	public void setEstadoGCSI(String estadoGCSI) {
		this.estadoGCSI = estadoGCSI;
	}
	
	
}