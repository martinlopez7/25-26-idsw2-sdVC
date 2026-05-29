package com.martin.exam_generator.service;

import com.martin.exam_generator.dto.UsuarioCreateDTO;
import com.martin.exam_generator.dto.UsuarioDTO;
import com.martin.exam_generator.dto.UsuarioUpdateDTO;
import com.martin.exam_generator.entities.Usuario;
import com.martin.exam_generator.exception.EntityNotFoundException;
import com.martin.exam_generator.repository.UsuarioRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    public UsuarioService(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
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
        docente.setPassword(passwordEncoder.encode(dto.getPassword()));
        docente.setTipoActor(Usuario.TipoActor.DOCENTE);
        docente.setNombre(dto.getNombre());
        docente.setApellidos(dto.getApellidos());
        docente.setDni(dto.getDni());
        docente.setEmail(dto.getEmail());

        Usuario saved = usuarioRepository.save(docente);
        return UsuarioDTO.fromEntity(saved);
    }

    public UsuarioDTO obtenerDocentePorId(Long id) {
        Usuario docente = usuarioRepository.findById(id)
                .filter(u -> u.getTipoActor() == Usuario.TipoActor.DOCENTE)
                .orElseThrow(() -> new EntityNotFoundException("Docente no encontrado con id: " + id));
        return UsuarioDTO.fromEntity(docente);
    }

    public UsuarioDTO actualizarDocente(Long id, UsuarioUpdateDTO dto) {
        Usuario docente = usuarioRepository.findById(id)
                .filter(u -> u.getTipoActor() == Usuario.TipoActor.DOCENTE)
                .orElseThrow(() -> new EntityNotFoundException("Docente no encontrado con id: " + id));

        if (!docente.getUsername().equals(dto.getUsername()) && usuarioRepository.existsByUsername(dto.getUsername())) {
            throw new IllegalArgumentException("El nombre de usuario ya existe");
        }
        if (!docente.getDni().equals(dto.getDni()) && usuarioRepository.existsByDni(dto.getDni())) {
            throw new IllegalArgumentException("El DNI ya está registrado");
        }
        if (!docente.getEmail().equals(dto.getEmail()) && usuarioRepository.existsByEmail(dto.getEmail())) {
            throw new IllegalArgumentException("El email ya está registrado");
        }

        docente.setNombre(dto.getNombre());
        docente.setApellidos(dto.getApellidos());
        docente.setDni(dto.getDni());
        docente.setUsername(dto.getUsername());
        docente.setEmail(dto.getEmail());

        if (dto.getPassword() != null && !dto.getPassword().isEmpty()) {
            if (dto.getPassword().length() < 6) {
                throw new IllegalArgumentException("La contraseña debe tener al menos 6 caracteres");
            }
            docente.setPassword(passwordEncoder.encode(dto.getPassword()));
        }

        Usuario saved = usuarioRepository.save(docente);
        return UsuarioDTO.fromEntity(saved);
    }

    public void eliminarDocente(Long id) {
        Usuario docente = usuarioRepository.findById(id)
                .filter(u -> u.getTipoActor() == Usuario.TipoActor.DOCENTE)
                .orElseThrow(() -> new EntityNotFoundException("Docente no encontrado con id: " + id));
        usuarioRepository.delete(docente);
    }
}
