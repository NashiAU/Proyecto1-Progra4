package com.proyecto1.proyecto1progra4.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(customizer -> customizer
                        .requestMatchers("/", "/com/proyecto1/proyecto1progra4/presentation/prestamos/list",
                                "/com/proyecto1/proyecto1progra4/presentation/prestamos/search",
                                "/css/**", "/images/**").permitAll()
                        .requestMatchers("/com/proyecto1/proyecto1progra4/presentation/prestamos/show",
                                "/com/proyecto1/proyecto1progra4/presentation/prestamos/create",
                                "/com/proyecto1/proyecto1progra4/presentation/prestamos/edit/**",
                                "/com/proyecto1/proyecto1progra4/presentation/prestamos/delete/**",
                                "/com/proyecto1/proyecto1progra4/presentation/prestamos/update").hasAuthority("ADM")
                        .anyRequest().authenticated()
                ).formLogin(customizer -> customizer
                        .loginPage("/login")
                        .permitAll()
                        .defaultSuccessUrl("/com/proyecto1/proyecto1progra4/presentation/prestamos/list")
                ).logout(customizer -> customizer
                        .permitAll()
                        .logoutSuccessUrl("/")
                ).exceptionHandling(customizer -> customizer
                        .accessDeniedPage("/notAuthorized")
                ).csrf( customizer -> customizer.disable());
        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() { return new BCryptPasswordEncoder(); }
}