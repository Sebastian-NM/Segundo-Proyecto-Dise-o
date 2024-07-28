package utilities;

import java.io.IOException;
import java.awt.Image;
import java.io.FileNotFoundException;
import com.google.cloud.translate.Translate;
import com.google.cloud.translate.TranslateOptions;
import com.google.cloud.translate.Translation;

public class PDFDecorator implements PDFSender {

    private Translate translate = null;
    protected PDFSender decoratedPDFSender;

    public PDFDecorator(PDFSender decoratedPDFSender) {
        this.decoratedPDFSender = decoratedPDFSender;
        this.translate = TranslateOptions.newBuilder().setApiKey("AIzaSyDIHp3JjrxKzOXfF1OxMeO9vA2o2s9Ao98").build().getService();
    }

    @Override
    public void generarYEnviarPDF(String texto, String correo, Image imagen) throws IOException, FileNotFoundException {
        decoratedPDFSender.generarYEnviarPDF(texto, correo, imagen);

        String textoEnFrances = traducirTexto(texto);

        WordCloud wc = new WordCloud();
        Image frac = wc.retornarImagen("TEXTO EN FRANCES: " + textoEnFrances);

        decoratedPDFSender.generarYEnviarPDF(textoEnFrances, correo, frac);
    }

    public String traducirTexto(String texto) {
        Translation translation = translate.translate(
                texto,
                Translate.TranslateOption.sourceLanguage("es"),
                Translate.TranslateOption.targetLanguage("fr")
        );
        return translation.getTranslatedText();
    }
}
