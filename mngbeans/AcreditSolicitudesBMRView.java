package com.americacg.cargavirtual.web.mngbeans;

import java.io.Serializable;
import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.inject.Named;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import javax.xml.ws.WebServiceException;

import org.primefaces.PrimeFaces;
import org.springframework.context.annotation.Scope;

import com.americacg.cargavirtual.gestion.model.ArrayOfDatosDepAdel;
import com.americacg.cargavirtual.gestion.model.DatosDepAdel;
import com.americacg.cargavirtual.gestion.model.DatosDepAdelContainer;
import com.americacg.cargavirtual.gestion.model.ObjectFactory;
import com.americacg.cargavirtual.gestion.model.RespDepAdel;
import com.americacg.cargavirtual.gestion.model.RespDepAdelContainer;
import com.americacg.cargavirtual.web.enums.CfgTimeout;
import com.americacg.cargavirtual.web.helpers.GestionServiceHelper;
import com.americacg.cargavirtual.web.helpers.LogACGHelper;
import com.americacg.cargavirtual.web.shared.BasePage;

@Named("acreditSolicitudesBMRView")
@Scope(org.springframework.web.context.WebApplicationContext.SCOPE_SESSION)
public class AcreditSolicitudesBMRView extends BasePage implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 2514118992435229040L;
	private Boolean ignorarFechaAcredSolicBMR = true;
	private Date fechaAcredSolicBMRDesde = new Date();
	private Date fechaAcredSolicBMRHasta = new Date();
	private DatosDepAdelContainer datoAcredBMR;
	private ArrayList<DatosDepAdel> lstSeleccionDepsBMR;
	private Float totalAcredBMR;

	
	public void resetear() {
		this.ignorarFechaAcredSolicBMR = true;
		this.fechaAcredSolicBMRDesde = new Date();
		this.fechaAcredSolicBMRHasta = new Date();
		this.datoAcredBMR = null;
		this.lstSeleccionDepsBMR = new ArrayList<DatosDepAdel>();
		this.totalAcredBMR = 0F;		
	}
	
	public void leerDatosAcredBMR() {
		ArrayList<String> lstUpdt = null;

		try {

			// Limpio la lista
			this.datoAcredBMR = null;
			this.lstSeleccionDepsBMR = new ArrayList<DatosDepAdel>();
			totalAcredBMR = 0F;

			Integer igfe = 3;
			if (ignorarFechaAcredSolicBMR) {
				igfe = 1;
			}

			// idTipoMovimiento 14 (Solicitud de Adelanto BMR)
			// idEstadoDepAdel 1 (Pendiente)

			GregorianCalendar gcFechaHoraDesde = new GregorianCalendar();
			gcFechaHoraDesde.setTime(fechaAcredSolicBMRDesde);
			XMLGregorianCalendar xmlGCFechaHoraDesde = DatatypeFactory.newInstance()
					.newXMLGregorianCalendar(gcFechaHoraDesde);

			GregorianCalendar gcFechaHoraHasta = new GregorianCalendar();
			gcFechaHoraHasta.setTime(fechaAcredSolicBMRHasta);
			XMLGregorianCalendar xmlGCFechaHoraHasta = DatatypeFactory.newInstance()
					.newXMLGregorianCalendar(gcFechaHoraHasta);

			datoAcredBMR = GestionServiceHelper.getGestionService(CfgTimeout.DEFAULT).leerDatosDepAdel(
					this.getUsuario().getIdMayorista(), this.getUsuario().getIdDistribuidor(),
					this.getUsuario().getIdDistribuidor(), this.getUsuario().getNivelDistribuidorSuperior(), 14L, 1L,
					igfe, xmlGCFechaHoraDesde, xmlGCFechaHoraHasta, null, 1, this.getUsuario().getIdCliente(),
					this.getUsuario().getIdDistribuidor(), this.getUsuario().getUsername(), this.getUsuario().getPassword(), null, false, false, 0, false, false);

			if (datoAcredBMR != null) {
				if (datoAcredBMR.getError().getValue().getHayError().getValue()) {
					FacesContext.getCurrentInstance().addMessage(null,
							new FacesMessage(FacesMessage.SEVERITY_ERROR,
									"Error: |" + datoAcredBMR.getError().getValue().getCodigoError().getValue() + ", "
											+ datoAcredBMR.getError().getValue().getMsgError().getValue() + "|",
									null));
				} else {
					if (datoAcredBMR.getListDatosDepAdel().getValue().getDatosDepAdel().isEmpty()) {
						FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
								"No se encontraron Depositos ni Solicitudes de adelanto pendientes", null));
					} else {
						for (DatosDepAdel dda : datoAcredBMR.getListDatosDepAdel().getValue().getDatosDepAdel()) {
							dda.getAccionDepAdel().setValue(0);
							totalAcredBMR = totalAcredBMR + dda.getSaldoPedido().getValue();
						}
					}
				}
			} else {
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
						"El WS de busqueda de Depositos y Adelantos devolvio null en la opcion de Acreditaciones para BMR",
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
							"Error ejecutando el WS de lectura de Depositos y Adelantos Pendientes en la opcion de Acreditaciones para BMR: |"
									+ ste.getMessage() + "|");
					FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
							"Error ejecutando el WS de lectura de Depositos y Adelantos Pendientes en la opcion de Acreditaciones para BMR: |"
									+ ste.getMessage() + "|",
							null));
				}
			} else {
				LogACGHelper.escribirLog(null,
						"Error ejecutando el WS de lectura de Depositos y Adelantos Pendientes en la opcion de Acreditaciones para BMR: |"
								+ ste.getMessage() + "|");
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
						"Error ejecutando el WS de lectura de Depositos y Adelantos Pendientes en la opcion de Acreditaciones para BMR: |"
								+ ste.getMessage() + "|",
						null));
			}
			FacesContext.getCurrentInstance().validationFailed();
		} catch (Exception e) {
			LogACGHelper.escribirLog(null,
					"Error ejecutando el WS de lectura de Depositos y Adelantos Pendientes en la opcion de Acreditaciones para BMR: |"
							+ e.getMessage() + "|");
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
					"Error ejecutando el WS de lectura de Depositos y Adelantos Pendientes en la opcion de Acreditaciones para BMR: |"
							+ e.getMessage() + "|",
					null));
			FacesContext.getCurrentInstance().validationFailed();
		}

		lstUpdt = new ArrayList<String>();
		lstUpdt.add("msgsProductos");
		lstUpdt.add("tablaAcreditaciones");

		PrimeFaces.current().ajax().update(lstUpdt);
	}

	public void aceptarAcreditBMR(ActionEvent ev) {

		try {
			
			ObjectFactory factory = new ObjectFactory();
			ArrayOfDatosDepAdel listRegsAprocesar = factory.createArrayOfDatosDepAdel();
			
			if(lstSeleccionDepsBMR != null){
				for (DatosDepAdel datosDepAdel : lstSeleccionDepsBMR) {
					datosDepAdel.setAccionDepAdel(factory.createDatosDepAdelAccionDepAdel(1));
				}
			}
			
			listRegsAprocesar.getDatosDepAdel().addAll(this.lstSeleccionDepsBMR);

			if (listRegsAprocesar != null && listRegsAprocesar.getDatosDepAdel() != null && listRegsAprocesar.getDatosDepAdel().size() > 0) {

				RespDepAdelContainer res = GestionServiceHelper.getGestionService(CfgTimeout.DEFAULT).aceptarDepAdel(
						this.getUsuario().getIdMayorista(), this.getUsuario().getIdCliente(), this.getUsuario().getUsername(),
						this.getUsuario().getIdUsuario(), this.getUsuario().getPassword(), listRegsAprocesar);

				if (res.getError().getValue().getHayError().getValue()) {
					FacesContext.getCurrentInstance().addMessage(null,
							new FacesMessage(FacesMessage.SEVERITY_ERROR,
									"Error: |" + res.getError().getValue().getCodigoError().getValue() + ", "
											+ res.getError().getValue().getMsgError().getValue(),
									null));
				} else {
					for (RespDepAdel r : res.getListRespDepAdel().getValue().getRespDepAdel()) {

						if (r.getError().getValue().getHayError().getValue()) {
							FacesContext.getCurrentInstance().addMessage(null,
									new FacesMessage(FacesMessage.SEVERITY_ERROR,
											r.getError().getValue().getCodigoError().getValue() + ", "
													+ r.getError().getValue().getMsgError().getValue(),
											null));

							// System.out.println(r.getError().getCodigoError() + ", " +
							// r.getError().getMsgError());

						} else {
							// Accion: --> 0=Ignorar 1=Aceptar 2=Borrar
							FacesContext.getCurrentInstance().addMessage(null,
									new FacesMessage(FacesMessage.SEVERITY_ERROR, r.getDescProceso().getValue(), null));

						}
					}
				}

				// Para limpiar la pantalla luego de hacer las acreditaciones de saldo
				this.datoAcredBMR = null;
				leerDatosAcredBMR();

			} else {
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
						"Debe seleccionar algun registro para Acreditar o Rechazar", null));
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
							"Error ejecutando el WS de Acreditacion de BMR: |" + ste.getMessage() + "|");
					FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
							"Error ejecutando el WS de Acreditacion de BMR: |" + ste.getMessage() + "|", null));
				}
			} else {
				LogACGHelper.escribirLog(null,
						"Error ejecutando el WS de Acreditacion de BMR: |" + ste.getMessage() + "|");
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
						"Error ejecutando el WS de Acreditacion de BMR: |" + ste.getMessage() + "|", null));
			}
			FacesContext.getCurrentInstance().validationFailed();
		} catch (Exception e) {
			LogACGHelper.escribirLog(null, "Error ejecutando el WS de Acreditacion de BMR: |" + e.getMessage() + "|");
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
					"Error ejecutando el WS de Acreditacion de BMR: |" + e.getMessage() + "|", null));
			FacesContext.getCurrentInstance().validationFailed();
		}
		PrimeFaces.current().executeScript("PF('blockPanelGral').hide()");
	}

	public void cancelarAcreditBMR(ActionEvent ev) {

		try {
			this.datoAcredBMR = null;
			this.lstSeleccionDepsBMR = new ArrayList<DatosDepAdel>();			
			leerDatosAcredBMR();
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR, "Cancelacion OK", null));

		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR,
							"Error tratando de leer nuevamente los registros de Acreditaciones de BMR: |"
									+ e.getMessage() + "|",
							null));
		}

	}

	public Boolean getIgnorarFechaAcredSolicBMR() {
		return ignorarFechaAcredSolicBMR;
	}

	public void setIgnorarFechaAcredSolicBMR(Boolean ignorarFechaAcredSolicBMR) {
		this.ignorarFechaAcredSolicBMR = ignorarFechaAcredSolicBMR;
	}

	public Date getFechaAcredSolicBMRDesde() {
		return fechaAcredSolicBMRDesde;
	}

	public void setFechaAcredSolicBMRDesde(Date fechaAcredSolicBMRDesde) {
		this.fechaAcredSolicBMRDesde = fechaAcredSolicBMRDesde;
	}

	public Date getFechaAcredSolicBMRHasta() {
		return fechaAcredSolicBMRHasta;
	}

	public void setFechaAcredSolicBMRHasta(Date fechaAcredSolicBMRHasta) {
		this.fechaAcredSolicBMRHasta = fechaAcredSolicBMRHasta;
	}

	public Float getTotalAcredBMR() {
		return totalAcredBMR;
	}

	public void setTotalAcredBMR(Float totalAcredBMR) {
		this.totalAcredBMR = totalAcredBMR;
	}

	public DatosDepAdelContainer getDatoAcredBMR() {
		return datoAcredBMR;
	}

	public void setDatoAcredBMR(DatosDepAdelContainer datoAcredBMR) {
		this.datoAcredBMR = datoAcredBMR;
	}

