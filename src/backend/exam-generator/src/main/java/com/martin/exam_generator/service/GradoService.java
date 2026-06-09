package com.martin.exam_generator.service;

import com.martin.exam_generator.dto.alumno.AlumnoDTO;
import com.martin.exam_generator.dto.grado.GradoCreateDTO;
import com.martin.exam_generator.dto.grado.GradoDTO;
import com.martin.exam_generator.dto.grado.GradoUpdateDTO;
import com.martin.exam_generator.entities.Grado;
import com.martin.exam_generator.repository.GradoRepository;
import com.martin.exam_generator.repository.AsignaturaRepository;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.persistence.EntityNotFoundException;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class GradoService {

    private final GradoRepository gradoRepository;
    private final AsignaturaRepository asignaturaRepository;
    private final AlumnoService alumnoService;
    private final AsignaturaService asignaturaService;

    public GradoService(GradoRepository gradoRepository,
                        AsignaturaRepository asignaturaRepository,
                        AlumnoService alumnoService,
                        @Lazy AsignaturaService asignaturaService) {
        this.gradoRepository = gradoRepository;
        this.asignaturaRepository = asignaturaRepository;
        this.alumnoService = alumnoService;
        this.asignaturaService = asignaturaService;
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

    public GradoDTO crearGrado(GradoCreateDTO dto, Long docenteId) {
        if (gradoRepository.existsByDocenteIdAndCodigo(docenteId, dto.getCodigo())) {
            throw new IllegalArgumentException("Ya existe un grado con este código");
        }

        Grado grado = new Grado();
        grado.setTitulo(dto.getTitulo());
        grado.setCodigo(dto.getCodigo());
        grado.setDocenteId(docenteId);

        Grado saved = gradoRepository.save(grado);
        return GradoDTO.fromEntity(saved);
    }

    public GradoDTO obtenerGradoPorId(Long id) {
        Grado grado = gradoRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Grado no encontrado"));
        List<AlumnoDTO> alumnos = alumnoService.obtenerAlumnosPorGrado(id);
        GradoDTO dto = GradoDTO.fromEntity(grado);
        dto.setAlumnos(alumnos);
        return dto;
    }

    public List<AlumnoDTO> obtenerAlumnosSinGrado(Long docenteId) {
        return alumnoService.obtenerAlumnosSinGrado(docenteId);
    }

    @Transactional
    public AlumnoDTO anadirAlumnoAGrado(Long gradoId, Long alumnoId, Long docenteId) {
        Grado grado = gradoRepository.findById(gradoId)
                .orElseThrow(() -> new EntityNotFoundException("Grado no encontrado con id: " + gradoId));
        return alumnoService.anadirAlumnoAGrado(alumnoId, docenteId, grado);
    }

    @Transactional
    public void quitarAlumnoDeGrado(Long gradoId, Long alumnoId) {
        alumnoService.quitarAlumnoDeGrado(gradoId, alumnoId);
    }

    @Transactional
    public GradoDTO actualizarGrado(Long id, GradoUpdateDTO dto) {
        Grado grado = gradoRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Grado no encontrado"));

        validarUnicidadTituloYCodigo(id, dto.getTitulo(), dto.getCodigo(), grado.getDocenteId());

        grado.setTitulo(dto.getTitulo());
        grado.setCodigo(dto.getCodigo());

        Grado saved = gradoRepository.save(grado);

        List<AlumnoDTO> alumnos = alumnoService.obtenerAlumnosPorGrado(id);
        GradoDTO result = GradoDTO.fromEntity(saved);
        result.setAlumnos(alumnos);
        return result;
    }

    private void validarUnicidadTituloYCodigo(Long gradoId, String titulo, String codigo, Long docenteId) {
        List<Grado> grados = gradoRepository.findByDocenteId(docenteId);
        for (Grado g : grados) {
            if (g.getId().equals(gradoId)) continue;
            if (g.getTitulo().equalsIgnoreCase(titulo)) {
                throw new IllegalArgumentException("Ya existe un grado con este título");
            }
            if (g.getCodigo().equalsIgnoreCase(codigo)) {
                throw new IllegalArgumentException("Ya existe un grado con este código");
            }
        }
    }

    @Transactional
    public void eliminarGrado(Long id, Long docenteId) {
        Grado grado = gradoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Grado no encontrado con id: " + id));

        if (!grado.getDocenteId().equals(docenteId)) {
            throw new EntityNotFoundException("Grado no encontrado con id: " + id);
        }

        asignaturaService.procesarEliminacionGrado(id);

        gradoRepository.delete(grado);
    }

    public List<Long> obtenerGradoIdsDeAsignatura(Long asignaturaId) {
        return asignaturaRepository.findById(asignaturaId)
                .orElseThrow(() -> new EntityNotFoundException("Asignatura no encontrada"))
                .getGrados().stream()
                .map(Grado::getId)
                .collect(Collectors.toList());
    }

    public boolean verificarAlumnoPerteneceAGradoDeAsignatura(Long alumnoId, Long asignaturaId) {
        List<Long> gradoIds = obtenerGradoIdsDeAsignatura(asignaturaId);
        return alumnoService.verificarAlumnoPerteneceAGrado(alumnoId, gradoIds);
    }

    public void verificarGradoPerteneceAlDocente(Long gradoId, Long docenteId) {
        Grado grado = gradoRepository.findById(gradoId)
                .orElseThrow(() -> new EntityNotFoundException("Grado no encontrado con id: " + gradoId));
        if (!grado.getDocenteId().equals(docenteId)) {
            throw new EntityNotFoundException("Grado no encontrado con id: " + gradoId);
        }
    }

    public Grado obtenerGradoEntityPorId(Long gradoId) {
        return gradoRepository.findById(gradoId)
                .orElseThrow(() -> new EntityNotFoundException("Grado no encontrado con id: " + gradoId));
    }

    @Transactional
    public void eliminarGradosPorDocenteId(Long docenteId) {
        gradoRepository.deleteByDocenteId(docenteId);
    }
}