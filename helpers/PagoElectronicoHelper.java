package com.americacg.cargavirtual.web.helpers;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.inject.Named;
import org.springframework.context.annotation.Scope;
import com.americacg.cargavirtual.gateway.pagoElectronico.client.PagoElectronico;
import com.americacg.cargavirtual.gateway.pagoElectronico.funciones.FuncionANPT;
import com.americacg.cargavirtual.gateway.pagoElectronico.funciones.FuncionCOPA;
import com.americacg.cargavirtual.gateway.pagoElectronico.funciones.FuncionGCOM;
import com.americacg.cargavirtual.gateway.pagoElectronico.funciones.FuncionGPCU;
import com.americacg.cargavirtual.gateway.pagoElectronico.funciones.FuncionLICP;
import com.americacg.cargavirtual.gateway.pagoElectronico.funciones.FuncionLPOP;
import com.americacg.cargavirtual.gateway.pagoElectronico.funciones.FuncionMCOM;
import com.americacg.cargavirtual.gateway.pagoElectronico.funciones.FuncionPAGO;
import com.americacg.cargavirtual.gateway.pagoElectronico.funciones.FuncionVABI;
import com.americacg.cargavirtual.gateway.pagoElectronico.model.HeaderInGateway;
import com.americacg.cargavirtual.gateway.pagoElectronico.model.MensajeOutboundGateway;
import com.americacg.cargavirtual.gateway.pagoElectronico.model.entidad.Identificacion;
import com.americacg.cargavirtual.gateway.pagoElectronico.model.parametros.funciones.input.DataInputFcnANPT;
import com.americacg.cargavirtual.gateway.pagoElectronico.model.parametros.funciones.input.DataInputFcnCOPA;
import com.americacg.cargavirtual.gateway.pagoElectronico.model.parametros.funciones.input.DataInputFcnGPCU;
import com.americacg.cargavirtual.gateway.pagoElectronico.model.parametros.funciones.input.DataInputFcnLICP;
import com.americacg.cargavirtual.gateway.pagoElectronico.model.parametros.funciones.input.DataInputFcnLPOP;
import com.americacg.cargavirtual.gateway.pagoElectronico.model.parametros.funciones.input.DataInputFcnMCOM;
import com.americacg.cargavirtual.gateway.pagoElectronico.model.parametros.funciones.input.DataInputFcnPAGO;
import com.americacg.cargavirtual.gateway.pagoElectronico.model.parametros.funciones.input.DataInputFcnVABI;
import com.americacg.cargavirtual.gateway.pagoElectronico.model.parametros.funciones.output.DataOutputFcnGCOM;
import com.americacg.cargavirtual.gestion.model.ArrayOfCabeceraProducto;
import com.americacg.cargavirtual.gestion.model.CabeceraProducto;
import com.americacg.cargavirtual.gestion.model.PlatCfgH2HEntidadExt;
import com.americacg.cargavirtual.web.enums.CfgTimeout;
import com.americacg.cargavirtual.web.handlers.HandledException;

@Named("pagoElectronicoHelper")
@Scope(org.springframework.web.context.WebApplicationContext.SCOPE_SESSION)
public class PagoElectronicoHelper {

	private PagoElectronico pagoElectronico = null;

	private String headerIdConfiguracionComercio = null;
	private Long headerIdMayorista = null;
	private Long headerIdCliente = null;
	private Long headerIdUsuario = null;
	private String headerUsuario = null;
	private String headerClave = null;
	private String headerOrigen = null;

	private Integer estado = 0;
	private Boolean halitadoParaWeb = false;

	private Long idMayorista = null;
	private Long idCliente = null;
	private Long idUsuario = null;
	private String username = null;
	private String password = null;
	private String tipoCliente = null;
	private Boolean debug = false;

	private Long idConfiguracionSite;
	private String configuracionSite;
	
	private String idMerchant; 
	private String nombre;
	private String email; 
	
	private String codMnemonicoArancelDebito;
	private String codMnemonicoArancelCredito;
	private Long idCuentaAcreditacion;
	
	private DataOutputFcnGCOM dataGCOM = null;
	
