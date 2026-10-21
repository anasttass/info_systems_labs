
package ru.stud.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;

import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.List;

@Configuration
@EnableMethodSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(
            HttpSecurity http
    ) throws Exception {

        http
                .csrf(csrf -> csrf.disable())

                .sessionManagement(session -> session
                        .sessionCreationPolicy(
                                SessionCreationPolicy.STATELESS
                        )
                )

                .authorizeHttpRequests(auth -> auth

                        .requestMatchers(
                                HttpMethod.GET,
                                "/",
                                "/index.html",
                                "/assets/**",
                                "/favicon.ico",
                                "/vite.svg",
                                "/error",
                                "/login",
                                "/register",
                                "/registration",
                                "/ws/humanbeings",
                                "/auth/server-instance"
                        ).permitAll()

                        .requestMatchers(
                                HttpMethod.POST,
                                "/auth/login",
                                "/auth/register"
                        ).permitAll()

                        // Только администратор
                        .requestMatchers("/admin/**")
                        .hasRole("ADMIN")

                        // Остальные запросы требуют авторизации
                        .anyRequest().hasAnyRole("USER", "ADMIN")
                )

                .oauth2ResourceServer(oauth2 ->
                        oauth2.jwt(jwt ->
                                jwt.jwtAuthenticationConverter(
                                        jwtAuthenticationConverter()
                                )
                        )
                );

        return http.build();
    }

    @Bean
    public JwtAuthenticationConverter jwtAuthenticationConverter() {

        JwtAuthenticationConverter converter =
                new JwtAuthenticationConverter();

        // authentication.getName() возвращает логин
        converter.setPrincipalClaimName("login");

        converter.setJwtGrantedAuthoritiesConverter(jwt -> {

            String role = jwt.getClaimAsString("role");

            if ("ADMIN".equals(role)) {
                return List.of(
                        new SimpleGrantedAuthority(
                                "ROLE_ADMIN"
                        )
                );
            }

            if ("GENERAL".equals(role)) {
                return List.of(
                        new SimpleGrantedAuthority(
                                "ROLE_USER"
                        )
                );
            }

            // Неизвестная роль не получает полномочий
            return List.of();
        });

        return converter;
    }
}
