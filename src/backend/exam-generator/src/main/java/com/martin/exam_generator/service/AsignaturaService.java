package com.martin.exam_generator.service;

import com.martin.exam_generator.dto.AsignaturaCreateDTO;
import com.martin.exam_generator.dto.AsignaturaDTO;
import com.martin.exam_generator.dto.GradoDTO;
import com.martin.exam_generator.entities.Asignatura;
import com.martin.exam_generator.repository.AsignaturaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class AsignaturaService {

    private final AsignaturaRepository asignaturaRepository;

    public AsignaturaService(AsignaturaRepository asignaturaRepository) {
        this.asignaturaRepository = asignaturaRepository;
    }

    public List<AsignaturaDTO> obtenerAsignaturasDelDocente(Long docenteId) {
        return asignaturaRepository.findByDocenteId(docenteId)
                .stream()
                .map(AsignaturaDTO::fromEntity)
                .collect(Collectors.toList());
    }

    public List<AsignaturaDTO> filtrarAsignaturas(Long docenteId, String criterio) {
        return asignaturaRepository.findByDocenteIdAndCriterio(docenteId, criterio)
                .stream()
                .map(AsignaturaDTO::fromEntity)
                .collect(Collectors.toList());
    }

    public AsignaturaDTO crearAsignatura(AsignaturaCreateDTO dto, Long docenteId) {
        Asignatura asignatura = new Asignatura();
        asignatura.setTitulo(dto.getTitulo());
        asignatura.setCodigo(dto.getCodigo());
        asignatura.setCursoAcademico(dto.getCursoAcademico());
        asignatura.setDocenteId(docenteId);

        Asignatura guardada = asignaturaRepository.save(asignatura);
        return AsignaturaDTO.fromEntity(guardada);
    }

    public boolean existsByCodigoAndDocenteId(String codigo, Long docenteId) {
        return asignaturaRepository.findByDocenteId(docenteId).stream()
                .anyMatch(a -> a.getCodigo().equalsIgnoreCase(codigo));
    }

}