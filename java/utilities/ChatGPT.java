package utilities;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

public class ChatGPT implements GeneradorTexto {

    private static final String URL_API = "https://api.openai.com/v1/chat/completions";
    private static final String API_KEY = "sk-proj-x35DhNSPKgfQL7CUuE1GT3BlbkFJoifWpmlagPl9oWQiaCiY";
    private static final String MODEL = "gpt-3.5-turbo";

    @Override
    public String realizarOperacion(String prompt, String texto) {
        String respuesta = enviarPregunta(prompt, texto);
        return extraerMensaje(respuesta);
    }

    private String enviarSolicitudHttp(String requestBody) throws IOException {
        URL url = new URL(URL_API);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Authorization", "Bearer " + API_KEY);
        connection.setRequestProperty("Content-Type", "application/json");
        connection.setDoOutput(true);

        try (OutputStream os = connection.getOutputStream(); OutputStreamWriter writer = new OutputStreamWriter(os)) {
            writer.write(requestBody);
        }

        try (BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) {
                response.append(line);
            }
            return response.toString();
        }
    }

    private String enviarPregunta(String prompt, String texto) {
        String requestBody = "{\"model\": \"" + MODEL + "\", \"messages\": [{\"role\": \"user\", \"content\": \"" + prompt + " " + texto + "\"}]}";

        try {
            return enviarSolicitudHttp(requestBody);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private String extraerMensaje(String respuesta) {
        int start = respuesta.indexOf("content") + 11;
        int end = respuesta.indexOf("\"", start);
        return respuesta.substring(start, end);
    }
}
