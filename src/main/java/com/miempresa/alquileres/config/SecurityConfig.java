package com.miempresa.alquileres.config;

import com.miempresa.alquileres.security.CustomUserDetailsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableMethodSecurity // para @PreAuthorize en controladores/servicios
public class SecurityConfig {

    private final CustomUserDetailsService userDetailsService;

    public SecurityConfig(CustomUserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @Bean
    BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    DaoAuthenticationProvider authProvider() {
        DaoAuthenticationProvider p = new DaoAuthenticationProvider();
        p.setUserDetailsService(userDetailsService);
        p.setPasswordEncoder(passwordEncoder());
        return p;
    }

    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests(auth -> auth
                // recursos estáticos y login públicos
                .requestMatchers("/css/**", "/js/**", "/images/**", "/webjars/**", "/login", "/error").permitAll()

                // vistas públicas si tienes
                .requestMatchers("/", "/inicio", "/home").authenticated()

                // módulos por rol (ajusta a tus rutas reales)
                .requestMatchers("/equipos/**", "/alquileres/**", "/menu_empresas", "/menu_proveedores").hasAnyRole("USER","ADMIN")
                .requestMatchers("/equipos/crear", "/equipos/guardar", "/usuarios/**").hasRole("ADMIN")

                .anyRequest().authenticated()
            )
            .formLogin(form -> form
                .loginPage("/login")          // GET -> muestra login.html
                .loginProcessingUrl("/login") // POST del form
                .defaultSuccessUrl("/", true) // a dónde redirigir tras login
                .permitAll()
            )
            .logout(logout -> logout
                .logoutUrl("/logout")
                .logoutSuccessUrl("/login?logout")
                .permitAll()
            )
            .exceptionHandling(ex -> ex
                .accessDeniedPage("/403")
            )
            .csrf(Customizer.withDefaults());

        http.authenticationProvider(authProvider());
        return http.build();
    }
}