	private boolean inicializado = false;
	

	 
	public static PagoElectronicoHelper getInstance() throws HandledException {

		String methodName = new Object() {
		}.getClass().getEnclosingMethod().getName();

		try {
			if (UsuarioHelper.usuarioSession().getPagoElectronicoInstance() == null
					|| (UsuarioHelper.usuarioSession().getPagoElectronicoInstance() != null
							&& !UsuarioHelper.usuarioSession().getPagoElectronicoInstance().isInicializado())) {

				/*
				 * Instancio un objeto con todo deshabilitado
				 */
				UsuarioHelper.usuarioSession().setPagoElectronicoInstance(new PagoElectronicoHelper());
				UsuarioHelper.usuarioSession().getPagoElectronicoInstance().setHalitadoParaWeb(false);
				UsuarioHelper.usuarioSession().getPagoElectronicoInstance().setInicializado(false);

				List<CabeceraProducto> cpMepHabilitados = new ArrayList<CabeceraProducto>();

				/*
				 * Busco los productos MEP habilitados
				 */

				ArrayOfCabeceraProducto aocp = GestionServiceHelper.getGestionService(CfgTimeout.DEFAULT)
						.obtenerProductosHabilitadosParaOperar(UsuarioHelper.usuarioSession().getIdMayorista(),
								UsuarioHelper.usuarioSession().getIdCliente(), "ONH");

				if (aocp != null && aocp.getCabeceraProducto() != null) {
					for (CabeceraProducto cp : aocp.getCabeceraProducto()) {
						if ("MEP".equals(cp.getCodMnemonicoTipoProducto().getValue())) {
							cpMepHabilitados.add(cp);
						}
					}
				}

				if (cpMepHabilitados.size() == 0) {
					UsuarioHelper.usuarioSession().getPagoElectronicoInstance().setHalitadoParaWeb(false);
					UsuarioHelper.usuarioSession().getPagoElectronicoInstance().setInicializado(false);
				} else if (cpMepHabilitados.size() > 1) {
					UsuarioHelper.usuarioSession().getPagoElectronicoInstance().setHalitadoParaWeb(false);
					UsuarioHelper.usuarioSession().getPagoElectronicoInstance().setInicializado(false);
					throw new HandledException("E0002",
							"Pago Electronico: NO puede haber habilitado mas de un producto MEP.");
				} else if (cpMepHabilitados.size() == 1) {

					UsuarioHelper.usuarioSession().getPagoElectronicoInstance().setHalitadoParaWeb(true);

					UsuarioHelper.usuarioSession().getPagoElectronicoInstance()
							.setHeaderOrigen("M" + UsuarioHelper.usuarioSession().getIdMayorista() + "-C"
									+ UsuarioHelper.usuarioSession().getIdCliente() + "-U"
									+ UsuarioHelper.usuarioSession().getIdUsuario());

					if ("MEPNAT".equals(cpMepHabilitados.get(0).getCodMnemonicoProducto().getValue())) {
						UsuarioHelper.usuarioSession().getPagoElectronicoInstance()
								.setHeaderIdMayorista(UsuarioHelper.usuarioSession().getIdMayorista());
						UsuarioHelper.usuarioSession().getPagoElectronicoInstance()
								.setHeaderIdCliente(UsuarioHelper.usuarioSession().getIdCliente());
						UsuarioHelper.usuarioSession().getPagoElectronicoInstance()
								.setHeaderIdUsuario(UsuarioHelper.usuarioSession().getIdUsuario());
						UsuarioHelper.usuarioSession().getPagoElectronicoInstance()
								.setHeaderUsuario(UsuarioHelper.usuarioSession().getUsername());
						UsuarioHelper.usuarioSession().getPagoElectronicoInstance()
								.setHeaderClave(UsuarioHelper.usuarioSession().getPassword());
						UsuarioHelper.usuarioSession().getPagoElectronicoInstance()
								.setHeaderIdConfiguracionComercio(null);
						UsuarioHelper.usuarioSession().getPagoElectronicoInstance().setInicializado(true);

					} else if ("MEPSP".equals(cpMepHabilitados.get(0).getCodMnemonicoProducto().getValue())) {

						PlatCfgH2HEntidadExt platCfgH2HEntidadExt = GestionServiceHelper
								.getGestionService(CfgTimeout.DEFAULT)
								.obtenerPlatCfgH2HEntidadExt(UsuarioHelper.usuarioSession().getIdMayorista(),
										UsuarioHelper.usuarioSession().getIdCliente());

						if (platCfgH2HEntidadExt != null) {
							UsuarioHelper.usuarioSession().getPagoElectronicoInstance()
									.setHeaderIdMayorista(platCfgH2HEntidadExt.getIdMayoristaExterno().getValue());
							UsuarioHelper.usuarioSession().getPagoElectronicoInstance()
									.setHeaderIdCliente(platCfgH2HEntidadExt.getIdClienteExterno().getValue());
							UsuarioHelper.usuarioSession().getPagoElectronicoInstance()
									.setHeaderIdUsuario(platCfgH2HEntidadExt.getIdUsuarioExterno().getValue());
							UsuarioHelper.usuarioSession().getPagoElectronicoInstance()
									.setHeaderUsuario(platCfgH2HEntidadExt.getUsrExterno().getValue());
							UsuarioHelper.usuarioSession().getPagoElectronicoInstance()
									.setHeaderClave(platCfgH2HEntidadExt.getPwdExterno().getValue());
							UsuarioHelper.usuarioSession().getPagoElectronicoInstance()
									.setHeaderIdConfiguracionComercio(null);
							UsuarioHelper.usuarioSession().getPagoElectronicoInstance().setInicializado(true);
						} else {
							UsuarioHelper.usuarioSession().getPagoElectronicoInstance().setInicializado(false);
							throw new HandledException("E0003",
									"Pago Electronico: El comercio no se encuentra habilitado para operar con MEPSP.");
						}

					} else {
						UsuarioHelper.usuarioSession().getPagoElectronicoInstance().setInicializado(false);
						throw new HandledException("E0004",
								"Pago Electronico: Producto MEP no contemplado en esta version de la plataforma.");
					}

				} else {
					UsuarioHelper.usuarioSession().getPagoElectronicoInstance().setInicializado(false);
					throw new HandledException("E0005",
							"Pago Electronico: NO pudo consultar si producto MEP esta habilitado.");
				}

				UsuarioHelper.usuarioSession().getPagoElectronicoInstance().setPagoElectronico(new PagoElectronico(
						UsuarioHelper.usuarioSession().getIdMayorista(), UsuarioHelper.usuarioSession().getIdCliente(),
						UsuarioHelper.usuarioSession().getIdUsuario(), UsuarioHelper.usuarioSession().getUsername(),
						UsuarioHelper.usuarioSession().getPassword(), UsuarioHelper.usuarioSession().getTipoCliente(),
						false));
			} else {

			}

		} catch (Exception e) {
			UsuarioHelper.usuarioSession().getPagoElectronicoInstance().setInicializado(false);
			throw new HandledException("E0001", methodName + ". Excepcion: |" + e.getMessage() + "|");
		}

		return UsuarioHelper.usuarioSession().getPagoElectronicoInstance();
	}

