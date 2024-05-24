package catalogo.persistence.services;

import catalogo.persistence.dto.UsuarioDTO;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

@Service
public class SendEmailService {

    private final JavaMailSender  mailSender;
    private final SpringTemplateEngine templateEngine;

    @Autowired
    public SendEmailService(JavaMailSender mailSender, SpringTemplateEngine templateEngine) {
        this.mailSender = mailSender;
        this.templateEngine = templateEngine;
    }

    @Value("${variables.correo}")
    private String correo;

    public void SendEmail(String destintario, Long ruc, String password, String razonSocial, int tipo){

        Context context = new Context();
        context.setVariable("ruc", ruc);

        String passwordValud = (password != null && !password.isEmpty()) ? password : "";
        context.setVariable("password", passwordValud);
        context.setVariable("razonSocial", razonSocial);
        context.setVariable("correo", correo);

        String contentHtml = "";
        String asunto = "";

        switch (tipo) {
            case 1 -> {
                contentHtml = templateEngine.process("Bienvenida-email.html", context);
                asunto = "Bienvenido/a a Catálogo de Proveedores";
            }
            case 2 -> {
                templateEngine.process("Aprobado-email.html", context);
                asunto = "Bienvenido/a a Catálogo de Proveedores";
            }
            case 3 -> {
                templateEngine.process("Aprobado-email.html", context);
                asunto = "Bienvenido/a a Catálogo de Proveedores";
            }

            default -> throw new IllegalArgumentException("Tipo de correo electrónico no válido");
        };


        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);

            helper.setTo(destintario);
            helper.setSubject(asunto);
            helper.setText(contentHtml, true);

            mailSender.send(mimeMessage);
        } catch (MessagingException e) {
            throw new RuntimeException("Error al enviar el correo electrónico", e);
        }
    }
}




















