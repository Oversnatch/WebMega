package com.americacg.cargavirtual.web.mngbeans;

import java.io.Serializable;
import java.net.SocketTimeoutException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.model.SelectItem;
import javax.inject.Named;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import javax.xml.ws.WebServiceException;

import org.apache.commons.beanutils.BeanUtils;
import org.primefaces.PrimeFaces;
import org.primefaces.event.SelectEvent;
import org.springframework.context.annotation.Scope;
import com.americacg.cargavirtual.gestion.model.CoberturaSeguro;
import com.americacg.cargavirtual.gestion.model.CoberturaSegurosContainer;
import com.americacg.cargavirtual.gestion.model.Descripcion;
import com.americacg.cargavirtual.gestion.model.DescripcionContainer;
import com.americacg.cargavirtual.gestion.model.ObjectFactory;
import com.americacg.cargavirtual.gestion.model.PolizaSeguro;
import com.americacg.cargavirtual.gestion.model.RespIdString;
import com.americacg.cargavirtual.web.enums.CfgTimeout;
import com.americacg.cargavirtual.web.helpers.GestionServiceHelper;
import com.americacg.cargavirtual.web.shared.BasePage;

@Named("segurosView")
@Scope(org.springframework.web.context.WebApplicationContext.SCOPE_SESSION)
public class SegurosView   extends BasePage implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -7262436147822053373L;
	//VARIABLES
	private String asegApellido;
	private String asegNombre;
	private String asegDNICUIT;
	private String asegCalle;
	private String asegNumero;
	private String asegPiso;
	private String asegDepto;
	
	private Long asegIdProvincia;
	private List<SelectItem> provincias;
	
	private Long asegIdLocalidad;
	private List<SelectItem> localidades;
	
	private Long asegIdPais;
	private List<SelectItem> paises;
	
	private String asegTelFijo;
	private String asegCelular;
	private String asegMail;
	
	private String marca;
	private String modelo;
	private String numeroLinea;

	private Long idProducto;
	private List<SelectItem> productos;

	private Long idEmpresaPrestataria;
	private List<SelectItem> empresasPrestatarias;

	private Long idFormaPago;
	private List<SelectItem> formasDePago;
	
	private Long idCobertura;
	private List<SelectItem> coberturas;
	
	private Long idTipoTarjeta;
	private List<SelectItem> tipoTarjetas;
	
	private Long idBanco;
	private List<SelectItem> bancos;
	
	private String cbuDebitoBancario;
	private String tarjetaCredNumero;
	private String tarjetaCredVto;
	private String tarjetaCredCodSeg;
	
	private String fechaTicket;
	private String prodTicket;
	private String coberturaTicket;
	private String formaPagoTicket;
	private Float importeTicket;
	private Long idPolizaSeguroTicket;
	
	private String tipoTarjetaTicket;
	private String bancoTicket;
	
	private PolizaSeguro psParaTicket;
	
	
	public void altaSeguro(ActionEvent evt) {
		
		PolizaSeguro p = new PolizaSeguro();
		psParaTicket = new PolizaSeguro();
		
		try {
			GregorianCalendar gcFechaAlta = new GregorianCalendar();
			gcFechaAlta.setTime(new Date());
			XMLGregorianCalendar xmlGCFechaAlta = DatatypeFactory.newInstance()
					.newXMLGregorianCalendar(gcFechaAlta);
			
			p.setFechaAlta(xmlGCFechaAlta);
			
			
			ObjectFactory factory = new ObjectFactory();
			p.setIdMayorista(factory.createPolizaSeguroIdMayorista(this.getUsuario().getIdMayorista()));
			p.setIdCliente(factory.createPolizaSeguroIdCliente(this.getUsuario().getIdCliente()));
			p.setIdUsuAlta(factory.createPolizaSeguroIdUsuAlta(this.getUsuario().getIdUsuario()));
			p.setAsegApellido(factory.createPolizaSeguroAsegApellido(asegApellido));
			p.setAsegNombre(factory.createPolizaSeguroAsegNombre(asegNombre));
			p.setAsegDNICUIT(factory.createPolizaSeguroAsegDNICUIT(asegDNICUIT));
			p.setAsegCalle(factory.createPolizaSeguroAsegCalle(asegCalle));
			p.setAsegNumero(factory.createPolizaSeguroAsegNumero(asegNumero));
			p.setAsegPiso(factory.createPolizaSeguroAsegPiso(asegPiso));
			p.setAsegDepto(factory.createPolizaSeguroAsegDepto(asegDepto));
			p.setAsegIdPais(factory.createPolizaSeguroAsegIdPais(asegIdPais));
			p.setAsegIdProvincia(factory.createPolizaSeguroAsegIdProvincia(asegIdProvincia));
			p.setAsegIdLocalidad(factory.createPolizaSeguroAsegIdLocalidad(asegIdLocalidad));
			p.setAsegTelFijo(factory.createPolizaSeguroAsegTelFijo(asegTelFijo));
			p.setAsegCelular(factory.createPolizaSeguroAsegCelular(asegCelular));
			p.setAsegMail(factory.createPolizaSeguroAsegMail(asegMail));
			p.setIdProducto(factory.createPolizaSeguroIdProducto(idProducto));
			p.setMarca(factory.createPolizaSeguroMarca(marca));
			p.setModelo(factory.createPolizaSeguroModelo(modelo));
			p.setNumeroLinea(factory.createPolizaSeguroNumeroLinea(numeroLinea));
			p.setIdEmpresaPrestataria(factory.createPolizaSeguroIdEmpresaPrestataria(idEmpresaPrestataria));
			p.setIdCobertura(factory.createPolizaSeguroIdCobertura(idCobertura));
			p.setIdFormaPago(factory.createPolizaSeguroIdFormaPago(idFormaPago));
			p.setCbuDebitoBancario(factory.createPolizaSeguroCbuDebitoBancario(cbuDebitoBancario));
			p.setTarjetaCredNumero(factory.createPolizaSeguroTarjetaCredNumero(tarjetaCredNumero));
			p.setTarjetaCredVto(factory.createPolizaSeguroTarjetaCredVto(tarjetaCredVto));
			p.setTarjetaCredCodSeg(factory.createPolizaSeguroTarjetaCredCodSeg(tarjetaCredCodSeg));
			p.setIdTipoTarjeta(factory.createPolizaSeguroIdTipoTarjeta(idTipoTarjeta));
			p.setIdBanco(factory.createPolizaSeguroIdBanco(idBanco));

			
			
			RespIdString r = GestionServiceHelper.getGestionService(CfgTimeout.DEFAULT).grabarPolizaSeguro(p);
			String estadoSeguro = null;
			
			if (r.getError().getValue().getHayError().getValue()){
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, r.getError().getValue().getMsgError().getValue(), null));	
			}else{
				FacesContext.getCurrentInstance().addMessage(null,new FacesMessage(FacesMessage.SEVERITY_INFO,r.getDesc().getValue() + "idPolizaSeguro: " + r.getId().getValue(), null));
				
				estadoSeguro = "OK";
				
				fechaTicket = ""; 
				prodTicket = "";
				coberturaTicket = "";
				formaPagoTicket = "";
				importeTicket = 0F;
				idPolizaSeguroTicket = 0L;
				
				tipoTarjetaTicket = "";
				bancoTicket = "";
				
				try {
					idPolizaSeguroTicket = r.getId().getValue();
					
					
					SimpleDateFormat formato = new SimpleDateFormat("dd-MM-yyyy HH:mm");
					String fecha = formato.format(new Date()).toString();
					fechaTicket = fecha;
					
					DescripcionContainer cd = GestionServiceHelper.getGestionService(CfgTimeout.DEFAULT).mostrarSeguroProductos(this.getUsuario().getIdMayorista(), p.getIdProducto().getValue());
					for (Descripcion d : cd.getListDescripcion().getValue().getDescripcion()) {
						if (d.getId().getValue().compareTo(p.getIdProducto().getValue()) == 0){
							prodTicket = d.getDescripcion().getValue();
						}
					}

					CoberturaSegurosContainer cdc = GestionServiceHelper.getGestionService(CfgTimeout.DEFAULT).mostrarSeguroCoberturas(this.getUsuario().getIdMayorista(), p.getIdCobertura().getValue(), p.getIdProducto().getValue());
					for (CoberturaSeguro d : cdc.getListCoberturasSeguro().getValue().getCoberturaSeguro()) {
						if (d.getIdCobertura().getValue().compareTo(p.getIdCobertura().getValue()) == 0){
							coberturaTicket = d.getDescripcion().getValue();
							importeTicket = d.getImporte().getValue();
						}
					}
					
					DescripcionContainer cdf = GestionServiceHelper.getGestionService(CfgTimeout.DEFAULT).mostrarSeguroFormasPago(this.getUsuario().getIdMayorista(), p.getIdFormaPago().getValue());
					for (Descripcion d : cdf.getListDescripcion().getValue().getDescripcion()) {
						if (d.getId().getValue().compareTo(p.getIdFormaPago().getValue()) == 0){
							formaPagoTicket = d.getDescripcion().getValue();
						}
					}

					DescripcionContainer stt = GestionServiceHelper.getGestionService(CfgTimeout.DEFAULT).mostrarSeguroTipoTarjetas(this.getUsuario().getIdMayorista(), p.getIdTipoTarjeta().getValue());
					for (Descripcion d : stt.getListDescripcion().getValue().getDescripcion()) {
						if (d.getId().getValue().compareTo(p.getIdTipoTarjeta().getValue()) == 0){
							tipoTarjetaTicket = d.getDescripcion().getValue();
						}
					}

					DescripcionContainer sb = GestionServiceHelper.getGestionService(CfgTimeout.DEFAULT).mostrarSeguroBancos(this.getUsuario().getIdMayorista(), p.getIdBanco().getValue());
					for (Descripcion d : sb.getListDescripcion().getValue().getDescripcion()) {
						if (d.getId().getValue().compareTo(p.getIdBanco().getValue()) == 0){
							bancoTicket = d.getDescripcion().getValue();
						}
					}

					
				} catch (Exception e) {
					//No hago nada
				}
				
				try {
					BeanUtils.copyProperties(psParaTicket, p);
				} catch (Exception e) {
					//No hago nada
				}
				
				//((BooleanHolder) getServletContext().getAttribute("mostrarTicketSeguro")).setValor( || "OK".equals(estadoSeguro));		

				if("OK".equals(estadoSeguro)) {
					this.mostrarTicket("SEGURO");
				}
				
				//Limpio todos los datos
				p = new PolizaSeguro();
				limpiarPantalla();
			}
			
		} catch (WebServiceException ste) {
			if (ste.getCause().getClass().equals(SocketTimeoutException.class)) {
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "No se pudo establecer la comunicación con GESTION.\n Por favor intente nuevamente.", null));
			}else {
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error realizando consulta contra GESTION: " + ste.getMessage(), null));
			}
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error en Grabacion de Seguro", null));
		}
		
	}
	
	public void limpiarPantalla(){
		try {
			this.asegApellido = null;
			this.asegNombre = null;
			this.asegDNICUIT = null;
			this.asegCalle = null;
			this.asegNumero = null;
			this.asegPiso = null;
			this.asegDepto = null;
			this.asegIdProvincia = null;
			this.asegIdLocalidad = null;
			this.asegIdPais = null;
			this.asegTelFijo = null;
			this.asegCelular = null;
			this.asegMail = null;
			this.marca = null;
			this.modelo = null;
			this.numeroLinea = null;
			this.idProducto = null;
			this.idEmpresaPrestataria = null;
			this.idFormaPago = null;
			this.idCobertura = null;
			this.idTipoTarjeta = null;
			this.idBanco = null;
			this.cbuDebitoBancario = null;
			this.tarjetaCredNumero = null;
			this.tarjetaCredVto = null;
			this.tarjetaCredCodSeg = null;

		} catch (Exception e) {
			//System.out.println("Error en proceso de Limpieza de Pantalla de Seguros. Error: |" + e.getMessage() + "|");
		}
	}
	
	
	public void onChangePais(SelectEvent<?> event){
		asegIdProvincia = null;
		asegIdLocalidad = null;
	}

	public void onChangeProvincia(SelectEvent<?> event){
		asegIdLocalidad = null;
	}

	public void actualizarCoberturas(ActionEvent evt){
		// No hace falta hacer nada
	}

	public void actualizarPanelDatosDePago(ActionEvent evt){
		if (idFormaPago != 1){
			//System.out.println("limpiar Campos compra Debito Bancario");
			cbuDebitoBancario = null;
		}
		
		if (idFormaPago != 2){
			//System.out.println("limpiar Campos de Tarjeta");
			tarjetaCredNumero = null;
			tarjetaCredVto = null;
			tarjetaCredVto = null;
		}
	}

	public String getAsegApellido() {
		return asegApellido;
	}

	public void setAsegApellido(String asegApellido) {
		this.asegApellido = asegApellido;
	}

	public String getAsegNombre() {
		return asegNombre;
	}

	public void setAsegNombre(String asegNombre) {
		this.asegNombre = asegNombre;
	}

	public String getAsegCalle() {
		return asegCalle;
	}

	public void setAsegCalle(String asegCalle) {
		this.asegCalle = asegCalle;
	}

	public String getAsegNumero() {
		return asegNumero;
	}

	public void setAsegNumero(String asegNumero) {
		this.asegNumero = asegNumero;
	}

	public String getAsegPiso() {
		return asegPiso;
	}

	public void setAsegPiso(String asegPiso) {
		this.asegPiso = asegPiso;
	}

	public String getAsegDepto() {
		return asegDepto;
	}

	public void setAsegDepto(String asegDepto) {
		this.asegDepto = asegDepto;
	}

	public String getAsegTelFijo() {
		return asegTelFijo;
	}

	public void setAsegTelFijo(String asegTelFijo) {
		this.asegTelFijo = asegTelFijo;
	}

	public String getAsegCelular() {
		return asegCelular;
	}

	public void setAsegCelular(String asegCelular) {
		this.asegCelular = asegCelular;
	}

	public String getAsegMail() {
		return asegMail;
	}

	public void setAsegMail(String asegMail) {
		this.asegMail = asegMail;
	}
	
	public List<SelectItem> getProvincias() {

		try{

			this.provincias = new ArrayList<SelectItem>();
			this.localidades = new ArrayList<SelectItem>();
			
			if ((this.asegIdPais != null) && (this.asegIdPais > 0)){
				DescripcionContainer cd = GestionServiceHelper.getGestionService(CfgTimeout.DEFAULT).mostrarProvincias(this.getUsuario().getIdMayorista(), null, this.getAsegIdPais());

				if (!cd.getError().getValue().getHayError().getValue()){
					for (Descripcion d : cd.getListDescripcion().getValue().getDescripcion()) {
						this.provincias.add(new SelectItem(d.getId().getValue(), d.getDescripcion().getValue()));
					}
				}else{
					//System.out.println("Error en getProvincias: |" + cd.getError().getMsgError() + "|");
				}

			}
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error en lectura de Provincias: " + e.getMessage(), null));
		}
		return provincias;
	}


	public void setProvincias(List<SelectItem> provincias) {
		this.provincias = provincias;
	}


	public List<SelectItem> getLocalidades() {

		try{
			this.localidades = new ArrayList<SelectItem>();

			if ((this.asegIdProvincia != null) && (this.asegIdProvincia > 0)){
				DescripcionContainer cd = GestionServiceHelper.getGestionService(CfgTimeout.DEFAULT).mostrarLocalidadesPorProv(this.getUsuario().getIdMayorista(), this.asegIdProvincia, null);

				if (!cd.getError().getValue().getHayError().getValue()){
					for (Descripcion d : cd.getListDescripcion().getValue().getDescripcion()) {
						this.localidades.add(new SelectItem(d.getId().getValue(), d.getDescripcion().getValue()));
					}
				}else{
					//System.out.println("Error en getLocalidades: |" + cd.getError().getMsgError() + "|");
				}
			}
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error en lectura de Localidades S: " + e.getMessage(), null));
		}
		return this.localidades;
	}

	public List<SelectItem> getPaises() {
		try{
			
			this.provincias = new ArrayList<SelectItem>();
			this.localidades = new ArrayList<SelectItem>();
			
			if (this.paises == null){
				this.paises = new ArrayList<SelectItem>();
				DescripcionContainer cd = GestionServiceHelper.getGestionService(CfgTimeout.DEFAULT).mostrarPaises(this.getUsuario().getIdMayorista(), null);

				for (Descripcion d : cd.getListDescripcion().getValue().getDescripcion()) {
					this.paises.add(new SelectItem(d.getId().getValue(), d.getDescripcion().getValue()));
				}
			}
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error en lectura de Paises: " + e.getMessage(), null));
		}
		return paises;
	}

	public void setPaises(List<SelectItem> paises) {
		this.paises = paises;
	}

	public Long getAsegIdProvincia() {
		return asegIdProvincia;
	}

	public void setAsegIdProvincia(Long asegIdProvincia) {
		this.asegIdProvincia = asegIdProvincia;
	}

	public Long getAsegIdLocalidad() {
		return asegIdLocalidad;
	}

	public void setAsegIdLocalidad(Long asegIdLocalidad) {
		this.asegIdLocalidad = asegIdLocalidad;
	}

	public Long getAsegIdPais() {
		return asegIdPais;
	}

	public void setAsegIdPais(Long asegIdPais) {
		this.asegIdPais = asegIdPais;
	}

	public String getMarca() {
		return marca;
	}

	public void setMarca(String marca) {
		this.marca = marca;
	}

	public String getModelo() {
		return modelo;
	}

	public void setModelo(String modelo) {
		this.modelo = modelo;
	}

	public String getNumeroLinea() {
		return numeroLinea;
	}

	public void setNumeroLinea(String numeroLinea) {
		this.numeroLinea = numeroLinea;
	}

	public Long getIdProducto() {
		return idProducto;
	}

	public void setIdProducto(Long idProducto) {
		this.idProducto = idProducto;
	}

	public List<SelectItem> getProductos() {
		try{
			
			//Long primerIDprod = 0L;

			if (this.productos == null){
				this.productos = new ArrayList<SelectItem>();
				DescripcionContainer cd = GestionServiceHelper.getGestionService(CfgTimeout.DEFAULT).mostrarSeguroProductos(this.getUsuario().getIdMayorista(), null);

				for (Descripcion d : cd.getListDescripcion().getValue().getDescripcion()) {
					this.productos.add(new SelectItem(d.getId().getValue(), d.getDescripcion().getValue()));
				}

			}
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error en lectura de Productos: " + e.getMessage(), null));
		}
		return productos;
	}

	public void setProductos(List<SelectItem> productos) {
		this.productos = productos;
	}

	public Long getIdEmpresaPrestataria() {
		return idEmpresaPrestataria;
	}

	public void setIdEmpresaPrestataria(Long idEmpresaPrestataria) {
		this.idEmpresaPrestataria = idEmpresaPrestataria;
	}

	public List<SelectItem> getEmpresasPrestatarias() {
		try{
			if (this.empresasPrestatarias == null){
				this.empresasPrestatarias = new ArrayList<SelectItem>();
				DescripcionContainer cd = GestionServiceHelper.getGestionService(CfgTimeout.DEFAULT).mostrarSeguroEmpresasPrestataria(this.getUsuario().getIdMayorista(), null);

				for (Descripcion d : cd.getListDescripcion().getValue().getDescripcion()) {
					this.empresasPrestatarias.add(new SelectItem(d.getId().getValue(), d.getDescripcion().getValue()));
				}
			}

		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error en lectura de EmpresasPrestatarias: " + e.getMessage(), null));
		}
		return empresasPrestatarias;
	}

	public void setEmpresasPrestatarias(List<SelectItem> empresasPrestatarias) {
		this.empresasPrestatarias = empresasPrestatarias;
	}

	public Long getIdFormaPago() {
		return idFormaPago;
	}

	public void setIdFormaPago(Long idFormaPago) {
		this.idFormaPago = idFormaPago;
	}

	public List<SelectItem> getFormasDePago() {
		try{
			if (this.formasDePago == null){
				this.formasDePago = new ArrayList<SelectItem>();
				DescripcionContainer cd = GestionServiceHelper.getGestionService(CfgTimeout.DEFAULT).mostrarSeguroFormasPago(this.getUsuario().getIdMayorista(), null);

				for (Descripcion d : cd.getListDescripcion().getValue().getDescripcion()) {
					this.formasDePago.add(new SelectItem(d.getId().getValue(), d.getDescripcion().getValue()));
				}
			}

		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error en lectura de FormasDePago: " + e.getMessage(), null));
		}
		
		return formasDePago;
	}

	public void setFormasDePago(List<SelectItem> formasDePago) {
		this.formasDePago = formasDePago;
	}

	public Long getIdCobertura() {
		return idCobertura;
	}

	public void setIdCobertura(Long idCobertura) {
		this.idCobertura = idCobertura;
	}

	public List<SelectItem> getCoberturas() {
		try{
			this.coberturas = new ArrayList<SelectItem>();
			CoberturaSegurosContainer cd = GestionServiceHelper.getGestionService(CfgTimeout.DEFAULT).mostrarSeguroCoberturas(this.getUsuario().getIdMayorista(), null, idProducto);

			if (!cd.getError().getValue().getHayError().getValue()){
				for (CoberturaSeguro d : cd.getListCoberturasSeguro().getValue().getCoberturaSeguro()) {
					this.coberturas.add(new SelectItem(d.getIdCobertura().getValue(), d.getDescripcion().getValue() + " - Importe: " + d.getImporte().getValue()));
				}
			}else{
				//System.out.println("Error en getCoberturas: |" + cd.getError().getMsgError() + "|");
			}

		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error en lectura de Coberturas: " + e.getMessage(), null));
		}
		return coberturas;
	}

	public void setCoberturas(List<SelectItem> coberturas) {
		this.coberturas = coberturas;
	}

	public String getCbuDebitoBancario() {
		return cbuDebitoBancario;
	}

	public void setCbuDebitoBancario(String cbuDebitoBancario) {
		this.cbuDebitoBancario = cbuDebitoBancario;
	}

	public String getTarjetaCredNumero() {
		return tarjetaCredNumero;
	}

	public void setTarjetaCredNumero(String tarjetaCredNumero) {
		this.tarjetaCredNumero = tarjetaCredNumero;
	}

	public String getTarjetaCredVto() {
		return tarjetaCredVto;
	}

	public void setTarjetaCredVto(String tarjetaCredVto) {
		this.tarjetaCredVto = tarjetaCredVto;
	}

	public String getTarjetaCredCodSeg() {
		return tarjetaCredCodSeg;
	}

	public void setTarjetaCredCodSeg(String tarjetaCredCodSeg) {
		this.tarjetaCredCodSeg = tarjetaCredCodSeg;
	}


	public void setLocalidades(List<SelectItem> localidades) {
		this.localidades = localidades;
	}


	public String getFechaTicket() {
		return fechaTicket;
	}


	public void setFechaTicket(String fechaTicket) {
		this.fechaTicket = fechaTicket;
	}


	public PolizaSeguro getPsParaTicket() {
		return psParaTicket;
	}


	public void setPsParaTicket(PolizaSeguro psParaTicket) {
		this.psParaTicket = psParaTicket;
	}


	public String getProdTicket() {
		return prodTicket;
	}


	public void setProdTicket(String prodTicket) {
		this.prodTicket = prodTicket;
	}


	public String getCoberturaTicket() {
		return coberturaTicket;
	}


	public void setCoberturaTicket(String coberturaTicket) {
		this.coberturaTicket = coberturaTicket;
	}


	public String getFormaPagoTicket() {
		return formaPagoTicket;
	}


	public void setFormaPagoTicket(String formaPagoTicket) {
		this.formaPagoTicket = formaPagoTicket;
	}


	public Float getImporteTicket() {
		return importeTicket;
	}


	public void setImporteTicket(Float importeTicket) {
		this.importeTicket = importeTicket;
	}


	public String getAsegDNICUIT() {
		return asegDNICUIT;
	}


	public void setAsegDNICUIT(String asegDNICUIT) {
		this.asegDNICUIT = asegDNICUIT;
	}


	public Long getIdTipoTarjeta() {
		return idTipoTarjeta;
	}


	public void setIdTipoTarjeta(Long idTipoTarjeta) {
		this.idTipoTarjeta = idTipoTarjeta;
	}


	public List<SelectItem> getTipoTarjetas() {
		try{
			if (this.tipoTarjetas == null){
				this.tipoTarjetas = new ArrayList<SelectItem>();
				DescripcionContainer cd = GestionServiceHelper.getGestionService(CfgTimeout.DEFAULT).mostrarSeguroTipoTarjetas(this.getUsuario().getIdMayorista(), null);

				for (Descripcion d : cd.getListDescripcion().getValue().getDescripcion()) {
					this.tipoTarjetas.add(new SelectItem(d.getId().getValue(), d.getDescripcion().getValue()));
				}
			}
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error en lectura de TipoTarjetas: " + e.getMessage(), null));
		}		
		return tipoTarjetas;
	}


	public void setTipoTarjetas(List<SelectItem> tipoTarjetas) {
		this.tipoTarjetas = tipoTarjetas;
	}


	public Long getIdBanco() {
		return idBanco;
	}


	public void setIdBanco(Long idBanco) {
		this.idBanco = idBanco;
	}


	public List<SelectItem> getBancos() {
		try{
			if (this.bancos == null){
				this.bancos = new ArrayList<SelectItem>();
				DescripcionContainer cd = GestionServiceHelper.getGestionService(CfgTimeout.DEFAULT).mostrarSeguroBancos(this.getUsuario().getIdMayorista(), null);

				for (Descripcion d : cd.getListDescripcion().getValue().getDescripcion()) {
					this.bancos.add(new SelectItem(d.getId().getValue(), d.getDescripcion().getValue()));
				}
			}
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error en lectura de Bancos: " + e.getMessage(), null));
		}		
		return bancos;
	}


	public void setBancos(List<SelectItem> bancos) {
		this.bancos = bancos;
	}


	public String getTipoTarjetaTicket() {
		return tipoTarjetaTicket;
	}


	public void setTipoTarjetaTicket(String tipoTarjetaTicket) {
		this.tipoTarjetaTicket = tipoTarjetaTicket;
	}


	public String getBancoTicket() {
		return bancoTicket;
	}


	public void setBancoTicket(String bancoTicket) {
		this.bancoTicket = bancoTicket;
	}


	public Long getIdPolizaSeguroTicket() {
		return idPolizaSeguroTicket;
	}


	public void setIdPolizaSeguroTicket(Long idPolizaSeguroTicket) {
		this.idPolizaSeguroTicket = idPolizaSeguroTicket;
	}

	public void mostrarTicket(String tipoTicket) {
		String nombreTicketCompleto = "";
		String nombreTicket = this.getUsuario().getTicket();

		if (!"SEGURO".equals(tipoTicket) && (nombreTicket == null || nombreTicket.isEmpty())) {
			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, 
					"Ticket no configurado para el usuario.", null);

			PrimeFaces.current().dialog().showMessageDynamic(msg);
			PrimeFaces.current().executeScript("PF('blockPanelGral').hide()");
			FacesContext.getCurrentInstance().validationFailed();
			return;
		}

		Map<String, Object> options = new HashMap<String, Object>();
		options.put("modal", true);
		options.put("draggable", true);
		options.put("resizable", true);
		options.put("closable", true);
		options.put("contentWidth", 700); // this.getUsuario().getAnchoTicket());
		options.put("contentHeight", 500);
		options.put("position", "center center");
		options.put("includeViewParams", true);

		/*
		<script>
		function mostrarTicketSeguro(mostrarTicketSeguro) {
			if (mostrarTicketSeguro) {
				var winp = window
						.open(
								'ticketSeguro.html',
								'ticketSeguro',
								'height=500,width=700,left=100,top=100,resizable=yes,scrollbars=yes,toolbar=no,status=no');
				winp.focus();
			}
		}
	</script>*/
		
		
		
		List<String> paramList = new ArrayList<String>();
		// paramList.add("ENTIDAD");
		Map<String, List<String>> paramMap = new HashMap<String, List<String>>();
		// paramMap.put("ORIGEN", paramList);

		if ("SEGURO".equals(tipoTicket)) {
			nombreTicketCompleto = "/secure/shared/tickets/ticketSeguro";
		} else {
			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Tipo de Ticket invalido.", null);

			PrimeFaces.current().dialog().showMessageDynamic(msg);
			PrimeFaces.current().executeScript("PF('blockPanelGral').hide()");
			FacesContext.getCurrentInstance().validationFailed();
			return;
		}

		// --- Abre el dialogo para buscar articulos
		PrimeFaces.current().dialog().openDynamic(nombreTicketCompleto, options, paramMap);
		PrimeFaces.current().executeScript("PF('blockPanelGral').hide()");
	}
	
	
	
	

}


	
	
