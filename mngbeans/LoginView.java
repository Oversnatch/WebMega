package com.americacg.cargavirtual.web.mngbeans;

import java.awt.Color;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Calendar;
import java.util.List;
import java.util.Random;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.context.Flash;
import javax.faces.event.ActionEvent;
import javax.faces.model.SelectItem;
import javax.imageio.ImageIO;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.xml.ws.WebServiceException;
import org.json.JSONObject;
import org.primefaces.PrimeFaces;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Scope;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.CredentialsExpiredException;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.security.web.authentication.session.SessionAuthenticationException;
import com.americacg.cargavirtual.gestion.model.Descripcion;
import com.americacg.cargavirtual.gestion.model.DescripcionContainer;
import com.americacg.cargavirtual.gestion.model.RespString;
import com.americacg.cargavirtual.gestion.model.ResultadoBase;
import com.americacg.cargavirtual.gestion.model.SolicitudCoordenadas;
import com.americacg.cargavirtual.web.enums.CfgTimeout;
import com.americacg.cargavirtual.web.helpers.GestionServiceHelper;
import com.americacg.cargavirtual.web.helpers.LogACGHelper;
import com.americacg.cargavirtual.web.helpers.MayoristasHelper;
import com.americacg.cargavirtual.web.helpers.UsuarioHelper;
import com.americacg.cargavirtual.web.model.CfgWebMayorista;
import com.americacg.cargavirtual.web.model.Usuario;
import com.americacg.cargavirtual.web.security.ACGUsernamePasswordAuthenticationToken;
import com.americacg.cargavirtual.web.shared.Constants;

@Named("loginView")
@Scope(org.springframework.web.context.WebApplicationContext.SCOPE_SESSION)
public class LoginView implements Serializable {

	/**
	 * TODO
	 * 
	 * @Secured() para dar seguridad a nivel metodos.!!!!!!!!!!!!!!!!!!!!!!!!
	 */
	private static final long serialVersionUID = -3678832415607298346L;

	private int anioActual = 0;
	private int height = 30;
	private int width = 120;

	private String usuario = "";
	private String password = "";
	private String passwordNew = "";
	private String passwordNewCheck = "";
	private String coord1proxlogin = "";
	private String coord2proxlogin = "";
	private String paginaTemplate = "";
	private String usuarioActualizarRazonSocial;
	private String usuarioActualizarTelefono1;
	private String usuarioActualizarCelular;
	private String usuarioActualizarMail;
	private String usuarioActualizarCalle;
	private String tmpCaptcha = "";
	private String tmpCoordenada1 = "";
	private String tmpCoordenada2 = "";
	
	
	private Long usuarioActualizarIdPais;
	private Long usuarioActualizarIdProvincia;
	private Long usuarioActualizarIdLocalidad;
	private Long usuarioActualizarCuit;
	private Long usuarioActualizarAltura;
	private Long usuarioActualizarIdRubro;

	private boolean usarTarjetaCoordenadas = false;
	private boolean tmpValidarConTemporal = false;
	
	private List<SelectItem> paises;
	private List<SelectItem> provincias;
	private List<SelectItem> localidades;
	private List<SelectItem> rubros;

	private CfgWebMayorista configuracionMayorista = null;
	@Inject
	private AuthenticationManager authenticationManager;

	/*
	 * @Inject
	 * 
	 * @Qualifier("sas") SessionAuthenticationStrategy strategy;
	 */

	private String captcha = null;
	private String msgCambioPassword = "";
	
	@Autowired
	private ApplicationContext context;

	public LoginView() {
		anioActual = Calendar.getInstance().get(Calendar.YEAR);
	}

	public String getPaginaTemplate() {

		if (configuracionMayorista == null) {
			configuracionMayorista = obtenerConfigMayorista();
		}

		if ("".equals(this.paginaTemplate)) {
			if (!FacesContext.getCurrentInstance().isPostback()
					&& !FacesContext.getCurrentInstance().isValidationFailed()) {
				// Obtengo la primer carpeta de la URL (para saber si el proyecto es test1,
				// test2, etc....)

				// configuracionMayorista = obtenerConfigMayorista();

				validarCoordenadas();
			}
			this.paginaTemplate = (configuracionMayorista != null ? configuracionMayorista.getTemplate() : "default1");
		}
		return this.paginaTemplate;
	}

	public String getMsgCambioPassword() {
		return msgCambioPassword;
	}

	public void setMsgCambioPassword(String msgCambioPassword) {
		this.msgCambioPassword = msgCambioPassword;
	}

	private CfgWebMayorista obtenerConfigMayorista() {
		CfgWebMayorista oCfgMayo = null;
		String urlPathFolder = "";

		HttpServletRequest req = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext()
				.getRequest();
		try {
			urlPathFolder = req.getRequestURL().toString().split("/")[3];
		} catch (Exception e) {
			urlPathFolder = "";
			// TODO: ver como mostrar el error
			// out.println("Error buscando urlPathFolder en archivo parametrizacionCli.jsp:
			// |" + e.getMessage() + "|");
			LogACGHelper.escribirLog(null,
					"Error haciendo split del request para urlPathFolder: |" + e.getMessage() + "|");
		}

		try {
			JSONObject oJSON = MayoristasHelper.getSingletonInstance().obtenerMayorista(req.getServerName(),
					urlPathFolder);
			if (oJSON != null) {

				oCfgMayo = new CfgWebMayorista(oJSON.getString("url"), oJSON.getString("path"),
						("S".equals(oJSON.getString("validaPath")) ? true : false), oJSON.getLong("idMayorista"),
						oJSON.getString("logo"), oJSON.getString("template"), oJSON.getString("captchaBackgroundColor"),
						oJSON.getString("captchaDegradeOrigenColor"), oJSON.getString("captchaDegradeDestinoColor"));
			} else {
				// TODO: IR A DONDE???
			}

		} catch (Exception e) {
			LogACGHelper.escribirLog(null,
					"Error obteniendo configuracion mayorista de archivo JSON: |" + e.getMessage() + "|");
		}

		return oCfgMayo;
	}

