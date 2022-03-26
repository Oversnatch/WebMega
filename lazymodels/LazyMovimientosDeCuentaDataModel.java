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
import com.americacg.cargavirtual.gestion.model.FiltroListadoPlatMovimientoCuenta;
import com.americacg.cargavirtual.gestion.model.FiltroListadoPlatTransferencia;
import com.americacg.cargavirtual.gestion.model.ObjectFactory;
import com.americacg.cargavirtual.gestion.model.PlatCuenta;
import com.americacg.cargavirtual.gestion.model.PlatEntidadFinanciera;
import com.americacg.cargavirtual.gestion.model.PlatEstadoMovimientoCuenta;
import com.americacg.cargavirtual.gestion.model.PlatEstadoTransferencia;
import com.americacg.cargavirtual.gestion.model.PlatMoneda;
import com.americacg.cargavirtual.gestion.model.PlatMovimientoCuenta;
import com.americacg.cargavirtual.gestion.model.PlatMovimientoCuentaList;
import com.americacg.cargavirtual.gestion.model.PlatTipoIdentifCta;
import com.americacg.cargavirtual.gestion.model.PlatTipoMovimiento;
import com.americacg.cargavirtual.gestion.model.PlatTransferencia;
import com.americacg.cargavirtual.gestion.model.PlatTransferenciaList;
import com.americacg.cargavirtual.gestion.model.Usuario;
import com.americacg.cargavirtual.gestion.service.ArrayOfString;
import com.americacg.cargavirtual.web.enums.CfgTimeout;
import com.americacg.cargavirtual.web.helpers.GestionServiceHelper;
import com.americacg.cargavirtual.web.helpers.LogACGHelper;
import com.americacg.cargavirtual.web.model.Canal;
import com.americacg.cargavirtual.web.model.Modulo;
import com.americacg.cargavirtual.web.model.Moneda;
import com.americacg.cargavirtual.web.model.Producto;
import com.americacg.cargavirtual.web.model.superPago.Cuenta;
import com.americacg.cargavirtual.web.model.superPago.EstadoMovimientoDeCuenta;
import com.americacg.cargavirtual.web.model.superPago.ItemTransferencia;
import com.americacg.cargavirtual.web.model.superPago.MovimientoDeCuenta;
import com.americacg.cargavirtual.web.model.superPago.TipoMovimiento;

public class LazyMovimientosDeCuentaDataModel extends LazyDataModel<MovimientoDeCuenta> {
	/**
	 * 
	 */
	private static final long serialVersionUID = -8612286720134811300L;
	private List<MovimientoDeCuenta> datasource;
	private Long idMayorista;
	private Long idCliente;
	private Long idUsuarioPermReducido;
	private String campoOrdenamiento = "fechaAcreditacion";
	private String tipoOrdenamiento = "desc";
	private ArrayList<String> codigosMnemonicosExclusionEstado = null;
	private ArrayList<String> codigosMnemonicosInclusionEstado = null;
	private String filtroCampo = null;
	private String filtroValor = null;
	private Cuenta cuentaAdministradora = null;
	private Date fechaDesde = null;
	private Date fechaHasta = null;
	
	private ObjectFactory oGestionObjFac = new ObjectFactory();
	
	public LazyMovimientosDeCuentaDataModel(Long idMayorista, Long idCliente, Cuenta cuentaAdministradora, ArrayList<String> codigosMnemonicosInclusionEstado, ArrayList<String> codigosMnemonicosExclusionEstado,String campoOrdenamiento, String tipoOrdenamiento, Date fechaDesde, Date fechaHasta, Long idUsuarioPermReducido) {
		this.idMayorista = idMayorista;
		this.idCliente = idCliente;
		this.campoOrdenamiento = campoOrdenamiento;
		this.tipoOrdenamiento = tipoOrdenamiento;
		this.cuentaAdministradora = cuentaAdministradora;
		this.fechaDesde = fechaDesde;
		this.fechaHasta = fechaHasta;
		this.codigosMnemonicosExclusionEstado = codigosMnemonicosExclusionEstado;
		this.codigosMnemonicosInclusionEstado = codigosMnemonicosInclusionEstado;
		this.idUsuarioPermReducido = idUsuarioPermReducido;
	}

