package com.americacg.cargavirtual.web.mngbeans;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.time.Year;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.inject.Named;
import javax.xml.ws.WebServiceException;
import org.primefaces.PrimeFaces;
import org.springframework.context.annotation.Scope;
import com.americacg.cargavirtual.gateway.pagoElectronico.helper.GatewayHelper;
import com.americacg.cargavirtual.gateway.pagoElectronico.model.entidad.PlanDeCuota;
import com.americacg.cargavirtual.gateway.pagoElectronico.model.MensajeOutboundGateway;
import com.americacg.cargavirtual.gateway.pagoElectronico.model.entidad.ConceptoDePago;
import com.americacg.cargavirtual.gateway.pagoElectronico.model.entidad.MedioDePago;
import com.americacg.cargavirtual.gateway.pagoElectronico.model.entidad.Pago;
import com.americacg.cargavirtual.gateway.pagoElectronico.model.parametros.funciones.output.DataOutputFcnANPT;
import com.americacg.cargavirtual.gateway.pagoElectronico.model.parametros.funciones.output.DataOutputFcnCOPA;
import com.americacg.cargavirtual.gateway.pagoElectronico.model.parametros.funciones.output.DataOutputFcnGPCU;
import com.americacg.cargavirtual.gateway.pagoElectronico.model.parametros.funciones.output.DataOutputFcnLICP;
import com.americacg.cargavirtual.gateway.pagoElectronico.model.parametros.funciones.output.DataOutputFcnPAGO;
import com.americacg.cargavirtual.gateway.pagoElectronico.model.parametros.funciones.output.DataOutputFcnVABI;
import com.americacg.cargavirtual.gestion.model.FiltroListadoPlatTipoDocumento;
import com.americacg.cargavirtual.gestion.model.ObjectFactory;
import com.americacg.cargavirtual.gestion.model.PlatTipoDocumento;
import com.americacg.cargavirtual.gestion.model.PlatTipoDocumentoList;
import com.americacg.cargavirtual.gestion.service.ArrayOfString;
import com.americacg.cargavirtual.web.enums.CfgTimeout;
import com.americacg.cargavirtual.web.handlers.HandledException;
import com.americacg.cargavirtual.web.helpers.GestionServiceHelper;
import com.americacg.cargavirtual.web.helpers.LogACGHelper;
import com.americacg.cargavirtual.web.helpers.PagoElectronicoHelper;
import com.americacg.cargavirtual.web.model.TicketPagoElectronico;
import com.americacg.cargavirtual.web.shared.BasePage;

