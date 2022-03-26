package com.americacg.cargavirtual.sube.model;

import java.io.FileWriter;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.americacg.cargavirtual.web.helpers.LogACGHelper;
import com.americacg.cargavirtual.sube.model.parametros.SeparadorTrama;

public class GatewayHelper {
	public static String[] splitData(Long idProceso, boolean debug,
			char separadorCampo, String data) {
		String[] campo = data.split(Character.toString(separadorCampo)); // chr(3)

		if (debug) {
			LogACGHelper.escribirLog(idProceso,
					"Cantidad de Campos de la trama \"" + data + "\" : |"
							+ campo.length + "|");
				
		}

		int j = 0;
		while (j < campo.length) {
			campo[j] = campo[j].trim();
			if (debug) {
				LogACGHelper.escribirLog(idProceso, "Campo (" + j + "): |"
						+ campo[j] + "|");
			}
			j++;
		}

		return campo;
	}

	public static String truncar(String valor, Integer largo) {

		String ret = "";

		if ((valor != null) && (largo != null)) {
			if (valor.length() > largo) {
				ret = valor.substring(0, largo);
			} else {
				ret = valor;
			}
		}
		return ret;
	}

	public static String calcularHashSecurity(Long idProceso,
			SeparadorTrama separadorTrama, String claveParaCalcularHash,
			String strOrigen, String hashSecurity) {

		// ----------------------------------------//
		// CALCULO HASH - VALIDACION SHA1 //
		// ----------------------------------------//
		String leidoDelSocketW = "";
		String hash = "";

		// Al string original que leo del socket le reemplazo el Hash del campo
		// hashSecurity por un punto
		// ,le concateno la clave para poder calcular el hash al final
		// y lo guardo en leidoDelSocketW

		try {
			leidoDelSocketW = strOrigen.replace(
					Character
							.toString(separadorTrama.getInicioTrama())
							.concat(hashSecurity)
							.concat(Character.toString(separadorTrama
									.getSeparadorTrama())),
					Character
							.toString(separadorTrama.getInicioTrama())
							.concat(".")
							.concat(Character.toString(separadorTrama
									.getSeparadorTrama())));

			leidoDelSocketW = leidoDelSocketW.concat(claveParaCalcularHash);

			hash = generarHash(idProceso, leidoDelSocketW);
		} catch (Exception e) {
			LogACGHelper.escribirLog(
							idProceso,
							"Error calculando el HashBinario: |"
									+ e.getMessage() + "|");
		}

		return hash.toUpperCase();
	}

	public static String calcularHashSecurity(Long idProceso,
			String claveParaCalcularHash,
			String strOrigen) throws Exception {

		// ----------------------------------------//
		// CALCULO HASH - VALIDACION SHA1 //
		// ----------------------------------------//
		String leidoDelSocketW = "";
		String hash = "";

		if(claveParaCalcularHash == null)
			throw new Exception("Clave para hash seguridad core no configurada.");
		
		try {
			leidoDelSocketW = strOrigen.concat(claveParaCalcularHash);

			hash = generarHash(idProceso, leidoDelSocketW);
		} catch (Exception e) {
			LogACGHelper.escribirLog(
							idProceso,
							"Error calculando el HashBinario: |"
									+ e.getMessage() + "|");
		}

		return hash.toUpperCase();
	}
	
	public static String calcularHashSecurityBinario(Long idProceso, String claveParaCalcularHash, String strToCalcHash) {

		// ----------------------------------------//
		// CALCULO HASH BINARIO - VALIDACION SHA1 //
		// ----------------------------------------//

		String hashBinario = "";
		String hash = "";

		try {
			strToCalcHash = strToCalcHash.concat(claveParaCalcularHash);
			
			hash = generarHash(idProceso, strToCalcHash);
			
			// Convertir hash a Ascii
			Integer w = 0;
			String hashAscci = "";
			for (int i = 0; i < (hash.length() / 2); i++) {
				int j = Integer.parseInt(hash.substring(w, w + 2), 16);
				char c = (char) j;
				hashAscci = hashAscci + c;
				w = w + 2;
			}

			LogACGHelper.escribirLog(
					idProceso,
					"Hash Convertido a Ascii: |"
							+ hashAscci + "|");


			// Convertir hashAscii a Binario

			String n = "";
			int x = 0;
			String wb;
			for (int i = 0; i < hashAscci.length(); i++) {
				x = hashAscci.charAt(i);
				n = Integer.toBinaryString(x);
				wb = "00000000".concat(n);
				wb = wb.substring(wb.length() - 8, wb.length());

				hashBinario = hashBinario + wb;
			}

			LogACGHelper.escribirLog(
					idProceso,
					"Hash Convertido a Binario: |"
							+ hashBinario + "|");
							
		} catch (Exception e) {
			LogACGHelper.escribirLog(
					idProceso,
					"Error calculando el HashBinario: |"
							+ e.getMessage() + "|");

			hashBinario = "";
		}

		return hashBinario;
	}

	
	private static String generarHash(Long idProceso, String strOrigen){

		// ----------------------------------------//
		// CALCULO HASH - VALIDACION SHA1 //
		// ----------------------------------------//
		MessageDigest md;
		byte[] buffer, digest;
		String hash = "";

		try {
			LogACGHelper.escribirLog(idProceso, "Calculo Hash en Hexa de: |"
					+ strOrigen + "|");

			buffer = strOrigen.getBytes();
			md = MessageDigest.getInstance("SHA1");
			md.update(buffer);
			digest = md.digest();

			for (byte aux : digest) {
				int b = aux & 0xff;
				if (Integer.toHexString(b).length() == 1) {
					hash += "0";
				}
				hash += Integer.toHexString(b);
			}
			LogACGHelper.escribirLog(idProceso, "Hash Generado en Hexa: |"
					+ hash + "|");

		} catch (Exception e) {
			LogACGHelper.escribirLog(
							idProceso,
							"Error calculando el HashBinario: |"
									+ e.getMessage() + "|");
		}

		return hash.toUpperCase();		
	}
	