	@Override
	public MovimientoDeCuenta getRowData(String rowKey) {
		if (datasource != null) {
			for (MovimientoDeCuenta itmTrf : datasource) {
				if (itmTrf.getIdMovimientoCuenta().toString().equals(rowKey))
					return itmTrf;
			}
		}

		return null;
	}

	@Override
	public String getRowKey(MovimientoDeCuenta itmMovCta) {
		return itmMovCta.getIdMovimientoCuenta().toString();
	}

	@Override
	public List<MovimientoDeCuenta> getWrappedData() {
		// TODO Auto-generated method stub
		return super.getWrappedData();
	}

	@Override
	public List<MovimientoDeCuenta> load(int first, int pageSize, Map<String, SortMeta> sortBy,
			Map<String, FilterMeta> filterBy) {
		FiltroListadoPlatMovimientoCuenta oFlt = new FiltroListadoPlatMovimientoCuenta();
		PlatMovimientoCuenta oMovCtaFlt = new PlatMovimientoCuenta();
		PlatCuenta oCtaAdmin = null;
		
		Producto oPrdMC = null;
		Cuenta oCtaMC = null;
		Canal oCanalMC = null;
		Modulo oModuloMC = null;
		TipoMovimiento oTipMovMC = null;
		EstadoMovimientoDeCuenta oEstMC = null;
		
		datasource = new ArrayList<MovimientoDeCuenta>();
		ArrayOfString codMnemExcl = null;
		ArrayOfString codMnemIncl = null;
		GregorianCalendar gcFechaDesde = null;
		XMLGregorianCalendar xmlGCFechaDesde = null;
		GregorianCalendar gcFechaHasta = null;
		XMLGregorianCalendar xmlGCFechaHasta = null;
		PlatMovimientoCuentaList oLstMovCta = null;
		Usuario oUsr = null;
		
		try {
			if (!filterBy.isEmpty()) {
				oMovCtaFlt = this.asignarFiltros(filterBy);
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
			
			if(this.fechaDesde != null) {
				gcFechaDesde = new GregorianCalendar();
				gcFechaDesde.setTime(this.fechaDesde);
				xmlGCFechaDesde = DatatypeFactory.newInstance().newXMLGregorianCalendar(gcFechaDesde);
				
				oFlt.setFechaDesde(xmlGCFechaDesde);
			}

			if(this.fechaHasta != null) {
				gcFechaHasta = new GregorianCalendar();
				gcFechaHasta.setTime(this.fechaHasta);
				xmlGCFechaHasta = DatatypeFactory.newInstance().newXMLGregorianCalendar(gcFechaHasta);

				oFlt.setFechaHasta(xmlGCFechaHasta);
			}
            
			oMovCtaFlt.setIdMayorista(oGestionObjFac.createPlatMovimientoCuentaIdMayorista(idMayorista));
			Cliente oCli = new Cliente();
			oCli.setIdCliente(oGestionObjFac.createClienteIdCliente(idCliente));
			oMovCtaFlt.setCliente(oGestionObjFac.createPlatMovimientoCuentaCliente(oCli));
			
			//Para Uso Futuro.
			//oMovCtaFlt.setIdUsuario(oGestionObjFac.createPlatTransferenciaIdCliente(idUsuario));

			oFlt.setMovimientoCuenta(oGestionObjFac.createFiltroListadoPlatMovimientoCuentaMovimientoCuenta(oMovCtaFlt));
			
			if(this.codigosMnemonicosExclusionEstado != null && this.codigosMnemonicosExclusionEstado.size() >0) {
				codMnemExcl = new ArrayOfString();
				codMnemExcl.getString().addAll(this.codigosMnemonicosExclusionEstado);
				oFlt.setCodMnemonicosExclEstado(oGestionObjFac.createFiltroListadoPlatMovimientoCuentaCodMnemonicosExclEstado(codMnemExcl));
			}
			
			if(this.codigosMnemonicosInclusionEstado != null && this.codigosMnemonicosInclusionEstado.size() >0) {
				codMnemIncl = new ArrayOfString();
				codMnemIncl.getString().addAll(this.codigosMnemonicosInclusionEstado);
				oFlt.setCodMnemonicosIncluEstado(oGestionObjFac.createFiltroListadoPlatMovimientoCuentaCodMnemonicosIncluEstado(codMnemIncl));
			}

			if(cuentaAdministradora != null && (cuentaAdministradora.getIdCuenta().compareTo(0L) >0 || 
					(cuentaAdministradora.getNumeroCuenta() != null && !cuentaAdministradora.getNumeroCuenta().isEmpty()) ||  
					(cuentaAdministradora.getCVU() != null && !cuentaAdministradora.getCVU().isEmpty()) ||
					(cuentaAdministradora.getAlias() != null && !cuentaAdministradora.getAlias().isEmpty()))
				) {
				oCtaAdmin = new PlatCuenta();
				oCtaAdmin.setIdCuenta(oGestionObjFac.createPlatCuentaIdCuenta(cuentaAdministradora.getIdCuenta()));
				oCtaAdmin.setAlias(oGestionObjFac.createPlatCuentaAlias(cuentaAdministradora.getAlias()));
				oCtaAdmin.setNumeroCuenta(oGestionObjFac.createPlatCuentaNumeroCuenta(cuentaAdministradora.getNumeroCuenta()));
				oMovCtaFlt.setCuenta(oGestionObjFac.createPlatMovimientoCuentaCuenta(oCtaAdmin));
			}


			oMovCtaFlt.setIdMayorista(oGestionObjFac.createFiltroListadoPlatMovimientoCuentaIdMayorista(idMayorista));
			
			if("importe".equals(filtroCampo)) {
				oFlt.getMovimientoCuenta().getValue().setImporte(oGestionObjFac.createPlatMovimientoCuentaImporte(null));
				
				if("1.0".equals(filtroValor))
					oFlt.setTipoDebCred(oGestionObjFac.createFiltroListadoPlatMovimientoCuentaTipoDebCred("DEB"));
				else if("2.0".equals(filtroValor))
					oFlt.setTipoDebCred(oGestionObjFac.createFiltroListadoPlatMovimientoCuentaTipoDebCred("CRED"));
				else
					oFlt.setTipoDebCred(oGestionObjFac.createFiltroListadoPlatMovimientoCuentaTipoDebCred(null));
			}
			
			if(idUsuarioPermReducido != null) {
				oUsr = new Usuario();
				oUsr.setIdUsuario(oGestionObjFac.createUsuarioIdUsuario(idUsuarioPermReducido));
				oMovCtaFlt.setUsuario(oGestionObjFac.createPlatTransferenciaUsuario(oUsr));
			}
			
			oFlt.setPage(oGestionObjFac.createFiltroListadoPlatMovimientoCuentaPage((first / pageSize) + 1));
			oFlt.setPageSize(oGestionObjFac.createFiltroListadoPlatMovimientoCuentaPageSize(pageSize));
			oFlt.setCampoOrden(oGestionObjFac.createFiltroListadoPlatMovimientoCuentaCampoOrden(campoOrdenamiento));
			oFlt.setTipoOrden(oGestionObjFac.createFiltroListadoPlatMovimientoCuentaTipoOrden(tipoOrdenamiento));
			
			oLstMovCta = GestionServiceHelper.getGestionService(CfgTimeout.DEFAULT).listarPlatMovimientoCuenta(oFlt); 
			
			if (oLstMovCta != null && oLstMovCta.getRegistros() != null && oLstMovCta.getRegistros().getValue() != null
					&& oLstMovCta.getRegistros().getValue().getPlatMovimientoCuenta() != null) {
				for (PlatMovimientoCuenta oMovCtaAux : oLstMovCta.getRegistros().getValue().getPlatMovimientoCuenta()) {
					MovimientoDeCuenta oItmMovCta = new MovimientoDeCuenta();

					oItmMovCta.setIdMovimientoCuenta(oMovCtaAux.getIdMovimientoCuenta().getValue());
					oItmMovCta.setIdMayorista(oMovCtaAux.getIdMayorista().getValue());
					oItmMovCta.setIdCliente(oMovCtaAux.getCliente().getValue().getIdCliente().getValue());
					
					if(oMovCtaAux.getCuenta().getValue() != null) {
						oCtaMC = new Cuenta();
						oCtaMC.setIdCuenta(oMovCtaAux.getCuenta().getValue().getIdCuenta().getValue());
						
						if(oMovCtaAux.getCuenta().getValue().getMoneda().getValue() != null) {
							oCtaMC.setIdMoneda(oMovCtaAux.getCuenta().getValue().getMoneda().getValue().getIdMoneda().getValue());
						}
						
						oItmMovCta.setCuenta(oCtaMC);
					}
					
					oItmMovCta.setFechaAlta(oMovCtaAux.getFechaAlta().toGregorianCalendar().getTime());
					
					if(oMovCtaAux.getCanal().getValue() != null) {
						oCanalMC = new Canal();
						oCanalMC.setIdCanal(oMovCtaAux.getCanal().getValue().getIdCanal().getValue());
						oItmMovCta.setCanal(oCanalMC);
					}
					
					//oItmMovCta.setAdmiteDetalleOrigen(oMovCtaAux.getAdmiteDetalleOrigen().getValue());
					oItmMovCta.setIdClasificacion(oMovCtaAux.getClasificacion().getValue().getIdClasificacionMovimientoCuenta().getValue());
					oItmMovCta.setCodMnemonicoClasificacion(oMovCtaAux.getClasificacion().getValue().getCodMnemonico().getValue());
					oItmMovCta.setDescClasificacion(oMovCtaAux.getClasificacion().getValue().getDescripcion().getValue());
					
					if(oMovCtaAux.getModuloOrigen().getValue() != null) {
						oModuloMC = new Modulo();
						oModuloMC.setIdModulo(oMovCtaAux.getModuloOrigen().getValue().getIdModulo().getValue());
						oModuloMC.setCodigoModulo(oMovCtaAux.getModuloOrigen().getValue().getCodigoModulo().getValue());
						oItmMovCta.setModuloOrigen(oModuloMC);
					}
					
					if(oMovCtaAux.getProductoOrigen().getValue() != null) {
						oPrdMC = new Producto();
						oPrdMC.setIdProducto(oMovCtaAux.getProductoOrigen().getValue().getIdProducto().getValue());
						oPrdMC.setCodMnemonico(oMovCtaAux.getProductoOrigen().getValue().getCodMnemonico().getValue());
						oItmMovCta.setProductoOrigen(oPrdMC);
					}
					
					oItmMovCta.setIdOrigen(oMovCtaAux.getIdOrigen().getValue());
					oItmMovCta.setFechaAlta((oMovCtaAux.getFechaAlta() != null ? oMovCtaAux.getFechaAlta().toGregorianCalendar().getTime() : null));
					oItmMovCta.setFechaMovimiento((oMovCtaAux.getFechaMovimiento() != null ? oMovCtaAux.getFechaMovimiento().toGregorianCalendar().getTime() : null));
					oItmMovCta.setFechaAcreditacion((oMovCtaAux.getFechaAcreditacion() != null ? oMovCtaAux.getFechaAcreditacion().toGregorianCalendar().getTime() : null));
					
					if(oMovCtaAux.getTipoMovimiento().getValue() != null) {
						oTipMovMC = new TipoMovimiento();
						oTipMovMC.setIdTipoMovimiento(oMovCtaAux.getTipoMovimiento().getValue().getIdPlatTipoMovimiento().getValue());
						oTipMovMC.setDescripcion(oMovCtaAux.getTipoMovimiento().getValue().getDescripcion().getValue());
						oTipMovMC.setCodMnemonico(oMovCtaAux.getTipoMovimiento().getValue().getCodMnemonico().getValue());
						oItmMovCta.setTipoMovimiento(oTipMovMC);
					}
					
					oItmMovCta.setConcepto(oMovCtaAux.getConcepto().getValue());
					oItmMovCta.setImpCredito(oMovCtaAux.getImpCredito().getValue());
					oItmMovCta.setImpDebito(oMovCtaAux.getImpDebito().getValue());
					oItmMovCta.setImporte(oMovCtaAux.getImporte().getValue());
					oItmMovCta.setNroOrden(oMovCtaAux.getNroOrden().getValue());
					oItmMovCta.setObs(oMovCtaAux.getObs().getValue());
					
					if(oMovCtaAux.getEstado().getValue() != null) {
						oEstMC = new EstadoMovimientoDeCuenta();
						oEstMC.setIdEstadoMovimientoCuenta(oMovCtaAux.getEstado().getValue().getIdEstadoMovimientoCuenta().getValue());
						oEstMC.setDescripcion(oMovCtaAux.getEstado().getValue().getDescripcion().getValue());
						oEstMC.setCodMnemonico(oMovCtaAux.getEstado().getValue().getCodMnemonico().getValue());
						oItmMovCta.setEstado(oEstMC);
					}
					
					oItmMovCta.setIdUsuUltMod(oMovCtaAux.getIdUsuUltMod().getValue());
					oItmMovCta.setTmsUltMod((oMovCtaAux.getTmsUltMod() != null ? oMovCtaAux.getTmsUltMod().toGregorianCalendar().getTime() : null));
					oItmMovCta.setIdCliente(oMovCtaAux.getCliente().getValue().getIdCliente().getValue());
					oItmMovCta.setIdUsuario(oMovCtaAux.getUsuario().getValue().getIdUsuario().getValue());
					oItmMovCta.setUsuario(oMovCtaAux.getUsuario().getValue().getUsuario().getValue());
					
					datasource.add(oItmMovCta);
				}

				this.setRowCount(oLstMovCta.getTotalRegistros().getValue());
			} else {
				this.setRowCount(0);
			}

			return datasource;
		} catch (Exception ex) {
			return null;
		}
	}

	private PlatMovimientoCuenta asignarFiltros(Map<String, FilterMeta> filters) {
		boolean propNroEmpty = false;
		boolean propNro = false;
		boolean propBoolean = false;
		boolean propDate = false;
		MovimientoDeCuenta itmMovCta = new MovimientoDeCuenta();
		PlatMovimientoCuenta oMovCta = new PlatMovimientoCuenta();
		PlatCuenta oCta = null;
		PlatTipoMovimiento oTipMov = null;
		PlatMoneda oMoneda = null;
		com.americacg.cargavirtual.gestion.model.Producto oPrd = null;
		com.americacg.cargavirtual.gestion.model.Modulo oMod = null;
		PlatEstadoMovimientoCuenta oEst = null;
		
		SimpleDateFormat formato = new SimpleDateFormat("yyyy-MM-dd");
		
		this.filtroCampo = null;
		this.filtroValor = null;

		if (!filters.isEmpty()) {
			try {
				for (Map.Entry<String, FilterMeta> oProp : filters.entrySet()) {
					Class<?> xtype = PropertyUtils.getPropertyType(itmMovCta, oProp.getKey());
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
						PropertyUtils.setProperty(itmMovCta, oProp.getKey(), null);
						this.filtroCampo = mapearCampoFiltro(oProp.getKey());
						this.filtroValor = null;
					} else if(propBoolean) {
						PropertyUtils.setProperty(itmMovCta, oProp.getKey(), oVal);
						this.filtroCampo = mapearCampoFiltro(oProp.getKey());
						if (oVal != null && (Boolean)oVal == true) {
							this.filtroValor = "1";
						} else {
							this.filtroValor = null;
						}
					} else if(propDate) {
						PropertyUtils.setProperty(itmMovCta, oProp.getKey(), oVal);
						this.filtroCampo = mapearCampoFiltro(oProp.getKey());
						if (oVal != null) {
							this.filtroValor = formato.format((Date)oVal);
						} else {
							this.filtroValor = null;
						}
					}
					else {
						PropertyUtils.setProperty(itmMovCta, oProp.getKey(), oVal);
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
				
				if(itmMovCta.getIdMovimientoCuenta() != null) {
					oMovCta.setIdMovimientoCuenta(oGestionObjFac.createPlatMovimientoCuentaIdMovimientoCuenta(itmMovCta.getIdMovimientoCuenta()));
				}	
				else if(itmMovCta.getCuenta() != null && itmMovCta.getCuenta().getIdCuenta() != null && itmMovCta.getCuenta().getIdCuenta().compareTo(0L) > 0) {
					oCta = new PlatCuenta();
					oCta.setIdCuenta(oGestionObjFac.createPlatCuentaIdCuenta(itmMovCta.getCuenta().getIdCuenta()));
					oMovCta.setCuenta(oGestionObjFac.createPlatMovimientoCuentaCuenta(oCta));
				}	
				else if(itmMovCta.getCuenta() != null && itmMovCta.getCuenta().getNumeroCuenta() != null) {
					oCta = new PlatCuenta();
					oCta.setNumeroCuenta(oGestionObjFac.createPlatCuentaNumeroCuenta(itmMovCta.getCuenta().getNumeroCuenta()));
					oMovCta.setCuenta(oGestionObjFac.createPlatMovimientoCuentaCuenta(oCta));
				}	
				else if(itmMovCta.getCuenta() != null && itmMovCta.getCuenta().getCVU() != null) {
					oCta = new PlatCuenta();
					oCta.setCVU(oGestionObjFac.createPlatCuentaCVU(itmMovCta.getCuenta().getCVU()));
					oMovCta.setCuenta(oGestionObjFac.createPlatMovimientoCuentaCuenta(oCta));
				}	
				else if(itmMovCta.getCuenta() != null && itmMovCta.getCuenta().getAlias() != null) {
					oCta = new PlatCuenta();
					oCta.setAlias(oGestionObjFac.createPlatCuentaAlias(itmMovCta.getCuenta().getAlias()));
					oMovCta.setCuenta(oGestionObjFac.createPlatMovimientoCuentaCuenta(oCta));
				}	
				else if(itmMovCta.getCuenta() != null && itmMovCta.getCuenta().getAlias() != null) {
					oCta = new PlatCuenta();
					oCta.setAlias(oGestionObjFac.createPlatCuentaAlias(itmMovCta.getCuenta().getAlias()));
					oMovCta.setCuenta(oGestionObjFac.createPlatMovimientoCuentaCuenta(oCta));
				}	
				else if(itmMovCta.getCuenta() != null && itmMovCta.getCuenta().getIdMoneda() != null && itmMovCta.getCuenta().getIdMoneda().compareTo(0L) > 0) {
					oCta = new PlatCuenta();
					oMoneda = new PlatMoneda();
					oMoneda.setIdMoneda(oGestionObjFac.createPlatMonedaIdMoneda(itmMovCta.getCuenta().getIdMoneda()));
					oCta.setMoneda(oGestionObjFac.createPlatCuentaMoneda(oMoneda));
					
					oMovCta.setCuenta(oGestionObjFac.createPlatMovimientoCuentaCuenta(oCta));
				}	
				else if(itmMovCta.getTipoMovimiento() != null && itmMovCta.getTipoMovimiento().getIdTipoMovimiento() != null && itmMovCta.getTipoMovimiento().getIdTipoMovimiento().compareTo(0L) > 0) {
					oTipMov = new PlatTipoMovimiento();
					oTipMov.setIdPlatTipoMovimiento(oGestionObjFac.createPlatTipoMovimientoIdPlatTipoMovimiento(itmMovCta.getTipoMovimiento().getIdTipoMovimiento()));
					oMovCta.setTipoMovimiento(oGestionObjFac.createPlatMovimientoCuentaTipoMovimiento(oTipMov));
				}	
				else if(itmMovCta.getTipoMovimiento() != null && itmMovCta.getTipoMovimiento().getCodMnemonico() != null && !itmMovCta.getTipoMovimiento().getCodMnemonico().isEmpty()) {
					oTipMov = new PlatTipoMovimiento();
					oTipMov.setCodMnemonico(oGestionObjFac.createPlatTipoMovimientoCodMnemonico(itmMovCta.getTipoMovimiento().getCodMnemonico()));
					oMovCta.setTipoMovimiento(oGestionObjFac.createPlatMovimientoCuentaTipoMovimiento(oTipMov));
				}	
				else if(itmMovCta.getProductoOrigen() != null && itmMovCta.getProductoOrigen().getIdProducto() != null && itmMovCta.getProductoOrigen().getIdProducto().compareTo(0L) > 0) {
					oPrd = new com.americacg.cargavirtual.gestion.model.Producto();
					oPrd.setIdProducto(oGestionObjFac.createProductoIdProducto(itmMovCta.getProductoOrigen().getIdProducto()));
					
					oMovCta.setProductoOrigen(oGestionObjFac.createPlatMovimientoCuentaProductoOrigen(oPrd));
				}	
				else if(itmMovCta.getProductoOrigen() != null && itmMovCta.getProductoOrigen().getCodMnemonico() != null && !itmMovCta.getProductoOrigen().getCodMnemonico().isEmpty()) {
					oPrd = new com.americacg.cargavirtual.gestion.model.Producto();
					oPrd.setCodMnemonico(oGestionObjFac.createProductoCodMnemonico(itmMovCta.getProductoOrigen().getCodMnemonico()));
					
					oMovCta.setProductoOrigen(oGestionObjFac.createPlatMovimientoCuentaProductoOrigen(oPrd));
				}	
				else if(itmMovCta.getModuloOrigen() != null && itmMovCta.getModuloOrigen().getIdModulo() != null && itmMovCta.getModuloOrigen().getIdModulo().compareTo(0L) > 0) {
					oMod = new com.americacg.cargavirtual.gestion.model.Modulo();
					oMod.setIdModulo(oGestionObjFac.createModuloIdModulo(itmMovCta.getModuloOrigen().getIdModulo()));
					
					oMovCta.setModuloOrigen(oGestionObjFac.createPlatMovimientoCuentaModuloOrigen(oMod));
				}	
				else if(itmMovCta.getModuloOrigen() != null && itmMovCta.getModuloOrigen().getCodigoModulo() != null && !itmMovCta.getModuloOrigen().getCodigoModulo().isEmpty()) {
					oMod = new com.americacg.cargavirtual.gestion.model.Modulo();
					oMod.setCodigoModulo(oGestionObjFac.createModuloCodigoModulo(itmMovCta.getModuloOrigen().getCodigoModulo()));
					
					oMovCta.setModuloOrigen(oGestionObjFac.createPlatMovimientoCuentaModuloOrigen(oMod));
				}	
				else if(itmMovCta.getIdOrigen() != null && itmMovCta.getIdOrigen().compareTo(0L) > 0) {
					oMovCta.setIdOrigen(oGestionObjFac.createPlatMovimientoCuentaIdOrigen(itmMovCta.getIdOrigen()));
				}	
				else if(itmMovCta.getEstado() != null && itmMovCta.getEstado().getIdEstadoMovimientoCuenta() != null && itmMovCta.getEstado().getIdEstadoMovimientoCuenta().compareTo(0L) > 0) {
					oEst = new PlatEstadoMovimientoCuenta();
					oEst.setIdEstadoMovimientoCuenta(oGestionObjFac.createPlatEstadoMovimientoCuentaIdEstadoMovimientoCuenta(itmMovCta.getEstado().getIdEstadoMovimientoCuenta()));
					
					oMovCta.setEstado(oGestionObjFac.createPlatMovimientoCuentaEstado(oEst));
				}	
				else if(itmMovCta.getEstado() != null && itmMovCta.getEstado().getCodMnemonico() != null && !itmMovCta.getEstado().getCodMnemonico().isEmpty()) {
					oEst = new PlatEstadoMovimientoCuenta();
					oEst.setCodMnemonico(oGestionObjFac.createPlatEstadoMovimientoCuentaCodMnemonico(itmMovCta.getEstado().getCodMnemonico()));
					
					oMovCta.setEstado(oGestionObjFac.createPlatMovimientoCuentaEstado(oEst));
				}	

			} catch (Exception e) {
				LogACGHelper.escribirLog(null,
						"Se produjo un error LazyItemTransferenciaDataModel metodo load (ROL). Msg:|" + e.getMessage() + "|");
			}
		}
		
		return oMovCta;
	}

	private String mapearCampoFiltro(String campo) {
		String field = "";

		switch (campo.toLowerCase()) {
		case "idMovimientoCuenta":
			field = "idMovimientoCuenta";
			break;
		case "importe":
			field = "importe";
			break;
			
		default:
			field = "idMovimientoCuenta";
			break;
		}
		
		return field;
	}

	private String sortFieldSwitch(String sortField) {
		String field = "";

		switch (sortField.toLowerCase()) {
		case "idMovimientoCuenta":
			field = "idMovimientoCuenta";
			break;
		case "fechaAcreditacion":
			field = "fechaAcreditacion";
			break;
		case "fechaMovimiento":
			field = "fechaMovimiento";
			break;
		default:
			field = "idMovimientoCuenta";
			break;
		}
		
		return field;
	}
}