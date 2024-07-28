package controladores;

import DAOS.TextoDAO;
import conexiones.ConnectionBD;
import seguridad.Bitacoras;
import seguridad.Csv;
import seguridad.Plana;
import seguridad.Xml;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import utilities.Audio;
import utilities.GeneradorTexto;
import utilities.WordCloud;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileTime;
import java.sql.Connection;
import java.text.Normalizer;
import java.util.Comparator;
import utilities.Correo;
import utilities.PDF;
import utilities.PDFDecorator;
import utilities.PDFSender;

public class TextoControlador {

    private Bitacoras csv = new Csv();
    private Bitacoras xml = new Xml();
    private Bitacoras plana = new Plana();
    private String reporte;

    /**
     * Obtiene el mensaje de solicitud para analizar los sentimientos del texto.
     *
     * @param texto El texto del cual se analizarán los sentimientos.
     * @return El mensaje de solicitud para el análisis de sentimientos.
     */
    private String obtenerPromptSentimiento(String texto) {
        if (validaciones.ValidacionesTexto.detectarIdioma(texto).equals(("es"))) {
            return "Haz un análisis corto y conciso de los sentimientos que encuentres en este texto:";
        }
        return "Make a short and concise analysis of the feelings you find in this text:";
    }

    /**
     * Realiza un análisis de sentimientos sobre el texto dado.
     *
     * @param operacion La operación de análisis de texto a realizar.
     * @param texto El texto sobre el cual se realizará el análisis.
     * @return El resultado del análisis de sentimientos.
     */
    public String analizarSentimientos(GeneradorTexto operacion, String texto, String correo, boolean funcionalidad) {
        if (funcionalidad) {
            reporte = "Se realizo una accion de analizar sentimientos de un texto";
            notifyAll(correo, reporte);
        }
        return (operacion.realizarOperacion(obtenerPromptSentimiento(texto), texto));
    }

    /**
     * Obtiene el mensaje de solicitud para extraer la idea principal del texto.
     *
     * @param texto El texto del cual se extraerá la idea principal.
     * @return El mensaje de solicitud para la extracción de la idea principal.
     */
    private String obtenerPromptIdeaPrincipal(String texto) {
        if (validaciones.ValidacionesTexto.detectarIdioma(texto).equals(("es"))) {
            return "Extrae la idea principal de este texto de manera muy concisa:";
        }
        return "Extract the main idea of this text very concisely:";
    }

    /**
     * Extrae la idea principal del texto dado.
     *
     * @param operacion La operación de extracción de texto a realizar.
     * @param texto El texto del cual se extraerá la idea principal.
     * @return La idea principal extraída del texto.
     */
    public String extraerIdeaPrincipal(GeneradorTexto operacion, String texto, String correo, boolean funcionalidad) {
        if (funcionalidad) {
            reporte = "Se realizo una accion de extraer idea principal";
            notifyAll(correo, reporte);
        }
        return (operacion.realizarOperacion(obtenerPromptIdeaPrincipal(texto), texto));
    }

    /**
     * Obtiene el mensaje de solicitud para dar una opinión sobre el tema del
     * texto.
     *
     * @param texto El texto sobre el cual se dará una opinión.
     * @return El mensaje de solicitud para dar una opinión.
     */
    private String obtenerPromptOpinion(String texto) {
        if (validaciones.ValidacionesTexto.detectarIdioma(texto).equals(("es"))) {
            return "Da una opinion sobre este tema:";
        }
        return "Give an opinion on this topic:";
    }

    /**
     * Da una opinión sobre el tema del texto dado.
     *
     * @param operacion La operación de análisis de texto a realizar.
     * @param texto El texto sobre el cual se dará una opinión.
     * @param correo
     * @return La opinión dada sobre el tema del texto.
     */
    public String darOpinion(GeneradorTexto operacion, String texto, String correo, boolean funcionalidad) {
        if (funcionalidad) {
            reporte = "Se realizo una accion de consultar opinion a ChatGPT";
            notifyAll(correo, reporte);
        }
        return (operacion.realizarOperacion(obtenerPromptOpinion(texto), texto));
    }

