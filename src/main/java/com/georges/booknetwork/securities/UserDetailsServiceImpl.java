package com.georges.booknetwork.securities;

import java.util.Locale;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.context.MessageSource;
import com.georges.booknetwork.exceptions.BookException;
import com.georges.booknetwork.repositories.UserRepository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final MessageSource messageSource;
    private final UserRepository userRepository;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String userEmail) throws BookException {
        return userRepository.findByEmail(userEmail)
                .orElseThrow(() -> {
                    String message = this.messageSource.getMessage("user.not.found", new Object[] { userEmail },
                            Locale.getDefault());
                    return new BookException(message);
                });
    }
}
