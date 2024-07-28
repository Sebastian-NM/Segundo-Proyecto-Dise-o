package controladores;

import DAOS.TematicaDAO;
import conexiones.ConnectionBD;
import seguridad.Bitacoras;
import seguridad.Csv;
import seguridad.Plana;
import seguridad.Xml;
import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.text.Normalizer;
import java.util.ArrayList;
import modelo.Tematica;
import modelo.Texto;

public class TematicaControlador {

    private Bitacoras csv = new Csv();
    private Bitacoras xml = new Xml();
    private Bitacoras plana = new Plana();
    private String reporte;

    public void nuevaTematica(String nombre, String descripcion, File imagen, String correo) throws IOException, ClassNotFoundException {
        ConnectionBD conn = new ConnectionBD();
        conn.conectar();
        Connection sebas = conn.getConexion();
        TematicaDAO tdao = new TematicaDAO(sebas);
        tdao.crearTematica(nombre, descripcion, correo, imagen);
        reporte = "Se realizo una accion de nueva tematica";
        notifyAll(correo, reporte);
        conn.desconectar();
    }

    public ArrayList<Tematica> obtenerTematicas(String correo) throws ClassNotFoundException {
        ConnectionBD conn = new ConnectionBD();
        conn.conectar();
        Connection sebas = conn.getConexion();
        TematicaDAO tdao = new TematicaDAO(sebas);
        ArrayList<Tematica> tem = tdao.obtenerInfoTematicas(correo);
        reporte = "Se realizo una accion de consultar tematicas";
        notifyAll(correo, reporte);
        conn.desconectar();
        return tem;
    }

    public Tematica obtenerDatosTematica(String nombreTematica, String correo, String pedo) throws ClassNotFoundException {
        ConnectionBD conn = new ConnectionBD();
        conn.conectar();
        Connection sebas = conn.getConexion();
        TematicaDAO tdao = new TematicaDAO(sebas);
        Tematica tem = tdao.obtenerTematicaPorNombre(nombreTematica, correo);
        conn.desconectar();
        return tem;
    }

    public ArrayList<Texto> obtenerTextosTematica(String nombreTematica, String correo) throws ClassNotFoundException {
        ConnectionBD conn = new ConnectionBD();
        conn.conectar();
        Connection sebas = conn.getConexion();
        TematicaDAO tdao = new TematicaDAO(sebas);
        Tematica tem = tdao.obtenerTematicaPorNombre(nombreTematica, correo);
        conn.desconectar();
        return tem.obtenerTextosAsociados();
    }

    private void notifyAll(String correo, String info) {
        csv.formato(correo, info);
        xml.formato(correo, info);
        plana.formato(correo, info);
    }

}
