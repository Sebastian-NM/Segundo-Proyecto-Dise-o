package modelo;

import java.time.LocalDateTime;

/**
 * La clase Texto representa un texto con contenido, fecha y hora de registro, y cantidad de palabras.
 */
public class Texto {
    private String contenido;
    private String fechaHoraRegistro;
    private int cantidadPalabras;

    /**
     * Constructor para la clase Texto.
     * 
     * @param contenido El contenido del texto.
     * @param fechahoraIngreso
     */
    public Texto(String contenido, String fechahoraIngreso) {
        this.contenido = contenido;
        this.fechaHoraRegistro = fechahoraIngreso;
        this.cantidadPalabras = contarPalabras(contenido);
    }

    /**
     * Obtiene el contenido del texto.
     * 
     * @return El contenido del texto.
     */
    public String getContenido() {
        return contenido;
    }

    /**
     * Obtiene la cantidad de palabras en el contenido del texto.
     * 
     * @return La cantidad de palabras en el contenido del texto.
     */
    public int getCantidadPalabras() {
        return cantidadPalabras;
    }

    /**
     * Cuenta la cantidad de palabras en el contenido del texto.
     * 
     * @param contenido El contenido del texto en el que se contarán las palabras.
     * @return La cantidad de palabras en el contenido del texto.
     */
    private int contarPalabras(String contenido) {
        // Dividir el contenido en palabras usando espacios en blanco como delimitador
        String[] palabras = contenido.split("\\s+");
        // Retornar la cantidad de palabras encontradas
        return palabras.length;
    }
    
}