	public String login() {
		int expiry = 30 * 24 * 60 * 60; // 30 dias expresados en segundos
		Authentication request = null; 
		HttpServletRequest httpReq = null; 
		
		try {
			request = new ACGUsernamePasswordAuthenticationToken(this.getUsuario(), this.getPassword());

			authenticationManager.authenticate(request);

			this.usuario = "";
			this.password = "";
			this.passwordNew = "";
			this.passwordNewCheck = "";
			this.captcha = null;
			
			this.tmpCaptcha = "";
			this.tmpCoordenada1 = "";
			this.tmpCoordenada2 = "";
			
		} catch (CredentialsExpiredException ce) {
			LogACGHelper.escribirLog(null, "Error en login CredentialsExpiredException: |" + ce.getMessage() + "|", null);
			
			this.password = "";
			this.passwordNew = "";
			this.passwordNewCheck = "";
			PrimeFaces.current().executeScript("ocultarBlockPageLogin = false;");
			return "chgpwd";
		} catch (BadCredentialsException e) {
			this.captcha = null;
			// CookieHelper.setCookie("username", this.usuario, expiry);

			/*
			 * Mantiene el mensaje de error entre las vistas
			 */
			Flash flash = FacesContext.getCurrentInstance().getExternalContext().getFlash();
			flash.setKeepMessages(true);

			LogACGHelper.escribirLog(null, "Error en login BadCredentialsException: |" + e.getMessage() + "|");

			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR, e.getLocalizedMessage(), null));
			return "incorrect";
		} catch (LockedException eb) {
			this.captcha = null;

			/*
			 * Mantiene el mensaje de error entre las vistas
			 */
			Flash flash = FacesContext.getCurrentInstance().getExternalContext().getFlash();
			flash.setKeepMessages(true);

			LogACGHelper.escribirLog(null, "Error en login LockedException: |" + eb.getMessage() + "|");

			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR, eb.getLocalizedMessage(), null));
			return "incorrect";
		} catch (InsufficientAuthenticationException iae) {
			PrimeFaces.current().executeScript("ocultarBlockPageLogin = false;");

			/*
			httpReq = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
			
			if(httpReq != null) {
				this.tmpCaptcha = httpReq.getParameter("captcha");
				this.tmpCoordenada1 = httpReq.getParameter("coordenada1");
				this.tmpCoordenada2 = httpReq.getParameter("coordenada2");
				this.tmpValidarConTemporal = true;
				*/
				return "update";
/*				
			}else {
				
				/-*
				 * Mantiene el mensaje de error entre las vistas
				 *-/
				Flash flash = FacesContext.getCurrentInstance().getExternalContext().getFlash();
				flash.setKeepMessages(true);

				LogACGHelper.escribirLog(null, "Error en login BadCredentialsException: |No se pudo obtener los datos de login necesarios.|");

				FacesContext.getCurrentInstance().addMessage(null,
						new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error en ingreso al sistema, intente nuevamente mas tarde.", null));
				
				return "incorrect";
			}
		*/

		} catch (SessionAuthenticationException ae) {
			this.captcha = null;

			/*
			 * Mantiene el mensaje de error entre las vistas
			 */
			Flash flash = FacesContext.getCurrentInstance().getExternalContext().getFlash();
			flash.setKeepMessages(true);

			LogACGHelper.escribirLog(null, "Error en login SessionAuthenticationException: |" + ae.getMessage() + "|");

			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
					"Maximas sessiones permitidas para el usuario alcanzadas.", null));
			return "incorrect";
		} catch (Exception e) {
			this.captcha = null;

			/*
			 * Mantiene el mensaje de error entre las vistas
			 */
			Flash flash = FacesContext.getCurrentInstance().getExternalContext().getFlash();
			flash.setKeepMessages(true);

			LogACGHelper.escribirLog(null, "Error en login Exception gral: |" + e.getMessage() + "|");

			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
					"Se ha producido un error. Contacte a soporte.", null));
			return "incorrect";
		}

		if (UsuarioHelper.usuarioSession().getTipoCliente() != null
				&& !UsuarioHelper.usuarioSession().getTipoCliente().isEmpty()) {

			if ("P".equals(UsuarioHelper.usuarioSession().getTipoCliente())) {
				PrimeFaces.current().executeScript("ocultarBlockPageLogin = false;");
				PuntoVentaView ptoVentaView = context.getBean(PuntoVentaView.class);
				if(ptoVentaView != null){
					ptoVentaView.init();
				}
				return "correctPtoVta";
			} else if ("M".equals(UsuarioHelper.usuarioSession().getTipoCliente())
					|| "D".equals(UsuarioHelper.usuarioSession().getTipoCliente())) {

				// !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
				// TODO: Agregar parametro para que se pueda bajar el sitio de mayorista o el de
				// pto de vta.
				//
				if (UsuarioHelper.usuarioSession().getIdMayorista().compareTo(2L) > 0) {
					this.captcha = null;

					/*
					 * Mantiene el mensaje de error entre las vistas
					 */
					Flash flash = FacesContext.getCurrentInstance().getExternalContext().getFlash();
					flash.setKeepMessages(true);

					FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
							"Para acceder como Mayorista o Distribuidor por favor seguir utilizando la versión anterior.",
							null));
					// PrimeFaces.current().executeScript("PF('panelLoginWV').hide();");
					return "incorrect";
				} else {
					PrimeFaces.current().executeScript("ocultarBlockPageLogin = false;");
					AdministracionView admView = context.getBean(AdministracionView.class);
					if(admView != null){
						admView.init();
					}					
					return "correctAdmin";
				}
			} else {
				this.captcha = null;

				/*
				 * Mantiene el mensaje de error entre las vistas
				 */
				Flash flash = FacesContext.getCurrentInstance().getExternalContext().getFlash();
				flash.setKeepMessages(true);

				LogACGHelper.escribirLog(null, "Tipo de cliente invalido. Contacte a soporte.");

				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
						"Tipo de cliente invalido. Contacte a soporte.", null));
				return "incorrect";
			}
		} else {
			this.captcha = null;

			/*
			 * Mantiene el mensaje de error entre las vistas
			 */
			Flash flash = FacesContext.getCurrentInstance().getExternalContext().getFlash();
			flash.setKeepMessages(true);

			LogACGHelper.escribirLog(null, "Tipo de cliente no informado. Contacte a soporte.");

			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
					"Tipo de cliente no informado. Contacte a soporte.", null));
			return "incorrect";
		}
	}

	public String logout() {
		HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false);
		HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext()
				.getRequest();
		HttpServletResponse response = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext()
				.getResponse();
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();

		this.usuario = "";
		this.password = "";

		if (session != null) {
			session.invalidate();
		}

		new SecurityContextLogoutHandler().logout(request, response, auth);

		// SecurityContextHolder.clearContext();

		return "logout";
	}

	public void cambiarPassword() {
		Usuario oUsr = null;
		ResultadoBase oResu = null;

		try {
			oUsr = (Usuario) SecurityContextHolder.getContext().getAuthentication().getDetails();

			oResu = GestionServiceHelper.getGestionService(CfgTimeout.DEFAULT).cambiarClaveUsuario(
					oUsr.getIdMayorista(), oUsr.getIdCliente(), oUsr.getIdUsuario(), oUsr.getUsername(), this.password,
					this.getPasswordNew(), this.getPasswordNewCheck());

			if (oResu.getError() != null && oResu.getError().getValue().getHayError().getValue()) {
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
						oResu.getError().getValue().getMsgError().getValue(), null));

				PrimeFaces.current().executeScript("PF('blockPanelGral').hide()");
			} else {
				this.msgCambioPassword = "Clave modificada exitosamente. A continuación debera realizar login nuevamente.";
				PrimeFaces.current().executeScript("PF('blockPanelGral').hide()");
				PrimeFaces.current().executeScript("PF('dlgCambioPassword').show();");
			}
		} catch (WebServiceException ste) {
			LogACGHelper.escribirLog(null, "Error WebServiceException cambiarClave: |" + ste.getMessage() + "|");

			if (ste != null && ste.getCause() != null && ste.getCause().getClass() != null) {
				if (ste.getCause().getClass().equals(ConnectException.class)) {
					FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
							"No se pudo establecer la comunicación (GST-TOC).\n Por favor intente nuevamente.", null));
				} else if (ste.getCause().getClass().equals(SocketTimeoutException.class)) {
					FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
							"No se pudo establecer la comunicación (GST-TRW).\n Por favor intente nuevamente.", null));
				} else if (ste.getCause().getClass().equals(UnknownHostException.class)) {
					LogACGHelper.escribirLog(null,
							"No se pudo establecer la comunicación (GST-HNC): |" + ste.getMessage() + "|");
					FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
							"No se pudo establecer la comunicación (GST-HNC).\n Por favor intente nuevamente.", null));
				} else {
					LogACGHelper.escribirLog(null, "Error al cambiar la clave: |" + ste.getMessage() + "|");
					FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
							"Error al cambiar la clave: |" + ste.getMessage() + "|", null));
				}
			} else {
				LogACGHelper.escribirLog(null, "Error al cambiar la clave: |" + ste.getMessage() + "|");
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
						"Error al cambiar la clave: |" + ste.getMessage() + "|", null));
			}
			FacesContext.getCurrentInstance().validationFailed();
		} catch (Exception e) {
			LogACGHelper.escribirLog(null,
					"Error al cambiar la clave. Excepcion ejecutando el WS de consulta: |" + e.getMessage() + "|");
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
					"Error al cambiar la clave: |" + e.getMessage() + "|", null));
			FacesContext.getCurrentInstance().validationFailed();
		}
		PrimeFaces.current().executeScript("PF('blockPanelGral').hide()");
	}

	public void validarCoordenadas() {
		try {
			SolicitudCoordenadas sc = new SolicitudCoordenadas();

			if (configuracionMayorista != null) {
				StringBuilder requestURL = new StringBuilder(((HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext()
						.getRequest()).getRequestURL().toString());
			    String queryString = ((HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext()
						.getRequest()).getQueryString();
				
			    if (queryString == null) {
			    } else {
			    	requestURL.append('?').append(queryString).toString();
			    }
				
				sc = GestionServiceHelper.getGestionService(CfgTimeout.DEFAULT)
						.usarTarjetaCoordenadas1(configuracionMayorista.getIdMayorista(), usuario, true, requestURL.toString());

				if (sc != null) {
					if (sc.getError().getValue().getHayError().getValue()) {
						// Error que devolvio el gestionService
						// NO IMPRIMIR NADA
						if ("REDIR".equals(sc.getError().getValue().getCodigoError().getValue())){
							FacesContext.getCurrentInstance().getExternalContext().redirect(sc.getError().getValue().getMsgError().getValue());
							FacesContext.getCurrentInstance().responseComplete();
						}else {
							usarTarjetaCoordenadas = false;							
						}

					} else {
						usarTarjetaCoordenadas = (sc.getUsarTarjetaCoordenadas().getValue().compareTo(1) == 0);
						coord1proxlogin = sc.getCoord1Proxlogin().getValue();
						coord2proxlogin = sc.getCoord2Proxlogin().getValue();
					}
				} else {
					FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
							"El sistema de gestión no se encuentra disponible. Refresque la página.", null));
				}
			} else {
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
						"No se dispone de datos de configuración. Reintente nuevamente más tarde.", null));
			}
		} catch (WebServiceException ste) {
			if (ste != null && ste.getCause() != null && ste.getCause().getClass() != null) {
				if (ste.getCause().getClass().equals(ConnectException.class)) {
					FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
							"No se pudo establecer la comunicación (GST-TOC).\n Por favor intente nuevamente.", null));
				} else if (ste.getCause().getClass().equals(SocketTimeoutException.class)) {
					FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
							"No se pudo establecer la comunicación (GST-TRW).\n Por favor intente nuevamente.", null));
				} else if (ste.getCause().getClass().equals(UnknownHostException.class)) {
					LogACGHelper.escribirLog(null,
							"No se pudo establecer la comunicación (GST-HNC): |" + ste.getMessage() + "|");
					FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
							"No se pudo establecer la comunicación (GST-HNC).\n Por favor intente nuevamente.", null));
				} else {
					LogACGHelper.escribirLog(null, "validarCoordenadas error:|" + ste.getMessage() + "|");
					FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
							"Para validar coordenadas, debera reintentar el proceso de login nuevamente.", null));
				}
			} else {
				LogACGHelper.escribirLog(null, "validarCoordenadas error:|" + ste.getMessage() + "|");
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
						"Para validar coordenadas, debera reintentar el proceso de login nuevamente.", null));
			}
			FacesContext.getCurrentInstance().validationFailed();
		} catch (Exception e) {
			LogACGHelper.escribirLog(null, "validarCoordenadas error:|" + e.getMessage() + "|");
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
					"Para validar coordenadas, debera reintentar el proceso de login nuevamente.", null));
			FacesContext.getCurrentInstance().validationFailed();
		}

		return;
	}

	public void recuperar() {
		this.msgCambioPassword = "";
		try {
			RespString rs = new RespString();

			rs = GestionServiceHelper.getGestionService(CfgTimeout.DEFAULT)
					.olvideClave(configuracionMayorista.getIdMayorista(), usuario);

			if (rs.getError().getValue().getHayError().getValue()) {
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
						rs.getError().getValue().getMsgError().getValue(), null));
			} else {
				this.msgCambioPassword = "Para recuperar su contraseÃ±a, por favor, revise su e-mail y proceda segÃºn las instrucciones.";
				PrimeFaces.current().executeScript("PF('blockPageLogin').hide()");
				PrimeFaces.current().executeScript("PF('dlgRecuperarClave').show();");
			}
		} catch (WebServiceException ste) {
			if (ste != null && ste.getCause() != null && ste.getCause().getClass() != null) {
				if (ste.getCause().getClass().equals(ConnectException.class)) {
					FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
							"No se pudo establecer la comunicación (GST-TOC).\n Por favor intente nuevamente.", null));
				} else if (ste.getCause().getClass().equals(SocketTimeoutException.class)) {
					FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
							"No se pudo establecer la comunicación (GST-TRW).\n Por favor intente nuevamente.", null));
				} else if (ste.getCause().getClass().equals(UnknownHostException.class)) {
					LogACGHelper.escribirLog(null,
							"No se pudo establecer la comunicación (GST-HNC): |" + ste.getMessage() + "|");
					FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
							"No se pudo establecer la comunicación (GST-HNC).\n Por favor intente nuevamente.", null));
				} else {
					LogACGHelper.escribirLog(null, "recuperar error:|" + ste.getMessage() + "|");
					FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
							"Para recuperar contraseÃ±a, debera reintentar el proceso nuevamente.", null));
				}
			} else {
				LogACGHelper.escribirLog(null, "recuperar error:|" + ste.getMessage() + "|");
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
						"Para recuperar contraseÃ±a, debera reintentar el proceso nuevamente.", null));
			}
			FacesContext.getCurrentInstance().validationFailed();
			PrimeFaces.current().executeScript("PF('blockPanelGral').hide()");
		} catch (Exception e) {
			LogACGHelper.escribirLog(null, "recuperar error:|" + e.getMessage() + "|");
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
					"Para recuperar contraseÃ±a, debera reintentar el proceso nuevamente.", null));
			FacesContext.getCurrentInstance().validationFailed();
			PrimeFaces.current().executeScript("PF('blockPanelGral').hide()");
		}

	}

	public void captchaUpdate() {
		this.captcha = null;
	}

	public Color hex2Rgb(String colorStr, String colorDefaultStr) {
		try {
			return new Color(Integer.valueOf(colorStr.substring(1, 3), 16) / 255f,
					Integer.valueOf(colorStr.substring(3, 5), 16) / 255f,
					Integer.valueOf(colorStr.substring(5, 7), 16) / 255f);
		} catch (Exception e) {
			return new Color(Integer.valueOf(colorStr.substring(1, 3), 16) / 255f,
					Integer.valueOf(colorStr.substring(3, 5), 16) / 255f,
					Integer.valueOf(colorStr.substring(5, 7), 16) / 255f);
		}
	}

	public String getCaptcha() {
		try {
			if (captcha == null) {
				BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
				Graphics2D graphics2D = image.createGraphics();
				Color rgb = new Color(1f, 1f, 1f, 1f);
				graphics2D.setColor(rgb);

				if (configuracionMayorista == null) {
					configuracionMayorista = obtenerConfigMayorista();
				}

				Color colorCaptchaBackground = hex2Rgb(configuracionMayorista.getCaptchaBackgroundColor(), "#FFFFFF");
				Color colorCaptchaDegradeOrigen = hex2Rgb(configuracionMayorista.getCaptchaDegradeOrigenColor(),
						"#97CD39");
				Color colorCaptchaDegradeDestino = hex2Rgb(configuracionMayorista.getCaptchaDegradeDestinoColor(),
						"#8C8C8C");

				graphics2D.setColor(colorCaptchaBackground);
				graphics2D.fillRect(0, 0, width, height);

				Random r = new Random();
				// String token = Long.toString(Math.abs(r.nextLong()), 36); //Devuelve numeros
				// y letras
				String token = Long.toString(Math.abs(r.nextLong()), 10); // Devuelve solo numeros
				String ch = token.substring(0, 6);

				// Reemplazo los caracteres dificiles de identificar por otros mas simples
				ch = ch.replace("o", "a");
				ch = ch.replace("O", "A");
				ch = ch.replace("0", "1");
				ch = ch.replace("l", "h");

				GradientPaint gp = new GradientPaint(30, 30, colorCaptchaDegradeOrigen, 15, 25,
						colorCaptchaDegradeDestino, true);
				graphics2D.setPaint(gp);
				Font font = new Font("Verdana", Font.CENTER_BASELINE, 26);
				graphics2D.setFont(font);
				graphics2D.drawString(ch, 6, 24);
				graphics2D.dispose();

				HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext()
						.getRequest();
				request.getSession().setAttribute(Constants.CAPTCHA_KEY, ch);
				ByteArrayOutputStream baos = new ByteArrayOutputStream();

				try {
					ImageIO.write(image, "png", baos);
					baos.flush();

					captcha = "data:image/png;base64," + Base64.getEncoder().encodeToString(baos.toByteArray());

				} catch (IOException e) {
					captcha = "data:image/png;base64,";
				}
			}
		} catch (Exception e) {
			LogACGHelper.escribirLog(null, "Error al generar el captcha:|" + e.getMessage() + "|");
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
					"No se pudo generar un captcha, por favor reintente nuevamente mas tarde.", null));
			FacesContext.getCurrentInstance().validationFailed();
		}
		return captcha;
	}

	public String guardarDatosCliente() {
		Usuario oUsr = null;
		try {
			oUsr = (Usuario) SecurityContextHolder.getContext().getAuthentication().getDetails();

			ResultadoBase r = GestionServiceHelper.getGestionService(CfgTimeout.DEFAULT).guardarDatosCliente(
					configuracionMayorista.getIdMayorista(), oUsr.getIdCliente(), usuarioActualizarIdPais,
					usuarioActualizarIdProvincia, usuarioActualizarIdLocalidad, usuarioActualizarRazonSocial,
					usuarioActualizarCuit, usuarioActualizarTelefono1, usuarioActualizarCelular, usuarioActualizarMail,
					usuarioActualizarCalle, usuarioActualizarAltura, usuarioActualizarIdRubro);

			if (r.getError().getValue().getHayError().getValue()) {
				/*
				 * Mantiene el mensaje de error entre las vistas
				 */
				Flash flash = FacesContext.getCurrentInstance().getExternalContext().getFlash();
				flash.setKeepMessages(true);

				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
						r.getError().getValue().getMsgError().getValue(), null));
				PrimeFaces.current().executeScript("PF('blockPanelGral').hide()");

				return "incorrect";
			} else {
				
				Authentication auth = SecurityContextHolder.getContext().getAuthentication();

				List<GrantedAuthority> updatedAuthorities = new ArrayList<>(auth.getAuthorities());
				updatedAuthorities.remove(new SimpleGrantedAuthority("ROL_ACTUALIZARDATOS"));
				
				if ("D".equals(oUsr.getTipoCliente())) {
					updatedAuthorities.add(new SimpleGrantedAuthority("ROL_DISTRIBUIDOR"));
				} else if ("M".equals(oUsr.getTipoCliente())) {
					updatedAuthorities.add(new SimpleGrantedAuthority("ROL_MAYORISTA"));
				} else if ("P".equals(oUsr.getTipoCliente())) {
					updatedAuthorities.add(new SimpleGrantedAuthority("ROL_PUNTOVENTA"));
				}

				Authentication newAuth = new ACGUsernamePasswordAuthenticationToken(auth.getPrincipal(), auth.getCredentials(), updatedAuthorities, oUsr);
				
				SecurityContextHolder.getContext().setAuthentication(newAuth);

				if ("D".equals(oUsr.getTipoCliente()) || "M".equals(oUsr.getTipoCliente())) {
					PrimeFaces.current().executeScript("ocultarBlockPageLogin = false;");
					FacesContext facesContext = FacesContext.getCurrentInstance();
					AdministracionView admView = (AdministracionView) facesContext.getApplication().getELResolver()
							.getValue(facesContext.getELContext(), null, "administracionView");
					admView.init();
					return "correctAdmin";
				} else {
					PrimeFaces.current().executeScript("ocultarBlockPageLogin = false;");
					FacesContext facesContext = FacesContext.getCurrentInstance();
					Object view = facesContext.getApplication().getELResolver().getValue(facesContext.getELContext(), null,
							"puntoVentaView");
					if (view != null) {
						PuntoVentaView ptoVentaView = (PuntoVentaView) view;
						ptoVentaView.init();
					}
					return "correctPtoVta";
				}
			}

		} catch (WebServiceException ste) {
			/*
			 * Mantiene el mensaje de error entre las vistas
			 */
			Flash flash = FacesContext.getCurrentInstance().getExternalContext().getFlash();
			flash.setKeepMessages(true);

			if (ste != null && ste.getCause() != null && ste.getCause().getClass() != null) {
				if (ste.getCause().getClass().equals(ConnectException.class)) {
					FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
							"No se pudo establecer la comunicación (GST-TOC).\n Por favor intente nuevamente.", null));
				} else if (ste.getCause().getClass().equals(SocketTimeoutException.class)) {
					FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
							"No se pudo establecer la comunicación (GST-TRW).\n Por favor intente nuevamente.", null));
				} else if (ste.getCause().getClass().equals(UnknownHostException.class)) {
					LogACGHelper.escribirLog(null,
							"No se pudo establecer la comunicación (GST-HNC): |" + ste.getMessage() + "|");
					FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
							"No se pudo establecer la comunicación (GST-HNC).\n Por favor intente nuevamente.", null));
				} else {
					LogACGHelper.escribirLog(null, "Error guardando los datos del cliente:|" + ste.getMessage() + "|");
					FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
							"Para actualizar los datos, debera reintentar el proceso nuevamente.", null));
				}
			} else {
				LogACGHelper.escribirLog(null, "Error guardando los datos del cliente:|" + ste.getMessage() + "|");
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
						"Para actualizar los datos, debera reintentar el proceso nuevamente.", null));
			}
			FacesContext.getCurrentInstance().validationFailed();
			PrimeFaces.current().executeScript("PF('blockPanelGral').hide()");
			return "incorrect";
		} catch (Exception e) {
			/*
			 * Mantiene el mensaje de error entre las vistas
			 */
			Flash flash = FacesContext.getCurrentInstance().getExternalContext().getFlash();
			flash.setKeepMessages(true);

			LogACGHelper.escribirLog(null, "Error guardando los datos del cliente:|" + e.getMessage() + "|");
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
					"Para actualizar los datos, debera reintentar el proceso nuevamente.", null));
			FacesContext.getCurrentInstance().validationFailed();
			PrimeFaces.current().executeScript("PF('blockPanelGral').hide()");
			return "incorrect";
		}
	}

	public String salirSinActualizarDatos() {
		PrimeFaces.current().executeScript("PF('blockPanelGral').hide()");
		return "incorrect";
	}

	public void onChangePais(ActionEvent evt) {
		usuarioActualizarIdProvincia = null;
		usuarioActualizarIdLocalidad = null;
	}

	public void onChangeProvincia(ActionEvent evt) {
		usuarioActualizarIdLocalidad = null;
	}

	public List<SelectItem> getPaises() {
		try {
			this.provincias = new ArrayList<SelectItem>();
			this.localidades = new ArrayList<SelectItem>();

			if (this.paises == null) {
				this.paises = new ArrayList<SelectItem>();
				DescripcionContainer cd = GestionServiceHelper.getGestionService(CfgTimeout.DEFAULT)
						.mostrarPaises(configuracionMayorista.getIdMayorista(), null);

				if ((cd != null) && (!cd.getListDescripcion().getValue().getDescripcion().isEmpty())) {
					for (Descripcion d : cd.getListDescripcion().getValue().getDescripcion()) {
						this.paises.add(new SelectItem(d.getId().getValue(), d.getDescripcion().getValue()));
					}
				}
			}

		} catch (WebServiceException ste) {
			if (ste != null && ste.getCause() != null && ste.getCause().getClass() != null) {
				if (ste.getCause().getClass().equals(ConnectException.class)) {
					FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
							"No se pudo establecer la comunicación (GST-TOC).\n Por favor intente nuevamente.", null));
				} else if (ste.getCause().getClass().equals(SocketTimeoutException.class)) {
					FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
							"No se pudo establecer la comunicación (GST-TRW).\n Por favor intente nuevamente.", null));
				} else if (ste.getCause().getClass().equals(UnknownHostException.class)) {
					LogACGHelper.escribirLog(null,
							"No se pudo establecer la comunicación (GST-HNC): |" + ste.getMessage() + "|");
					FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
							"No se pudo establecer la comunicación (GST-HNC).\n Por favor intente nuevamente.", null));
				} else {
					LogACGHelper.escribirLog(null, "Error en lectura de Paises:|" + ste.getMessage() + "|");
				}
			} else {
				LogACGHelper.escribirLog(null, "Error en lectura de Paises:|" + ste.getMessage() + "|");
			}
		} catch (Exception e) {
			LogACGHelper.escribirLog(null, "Error en lectura de Paises:|" + e.getMessage() + "|");
		}

		return paises;
	}

	public void setPaises(List<SelectItem> paises) {
		this.paises = paises;
	}

	public List<SelectItem> getProvincias() {

		try {
			this.provincias = new ArrayList<SelectItem>();
			this.localidades = new ArrayList<SelectItem>();

			if ((usuarioActualizarIdPais != null) && (usuarioActualizarIdPais > 0)) {
				DescripcionContainer cd = GestionServiceHelper.getGestionService(CfgTimeout.DEFAULT)
						.mostrarProvincias(configuracionMayorista.getIdMayorista(), null, usuarioActualizarIdPais);

				if ((cd != null) && (!cd.getListDescripcion().getValue().getDescripcion().isEmpty())) {
					for (Descripcion d : cd.getListDescripcion().getValue().getDescripcion()) {
						this.provincias.add(new SelectItem(d.getId().getValue(), d.getDescripcion().getValue()));
					}
				}
			}

		} catch (WebServiceException ste) {
			if (ste != null && ste.getCause() != null && ste.getCause().getClass() != null) {
				if (ste.getCause().getClass().equals(ConnectException.class)) {
					FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
							"No se pudo establecer la comunicación (GST-TOC).\n Por favor intente nuevamente.", null));
				} else if (ste.getCause().getClass().equals(SocketTimeoutException.class)) {
					FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
							"No se pudo establecer la comunicación (GST-TRW).\n Por favor intente nuevamente.", null));
				} else if (ste.getCause().getClass().equals(UnknownHostException.class)) {
					LogACGHelper.escribirLog(null,
							"No se pudo establecer la comunicación (GST-HNC): |" + ste.getMessage() + "|");
					FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
							"No se pudo establecer la comunicación (GST-HNC).\n Por favor intente nuevamente.", null));
				} else {
					LogACGHelper.escribirLog(null, "Error en lectura de Provincias:|" + ste.getMessage() + "|");
				}
			} else {
				LogACGHelper.escribirLog(null, "Error en lectura de Provincias:|" + ste.getMessage() + "|");
			}
		} catch (Exception e) {
			LogACGHelper.escribirLog(null, "Error en lectura de Provincias:|" + e.getMessage() + "|");
		}

		return provincias;
	}

	public void setProvincias(List<SelectItem> provincias) {
		this.provincias = provincias;
	}

	public List<SelectItem> getLocalidades() {

		try {
			this.localidades = new ArrayList<SelectItem>();
			if ((usuarioActualizarIdProvincia != null) && (usuarioActualizarIdProvincia > 0)) {
				DescripcionContainer cd = GestionServiceHelper.getGestionService(CfgTimeout.DEFAULT)
						.mostrarLocalidadesPorProv(configuracionMayorista.getIdMayorista(),
								usuarioActualizarIdProvincia, null);

				if ((cd != null) && (!cd.getListDescripcion().getValue().getDescripcion().isEmpty())) {
					for (Descripcion d : cd.getListDescripcion().getValue().getDescripcion()) {
						this.localidades.add(new SelectItem(d.getId().getValue(), d.getDescripcion().getValue()));
					}
				}
			}

		} catch (WebServiceException ste) {
			if (ste != null && ste.getCause() != null && ste.getCause().getClass() != null) {
				if (ste.getCause().getClass().equals(ConnectException.class)) {
					FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
							"No se pudo establecer la comunicación (GST-TOC).\n Por favor intente nuevamente.", null));
				} else if (ste.getCause().getClass().equals(SocketTimeoutException.class)) {
					FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
							"No se pudo establecer la comunicación (GST-TRW).\n Por favor intente nuevamente.", null));
				} else if (ste.getCause().getClass().equals(UnknownHostException.class)) {
					LogACGHelper.escribirLog(null,
							"No se pudo establecer la comunicación (GST-HNC): |" + ste.getMessage() + "|");
					FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
							"No se pudo establecer la comunicación (GST-HNC).\n Por favor intente nuevamente.", null));
				} else {
					LogACGHelper.escribirLog(null, "Error en lectura de Localidades:|" + ste.getMessage() + "|");
				}
			} else {
				LogACGHelper.escribirLog(null, "Error en lectura de Localidades:|" + ste.getMessage() + "|");
			}
		} catch (Exception e) {
			LogACGHelper.escribirLog(null, "Error en lectura de Localidades:|" + e.getMessage() + "|");
		}

		return localidades;
	}

	public List<SelectItem> getRubros() {

		try {

			this.rubros = new ArrayList<SelectItem>();
			DescripcionContainer cd = GestionServiceHelper.getGestionService(CfgTimeout.DEFAULT)
					.mostrarRubros(configuracionMayorista.getIdMayorista(), null);

			if ((cd != null) && (!cd.getListDescripcion().getValue().getDescripcion().isEmpty())) {
				for (Descripcion d : cd.getListDescripcion().getValue().getDescripcion()) {
					this.rubros.add(new SelectItem(d.getId().getValue(), d.getDescripcion().getValue()));
				}
			}

		} catch (WebServiceException ste) {
			if (ste != null && ste.getCause() != null && ste.getCause().getClass() != null) {
				if (ste.getCause().getClass().equals(ConnectException.class)) {
					FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
							"No se pudo establecer la comunicación (GST-TOC).\n Por favor intente nuevamente.", null));
				} else if (ste.getCause().getClass().equals(SocketTimeoutException.class)) {
					FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
							"No se pudo establecer la comunicación (GST-TRW).\n Por favor intente nuevamente.", null));
				} else if (ste.getCause().getClass().equals(UnknownHostException.class)) {
					LogACGHelper.escribirLog(null,
							"No se pudo establecer la comunicación (GST-HNC): |" + ste.getMessage() + "|");
					FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
							"No se pudo establecer la comunicación (GST-HNC).\n Por favor intente nuevamente.", null));
				} else {
					LogACGHelper.escribirLog(null, "Error en lectura de Rubros:|" + ste.getMessage() + "|");
				}
			} else {
				LogACGHelper.escribirLog(null, "Error en lectura de Rubros:|" + ste.getMessage() + "|");
			}
		} catch (Exception e) {
			LogACGHelper.escribirLog(null, "Error en lectura de Rubros:|" + e.getMessage() + "|");
		}
		return rubros;
	}

	public AuthenticationManager getAuthenticationManager() {
		return authenticationManager;
	}

	public void setAuthenticationManager(AuthenticationManager authenticationManager) {
		this.authenticationManager = authenticationManager;
	}

	public String getUsuario() {
		return usuario;
	}

	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getPasswordNew() {
		return passwordNew;
	}

	public void setPasswordNew(String passwordNew) {
		this.passwordNew = passwordNew;
	}

	public String getPasswordNewCheck() {
		return passwordNewCheck;
	}

	public void setPasswordNewCheck(String passwordNewCheck) {
		this.passwordNewCheck = passwordNewCheck;
	}

	public int getAnioActual() {
		return anioActual;
	}

	public void setAnioActual(int anioActual) {
		this.anioActual = anioActual;
	}

	public void setRubros(List<SelectItem> rubros) {
		this.rubros = rubros;
	}

	public void setCaptcha(String captcha) {
		this.captcha = captcha;
	}

	public String getCoord1proxlogin() {
		return coord1proxlogin;
	}

	public void setCoord1proxlogin(String coord1proxlogin) {
		this.coord1proxlogin = coord1proxlogin;
	}

	public String getCoord2proxlogin() {
		return coord2proxlogin;
	}

	public void setCoord2proxlogin(String coord2proxlogin) {
		this.coord2proxlogin = coord2proxlogin;
	}

	public boolean isUsarTarjetaCoordenadas() {
		return usarTarjetaCoordenadas;
	}

	public void setUsarTarjetaCoordenadas(boolean usarTarjetaCoordenadas) {
		this.usarTarjetaCoordenadas = usarTarjetaCoordenadas;
	}

	public void cancelarChgPwd() {
		this.password = "";
		this.passwordNew = "";
		this.passwordNewCheck = "";

		PrimeFaces.current().executeScript("PF('panelProductosWV').close()");
	}

	public CfgWebMayorista getConfiguracionMayorista() {
		return configuracionMayorista;
	}

	public void setConfiguracionMayorista(CfgWebMayorista configuracionMayorista) {
		this.configuracionMayorista = configuracionMayorista;
	}

	public String getUsuarioActualizarRazonSocial() {
		return usuarioActualizarRazonSocial;
	}

	public void setUsuarioActualizarRazonSocial(String usuarioActualizarRazonSocial) {
		this.usuarioActualizarRazonSocial = usuarioActualizarRazonSocial;
	}

	public String getUsuarioActualizarTelefono1() {
		return usuarioActualizarTelefono1;
	}

	public void setUsuarioActualizarTelefono1(String usuarioActualizarTelefono1) {
		this.usuarioActualizarTelefono1 = usuarioActualizarTelefono1;
	}

	public String getUsuarioActualizarCelular() {
		return usuarioActualizarCelular;
	}

	public void setUsuarioActualizarCelular(String usuarioActualizarCelular) {
		this.usuarioActualizarCelular = usuarioActualizarCelular;
	}

	public String getUsuarioActualizarMail() {
		return usuarioActualizarMail;
	}

	public void setUsuarioActualizarMail(String usuarioActualizarMail) {
		this.usuarioActualizarMail = usuarioActualizarMail;
	}

	public String getUsuarioActualizarCalle() {
		return usuarioActualizarCalle;
	}

	public void setUsuarioActualizarCalle(String usuarioActualizarCalle) {
		this.usuarioActualizarCalle = usuarioActualizarCalle;
	}

	public Long getUsuarioActualizarIdPais() {
		return usuarioActualizarIdPais;
	}

	public void setUsuarioActualizarIdPais(Long usuarioActualizarIdPais) {
		this.usuarioActualizarIdPais = usuarioActualizarIdPais;
	}

	public Long getUsuarioActualizarIdProvincia() {
		return usuarioActualizarIdProvincia;
	}

	public void setUsuarioActualizarIdProvincia(Long usuarioActualizarIdProvincia) {
		this.usuarioActualizarIdProvincia = usuarioActualizarIdProvincia;
	}

	public Long getUsuarioActualizarIdLocalidad() {
		return usuarioActualizarIdLocalidad;
	}

	public void setUsuarioActualizarIdLocalidad(Long usuarioActualizarIdLocalidad) {
		this.usuarioActualizarIdLocalidad = usuarioActualizarIdLocalidad;
	}

	public Long getUsuarioActualizarCuit() {
		return usuarioActualizarCuit;
	}

	public void setUsuarioActualizarCuit(Long usuarioActualizarCuit) {
		this.usuarioActualizarCuit = usuarioActualizarCuit;
	}

	public Long getUsuarioActualizarAltura() {
		return usuarioActualizarAltura;
	}

	public void setUsuarioActualizarAltura(Long usuarioActualizarAltura) {
		this.usuarioActualizarAltura = usuarioActualizarAltura;
	}

	public Long getUsuarioActualizarIdRubro() {
		return usuarioActualizarIdRubro;
	}

	public void setUsuarioActualizarIdRubro(Long usuarioActualizarIdRubro) {
		this.usuarioActualizarIdRubro = usuarioActualizarIdRubro;
	}

	public void setLocalidades(List<SelectItem> localidades) {
		this.localidades = localidades;
	}
}
