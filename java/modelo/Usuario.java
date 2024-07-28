package modelo;

import java.awt.Image;
import java.util.ArrayList;

/**
 * La clase Usuario representa a un usuario del sistema.
 */
public class Usuario {
    private int cedula;
    private String nombreCompleto;
    private String correoElectronico;
    private int telefono;
    private Image fotografia;
    private ArrayList<Tematica> tematicasAsociadas;

    /**
     * Constructor para la clase Usuario.
     *
     * @param nombreCompleto El nombre completo del usuario.
     * @param correoElectronico El correo electrónico del usuario.
     * @param telefono El número de teléfono del usuario.
     * @param fotografia La fotografía del usuario.
     */
    public Usuario(int cedula, String nombreCompleto, String correoElectronico, int telefono, Image fotografia) {
        this.nombreCompleto = nombreCompleto;
        this.cedula = cedula;
        this.correoElectronico = correoElectronico;
        this.telefono = telefono;
        this.fotografia = fotografia;
        tematicasAsociadas = new ArrayList<>();
    }
    

    /**
     * Obtiene el correo electrónico del usuario.
     *
     * @return El correo electrónico del usuario.
     */
    public String getCorreoElectronico() {
        return correoElectronico;
    }

    /**
     * Obtiene el número de teléfono del usuario.
     *
     * @return El número de teléfono del usuario.
     */
    public int getTelefono() {
        return telefono;
    }

    public String getNombreCompleto() {
        return nombreCompleto;
    }

    public Image getFotografia() {
        return fotografia;
    }

    /**
     * Añade una temática creada por el usuario.
     *
     * @param nuevaTematica La temática a añadir.
     */
    public void añadirTematica(Tematica nuevaTematica) {
        tematicasAsociadas.add(nuevaTematica);
    }

    public int getCedula() {
        return cedula;
    }

    public void setCedula(int cedula) {
        this.cedula = cedula;
    }

    /**
     * Obtiene la lista de temáticas asociadas al usuario.
     *
     * @return La lista de temáticas asociadas al usuario.
     */
    public ArrayList<Tematica> obtenerTematicas() {
        return tematicasAsociadas;
    }
}
