package com.americacg.cargavirtual.web.mngbeans.reportes;

import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import javax.faces.application.FacesMessage;
import javax.inject.Named;
import javax.faces.context.FacesContext;
import javax.xml.ws.WebServiceException;
import org.primefaces.PrimeFaces;
import org.springframework.context.annotation.Scope;
import com.americacg.cargavirtual.gateway.pagoElectronico.client.PagoElectronico;
import com.americacg.cargavirtual.gateway.pagoElectronico.funciones.FuncionGCOM;
import com.americacg.cargavirtual.gateway.pagoElectronico.model.parametros.funciones.output.DataOutputFcnGCOM;
import com.americacg.cargavirtual.web.model.Error;
import com.americacg.cargavirtual.web.model.Factura;
import com.americacg.cargavirtual.web.helpers.LogACGHelper;
import com.americacg.cargavirtual.web.lazymodels.LazyLIAPDataModel;

@Named("reportePagoElectronicoLIAPView")
@Scope(org.springframework.web.context.WebApplicationContext.SCOPE_SESSION)
public class ReportePagoElectronicoLIAPView extends ReporteGeneral<Factura> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2326266592185648986L;

	private LazyLIAPDataModel lazyModel;
	private PagoElectronico pagoElectronico = null;
	private String idConfiguracionComercio = null;
	private Long idDePago = null;

	@Override
	public Error resetearReporte() {
		Error err = super.resetearReporte();
		return err;
	}

	public ReportePagoElectronicoLIAPView() {
		super();
		Long idProceso = 1L;
		com.americacg.cargavirtual.gateway.pagoElectronico.model.MensajeOutboundGateway mo = null;

		try {
			this.pagoElectronico = new PagoElectronico(this.getUsuario().getIdMayorista(),
					this.getUsuario().getIdCliente(), this.getUsuario().getIdUsuario(), this.getUsuario().getUsername(),
					this.getUsuario().getPassword(), this.getUsuario().getTipoCliente(), false);

			// try {

			mo = null;

			FuncionGCOM fGCOM = new FuncionGCOM(this.pagoElectronico.getParametros());
			fGCOM.setParametroServicio(this.pagoElectronico.getParametrosServicio());
			
			fGCOM.getHeaderIn().setIdMayorista(this.getUsuario().getIdMayorista());//
			fGCOM.getHeaderIn().setIdCliente(this.getUsuario().getIdCliente()); //
			fGCOM.getHeaderIn().setIdUsuario(this.getUsuario().getIdUsuario());// oSB.append("10198");
			fGCOM.getHeaderIn().setUsuario(this.getUsuario().getUsername());// oSB.append("10198");
			fGCOM.getHeaderIn().setClave(this.getUsuario().getPassword());//
			
			mo = fGCOM.ejecutar();

			if (mo != null) {
				DataOutputFcnGCOM doGCOM = (DataOutputFcnGCOM) mo.getDataOutputFcn();
				
				if (doGCOM != null) {
					this.idConfiguracionComercio = String.valueOf(doGCOM.getIdMerchant());
				}
			} else {
				LogACGHelper.escribirLog(null, "Error : |No se obtuvo una respuesta valida desde el Gateway|");
				//err.setError("GST-TRW", "No se obtuvo una respuesta valida desde el Gateway.");
			}

		} catch (WebServiceException ste) {
			if (ste != null && ste.getCause() != null && ste.getCause().getClass() != null) {
				if (ste.getCause().getClass().equals(ConnectException.class)) {
					LogACGHelper.escribirLog(null,
							"actualizarValores: No se pudo establecer la comunicación (GPE-TOC).");
				} else if (ste.getCause().getClass().equals(SocketTimeoutException.class)) {
					LogACGHelper.escribirLog(null,
							"actualizarValores: No se pudo establecer la comunicación (GPE-TRW).");
				} else if (ste.getCause().getClass().equals(UnknownHostException.class)) {
					LogACGHelper.escribirLog(null, "actualizarValores: Error PAGO: |" + ste.getMessage() + "|");
				} else {
					LogACGHelper.escribirLog(null, "actualizarValores: Error PAGO: |" + ste.getMessage() + "|");
				}

			} else {
				LogACGHelper.escribirLog(null, "actualizarValores: Error PAGO:|" + ste.getMessage() + "|");
			}
		} catch (Exception e) {
			LogACGHelper.escribirLog(null, "Error : |" + e.getMessage() + "|");
			//err.setError("GST-TRW", "Error .\n Por favor intente nuevamente.");

		}
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

			if (!this.exportToExcel) {
				lazyModel = new LazyLIAPDataModel(this.pagoElectronico, this.idConfiguracionComercio, this.idDePago,
						this.getUsuario().getIdMayorista(), this.getUsuario().getIdCliente(),
						this.getUsuario().getIdUsuario(), this.getUsuario().getUsername(), this.getUsuario().getPassword(), this.fechaHoraDesde, this.fechaHoraHasta);
			} else {
				/*
				 * clFR.setPagina(1); clFR.setCantRegXPagina(-1);
				 * clFR.setCampoOrden("FAC_IDTRX"); clFR.setTipoOrden("DESC");
				 * stDRACG.setFiltroReporte(clFR);
				 * 
				 * byte[] rtRACG = gsp.descargaReporteACG(stDRACG);
				 * 
				 * FacesContext fc = FacesContext.getCurrentInstance(); ExternalContext ec =
				 * fc.getExternalContext();
				 * 
				 * ec.responseReset(); ec.setResponseContentType("text/plain");
				 * ec.setResponseContentLength(rtRACG.length);
				 * 
				 * SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd_HHmmss");
				 * ec.setResponseHeader("Content-Disposition", "attachment; filename=\"" +
				 * sdf.format(new Date()) + "_(" + this.getUsuario().getIdMayorista() + ")_" +
				 * "InformePNet.csv" + "\"");
				 * 
				 * OutputStream os = ec.getResponseOutputStream(); OutputStreamWriter osw = new
				 * OutputStreamWriter(os); PrintWriter writer = new PrintWriter(osw);
				 * writer.write(new String(rtRACG)); writer.flush(); writer.close();
				 * 
				 * fc.responseComplete();
				 */
			}
			PrimeFaces.current().executeScript("PF('panelFiltroWG').toggle();");

		} catch (WebServiceException ste) {
			if (ste != null && ste.getCause() != null && ste.getCause().getClass() != null) {
				if (ste.getCause().getClass().equals(ConnectException.class)) {
					FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
							"No se pudo establecer la comunicación (GPN-TOC).\n Por favor intente nuevamente.", null));
				} else if (ste.getCause().getClass().equals(SocketTimeoutException.class)) {
					FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
							"No se pudo establecer la comunicación (GPN-TRW).\n Por favor intente nuevamente.", null));
				} else if (ste.getCause().getClass().equals(UnknownHostException.class)) {
					LogACGHelper.escribirLog(null,
							"No se pudo establecer la comunicación (GPN-HNC): |" + ste.getMessage() + "|");
					FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
							"No se pudo establecer la comunicación (GPN-HNC).\n Por favor intente nuevamente.", null));
				} else {
					LogACGHelper.escribirLog(null,
							"Informe PNet. Error ejecutando el WS de consulta: |" + ste.getMessage() + "|");
					FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
							"Error ejecutando el WS de consulta: |" + ste.getMessage() + "|", null));
				}
			} else {
				LogACGHelper.escribirLog(null,
						"Informe PNet. Error ejecutando el WS de consulta: |" + ste.getMessage() + "|");
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
						"Error ejecutando el WS de consulta: |" + ste.getMessage() + "|", null));
			}
			FacesContext.getCurrentInstance().validationFailed();
			this.list = null;
		} catch (Exception e) {
			LogACGHelper.escribirLog(null,
					"Informe PNet. Excepcion ejecutando el WS de consulta: |" + e.getMessage() + "|");
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
					"Error ejecutando el WS de consulta de PNet: |" + e.getMessage() + "|", null));
			FacesContext.getCurrentInstance().validationFailed();
			this.list = null;
		}
		PrimeFaces.current().executeScript("PF('blockPanelGral').hide()");
		return;
	}

	public LazyLIAPDataModel getLazyModel() {
		return lazyModel;
	}

	public void setLazyModel(LazyLIAPDataModel lazyModel) {
		this.lazyModel = lazyModel;
	}

	public Long getIdDePago() {
		return idDePago;
	}

	public void setIdDePago(Long idDePago) {
		this.idDePago = idDePago;
	}

}