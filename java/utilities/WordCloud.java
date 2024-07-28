package utilities;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * Esta clase proporciona funcionalidades para generar un WordCloud a partir de
 * un texto dado.
 */
public class WordCloud implements GeneracionArchivo {

    private final String rutaDirectorio = "C:\\Users\\sjwor\\Desktop\\DesignProyect\\src\\main\\java\\utilities\\wordcloudsgenerados\\";
    private final String rutaImagenFondo = "C:\\Users\\sjwor\\Desktop\\DesignProyect\\src\\main\\java\\utilities\\recursos\\fondo.png";

    /**
     * Genera un WordCloud a partir del texto proporcionado y lo guarda como una
     * imagen PNG en la ubicación especificada.
     *
     * @param texto el texto del que se generará el WordCloud
     */
    @Override
    public void generar(String texto) {
        Map<String, Integer> frequencyMap = obtenerFrecuenciaPalabras(texto);
        BufferedImage bufferedImage = crearImagenWordCloud();
        dibujarPalabras(bufferedImage, frequencyMap);
        String nombreArchivo = obtenerNombreArchivo();
        guardarImagen(bufferedImage, rutaDirectorio + nombreArchivo);
        liberarRecursos(bufferedImage);
    }
    
    public BufferedImage retornarImagen(String texto){
        Map<String, Integer> frequencyMap = obtenerFrecuenciaPalabras(texto);
        BufferedImage bufferedImage = crearImagenWordCloud();
        dibujarPalabras(bufferedImage, frequencyMap);
        return bufferedImage;
    }
    
    public String retornarPath(){
        String nombreArchivo = obtenerNombreArchivo();
        return rutaDirectorio+nombreArchivo;
    }

    /**
     * Obtiene la frecuencia de cada palabra en el texto dado.
     *
     * @param texto el texto del que se calculará la frecuencia de las palabras
     * @return un mapa que contiene la frecuencia de cada palabra
     */
    private Map<String, Integer> obtenerFrecuenciaPalabras(String texto) {
        String[] palabras = texto.split("\\s+");
        Map<String, Integer> frequencyMap = new HashMap<>();
        for (String palabra : palabras) {
            palabra = palabra.toLowerCase();
            frequencyMap.put(palabra, frequencyMap.getOrDefault(palabra, 0) + 1);
        }
        return frequencyMap;
    }

    /**
     * Crea una imagen en blanco para el WordCloud con la imagen de fondo.
     *
     * @return una instancia de BufferedImage para el WordCloud
     */
    private BufferedImage crearImagenWordCloud() {
        try {
            File fondoFile = new File(rutaImagenFondo);
            BufferedImage fondo = ImageIO.read(fondoFile);

            int width = fondo.getWidth();
            int height = fondo.getHeight();
            BufferedImage bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
            Graphics2D graphics = bufferedImage.createGraphics();
            graphics.drawImage(fondo, 0, 0, null);
            return bufferedImage;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Dibuja las palabras en el WordCloud dejando un margen alrededor de la
     * imagen de fondo.
     *
     * @param bufferedImage la imagen en la que se dibujarán las palabras
     * @param frequencyMap el mapa de frecuencia de palabras
     */
    private void dibujarPalabras(BufferedImage bufferedImage, Map<String, Integer> frequencyMap) {
        Graphics2D graphics = bufferedImage.createGraphics();
        Font font = new Font("Arial", Font.PLAIN, 18);
        graphics.setFont(font);
        int maxSize = frequencyMap.values().stream().max(Integer::compareTo).orElse(0);
        Random rand = new Random();
        int margin = 80;
        for (Map.Entry<String, Integer> entry : frequencyMap.entrySet()) {
            String palabra = entry.getKey();
            int frequency = entry.getValue();
            int size = calcularTamañoPalabra(frequency, maxSize);
            font = new Font("Arial", Font.PLAIN, size);
            graphics.setFont(font);
            graphics.setColor(new Color(generarColorAleatorio()));
            dibujarPalabra(graphics, palabra, bufferedImage, margin, rand);
        }
        graphics.dispose();
    }

    /**
     * Calcula el tamaño de la palabra en función de su frecuencia y el tamaño
     * máximo.
     *
     * @param frequency la frecuencia de la palabra
     * @param maxSize el tamaño máximo entre todas las frecuencias
     * @return el tamaño de la palabra
     */
    private int calcularTamañoPalabra(int frequency, int maxSize) {
        return 20 + (int) (40.0 * frequency / maxSize);
    }
    
    /**
     * Dibuja una palabra en la imagen con un margen alrededor de la imagen de
     * fondo.
     *
     * @param graphics el contexto gráfico
     * @param palabra la palabra a dibujar
     * @param bufferedImage la imagen en la que se dibujará la palabra
     * @param margin el margen alrededor de la imagen de fondo
     * @param rand el generador de números aleatorios
     */
    private void dibujarPalabra(Graphics2D graphics, String palabra, BufferedImage bufferedImage, int margin, Random rand) {
        FontMetrics metrics = graphics.getFontMetrics();
        Rectangle2D bounds = metrics.getStringBounds(palabra, graphics);
        int wordWidth = (int) bounds.getWidth();
        int wordHeight = (int) bounds.getHeight();
        int x, y;
        // Genera coordenadas dentro del margen alrededor de la imagen de fondo
        x = margin + rand.nextInt(bufferedImage.getWidth() - 2 * margin - wordWidth);
        y = margin + rand.nextInt(bufferedImage.getHeight() - 2 * margin - wordHeight) + metrics.getAscent();
        graphics.drawString(palabra, x, y);
    }

    /**
     * Guarda la imagen del WordCloud en la ubicación especificada.
     *
     * @param bufferedImage la imagen del WordCloud
     * @param ruta la ruta donde se guardará la imagen
     */
    private void guardarImagen(BufferedImage bufferedImage, String ruta) {
        try {
            ImageIO.write(bufferedImage, "png", new File(ruta));
            System.out.println("WordCloud guardado exitosamente en: " + ruta);
        } catch (IOException e) {
        }
    }

    /**
     * Libera los recursos utilizados por la imagen del WordCloud.
     *
     * @param bufferedImage la imagen del WordCloud
     */
    private void liberarRecursos(BufferedImage bufferedImage) {
        bufferedImage.flush();
    }

    /**
     * Genera un color aleatorio.
     *
     * @return el código RGB del color generado
     */
    private int generarColorAleatorio() {
        Random rand = new Random();
        return rand.nextInt(0xFFFFFF);
    }

    /**
     * Obtiene el nombre del archivo WordCloud con el número consecutivo al
     * último archivo en la carpeta.
     *
     * @return el nombre del archivo WordCloud
     */
    private String obtenerNombreArchivo() {
        File directorio = new File(rutaDirectorio);
        File[] archivos = directorio.listFiles();
        int cantidadArchivos = archivos != null ? archivos.length : 0;
        return (cantidadArchivos + 1) + ".png";
    }
}
