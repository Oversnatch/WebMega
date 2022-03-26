package com.americacg.cargavirtual.web.lazymodels;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.apache.commons.beanutils.PropertyUtils;
import org.primefaces.model.FilterMeta;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortMeta;

import com.americacg.cargavirtual.gestion.model.Cliente;
import com.americacg.cargavirtual.gestion.model.FiltroListadoPlatAgendaDestinatarioCta;
import com.americacg.cargavirtual.gestion.model.ObjectFactory;
import com.americacg.cargavirtual.gestion.model.PlatAgendaDestinatarioCta;
import com.americacg.cargavirtual.gestion.model.PlatAgendaDestinatarioCtaList;
import com.americacg.cargavirtual.gestion.model.PlatEntidadFinanciera;
import com.americacg.cargavirtual.gestion.model.PlatTipoIdentifCta;
import com.americacg.cargavirtual.web.enums.CfgTimeout;
import com.americacg.cargavirtual.web.helpers.GestionServiceHelper;
import com.americacg.cargavirtual.web.helpers.LogACGHelper;
import com.americacg.cargavirtual.web.model.superPago.ItemAgendaDestinatario;

public class LazyAgendaDestinatarioDataModel extends LazyDataModel<ItemAgendaDestinatario> {
	/**
	 * 
	 */
	private static final long serialVersionUID = -6331320010237912412L;
	private List<ItemAgendaDestinatario> datasource;
	private Long idMayorista;
	private Long idCliente;
	private String campoOrdenamiento = "oFlt";
	private String tipoOrdenamiento = "asc";
	private String filtroCampo = null;
	private String filtroValor = null;
	private ObjectFactory oGestionObjFac = new ObjectFactory();
	
	public LazyAgendaDestinatarioDataModel(Long idCliente, Long idMayorista, String campoOrdenamiento, String tipoOrdenamiento) {
		this.idMayorista = idMayorista;
		this.idCliente = idCliente;
		this.campoOrdenamiento = campoOrdenamiento;
		this.tipoOrdenamiento = tipoOrdenamiento;
	}

	@Override
	public ItemAgendaDestinatario getRowData(String rowKey) {
		if (datasource != null) {
			for (ItemAgendaDestinatario itmAgDest : datasource) {
				if (itmAgDest.getIdAgendaDestinatarioCta().toString().equals(rowKey))
					return itmAgDest;
			}
		}

		return null;
	}

	@Override
	public String getRowKey(ItemAgendaDestinatario itmAgDest) {
		return itmAgDest.getIdAgendaDestinatarioCta().toString();
	}

	@Override
	public List<ItemAgendaDestinatario> getWrappedData() {
		// TODO Auto-generated method stub
		return super.getWrappedData();
	}

