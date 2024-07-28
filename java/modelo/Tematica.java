package modelo;

import java.awt.Image;
import java.util.ArrayList;

/**
 * La clase Tematica representa una temática asociada a un usuario.
 */
public class Tematica {

    private String nombre;
    private String descripcion;
    private Image fotoRepresentativa;
    private ArrayList<Texto> textosAsociados;

    /**
     * Constructor para la clase Tematica.
     *
     * @param nombre El nombre de la temática.
     * @param descripcion La descripción de la temática.
     * @param fotoRepresentativa La fotografía representativa de la temática.
     */
    public Tematica(String nombre, String descripcion, Image fotoRepresentativa) {
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.fotoRepresentativa = fotoRepresentativa;
        textosAsociados = new ArrayList<>();
    }
    
    /**
     * Obtiene el nombre de la temática.
     *
     * @return El nombre de la temática.
     */
    public String getNombre() {
        return nombre;
    }

    /**
     * Obtiene la descripción de la temática.
     *
     * @return La descripción de la temática.
     */
    public String getDescripcion() {
        return descripcion;
    }

    /**
     * Obtiene la fotografía representativa de la temática.
     *
     * @return La fotografía representativa de la temática.
     */
    public Image getFotoRepresentativa() {
        return fotoRepresentativa;
    }

    /**
     * Obtiene la lista de textos asociados a esta temática.
     *
     * @return La lista de textos asociados a esta temática.
     */
    public ArrayList<Texto> obtenerTextosAsociados() {
        return textosAsociados;
    }

    /**
     * Añade un nuevo texto a la lista de textos asociados a esta temática.
     *
     * @param nuevoTexto El texto que se va a añadir a la lista de textos
     * asociados.
     */
    public void añadirTexto(Texto nuevoTexto) {
        textosAsociados.add(nuevoTexto);
    }
    
    public void añadirConjuntoTextos(ArrayList<Texto> txts){
        textosAsociados = txts;
    }

}
