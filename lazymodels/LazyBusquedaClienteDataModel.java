package com.americacg.cargavirtual.web.lazymodels;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.apache.commons.beanutils.PropertyUtils;
import org.primefaces.model.FilterMeta;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortMeta;
import com.americacg.cargavirtual.gestion.model.ClienteReducido;
import com.americacg.cargavirtual.gestion.model.ClienteReducidoList;
import com.americacg.cargavirtual.web.enums.CfgTimeout;
import com.americacg.cargavirtual.web.helpers.GestionServiceHelper;
import com.americacg.cargavirtual.web.helpers.LogACGHelper;
import com.americacg.cargavirtual.web.model.ItemArbol;

public class LazyBusquedaClienteDataModel extends LazyDataModel<ItemArbol> {
	/**
	 * 
	 */
	private static final long serialVersionUID = -6861528870671668752L;

	private List<ItemArbol> datasource;

	private Long idCliente;
	private Long idMayorista;
	private Integer nivelClienteSuperior;
	private String campoOrdenamiento = "idCliente";
	private String tipoOrdenamiento = "asc";

	private Long provinciaFlt = null;
	private Long localidadFlt = null;
	private Long tipoTerminalFlt = null;
	private String tipoClienteFlt = null;
	private Long idClienteFlt = null;
	private Long idUsuarioFlt = null;
	private String idAuxFlt = null;
	private String idAux2Flt = null;
	private Long idEntidadFlt = null;
	private String razonSocialFlt = "";
	private String nombreFantasiaFlt = "";
	private String usuarioFlt = "";
	private String cuitFlt = "";

	public LazyBusquedaClienteDataModel(Long idCliente, Long idMayorista, Integer nivelClienteSuperior,
			Long provinciaFlt, Long localidadFlt, Long tipoTerminalFlt, String tipoClienteFlt, Long idClienteFlt,
			Long idUsuarioFlt, String idAuxFlt, String idAux2Flt, Long idEntidadFlt, String razonSocialFlt,
			String nombreFantasiaFlt, String usuarioFlt, String cuitFlt, String campoOrdenamiento,
			String tipoOrdenamiento) {
		this.idCliente = idCliente;
		this.idMayorista = idMayorista;
		this.nivelClienteSuperior = nivelClienteSuperior;
		this.campoOrdenamiento = campoOrdenamiento;
		this.tipoOrdenamiento = tipoOrdenamiento;

		/* Filtros disponibles */
		this.provinciaFlt = provinciaFlt;
		this.localidadFlt = localidadFlt;
		this.tipoTerminalFlt = tipoTerminalFlt;
		this.tipoClienteFlt = tipoClienteFlt;
		this.idClienteFlt = idClienteFlt;
		this.idUsuarioFlt = idUsuarioFlt;
		this.idAuxFlt = idAuxFlt;
		this.idAux2Flt = idAux2Flt;
		this.idEntidadFlt = idEntidadFlt;
		this.razonSocialFlt = razonSocialFlt;
		this.nombreFantasiaFlt = nombreFantasiaFlt;
		this.usuarioFlt = usuarioFlt;
		this.cuitFlt = cuitFlt;
	}

	@Override
	public ItemArbol getRowData(String rowKey) {
		ItemArbol ia = new ItemArbol();
		ia.setIdMayorista(this.idMayorista);
		ia.setIdCliente(Long.parseLong(rowKey));
		return ia;
	}

	@Override
	public String getRowKey(ItemArbol itemArbol) {
		return itemArbol.getIdCliente().toString();
	}

