package com.americacg.cargavirtual.web.mngbeans.reportes;

import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.inject.Named;
import javax.xml.ws.WebServiceException;
import org.springframework.context.annotation.Scope;
import com.americacg.cargavirtual.gestion.model.Cliente;
import com.americacg.cargavirtual.gestion.model.RespIdString;
import com.americacg.cargavirtual.gestion.model.ResultadoBase;
import com.americacg.cargavirtual.gestion.model.Usuario;
import com.americacg.cargavirtual.web.enums.CfgTimeout;
import com.americacg.cargavirtual.web.helpers.GestionServiceHelper;
import com.americacg.cargavirtual.web.model.Error;

@Named("reporteCargaMasivaClientesView")
@Scope(org.springframework.web.context.WebApplicationContext.SCOPE_SESSION)
public class ReporteCargaMasivaClientesView  extends ReporteGeneral<Object>{

	/**
	 * 
	 */
	private static final long serialVersionUID = -8244927557079067672L;
	private String clientes;
	private Long idDistribuidorSuperior;

	@Override
	public Error resetearReporte() {
		Error e = super.resetearReporte();

		this.clientes = "";
		idDistribuidorSuperior = null;
		// file = null;
		return e;
	}


	public void cargarClientes(ActionEvent ae) {

		try {

			// System.out.println("CargarClientes");

			if (!"M".equals(this.getUsuario().getTipoCliente())) {
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Solo el Mayorista puede realizar este tipo de operacion", null));
				return;

			}

			if ((idDistribuidorSuperior == null) || (idDistribuidorSuperior <= 0)) {
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, 
						"El ID de Distribuidor Sobre el que se quieren cargar los clientes es invalido. Por favor revisar dicho dato", null));
				return;

			}

			if ((clientes == null) || ("".equals(clientes))) {
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Debe cargar al menos un cliente en el cuadro de texto inferior", null));
				return;
			}

			String ld[] = clientes.trim().split("\n", -1);
			// System.out.println("Lineas de clientes encontradas: |" + ld.length + "|");

			List<Cliente> lc = new ArrayList<Cliente>();
			List<Usuario> lu = new ArrayList<Usuario>();

			List<Boolean> lac0 = new ArrayList<Boolean>();

			// Cargo la lista de Clientes y usuarios

			for (int i = 0; i < ld.length; i++) {
				// escribirLog(null, "Cliente (" + i + "): |" + ld[i] + "|");
				if (!"".equals(ld[i])) {
					String d[] = ld[i].trim().split(String.valueOf((char) 185), -1);

					Integer cantCampos = 51;

					if (d.length != cantCampos) {
						FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, 
								"El formato del Cliente/Usuario de la linea " + (i + 1) + " no es valido. (" + ld[i]
										+ ") Debe contener " + cantCampos + " campos y contiene: " + d.length, null));
						return;

					} else {

						Cliente c = new Cliente();

						// ---------//
						// CLIENTE //
						// ---------//
						Integer col = 0;
						try {

							c.getIdMayorista().setValue(this.getUsuario().getIdMayorista());

							c.getIdCliente().setNil(true);

							c.getIdDistribuidorSuperior().setValue(idDistribuidorSuperior);

							c.getEstado().setValue("A");
							c.getComentario().setValue("");

							// Razon Social
							String razonSocialCliente = (d[0].trim());
							if ("".equals(razonSocialCliente)) {
								FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "La Razon Social de la linea " + (i + 1) + " esta vacia", null));
								return;
							} else {
								c.getRazonSocial().setValue(razonSocialCliente);
							}

							col = 1;
							// Nombre Fantasia
							c.getNombreFantasia().setValue(d[1].trim());

							col = 2;
							// ID Aux.
							c.getNombre().setValue(d[2].trim());

							col = 3;
							// ID Aux. 2
							c.getApellido().setValue(d[3].trim());

							col = 4;
							// Tipo Cliente
							c.getTipoCliente().setValue(d[4].trim());

							col = 5;
							// Tipo Terminal
							c.getIdTipoTerminal().setValue(Long.parseLong(d[5].trim()));

							col = 6;
							// Tipo Iva
							c.getTipoIva().setValue(d[6].trim());

