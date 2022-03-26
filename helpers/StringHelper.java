package com.americacg.cargavirtual.web.helpers;

import javax.inject.Named;

import org.apache.commons.lang3.StringUtils;

@Named("stringHelper")
public class StringHelper {
	public static String cutOffStr(String texto, Integer longitud){
		if(texto != null && !texto.isEmpty())
			return StringUtils.abbreviate(texto, longitud);
		else
			return texto;
	}
}
