package com.martin.exam_generator.service;

import com.martin.exam_generator.dto.AlumnoDTO;
import com.martin.exam_generator.entities.Alumno;
import com.martin.exam_generator.repository.AlumnoRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AlumnoService {

    private final AlumnoRepository alumnoRepository;

    public AlumnoService(AlumnoRepository alumnoRepository) {
        this.alumnoRepository = alumnoRepository;
    }

    public List<AlumnoDTO> obtenerAlumnosDelDocente(Long docenteId) {
        List<Alumno> alumnos = alumnoRepository.findByDocenteId(docenteId);
        return alumnos.stream()
                .map(AlumnoDTO::fromEntity)
                .collect(Collectors.toList());
    }

    public List<AlumnoDTO> filtrarAlumnos(Long docenteId, String criterio) {
        List<Alumno> alumnos = alumnoRepository.findByDocenteIdAndCriterio(docenteId, criterio);
        return alumnos.stream()
                .map(AlumnoDTO::fromEntity)
                .collect(Collectors.toList());
    }
}