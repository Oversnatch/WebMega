package com.americacg.cargavirtual.gateway.pagoElectronico.helper;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

import com.americacg.cargavirtual.gateway.pagoElectronico.enums.AlineacionLineaTicket;
import com.americacg.cargavirtual.gateway.pagoElectronico.enums.TamanoLetraTicket;
import com.americacg.cargavirtual.gateway.pagoElectronico.enums.TipoLetraTicket;
import com.americacg.cargavirtual.gateway.pagoElectronico.enums.TipoLineaTicket;
import com.americacg.cargavirtual.gateway.pagoElectronico.exceptions.ConfiguracionException;
import com.americacg.cargavirtual.gateway.pagoElectronico.model.parametros.SeparadorTrama;
import com.americacg.cargavirtual.gateway.pagoElectronico.model.ticket.LineaTicket;
import com.americacg.cargavirtual.gateway.pagoElectronico.model.ticket.Ticket;
import com.americacg.cargavirtual.web.helpers.LogACGHelper;

public class GatewayHelper {
	protected static final long MilliMuliplier = 1000L;
	protected static final long MicroMuliplier = MilliMuliplier * 1000L;
	protected static final long NanoMuliplier = MicroMuliplier * 1000L;

	// number of seconds passed 1970/1/1 00:00:00 GMT.
	private static long sec0;
	// fraction of seconds passed 1970/1/1 00:00:00 GMT, offset by
	// the base System.nanoTime (nano0), in nanosecond unit.
	private static long nano0;
	
	public static String[] splitData(Long idProceso, boolean debug, char separadorCampo, String data) {
		String[] campo = data.split(Character.toString(separadorCampo), -1); // chr(3)

		if (debug) {
			LogACGHelper.escribirLog(idProceso,
					"Cantidad de Campos de la trama \"" + data + "\" : |" + campo.length + "|");
			
			int j = 0;
			while (j < campo.length) {
				LogACGHelper.escribirLog(idProceso, "Campo (" + j + "): |" + campo[j] + "|");
				
				j++;
			}
		}

		return campo;
	}

	public static String[] splitDataTickets(Long idProceso, boolean debug, char separadorCampo, String data, char charInicioTicket, char charFinTicket) {
		
		String dataAux = data; 
		
		data = dataAux.substring(0 , dataAux.indexOf(Character.toString(charInicioTicket)));

		String[] campo = data.split(Character.toString(separadorCampo), -1); // chr(3)
		
		campo[campo.length - 1 ] = dataAux.substring(dataAux.indexOf(Character.toString(charInicioTicket)), dataAux.indexOf(Character.toString(charFinTicket)));
		
		if (debug) {
			LogACGHelper.escribirLog(idProceso,
					"Cantidad de Campos de la trama \"" + dataAux + "\" : |" + campo.length + "|");
			
			int j = 0;
			while (j < campo.length) {
				LogACGHelper.escribirLog(idProceso, "Campo (" + j + "): |" + campo[j] + "|");
				
				j++;
			}
		}

		return campo;
	}

