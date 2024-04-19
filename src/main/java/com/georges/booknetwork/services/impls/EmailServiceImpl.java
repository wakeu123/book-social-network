package com.georges.booknetwork.services.impls;

import com.georges.booknetwork.domains.EmailTemplateName;
import com.georges.booknetwork.services.EmailService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import java.util.HashMap;
import java.util.Map;

import static java.nio.charset.StandardCharsets.*;
import static org.springframework.mail.javamail.MimeMessageHelper.MULTIPART_MODE_MIXED;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {

    private final JavaMailSender javaMailSender;
    private final SpringTemplateEngine templateEngine;

    @Override
    @Async
    public void sendEmail(String to, String username, EmailTemplateName emailTemplateName,
                          String confirmationUrl, String activationCode, String subject) throws MessagingException {
        String templateName;
        if(emailTemplateName == null) {
            templateName = "confirm-email";
        } else {
            templateName = emailTemplateName.getName();
        }
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, MULTIPART_MODE_MIXED, UTF_8.name());

        Map<String, Object> properties = new HashMap<>();
        properties.put("username", username);
        properties.put("confirmationUrl", confirmationUrl);
        properties.put("activation_code", activationCode);

        Context context = new Context();
        context.setVariables(properties);

        helper.setTo(to);
        helper.setSubject(subject);
        helper.setFrom("georgeswakeu21@gmail.com");

        String template = templateEngine.process(templateName, context);
        helper.setText(template, true);
        javaMailSender.send(mimeMessage);
    }
}
