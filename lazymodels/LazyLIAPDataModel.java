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
import com.americacg.cargavirtual.gateway.pagoElectronico.funciones.FuncionLIAP;
import com.americacg.cargavirtual.gateway.pagoElectronico.model.entidad.Anulacion;
import com.americacg.cargavirtual.gateway.pagoElectronico.model.parametros.funciones.input.DataInputFcnLIAP;
import com.americacg.cargavirtual.gateway.pagoElectronico.model.parametros.funciones.output.DataOutputFcnLIAP;
import com.americacg.cargavirtual.web.helpers.LogACGHelper;

public class LazyLIAPDataModel extends LazyDataModel<Anulacion> {
	/**
	 * 
	 */
	private static final long serialVersionUID = 3780015991880104269L;
	private List<Anulacion> datasource;
	private PagoElectronico pagoElectronico = null;
	private Float importeTotalAbonado = 0F;
	
	private String idConfiguracionComercio;
	private Long idDePago; // idPago
	private Long usuarioIdMayorista; // mayorista
	private Long usuarioIdCliente; // cliente
	private Long usuarioIdUsuario; // usuario
	private String usuarioUsuario; 
	private String usuarioClave; 
	private Date fechaDesde; // Fecha desde
	private Date fechaHasta; // Fecha hasta
	

	public LazyLIAPDataModel(PagoElectronico pagoElectronico, String idConfiguracionComercio, Long idDePago, Long usuarioIdMayorista, Long usuarioIdCliente, Long usuarioIdUsuario, String usuarioUsuario, String usuarioClave, Date fechaDesde, Date fechaHasta) {
		if (this.pagoElectronico == null)
			this.pagoElectronico = pagoElectronico;

		this.idConfiguracionComercio = idConfiguracionComercio;
		this.idDePago = idDePago;
		this.usuarioIdMayorista = usuarioIdMayorista;
		this.usuarioIdCliente = usuarioIdCliente;
		this.usuarioIdUsuario = usuarioIdUsuario;
		this.usuarioUsuario = usuarioUsuario;
		this.usuarioClave = usuarioClave;
		this.fechaDesde = fechaDesde;
		this.fechaHasta =fechaHasta;
		
	}

	@Override
	public Anulacion getRowData(String rowKey) {
		// REVISAR
		if (datasource != null) {
			for (Anulacion anulacion : datasource) {
				if (anulacion.getId().toString().equals(rowKey))
					return anulacion;
			}
		}
		return null;
	}

	@Override
	public String getRowKey(Anulacion anulacion) {
		return anulacion.getId().toString();
	}

	@Override
	public List<Anulacion> load(int first, int pageSize, Map<String, SortMeta> sortBy, Map<String, FilterMeta> filterBy) {
		datasource = new ArrayList<Anulacion>();
		boolean propNroEmpty = false;
		boolean propNro = false;
		Anulacion anulacion = new Anulacion();

		if (!filterBy.isEmpty()) {
			try {
				for (Map.Entry<String, FilterMeta> oProp : filterBy.entrySet()) {
					Class<?> xtype = PropertyUtils.getPropertyType(anulacion, oProp.getKey());
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
						PropertyUtils.setProperty(anulacion, oProp.getKey(), null);
					} else
						PropertyUtils.setProperty(anulacion, oProp.getKey(), oVal);
				}

			} catch (Exception e) {
				LogACGHelper.escribirLog(null,
						"Se produjo un error LazyLIAPDataModel metodo load (ROL). Msg:|" + e.getMessage() + "|");
			}
		}