	public static String convertHexToString(String hex) {

		StringBuilder sb = new StringBuilder();
		StringBuilder temp = new StringBuilder();

		// 49204c6f7665204a617661 split into two characters 49, 20, 4c...
		for (int i = 0; i < hex.length() - 1; i += 2) {

			// grab the hex in pairs
			String output = hex.substring(i, (i + 2));
			// convert hex to decimal
			int decimal = Integer.parseInt(output, 16);
			// convert the decimal to character
			sb.append((char) decimal);

			temp.append(decimal);

		}

		return sb.toString();
	}

	public static String convertHexToStringNumber(String hex) {

		StringBuilder sb = new StringBuilder();

		for (int i = 0; i < hex.length() - 1; i += 2) {

			// grab the hex in pairs
			String output = hex.substring(i, (i + 2));
			// convert hex to decimal
			int decimal = Integer.parseInt(output, 16);
			// convert the decimal to character
			sb.append(decimal);
		}

		return sb.toString();
	}

	public static byte[] hexStringToByteArray(String s) {
		int len = s.length();
		byte[] data = new byte[len / 2];
		for (int i = 0; i < len; i += 2) {
			data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4) + Character
					.digit(s.charAt(i + 1), 16));
		}
		return data;
	}

	public static String strIntToStrHex(String valToHex,
			Integer longitudRespuesta) {
		int nro = Integer.parseInt(valToHex);
		// Genera relleno de n caracteres
		String relleno = new String(new char[longitudRespuesta]).replace("\0",
				"0");
		String valHexa = Integer.toHexString(nro);

		String valHexaRet = relleno.concat(valHexa);
		valHexaRet = valHexaRet.substring(valHexaRet.length()
				- longitudRespuesta, valHexaRet.length());
		return valHexaRet;
	}

	public static String strBigIntToStrHex(String valtoHex,
			boolean paddingZeroes, Integer padding, boolean upperCase) {
		BigInteger toHex = new BigInteger(valtoHex);

		if (paddingZeroes)
			if (upperCase)
				return GatewayHelper.paddingZeroes(toHex.toString(16), padding)
						.toUpperCase();
			else
				return GatewayHelper.paddingZeroes(toHex.toString(16), padding);
		else if (upperCase)
			return toHex.toString(16).toUpperCase();
		else
			return toHex.toString(16);
	}

	public static String strHexToLittleEndian(String strHex, Integer size){
		StringBuilder resultado = new StringBuilder();
		String arrIn[];
		
		if(strHex.length() < size){
			strHex = GatewayHelper.paddingZeroes(strHex, size);
		}
		
		arrIn = stringToArray(strHex);
		
		for(Integer ind=strHex.length()-1; ind >= 0; ind=ind-2){
			resultado.append(arrIn[ind-1].concat(arrIn[ind]));
		}
		
		return resultado.toString();
	}
	
	public static String[] stringToArray(String s) {
	    String[] arr = new String[s.length()];
	    for(int i = 0; i < s.length(); i++)
	    {
	        arr[i] = String.valueOf(s.charAt(i));
	    }
	    return arr;
	}
	
	public static String generaIdentificacionTerminalSUBE(Long idMayorista,
			Long idCliente) {
		String mayoristaStr = "00".concat(idMayorista.toString());
		mayoristaStr = mayoristaStr.substring(mayoristaStr.length() - 2,
				mayoristaStr.length());

		String idClienteStr = "0000000".concat(idCliente.toString());
		idClienteStr = idClienteStr.substring(idClienteStr.length() - 7,
				idClienteStr.length());

		String valToHex = mayoristaStr.concat(idClienteStr);

		return strIntToStrHex(valToHex, 8);
	}

	public static String paddingZeroes(Long id, Integer longitud) {
		return paddingZeroesInt(id.toString(), longitud);
	}

	public static String paddingZeroes(String id, Integer longitud) {
		return paddingZeroesInt(id, longitud);
	}

	private static String paddingZeroesInt(String data, Integer longitud) {
		String relleno = new String(new char[longitud]).replace("\0", "0");
		String idPadding = relleno.concat(data);
		idPadding = idPadding.substring(idPadding.length() - longitud,
				idPadding.length());

		return idPadding;
	}
	
	public static String convertirCadenaAsciiToHexa(String cadenaASCII) throws Exception {
		return stringAsciiToHexa(cadenaASCII.getBytes("ISO-8859-1")).toUpperCase();	
	}

	public static String convertirCadenaAsciiToHexa(byte[] buffer) {
		return stringAsciiToHexa(buffer);
	}
	
	private static String stringAsciiToHexa(byte[] cadenaBytes){
		String cadenaHexa = "";

		for (byte aux : cadenaBytes) {
			int b = aux & 0xff;
			if (Integer.toHexString(b).length() == 1) {
				cadenaHexa += "0";
			}
			cadenaHexa += Integer.toHexString(b);
		}

		// System.out.println("cadenaHexa: |" + cadenaHexa + "|");

		return cadenaHexa;
	}
	
}
