package com.americacg.cargavirtual.web.shared;

import java.io.Serializable;
import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.xml.ws.WebServiceException;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.collections.comparators.NullComparator;
import org.apache.commons.collections.comparators.ReverseComparator;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import com.americacg.cargavirtual.gestion.model.HeaderSecur;
import com.americacg.cargavirtual.gestion.model.ObjectFactory;
import com.americacg.cargavirtual.gestion.model.Publicidad;
import com.americacg.cargavirtual.web.enums.CfgTimeout;
import com.americacg.cargavirtual.web.helpers.GestionServiceHelper;
import com.americacg.cargavirtual.web.helpers.LogACGHelper;
import com.americacg.cargavirtual.web.model.Usuario;

public class BasePage implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4233521377661288429L;

	protected final Log log = LogFactory.getLog(getClass());

	protected SimpleMailMessage message;
	protected String templateName;
	protected FacesContext facesContext;
	protected String sortColumn;
	protected boolean ascending;
	protected boolean nullsAreHigh;
	protected Authentication authentication;
	protected String publicidad;

	public FacesContext getFacesContext() {
		return FacesContext.getCurrentInstance();
	}

	// Convenience methods ====================================================
	public String getParameter(String name) {
		return getRequest().getParameter(name);
	}

	public String getBundleName() {
		return getFacesContext().getApplication().getMessageBundle();
	}

	public ResourceBundle getBundle() {
		ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
		return ResourceBundle.getBundle(getBundleName(), getRequest().getLocale(), classLoader);
	}

	public String getText(String key) {
		String message;

		try {
			message = getBundle().getString(key);
		} catch (java.util.MissingResourceException mre) {
			log.warn("Missing key for '" + key + "'");
			return "???" + key + "???";
		}

		return message;
	}

	public String getText(String key, Object arg) {
		if (arg == null) {
			return getText(key);
		}

		MessageFormat form = new MessageFormat(getBundle().getString(key));

		if (arg instanceof String) {
			return form.format(new Object[] { arg });
		} else if (arg instanceof Object[]) {
			return form.format(arg);
		} else {
			log.error("arg '" + arg + "' not String or Object[]");

			return "";
		}
	}

	@SuppressWarnings("unchecked")
	protected void addMessage(String key, Object arg) {
		// JSF Success Messages won't live past a redirect, so it's not used
		// FacesUtils.addInfoMessage(formatMessage(key, arg));
		List<String> messages = (List<String>) getSession().getAttribute("messages");

		if (messages == null) {
			messages = new ArrayList<String>();
		}

		messages.add(getText(key, arg));
		getSession().setAttribute("messages", messages);
	}

	protected void addMessage(String key) {
		addMessage(key, null);
	}

	@SuppressWarnings("unchecked")
	protected void addError(String key, Object arg) {
		// The "JSF Way" doesn't allow you to put HTML in your error messages, so I
		// don't use it.
		// FacesContext.getCurrentInstance().addMessage(null, new
		// FacesMessage(FacesMessage.SEVERITY_ERROR, formatMessage(key, arg), null);
		List<String> errors = (List) getSession().getAttribute("errors");

		if (errors == null) {
			errors = new ArrayList<String>();
		}

		// if key contains a space, don't look it up, it's likely a raw message
		if (key.contains(" ") && arg == null) {
			errors.add(key);
		} else {
			errors.add(getText(key, arg));
		}

		getSession().setAttribute("errors", errors);
	}

	protected void addError(String key) {
		addError(key, null);
	}

	/**
	 * Convenience method for unit tests.
	 * 
	 * @return boolean indicator of an "errors" attribute in the session
	 */
	public boolean hasErrors() {
		return (getSession().getAttribute("errors") != null);
	}

	/**
	 * Servlet API Convenience method
	 * 
	 * @return HttpServletRequest from the FacesContext
	 */
	protected HttpServletRequest getRequest() {
		return (HttpServletRequest) getFacesContext().getExternalContext().getRequest();
	}

	/**
	 * Servlet API Convenience method
	 * 
	 * @return the current user's session
	 */
	protected HttpSession getSession() {
		return getRequest().getSession();
	}

	/**
	 * Servlet API Convenience method
	 * 
	 * @return HttpServletResponse from the FacesContext
	 */
	protected HttpServletResponse getResponse() {
		return (HttpServletResponse) getFacesContext().getExternalContext().getResponse();
	}

	/**
	 * Servlet API Convenience method
	 * 
	 * @return the ServletContext form the FacesContext
	 */
	protected ServletContext getServletContext() {
		return (ServletContext) getFacesContext().getExternalContext().getContext();
	}

	/**
	 * Convenience method to get the Configuration HashMap from the servlet context.
	 *
	 * @return the user's populated form from the session
	 */
	protected Map getConfiguration() {
		Map config = (HashMap) getServletContext().getAttribute(Constants.CONFIG);

		// so unit tests don't puke when nothing's been set
		if (config == null) {
			return new HashMap();
		}

		return config;
	}

	public void setMessage(SimpleMailMessage message) {
		this.message = message;
	}

	public void setTemplateName(String templateName) {
		this.templateName = templateName;
	}

	// The following methods are used by t:dataTable for sorting.
	public String getSortColumn() {
		return sortColumn;
	}

	public void setSortColumn(String sortColumn) {
		this.sortColumn = sortColumn;
	}

	public boolean isAscending() {
		return ascending;
	}

	public void setAscending(boolean ascending) {
		this.ascending = ascending;
	}

	/**
	 * Sort list according to which column has been clicked on.
	 * 
	 * @param list
	 *            the java.util.List to sort
	 * @return ordered list
	 */
	@SuppressWarnings("unchecked")
	protected List<?> sort(List<?> list) {
		Comparator comparator = new BeanComparator(sortColumn, new NullComparator(nullsAreHigh));
		if (!ascending) {
			comparator = new ReverseComparator(comparator);
		}
		Collections.sort(list, comparator);
		return list;
	}

	public void addGrowlMessage(String summary) {
		FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, summary, null);
		FacesContext.getCurrentInstance().addMessage(null, message);
	}

	public void addGrowlErrorMessage(String summary) {
		FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, summary, null);
		FacesContext.getCurrentInstance().addMessage(null, message);
	}

	public Usuario getUsuario() {
		return (Usuario) SecurityContextHolder.getContext().getAuthentication().getDetails();
	}

	public HeaderSecur getHeaderSeguridad() {
		ObjectFactory oGestionObjFac = new ObjectFactory();
		HeaderSecur oHeadSec = new HeaderSecur();
		
		try {
			oHeadSec.setIdMayorista(oGestionObjFac.createHeaderSecurIdMayorista(this.getUsuario().getIdMayorista()));
			oHeadSec.setIdCliente(oGestionObjFac.createHeaderSecurIdCliente(this.getUsuario().getIdCliente()));
			oHeadSec.setIdUsuario(oGestionObjFac.createHeaderSecurIdUsuario(this.getUsuario().getIdUsuario()));
			oHeadSec.setUsuario(oGestionObjFac.createHeaderSecurUsuario(this.getUsuario().getUsername()));
			oHeadSec.setPassword(oGestionObjFac.createHeaderSecurPassword(this.getUsuario().getPassword()));
		}
		catch(Exception e) {
			LogACGHelper.escribirLog(null, "Se produjo un error al obtener el header de seguridad: |" + e.getMessage() + "|");
		}
		
		return oHeadSec;
	}
	
	public String getPublicidad() {

		if (publicidad == null) {
			buscarPublicidad();
		}
		return publicidad;
	}

	public void setPublicidad(String publicidad) {
		this.publicidad = publicidad;
	}

	public void expandirContraerPublicidad() {

		buscarPublicidad();

	}

	private void buscarPublicidad() {

		try {

			Publicidad pub = GestionServiceHelper.getGestionService(CfgTimeout.CONSULTA).refreshPublicidad(
					this.getUsuario().getIdMayorista(), this.getUsuario().getTipoCliente(), 1L,
					this.getUsuario().getIdDistribuidor(), this.getUsuario().getIdCliente());

			if (pub != null) {
				publicidad = pub.getPublicidad().getValue().replaceAll("\"../imagenes_publicidad/", "\"../../../imagenes_publicidad/");
			} else {
				// NO SE ENCONTRO LA PUBLICIDAD
				// System.out.println("No se encontro la Publicidad");
			}
		} catch (WebServiceException ste) {
			if(ste != null && ste.getCause() != null && ste.getCause().getClass() != null) {
				if (ste.getCause().getClass().equals(ConnectException.class)) {
					FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "No se pudo establecer la comunicación (GST-TOC), refresh de Publicidad.\n Por favor intente nuevamente.", null));
				}else if (ste.getCause().getClass().equals(SocketTimeoutException.class)) {
					FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "No se pudo establecer la comunicación (GST-TRW), refresh de Publicidad.\n Por favor intente nuevamente.", null));
				} else if (ste.getCause().getClass().equals(UnknownHostException.class)) {
					LogACGHelper.escribirLog(null, "No se pudo establecer la comunicación (GST-HNC): |" + ste.getMessage() + "|");
					FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
							"No se pudo establecer la comunicación (GST-HNC).\n Por favor intente nuevamente.", null));					
				}else {
					LogACGHelper.escribirLog(null, "BasePage. Error ejecutando el WS de consulta de publicidad: |" + ste.getMessage() + "|");
					FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error ejecutando el WS de consulta: |" + ste.getMessage() + "|", null));
				}
			}else {
				LogACGHelper.escribirLog(null, "BasePage. Error ejecutando el WS de consulta de publicidad: |" + ste.getMessage() + "|");				
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error ejecutando el WS de consulta: |" + ste.getMessage() + "|", null));
			}
			FacesContext.getCurrentInstance().validationFailed();
		} catch (Exception e) {
			LogACGHelper.escribirLog(null, "BasePage. Excepcion ejecutando el WS de consulta de publicidad: |" + e.getMessage() + "|");	
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, 
					"Error ejecutando el refresh de la Publicidad: |" + e.getMessage() + "|", null));
			FacesContext.getCurrentInstance().validationFailed();
		}
		return;			
	}				

}