		try {

			com.americacg.cargavirtual.gateway.pagoElectronico.model.MensajeOutboundGateway mo = null;
			
			FuncionLIAP fLIAP = new FuncionLIAP(this.pagoElectronico.getParametros());
			
			fLIAP.getHeaderIn().setIdMayorista(this.usuarioIdMayorista);//
			fLIAP.getHeaderIn().setIdCliente(this.usuarioIdCliente); //
			fLIAP.getHeaderIn().setIdUsuario(this.usuarioIdCliente);// oSB.append("10198");
			fLIAP.getHeaderIn().setUsuario(this.usuarioUsuario );//
			fLIAP.getHeaderIn().setClave(this.usuarioClave);//
			fLIAP.getHeaderIn().setIdConfiguracionComercio(this.idConfiguracionComercio);

			
			fLIAP.setParametroServicio(this.pagoElectronico.getParametrosServicio());
			fLIAP.setDataIn(new DataInputFcnLIAP());
			fLIAP.getDataIn().setIdPagoACG(this.idDePago); // idPago
			fLIAP.getDataIn().setIdMayorista(this.usuarioIdMayorista); // mayorista
			fLIAP.getDataIn().setIdCliente(this.usuarioIdCliente); // cliente
			fLIAP.getDataIn().setIdUsuario(this.usuarioIdUsuario); // usuario
			fLIAP.getDataIn().setFechaDesde(this.fechaDesde); // Fecha desde
			fLIAP.getDataIn().setFechaHasta(this.fechaHasta); // Fecha hasta
			fLIAP.getDataIn().setPageSize(pageSize); // page size
			fLIAP.getDataIn().setPage((first / pageSize) + 1); // page
			for (SortMeta sm : sortBy.values()) {
				
				fLIAP.getDataIn().setCampoOrden(sortFieldSwitch(sm.getField())); // campo orden
				
	            switch (sm.getOrder()) {
	                case ASCENDING:
	                	fLIAP.getDataIn().setTipoOrden("ASC");
	                    break;
	                case DESCENDING:
	                	fLIAP.getDataIn().setTipoOrden("DESC");
	                    break;
	                case UNSORTED:
	                	fLIAP.getDataIn().setTipoOrden("ASC");
	                    break;
	                default:
	                	fLIAP.getDataIn().setTipoOrden("ASC");
	            }
	            break;
	        }

			mo = fLIAP.ejecutar();

			if (mo != null) {
				
				if(!"M0000".equals(mo.getDataOutputFcn().getCodigoRetorno())) {
					PrimeFaces.current().dialog().showMessageDynamic(new FacesMessage("Atencion", mo.getDataOutputFcn().getMensajeRetorno()));
					throw new RuntimeException(mo.getDataOutputFcn().getMensajeRetorno());
				}
				
				DataOutputFcnLIAP doLIAP = (DataOutputFcnLIAP) mo.getDataOutputFcn();
	
				if (doLIAP != null) {
					
					
					
					if (doLIAP.getListAnulacion() != null) {
						this.setRowCount(doLIAP.getListAnulacion().size());
						for (Anulacion anul : doLIAP.getListAnulacion()) {
							Anulacion oAnulacion = new Anulacion();
							BeanUtils.copyProperties(anul, oAnulacion);
							//f.setIdLote(lote.getId());
							datasource.add(oAnulacion);
						}
					}
					
				}
			} else {
				PrimeFaces.current().dialog().showMessageDynamic(new FacesMessage("Atencion", "No se obtuvo una respuesta valida desde el Gateway"));
				throw new RuntimeException("No se obtuvo una respuesta valida desde el Gateway");
			}
			return datasource;
		} catch (Exception ex) {
			PrimeFaces.current().dialog().showMessageDynamic(new FacesMessage("Atencion", ex.getMessage()));
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
			field = "idPago";
			break;
		case "fechaDeServidor.toGregorianCalendar()":
			field = "fechaDeServidor";
			break;
		case "fechadepago":
			field = "fechaDePago";
			break;
		case "clientepagoelectronico.getid":
			field = "idCliente";
			break;
		// case "ClientePagoElectronico().getRazonSocial":
		// case "ClientePagoElectronico.getiddistribuidor1":
		// case "ClientePagoElectronico().getRazonSocialDist1":
		// case "ClientePagoElectronico().getIdDistribuidor2":
		// case "ClientePagoElectronico().getRazonSocialDist2":
		// case "ClientePagoElectronico().getIdDistribuidor3":
		// case "ClientePagoElectronico().getRazonSocialDist3":
		// case "ClientePagoElectronico().getIdDistribuidor4":
		// case "ClientePagoElectronico().getRazonSocialDist4":
		// case "ClientePagoElectronico().getIdDistribuidor5":
		// case "ClientePagoElectronico().getRazonSocialDist5":
		case "usuariopagoelectronico.getid":
			field = "idUsuario";
			break;
		// case "UsuarioPagoElectronico().getUsuario":
		case "conceptodepago.descripcion":
			field = "idConceptoPago";
			break;
		// case "DescripcionAdicionalPago":
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
		// case "BancoDenominacion":
		// case "pago.getIdBancoBCRA":
		case "idpagooperador":
			field = "idPagoOperador";
			break;

		// case "MetodoDePago().getMarca":
		// case "MetodoDePago().getTipoMedioDePago":
		// case "pago.getIdBancoBCRA":
		case "moneda.getid":
			field = "idMoneda";
			break;
		case "importebase":
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
		case "TokenOperador().getNumeroTarjeta":
			field = "idPago";
			break;
		case "estadooperador":
			field = "idEstado";
			break;
		// case "Estado().getDescripcion":
		case "isConAnulacion":
			field = "flgConAnulacion";
			break;
		default:
			LogACGHelper.escribirLog(null, "Consulta Listado de Anulacion de Pagos. sortField:|"+ sortField + "| analizar como enviarlo a la consulta LIAP");
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