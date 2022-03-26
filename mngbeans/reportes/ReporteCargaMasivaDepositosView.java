package com.americacg.cargavirtual.web.mngbeans.reportes;

import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.inject.Named;
import javax.xml.ws.WebServiceException;

import org.springframework.context.annotation.Scope;

import com.americacg.cargavirtual.gestion.model.ArrayOfIncrementoSaldo;
import com.americacg.cargavirtual.gestion.model.IncrementoSaldo;
import com.americacg.cargavirtual.gestion.model.ItemRespRepSaldo;
import com.americacg.cargavirtual.gestion.model.ObjectFactory;
import com.americacg.cargavirtual.gestion.model.RespRepSaldo;

import com.americacg.cargavirtual.web.enums.CfgTimeout;
import com.americacg.cargavirtual.web.helpers.GestionServiceHelper;
import com.americacg.cargavirtual.web.model.Error;

@Named("reporteCargaMasivaDepositosView")
@Scope(org.springframework.web.context.WebApplicationContext.SCOPE_SESSION)
public class ReporteCargaMasivaDepositosView extends ReporteGeneral<Object>  {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7133736556768913064L;
	private String depositos;

	@Override
	public Error resetearReporte() {
		Error e = super.resetearReporte();

		return e;
	}

	

	public void cargarDepositos(ActionEvent ae) {
		ObjectFactory objectFactory = new ObjectFactory();
		
		try {

			// System.out.println("CargarDepositos");

			if (!"M".equals(this.getUsuario().getTipoCliente())) {
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Solo el Mayorista puede realizar este tipo de transaccion", null));
				return;
			}

			if ((depositos == null) || ("".equals(depositos))) {
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Debe cargar al menos un deposito", null));
				return;
			}

			String ld[] = depositos.trim().split("\n", -1);
			// System.out.println("Lineas de depositos encontradas: |" + ld.length + "|");

			List<IncrementoSaldo> l = new ArrayList<IncrementoSaldo>();

			for (int i = 0; i < ld.length; i++) {
				// escribirLog(null, "Deposito(" + i + "): |" + ld[i] + "|");
				if (!"".equals(ld[i])) {
					String d[] = ld[i].trim().split(";", -1);

					if (d.length != 4) {
						FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "El formato del Reparto de la linea " + (i + 1) + " no es valido. ("
								+ ld[i] + ") Debe contener 4 campos", null));
						return;

					} else {

						// IDTIPOMOVIMIENTO
						Long idTipoMovimiento = null;
						String idTipMovAux = (d[0].trim());
						if ("".equals(idTipMovAux)) {
							FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "El idTipoMovimiento de la linea " + (i + 1) + " esta vacio", null));
							return;
						} else {
							try {
								idTipoMovimiento = Long.parseLong(idTipMovAux);
							} catch (Exception e) {
								FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, 
										"El idTipoMovimiento de la linea " + (i + 1) + " no es numerico", null));
								return;
							}
						}

						// IDCLIENTE
						Long idCli = null;
						String idCliAux = (d[1].trim());
						if ("".equals(idCliAux)) {
							FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "El idCliente de la linea " + (i + 1) + " esta vacio", null));
							return;
						} else {
							try {
								idCli = Long.parseLong(idCliAux);
							} catch (Exception e) {
								FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "El idCliente de la linea " + (i + 1) + " no es numerico", null));
								return;
							}
						}

						// VALOR
						String valor = d[2].trim();
						Float valorF = null;

						try {
							valorF = Float.parseFloat(valor);

							if (valorF == 0) {
								FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, 
										"El valor del deposito de la linea " + (i + 1) + " no puede ser cero", null));
								return;
							}

						} catch (Exception e) {
							FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, 
									"El valor del deposito de la linea " + (i + 1) + " no es numerico", null));
							return;
						}

						// VALIDO IDTIPOMOVIMIENTO CON VALOR

						if ((idTipoMovimiento == 5 || idTipoMovimiento == 28) && valorF < 0) {
							// OK
						} else if (idTipoMovimiento == 22 && valorF > 0) {
							// OK
						} else {
							FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "El tipo de movimiento de la linea " + (i + 1)
									+ " es incorrecto o no se corresponde con el importe cargado", null));
							return;
						}

						// COMENTARIO
						String comentario = d[3].trim();

						// Armo una clase con el IncrementoSaldo OK
						IncrementoSaldo ic = new IncrementoSaldo();
						ic.setIdCliente(objectFactory.createIncrementoSaldoIdCliente(idCli));
						ic.setIdTipoMovimiento(objectFactory.createIncrementoSaldoIdTipoMovimiento(idTipoMovimiento));
						ic.setValorAincrementar(objectFactory.createIncrementoSaldoValorAincrementar(valorF));
						ic.setNroFactura(objectFactory.createIncrementoSaldoNroFactura(""));
						ic.setComentario(objectFactory.createIncrementoSaldoComentario("CMD generado por usuario: " + this.getUsuario().getUsername() + ". " + comentario));
						ic.setHashGenerado(objectFactory.createIncrementoSaldoHashGenerado(""));

						// Agrego el Movimiento a la lista de Movimientes a insertar en la Base de Datos
						l.add(ic);
					}

				} else {
					// Se ignora el registro de la Linea i
				}
			}

			// System.out.println("Lista de Depositos PreCargada OK");
			for (Iterator iterator = l.iterator(); iterator.hasNext();) {
				IncrementoSaldo icf = (IncrementoSaldo) iterator.next();

				String msg = "idCli: " + icf.getIdCliente().getValue() + ", $ " + icf.getValorAincrementar().getValue() + " ";

				Long idDistSup = null;
				try {
					idDistSup = GestionServiceHelper.getGestionService(CfgTimeout.REPORTE)
							.buscaridDistribuidorSuperior(this.getUsuario().getIdMayorista(), icf.getIdCliente().getValue());
				} catch (WebServiceException ste) {
					if (ste.getCause().getClass().equals(SocketTimeoutException.class)) {
						FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "No se pudo establecer la comunicación con GESTION.\n Por favor intente nuevamente.", null));
					}else {
						FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error realizando consulta contra GESTION: " + ste.getMessage(), null));
					}					
				} catch (Exception e) {
					System.out.println("Error buscando el Distribuidor");
				}

				if ((idDistSup != null) && (idDistSup > 0)) {

					ArrayOfIncrementoSaldo lw = new ArrayOfIncrementoSaldo();
					lw.getIncrementoSaldo().add(icf);

					try {
						// Ejecuto el Metodo RepartoSaldo
						RespRepSaldo resul = GestionServiceHelper.getGestionService(CfgTimeout.REPORTE)
								.aceptarReparto(this.getUsuario().getIdMayorista(), this.getUsuario().getIdUsuario(), this.getUsuario().getPassword(), idDistSup, lw, "");

						if (resul.getError().getValue().getHayError().getValue()) {
							FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, msg + resul.getError().getValue().getMsgError().getValue(), null));
						} else {
							// Recorrer el array de registros procesados y mostrarlo en la pantalla
							if (resul.getListItemRespRepSaldo() != null) {
								for (ItemRespRepSaldo irrs : resul.getListItemRespRepSaldo().getValue().getItemRespRepSaldo()) {

									if (irrs.getError().getValue().getHayError().getValue()) {
										FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, msg + irrs.getError().getValue().getMsgError().getValue(), null));
									} else {
										FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, msg + "Se repartio exitosamente", null));
									}
								}
							} else {
								FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, msg + "El resultado del reparto fue nulo", null));
							}
						}
					} catch (WebServiceException ste) {
						if (ste.getCause().getClass().equals(SocketTimeoutException.class)) {
							FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "No se pudo establecer la comunicación con GESTION.\n Por favor intente nuevamente.", null));
						}else {
							FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error realizando consulta contra GESTION: " + ste.getMessage(), null));
						}
					} catch (Exception e) {
						FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,msg + "Error ejecutando el reparto de saldo: " + e.getMessage(), null));
					}

				} else {
					FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, 
							msg + " No se puede realizar el reparto" + " porque no se encontro el distribuidor", null));
				}
			}
			// Reseteo la variables depositos para que se limpie la inputTextarea
			depositos = "";

		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, 
					"Error ejecutando el WS de Carga de Masiva de Depositos: |" + e.getMessage() + "|", null));
		}
		return;
	}

	public void actualizarProductos(ActionEvent evt) {
		// No hace falta hacer nada
	}

	public String getDepositos() {
		return depositos;
	}

	public void setDepositos(String depositos) {
		this.depositos = depositos;
	}

}
