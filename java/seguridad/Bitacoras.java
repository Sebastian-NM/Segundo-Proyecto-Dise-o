/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package seguridad;

/**
 *
 * @author chave
 */
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

import conexiones.ConnectionBD;

import java.sql.Connection;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public  abstract class Bitacoras {
    
    protected String ip;
    protected String pais;
    protected String So;
    protected String fecha;
    private ConnectionBD conexion;
    private  String tipo;
    private  String info;
    private  String autor;

    public abstract String formato(String autor,String info);
    
    protected static String obtenerSistemaOperativo() {
        return System.getProperty("os.name");
    }           

    protected  Connection getBase() {
        conexion = new ConnectionBD ();
        try {
            conexion.conectar();
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(Bitacoras.class.getName()).log(Level.SEVERE, null, ex);
        }
        return conexion.getConexion();
    }
    
    protected void desconectar(){
        conexion.desconectar();
    }
     
    protected static LocalDateTime obtenerFechaHora() {
        // Obtener la fecha y hora actual
        return LocalDateTime.now();
    }
    
    protected static String obtenerUbicacionPorIP() {
        try {
            // Obtener la dirección IP del usuario
            String ipAddress = obtenerDireccionIP();

            // Consultar la ubicación utilizando GeoLite2
            String apiUrl = "https://geolocation-db.com/json/" + ipAddress;
            URL url = new URL(apiUrl);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");
            int responseCode = con.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
                String inputLine;
                StringBuilder response = new StringBuilder();
                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();
                
                // Analizar la respuesta JSON y obtener la ubicación del usuario
                JSONParser parser = new JSONParser();
                JSONObject jsonResponse = (JSONObject) parser.parse(response.toString());
                String country = (String) jsonResponse.get("country_name");
                return country;
            } else {
                return "Ubicación no disponible";
            }
        } catch (IOException | ParseException e) {
            e.printStackTrace();
            return "Ubicación no disponible";
        }
    }

    protected static String obtenerDireccionIP() {
        try {
            // Obtener la dirección IP del cliente utilizando servicios externos
            URL url = new URL("http://checkip.amazonaws.com");
            BufferedReader br = new BufferedReader(new InputStreamReader(url.openStream()));
            return br.readLine();
        } catch (IOException e) {
            e.printStackTrace();
            return "IP desconocida";
        }
    }
     
    protected static String obtenerDireccionIPFinal() {
        try {
            // Obtener la dirección IP del cliente utilizando InetAddress
            InetAddress inetAddress = InetAddress.getLocalHost();
            return inetAddress.getHostAddress();
        } catch (UnknownHostException e) {
            e.printStackTrace();
            return "IP desconocida";
        }
    }
}
