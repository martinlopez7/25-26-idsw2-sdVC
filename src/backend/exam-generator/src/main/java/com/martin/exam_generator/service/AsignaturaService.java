package com.martin.exam_generator.service;

import com.martin.exam_generator.dto.AlumnoDTO;
import com.martin.exam_generator.dto.AsignaturaCreateDTO;
import com.martin.exam_generator.dto.AsignaturaDTO;
import com.martin.exam_generator.dto.AsignaturaUpdateDTO;
import com.martin.exam_generator.dto.GradoDTO;
import com.martin.exam_generator.entities.Alumno;
import com.martin.exam_generator.entities.Asignatura;
import com.martin.exam_generator.entities.Grado;
import com.martin.exam_generator.repository.AlumnoRepository;
import com.martin.exam_generator.repository.AsignaturaRepository;
import com.martin.exam_generator.repository.GradoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.persistence.EntityNotFoundException;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class AsignaturaService {

    private final AsignaturaRepository asignaturaRepository;
    private final GradoRepository gradoRepository;
    private final AlumnoRepository alumnoRepository;

    public AsignaturaService(AsignaturaRepository asignaturaRepository,
                            GradoRepository gradoRepository,
                            AlumnoRepository alumnoRepository) {
        this.asignaturaRepository = asignaturaRepository;
        this.gradoRepository = gradoRepository;
        this.alumnoRepository = alumnoRepository;
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

    public AsignaturaDTO obtenerAsignaturaPorId(Long id, Long docenteId) {
        Asignatura asignatura = asignaturaRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Asignatura no encontrada con id: " + id));

        if (!asignatura.getDocenteId().equals(docenteId)) {
            throw new EntityNotFoundException("Asignatura no encontrada con id: " + id);
        }

        AsignaturaDTO dto = new AsignaturaDTO();
        dto.setId(asignatura.getId());
        dto.setTitulo(asignatura.getTitulo());
        dto.setCodigo(asignatura.getCodigo());
        dto.setCursoAcademico(asignatura.getCursoAcademico());

        if (asignatura.getGrados() != null) {
            dto.setGrados(asignatura.getGrados().stream()
                    .map(GradoDTO::fromEntity)
                    .collect(Collectors.toList()));
        }

        if (asignatura.getAlumnos() != null) {
            dto.setAlumnos(asignatura.getAlumnos().stream()
                    .map(AlumnoDTO::fromEntity)
                    .collect(Collectors.toList()));
        }

        return dto;
    }

    public AsignaturaDTO actualizarAsignatura(Long id, AsignaturaUpdateDTO dto, Long docenteId) {
        Asignatura asignatura = asignaturaRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Asignatura no encontrada con id: " + id));

        if (!asignatura.getDocenteId().equals(docenteId)) {
            throw new EntityNotFoundException("Asignatura no encontrada con id: " + id);
        }

        if (asignaturaRepository.existsByCodigoAndDocenteIdAndIdNot(dto.getCodigo(), docenteId, id)) {
            throw new IllegalArgumentException("Ya existe una asignatura con este código");
        }

        asignatura.setTitulo(dto.getTitulo());
        asignatura.setCodigo(dto.getCodigo());
        asignatura.setCursoAcademico(dto.getCursoAcademico());

        if (dto.getGradoIds() != null) {
            List<Grado> grados = new ArrayList<>();
            for (Long gradoId : dto.getGradoIds()) {
                Grado grado = gradoRepository.findById(gradoId)
                        .orElseThrow(() -> new EntityNotFoundException("Grado no encontrado con id: " + gradoId));
                if (!grado.getDocenteId().equals(docenteId)) {
                    throw new EntityNotFoundException("Grado no encontrado con id: " + gradoId);
                }
                grados.add(grado);
            }

            List<Grado> gradosAnteriores = new ArrayList<>(asignatura.getGrados());
            List<Long> nuevosGradoIds = dto.getGradoIds();

            for (Grado gradoAnterior : gradosAnteriores) {
                if (!nuevosGradoIds.contains(gradoAnterior.getId())) {
                    List<Alumno> alumnosDelGrado = alumnoRepository.findByGradoId(gradoAnterior.getId());
                    for (Alumno alumno : alumnosDelGrado) {
                        if (asignatura.getAlumnos().contains(alumno)) {
                            asignatura.getAlumnos().remove(alumno);
                            alumno.getAsignaturas().remove(asignatura);
                        }
                    }
                }
            }

            asignatura.setGrados(grados);
        }

        Asignatura saved = asignaturaRepository.save(asignatura);
        return obtenerAsignaturaPorId(saved.getId(), docenteId);
    }

    public List<AlumnoDTO> getAlumnosDisponibles(Long asignaturaId, Long docenteId) {
        Asignatura asignatura = asignaturaRepository.findById(asignaturaId)
                .orElseThrow(() -> new EntityNotFoundException("Asignatura no encontrada con id: " + asignaturaId));

        if (!asignatura.getDocenteId().equals(docenteId)) {
            throw new EntityNotFoundException("Asignatura no encontrada con id: " + asignaturaId);
        }

        List<Long> gradoIds = asignatura.getGrados().stream()
                .map(Grado::getId)
                .collect(Collectors.toList());

        if (gradoIds.isEmpty()) {
            return new ArrayList<>();
        }

        List<Alumno> alumnos = alumnoRepository.findAvailableAlumnosForAsignatura(docenteId, asignaturaId, gradoIds);
        return alumnos.stream()
                .map(AlumnoDTO::fromEntity)
                .collect(Collectors.toList());
    }

    @Transactional
    public AsignaturaDTO addAlumno(Long asignaturaId, Long alumnoId, Long docenteId) {
        Asignatura asignatura = asignaturaRepository.findById(asignaturaId)
                .orElseThrow(() -> new EntityNotFoundException("Asignatura no encontrada con id: " + asignaturaId));

        if (!asignatura.getDocenteId().equals(docenteId)) {
            throw new EntityNotFoundException("Asignatura no encontrada con id: " + asignaturaId);
        }

        Alumno alumno = alumnoRepository.findById(alumnoId)
                .orElseThrow(() -> new EntityNotFoundException("Alumno no encontrado con id: " + alumnoId));

        if (!alumno.getDocenteId().equals(docenteId)) {
            throw new EntityNotFoundException("Alumno no encontrado con id: " + alumnoId);
        }

        if (!asignatura.getGrados().contains(alumno.getGrado())) {
            throw new IllegalArgumentException("El alumno no pertenece a ningún grado de esta asignatura");
        }

        if (!asignatura.getAlumnos().contains(alumno)) {
            asignatura.getAlumnos().add(alumno);
            alumno.getAsignaturas().add(asignatura);
            asignaturaRepository.save(asignatura);
        }

        return obtenerAsignaturaPorId(asignaturaId, docenteId);
    }

    @Transactional
    public AsignaturaDTO removeAlumno(Long asignaturaId, Long alumnoId, Long docenteId) {
        Asignatura asignatura = asignaturaRepository.findById(asignaturaId)
                .orElseThrow(() -> new EntityNotFoundException("Asignatura no encontrada con id: " + asignaturaId));

        if (!asignatura.getDocenteId().equals(docenteId)) {
            throw new EntityNotFoundException("Asignatura no encontrada con id: " + asignaturaId);
        }

        Alumno alumno = alumnoRepository.findById(alumnoId)
                .orElseThrow(() -> new EntityNotFoundException("Alumno no encontrado con id: " + alumnoId));

        if (!alumno.getDocenteId().equals(docenteId)) {
            throw new EntityNotFoundException("Alumno no encontrado con id: " + alumnoId);
        }

        if (asignatura.getAlumnos().contains(alumno)) {
            asignatura.getAlumnos().remove(alumno);
            alumno.getAsignaturas().remove(asignatura);
            asignaturaRepository.save(asignatura);
        }

        return obtenerAsignaturaPorId(asignaturaId, docenteId);
    }
}