package com.martin.exam_generator.service;

import com.martin.exam_generator.dto.AlumnoDTO;
import com.martin.exam_generator.dto.AsignaturaCreateDTO;
import com.martin.exam_generator.dto.AsignaturaDTO;
import com.martin.exam_generator.dto.AsignaturaUpdateDTO;
import com.martin.exam_generator.dto.GradoDTO;
import com.martin.exam_generator.entities.Alumno;
import com.martin.exam_generator.entities.Asignatura;
import com.martin.exam_generator.entities.Grado;
import com.martin.exam_generator.repository.AsignaturaRepository;
import org.springframework.context.annotation.Lazy;
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
    private final AlumnoService alumnoService;
    private final PreguntaService preguntaService;
    private final GradoService gradoService;

    public AsignaturaService(AsignaturaRepository asignaturaRepository,
                            AlumnoService alumnoService,
                            PreguntaService preguntaService,
                            @Lazy GradoService gradoService) {
        this.asignaturaRepository = asignaturaRepository;
        this.alumnoService = alumnoService;
        this.preguntaService = preguntaService;
        this.gradoService = gradoService;
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
                gradoService.verificarGradoPerteneceAlDocente(gradoId, docenteId);
                grados.add(gradoService.obtenerGradoEntityPorId(gradoId));
            }

            List<Grado> gradosAnteriores = new ArrayList<>(asignatura.getGrados());
            List<Long> nuevosGradoIds = dto.getGradoIds();

            for (Grado gradoAnterior : gradosAnteriores) {
                if (!nuevosGradoIds.contains(gradoAnterior.getId())) {
                    List<AlumnoDTO> alumnosDelGrado = alumnoService.obtenerAlumnosPorGrado(gradoAnterior.getId());
                    for (AlumnoDTO alumno : alumnosDelGrado) {
                        if (asignatura.getAlumnos().stream().anyMatch(a -> a.getId().equals(alumno.getId()))) {
                            alumnoService.desmatricularAlumnoDeAsignatura(alumno.getId(), id);
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

        List<Long> gradoIds = gradoService.obtenerGradoIdsDeAsignatura(asignaturaId);

        if (gradoIds.isEmpty()) {
            return new ArrayList<>();
        }

        return alumnoService.obtenerAlumnosDisponiblesParaAsignatura(docenteId, gradoIds, asignaturaId);
    }

    @Transactional
    public void matricularAlumno(Long asignaturaId, Long alumnoId, Long docenteId) {
        Asignatura asignatura = asignaturaRepository.findById(asignaturaId)
                .orElseThrow(() -> new EntityNotFoundException("Asignatura no encontrada con id: " + asignaturaId));

        if (!asignatura.getDocenteId().equals(docenteId)) {
            throw new EntityNotFoundException("Asignatura no encontrada con id: " + asignaturaId);
        }

        if (!gradoService.verificarAlumnoPerteneceAGradoDeAsignatura(alumnoId, asignaturaId)) {
            throw new IllegalArgumentException("El alumno no pertenece a ningún grado de esta asignatura");
        }

        alumnoService.matricularAlumnoEnAsignatura(alumnoId, asignaturaId);
    }

    @Transactional
    public void desmatricularAlumno(Long asignaturaId, Long alumnoId, Long docenteId) {
        Asignatura asignatura = asignaturaRepository.findById(asignaturaId)
                .orElseThrow(() -> new EntityNotFoundException("Asignatura no encontrada con id: " + asignaturaId));

        if (!asignatura.getDocenteId().equals(docenteId)) {
            throw new EntityNotFoundException("Asignatura no encontrada con id: " + asignaturaId);
        }

        alumnoService.desmatricularAlumnoDeAsignatura(alumnoId, asignaturaId);
    }

    public void eliminarAsignatura(Long id, Long docenteId) {
        Asignatura asignatura = asignaturaRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Asignatura no encontrada con id: " + id));

        if (!asignatura.getDocenteId().equals(docenteId)) {
            throw new EntityNotFoundException("Asignatura no encontrada con id: " + id);
        }

        preguntaService.eliminarPreguntasPorAsignatura(id);

        asignatura.getAlumnos().clear();
        asignatura.getGrados().clear();

        asignaturaRepository.delete(asignatura);
    }

    @Transactional
    public void eliminarRelacionGrado(Long gradoId, Long asignaturaId) {
        Asignatura asignatura = asignaturaRepository.findById(asignaturaId)
                .orElseThrow(() -> new EntityNotFoundException("Asignatura no encontrada con id: " + asignaturaId));

        asignatura.getGrados().removeIf(grado -> grado.getId().equals(gradoId));
        asignaturaRepository.save(asignatura);
    }

    @Transactional
    public void eliminarAsignaturasPorDocenteId(Long docenteId) {
        List<Asignatura> asignaturas = asignaturaRepository.findByDocenteId(docenteId);
        for (Asignatura asignatura : asignaturas) {
            eliminarAsignatura(asignatura.getId(), docenteId);
        }
    }

    public void procesarEliminacionGrado(Long gradoId) {
        List<Asignatura> asignaturas = asignaturaRepository.findByGradosId(gradoId);
        for (Asignatura asignatura : asignaturas) {
            List<Alumno> alumnos = new ArrayList<>(asignatura.getAlumnos());
            for (Alumno alumno : alumnos) {
                if (alumno.getGrado() != null && alumno.getGrado().getId().equals(gradoId)) {
                    alumnoService.desmatricularAlumnoDeAsignatura(alumno.getId(), asignatura.getId());
                    alumnoService.quitarAlumnoDeGrado(gradoId, alumno.getId());
                }
            }
            eliminarRelacionGrado(gradoId, asignatura.getId());
        }
    }
}