	@Override
	public List<ItemArbol> load(int first, int pageSize, Map<String, SortMeta> sortBy, Map<String, FilterMeta> filterBy){		

		datasource = new ArrayList<ItemArbol>();

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

			ClienteReducidoList oCRLst = GestionServiceHelper.getGestionService(CfgTimeout.DEFAULT).buscarClientePaginado(
					this.idMayorista, this.idCliente, this.nivelClienteSuperior, this.idClienteFlt, this.razonSocialFlt,
					this.nombreFantasiaFlt, this.tipoTerminalFlt, this.tipoClienteFlt, this.idUsuarioFlt,
					this.usuarioFlt, this.idAuxFlt, this.idAux2Flt, this.provinciaFlt, this.localidadFlt,
					this.idEntidadFlt, this.cuitFlt, this.campoOrdenamiento, this.tipoOrdenamiento, (Integer) first,
					(Integer) pageSize);

			if (oCRLst != null && oCRLst.getListaClientes() != null && oCRLst.getListaClientes().getValue() != null
					&& oCRLst.getListaClientes().getValue().getClienteReducido() != null) {
				for (ClienteReducido cr : oCRLst.getListaClientes().getValue().getClienteReducido()) {

					ItemArbol ia = new ItemArbol();
					ia.setIdCliente(cr.getIdCliente().getValue());
					ia.setIdMayorista(this.idMayorista);
					ia.setNivelDistribuidorSuperior(cr.getNivelDistribuidorSuperior().getValue());
					ia.setIdTipoTerminal(cr.getIdTipoTerminal().getValue());
					ia.setTipoCliente(cr.getTipoCliente().getValue());
					ia.setDescTipoTerminal(cr.getDescTipoTerminal().getValue());
					ia.setEstado(cr.getEstado().getValue());
					ia.setRazonSocial(cr.getRazonSocial().getValue());
					ia.setNombreFantasia(cr.getNombreFantasia().getValue());

					if ("IDCLIENTE".equals(this.campoOrdenamiento.toUpperCase().trim())
							|| "RAZONSOCIAL".equals(this.campoOrdenamiento.toUpperCase().trim()))
						ia.setTitulo(cr.getRazonSocial().getValue());
					else
						ia.setTitulo(cr.getNombreFantasia().getValue());

					datasource.add(ia);
				}

				this.setRowCount(oCRLst.getTotalFilas().getValue());
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
		ItemArbol itemArbol = new ItemArbol();

		try {
			for (Map.Entry<String, FilterMeta> oProp : filters.entrySet()) {
				Class<?> xtype = PropertyUtils.getPropertyType(itemArbol, oProp.getKey());
				Object value = oProp.getValue().getFilterValue();

				Object oVal = null;

				if (value != null) {
					propNroEmpty = false;
					if (Integer.class.isAssignableFrom(xtype)) {
						propNro = true;
						try {
							oVal = Integer.valueOf(value.toString()).intValue();
							if (((Integer) oVal).compareTo(-1) == 0)
								propNroEmpty = true;
						} catch (Exception e) {
							propNroEmpty = true;
							oVal = BigDecimal.ZERO;
						}
					} else if (Double.class.isAssignableFrom(xtype)) {
						propNro = true;
						try {
							oVal = Double.valueOf(value.toString()).doubleValue();
							if (((Double) oVal).compareTo(-1d) == 0)
								propNroEmpty = true;
						} catch (Exception e) {
							propNroEmpty = true;
							oVal = BigDecimal.ZERO;
						}
					} else if (Long.class.isAssignableFrom(xtype)) {
						propNro = true;
						try {
							oVal = Long.valueOf(value.toString()).longValue();
							if (((Long) oVal).compareTo(-1L) == 0)
								propNroEmpty = true;
						} catch (Exception e) {
							propNroEmpty = true;
							oVal = BigDecimal.ZERO;
						}
					} else if (Float.class.isAssignableFrom(xtype)) {
						propNro = true;
						try {
							oVal = Float.valueOf(value.toString()).floatValue();
							if (((Float) oVal).compareTo(-1f) == 0)
								propNroEmpty = true;
						} catch (Exception e) {
							propNroEmpty = true;
							oVal = BigDecimal.ZERO;
						}
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

				if (oVal != null) {
					if (propNro && propNroEmpty) {
						switch (oProp.getKey().toUpperCase()) {
						case "IDCLIENTE": {
							this.idClienteFlt = null;
							break;
						}
						case "TIPOCLIENTE": {
							this.tipoClienteFlt = null;
							break;
						}
						case "IDTIPOTERMINAL": {
							this.tipoTerminalFlt = null;
							break;
						}
						case "RAZONSOCIAL": {
							this.razonSocialFlt = null;
							break;
						}
						case "NOMBREFANTASIA": {
							this.nombreFantasiaFlt = null;
							break;
						}
						}
					} else {
						switch (oProp.getKey().toUpperCase()) {
						case "IDCLIENTE": {
							this.idClienteFlt = (Long) oVal;
							break;
						}
						case "TIPOCLIENTE": {
							this.tipoClienteFlt = (String) oVal;
							break;
						}
						case "IDTIPOTERMINAL": {
							this.tipoTerminalFlt = (Long) oVal;
							break;
						}
						case "RAZONSOCIAL": {
							this.razonSocialFlt = (String) oVal;
							break;
						}
						case "NOMBREFANTASIA": {
							this.nombreFantasiaFlt = (String) oVal;
							break;
						}
						}
					}
				}
			}

		} catch (Exception e) {
			LogACGHelper.escribirLog(null,
					"Se produjo un error LazyBusquedaClienteDataModel metodo load (ROL). Msg:|" + e.getMessage() + "|");
		}
	}
	
	private String sortFieldSwitch(String sortField) {

		String field = "idCliente";
		
		switch (sortField.toLowerCase()) {
		case "idcliente":
			field = "idCliente";
			break;
		case "razonsocial":
			field = "razonSocial";
			break;
		case "nombrefantasia":
			field = "nombreFantasia";
			break;
		case "tipocliente":
			field = "tipoCliente";
			break;
		case "idtipoterminal":
			field = "idTipoTerminal";
			break;
		default:
			LogACGHelper.escribirLog(null, "Consulta LazyBusquedaClienteDataModel. sortField:|"+ sortField + "| analizar como enviarlo a la consulta.");
			field = "idCliente";
			break;
		}

		return field;
	}

}
