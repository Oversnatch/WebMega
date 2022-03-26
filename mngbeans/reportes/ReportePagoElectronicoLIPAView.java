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
import javax.inject.Named;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.xml.ws.WebServiceException;
import org.primefaces.PrimeFaces;
import org.springframework.context.annotation.Scope;
import com.americacg.cargavirtual.gateway.pagoElectronico.client.PagoElectronico;
import com.americacg.cargavirtual.gateway.pagoElectronico.funciones.FuncionLEOP;
import com.americacg.cargavirtual.gateway.pagoElectronico.funciones.FuncionLIBC;
import com.americacg.cargavirtual.gateway.pagoElectronico.funciones.FuncionLIMP;
import com.americacg.cargavirtual.gateway.pagoElectronico.funciones.FuncionLIOP;
import com.americacg.cargavirtual.gateway.pagoElectronico.funciones.FuncionLIPA;
import com.americacg.cargavirtual.gateway.pagoElectronico.model.entidad.BancoPagoElectronico;
import com.americacg.cargavirtual.gateway.pagoElectronico.model.entidad.EstadoOperador;
import com.americacg.cargavirtual.gateway.pagoElectronico.model.entidad.MedioDePago;
import com.americacg.cargavirtual.gateway.pagoElectronico.model.entidad.Operador;
import com.americacg.cargavirtual.gateway.pagoElectronico.model.entidad.Pago;
import com.americacg.cargavirtual.gateway.pagoElectronico.model.parametros.funciones.input.DataInputFcnLEOP;
import com.americacg.cargavirtual.gateway.pagoElectronico.model.parametros.funciones.input.DataInputFcnLIBC;
import com.americacg.cargavirtual.gateway.pagoElectronico.model.parametros.funciones.input.DataInputFcnLIMP;
import com.americacg.cargavirtual.gateway.pagoElectronico.model.parametros.funciones.input.DataInputFcnLIOP;
import com.americacg.cargavirtual.gateway.pagoElectronico.model.parametros.funciones.input.DataInputFcnLIPA;
import com.americacg.cargavirtual.gateway.pagoElectronico.model.parametros.funciones.output.DataOutputFcnLEOP;
import com.americacg.cargavirtual.gateway.pagoElectronico.model.parametros.funciones.output.DataOutputFcnLIBC;
import com.americacg.cargavirtual.gateway.pagoElectronico.model.parametros.funciones.output.DataOutputFcnLIMP;
import com.americacg.cargavirtual.gateway.pagoElectronico.model.parametros.funciones.output.DataOutputFcnLIOP;
import com.americacg.cargavirtual.gateway.pagoElectronico.model.parametros.funciones.output.DataOutputFcnLIPA;
import com.americacg.cargavirtual.gestion.model.ArrayOfCabeceraProducto;
import com.americacg.cargavirtual.gestion.model.CabeceraProducto;
import com.americacg.cargavirtual.gestion.model.ParametrosList;
import com.americacg.cargavirtual.gestion.model.PlatCfgH2HEntidadExt;
import com.americacg.cargavirtual.web.model.Error;
import com.americacg.cargavirtual.web.model.Factura;
import com.americacg.cargavirtual.web.enums.CfgTimeout;
import com.americacg.cargavirtual.web.helpers.ACGFormatHelper;
import com.americacg.cargavirtual.web.helpers.GestionServiceHelper;
import com.americacg.cargavirtual.web.helpers.LogACGHelper;
import com.americacg.cargavirtual.web.lazymodels.LazyLIPADataModel;

