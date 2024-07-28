package DAOS;

import seguridad.Registro;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class BitacoraDAO {
    private Connection con;
    
    public BitacoraDAO(Connection con) {
        this.con=con;
    }
    
    public void agregarEntradaBitacora(String informacion,String autor, String tipo) {
        try {
            String sql = "INSERT INTO Bitacora (informacion, tipo, fecha, autor) VALUES (?, ?, ?, ?)";
            try (PreparedStatement pstmt = this.con.prepareStatement(sql)) {
                pstmt.setString(1, informacion);
                pstmt.setString(2, tipo);
                pstmt.setString(3, obtenerFechaHora());
                pstmt.setString(4, autor);
                pstmt.executeUpdate();
                System.out.println("Entrada agregada a la bitácora correctamente.");
            }
        } catch (SQLException e) {
            System.err.println("Error al agregar entrada a la bitácora: " + e.getMessage());
        }
    }
   
    public  ArrayList<Registro> obtenerBitacorasHoyPorAutor(String autor,String formato){
        String fecha = obtenerFechaHora();
        ArrayList<Registro> registros = new ArrayList<>();
        String sql = "SELECT * FROM Bitacora WHERE fecha LIKE ? and autor=? and tipo=?";
        try (PreparedStatement pstmt = con.prepareStatement(sql)) {
            pstmt.setString(1, fecha + "%");
            pstmt.setString(2, autor);
            pstmt.setString(3, formato);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    String informacion = rs.getString("informacion");
                    String fechaString = rs.getString("fecha");
                    LocalDateTime fechaHora = LocalDateTime.parse(fechaString, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
                    String tipo = rs.getString("tipo");
                    Registro re= new Registro (fechaHora.toString(),tipo,informacion,autor);
                    registros.add(re);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener las bitácoras: " + e.getMessage());
        }
        return registros;
    }
    
    public  ArrayList<Registro> obtenerBitacorasRango(String autor,String formato,String horaInicio,String horaFinal){
        String fechaInicio=obtenerFecha();
        String fechaFinal=obtenerFecha();
        fechaInicio+=horaInicio;
        fechaFinal+=horaFinal;
        System.out.println(fechaInicio);
        System.out.println(fechaFinal);
        ArrayList<Registro> registros = new ArrayList<>();
        String sql = "SELECT * FROM Bitacora WHERE fecha BETWEEN ? and ? and autor=? and tipo=?";
        try (PreparedStatement pstmt = con.prepareStatement(sql)) {
            pstmt.setString(1, fechaInicio);
            pstmt.setString(2, fechaFinal);
            pstmt.setString(3, autor);
            pstmt.setString(4, formato);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    String informacion = rs.getString("informacion");
                    String fechaString = rs.getString("fecha");
                    LocalDateTime fechaHora = LocalDateTime.parse(fechaString, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
                    String tipo = rs.getString("tipo");
                    Registro re= new Registro (fechaHora.toString(),tipo,informacion,autor);
                    registros.add(re);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener las bitácoras: " + e.getMessage());
        }
        return registros;
    }
   
    public  ArrayList<Registro> obtenerBitacorasPorAutor(String autor,String formato) {
        ArrayList<Registro> registro = new ArrayList<>();
        String sql = "SELECT * FROM Bitacora WHERE autor = ? and tipo=?";
        try (PreparedStatement pstmt = this.con.prepareStatement(sql)) {
            pstmt.setString(1, autor);
            pstmt.setString(2, formato);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    String informacion = rs.getString("informacion");
                    LocalDateTime fecha = rs.getObject("fecha", LocalDateTime.class);
                    String tipo = rs.getString("tipo");
                    Registro re= new Registro (fecha.toString(),tipo,informacion,autor);
                    registro.add(re);
                    }
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener las bitácoras: " + e.getMessage());
        }
        return registro;
    }
    
    public  ArrayList<Registro> obtenerBitacorasPorTipo(String formato) {
        ArrayList<Registro> registro = new ArrayList<>();
        String sql = "SELECT * FROM Bitacora WHERE tipo=?";
        try (PreparedStatement pstmt = this.con.prepareStatement(sql)) {
            pstmt.setString(1, formato);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    String informacion = rs.getString("informacion");
                    LocalDateTime fecha = rs.getObject("fecha", LocalDateTime.class);
                    String tipo = rs.getString("tipo");
                    Registro re= new Registro (fecha.toString(),tipo,informacion);
                    registro.add(re);
                    }
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener las bitácoras: " + e.getMessage());
        }
        return registro;
    }
    

    private String obtenerFechaHora(){
        LocalDateTime fechaHoraActual = LocalDateTime.now();
        
        // Formatear la fecha y hora
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String fechaHora = fechaHoraActual.format(formatter);
        return fechaHora;
    }
    
    private String obtenerFecha(){
        LocalDateTime fechaHoraActual = LocalDateTime.now();
        // Formatear la fecha y hora
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String fechaHora = fechaHoraActual.format(formatter);
        fechaHora+=" ";
        return fechaHora;
    }

}
