package com.georges.booknetwork.controllers;

import java.util.Locale;

import com.georges.booknetwork.domains.request.AuthenticationRequest;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import lombok.RequiredArgsConstructor;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.context.MessageSource;
import org.springframework.web.bind.annotation.*;
import com.georges.booknetwork.exceptions.BookException;
import com.georges.booknetwork.domains.request.RegistrationRequest;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import com.georges.booknetwork.securities.services.AuthenticationService;

@Slf4j
@RestController
@RequestMapping("auth")
@RequiredArgsConstructor
@Tag(name = "Authentication")
public class AuthController {

    private final MessageSource messageSource;
    private final AuthenticationService service;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegistrationRequest request, @RequestHeader("Accept-Language")Locale locale) {
        log.debug("Call of save user: {}", request);
        try {
            request = service.register(request, locale);
            log.debug("entity : {}", request);
            return ResponseEntity.ok().body(request);
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

    @PostMapping("/login")
    public ResponseEntity<String> authenticate(@RequestBody @Valid AuthenticationRequest request, @RequestHeader("Accept-Language") Locale locale) {
        log.debug("Call of authenticate username: {} with password: {}", request.getUsername(), request.getPassword());
        try {
            var token = service.authenticate(request, locale);
            log.debug("Username: {} successfully authenticated", request.getUsername());
            return ResponseEntity.ok().body(token);
        } catch (BookException ex) {
            log.debug(ex.getMessage());
            return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(ex.getMessage());
        } catch (Exception e) {
            log.error("Unexpected error while login user : {}", request, e);
            String message = messageSource.getMessage("unable.to.authenticate.username", new Object[]{ request.getUsername() }, locale);
            return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(message);
        }
    }

    @GetMapping("/activate-account")
    public ResponseEntity<?> authenticate(@RequestParam String token, @RequestHeader("Accept-Language") Locale locale) {
        log.debug("Call of activate account with token: {}", token);
        try {
            service.activateAccount(token, locale);
            log.debug("Account successfully activated");
            return ResponseEntity.ok().body(true);
        } catch (BookException ex) {
            log.debug(ex.getMessage());
            return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(ex.getMessage());
        } catch (Exception e) {
            log.error("Unexpected error while activate account : {}", token, e);
            String message = messageSource.getMessage("unable.to.activate.account.with.token", new Object[]{ token }, locale);
            return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(message);
        }
    }
}
