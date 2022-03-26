package com.americacg.cargavirtual.web.mngbeans;

import java.io.Serializable;
import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;

import javax.inject.Named;
import javax.xml.ws.WebServiceException;

import org.springframework.context.annotation.Scope;

import com.americacg.cargavirtual.gestion.model.Banner;
import com.americacg.cargavirtual.gestion.model.Publicidad;
import com.americacg.cargavirtual.gestion.model.ResultadoBase;
import com.americacg.cargavirtual.web.enums.CfgTimeout;
import com.americacg.cargavirtual.web.helpers.GestionServiceHelper;
import com.americacg.cargavirtual.web.helpers.LogACGHelper;
import com.americacg.cargavirtual.web.shared.BasePage;

@Named("mensajesView")
@Scope(org.springframework.web.context.WebApplicationContext.SCOPE_SESSION)
public class MensajesView extends BasePage implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3625309911247204739L;
	private Long idMayorista;
	private String bannerWEB;
	private String bannerPC;
	private String bannerAS;
	private String publicidadWeb;
	private String bannerIVR;
	private String bannerWap;
	private String bannerJava;
	private String bannerPOS;
	private String colorBannerWeb;
	private String colorBannerPC;
	private String colorBannerAS;

	// -----------------------------------------------------------------------------------------------------------
	// CARGAR BANNERS
	// -----------------------------------------------------------------------------------------------------------
	public void cargarBannerWeb() {
		cargarBanner(1L);
	}

	public void cargarBannerPC() {

		cargarBanner(10L);
	}

	public void cargarBannerAS() {

		cargarBanner(9L);
	}

	public void cargarBannerIvr() {

		cargarBanner(2L);
	}

	public void cargarBannerWap() {

		cargarBanner(3L);
	}

	public void cargarBannerJAVA() {

		cargarBanner(6L);
	}

	public void cargarBannerPOS() {

		cargarBanner(4L);
	}

	private void cargarBanner(Long idTipTer) {
		try {

			Long cli = null;
			if (("M".equals(this.getUsuario().getTipoCliente())) || ("D".equals(this.getUsuario().getTipoCliente()))) {
				cli = this.getUsuario().getIdCliente();

			} else {
				if ("P".equals(this.getUsuario().getTipoCliente())) {
					cli = this.getUsuario().getIdDistribuidor();
				} else {
					FacesContext.getCurrentInstance().addMessage(null,
							new FacesMessage(FacesMessage.SEVERITY_ERROR,
									"El tipo de cliente no es valido para mostrar el banner: |"
											+ this.getUsuario().getTipoCliente() + "|",
									null));
					return;
				}
			}

			Banner banner = GestionServiceHelper.getGestionService(CfgTimeout.DEFAULT)
					.mostrarBannerParaAdm(this.getUsuario().getIdMayorista(), idTipTer, cli);

			if (banner != null) {
				if (idTipTer == 1) {
					bannerWEB = banner.getTextoBanner().getValue();
					this.colorBannerWeb = banner.getColor().getValue();
				} else {
					if (idTipTer == 2) {
						bannerIVR = banner.getTextoBanner().getValue();
					} else {
						if (idTipTer == 3) {
							bannerWap = banner.getTextoBanner().getValue();
						} else {
							if (idTipTer == 4) {
								bannerPOS = banner.getTextoBanner().getValue();
							} else {
								if (idTipTer == 9) {
									bannerAS = banner.getTextoBanner().getValue();
									this.colorBannerAS = banner.getColor().getValue();
								} else {
									if (idTipTer == 10) {
										bannerPC = banner.getTextoBanner().getValue();
										this.colorBannerPC = banner.getColor().getValue();
									}
								}
							}
						}
					}
				}
			} else {
				FacesContext.getCurrentInstance().addMessage(null,
						new FacesMessage(FacesMessage.SEVERITY_ERROR,
								"No existe banner cargado. Los clientes visualizarán el banner del Mayorista."
										+ " Puede cargar un banner para su estructura",
								null));
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
					LogACGHelper.escribirLog(null, "No se pudo establecer la comunicación (GST-HNC): |" + ste.getMessage() + "|");
					FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
							"No se pudo establecer la comunicación (GST-HNC).\n Por favor intente nuevamente.", null));					
				} else {
					LogACGHelper.escribirLog(null,
							"Error al tratar de mostrar el Banner. Error: |" + ste.getMessage() + "|");
					FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
							"Error al tratar de mostrar el Banner. Error: |" + ste.getMessage() + "|", null));
				}
			} else {
				LogACGHelper.escribirLog(null,
						"Error al tratar de mostrar el Banner. Error: |" + ste.getMessage() + "|");
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
						"Error al tratar de mostrar el Banner. Error: |" + ste.getMessage() + "|", null));
			}
		} catch (Exception e) {
			LogACGHelper.escribirLog(null, "Error al tratar de mostrar el Banner. Error: |" + e.getMessage() + "|");
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
					"Error al tratar de mostrar el Banner. Error: |" + e.getMessage() + "|", null));
			FacesContext.getCurrentInstance().validationFailed();
		}
		return;
	}

	// -----------------------------------------------------------------------------------------------------------
	// CARGAR PUBLICIDAD WEB
	// -----------------------------------------------------------------------------------------------------------
	public void cargarPublicidadWeb() {

		try {
			Long cli = null;
			if (("M".equals(this.getUsuario().getTipoCliente())) || ("D".equals(this.getUsuario().getTipoCliente()))) {
				cli = this.getUsuario().getIdCliente();

			} else {
				if ("P".equals(this.getUsuario().getTipoCliente())) {
					cli = this.getUsuario().getIdDistribuidor();
				} else {
					FacesContext.getCurrentInstance().addMessage(null,
							new FacesMessage(FacesMessage.SEVERITY_ERROR,
									"El tipo de cliente no es valido para mostrar el banner: |"
											+ this.getUsuario().getTipoCliente() + "|",
									null));
					return;
				}
			}

			Publicidad publicidad = GestionServiceHelper.getGestionService(CfgTimeout.DEFAULT)
					.mostrarPublicidadParaAdm(this.getUsuario().getIdMayorista(), 1L, cli);

			if (publicidad != null) {
				publicidadWeb = publicidad.getPublicidad().getValue();
			} else {
				FacesContext.getCurrentInstance().addMessage(null,
						new FacesMessage(FacesMessage.SEVERITY_ERROR,
								"No existe Publicidad cargada. Los clientes visualizarán la publicidad del Mayorista."
										+ " Puede cargar una publicidad para su estructura",
								null));
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
					LogACGHelper.escribirLog(null, "No se pudo establecer la comunicación (GST-HNC): |" + ste.getMessage() + "|");
					FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
							"No se pudo establecer la comunicación (GST-HNC).\n Por favor intente nuevamente.", null));					
				} else {
					LogACGHelper.escribirLog(null,
							"Error al tratar de mostrar la Publicidad. Error: |" + ste.getMessage() + "|");
					FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
							"Error al tratar de mostrar la Publicidad. Error: |" + ste.getMessage() + "|", null));
				}
			} else {
				LogACGHelper.escribirLog(null,
						"Error al tratar de mostrar la Publicidad. Error: |" + ste.getMessage() + "|");
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
						"Error al tratar de mostrar la Publicidad. Error: |" + ste.getMessage() + "|", null));
			}
		} catch (Exception e) {
			LogACGHelper.escribirLog(null, "Error al tratar de mostrar la Publicidad. Error: |" + e.getMessage() + "|");
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
					"Error al tratar de mostrar la Publicidad. Error: |" + e.getMessage() + "|", null));
			FacesContext.getCurrentInstance().validationFailed();
		}
		return;
	}

	// -----------------------------------------------------------------------------------------------------------
	// ACTUALIZACION BANNERS
	// -----------------------------------------------------------------------------------------------------------
	public void actualizarBannerWeb(ActionEvent evt) {

		actualizarBanner(1L, bannerWEB, colorBannerWeb);
		return;

	}

	public void actualizarBannerIvr(ActionEvent evt) {

		actualizarBanner(2L, bannerIVR, "");
		return;

	}

	public void actualizarBannerWap(ActionEvent evt) {

		actualizarBanner(3L, bannerWap, "");
		return;

	}

	public void actualizarBannerJAVA(ActionEvent evt) {

		actualizarBanner(6L, bannerJava, "");
		return;

	}

	public void actualizarBannerAS(ActionEvent evt) {

		actualizarBanner(9L, bannerAS, colorBannerAS);
		return;

	}

	public void actualizarBannerPC(ActionEvent evt) {

		actualizarBanner(10L, bannerPC, colorBannerPC);
		return;

	}

	public void actualizarBannerPOS(ActionEvent evt) {

		actualizarBanner(4L, bannerPOS, "");
		return;
	}

	public void actualizarBanner(Long idTipTer, String banner, String color) {
		try {
			ResultadoBase rb;

			// System.out.println("Actualizar el banner con el siguiente texto: |" + banner
			// + "|");
			// System.out.println("Actualizar el banner con el siguiente color: |" + color +
			// "|");

			rb = GestionServiceHelper.getGestionService(CfgTimeout.DEFAULT).actualizarBanner(
					this.getUsuario().getIdMayorista(), banner, idTipTer, color, this.getUsuario().getIdCliente());

			if (rb.getError().getValue().getHayError().getValue()) {
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
						"Error actualizando el banner. " + rb.getError().getValue().getMsgError().getValue(), null));
			} else {
				FacesContext.getCurrentInstance().addMessage(null,
						new FacesMessage(FacesMessage.SEVERITY_INFO, "Banner actualizado Correctamente", null));
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
					LogACGHelper.escribirLog(null, "No se pudo establecer la comunicación (GST-HNC): |" + ste.getMessage() + "|");
					FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
							"No se pudo establecer la comunicación (GST-HNC).\n Por favor intente nuevamente.", null));					
				} else {
					LogACGHelper.escribirLog(null, "Error actualizando el Banner. Error: |" + ste.getMessage() + "|");
					FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
							"Error actualizando el Banner. Error: |" + ste.getMessage() + "|", null));
				}
			} else {
				LogACGHelper.escribirLog(null, "Error actualizando el Banner. Error: |" + ste.getMessage() + "|");
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
						"Error actualizando el Banner. Error: |" + ste.getMessage() + "|", null));
			}
			FacesContext.getCurrentInstance().validationFailed();
		} catch (Exception e) {
			LogACGHelper.escribirLog(null, "Error actualizando el Banner. Error: |" + e.getMessage() + "|");
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
					"Error actualizando el Banner. Error: |" + e.getMessage() + "|", null));
			FacesContext.getCurrentInstance().validationFailed();
		}
		return;
	}

	// -----------------------------------------------------------------------------------------------------------
	// ACTUALIZACION PUBLICIDADES
	// -----------------------------------------------------------------------------------------------------------
	public void actualizarPublicidadWeb(ActionEvent evt) {

		try {
			ResultadoBase rb;

			// System.out.println("Actualizar la publicidad con el siguiente texto: |" +
			// publicidadWeb + "|");

			rb = GestionServiceHelper.getGestionService(CfgTimeout.DEFAULT).actualizarPublicidad(
					this.getUsuario().getIdMayorista(), publicidadWeb, 1L, this.getUsuario().getIdCliente());

			if (rb.getError().getValue().getHayError().getValue()) {
				FacesContext.getCurrentInstance().addMessage(null,
						new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error actualizando la publicidad", null));
			} else {
				FacesContext.getCurrentInstance().addMessage(null,
						new FacesMessage(FacesMessage.SEVERITY_INFO, "Publicidad actualizada Correctamente", null));
			}
		} catch (WebServiceException ste) {
			if (ste.getCause().getClass().equals(SocketTimeoutException.class)) {
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
						"No se pudo establecer la comunicación con GESTION.\n Por favor intente nuevamente.", null));
			} else {
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
						"Error realizando consulta contra GESTION: " + ste.getMessage(), null));
			}
		} catch (RuntimeException e) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
					"Error actualizando la Publicidad. Error:" + e.getMessage(), null));
		}
		return;
	}

	// -----------------------------------------------------------------------------------------------------------
	// GETTERS AND SETTERS
	// -----------------------------------------------------------------------------------------------------------

	public Long getIdMayorista() {
		return idMayorista;
	}

	public void setIdMayorista(Long idMayorista) {
		this.idMayorista = idMayorista;
	}

	public String getBannerIVR() {
		return bannerIVR;
	}

	public void setBannerIVR(String bannerIVR) {
		this.bannerIVR = bannerIVR;
	}

	public String getBannerWap() {
		return bannerWap;
	}

	public void setBannerWap(String bannerWap) {
		this.bannerWap = bannerWap;
	}

	public String getBannerWEB() {
		return bannerWEB;
	}

	public void setBannerWEB(String bannerWEB) {
		this.bannerWEB = bannerWEB;
	}

	public String getPublicidadWeb() {
		return publicidadWeb;
	}

	public void setPublicidadWeb(String publicidadWeb) {
		this.publicidadWeb = publicidadWeb;
	}

	public String getBannerPOS() {
		return bannerPOS;
	}

	public void setBannerPOS(String bannerPOS) {
		this.bannerPOS = bannerPOS;
	}

	public String getBannerJava() {
		return bannerJava;
	}

	public void setBannerJava(String bannerJava) {
		this.bannerJava = bannerJava;
	}

	public String getBannerPC() {
		return bannerPC;
	}

	public void setBannerPC(String bannerPC) {
		this.bannerPC = bannerPC;
	}

	public String getBannerAS() {
		return bannerAS;
	}

	public void setBannerAS(String bannerAS) {
		this.bannerAS = bannerAS;
	}

	public String getColorBannerWeb() {
		return colorBannerWeb;
	}

	public void setColorBannerWeb(String colorBannerWeb) {
		this.colorBannerWeb = colorBannerWeb;
	}

	public String getColorBannerPC() {
		return colorBannerPC;
	}

	public void setColorBannerPC(String colorBannerPC) {
		this.colorBannerPC = colorBannerPC;
	}

	public String getColorBannerAS() {
		return colorBannerAS;
	}

	public void setColorBannerAS(String colorBannerAS) {
		this.colorBannerAS = colorBannerAS;
	}

}
