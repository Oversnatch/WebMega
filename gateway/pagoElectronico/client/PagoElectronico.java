package com.americacg.cargavirtual.gateway.pagoElectronico.client;

import java.util.HashMap;
import java.util.Map;

import com.americacg.cargavirtual.gateway.pagoElectronico.enums.ConfigParam;
import com.americacg.cargavirtual.gateway.pagoElectronico.enums.FuncionParam;
import com.americacg.cargavirtual.gateway.pagoElectronico.model.ParametrosServicio;
import com.americacg.cargavirtual.gateway.pagoElectronico.model.VariableParametro;
import com.americacg.cargavirtual.gateway.pagoElectronico.model.parametros.SeparadorTrama;
import com.americacg.cargavirtual.gestion.model.ParametrosList;
import com.americacg.cargavirtual.web.enums.CfgTimeout;
import com.americacg.cargavirtual.web.helpers.GestionServiceHelper;

public class PagoElectronico {

	private static final char CHR_INICIO_TRAMA = (char) 2;
	private static final char CHR_FIN_TRAMA = (char) 3;
	private static final char CHR_SEPARADOR_TRAMA = (char) 4;
	private static final char CHR_SEPARADOR_HEADER_DATA = (char) 5;
	private static final char CHR_INICIO_VECTOR = (char) 17;
	private static final char CHR_FIN_VECTOR = (char) 18;
	private static final char CHR_SEPARADOR_ITEM = (char) 19;
	private static final char CHR_INICIO_TICKET = (char) 28;
	private static final char CHR_FIN_TICKET = (char) 29;

	private Map<FuncionParam, Object> parametros = null;
	private ParametrosServicio parametrosServicio = null;