	public static Ticket splitTicket(Long idProceso, boolean debug, char separadorCampo, String data, char charInicioVector, char charFinVector, char charSeparadorItem) {
		
		Ticket ticket = new Ticket(); 
		String dataTicket = data.substring(data.indexOf(Character.toString(charInicioVector)) + 1, data.indexOf(Character.toString(charFinVector)) - 1);
		
		String[] lineas = dataTicket.split(Character.toString(charSeparadorItem) , -1);
		
		for (String dataLinea : lineas) {
			LineaTicket  lineaTicket = new LineaTicket(); 
			
			String[] linea = dataLinea.split(Character.toString(separadorCampo), -1);
			
			
			switch (linea[0].toUpperCase()) {
			case "H":
				lineaTicket.getConfiguracionLinea().setTipoLinea(TipoLineaTicket.TICKET_HEADER);
				break;
			case "D":
				lineaTicket.getConfiguracionLinea().setTipoLinea(TipoLineaTicket.TICKET_DATA);
				break;
			case "F":
				lineaTicket.getConfiguracionLinea().setTipoLinea(TipoLineaTicket.TICKET_FOOTER);
				break;
			default:
				lineaTicket.getConfiguracionLinea().setTipoLinea(TipoLineaTicket.TICKET_DATA);
				break;
			}
			
			switch (linea[1].toUpperCase()) {
			case "L":
				lineaTicket.getConfiguracionLinea().setAlineacionLinea(AlineacionLineaTicket.ALINEACION_IZQUIERDA);
				break;
			case "C":
				lineaTicket.getConfiguracionLinea().setAlineacionLinea(AlineacionLineaTicket.ALINEACION_CENTRADA);
				break;
			case "R":
				lineaTicket.getConfiguracionLinea().setAlineacionLinea(AlineacionLineaTicket.ALINEACION_DERECHA);
				break;			
			default:
				lineaTicket.getConfiguracionLinea().setAlineacionLinea(AlineacionLineaTicket.ALINEACION_IZQUIERDA);
				break;
			}
			
			switch (linea[2].toUpperCase()) {
			case "N":
				lineaTicket.getConfiguracionLinea().setTipoLetra(TipoLetraTicket.LETRA_NORMAL);
				break;
			case "B":
				lineaTicket.getConfiguracionLinea().setTipoLetra(TipoLetraTicket.LETRA_NEGRITA);
				break;
			case "K":
				lineaTicket.getConfiguracionLinea().setTipoLetra(TipoLetraTicket.LETRA_ITALICA);
				break;			
			case "I":
				lineaTicket.getConfiguracionLinea().setTipoLetra(TipoLetraTicket.LETRA_FONDO_NEGRO);
				break;			
			case "U":
				lineaTicket.getConfiguracionLinea().setTipoLetra(TipoLetraTicket.LETRA_SUBRAYADA);
				break;		
			default:
				lineaTicket.getConfiguracionLinea().setTipoLetra(TipoLetraTicket.LETRA_NORMAL);
				break;
			}
			
			switch (linea[3].toUpperCase()) {
			case "N":
				lineaTicket.getConfiguracionLinea().setTamanoLetra(TamanoLetraTicket.LETRA_CHICA);
				break;
			case "M":
				lineaTicket.getConfiguracionLinea().setTamanoLetra(TamanoLetraTicket.LETRA_MEDIANA);
				break;
			case "G":
				lineaTicket.getConfiguracionLinea().setTamanoLetra(TamanoLetraTicket.LETRA_GRANDE);
				break;			
			default:
				lineaTicket.getConfiguracionLinea().setTamanoLetra(TamanoLetraTicket.LETRA_CHICA);
				break;
			}			

			lineaTicket.setLinea(linea[4]);
			
			ticket.getLineas().add(lineaTicket);	
		}
		
		return ticket;
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

	public static String calcularHashSecurity(Long idProceso, SeparadorTrama separadorTrama,
			String claveParaCalcularHash, String strOrigen, String hashSecurity) {

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
					Character.toString(separadorTrama.getInicioTrama()).concat(hashSecurity)
							.concat(Character.toString(separadorTrama.getSeparadorTrama())),
					Character.toString(separadorTrama.getInicioTrama()).concat(".")
							.concat(Character.toString(separadorTrama.getSeparadorTrama())));

			leidoDelSocketW = leidoDelSocketW.concat(claveParaCalcularHash);

			if ((hashSecurity != null) && (hashSecurity.length() < 40))
				hash = generarHash(idProceso, leidoDelSocketW, true);
			else
				hash = generarHash(idProceso, leidoDelSocketW, false);
		} catch (Exception e) {
			LogACGHelper.escribirLog(idProceso, "Error calculando el HashBinario: |" + e.getMessage() + "|");
		}

