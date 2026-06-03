package com.martin.exam_generator.service;

import com.martin.exam_generator.dto.ConfiguracionExportDTO;
import com.martin.exam_generator.dto.ConfiguracionExportDTO.*;
import com.martin.exam_generator.entities.Pregunta;
import com.martin.exam_generator.entities.Usuario;
import com.martin.exam_generator.repository.*;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ConfiguracionService {

    private final GradoRepository gradoRepository;
    private final AsignaturaRepository asignaturaRepository;
    private final AlumnoRepository alumnoRepository;
    private final PreguntaRepository preguntaRepository;
    private final RespuestaRepository respuestaRepository;
    private final UsuarioRepository usuarioRepository;

    public ConfiguracionService(
            GradoRepository gradoRepository,
            AsignaturaRepository asignaturaRepository,
            AlumnoRepository alumnoRepository,
            PreguntaRepository preguntaRepository,
            RespuestaRepository respuestaRepository,
            UsuarioRepository usuarioRepository) {
        this.gradoRepository = gradoRepository;
        this.asignaturaRepository = asignaturaRepository;
        this.alumnoRepository = alumnoRepository;
        this.preguntaRepository = preguntaRepository;
        this.respuestaRepository = respuestaRepository;
        this.usuarioRepository = usuarioRepository;
    }

    public ConfiguracionExportDTO exportarConfiguracionGlobal(Long docenteId) {
        Usuario docente = usuarioRepository.findById(docenteId)
                .orElseThrow(() -> new RuntimeException("Docente no encontrado"));

        List<GradoExportDTO> grados = gradoRepository.findByDocenteId(docenteId).stream()
                .map(g -> new GradoExportDTO(g.getId(), g.getTitulo(), g.getCodigo()))
                .collect(Collectors.toList());

        List<AsignaturaExportDTO> asignaturas = asignaturaRepository.findByDocenteId(docenteId).stream()
                .map(a -> new AsignaturaExportDTO(
                        a.getId(),
                        a.getTitulo(),
                        a.getCodigo(),
                        a.getCursoAcademico(),
                        a.getGrados() != null ?
                                a.getGrados().stream().map(g -> g.getId()).collect(Collectors.toList()) :
                                java.util.Collections.emptyList()
                ))
                .collect(Collectors.toList());

        List<AlumnoExportDTO> alumnos = alumnoRepository.findByDocenteId(docenteId).stream()
                .map(a -> new AlumnoExportDTO(
                        a.getId(),
                        a.getNombre(),
                        a.getApellidos(),
                        a.getDni(),
                        a.getGrado() != null ? a.getGrado().getId() : null,
                        a.getAsignaturas() != null ?
                                a.getAsignaturas().stream().map(as -> as.getId()).collect(Collectors.toList()) :
                                java.util.Collections.emptyList()
                ))
                .collect(Collectors.toList());

        List<PreguntaExportDTO> preguntas = preguntaRepository.findByDocenteId(docenteId).stream()
                .map(this::mapPreguntaToExport)
                .collect(Collectors.toList());

        ElementosExportDTO elementos = new ElementosExportDTO(grados, asignaturas, alumnos, preguntas);

        return new ConfiguracionExportDTO(
                "1.0",
                LocalDateTime.now(),
                new DocenteOrigenDTO(docente.getId(), docente.getNombre(), docente.getApellidos()),
                elementos
        );
    }

    private PreguntaExportDTO mapPreguntaToExport(Pregunta pregunta) {
        List<RespuestaExportDTO> respuestas = pregunta.getRespuestas().stream()
                .map(r -> new RespuestaExportDTO(r.getOpcion(), r.getEsCorrecta()))
                .collect(Collectors.toList());

        return new PreguntaExportDTO(
                pregunta.getId(),
                pregunta.getEnunciado(),
                pregunta.getTema(),
                pregunta.getDificultad().name(),
                pregunta.getAsignaturaId(),
                respuestas
        );
    }
}