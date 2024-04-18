package com.georges.booknetwork.controllers;

import com.georges.booknetwork.domains.request.RegistrationRequest;
import com.georges.booknetwork.exceptions.BookException;
import com.georges.booknetwork.securities.services.AuthenticationService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Locale;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.OK;

@Slf4j
@Service
@RequestMapping("auth")
@RequiredArgsConstructor
@Tag(name = "Authentication")
public class AuthController {

    private final MessageSource messageSource;
    private final AuthenticationService service;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody @Valid RegistrationRequest request, @RequestHeader("Accept-Language")Locale locale) {
        log.debug("Call of save user: {}", request);
        try {
            request = service.register(request, locale);
            log.debug("entity : {}", request != null ? request.getFirstname() : null);
            return ResponseEntity.status(OK).body(request);
        }
        catch (BookException e) {
            log.debug(e.getMessage());
            return ResponseEntity.status(e.getStatusCode()).body(e.getMessage());
        } catch (Exception e) {
            log.error("Unexpected error while save entity : {}", request, e);
            String message = messageSource.getMessage("unable.to.save.entity", new Object[] { request }, locale);
            return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(message);
        }
    }
}