@Named("pagoElectronicoView")
@Scope(org.springframework.web.context.WebApplicationContext.SCOPE_SESSION)
public class PagoElectronicoView extends BasePage implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4632176115821243509L;

	private String numeroTicketACG = "";
	private Integer paymentId = null;
	private Long idPagoACG = 0L;
	private Date fechaDesde = new Date();
	private Date fechaHasta = new Date();
	private String tarjetaNumero = "";
	private String tarjetaExpiracion = "";
	private String tarjetaExpiracionMes = "";
	private String tarjetaExpiracionAnio = "";
	private String codigoSeguridad = "";
	private String tarjetaTitularNombre = "";
	private String tipoIdentificacion = "";
	private String numeroIdentificacion = "";
	private Float peimporte = null;
	private String moneda = "ARS";
	private PlanDeCuota planDeCuota = null;
	private MedioDePago medioDePago = null;
	private Pago pago = null;
	private String telefonoCliente = "";
	private String emailCliente = "";
	private Long idConceptoDePago = 0L;
	private String datosAdicionalesPago = "";
	private Integer idAccion = 0;
	private Long idBancoPagoElectronico = null;
	private String idPagoOperador = null;
	private List<SelectItem> anios;
	private List<SelectItem> conceptosDePago;
	private ArrayList<PlanDeCuota> planesDeCuota;
	private List<SelectItem> tiposDocumento = null;
	private boolean muestraConfirmaAnulacion = false;
	private boolean muestraDialogoLectorDeTarjeta = false;
	private String cardBandData = "";
	private String tipoCargaTarjeta = "";
	private TicketPagoElectronico ticketPagoElectronico = new TicketPagoElectronico();
	private Integer mostrarPantallasDePagosElectronicos = null;
	private String logo = null;
	private String tipoBusqueda = "NRO_COMPROBANTE";

	private ObjectFactory oGestionObjFac = new ObjectFactory();

	/*
	 * Instancia e inicializa PagoElectronico
	 */

	public void inicializar() {

		com.americacg.cargavirtual.gateway.pagoElectronico.model.MensajeOutboundGateway mo = null;
		idAccion = 0;
		this.limpiarPantalla();

		try {

			Integer estado = PagoElectronicoHelper.ejecutarFuncionGCOM();

			if (estado.compareTo(1) != 0) {
				idAccion = 0;
				LogACGHelper.escribirLog(null, "Inicializar Pago Electronico. Comercio deshabilitado");
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
						"El comercio no se encuentra habilitado para operar.", null));
				return;
			} else {

				this.anios = new ArrayList<SelectItem>();
				int anio = Year.now().getValue() - 2000;

				for (int i = 0; i < 10; i++) {
					this.anios.add(new SelectItem(String.valueOf(anio + i), String.valueOf(anio + i)));
				}

				mo = null;
				this.conceptosDePago = new ArrayList<SelectItem>();

				mo = PagoElectronicoHelper.ejecutarFuncionLICP(this.getUsuario().getIdMayorista(),
						this.getUsuario().getIdCliente(), this.getUsuario().getIdUsuario(), Integer.MAX_VALUE, 0,
						"descripcion", "asc", this.fechaDesde, this.fechaHasta, this.idPagoACG.longValue());

				if (mo != null) {
					if (mo.getHeaderOut() != null && mo.getHeaderOut().getCodigoRetorno() != null) {
						LogACGHelper.escribirLog(null,
								"Inicializar Pago Electronico. Error: |" + mo.getHeaderOut().getMensajeRetorno() + "|");
						FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
								"Inicializar Pago Electronico. " + mo.getHeaderOut().getMensajeRetorno(), null));
						return;
					} else {

						DataOutputFcnLICP doLICP = (DataOutputFcnLICP) mo.getDataOutputFcn();
						if (doLICP != null) {
							if (doLICP.getRegistros() != null) {
								for (ConceptoDePago cp : doLICP.getRegistros()) {
									if (cp.getEstado().compareTo(1) == 0) {
										if(this.getUsuario().getPagoElectronicoInstance().getDataGCOM() != null && this.getUsuario().getPagoElectronicoInstance().getDataGCOM().getConceptoPagoDefault() != null &&
										   this.getUsuario().getPagoElectronicoInstance().getDataGCOM().getConceptoPagoDefault().equals(cp.getCodMnemonico())) {
											this.setIdConceptoDePago(cp.getId());
										}
										
										this.conceptosDePago.add(new SelectItem(cp.getId(), cp.getDescripcion()));
									}
								}
							}
						}
					}
				} else {
					// respuestaPE = "No se obtuvo una respuesta valida desde el Gateway";
					LogACGHelper.escribirLog(null,
							"Inicializar Pago Electronico. Error: |No se obtuvo una respuesta valida desde el Gateway|");
					FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
							"Inicializar Pago Electronico. No se obtuvo una respuesta valida desde el Gateway", null));
					return;
				}

				idAccion = 1;
			}
					
			this.getUsuario().getPagoElectronicoInstance().getPagoElectronico().getParametros().containsValue("HOLA");
		} catch (HandledException ha) {
			if ("E0002".equals(ha.getCode())) {
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
						"Inicializar Pago Electronico. No se pudo establecer la comunicación (GPE-TOC).|", null));
			} else {
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
						"Inicializar Pago Electronico. |" + ha.getMessage() + "|", null));
			}

		} catch (Exception e) {
			LogACGHelper.escribirLog(null, "Inicializar Pago Electronico. Error: |" + e.getMessage() + "|");
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
					"Inicializar Pago Electronico. " + e.getMessage() + "|", null));

		}
	}

	public void accion(Integer accion) {
		this.idAccion = accion;
		this.limpiarPantalla();
	}

	public void calcularCuotas() {
		ArrayList<String> lstUpdt = null;
		String etiqueta = "";
		MensajeOutboundGateway mo = null;

		try {

			this.planDeCuota = null;
			this.planesDeCuota = new ArrayList<PlanDeCuota>();

			if (this.getMedioDePago() != null && this.getMedioDePago().getId().compareTo(0L) >= 0
					&& this.idBancoPagoElectronico != null && this.idBancoPagoElectronico.compareTo(0L) >= 0
					&& this.peimporte != null && this.peimporte.compareTo(0F) > 0) {

				Integer importe = (int) (this.peimporte * 100);
				mo = PagoElectronicoHelper.ejecutarFuncionGPCU(this.getUsuario().getIdMayorista(),
						this.getUsuario().getIdCliente(), this.getUsuario().getIdUsuario(), Integer.MAX_VALUE, 0, "",
						"desc", this.idBancoPagoElectronico, this.getMedioDePago().getId(), new Date(), importe);

				if (mo != null) {
					if (mo.getHeaderOut() != null && mo.getHeaderOut().getCodigoRetorno() != null) {
						if (mo.getHeaderOut().getMensajeRetorno().contains("GPE-TRW")) {
							LogACGHelper.escribirLog(null,
									"CALCULO DE CUOTAS. Error: |" + mo.getHeaderOut().getCodigoRetorno() + "|"
											+ mo.getHeaderOut().getMensajeRetorno() + "|");
							FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(
									FacesMessage.SEVERITY_ERROR,
									"CALCULO DE CUOTAS. Por favor, actualice el importe nuevamente, para poder realizar el calculo de cuotas.",
									null));
						} else {
							LogACGHelper.escribirLog(null,
									"CALCULO DE CUOTAS. Error: |" + mo.getHeaderOut().getCodigoRetorno() + "|"
											+ mo.getHeaderOut().getMensajeRetorno() + "|");
							FacesContext.getCurrentInstance().addMessage(null,
									new FacesMessage(FacesMessage.SEVERITY_ERROR,
											"CALCULO DE CUOTAS. " + mo.getHeaderOut().getCodigoRetorno() + "|"
													+ mo.getHeaderOut().getMensajeRetorno(),
											null));
						}

					} else {
						DataOutputFcnGPCU doGPCU = (DataOutputFcnGPCU) mo.getDataOutputFcn();

						if (doGPCU != null) {
							if (doGPCU.getListPlanDeCuota() != null) {
								for (PlanDeCuota pc : doGPCU.getListPlanDeCuota()) {

									etiqueta = pc.getCantidadDeCuotas() + " cuota";

									if (pc.getCantidadDeCuotas().compareTo(1) > 0) {
										etiqueta += "s";
									}

									if("VALCUOTA".equals(this.getUsuario().getPagoElectronicoInstance().getDataGCOM().getPlanCuotaMuestra())) { 
										etiqueta += " de $ ";
										etiqueta += pc.getImporteCuota().setScale(2, RoundingMode.HALF_EVEN);
										
										if (pc.getEtiqueta() != null && !"".equals(pc.getEtiqueta().trim())) {
											etiqueta += " " + pc.getEtiqueta();
										}
									}
									else if("PORINT".equals(this.getUsuario().getPagoElectronicoInstance().getDataGCOM().getPlanCuotaMuestra())) {
										if (pc.getPorcentajeInteres().compareTo(new BigDecimal(0)) > 0) {
											etiqueta += " (" + pc.getPorcentajeInteres().setScale(2, RoundingMode.HALF_EVEN).toString()	+ " % de interes)";
										}

										if (pc.getEtiqueta() != null && !"".equals(pc.getEtiqueta().trim())) {
											etiqueta += " " + pc.getEtiqueta();
										} 
									}
									
									pc.setEtiqueta(etiqueta);
									this.planesDeCuota.add(pc);
								}
								
								//Setea el plan de cuotas unico que devolvio.
								if(this.planesDeCuota != null && this.planesDeCuota.size() == 1) {
									this.setPlanDeCuota(this.planesDeCuota.get(0));
								}
							}
						}
					}
				} else {
					LogACGHelper.escribirLog(null,
							"calcularCuotas. Error: |No se obtuvo una respuesta valida desde el Gateway|");
					FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
							"Calculo de Cuotas. No se obtuvo una respuesta valida desde el Gateway", null));
				}
			} else {
				this.planDeCuota = null;
				if (this.planesDeCuota != null) {
					this.planesDeCuota.clear();
				}
			}
		} catch (WebServiceException ste) {
			if (ste != null && ste.getCause() != null && ste.getCause().getClass() != null) {
				if (ste.getCause().getClass().equals(ConnectException.class)) {
					LogACGHelper.escribirLog(null,
							"CALCULO DE CUOTAS. Error: |No se pudo establecer la comunicación (GPE-TOC)|");
					FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
							"CALCULO DE CUOTAS. No se pudo establecer la comunicación (GPE-TOC). Reingrese el importe nuevamente para poder calcular las cuotas",
							null));
				} else if (ste.getCause().getClass().equals(SocketTimeoutException.class)) {
					LogACGHelper.escribirLog(null,
							"CALCULO DE CUOTAS. Error: |No se pudo establecer la comunicación (GPE-TRW)|");

					FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
							"CALCULO DE CUOTAS. No se pudo establecer la comunicación (GPE-TRW). Reingrese el importe nuevamente para poder calcular las cuotas",
							null));

				} else if (ste.getCause().getClass().equals(UnknownHostException.class)) {
					LogACGHelper.escribirLog(null, "CALCULO DE CUOTAS. Error: |" + ste.getMessage() + "|");
					FacesContext.getCurrentInstance().addMessage(null,
							new FacesMessage(FacesMessage.SEVERITY_ERROR, "Cobro. " + ste.getMessage(), null));
				} else {
					LogACGHelper.escribirLog(null, "CALCULO DE CUOTAS. Error: |" + ste.getMessage() + "|");
					FacesContext.getCurrentInstance().addMessage(null,
							new FacesMessage(FacesMessage.SEVERITY_ERROR, "Cobro. " + ste.getMessage(), null));
				}

			} else {
				LogACGHelper.escribirLog(null, "CALCULO DE CUOTAS. Error: |" + ste.getMessage() + "|");
				FacesContext.getCurrentInstance().addMessage(null,
						new FacesMessage(FacesMessage.SEVERITY_ERROR, "Cobro. " + ste.getMessage(), null));
			}
		} catch (Exception e) {
			LogACGHelper.escribirLog(null, "CALCULO DE CUOTAS. Error: |" + e.getMessage() + "|");
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR, "Cobro. " + e.getMessage(), null));
		}
		lstUpdt = new ArrayList<String>();
		lstUpdt.add("msgsProductos");
		PrimeFaces.current().ajax().update(lstUpdt);
	}

	public void botonGPEANPT() {

		ArrayList<String> lstUpdt = null;

		try {
			com.americacg.cargavirtual.gateway.pagoElectronico.model.MensajeOutboundGateway mo = null;

			mo = PagoElectronicoHelper.ejecutarFuncionANPT(this.getUsuario().getIdMayorista(),
					this.getUsuario().getIdCliente(), this.getUsuario().getIdUsuario(),
					GatewayHelper.generaIdTrxCliente(this.getUsuario().getIdMayorista(),
							this.getUsuario().getIdCliente()),
					Integer.parseInt(this.idPagoOperador), this.getUsuario().getIdMayorista() + "-"
							+ this.getUsuario().getIdCliente() + "-" + this.getUsuario().getIdUsuario());

			if (mo != null) {
				if (mo.getDataOutputFcn() != null) {
					if ("M0000".equals(mo.getDataOutputFcn().getCodigoRetorno())) {
						this.ticketPagoElectronico = new TicketPagoElectronico();
						DataOutputFcnANPT dofp = (DataOutputFcnANPT) mo.getDataOutputFcn();
						this.ticketPagoElectronico = new TicketPagoElectronico();
						this.ticketPagoElectronico.setTicket(dofp.getTicket().toString());
						mostrarTicket();
						limpiarPantalla();
					} else {
						LogACGHelper.escribirLog(null, "Anulacion. Error: |" + mo.getDataOutputFcn().getCodigoRetorno()
								+ " " + mo.getDataOutputFcn().getMensajeRetorno() + "|");
						FacesContext.getCurrentInstance().addMessage(null,
								new FacesMessage(FacesMessage.SEVERITY_ERROR,
										"Anulacion. " + mo.getDataOutputFcn().getCodigoRetorno() + " "
												+ mo.getDataOutputFcn().getMensajeRetorno(),
										null));
					}
				} else {

					if (mo.getHeaderOut() != null) {
						LogACGHelper.escribirLog(null, "Anulacion. Error: |" + mo.getHeaderOut().getCodigoRetorno()
								+ " " + mo.getHeaderOut().getMensajeRetorno() + "|");
						FacesContext.getCurrentInstance().addMessage(null,
								new FacesMessage(FacesMessage.SEVERITY_ERROR,
										"Anulacion. " + mo.getHeaderOut().getCodigoRetorno() + " "
												+ mo.getHeaderOut().getMensajeRetorno(),
										null));
					} else {
						LogACGHelper.escribirLog(null,
								"Anulacion. Error: |No se obtuvo una respuesta valida desde el Gateway|");
						FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
								"Anulacion. No se obtuvo una respuesta valida desde el Gateway", null));
					}
				}
			} else {
				LogACGHelper.escribirLog(null,
						"Anulacion. Error: |No se obtuvo una respuesta valida desde el Gateway|");
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
						"Anulacion. No se obtuvo una respuesta valida desde el Gateway", null));
			}

		} catch (WebServiceException ste) {
			if (ste != null && ste.getCause() != null && ste.getCause().getClass() != null) {
				if (ste.getCause().getClass().equals(ConnectException.class)) {
					LogACGHelper.escribirLog(null,
							"Anulacion. Error: |No se pudo establecer la comunicación (GPE-TOC)|");
					FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
							"No se pudo realizar la ANULACION. Error GPE-TOC. Por favor reintente.", null));

				} else if (ste.getCause().getClass().equals(SocketTimeoutException.class)) {
					LogACGHelper.escribirLog(null,
							"Anulacion. Error: |No se pudo establecer la comunicación (GPE-TRW)|");

					FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
							"No se pudo realizar la ANULACION. Error GPE-TRW. Por favor reintente.", null));

				} else if (ste.getCause().getClass().equals(UnknownHostException.class)) {
					LogACGHelper.escribirLog(null, "Anulacion. Error: |" + ste.getMessage() + "|");
					FacesContext.getCurrentInstance().addMessage(null,
							new FacesMessage(FacesMessage.SEVERITY_ERROR, "Anulacion. " + ste.getMessage(), null));

				} else {
					LogACGHelper.escribirLog(null, "Anulacion. Error: |" + ste.getMessage() + "|");
					FacesContext.getCurrentInstance().addMessage(null,
							new FacesMessage(FacesMessage.SEVERITY_ERROR, "Anulacion. " + ste.getMessage(), null));
				}

			} else {
				LogACGHelper.escribirLog(null, "Anulacion. Error: |" + ste.getMessage() + "|");
				FacesContext.getCurrentInstance().addMessage(null,
						new FacesMessage(FacesMessage.SEVERITY_ERROR, "Anulacion. " + ste.getMessage(), null));

			}
		} catch (Exception e) {
			LogACGHelper.escribirLog(null, "actualizarValores: Error PAGO: |" + e.getMessage() + "|");
		}
		lstUpdt = new ArrayList<String>();
		lstUpdt.add("msgsProductos");
		PrimeFaces.current().ajax().update(lstUpdt);
		this.muestraConfirmaAnulacion = false;
	}

	public void botonGPECOPA() {
		funcionCOPA("PAGO", "Ticket de Consulta", false);
	}

	public boolean funcionCOPA(String titulo, String pie, boolean retornoObjeto) {
		ArrayList<String> lstUpdt = null;
		DataOutputFcnCOPA oDataOutCOPA = null;
		boolean respuesta = true;

		try {
			this.ticketPagoElectronico = null;

			com.americacg.cargavirtual.gateway.pagoElectronico.model.MensajeOutboundGateway mo = null;

			if ("NRO_COMPROBANTE".equals(this.tipoBusqueda)) {
				mo = PagoElectronicoHelper.ejecutarFuncionCOPA(this.tipoBusqueda, this.idPagoOperador, 1, 0,
						(retornoObjeto ? 1 : 0));
			} else {
				mo = PagoElectronicoHelper.ejecutarFuncionLPOP(this.getUsuario().getIdMayorista(), null,
						this.idPagoOperador, null, null, null, null);
			}

			if (mo != null) {
				if (mo.getDataOutputFcn() != null) {
					if ("M0000".equals(mo.getDataOutputFcn().getCodigoRetorno())) {
						if (retornoObjeto) {
							oDataOutCOPA = (DataOutputFcnCOPA) mo.getDataOutputFcn();
							this.setPago(oDataOutCOPA.getPago());
						} else {
							this.ticketPagoElectronico = new TicketPagoElectronico();
							DataOutputFcnCOPA dofp = (DataOutputFcnCOPA) mo.getDataOutputFcn();
							this.ticketPagoElectronico = new TicketPagoElectronico();
							this.ticketPagoElectronico.setTicket(dofp.getTicket().toString());
							mostrarTicket();
							limpiarPantalla();
						}
					} else {
						LogACGHelper.escribirLog(null,
								"Consulta de Cobro. Error: |" + mo.getDataOutputFcn().getCodigoRetorno() + " "
										+ mo.getDataOutputFcn().getMensajeRetorno() + "|");
						FacesContext.getCurrentInstance().addMessage(null,
								new FacesMessage(FacesMessage.SEVERITY_ERROR,
										"Consulta de Cobro. " + mo.getDataOutputFcn().getCodigoRetorno() + " "
												+ mo.getDataOutputFcn().getMensajeRetorno(),
										null));
						respuesta = false;
					}
				} else {

					if (mo.getHeaderOut() != null) {
						LogACGHelper.escribirLog(null,
								"Consulta de Cobro. Error: |" + mo.getHeaderOut().getCodigoRetorno() + " "
										+ mo.getHeaderOut().getMensajeRetorno() + "|");
						FacesContext.getCurrentInstance().addMessage(null,
								new FacesMessage(FacesMessage.SEVERITY_ERROR,
										"Consulta de Cobro. " + mo.getHeaderOut().getCodigoRetorno() + " "
												+ mo.getHeaderOut().getMensajeRetorno(),
										null));
					} else {
						LogACGHelper.escribirLog(null,
								"Consulta de Cobro. Error: |No se obtuvo una respuesta valida desde el Gateway|");
						FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
								"Consulta de Cobro. No se obtuvo una respuesta valida desde el Gateway", null));
					}

					respuesta = false;
				}
			} else {
				LogACGHelper.escribirLog(null,
						"Consulta de Cobro. Error: |No se obtuvo una respuesta valida desde el Gateway|");
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
						"Consulta de Cobro. No se obtuvo una respuesta valida desde el Gateway", null));

				respuesta = false;
			}
		} catch (WebServiceException ste) {
			if (ste != null && ste.getCause() != null && ste.getCause().getClass() != null) {
				if (ste.getCause().getClass().equals(ConnectException.class)) {
					LogACGHelper.escribirLog(null,
							"Consulta de Cobro. Error: |No se pudo establecer la comunicación (GPE-TOC)|");
					FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
							"Consulta de Cobro. No se pudo establecer la comunicación (GPE-TOC)", null));
				} else if (ste.getCause().getClass().equals(SocketTimeoutException.class)) {
					LogACGHelper.escribirLog(null,
							"Consulta de Cobro. Error: |No se pudo establecer la comunicación (GPE-TRW)|");
					FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
							"Consulta de Cobro. No se pudo establecer la comunicación (GPE-TRW)", null));
				} else if (ste.getCause().getClass().equals(UnknownHostException.class)) {
					LogACGHelper.escribirLog(null, "Consulta de Cobro. Error: |" + ste.getMessage() + "|");
					FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
							"Consulta de Cobro. " + ste.getMessage(), null));
				} else {
					LogACGHelper.escribirLog(null, "Consulta de Cobro. Error: |" + ste.getMessage() + "|");
					FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
							"Consulta de Cobro. " + ste.getMessage(), null));
				}

			} else {
				LogACGHelper.escribirLog(null, "Consulta de Cobro. Error: |" + ste.getMessage() + "|");
				FacesContext.getCurrentInstance().addMessage(null,
						new FacesMessage(FacesMessage.SEVERITY_ERROR, "Consulta de Cobro. " + ste.getMessage(), null));
			}

			respuesta = false;
		} catch (Exception e) {
			LogACGHelper.escribirLog(null, "Consulta de Cobro. Error: |" + e.getMessage() + "|");
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR, "Consulta de Cobro. " + e.getMessage(), null));

			respuesta = false;
		}
		lstUpdt = new ArrayList<String>();
		lstUpdt.add("msgsProductos");
		PrimeFaces.current().ajax().update(lstUpdt);

		return respuesta;
	}
