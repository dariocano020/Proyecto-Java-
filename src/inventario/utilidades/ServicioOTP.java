package inventario.utilidades;

import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;
import java.util.Random;

public class ServicioOTP {
    
    public static int generarOTP() {
        return 1000 + new Random().nextInt(9000);
    }
    
    public static void enviarCorreoOTP(String destinatario, int otp) throws Exception {
        String remitente = "dariocano2005@gmail.com";
        String clave = "esqq weco pafs mmut";
        
        Properties props = System.getProperties();
        props.put("mail.smtp.host", "smtp.gmail.com"); 
        props.put("mail.smtp.user", remitente);
        props.put("mail.smtp.clave", clave);
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.port", "587");
        
        Session session = Session.getDefaultInstance(props);
        
        MimeMessage message = new MimeMessage(session);
        message.setFrom(new InternetAddress(remitente, "SISTEMA INVENTARIO")); 
        message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(destinatario));
        
        message.setSubject("Codigo de seguridad (OTP) - Sistema de Inventario");
        String cuerpoHtml = "<div style=\"font-family: 'Segoe UI', Arial, sans-serif; max-width: 500px; margin: auto; border-radius: 12px; overflow: hidden; box-shadow: 0 4px 15px rgba(0,0,0,0.1); border: 1px solid #eaeaea;\">"
                          + "<div style=\"background: linear-gradient(135deg, #667eea 0%, #764ba2 100%); padding: 30px 20px; text-align: center;\">"
                          + "<h1 style=\"color: #ffffff; margin: 0; font-size: 24px; letter-spacing: 1px;\"> SISTEMA DE INVENTARIO</h1>"
                          + "</div>"
                          + "<div style=\"padding: 40px 30px; text-align: center; background-color: #ffffff;\">"
                          + "<h2 style=\"color: #333333; margin-top: 0;\">Codigo de Verificacion</h2>"
                          + "<p style=\"color: #666666; font-size: 16px; line-height: 1.5; margin-bottom: 30px;\">Has solicitado iniciar sesion en tu cuenta de forma segura:</p>"
                          + "<div style=\"margin: 20px auto; padding: 15px 40px; background-color: #f8f9fa; border: 2px dashed #764ba2; display: inline-block; border-radius: 8px;\">"
                          + "<span style=\"font-size: 40px; font-weight: bold; letter-spacing: 8px; color: #764ba2;\">" + otp + "</span>"
                          + "</div>"
                          + "<p style=\"color: #999999; font-size: 14px; margin-top: 30px;\">Si no has solicitado este codigo, puedes ignorar este correo.</p>"
                          + "</div>"
                          + "<div style=\"background-color: #f4f4f4; padding: 15px; text-align: center; border-top: 1px solid #eaeaea;\">"
                          + "<p style=\"color: #aaaaaa; margin: 0; font-size: 12px;\"> 2026 DAW Inventory System. Todos los derechos reservados.</p>"
                          + "</div>"
                          + "</div>";
        
        message.setContent(cuerpoHtml, "text/html; charset=utf-8");
        
        Transport transport = session.getTransport("smtp");
        transport.connect("smtp.gmail.com", remitente, clave);
        transport.sendMessage(message, message.getAllRecipients());
        transport.close();
    }

    public static void enviarFactura(inventario.modelo.Pedido pedido, String destinatario) throws Exception {
        String remitente = "dariocano2005@gmail.com";
        String clave = "esqq weco pafs mmut";
        
        Properties props = System.getProperties();
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.user", remitente);
        props.put("mail.smtp.clave", clave);
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.port", "587");
        
        Session session = Session.getDefaultInstance(props);
        
        MimeMessage message = new MimeMessage(session);
        message.setFrom(new InternetAddress(remitente, "SISTEMA INVENTARIO")); 
        message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(destinatario));
        message.setSubject("Factura de tu Pedido: " + pedido.getIdPedido());
        
        StringBuilder sb = new StringBuilder();
        sb.append("<div style=\"font-family: Arial, sans-serif; max-width: 500px; margin: auto; border: 1px solid #ddd; border-radius: 10px; padding: 20px;\">");
        sb.append("<h2 style=\"color: #2c3e50; text-align: center;\">Gracias por tu compra, ").append(pedido.getUsuario().getNombre()).append("!</h2>");
        sb.append("<hr style=\"border: 1px dashed #ccc;\">");
        sb.append("<p><strong>N Pedido:</strong> ").append(pedido.getIdPedido()).append("</p>");
        sb.append("<table style=\"width: 100%; border-collapse: collapse; margin-top: 20px;\">");
        sb.append("<tr style=\"background-color: #f2f2f2;\"><th style=\"padding: 10px; text-align: left;\">Producto</th><th style=\"padding: 10px; text-align: center;\">Cant.</th><th style=\"padding: 10px; text-align: right;\">Subtotal</th></tr>");
        
        for (inventario.modelo.LineaPedido linea : pedido.getLineas()) {
            sb.append("<tr>");
            sb.append("<td style=\"padding: 10px; border-bottom: 1px solid #eee;\">").append(linea.getProducto().getNombre()).append("</td>");
            sb.append("<td style=\"padding: 10px; text-align: center; border-bottom: 1px solid #eee;\">").append(linea.getCantidad()).append("</td>");
            sb.append("<td style=\"padding: 10px; text-align: right; border-bottom: 1px solid #eee;\">").append(String.format("%.2f", linea.getSubtotal())).append("</td>");
            sb.append("</tr>");
        }
        
        sb.append("</table>");
        sb.append("<h3 style=\"text-align: right; color: #27ae60; margin-top: 20px;\">TOTAL A PAGAR: ").append(String.format("%.2f", pedido.getTotal())).append("</h3>");
        sb.append("<p style=\"text-align: center; color: #888; font-size: 12px; margin-top: 30px;\"> 2026 DAW Inventory System</p>");
        sb.append("</div>");
        
        message.setContent(sb.toString(), "text/html; charset=utf-8");
        
        Transport transport = session.getTransport("smtp");
        transport.connect("smtp.gmail.com", remitente, clave);
        transport.sendMessage(message, message.getAllRecipients());
        transport.close();
    }
}