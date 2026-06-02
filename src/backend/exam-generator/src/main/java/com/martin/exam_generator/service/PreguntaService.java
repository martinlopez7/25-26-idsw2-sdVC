package com.martin.exam_generator.service;

import com.martin.exam_generator.dto.PreguntaDTO;
import com.martin.exam_generator.entities.Pregunta;
import com.martin.exam_generator.repository.PreguntaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.persistence.EntityNotFoundException;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class PreguntaService {

    private final PreguntaRepository preguntaRepository;

    public PreguntaService(PreguntaRepository preguntaRepository) {
        this.preguntaRepository = preguntaRepository;
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

    public boolean verificarPreguntaPerteneceADocente(Long preguntaId, Long docenteId) {
        Pregunta pregunta = preguntaRepository.findById(preguntaId)
                .orElseThrow(() -> new EntityNotFoundException("Pregunta no encontrada con id: " + preguntaId));
        return pregunta.getDocenteId().equals(docenteId);
    }
}