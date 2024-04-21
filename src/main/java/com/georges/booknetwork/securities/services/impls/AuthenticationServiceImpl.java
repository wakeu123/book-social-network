package com.georges.booknetwork.securities.services.impls;

import com.georges.booknetwork.domains.EmailTemplateName;
import com.georges.booknetwork.domains.Role;
import com.georges.booknetwork.domains.Token;
import com.georges.booknetwork.domains.User;
import com.georges.booknetwork.domains.request.AuthenticationRequest;
import com.georges.booknetwork.domains.request.RegistrationRequest;
import com.georges.booknetwork.exceptions.BookException;
import com.georges.booknetwork.repositories.RoleRepository;
import com.georges.booknetwork.repositories.TokenRepository;
import com.georges.booknetwork.repositories.UserRepository;
import com.georges.booknetwork.securities.JwtService;
import com.georges.booknetwork.securities.services.AuthenticationService;
import com.georges.booknetwork.services.EmailService;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {

    private final JwtService jwtService;
    private final EmailService emailService;
    private final MessageSource messageSource;
    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final TokenRepository tokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    @Value("${application.mailing.frontend.activation-url}")
    private String activationUrl;

    @Override
    public RegistrationRequest register(RegistrationRequest request, Locale locale) {
        log.debug("Try to Save entity : {}", request);
        if (userRepository == null) {
            return request;
        }
        try {
            if (request == null) {
                String message = messageSource.getMessage("unable.to.save.null.object", new Object[] { request },
                        locale);
                throw new BookException(message);
            }
            validateBeforeSave(request, locale);
            Role role = roleRepository.findByName(request.getRole())
                    .orElseThrow(() -> {
                        String message = messageSource.getMessage("unable.to.initialize.role", new Object[] { request },
                                locale);
                        return new BookException(message);
                    });
            User user = User
                    .builder()
                    .firstname(request.getFirstname())
                    .lastname(request.getLastname())
                    .email(request.getEmail())
                    .dateOfBirth(LocalDateTime.now().toLocalDate())
                    .roles(List.of(role))
                    .enabled(false)
                    .accountLocked(false)
                    .password(passwordEncoder.encode(request.getPassword()))
                    .build();
            request.setId(userRepository.save(user).getId());
            sendValidationEmail(user);
            log.debug("entity successfully saved : {} ", request);
            return request;
        } catch (BookException e) {
            throw e;
        } catch (Exception e) {
            log.error("Unexpected error while save entity : {}", request, e);
            String message = messageSource.getMessage("unable.to.save.model", new Object[] { request }, locale);
            throw new BookException(INTERNAL_SERVER_ERROR.value(), message);
        }
    }

    @Override
    public String authenticate(AuthenticationRequest request, Locale locale) {
        var auth = this.authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));
        var claims = new HashMap<String, Object>();
        var user = ((User)auth.getPrincipal());
        claims.put("fullName", user.fullName());
        var token = this.jwtService.generateToken(claims, user);
        return token;
    }

    private void sendValidationEmail(User user) throws MessagingException {
        String newToken = generateAndSaveActivationToken(user);
        emailService.sendEmail(user.getEmail(), user.fullName(), EmailTemplateName.ACTIVATE_ACCOUNT,
                activationUrl, newToken, "Account Activation");
    }

    private String generateAndSaveActivationToken(User user) {
        String generateToken = generateActivationCode(6);
        var token = Token
                .builder()
                .userToken(generateToken)
                .createdAt(LocalDateTime.now())
                .expiredAt(LocalDateTime.now().plusMinutes(15))
                .user(user)
                .build();
        tokenRepository.save(token);
        return generateToken;
    }

    private String generateActivationCode(int length) {
        String characters = "0123456789";
        StringBuilder codeBuilder = new StringBuilder();
        SecureRandom secureRandom = new SecureRandom();
        for (int i = 0; i < length; i++) {
            int randomIndex = secureRandom.nextInt(characters.length());
            codeBuilder.append(characters.charAt(randomIndex));
        }
        return codeBuilder.toString();
    }

    private void validateBeforeSave(RegistrationRequest request, Locale locale) {
        
    }
}
