package com.americacg.cargavirtual.web.helpers;

import java.io.FileWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.stereotype.Component;

@Component
public class LogACGHelper {
	private static String pathLog;
	private static String pathLogWin;
	
	public LogACGHelper(){};

	public static String getPathLog() {
		return pathLog;
	}

	public static void setPathLog(String pathLog) {
		LogACGHelper.pathLog = pathLog;
	}

	public static String getPathLogWin() {
		return pathLogWin;
	}

	public static void setPathLogWin(String pathLogWin) {
		LogACGHelper.pathLogWin = pathLogWin;
	}

	public static Boolean escribirLog(Long idProc, String datos, String log) {
		return escribirLogInterno(idProc, datos, log);
	}
	
	public static Boolean escribirLog(Long idProc, String datos) {
		return escribirLogInterno(idProc, datos, "");
	}
	
	private static Boolean escribirLogInterno(Long idProc, String datos, String log) {

		try {
			String logFileName="";
			
			// Formato de fecha para el log
			SimpleDateFormat formato = new SimpleDateFormat(
					"yyyy-MM-dd HH:mm:ss.SSS");
			SimpleDateFormat formatoNL = new SimpleDateFormat("yyyy-MM-dd_HH");
			String fecha = formato.format(new Date()).toString();
			String fechaNL = formatoNL.format(new Date()).toString();

			// Armo string de datos que voy a loguear

			if (idProc == null) {
				datos = fecha + "|[C]|" + datos;
			} else {
				datos = fecha + "|[" + idProc.toString() + "]|" + datos;
			}

			// Replace de enter o nueva linea y retorno de carro por espacios.
			datos = datos.replace("\r", " ");
			datos = datos.replace("\n", " ");
			datos = datos.replace("\r\n", " ");
			datos = datos.replace("\n\r", " ");

			// Escribo en disco

			try {
				if(!log.isEmpty()){
					//--- Si el parametros log viene informado, se va modificando el nombre
					//--- del archivo fisico de acuerdo a ese valor.
					//--- por el momento se define el parametro B1 --> Batch1
					if("B1".equals(log.trim())){
						logFileName="_logWeb2.txt";
					}
				}
				else{
					// Si el parametro no viene informado, corresponde al log Online del gateway. Nombre único.
					logFileName = "_logWeb2.txt";
				}

				FileWriter escritor = new FileWriter(LogACGHelper.getPathLog() + fechaNL
						+ logFileName, true);
				escritor.write(datos + "\n");
				escritor.flush();
				escritor.close();

			} catch (Exception e) {
				// Muestro el string por pantalla (Solo cuando estoy en Windows)
				System.out.println(datos);

				FileWriter escritor = new FileWriter(LogACGHelper.getPathLogWin()
						+ fechaNL + logFileName, true);
				escritor.write(datos + "\n");
				escritor.flush();
				escritor.close();
			}

			return true;

		} catch (Exception e) {
			System.out
					.println("Error a escribir en fichero de Logs de Carga Virtual Web 2: |"
							+ e.getMessage() + "|" );
			return false;
		}
	}

}
