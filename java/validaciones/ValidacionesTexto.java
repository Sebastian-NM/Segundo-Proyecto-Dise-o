package validaciones;

import com.cybozu.labs.langdetect.Detector;
import com.cybozu.labs.langdetect.DetectorFactory;
import com.cybozu.labs.langdetect.LangDetectException;
import utilities.ChatGPT;

public final class ValidacionesTexto {
    public static String detectarIdioma(String texto) {
        try {
            DetectorFactory.loadProfile("C:\\Users\\sjwor\\Desktop\\DesignProyect\\src\\main\\java\\utilities\\recursos\\perfilesLenguaje\\"); 
            Detector detector = DetectorFactory.create();
            detector.append(texto);
            return detector.detect();
        } catch (LangDetectException e) {
            return "es"; 
        }
    }
    public static String detectarIdiomaVergas(String texto) {
        ChatGPT gpt = new ChatGPT();
        String result = gpt.realizarOperacion("Escribe unicamente las letras 'es' si el texto está en español o 'en' si está en inglés", texto);
        return result;
    }
}
