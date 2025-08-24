package com.miempresa.alquileres.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.miempresa.alquileres.model.Rol;
import com.miempresa.alquileres.model.Usuario;
import com.miempresa.alquileres.repository.RolRepository;
import com.miempresa.alquileres.repository.UsuarioRepository;

@Configuration
public class DataLoader {

    @Bean
    CommandLineRunner initData(RolRepository rolRepo,
                               UsuarioRepository usuarioRepo,
                               BCryptPasswordEncoder encoder) {
        return args -> {
            // Roles base
            if (!rolRepo.existsByNombre("ADMIN")) rolRepo.save(new Rol("ADMIN"));
            if (!rolRepo.existsByNombre("USER"))  rolRepo.save(new Rol("USER"));

            // Usuario admin
            if (!usuarioRepo.existsByUsername("admin")) {
                Usuario admin = new Usuario("admin", encoder.encode("Admin123*"), "Administrador", true);
                admin.setCorreo("admin@local"); 
                admin.getRoles().add(rolRepo.findByNombre("ADMIN").orElseThrow());
                admin.getRoles().add(rolRepo.findByNombre("USER").orElseThrow());
                usuarioRepo.save(admin);
            }

            // Usuario operador
            if (!usuarioRepo.existsByUsername("operador")) {
                Usuario op = new Usuario("operador", encoder.encode("Operador123*"), "Operador", true);
                op.setCorreo("operador@local");
                op.getRoles().add(rolRepo.findByNombre("USER").orElseThrow());
                usuarioRepo.save(op);
            }
        };
    }
}
