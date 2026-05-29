package com.martin.exam_generator.service;

import com.martin.exam_generator.dto.UsuarioCreateDTO;
import com.martin.exam_generator.dto.UsuarioDTO;
import com.martin.exam_generator.entities.Usuario;
import com.martin.exam_generator.repository.UsuarioRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;

    public UsuarioService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    public List<UsuarioDTO> obtenerTodosLosDocentes() {
        List<Usuario> docentes = usuarioRepository.findByTipoActor(Usuario.TipoActor.DOCENTE);
        return docentes.stream()
                .map(UsuarioDTO::fromEntity)
                .collect(Collectors.toList());
    }

    public List<UsuarioDTO> filtrarDocentes(String criterio) {
        List<Usuario> docentes = usuarioRepository.findByTipoActorAndCriterio(
                Usuario.TipoActor.DOCENTE, criterio);
        return docentes.stream()
                .map(UsuarioDTO::fromEntity)
                .collect(Collectors.toList());
    }

    public UsuarioDTO crearDocente(UsuarioCreateDTO dto) {
        if (usuarioRepository.existsByUsername(dto.getUsername())) {
            throw new IllegalArgumentException("El nombre de usuario ya existe");
        }
        if (usuarioRepository.existsByDni(dto.getDni())) {
            throw new IllegalArgumentException("El DNI ya está registrado");
        }
        if (usuarioRepository.existsByEmail(dto.getEmail())) {
            throw new IllegalArgumentException("El email ya está registrado");
        }

        Usuario docente = new Usuario();
        docente.setUsername(dto.getUsername());
        docente.setPassword(dto.getPassword());
        docente.setTipoActor(Usuario.TipoActor.DOCENTE);
        docente.setNombre(dto.getNombre());
        docente.setApellidos(dto.getApellidos());
        docente.setDni(dto.getDni());
        docente.setEmail(dto.getEmail());

        Usuario saved = usuarioRepository.save(docente);
        return UsuarioDTO.fromEntity(saved);
    }
}