	public static Integer ejecutarFuncionGCOM() throws HandledException {
		String methodName = new Object() {
		}.getClass().getEnclosingMethod().getName();

		MensajeOutboundGateway mo = null;
		FuncionGCOM fGCOM;

		try {
			UsuarioHelper.usuarioSession().getPagoElectronicoInstance().setEstado(0);

			fGCOM = new FuncionGCOM(
					UsuarioHelper.usuarioSession().getPagoElectronicoInstance().getPagoElectronico().getParametros());

			fGCOM.setParametroServicio(UsuarioHelper.usuarioSession().getPagoElectronicoInstance().getPagoElectronico()
					.getParametrosServicio());

			fGCOM.setHeaderIn(obtenerHeaderInGateway(fGCOM.getHeaderIn()));

			mo = fGCOM.ejecutar();

			if (mo != null) {
				if (mo.getHeaderOut() != null && mo.getHeaderOut().getCodigoRetorno() != null
						&& !"M0000".equals(mo.getHeaderOut().getCodigoRetorno())) {
					throw new HandledException(mo.getHeaderOut().getCodigoRetorno(),
							mo.getHeaderOut().getMensajeRetorno());
				} else if (mo.getDataOutputFcn() != null && mo.getDataOutputFcn().getCodigoRetorno() != null
						&& "M0000".equals(mo.getDataOutputFcn().getCodigoRetorno())) {
					DataOutputFcnGCOM doGCOM = (DataOutputFcnGCOM) mo.getDataOutputFcn();
					if (doGCOM != null) {
						UsuarioHelper.usuarioSession().getPagoElectronicoInstance().setEstado(doGCOM.getEstado());
						if (UsuarioHelper.usuarioSession().getPagoElectronicoInstance().getEstado().compareTo(1) == 0) {
							UsuarioHelper.usuarioSession().getPagoElectronicoInstance()
									.setHeaderIdConfiguracionComercio(String.valueOf(doGCOM.getId()));

							UsuarioHelper.usuarioSession().getPagoElectronicoInstance().setIdConfiguracionSite(Long.parseLong(doGCOM.getConfiguracionSite().getSiteId())); 
							UsuarioHelper.usuarioSession().getPagoElectronicoInstance().setConfiguracionSite(doGCOM.getConfiguracionSite().getNombre());
							UsuarioHelper.usuarioSession().getPagoElectronicoInstance().setIdMerchant(doGCOM.getIdMerchant());
							UsuarioHelper.usuarioSession().getPagoElectronicoInstance().setEmail(doGCOM.getConfiguracionSite().getEmail());
							UsuarioHelper.usuarioSession().getPagoElectronicoInstance().setEstado(doGCOM.getEstado());
							
							UsuarioHelper.usuarioSession().getPagoElectronicoInstance()
									.setCodMnemonicoArancelDebito(doGCOM.getCodMnemonicoArancelDebito());
							UsuarioHelper.usuarioSession().getPagoElectronicoInstance()
									.setCodMnemonicoArancelCredito(doGCOM.getCodMnemonicoArancelCredito());
							UsuarioHelper.usuarioSession().getPagoElectronicoInstance()
									.setIdCuentaAcreditacion(doGCOM.getIdCuentaAcreditacion());
							
							UsuarioHelper.usuarioSession().getPagoElectronicoInstance().setDataGCOM(doGCOM);
						} else {
							UsuarioHelper.usuarioSession().getPagoElectronicoInstance()
									.setHeaderIdConfiguracionComercio(null);
						}
					}
				} else {
					throw new HandledException(mo.getDataOutputFcn().getCodigoRetorno(),
							mo.getDataOutputFcn().getMensajeRetorno());
				}
			} else {
				throw new HandledException("E0020", "Se obtuvo una respuesta nula.");
			}
			return UsuarioHelper.usuarioSession().getPagoElectronicoInstance().getEstado();
		} catch (HandledException ha) {
			throw new HandledException(ha.getCode(), ha.getMessage());
		} catch (Exception e) {
			throw new HandledException("E0010", methodName + ". Excepcion: |" + e.getMessage() + "|");
		}

	}

