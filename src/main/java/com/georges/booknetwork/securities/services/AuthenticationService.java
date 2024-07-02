package com.georges.booknetwork.securities.services;

import java.util.Locale;
import jakarta.mail.MessagingException;
import com.georges.booknetwork.domains.request.RegistrationRequest;
import com.georges.booknetwork.domains.request.AuthenticationRequest;

public interface AuthenticationService {

    RegistrationRequest register(RegistrationRequest request, Locale locale);

    String authenticate(AuthenticationRequest request, Locale locale);

    void activateAccount(String token, Locale locale) throws MessagingException;
}
