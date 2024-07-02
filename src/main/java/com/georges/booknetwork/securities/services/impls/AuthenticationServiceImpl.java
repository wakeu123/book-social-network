package com.georges.booknetwork.securities.services.impls;

import com.georges.booknetwork.domains.Role;
import com.georges.booknetwork.domains.User;
import com.georges.booknetwork.domains.Token;
import com.georges.booknetwork.services.EmailService;
import com.georges.booknetwork.exceptions.BookException;
import com.georges.booknetwork.domains.EmailTemplateName;
import com.georges.booknetwork.repositories.RoleRepository;
import com.georges.booknetwork.repositories.UserRepository;
import com.georges.booknetwork.repositories.TokenRepository;
import com.georges.booknetwork.domains.request.RegistrationRequest;
import com.georges.booknetwork.domains.request.AuthenticationRequest;
import com.georges.booknetwork.securities.services.AuthenticationService;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import lombok.RequiredArgsConstructor;
import jakarta.mail.MessagingException;
import org.springframework.stereotype.Service;
import org.springframework.context.MessageSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import java.util.List;
import java.util.Locale;
import java.util.HashMap;
import java.time.LocalDateTime;
import java.security.SecureRandom;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {

    private final JwtServiceImpl jwtService;
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
        if (this.userRepository == null) {
            return request;
        }
        try {
            if (request == null) {
                String message = this.messageSource.getMessage("unable.to.save.null.object", new Object[] { null },
                        locale);
                throw new BookException(message);
            }
            validateBeforeSave(request, locale);
            Role role = this.roleRepository.findByName("USER")
                    .orElseThrow(() -> {
                        String message = this.messageSource.getMessage("unable.to.initialize.role", new Object[] { request },
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
                    .password(this.passwordEncoder.encode(request.getPassword()))
                    .build();
            request.setId(this.userRepository.save(user).getId());
            //sendValidationEmail(user);
            log.debug("entity successfully saved : {} ", request);
            return request;
        } catch (BookException e) {
            throw e;
        } catch (Exception e) {
            log.error("Unexpected error while save entity : {}", request, e);
            String message = this.messageSource.getMessage("unable.to.save.model", new Object[] { request }, locale);
            throw new BookException(INTERNAL_SERVER_ERROR.value(), message);
        }
    }

    @Override
    public String authenticate(AuthenticationRequest request, Locale locale) {
        log.debug("Try to authenticate username : {}", request.getEmail());
        try {
            if (request.getEmail() == null || request.getPassword() == null) {
                String message = this.messageSource.getMessage("unable.to.authenticate.username", new Object[] { request },
                        locale);
                throw new BookException(message);
            }
            var auth = this.authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));
            var claims = new HashMap<String, Object>();
            var user = ((User)auth.getPrincipal());
            claims.put("fullName", user.fullName());
            log.debug("Username successfully authenticated : {} ", request.getEmail());
            return this.jwtService.generateToken(claims, user);
        } catch (BookException e) {
            throw e;
        } catch (Exception e) {
            log.error("Unexpected error while authenticate username : {}", request, e);
            String message = this.messageSource.getMessage("unable.to.authenticate.username", new Object[] { request }, locale);
            throw new BookException(INTERNAL_SERVER_ERROR.value(), message);
        }
    }

    @Override
    @Transactional
    public void activateAccount(String token, Locale locale) {
        log.debug("Try to activate account with this token : {}", token);
        try {
            if (token == null) {
                String message = this.messageSource.getMessage("unable.to.activate.account", new Object[] { null }, locale);
                throw new BookException(message);
            }
            Token savedToken = this.tokenRepository.findByUserToken(token)
                    .orElseThrow(() -> {
                        String message = this.messageSource.getMessage("invalid.token", new Object[] { token }, locale);
                        return new BookException(message);
                    });

            if (LocalDateTime.now().isAfter(savedToken.getExpiredAt())) {
                this.sendValidationEmail(savedToken.getUser());
                String message = this.messageSource.getMessage("token.has.expired", new Object[] { token }, locale);
                throw new BookException(message);
            }

            var user = this.userRepository.findById(savedToken.getUser().getId())
                    .orElseThrow(() -> {
                    String message = this.messageSource.getMessage("user.not.found", new Object[] { savedToken.getUser() }, locale);
                    return new BookException(message);
            });

            user.setEnabled(true);
            this.userRepository.save(user);
            savedToken.setValidatedAt(LocalDateTime.now());
            this.tokenRepository.save(savedToken);
        } catch (BookException e) {
            throw e;
        } catch (Exception e) {
            log.error("Unexpected error while activated account : {}", token, e);
            String message = this.messageSource.getMessage("unable.to.activated.account", new Object[] { token }, locale);
            throw new BookException(INTERNAL_SERVER_ERROR.value(), message);
        }
    }

    private void sendValidationEmail(User user) throws MessagingException {
        String newToken = generateAndSaveActivationToken(user);
        this.emailService.sendEmail(user.getEmail(), user.fullName(), EmailTemplateName.ACTIVATE_ACCOUNT,
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
        this.tokenRepository.save(token);
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
