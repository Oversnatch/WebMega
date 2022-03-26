package com.americacg.cargavirtual.web.lazymodels;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.apache.commons.beanutils.PropertyUtils;
import org.primefaces.model.FilterMeta;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortMeta;
import org.springframework.beans.BeanUtils;

import com.americacg.cargavirtual.gcsi.entidad.Factura;
import com.americacg.cargavirtual.gcsi.entidad.FacturaGIRE;
import com.americacg.cargavirtual.gcsi.entidad.Lote;
import com.americacg.cargavirtual.gcsi.model.acg.miscelaneo.OrdenBBDD;
import com.americacg.cargavirtual.gcsi.model.acg.request.ConsultarOperacionesRequest;
import com.americacg.cargavirtual.gcsi.model.acg.request.FiltroListadoOperacionesRequest;
import com.americacg.cargavirtual.gcsi.model.acg.response.ConsultarOperacionesResponse;
import com.americacg.cargavirtual.gcsi.model.acg.response.ObtenerEstadosResponse;
import com.americacg.cargavirtual.gcsi.service.CodigoRetorno;
import com.americacg.cargavirtual.web.enums.CfgTimeout;
import com.americacg.cargavirtual.web.helpers.GCSIServiceHelper;
import com.americacg.cargavirtual.web.helpers.LogACGHelper;
import com.americacg.cargavirtual.web.model.FacturaGCSIWeb;

public class LazyOperacionGCSIDataModel extends LazyDataModel<FacturaGCSIWeb> {
	/**
	 * 
	 */
	private static final long serialVersionUID = 4888158342714901912L;
	private List<FacturaGCSIWeb> datasource;
	private ConsultarOperacionesRequest oConsOpersReq = null;
	private Float importeTotalAbonado = 0F;

	public LazyOperacionGCSIDataModel(ConsultarOperacionesRequest consultarOperacionesRequest) {
		this.oConsOpersReq = consultarOperacionesRequest;
	}

	@Override
	public FacturaGCSIWeb getRowData(String rowKey) {
		// REVISAR
		if (datasource != null) {
			for (FacturaGCSIWeb factura : datasource) {
				if (factura.getFactura().getId().toString().equals(rowKey))
					return factura;
			}
		}
		return null;
	}

	@Override
	public String getRowKey(FacturaGCSIWeb factura) {
		// REVISAR
		return factura.getFactura().getId().toString();
	}

	@Override
	public List<FacturaGCSIWeb> load(int first, int pageSize, Map<String, SortMeta> sortBy, Map<String, FilterMeta> filterBy){
		datasource = new ArrayList<FacturaGCSIWeb>();
		boolean propNroEmpty = false;
		boolean propNro = false;
		Lote oLote = null;
		ConsultarOperacionesResponse oResp = null;
		OrdenBBDD oOrden = null;
		//------------------------------------
		//Tener en cuenta las otras operadoras
		//------------------------------------
		/*if("GIRE".equals(this.oConsOpersReq.getFiltro().getOperador().getCodMnemonico())) {
			oFactura = new FacturaGIRE();
		}*/
		oLote = this.oConsOpersReq.getFiltro().getLote();

		if (!filterBy.isEmpty()) {
			try {
				for (Map.Entry<String, FilterMeta> oProp : filterBy.entrySet()) {
					Class<?> xtype = PropertyUtils.getPropertyType(oLote, oProp.getKey());
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
						PropertyUtils.setProperty(oLote, oProp.getKey(), null);
					} else
						PropertyUtils.setProperty(oLote, oProp.getKey(), oVal);
				}

			} catch (Exception e) {
				LogACGHelper.escribirLog(null,
						"Se produjo un error LazyOperacionGCSIDataModel metodo load (ROL). Msg:|" + e.getMessage() + "|");
			}
		}

		try {

			//stRACG.getFiltroReporte().setPagina((first / pageSize) + 1);
			//stRACG.getFiltroReporte().setCantRegXPagina(pageSize);
			oConsOpersReq.getFiltro().getOrdenBBDD().clear();
			
			for (SortMeta sm : sortBy.values()) {
				//oFltOpers.get.getFiltroReporte().setCampoOrden(sortFieldSwitch(sm.getField())); // campo orden
				//oConsOpersReq.getFiltro().setCampoOrden(sm.getField());
	            
				oOrden = new OrdenBBDD();
				
				switch (sm.getOrder()) {
	                case ASCENDING:
	                	//oConsOpersReq.getFiltro().setTipoOrden("ASC");
	                	oOrden.setTipoOrden("ASC");
	                    break;
	                case DESCENDING:
	                	//oConsOpersReq.getFiltro().setTipoOrden("DESC");
	                	oOrden.setTipoOrden("DESC");
	                    break;
	                case UNSORTED:
	                	//oConsOpersReq.getFiltro().setTipoOrden("ASC");
	                	oOrden.setTipoOrden("ASC");
	                    break;
	                default:
	                	oOrden.setTipoOrden("ASC");
	                	//oConsOpersReq.getFiltro().setTipoOrden("ASC");
	            }
	            
				oOrden.setCampoOrden(sortFieldSwitch(sm.getField()));
				
				oConsOpersReq.getFiltro().getOrdenBBDD().add(oOrden);
	            break;
	        }

			oConsOpersReq.getFiltro().setPageSize(pageSize);
			oConsOpersReq.getFiltro().setOffset(first);
			
			//com.americacg.cargavirtual.pnet.message.respuesta.ReporteACG rtRACG = gsp.reporteACG(stRACG);
			oResp = (ConsultarOperacionesResponse) GCSIServiceHelper.getGatewayGCSI(CfgTimeout.DEFAULT).consultarOperaciones(oConsOpersReq);
			
			if (oResp == null) {
				throw new RuntimeException("La consulta de Reporte ACG devolvio null");
			} else if (!CodigoRetorno.M_0000.equals(oResp.getCodigo())) {
				throw new RuntimeException(oResp.getMensaje());
			} else if (CodigoRetorno.M_0000.equals(oResp.getCodigo())) {
				if (oResp.getLotes() != null && oResp.getLotes().size() > 0) {
						this.setRowCount(((Long)oResp.getTotalRegistros()).intValue());
						//this.importeTotalAbonado = rtRACG.getImporteTotalAbonado();
						//this.datasource = oResp.getFacturas();
						for (Lote oLoteAux : oResp.getLotes()) {
							for (Factura oFacAux : oLoteAux.getFacturas().getFacturaGIREOrFacturaPNETOrFacturaWU()) {
								FacturaGCSIWeb oF = new FacturaGCSIWeb();
								oF.setFactura(oFacAux);
								//BeanUtils.copyProperties(oFacAux, oF);
								oF.setIdLote(oLoteAux.getId());
								datasource.add(oF);
							}
						}
				} else {
					throw new RuntimeException("No existe información para la consulta realizada.");
				}
			}

			return datasource;
		} catch (Exception ex) {
			// GRABAR EXCEPCION AL LOG !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
			// GRABAR EXCEPCION AL LOG !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
			// GRABAR EXCEPCION AL LOG !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
			// GRABAR EXCEPCION AL LOG !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
			return null;
		}

	}

	private String sortFieldSwitch(String sortField) {

		String field = "FAC_IDTRX";

		switch (sortField.toLowerCase()) {
		case "idlote":
			field = "id";
			break;
		default:
			field = sortField;
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