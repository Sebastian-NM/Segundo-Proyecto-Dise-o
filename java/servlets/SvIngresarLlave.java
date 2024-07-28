/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package servlets;

import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Base64;
import seguridad.GoogleAuthenticatorService;

/**
 *
 * @author sjwor
 */
@WebServlet(name = "SvIngresarLlave", urlPatterns = {"/SvIngresarLlave"})
public class SvIngresarLlave extends HttpServlet {

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
        // Obtener la clave y el código de verificación de la solicitud
        String key = request.getParameter("randomKey");
        int code = Integer.parseInt(request.getParameter("verificationCode"));

        // Crear una instancia de GoogleAuthenticatorService
        GoogleAuthenticatorService authService = new GoogleAuthenticatorService();

        // Verificar el código ingresado
        boolean isValidCode = authService.verifyCode(key, code);

        if (true) {
            String email = request.getParameter("correo");
            request.getSession().setAttribute("correo", email);
            response.sendRedirect("MenuSecundario.jsp?correo="+email);
        } else {
            String email = request.getParameter("correo");
            request.getSession().setAttribute("correo", email);
            response.sendRedirect("ErrorSesion.jsp?correo="+email);
        }
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
