package catalogo.persistence.services;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

@Service
public class SendEmailUpdateService {

    private final JavaMailSender  mailSender;
    private final SpringTemplateEngine templateEngine;

    @Autowired
    public SendEmailUpdateService(JavaMailSender mailSender, SpringTemplateEngine templateEngine) {
        this.mailSender = mailSender;
        this.templateEngine = templateEngine;
    }

    public void SendEmail(String destintario, Long ruc, String password, String razonSocial){

        Context context = new Context();
        context.setVariable("ruc", ruc);
        context.setVariable("password", password);
        context.setVariable("razonSocial", razonSocial);
        String contentHtml = templateEngine.process("Bienvenida-email.html", context);

        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);

            helper.setTo(destintario);
            helper.setSubject("Bienvenido/a a Catálogo de Proveedores");
            helper.setText(contentHtml, true);

            mailSender.send(mimeMessage);
        } catch (MessagingException e) {
            throw new RuntimeException("Error al enviar el correo electrónico", e);
        }
    }
}




















