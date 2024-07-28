/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package servlets;

import controladores.TextoControlador;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.net.URLDecoder;
import utilities.ChatGPT;

/**
 *
 * @author sjwor
 */
@WebServlet(name = "SvPaginaResumen", urlPatterns = {"/SvPaginaResumen"})
public class SvPaginaResumen extends HttpServlet {

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
        processRequest(request, response);
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
        String ideaPrincipal = request.getParameter("ideaPrincipal");
        ChatGPT gpt = new ChatGPT();
        PrintWriter out = response.getWriter();
        TextoControlador tc = new TextoControlador();
        String correo = request.getParameter("correo");
        String elmero = tc.darOpinion(gpt, ideaPrincipal, correo, true);
        response.setContentType("text/html");
        out.println("<!DOCTYPE html>");
        out.println("<html>");
        out.println("<head>");
        out.println("<meta charset=\"UTF-8\">");
        out.println("<title>AI Opinion by ChatGPT</title>");
        out.println("<style>");
        out.println("body {");
        out.println("    font-family: Arial, sans-serif;");
        out.println("    margin: 0;");
        out.println("    padding: 0;");
        out.println("    background-color: #f2f2f2;");
        out.println("}");
        out.println("h2 {");
        out.println("    font-size: 24px;");
        out.println("    color: #333;");
        out.println("    margin-bottom: 20px;");
        out.println("}");
        out.println("p {");
        out.println("    font-size: 16px;");
        out.println("    color: #666;");
        out.println("    margin-bottom: 20px;");
        out.println("}");
        out.println("</style>");
        out.println("</head>");
        out.println("<body>");
        out.println("<div class=\"container\">");
        out.println("<h2>Opinion de ChatGPT:</h2>");
        out.println("<p>" + (elmero) + "</p>");
        out.println("</div>");
        out.println("</body>");
        out.println("</html>");
    }

    public static String eliminarSaltosDeLinea(String texto) {
        return texto.replaceAll("\\r|\\n", "");
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