		return hash.toUpperCase();
	}

	public static String calcularHashSecurity(Long idProceso, String claveParaCalcularHash, String strOrigen)
			throws Exception {

		// ----------------------------------------//
		// CALCULO HASH - VALIDACION SHA1 //
		// ----------------------------------------//
		String leidoDelSocketW = "";
		String hash = "";

		if (claveParaCalcularHash == null)
			throw new ConfiguracionException("Clave para hash seguridad core no configurada.");

		try {
			leidoDelSocketW = strOrigen.concat(claveParaCalcularHash);

			hash = generarHash(idProceso, leidoDelSocketW, false);
		} catch (Exception e) {
			LogACGHelper.escribirLog(idProceso, "Error calculando el HashBinario: |" + e.getMessage() + "|");
		}

		return hash.toUpperCase();
	}

	public static String calcularHashSecurityBinario(Long idProceso, String claveParaCalcularHash,
			String strToCalcHash) {

		// ----------------------------------------//
		// CALCULO HASH BINARIO - VALIDACION SHA1 //
		// ----------------------------------------//

		String hashBinario = "";
		String hash = "";

		try {
			strToCalcHash = strToCalcHash.concat(claveParaCalcularHash);

			hash = generarHash(idProceso, strToCalcHash, false);

			// Convertir hash a Ascii
			Integer w = 0;
			String hashAscci = "";
			for (int i = 0; i < (hash.length() / 2); i++) {
				int j = Integer.parseInt(hash.substring(w, w + 2), 16);
				char c = (char) j;
				hashAscci = hashAscci + c;
				w = w + 2;
			}

			LogACGHelper.escribirLog(idProceso, "Hash Convertido a Ascii: |" + hashAscci + "|");

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

			LogACGHelper.escribirLog(idProceso, "Hash Convertido a Binario: |" + hashBinario + "|");
		} catch (Exception e) {
			LogACGHelper.escribirLog(idProceso, "Error calculando el HashBinario: |" + e.getMessage() + "|");

			hashBinario = "";
		}

		return hashBinario;
	}

	private static String generarHash(Long idProceso, String strOrigen, boolean hashPropietario) {

		// ----------------------------------------//
		// CALCULO HASH - VALIDACION SHA1 //
		// ----------------------------------------//
		MessageDigest md;
		byte[] buffer, digest;
		String hash = "";
		Integer suma = 0;

		try {
			if (hashPropietario) {
				for (int i = 0; i < strOrigen.length(); i++) {
					suma = suma + ((int) (strOrigen.charAt(i)) * i);
				}

				// A la suma de los char por la posicion, le suma otra clave privada....
				suma = suma + 1018429181;

				hash = suma.toString();
			} else {
				LogACGHelper.escribirLog(idProceso, "Calculo Hash en Hexa de: |" + strOrigen + "|");

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
			}

			LogACGHelper.escribirLog(idProceso, "Hash Generado en Hexa: |" + hash + "|");
		} catch (Exception e) {
			LogACGHelper.escribirLog(idProceso, "Error calculando el HashBinario: |" + e.getMessage() + "|");
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
			data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4) + Character.digit(s.charAt(i + 1), 16));
		}
		return data;
	}

	public static String strIntToStrHex(String valToHex, Integer longitudRespuesta) {
		int nro = Integer.parseInt(valToHex);
		// Genera relleno de n caracteres
		String relleno = new String(new char[longitudRespuesta]).replace("\0", "0");
		String valHexa = Integer.toHexString(nro);

		String valHexaRet = relleno.concat(valHexa);
		valHexaRet = valHexaRet.substring(valHexaRet.length() - longitudRespuesta, valHexaRet.length());
		return valHexaRet;
	}

	public static String strBigIntToStrHex(String valtoHex, boolean paddingZeroes, Integer padding, boolean upperCase) {
		BigInteger toHex = new BigInteger(valtoHex);

		if (paddingZeroes)
			if (upperCase)
				return GatewayHelper.paddingZeroes(toHex.toString(16), padding).toUpperCase();
			else
				return GatewayHelper.paddingZeroes(toHex.toString(16), padding);
		else if (upperCase)
			return toHex.toString(16).toUpperCase();
		else
			return toHex.toString(16);
	}

	public static String strHexToLittleEndian(String strHex, Integer size) {
		StringBuilder resultado = new StringBuilder();
		String arrIn[];

		if (strHex.length() < size) {
			strHex = GatewayHelper.paddingZeroes(strHex, size);
		}

		arrIn = stringToArray(strHex);

		for (Integer ind = strHex.length() - 1; ind >= 0; ind = ind - 2) {
			resultado.append(arrIn[ind - 1].concat(arrIn[ind]));
		}

		return resultado.toString();
	}

	public static String[] stringToArray(String s) {
		String[] arr = new String[s.length()];
		for (int i = 0; i < s.length(); i++) {
			arr[i] = String.valueOf(s.charAt(i));
		}
		return arr;
	}

	public static String generaIdentificacionTerminalPagoElectronico(Long idMayorista, Long idCliente, Long idUsuario) {
		String mayoristaStr = "00".concat(idMayorista.toString());
		mayoristaStr = mayoristaStr.substring(mayoristaStr.length() - 2, mayoristaStr.length());

		String idClienteStr = "0000000".concat(idCliente.toString());
		idClienteStr = idClienteStr.substring(idClienteStr.length() - 7, idClienteStr.length());

		String idUsuarioStr = "0000000".concat(idUsuario.toString());
		idUsuarioStr = idUsuarioStr.substring(idClienteStr.length() - 7, idClienteStr.length());

		
		String valToHex = mayoristaStr.concat(idClienteStr).concat(idUsuarioStr);

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
		idPadding = idPadding.substring(idPadding.length() - longitud, idPadding.length());

		return idPadding;
	}

	public static String convertirCadenaAsciiToHexa(String cadenaASCII) throws Exception {
		return stringAsciiToHexa(cadenaASCII.getBytes("ISO-8859-1")).toUpperCase();
	}

	public static String convertirCadenaAsciiToHexa(byte[] buffer) {
		return stringAsciiToHexa(buffer);
	}

	private static String stringAsciiToHexa(byte[] cadenaBytes) {
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
	
	public static String generaIdTrxCliente(Long idMayorista, Long idCliente) {
		String sFecha = "";
		String sIdMayorista = "";
 		String sIdCliente = "";
 		Integer rand = 0;
 		String sRandom = "";
 		Random random = null;
 		
 		sFecha = new SimpleDateFormat("yyyyMMddHHmmssSS").format(new Date(System.currentTimeMillis()));
 		sIdMayorista = String.format("%02d", idMayorista);
 		sIdCliente = String.format("%06d", idCliente);
 		
 		random = new Random(System.nanoTime());
 		rand = random.nextInt(100000);
 		sRandom = String.format("%05d", rand);
 		
 		return sFecha.concat(sRandom.concat(sIdMayorista.concat(sIdCliente)));
	}
	
	public static Timestamp getNanoPrecisionTimestamp() {
		long nano_delta = nano0 + System.nanoTime();
		long sec1 = sec0 + (nano_delta / NanoMuliplier);
		long nano1 = nano_delta % NanoMuliplier;

		Timestamp rtnTs = new Timestamp(sec1 * MilliMuliplier);
		rtnTs.setNanos((int) nano1);
		return rtnTs;
	}
	
}
