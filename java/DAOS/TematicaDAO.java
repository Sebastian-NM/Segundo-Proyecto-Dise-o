/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DAOS;

import conexiones.ConnectionBD;
import java.awt.Image;
import java.io.ByteArrayInputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import javax.imageio.ImageIO;
import modelo.Tematica;
import modelo.Texto;

/**
 *
 * @author chave
 */
public class TematicaDAO {

    private Connection con;
    private String nombre_tematica;
    private String descripcion;
    private byte[] foto;
    private Integer id_tematica;
    private ArrayList<Texto> textos;

    public TematicaDAO(Connection con) {
        this.con = con;
    }

    public ArrayList<Tematica> obtenerInfoTematicas(String correo) {
        ArrayList<Tematica> tematicas = new ArrayList<>();
        TextoDAO tDao = new TextoDAO(con);
        String query = "SELECT * FROM Tematica where idCorreo=? Order By nombreTematica ;";
        try (PreparedStatement ps = con.prepareStatement(query)) {
            ps.setString(1, correo);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    nombre_tematica = rs.getString("nombreTematica");
                    descripcion = rs.getString("descripcion");
                    textos = tDao.obtenerTextos(nombre_tematica, correo);
                    foto = rs.getBytes("foto");
                    Image imagen = null;
                    try {
                        imagen = ImageIO.read(new ByteArrayInputStream(foto));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    Tematica tematica = new Tematica(nombre_tematica, descripcion, imagen);
                    tematica.añadirConjuntoTextos(tDao.obtenerTextos(nombre_tematica, correo));
                    tematicas.add(tematica);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener las tematicas:");
            e.printStackTrace();
        }
        return tematicas;
    }

    public ArrayList<String> obtenerTematicas(String correo) {
        ArrayList<String> tematicas = new ArrayList<>();
        String query = "SELECT nombreTematica FROM Tematica where idCorreo=? Order By nombreTematica;";
        try (PreparedStatement ps = con.prepareStatement(query)) {
            ps.setString(1, correo);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    nombre_tematica = rs.getString("nombreTematica");
                    tematicas.add(nombre_tematica);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener las tematicas:");
            e.printStackTrace();
        }
        return tematicas;
    }

    public Tematica obtenerTematicaPorNombre(String nombreTematica, String correo) {
        TextoDAO tDao = new TextoDAO(con);
        Tematica tematica = null;
        String query = "SELECT * FROM Tematica WHERE nombreTematica = ? AND idCorreo = ?;";
        try (PreparedStatement ps = con.prepareStatement(query)) {
            ps.setString(1, nombreTematica);
            ps.setString(2, correo);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    String descripcion = rs.getString("descripcion");
                    byte[] foto = rs.getBytes("foto");
                    Image imagen = null;
                    try {
                        imagen = ImageIO.read(new ByteArrayInputStream(foto));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    tematica = new Tematica(nombreTematica, descripcion, imagen);
                    tematica.añadirConjuntoTextos(tDao.obtenerTextos(nombreTematica, correo));
                } else {
                    System.out.println("No se encontró ninguna temática con ese nombre para el usuario especificado.");
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener la temática:");
            e.printStackTrace();
        }
        return tematica;
    }

    public Integer obtenerIdTematica(String correo, String nombre_tematica) {
        String query = "SELECT idTematica FROM Tematica WHERE nombreTematica = ? AND idCorreo = ?;";
        try (PreparedStatement ps = con.prepareStatement(query)) {
            ps.setString(1, nombre_tematica);
            ps.setString(2, correo);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt("idTematica");
            } else {
                System.out.println("No se encontró ninguna temática con ese nombre para el usuario especificado.");
                return null;
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener las tematicas:");
            e.printStackTrace();
            return null;
        }
    }

    public boolean crearTematica(String nombre_tematica, String descripcion, String correo, File foto) {
        try (FileInputStream fis = new FileInputStream(foto); PreparedStatement ps = con.prepareStatement("INSERT INTO Tematica (nombreTematica, descripcion, foto, idCorreo) SELECT ?, ?, ?, correo FROM Usuario WHERE correo = ?")) {
            ps.setString(1, nombre_tematica);
            ps.setString(2, descripcion);
            ps.setBinaryStream(3, fis, (int) foto.length());
            ps.setString(4, correo);

            int rowsInserted = ps.executeUpdate();
            if (rowsInserted > 0) {
                System.out.println("Tematica agregada");
                return true;
            } else {
                System.out.println("No se ha insertado ninguna fila.");
            }
        } catch (FileNotFoundException e) {
            System.err.println("No se encontró el archivo de la imagen.");
            e.printStackTrace();
            return false;
        } catch (IOException e) {
            System.err.println("Error al leer el archivo de la imagen.");
            e.printStackTrace();
            return false;
        } catch (SQLException e) {
            System.err.println("Error al insertar la tematica en la base de datos:");
            e.printStackTrace();
            return false;
        }
        return false;
    }

}
