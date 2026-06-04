package com.martin.exam_generator.service;

import com.martin.exam_generator.dto.PreguntaCreateDTO;
import com.martin.exam_generator.dto.PreguntaDTO;
import com.martin.exam_generator.dto.PreguntaUpdateDTO;
import com.martin.exam_generator.entities.Pregunta;
import com.martin.exam_generator.repository.PreguntaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.persistence.EntityNotFoundException;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class PreguntaService {

    private final PreguntaRepository preguntaRepository;
    private final RespuestaService respuestaService;

    public PreguntaService(PreguntaRepository preguntaRepository, RespuestaService respuestaService) {
        this.preguntaRepository = preguntaRepository;
        this.respuestaService = respuestaService;
    }

    @Transactional
    public PreguntaDTO crearPregunta(PreguntaCreateDTO dto, Long docenteId) {
        if (preguntaRepository.existsByAsignaturaIdAndEnunciadoIgnoreCase(dto.getAsignaturaId(), dto.getEnunciado())) {
            throw new IllegalArgumentException("Ya existe una pregunta con este enunciado en la asignatura");
        }

        Pregunta pregunta = dto.toEntity();
        pregunta.setDocenteId(docenteId);

        Pregunta guardada = preguntaRepository.save(pregunta);
        return PreguntaDTO.fromEntity(guardada);
    }

    public List<PreguntaDTO> obtenerPreguntasPorAsignaturaYTemas(Long asignaturaId, List<String> temas) {
        List<Pregunta> preguntas = preguntaRepository.findByAsignaturaIdAndTemaIn(asignaturaId, temas);

        List<PreguntaDTO> dtos = new ArrayList<>();
        for (Pregunta pregunta : preguntas) {
            if (pregunta.getHabilitada()) {
                dtos.add(PreguntaDTO.fromEntity(pregunta));
            }
        }
        return dtos;
    }

    public Map<String, List<PreguntaDTO>> clasificarPreguntasPorDificultad(List<PreguntaDTO> preguntasDTO) {
        return preguntasDTO.stream()
                .collect(Collectors.groupingBy(PreguntaDTO::getDificultad));
    }

    public List<PreguntaDTO> obtenerPreguntasDelDocente(Long docenteId) {
        return preguntaRepository.findByDocenteId(docenteId)
                .stream()
                .map(PreguntaDTO::fromEntity)
                .collect(Collectors.toList());
    }

    public List<PreguntaDTO> filtrarPreguntasDelDocente(Long docenteId, String criterio) {
        return preguntaRepository.findByDocenteIdAndCriterio(docenteId, criterio)
                .stream()
                .map(PreguntaDTO::fromEntity)
                .collect(Collectors.toList());
    }

    public List<PreguntaDTO> obtenerPreguntasPorAsignatura(Long asignaturaId) {
        return preguntaRepository.findByAsignaturaId(asignaturaId)
                .stream()
                .map(PreguntaDTO::fromEntity)
                .collect(Collectors.toList());
    }

    public List<PreguntaDTO> filtrarPreguntasPorAsignatura(Long asignaturaId, String criterio) {
        return preguntaRepository.findByAsignaturaIdAndCriterio(asignaturaId, criterio)
                .stream()
                .map(PreguntaDTO::fromEntity)
                .collect(Collectors.toList());
    }

    public PreguntaDTO obtenerPreguntaPorId(Long id) {
        Pregunta pregunta = preguntaRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Pregunta no encontrada con id: " + id));
        return PreguntaDTO.fromEntity(pregunta);
    }

    public Pregunta obtenerPreguntaEntityPorId(Long id) {
        return preguntaRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Pregunta no encontrada con id: " + id));
    }

    public boolean verificarPreguntaPerteneceADocente(Long preguntaId, Long docenteId) {
        Pregunta pregunta = preguntaRepository.findById(preguntaId)
                .orElseThrow(() -> new EntityNotFoundException("Pregunta no encontrada con id: " + preguntaId));
        return pregunta.getDocenteId().equals(docenteId);
    }

    @Transactional
    public PreguntaDTO actualizarPregunta(Long id, PreguntaUpdateDTO dto, Long docenteId) {
        Pregunta pregunta = preguntaRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Pregunta no encontrada con id: " + id));

        if (!pregunta.getDocenteId().equals(docenteId)) {
            throw new EntityNotFoundException("Pregunta no encontrada con id: " + id);
        }

        if (preguntaRepository.existsByAsignaturaIdAndEnunciadoIgnoreCaseAndIdNot(
                pregunta.getAsignaturaId(), dto.getEnunciado(), id)) {
            throw new IllegalArgumentException("Ya existe una pregunta con este enunciado en la asignatura");
        }

        pregunta.setEnunciado(dto.getEnunciado());
        pregunta.setTema(dto.getTema());
        pregunta.setDificultad(dto.getDificultad());
        pregunta.setHabilitada(dto.getHabilitada());

        if (dto.getRespuestas() != null && !dto.getRespuestas().isEmpty()) {
            respuestaService.procesarRespuestas(id, dto.getRespuestas());
        }

        Pregunta guardada = preguntaRepository.save(pregunta);
        return PreguntaDTO.fromEntity(guardada);
    }

    @Transactional
    public void eliminarPregunta(Long preguntaId, Long docenteId) {
        Pregunta pregunta = preguntaRepository.findById(preguntaId)
                .orElseThrow(() -> new EntityNotFoundException("Pregunta no encontrada con id: " + preguntaId));

        if (!pregunta.getDocenteId().equals(docenteId)) {
            throw new EntityNotFoundException("Pregunta no encontrada con id: " + preguntaId);
        }

        preguntaRepository.delete(pregunta);
    }

    @Transactional
    public void eliminarPreguntasPorAsignatura(Long asignaturaId) {
        List<Pregunta> preguntas = preguntaRepository.findByAsignaturaId(asignaturaId);
        for (Pregunta pregunta : preguntas) {
            pregunta.getRespuestas().clear();
            preguntaRepository.delete(pregunta);
        }
    }

    @Transactional
    public void eliminarPreguntasPorDocenteId(Long docenteId) {
        preguntaRepository.deleteByDocenteId(docenteId);
    }
}