/*
	public void funcionVABI(ValueChangeEvent event) {
		funcionVABI();
		this.blanquearDatosTarjeta(true);
		this.calcularCuotas();
	}
*/
	public void funcionVABI() {

		ArrayList<String> lstUpdt = null;
		String tarjetaNumeroAux = "";

		try {

			tarjetaNumeroAux = this.tarjetaNumero.replace(" ", "");
			// tarjetaNumeroAux = this.tarjetaNumero;
			if ((tarjetaNumeroAux.length() >= 6)
					|| (tarjetaNumeroAux.length() >= 6)) {

				//this.blanquearDatosTarjeta(true);
				
				com.americacg.cargavirtual.gateway.pagoElectronico.model.MensajeOutboundGateway mo = null;

				mo = PagoElectronicoHelper.ejecutarFuncionVABI(tarjetaNumeroAux);

				if (mo != null) {
					if (mo.getDataOutputFcn() != null) {
						if ("M0000".equals(mo.getDataOutputFcn().getCodigoRetorno())) {

							DataOutputFcnVABI dofp = (DataOutputFcnVABI) mo.getDataOutputFcn();

							if (dofp.getBancoPagoElectronico() != null) {
								this.idBancoPagoElectronico = dofp.getBancoPagoElectronico().getId();
							}

							if (dofp.getMedioDePago() != null) {
								this.setMedioDePago(dofp.getMedioDePago());
							}

							// Parche para test1, test2 y test3 que permite hacer validaciones con una
							// tarjeta de prueba
							if ((this.getUsuario().getIdMayorista().compareTo(1L) == 0
									|| this.getUsuario().getIdMayorista().compareTo(2L) == 0
									|| this.getUsuario().getIdMayorista().compareTo(3L) == 0)) {
								this.getMedioDePago().setId(2L);
							}

							this.logo = dofp.getImagen();
						} else {
							if ("M0020".equals(mo.getDataOutputFcn().getCodigoRetorno())
									|| "M0021".equals(mo.getDataOutputFcn().getCodigoRetorno())
									|| "M0022".equals(mo.getDataOutputFcn().getCodigoRetorno())) {
								LogACGHelper.escribirLog(null,
										"Validacion de BIN. Error: |" + mo.getDataOutputFcn().getCodigoRetorno() + " "
												+ mo.getDataOutputFcn().getMensajeRetorno() + "|");
								FacesContext.getCurrentInstance().addMessage(null,
										new FacesMessage(FacesMessage.SEVERITY_ERROR,
												"Medio de pago (Tarjeta) no habilitado para operar en este comercio.",
												null));
								this.logo = null;
							} else {
								LogACGHelper.escribirLog(null,
										"Validacion de BIN. Error: |" + mo.getDataOutputFcn().getCodigoRetorno() + " "
												+ mo.getDataOutputFcn().getMensajeRetorno() + "|");
								FacesContext.getCurrentInstance()
										.addMessage(null,
												new FacesMessage(FacesMessage.SEVERITY_ERROR,
														"Validacion de BIN. " + mo.getDataOutputFcn().getCodigoRetorno()
																+ " " + mo.getDataOutputFcn().getMensajeRetorno(),
														null));
								this.logo = null;
							}
						}
					} else {

						if (mo.getHeaderOut() != null) {
							LogACGHelper.escribirLog(null,
									"Validacion de BIN. Error: |" + mo.getHeaderOut().getCodigoRetorno() + " "
											+ mo.getHeaderOut().getMensajeRetorno() + "|");
							FacesContext.getCurrentInstance().addMessage(null,
									new FacesMessage(FacesMessage.SEVERITY_ERROR,
											"Validacion de BIN. " + mo.getHeaderOut().getCodigoRetorno() + " "
													+ mo.getHeaderOut().getMensajeRetorno(),
											null));
						} else {
							LogACGHelper.escribirLog(null,
									"Validacion de BIN. Error: |No se obtuvo una respuesta valida desde el Gateway|");
							FacesContext.getCurrentInstance().addMessage(null,
									new FacesMessage(FacesMessage.SEVERITY_ERROR,
											"Validacion de BIN. No se obtuvo una respuesta valida desde el Gateway",
											null));
						}
						this.logo = null;
					}
				} else {
					LogACGHelper.escribirLog(null,
							"Validacion de BIN. Error: |No se obtuvo una respuesta valida desde el Gateway|");
					FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
							"Validacion de BIN. No se obtuvo una respuesta valida desde el Gateway", null));
					this.logo = null;
				}

			} else if (tarjetaNumeroAux.length() < 6) {
				this.logo = null;
			}
		} catch (WebServiceException ste) {
			if (ste != null && ste.getCause() != null && ste.getCause().getClass() != null) {
				if (ste.getCause().getClass().equals(ConnectException.class)) {
					LogACGHelper.escribirLog(null,
							"Validacion de BIN. Error: |No se pudo establecer la comunicación (GPE-TOC)|");
					FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
							"Validacion de BIN. No se pudo establecer la comunicación (GPE-TOC)", null));
				} else if (ste.getCause().getClass().equals(SocketTimeoutException.class)) {
					LogACGHelper.escribirLog(null,
							"Validacion de BIN. Error: |No se pudo establecer la comunicación (GPE-TRW)|");
					FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
							"Validacion de BIN. No se pudo establecer la comunicación (GPE-TRW)", null));
				} else if (ste.getCause().getClass().equals(UnknownHostException.class)) {
					LogACGHelper.escribirLog(null, "Validacion de BIN. Error: |" + ste.getMessage() + "|");
					FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
							"Validacion de BIN. " + ste.getMessage(), null));
				} else {
					LogACGHelper.escribirLog(null, "Validacion de BIN. Error: |" + ste.getMessage() + "|");
					FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
							"Validacion de BIN. " + ste.getMessage(), null));
				}

			} else {
				LogACGHelper.escribirLog(null, "Validacion de BIN. Error: |" + ste.getMessage() + "|");
				FacesContext.getCurrentInstance().addMessage(null,
						new FacesMessage(FacesMessage.SEVERITY_ERROR, "Validacion de BIN. " + ste.getMessage(), null));
			}
			this.logo = null;
		} catch (Exception e) {
			LogACGHelper.escribirLog(null, "Validacion de BIN. Error: |" + e.getMessage() + "|");
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR, "Validacion de BIN. " + e.getMessage(), null));
			this.logo = null;
		}
		lstUpdt = new ArrayList<String>();
		lstUpdt.add("msgsProductos");
		lstUpdt.add("logoTarjeta");
		PrimeFaces.current().ajax().update(lstUpdt);
	}

	public void botonGPEPAGO() {
		ArrayList<String> lstUpdt = null;
		String nroInternoACG = "";
		String tarjetaNumero = "";
		String tarjetaExpiracionMes = "";
		String tarjetaExpiracionAnio = "";
		Long idMedioPago = null;
		Integer importe = 0;
		String idTerminal = null;

		try {

			if (

			this.tarjetaNumero == null || (this.tarjetaNumero != null && "".equals(this.tarjetaNumero.trim()))
					|| this.tarjetaTitularNombre == null
					|| (this.tarjetaTitularNombre != null && "".equals(this.tarjetaTitularNombre.trim()))
					|| this.tarjetaExpiracion == null
					|| (this.tarjetaExpiracion != null && "".equals(this.tarjetaExpiracion.trim()))
					|| this.codigoSeguridad == null
					|| (this.codigoSeguridad != null && "".equals(this.codigoSeguridad.trim()))
					|| this.tipoIdentificacion == null
					|| (this.tipoIdentificacion != null && "".equals(this.tipoIdentificacion.trim()))
					|| this.idConceptoDePago == null
					|| (this.idConceptoDePago != null && this.idConceptoDePago.compareTo(0L) == 0)
					|| this.moneda == null || (this.moneda != null && "".equals(this.moneda.trim()))
					|| this.peimporte == null || (this.peimporte != null && this.peimporte.compareTo(0F) == 0)
					|| this.planDeCuota == null
			) {

				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
						"Los campos indicados con *, son requeridos.", null));
				return;
			}

			com.americacg.cargavirtual.gateway.pagoElectronico.model.MensajeOutboundGateway mo = null;

			nroInternoACG = GatewayHelper.generaIdTrxCliente(this.getUsuario().getIdMayorista(),
					this.getUsuario().getIdCliente());

			try {
				tarjetaNumero = this.tarjetaNumero.replace(" ", "");
			} catch (Exception e) {
				tarjetaNumero = this.tarjetaNumero;
			}

			try {
				String[] sExpiracion = this.tarjetaExpiracion.split("/");

				tarjetaExpiracionMes = sExpiracion[0];
				tarjetaExpiracionAnio = sExpiracion[1];

			} catch (Exception e) {
				// TODO: handle exception
			}

			if (this.getMedioDePago() != null) {
				if (this.getMedioDePago().getId().compareTo(-1L) == 0) {
					idMedioPago = null;
				} else {
					idMedioPago = this.getMedioDePago().getId();
				}
			} else {
				idMedioPago = null;
			}
			importe = this.peimporte.intValue() * 100;
			idTerminal = this.getUsuario().getIdMayorista() + "-" + this.getUsuario().getIdCliente() + "-"
					+ this.getUsuario().getIdUsuario();

			mo = PagoElectronicoHelper.ejecutarFuncionPAGO(
					GatewayHelper.generaIdTrxCliente(this.getUsuario().getIdMayorista(),
							this.getUsuario().getIdCliente()),
					tarjetaNumero, tarjetaExpiracionMes, tarjetaExpiracionAnio, this.codigoSeguridad,
					this.tarjetaTitularNombre, this.tipoIdentificacion, this.numeroIdentificacion, idMedioPago, importe,
					this.moneda, this.telefonoCliente, this.emailCliente, this.idBancoPagoElectronico,
					this.planDeCuota.getId(), this.idConceptoDePago, this.datosAdicionalesPago, idTerminal, tipoCargaTarjeta);

			if (mo != null) {
				if (mo.getDataOutputFcn() != null) {
					if ("M0000".equals(mo.getDataOutputFcn().getCodigoRetorno())) {
						DataOutputFcnPAGO dofp = (DataOutputFcnPAGO) mo.getDataOutputFcn();
						this.ticketPagoElectronico = new TicketPagoElectronico();
						this.ticketPagoElectronico.setTicket(dofp.getTicket().toString());

						mostrarTicket();
						limpiarPantalla();

					} else {
						if (mo.getDataOutputFcn().getMensajeRetorno().contains("GPE-TRW")) {
							FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(
									FacesMessage.SEVERITY_ERROR,
									"COBRO EN PROCESO. Error GPE-TRW. Por favor consulte el estado de la transaccion.",
									null));
							PrimeFaces.current().executeScript(
									"ACGSiteScriptsFCNS.cambiaFondoMsg('msgsProductos', '#212121', '#E4B34E', '#a0d4b7', '"
											+ FacesContext.getCurrentInstance().getExternalContext()
													.getRequestContextPath().replace("/", "\\/")
											+ "\\/javax.faces.resource\\/images\\/24x24\\/enProceso.png')");
						} else {
							LogACGHelper.escribirLog(null,
									"Cobro rechazado: |" + mo.getDataOutputFcn().getCodigoRetorno() + "|"
											+ mo.getDataOutputFcn().getMensajeRetorno() + "|");
							FacesContext.getCurrentInstance().addMessage(null,
									new FacesMessage(FacesMessage.SEVERITY_ERROR,
											"Cobro rechazado. " + mo.getDataOutputFcn().getCodigoRetorno() + "|"
													+ mo.getDataOutputFcn().getMensajeRetorno(),
											null));
						}
						
						FacesContext.getCurrentInstance().validationFailed();
					} 
				} else {

					if (mo.getHeaderOut() != null) {
						if (mo.getHeaderOut().getMensajeRetorno().contains("GPE-TRW")) {
							FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(
									FacesMessage.SEVERITY_ERROR,
									"COBRO EN PROCESO. Error GPE-TRW. Por favor consulte el estado de la transaccion. Nro. Interno: |"
											+ nroInternoACG + "|",
									null));
							PrimeFaces.current().executeScript(
									"ACGSiteScriptsFCNS.cambiaFondoMsg('msgsProductos', '#212121', '#E4B34E', '#a0d4b7', '"
											+ FacesContext.getCurrentInstance().getExternalContext()
													.getRequestContextPath().replace("/", "\\/")
											+ "\\/javax.faces.resource\\/images\\/24x24\\/enProceso.png')");
						} else {
							LogACGHelper.escribirLog(null, "Cobro rechazado: |" + mo.getHeaderOut().getCodigoRetorno()
									+ "|" + mo.getHeaderOut().getMensajeRetorno() + "|");
							FacesContext.getCurrentInstance().addMessage(null,
									new FacesMessage(FacesMessage.SEVERITY_ERROR,
											"Cobro rechazado. " + mo.getHeaderOut().getCodigoRetorno() + "|"
													+ mo.getHeaderOut().getMensajeRetorno(),
											null));
						}

						FacesContext.getCurrentInstance().validationFailed();
					} else {
						LogACGHelper.escribirLog(null,
								"Cobro. Error: |No se obtuvo una respuesta valida desde el Gateway|");
						FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
								"Cobro. No se obtuvo una respuesta valida desde el Gateway", null));
					}
				}
			} else {
				LogACGHelper.escribirLog(null, "Cobro. Error: |No se obtuvo una respuesta valida desde el Gateway|");
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
						"Cobro. No se obtuvo una respuesta valida desde el Gateway", null));
			}
		} catch (WebServiceException ste) {
			if (ste != null && ste.getCause() != null && ste.getCause().getClass() != null) {
				if (ste.getCause().getClass().equals(ConnectException.class)) {
					LogACGHelper.escribirLog(null, "Cobro. Error: |No se pudo establecer la comunicación (GPE-TOC)|");
					FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
							"Cobro. No se pudo establecer la comunicación (GPE-TOC)", null));
				} else if (ste.getCause().getClass().equals(SocketTimeoutException.class)) {
					LogACGHelper.escribirLog(null, "Cobro. Error: |No se pudo establecer la comunicación (GPE-TRW)|");

					FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
							"COBRO EN PROCESO. Error GPE-TRW. Por favor consulte el estado de la transaccion. Nro. Interno: |"
									+ nroInternoACG + "|",
							null));
					PrimeFaces.current().executeScript(
							"ACGSiteScriptsFCNS.cambiaFondoMsg('msgsProductos', '#212121', '#E4B34E', '#a0d4b7', '"
									+ FacesContext.getCurrentInstance().getExternalContext().getRequestContextPath()
											.replace("/", "\\/")
									+ "\\/javax.faces.resource\\/images\\/24x24\\/enProceso.png')");
				} else if (ste.getCause().getClass().equals(UnknownHostException.class)) {
					LogACGHelper.escribirLog(null, "Cobro. Error: |" + ste.getMessage() + "|");
					FacesContext.getCurrentInstance().addMessage(null,
							new FacesMessage(FacesMessage.SEVERITY_ERROR, "Cobro. " + ste.getMessage(), null));
				} else {
					LogACGHelper.escribirLog(null, "Cobro. Error: |" + ste.getMessage() + "|");
					FacesContext.getCurrentInstance().addMessage(null,
							new FacesMessage(FacesMessage.SEVERITY_ERROR, "Cobro. " + ste.getMessage(), null));
				}

			} else {
				LogACGHelper.escribirLog(null, "Cobro. Error: |" + ste.getMessage() + "|");
				FacesContext.getCurrentInstance().addMessage(null,
						new FacesMessage(FacesMessage.SEVERITY_ERROR, "Cobro. " + ste.getMessage(), null));
			}
		} catch (Exception e) {
			LogACGHelper.escribirLog(null, "Cobro. Error: |" + e.getMessage() + "|");
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR, "Cobro. " + e.getMessage(), null));
		}
		lstUpdt = new ArrayList<String>();
		lstUpdt.add("msgsProductos");
		PrimeFaces.current().ajax().update(lstUpdt);
	}

	public void limpiarPantalla() {
		this.numeroTicketACG = "";
		this.paymentId = null;
		this.idPagoACG = 0L;
		this.fechaDesde = new Date();
		this.fechaHasta = new Date();
		this.tarjetaNumero = "";
		this.tarjetaExpiracion = "";
		this.tarjetaExpiracionMes = "";
		this.tarjetaExpiracionAnio = "";
		this.codigoSeguridad = "";
		this.tarjetaTitularNombre = "";
		this.tipoIdentificacion = "";
		this.numeroIdentificacion = "";
		this.peimporte = null;
		this.moneda = "ARS";
		this.planDeCuota = null;
		this.setMedioDePago(null);
		this.emailCliente = "";
		this.idConceptoDePago = 0L;
		this.datosAdicionalesPago = "";
		this.telefonoCliente = "";
		this.idBancoPagoElectronico = null;
		if (this.planesDeCuota != null) {
			this.planesDeCuota.clear();
		}
		this.idPagoOperador = null;
		this.logo = null;
		tipoCargaTarjeta = "M"; 
	}

	public void mostrarTicket() {
		String nombreTicketCompleto = "";
		//String nombreTicket = this.getUsuario().getTicket();

		Map<String, Object> options = new HashMap<String, Object>();
		options.put("modal", true);
		options.put("draggable", true);
		options.put("resizable", false);
		options.put("closable", true);
		options.put("contentWidth", this.getUsuario().getAnchoTicket() + 45);
		options.put("position", "center center");
		options.put("includeViewParams", true);
		options.put("fitViewport", true);

		Map<String, List<String>> paramMap = new HashMap<String, List<String>>();

		nombreTicketCompleto = "/secure/shared/tickets/ticketPagoElectronico";

		// --- Abre el dialogo para buscar articulos
		PrimeFaces.current().dialog().openDynamic(nombreTicketCompleto, options, paramMap);
		PrimeFaces.current().executeScript("PF('blockPanelGral').hide()");
	}

	public String getNumeroTicketACG() {
		return numeroTicketACG;
	}

	public void setNumeroTicketACG(String numeroTicketACG) {
		this.numeroTicketACG = numeroTicketACG;
	}

	public Long getIdPagoACG() {
		return idPagoACG;
	}

	public void setIdPagoACG(Long idPagoACG) {
		this.idPagoACG = idPagoACG;
	}

	public Date getFechaDesde() {
		return fechaDesde;
	}

	public void setFechaDesde(Date fechaDesde) {
		this.fechaDesde = fechaDesde;
	}

	public Date getFechaHasta() {
		return fechaHasta;
	}

	public void setFechaHasta(Date fechaHasta) {
		this.fechaHasta = fechaHasta;
	}

	public String getTarjetaNumero() {
		return tarjetaNumero;
	}

	public void setTarjetaNumero(String tarjetaNumero) {
		this.tarjetaNumero = tarjetaNumero;
	}

	public String getTarjetaExpiracion() {
		return tarjetaExpiracion;
	}

	public void setTarjetaExpiracion(String tarjetaExpiracion) {
		this.tarjetaExpiracion = tarjetaExpiracion;
	}

	public String getCodigoSeguridad() {
		return codigoSeguridad;
	}

	public void setCodigoSeguridad(String codigoSeguridad) {
		this.codigoSeguridad = codigoSeguridad;
	}

	public String getTarjetaTitularNombre() {
		return tarjetaTitularNombre;
	}

	public void setTarjetaTitularNombre(String tarjetaTitularNombre) {
		this.tarjetaTitularNombre = tarjetaTitularNombre;
	}

	public String getTipoIdentificacion() {
		return tipoIdentificacion;
	}

	public void setTipoIdentificacion(String tipoIdentificacion) {
		this.tipoIdentificacion = tipoIdentificacion;
	}

	public String getNumeroIdentificacion() {
		return numeroIdentificacion;
	}

	public void setNumeroIdentificacion(String numeroIdentificacion) {
		this.numeroIdentificacion = numeroIdentificacion;
	}

	public Float getPeimporte() {
		return peimporte;
	}

	public void setPeimporte(Float peimporte) {
		this.peimporte = peimporte;
	}

	public String getMoneda() {
		return moneda;
	}

	public void setMoneda(String moneda) {
		this.moneda = moneda;
	}

	public MedioDePago getMedioDePago() {
		return medioDePago;
	}

	public void setMedioDePago(MedioDePago medioDePago) {
		this.medioDePago = medioDePago;
	}

	public String getEmailCliente() {
		return emailCliente;
	}

	public void setEmailCliente(String emailCliente) {
		this.emailCliente = emailCliente;
	}

	public Long getIdConceptoDePago() {
		return idConceptoDePago;
	}

	public void setIdConceptoDePago(Long idConceptoDePago) {
		this.idConceptoDePago = idConceptoDePago;
	}

	public String getDatosAdicionalesPago() {
		return datosAdicionalesPago;
	}

	public void setDatosAdicionalesPago(String datosAdicionalesPago) {
		this.datosAdicionalesPago = datosAdicionalesPago;
	}

	public Integer getIdAccion() {
		return idAccion;
	}

	public void setIdAccion(Integer idAccion) {
		this.idAccion = idAccion;
	}

	public List<SelectItem> getConceptosDePago() {
		return conceptosDePago;
	}

	public void setConceptosDePago(List<SelectItem> conceptosDePago) {
		this.conceptosDePago = conceptosDePago;
	}

	public Integer getPaymentId() {
		return paymentId;
	}

	public void setPaymentId(Integer paymentId) {
		this.paymentId = paymentId;
	}

	public TicketPagoElectronico getTicketPagoElectronico() {
		return ticketPagoElectronico;
	}

	public void setTicketPagoElectronico(TicketPagoElectronico ticketPagoElectronico) {
		this.ticketPagoElectronico = ticketPagoElectronico;
	}

	public Long getIdBancoPagoElectronico() {
		return idBancoPagoElectronico;
	}

	public void setIdBancoPagoElectronico(Long idBancoPagoElectronico) {
		this.idBancoPagoElectronico = idBancoPagoElectronico;
	}

	public ArrayList<PlanDeCuota> getPlanesDeCuota() {
		return planesDeCuota;
	}

	public void setPlanesDeCuota(ArrayList<PlanDeCuota> planDeCuota) {
		this.planesDeCuota = planDeCuota;
	}

	public String getTarjetaExpiracionMes() {
		return tarjetaExpiracionMes;
	}

	public void setTarjetaExpiracionMes(String tarjetaExpiracionMes) {
		this.tarjetaExpiracionMes = tarjetaExpiracionMes;
	}

	public String getTarjetaExpiracionAnio() {
		return tarjetaExpiracionAnio;
	}

	public void setTarjetaExpiracionAnio(String tarjetaExpiracionAnio) {
		this.tarjetaExpiracionAnio = tarjetaExpiracionAnio;
	}

	public Integer getMostrarPantallasDePagosElectronicos() {
		try {
			if (this.mostrarPantallasDePagosElectronicos == null)
				this.mostrarPantallasDePagosElectronicos = (PagoElectronicoHelper.getInstance().getHalitadoParaWeb() ? 1
						: 0);
		} catch (Exception e) {
			LogACGHelper.escribirLog(null, "getMostrarPantallasDePagosElectronicos - Error: |" + e.getMessage() + "|");
		}

		return this.mostrarPantallasDePagosElectronicos;
	}

	public void setMostrarPantallasDePagosElectronicos(Integer mostrarPantallasDePagosElectronicos) {
		LogACGHelper.escribirLog(null, "setMostrarPantallasDePagosElectronicos - mostrarPantallasDePagosElectronicos: |"
				+ this.mostrarPantallasDePagosElectronicos + "|");
		this.mostrarPantallasDePagosElectronicos = mostrarPantallasDePagosElectronicos;
	}

	public List<SelectItem> getAnios() {
		return anios;
	}

	public void setAnios(List<SelectItem> anios) {
		this.anios = anios;
	}

	public String getIdPagoOperador() {
		return idPagoOperador;
	}

	public void setIdPagoOperador(String idPagoOperador) {
		this.idPagoOperador = idPagoOperador;
	}

	public String getTelefonoCliente() {
		return telefonoCliente;
	}

	public void setTelefonoCliente(String telefonoCliente) {
		this.telefonoCliente = telefonoCliente;
	}

	public String getLogo() {
		return logo;
	}

	public void setLogo(String logo) {
		this.logo = logo;
	}

	public String getTipoBusqueda() {
		return tipoBusqueda;
	}

	public void setTipoBusqueda(String tipoBusqueda) {
		this.tipoBusqueda = tipoBusqueda;
	}

	public List<SelectItem> getTiposDocumento() {
		if (tiposDocumento == null) {
			FiltroListadoPlatTipoDocumento oFlt = null;
			PlatTipoDocumentoList oLstTD = null;
			ArrayOfString codMnemIncl = null;

			oFlt = new FiltroListadoPlatTipoDocumento();
			oFlt.setPage(oGestionObjFac.createFiltroListadoPlatTipoDocumentoPage(0));
			oFlt.setPageSize(oGestionObjFac.createFiltroListadoPlatTipoDocumentoPageSize(Integer.MAX_VALUE));

			oLstTD = GestionServiceHelper.getGestionService(CfgTimeout.DEFAULT).listarPlatTipoDocumento(oFlt);

			if (oLstTD != null && oLstTD.getTotalRegistros().getValue().compareTo(0) > 0) {
				tiposDocumento = new ArrayList<SelectItem>();

				for (PlatTipoDocumento oTD : oLstTD.getRegistros().getValue().getPlatTipoDocumento()) {
					if(this.getUsuario().getPagoElectronicoInstance().getDataGCOM() != null && this.getUsuario().getPagoElectronicoInstance().getDataGCOM().getTipoDocumentoDefault() != null &&
					   !this.getUsuario().getPagoElectronicoInstance().getDataGCOM().getTipoDocumentoDefault().isEmpty() &&
					    this.getUsuario().getPagoElectronicoInstance().getDataGCOM().getTipoDocumentoDefault().equals(oTD.getCodMnemonico().getValue())) {
						this.setTipoIdentificacion(oTD.getCodMnemonico().getValue());						
					}
				
					tiposDocumento.add(new SelectItem(oTD.getCodMnemonico().getValue(), oTD.getDescripcion().getValue()));
				}
			}

		}

		return tiposDocumento;
	}

	public void setTiposDocumento(List<SelectItem> tiposDocumento) {
		this.tiposDocumento = tiposDocumento;
	}

	public PlanDeCuota getPlanDeCuota() {
		return planDeCuota;
	}

	public void setPlanDeCuota(PlanDeCuota planDeCuota) {
		this.planDeCuota = planDeCuota;
	}

	public void setearPlanDeCuotas() {
		if (this.getPlanesDeCuota() != null && this.getPlanesDeCuota().size() > 0) {
			if (this.getPlanesDeCuota().indexOf(this.getPlanDeCuota()) > -1)
				this.setPlanDeCuota(
						this.getPlanesDeCuota().get(this.getPlanesDeCuota().indexOf(this.getPlanDeCuota())));
		}
	}

	public Pago getPago() {
		return pago;
	}

	public void setPago(Pago pago) {
		this.pago = pago;
	}

	public void confirmarAnulacion() {
		if (!funcionCOPA("", "", true))
			FacesContext.getCurrentInstance().validationFailed();
		else {
			this.muestraConfirmaAnulacion = true;
			PrimeFaces.current().ajax().update("confirmaAnulacionDialog");
			PrimeFaces.current().executeScript("PF('confirmaAnulacionDialogWV').show()");
		}
	}

	public boolean isMuestraConfirmaAnulacion() {
		return muestraConfirmaAnulacion;
	}

	public void setMuestraConfirmaAnulacion(boolean muestraConfirmaAnulacion) {
		this.muestraConfirmaAnulacion = muestraConfirmaAnulacion;
	}

	public void cancelarDialogoConfirmaAnulacion() {
		this.muestraConfirmaAnulacion = false;
	}
	
	public boolean isMuestraDialogoLectorDeTarjeta() {
		return muestraDialogoLectorDeTarjeta;
	}

	public void setMuestraDialogoLectorDeTarjeta(boolean muestraDialogoLectorDeTarjeta) {
		this.muestraDialogoLectorDeTarjeta = muestraDialogoLectorDeTarjeta;
	}

	public void abrirDialogLectorDeTarjeta() {
		ArrayList<String> lstUpdt = null;
		blanquearDatosTarjeta(false); 
		muestraDialogoLectorDeTarjeta = true;
		cardBandData = "";
		this.tarjetaNumero = "";
		lstUpdt = new ArrayList<String>();
		lstUpdt.add("lectorDeTarjetaDialog");
		PrimeFaces.current().ajax().update(lstUpdt);
		PrimeFaces.current().executeScript("PF('lectorDeTarjetaDialogWV').show();setFocusAndSelectInput('cardBandData');");
	}
	
	public void cancelarDialogoLectorDeTarjeta() {
		muestraDialogoLectorDeTarjeta = false;
		
		this.blanquearDatosTarjeta(false);
	}
	
	public void blanquearDatosTarjeta(boolean omitirNroTarjeta) {
		ArrayList<String> lstUpdt = null;

		lstUpdt = new ArrayList<String>();
		
		if(!omitirNroTarjeta) {
			tarjetaNumero = "";
			lstUpdt.add("number");
		}
		
		tarjetaTitularNombre = "";
		tarjetaExpiracion = "";
		codigoSeguridad = "";
		numeroIdentificacion = "";
		peimporte = null;
		telefonoCliente = "";
		emailCliente = "";
		logo = null;
		this.setPlanesDeCuota(null);  
		
		lstUpdt.add("name");
		lstUpdt.add("expiry");
		lstUpdt.add("logoTarjeta");
		lstUpdt.add("idPlanDeCuota");
		lstUpdt.add("creditCard");
		lstUpdt.add("idPlanDeCuota");
		lstUpdt.add("cvc");
		lstUpdt.add("numeroIdentificacion");
		lstUpdt.add("peimporte");
		lstUpdt.add("telefonoCliente");
		lstUpdt.add("emailCliente");
		lstUpdt.add("logoTarjeta");
		
		PrimeFaces.current().ajax().update(lstUpdt);
		
		if(!omitirNroTarjeta)
			PrimeFaces.current().executeScript("setNext(13, 'number')");		
	}
	
	public void parsearCardData() {
		ArrayList<String> lstUpdt = null;
		Pattern pattern = null;
		Matcher matcher =  null;
		String regex= "";
		this.muestraDialogoLectorDeTarjeta = false;
		
		lstUpdt = new ArrayList<String>();
		
		if(this.getUsuario().getPagoElectronicoInstance().getPagoElectronico().getParametrosServicio().getParametros().containsKey("REGEX_CARD_BAND_PARSE")) {
			regex = this.getUsuario().getPagoElectronicoInstance().getPagoElectronico().getParametrosServicio().getParametros().get("REGEX_CARD_BAND_PARSE").getValor().toString();
			pattern = Pattern.compile(regex);
			matcher = pattern.matcher(cardBandData);

			LogACGHelper.escribirLog(null, "Cardband data: |" + cardBandData + "|");
			if(matcher.matches()) {
				if(matcher.group(1).trim().length() == 16)
					tarjetaNumero = matcher.group(1).trim().substring(0, 4).concat(" ").concat(matcher.group(1).trim().substring(4, 8)).concat(" ").concat(matcher.group(1).trim().substring(8, 12)).concat(" ").concat(matcher.group(1).trim().substring(12));
				else
					tarjetaNumero = matcher.group(1).trim();
				
				if(matcher.group(2) != null) {
					tarjetaTitularNombre = matcher.group(2).trim().concat(" ");
					if(matcher.group(3) != null){
						tarjetaTitularNombre = tarjetaTitularNombre.concat(matcher.group(3).trim()).concat(" ");
						
						if(matcher.group(4) != null)
							tarjetaTitularNombre = tarjetaTitularNombre.concat(matcher.group(4).trim());
					}
				}
				
				tarjetaExpiracion = matcher.group(6).trim().concat(" / ").concat(matcher.group(5).trim());
								
				this.funcionVABI();
				
				PrimeFaces.current().executeScript("setNext(13, 'cvc')");
				
				tipoCargaTarjeta = "A";
			}
			else {
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error al leer la banda de la tarjeta. Intente nuevamente o realice carga manual.", null));
				lstUpdt.add("msgsProductos");

				tarjetaNumero = "";
				tarjetaTitularNombre = "";
				tarjetaExpiracion = "";
				tipoCargaTarjeta = "M";
				PrimeFaces.current().executeScript("setNext(13, 'number')");
			}
		}
		else {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error al obtener configuracion de banda de tarjeta. Intente la operacion nuevamente o realice carga manual.", null));
			lstUpdt.add("msgsProductos");

			tarjetaNumero = "";
			tarjetaTitularNombre = "";
			tarjetaExpiracion = "";
			PrimeFaces.current().executeScript("setNext(13, 'number')");
		}

		lstUpdt.add("number");
		lstUpdt.add("name");
		lstUpdt.add("expiry");
		lstUpdt.add("logoTarjeta");
		lstUpdt.add("idPlanDeCuota");
		lstUpdt.add("creditCard");

		PrimeFaces.current().ajax().update(lstUpdt);
	}

	public String getCardBandData() {
		return cardBandData;
	}

	public void setCardBandData(String cardBandData) {
		this.cardBandData = cardBandData;
	}	
}
