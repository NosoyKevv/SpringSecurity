package com.security.SpringSecurity.Config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/v1/index2").permitAll() // Acceso sin autenticación
                        .anyRequest().authenticated() // El resto requiere autenticación
                )
                .formLogin(login -> login
                        .permitAll()
                        .successHandler(successHandler()) // Redirección después de iniciar sesión
                )
                .sessionManagement(sessionManagement -> sessionManagement
                        .sessionCreationPolicy(SessionCreationPolicy.ALWAYS) // Política de creación de sesión
                        .sessionConcurrency(sessionConcurrency -> sessionConcurrency
                                .maximumSessions(1)
                                .expiredUrl("/login")
                                .sessionRegistry(sessionRegistry()) // Se llama a `sessionRegistry()` en lugar de inyectarlo
                        )
                );

        return http.build();
    }


    @Bean
    public SessionRegistry sessionRegistry() {
        return new SessionRegistryImpl();
    }

    public AuthenticationSuccessHandler successHandler() {
        return ((request, response, auth) -> response.sendRedirect("/v1/session"));
    }
}
