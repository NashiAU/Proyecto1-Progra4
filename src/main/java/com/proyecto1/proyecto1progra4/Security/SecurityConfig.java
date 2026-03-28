package com.proyecto1.proyecto1progra4.Security;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // Recomendado en apps SSR con login por formulario:
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED))

                .authorizeHttpRequests(auth -> auth
                        // ===== RUTAS PÚBLICAS =====
                        .requestMatchers(
                                "/",
                                "/home",
                                "/login",
                                "/login**",
                                "/dologin",
                                "/error",
                                "/notAuthorized",
                                "/Administrador/**",
                                "/Empresa/**",
                                "/Oferente/**",
                                "/css/**", "/Styles/**", "/images/**"
                        ).permitAll()


                        // Si estas son las URLs reales de tu app (ojo: parecen nombres de paquete),
                        // entonces déjalas así. Si no lo son, cámbialas por /prestamos/list etc.
                        .requestMatchers(
                                "/com/proyecto1/proyecto1progra4/presentation/prestamos/list",
                                "/com/proyecto1/proyecto1progra4/presentation/prestamos/search"
                        ).permitAll()

                        // ===== REQUIERE AUTORIDAD ADM (módulo préstamos) =====
                        .requestMatchers(
                                "/com/proyecto1/proyecto1progra4/presentation/prestamos/show",
                                "/com/proyecto1/proyecto1progra4/presentation/prestamos/create",
                                "/com/proyecto1/proyecto1progra4/presentation/prestamos/edit/**",
                                "/com/proyecto1/proyecto1progra4/presentation/prestamos/delete/**",
                                "/com/proyecto1/proyecto1progra4/presentation/prestamos/update"
                        ).hasAuthority("ADM")

                        // ===== RUTAS PROTEGIDAS POR ROL =====
                        // Corregido: "Admintrador" -> "Administrador" y se usa /** para cubrir subrutas
                        .requestMatchers("/Administrador/**").hasAuthority("ADMIN")
                        .requestMatchers("/Empresa/**").hasAuthority("EMPRESA")
                        .requestMatchers("/Oferente/**").hasAuthority("OFERENTE")

                        // ===== TODO LO DEMÁS REQUIERE LOGIN =====
                        .anyRequest().authenticated()
                )

                .formLogin(form -> form
                        .loginPage("/login")
                        .loginProcessingUrl("/doLogin")
                        .failureUrl("/login?fallo=1")
                        .successHandler(this::roleAwareSuccessHandler)
                        .permitAll()
                )

                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/")
                        .invalidateHttpSession(true)
                        .clearAuthentication(true)
                        .permitAll()
                )

                .exceptionHandling(ex -> ex
                        .accessDeniedPage("/notAuthorized")
                )

                // Si no estás usando CSRF tokens en forms, esto evita bloqueos.
                .csrf(csrf -> csrf.disable());

        return http.build();
    }

    private void roleAwareSuccessHandler(jakarta.servlet.http.HttpServletRequest request,
                                         HttpServletResponse response,
                                         Authentication authentication) throws java.io.IOException {
        boolean isAdmin = authentication.getAuthorities().stream()
                .anyMatch(a -> "ADMIN".equals(a.getAuthority()));
        boolean isEmpresa = authentication.getAuthorities().stream()
                .anyMatch(a -> "EMPRESA".equals(a.getAuthority()));
        boolean isOferente = authentication.getAuthorities().stream()
                .anyMatch(a -> "OFERENTE".equals(a.getAuthority()));

        if (isAdmin) {
            response.sendRedirect("/Administrador/dashboard");
            return;
        }
        if (isEmpresa) {
            response.sendRedirect("/Empresa/dashboard");
            return;
        }
        if (isOferente) {
            response.sendRedirect("/Oferente/dashboard");
            return;
        }
        response.sendRedirect("/home");
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}