package com.americacg.cargavirtual.web.security;

import java.util.ArrayList;
import java.util.Collection;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.CredentialsExpiredException;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.session.SessionAuthenticationStrategy;
import com.americacg.cargavirtual.gestion.model.Componente;
import com.americacg.cargavirtual.gestion.model.Login;
import com.americacg.cargavirtual.gestion.model.ParametrosList;
import com.americacg.cargavirtual.gestion.model.SolicitudCoordenadas;
import com.americacg.cargavirtual.web.enums.CfgTimeout;
import com.americacg.cargavirtual.web.helpers.GestionServiceHelper;
import com.americacg.cargavirtual.web.helpers.LogACGHelper;
import com.americacg.cargavirtual.web.helpers.MayoristasHelper;
import com.americacg.cargavirtual.web.model.Usuario;
import com.americacg.cargavirtual.web.model.superPago.SuperPagoInstance;
import com.americacg.cargavirtual.web.shared.Constants;

@Named
public class ACGAuthenticationProvider implements AuthenticationProvider {
	@Autowired(required = false)
	private HttpServletRequest request;

	@Inject
	@Qualifier("sas")
	SessionAuthenticationStrategy strategy;

	@SuppressWarnings("unused")
	@Override
	public Authentication authenticate(final Authentication authentication) throws AuthenticationException {
		String ipCli = null;
		Boolean success = null;
		ACGAuthenticationDetails acgAuthenticationDetails = null;
		Login oLogin = null;
		Boolean usuarioNumerico = false;
		SolicitudCoordenadas oWsSolCoord = null;
		String coordenada1 = "";
		String coordenada2 = "";
		ACGUsernamePasswordAuthenticationToken resultAuthTokenTmp = null;
		HttpServletRequest httpReq = null;
		HttpServletResponse httpResp = null;
		Integer mostrarPantallasDeSube = 0;

		acgAuthenticationDetails = new ACGAuthenticationDetails(request);

		LogACGHelper.escribirLog(null,
				"---------------------------------------------------------------------------------------------------------");
		LogACGHelper.escribirLog(null,
				"Intento de Login." + " IPcli (real): |" + acgAuthenticationDetails.getRemoteAddress() + "|, usuario: |"
						+ authentication.getPrincipal().toString() + "|, clave: |"
						+ authentication.getCredentials().toString() + "|, Dominio: |"
						+ acgAuthenticationDetails.getDominio() + "|, Path: |" + acgAuthenticationDetails.getPath()
						+ "|, coordenada1: |" + acgAuthenticationDetails.getCoordenada1() + "|, coordenada2: |"
						+ acgAuthenticationDetails.getCoordenada2() + "|, ipInetAddress: |"
						+ acgAuthenticationDetails.getIpInetAddress() + "|");

		JSONObject mayorista = MayoristasHelper.getSingletonInstance()
				.obtenerMayorista(acgAuthenticationDetails.getDominio(), acgAuthenticationDetails.getPath());

		Long idMayorista = 0L;
		if (mayorista != null) {
			idMayorista = mayorista.getLong("idMayorista");
		} else {
			LogACGHelper.escribirLog(null, "El sistema no se encuentra correctamente configurado. idMayorista nulo.");
			throw new BadCredentialsException("El sistema no se encuentra correctamente configurado.");
		}

		// Valido Nulls
		if ((authentication.getPrincipal().toString() == null)
				|| (authentication.getPrincipal().toString().length() < 1) || (idMayorista == null)
				|| (idMayorista <= 0L) || (authentication.getCredentials().toString() == null)
				|| (authentication.getCredentials().toString().length() < 1)
				|| (acgAuthenticationDetails.getIpInetAddress() == null)
				|| (acgAuthenticationDetails.getIpInetAddress().length() < 1)) {

			LogACGHelper.escribirLog(null, "Alguno de los datos de la credencial de usuario es nulo.");
			throw new BadCredentialsException("Los datos ingresados no pueden ser vacios ni nulos.");
		}

		// Valido que el captcha no sea Null
		if ((acgAuthenticationDetails.getCaptcha() == null) || (acgAuthenticationDetails.getCaptcha().length() < 1)) {

			LogACGHelper.escribirLog(null, "Error Captcha informado vacio o nulo.");
			throw new BadCredentialsException("Los datos ingresados no pueden ser vacios ni nulos.");
		}

		// Valido el Captcha Personalizado
		success = validarCaptchaPersonalizado(acgAuthenticationDetails.getCaptcha());
		if (!success) {
			LogACGHelper.escribirLog(null, "Error Captcha informado invalido o vencido.");
			throw new BadCredentialsException("Captcha invalido o vencido.");
		}

		try {
			Integer.parseInt(authentication.getPrincipal().toString());
			usuarioNumerico = true;
		} catch (Exception e) {
			//LogACGHelper.escribirLog(null, "El usuario informado no es numerico.");
		}
		if (usuarioNumerico) {
			throw new BadCredentialsException("El usuario no puede ser numerico.");
		}

		// Consulto si el usuario existe y si requiere ingreso de coordenadas.
		oWsSolCoord = GestionServiceHelper.getGestionService(CfgTimeout.CONSULTA).usarTarjetaCoordenadas(idMayorista,
				authentication.getPrincipal().toString(), true);

		// Ejecuto Login

		if (oWsSolCoord != null) {
			if (oWsSolCoord.getError().getValue().getHayError().getValue()) {
				LogACGHelper.escribirLog(null, "Error Consultando Usuario para verificacion de Coordenadas: idMayorista:|" + idMayorista + "|, usuario:|" + authentication.getPrincipal().toString() + "|");
				throw new BadCredentialsException("Error Consultando Usuario para verificacion de Coordenadas");
			} else {

				LogACGHelper.escribirLog(null,
						"Validacion de Tarjeta de Coordenadas." + " IPcli (real): |" + acgAuthenticationDetails.getRemoteAddress() + "|, usuario: |"
								+ authentication.getPrincipal().toString() + "|, clave: |"
								+ authentication.getCredentials().toString() + "|, idMayorista: |" + idMayorista
								+ "|, coordenada1: |" + acgAuthenticationDetails.getCoordenada1() + "|, coordenada2: |"
								+ acgAuthenticationDetails.getCoordenada2() + "|, resultadoCaptcha: |" + success
								+ "|, ipInetAddress: |" + acgAuthenticationDetails.getIpInetAddress() + "|");
				LogACGHelper.escribirLog(null,
						"---------------------------------------------------------------------------------------------------------");

				if (oWsSolCoord.getUsarTarjetaCoordenadas().getValue() == 0) {
					coordenada1 = "";
					coordenada2 = "";
				} else {
					coordenada1 = acgAuthenticationDetails.getCoordenada1();
					coordenada2 = acgAuthenticationDetails.getCoordenada2();
				}
				oLogin = GestionServiceHelper.getGestionService(CfgTimeout.CONSULTA).loginConCoordenadas(
						authentication.getPrincipal().toString(), authentication.getCredentials().toString(),
						idMayorista, coordenada1, coordenada2);

			}
		} else {
			LogACGHelper.escribirLog(null, "Metodo usarTarjetaCoordenadas devolvio nulo.");
			throw new BadCredentialsException("Metodo usarTarjetaCoordenadas devolvio nulo.");
		}

		// Chequeo Loguin
		if (oLogin == null) {
			LogACGHelper.escribirLog(null, "Metodo loginConCoordenadas devolvio nulo.");
			throw new BadCredentialsException("El login devolvio un valor nulo.");
		}

		if (oLogin.getError().getValue().getHayError().getValue()) {
			LogACGHelper.escribirLog(null, "Error metodo login. Err: |" + oLogin.getError().getValue().getMsgError().getValue() +"|");
			throw new BadCredentialsException(oLogin.getError().getValue().getMsgError().getValue());
		}

		// Cargo los permisos
		Collection<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();

		
		if (oLogin.getSolicActualizacionDatosAlCliente().getValue()) {
			//ROL TEMPORAL PARA PODER ACTUALIZAR DATOS Y LUEGO RE LOGIN
			authorities.add(new SimpleGrantedAuthority("ROL_ACTUALIZARDATOS"));
		}else if ("D".equals(oLogin.getTipoCliente().getValue())) {
			authorities.add(new SimpleGrantedAuthority("ROL_DISTRIBUIDOR"));
		} else if ("M".equals(oLogin.getTipoCliente().getValue())) {
			authorities.add(new SimpleGrantedAuthority("ROL_MAYORISTA"));
		} else if ("P".equals(oLogin.getTipoCliente().getValue())) {
			authorities.add(new SimpleGrantedAuthority("ROL_PUNTOVENTA"));
		}

		authorities.add(new SimpleGrantedAuthority("ROLE_PLATAFORMA"));

		/*
		 * if (l.getCambiarClaveEnProximoIngreso() == 1){ listPermisos.add(new
		 * GrantedAuthorityImpl(Roles.ROL_CAMBIOCLAVE)); }else{ if
		 * ("D".equals(l.getTipoCliente())) { listPermisos.add(new
		 * GrantedAuthorityImpl(Roles.ROL_DISTRIBUIDOR)); } else if
		 * ("M".equals(l.getTipoCliente())){ listPermisos.add(new
		 * GrantedAuthorityImpl(Roles.ROL_MAYORISTA)); } else if
		 * ("P".equals(l.getTipoCliente())){ listPermisos.add(new
		 * GrantedAuthorityImpl(Roles.ROL_PUNTOVENTA)); }
		 * 
		 * if ("V".equals(l.getTipoUsuario())) { listPermisos.add(new
		 * GrantedAuthorityImpl(Roles.ROL_VENDEDOR)); } else if
		 * ("A".equals(l.getTipoUsuario())){ listPermisos.add(new
		 * GrantedAuthorityImpl(Roles.ROL_ADMINISTRADOR)); } }
		 */

		// Con el resultado de Login, grabo todo lo que devuelve en la sesion
		/*
		 * UsuarioSecurity u = new UsuarioSecurity(oLogin, null, usuario, clave, boolean
		 * enabled, boolean accountNonExpired, boolean credentialsNonExpired, boolean
		 * accountNonLocked, listPermisos);
		 */

		// UsuarioSecurity oUsr = new UsuarioSecurity(oLogin, null, usuario, clave,
		// true, false,
		// true, true, listPermisos);

		// return new UsernamePasswordAuthenticationToken(oUsr, clave);
		resultAuthTokenTmp = new ACGUsernamePasswordAuthenticationToken(authentication.getPrincipal(),
				authentication.getCredentials(), authorities);

		// resultAuthTokenTmp.setDetails(acgAuthenticationDetails);

		Usuario oUsuario = new Usuario(authentication.getPrincipal().toString(),
				authentication.getCredentials().toString(), (oLogin.getCambiarClaveEnProximoIngreso().getValue() == 0),
				true, true, true, authorities);

		if ("D".equals(oLogin.getTipoCliente().getValue())) {
			oUsuario.setPathTemplate("/secure/administracion");
		} else if ("M".equals(oLogin.getTipoCliente().getValue())) {
			oUsuario.setPathTemplate("/secure/administracion");
		} else if ("P".equals(oLogin.getTipoCliente().getValue())) {
			oUsuario.setPathTemplate("/secure/puntoDeVenta");
		}

		if ("secure".equals(acgAuthenticationDetails.getPath())
				|| "unsecure".equals(acgAuthenticationDetails.getPath())) {

		} else {
			oUsuario.setPathProject("/" + acgAuthenticationDetails.getPath());
		}

		oUsuario.setIdUsuario(oLogin.getIdUsuario().getValue());
		oUsuario.setNombre(oLogin.getNombre().getValue());
		oUsuario.setApellido(oLogin.getApellido().getValue());
		oUsuario.setSoloInformes(oLogin.getSoloInformes().getValue());
		oUsuario.setCambiarClaveEnProximoIngreso(oLogin.getCambiarClaveEnProximoIngreso().getValue());
		oUsuario.setSuperUsuario(oLogin.getSuperUsuario().getValue());
		oUsuario.setIdCliente(oLogin.getIdCliente().getValue());
		oUsuario.setRazonSocial(oLogin.getRazonSocial().getValue());
		oUsuario.setNombreFantasia(oLogin.getNombreFantasia().getValue());
		oUsuario.setTipoCliente(oLogin.getTipoCliente().getValue());
		oUsuario.setNivelDistribuidorSuperior(oLogin.getNivelDistribuidorSuperior().getValue());
		oUsuario.setIdMayorista(oLogin.getIdMayorista().getValue());
		oUsuario.setIdDistribuidor(oLogin.getIdDistribuidor().getValue());
		oUsuario.setHusoHorario(oLogin.getHusoHorario().getValue());
		oUsuario.setIdTipoTerminal(oLogin.getIdTipoTerminal().getValue());
		oUsuario.setIdEntidad(oLogin.getIdEntidad().getValue());
		oUsuario.setIdPais(oLogin.getIdPais().getValue());
		oUsuario.setDescPais(oLogin.getDescPais().getValue());
		oUsuario.setIdProvincia(oLogin.getIdProvincia().getValue());
		oUsuario.setDescProvincia(oLogin.getDescProvincia().getValue());
		oUsuario.setIdLocalidad(oLogin.getIdLocalidad().getValue());
		oUsuario.setDescLocalidad(oLogin.getDescLocalidad().getValue());
		oUsuario.setHabilitarLog(oLogin.getHabilitarLog().getValue());
		oUsuario.setSaldoDisponible(oLogin.getSaldoDisponible().getValue());
		oUsuario.setPermitirLimiteDescubierto(oLogin.getPermitirLimiteDescubierto().getValue());
		oUsuario.setLogoChico(oLogin.getLogoChico().getValue());
		oUsuario.setFooter(oLogin.getFooter().getValue());
		oUsuario.setBanner(oLogin.getBanner().getValue());
		oUsuario.setTituloSistema(oLogin.getTituloSistema().getValue());
		oUsuario.setValorLimiteDescubierto(oLogin.getValorLimiteDescubierto().getValue());

		if (oLogin.getColorBanner().getValue() == null || oLogin.getColorBanner().getValue().isEmpty())
			oUsuario.setColorBanner("black");
		else
			oUsuario.setColorBanner(oLogin.getColorBanner().getValue());

		oUsuario.setMostrarPin(oLogin.getMostrarPin().getValue());
		oUsuario.setMostrarMonitorTRXs(oLogin.getMostrarMonitorTRXs().getValue());
		oUsuario.setRefreshAutomMonTRXsPV(oLogin.getRefreshAutomMonTRXsPV().getValue());
		oUsuario.setHabilitarPrestamos(oLogin.getHabilitarPrestamos().getValue());
		oUsuario.setHabilitarVirtual(oLogin.getHabilitarVirtual().getValue());
		oUsuario.setPvDefTab(oLogin.getPvDefTab().getValue());
		oUsuario.setPermitirComisionPlanaAdelantada(oLogin.getPermitirComisionPlanaAdelantada().getValue());
		oUsuario.setHabilitarInformeRentabilidadPV(oLogin.getHabilitarInformeRentabilidadPV().getValue());
		oUsuario.setValDefCalculoAutomComisiones(oLogin.getValDefCalculoAutomComisiones().getValue());
		oUsuario.setPermitirIncrementoAutomaticoDeSaldo(oLogin.getPermitirIncrementoAutomaticoDeSaldo().getValue());
		oUsuario.setPermitirVentasPorWeb(oLogin.getPermitirVentasPorWeb().getValue());
		oUsuario.setTipoGeoLoc(oLogin.getTipoGeoLoc().getValue());
		//oUsuario.setPermitirLimiteDescubierto(oLogin.getPermitirLimiteDescubierto().getValue());
		oUsuario.setSolicActualizacionDatosAlCliente(oLogin.getSolicActualizacionDatosAlCliente().getValue());
		oUsuario.setLogoutEnSolicDatosCliente(oLogin.getLogoutEnSolicDatosCliente().getValue());
		oUsuario.setHabilitarSeguros(oLogin.getHabilitarSeguros().getValue());
		oUsuario.setAcredComisionConIVAsoloMayorista(oLogin.getAcredComisionConIVAsoloMayorista().getValue());
		oUsuario.setHabilitarWU(oLogin.getHabilitarWU().getValue());
		oUsuario.setInfComMostrarComConIVA(oLogin.getInfComMostrarComConIVA().getValue());
		oUsuario.setCsvSeparadorCampo(oLogin.getCsvSeparadorCampo().getValue());
		oUsuario.setCsvSeparadorDecimales(oLogin.getCsvSeparadorDecimales().getValue());
		oUsuario.setMostrarPantallasDeSube(oLogin.getMostrarPantallasDeSube().getValue());
		oUsuario.setHabilitarPNet(oLogin.getHabilitarPNet().getValue());

		ParametrosList lp = GestionServiceHelper.getGestionService(CfgTimeout.DEFAULT)
						.mostrarParametros(idMayorista, "P", "sube_mostrar_interfaz_web");
		if (lp != null && lp.getListParametros() != null && lp.getListParametros().getValue() != null && lp.getListParametros().getValue().getParametro() != null
				&& lp.getListParametros().getValue().getParametro().size() == 1) {
			if("1".equals(lp.getListParametros().getValue().getParametro().get(0).getValor().getValue()) ) {
				//deja el valor seteado a nivel de usuario
			}else {
				oUsuario.setMostrarPantallasDeSube(0);
			}
		}else {
			oUsuario.setMostrarPantallasDeSube(0);
		}
			
		oUsuario.setMostrarPantallaRepartoMasivoDeSaldo(oLogin.getMostrarPantallaRepartoMasivoDeSaldo().getValue());
		oUsuario.setDistInformarDepositosAnivelesSuperiores(
				oLogin.getDistInformarDepositosAnivelesSuperiores().getValue());
		oUsuario.setPvInformarDepositosAnivelesSuperiores(oLogin.getPvInformarDepositosAnivelesSuperiores().getValue());
		oUsuario.setHabilitarComprobantesExternos(oLogin.getHabilitarComprobantesExternos().getValue());
		oUsuario.setHabilitarLog(oLogin.getHabilitarLog().getValue());
		oUsuario.setCalle(oLogin.getCalle().getValue());
		oUsuario.setAltura(oLogin.getAltura().getValue());
		oUsuario.setMostrarIdsunificacionclientes(oLogin.getMostrarIdsunificacionclientes().getValue());
		oUsuario.setTicket(oLogin.getTicket().getValue());
		oUsuario.setAnchoTicket(oLogin.getAnchoTicket().getValue());
		oUsuario.setClaveGeoLocGoogleMap(oLogin.getClaveGeoLocGoogleMap().getValue());
		oUsuario.setPermisosReducidosEnModuloDeCuentas(oLogin.getPermisosReducidosEnModuloDeCuentas().getValue());
		oUsuario.setPermitirAprobacionesDeSolicitudesDeDinero(oLogin.getPermitirAprobacionesDeSolicitudesDeDinero().getValue());
		
		oUsuario.setSuperPagoInstance(new SuperPagoInstance());
		
		
		com.americacg.cargavirtual.gestion.model.ListadoDeComponentes lc = GestionServiceHelper.getGestionService(CfgTimeout.CONSULTA).listarComponentes(idMayorista);
		oUsuario.getPermisosDeComponentes().clear();
		
		if (lc != null && lc.getError() != null && lc.getError().getValue() != null && lc.getError().getValue().getHayError() != null &&  !lc.getError().getValue().getHayError().getValue() && lc.getRegistros() != null  && lc.getRegistros().getValue() != null  && lc.getRegistros().getValue().getComponente() != null  && !lc.getRegistros().getValue().getComponente().isEmpty()){
			for (Componente c : lc.getRegistros().getValue().getComponente()) {
				oUsuario.getPermisosDeComponentes().put(c.getIdModulo().getValue().concat(c.getPantalla().getValue()).concat(c.getComponente().getValue()),c.getHabilitado().getValue());
				//LogACGHelper.escribirLog(null, "HashPermisos:=|" + c.getIdModulo().getValue().concat(c.getPantalla().getValue()).concat(c.getComponente().getValue()),c.getHabilitado().getValue() + "|");
			}
			
			/*
			Ejemplo
			u.getPermisosDeComponentes().put("2tabAdminpanel4", true);
			u.getPermisosDeComponentes().put("2tabPaneltabAdmin", false);
			u.getPermisosDeComponentes().put("2tabPaneltabUsu", true);
			*/
		
		}else{
			LogACGHelper.escribirLog(null, "El metodo listarComponentes devolvio null, error o no devolvio registros");
		}
		
		
		resultAuthTokenTmp.setDetails(oUsuario);

		// HttpSession session = (HttpSession)
		// FacesContext.getCurrentInstance().getExternalContext().getSession(false);

		// Graba autenticacion y blanquea el usuario y password de la view session
		// SecurityContextHolder.getContext().setAuthentication(resultAuthTokenTmp);

		// Graba autenticacion y blanquea el usuario y password de la view session *
		SecurityContextHolder.getContext().setAuthentication(resultAuthTokenTmp);

		httpReq = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
		httpResp = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();

		strategy.onAuthentication(resultAuthTokenTmp, httpReq, httpResp);

		if (!resultAuthTokenTmp.isAuthenticated()) {
			LogACGHelper.escribirLog(null, "El usuario no pudo autenticarse.");
			throw new BadCredentialsException("El usuario no pudo autenticarse.");
		}

		if (oLogin.getCambiarClaveEnProximoIngreso().getValue() == 1) {
			throw new CredentialsExpiredException("Debe cambiar la clave.");
		}

		if (oLogin.getSolicActualizacionDatosAlCliente().getValue()) {
			throw new InsufficientAuthenticationException("Debe completar datos del usuario.");
		}
		
		// TODO: corregir estas validaciones
		/*
		 * if (!accountNonExpired) { throw new
		 * AccountExpiredException("Cuenta Expirada"); } if (!credentialsNonExpired) {
		 * throw new CredentialsExpiredException("Credencial Expirada"); } if
		 * (!accountNonLocked) { throw new
		 * AccountLockedException("Cuenta/Usuario bloqueado"); }
		 */

		return resultAuthTokenTmp;
	}

	@Override
	public boolean supports(final Class<?> authentication) {
		return authentication.equals(ACGUsernamePasswordAuthenticationToken.class);
	}

	private Boolean validarCaptchaPersonalizado(String captcha) {
		try {

			HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext()
					.getRequest();
			String c = (String) request.getSession().getAttribute(Constants.CAPTCHA_KEY);

			if (captcha.equals(c)) {
				return true;
			} else {
				return false;
			}

		} catch (Exception e) {
			LogACGHelper.escribirLog(null, "Error en validarCaptchaPersonalizado: |" + e.getMessage() + "|");
			return false;
		}

	}

}