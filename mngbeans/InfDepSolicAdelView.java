package com.americacg.cargavirtual.web.mngbeans;

import java.io.Serializable;
import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.model.SelectItem;

import javax.inject.Named;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import javax.xml.ws.WebServiceException;

import org.primefaces.PrimeFaces;
import org.springframework.context.annotation.Scope;

import com.americacg.cargavirtual.gestion.model.BancoContainer;
import com.americacg.cargavirtual.gestion.model.Descripcion;
import com.americacg.cargavirtual.gestion.model.DescripcionContainer;
import com.americacg.cargavirtual.gestion.model.ResInfDepSolicAdel;
import com.americacg.cargavirtual.web.enums.CfgTimeout;
import com.americacg.cargavirtual.web.helpers.GestionServiceHelper;
import com.americacg.cargavirtual.web.helpers.LogACGHelper;
import com.americacg.cargavirtual.web.model.Banco;
import com.americacg.cargavirtual.web.model.Error;
import com.americacg.cargavirtual.web.shared.BasePage;

@Named("infDepSolicAdelView")
@Scope(org.springframework.web.context.WebApplicationContext.SCOPE_SESSION)
public class InfDepSolicAdelView extends BasePage implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 8523062572103734505L;
	private Long tipoMovimiento;
	private Float importeDepAdel;
	private Long idBanco;
	private String comentarioDepAdel;
	private ArrayList<Banco> bancos;
	private List<SelectItem> tipoMovimientos;
	private Long idClienteSolicAdelBMR;

	// Datos adicionales para Depositos
	private Date dep_fechaHora;
	private String dep_nroTicketRef;
	private String dep_numSucursal;
	private Boolean dep_ventanillaBuzon;
	private Boolean dep_esTarjeta;
	private String dep_nroTarjeta;
	private String dep_secuencia;
	private Boolean dep_esCheque;
	private String dep_nroCheque;
	private Long idCobrador;
	private List<SelectItem> cobradores;
	private Long idClienteAacreditar;
	private com.americacg.cargavirtual.gestion.model.Banco bancoXML;
	private Banco banco;

	public Error onLoadInfDepYAdel() {
		Error e = new Error();		
		try {

			if ("M".equals(this.getUsuario().getTipoCliente())) {
				idClienteAacreditar = null;
			} else {
				idClienteAacreditar = this.getUsuario().getIdCliente();
			}

			Banco oBanco = null;

			this.bancos = new ArrayList<Banco>();
			// TODO Revisar pq se ejecuta la consulta de bancos cada vez que cambio de
			// solapa en PuntoDeVenta

			BancoContainer bc = GestionServiceHelper.getGestionService(CfgTimeout.DEFAULT)
					.mostrarBanco(this.getUsuario().getIdMayorista(), null, "A", false);

			if ((bc != null) && (bc.getListBanco() != null) && (bc.getListBanco().getValue() != null)
					&& (bc.getListBanco().getValue().getBanco() != null)
					&& (bc.getListBanco().getValue().getBanco().size() > 0)) {
				/*
				 * for (Banco b : bc.getListBanco().getValue().getBanco()){ this.bancos.add(new
				 * SelectItem(b.getIdBanco(), b.getNombreBanco() + "|" + b.getSucursal() + "|" +
				 * b.getCuenta() + "|" + b.getCBU() + "|" + b.getDescripcion() + "|" )); }
				 */
				for (com.americacg.cargavirtual.gestion.model.Banco oBancoXML : bc.getListBanco().getValue()
						.getBanco()) {
					oBanco = new Banco();
					oBanco.setCBU(oBancoXML.getCBU().getValue());
					oBanco.setCuenta(oBancoXML.getCuenta().getValue());
					oBanco.setCuentaDeTercero(oBancoXML.getCuentaDeTercero().getValue());
					oBanco.setDescInterna(oBancoXML.getDescInterna().getValue());
					oBanco.setDescripcion(oBancoXML.getDescripcion().getValue());
					oBanco.setEstado(oBancoXML.getEstado().getValue());
					oBanco.setIdBanco(oBancoXML.getIdBanco().getValue());
					oBanco.setIdMayorista(oBancoXML.getIdMayorista().getValue());
					oBanco.setIdProveedorDeLaCuentaDeTercero(oBancoXML.getIdProveedorDeLaCuentaDeTercero().getValue());
					oBanco.setNombreBanco(oBancoXML.getNombreBanco().getValue());
					oBanco.setRazSocProveedorDeLaCuentaDeTercero(
							oBancoXML.getRazSocProveedorDeLaCuentaDeTercero().getValue());
					oBanco.setSucursal(oBancoXML.getSucursal().getValue());

					this.bancos.add(oBanco);
				}
			}

			this.cobradores = new ArrayList<SelectItem>();
			DescripcionContainer dc = GestionServiceHelper.getGestionService(CfgTimeout.DEFAULT)
					.mostrarCobradores(this.getUsuario().getIdMayorista(), null);
			for (Descripcion d : dc.getListDescripcion().getValue().getDescripcion()) {
				this.cobradores.add(new SelectItem(d.getId().getValue(), d.getDescripcion().getValue()));
			}

			this.tipoMovimientos = new ArrayList<SelectItem>();
			this.tipoMovimientos.add(new SelectItem(3L, "Transf. Bancaria"));
			this.tipoMovimientos.add(new SelectItem(2L, "Cheque"));
			this.tipoMovimientos.add(new SelectItem(6L, "Solicitud Adelanto"));
			this.tipoMovimientos.add(new SelectItem(7L, "Recaudacion"));
			this.tipoMovimientos.add(new SelectItem(20L, "Deposito en Banco"));
			if (this.getUsuario().getIdEntidad() == 1) {
				this.tipoMovimientos.add(new SelectItem(14L, "Solicitud de Adelanto BMR"));
			}
		} catch (WebServiceException ste) {
			if (ste != null && ste.getCause() != null && ste.getCause().getClass() != null) {
				if (ste.getCause().getClass().equals(ConnectException.class)) {
					e.setError("GST-TOC",
							"No se pudo establecer la comunicación (GST-TOC).\n Por favor intente nuevamente.");
				} else if (ste.getCause().getClass().equals(SocketTimeoutException.class)) {
					e.setError("GST-TRW",
							"No se pudo establecer la comunicación (GST-TRW).\n Por favor intente nuevamente.");
				} else if (ste.getCause().getClass().equals(UnknownHostException.class)) {
					LogACGHelper.escribirLog(null, "No se pudo establecer la comunicación (GST-HNC): |" + ste.getMessage() + "|");
					e.setError("GST-TRW",
							"No se pudo establecer la comunicación (GST-HNC).\n Por favor intente nuevamente.");					
				} else {
					LogACGHelper.escribirLog(null,
							"Informar Deposito y Solicitud de Adelanto. Error ejecutando el WS de consulta: |" + ste.getMessage() + "|");
					e.setError("GST-OTR",
							"No se pudo establecer la comunicación (GST-OTR).\n Por favor intente nuevamente.");
				}
			} else {
				LogACGHelper.escribirLog(null, "Informar Deposito y Solicitud de Adelanto. Error ejecutando el WS de consulta: |"
						+ ste.getMessage() + "|");
				e.setError("GST-OTR",
						"No se pudo establecer la comunicación (GST-OTR).\n Por favor intente nuevamente.");
			}
			FacesContext.getCurrentInstance().validationFailed();
		} catch (Exception ex) {
			LogACGHelper.escribirLog(null,
					"Informar Deposito y Solicitud de Adelanto. Excepcion ejecutando el WS de consulta: |" + ex.getMessage() + "|");
			e.setError("GST-OTR", "No se pudo establecer la comunicación (GST-OTR).\n Por favor intente nuevamente.");
		}
		return e;
	}

	public void altaInfDepSolicAdel() {

		String usuario = this.getUsuario().getUsername();
		String clave = this.getUsuario().getPassword();

		try {
			// System.out.println("tipoMovimiento: |" + tipoMovimiento + "|,
			// idClienteSolicAdelBMR: |" + idClienteSolicAdelBMR + "|");

			if (importeDepAdel == null) {
				FacesContext.getCurrentInstance().addMessage(null,
						new FacesMessage(FacesMessage.SEVERITY_ERROR, "El importe no puede ser null", null));
				FacesContext.getCurrentInstance().validationFailed();
				PrimeFaces.current().executeScript("PF('blockPanelGral').hide()");
				return;
			} else {
				if (importeDepAdel <= 0) {
					FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
							"El importe no puede ser cero ni menor a cero", null));
					FacesContext.getCurrentInstance().validationFailed();
					PrimeFaces.current().executeScript("PF('blockPanelGral').hide()");
					return;
				}
			}

			if (tipoMovimiento == 14 && ("D".equals(this.getUsuario().getTipoCliente()))) {
				if (idClienteSolicAdelBMR == null) {
					FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
							"Debe seleccionar un cliente al cual se le asignara la Solicitud de Adelanto", null));
					FacesContext.getCurrentInstance().validationFailed();
					PrimeFaces.current().executeScript("PF('blockPanelGral').hide()");
					return;
				}

				if (idClienteSolicAdelBMR.compareTo(this.getUsuario().getIdCliente()) == 0) {
					FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
							"El idClienteLogueado no puede ser igual al cliente al que se le asigna la Solicitud de Adelanto",
							null));
					FacesContext.getCurrentInstance().validationFailed();
					PrimeFaces.current().executeScript("PF('blockPanelGral').hide()");
					return;
				}
			}

			if ("P".equals(this.getUsuario().getTipoCliente())) {
				if (idClienteSolicAdelBMR != null) {
					FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
							"Un punto de venta no puede realizar una Solicitud de Adelanto para otro cliente", null));
					FacesContext.getCurrentInstance().validationFailed();
					PrimeFaces.current().executeScript("PF('blockPanelGral').hide()");
					return;
				}
			}

			// ---------------------------------------//
			// Ejecuto insert de Solicitud de Adelato //
			// ---------------------------------------//
			ResInfDepSolicAdel r = new ResInfDepSolicAdel();

			if (dep_fechaHora == null)
				dep_fechaHora = new Date();

			GregorianCalendar gcFechaHoy = new GregorianCalendar();
			gcFechaHoy.setTime(dep_fechaHora);
			XMLGregorianCalendar xmlGCDep_fechaHora = DatatypeFactory.newInstance().newXMLGregorianCalendar(gcFechaHoy);

			Boolean ejecutarInsertConGestionService = false;

			if ((tipoMovimiento == 14) && (idClienteSolicAdelBMR != null)
					&& ("D".equals(this.getUsuario().getTipoCliente())) && (this.getUsuario().getIdEntidad() == 1)) {
				// Si el tipoMovimiento es 14(Solicitud de Adelanto BMR) y el idCliente del BMR
				// a repartir es distinto de null
				// y el cliente logueado es Distribuidor y pertenece a la entidad 1(BMR)

				ejecutarInsertConGestionService = true;
				r = GestionServiceHelper.getGestionService(CfgTimeout.DEFAULT).insertarInfDepSolicAdel(
						this.getUsuario().getIdMayorista(), this.getUsuario().getIdCliente(), idClienteSolicAdelBMR,
						this.getUsuario().getIdUsuario(), usuario, clave, tipoMovimiento,
						(this.getBanco() != null ? this.getBanco().getIdBanco() : null), importeDepAdel,
						comentarioDepAdel, xmlGCDep_fechaHora, "", "", false, false, "", "", false, "", null);

			} else {
				// Si el tipoMovimiento no es 14 (Ejecuto la insercion de solicitud de adelanto
				// de forma convencional)

				if (dep_ventanillaBuzon == null) {
					dep_ventanillaBuzon = false;
				}
				if (dep_esTarjeta == null) {
					dep_esTarjeta = false;
				}
				if (dep_esCheque == null) {
					dep_esCheque = false;
				}

				Long idDistSup = null;
				Long idCliente = null;

				Boolean realizarInsert = true;

				if ("M".equals(this.getUsuario().getTipoCliente())
						|| ("D".equals(this.getUsuario().getTipoCliente())
								&& this.getUsuario().getDistInformarDepositosAnivelesSuperiores())
						|| ("P".equals(this.getUsuario().getTipoCliente())
								&& this.getUsuario().getPvInformarDepositosAnivelesSuperiores())) {

					// Si el cliente logueado es Mayorista
					// o si es Distribuidor y los Distribuidores pueden informar depositos a niveles
					// superiores
					// o si es PV y los PV pueden informar depositos a niveles superiores

					if (idClienteAacreditar == null || idClienteAacreditar <= 0) {
						FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
								"El ID Cliente a Acreditar es invalido", null));
						FacesContext.getCurrentInstance().validationFailed();
						realizarInsert = false;
					}

					if (realizarInsert) {

						idDistSup = GestionServiceHelper.getGestionService(CfgTimeout.DEFAULT)
								.buscaridDistribuidorSuperior(this.getUsuario().getIdMayorista(), idClienteAacreditar);
						idCliente = idClienteAacreditar;

						if (idDistSup == null) {
							FacesContext.getCurrentInstance().addMessage(null,
									new FacesMessage(FacesMessage.SEVERITY_ERROR,
											"El cliente superior no existe, o no es de tipo Distribuidor o PV", null));
							realizarInsert = false;
						}

						if (realizarInsert) {
							if (("D".equals(this.getUsuario().getTipoCliente()))) {

								// Boolean esSubcliente(Long idMayorista, Long idDistribuidor, Long idCliente){

								// VALIDAR QUE EL IDCLIENTE A ACREDITAR SEA CLIENTE SUPERIOR DEL CLIENTE
								// LOGUEADO
								Boolean esSubCliente1 = GestionServiceHelper.getGestionService(CfgTimeout.DEFAULT)
										.esSubcliente(this.getUsuario().getIdMayorista(), idClienteAacreditar,
												this.getUsuario().getIdCliente());

								// VALIDAR QUE IDCLIENTE A ACREDITAR PERTENEZCA A LA JERARQUIA DEL IDCLIENTE
								// LOGUEADO
								Boolean esSubCliente2 = GestionServiceHelper.getGestionService(CfgTimeout.DEFAULT)
										.esSubcliente(this.getUsuario().getIdMayorista(),
												this.getUsuario().getIdCliente(), idClienteAacreditar);

								if (esSubCliente1 || esSubCliente2
										|| idClienteAacreditar.equals(this.getUsuario().getIdCliente())) {
									// OK
								} else {

									FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(
											FacesMessage.SEVERITY_ERROR,
											"El ID Cliente a Acreditar no pertenece a la jerarquia superior o inferior",
											null));
									realizarInsert = false;

								}

							} else if ("P".equals(this.getUsuario().getTipoCliente())) {

								// VALIDAR QUE EL IDCLIENTE A ACREDITAR SEA CLIENTE SUPERIOR DEL CLIENTE
								// LOGUEADO
								Boolean esSubCliente1 = GestionServiceHelper.getGestionService(CfgTimeout.DEFAULT)
										.esSubcliente(this.getUsuario().getIdMayorista(), idClienteAacreditar,
												this.getUsuario().getIdCliente());

								if (esSubCliente1 || idClienteAacreditar.equals(this.getUsuario().getIdCliente())) {
									// OK
								} else {

									FacesContext.getCurrentInstance().addMessage(null,
											new FacesMessage(FacesMessage.SEVERITY_ERROR,
													"El ID Cliente a Acreditar no pertenece a la jerarquia superior",
													null));
									realizarInsert = false;
								}
							}
						}
					}
				} else {
					// Si el cliente logueado es "Distribuidor o Punto de Venta" y la plataforma no
					// esta habilitada para permitir informe de depositos a niveles superiores
					// permito cargar la solicitud del deposito solo a el mismo
					idDistSup = this.getUsuario().getIdDistribuidor();
					idCliente = this.getUsuario().getIdCliente();
				}

				if (realizarInsert) {
					ejecutarInsertConGestionService = true;
					r = GestionServiceHelper.getGestionService(CfgTimeout.DEFAULT).insertarInfDepSolicAdel(
							this.getUsuario().getIdMayorista(), idDistSup, idCliente, this.getUsuario().getIdUsuario(),
							usuario, clave, tipoMovimiento,
							(this.getBanco() != null ? this.getBanco().getIdBanco() : null), importeDepAdel,
							comentarioDepAdel, xmlGCDep_fechaHora, dep_nroTicketRef, dep_numSucursal,
							dep_ventanillaBuzon, dep_esTarjeta, dep_nroTarjeta, dep_secuencia, dep_esCheque,
							dep_nroCheque, idCobrador);
				}
			}

			if (ejecutarInsertConGestionService) {
				// Solo muestro el mensaje de OK o Error si se ejecuto el insert con
				// GestionService
				if (r.getError() != null && r.getError().getValue().getHayError().getValue()) {
					FacesContext.getCurrentInstance().addMessage(null,
							new FacesMessage(FacesMessage.SEVERITY_ERROR,
									"Error Insertando el registro: |"
											+ r.getError().getValue().getCodigoError().getValue() + ","
											+ r.getError().getValue().getMsgError().getValue() + "|",
									null));
					FacesContext.getCurrentInstance().validationFailed();
				} else {
					FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,
							"Solicitud OK. Su numero de comprobante es: |" + r.getIdDepAdelanto().getValue() + "|",
							null));

					PrimeFaces.current().executeScript(
							"ACGSiteScriptsFCNS.cambiaFondoMsg('msgsProductos', '#212121', '#9ed5a0', '#a0d4b7', '"
									+ FacesContext.getCurrentInstance().getExternalContext().getRequestContextPath()
											.replace("/", "\\/")
									+ "\\/javax.faces.resource\\/images\\/24x24\\/operacionOK.png')");

					limpiarCampos(true);
				}
			}
		
		} catch (WebServiceException ste) {
			if(ste != null && ste.getCause() != null && ste.getCause().getClass() != null) {
				if (ste.getCause().getClass().equals(ConnectException.class)) {
					FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "No se pudo establecer la comunicación (GST-TOC).\n Por favor intente nuevamente.", null));
				}else if (ste.getCause().getClass().equals(SocketTimeoutException.class)) {
					FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "No se pudo establecer la comunicación (GST-TRW).\n Por favor intente nuevamente.", null));
				} else if (ste.getCause().getClass().equals(UnknownHostException.class)) {
					LogACGHelper.escribirLog(null, "No se pudo establecer la comunicación (GST-HNC): |" + ste.getMessage() + "|");
					FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
							"No se pudo establecer la comunicación (GST-HNC).\n Por favor intente nuevamente.", null));					
				}else {
					LogACGHelper.escribirLog(null, "Error ejecutando el WS de Informe de Deposito o Solicitud de Adelanto: |" + ste.getMessage() + "|");
					FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error ejecutando el WS de Informe de Deposito o Solicitud de Adelanto: |" + ste.getMessage() + "|", null));
				}
			}else {
				LogACGHelper.escribirLog(null, "Error ejecutando el WS de Informe de Deposito o Solicitud de Adelanto: |" + ste.getMessage() + "|");				
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error ejecutando el WS de Informe de Deposito o Solicitud de Adelanto: |" + ste.getMessage() + "|", null));
			}
			FacesContext.getCurrentInstance().validationFailed();
		} catch (Exception e) {
			LogACGHelper.escribirLog(null, "Error ejecutando el WS de Informe de Deposito o Solicitud de Adelanto: |" + e.getMessage() + "|");	
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, 
					"Error ejecutando el WS de Informe de Deposito o Solicitud de Adelanto: |" + e.getMessage() + "|", null));
			FacesContext.getCurrentInstance().validationFailed();
		}
		PrimeFaces.current().executeScript("PF('blockPanelGral').hide()");
		return;			
	}	

	public void limpiarCamposAE() {
		limpiarCampos(false);
	}

	private void limpiarCampos(Boolean asignarMovimientoPorDefecto) {
		if (asignarMovimientoPorDefecto) {
			tipoMovimiento = 3L;
		}
		importeDepAdel = null;
		idBanco = null;
		this.setBanco(null);
		comentarioDepAdel = "";
		idClienteSolicAdelBMR = null;
		dep_fechaHora = new Date();
		dep_nroTicketRef = "";
		dep_numSucursal = "";
		dep_ventanillaBuzon = false;
		dep_esTarjeta = false;
		dep_nroTarjeta = "";
		dep_secuencia = "";
		dep_esCheque = false;
		dep_nroCheque = "";
		idCobrador = null;

		if ("M".equals(this.getUsuario().getTipoCliente())) {
			idClienteAacreditar = null;
		} else {
			idClienteAacreditar = this.getUsuario().getIdCliente();
		}

	}

	public void cancelarAltaInfDepSolicAdel(ActionEvent ev) {
		try {
			limpiarCampos(true);
			PrimeFaces.current().executeScript("PF('panelProductosWV').close()");
		} catch (RuntimeException e) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
					"Error limpiando la pantalla: " + e.getMessage(), null));
		}
	}

	public Float getImporteDepAdel() {
		return importeDepAdel;
	}

	public void setImporteDepAdel(Float importeDepAdel) {
		this.importeDepAdel = importeDepAdel;
	}

	public String getComentarioDepAdel() {
		return comentarioDepAdel;
	}

	public void setComentarioDepAdel(String comentarioDepAdel) {
		this.comentarioDepAdel = comentarioDepAdel;
	}

	public Long getTipoMovimiento() {
		return tipoMovimiento;
	}

	public void setTipoMovimiento(Long tipoMovimiento) {
		this.tipoMovimiento = tipoMovimiento;
	}

	public ArrayList<Banco> getBancos() {
		return bancos;
	}

	public void setBancos(ArrayList<Banco> bancos) {
		this.bancos = bancos;
	}

	public Long getIdBanco() {
		return idBanco;
	}

	public void setIdBanco(Long idBanco) {
		this.idBanco = idBanco;
	}

	public List<SelectItem> getTipoMovimientos() {
		return tipoMovimientos;
	}

	public void setTipoMovimientos(List<SelectItem> tipoMovimientos) {
		this.tipoMovimientos = tipoMovimientos;
	}

	public Long getIdClienteSolicAdelBMR() {
		return idClienteSolicAdelBMR;
	}

	public void setIdClienteSolicAdelBMR(Long idClienteSolicAdelBMR) {
		this.idClienteSolicAdelBMR = idClienteSolicAdelBMR;
	}

	public Date getDep_fechaHora() {
		return dep_fechaHora;
	}

	public void setDep_fechaHora(Date dep_fechaHora) {
		this.dep_fechaHora = dep_fechaHora;
	}

	public String getDep_nroTicketRef() {
		return dep_nroTicketRef;
	}

	public void setDep_nroTicketRef(String dep_nroTicketRef) {
		this.dep_nroTicketRef = dep_nroTicketRef;
	}

	public String getDep_numSucursal() {
		return dep_numSucursal;
	}

	public void setDep_numSucursal(String dep_numSucursal) {
		this.dep_numSucursal = dep_numSucursal;
	}

	public Boolean getDep_esTarjeta() {
		return dep_esTarjeta;
	}

	public void setDep_esTarjeta(Boolean dep_esTarjeta) {
		this.dep_esTarjeta = dep_esTarjeta;
	}

	public String getDep_nroTarjeta() {
		return dep_nroTarjeta;
	}

	public void setDep_nroTarjeta(String dep_nroTarjeta) {
		this.dep_nroTarjeta = dep_nroTarjeta;
	}

	public String getDep_secuencia() {
		return dep_secuencia;
	}

	public void setDep_secuencia(String dep_secuencia) {
		this.dep_secuencia = dep_secuencia;
	}

	public Boolean getDep_ventanillaBuzon() {
		return dep_ventanillaBuzon;
	}

	public void setDep_ventanillaBuzon(Boolean dep_ventanillaBuzon) {
		this.dep_ventanillaBuzon = dep_ventanillaBuzon;
	}

	public Boolean getDep_esCheque() {
		return dep_esCheque;
	}

	public void setDep_esCheque(Boolean dep_esCheque) {
		this.dep_esCheque = dep_esCheque;
	}

	public String getDep_nroCheque() {
		return dep_nroCheque;
	}

	public void setDep_nroCheque(String dep_nroCheque) {
		this.dep_nroCheque = dep_nroCheque;
	}

	public Long getIdCobrador() {
		return idCobrador;
	}

	public void setIdCobrador(Long idCobrador) {
		this.idCobrador = idCobrador;
	}

	public List<SelectItem> getCobradores() {
		return cobradores;
	}

	public void setCobradores(List<SelectItem> cobradores) {
		this.cobradores = cobradores;
	}

	public Long getIdClienteAacreditar() {
		return idClienteAacreditar;
	}

	public void setIdClienteAacreditar(Long idClienteAacreditar) {
		this.idClienteAacreditar = idClienteAacreditar;
	}

	public com.americacg.cargavirtual.gestion.model.Banco getBancoXML() {
		return bancoXML;
	}

	public void setBancoXML(com.americacg.cargavirtual.gestion.model.Banco banco) {
		this.bancoXML = banco;
	}

	public Banco getBanco() {
		return banco;
	}

	public void setBanco(Banco banco) {
		this.banco = banco;
	}
}