/*	
	public DatosDepAdelContainer getDatoAcredBMRSel() {
		if(datoAcredBMRSel == null) {
			datoAcredBMRSel = new DatosDepAdelContainer();
			ObjectFactory factory = new ObjectFactory();
			
			JAXBElement<ArrayOfDatosDepAdel> jaxbElement =  new JAXBElement( 
				         new QName(ArrayOfDatosDepAdel.class.getSimpleName()), ArrayOfDatosDepAdel.class, null);
			
			ArrayOfDatosDepAdel jaxbElement2 = factory.createArrayOfDatosDepAdel();
			
			jaxbElement.setValue(jaxbElement2);
			datoAcredBMRSel.setListDatosDepAdel(jaxbElement);
		}

		return datoAcredBMRSel;
	}

	
	public void setDatoAcredBMRSel(DatosDepAdelContainer datoAcredBMRSel) {
		this.datoAcredBMRSel = datoAcredBMRSel;
	}

	
	public ArrayOfDatosDepAdel getSeleccionDepsBMR() {
		if(seleccionDepsBMR == null) {
			ObjectFactory factory = new ObjectFactory();
			seleccionDepsBMR = factory.createArrayOfDatosDepAdel();
		}
		
		return seleccionDepsBMR;
	}

	public void setSeleccionDepsBMR(ArrayOfDatosDepAdel seleccionDepsBMR) {
		this.seleccionDepsBMR = seleccionDepsBMR;
	}
*/
	public ArrayList<DatosDepAdel> getLstSeleccionDepsBMR() {
		if(lstSeleccionDepsBMR == null)
			lstSeleccionDepsBMR = new ArrayList<DatosDepAdel>();
		
		return lstSeleccionDepsBMR;
	}

	public void setLstSeleccionDepsBMR(ArrayList<DatosDepAdel> lstSeleccionDepsBMR) {
		this.lstSeleccionDepsBMR = lstSeleccionDepsBMR;
	}

	
}
