package com.georges.booknetwork;

import com.georges.booknetwork.domains.Role;
import com.georges.booknetwork.repositories.RoleRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableJpaAuditing
@EnableAsync
@Slf4j
public class BookNetworkApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(BookNetworkApiApplication.class, args);
	}

	@Bean
	public CommandLineRunner start(RoleRepository repository) {
		return args -> {
			if(repository.findByName("USER").isEmpty() && repository.findByName("ADMIN").isEmpty()) {
				repository.save(Role.builder().name("USER").build());
				repository.save(Role.builder().name("ADMIN").build());
			}
		};
	}
}
