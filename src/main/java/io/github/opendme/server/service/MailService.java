package io.github.opendme.server.service;

import java.util.Map;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

@Service
public class MailService {

    private final JavaMailSender mailSender;
    private final SpringTemplateEngine thymeleafTemplateEngine;

    public MailService(JavaMailSender sender, SpringTemplateEngine engine) {
        mailSender = sender;
        thymeleafTemplateEngine = engine;
    }

    public void sendMessageUsingThymeleafTemplate(
            String to, String subject, String templateName, Map<String, Object> templateModel) {

        Context thymeleafContext = new Context();
        thymeleafContext.setVariables(templateModel);

        String htmlBody = thymeleafTemplateEngine.process(templateName, thymeleafContext);

        sendMail(to, subject, htmlBody);
    }


    public void sendMail(String to, String subject, String text) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);
        mailSender.send(message);
    }
}
