package com.georges.booknetwork.securities.services.impls;

import com.georges.booknetwork.domains.User;
import com.georges.booknetwork.domains.request.RegistrationRequest;
import com.georges.booknetwork.exceptions.BookException;
import com.georges.booknetwork.repositories.UserRepository;
import com.georges.booknetwork.securities.services.AuthenticationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import java.util.Locale;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {

    private final MessageSource messageSource;
    private final UserRepository userRepository;

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
            //validateBeforeSave(request, locale);
            User user = new User();
            user.setFirstname(request.getFirstname());
            user.setLastname(request.getLastname());
            user.setEmail(request.getEmail());
            user.setDateOfBirth(request.getDateOfBirth());
            request.setId(userRepository.save(user).getId());
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
}