							col = 7;
							// CUIT
							try {
								c.getCUIT().setValue(Long.parseLong(d[7].trim()));
							} catch (Exception e) {
								c.getCUIT().setValue(0L);
							}

							col = 8;
							// N. Contrato
							try {
								c.getNroContrato().setValue(Long.parseLong(d[8].trim()));
							} catch (Exception e) {
								c.getNroContrato().setValue(0L);
							}

							col = 9;
							// Rubro
							c.getIdRubro().setValue(Long.parseLong(d[9].trim()));

							col = 10;
							// Habilitar Todos los Prods con Comis cero
							// Es un parametro del metodo "agregarModificarCliente"
							lac0.add((Integer.parseInt(d[10].trim()) == 1) ? Boolean.TRUE : Boolean.FALSE);

							col = 11;
							// Facturable
							c.getFacturable().setValue((Integer.parseInt(d[11].trim()) == 1) ? Boolean.TRUE : Boolean.FALSE);

							col = 12;
							// Acred.Comis C/IVA
							c.getAcredComisionConIVA().setValue(
									(Integer.parseInt(d[12].trim()) == 1) ? Boolean.TRUE : Boolean.FALSE);

							col = 13;
							// Dist(habilit. login POS)
							c.getDistPermitirLogPos().setValue(
									(Integer.parseInt(d[13].trim()) == 1) ? Boolean.TRUE : Boolean.FALSE);

							col = 14;
							// Mostrar Pin
							c.getMostrarPin().setValue((Integer.parseInt(d[14].trim()) == 1) ? Boolean.TRUE : Boolean.FALSE);

							col = 15;
							// Habilit. Virtual
							c.getHabilitarVirtual().setValue((Integer.parseInt(d[15].trim()) == 1) ? Boolean.TRUE : Boolean.FALSE);

							col = 16;
							// Calc. Comisi. Autom
							c.getCalculoAutomComisiones().setValue(
									(Integer.parseInt(d[16].trim()) == 1) ? Boolean.TRUE : Boolean.FALSE);

							col = 17;
							// Habilit. Prestamos
							c.getHabilitarPrestamos().setValue(
									(Integer.parseInt(d[17].trim()) == 1) ? Boolean.TRUE : Boolean.FALSE);

							col = 18;
							// WU_idAgente # WU_idLocation # WU_codLocalAgente ( d[18] )
							c.getWuIdLocation().setValue(d[18].trim());

							col = 19;
							// Solicit. datos al Cliente
							c.getSolicActualizacionDatosAlCliente().setValue(
									(Integer.parseInt(d[19].trim()) == 1) ? Boolean.TRUE : Boolean.FALSE);

							col = 20;
							// Cliente Validado OK
							c.getClienteValidadoOK().setValue(
									(Integer.parseInt(d[20].trim()) == 1) ? Boolean.TRUE : Boolean.FALSE);

							col = 21;
							// Permitir Ventas por Web
							c.getPermitirVentasPorWeb().setValue(
									(Integer.parseInt(d[21].trim()) == 1) ? Boolean.TRUE : Boolean.FALSE);

							col = 22;
							// Valor AutoIncremento de Saldo
							try {
								c.getValorParaIncrementoAutomaticoDeSaldo().setValue(Integer.parseInt(d[22].trim()));
							} catch (Exception e) {
								c.getValorParaIncrementoAutomaticoDeSaldo().setValue(0);
							}

							col = 23;
							// Tel 1
							c.getTelefono1().setValue(d[23].trim());

							col = 24;
							// Tel 2
							c.getTelefono2().setValue(d[24].trim());

							col = 25;
							// Tel 3
							c.getTelefono3().setValue(d[25].trim());

							col = 26;
							// Celular
							c.getCelular().setValue(d[26].trim());

							col = 27;
							// fax
							c.getFax().setValue(d[27].trim());

							col = 28;
							// mail
							c.getMail().setValue(d[28].trim());

							col = 29;
							// Pais
							c.getIdPais().setValue(Long.parseLong(d[29].trim()));

							col = 30;
							// Provincia
							c.getIdProvincia().setValue(Long.parseLong(d[30].trim()));

