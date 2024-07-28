package validaciones;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class ValidacionesUsuario {

    public boolean verificarCorreo(String correo) {
        try {
            String respuestaAPI = invocarAPIEmailable(correo);
            return procesarRespuestaAPI(respuestaAPI);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return true;
    }

    private String invocarAPIEmailable(String correo) throws IOException {
        String url = "https://api.emailable.com/v1/verify?email=" + correo + "&api_key=live_a6b10ba975899899d79a";
        List<String> comando = new ArrayList<>();
        comando.add("curl");
        comando.add("-X");
        comando.add("GET");
        comando.add(url);

        ProcessBuilder procesoBuilder = new ProcessBuilder(comando);
        Process proceso = procesoBuilder.start();

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(proceso.getInputStream()))) {
            StringBuilder respuesta = new StringBuilder();
            String linea;
            while ((linea = reader.readLine()) != null) {
                respuesta.append(linea);
            }
            return respuesta.toString();
        }
    }

    private boolean procesarRespuestaAPI(String respuestaAPI) {
        System.out.println(respuestaAPI);
        return !(respuestaAPI.contains("\"state\":\"undeliverable\"")
                || respuestaAPI.contains("\"state\":\"risky\"")
                || respuestaAPI.contains("\"reason\":\"rejected_email\""));
    }
    
        public static void validar(String numeroEnvia, String numeroRecep) {
        try {
            String apiUrl = construirUrl(numeroEnvia, numeroRecep);
            String apiKey = "UAKb0c9f9e6-01a9-43b3-9c3a-72f3eaa4c2a5";
            HttpURLConnection connection = configurarConexion(apiUrl, apiKey);
            int responseCode = connection.getResponseCode();
            System.out.println("Código de respuesta: " + responseCode);
            procesarRespuesta(connection, responseCode);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static String construirUrl(String numeroEnvia, String numeroRecep) {
        return "https://api.p.2chat.io/open/whatsapp/check-number/" + numeroEnvia + "/" + numeroRecep + "";
    }

    private static HttpURLConnection configurarConexion(String apiUrl, String apiKey) throws Exception {
        URL url = new URL(apiUrl);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.setRequestProperty("X-User-API-Key", apiKey);
        return connection;
    }

    private static void procesarRespuesta(HttpURLConnection connection, int responseCode) throws Exception {
        BufferedReader reader;
        if (responseCode == HttpURLConnection.HTTP_OK) {
            reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        } else {
            reader = new BufferedReader(new InputStreamReader(connection.getErrorStream()));
        }
        StringBuilder response = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            response.append(line);
        }
        reader.close();
        if (responseCode == HttpURLConnection.HTTP_OK) {
            procesarRespuestaExitosa(response.toString());
        } else {
            procesarErrorRespuesta(response.toString());
        }
    }

    private static void procesarRespuestaExitosa(String jsonResponse) {
        boolean onWhatsapp = jsonResponse.contains("\"on_whatsapp\": true");
        System.out.println("Está en WhatsApp: " + onWhatsapp);
    }

    private static void procesarErrorRespuesta(String errorResponse) {
        System.out.println("Error en la respuesta:");
        System.out.println(errorResponse);
    }
}
