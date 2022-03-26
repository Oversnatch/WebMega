package com.americacg.cargavirtual.web.lazymodels;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import javax.faces.application.FacesMessage;
import org.apache.commons.beanutils.PropertyUtils;
import org.primefaces.PrimeFaces;
import org.primefaces.model.FilterMeta;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortMeta;
import org.springframework.beans.BeanUtils;
import com.americacg.cargavirtual.gateway.pagoElectronico.client.PagoElectronico;
import com.americacg.cargavirtual.gateway.pagoElectronico.funciones.FuncionLIPA;
import com.americacg.cargavirtual.gateway.pagoElectronico.model.entidad.Pago;
import com.americacg.cargavirtual.gateway.pagoElectronico.model.parametros.funciones.input.DataInputFcnLIPA;
import com.americacg.cargavirtual.gateway.pagoElectronico.model.parametros.funciones.output.DataOutputFcnLIPA;
import com.americacg.cargavirtual.web.helpers.LogACGHelper;

public class LazyLIPADataModel extends LazyDataModel<Pago> {
	/**
	 * 
	 */
	private static final long serialVersionUID = 3780015991880104269L;
	private List<Pago> datasource;
	private PagoElectronico pagoElectronico = null;
	private Float importeTotalAbonado = 0F;
	private String idConfiguracionComercio;
	private Long usuarioIdMayorista; // mayorista
	private Long usuarioIdCliente; // cliente
	private Long usuarioIdUsuario; // usuario
	private String usuarioUsuario;
	private String usuarioClave;
	private String usuarioOrigen;	
	private Date fechaDesde; // Fecha desde
	private Date fechaHasta; // Fecha hasta
	private Integer tipoFiltroCliente = null;
	private Long idClienteFiltro = null;
	private String idEstadoOperador;
	private Integer idPagoOperador = null;
	private String idPagoACG = null;
	private Long idOperador;
	private Long idCfgSite;
	private Long idBanco;
	private Long idMedioDePago;
	private boolean infHistorico; 
	private Long idUsuarioPermReducido;
	private com.americacg.cargavirtual.gestion.model.ListaDeStrings ls;

	public LazyLIPADataModel(PagoElectronico pagoElectronico, String idConfiguracionComercio, Long usuarioIdMayorista,
			Long usuarioIdCliente, Long usuarioIdUsuario, String usuarioUsuario, String usuarioClave, String usuarioOrigen, Date fechaDesde,
			Date fechaHasta, Integer tipoFiltroCliente, Long idClienteFiltro, String idEstadoOperador,
			Integer idPagoOperador, String idPagoACG, Long idOperador, Long idCfgSite, Long idBanco, Long idMedioDePago, boolean infHistorico, com.americacg.cargavirtual.gestion.model.ListaDeStrings ls, Long idUsuarioPermReducido) {

		this.pagoElectronico = pagoElectronico;
		this.idConfiguracionComercio = idConfiguracionComercio;
		this.usuarioIdMayorista = usuarioIdMayorista;
		this.usuarioIdCliente = usuarioIdCliente;
		this.usuarioIdUsuario = usuarioIdUsuario;
		this.usuarioUsuario = usuarioUsuario;
		this.usuarioClave = usuarioClave;
		this.usuarioOrigen = usuarioOrigen;
		this.fechaDesde = fechaDesde;
		this.fechaHasta = fechaHasta;
		this.tipoFiltroCliente = tipoFiltroCliente;
		this.idClienteFiltro = idClienteFiltro;
		this.idEstadoOperador = idEstadoOperador;
		this.idPagoOperador = idPagoOperador;
		this.idPagoACG = idPagoACG;
		this.idOperador = idOperador;
		this.idCfgSite = idCfgSite;
		this.idBanco = idBanco;
		this.idMedioDePago = idMedioDePago;
		this.infHistorico = infHistorico;
		this.ls = ls;
		this.idUsuarioPermReducido = idUsuarioPermReducido;
	}