	public static MensajeOutboundGateway ejecutarFuncionMCOM(String codMnemonicoArancelDebito, String codMnemonicoArancelCredito, Long idCuentaAcreditacion) throws HandledException {
	
		String methodName = new Object() {
		}.getClass().getEnclosingMethod().getName();

		MensajeOutboundGateway mo = null;

		try {

			FuncionMCOM fMCOM;
			fMCOM = new FuncionMCOM(UsuarioHelper.usuarioSession().getPagoElectronicoInstance().getPagoElectronico().getParametros());
			fMCOM.setParametroServicio(UsuarioHelper.usuarioSession().getPagoElectronicoInstance().getPagoElectronico().getParametrosServicio());

			fMCOM.setHeaderIn(obtenerHeaderInGateway(fMCOM.getHeaderIn()));

			fMCOM.setDataIn(new DataInputFcnMCOM());
			fMCOM.getDataIn().setIdConfiguracionComercio(Long.parseLong(UsuarioHelper.usuarioSession().getPagoElectronicoInstance().getHeaderIdConfiguracionComercio())); 
			fMCOM.getDataIn().setIdMayorista(UsuarioHelper.usuarioSession().getPagoElectronicoInstance().getHeaderIdMayorista());
			fMCOM.getDataIn().setIdCliente(UsuarioHelper.usuarioSession().getPagoElectronicoInstance().getHeaderIdCliente()); 
			fMCOM.getDataIn().setIdConfiguracionSite(UsuarioHelper.usuarioSession().getPagoElectronicoInstance().getIdConfiguracionSite()); 
			fMCOM.getDataIn().setIdMerchant(UsuarioHelper.usuarioSession().getPagoElectronicoInstance().getIdMerchant());
			fMCOM.getDataIn().setNombre(UsuarioHelper.usuarioSession().getPagoElectronicoInstance().getConfiguracionSite());
			fMCOM.getDataIn().setEmail(UsuarioHelper.usuarioSession().getPagoElectronicoInstance().getEmail());
			fMCOM.getDataIn().setEstado(UsuarioHelper.usuarioSession().getPagoElectronicoInstance().getEstado());
			fMCOM.getDataIn().setCodMnemonicoArancelDebito(codMnemonicoArancelDebito); 
			fMCOM.getDataIn().setCodMnemonicoArancelCredito(codMnemonicoArancelCredito);
			fMCOM.getDataIn().setIdCuentaAcreditacion(idCuentaAcreditacion);
			
			mo = fMCOM.ejecutar();

		} catch (Exception e) {
			throw new HandledException("E0001", methodName + ". Excepcion: |" + e.getMessage() + "|");
		}
		return mo;
	}

