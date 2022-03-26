package com.americacg.cargavirtual.web.lazymodels;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Map;

import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

import org.apache.commons.beanutils.PropertyUtils;
import org.primefaces.model.FilterMeta;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortMeta;

import com.americacg.cargavirtual.gestion.model.Cliente;
import com.americacg.cargavirtual.gestion.model.FiltroListadoPlatTransferencia;
import com.americacg.cargavirtual.gestion.model.ObjectFactory;
import com.americacg.cargavirtual.gestion.model.PlatCuenta;
import com.americacg.cargavirtual.gestion.model.PlatEntidadFinanciera;
import com.americacg.cargavirtual.gestion.model.PlatEstadoTransferencia;
import com.americacg.cargavirtual.gestion.model.PlatMoneda;
import com.americacg.cargavirtual.gestion.model.PlatTipoIdentifCta;
import com.americacg.cargavirtual.gestion.model.PlatTransferencia;
import com.americacg.cargavirtual.gestion.model.PlatTransferenciaList;
import com.americacg.cargavirtual.gestion.model.Usuario;
import com.americacg.cargavirtual.gestion.service.ArrayOfString;
import com.americacg.cargavirtual.web.enums.CfgTimeout;
import com.americacg.cargavirtual.web.helpers.GestionServiceHelper;
import com.americacg.cargavirtual.web.helpers.LogACGHelper;
import com.americacg.cargavirtual.web.model.superPago.Cuenta;
import com.americacg.cargavirtual.web.model.superPago.ItemTransferencia;

public class LazyTransferenciasDataModel extends LazyDataModel<ItemTransferencia> {
	/**
	 * 
	 */
	private static final long serialVersionUID = -990446514015779393L;
	private List<ItemTransferencia> datasource;
	private Long idMayorista;
	private Long idCliente;
	private Long idUsuarioPermReducido;
	private String campoOrdenamiento = "oFlt";
	private String tipoOrdenamiento = "asc";
	private String filtroCampo = null;
	private String filtroValor = null;
	private ArrayList<String> codigosMnemonicosExclusionClasificacion = null;
	private ArrayList<String> codigosMnemonicosInclusionClasificacion = null;
	private Long idClienteOrigen = null;
	private Long idClienteDestino = null;
	private Cuenta cuentaAdministradora = null;
	private Date fechaDesde = null;
	private Date fechaHasta = null;
	
	private ObjectFactory oGestionObjFac = new ObjectFactory();
	
	public LazyTransferenciasDataModel(Long idCliente, Long idMayorista, Cuenta cuentaAdministradora, Long idClienteOrigen, Long idClienteDestino, ArrayList<String> codigosMnemonicosInclusionClasificacion, ArrayList<String> codigosMnemonicosExclusionClasificacion, String campoOrdenamiento, String tipoOrdenamiento, Date fechaDesde, Date fechaHasta, Long idUsuarioPermReducido) {
		this.idMayorista = idMayorista;
		this.idCliente = idCliente;
		this.campoOrdenamiento = campoOrdenamiento;
		this.tipoOrdenamiento = tipoOrdenamiento;
		this.codigosMnemonicosExclusionClasificacion = codigosMnemonicosExclusionClasificacion;
		this.codigosMnemonicosInclusionClasificacion = codigosMnemonicosInclusionClasificacion;
		this.idClienteOrigen = idClienteOrigen;
		this.idClienteDestino = idClienteDestino;
		this.cuentaAdministradora = cuentaAdministradora;
		this.fechaDesde = fechaDesde;
		this.fechaHasta = fechaHasta;
		this.idUsuarioPermReducido = idUsuarioPermReducido;
	}

	@Override
	public ItemTransferencia getRowData(String rowKey) {
		if (datasource != null) {
			for (ItemTransferencia itmTrf : datasource) {
				if (itmTrf.getIdTransferencia().toString().equals(rowKey))
					return itmTrf;
			}
		}

		return null;
	}

