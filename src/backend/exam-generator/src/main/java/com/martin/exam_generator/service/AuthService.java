package com.martin.exam_generator.service;

import com.martin.exam_generator.dto.JwtResponse;
import com.martin.exam_generator.dto.LoginRequest;
import com.martin.exam_generator.entities.Usuario;
import com.martin.exam_generator.repository.UsuarioRepository;
import com.martin.exam_generator.security.JwtTokenProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;

    public AuthService(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder, JwtTokenProvider jwtTokenProvider) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    public JwtResponse login(LoginRequest request) {
        Optional<Usuario> usuarioOpt = usuarioRepository.findByUsername(request.getUsername());

        if (usuarioOpt.isEmpty()) {
            throw new BadCredentialsException("Credenciales inválidas");
        }

        Usuario usuario = usuarioOpt.get();

        if (!passwordEncoder.matches(request.getPassword(), usuario.getPassword())) {
            throw new BadCredentialsException("Credenciales inválidas");
        }

        String token = jwtTokenProvider.generateToken(
                usuario.getUsername(),
                usuario.getTipoActor().name()
        );

        return new JwtResponse(token, usuario.getTipoActor().name());
    }

    public void logout(String authHeader) {
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            jwtTokenProvider.invalidateToken(token);
        }
    }
}