    public BufferedImage obtenerWordCloud(String texto, String correo, boolean funcionalidad) {
        WordCloud cloud = new WordCloud();
        cloud.generar(texto);
        String directoryPath = "C:\\Users\\sjwor\\Desktop\\DesignProyect\\src\\main\\java\\utilities\\wordcloudsgenerados\\";

        try {
            Path path = Files.list(Paths.get(directoryPath))
                    .filter(Files::isRegularFile)
                    .max(Comparator.comparingLong(f -> {
                        try {
                            return Files.readAttributes(f, BasicFileAttributes.class).creationTime().toMillis();
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    }))
                    .orElseThrow(() -> new IOException("No se encontraron archivos en el directorio"));
            BufferedImage image = ImageIO.read(path.toFile());
            if (funcionalidad) {
                reporte = "Se realizo una accion de generar un WordCloud";
                notifyAll(correo, reporte);
            }
            return image;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private String obtenerNombreArchivoMayor() {
        File directorio = new File("C:\\Users\\sjwor\\Desktop\\DesignProyect\\src\\main\\java\\utilities\\wordcloudsGenerados\\");
        File[] archivos = directorio.listFiles();
        int maxNumero = 0;
        String nombreArchivoMax = "";
        if (archivos != null) {
            for (File archivo : archivos) {
                String nombre = archivo.getName();
                if (nombre.endsWith(".png")) {
                    int numero = Integer.parseInt(nombre.replace(".png", ""));
                    if (numero > maxNumero) {
                        maxNumero = numero;
                        nombreArchivoMax = nombre;
                    }
                }
            }
        }
        return nombreArchivoMax;
    }

    public void generarAudio(String texto, String correo, boolean funcionalidad) {
        Audio au = new Audio();
        if (funcionalidad) {
            reporte = "Se realizo una accion de generar un Audio";
            notifyAll(correo, reporte);
        }
        au.generar(texto);
        au.hablar(texto);
    }

    public void generarPDF(GeneradorTexto operacion, String texto, String correo) throws IOException {
        String t1 = "Análisis de Sentimientos:";
        String cont1 = analizarSentimientos(operacion, texto, correo, false);
        String t2 = "Idea Principal:";
        String cont2 = extraerIdeaPrincipal(operacion, texto, correo, false);
        String t3 = "Opinión de ChatGPT:";
        String cont3 = darOpinion(operacion, texto, correo, false);
        String t4 = "WordCloud";

        StringBuilder contenidoPDF = new StringBuilder();
        contenidoPDF.append("¡Resumen del análisis del texto proporcionado!").append("\n");
        contenidoPDF.append(formatearTexto("Usuario: ", correo)).append("\n\n");
        contenidoPDF.append(formatearTexto("Texto: ", texto)).append("\n\n");
        contenidoPDF.append(formatearTexto(t1, cont1)).append("\n\n");
        contenidoPDF.append(formatearTexto(t2, cont2)).append("\n\n");
        contenidoPDF.append(formatearTexto(t3, cont3)).append("\n\n");
        contenidoPDF.append(formatearTexto(t4, ""));

        Image cloudImagen = obtenerWordCloud(texto, correo, false);

        PDFSender pdfSender = new PDF();
        PDFSender pdfDecorator = new PDFDecorator(pdfSender);
        pdfDecorator.generarYEnviarPDF(contenidoPDF.toString(), correo, cloudImagen);

        reporte = "Se realizo una accion de enviar un PDF por correo";
        notifyAll(correo, reporte);
    }

    private String formatearTexto(String titulo, String contenido) {
        return "" + titulo + "\n" + contenido;
    }

    public static Image cargarImagen(String rutaImagen) {
        try {
            File archivoImagen = new File(rutaImagen);
            return ImageIO.read(archivoImagen);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void nuevoTexto(String contenido, String nombreTematica, String correo) throws IOException, ClassNotFoundException {
        ConnectionBD conn = new ConnectionBD();
        conn.conectar();
        Connection sebas = conn.getConexion();
        TextoDAO tdao = new TextoDAO(sebas);
        tdao.crearTexto(correo, nombreTematica, contenido);
        reporte = "Se realizo una accion de generar un nuevo texto";
        notifyAll(correo, reporte);
        conn.desconectar();
    }

    public String obtenerArchivoMasReciente(String carpeta) throws IOException {
        Path directorio = Paths.get(carpeta);
        Path archivoMasReciente = null;
        FileTime tiempoMasReciente = FileTime.fromMillis(0);

        try (DirectoryStream<Path> stream = Files.newDirectoryStream(directorio)) {
            for (Path archivo : stream) {
                BasicFileAttributes attrs = Files.readAttributes(archivo, BasicFileAttributes.class);
                FileTime tiempoModificacion = attrs.lastModifiedTime();

                if (tiempoModificacion.compareTo(tiempoMasReciente) > 0) {
                    tiempoMasReciente = tiempoModificacion;
                    archivoMasReciente = archivo;
                }
            }
        }

        if (archivoMasReciente != null) {
            return archivoMasReciente.toAbsolutePath().toString();
        } else {
            return null;
        }
    }

    public String normalizarTexto(String texto) {
        String cadenaNormalizada = Normalizer.normalize(texto, Normalizer.Form.NFD)
                .replaceAll("\\p{InCombiningDiacriticalMarks}+", "")
                .toLowerCase();
        return cadenaNormalizada;
    }

    private int contarPalabras(String texto) {
        if (texto == null || texto.isEmpty()) {
            return 0;
        }
        String[] palabras = texto.split("\\s+");
        return palabras.length;
    }

    public String acortarTexto(String texto) {
        String[] palabras = texto.split("\\s+");
        if (palabras.length > 30) {
            StringBuilder textoAcortado = new StringBuilder();
            for (int i = 0; i < 30; i++) {
                textoAcortado.append(palabras[i]).append(" ");
            }
            return textoAcortado.toString().trim();
        } else {
            return texto.trim();
        }
    }

    private void notifyAll(String correo, String info) {
        csv.formato(correo, info);
        xml.formato(correo, info);
        plana.formato(correo, info);
    }
}
