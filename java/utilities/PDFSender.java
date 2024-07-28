package utilities;

import java.awt.Image;
import java.io.IOException;

public interface PDFSender {

    void generarYEnviarPDF(String texto, String correo, Image imagen) throws IOException;
}
