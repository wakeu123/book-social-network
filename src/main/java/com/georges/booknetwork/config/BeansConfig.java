package com.georges.booknetwork.config;

import io.swagger.v3.oas.models.parameters.Parameter;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.customizers.OperationCustomizer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.util.Arrays;
import java.util.List;

import static org.springframework.http.HttpHeaders.*;

@Configuration
@RequiredArgsConstructor
public class BeansConfig {

    private final MessageSource messageSource;
    private final UserDetailsService userDetailsService;

    @Value("${application.cors.allowedOrigins}")
    private List<String> allowedOrigins;

    @Bean
    public MessageSource sourceAutoConfiguration() {
        var source = new ReloadableResourceBundleMessageSource();
        source.setBasename("bundle/messages");
        source.setDefaultEncoding("UTF-8");
        return source;
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public CorsFilter corsFilter() {
        final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        final CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOrigins(allowedOrigins);
        config.setAllowedHeaders(Arrays.asList(
                ORIGIN,
                ACCEPT,
                CONTENT_TYPE,
                AUTHORIZATION,
                ACCEPT_LANGUAGE
        ));
        config.setAllowedMethods(Arrays.asList("GET", "POST"));
        source.registerCorsConfiguration("/**", config);
        return new CorsFilter(source);
    }

    @Bean
    public OperationCustomizer customizerHeaderApi() {
        return ((operation, handlerMethod) -> operation.addParametersItem(
                new Parameter()
                        .in("header")
                        .required(true)
                        .name("Accept-Language")
                        .description("Accept-Language")));

    }
}