	public static MensajeOutboundGateway ejecutarFuncionLICP(Long idMayorista, Long idCliente, Long idUsuario,
			Integer pageSize, Integer page, String campoOrden, String tipoOrden, Date fechaDesde, Date fechaHasta,
			Long idDePago) throws HandledException {

		String methodName = new Object() {
		}.getClass().getEnclosingMethod().getName();

		MensajeOutboundGateway mo = null;

		try {

			FuncionLICP fLICP;
			fLICP = new FuncionLICP(
					UsuarioHelper.usuarioSession().getPagoElectronicoInstance().getPagoElectronico().getParametros());
			fLICP.setParametroServicio(UsuarioHelper.usuarioSession().getPagoElectronicoInstance().getPagoElectronico()
					.getParametrosServicio());

			fLICP.setHeaderIn(obtenerHeaderInGateway(fLICP.getHeaderIn()));

			fLICP.setDataIn(new DataInputFcnLICP());
			fLICP.getDataIn().setIdMayorista(idMayorista); // mayorista
			fLICP.getDataIn().setIdCliente(idCliente); // cliente
			fLICP.getDataIn().setIdUsuario(idUsuario); // usuario
			fLICP.getDataIn().setPageSize(pageSize); // page size
			fLICP.getDataIn().setPage(page); // page
			fLICP.getDataIn().setCampoOrden(campoOrden); // campo orden

			fLICP.getDataIn().setFechaDesde(fechaDesde); // Fecha desde
			fLICP.getDataIn().setFechaHasta(fechaHasta); // Fecha hasta
			fLICP.getDataIn().setTipoOrden(tipoOrden); // tipo Orden
			fLICP.getDataIn().setIdDePago(idDePago); // idPago

			mo = fLICP.ejecutar();

		} catch (Exception e) {
			throw new HandledException("E0001", methodName + ". Excepcion: |" + e.getMessage() + "|");
		}
		return mo;
	}

	public static MensajeOutboundGateway ejecutarFuncionGPCU(Long idMayorista, Long idCliente, Long idUsuario,
			Integer pageSize, Integer page, String campoOrden, String tipoOrden, Long idBanco, Long idMedioPago,
			Date fechaCobranza, Integer importe) throws HandledException {

		String methodName = new Object() {
		}.getClass().getEnclosingMethod().getName();

		MensajeOutboundGateway mo = null;

		try {

			FuncionGPCU fGPCU;
			fGPCU = new FuncionGPCU(
					UsuarioHelper.usuarioSession().getPagoElectronicoInstance().getPagoElectronico().getParametros());
			fGPCU.setParametroServicio(UsuarioHelper.usuarioSession().getPagoElectronicoInstance().getPagoElectronico()
					.getParametrosServicio());

			fGPCU.setHeaderIn(obtenerHeaderInGateway(fGPCU.getHeaderIn()));
			fGPCU.setDataIn(new DataInputFcnGPCU());
			fGPCU.getDataIn().setIdMayorista(idMayorista); // mayorista
			fGPCU.getDataIn().setIdCliente(idCliente); // cliente
			fGPCU.getDataIn().setIdUsuario(idUsuario); // usuario
			fGPCU.getDataIn().setPageSize(pageSize); // page size
			fGPCU.getDataIn().setPage(page); // page
			fGPCU.getDataIn().setCampoOrden(campoOrden); // campo orden
			fGPCU.getDataIn().setTipoOrden(tipoOrden); // tipo Orden

			fGPCU.getDataIn().setIdBanco(idBanco);
			fGPCU.getDataIn().setIdMedioDePago(idMedioPago);
			fGPCU.getDataIn().setFechaCobranza(fechaCobranza);
			fGPCU.getDataIn().setImporte(importe);

			mo = fGPCU.ejecutar();

		} catch (Exception e) {
			throw new HandledException("E0001", methodName + ". Excepcion: |" + e.getMessage() + "|");
		}
		return mo;
	}

