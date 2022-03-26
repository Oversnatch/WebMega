package com.americacg.cargavirtual.web.mngbeans.reportes;

import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import javax.faces.application.FacesMessage;
import javax.inject.Named;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import org.primefaces.PrimeFaces;
import org.springframework.context.annotation.Scope;
import com.americacg.cargavirtual.gateway.pagoElectronico.model.parametros.funciones.output.DataOutputFcnCOPA;
import com.americacg.cargavirtual.gestion.model.FiltroListadoPlatMovimientoCuenta;
import com.americacg.cargavirtual.gestion.model.ObjectFactory;
import com.americacg.cargavirtual.gestion.model.PlatCuenta;
import com.americacg.cargavirtual.gestion.model.PlatEstadoMovimientoCuenta;
import com.americacg.cargavirtual.gestion.model.PlatMovimientoCuenta;
import com.americacg.cargavirtual.gestion.model.PlatMovimientoCuentaList;
import com.americacg.cargavirtual.web.enums.CfgTimeout;
import com.americacg.cargavirtual.web.handlers.HandledException;
import com.americacg.cargavirtual.web.helpers.GestionServiceHelper;
import com.americacg.cargavirtual.web.helpers.LogACGHelper;
import com.americacg.cargavirtual.web.helpers.PagoElectronicoHelper;
import com.americacg.cargavirtual.web.helpers.UsuarioHelper;
import com.americacg.cargavirtual.web.model.Error;
import com.americacg.cargavirtual.web.model.superPago.Cuenta;
import com.americacg.cargavirtual.web.model.superPago.Operacion;
import com.americacg.cargavirtual.web.model.superPago.OperacionDetalle;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;

