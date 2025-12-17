package com.sweet_temptation.api.servicios;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    private final JavaMailSender mailSender;

    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void sendEmail(String to, String subject, String htmlBody) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
        
        helper.setTo(to);
        helper.setSubject(subject);
        helper.setText(htmlBody, true);
        
        mailSender.send(message);
    }

    public void sendPasswordResetEmail(String to, String token, String nombreUsuario) throws MessagingException {
        String subject = "Sweet Temptation - Recuperar Contraseña";
        String htmlBody = buildPasswordResetEmailBody(token, nombreUsuario);
        sendEmail(to, subject, htmlBody);
    }

    private String buildPasswordResetEmailBody(String token, String nombreUsuario) {
        return """
            <!DOCTYPE html>
            <html>
            <head>
                <meta charset="UTF-8">
                <style>
                    body { font-family: Arial, sans-serif; background-color: #FFF7F7; padding: 20px; }
                    .container { max-width: 600px; margin: 0 auto; background: white; padding: 30px; border-radius: 10px; box-shadow: 0 2px 10px rgba(0,0,0,0.1); }
                    .header { text-align: center; color: #FF70B5; }
                    .token-box { background: #FFFFD2F7; padding: 20px; text-align: center; font-size: 24px; font-weight: bold; letter-spacing: 3px; border-radius: 8px; margin: 20px 0; color: #333; }
                    .footer { text-align: center; color: #666; font-size: 12px; margin-top: 20px; }
                    .warning { color: #e74c3c; font-size: 14px; }
                </style>
            </head>
            <body>
                <div class="container">
                    <h1 class="header">🧁 Sweet Temptation</h1>
                    <p>Hola <strong>%s</strong>,</p>
                    <p>Recibimos una solicitud para restablecer tu contraseña. Usa el siguiente código para continuar:</p>
                    <div class="token-box">%s</div>
                    <p class="warning">⚠️ Este código expira en 15 minutos.</p>
                    <p>Si no solicitaste este cambio, puedes ignorar este correo.</p>
                    <div class="footer">
                        <p>© 2025 Sweet Temptation - Todos los derechos reservados</p>
                    </div>
                </div>
            </body>
            </html>
            """.formatted(nombreUsuario, token);
    }
}




