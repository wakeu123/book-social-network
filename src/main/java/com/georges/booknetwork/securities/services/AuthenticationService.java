package com.georges.booknetwork.securities.services;

import com.georges.booknetwork.domains.request.AuthenticationRequest;
import com.georges.booknetwork.domains.request.RegistrationRequest;
import jakarta.mail.MessagingException;

import java.util.Locale;

public interface AuthenticationService {
    public RegistrationRequest register(RegistrationRequest request, Locale locale);

    String authenticate(AuthenticationRequest request, Locale locale);

    void activateAccount(String token, Locale locale) throws MessagingException;
}
