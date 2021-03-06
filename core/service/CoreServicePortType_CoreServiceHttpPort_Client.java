
package com.americacg.cargavirtual.core.service;

/**
 * Please modify this class to meet your needs
 * This class is not complete
 */

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import javax.xml.namespace.QName;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.ws.RequestWrapper;
import javax.xml.ws.ResponseWrapper;

/**
 * This class was generated by Apache CXF 2.4.2
 * 2020-03-02T12:04:43.119-03:00
 * Generated source version: 2.4.2
 * 
 */
public final class CoreServicePortType_CoreServiceHttpPort_Client {

    private static final QName SERVICE_NAME = new QName("http://service.core.cargavirtual.americacg.com", "CoreService");

    private CoreServicePortType_CoreServiceHttpPort_Client() {
    }

    public static void main(String args[]) throws java.lang.Exception {
        URL wsdlURL = CoreService.WSDL_LOCATION;
        if (args.length > 0 && args[0] != null && !"".equals(args[0])) { 
            File wsdlFile = new File(args[0]);
            try {
                if (wsdlFile.exists()) {
                    wsdlURL = wsdlFile.toURI().toURL();
                } else {
                    wsdlURL = new URL(args[0]);
                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
        }
      
        CoreService ss = new CoreService(wsdlURL, SERVICE_NAME);
        CoreServicePortType port = ss.getCoreServiceHttpPort();  
        
        {
        System.out.println("Invoking ajustarTransaccion...");
        java.lang.Long _ajustarTransaccion_in0 = null;
        javax.xml.datatype.XMLGregorianCalendar _ajustarTransaccion_in1 = null;
        java.lang.Long _ajustarTransaccion_in2 = null;
        java.lang.Long _ajustarTransaccion_in3 = null;
        java.lang.Float _ajustarTransaccion_in4 = null;
        java.lang.Float _ajustarTransaccion_in5 = null;
        java.lang.String _ajustarTransaccion_in6 = "";
        com.americacg.cargavirtual.core.model.Ticket _ajustarTransaccion__return = port.ajustarTransaccion(_ajustarTransaccion_in0, _ajustarTransaccion_in1, _ajustarTransaccion_in2, _ajustarTransaccion_in3, _ajustarTransaccion_in4, _ajustarTransaccion_in5, _ajustarTransaccion_in6);
        System.out.println("ajustarTransaccion.result=" + _ajustarTransaccion__return);


        }
        {
        System.out.println("Invoking prueba...");
        java.lang.Long _prueba_in0 = null;
        com.americacg.cargavirtual.core.model.Prueba _prueba__return = port.prueba(_prueba_in0);
        System.out.println("prueba.result=" + _prueba__return);


        }
        {
        System.out.println("Invoking enviarConfirmacion...");
        java.lang.Long _enviarConfirmacion_in0 = null;
        java.lang.Long _enviarConfirmacion_in1 = null;
        java.lang.Long _enviarConfirmacion_in2 = null;
        java.lang.String _enviarConfirmacion_in3 = "";
        javax.xml.datatype.XMLGregorianCalendar _enviarConfirmacion_in4 = null;
        javax.xml.datatype.XMLGregorianCalendar _enviarConfirmacion_in5 = null;
        java.lang.Long _enviarConfirmacion_in6 = null;
        java.lang.Long _enviarConfirmacion_in7 = null;
        java.lang.Float _enviarConfirmacion_in8 = null;
        java.lang.Integer _enviarConfirmacion_in9 = null;
        java.lang.Integer _enviarConfirmacion_in10 = null;
        java.lang.String _enviarConfirmacion_in11 = "";
        java.lang.Boolean _enviarConfirmacion__return = port.enviarConfirmacion(_enviarConfirmacion_in0, _enviarConfirmacion_in1, _enviarConfirmacion_in2, _enviarConfirmacion_in3, _enviarConfirmacion_in4, _enviarConfirmacion_in5, _enviarConfirmacion_in6, _enviarConfirmacion_in7, _enviarConfirmacion_in8, _enviarConfirmacion_in9, _enviarConfirmacion_in10, _enviarConfirmacion_in11);
        System.out.println("enviarConfirmacion.result=" + _enviarConfirmacion__return);


        }
        {
        System.out.println("Invoking estadoCore...");
        java.lang.String _estadoCore_in0 = "";
        java.lang.String _estadoCore_in1 = "";
        com.americacg.cargavirtual.core.model.ArrayOfEstado _estadoCore__return = port.estadoCore(_estadoCore_in0, _estadoCore_in1);
        System.out.println("estadoCore.result=" + _estadoCore__return);


        }
        {
        System.out.println("Invoking apagar...");
        java.lang.String _apagar_in0 = "";
        java.lang.String _apagar_in1 = "";
        java.lang.String _apagar_in2 = "";
        com.americacg.cargavirtual.core.model.RespApagado _apagar__return = port.apagar(_apagar_in0, _apagar_in1, _apagar_in2);
        System.out.println("apagar.result=" + _apagar__return);


        }
        {
        System.out.println("Invoking realizarVenta...");
        java.lang.Long _realizarVenta_in0 = null;
        java.lang.Long _realizarVenta_in1 = null;
        java.lang.String _realizarVenta_in2 = "";
        java.lang.String _realizarVenta_in3 = "";
        javax.xml.datatype.XMLGregorianCalendar _realizarVenta_in4 = null;
        java.lang.String _realizarVenta_in5 = "";
        java.lang.Long _realizarVenta_in6 = null;
        java.lang.Float _realizarVenta_in7 = null;
        java.lang.Integer _realizarVenta_in8 = null;
        java.lang.Integer _realizarVenta_in9 = null;
        java.lang.String _realizarVenta_in10 = "";
        java.lang.String _realizarVenta_in11 = "";
        java.lang.String _realizarVenta_in12 = "";
        com.americacg.cargavirtual.core.model.Ticket _realizarVenta__return = port.realizarVenta(_realizarVenta_in0, _realizarVenta_in1, _realizarVenta_in2, _realizarVenta_in3, _realizarVenta_in4, _realizarVenta_in5, _realizarVenta_in6, _realizarVenta_in7, _realizarVenta_in8, _realizarVenta_in9, _realizarVenta_in10, _realizarVenta_in11, _realizarVenta_in12);
        System.out.println("realizarVenta.result=" + _realizarVenta__return);


        }
        {
        System.out.println("Invoking consultarSaldo...");
        java.lang.Long _consultarSaldo_in0 = null;
        java.lang.Long _consultarSaldo_in1 = null;
        java.lang.Long _consultarSaldo_in2 = null;
        java.lang.String _consultarSaldo_in3 = "";
        javax.xml.datatype.XMLGregorianCalendar _consultarSaldo_in4 = null;
        java.lang.String _consultarSaldo_in5 = "";
        java.lang.Long _consultarSaldo_in6 = null;
        java.lang.Long _consultarSaldo_in7 = null;
        java.lang.String _consultarSaldo_in8 = "";
        com.americacg.cargavirtual.core.model.Ticket _consultarSaldo__return = port.consultarSaldo(_consultarSaldo_in0, _consultarSaldo_in1, _consultarSaldo_in2, _consultarSaldo_in3, _consultarSaldo_in4, _consultarSaldo_in5, _consultarSaldo_in6, _consultarSaldo_in7, _consultarSaldo_in8);
        System.out.println("consultarSaldo.result=" + _consultarSaldo__return);


        }
        {
        System.out.println("Invoking anularTransaccion...");
        java.lang.Long _anularTransaccion_in0 = null;
        java.lang.Long _anularTransaccion_in1 = null;
        java.lang.Long _anularTransaccion_in2 = null;
        java.lang.String _anularTransaccion_in3 = "";
        javax.xml.datatype.XMLGregorianCalendar _anularTransaccion_in4 = null;
        java.lang.Long _anularTransaccion_in5 = null;
        java.lang.String _anularTransaccion_in6 = "";
        java.lang.String _anularTransaccion_in7 = "";
        java.lang.Float _anularTransaccion_in8 = null;
        java.lang.Integer _anularTransaccion_in9 = null;
        java.lang.Integer _anularTransaccion_in10 = null;
        com.americacg.cargavirtual.core.model.Ticket _anularTransaccion__return = port.anularTransaccion(_anularTransaccion_in0, _anularTransaccion_in1, _anularTransaccion_in2, _anularTransaccion_in3, _anularTransaccion_in4, _anularTransaccion_in5, _anularTransaccion_in6, _anularTransaccion_in7, _anularTransaccion_in8, _anularTransaccion_in9, _anularTransaccion_in10);
        System.out.println("anularTransaccion.result=" + _anularTransaccion__return);


        }
        {
        System.out.println("Invoking resetearHashConectores...");
        java.lang.String _resetearHashConectores_in0 = "";
        java.lang.String _resetearHashConectores_in1 = "";
        java.lang.String _resetearHashConectores__return = port.resetearHashConectores(_resetearHashConectores_in0, _resetearHashConectores_in1);
        System.out.println("resetearHashConectores.result=" + _resetearHashConectores__return);


        }
        {
        System.out.println("Invoking consultarTransaccion...");
        java.lang.Long _consultarTransaccion_in0 = null;
        java.lang.Long _consultarTransaccion_in1 = null;
        java.lang.Long _consultarTransaccion_in2 = null;
        java.lang.String _consultarTransaccion_in3 = "";
        javax.xml.datatype.XMLGregorianCalendar _consultarTransaccion_in4 = null;
        java.lang.Long _consultarTransaccion_in5 = null;
        java.lang.String _consultarTransaccion_in6 = "";
        java.lang.Long _consultarTransaccion_in7 = null;
        java.lang.Float _consultarTransaccion_in8 = null;
        java.lang.Integer _consultarTransaccion_in9 = null;
        java.lang.Integer _consultarTransaccion_in10 = null;
        java.lang.String _consultarTransaccion_in11 = "";
        java.lang.String _consultarTransaccion_in12 = "";
        java.lang.Boolean _consultarTransaccion_in13 = null;
        com.americacg.cargavirtual.core.model.Ticket _consultarTransaccion__return = port.consultarTransaccion(_consultarTransaccion_in0, _consultarTransaccion_in1, _consultarTransaccion_in2, _consultarTransaccion_in3, _consultarTransaccion_in4, _consultarTransaccion_in5, _consultarTransaccion_in6, _consultarTransaccion_in7, _consultarTransaccion_in8, _consultarTransaccion_in9, _consultarTransaccion_in10, _consultarTransaccion_in11, _consultarTransaccion_in12, _consultarTransaccion_in13);
        System.out.println("consultarTransaccion.result=" + _consultarTransaccion__return);


        }
        {
        System.out.println("Invoking activar...");
        java.lang.String _activar_in0 = "";
        java.lang.String _activar_in1 = "";
        java.lang.String _activar_in2 = "";
        com.americacg.cargavirtual.core.model.RespApagado _activar__return = port.activar(_activar_in0, _activar_in1, _activar_in2);
        System.out.println("activar.result=" + _activar__return);


        }

        System.exit(0);
    }

}
