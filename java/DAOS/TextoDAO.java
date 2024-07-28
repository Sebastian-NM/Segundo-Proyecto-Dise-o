/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DAOS;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import modelo.Texto;

/**
 *
 * @author sjwor
 */
public class TextoDAO {

    private Connection con;

    public TextoDAO(Connection con) {
        this.con = con;
    }

    public ArrayList<Texto> obtenerTextos(String tematica, String correo) {
        ArrayList<Texto> textos = new ArrayList<>();
        String query = "SELECT * FROM Texto t JOIN Tematica tm ON t.idTematica = tm.idTematica JOIN Usuario u ON tm.idCorreo = u.correo WHERE tm.nombreTematica = ? AND u.correo = ?";
        try (PreparedStatement ps = con.prepareStatement(query)) {
            ps.setString(1, tematica);
            ps.setString(2, correo);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    String descripcion = rs.getString("contenido");
                    String horaIngreso = rs.getString("horaIngreso");
                    Texto texto = new Texto(descripcion, horaIngreso);
                    textos.add(texto);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener las textos:");
            e.printStackTrace();
        }
        return textos;
    }

    public boolean crearTexto(String correo, String tematica, String contenido) {
        TematicaDAO consultaTem = new TematicaDAO(con);
        int idTematica = consultaTem.obtenerIdTematica(correo, tematica);
        String horaIngreso = obtenerHora();
        String query = "INSERT INTO Texto (contenido, horaIngreso, idTematica) VALUES (?, ?, ?)";
        try (PreparedStatement ps = con.prepareStatement(query)) {
            ps.setString(1, contenido);
            ps.setString(2, horaIngreso);
            ps.setInt(3, idTematica);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al crear el texto");
            return false;
        }
    }

    private String obtenerHora() {
        LocalDateTime fechaHoraActual = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String fechaHoraFormateada = fechaHoraActual.format(formatter);
        return fechaHoraFormateada;
    }
}
