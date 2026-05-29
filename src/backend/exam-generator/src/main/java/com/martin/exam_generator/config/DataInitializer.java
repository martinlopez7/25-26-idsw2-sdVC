package com.martin.exam_generator.config;

import com.martin.exam_generator.entities.Usuario;
import com.martin.exam_generator.repository.UsuarioRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class DataInitializer {

    @Bean
    CommandLineRunner initDatabase(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder) {
        return args -> {
            if (usuarioRepository.findByUsername("admin").isEmpty()) {
                Usuario admin = new Usuario();
                admin.setUsername("admin");
                admin.setPassword(passwordEncoder.encode("admin123"));
                admin.setTipoActor(Usuario.TipoActor.ADMINISTRADOR_INSTITUCIONAL);
                usuarioRepository.save(admin);
            }
        };
    }
}
