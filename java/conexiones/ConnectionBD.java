package conexiones;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionBD {

    private static Connection conexion;

    public void conectar() throws ClassNotFoundException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            String url = "jdbc:mysql://database-1.cz0au4qg09h3.us-east-2.rds.amazonaws.com:3306/proyectod";
            String usuario = "admin";
            String contraseña = "proyecto1";
            conexion = DriverManager.getConnection(url, usuario, contraseña);
        } catch (SQLException e) {
            //System.err.println("Error al establecer la conexión:");
            e.printStackTrace();
        }
    }

    public void desconectar() {
        if (conexion != null) {
            try {
                conexion.close();
                System.out.println("Conexión cerrada correctamente.");
            } catch (SQLException e) {
                System.err.println("Error al cerrar la conexión:");
                e.printStackTrace();
            }
        }
    }

    public Connection getConexion() {
        return ConnectionBD.conexion;
    }

}