							col = 31;
							// Localidad
							c.getIdlocalidad().setValue(Long.parseLong(d[31].trim()));

							col = 32;
							// Calle
							c.getCalle().setValue(d[32].trim());

							col = 33;
							// Numero
							try {
								c.getAltura().setValue(Long.parseLong(d[33].trim()));
							} catch (Exception e) {
								c.getAltura().setValue(0L);
							}

							col = 34;
							// Latitud
							try {
								c.getGeoLatitud().setValue(Double.parseDouble(d[34].trim()));
							} catch (Exception e) {
								c.getGeoLatitud().setValue(0D);
							}

							col = 35;
							// Longitud
							try {
								c.getGeoLongitud().setValue(Double.parseDouble(d[35].trim()));
							} catch (Exception e) {
								c.getGeoLongitud().setValue(0D);
							}

							col = 36;
							// LogoChico
							c.getIdLogoChico().setValue(Long.parseLong(d[36].trim()));

							col = 37;
							// Footer
							c.getIdFooter().setValue(Long.parseLong(d[37].trim()));

							col = 38;
							// Ticket
							c.getIdTicket().setValue(Long.parseLong(d[38].trim()));

							col = 39;
							// ID Cliente Externo
							if ("".equals(d[39].trim())) {
								c.getIdClienteExterno().setValue(null);
							} else {
								c.getIdClienteExterno().setValue(d[39].trim());
							}

						} catch (Exception e) {
							FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error o datos incompletos en el cliente de la linea " + (i + 1)
									+ (col > 0 ? ", columna " + (col + 1) : "") + ".", null));
							return;
						}

						// -----------------------------
						// -----------------------------

						Usuario u = new Usuario();

						// ---------//
						// USUARIO //
						// ---------//

						try {

							col = 0;
							u.getIdMayorista().setValue(this.getUsuario().getIdMayorista());

							String usuario = (d[40].trim());
							if ("".equals(usuario)) {
								FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "El usuario de la linea " + (i + 1) + " esta vacio", null));
								return;
							} else {
								u.getUsuario().setValue(usuario);
							}

							// Estado
							u.getEstado().setValue("A");

							col = 41;
							// Nombre
							u.getNombre().setValue(d[41].trim());

							col = 42;
							// Apellido
							u.getApellido().setValue(d[42].trim());

							col = 43;
							// Clave
							u.getClave().setValue(d[43].trim());

							col = 44;
							// Tipo Doc
							u.getIdTipoDocumento().setValue(Long.parseLong(d[44].trim()));

							col = 45;
							// Nro Doc
							try {
								u.getNumeroDocumento().setValue(Integer.parseInt(d[45].trim()));
							} catch (Exception e) {
								u.getNumeroDocumento().setValue(0);
							}

							col = 46;
							// Telefono
							u.getTelefono().setValue(d[46].trim());

							col = 47;
							// Celular
							u.getCelular().setValue(d[47].trim());

							col = 48;
							// Mail
							u.getMail().setValue(d[48].trim());

							col = 49;
							// Solici. Coordenadas
							u.getUsarTarjetaCoordenadas().setValue(
									(Integer.parseInt(d[49].trim()) == 1) ? Boolean.TRUE : Boolean.FALSE);

							col = 50;
							// Solo Informes
							u.getSoloInformes().setValue((Integer.parseInt(d[50].trim()) == 1) ? Boolean.TRUE : Boolean.FALSE);

						} catch (Exception e) {
							FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error o datos incompletos en el Usuario de la linea " + (i + 1)
									+ (col > 0 ? ", columna " + (col + 1) : "") + ".", null));
							return;
						}

						// -----------------------------
						// -----------------------------

						// Agrego el Cliente y Usuario en las listas correspondientes
						lc.add(c);
						lu.add(u);
					}

				} else {
					// Se ignora el Cliente de la Linea i
				}

			}

			// Verifico si hay usuarios repetidos

			List<String> ls = new ArrayList<String>();

			for (Iterator iterator = lu.iterator(); iterator.hasNext();) {
				Usuario u = (Usuario) iterator.next();
				ls.add(u.getUsuario().getValue());
			}

			Boolean hayUsuariosRepetidos = new HashSet<String>(ls).size() != ls.size();

			if (hayUsuariosRepetidos) {
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "En el listado existen usuarios repetidos", null));
				return;
			}

			// Fin Verifico si hay usuarios repetidos

			// Verifico si alguno de los usuarios existe ya cargados en la BD

			Boolean usuExistente = false;

			Integer i = 0;
			for (Iterator iterator = lu.iterator(); iterator.hasNext();) {

				i++;

				Usuario u = (Usuario) iterator.next();

				Boolean e = GestionServiceHelper.getGestionService(CfgTimeout.CONSULTA).existeUsuario(this.getUsuario().getIdMayorista(),
						u.getUsuario().getValue());

				if (e) {

					FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, 
							"El usuario: " + u.getUsuario() + ", de la linea " + i + ", ya existe en la BD", null));

					usuExistente = true;
					break;

				}

			}

			if (usuExistente) {
				return;
			}

			// --------------

			// Verifico si hay idClienteExterno repetidos

			List<String> lcw = new ArrayList<String>();

			for (Iterator iterator = lc.iterator(); iterator.hasNext();) {
				Cliente c = (Cliente) iterator.next();

				if (c.getIdClienteExterno() == null || "".equals(c.getIdClienteExterno().getValue().trim())) {
					// No lo agrego
				} else {
					lcw.add(c.getIdClienteExterno().getValue());
				}
			}

			Boolean hayIdClienteExternoRepetidos = new HashSet<String>(lcw).size() != lcw.size();

			if (hayIdClienteExternoRepetidos) {
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "En el listado existen IdClienteExterno repetidos", null));
				return;
			}

			// Fin Verifico si hay idClienteExterno repetidos

			// Verifico si alguno de los idClienteExterno existe ya cargados en la BD

			Boolean idClienteExternoExistente = false;

			Integer j = 0;
			for (Iterator iterator = lc.iterator(); iterator.hasNext();) {

				j++;

				Cliente c = (Cliente) iterator.next();

				if (c.getIdClienteExterno() == null || "".equals(c.getIdClienteExterno().getValue().trim())) {
					// No lo verifico
				} else {

					Boolean e = GestionServiceHelper.getGestionService(CfgTimeout.REPORTE).existeIdClienteExterno(this.getUsuario().getIdMayorista(),
							c.getIdClienteExterno().getValue());

					if (e) {

						FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "El idClienteExterno: " + c.getIdClienteExterno() + ", de la linea "
								+ j + ", ya existe en la BD", null));

						idClienteExternoExistente = true;
						break;

					}
				}

			}

			if (idClienteExternoExistente) {
				return;
			}

			// --------------

			// Prevalido la carga de Clientes y Usuarios
			Integer ii = 0;
			Integer ij = 0;

			Boolean errorEnPreValid = false;

			Integer cantClientesAcargar = lc.size();

			try {

				for (Iterator iterator = lc.iterator(); iterator.hasNext();) {
					Cliente c = (Cliente) iterator.next();

					// public ResultadoBase agregarModificarCliente(Cliente cli, Boolean
					// altaProdsConComisionCero, Boolean soloValidacion) {
					RespIdString rc = GestionServiceHelper.getGestionService(CfgTimeout.REPORTE).agregarModificarCliente(c, lac0.get(ii),
							true, this.getUsuario().getIdMayorista(), this.getUsuario().getIdUsuario(), this.getUsuario().getPassword());

					if (rc.getError().getValue().getHayError().getValue()) {
						FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error en cliente: " + c.getRazonSocial() + ". "
								+ rc.getError().getValue().getMsgError().getValue() + ". " + "Revisar la linea " + (ii + 1), null));
						errorEnPreValid = true;
						break;
					}

					// public ResultadoBase agregarModificarUsuario(Long idMayorista, Long
					// idClienteQueRealizoCambio, Long idUsuarioQueRealizoCambio, String
					// usuarioQueRealizoCambio, Usuario usr, Boolean soloValidacion
					ResultadoBase ru = GestionServiceHelper.getGestionService(CfgTimeout.REPORTE).agregarModificarUsuario(
							this.getUsuario().getIdMayorista(), this.getUsuario().getIdCliente(), this.getUsuario().getIdUsuario(), this.getUsuario().getUsername(),
							lu.get(ii), true, false);

					if (ru.getError().getValue().getHayError().getValue()) {
						FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error en Usuario: " + lu.get(ii).getUsuario() + ". "
								+ ru.getError().getValue().getMsgError().getValue() + ". " + "Revisar la linea " + (ii + 1), null));
						errorEnPreValid = true;
						break;
					}

					ii++;

				}

				if (!errorEnPreValid) {

					ij = 0;

					// Hago la carga de Clientes y Usuarios

					for (Iterator iterator = lc.iterator(); iterator.hasNext();) {
						Cliente c = (Cliente) iterator.next();

						// public ResultadoBase agregarModificarCliente(Cliente cli, Boolean
						// altaProdsConComisionCero, Boolean soloValidacion) {
						RespIdString rc = GestionServiceHelper.getGestionService(CfgTimeout.REPORTE).agregarModificarCliente(c,
								lac0.get(ij), false, this.getUsuario().getIdMayorista(), this.getUsuario().getIdUsuario(), this.getUsuario().getPassword());

						if (rc.getError().getValue().getHayError().getValue()) {
							FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error en cliente: " + c.getRazonSocial() + ". "
									+ rc.getError().getValue().getMsgError().getValue() + ". " + "Revisar la linea " + (ij + 1), null));
							errorEnPreValid = true;
							break;
						} else {
							// Cliente dado de alta ok
							Long idCliente = rc.getId().getValue();

							// Asigno el idCliente al usuario que tengo que cargar
							lu.get(ij).getIdCliente().setValue(idCliente);
						}

						// public ResultadoBase agregarModificarUsuario(Long idMayorista, Long
						// idClienteQueRealizoCambio, Long idUsuarioQueRealizoCambio, String
						// usuarioQueRealizoCambio, Usuario usr, Boolean soloValidacion
						ResultadoBase ru = GestionServiceHelper.getGestionService(CfgTimeout.REPORTE).agregarModificarUsuario(
								this.getUsuario().getIdMayorista(), this.getUsuario().getIdCliente(), this.getUsuario().getIdUsuario(), this.getUsuario().getUsername(),
								lu.get(ij), false, false);

						if (ru.getError().getValue().getHayError().getValue()) {
							FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error en Usuario: " + lu.get(ij).getUsuario() + ". "
									+ ru.getError().getValue().getMsgError().getValue() + ". " + "Revisar la linea " + (ij + 1), null));
							errorEnPreValid = true;
							break;
						}

						ij++;

					}

				}
			} catch (WebServiceException ste) {
				if (ste.getCause().getClass().equals(SocketTimeoutException.class)) {
					FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "No se pudo establecer la comunicación con GESTION.\n Por favor intente nuevamente.", null));
				}else {
					FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error realizando consulta contra GESTION: " + ste.getMessage(), null));
				}
			} catch (Exception e) {
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error ejecutando la Carga Masiva de Clientes: " + e.getMessage(), null));
			}

			// Reseteo la variables clientes para que se limpie la inputTextarea
			if (cantClientesAcargar.equals(ij)) {

				// Se cargadon todos los clientes bien. Limpio la lista
				clientes = "";

				idDistribuidorSuperior = null;

				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Alta Masiva de Clientes realizada exitosamente", null));

			}
		} catch (WebServiceException ste) {
			if (ste.getCause().getClass().equals(SocketTimeoutException.class)) {
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "No se pudo establecer la comunicación con GESTION.\n Por favor intente nuevamente.", null));
			}else {
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error realizando consulta contra GESTION: " + ste.getMessage(), null));
			}
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error ejecutando el WS de Carga Masiva de Clientes: |" + e.getMessage() + "|", null));
		}
		return;
	}

	public void actualizarProductos(ActionEvent evt) {
		// No hace falta hacer nada
	}

	public String getClientes() {
		return clientes;
	}

	public void setClientes(String clientes) {
		this.clientes = clientes;
	}

	public Long getIdDistribuidorSuperior() {
		return idDistribuidorSuperior;
	}

	public void setIdDistribuidorSuperior(Long idDistribuidorSuperior) {
		this.idDistribuidorSuperior = idDistribuidorSuperior;
	}

}
