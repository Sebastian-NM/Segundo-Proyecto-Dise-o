/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package servlets;

import com.amazonaws.util.IOUtils;
import controladores.TextoControlador;
import controladores.UsuarioControlador;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.Base64;
import java.util.Comparator;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;
import seguridad.CompareFaces;

/**
 *
 * @author sjwor
 */
@WebServlet(name = "SvComparadorRostros", urlPatterns = {"/SvComparadorRostros"})
public class SvComparadorRostros extends HttpServlet {

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
        String imageData = request.getParameter("image");
        if (imageData != null) {
            String imagePath = "C:\\Users\\sjwor\\Desktop\\DesignProyect\\src\\main\\java\\utilities\\imagenesTomadas\\";
            String fileName = System.currentTimeMillis() + ".jpg";
            try {
                byte[] imageBytes = Base64.getDecoder().decode(imageData.split(",")[1]);
                OutputStream outputStream = new FileOutputStream(imagePath + fileName);
                outputStream.write(imageBytes);
                outputStream.close();
                CompareFaces comp = new CompareFaces();
                ByteBuffer imagen = loadImageBytes(obtenerDireccionImagenMasReciente());
                if(comp.compararRostrosConCarpeta(imagen)==null) {
                    response.getWriter().write("¡Rostro no reconocido!");
                }else{
                    request.getSession().setAttribute("correo",(comp.compararRostrosConCarpeta(imagen)));
                    UsuarioControlador uc = new UsuarioControlador();
                    uc.verificarUsuario(comp.compararRostrosConCarpeta(imagen));
                    response.getWriter().write("redirect:MenuSecundario.jsp?correo="+comp.compararRostrosConCarpeta(imagen));
                }
                
            } catch (IOException e) {
                e.printStackTrace();
                response.getWriter().write("Error al guardar la imagen.");
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(SvComparadorRostros.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            response.getWriter().write("No se recibió ninguna imagen.");
        }
    }
    
    
    private String obtenerDireccionImagenMasReciente() {
        String filePath = "C:\\Users\\sjwor\\Desktop\\DesignProyect\\src\\main\\java\\utilities\\imagenesTomadas\\";
        File carpeta = new File(filePath);
        File[] archivos = carpeta.listFiles();

        if (archivos != null && archivos.length > 0) {
            File imagenMasReciente = Arrays.stream(archivos)
                    .filter(Objects::nonNull)
                    .max(Comparator.comparingLong(File::lastModified))
                    .orElse(null);

            if (imagenMasReciente != null) {
                return imagenMasReciente.getAbsolutePath();
            }
        }

        return null;
    }
    
    // Método para cargar las imágenes desde archivos locales
    private static ByteBuffer loadImageBytes(String imagePath) throws IOException {
        FileInputStream inputStream = new FileInputStream(new File(imagePath));
        byte[] bytes = IOUtils.toByteArray(inputStream);
        inputStream.close();
        return ByteBuffer.wrap(bytes);
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
