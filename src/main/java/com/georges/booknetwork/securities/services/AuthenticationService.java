package com.georges.booknetwork.securities.services;

import com.georges.booknetwork.domains.request.RegistrationRequest;

import java.util.Locale;

public interface AuthenticationService {
    public RegistrationRequest register(RegistrationRequest request, Locale locale);
}