@Named("reportePagoElectronicoLIPAView")
@Scope(org.springframework.web.context.WebApplicationContext.SCOPE_SESSION)
public class ReportePagoElectronicoLIPAView extends ReporteGeneral<Factura> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2326266592185648986L;

	private LazyLIPADataModel lazyModel;
	private PagoElectronico pagoElectronico = null;
	private Integer tipoFiltroCliente = 3;
	protected Long idClienteFiltro = null;

	private String idPagoACG = null;
	private Integer idPagoOperador = null;
	private Long idOperador = null;
	private Long idCfgSite = null;
	private List<SelectItem> operadores;
	private Long idBanco;
	protected List<SelectItem> bancos = new ArrayList<SelectItem>();
	private Long idMedioDePago;
	protected List<SelectItem> mediosDePago = new ArrayList<SelectItem>();

	private String idEstadoOperador;
	protected List<SelectItem> estadoOperador = new ArrayList<SelectItem>();
	protected boolean agrJerarquia = false;
	protected boolean mostrarDatosSensibles = false;
	protected boolean infHistorico = false;
	protected Integer cantMaxRegPorPagReporte = 100000;
	
	private String headerIdConfiguracionComercio = null;
	private Long headerIdMayorista = null;
	private Long headerIdCliente = null;
	private Long headerIdUsuario = null;
	private String headerUsuario = null;
	private String headerClave = null;
	private String headerOrigen = null;

	@Override
	public Error resetearReporte() {
		Error err = super.resetearReporte();

		this.tipoFiltroCliente = 3;
		this.idClienteFiltro = null;
		this.idEstadoOperador = null;
		this.idPagoACG = null;
		this.mediosDePago = new ArrayList<SelectItem>();
		this.estadoOperador = new ArrayList<SelectItem>();
		this.infHistorico = false;

		com.americacg.cargavirtual.gateway.pagoElectronico.model.MensajeOutboundGateway mo = null;

		try {
			ArrayOfCabeceraProducto  aocp = GestionServiceHelper.getGestionService(CfgTimeout.DEFAULT).obtenerProductosHabilitadosParaOperarConValidacionDeHabilitacionDeProductoEnEstructuraDelCliente(this.getUsuario().getIdMayorista(), this.getUsuario().getIdCliente(), "ONH", true);
			
			List<CabeceraProducto> cpMepHabilitados = new ArrayList<CabeceraProducto>();
			
			if(aocp !=null && aocp.getCabeceraProducto() != null){ 
				for (CabeceraProducto cp : aocp.getCabeceraProducto()) {
					if("MEP".equals(cp.getCodMnemonicoTipoProducto().getValue())) {
						cpMepHabilitados.add(cp);
					}
				}
			}
			
			if(cpMepHabilitados.size() == 0) {
				LogACGHelper.escribirLog(null,
						"Inicializar Listado de Pago Electronico. Error: |NO tiene habilitado el producto MEP|");
				err.setError("Inicializar Listado de Pago Electronico",
						"Pago Electronico: NO tiene habilitado el producto MEP");

				return err;
				
			}else if(cpMepHabilitados.size() >1) {
				LogACGHelper.escribirLog(null,
						"Inicializar Listado de Pago Electronico. Error: |NO puede haber habilitado mas de un producto MEP|");
				err.setError("Inicializar Listado de Pago Electronico",
						"Pago Electronico: NO puede haber habilitado mas de un producto MEP");

				return err;				
			}else if(cpMepHabilitados.size() == 1) {
				if("MEPNAT".equals(cpMepHabilitados.get(0).getCodMnemonicoProducto().getValue())) {
					this.headerIdMayorista = this.getUsuario().getIdMayorista();
					this.headerIdCliente = this.getUsuario().getIdCliente();
					this.headerIdUsuario = this.getUsuario().getIdUsuario();
					this.headerUsuario = this.getUsuario().getUsername();
					this.headerClave = this.getUsuario().getPassword();
					this.headerIdConfiguracionComercio = null;
					this.headerOrigen = "M" + this.getUsuario().getIdMayorista() + "-C" + this.getUsuario().getIdCliente() + "-U" +this.getUsuario().getIdUsuario(); 
				}else if("MEPSP".equals(cpMepHabilitados.get(0).getCodMnemonicoProducto().getValue())) {
					
					PlatCfgH2HEntidadExt  platCfgH2HEntidadExt  = GestionServiceHelper.getGestionService(CfgTimeout.DEFAULT).obtenerPlatCfgH2HEntidadExt(this.getUsuario().getIdMayorista(), this.getUsuario().getIdCliente());
					
					if(platCfgH2HEntidadExt != null ) {
						this.headerIdMayorista = platCfgH2HEntidadExt.getIdMayoristaExterno().getValue();
						this.headerIdCliente = platCfgH2HEntidadExt.getIdClienteExterno().getValue();
						this.headerIdUsuario = platCfgH2HEntidadExt.getIdUsuarioExterno().getValue();
						this.headerUsuario = platCfgH2HEntidadExt.getUsrExterno().getValue();
						this.headerClave = platCfgH2HEntidadExt.getPwdExterno().getValue();
						this.headerIdConfiguracionComercio = null;
						this.headerOrigen = "M" + this.getUsuario().getIdMayorista() + "-C" + this.getUsuario().getIdCliente() + "-U" +this.getUsuario().getIdUsuario();					
					}else {
						LogACGHelper.escribirLog(null,
								"Inicializar Listado de Pago Electronico. Error: |El comercio no se encuentra habilitado para operar con MEPSP.|");
						err.setError("Inicializar Listado de Pago Electronico",
								"Pago Electronico: El comercio no se encuentra habilitado para operar con MEPSP.");

						return err;								
					}
					
				}else {
					LogACGHelper.escribirLog(null,
							"Inicializar Listado de Pago Electronico. Error: |Producto MEP no contemplado en esta version de la plataforma.|");
					err.setError("Inicializar Listado de Pago Electronico",
							"Pago Electronico: Producto MEP no contemplado en esta version de la plataforma.");
					return err;					
				}
				
			}else {
				LogACGHelper.escribirLog(null,
						"Inicializar Listado de Pago Electronico. Error: |NO pudo consultar si producto MEP esta habilitado.|");
				err.setError("Inicializar Listado de Pago Electronico",
						"Pago Electronico: NO pudo consultar si producto MEP esta habilitado.");
				return err;		
			}
			

			mostrarDatosSensibles = false;
			ParametrosList lp = GestionServiceHelper.getGestionService(CfgTimeout.DEFAULT)
					.mostrarParametros(this.getUsuario().getIdMayorista(), "P", "gpe_mostrar_datosSensiblesEnReportes");
			if (lp != null && lp.getListParametros() != null && lp.getListParametros().getValue() != null
					&& lp.getListParametros().getValue().getParametro() != null
					&& lp.getListParametros().getValue().getParametro().size() == 1) {
				mostrarDatosSensibles = "1"
						.equals(lp.getListParametros().getValue().getParametro().get(0).getValor().getValue().trim());
			} else {
				mostrarDatosSensibles = false;
			}

			cantMaxRegPorPagReporte = 100000;
			lp = GestionServiceHelper.getGestionService(CfgTimeout.DEFAULT)
					.mostrarParametros(this.getUsuario().getIdMayorista(), "E", "cant_max_reg_por_pag_reporte");
			if (lp != null && lp.getListParametros() != null && lp.getListParametros().getValue() != null
					&& lp.getListParametros().getValue().getParametro() != null
					&& lp.getListParametros().getValue().getParametro().size() == 1) {
				try {
					cantMaxRegPorPagReporte = Integer.parseInt(
							lp.getListParametros().getValue().getParametro().get(0).getValor().getValue().trim());
				} catch (Exception e) {
					cantMaxRegPorPagReporte = 100000;
				}

			} else {
				cantMaxRegPorPagReporte = 100000;
			}

			try {

				this.pagoElectronico = new PagoElectronico(this.getUsuario().getIdMayorista(),
						this.getUsuario().getIdCliente(), this.getUsuario().getIdUsuario(),
						this.getUsuario().getUsername(), this.getUsuario().getPassword(),
						this.getUsuario().getTipoCliente(), false);
			} catch (Exception e) {
				LogACGHelper.escribirLog(null, e.getMessage());
			}

			mo = null;
			this.operadores = new ArrayList<SelectItem>();

			FuncionLIOP fLIOP;
			fLIOP = new FuncionLIOP(this.pagoElectronico.getParametros());
			fLIOP.setParametroServicio(this.pagoElectronico.getParametrosServicio());

			fLIOP.getHeaderIn().setIdMayorista(this.headerIdMayorista);//
			fLIOP.getHeaderIn().setIdCliente(this.headerIdCliente); //
			fLIOP.getHeaderIn().setIdUsuario(this.headerIdUsuario);
			fLIOP.getHeaderIn().setUsuario(this.headerUsuario);
			fLIOP.getHeaderIn().setClave(this.headerClave);//
			fLIOP.getHeaderIn().setIdConfiguracionComercio(this.headerIdConfiguracionComercio);
			fLIOP.getHeaderIn().setOrigen(this.headerOrigen);

			fLIOP.setDataIn(new DataInputFcnLIOP());

			fLIOP.getDataIn().setIdOperador(this.idOperador);
			fLIOP.getDataIn().setPageSize(Integer.MAX_VALUE); // page size
			fLIOP.getDataIn().setPage(0); // page
			fLIOP.getDataIn().setCampoOrden("descripcion"); // campo orden
			fLIOP.getDataIn().setTipoOrden("asc"); // tipo Orden
			mo = fLIOP.ejecutar();

			if (mo != null) {
				if (mo.getHeaderOut() != null && mo.getHeaderOut().getCodigoRetorno() != null) {
					LogACGHelper.escribirLog(null,
							"Inicializar Pago Electronico. Error: |" + mo.getHeaderOut().getMensajeRetorno() + "|");

					err.setError(mo.getHeaderOut().getMensajeRetorno(), mo.getHeaderOut().getMensajeRetorno());

					return err;
				} else {
					DataOutputFcnLIOP doLIOP = (DataOutputFcnLIOP) mo.getDataOutputFcn();

					if (doLIOP != null) {
						if (doLIOP.getListOperadores() != null) {
							for (Operador ope : doLIOP.getListOperadores()) {
								this.operadores.add(new SelectItem(ope.getId(), ope.getDescripcion()));
							}
						}
					}
				}
			} else {
				LogACGHelper.escribirLog(null,
						"Inicializar Listado de Pago Electronico. Error: |No se obtuvo una respuesta valida desde el Gateway al consultar operadores|");
				err.setError("Inicializar Listado de Pago Electronico",
						"No se pudo cargar los operadores, por favor reintente nuevamente.");

				return err;
			}

			mo = null;
			this.bancos = new ArrayList<SelectItem>();

			FuncionLIBC fLIBC;
			fLIBC = new FuncionLIBC(this.pagoElectronico.getParametros());
			fLIBC.setParametroServicio(this.pagoElectronico.getParametrosServicio());

			fLIBC.getHeaderIn().setIdMayorista(this.headerIdMayorista);//
			fLIBC.getHeaderIn().setIdCliente(this.headerIdCliente); //
			fLIBC.getHeaderIn().setIdUsuario(this.headerIdUsuario);
			fLIBC.getHeaderIn().setUsuario(this.headerUsuario);
			fLIBC.getHeaderIn().setClave(this.headerClave);//
			fLIBC.getHeaderIn().setIdConfiguracionComercio(this.headerIdConfiguracionComercio);	
			fLIBC.getHeaderIn().setOrigen(this.headerOrigen);

			fLIBC.setDataIn(new DataInputFcnLIBC());

			fLIBC.getDataIn().setIdOperador(this.idOperador);
			fLIBC.getDataIn().setPageSize(Integer.MAX_VALUE); // page size
			fLIBC.getDataIn().setPage(0); // page
			fLIBC.getDataIn().setCampoOrden("denominacion"); // campo orden
			fLIBC.getDataIn().setTipoOrden("asc"); // tipo Orden
			mo = fLIBC.ejecutar();

			if (mo != null) {
				if (mo.getHeaderOut() != null && mo.getHeaderOut().getCodigoRetorno() != null) {
					LogACGHelper.escribirLog(null,
							"Inicializar Pago Electronico. Error: |" + mo.getHeaderOut().getMensajeRetorno() + "|");

					err.setError(mo.getHeaderOut().getMensajeRetorno(), mo.getHeaderOut().getMensajeRetorno());

					return err;
				} else {
					DataOutputFcnLIBC doLIBC = (DataOutputFcnLIBC) mo.getDataOutputFcn();

					if (doLIBC != null) {
						if (doLIBC.getListBancos() != null) {
							for (BancoPagoElectronico b : doLIBC.getListBancos()) {
								this.bancos.add(new SelectItem(b.getId(), b.getDenominacion()));
							}
						}
					}
				}
			} else {
				LogACGHelper.escribirLog(null,
						"Inicializar Listado de Pago Electronico. Error: |No se obtuvo una respuesta valida desde el Gateway al consultar bancos|");
				err.setError("Inicializar Listado de Pago Electronico",
						"No se pudo cargar los bancos, por favor reintente nuevamente.");

				return err;
			}

		} catch (Exception e) {
			LogACGHelper.escribirLog(null, "Inicializar Listado de Pago Electronico. Error: |" + e.getMessage() + "|");
			err.setError("Inicializar Listado de Pago Electronico",
					"No se pudo inicializar el reporte, por favor reintente nuevamente.");
		}

		return err;
	}

	public ReportePagoElectronicoLIPAView() {
		super();
	}

	public void obtenerDatos() {

	}

	public void onChangePagoOperador() {
		obtenerLIMP();
		obtenerLEOP();
	}

	public void obtenerLIMP() {
		try {
			com.americacg.cargavirtual.gateway.pagoElectronico.model.MensajeOutboundGateway mo = null;
			this.mediosDePago = new ArrayList<SelectItem>();

			FuncionLIMP fLIMP;

			fLIMP = new FuncionLIMP(this.pagoElectronico.getParametros());
			fLIMP.setParametroServicio(this.pagoElectronico.getParametrosServicio());

			fLIMP.getHeaderIn().setIdMayorista(this.headerIdMayorista);//
			fLIMP.getHeaderIn().setIdCliente(this.headerIdCliente); //
			fLIMP.getHeaderIn().setIdUsuario(this.headerIdUsuario);
			fLIMP.getHeaderIn().setUsuario(this.headerUsuario);
			fLIMP.getHeaderIn().setClave(this.headerClave);//
			fLIMP.getHeaderIn().setIdConfiguracionComercio(this.headerIdConfiguracionComercio);		
			fLIMP.getHeaderIn().setOrigen(this.headerOrigen);

			fLIMP.setDataIn(new DataInputFcnLIMP());
			fLIMP.getDataIn().setOperador(new Operador(this.idOperador));
			fLIMP.getDataIn().setPageSize(Integer.MAX_VALUE); // page size
			fLIMP.getDataIn().setPage(0); // page
			fLIMP.getDataIn().setOperacion("REPORTE");
			fLIMP.getDataIn().setCampoOrden("marca"); // campo orden
			fLIMP.getDataIn().setTipoOrden("asc"); // tipo Orden
			mo = fLIMP.ejecutar();

			if (mo != null) {
				if (mo.getHeaderOut() != null && mo.getHeaderOut().getCodigoRetorno() != null) {
					LogACGHelper.escribirLog(null,
							"Inicializar Pago Electronico. Error: |" + mo.getHeaderOut().getMensajeRetorno() + "|");
					FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
							"Inicializar Pago Electronico. " + mo.getHeaderOut().getMensajeRetorno(), null));
				} else {
					DataOutputFcnLIMP doLIMP = (DataOutputFcnLIMP) mo.getDataOutputFcn();

					if (doLIMP != null) {
						if (doLIMP.getMediosDePago() != null) {
							for (MedioDePago mp : doLIMP.getMediosDePago()) {
								this.mediosDePago.add(new SelectItem(mp.getId(), mp.getMarca()));
							}
						}
					}
				}
			} else {
				// respuestaPE = "No se obtuvo una respuesta valida desde el Gateway";
				LogACGHelper.escribirLog(null,
						"Obtener Medios de Pago. Error: |No se obtuvo una respuesta valida desde el Gateway|");
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
						"Obtener Medios de Pago. No se obtuvo una respuesta valida desde el Gateway", null));
			}
		} catch (Exception e) {
			// respuestaPE = "No se obtuvo una respuesta valida desde el Gateway";
			LogACGHelper.escribirLog(null, "Obtener Medios de Pago. Error: |" + e.getMessage() + "|");
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
					"Obtener Medios de Pago. No se obtuvo una respuesta valida desde el Gateway. Reintente nuevamente.",
					null));
		}

	}

	public void obtenerLEOP() {
		try {
			com.americacg.cargavirtual.gateway.pagoElectronico.model.MensajeOutboundGateway mo = null;
			this.estadoOperador = new ArrayList<SelectItem>();

			FuncionLEOP fLEOP;

			fLEOP = new FuncionLEOP(this.pagoElectronico.getParametros());
			fLEOP.setParametroServicio(this.pagoElectronico.getParametrosServicio());

			fLEOP.getHeaderIn().setIdMayorista(this.headerIdMayorista);//
			fLEOP.getHeaderIn().setIdCliente(this.headerIdCliente); //
			fLEOP.getHeaderIn().setIdUsuario(this.headerIdUsuario);
			fLEOP.getHeaderIn().setUsuario(this.headerUsuario);
			fLEOP.getHeaderIn().setClave(this.headerClave);//
			fLEOP.getHeaderIn().setIdConfiguracionComercio(this.headerIdConfiguracionComercio);	
			fLEOP.getHeaderIn().setOrigen(this.headerOrigen);

			fLEOP.setDataIn(new DataInputFcnLEOP());
			fLEOP.getDataIn().setIdOperador(this.idOperador);
			fLEOP.getDataIn().setPageSize(Integer.MAX_VALUE); // page size
			fLEOP.getDataIn().setPage(0); // page
			fLEOP.getDataIn().setCampoOrden("descripcionCortaACG"); // campo orden
			fLEOP.getDataIn().setTipoOrden("asc"); // tipo Orden
			mo = fLEOP.ejecutar();

			if (mo != null) {
				if (mo.getHeaderOut() != null && mo.getHeaderOut().getCodigoRetorno() != null) {
					LogACGHelper.escribirLog(null,
							"Inicializar Pago Electronico. Error: |" + mo.getHeaderOut().getMensajeRetorno() + "|");
					FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
							"Inicializar Pago Electronico. " + mo.getHeaderOut().getMensajeRetorno(), null));
				} else {
					DataOutputFcnLEOP doLEOP = (DataOutputFcnLEOP) mo.getDataOutputFcn();

					if (doLEOP != null) {
						if (doLEOP.getListEstadoOperador() != null) {
							for (EstadoOperador eo : doLEOP.getListEstadoOperador()) {
								this.estadoOperador
										.add(new SelectItem(eo.getCodigoOperador(), eo.getDescripcionCortaACG()));
							}
						}
					}
				}
			} else {
				// respuestaPE = "No se obtuvo una respuesta valida desde el Gateway";
				LogACGHelper.escribirLog(null,
						"Obtener Estado Operador. Error: |No se obtuvo una respuesta valida desde el Gateway|");
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
						"Obtener Estado Operador. No se obtuvo una respuesta valida desde el Gateway", null));
			}
		} catch (Exception e) {
			// respuestaPE = "No se obtuvo una respuesta valida desde el Gateway";
			LogACGHelper.escribirLog(null, "Obtener Estado Operador. Error: |" + e.getMessage() + "|");
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
					"Obtener Estado Operador. No se obtuvo una respuesta valida desde el Gateway. Reintente nuevamente.",
					null));
		}

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
		
		this.list = null;
		this.mostrarRegistros = true;
		
		StringBuilder sb = new StringBuilder();
		// DEFINO FORMATO DE FECHA PARA MOSTRAR EN EL REPORTE QUE SE EXPORTA
		//SimpleDateFormat ff = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		SimpleDateFormat ff = new SimpleDateFormat("yyyy-MM-dd");
		String csvSepCamp = this.getUsuario().getCsvSeparadorCampo();
		String csvSepDec = this.getUsuario().getCsvSeparadorDecimales();

		if (this.getUsuario().getTipoCliente() != null) {
			if ("P".equals(this.getUsuario().getTipoCliente())) {
				// Para el caso del Punto de venta donde no tengo filtro de cliente
				// System.out.println("Inicializo tipoFiltroCliente porque estoy en punto de
				// venta");
				tipoFiltroCliente = 1;
				idClienteFiltro = this.getUsuario().getIdCliente();
			}
		} else {
			PrimeFaces.current().dialog().showMessageDynamic(
					new FacesMessage("Atencion", "El tipo de Cliente no es valido para la consulta"));
			return;
		}

		// Prevalido las condiciones de filtro por cliente antes de Buscar la lista de
		// Clientes a consultar
		if ("P".equals(this.getUsuario().getTipoCliente())) {
			tipoFiltroCliente = 1;
			idClienteFiltro = this.getUsuario().getIdCliente();

		} else if (tipoFiltroCliente == null) {
			PrimeFaces.current().dialog()
					.showMessageDynamic(new FacesMessage("Atencion", "El tipoFiltroCliente es null"));
			return;
		} else if (tipoFiltroCliente == 3) {
			if (idClienteFiltro != null) {
				PrimeFaces.current().dialog()
						.showMessageDynamic(new FacesMessage("Atencion", "El cliente del filtro debe ser vacio"));
				return;
			} else {
				idClienteFiltro = this.getUsuario().getIdCliente();
			}

		} else if ((tipoFiltroCliente == 1) || (tipoFiltroCliente == 2)) {
			if ((idClienteFiltro == null) || (idClienteFiltro <= 0)) {
				PrimeFaces.current().dialog()
						.showMessageDynamic(new FacesMessage("Atencion", "Debe cargar un cliente para filtrar"));
				return;
			}

		} else {
			PrimeFaces.current().dialog()
					.showMessageDynamic(new FacesMessage("Atencion", "El tipoCliente de Filtro no es valido"));
			return;
		}
		
		com.americacg.cargavirtual.gestion.model.ListaDeStrings ls = GestionServiceHelper
				.getGestionService(CfgTimeout.DEFAULT).listaDeSubClientes(this.getUsuario().getIdMayorista(),
						this.getUsuario().getIdCliente(), tipoFiltroCliente, idClienteFiltro, false);
		if (ls == null) {
			PrimeFaces.current().dialog().showMessageDynamic(new FacesMessage("Atencion",
					"No se encontraron subclientes para consultar. La respuesta de la consulta fue null"));
			return;
		} else if (ls.getError().getValue().getHayError().getValue()) {
			PrimeFaces.current().dialog().showMessageDynamic(
					new FacesMessage("Atencion", ls.getError().getValue().getMsgError().getValue()));
			return;
		} else if ((ls.getL() == null) || (ls.getL().getValue() == null) || (ls.getL().getValue().getString() == null)
				|| (ls.getL().getValue().getString().size() <= 0)) {
			PrimeFaces.current().dialog()
					.showMessageDynamic(new FacesMessage("Atencion", "No se encontraron subclientes para consultar"));
			return;
		} else {

			try {

				this.mostrarRegistros = true;

				if (!this.exportToExcel) {
					
					lazyModel = new LazyLIPADataModel(this.pagoElectronico, null, headerIdMayorista,
							headerIdCliente, headerIdUsuario,
							headerUsuario, headerClave, this.headerOrigen, this.fechaHoraDesde,
							this.fechaHoraHasta, this.tipoFiltroCliente, this.idClienteFiltro, this.idEstadoOperador,
							this.idPagoOperador, this.idPagoACG, this.idOperador, this.idCfgSite, this.idBanco, this.idMedioDePago, this.infHistorico, ls,
							(this.getUsuario().getPermisosReducidosEnModuloDeCuentas() ? this.getUsuario().getIdUsuario() : null));
					PrimeFaces.current().executeScript("PF('panelFiltroWG').toggle();");					
				} else {

					com.americacg.cargavirtual.gateway.pagoElectronico.model.MensajeOutboundGateway mo = null;

					FuncionLIPA fLIPA = new FuncionLIPA(this.pagoElectronico.getParametros());

					fLIPA.getHeaderIn().setIdMayorista(this.headerIdMayorista);//
					fLIPA.getHeaderIn().setIdCliente(this.headerIdCliente); //
					fLIPA.getHeaderIn().setIdUsuario(this.headerIdUsuario);
					fLIPA.getHeaderIn().setUsuario(this.headerUsuario);
					fLIPA.getHeaderIn().setClave(this.headerClave);//
					fLIPA.getHeaderIn().setIdConfiguracionComercio(this.headerIdConfiguracionComercio);	
					fLIPA.getHeaderIn().setOrigen(this.headerOrigen);
					
					fLIPA.setParametroServicio(this.pagoElectronico.getParametrosServicio());
					fLIPA.setDataIn(new DataInputFcnLIPA());
					// fLIPA.getDataIn().setIdDePago(this.idDePago); // idPago
					fLIPA.getDataIn().setIdMayorista(this.getUsuario().getIdMayorista()); // mayorista
					if (this.tipoFiltroCliente == 1) {
						
						fLIPA.getDataIn().setIdCliente(idClienteFiltro); // cliente
//						fLIPA.getDataIn().setIdCliente(this.getUsuario().getIdCliente()); // cliente
	//					fLIPA.getDataIn().setIdUsuario(this.getUsuario().getIdUsuario()); // usuario
					}					
					fLIPA.getDataIn().setFechaDesde(this.fechaHoraDesde); // Fecha desde
					fLIPA.getDataIn().setFechaHasta(this.fechaHoraHasta); // Fecha hasta
					// this.tipoFiltroCliente
					// fLIPA.getDataIn().setIdCliente(this.idClienteFiltro);
					fLIPA.getDataIn().setEstadoOperador(this.idEstadoOperador);
					fLIPA.getDataIn().setIdPagoACG(this.idPagoACG);
					fLIPA.getDataIn().setPaymentId(this.idPagoOperador);

					fLIPA.getDataIn().setIdUsuario((this.getUsuario().getPermisosReducidosEnModuloDeCuentas() ? this.getUsuario().getIdUsuario() : null));
					
					if (tipoFiltroCliente == 1) {
						fLIPA.getDataIn().setListaIdClientes(null);
					} else {
						StringBuilder sbL = new StringBuilder();
						int i = 0;
						while (i < ls.getL().getValue().getString().size() - 1) {
							sbL.append(ls.getL().getValue().getString().get(i));
							sbL.append(",");
							i++;
						}
						sbL.append(ls.getL().getValue().getString().get(i));

						fLIPA.getDataIn().setListaIdClientes(sbL.toString());
					}
					
					fLIPA.getDataIn().setIdOperador(this.idOperador);
					fLIPA.getDataIn().setIdCfgSite(this.idCfgSite); 
					fLIPA.getDataIn().setIdBanco(this.idBanco);
					fLIPA.getDataIn().setIdMedioDePago(this.idMedioDePago);
					fLIPA.getDataIn().setPageSize(this.cantMaxRegPorPagReporte); // page size
					fLIPA.getDataIn().setPage(1); // page
					fLIPA.getDataIn().setCampoOrden("idPago"); // campo orden
					fLIPA.getDataIn().setTipoOrden("DESC"); // tipo Orden

					mo = fLIPA.ejecutar();

					if (mo != null) {

						if (!"M0000".equals(mo.getDataOutputFcn().getCodigoRetorno())) {
							PrimeFaces.current().dialog().showMessageDynamic(
									new FacesMessage("Atencion", mo.getDataOutputFcn().getMensajeRetorno()));
							throw new RuntimeException(mo.getDataOutputFcn().getMensajeRetorno());
						}

						DataOutputFcnLIPA doLIPA = (DataOutputFcnLIPA) mo.getDataOutputFcn();

						if (doLIPA != null) {
							if (doLIPA.getListPagos() != null) {
								if (doLIPA.getListPagos().size() == 0) {
									PrimeFaces.current().dialog().showMessageDynamic(new FacesMessage("Atencion",
											"No hay registros que correspondan a esta busqueda"));
									throw new RuntimeException("No hay registros que correspondan a esta busqueda");
								} else {

									mostrarArchivoCSV = doLIPA.getListPagos().size() > 0;

									// Header
									sb.append((char) 34).append("Id").append((char) 34).append(csvSepCamp);
									sb.append((char) 34).append("Id Terminal").append((char) 34).append(csvSepCamp);
									sb.append((char) 34).append("Fecha Cobranza").append((char) 34).append(csvSepCamp);
									sb.append((char) 34).append("Fecha Acreditacion").append((char) 34).append(csvSepCamp);
									sb.append((char) 34).append("Acreditado").append((char) 34).append(csvSepCamp);
									sb.append((char) 34).append("Estado Operador").append((char) 34).append(csvSepCamp);
									sb.append((char) 34).append("Moneda").append((char) 34).append(csvSepCamp);
									sb.append((char) 34).append("Importe Total").append((char) 34).append(csvSepCamp);
									sb.append((char) 34).append("Nro. Comprobante").append((char) 34).append(csvSepCamp);
									
									sb.append((char) 34).append("ID Cliente").append((char) 34).append(csvSepCamp);
									sb.append((char) 34).append("Cliente").append((char) 34).append(csvSepCamp);

									if (agrJerarquia) {
										sb.append((char) 34).append("ID Distribuidor 1").append((char) 34)
												.append(csvSepCamp);
										sb.append((char) 34).append("Distribuidor 1").append((char) 34)
												.append(csvSepCamp);
										sb.append((char) 34).append("ID Distribuidor 2").append((char) 34)
												.append(csvSepCamp);
										sb.append((char) 34).append("Distribuidor 2").append((char) 34)
												.append(csvSepCamp);
										sb.append((char) 34).append("ID Distribuidor 3").append((char) 34)
												.append(csvSepCamp);
										sb.append((char) 34).append("Distribuidor 3").append((char) 34)
												.append(csvSepCamp);
										sb.append((char) 34).append("ID Distribuidor 4").append((char) 34)
												.append(csvSepCamp);
										sb.append((char) 34).append("Distribuidor 4").append((char) 34)
												.append(csvSepCamp);
										sb.append((char) 34).append("ID Distribuidor 5").append((char) 34)
												.append(csvSepCamp);
										sb.append((char) 34).append("Distribuidor 5").append((char) 34)
												.append(csvSepCamp);
									}

									sb.append((char) 34).append("Id Usuario").append((char) 34).append(csvSepCamp);
									sb.append((char) 34).append("Usuario").append((char) 34).append(csvSepCamp);
									sb.append((char) 34).append("Concepto de Pago").append((char) 34)
											.append(csvSepCamp);
									sb.append((char) 34).append("Descripcion Adicional de Pago").append((char) 34)
											.append(csvSepCamp);
									sb.append((char) 34).append("Comerciante Id").append((char) 34).append(csvSepCamp);
									sb.append((char) 34).append("Nombre Comercio").append((char) 34).append(csvSepCamp);
									sb.append((char) 34).append("Numero Ticket ACG").append((char) 34)
											.append(csvSepCamp);
									sb.append((char) 34).append("Numero Ticket Operador").append((char) 34)
											.append(csvSepCamp);
									sb.append((char) 34).append("ID Banco").append((char) 34).append(csvSepCamp);
									sb.append((char) 34).append("Banco").append((char) 34).append(csvSepCamp);
									sb.append((char) 34).append("ID Banco BCRA").append((char) 34).append(csvSepCamp);
									
									sb.append((char) 34).append("Metodo De Pago").append((char) 34).append(csvSepCamp);
									sb.append((char) 34).append("Tipo Medio De Pago").append((char) 34)
											.append(csvSepCamp);

									sb.append((char) 34).append("Importe Neto").append((char) 34).append(csvSepCamp);
									sb.append((char) 34).append("Importe Costo Financiero").append((char) 34)
											.append(csvSepCamp);
									
									sb.append((char) 34).append("Cuotas").append((char) 34).append(csvSepCamp);
									sb.append((char) 34).append("Etiqueta Plan").append((char) 34).append(csvSepCamp);
									sb.append((char) 34).append("Numero Tarjeta").append((char) 34).append(csvSepCamp);
									if ("M".equals(this.getUsuario().getTipoCliente().toUpperCase())
											&& this.mostrarDatosSensibles) {
										sb.append((char) 34).append("Titular Tarjeta").append((char) 34)
												.append(csvSepCamp);
										sb.append((char) 34).append("Tipo Documento").append((char) 34).append(csvSepCamp);
										sb.append((char) 34).append("Nro. Documento").append((char) 34)
												.append(csvSepCamp);
										sb.append((char) 34).append("Telefono Titular Tarjeta").append((char) 34)
												.append(csvSepCamp);
										sb.append((char) 34).append("Email Titular Tarjeta").append((char) 34)
												.append(csvSepCamp);
									}

									sb.append((char) 34).append("Estado Descripcion").append((char) 34)
											.append(csvSepCamp);
									sb.append((char) 34).append("Con Anulacion").append((char) 34).append(csvSepCamp);
									sb.append((char) 34).append("Tipo Carga").append((char) 34).append(csvSepCamp);
									sb.append((char) 34).append("ID Anul. Operador").append((char) 34)
											.append(csvSepCamp);
									sb.append((char) 34).append("Nro. Ticket Oper. Anul.").append((char) 34)
											.append(csvSepCamp);
									sb.append((char) 34).append("Nro. Ticket ACG Anul.").append((char) 34);
									sb.append((char) 13).append((char) 10);

									for (Pago pago : doLIPA.getListPagos()) {

										// CREO ARCHIVO DE EXPORTACION SI ES NECESARIO PARA EXPORTAR A CSV
										sb.append((char) 34).append(pago.getId()).append((char) 34).append(csvSepCamp);
										sb.append((char) 34).append("'").append(pago.getIdTerminal()).append((char) 34)
												.append(csvSepCamp);
										sb.append((char) 34).append(ff.format(pago.getFechaDeServidor()))
												.append((char) 34).append(csvSepCamp);
										sb.append((char) 34).append((pago.getFechaAcreditacionCuenta() != null ? ff.format(pago.getFechaAcreditacionCuenta()) : "")).append((char) 34).append(csvSepCamp);
										
										sb.append((char) 34).append((pago.isAcreditadoEnCuenta() ? "SI" : "NO")).append((char) 34).append(csvSepCamp);
										
										sb.append((char) 34).append(pago.getEstadoOperador().getDescripcionLargaACG()).append((char) 34).append(csvSepCamp);
										sb.append((char) 34).append(pago.getMoneda().getSimbolo()).append((char) 34).append(csvSepCamp);
										sb.append((char) 34).append(ACGFormatHelper.format(pago.getImporteCalculado().floatValue(), csvSepDec)).append((char) 34).append(csvSepCamp);
										sb.append((char) 34).append(pago.getIdPagoOperador()).append((char) 34).append(csvSepCamp);
										
										sb.append((char) 34).append(pago.getClientePagoElectronico().getId())
												.append((char) 34).append(csvSepCamp);
										sb.append((char) 34).append(pago.getClientePagoElectronico().getRazonSocial())
												.append((char) 34).append(csvSepCamp);

										if (agrJerarquia) {
											sb.append((char) 34)
													.append(pago.getClientePagoElectronico().getIdDistribuidor1())
													.append((char) 34).append(csvSepCamp);
											sb.append((char) 34)
													.append(pago.getClientePagoElectronico().getRazonSocialDist1())
													.append((char) 34).append(csvSepCamp);
											sb.append((char) 34)
													.append(pago.getClientePagoElectronico().getIdDistribuidor2())
													.append((char) 34).append(csvSepCamp);
											sb.append((char) 34)
													.append(pago.getClientePagoElectronico().getRazonSocialDist2())
													.append((char) 34).append(csvSepCamp);
											sb.append((char) 34)
													.append(pago.getClientePagoElectronico().getIdDistribuidor3())
													.append((char) 34).append(csvSepCamp);
											sb.append((char) 34)
													.append(pago.getClientePagoElectronico().getRazonSocialDist3())
													.append((char) 34).append(csvSepCamp);
											sb.append((char) 34)
													.append(pago.getClientePagoElectronico().getIdDistribuidor4())
													.append((char) 34).append(csvSepCamp);
											sb.append((char) 34)
													.append(pago.getClientePagoElectronico().getRazonSocialDist4())
													.append((char) 34).append(csvSepCamp);
											sb.append((char) 34)
													.append(pago.getClientePagoElectronico().getIdDistribuidor5())
													.append((char) 34).append(csvSepCamp);
											sb.append((char) 34)
													.append(pago.getClientePagoElectronico().getRazonSocialDist5())
													.append((char) 34).append(csvSepCamp);
										}

										sb.append((char) 34).append(pago.getUsuarioPagoElectronico().getId())
												.append((char) 34).append(csvSepCamp);
										sb.append((char) 34).append(pago.getUsuarioPagoElectronico().getUsuario())
												.append((char) 34).append(csvSepCamp);

										sb.append((char) 34).append(pago.getConceptoDePago().getDescripcion())
												.append((char) 34).append(csvSepCamp);
										sb.append((char) 34).append(pago.getDescripcionAdicionalPago())
												.append((char) 34).append(csvSepCamp);
										sb.append((char) 34).append(pago.getMerchantId()).append((char) 34)
												.append(csvSepCamp);
										sb.append((char) 34).append(pago.getNombreComercio()).append((char) 34)
												.append(csvSepCamp);
										sb.append((char) 34).append("'").append(pago.getNumeroTicketACG())
												.append((char) 34).append(csvSepCamp);
										sb.append((char) 34).append(pago.getNumeroTicketOperador()).append((char) 34)
												.append(csvSepCamp);
										sb.append((char) 34).append(pago.getIdBanco()).append((char) 34)
												.append(csvSepCamp);
										sb.append((char) 34).append(pago.getBancoDenominacion()).append((char) 34)
												.append(csvSepCamp);
										sb.append((char) 34)
												.append((pago.getIdBancoBCRA() == null
														|| "".equals(pago.getIdBancoBCRA())
														|| "0".equals(pago.getIdBancoBCRA())) ? "No Aplica"
																: pago.getIdBancoBCRA())
												.append((char) 34).append(csvSepCamp);
										
										sb.append((char) 34).append(pago.getMetodoDePago().getMarca()).append((char) 34)
												.append(csvSepCamp);
										sb.append((char) 34).append(pago.getMetodoDePago().getTipoMedioDePago())
												.append((char) 34).append(csvSepCamp);
																				
										sb.append((char) 34).append(
												ACGFormatHelper.format(pago.getImporteBase().floatValue(), csvSepDec))
												.append((char) 34).append(csvSepCamp);
										sb.append((char) 34).append(ACGFormatHelper.format(pago.getImporteCalculado().subtract(pago.getImporteBase()).floatValue(), csvSepDec)).append((char) 34).append(csvSepCamp);
										
										sb.append((char) 34).append(pago.getCantidadCuotas()).append((char) 34)
												.append(csvSepCamp);
										sb.append((char) 34).append(pago.getEtiqueta()).append((char) 34)
										.append(csvSepCamp);
										
										sb.append((char) 34).append("'")
												.append(pago.getTokenOperador().getNumeroTarjeta()).append((char) 34)
												.append(csvSepCamp);
										if ("M".equals(this.getUsuario().getTipoCliente().toUpperCase())
												&& this.mostrarDatosSensibles) {
											sb.append((char) 34).append("'").append(pago.getNombreTitularTarjeta())
													.append((char) 34).append(csvSepCamp);
											//sb.append((char) 34).append("'").append(pago.getIdTipoDocumento()).append((char) 34).append(csvSepCamp);
											sb.append((char) 34).append("'").append(pago.getDescripcionTipoDocumento()).append((char) 34).append(csvSepCamp);
											sb.append((char) 34).append("'").append(pago.getNroDocumento())
													.append((char) 34).append(csvSepCamp);
											sb.append((char) 34).append("'").append(pago.getTelefonoCliente())
													.append((char) 34).append(csvSepCamp);
											sb.append((char) 34).append("'").append(pago.getEmailCliente())
													.append((char) 34).append(csvSepCamp);
										}

										sb.append((char) 34).append(pago.getEstado().getDescripcion()).append((char) 34)
												.append(csvSepCamp);
										sb.append((char) 34).append(pago.isConAnulacion() ? "SI" : "NO")
												.append((char) 34).append(csvSepCamp);
										sb.append((char) 34)
												.append(pago.getTipoCarga() != null
														&& "M".equals(pago.getTipoCarga().toUpperCase())
																? "Manual"
																: (pago.getTipoCarga() != null
																		&& "A".equals(pago.getTipoCarga().toUpperCase())
																				? "Automatico"
																				: ""))
												.append((char) 34).append(csvSepCamp);
										sb.append((char) 34).append(pago.getIdOperadorAnulacion()).append((char) 34)
												.append(csvSepCamp);
										sb.append((char) 34).append(pago.getNumeroTicketOperadorAnulacion())
												.append((char) 34).append(csvSepCamp);
										sb.append((char) 34).append("'").append(pago.getNumeroTicketACGAnulacion())
												.append((char) 34).append(csvSepCamp);
										sb.append((char) 13).append((char) 10);

									}

									// GENERO ARCHIVO DE EXPORTACION SI ES NECESARIO PARA EXPORTAR A CSV

									FacesContext fc = FacesContext.getCurrentInstance();
									ExternalContext ec = fc.getExternalContext();

									ec.responseReset();
									ec.setResponseContentType("text/plain");
									ec.setResponseContentLength(sb.toString().length());

									SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd_HHmmss");
									ec.setResponseHeader("Content-Disposition",
											"attachment; filename=\"" + sdf.format(new Date()) + "_("
													+ this.getUsuario().getIdMayorista() + ")_"
													+ "InformePagosElectronicos.csv" + "\"");

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
								PrimeFaces.current().dialog().showMessageDynamic(new FacesMessage("Atencion",
										"No se obtuvo una respuesta valida desde el Gateway"));
								throw new RuntimeException("No se obtuvo una respuesta valida desde el Gateway");
							}
						} else {
							PrimeFaces.current().dialog().showMessageDynamic(
									new FacesMessage("Atencion", "No se obtuvo una respuesta valida desde el Gateway"));
							throw new RuntimeException("No se obtuvo una respuesta valida desde el Gateway");
						}
					} else {
						PrimeFaces.current().dialog().showMessageDynamic(
								new FacesMessage("Atencion", "No se obtuvo una respuesta valida desde el Gateway"));
						throw new RuntimeException("No se obtuvo una respuesta valida desde el Gateway");
					}
				
				}
			} catch (WebServiceException ste) {
				if (ste != null && ste.getCause() != null && ste.getCause().getClass() != null) {
					if (ste.getCause().getClass().equals(ConnectException.class)) {
						FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
								"No se pudo establecer la comunicación (GPN-TOC).\n Por favor intente nuevamente.",
								null));
					} else if (ste.getCause().getClass().equals(SocketTimeoutException.class)) {
						FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
								"No se pudo establecer la comunicación (GPN-TRW).\n Por favor intente nuevamente.",
								null));
					} else if (ste.getCause().getClass().equals(UnknownHostException.class)) {
						LogACGHelper.escribirLog(null,
								"No se pudo establecer la comunicación (GPN-HNC): |" + ste.getMessage() + "|");
						FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
								"No se pudo establecer la comunicación (GPN-HNC).\n Por favor intente nuevamente.",
								null));
					} else {
						LogACGHelper.escribirLog(null, "Informe Pago Electronico. Error ejecutando el WS de consulta: |"
								+ ste.getMessage() + "|");
						FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
								"Error ejecutando el WS de consulta: |" + ste.getMessage() + "|", null));
					}
				} else {
					LogACGHelper.escribirLog(null,
							"Informe Pago Electronico. Error ejecutando el WS de consulta: |" + ste.getMessage() + "|");
					FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
							"Error ejecutando el WS de consulta: |" + ste.getMessage() + "|", null));
				}
				FacesContext.getCurrentInstance().validationFailed();
				this.list = null;
			} catch (Exception e) {
				LogACGHelper.escribirLog(null,
						"Informe Pago Electronico. Excepcion ejecutando el WS de consulta: |" + e.getMessage() + "|");
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
						"Error ejecutando el WS de consulta de Pago Electronico: |" + e.getMessage() + "|", null));
				FacesContext.getCurrentInstance().validationFailed();
				this.list = null;
			}
		}
		PrimeFaces.current().executeScript("PF('blockPanelGral').hide()");
		return;
	}

	public LazyLIPADataModel getLazyModel() {
		return lazyModel;
	}

	public void setLazyModel(LazyLIPADataModel lazyModel) {
		this.lazyModel = lazyModel;
	}

	public String getIdEstadoOperador() {
		return idEstadoOperador;
	}

	public void setIdEstadoOperador(String idEstadoOperador) {
		this.idEstadoOperador = idEstadoOperador;
	}

	public String getIdPagoACG() {
		return idPagoACG;
	}

	public void setIdPagoACG(String idPagoACG) {
		this.idPagoACG = idPagoACG;
	}

	public PagoElectronico getPagoElectronico() {
		return pagoElectronico;
	}

	public void setPagoElectronico(PagoElectronico pagoElectronico) {
		this.pagoElectronico = pagoElectronico;
	}

	public Integer getTipoFiltroCliente() {
		return tipoFiltroCliente;
	}

	public void setTipoFiltroCliente(Integer tipoFiltroCliente) {
		this.tipoFiltroCliente = tipoFiltroCliente;
	}

	public Long getIdClienteFiltro() {
		return idClienteFiltro;
	}

	public void setIdClienteFiltro(Long idClienteFiltro) {
		this.idClienteFiltro = idClienteFiltro;
	}

	public Long getIdOperador() {
		return idOperador;
	}

	public void setIdOperador(Long idOperador) {
		this.idOperador = idOperador;
	}

	public Long getIdCfgSite() {
		return idCfgSite;
	}

	public void setIdCfgSite(Long idCfgSite) {
		this.idCfgSite = idCfgSite;
	}

	public List<SelectItem> getOperadores() {
		return operadores;
	}

	public void setOperadores(List<SelectItem> operadores) {
		this.operadores = operadores;
	}

	public Long getIdBanco() {
		return idBanco;
	}

	public void setIdBanco(Long idBanco) {
		this.idBanco = idBanco;
	}

	public List<SelectItem> getBancos() {
		return bancos;
	}

	public void setBancos(List<SelectItem> bancos) {
		this.bancos = bancos;
	}

	public Long getIdMedioDePago() {
		return idMedioDePago;
	}

	public void setIdMedioDePago(Long idMedioDePago) {
		this.idMedioDePago = idMedioDePago;
	}

	public List<SelectItem> getMediosDePago() {
		return mediosDePago;
	}

	public void setMediosDePago(List<SelectItem> mediosDePago) {
		this.mediosDePago = mediosDePago;
	}

	public boolean isAgrJerarquia() {
		return agrJerarquia;
	}

	public void setAgrJerarquia(boolean agrJerarquia) {
		this.agrJerarquia = agrJerarquia;
	}

	public List<SelectItem> getEstadoOperador() {
		return estadoOperador;
	}

	public void setEstadoOperador(List<SelectItem> estadoOperador) {
		this.estadoOperador = estadoOperador;
	}

	public boolean isMostrarDatosSensibles() {
		return mostrarDatosSensibles;
	}

	public void setMostrarDatosSensibles(boolean mostrarDatosSensibles) {
		this.mostrarDatosSensibles = mostrarDatosSensibles;
	}

	public boolean isInfHistorico() {
		return infHistorico;
	}

	public void setInfHistorico(boolean infHistorico) {
		this.infHistorico = infHistorico;
	}

	
	public Integer getIdPagoOperador() {
		return idPagoOperador;
	}

	
	public void setIdPagoOperador(Integer idPagoOperador) {
		this.idPagoOperador = idPagoOperador;
	}	
}