	@Override
	public Pago getRowData(String rowKey) {
		// REVISAR
		if (datasource != null) {
			for (Pago pago : datasource) {
				if (pago.getId().toString().equals(rowKey))
					return pago;
			}
		}
		return null;
	}

	@Override
	public String getRowKey(Pago pago) {
		return pago.getId().toString();
	}

	@Override
	public List<Pago> load(int first, int pageSize, Map<String, SortMeta> sortBy, Map<String, FilterMeta> filterBy) {
		datasource = new ArrayList<Pago>();
		boolean propNroEmpty = false;
		boolean propNro = false;
		Pago pago = new Pago();

		if (!filterBy.isEmpty()) {
			try {
				for (Map.Entry<String, FilterMeta> oProp : filterBy.entrySet()) {
					Class<?> xtype = PropertyUtils.getPropertyType(pago, oProp.getKey());
					Object value = oProp.getValue().getFilterValue();

					Object oVal = null;

					if (value != null) {
						propNroEmpty = false;
						if (Integer.class.isAssignableFrom(xtype)) {
							propNro = true;
							oVal = Integer.valueOf(value.toString()).intValue();
							if (((Integer) oVal).compareTo(-1) == 0)
								propNroEmpty = true;
						} else if (Double.class.isAssignableFrom(xtype)) {
							propNro = true;
							oVal = Double.valueOf(value.toString()).doubleValue();
							if (((Double) oVal).compareTo(-1d) == 0)
								propNroEmpty = true;
						} else if (Long.class.isAssignableFrom(xtype)) {
							propNro = true;
							oVal = Long.valueOf(value.toString()).longValue();
							if (((Long) oVal).compareTo(-1L) == 0)
								propNroEmpty = true;
						} else if (Float.class.isAssignableFrom(xtype)) {
							propNro = true;
							oVal = Float.valueOf(value.toString()).floatValue();
							if (((Float) oVal).compareTo(-1f) == 0)
								propNroEmpty = true;
						} else if (BigDecimal.class.isAssignableFrom(xtype)) {
							// TODO replicar a todos los LAZY o armar un helper para sacarlo de todos los
							// LAZY propNro = true;
							try {
								oVal = new BigDecimal(value.toString());
								if (((BigDecimal) oVal).compareTo(new BigDecimal(-1)) == 0)
									propNroEmpty = true;
							} catch (Exception e) {
								propNroEmpty = true;
								oVal = BigDecimal.ZERO;
							}
						} else {
							propNro = false;
							oVal = value;
						}
					}

					if (propNro && propNroEmpty) {
						PropertyUtils.setProperty(pago, oProp.getKey(), null);
					} else
						PropertyUtils.setProperty(pago, oProp.getKey(), oVal);
				}

			} catch (Exception e) {
				LogACGHelper.escribirLog(null,
						"Se produjo un error LazyLIPADataModel metodo load (ROL). Msg:|" + e.getMessage() + "|");
			}
		}

		try {

			com.americacg.cargavirtual.gateway.pagoElectronico.model.MensajeOutboundGateway mo = null;

			FuncionLIPA fLIPA = new FuncionLIPA(this.pagoElectronico.getParametros());

			fLIPA.getHeaderIn().setIdMayorista(this.usuarioIdMayorista);//
			fLIPA.getHeaderIn().setIdCliente(this.usuarioIdCliente); //
			fLIPA.getHeaderIn().setIdUsuario(this.usuarioIdUsuario);// oSB.append("10198");
			fLIPA.getHeaderIn().setUsuario(this.usuarioUsuario);//
			fLIPA.getHeaderIn().setClave(this.usuarioClave);//
			fLIPA.getHeaderIn().setIdConfiguracionComercio(this.idConfiguracionComercio);
			fLIPA.getHeaderIn().setOrigen(this.usuarioOrigen);

			fLIPA.setParametroServicio(this.pagoElectronico.getParametrosServicio());
			fLIPA.setDataIn(new DataInputFcnLIPA());
			fLIPA.getDataIn().setIdMayorista(this.usuarioIdMayorista); // mayorista
			if (this.tipoFiltroCliente == 1) {
				fLIPA.getDataIn().setIdCliente(idClienteFiltro); // cliente
				//fLIPA.getDataIn().setIdCliente(this.usuarioIdCliente); // cliente
				//fLIPA.getDataIn().setIdUsuario(this.usuarioIdUsuario); // usuario
			}
			fLIPA.getDataIn().setFechaDesde(this.fechaDesde); // Fecha desde
			fLIPA.getDataIn().setFechaHasta(this.fechaHasta); // Fecha hasta
			fLIPA.getDataIn().setEstadoOperador(this.idEstadoOperador);
			fLIPA.getDataIn().setPaymentId(this.idPagoOperador);
			fLIPA.getDataIn().setIdPagoACG(this.idPagoACG);
			
			if(idUsuarioPermReducido != null)
				fLIPA.getDataIn().setIdUsuario(idUsuarioPermReducido);

			if (this.tipoFiltroCliente == 1) {
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
			fLIPA.getDataIn().setIdCfgSite(idCfgSite);
			fLIPA.getDataIn().setIdBanco(this.idBanco);
			fLIPA.getDataIn().setIdMedioDePago(this.idMedioDePago);
			fLIPA.getDataIn().setPageSize(pageSize); // page size
			fLIPA.getDataIn().setPage((first / pageSize) + 1); // page
			
			for (SortMeta sm : sortBy.values()) {
				
				fLIPA.getDataIn().setCampoOrden(sortFieldSwitch(sm.getField())); // campo orden
				
	            switch (sm.getOrder()) {
	                case ASCENDING:
	                    fLIPA.getDataIn().setTipoOrden("ASC");
	                    break;
	                case DESCENDING:
	                    fLIPA.getDataIn().setTipoOrden("DESC");
	                    break;
	                case UNSORTED:
	                	fLIPA.getDataIn().setTipoOrden("ASC");
	                    break;
	                default:
	                	fLIPA.getDataIn().setTipoOrden("ASC");
	            }
	            break;
	        }
			
			fLIPA.getDataIn().setInformeHistorico(infHistorico);

			mo = fLIPA.ejecutar();

			if (mo != null) {

				if (mo.getDataOutputFcn() != null && !"M0000".equals(mo.getDataOutputFcn().getCodigoRetorno())) {
					PrimeFaces.current().dialog().showMessageDynamic(
							new FacesMessage("Atencion", mo.getDataOutputFcn().getMensajeRetorno()));
					return null;
				}else if (mo.getHeaderOut() != null && !"M0000".equals(mo.getHeaderOut().getCodigoRetorno())) {
					PrimeFaces.current().dialog().showMessageDynamic(
							new FacesMessage("Atencion", mo.getHeaderOut().getMensajeRetorno()));
					return null;
				}else {
					DataOutputFcnLIPA doLIPA = (DataOutputFcnLIPA) mo.getDataOutputFcn();
	
					if (doLIPA != null) {
						this.setRowCount(doLIPA.getListPagos().size());
						if (doLIPA.getListPagos() != null) {
							for (Pago p : doLIPA.getListPagos()) {
								Pago oPago = new Pago();
								BeanUtils.copyProperties(p, oPago);
								datasource.add(oPago);
							}
						}
	
					}
				}
			} else {
				PrimeFaces.current().dialog().showMessageDynamic(
						new FacesMessage("Atencion", "No se obtuvo una respuesta valida desde el Gateway"));
				return null;
			}
			return datasource;
		} catch (Exception ex) {
			LogACGHelper.escribirLog(null, "Consulta Listado de Pago. Error:|" + ex.getMessage() + "|");
			PrimeFaces.current().dialog().showMessageDynamic(
					new FacesMessage("Listado de Pago", "No se pudo realizar la consulta reintente nuevamente."));
			return null;
		}

	}

	private String sortFieldSwitch(String sortField) {

		String field = "idPago";

		switch (sortField.toLowerCase()) {
		case "id":
			field = "idPago";
			break;
		case "idterminal":
			field = "idTerminalPago";
			break;
		case "fechadeservidor.togregoriancalendar()":
			field = "fechaServidor";
			break;
		case "fechadepago.togregoriancalendar()":
			field = "fechaDePago";
			break;
		case "clientepagoelectronico.id":
			field = "idCliente";
			break;
		case "clientePagoElectronico.idDistribuidor1":
			field = "idDistribuidor1";
			break;			
		case "clientePagoElectronico.idDistribuidor2":
			field = "idDistribuidor2";
			break;			
		case "clientePagoElectronico.idDistribuidor3":
			field = "idDistribuidor3";
			break;			
		case "clientePagoElectronico.idDistribuidor4":
			field = "idDistribuidor4";
			break;			
		case "clientePagoElectronico.idDistribuidor5":
			field = "idDistribuidor5";
			break;			
		case "usuariopagoelectronico.id":
			field = "idUsuario";
			break;
		case "usuariopagoelectronico.usuario":
			field = "idUsuario";
			break;			
		case "conceptodepago.descripcion":
			field = "idConceptoPago";
			break;
		case "descripcionadicionalpago":
			field = "descripcionAdicionalPago";
			break;			
		case "merchantid":
			field = "merchantId";
			break;
		case "nombrecomercio":
			field = "nombreComercio";
			break;
		case "numeroticketacg":
			field = "numeroTicketACG";
			break;
		case "numeroticketoperador":
			field = "numeroTicketOperador";
			break;
		case "idbanco":
			field = "idBanco";
			break;
		case "idbancobcra":
			field = "idBanco";
			break;			
		case "idpagooperador":
			field = "idPagoOperador";
			break;
		case "metododepago.marca":
			field = "marca";
			break;
		case "metododepago.tipomediodepago":
			field = "mp_tipoMedioDePago";
			break;			
		case "moneda.id":
			field = "idMoneda";
			break;
		case "importe":
			field = "importeBase";
			break;

		case "porcentajeinteres":
			field = "porcInteres";
			break;

		case "importecalculado":
			field = "importeCalculado";
			break;

		case "cantidadcuotas":
			field = "cantidadCuotas";
			break;
		case "tokenoperador.numerotarjeta":
			field = "idTokenOperador";
			break;
		case "nombretitulartarjeta":
			field = "titular";
			break;
		case "tipodocumento":
			field = "tipoDocumentoCliente";
			break;				
		case "nrodocumento":
			field = "numeroDocumentoCliente";
			break;
		case "telefonocliente":
			field = "telefonoCliente";
			break;			
		case "emailcliente":
			field = "emailCliente";
			break;						
		case "estadooperador":
			field = "estadoOperador";
			break;				
		case "estado.descripcion":
			field = "estadoOperador";
			break;				
		case "conanulacion":
			field = "flgConAnulacion";
			break;
		case "tipocarga":
			field = "tipoDeCargaTarjeta";
			break;
		case "idoperadoranulacion":
			field = "idAnulacionOperadorAnu";
			break;
		case "numeroticketoperadoranulacion":
			field = "numeroTicketOperadorAnu";
			break;
		case "numeroticketacganulacion":
			field = "numeroTicketACGAnu";
			break;
		case "idCfgSite":
			field = "idCfgSite";
			break;
		case "fechaacreditacioncuenta.togregoriancalendar()":
			field = "fechaAcreditacionCta";
			break;
		case "acreditadoencuenta":
			field = "flgMovCtaAcreditado";
			break;
		case "idmovimientodecuenta":
			field = "idMovimientoDeCuenta";
			break;

		default:
			LogACGHelper.escribirLog(null, "Consulta Listado de Pago. sortField:|"+ sortField + "| analizar como enviarlo a la consulta LIPA");
			field = "idPago";
			break;
		}

		return field;
	}

	public Float getImporteTotalAbonado() {
		return importeTotalAbonado;
	}

	public void setImporteTotalAbonado(Float importeTotalAbonado) {
		this.importeTotalAbonado = importeTotalAbonado;
	}

}