package com.martin.exam_generator.service;

import com.martin.exam_generator.dto.GradoDTO;
import com.martin.exam_generator.entities.Grado;
import com.martin.exam_generator.repository.GradoRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class GradoService {

    private final GradoRepository gradoRepository;

    public GradoService(GradoRepository gradoRepository) {
        this.gradoRepository = gradoRepository;
    }

    public List<GradoDTO> obtenerGradosDelDocente(Long docenteId) {
        List<Grado> grados = gradoRepository.findByDocenteId(docenteId);
        return grados.stream()
                .map(GradoDTO::fromEntity)
                .collect(Collectors.toList());
    }

    public List<GradoDTO> filtrarGradosDelDocente(Long docenteId, String criterio) {
        List<Grado> grados = gradoRepository.findByDocenteIdAndCriterio(docenteId, criterio);
        return grados.stream()
                .map(GradoDTO::fromEntity)
                .collect(Collectors.toList());
    }
}