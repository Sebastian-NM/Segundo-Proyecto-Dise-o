package utilities;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import com.darkprograms.speech.synthesiser.SynthesiserV2;
import java.io.ByteArrayInputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.advanced.AdvancedPlayer;

public class Audio implements GeneracionArchivo {

    private static final String CARPETA_DESTINO = "C:\\Users\\sjwor\\Desktop\\DesignProyect\\src\\main\\java\\utilities\\audiosGenerados\\";
    SynthesiserV2 sintetizador = new SynthesiserV2("AIzaSyBOti4mM-6x9WDnZIjIeyEU21OpBXqWBgw");

    @Override
    public void generar(String texto) {
        guardarArchivoMP3(texto);
    }

    /**
     * Llama al sintetizador de voz para decir el texto dado
     *
     * @param texto Texto a decir
     */
    public void hablar(String texto) {
        Thread hilo = new Thread(() -> {
            try {
                InputStream entradaStream = sintetizador.getMP3Data(texto);
                byte[] datosAudio = entradaStream.readAllBytes();

                AdvancedPlayer reproductor = new AdvancedPlayer(new ByteArrayInputStream(datosAudio));
                reproductor.play();

            } catch (IOException | JavaLayerException e) {
                e.printStackTrace();
            }
        });

        hilo.setDaemon(false);
        hilo.start();
    }

    /**
     * Guarda un archivo MP3 con el texto dado en la carpeta de destino
     *
     * @param texto Texto a guardar como archivo MP3
     */
    public void guardarArchivoMP3(String texto) {
        try {
            InputStream entradaStream = sintetizador.getMP3Data(texto);
            byte[] datosAudio = entradaStream.readAllBytes();

            String nombreArchivo = generarNombreArchivo();
            File archivoSalida = new File(CARPETA_DESTINO + nombreArchivo);
            FileOutputStream salidaStream = new FileOutputStream(archivoSalida);
            salidaStream.write(datosAudio);
            salidaStream.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Genera un nombre de archivo único basado en la marca de tiempo actual
     *
     * @return El nombre del archivo
     */
    private String generarNombreArchivo() {
        String marcaTiempo = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
        return "AUDIO-" + marcaTiempo + ".mp3";
    }

}