	public static MensajeOutboundGateway ejecutarFuncionPAGO(String nroInternoACG, String tarjetaNumero,
			String tarjetaExpiracionMes, String tarjetaExpiracionAnio, String tarjetaCodigoSeguridad,
			String tarjetaTitularNombre, String tipoIdentificacion, String numeroIdentificacion, Long idMedioPago,
			Integer importe, String moneda, String clienteTelefono, String clienteEmail, Long idBanco,
			Long idPlanDeCuota, Long idConceptoDePago, String datosAdicionalesPago, String idTerminal, String tipoCargaTarjeta)
			throws HandledException {

		String methodName = new Object() {
		}.getClass().getEnclosingMethod().getName();

		MensajeOutboundGateway mo = null;

		try {

			FuncionPAGO fPAGO;
			fPAGO = new FuncionPAGO(
					UsuarioHelper.usuarioSession().getPagoElectronicoInstance().getPagoElectronico().getParametros());
			fPAGO.setParametroServicio(UsuarioHelper.usuarioSession().getPagoElectronicoInstance().getPagoElectronico()
					.getParametrosServicio());

			fPAGO.setHeaderIn(obtenerHeaderInGateway(fPAGO.getHeaderIn()));

			fPAGO.setDataIn(new DataInputFcnPAGO());

			fPAGO.getDataIn().setNumeroTicketACG(nroInternoACG);
			/*if (bin != null) {
				fPAGO.getDataIn().setBin(bin);
			} else {
				fPAGO.getDataIn().setBin("");
			}*/
			try {
				fPAGO.getDataIn().setTarjetaNumero(tarjetaNumero.replace(" ", ""));
			} catch (Exception e) {
				fPAGO.getDataIn().setTarjetaNumero(tarjetaNumero);
			}

			fPAGO.getDataIn().setTarjetaExpiracionMes(tarjetaExpiracionMes);
			fPAGO.getDataIn().setTarjetaExpiracionAnio(tarjetaExpiracionAnio);

			fPAGO.getDataIn().setCodigoSeguridad(tarjetaCodigoSeguridad);
			fPAGO.getDataIn().setTarjetaTitularNombre(tarjetaTitularNombre);

			Identificacion oIdentificacion = new Identificacion(tipoIdentificacion, numeroIdentificacion);
			fPAGO.getDataIn().setTarjetaTitularIdentificacion(oIdentificacion);

			if (idMedioPago != null) {
				if (idMedioPago.compareTo(-1L) == 0) {
					fPAGO.getDataIn().setIdMedioPago(null);
				} else {
					fPAGO.getDataIn().setIdMedioPago(idMedioPago);
				}
			} else {
				fPAGO.getDataIn().setIdMedioPago(null);
			}
			fPAGO.getDataIn().setImporte(importe);
			fPAGO.getDataIn().setMoneda(moneda);
			fPAGO.getDataIn().setTelefonoCliente(clienteTelefono);
			fPAGO.getDataIn().setEmailCliente(clienteEmail);
			fPAGO.getDataIn().setIdBanco(idBanco);
			fPAGO.getDataIn().setIdPlanDeCuota(idPlanDeCuota);
			fPAGO.getDataIn().setIdConceptoDePago(idConceptoDePago);
			fPAGO.getDataIn().setDatosAdicionalesPago(datosAdicionalesPago);

			fPAGO.getDataIn().setTipoCargaTarjeta(tipoCargaTarjeta);
			fPAGO.getDataIn().setIdTerminal(idTerminal);

			mo = fPAGO.ejecutar();

		} catch (Exception e) {
			throw new HandledException("E0001", methodName + ". Excepcion: |" + e.getMessage() + "|");
		}
		return mo;

	}

	public static MensajeOutboundGateway ejecutarFuncionANPT(Long idMayorista, Long idCliente, Long idUsuario,
			String numeroTicketACG, Integer idPagoOperador, String idTerminal) throws HandledException {

		String methodName = new Object() {
		}.getClass().getEnclosingMethod().getName();

		MensajeOutboundGateway mo = null;

		try {

			FuncionANPT fANPT;
			fANPT = new FuncionANPT(
					UsuarioHelper.usuarioSession().getPagoElectronicoInstance().getPagoElectronico().getParametros());
			fANPT.setParametroServicio(UsuarioHelper.usuarioSession().getPagoElectronicoInstance().getPagoElectronico()
					.getParametrosServicio());

			fANPT.setHeaderIn(obtenerHeaderInGateway(fANPT.getHeaderIn()));

			fANPT.setDataIn(new DataInputFcnANPT());
			fANPT.getDataIn().setNumeroTicketACG(numeroTicketACG);
			fANPT.getDataIn().setPaymentId(idPagoOperador);
			fANPT.getDataIn().setIdTerminal(idTerminal);

			mo = fANPT.ejecutar();

		} catch (Exception e) {
			throw new HandledException("E0001", methodName + ". Excepcion: |" + e.getMessage() + "|");
		}
		return mo;
	}

