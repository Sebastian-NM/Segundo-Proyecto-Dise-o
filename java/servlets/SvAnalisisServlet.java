/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package servlets;

import com.google.gson.Gson;
import controladores.TematicaControlador;
import controladores.TextoControlador;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import static java.lang.System.out;
import java.util.ArrayList;
import java.util.Base64;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import modelo.Texto;
import utilities.ChatGPT;
import utilities.WordCloud;

/**
 *
 * @author sjwor
 */
@WebServlet(name = "SvAnalisisServlet", urlPatterns = {"/SvAnalisisServlet"})
public class SvAnalisisServlet extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Obtener los parámetros enviados desde el cliente (nombre de la temática y correo del usuario)
        String tematica = request.getParameter("tematica");
        String correo = request.getParameter("correo");

        // Obtener los textos correspondientes a la temática y al usuario desde el controlador
        TematicaControlador controlador = new TematicaControlador();
        ArrayList<Texto> textos = null;
        try {
            textos = controlador.obtenerTextosTematica(tematica, correo);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(SvAnalisisServlet.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        if (textos == null || textos.isEmpty()) {
            response.sendRedirect("RegistroTexto.jsp?correo=" + correo);
            return;
        }

        // Convertir los textos a formato JSON
        Gson gson = new Gson();
        String jsonTextos = gson.toJson(textos);

        // Configurar la respuesta HTTP
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        // Enviar los textos como respuesta al cliente
        PrintWriter out = response.getWriter();
        out.print(jsonTextos);
        out.flush();
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException { 
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
