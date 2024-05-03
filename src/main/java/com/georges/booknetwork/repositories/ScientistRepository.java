package com.georges.booknetwork.repositories;

import com.georges.booknetwork.domains.Scientist;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ScientistRepository extends JpaRepository<Scientist, Long> {

    Optional<Scientist> findByName(String name);
}