	private Map<ConfigParam, Object> parametrosConfiguracion = new HashMap<ConfigParam, Object>(0);
	private SeparadorTrama oSeparadorTrama = null;

	
	public PagoElectronico(Long usuarioIdMayorista, Long usuarioIdCliente, Long usuarioIdUsuario,
			String usuarioUsername, String usuarioPassword, String usuarioTipoCliente, Boolean debug) {

		parametrosConfiguracion.clear();
		parametrosConfiguracion.put(ConfigParam.INICIO_TRAMA, CHR_INICIO_TRAMA);
		parametrosConfiguracion.put(ConfigParam.FIN_TRAMA, CHR_FIN_TRAMA);
		parametrosConfiguracion.put(ConfigParam.SEPARADOR_HEADER_DATA, CHR_SEPARADOR_HEADER_DATA);
		parametrosConfiguracion.put(ConfigParam.SEPARADOR_TRAMA, CHR_SEPARADOR_TRAMA);
		parametrosConfiguracion.put(ConfigParam.INICIO_VECTOR, CHR_INICIO_VECTOR);
		parametrosConfiguracion.put(ConfigParam.FIN_VECTOR, CHR_FIN_VECTOR);
		parametrosConfiguracion.put(ConfigParam.SEPARADOR_ITEM, CHR_SEPARADOR_ITEM);
		parametrosConfiguracion.put(ConfigParam.INICIO_TICKET, CHR_INICIO_TICKET);
		parametrosConfiguracion.put(ConfigParam.FIN_TICKET, CHR_FIN_TICKET);		

		try {
			oSeparadorTrama = new SeparadorTrama(parametrosConfiguracion);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		if (parametros == null) {
			this.parametros = new HashMap<FuncionParam, Object>(0);
		}

		parametros.put(FuncionParam.SEPARADORTRAMA, oSeparadorTrama);
		parametros.put(FuncionParam.OMITIRSEPARADORES, false);
		parametros.put(FuncionParam.DEBUG, debug);
		parametros.put(FuncionParam.VERSIONGATEWAY, "1");
		parametros.put(FuncionParam.HEADERINGATEWAY, false);
		parametros.put(FuncionParam.TIPOCLIENTE, usuarioTipoCliente);
		parametros.put(FuncionParam.IDMAYORISTA, usuarioIdMayorista);
		parametros.put(FuncionParam.IDCLIENTE, usuarioIdCliente);

		if (this.parametrosServicio == null) {
			this.parametrosServicio = new ParametrosServicio();
			if (this.parametrosServicio.getParametros() == null) {
				this.parametrosServicio.setParametros(new HashMap<String, VariableParametro>());
			}
		}

		VariableParametro vp = new VariableParametro();
		vp.setNombre("IpGateway");
		vp.setValor("");
		ParametrosList lp = GestionServiceHelper.getGestionService(CfgTimeout.DEFAULT)
				.mostrarParametros(usuarioIdMayorista, "P", "gpe_ip_intra");
		if (lp != null && lp.getListParametros().getValue().getParametro() != null
				&& lp.getListParametros().getValue().getParametro().size() == 1) {
			vp.setValor(lp.getListParametros().getValue().getParametro().get(0).getValor().getValue());
		}

		this.parametrosServicio.getParametros().put("IpGateway", vp);

		vp = new VariableParametro();
		vp.setNombre("DnsGateway");
		vp.setValor("");
		lp = GestionServiceHelper.getGestionService(CfgTimeout.DEFAULT).mostrarParametros(usuarioIdMayorista, "P",
				"gpe_dns_intra");
		if (lp != null && lp.getListParametros().getValue().getParametro() != null
				&& lp.getListParametros().getValue().getParametro().size() == 1) {
			vp.setValor((String) lp.getListParametros().getValue().getParametro().get(0).getValor().getValue());
		}
		this.parametrosServicio.getParametros().put("DnsGateway", vp);
		
		vp = new VariableParametro();
		vp.setNombre("PuertoGateway");
		vp.setValor("0");
		lp = GestionServiceHelper.getGestionService(CfgTimeout.DEFAULT).mostrarParametros(usuarioIdMayorista, "P",
				"gpe_puerto_intra");
		if (lp != null && lp.getListParametros().getValue().getParametro() != null
				&& lp.getListParametros().getValue().getParametro().size() == 1) {
			vp.setValor((String) lp.getListParametros().getValue().getParametro().get(0).getValor().getValue());
		}
		this.parametrosServicio.getParametros().put("PuertoGateway", vp);

		vp = new VariableParametro();
		vp.setNombre("TimeoutConnGateway");
		vp.setValor("0");
		lp = GestionServiceHelper.getGestionService(CfgTimeout.DEFAULT).mostrarParametros(usuarioIdMayorista, "P",
				"gpe_timeOut_conexion_ms");
		if (lp != null && lp.getListParametros().getValue().getParametro() != null
				&& lp.getListParametros().getValue().getParametro().size() == 1) {
			vp.setValor((String) lp.getListParametros().getValue().getParametro().get(0).getValor().getValue());
		}
		this.parametrosServicio.getParametros().put("TimeoutConnGateway", vp);

		vp = new VariableParametro();
		vp.setNombre("TimeoutRWGateway");
		vp.setValor("0");
		lp = GestionServiceHelper.getGestionService(CfgTimeout.DEFAULT).mostrarParametros(usuarioIdMayorista, "P",
				"gpe_timeOut_rw_ms");
		if (lp != null && lp.getListParametros().getValue().getParametro() != null
				&& lp.getListParametros().getValue().getParametro().size() == 1) {
			vp.setValor((String) lp.getListParametros().getValue().getParametro().get(0).getValor().getValue());
		}
		this.parametrosServicio.getParametros().put("TimeoutRWGateway", vp);
		
		vp = new VariableParametro();
		vp.setNombre("REGEX_CARD_BAND_PARSE");
		vp.setValor("");
		lp = GestionServiceHelper.getGestionService(CfgTimeout.DEFAULT)
				.mostrarParametros(usuarioIdMayorista, "E", "regex_card_band_parse");
		if (lp != null && lp.getListParametros().getValue().getParametro() != null
				&& lp.getListParametros().getValue().getParametro().size() == 1) {
			vp.setValor(lp.getListParametros().getValue().getParametro().get(0).getValor().getValue());
		}

		this.parametrosServicio.getParametros().put("REGEX_CARD_BAND_PARSE", vp);
	}

	public ParametrosServicio getParametrosServicio() {
		return parametrosServicio;
	}

	public void setParametrosServicio(ParametrosServicio parametrosServicio) {
		this.parametrosServicio = parametrosServicio;
	}

	public Map<FuncionParam, Object> getParametros() {
		return parametros;
	}

	public void setParametros(Map<FuncionParam, Object> parametros) {
		this.parametros = parametros;
	}
}
