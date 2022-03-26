package com.americacg.cargavirtual.web.lazymodels;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.apache.commons.beanutils.PropertyUtils;
import org.primefaces.model.FilterMeta;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortMeta;
import com.americacg.cargavirtual.gestion.model.CabeceraUsuarioList;
import com.americacg.cargavirtual.gestion.model.CabeceraUsuarios;
import com.americacg.cargavirtual.web.enums.CfgTimeout;
import com.americacg.cargavirtual.web.helpers.GestionServiceHelper;
import com.americacg.cargavirtual.web.helpers.LogACGHelper;
import com.americacg.cargavirtual.web.model.CabeceraUsuario;

public class LazyCabeceraUsuarioDataModel extends LazyDataModel<CabeceraUsuario> {
	/**
	 * 
	 */
	private static final long serialVersionUID = 5098879267133957975L;
	private List<CabeceraUsuario> datasource;
	private Long idMayorista;
	private Long idCliente;
	private String campoOrdenamiento = "idCliente";
	private String tipoOrdenamiento = "asc";
	private String filtroCampo = null;
	private String filtroValor = null;

	public LazyCabeceraUsuarioDataModel(Long idCliente, Long idMayorista, Integer nivelClienteSuperior,
			String campoOrdenamiento, String tipoOrdenamiento) {
		this.idMayorista = idMayorista;
		this.idCliente = idCliente;
		this.campoOrdenamiento = campoOrdenamiento;
		this.tipoOrdenamiento = tipoOrdenamiento;
	}

	@Override
	public CabeceraUsuario getRowData(String rowKey) {
		if (datasource != null) {
			for (CabeceraUsuario cabeceraUsuario : datasource) {
				if (cabeceraUsuario.getIdUsuario().toString().equals(rowKey))
					return cabeceraUsuario;
			}
		}

		return null;
	}

	@Override
	public String getRowKey(CabeceraUsuario cabeceraUsuario) {
		return cabeceraUsuario.getIdUsuario().toString();
	}

	@Override
	public List<CabeceraUsuario> getWrappedData() {
		// TODO Auto-generated method stub
		return super.getWrappedData();
	}

	@Override
	public List<CabeceraUsuario> load(int first, int pageSize, Map<String, SortMeta> sortBy,
			Map<String, FilterMeta> filterBy) {

		datasource = new ArrayList<CabeceraUsuario>();

		try {
			if (!filterBy.isEmpty()) {
				this.asignarFiltros(filterBy);
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
			
			CabeceraUsuarioList oCULst = GestionServiceHelper.getGestionService(CfgTimeout.DEFAULT).obtenerCabeceraUsuarioPaginado(idCliente, 
					idMayorista, 
					campoOrdenamiento, 
					tipoOrdenamiento, 
					(Integer) first, 
					(Integer) pageSize, 
					filtroCampo, 
					filtroValor);
			
			if (oCULst != null && oCULst.getListaCabeceraUsuarios() != null && oCULst.getListaCabeceraUsuarios().getValue() != null
					&& oCULst.getListaCabeceraUsuarios().getValue().getCabeceraUsuarios() != null) {
				for (CabeceraUsuarios cu : oCULst.getListaCabeceraUsuarios().getValue().getCabeceraUsuarios()) {

					CabeceraUsuario cutmp = new CabeceraUsuario();
					cutmp.setIdUsuario(cu.getIdUsuario().getValue());
					cutmp.setUsuario(cu.getUsuario().getValue());
					cutmp.setApellido(cu.getApellido().getValue());
					cutmp.setNombre(cu.getNombre().getValue());
					cutmp.setEstado(cu.getEstado().getValue());
					cutmp.setSoloInformes(cu.getSoloInformes().getValue());
					cutmp.setSuperUsuario(cu.getSuperUsuario().getValue());
					
					datasource.add(cutmp);
				}

				this.setRowCount(oCULst.getTotalFilas().getValue());
			} else {
				this.setRowCount(0);
			}

			return datasource;
		} catch (Exception ex) {
			return null;
		}
	}

	private void asignarFiltros(Map<String, FilterMeta> filters) {
		boolean propNroEmpty = false;
		boolean propNro = false;
		boolean propBoolean = false;
		CabeceraUsuario cabeceraUsuario = new CabeceraUsuario();

		this.filtroCampo = null;
		this.filtroValor = null;

		if (!filters.isEmpty()) {
			try {
				for (Map.Entry<String, FilterMeta> oProp : filters.entrySet()) {
					Class<?> xtype = PropertyUtils.getPropertyType(cabeceraUsuario, oProp.getKey());
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
							oVal = value;
						}
					}

					if (propNro && propNroEmpty) {
						PropertyUtils.setProperty(cabeceraUsuario, oProp.getKey(), null);
						this.filtroCampo = oProp.getKey();
						this.filtroValor = null;
					} else if(propBoolean) {
						PropertyUtils.setProperty(cabeceraUsuario, oProp.getKey(), oVal);
						this.filtroCampo = oProp.getKey();
						if (oVal != null && (Boolean)oVal == true) {
							this.filtroValor = "1";
						} else {
							this.filtroValor = null;
						}
					}
					else {
						PropertyUtils.setProperty(cabeceraUsuario, oProp.getKey(), oVal);
						this.filtroCampo = oProp.getKey();
						if (oVal != null) {
							this.filtroValor = String.valueOf(oVal);
						} else {
							this.filtroValor = null;
						}
					}
					
					if(this.filtroValor != null && !this.filtroValor.isEmpty())
						break;
				}

			} catch (Exception e) {
				LogACGHelper.escribirLog(null,
						"Se produjo un error LazyCabeceraUsuarioDataModel metodo load (ROL). Msg:|" + e.getMessage() + "|");
			}
		}
	}
	
	private String sortFieldSwitch(String sortField) {

		String field = "idCliente";

		switch (sortField.toLowerCase()) {
		case "idusuario":
			field = "idUsuario";
			break;
		case "usuario":
			field = "usuario";
			break;
		case "nombre":
			field = "nombre";
			break;
		case "apellido":
			field = "apellido";
			break;
		case "estado":
			field = "estado";
			break;	
		case "soloInformes":
			field = "soloInformes";
			break;	
		default:
			LogACGHelper.escribirLog(null, "Consulta LazyCabeceraUsuarioDataModel. sortField:|"+ sortField + "| analizar como enviarlo a la consulta.");			
			field = "idUsuario";
			break;
		}
		return field;
	}
}