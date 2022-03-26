package com.americacg.cargavirtual.gateway.pagoElectronico.helper;

import java.security.Key;
import java.util.Base64;
import java.util.Properties;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;

import sun.misc.BASE64Decoder;

public class CryptoHelper {
	private static String algorithm = "AES";
	
	// Performs Encryption
	public static String encrypt(String plainText) throws Exception {
		byte[] dataToCrypt = null;
		byte[] keyValue = null;
		byte[] initVector = null;
		byte[] encVal = null;
		Properties values = null;
		IvParameterSpec iv = null;
		SecretKeySpec skeySpec = null;
		Cipher cipher = null;
		String encryptedValue = "";
		
		values = new Properties();
		values.load(CryptoHelper.class.getClassLoader().getResourceAsStream("gatewayPagoElectronico.properties"));
		
		keyValue=hexStringToByteArray(values.get("pagoelectronico.keyValue").toString().trim());
		initVector=hexStringToByteArray(values.get("pagoelectronico.initVector").toString().trim());
		
		iv = new IvParameterSpec(initVector);
		skeySpec = new SecretKeySpec(keyValue, "AES");
		
		dataToCrypt = plainText.getBytes("UTF-8");

		cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING"); // "AES/CBC/PKCS5PADDING"
		cipher.init(Cipher.ENCRYPT_MODE, skeySpec, iv);
		encVal = cipher.doFinal(dataToCrypt);

		encryptedValue = convertirCadenaAsciiToHexa(encVal).toUpperCase();

		return encryptedValue;
	}

	// Performs decryption
	public static String decrypt(String encryptedText) throws Exception {
		Properties values = null;
		byte[] keyValue = null;
		byte[] initVector = null;
		IvParameterSpec iv = null;
		SecretKeySpec skeySpec = null;
		Cipher cipher = null;
		byte[] decordedValue = null;
		byte[] original = null;
		
		values = new Properties();
		values.load(CryptoHelper.class.getClassLoader().getResourceAsStream("gatewayPagoElectronico.properties"));
		
		keyValue=hexStringToByteArray(values.get("pagoelectronico.keyValue").toString().trim());
		initVector=hexStringToByteArray(values.get("pagoelectronico.initVector").toString().trim());
		
		iv = new IvParameterSpec(initVector);
		skeySpec = new SecretKeySpec(keyValue, "AES");

		cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
		cipher.init(Cipher.DECRYPT_MODE, skeySpec, iv);

		decordedValue = new BASE64Decoder().decodeBuffer(encryptedText);
		original = cipher.doFinal(decordedValue);

		return new String(original);
	}

	public static String decryptHEX(String encryptedText) throws Exception {
		Properties values = null;
		byte[] keyValue = null;
		byte[] initVector = null;
		IvParameterSpec iv = null;
		SecretKeySpec skeySpec = null;
		Cipher cipher = null;
		byte[] decordedValue = null;
		byte[] original = null;
		
		values = new Properties();
		values.load(CryptoHelper.class.getClassLoader().getResourceAsStream("gatewayPagoElectronico.properties"));
		
		keyValue=hexStringToByteArray(values.get("pagoelectronico.keyValue").toString().trim());
		initVector=hexStringToByteArray(values.get("pagoelectronico.initVector").toString().trim());
		
		iv = new IvParameterSpec(initVector);
		skeySpec = new SecretKeySpec(keyValue, "AES");
		
		cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
		cipher.init(Cipher.DECRYPT_MODE, skeySpec, iv);

		decordedValue = hexStringToByteArray(encryptedText);
		original = cipher.doFinal(decordedValue);
		
		return new String(original);
	}

	private static byte[] hexStringToByteArray(String s) {
		return DatatypeConverter.parseHexBinary(s);
	}

	private static byte[] toBytes(int... ints) { // helper function
		byte[] result = new byte[ints.length];
		for (int i = 0; i < ints.length; i++) {
			result[i] = (byte) ints[i];
		}
		return result;
	}

	public static String convertirCadenaAsciiToHexa(String cadenaASCII) throws Exception {
		return stringAsciiToHexa(cadenaASCII.getBytes("ISO-8859-1")).toUpperCase();
	}

	public static String convertirCadenaAsciiToHexa(byte[] buffer) {
		return stringAsciiToHexa(buffer);
	}

	private static String stringAsciiToHexa(byte[] cadenaBytes) {

		StringBuilder cadenaHexa = new StringBuilder("");

		for (byte aux : cadenaBytes) {
			int b = aux & 0xff;
			if (Integer.toHexString(b).length() == 1) {
				cadenaHexa.append("0");
			}
			cadenaHexa.append(Integer.toHexString(b));
		}

		return cadenaHexa.toString();
	}
}
