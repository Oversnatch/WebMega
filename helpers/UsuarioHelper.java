package com.americacg.cargavirtual.web.helpers;

import javax.inject.Named;

import org.springframework.security.core.context.SecurityContextHolder;

import com.americacg.cargavirtual.web.model.Usuario;

@Named("usuarioHelper")
public class UsuarioHelper {
	public static Usuario usuarioSession() {
		return (Usuario) SecurityContextHolder.getContext().getAuthentication().getDetails();
	}
}
