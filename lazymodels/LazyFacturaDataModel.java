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
import com.americacg.cargavirtual.pnet.gateway.ICargaVirtualGatewayPNet;
import com.americacg.cargavirtual.web.helpers.LogACGHelper;
import com.americacg.cargavirtual.web.model.Factura;

public class LazyFacturaDataModel extends LazyDataModel<Factura> {
	/**
	 * 
	 */
	private static final long serialVersionUID = 3780015991880104269L;
	private List<Factura> datasource;
	private ICargaVirtualGatewayPNet gsp;
	private com.americacg.cargavirtual.pnet.message.solicitud.ReporteACG stRACG;
	private Float importeTotalAbonado = 0F;

	public LazyFacturaDataModel(ICargaVirtualGatewayPNet gsp,
			com.americacg.cargavirtual.pnet.message.solicitud.ReporteACG stRACG) {
		if (this.gsp == null)
			this.gsp = gsp;

		if (this.stRACG == null)
			this.stRACG = stRACG;
	}

	public ICargaVirtualGatewayPNet getICargaVirtualGatewayPNet() {
		return gsp;
	}

	public void setICargaVirtualGatewayPNet(ICargaVirtualGatewayPNet gsp) {
		this.gsp = gsp;
	}

	@Override
	public Factura getRowData(String rowKey) {
		// REVISAR
		if (datasource != null) {
			for (Factura factura : datasource) {
				if (factura.getId().toString().equals(rowKey))
					return factura;
			}
		}
		return null;
	}

	@Override
	public String getRowKey(Factura factura) {
		// REVISAR
		return factura.getId().toString();
	}

	@Override
	public List<Factura> load(int first, int pageSize, Map<String, SortMeta> sortBy, Map<String, FilterMeta> filterBy){
		datasource = new ArrayList<Factura>();
		boolean propNroEmpty = false;
		boolean propNro = false;
		Factura factura = new Factura();

		if (!filterBy.isEmpty()) {
			try {
				for (Map.Entry<String, FilterMeta> oProp : filterBy.entrySet()) {
					Class<?> xtype = PropertyUtils.getPropertyType(factura, oProp.getKey());
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
						PropertyUtils.setProperty(factura, oProp.getKey(), null);
					} else
						PropertyUtils.setProperty(factura, oProp.getKey(), oVal);
				}

			} catch (Exception e) {
				LogACGHelper.escribirLog(null,
						"Se produjo un error LazyFacturaDataModel metodo load (ROL). Msg:|" + e.getMessage() + "|");
			}
		}

		try {

			stRACG.getFiltroReporte().setPagina((first / pageSize) + 1);
			stRACG.getFiltroReporte().setCantRegXPagina(pageSize);
			
			for (SortMeta sm : sortBy.values()) {
				
				stRACG.getFiltroReporte().setCampoOrden(sortFieldSwitch(sm.getField())); // campo orden
				
	            switch (sm.getOrder()) {
	                case ASCENDING:
	                	stRACG.getFiltroReporte().setTipoOrden("ASC");
	                    break;
	                case DESCENDING:
	                	stRACG.getFiltroReporte().setTipoOrden("DESC");
	                    break;
	                case UNSORTED:
	                	stRACG.getFiltroReporte().setTipoOrden("ASC");
	                    break;
	                default:
	                	stRACG.getFiltroReporte().setTipoOrden("ASC");
	            }
	            break;
	        }

			com.americacg.cargavirtual.pnet.message.respuesta.ReporteACG rtRACG = gsp.reporteACG(stRACG);

			if (rtRACG == null) {
				throw new RuntimeException("La consulta de Reporte ACG devolvio null");
			} else if (!"P0000".equals(rtRACG.getErrorRb().getCodigo())) {
				throw new RuntimeException(rtRACG.getErrorRb().getMensaje());
			} else if ("P0000".equals(rtRACG.getErrorRb().getCodigo())) {
				if (rtRACG.getListaLotes() != null) {
					if ((rtRACG.getListaLotes().getLote() != null) && (!rtRACG.getListaLotes().getLote().isEmpty())) {

						this.setRowCount(rtRACG.getCantidadRegistros());
						this.importeTotalAbonado = rtRACG.getImporteTotalAbonado();

						for (com.americacg.cargavirtual.pnet.model.Lote lote : rtRACG.getListaLotes().getLote()) {
							for (com.americacg.cargavirtual.pnet.model.Factura fac : lote.getListaFacturas()
									.getFactura()) {
								Factura f = new Factura();
								BeanUtils.copyProperties(fac, f);
								f.setIdLote(lote.getId());
								datasource.add(f);
							}
						}

					} else {
						throw new RuntimeException("No existe información para la consulta realizada.");
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
		case "idtrxacg":
			field = "FAC_IDTRX";
			break;
		case "fechaalta":
			field = "FAC_FECALTA";
			break;
		case "idcliente":
			field = "FAC_CLIID";
			break;
		case "descripcion":
			field = "FAC_DESC";
			break;
		case "resultado":
			field = "FAC_RESULT";
			break;
		case "numerounicosiris":
			field = "FAC_NROUNICOSIRIS";
			break;
		case "identificadorservicio":
			field = "FAC_IDSERVICIO";
			break;
		case "identificador1":
			field = "FAC_IDEN1";
			break;
		case "identificador2":
			field = "FAC_IDEN2";
			break;
		case "identificador3":
			field = "FAC_IDEN3";
			break;
		case "codbarra1":
			field = "FAC_CODBARRA1";
			break;
		case "codbarra2":
			field = "FAC_CODBARRA2";
			break;
		case "impabonado":
			field = "FAC_IMPABONADO";
			break;
		case "ente":
			field = "FAC_ENTE";
			break;
		case "subente":
			field = "FAC_SUBENTE";
			break;
		case "transaccionsiris":
			field = "FAC_IDSIRIS";
			break;
		case "numerotarjeta":
			field = "FAC_NROTARJETA";
			break;
		case "nrocupontarjeta":
			field = "FAC_NROCUPON";
			break;
		case "transaccionprisma":
			field = "FAC_TRXPRISMA";
			break;
		case "estado.id":
			field = "FAC_ESTID";
			break;
		default:
			// System.out.println("sortField:|" + sortField + "|");
			LogACGHelper.escribirLog(null, "Consulta LazyFacturaDataModel. sortField:|"+ sortField + "| analizar como enviarlo a la consulta.");						
			field = "FAC_IDTRX";
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