	@Override
	public List<ItemAgendaDestinatario> load(int first, int pageSize, Map<String, SortMeta> sortBy,
			Map<String, FilterMeta> filterBy) {
		FiltroListadoPlatAgendaDestinatarioCta oFlt = new FiltroListadoPlatAgendaDestinatarioCta();
		PlatAgendaDestinatarioCta oCtaFlt = new PlatAgendaDestinatarioCta();
		Cliente oCli = null;
		datasource = new ArrayList<ItemAgendaDestinatario>();

		try {
			if (!filterBy.isEmpty()) {
				oCtaFlt = this.asignarFiltros(filterBy);
			}

			
			for (SortMeta sm : sortBy.values()) {
				
				this.campoOrdenamiento = sortFieldSwitch(sm.getField()); // campo orden
				
	            switch (sm.getOrder()) {
	                case ASCENDING:
	                	this.tipoOrdenamiento = "ASC";
	                    break;
	                case DESCENDING:
	                	this.tipoOrdenamiento = "DESC";
	                    break;
	                case UNSORTED:
	                	this.tipoOrdenamiento = "ASC";
	                    break;
	                default:
	                	this.tipoOrdenamiento = "ASC";
	            }
	           
	            break;
	        }
			
            //LogACGHelper.escribirLog(null, "Consulta LazyItemAgendaDestinatarioDataModel. sortField:|"+ campoOrdenamiento + "| sortOrder:|" + tipoOrdenamiento + ".");
            
			oCtaFlt.setIdMayorista(oGestionObjFac.createPlatAgendaDestinatarioCtaIdMayorista(idMayorista));
			oCli = new Cliente();
			oCli.setIdCliente(oGestionObjFac.createClienteIdCliente(idCliente));
			oCtaFlt.setCliente(oGestionObjFac.createPlatAgendaDestinatarioCtaCliente(oCli));

			oFlt.setAgendaDestinatario(oGestionObjFac.createFiltroListadoPlatAgendaDestinatarioCtaAgendaDestinatario(oCtaFlt));
			oFlt.setPage(oGestionObjFac.createFiltroListadoPlatAgendaDestinatarioCtaPage((first / pageSize) + 1));
			oFlt.setPageSize(oGestionObjFac.createFiltroListadoPlatAgendaDestinatarioCtaPageSize(pageSize));
			oFlt.setCampoOrden(oGestionObjFac.createFiltroListadoPlatAgendaDestinatarioCtaCampoOrden(campoOrdenamiento));
			oFlt.setTipoOrden(oGestionObjFac.createFiltroListadoPlatAgendaDestinatarioCtaTipoOrden(tipoOrdenamiento));
			
			PlatAgendaDestinatarioCtaList oLstAgDes = GestionServiceHelper.getGestionService(CfgTimeout.DEFAULT).listarPlatAgendaDestinatarioCta(oFlt); 
			
			if (oLstAgDes != null && oLstAgDes.getRegistros() != null && oLstAgDes.getRegistros().getValue() != null
					&& oLstAgDes.getRegistros().getValue().getPlatAgendaDestinatarioCta() != null) {
				for (PlatAgendaDestinatarioCta agDes : oLstAgDes.getRegistros().getValue().getPlatAgendaDestinatarioCta()) {
					ItemAgendaDestinatario oItmAgeDes = new ItemAgendaDestinatario();
					oItmAgeDes.setIdAgendaDestinatarioCta(agDes.getIdAgendaDestinatarioCta().getValue());
					oItmAgeDes.setIdMayorista(agDes.getIdMayorista().getValue());
					oItmAgeDes.setIdUsuario(agDes.getIdUsuario().getValue());
					oItmAgeDes.setFechaAlta((agDes.getFechaAlta() != null ? agDes.getFechaAlta().toGregorianCalendar().getTime() : null));
					oItmAgeDes.setReferencia(agDes.getReferencia().getValue());
					oItmAgeDes.setTitular(agDes.getTitular().getValue());
					oItmAgeDes.setIdEntidadFinanciera((agDes.getEntidadFinanciera() != null && agDes.getEntidadFinanciera().getValue().getId() != null ? agDes.getEntidadFinanciera().getValue().getId().getValue() : null));
					oItmAgeDes.setDescEntidadFinanciera((agDes.getEntidadFinanciera() != null && agDes.getEntidadFinanciera().getValue().getDenominacion() != null ? agDes.getEntidadFinanciera().getValue().getDenominacion().getValue() : null));
					oItmAgeDes.setEsCtaPropia(agDes.getEsCtaPropia().getValue());
					oItmAgeDes.setIdTipoIdentifCta((agDes.getTipoIdentifCta() != null && agDes.getTipoIdentifCta().getValue().getIdTipoIdentifCta() != null ? agDes.getTipoIdentifCta().getValue().getIdTipoIdentifCta().getValue() : null));
					oItmAgeDes.setDescTipoIdentifCta((agDes.getTipoIdentifCta() != null && agDes.getTipoIdentifCta().getValue().getDescripcion() != null ? agDes.getTipoIdentifCta().getValue().getDescripcion().getValue() : null));
					oItmAgeDes.setCodigoIdentifCta(agDes.getCodigoIdentifCta().getValue());
					oItmAgeDes.setEsCtaCte(agDes.getEsCtaCte().getValue());
					oItmAgeDes.setIdTipoIdenTrib((agDes.getTipoIdenTrib().getValue() != null ? agDes.getTipoIdenTrib().getValue().getIdTipoDocumento().getValue() : null));
					oItmAgeDes.setCodMnemonicoTipoIdenTrib((agDes.getTipoIdenTrib().getValue() != null ? agDes.getTipoIdenTrib().getValue().getCodMnemonico().getValue() : null));
					oItmAgeDes.setDescripcionTipoIdenTrib((agDes.getTipoIdenTrib().getValue() != null ? agDes.getTipoIdenTrib().getValue().getDescripcion().getValue() : null));
					oItmAgeDes.setNroIdenTrib(agDes.getNroIdenTrib().getValue());
					oItmAgeDes.setEmailNotifTransfer(agDes.getEmailNotifTransfer().getValue());
					oItmAgeDes.setIdEstado((agDes.getEstado() != null && agDes.getEstado().getValue().getIdEstadoAgendaDestCta() != null ? agDes.getEstado().getValue().getIdEstadoAgendaDestCta().getValue() : null));
					oItmAgeDes.setLimiteTransferencia(agDes.getLimiteTransferencia().getValue());
					oItmAgeDes.setDescEstado((agDes.getEstado() != null && agDes.getEstado().getValue().getDescripcion() != null ? agDes.getEstado().getValue().getDescripcion().getValue() : null));
					oItmAgeDes.setVistaPublica(agDes.getVistaPublica().getValue());
					
					datasource.add(oItmAgeDes);
				}

				this.setRowCount(oLstAgDes.getTotalRegistros().getValue());
			} else {
				this.setRowCount(0);
			}

			return datasource;
		} catch (Exception ex) {
			return null;
		}
	}

