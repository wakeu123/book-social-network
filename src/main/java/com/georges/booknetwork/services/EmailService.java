package com.georges.booknetwork.services;

import com.georges.booknetwork.domains.EmailTemplateName;
import jakarta.mail.MessagingException;

public interface EmailService {

    public void sendEmail(String to, String username, EmailTemplateName emailTemplateName,
                  String confirmationUrl, String activationCode, String subject) throws MessagingException;
}
