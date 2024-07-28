package servlets;

import conexiones.ConnectionBD;
import controladores.TematicaControlador;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.awt.Image;
import java.io.File;
import java.sql.Connection;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;

/**
 *
 * @author sjwor
 */
@WebServlet(name = "SvRegistroTematica", urlPatterns = {"/SvRegistroTematica"})
public class SvRegistroTematica extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setCharacterEncoding("UTF-8");
        processRequest(request, response);
        String nombre = request.getParameter("nombre");
        String descripcion = request.getParameter("descripcion");
        String rutaImagen = request.getParameter("rutaImagen");
        String correo = request.getParameter("correoPasar");
        File imageFile = new File(rutaImagen);
        TematicaControlador tc = new TematicaControlador();
        try {
            tc.nuevaTematica(nombre, descripcion, imageFile, correo);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(SvRegistroTematica.class.getName()).log(Level.SEVERE, null, ex);
        }
        PrintWriter out = response.getWriter();
        out.println("<script>alert('Tematica guardada exitosamente'); window.location.href = 'RegistroTematica.jsp';</script>");
    }

    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
