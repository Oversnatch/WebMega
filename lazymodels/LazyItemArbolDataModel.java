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
import com.americacg.cargavirtual.gestion.model.JerarquiaList;
import com.americacg.cargavirtual.web.enums.CfgTimeout;
import com.americacg.cargavirtual.web.helpers.GestionServiceHelper;
import com.americacg.cargavirtual.web.helpers.LogACGHelper;
import com.americacg.cargavirtual.web.model.ItemArbol;

public class LazyItemArbolDataModel extends LazyDataModel<ItemArbol> {
	/**
	 * 
	 */
	private static final long serialVersionUID = 3780015991880104269L;
	private List<ItemArbol> datasource;

	private Long idCliente;
	private Long idMayorista;
	private Integer nivelDistribuidorSuperior;
	private String campoOrdenamiento = "idCliente";
	private String tipoOrdenamiento = "asc";
	private String filtroCampo = null;
	private String filtroValor = null;

	public LazyItemArbolDataModel(Long idCliente, Long idMayorista, Integer nivelDistribuidorSuperior,
			String campoOrdenamiento, String tipoOrdenamiento) {
		this.idCliente = idCliente;
		this.idMayorista = idMayorista;
		this.nivelDistribuidorSuperior = nivelDistribuidorSuperior;
		this.campoOrdenamiento = campoOrdenamiento;
		this.tipoOrdenamiento = tipoOrdenamiento;
	}

	@Override
	public ItemArbol getRowData(String rowKey) {
		/* Parche para poder determinar el item del arbol */
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
	public List<ItemArbol> load(int first, int pageSize, Map<String, SortMeta> sortBy,
			Map<String, FilterMeta> filterBy) {
		datasource = new ArrayList<ItemArbol>();
		boolean propNroEmpty = false;
		boolean propNro = false;
		ItemArbol itemArbol = new ItemArbol();

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
		
		this.filtroCampo = null;
		this.filtroValor = null;

		if (!filterBy.isEmpty()) {
			try {
				for (Map.Entry<String, FilterMeta> oProp : filterBy.entrySet()) {
					Class<?> xtype = PropertyUtils.getPropertyType(itemArbol, oProp.getKey());
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
						PropertyUtils.setProperty(itemArbol, oProp.getKey(), null);
						this.filtroCampo = oProp.getKey();
						this.filtroValor = null;
					} else {
						PropertyUtils.setProperty(itemArbol, oProp.getKey(), oVal);
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
						"Se produjo un error LazyItemArbolDataModel metodo load (ROL). Msg:|" + e.getMessage() + "|");
			}
		}

		try {

			JerarquiaList aojC = GestionServiceHelper.getGestionService(CfgTimeout.DEFAULT).traerJerarquiaPaginado(this.idCliente,
					this.idMayorista, this.campoOrdenamiento, this.tipoOrdenamiento, (Integer) first,
					(Integer) pageSize, this.filtroCampo, this.filtroValor);

			if (aojC != null && aojC.getListaClientes() != null && aojC.getListaClientes().getValue() != null
					&& aojC.getListaClientes().getValue().getClienteReducido() != null) {
				for (ClienteReducido cr : aojC.getListaClientes().getValue().getClienteReducido()) {

					ItemArbol ia = new ItemArbol();
					ia.setIdCliente(cr.getIdCliente().getValue());
					ia.setIdMayorista(this.idMayorista);
					ia.setNivelDistribuidorSuperior(cr.getNivelDistribuidorSuperior().getValue());
					ia.setIdTipoTerminal(cr.getIdTipoTerminal().getValue());
					ia.setTipoCliente(cr.getTipoCliente().getValue());

					if ("IDCLIENTE".equals(this.campoOrdenamiento.toUpperCase().trim())
							|| "RAZONSOCIAL".equals(this.campoOrdenamiento.toUpperCase().trim()))
						ia.setTitulo(cr.getRazonSocial().getValue());
					else
						ia.setTitulo(cr.getNombreFantasia().getValue());

					datasource.add(ia);
				}
				this.setRowCount(aojC.getTotalFilas().getValue());
			} else {
				this.setRowCount(0);
			}

			return datasource;
		} catch (Exception ex) {
			return null;
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
		default:
			LogACGHelper.escribirLog(null, "Consulta LazyItemArbolDataModel. sortField:|"+ sortField + "| analizar como enviarlo a la consulta.");			
			field = "idCliente";
			break;
		}

		return field;
	}

}
