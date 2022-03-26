package com.americacg.cargavirtual.web.helpers;

import java.io.InputStream;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;



public class MayoristasHelper {

	private static MayoristasHelper mayoristasHelper;
	JSONArray jsonSitiosProduccion = null;
	JSONArray jsonSitiosTest = null;

	// El constructor es privado, no permite que se genere un constructor por
	// defecto.
	private MayoristasHelper() {
		if (jsonSitiosProduccion == null) {
			String resourceName = "/cfgWebMayoristas.json";
			InputStream is = MayoristasHelper.class.getResourceAsStream(resourceName);
			if (is == null) {
				throw new NullPointerException("Cannot find resource file " + resourceName);
			}

			JSONTokener tokener = new JSONTokener(is);
			JSONObject object = new JSONObject(tokener);
			jsonSitiosProduccion = object.getJSONArray("sitiosProduccion");
			
			jsonSitiosTest = object.getJSONArray("sitiosTest");

		}
	}

	public static MayoristasHelper getSingletonInstance() {
		if (mayoristasHelper == null) {
			mayoristasHelper = new MayoristasHelper();
		}

		return mayoristasHelper;
	}

	// metodos getter y setter

	public JSONObject obtenerMayorista(String url, String path) {
		
		//TODO: BORRAR ESTAS LINEAS DE DEBUG
		//System.out.println (10 /0) ;
		
		//url = "recarga.mcm.com.ar";
		
		for (int i = 0; i < jsonSitiosTest.length(); i++) {
			JSONObject oJSON = (JSONObject) jsonSitiosTest.get(i);
			if(url.equals(oJSON.getString("url"))){
				if ("S".equals(oJSON.getString("validaPath"))) {
					if (path.equals(oJSON.getString("path"))) {
						return oJSON;
					}
				}else{
					return oJSON;
				}
			}
		}
		
		for (int i = 0; i < jsonSitiosProduccion.length(); i++) {
			JSONObject oJSON = (JSONObject) jsonSitiosProduccion.get(i);
			if(url.equals(oJSON.getString("url"))){
				if ("S".equals(oJSON.getString("validaPath"))) {
					if (path.equals(oJSON.getString("path"))) {
						return oJSON;
					}
				}else{
					return oJSON;
				}
			}
		}

		// TODO: no existe web
		return null;

	}

}
