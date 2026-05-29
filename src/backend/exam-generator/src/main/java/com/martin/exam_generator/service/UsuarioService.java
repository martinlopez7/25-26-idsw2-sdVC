package com.martin.exam_generator.service;

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
}