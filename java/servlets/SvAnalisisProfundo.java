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
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.Normalizer;
import java.util.Base64;
import javax.imageio.ImageIO;
import utilities.ChatGPT;
import utilities.WordCloud;

/**
 *
 * @author sjwor
 */
@WebServlet(name = "SvAnalisisProfundo", urlPatterns = {"/SvAnalisisProfundo"})
public class SvAnalisisProfundo extends HttpServlet {

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
        String action = request.getParameter("action");
        String correo = request.getParameter("correo");
        TextoControlador tc = new TextoControlador();
        ChatGPT gpt = new ChatGPT();
        String textoSeleccionado = tc.normalizarTexto(request.getParameter("textoSeleccionado"));
        PrintWriter out = response.getWriter();
        if (action != null) {
            switch (action) {
                case "analisisSentimientos":
                    String resultado = tc.analizarSentimientos(gpt, textoSeleccionado, correo, true);
                    response.setContentType("text/html");
                    out.println("<html><body>");
                    out.println("<h2>Analisis de Sentimientos:</h2>");
                    out.println("<p>" + eliminarSaltosDeLinea(resultado) + "</p>");
                    out.println("</body></html>");
                    break;
                case "wordCloud":
                    BufferedImage imagenGenerada = tc.obtenerWordCloud(textoSeleccionado, correo, true);
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    
                    try {
                        ImageIO.write(imagenGenerada, "png", baos);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    byte[] imageBytes = baos.toByteArray();
                    String base64Image = Base64.getEncoder().encodeToString(imageBytes);

                    response.setContentType("text/html");
                    out = response.getWriter();
                    out.println("<html><body>");
                    out.println("<h2>WordCloud generado existosamente!</h2>");
                    out.println("<img src='data:image/png;base64," + base64Image + "' />");
                    out.println("</body></html>");
                    break;
                case "resumenTexto":
                    String resultado2 = eliminarSaltosDeLinea(tc.extraerIdeaPrincipal(gpt, textoSeleccionado, correo, true));
                    String encodedCorreo = URLEncoder.encode(correo, "UTF-8");
                    String encodedContent = URLEncoder.encode(resultado2, "UTF-8");
                    response.sendRedirect("paginaResumen.jsp?resumenTexto=" + encodedContent + "&correo=" + encodedCorreo);
                    break;
                case "generarPDF":
                    tc.generarPDF(gpt, textoSeleccionado, correo);
                    out = response.getWriter();
                    out.println("<html><body>");
                    out.println("<h2>El PDF ha sido generado correctamente y se ha enviado a su correo personal</h2>");
                    out.println("</body></html>");
                    break;
                case "generarAudio":
                    tc.generarAudio(textoSeleccionado, correo, true);
                    response.setContentType("text/html");
                    out = response.getWriter();
                    out.println("<html><body>");
                    out.println("<h2>Reproduciendo audio...</h2>");
                    out.println("<p>Audio guardado exitosamente</p>");
                    out.println("</body></html>");
                    break;
                default:
                    break;
            }
        }
    }

    public static String eliminarSaltosDeLinea(String texto) {
        return texto.replaceAll("\\r|\\n", "");
    }

    public static String eliminarTildes(String texto) {
        String textoSinTildes = Normalizer.normalize(texto, Normalizer.Form.NFD);
        textoSinTildes = textoSinTildes.replaceAll("[^\\p{ASCII}]", "");
        return textoSinTildes;
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