	public static MensajeOutboundGateway ejecutarFuncionCOPA(String tipoBusqueda, String idPago,
			Integer tarjetaDatosAdicionales, Integer omitirRequestOperador, Integer dataObjectReturnType)
			throws HandledException {
		String methodName = new Object() {
		}.getClass().getEnclosingMethod().getName();

		MensajeOutboundGateway mo = null;

		try {

			FuncionCOPA fCOPA;
			fCOPA = new FuncionCOPA(
					UsuarioHelper.usuarioSession().getPagoElectronicoInstance().getPagoElectronico().getParametros());
			fCOPA.setParametroServicio(UsuarioHelper.usuarioSession().getPagoElectronicoInstance().getPagoElectronico()
					.getParametrosServicio());

			fCOPA.setHeaderIn(obtenerHeaderInGateway(fCOPA.getHeaderIn()));

			fCOPA.setDataIn(new DataInputFcnCOPA());

			if ("NRO_COMPROBANTE".equals(tipoBusqueda.toUpperCase())) {
				fCOPA.getDataIn().setPaymentId(Integer.parseInt(idPago));
			} else {
				fCOPA.getDataIn().setIdPagoACG(idPago);
			}
			fCOPA.getDataIn().setTarjetaDatosAdicionales(tarjetaDatosAdicionales);
			fCOPA.getDataIn().setOmitirRequestOperador(omitirRequestOperador);
			fCOPA.getDataIn().setDataObjectReturnType(dataObjectReturnType);

			mo = fCOPA.ejecutar();

		} catch (Exception e) {
			throw new HandledException("E0001", methodName + ". Excepcion: |" + e.getMessage() + "|");
		}
		return mo;
	}

	public static MensajeOutboundGateway ejecutarFuncionVABI(String bin) throws HandledException {

		String methodName = new Object() {
		}.getClass().getEnclosingMethod().getName();

		MensajeOutboundGateway mo = null;

		try {

			FuncionVABI fVABI;
			fVABI = new FuncionVABI(
					UsuarioHelper.usuarioSession().getPagoElectronicoInstance().getPagoElectronico().getParametros());
			fVABI.setParametroServicio(UsuarioHelper.usuarioSession().getPagoElectronicoInstance().getPagoElectronico()
					.getParametrosServicio());

			fVABI.setHeaderIn(obtenerHeaderInGateway(fVABI.getHeaderIn()));

			fVABI.setDataIn(new DataInputFcnVABI());
			fVABI.getDataIn().setBin(bin);

			mo = fVABI.ejecutar();

		} catch (Exception e) {
			throw new HandledException("E0001", methodName + ". Excepcion: |" + e.getMessage() + "|");
		}
		return mo;
	}
	
	public static MensajeOutboundGateway ejecutarFuncionLPOP(Long idMayorista, Long operador, String siteTransactionId, String siteId, String merchantId, Date fechaDesde, Date fechaHasta)
			throws HandledException {
		String methodName = new Object() {
		}.getClass().getEnclosingMethod().getName();

		MensajeOutboundGateway mo = null;

		try {

			FuncionLPOP fLPOP;
			fLPOP = new FuncionLPOP(
					UsuarioHelper.usuarioSession().getPagoElectronicoInstance().getPagoElectronico().getParametros());
			fLPOP.setParametroServicio(UsuarioHelper.usuarioSession().getPagoElectronicoInstance().getPagoElectronico()
					.getParametrosServicio());

			fLPOP.setHeaderIn(obtenerHeaderInGateway(fLPOP.getHeaderIn()));

			fLPOP.setDataIn(new DataInputFcnLPOP());

			fLPOP.getDataIn().setIdMayorista(idMayorista);
			fLPOP.getDataIn().setOperador(operador);
			fLPOP.getDataIn().setSiteTransactionId(siteTransactionId);
			fLPOP.getDataIn().setSiteId(siteId);
			fLPOP.getDataIn().setMerchantId(merchantId);
			fLPOP.getDataIn().setFechaDesde(fechaDesde);
			fLPOP.getDataIn().setFechaHasta(fechaHasta);
			fLPOP.getDataIn().setPage(0);
			fLPOP.getDataIn().setPageSize(Integer.MAX_VALUE);

			mo = fLPOP.ejecutar();

		} catch (Exception e) {
			throw new HandledException("E0001", methodName + ". Excepcion: |" + e.getMessage() + "|");
		}
		return mo;
	}

	private static HeaderInGateway obtenerHeaderInGateway(HeaderInGateway headerInGateway) {

		headerInGateway
				.setIdMayorista(UsuarioHelper.usuarioSession().getPagoElectronicoInstance().getHeaderIdMayorista());//
		headerInGateway.setIdCliente(UsuarioHelper.usuarioSession().getPagoElectronicoInstance().getHeaderIdCliente()); //
		headerInGateway.setIdUsuario(UsuarioHelper.usuarioSession().getPagoElectronicoInstance().getHeaderIdUsuario());
		headerInGateway.setUsuario(UsuarioHelper.usuarioSession().getPagoElectronicoInstance().getHeaderUsuario());
		headerInGateway.setClave(UsuarioHelper.usuarioSession().getPagoElectronicoInstance().getHeaderClave());//
		headerInGateway.setIdConfiguracionComercio(
				UsuarioHelper.usuarioSession().getPagoElectronicoInstance().getHeaderIdConfiguracionComercio());
		headerInGateway.setOrigen(UsuarioHelper.usuarioSession().getPagoElectronicoInstance().getHeaderOrigen());

		return headerInGateway;
	}

