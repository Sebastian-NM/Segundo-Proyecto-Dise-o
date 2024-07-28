package controladores;

import DAOS.TematicaDAO;
import DAOS.UsuarioDAO;
import conexiones.ConnectionBD;
import java.sql.Connection;
import java.util.ArrayList;
import modelo.Usuario;
import seguridad.Bitacoras;
import seguridad.Csv;
import seguridad.Plana;
import seguridad.Xml;

public class UsuarioControlador {

    private Bitacoras csv = new Csv();
    private Bitacoras xml = new Xml();
    private Bitacoras plana = new Plana();
    private String reporte;
    
    public boolean verificarUsuario(String correo) throws ClassNotFoundException {
        ConnectionBD conn = new ConnectionBD();
        conn.conectar();
        Connection sebas = conn.getConexion();
        UsuarioDAO udao = new UsuarioDAO(sebas);
        if (udao.obtenerUsuarioPorCorreo(correo)==null) {
            return false;
        }
        reporte = "Se realizo una accion de ingreso de usuario";
        notifyAll(correo, reporte);
        conn.desconectar();
        return true;
    }

    public ArrayList<Usuario> consultarUsuarios(String correo) throws ClassNotFoundException {
        ConnectionBD conn = new ConnectionBD();
        conn.conectar();
        Connection sebas = conn.getConexion();
        UsuarioDAO udao = new UsuarioDAO(sebas);
        ArrayList<Usuario> lista = udao.obtenerUsuarios();
        reporte = "Se realizo una accion de consulta de usuarios";
        notifyAll(correo, reporte);
        conn.desconectar();
        return lista;
    }
    
    private void notifyAll(String correo, String info){
       csv.formato(correo, info);
       xml.formato(correo, info);
       plana.formato(correo, info);
    }

}