	@Override
	public String getRowKey(ItemTransferencia itmTrf) {
		return itmTrf.getIdTransferencia().toString();
	}

	@Override
	public List<ItemTransferencia> getWrappedData() {
		// TODO Auto-generated method stub
		return super.getWrappedData();
	}

	@Override
	public List<ItemTransferencia> load(int first, int pageSize, Map<String, SortMeta> sortBy,
			Map<String, FilterMeta> filterBy) {
		FiltroListadoPlatTransferencia oFlt = new FiltroListadoPlatTransferencia();
		PlatTransferencia oTrfFlt = new PlatTransferencia();
		PlatCuenta oCtaAdmin = null;
		Usuario oUsr = null;
		Cliente oCliOri = null;
		Cliente oCliDes = null;
		datasource = new ArrayList<ItemTransferencia>();
		ArrayOfString codMnemExcl = null;
		ArrayOfString codMnemIncl = null;
		GregorianCalendar gcFechaDesde = null;
		XMLGregorianCalendar xmlGCFechaDesde = null;
		GregorianCalendar gcFechaHasta = null;
		XMLGregorianCalendar xmlGCFechaHasta = null;

		try {
			if (!filterBy.isEmpty()) {
				oTrfFlt = this.asignarFiltros(filterBy);
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
			
            /*if(oTrfFlt.getFechaTransferencia() != null) {
            	oFlt.setFechaDesde(oTrfFlt.getFechaTransferencia());
            	oFlt.setFechaHasta(oTrfFlt.getFechaTransferencia());
            }*/
			
			gcFechaDesde = new GregorianCalendar();
			gcFechaDesde.setTime(this.fechaDesde);
			xmlGCFechaDesde = DatatypeFactory.newInstance().newXMLGregorianCalendar(gcFechaDesde);

			gcFechaHasta = new GregorianCalendar();
			gcFechaHasta.setTime(this.fechaHasta);
			xmlGCFechaHasta = DatatypeFactory.newInstance().newXMLGregorianCalendar(gcFechaHasta);

			oFlt.setFechaDesde(xmlGCFechaDesde);
			oFlt.setFechaHasta(xmlGCFechaHasta);
            
			oTrfFlt.setIdMayorista(oGestionObjFac.createPlatTransferenciaIdMayorista(idMayorista));
			oTrfFlt.setIdCliente(oGestionObjFac.createPlatTransferenciaIdCliente(idCliente));

			if(idUsuarioPermReducido != null) {
				oUsr = new Usuario();
				oUsr.setIdUsuario(oGestionObjFac.createUsuarioIdUsuario(idUsuarioPermReducido));
				oTrfFlt.setUsuario(oGestionObjFac.createPlatTransferenciaUsuario(oUsr));
			}
			
			oFlt.setTransferencia(oGestionObjFac.createFiltroListadoPlatTransferenciaTransferencia(oTrfFlt));
			
			if(this.codigosMnemonicosExclusionClasificacion != null && this.codigosMnemonicosExclusionClasificacion.size() >0) {
				codMnemExcl = new ArrayOfString();
				codMnemExcl.getString().addAll(this.codigosMnemonicosExclusionClasificacion);
				oFlt.setCodMnemonicosExclClasifTrf(oGestionObjFac.createFiltroListadoPlatTransferenciaCodMnemonicosExclClasifTrf(codMnemExcl));
			}
			
			if(this.codigosMnemonicosInclusionClasificacion != null && this.codigosMnemonicosInclusionClasificacion.size() >0) {
				codMnemIncl = new ArrayOfString();
				codMnemIncl.getString().addAll(this.codigosMnemonicosInclusionClasificacion);
				oFlt.setCodMnemonicosIncluClasifTrf(oGestionObjFac.createFiltroListadoPlatTransferenciaCodMnemonicosIncluClasifTrf(codMnemIncl));
			}

			if(idClienteOrigen != null && idClienteOrigen.compareTo(0L) >0) {
				oCliOri = new Cliente();
				oCliOri.setIdCliente(oGestionObjFac.createClienteIdCliente(idClienteOrigen));
				oTrfFlt.setClienteOrigen(oGestionObjFac.createPlatTransferenciaClienteOrigen(oCliOri));
			}

			if(idClienteDestino != null && idClienteDestino.compareTo(0L) >0) {
				oCliDes = new Cliente();
				oCliDes.setIdCliente(oGestionObjFac.createClienteIdCliente(idClienteDestino));
				oTrfFlt.setClienteDestino(oGestionObjFac.createPlatTransferenciaClienteDestino(oCliDes));
			}
			
			if(cuentaAdministradora != null) {
				oCtaAdmin = new PlatCuenta();
				//oCtaAdmin.setIdCuenta(oGestionObjFac.createPlatCuentaIdCuenta(cuentaAdministradora.getIdCuenta()));
				//oCtaAdmin.setAlias(oGestionObjFac.createPlatCuentaAlias(cuentaAdministradora.getAlias()));
				oCtaAdmin.setNumeroCuenta(oGestionObjFac.createPlatCuentaNumeroCuenta(cuentaAdministradora.getNumeroCuenta()));
				//oCtaAdmin.setCVU(oGestionObjFac.createPlatCuentaCVU(cuentaAdministradora.getCVU()));
				
				oFlt.setCuentaAdministradoraTrf(oGestionObjFac.createFiltroListadoPlatTransferenciaCuentaAdministradoraTrf(oCtaAdmin));
			}

			oFlt.setPage(oGestionObjFac.createFiltroListadoPlatTransferenciaPage((first / pageSize) + 1));
			oFlt.setPageSize(oGestionObjFac.createFiltroListadoPlatTransferenciaPageSize(pageSize));
			oFlt.setCampoOrden(oGestionObjFac.createFiltroListadoPlatTransferenciaCampoOrden(campoOrdenamiento));
			oFlt.setTipoOrden(oGestionObjFac.createFiltroListadoPlatTransferenciaTipoOrden(tipoOrdenamiento));
			
			PlatTransferenciaList oLstTrf = GestionServiceHelper.getGestionService(CfgTimeout.DEFAULT).listarPlatTransferencia(oFlt); 
			
			if (oLstTrf != null && oLstTrf.getRegistros() != null && oLstTrf.getRegistros().getValue() != null
					&& oLstTrf.getRegistros().getValue().getPlatTransferencia() != null) {
				for (PlatTransferencia trfAux : oLstTrf.getRegistros().getValue().getPlatTransferencia()) {
					ItemTransferencia oItmTrf = new ItemTransferencia();
					oItmTrf.setIdTransferencia(trfAux.getIdTransferencia().getValue());
					oItmTrf.setIdMayorista(trfAux.getIdMayorista().getValue());
					oItmTrf.setIdUsuario(trfAux.getUsuario().getValue().getIdUsuario().getValue());
					oItmTrf.setUsuario(trfAux.getUsuario().getValue().getUsuario().getValue());
					oItmTrf.setIdCliente(trfAux.getIdCliente().getValue());
					oItmTrf.setFechaAlta((trfAux.getFechaAlta() != null ? trfAux.getFechaAlta().toGregorianCalendar().getTime() : null));
					oItmTrf.setReferencia(trfAux.getReferencia().getValue());
					oItmTrf.setIdCanal((trfAux.getCanal().getValue() != null ? trfAux.getCanal().getValue().getIdCanal().getValue() : null));
					oItmTrf.setDescCanal((trfAux.getCanal().getValue() != null ? trfAux.getCanal().getValue().getDescripcion().getValue() : null));
					oItmTrf.setFechaTransferencia(trfAux.getFechaTransferencia().toGregorianCalendar().getTime());
					oItmTrf.setIdClasificacion((trfAux.getClasificacion().getValue() != null ? trfAux.getClasificacion().getValue().getIdClasificacionTransferencia().getValue() : null));
					oItmTrf.setDescClasificacion((trfAux.getClasificacion().getValue() != null ? trfAux.getClasificacion().getValue().getDescripcion().getValue() : null));
					oItmTrf.setCodMnemonicoClasificacion((trfAux.getClasificacion().getValue() != null ? trfAux.getClasificacion().getValue().getCodMnemonico().getValue() : null));
					oItmTrf.setIdMoneda((trfAux.getMoneda().getValue() != null ? trfAux.getMoneda().getValue().getIdMoneda().getValue() : null));
					oItmTrf.setDescripcionMoneda((trfAux.getMoneda().getValue() != null ? trfAux.getMoneda().getValue().getDescripcion().getValue() : null));
					oItmTrf.setSimboloMoneda((trfAux.getMoneda().getValue() != null ? trfAux.getMoneda().getValue().getTxtSimbolo().getValue() : null));
					oItmTrf.setImporte(trfAux.getImporte().getValue());
					oItmTrf.setIdEntidadOrigen((trfAux.getEntidadOrigen().getValue() != null ? trfAux.getEntidadOrigen().getValue().getId().getValue() : null));
					oItmTrf.setDescEntidadOrigen((trfAux.getEntidadOrigen().getValue() != null ? trfAux.getEntidadOrigen().getValue().getDenominacion().getValue() : null));
					oItmTrf.setCodMnemonicoEntOrigen((trfAux.getEntidadOrigen().getValue() != null ? trfAux.getEntidadOrigen().getValue().getCodMnemonico().getValue() : null));
					oItmTrf.setIdTipoIdentifOrigen((trfAux.getTipoIdentifOrigen().getValue() != null ? trfAux.getTipoIdentifOrigen().getValue().getIdTipoIdentifCta().getValue() : null));
					oItmTrf.setDescTipoIdentifOrigen((trfAux.getTipoIdentifOrigen().getValue() != null ? trfAux.getTipoIdentifOrigen().getValue().getDescripcion().getValue() : null));
					oItmTrf.setCodigoIdentifOrigen(trfAux.getCodigoIdentifOrigen().getValue());
					oItmTrf.setIdTipoIdentifDestino((trfAux.getTipoIdentifDestino().getValue() != null ? trfAux.getTipoIdentifDestino().getValue().getIdTipoIdentifCta().getValue() : null));
					oItmTrf.setDescTipoIdentifDestino((trfAux.getTipoIdentifDestino().getValue() != null ? trfAux.getTipoIdentifDestino().getValue().getDescripcion().getValue() : null));
					oItmTrf.setCodigoIdentifDestino(trfAux.getCodigoIdentifDestino().getValue());
					oItmTrf.setNroComprobanteOrigen(trfAux.getNroComprobanteOrigen().getValue());
					oItmTrf.setIdEntidadDestino((trfAux.getEntidadDestino().getValue() != null ? trfAux.getEntidadDestino().getValue().getId().getValue() : null));
					oItmTrf.setDescEntidadDestino((trfAux.getEntidadDestino().getValue() != null ? trfAux.getEntidadDestino().getValue().getDenominacion().getValue() : null));
					oItmTrf.setCodMnemonicoEntDestino((trfAux.getEntidadDestino().getValue() != null ? trfAux.getEntidadDestino().getValue().getCodMnemonico().getValue() : null));
					oItmTrf.setConcepto(trfAux.getConcepto().getValue());
					oItmTrf.setFecUltMod(trfAux.getFecUltMod().toGregorianCalendar().getTime());
					oItmTrf.setUsuUltMod(trfAux.getUsuUltMod().getValue());
					oItmTrf.setObs(trfAux.getObs().getValue());
					oItmTrf.setFechaAcreditacionCuentaOrigen((trfAux.getFechaAcreditacionCuentaOrigen() != null ? trfAux.getFechaAcreditacionCuentaOrigen().toGregorianCalendar().getTime() : null));
					oItmTrf.setIdMovimientoDeCuentaOrigen(trfAux.getIdMovimientoDeCuentaOrigen().getValue());
					oItmTrf.setFechaAcreditacionCuentaDestino((trfAux.getFechaAcreditacionCuentaDestino() != null ? trfAux.getFechaAcreditacionCuentaDestino().toGregorianCalendar().getTime() : null));
					oItmTrf.setIdMovimientoDeCuentaDestino(trfAux.getIdMovimientoDeCuentaDestino().getValue());
					oItmTrf.setIdEstado((trfAux.getEstado().getValue() != null && trfAux.getEstado().getValue().getIdEstadoTransferencia() != null ? trfAux.getEstado().getValue().getIdEstadoTransferencia().getValue() : null));
					oItmTrf.setDescEstado((trfAux.getEstado().getValue() != null && trfAux.getEstado().getValue().getDescripcion() != null ? trfAux.getEstado().getValue().getDescripcion().getValue() : null));					
					oItmTrf.setCodMnemonicoEstado((trfAux.getEstado().getValue() != null && trfAux.getEstado().getValue().getCodMnemonico() != null ? trfAux.getEstado().getValue().getCodMnemonico().getValue() : null));
					
					oItmTrf.setIdClienteOrigen((trfAux.getClienteOrigen().getValue() != null ? trfAux.getClienteOrigen().getValue().getIdCliente().getValue() : null));
					oItmTrf.setCuitClienteOrigen((trfAux.getClienteOrigen().getValue() != null ? trfAux.getClienteOrigen().getValue().getCUIT().getValue() : null));
					oItmTrf.setApellidoClienteOrigen((trfAux.getClienteOrigen().getValue() != null ? trfAux.getClienteOrigen().getValue().getApellido().getValue() : null));
					oItmTrf.setNombreClienteOrigen((trfAux.getClienteOrigen().getValue() != null ? trfAux.getClienteOrigen().getValue().getNombre().getValue() : null));
					oItmTrf.setNombreFantasiaClienteOrigen((trfAux.getClienteOrigen().getValue() != null ? trfAux.getClienteOrigen().getValue().getNombreFantasia().getValue() : null));
					oItmTrf.setRazonSocialClienteOrigen((trfAux.getClienteOrigen().getValue() != null ? trfAux.getClienteOrigen().getValue().getRazonSocial().getValue() : null));
					
					oItmTrf.setIdClienteDestino((trfAux.getClienteDestino().getValue() != null ? trfAux.getClienteDestino().getValue().getIdCliente().getValue() : null));
					oItmTrf.setCuitClienteDestino((trfAux.getClienteDestino().getValue() != null ? trfAux.getClienteDestino().getValue().getCUIT().getValue() : null));
					oItmTrf.setApellidoClienteDestino((trfAux.getClienteDestino().getValue() != null ? trfAux.getClienteDestino().getValue().getApellido().getValue() : null));
					oItmTrf.setNombreClienteDestino((trfAux.getClienteDestino().getValue() != null ? trfAux.getClienteDestino().getValue().getNombre().getValue() : null));
					oItmTrf.setNombreFantasiaClienteDestino((trfAux.getClienteDestino().getValue() != null ? trfAux.getClienteDestino().getValue().getNombreFantasia().getValue() : null));
					oItmTrf.setRazonSocialClienteDestino((trfAux.getClienteDestino().getValue() != null ? trfAux.getClienteDestino().getValue().getRazonSocial().getValue() : null));

					oItmTrf.setTipoIdenTribDestino(trfAux.getTipoIdentTribDestino().getValue().getDescripcion().getValue());
					oItmTrf.setNroIdenTribDestino(trfAux.getNroIdenTribDestino().getValue());
					oItmTrf.setTitularDestino(trfAux.getTitularDestino().getValue());
					
					datasource.add(oItmTrf);
				}

				this.setRowCount(oLstTrf.getTotalRegistros().getValue());
			} else {
				this.setRowCount(0);
			}

			return datasource;
		} catch (Exception ex) {
			return null;
		}
	}

	private PlatTransferencia asignarFiltros(Map<String, FilterMeta> filters) {
		boolean propNroEmpty = false;
		boolean propNro = false;
		boolean propBoolean = false;
		boolean propDate = false;
		ItemTransferencia itmTrf = new ItemTransferencia();
		PlatTransferencia oTrf = new PlatTransferencia();
		PlatEntidadFinanciera oEntFinan = null;
		PlatTipoIdentifCta oTipIdDest = null;
		PlatTipoIdentifCta oTipIdOri = null;
		PlatEstadoTransferencia oEst = null;
		PlatMoneda oMoneda = null;
		GregorianCalendar gcFechaAux = null;
		XMLGregorianCalendar xmlGCFechaAux = null;
		SimpleDateFormat formato = new SimpleDateFormat("yyyy-MM-dd");
		
		this.filtroCampo = null;
		this.filtroValor = null;

		if (!filters.isEmpty()) {
			try {
				for (Map.Entry<String, FilterMeta> oProp : filters.entrySet()) {
					Class<?> xtype = PropertyUtils.getPropertyType(itmTrf, oProp.getKey());
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
						} else if (Date.class.isAssignableFrom(xtype)) {
							propNro = false;
							propDate = true;
							oVal = formato.parse(value.toString());
							if (oVal != null)
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
						PropertyUtils.setProperty(itmTrf, oProp.getKey(), null);
						this.filtroCampo = mapearCampoFiltro(oProp.getKey());
						this.filtroValor = null;
					} else if(propBoolean) {
						PropertyUtils.setProperty(itmTrf, oProp.getKey(), oVal);
						this.filtroCampo = mapearCampoFiltro(oProp.getKey());
						if (oVal != null && (Boolean)oVal == true) {
							this.filtroValor = "1";
						} else {
							this.filtroValor = null;
						}
					} else if(propDate) {
						PropertyUtils.setProperty(itmTrf, oProp.getKey(), oVal);
						this.filtroCampo = mapearCampoFiltro(oProp.getKey());
						if (oVal != null) {
							this.filtroValor = formato.format((Date)oVal);
						} else {
							this.filtroValor = null;
						}
					}
					else {
						PropertyUtils.setProperty(itmTrf, oProp.getKey(), oVal);
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

				if(itmTrf.getIdTransferencia() != null) {
					oTrf.setIdTransferencia(oGestionObjFac.createPlatTransferenciaIdTransferencia(itmTrf.getIdTransferencia()));
				}	
				else if(itmTrf.getFechaTransferencia() != null) {
					gcFechaAux = new GregorianCalendar();
					gcFechaAux.setTime(itmTrf.getFechaTransferencia());
					xmlGCFechaAux = DatatypeFactory.newInstance().newXMLGregorianCalendar(gcFechaAux);

					oTrf.setFechaTransferencia(xmlGCFechaAux);
				}	
				else if(itmTrf.getIdEntidadOrigen() != null) {
					oEntFinan = new PlatEntidadFinanciera();
					oEntFinan.setId(oGestionObjFac.createPlatEntidadFinancieraId(itmTrf.getIdEntidadOrigen()));
					oTrf.setEntidadOrigen(oGestionObjFac.createPlatTransferenciaEntidadOrigen(oEntFinan));
				}
				else if(itmTrf.getIdEntidadDestino() != null) {
					oEntFinan = new PlatEntidadFinanciera();
					oEntFinan.setId(oGestionObjFac.createPlatEntidadFinancieraId(itmTrf.getIdEntidadDestino()));
					oTrf.setEntidadDestino(oGestionObjFac.createPlatTransferenciaEntidadDestino(oEntFinan));
				}
				else if(itmTrf.getIdTipoIdentifOrigen() != null) {
					oTipIdOri = new PlatTipoIdentifCta();
					oTipIdOri.setIdTipoIdentifCta(oGestionObjFac.createPlatTipoIdentifCtaIdTipoIdentifCta(itmTrf.getIdTipoIdentifOrigen()));
					oTrf.setTipoIdentifDestino(oGestionObjFac.createPlatTransferenciaTipoIdentifOrigen(oTipIdOri));
				}
				else if(itmTrf.getCodigoIdentifOrigen() != null) {
					oTrf.setCodigoIdentifOrigen(oGestionObjFac.createPlatTransferenciaCodigoIdentifOrigen(itmTrf.getCodigoIdentifOrigen()));
				}
				else if(itmTrf.getIdTipoIdentifDestino() != null) {
					oTipIdDest = new PlatTipoIdentifCta();
					oTipIdDest.setIdTipoIdentifCta(oGestionObjFac.createPlatTipoIdentifCtaIdTipoIdentifCta(itmTrf.getIdTipoIdentifDestino()));
					oTrf.setTipoIdentifDestino(oGestionObjFac.createPlatTransferenciaTipoIdentifDestino(oTipIdDest));
				}
				else if(itmTrf.getCodigoIdentifDestino() != null) {
					oTrf.setCodigoIdentifDestino(oGestionObjFac.createPlatTransferenciaCodigoIdentifDestino(itmTrf.getCodigoIdentifDestino()));
				}
				else if(itmTrf.getIdEstado() != null) {
					oEst = new PlatEstadoTransferencia();
					oEst.setIdEstadoTransferencia(oGestionObjFac.createPlatEstadoTransferenciaIdEstadoTransferencia(itmTrf.getIdEstado()));
					oTrf.setEstado(oGestionObjFac.createPlatTransferenciaEstado(oEst));
				}
				else if(itmTrf.getIdMoneda() != null) {
					oMoneda = new PlatMoneda();
					oMoneda.setIdMoneda(oGestionObjFac.createPlatMonedaIdMoneda(itmTrf.getIdMoneda()));
					oTrf.setMoneda(oGestionObjFac.createPlatTransferenciaMoneda(oMoneda));
				}

			} catch (Exception e) {
				LogACGHelper.escribirLog(null,
						"Se produjo un error LazyItemTransferenciaDataModel metodo load (ROL). Msg:|" + e.getMessage() + "|");
			}
		}
		
		return oTrf;
	}

	private String mapearCampoFiltro(String campo) {
		String field = "";

		switch (campo.toLowerCase()) {
		case "idtransferencia":
			field = "idtransferencia";
			break;
		case "fechatransferencia":
			field = "fechatransferencia";
			break;
		case "identidadorigen":
			field = "identidadorigen";
			break;
		case "identidaddestino":
			field = "identidaddestino";
			break;
		case "idtipoidentiforigen":
			field = "idtipoidentiforigen";
			break;
		case "codigoidentiforigen":
			field = "codigoidentiforigen";
			break;
		case "idtipoidentifdestino":
			field = "idtipoidentifdestino";
			break;
		case "codigoidentifdestino":
			field = "codigoidentifdestino";
			break;
		case "idEstado":
			field = "idEstado";
			break;
		case "idMoneda":
			field = "idMoneda";
			break;
			
		default:
			field = "idtransferencia";
			break;
		}
		
		return field;
	}

	private String sortFieldSwitch(String sortField) {
		String field = "";

		switch (sortField.toLowerCase()) {
		case "idtransferencia":
			field = "idtransferencia";
			break;
		case "fechatransferencia":
			field = "fechatransferencia";
			break;
		case "identidadorigen":
			field = "identidadorigen";
			break;
		case "identidaddestino":
			field = "identidaddestino";
			break;
		case "idtipoidentiforigen":
			field = "idtipoidentiforigen";
			break;
		case "codigoidentiforigen":
			field = "codigoidentiforigen";
			break;
		case "idtipoidentifdestino":
			field = "idtipoidentifdestino";
			break;
		case "codigoidentifdestino":
			field = "codigoidentifdestino";
			break;
		case "idEstado":
			field = "idEstado";
			break;
		case "idMoneda":
			field = "idMoneda";
			break;
		case "importe":
			field = "importe";
			break;

		default:
			field = "idtransferencia";
			break;
		}
		
		return field;
	}
}