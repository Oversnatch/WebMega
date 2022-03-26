package com.americacg.cargavirtual.web.mngbeans.reportes;

import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.List;
import javax.faces.application.FacesMessage;
import javax.inject.Named;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import javax.xml.ws.WebServiceException;

import org.primefaces.PrimeFaces;
import org.springframework.context.annotation.Scope;

import com.americacg.cargavirtual.gestion.model.InformePolizaSeguro;
import com.americacg.cargavirtual.gestion.model.PolizaSeguro;
import com.americacg.cargavirtual.web.enums.CfgTimeout;
import com.americacg.cargavirtual.web.helpers.ACGFormatHelper;
import com.americacg.cargavirtual.web.helpers.GestionServiceHelper;
import com.americacg.cargavirtual.web.helpers.LogACGHelper;
import com.americacg.cargavirtual.web.model.Error;

import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;

@Named("reporteSegurosView")
@Scope(org.springframework.web.context.WebApplicationContext.SCOPE_SESSION)
public class ReporteSegurosView extends ReporteGeneral<PolizaSeguro> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7339964668975337956L;

	private Long idPolizaSeguro;

	private Long idClienteInferior;
	private Integer tipoFiltroCliente;
	private Float importeTotal;

	@Override
	public Error resetearReporte() {
		Error e = super.resetearReporte();
		this.idPolizaSeguro = null;
		this.tipoFiltroCliente = 3;
		this.idClienteInferior = null;
		this.importeTotal = 0F;
		return e;
	}

	public ReporteSegurosView() {
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

		try {

			this.list = null;
			this.mostrarRegistros = true;

			importeTotal = 0F;

			// Fecha Desde
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(fechaHoraDesde);
			calendar.set(Calendar.SECOND, 0);
			calendar.set(Calendar.MILLISECOND, 0);
			fechaHoraDesde = calendar.getTime();

			// Asigno 59 segundos a la fechaHoraHasta
			// Fecha Hasta
			calendar = Calendar.getInstance();
			calendar.setTime(fechaHoraHasta);
			calendar.set(Calendar.SECOND, 59);
			calendar.set(Calendar.MILLISECOND, 999);
			fechaHoraHasta = calendar.getTime();

			// Limpio la tabla
			list = null;

			if (this.getUsuario().getTipoCliente() != null) {
				if ("P".equals(this.getUsuario().getTipoCliente())) {
					// Para el caso del Punto de venta donde no tengo filtro de cliente
					// System.out.println("Inicializo tipoFiltroCliente porque estoy en punto de
					// venta");
					tipoFiltroCliente = 1;
					idClienteInferior = this.getUsuario().getIdCliente();
				}
			} else {
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
						"El tipo de Cliente no es valido para la consulta", null));
				return;
			}

			// TODO validar filtros...
			Boolean filtroOK = false;
			if (tipoFiltroCliente == 1) { // Solo cliente
				if (idClienteInferior != null) {
					filtroOK = true;
				} else {
					FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
							"El idCliente en el filtro no puede ser vacio", null));
				}

			} else {
				if (tipoFiltroCliente == 2) { // Todos los subclientes de
					if (idClienteInferior != null) {
						filtroOK = true;
					} else {
						FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
								"El idCliente en el filtro no puede ser vacio", null));
					}
				} else {
					if (tipoFiltroCliente == 3) { // Todos los clientes que pertenecen al cliente logueado
						if (idClienteInferior == null) {
							filtroOK = true;
						} else {
							FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(
									FacesMessage.SEVERITY_ERROR, "El idCliente en el filtro debe ser vacio", null));
						}
					} else {
						// tipo de Filtro Incorrecto
						FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
								"El tipo de filtro seleccionado es incorrecto", null));
					}
				}
			}

			if (filtroOK) {

				GregorianCalendar gcFechaHoraDesde = new GregorianCalendar();
				gcFechaHoraDesde.setTime(fechaHoraDesde);
				XMLGregorianCalendar xmlGCFechaHoraDesde = DatatypeFactory.newInstance()
						.newXMLGregorianCalendar(gcFechaHoraDesde);

				GregorianCalendar gcFechaHoraHasta = new GregorianCalendar();
				gcFechaHoraHasta.setTime(fechaHoraHasta);
				XMLGregorianCalendar xmlGCFechaHoraHasta = DatatypeFactory.newInstance()
						.newXMLGregorianCalendar(gcFechaHoraHasta);

				InformePolizaSeguro is = GestionServiceHelper.getGestionService(CfgTimeout.REPORTE).informePolizaSeguro(
						this.getUsuario().getIdMayorista(), this.getUsuario().getIdCliente(),
						this.getUsuario().getIdUsuario(), this.getUsuario().getPassword(), idClienteInferior,
						xmlGCFechaHoraDesde, xmlGCFechaHoraHasta, null, null, null, null, null, null, null, null, null,
						null, idPolizaSeguro, tipoFiltroCliente);

				if (is == null) {
					FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
							"La respuesta del informe de CargasAutomaticas es null", null));
				} else if (is.getError().getValue().getHayError().getValue()) {
					FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
							is.getError().getValue().getMsgError().getValue(), null));
				} else {

					List<PolizaSeguro> listaux = is.getLps().getValue().getPolizaSeguro();
					if (listaux.isEmpty()) {
						FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
								"No existe información para la consulta realizada.", null));
					} else {
						for (Iterator<PolizaSeguro> iterator = listaux.iterator(); iterator.hasNext();) {
							PolizaSeguro polizaSeguro = (PolizaSeguro) iterator.next();
							importeTotal = importeTotal + polizaSeguro.getRespImporte().getValue();
						}

						list = listaux;
						cantRegistros = list.size();

						// CREO HEADER DE EXPORTACION SI ES NECESARIO PARA EXPORTAR A CSV
						if (this.exportToExcel) {

							// GENERO Y LIMPIO LAS VARIABLES PARA LA EXPORTACION
							mostrarArchivoCSV = false;

							String csvSepCamp = this.getUsuario().getCsvSeparadorCampo();
							String csvSepDec = this.getUsuario().getCsvSeparadorDecimales();

							StringBuilder sb = new StringBuilder();

							sb.append((char) 34).append("Informe de Seguros").append((char) 34);
							sb.append((char) 13).append((char) 10);

							sb.append((char) 34).append("Id Poliza").append((char) 34).append(csvSepCamp);
							sb.append((char) 34).append("Fecha Alta").append((char) 34).append(csvSepCamp);
							sb.append((char) 34).append("ID Cliente").append((char) 34).append(csvSepCamp);
							sb.append((char) 34).append("Cliente").append((char) 34).append(csvSepCamp);
							sb.append((char) 34).append("ID Usuario").append((char) 34).append(csvSepCamp);
							sb.append((char) 34).append("Usuario").append((char) 34).append(csvSepCamp);
							sb.append((char) 34).append("Apellido").append((char) 34).append(csvSepCamp);
							sb.append((char) 34).append("Nombre").append((char) 34).append(csvSepCamp);
							sb.append((char) 34).append("DNI/CUIT").append((char) 34).append(csvSepCamp);
							sb.append((char) 34).append("Calle").append((char) 34).append(csvSepCamp);
							sb.append((char) 34).append("Numero").append((char) 34).append(csvSepCamp);
							sb.append((char) 34).append("Piso").append((char) 34).append(csvSepCamp);
							sb.append((char) 34).append("Depto").append((char) 34).append(csvSepCamp);

							sb.append((char) 34).append("ID Pais").append((char) 34).append(csvSepCamp);
							sb.append((char) 34).append("Pais").append((char) 34).append(csvSepCamp);
							sb.append((char) 34).append("ID Provincia").append((char) 34).append(csvSepCamp);
							sb.append((char) 34).append("Provincia").append((char) 34).append(csvSepCamp);
							sb.append((char) 34).append("ID Localidad").append((char) 34).append(csvSepCamp);
							sb.append((char) 34).append("Localidad").append((char) 34).append(csvSepCamp);
							sb.append((char) 34).append("Telefono").append((char) 34).append(csvSepCamp);
							sb.append((char) 34).append("Celular").append((char) 34).append(csvSepCamp);
							sb.append((char) 34).append("Mail").append((char) 34).append(csvSepCamp);
							sb.append((char) 34).append("ID Producto").append((char) 34).append(csvSepCamp);
							sb.append((char) 34).append("Producto").append((char) 34).append(csvSepCamp);
							sb.append((char) 34).append("Marca").append((char) 34).append(csvSepCamp);
							sb.append((char) 34).append("Modelo").append((char) 34).append(csvSepCamp);
							sb.append((char) 34).append("Nro Linea").append((char) 34).append(csvSepCamp);
							sb.append((char) 34).append("ID Prestataria").append((char) 34).append(csvSepCamp);
							sb.append((char) 34).append("Prestataria").append((char) 34).append(csvSepCamp);
							sb.append((char) 34).append("ID Cobertura").append((char) 34).append(csvSepCamp);
							sb.append((char) 34).append("Cobertura").append((char) 34).append(csvSepCamp);
							sb.append((char) 34).append("Importe").append((char) 34).append(csvSepCamp);
							sb.append((char) 34).append("ID Forma de Pago").append((char) 34).append(csvSepCamp);
							sb.append((char) 34).append("Forma de Pago").append((char) 34).append(csvSepCamp);
							sb.append((char) 34).append("CBU Debito").append((char) 34).append(csvSepCamp);
							sb.append((char) 34).append("Nro. Tarjeta Credito").append((char) 34).append(csvSepCamp);
							sb.append((char) 34).append("Fecha Venc. Tarjeta Credito").append((char) 34)
									.append(csvSepCamp);
							sb.append((char) 34).append("Clave Seg. Tarjeta Credito").append((char) 34)
									.append(csvSepCamp);
							sb.append((char) 34).append("ID Tipo de Tarjeta").append((char) 34).append(csvSepCamp);
							sb.append((char) 34).append("Tipo de Tarjeta").append((char) 34).append(csvSepCamp);
							sb.append((char) 34).append("ID Banco").append((char) 34).append(csvSepCamp);
							sb.append((char) 34).append("Banco").append((char) 34).append(csvSepCamp);

							sb.append((char) 13).append((char) 10);

							// DEFINO FORMATO DE FECHA PARA MOSTRAR EN EL REPORTE QUE SE EXPORTA
							SimpleDateFormat ff = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

							// RECORRO LA LISTA QUE HACER LAS SUMATORIAS Y PARA GENERAR EL REPORTE A
							// EXPORTAR EN CASO DE SER NECESARIO
							if (list != null) {
								for (PolizaSeguro ic : list) {

									sb.append((char) 34).append(ic.getIdPolizaSeguro().getValue()).append((char) 34)
											.append(csvSepCamp);
									sb.append((char) 34).append(
											ff.format(ic.getFechaAlta().toGregorianCalendar().getTime()).toString())
											.append((char) 34).append(csvSepCamp);
									sb.append((char) 34).append(ic.getIdCliente().getValue()).append((char) 34)
											.append(csvSepCamp);
									sb.append((char) 34).append(ic.getRespRazonSocialClienteAlta().getValue())
											.append((char) 34).append(csvSepCamp);
									sb.append((char) 34).append(ic.getIdUsuAlta().getValue()).append((char) 34)
											.append(csvSepCamp);
									sb.append((char) 34).append(ic.getRespUsuarioAlta().getValue()).append((char) 34)
											.append(csvSepCamp);
									sb.append((char) 34).append(ic.getAsegApellido().getValue()).append((char) 34)
											.append(csvSepCamp);
									sb.append((char) 34).append(ic.getAsegNombre().getValue()).append((char) 34)
											.append(csvSepCamp);
									sb.append((char) 34).append(ic.getAsegDNICUIT().getValue()).append((char) 34)
											.append(csvSepCamp);
									sb.append((char) 34).append(ic.getAsegCalle().getValue()).append((char) 34)
											.append(csvSepCamp);
									sb.append((char) 34).append(ic.getAsegNumero().getValue()).append((char) 34)
											.append(csvSepCamp);
									sb.append((char) 34).append(ic.getAsegPiso().getValue()).append((char) 34)
											.append(csvSepCamp);
									sb.append((char) 34).append(ic.getAsegDepto().getValue()).append((char) 34)
											.append(csvSepCamp);

									sb.append((char) 34).append(ic.getAsegIdPais().getValue()).append((char) 34)
											.append(csvSepCamp);
									sb.append((char) 34).append(ic.getRespDescPais().getValue()).append((char) 34)
											.append(csvSepCamp);
									sb.append((char) 34).append(ic.getAsegIdProvincia().getValue()).append((char) 34)
											.append(csvSepCamp);
									sb.append((char) 34).append(ic.getRespDescProvincia().getValue()).append((char) 34)
											.append(csvSepCamp);
									sb.append((char) 34).append(ic.getAsegIdLocalidad().getValue()).append((char) 34)
											.append(csvSepCamp);
									sb.append((char) 34).append(ic.getRespDescLocalidad().getValue()).append((char) 34)
											.append(csvSepCamp);
									sb.append((char) 34).append(ic.getAsegTelFijo().getValue()).append((char) 34)
											.append(csvSepCamp);
									sb.append((char) 34).append(ic.getAsegCelular().getValue()).append((char) 34)
											.append(csvSepCamp);
									sb.append((char) 34).append(ic.getAsegMail().getValue()).append((char) 34)
											.append(csvSepCamp);
									sb.append((char) 34).append(ic.getIdProducto().getValue()).append((char) 34)
											.append(csvSepCamp);
									sb.append((char) 34).append(ic.getRespDescProducto().getValue()).append((char) 34)
											.append(csvSepCamp);
									sb.append((char) 34).append(ic.getMarca().getValue()).append((char) 34)
											.append(csvSepCamp);
									sb.append((char) 34).append(ic.getModelo().getValue()).append((char) 34)
											.append(csvSepCamp);
									sb.append((char) 34).append(ic.getNumeroLinea().getValue()).append((char) 34)
											.append(csvSepCamp);
									sb.append((char) 34).append(ic.getIdEmpresaPrestataria().getValue())
											.append((char) 34).append(csvSepCamp);
									sb.append((char) 34).append(ic.getRespDescEmpresaPrest().getValue())
											.append((char) 34).append(csvSepCamp);
									sb.append((char) 34).append(ic.getIdCobertura().getValue()).append((char) 34)
											.append(csvSepCamp);
									sb.append((char) 34).append(ic.getRespDescCobertura().getValue()).append((char) 34)
											.append(csvSepCamp);
									sb.append((char) 34).append("$")
									.append(ACGFormatHelper.format(ic.getRespImporte().getValue(), csvSepDec))
											.append((char) 34).append(csvSepCamp);
									sb.append((char) 34).append(ic.getIdFormaPago().getValue()).append((char) 34)
											.append(csvSepCamp);
									sb.append((char) 34).append(ic.getRespDescFormaPago().getValue()).append((char) 34)
											.append(csvSepCamp);
									sb.append((char) 34).append(ic.getCbuDebitoBancario().getValue()).append((char) 34)
											.append(csvSepCamp);
									sb.append((char) 34).append(ic.getTarjetaCredNumero().getValue()).append((char) 34)
											.append(csvSepCamp);
									sb.append((char) 34).append(ic.getTarjetaCredVto().getValue()).append((char) 34)
											.append(csvSepCamp);
									sb.append((char) 34).append(ic.getTarjetaCredCodSeg().getValue()).append((char) 34)
											.append(csvSepCamp);
									sb.append((char) 34).append(ic.getIdTipoTarjeta().getValue()).append((char) 34)
											.append(csvSepCamp);
									sb.append((char) 34).append(ic.getRespDescTipoTarjeta().getValue())
											.append((char) 34).append(csvSepCamp);
									sb.append((char) 34).append(ic.getIdBanco().getValue()).append((char) 34)
											.append(csvSepCamp);
									sb.append((char) 34).append(ic.getRespDescBanco().getValue()).append((char) 34)
											.append(csvSepCamp);
									sb.append((char) 13).append((char) 10);
								}
							}

							sb.append((char) 34).append("-").append((char) 34).append(csvSepCamp);
							sb.append((char) 34).append("-").append((char) 34).append(csvSepCamp);
							sb.append((char) 34).append("-").append((char) 34).append(csvSepCamp);
							sb.append((char) 34).append("-").append((char) 34).append(csvSepCamp);
							sb.append((char) 34).append("-").append((char) 34).append(csvSepCamp);
							sb.append((char) 34).append("-").append((char) 34).append(csvSepCamp);
							sb.append((char) 34).append("-").append((char) 34).append(csvSepCamp);
							sb.append((char) 34).append("-").append((char) 34).append(csvSepCamp);
							sb.append((char) 34).append("-").append((char) 34).append(csvSepCamp);
							sb.append((char) 34).append("-").append((char) 34).append(csvSepCamp);
							sb.append((char) 34).append("-").append((char) 34).append(csvSepCamp);
							sb.append((char) 34).append("-").append((char) 34).append(csvSepCamp);
							sb.append((char) 34).append("-").append((char) 34).append(csvSepCamp);

							sb.append((char) 34).append("-").append((char) 34).append(csvSepCamp);
							sb.append((char) 34).append("-").append((char) 34).append(csvSepCamp);
							sb.append((char) 34).append("-").append((char) 34).append(csvSepCamp);
							sb.append((char) 34).append("-").append((char) 34).append(csvSepCamp);
							sb.append((char) 34).append("-").append((char) 34).append(csvSepCamp);
							sb.append((char) 34).append("-").append((char) 34).append(csvSepCamp);
							sb.append((char) 34).append("-").append((char) 34).append(csvSepCamp);
							sb.append((char) 34).append("-").append((char) 34).append(csvSepCamp);
							sb.append((char) 34).append("-").append((char) 34).append(csvSepCamp);
							sb.append((char) 34).append("-").append((char) 34).append(csvSepCamp);
							sb.append((char) 34).append("-").append((char) 34).append(csvSepCamp);
							sb.append((char) 34).append("-").append((char) 34).append(csvSepCamp);
							sb.append((char) 34).append("-").append((char) 34).append(csvSepCamp);
							sb.append((char) 34).append("-").append((char) 34).append(csvSepCamp);
							sb.append((char) 34).append("-").append((char) 34).append(csvSepCamp);
							sb.append((char) 34).append("-").append((char) 34).append(csvSepCamp);
							sb.append((char) 34).append("-").append((char) 34).append(csvSepCamp);
							sb.append((char) 34).append("-").append((char) 34).append(csvSepCamp);
							sb.append((char) 34).append("$")
							.append(ACGFormatHelper.format(importeTotal, csvSepDec)).append((char) 34).append(csvSepCamp);
							sb.append((char) 34).append("-").append((char) 34).append(csvSepCamp);
							sb.append((char) 34).append("-").append((char) 34).append(csvSepCamp);
							sb.append((char) 34).append("-").append((char) 34).append(csvSepCamp);
							sb.append((char) 34).append("-").append((char) 34).append(csvSepCamp);
							sb.append((char) 34).append("-").append((char) 34).append(csvSepCamp);
							sb.append((char) 34).append("-").append((char) 34).append(csvSepCamp);
							sb.append((char) 34).append("-").append((char) 34).append(csvSepCamp);
							sb.append((char) 34).append("-").append((char) 34).append(csvSepCamp);
							sb.append((char) 34).append("-").append((char) 34).append(csvSepCamp);
							sb.append((char) 34).append("-").append((char) 34).append(csvSepCamp);

							sb.append((char) 13).append((char) 10);

							FacesContext fc = FacesContext.getCurrentInstance();
							ExternalContext ec = fc.getExternalContext();

							ec.responseReset();
							ec.setResponseContentType("text/plain");
							ec.setResponseContentLength(sb.toString().length());
							SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd_HHmmss");
							ec.setResponseHeader("Content-Disposition",
									"attachment; filename=\"" + sdf.format(new Date()) + "_("
											+ this.getUsuario().getIdMayorista() + ")_" + "InformeSeguros.csv" + "\"");

							OutputStream os = ec.getResponseOutputStream();
							OutputStreamWriter osw = new OutputStreamWriter(os);
							PrintWriter writer = new PrintWriter(osw);
							writer.write(sb.toString());
							writer.flush();
							writer.close();
							sb.setLength(0);

							fc.responseComplete();

						}

						PrimeFaces.current().executeScript("PF('panelFiltroWG').toggle();");
					}
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
					LogACGHelper.escribirLog(null, "Informe Seguros. Error ejecutando el WS de consulta: |" + ste.getMessage() + "|");
					FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error ejecutando el WS de consulta: |" + ste.getMessage() + "|", null));
				}
			}else {
				LogACGHelper.escribirLog(null, "Informe Seguros. Error ejecutando el WS de consulta: |" + ste.getMessage() + "|");				
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error ejecutando el WS de consulta: |" + ste.getMessage() + "|", null));
			}
			FacesContext.getCurrentInstance().validationFailed();
			this.list = null;
		} catch (Exception e) {
			LogACGHelper.escribirLog(null, "Informe Seguros. Excepcion ejecutando el WS de consulta: |" + e.getMessage() + "|");	
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, 
					"Error ejecutando el WS de consulta de Seguros: |" + e.getMessage() + "|", null));
			FacesContext.getCurrentInstance().validationFailed();
			this.list = null;
		}
		PrimeFaces.current().executeScript("PF('blockPanelGral').hide()");
		return;
	}

	public Long getIdPolizaSeguro() {
		return idPolizaSeguro;
	}

	public void setIdPolizaSeguro(Long idPolizaSeguro) {
		this.idPolizaSeguro = idPolizaSeguro;
	}

	public Long getIdClienteInferior() {
		return idClienteInferior;
	}

	public void setIdClienteInferior(Long idClienteInferior) {
		this.idClienteInferior = idClienteInferior;
	}

	public Integer getTipoFiltroCliente() {
		return tipoFiltroCliente;
	}

	public void setTipoFiltroCliente(Integer tipoFiltroCliente) {
		this.tipoFiltroCliente = tipoFiltroCliente;
	}

	public Float getImporteTotal() {
		return importeTotal;
	}

	public void setImporteTotal(Float importeTotal) {
		this.importeTotal = importeTotal;
	}

}
