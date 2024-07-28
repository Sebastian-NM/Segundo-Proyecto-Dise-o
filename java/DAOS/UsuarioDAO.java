package DAOS;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import javax.imageio.ImageIO;
import modelo.Usuario;

public class UsuarioDAO {

    private Connection con;
    private String nombre;
    private int cedula;
    private String correo;
    private int numero;
    private byte[] foto;

    public UsuarioDAO(Connection con) {
        this.con = con;
    }

    public ArrayList<Usuario> obtenerUsuarios() {
        ArrayList<Usuario> usuarios = new ArrayList<>();
        String query = "SELECT * FROM Usuario";
        try (PreparedStatement ps = con.prepareStatement(query); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                cedula = rs.getInt("cedula");
                nombre = rs.getString("nombre");
                correo = rs.getString("correo");
                numero = rs.getInt("numero");
                foto = rs.getBytes("foto");
                Usuario usuario = new Usuario(cedula,nombre, correo, numero, convertirBytesAImagen(foto));
                usuarios.add(usuario);
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener los usuarios:");
            e.printStackTrace();
        }
        return usuarios;
    }
    
     private Image convertirBytesAImagen(byte[] bytes) {
        try {
            ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
            BufferedImage bImage = ImageIO.read(bis);
            return bImage;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public Usuario obtenerUsuarioPorCorreo(String corre) {
        String query = "SELECT * FROM Usuario WHERE correo = ?";
        try (PreparedStatement ps = con.prepareStatement(query)) {
            ps.setString(1, corre);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    cedula = rs.getInt("cedula");
                    nombre = rs.getString("nombre");
                    correo = rs.getString("correo");
                    numero = rs.getInt("numero");
                    foto = rs.getBytes("foto");
                    // Crear y retornar un objeto Usuario con los datos obtenidos
                    return new Usuario(cedula, nombre, correo, numero, convertirBytesAImagen(foto));
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener el usuario:");
            e.printStackTrace();
        }
        return null; // Retorna null si no se encuentra ningún usuario con el ID especificado
    }
}
