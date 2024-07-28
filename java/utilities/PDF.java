package utilities;

import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.PdfWriter;
import java.awt.Image;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Esta clase proporciona métodos para generar archivos PDF.
 */
public class PDF implements PDFSender {

    @Override
    public void generarYEnviarPDF(String texto, String correo, Image imagen) throws IOException {
        String carpeta = "C:\\Users\\sjwor\\Desktop\\DesignProyect\\src\\main\\java\\utilities\\documentosGenerados\\";
        String nombreArchivo = generarNombreArchivo(carpeta);
        String rutaArchivo = carpeta + nombreArchivo;

        try {
            crearPDFConImagen(texto, imagen, rutaArchivo);
        } catch (FileNotFoundException | DocumentException e) {
            e.printStackTrace();
        }

        Correo mail = new Correo();
        mail.enviar(rutaArchivo, correo);
    }

    private String generarNombreArchivo(String carpeta) {
        int cantidadArchivos = contarArchivos(carpeta);
        return (cantidadArchivos + 1) + ".pdf";
    }

    private int contarArchivos(String carpeta) {
        File directorio = new File(carpeta);
        File[] archivos = directorio.listFiles();
        if (archivos != null) {
            return archivos.length;
        } else {
            return 0;
        }
    }

    private void crearPDFConImagen(String texto, Image imagen, String rutaArchivo) throws FileNotFoundException, DocumentException, IOException {
        com.lowagie.text.Document documento = new com.lowagie.text.Document();
        PdfWriter.getInstance(documento, new FileOutputStream(rutaArchivo));
        documento.open();
        documento.add(new Paragraph(texto));

        // Convertir BufferedImage a com.lowagie.text.Image
        com.lowagie.text.Image imagenPDF = com.lowagie.text.Image.getInstance(imagen, null);
        imagenPDF.scaleToFit(300, 300);
        documento.add(imagenPDF);

        documento.close();
    }
}