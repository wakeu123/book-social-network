package com.georges.booknetwork.repositories;

import com.georges.booknetwork.domains.Token;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TokenRepository extends JpaRepository<Token, Long> {

    Optional<Token> findByUserToken(String token);
}