@Named("reporteSuperPagoOperacionesView")
@Scope(org.springframework.web.context.WebApplicationContext.SCOPE_SESSION)
public class ReporteSuperPagoOperacionesView extends ReporteGeneral<Operacion> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5593796093654181336L;
	private ObjectFactory oGestionObjFac = new ObjectFactory();

	/**
	 * 
	 */
	/*
	 * private static final long serialVersionUID = -6216664193927846349L; private
	 * String estadoTransacciones; private Long idClienteInferior; private Long
	 * idProducto; private Long idProveedor; private Long idUsuario; private Long
	 * idLote; private Integer caracteristicaTelefono; private String
	 * identifTerminal; private String imeiTerminal;
	 * 
	 * private Integer tipoFiltroCliente;
	 * 
	 * private String tipoTRX; private Long idTipoTerminal;
	 * 
	 * private Float importeTotal = 0F; private Integer cantRegistros = 0; private
	 * Integer cantidadOperaciones = 0; private Float promedioTotal = 0F;
	 * 
	 * private List<SelectItem> productos; private List<SelectItem> proveedores;
	 * 
	 * private List<SelectItem> nivelesJerarquia;
	 * 
	 * private String fechaHoraDesdeMostrar; private String fechaHoraHastaMostrar;
	 * 
	 * private Boolean agrupar; private Boolean agrUsu; private Boolean agrProv;
	 * private Boolean agrFec; private Boolean agrTerm; private Boolean agrCarTel;
	 * private Boolean agrProd; private Boolean agrTipoTRX; private Boolean
	 * infHistorico; private Boolean agrLote; private Boolean agrIdentifTerminal;
	 * private Boolean agrImeiTerminal; private Integer nivelJerarquia; private
	 * Boolean agrModTerm;
	 * 
	 * private List<SelectItem> tipoTerminales;
	 * 
	 * private Long idPais; private List<SelectItem> paises; private Long
	 * idProvincia; private List<SelectItem> provincias; private Long idLocalidad;
	 * private List<SelectItem> localidades; private Long idZona; private
	 * List<SelectItem> zonas; private Boolean agrProvincia = false; private Boolean
	 * agrLocalidad = false; private Boolean agrPais = false; private Boolean
	 * agrZona = false; private Boolean agrSube_DistribuidorRed = false;
	 * 
	 * private Long sube_idDistribuidorRed; private List<SelectItem>
	 * listDistribuidorRed;
	 * 
	 * private RespResumenTrxsContainer rt;
	 */

	@Override
	public Error resetearReporte() {
		Error e = super.resetearReporte();
		this.cantRegistros = 0;

		/*
		 * this.importeTotal = 0F;
		 * 
		 * this.cantidadOperaciones = 0; this.promedioTotal = 0F; this.idClienteInferior
		 * = null; this.idUsuario = null; this.idLote = null; this.identifTerminal =
		 * null; this.imeiTerminal = null; this.caracteristicaTelefono = null;
		 * this.estadoTransacciones = null; this.idProducto = null; this.idProveedor =
		 * null; this.tipoTRX = null; this.idTipoTerminal = null; this.tipoFiltroCliente
		 * = 3; this.fechaHoraDesdeMostrar = null; this.fechaHoraHastaMostrar = null;
		 * this.agrupar = false; this.agrUsu = false; this.agrProv = false; this.agrFec
		 * = false; this.agrTerm = false; this.agrCarTel = false; this.agrProd = null;
		 * this.agrTipoTRX = null; this.infHistorico = null; this.agrLote = null;
		 * this.agrModTerm = null; this.agrIdentifTerminal = null; this.agrImeiTerminal
		 * = null; this.nivelJerarquia = 0; this.idPais = null; this.idProvincia = null;
		 * this.idLocalidad = null; this.idZona = null; this.agrPais = false;
		 * this.agrProvincia = false; this.agrLocalidad = false; this.agrZona = false;
		 * this.sube_idDistribuidorRed = null; this.agrSube_DistribuidorRed = false;
		 * 
		 * try { this.listDistribuidorRed = new ArrayList<SelectItem>();
		 * DescripcionContainer cd =
		 * GestionServiceHelper.getGestionService(CfgTimeout.REPORTE)
		 * .mostrarSubeDistribuidorRed(this.getUsuario().getIdMayorista());
		 * 
		 * for (Descripcion d : cd.getListDescripcion().getValue().getDescripcion()) {
		 * 
		 * if (d.getDescripcion().getValue() != null) { if
		 * (d.getDescripcion().getValue().length() > 0) { this.listDistribuidorRed
		 * .add(new SelectItem(d.getId().getValue(), d.getDescripcion().getValue())); }
		 * }
		 * 
		 * } this.provincias = new ArrayList<SelectItem>(); this.localidades = new
		 * ArrayList<SelectItem>();
		 * 
		 * this.paises = new ArrayList<SelectItem>(); DescripcionContainer p =
		 * GestionServiceHelper.getGestionService(CfgTimeout.REPORTE)
		 * .mostrarPaises(this.getUsuario().getIdMayorista(), null);
		 * 
		 * if ((p != null) &&
		 * (!p.getListDescripcion().getValue().getDescripcion().isEmpty())) { for
		 * (Descripcion d : p.getListDescripcion().getValue().getDescripcion()) {
		 * this.paises.add(new SelectItem(d.getId().getValue(),
		 * d.getDescripcion().getValue())); } }
		 * 
		 * this.zonas = new ArrayList<SelectItem>(); DescripcionContainer z =
		 * GestionServiceHelper.getGestionService(CfgTimeout.REPORTE)
		 * .mostrarZonas(this.getUsuario().getIdMayorista(), null);
		 * 
		 * for (Descripcion d : z.getListDescripcion().getValue().getDescripcion()) {
		 * this.zonas.add(new SelectItem(d.getId().getValue(),
		 * d.getDescripcion().getValue())); } this.productos = new
		 * ArrayList<SelectItem>(); ArrayOfCabeceraProducto l =
		 * GestionServiceHelper.getGestionService(CfgTimeout.REPORTE)
		 * .mostrarCabeceraProductos(this.getUsuario().getIdMayorista(), null, false);
		 * for (CabeceraProducto cp : l.getCabeceraProducto()) { this.productos.add(new
		 * SelectItem(cp.getIdProducto().getValue(), cp.getDescProducto().getValue()));
		 * } this.proveedores = new ArrayList<SelectItem>(); ArrayOfSaldoProveedor lp =
		 * GestionServiceHelper.getGestionService(CfgTimeout.REPORTE)
		 * .obtenerSaldoProveedores(this.getUsuario().getIdMayorista(), null, false,
		 * false); for (SaldoProveedor sp : lp.getSaldoProveedor()) {
		 * this.proveedores.add(new SelectItem(sp.getIdProveedor().getValue(),
		 * sp.getRazonSocial().getValue())); } this.tipoTerminales = new
		 * ArrayList<SelectItem>(); DescripcionContainer tt =
		 * GestionServiceHelper.getGestionService(CfgTimeout.REPORTE)
		 * .mostrarTipoTerminales(this.getUsuario().getIdMayorista());
		 * 
		 * for (Descripcion d : tt.getListDescripcion().getValue().getDescripcion()) {
		 * this.tipoTerminales.add(new SelectItem(d.getId().getValue(),
		 * d.getDescripcion().getValue())); } this.nivelesJerarquia = new
		 * ArrayList<SelectItem>();
		 * 
		 * int ini = 0;
		 * 
		 * if (this.getUsuario().getNivelDistribuidorSuperior() == null) { ini = 0; }
		 * else { ini = this.getUsuario().getNivelDistribuidorSuperior(); }
		 * 
		 * Boolean primerIngreso = true;
		 * 
		 * for (int i = ini; i < 6; i++) {
		 * 
		 * if (primerIngreso) { this.nivelesJerarquia.add(new SelectItem(0, i + "")); }
		 * else { this.nivelesJerarquia.add(new SelectItem(i, i + "")); }
		 * 
		 * primerIngreso = false;
		 * 
		 * }
		 * 
		 * } catch (WebServiceException ste) { if (ste != null && ste.getCause() != null
		 * && ste.getCause().getClass() != null) { if
		 * (ste.getCause().getClass().equals(ConnectException.class)) {
		 * e.setError("GST-TOC",
		 * "No se pudo establecer la comunicación (GST-TOC).\n Por favor intente nuevamente."
		 * ); } else if (ste.getCause().getClass().equals(SocketTimeoutException.class))
		 * { e.setError("GST-TRW",
		 * "No se pudo establecer la comunicación (GST-TRW).\n Por favor intente nuevamente."
		 * ); } else if (ste.getCause().getClass().equals(UnknownHostException.class)) {
		 * LogACGHelper.escribirLog(null,
		 * "No se pudo establecer la comunicación (GST-HNC): |" + ste.getMessage() +
		 * "|"); e.setError("GST-TRW",
		 * "No se pudo establecer la comunicación (GST-HNC).\n Por favor intente nuevamente."
		 * ); } else { LogACGHelper.escribirLog(null,
		 * "Informe Transacciones Resumido. Error ejecutando el WS de consulta: |" +
		 * ste.getMessage() + "|"); e.setError("GST-OTR",
		 * "No se pudo establecer la comunicación (GST-OTR).\n Por favor intente nuevamente."
		 * ); } } else { LogACGHelper.escribirLog(null,
		 * "Informe Transacciones Resumido. Error ejecutando el WS de consulta: |" +
		 * ste.getMessage() + "|"); e.setError("GST-OTR",
		 * "No se pudo establecer la comunicación (GST-OTR).\n Por favor intente nuevamente."
		 * ); } FacesContext.getCurrentInstance().validationFailed(); } catch (Exception
		 * ex) { LogACGHelper.escribirLog(null,
		 * "Informe Transacciones Resumido. Excepcion ejecutando el WS de consulta: |" +
		 * ex.getMessage() + "|"); e.setError("GST-OTR",
		 * "No se pudo establecer la comunicación (GST-OTR).\n Por favor intente nuevamente."
		 * ); }
		 */
		return e;
	}

	public ReporteSuperPagoOperacionesView() {
		super();
	}

	public void realizarInforme() {
		this.exportToExcel = false;
		informe();
	}

	public void exportarInforme() {
		this.exportToExcel = true;
		informe();
	}

	public void informe() {

		XMLGregorianCalendar xmlGCFechaSaldo = null;
		GregorianCalendar gcFechaSaldo = null;
		PlatEstadoMovimientoCuenta oEstMovCta = null;
		Cuenta oCtaAux = null;
		
		try {

			this.list = new ArrayList<Operacion>();
			this.mostrarRegistros = true;
			cantRegistros = 0;

			// Indica la cantidad de registros que voy a usar en el limit de la query a la
			// base de datos
			Integer cantRegistrosFiltro = null;
			if (exportToExcel) {
				cantRegistrosFiltro = cantMaxRegistrosAexportar;
			} else {
				cantRegistrosFiltro = cantMaxRegistrosAmostrarPorPantalla;
			}

			PlatMovimientoCuentaList pmcl = null;
			FiltroListadoPlatMovimientoCuenta flpmc = new FiltroListadoPlatMovimientoCuenta();

			ObjectFactory factory = new ObjectFactory();

			flpmc.setIdMayorista(factory.createFiltroListadoPlatMovimientoCuentaIdMayorista(
					UsuarioHelper.usuarioSession().getSuperPagoInstance().getCuentaSeleccionada().getIdMayorista()));

			/*
			 * flpmc.setIdCuenta(factory.createFiltroListadoPlatMovimientoCuentaIdCuenta(
			 * UsuarioHelper.usuarioSession().getSuperPagoInstance().getCuentaSeleccionada()
			 * .getIdCuenta()));
			 */

			PlatCuenta oCta = factory.createPlatCuenta();
			oCta.setIdCuenta(factory.createPlatCuentaIdCuenta(
					UsuarioHelper.usuarioSession().getSuperPagoInstance().getCuentaSeleccionada().getIdCuenta()));

			PlatMovimientoCuenta oMovCta = new PlatMovimientoCuenta();
			oMovCta.setCuenta(factory.createPlatMovimientoCuentaCuenta(oCta));

			flpmc.setPage(factory.createFiltroListadoPlatMovimientoCuentaPage(1));
			flpmc.setPageSize(factory.createFiltroListadoPlatMovimientoCuentaPageSize(Integer.MAX_VALUE));
			if (UsuarioHelper.usuarioSession().getSuperPagoInstance().getOperacionesPendientes()) {
				// OPERACIONES PENDIENTES
				flpmc.setCampoOrden(factory
						.createFiltroListadoPlatMovimientoCuentaCampoOrden("fechaMovimiento DESC, idOrigen, nroOrden"));
			} else {
				flpmc.setCampoOrden(factory.createFiltroListadoPlatMovimientoCuentaCampoOrden(
						"fechaAcreditacion DESC, idOrigen, nroOrden"));
			}
			flpmc.setTipoOrden(factory.createFiltroListadoPlatMovimientoCuentaTipoOrden(""));

			try {

				Date referenceDate = new Date();
				Calendar c = Calendar.getInstance();
				c.setTime(referenceDate);
				c.add(Calendar.MONTH, -6);
				c.set(Calendar.DAY_OF_MONTH, 1);
				c.set(Calendar.HOUR_OF_DAY, 0);
				c.set(Calendar.MINUTE, 0);
				c.set(Calendar.SECOND, 0);
				// c.set(c.YEAR, c.MONTH, 1, 0, 0, 0);

				GregorianCalendar gcFechaHoraDesde = new GregorianCalendar();
				gcFechaHoraDesde.setTime(c.getTime());
				XMLGregorianCalendar xmlGCFechaHoraDesde;

				xmlGCFechaHoraDesde = DatatypeFactory.newInstance().newXMLGregorianCalendar(gcFechaHoraDesde);
				oMovCta.setFechaMovimiento(xmlGCFechaHoraDesde);
				// flpmc.setFechaMovimiento(xmlGCFechaHoraDesde);
			} catch (DatatypeConfigurationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			if (UsuarioHelper.usuarioSession().getSuperPagoInstance().getOperacionesPendientes()) {
				// OPERACIONES PENDIENTES
				oEstMovCta = new PlatEstadoMovimientoCuenta();
				oEstMovCta.setCodMnemonico(factory.createPlatEstadoMovimientoCuentaCodMnemonico("PENDACRE"));

				oMovCta.setEstado(factory.createPlatMovimientoCuentaEstado(oEstMovCta));

				// flpmc.setCodMnemonicoEstado(factory.createFiltroListadoPlatMovimientoCuentaCodMnemonicoEstado("PENDACRE"));
				flpmc.setMovimientoCuenta(factory.createFiltroListadoPlatMovimientoCuentaMovimientoCuenta(oMovCta));
				pmcl = GestionServiceHelper.getGestionService(CfgTimeout.DEFAULT).listarPlatMovimientoCuenta(flpmc);
			} else {
				// OPERACIONES ACREDITADAS
				oEstMovCta = new PlatEstadoMovimientoCuenta();
				oEstMovCta.setCodMnemonico(factory.createPlatEstadoMovimientoCuentaCodMnemonico("ACRE"));

				oMovCta.setEstado(factory.createPlatMovimientoCuentaEstado(oEstMovCta));

				// flpmc.setCodMnemonicoEstado(factory.createFiltroListadoPlatMovimientoCuentaCodMnemonicoEstado("ACRE"));
				flpmc.setMovimientoCuenta(factory.createFiltroListadoPlatMovimientoCuentaMovimientoCuenta(oMovCta));
				pmcl = GestionServiceHelper.getGestionService(CfgTimeout.DEFAULT).listarPlatMovimientoCuenta(flpmc);
			}

			// Obtengo el saldo en caso de ser necesario
			if (!UsuarioHelper.usuarioSession().getSuperPagoInstance().getOperacionesPendientes()) {
				oCtaAux = this.obtenerCuentaSuperPago(UsuarioHelper.usuarioSession().getSuperPagoInstance().getCuentaSeleccionada());
				
				if(oCtaAux != null) {
					UsuarioHelper.usuarioSession().getSuperPagoInstance().getCuentaSeleccionada().setSaldo(oCtaAux.getSaldo());
				} else {
					UsuarioHelper.usuarioSession().getSuperPagoInstance().getCuentaSeleccionada().setSaldo(0F);
				}

				/*rf = GestionServiceHelper.getGestionService(CfgTimeout.DEFAULT).obtenerSaldoActual(null,
						this.getUsuario().getIdMayorista(),
						UsuarioHelper.usuarioSession().getSuperPagoInstance().getCuentaSeleccionada().getIdCuenta());
				if (rf != null) {
					UsuarioHelper.usuarioSession().getSuperPagoInstance().getCuentaSeleccionada()
							.setSaldo(rf.getValor().getValue());
				} else {
					UsuarioHelper.usuarioSession().getSuperPagoInstance().getCuentaSeleccionada().setSaldo(0F);
				}*/
			}

			// GENERO Y LIMPIO LAS VARIABLES PARA LA EXPORTACION
			mostrarArchivoCSV = false;

			String csvSepCamp = this.getUsuario().getCsvSeparadorCampo();
			String csvSepDec = this.getUsuario().getCsvSeparadorDecimales();

			StringBuilder sb = new StringBuilder();

			if (pmcl == null) {
				FacesContext.getCurrentInstance().addMessage(null,
						new FacesMessage(FacesMessage.SEVERITY_ERROR, "La consulta devolvio null", null));
			} else {
				if (pmcl.getError().getValue().getHayError().getValue()) {
					FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
							pmcl.getError().getValue().getMsgError().getValue(), null));
				} else {
					if (pmcl.getRegistros() != null && pmcl.getRegistros().getValue() != null
							&& pmcl.getRegistros().getValue().getPlatMovimientoCuenta() != null) {
						for (PlatMovimientoCuenta pmc : pmcl.getRegistros().getValue().getPlatMovimientoCuenta()) {
							Operacion o = new Operacion();
							o.setFechaMovimiento(pmc.getFechaMovimiento().toGregorianCalendar().getTime());
							o.setTipoMovimientoDescripcion(
									pmc.getTipoMovimiento().getValue().getDescripcion().getValue());
							if (pmc.getFechaAcreditacion() != null) {
								o.setFechaAcreditacion(pmc.getFechaAcreditacion().toGregorianCalendar().getTime());
							}
							o.setConcepto(pmc.getConcepto().getValue());
							o.setImpCredito(pmc.getImpCredito().getValue());
							o.setImpDebito(pmc.getImpDebito().getValue());
							o.setImporte(pmc.getImporte().getValue());
							// o.setSaldo(pmc.getSaldo().getValue());
							o.setIdModuloOrigen(pmc.getModuloOrigen().getValue().getIdModulo().getValue());
							o.setCodMnemonicoModOrigen(pmc.getModuloOrigen().getValue().getCodigoModulo().getValue());
							// o.setIdOrigen(pmc.getIdOrigen().getValue());
							// o.setIdModuloOrigen(pmc.getModuloOrigen().getValue().getIdModulo().getValue());
							o.setIdOrigen(pmc.getIdOrigen().getValue());
							o.setOrden(pmc.getNroOrden().getValue());
							// o.setAdmiteDetalleOrigen(pmc.getAdmiteDetalleOrigen().getValue());
							o.setIdClasificacion(
									pmc.getClasificacion().getValue().getIdClasificacionMovimientoCuenta().getValue());
							o.setCodMnemonicoClasificacion(
									pmc.getClasificacion().getValue().getCodMnemonico().getValue());
							o.setDescripcionClasificacion(
									pmc.getClasificacion().getValue().getDescripcion().getValue());
							list.add(o);
						}

						if (list == null) {
							FacesContext.getCurrentInstance().addMessage(null,
									new FacesMessage(FacesMessage.SEVERITY_ERROR,
											"No existe informacion para la consulta realizada.", null));
						} else {
							if (list.isEmpty()) {
								FacesContext.getCurrentInstance().addMessage(null,
										new FacesMessage(FacesMessage.SEVERITY_ERROR,
												"No existe informacion para la consulta realizada.", null));
							} else {
								// CREO HEADER DE EXPORTACION SI ES NECESARIO PARA EXPORTAR A CSV
								if (this.exportToExcel) {
									// Header

									sb.append((char) 34).append("Fecha").append((char) 34).append(csvSepCamp);
									sb.append((char) 34).append("Ultimos Movimientos").append((char) 34)
											.append(csvSepCamp);
									if (UsuarioHelper.usuarioSession().getSuperPagoInstance().isMostrarDebeHaber()) {
										sb.append((char) 34).append("Acreditaciones").append((char) 34)
												.append(csvSepCamp);
										sb.append((char) 34).append("Debitos").append((char) 34).append(csvSepCamp);
									} else {
										sb.append((char) 34).append("Importe").append((char) 34).append(csvSepCamp);
									}

									sb.append((char) 13).append((char) 10);

									for (Operacion it : list) {
										// CREO ARCHIVO DE EXPORTACION SI ES NECESARIO PARA EXPORTAR A CSV

										if (cantRegistros == 0) {
											mostrarArchivoCSV = true;
										}

										if (UsuarioHelper.usuarioSession().getSuperPagoInstance()
												.getOperacionesPendientes()) {
											// OPERACIONES PENDIENTES

											sb.append((char) 34)
													.append(it.getFechaMovimiento() == null ? ""
															: it.getFechaMovimiento())
													.append((char) 34).append(csvSepCamp);
										} else {
											// OPERACIONES ACREDITADAS

											sb.append((char) 34)
													.append(it.getFechaAcreditacion() == null ? ""
															: it.getFechaAcreditacion())
													.append((char) 34).append(csvSepCamp);
										}

										sb.append((char) 34).append(it.getConcepto()).append((char) 34)
												.append(csvSepCamp);
										if (UsuarioHelper.usuarioSession().getSuperPagoInstance()
												.isMostrarDebeHaber()) {
											sb.append((char) 34)
													.append(it.getImpCredito().toString().replace(".", csvSepDec)
															.replace(",", csvSepDec))
													.append((char) 34).append(csvSepCamp);
											sb.append((char) 34)
													.append(it.getImpDebito().toString().replace(".", csvSepDec)
															.replace(",", csvSepDec))
													.append((char) 34).append(csvSepCamp);
										} else {
											sb.append((char) 34)
													.append(it.getImporte().toString().replace(".", csvSepDec)
															.replace(",", csvSepDec))
													.append((char) 34).append(csvSepCamp);
										}
										sb.append((char) 13).append((char) 10);

										// Cantidad total de Registros devueltos
										cantRegistros += 1;
									}

									// GENERO ARCHIVO DE EXPORTACION SI ES NECESARIO PARA EXPORTAR A CSV
									FacesContext fc = FacesContext.getCurrentInstance();
									ExternalContext ec = fc.getExternalContext();

									ec.responseReset();
									ec.setResponseContentType("text/plain");
									ec.setResponseContentLength(sb.toString().length());
									SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd_HHmmss");

									if (UsuarioHelper.usuarioSession().getSuperPagoInstance().isMostrarDebeHaber()) {
										ec.setResponseHeader("Content-Disposition",
												"attachment; filename=\"" + sdf.format(new Date()) + "_("
														+ this.getUsuario().getIdMayorista() + ")_"
														+ "Super Pago Movimientos.csv" + "\"");
									} else {
										ec.setResponseHeader("Content-Disposition",
												"attachment; filename=\"" + sdf.format(new Date()) + "_("
														+ this.getUsuario().getIdMayorista() + ")_"
														+ "Super Pago Movimientos Pendientes.csv" + "\"");
									}

									OutputStream os = ec.getResponseOutputStream();
									OutputStreamWriter osw = new OutputStreamWriter(os);
									PrintWriter writer = new PrintWriter(osw);
									writer.write(sb.toString());
									writer.flush();
									writer.close();
									sb.setLength(0);

									fc.responseComplete();

									PrimeFaces.current().executeScript("PF('panelFiltroWG').toggle();");
								}
							}
						}
					}
				}

				/*
				 * if (UsuarioHelper.usuarioSession().getSuperPagoInstance().
				 * getOperacionesPendientes()) { // OPERACIONES PENDIENTES
				 * 
				 * } else { // OPERACIONES ACREDITADAS createCartesianLinerModel(); }
				 */

			}

		} catch (Exception e) {
			LogACGHelper.escribirLog(null,
					"Informe Super Pago Operaciones. Excepcion ejecutando el WS de consulta: |" + e.getMessage() + "|");
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
					"Error ejecutando el WS de consulta de Transacciones: |" + e.getMessage() + "|", null));
			FacesContext.getCurrentInstance().validationFailed();
			this.list = null;
		}
		PrimeFaces.current().executeScript("PF('blockPanelGral').hide()");
		return;
	}

	public void verOperacion(Operacion operacion) {

		OperacionDetalle od = new OperacionDetalle();

		try {
			if ("MOVSYSORI".equals(operacion.getCodMnemonicoClasificacion())) {
				if (operacion.getDetalle() == null) {
					if ("GPE".equals(operacion.getCodMnemonicoModOrigen())) {
						PagoElectronicoHelper.getInstance();
						Integer estado = PagoElectronicoHelper.ejecutarFuncionGCOM();

						if (estado.compareTo(1) != 0) {
							LogACGHelper.escribirLog(null, "Inicializar Pago Electronico. Comercio deshabilitado");
							FacesContext.getCurrentInstance().addMessage(null,
									new FacesMessage(FacesMessage.SEVERITY_ERROR,
											"El comercio no se encuentra habilitado para operar.", null));
							return;
						}

						com.americacg.cargavirtual.gateway.pagoElectronico.model.MensajeOutboundGateway mo = null;

						mo = PagoElectronicoHelper.ejecutarFuncionCOPA("ID_ORIGEN",
								String.valueOf(operacion.getIdOrigen()), 1, 1, 1);

						if (mo != null) {
							if (mo.getHeaderOut() != null && mo.getHeaderOut().getCodigoRetorno() != null) {
								LogACGHelper.escribirLog(null, "Inicializar Pago Electronico. Error: |"
										+ mo.getHeaderOut().getMensajeRetorno() + "|");
								FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(
										FacesMessage.SEVERITY_ERROR,
										"Inicializar Pago Electronico. " + mo.getHeaderOut().getMensajeRetorno(),
										null));
								return;
							} else {

								DataOutputFcnCOPA doCOPA = (DataOutputFcnCOPA) mo.getDataOutputFcn();
								if (doCOPA != null) {
									if (doCOPA.getPago() != null) {
										if (doCOPA.getPago().getConceptoDePago() != null) {
											od.setConceptoDePago(doCOPA.getPago().getConceptoDePago().getDescripcion());
										}
										od.setNroComprobante(doCOPA.getPago().getIdPagoOperador().toString());
										od.setFecha(doCOPA.getPago().getFechaDePago());
										if (doCOPA.getPago().getMetodoDePago() != null) {

											if ("C".equals(doCOPA.getPago().getMetodoDePago().getTipoMedioDePago())) {
												od.setMetodoDePago(doCOPA.getPago().getMetodoDePago().getMarca() + " "
														+ "Credito");
											} else if ("D"
													.equals(doCOPA.getPago().getMetodoDePago().getTipoMedioDePago())) {
												od.setMetodoDePago(
														doCOPA.getPago().getMetodoDePago().getMarca() + " " + "Dedito");
											} else {
												od.setMetodoDePago(doCOPA.getPago().getMetodoDePago().getMarca());
											}

											if (od.getNumeroTarjeta() != null) {
												od.setMetodoDePago(od.getMetodoDePago() + " " + od.getNumeroTarjeta());
											}
										}
										if (doCOPA.getPago().getEstadoOperador() != null) {
											od.setDescripcionOperador(
													doCOPA.getPago().getEstadoOperador().getDescripcionLargaACG());
										}
										try {
											od.setImporte(doCOPA.getPago().getImporteBase().floatValue());
										} catch (Exception e) {
											// TODO: handle exception
										}

										od.setCuotas(doCOPA.getPago().getCantidadCuotas());
										if (doCOPA.getPago().getEstado() != null) {
											od.setEstado(doCOPA.getPago().getEstado().getDescripcion());
										}
										od.setNumeroTarjeta(doCOPA.getPago().getTokenOperador().getNumeroTarjeta());
										od.setEmailCliente(doCOPA.getPago().getEmailCliente());
										od.setTelefonoCliente(doCOPA.getPago().getTelefonoCliente());
										od.setCodigoOperador(doCOPA.getPago().getEstadoOperador().getCodigoOperador());
										od.setDescripcionOperador(
												doCOPA.getPago().getEstadoOperador().getDescripcionLargaACG());

										/*
										 * 
										 * try {
										 * this.pago.setCantidadCuotas(Integer.parseInt(campo[this.getCampoAParsear()]))
										 * ; } catch (Exception e) { // TODO: handle exception }
										 * 
										 * this.pago.setConceptoDePago(new ConceptoDePago());
										 * 
										 * try {
										 * this.pago.getConceptoDePago().setDescripcion(campo[this.getCampoAParsear()]);
										 * } catch (Exception e) { // TODO: handle exception }
										 * 
										 * this.pago.setTokenOperador(new TokenOperador());
										 * 
										 * try {
										 * this.pago.getTokenOperador().setNumeroTarjeta(campo[this.getCampoAParsear()])
										 * ; } catch (Exception e) { // TODO: handle exception }
										 * 
										 * this.pago.setEmailCliente(campo[this.getCampoAParsear()]);
										 * this.pago.setTelefonoCliente(campo[this.getCampoAParsear()]);
										 * 
										 * this.pago.setEstadoOperador(new EstadoOperador());
										 * this.pago.getEstadoOperador().setCodigoOperador(campo[this.getCampoAParsear()
										 * ]); this.pago.getEstadoOperador().setDescripcionLargaACG(campo[this.
										 * getCampoAParsear()]);
										 * 
										 */

										this.list.get(this.list.lastIndexOf(operacion)).setDetalle(od);
									}
								}
							}
						} else {
							// respuestaPE = "No se obtuvo una respuesta valida desde el Gateway";
							LogACGHelper.escribirLog(null,
									"Inicializar Pago Electronico. Error: |No se obtuvo una respuesta valida desde el Gateway|");
							FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(
									FacesMessage.SEVERITY_ERROR,
									"Inicializar Pago Electronico. No se obtuvo una respuesta valida desde el Gateway",
									null));
							return;
						}
					}
				}
			} else {
				LogACGHelper.escribirLog(null, "Pago Electronico: |El movimiento no admite informacion detallada.|");
				FacesContext.getCurrentInstance().addMessage(null,
						new FacesMessage(FacesMessage.SEVERITY_INFO, "El movimiento no admite detalle", null));
				return;
			}
		} catch (HandledException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		PrimeFaces.current().ajax().update("consultaOperacionDialog");
		PrimeFaces.current().executeScript("PF('consultaOperacionDialogWV').show()");

	}

	/*
	 * public LineChartModel getCartesianLinerModel() { return cartesianLinerModel;
	 * }
	 * 
	 * public void setCartesianLinerModel(LineChartModel cartesianLinerModel) {
	 * this.cartesianLinerModel = cartesianLinerModel; }
	 */


	private Cuenta obtenerCuentaSuperPago(Cuenta cuenta) throws Exception {
		PlatCuenta oPlatCta = null;
		GregorianCalendar gcFechaAux = null;
		XMLGregorianCalendar xmlGCFechaAux = null;

		gcFechaAux = new GregorianCalendar();
		gcFechaAux.setTime(new Date());

		xmlGCFechaAux = DatatypeFactory.newInstance().newXMLGregorianCalendar(gcFechaAux);

		oPlatCta = new PlatCuenta();
		oPlatCta.setIdCuenta(oGestionObjFac.createPlatCuentaIdCuenta(cuenta.getIdCuenta()));
		oPlatCta.setNumeroCuenta(oGestionObjFac.createPlatCuentaNumeroCuenta(cuenta.getNumeroCuenta()));
		oPlatCta = GestionServiceHelper.getGestionService(CfgTimeout.DEFAULT).obtenerPlatCuenta(oPlatCta,
				xmlGCFechaAux);

		if (oPlatCta != null) {
			cuenta.setAlias(oPlatCta.getAlias().getValue());
			cuenta.setCVU(oPlatCta.getCVU().getValue());
			cuenta.setFechaAlta(oPlatCta.getFechaAlta().toGregorianCalendar().getTime());
			cuenta.setIdCliente(oPlatCta.getCliente().getValue().getIdCliente().getValue());
			cuenta.setIdCuenta(oPlatCta.getIdCuenta().getValue());
			cuenta.setIdMayorista(oPlatCta.getIdMayorista().getValue());
			cuenta.setIdMoneda(oPlatCta.getMoneda().getValue().getIdMoneda().getValue());
			cuenta.setDescripcionMoneda(oPlatCta.getMoneda().getValue().getDescripcion().getValue());
			cuenta.setSimboloMoneda(oPlatCta.getMoneda().getValue().getTxtSimbolo().getValue());
			cuenta.setIdProducto((oPlatCta != null && oPlatCta.getProducto() != null
					&& oPlatCta.getProducto().getValue().getIdProducto() != null
							? oPlatCta.getProducto().getValue().getIdProducto().getValue()
							: null));
			cuenta.setNumeroCuenta(oPlatCta.getNumeroCuenta().getValue());
			cuenta.setProductoDescripcion((oPlatCta != null && oPlatCta.getProducto() != null
					&& oPlatCta.getProducto().getValue().getDescProducto() != null
							? oPlatCta.getProducto().getValue().getDescProducto().getValue()
							: null));
			cuenta.setSaldo(oPlatCta.getSaldo().getValue());
			cuenta.setSaldoPendiente(oPlatCta.getSaldoPendiente().getValue());
			cuenta.setIdCuenta(oPlatCta.getIdCuenta().getValue());
		} else
			cuenta = null;

		return cuenta;
	}

}
