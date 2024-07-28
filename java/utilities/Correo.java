package utilities;

import modelo.Usuario;
import java.io.File;
import java.io.IOException;
import java.util.Properties;
import javax.mail.*;
import javax.mail.internet.*;

public class Correo implements EnvioDocumento {

    private final String SERVIDOR = "smtp.gmail.com";
    private final String PUERTO = "587";
    private final String USUARIO = "navarrosebastian8@gmail.com";
    private final String CLAVE = "qxcm rshw gjjb suol";

    /**
     * Construye el mensaje de correo electrónico.
     *
     * @param usuario el usuario al que se enviará el correo
     * @return el mensaje construido
     */
    private String construirMensaje(String correo) {
        StringBuilder mensaje = new StringBuilder();
        mensaje.append("Hola ").append(correo).append(",\n\n");
        mensaje.append("Adjunto encontrarás los resultados de los análisis aplicados en el texto seleccionado.\n\n");
        mensaje.append("Saludos.\n\n\n");
        mensaje.append("[Este correo se generó de manera automática, no responder a este correo]");
        return mensaje.toString();
    }

    /**
     * Configura las propiedades de la sesión de correo.
     *
     * @return las propiedades configuradas
     */
    private Properties configurarPropiedades() {
        Properties propiedades = new Properties();
        propiedades.put("mail.smtp.host", SERVIDOR);
        propiedades.put("mail.smtp.port", PUERTO);
        propiedades.put("mail.smtp.auth", "true");
        propiedades.put("mail.smtp.starttls.enable", "true");
        return propiedades;
    }

    /**
     * Inicia una sesión de correo electrónico.
     *
     * @return la sesión de correo iniciada
     */
    private Session iniciarSesion() {
        return Session.getInstance(configurarPropiedades(),
                new javax.mail.Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(USUARIO, CLAVE);
            }
        });
    }

    /**
     * Construye un objeto MimeMessage para el correo electrónico.
     *
     * @param sesion la sesión de correo
     * @param correoDestinatario la dirección de correo electrónico del
     * destinatario
     * @return el mensaje de correo electrónico construido
     * @throws MessagingException si ocurre un error al construir el mensaje
     */
    private MimeMessage construirMensaje(Session sesion, String correoDestinatario) throws MessagingException {
        MimeMessage message = new MimeMessage(sesion);
        message.setFrom(new InternetAddress(USUARIO));
        message.addRecipient(Message.RecipientType.TO, new InternetAddress(correoDestinatario));
        message.setSubject("Resultados de análisis de texto");
        return message;
    }

    /**
     * Construye el contenido del mensaje de correo electrónico.
     *
     * @param direccionArchivo la dirección del archivo adjunto
     * @param mensaje el cuerpo del mensaje
     * @return el contenido del mensaje de correo electrónico
     * @throws MessagingException si ocurre un error al construir el contenido
     * @throws IOException si ocurre un error de lectura del archivo
     */
    private Multipart construirContenido(String direccionArchivo, String mensaje) throws MessagingException, IOException {
        MimeBodyPart adjunto = new MimeBodyPart();
        adjunto.attachFile(new File(direccionArchivo));

        MimeBodyPart texto = new MimeBodyPart();
        texto.setText(mensaje);

        Multipart multipart = new MimeMultipart();
        multipart.addBodyPart(adjunto);
        multipart.addBodyPart(texto);
        return multipart;
    }

    /**
     * Envía un correo electrónico con archivo adjunto.
     *
     * @param direccionArchivo la dirección del archivo adjunto
     * @param mensaje el cuerpo del mensaje
     * @param correoDestinatario la dirección de correo electrónico del
     * destinatario
     * @throws MessagingException si ocurre un error durante el envío del correo
     * @throws IOException si ocurre un error de lectura del archivo
     */
    private void enviarCorreo(String direccionArchivo, String mensaje, String correoDestinatario) throws MessagingException, IOException {
        Session sesion = iniciarSesion();
        MimeMessage message = construirMensaje(sesion, correoDestinatario);
        Multipart contenido = construirContenido(direccionArchivo, mensaje);
        message.setContent(contenido);
        Transport.send(message);
    }

    @Override
    public void enviar(String direccionArchivo, String correo) {
        try {
            String mensaje = construirMensaje(correo);
            enviarCorreo(direccionArchivo, mensaje, correo);
        } catch (MessagingException e) {
            e.printStackTrace();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
