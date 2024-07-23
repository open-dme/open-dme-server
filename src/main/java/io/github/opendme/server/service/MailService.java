package io.github.opendme.server.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.opendme.server.config.QrCodeConfig;
import jakarta.annotation.PostConstruct;
import jakarta.mail.Message;
import jakarta.mail.internet.InternetAddress;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import java.io.ByteArrayInputStream;
import java.util.Map;

@Service
public class MailService {

    private final JavaMailSender mailSender;
    private final SpringTemplateEngine thymeleafTemplateEngine;
    private final QrCodeGeneratorService codeGeneratorService;
    private byte[] qrCode;

    private final QrCodeConfig config;
    private final ObjectMapper mapper;
    private final Resource appleImage;
    private final Resource androidImage;
    private final String appleStoreLink;
    private final String androidStoreLink;

    public MailService(
            JavaMailSender sender,
            SpringTemplateEngine engine,
            QrCodeGeneratorService qrCodeGeneratorService,
            QrCodeConfig qrCodeConfig,
            ObjectMapper objectMapper,
            @Value("classpath:static/apple.png") Resource appleImage,
            @Value("classpath:static/android.png") Resource androidImage,
            @Value("${store-links.apple}") String appleStoreLink,
            @Value("${store-links.android}") String androidStoreLink
    ) {
        this.mailSender = sender;
        this.thymeleafTemplateEngine = engine;
        this.codeGeneratorService = qrCodeGeneratorService;
        this.config = qrCodeConfig;
        this.mapper = objectMapper;
        this.appleImage = appleImage;
        this.androidImage = androidImage;
        this.appleStoreLink = appleStoreLink;
        this.androidStoreLink = androidStoreLink;
    }

    @PostConstruct
    public void init() throws Exception {
        var bufferedImage = codeGeneratorService.generateQRCodeImage(
                this.mapper.writeValueAsString(config)
        );

        qrCode = codeGeneratorService.convertFromBufferedImage(bufferedImage);
    }

    public void sendMessageUsingThymeleafTemplate(
            String to, String subject, String templateName, Map<String, Object> templateModel) {

        templateModel.put("appleStoreLink", appleStoreLink);
        templateModel.put("androidStoreLink", androidStoreLink);
        Context thymeleafContext = new Context();
        thymeleafContext.setVariables(templateModel);

        String htmlBody = thymeleafTemplateEngine.process(templateName, thymeleafContext);

        sendMail(to, subject, htmlBody);
    }


    public void sendMail(String to, String subject, String text) {

        MimeMessagePreparator preparator = mimeMessage -> {
            mimeMessage.setRecipient(Message.RecipientType.TO, new InternetAddress(to));
            mimeMessage.setSubject(subject);
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
            helper.setText(text, true);
            helper.addInline("identifier1234", () -> new ByteArrayInputStream(qrCode), "image/*");
            helper.addInline("apple", appleImage, "image/*");
            helper.addInline("android", androidImage, "image/*");
        };
        mailSender.send(preparator);
    }
}
