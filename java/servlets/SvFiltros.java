package servlets;

import DAOS.BitacoraDAO;
import conexiones.ConnectionBD;
import java.io.IOException;
import java.net.URLEncoder;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import seguridad.Registro;

@WebServlet(name = "SvFiltros", urlPatterns = {"/SvFiltros"})
public class SvFiltros extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
    }

    private String concatenarRegistros(ArrayList<Registro> registros) {
        StringBuilder resultado = new StringBuilder();
        for (Registro registro : registros) {
            resultado.append(registro.info).append("\n");
        }
        return resultado.toString();
    }
    
    private String concatenarRegistrosXML(ArrayList<Registro> registros) {
        String resultado = "";
        for (Registro registro : registros) {
            resultado+=registro.info;
        }
        return resultado;
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            processRequest(request, response);

            ConnectionBD conn = new ConnectionBD();
            conn.conectar();
            BitacoraDAO bdao = new BitacoraDAO(conn.getConexion());
            String formato = request.getParameter("formato");
            String action = request.getParameter("action");
            String correo = request.getParameter("correo");
            if (action == null) {
                response.sendRedirect("ConsultaBitacoras.jsp");
                return;
            }
            switch (action) {
                case "verMisMovimientos":
                    if (formato.equals("xml")) {
                        String contenido = concatenarRegistrosXML(bdao.obtenerBitacorasPorAutor(correo, "XML"));
                        String encodedContent = URLEncoder.encode(contenido, "UTF-8");
                        response.sendRedirect("Bitacora.jsp?contenido=" + encodedContent);
                    }
                    if (formato.equals("csv")) {
                        String contenido = concatenarRegistros(bdao.obtenerBitacorasPorAutor(correo, "CSV"));
                        String encodedContent = URLEncoder.encode(contenido, "UTF-8");
                        response.sendRedirect("Bitacora.jsp?contenido=" + encodedContent);
                    }
                    if (formato.equals("plana")) {
                        String contenido = concatenarRegistros(bdao.obtenerBitacorasPorAutor(correo, "PLANA"));
                        String encodedContent = URLEncoder.encode(contenido, "UTF-8");
                        response.sendRedirect("Bitacora.jsp?contenido=" + encodedContent);
                    }
                    break;

                case "filtrarPorHora":
                    String hora = request.getParameter("hora");
                    LocalTime[] times = parseTimeRange(hora);
                    LocalTime x = times[0];
                    LocalTime y = times[1];
                    String formating = "HH:mm:ss";
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern(formating);
                    String horaInicio = x.format(formatter);
                    String horaFin = y.format(formatter);
                    String contenido = "";
                    if (formato.equals("xml")) {
                        response.setContentType("application/xml");
                        contenido = concatenarRegistros(bdao.obtenerBitacorasRango(correo, "XML", horaInicio, horaFin));
                    }
                    if (formato.equals("csv")) {
                        contenido = concatenarRegistros(bdao.obtenerBitacorasRango(correo, "CSV", horaInicio, horaFin));
                    }
                    if (formato.equals("plana")) {
                        contenido = concatenarRegistros(bdao.obtenerBitacorasRango(correo, "PLANA", horaInicio, horaFin));
                    }
                    String encodedContent = URLEncoder.encode(contenido, "UTF-8");
                    response.sendRedirect("Bitacora.jsp?contenido=" + encodedContent);
                    break;

                case "verTodosLosMovimientos":
                    String contenide = "";
                    if (formato.equals("xml")) {
                        response.setContentType("application/xml");
                        contenide = concatenarRegistros(bdao.obtenerBitacorasPorTipo("XML"));
                    }
                    if (formato.equals("csv")) {
                        contenide = concatenarRegistros(bdao.obtenerBitacorasPorTipo("CSV"));
                    }
                    if (formato.equals("plana")) {
                        contenide = concatenarRegistros(bdao.obtenerBitacorasPorTipo("PLANA"));
                    }
                    String encodedContento = URLEncoder.encode(contenide, "UTF-8");
                    response.sendRedirect("Bitacora.jsp?contenido=" + encodedContento);
                    break;

                case "filtrarPorUsuario":
                    String usuario = request.getParameter("usuario");
                    String cont = "";
                    System.out.println("Usuario" + usuario);

                    if (formato.equals("xml")) {
                        response.setContentType("application/xml");
                        cont = concatenarRegistros(bdao.obtenerBitacorasPorAutor(usuario, "XML"));
                    }
                    if (formato.equals("csv")) {
                        cont = concatenarRegistros(bdao.obtenerBitacorasPorAutor(usuario, "CSV"));
                    }
                    if (formato.equals("plana")) {
                        cont = concatenarRegistros(bdao.obtenerBitacorasPorAutor(usuario, "PLANA"));
                    }

                    String encodedContenido = URLEncoder.encode(cont, "UTF-8");
                    response.sendRedirect("Bitacora.jsp?contenido=" + encodedContenido);
                    break;

                case "regresar":
                    response.sendRedirect("ConsultaBitacoras.jsp?correo=" + correo);
                    break;

                default:
                    response.sendRedirect("ConsultaBitacoras.jsp");
                    break;
            }
            conn.desconectar();
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(SvFiltros.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public LocalTime[] parseTimeRange(String timeRange) {
        String[] parts = timeRange.split("-");

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");

        LocalTime startTime = LocalTime.parse(parts[0], formatter);
        LocalTime endTime = LocalTime.parse(parts[1], formatter);

        return new LocalTime[]{startTime, endTime};
    }

    @Override
    public String getServletInfo() {
        return "Short description";
    }
}