	private PlatAgendaDestinatarioCta asignarFiltros(Map<String, FilterMeta> filters) {
		boolean propNroEmpty = false;
		boolean propNro = false;
		boolean propBoolean = false;
		ItemAgendaDestinatario itmAgDesFlt = new ItemAgendaDestinatario();
		PlatAgendaDestinatarioCta oAgDes = new PlatAgendaDestinatarioCta();
		
		this.filtroCampo = null;
		this.filtroValor = null;

		if (!filters.isEmpty()) {
			try {
				for (Map.Entry<String, FilterMeta> oProp : filters.entrySet()) {
					Class<?> xtype = PropertyUtils.getPropertyType(itmAgDesFlt, oProp.getKey());
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
						} else if (Boolean.class.isAssignableFrom(xtype)) {
							propNro = false;
							propBoolean = true;
							oVal = Boolean.valueOf(value.toString()).booleanValue();
							propNroEmpty = false;
							
							oVal = value;
						} else {
							propNro = false;
							propBoolean = false;
							//oVal = value;
							oVal = String.valueOf(value).replaceAll("[^A-Za-z0-9]","");
						}
					}

					if (propNro && propNroEmpty) {
						PropertyUtils.setProperty(itmAgDesFlt, oProp.getKey(), null);
						this.filtroCampo = mapearCampoFiltro(oProp.getKey());
						this.filtroValor = null;
					} else if(propBoolean) {
						PropertyUtils.setProperty(itmAgDesFlt, oProp.getKey(), oVal);
						this.filtroCampo = mapearCampoFiltro(oProp.getKey());
						if (oVal != null && (Boolean)oVal == true) {
							this.filtroValor = "1";
						} else {
							this.filtroValor = null;
						}
					}
					else {
						PropertyUtils.setProperty(itmAgDesFlt, oProp.getKey(), oVal);
						this.filtroCampo = mapearCampoFiltro(oProp.getKey());
						if (oVal != null) {
							this.filtroValor = String.valueOf(oVal);
						} else {
							this.filtroValor = null;
						}
					}
					
					if(this.filtroValor != null && !this.filtroValor.isEmpty())
						break;
				}

				if(itmAgDesFlt.getTitular() != null)
					oAgDes.setTitular(oGestionObjFac.createPlatAgendaDestinatarioCtaTitular(itmAgDesFlt.getTitular()));
				if(itmAgDesFlt.getReferencia() != null)
					oAgDes.setReferencia(oGestionObjFac.createPlatAgendaDestinatarioCtaReferencia(itmAgDesFlt.getReferencia()));
				else if(itmAgDesFlt.getCodigoIdentifCta() != null)
					oAgDes.setCodigoIdentifCta(oGestionObjFac.createPlatAgendaDestinatarioCtaCodigoIdentifCta(itmAgDesFlt.getCodigoIdentifCta()));
				else if(itmAgDesFlt.getIdEntidadFinanciera() != null) {
					PlatEntidadFinanciera oEF = new PlatEntidadFinanciera();
					oEF.setId(oGestionObjFac.createPlatEntidadFinancieraId(itmAgDesFlt.getIdEntidadFinanciera()));
					oAgDes.setEntidadFinanciera(oGestionObjFac.createPlatAgendaDestinatarioCtaEntidadFinanciera(oEF));
				}
				else if(itmAgDesFlt.getDescEntidadFinanciera() != null) {
					PlatEntidadFinanciera oEF = new PlatEntidadFinanciera();
					oEF.setDenominacion(oGestionObjFac.createPlatEntidadFinancieraDenominacion(itmAgDesFlt.getDescEntidadFinanciera()));
					oAgDes.setEntidadFinanciera(oGestionObjFac.createPlatAgendaDestinatarioCtaEntidadFinanciera(oEF));
				}
				else if(itmAgDesFlt.getEmailNotifTransfer() != null) {
					oAgDes.setEmailNotifTransfer(oGestionObjFac.createPlatAgendaDestinatarioCtaEmailNotifTransfer(itmAgDesFlt.getEmailNotifTransfer()));
				}
				else if(itmAgDesFlt.getEsCtaPropia() != null) {
					oAgDes.setEsCtaPropia(oGestionObjFac.createPlatAgendaDestinatarioCtaEsCtaPropia(itmAgDesFlt.getEsCtaPropia()));
				}
				else if(itmAgDesFlt.getIdTipoIdentifCta() != null) {
					PlatTipoIdentifCta oTID = new PlatTipoIdentifCta();
					oTID.setIdTipoIdentifCta(oGestionObjFac.createPlatTipoIdentifCtaIdTipoIdentifCta(itmAgDesFlt.getIdTipoIdentifCta()));
					oAgDes.setTipoIdentifCta(oGestionObjFac.createPlatAgendaDestinatarioCtaTipoIdentifCta(oTID));
				}
				else if(itmAgDesFlt.getDescTipoIdentifCta() != null) {
					PlatTipoIdentifCta oTID = new PlatTipoIdentifCta();
					oTID.setDescripcion(oGestionObjFac.createPlatTipoIdentifCtaDescripcion(itmAgDesFlt.getDescTipoIdentifCta()));
					oAgDes.setTipoIdentifCta(oGestionObjFac.createPlatAgendaDestinatarioCtaTipoIdentifCta(oTID));
				}
			} catch (Exception e) {
				LogACGHelper.escribirLog(null,
						"Se produjo un error LazyItemAgendaDestinatarioDataModel metodo load (ROL). Msg:|" + e.getMessage() + "|");
			}
		}
		
		return oAgDes;
	}

	private String mapearCampoFiltro(String campo) {
		String field = "";

		switch (campo.toLowerCase()) {
		case "referencia":
			field = "referencia";
			break;
		case "titular":
			field = "titular";
			break;
		case "esctapropia":
			field = "esctapropia";
			break;
		case "idtipoidentifcta":
			field = "idtipoidentifcta";
			break;
		case "desctipoidentifcta":
			field = "desctipoidentifcta";
			break;
		case "codigoidentifcta":
			field = "codigoidentifcta";
			break;
		case "identidadfinanciera":
			field = "idEntidadFinanciera";
			break;
		case "descentidadfinanciera":
			field = "denominacion";
			break;
		default:
			LogACGHelper.escribirLog(null, "LazyItemAgendaDestinatarioDataModel. mapearCampoFiltro:|"+ campo + "| analizar como enviarlo a la consulta.");			
			field = "referencia";
			break;
		}
		
		return field;
	}

	private String sortFieldSwitch(String sortField) {
		String field = "referencia";

		switch (sortField.toLowerCase()) {
		case "referencia":
			field = "referencia";
			break;
		case "titular":
			field = "titular";
			break;		
		case "esctapropia":
			field = "esCtaPropia";
			break;
		case "idtipoidentifcta":
			field = "idtipoidentifcta";
			break;
		case "desctipoidentifcta":
			field = "descTipoIdentifCta";
			break;
		case "codigoidentifcta":
			field = "codigoIdentifCta";
			break;
		case "identidadfinanciera":
			field = "identidadfinanciera";
			break;
		case "descentidadfinanciera":
			field = "denominacion";
			break;
		default:
			//LogACGHelper.escribirLog(null, "Consulta LazyItemAgendaDestinatarioDataModel. sortField:|"+ sortField + "| analizar como enviarlo a la consulta.");			
			field = "referencia";
			break;
		}
		
		return field;
	}
}