	public PagoElectronico getPagoElectronico() {
		return pagoElectronico;
	}

	public void setPagoElectronico(PagoElectronico pagoElectronico) {
		this.pagoElectronico = pagoElectronico;
	}

	public String getHeaderIdConfiguracionComercio() {
		return headerIdConfiguracionComercio;
	}

	public void setHeaderIdConfiguracionComercio(String headerIdConfiguracionComercio) {
		this.headerIdConfiguracionComercio = headerIdConfiguracionComercio;
	}

	public Long getHeaderIdMayorista() {
		return headerIdMayorista;
	}

	public void setHeaderIdMayorista(Long headerIdMayorista) {
		this.headerIdMayorista = headerIdMayorista;
	}

	public Long getHeaderIdCliente() {
		return headerIdCliente;
	}

	public void setHeaderIdCliente(Long headerIdCliente) {
		this.headerIdCliente = headerIdCliente;
	}

	public Long getHeaderIdUsuario() {
		return headerIdUsuario;
	}

	public void setHeaderIdUsuario(Long headerIdUsuario) {
		this.headerIdUsuario = headerIdUsuario;
	}

	public String getHeaderUsuario() {
		return headerUsuario;
	}

	public void setHeaderUsuario(String headerUsuario) {
		this.headerUsuario = headerUsuario;
	}

	public String getHeaderClave() {
		return headerClave;
	}

	public void setHeaderClave(String headerClave) {
		this.headerClave = headerClave;
	}

	public String getHeaderOrigen() {
		return headerOrigen;
	}

	public void setHeaderOrigen(String headerOrigen) {
		this.headerOrigen = headerOrigen;
	}

	public Integer getEstado() {
		return estado;
	}

	public void setEstado(Integer estado) {
		this.estado = estado;
	}

	public Boolean getHalitadoParaWeb() {
		return halitadoParaWeb;
	}

	public void setHalitadoParaWeb(Boolean halitadoParaWeb) {
		this.halitadoParaWeb = halitadoParaWeb;
	}

	public Long getIdMayorista() {
		return idMayorista;
	}

	public void setIdMayorista(Long idMayorista) {
		this.idMayorista = idMayorista;
	}

	public Long getIdCliente() {
		return idCliente;
	}

	public void setIdCliente(Long idCliente) {
		this.idCliente = idCliente;
	}

	public Long getIdUsuario() {
		return idUsuario;
	}

	public void setIdUsuario(Long idUsuario) {
		this.idUsuario = idUsuario;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getTipoCliente() {
		return tipoCliente;
	}

	public void setTipoCliente(String tipoCliente) {
		this.tipoCliente = tipoCliente;
	}

	public Boolean getDebug() {
		return debug;
	}

	public void setDebug(Boolean debug) {
		this.debug = debug;
	}

	public boolean isInicializado() {
		return inicializado;
	}

	public void setInicializado(boolean inicializado) {
		this.inicializado = inicializado;
	}

	public String getCodMnemonicoArancelDebito() {
		return codMnemonicoArancelDebito;
	}

	public void setCodMnemonicoArancelDebito(String codMnemonicoArancelDebito) {
		this.codMnemonicoArancelDebito = codMnemonicoArancelDebito;
	}

	public String getCodMnemonicoArancelCredito() {
		return codMnemonicoArancelCredito;
	}

	public void setCodMnemonicoArancelCredito(String codMnemonicoArancelCredito) {
		this.codMnemonicoArancelCredito = codMnemonicoArancelCredito;
	}

	public Long getIdCuentaAcreditacion() {
		return idCuentaAcreditacion;
	}

	public void setIdCuentaAcreditacion(Long idCuentaAcreditacion) {
		this.idCuentaAcreditacion = idCuentaAcreditacion;
	}

	public Long getIdConfiguracionSite() {
		return idConfiguracionSite;
	}

	public void setIdConfiguracionSite(Long idConfiguracionSite) {
		this.idConfiguracionSite = idConfiguracionSite;
	}

	public String getConfiguracionSite() {
		return configuracionSite;
	}

	public void setConfiguracionSite(String configuracionSite) {
		this.configuracionSite = configuracionSite;
	}

	public String getIdMerchant() {
		return idMerchant;
	}

	public void setIdMerchant(String idMerchant) {
		this.idMerchant = idMerchant;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public DataOutputFcnGCOM getDataGCOM() {
		return dataGCOM;
	}

	public void setDataGCOM(DataOutputFcnGCOM dataGCOM) {
		this.dataGCOM = dataGCOM;
	}

}
