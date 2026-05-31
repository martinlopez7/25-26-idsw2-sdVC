package com.martin.exam_generator.service;

import com.martin.exam_generator.dto.AlumnoDTO;
import com.martin.exam_generator.dto.GradoCreateDTO;
import com.martin.exam_generator.dto.GradoDTO;
import com.martin.exam_generator.dto.GradoUpdateDTO;
import com.martin.exam_generator.entities.Alumno;
import com.martin.exam_generator.entities.Grado;
import com.martin.exam_generator.repository.AlumnoRepository;
import com.martin.exam_generator.repository.GradoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class GradoService {

    private final GradoRepository gradoRepository;
    private final AlumnoRepository alumnoRepository;

    public GradoService(GradoRepository gradoRepository, AlumnoRepository alumnoRepository) {
        this.gradoRepository = gradoRepository;
        this.alumnoRepository = alumnoRepository;
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
        List<Alumno> alumnos = alumnoRepository.findByGradoId(id);
        GradoDTO dto = GradoDTO.fromEntity(grado);
        dto.setAlumnos(alumnos.stream().map(AlumnoDTO::fromEntity).collect(Collectors.toList()));
        return dto;
    }

    public List<AlumnoDTO> obtenerAlumnosSinGrado(Long docenteId) {
        List<Alumno> alumnos = alumnoRepository.findByDocenteIdAndGradoIsNull(docenteId);
        return alumnos.stream().map(AlumnoDTO::fromEntity).collect(Collectors.toList());
    }

    @Transactional
    public AlumnoDTO anadirAlumnoAGrado(Long gradoId, Long alumnoId) {
        Alumno alumno = alumnoRepository.findById(alumnoId)
                .orElseThrow(() -> new IllegalArgumentException("Alumno no encontrado"));
        alumno.setGradoId(gradoId);
        Alumno saved = alumnoRepository.save(alumno);
        return AlumnoDTO.fromEntity(saved);
    }

    @Transactional
    public void quitarAlumnoDeGrado(Long gradoId, Long alumnoId) {
        Alumno alumno = alumnoRepository.findById(alumnoId)
                .orElseThrow(() -> new IllegalArgumentException("Alumno no encontrado"));
        if (!gradoId.equals(alumno.getGradoId())) {
            throw new IllegalArgumentException("El alumno no pertenece a este grado");
        }
        alumno.setGradoId(null);
        alumnoRepository.save(alumno);
    }

    @Transactional
    public GradoDTO actualizarGrado(Long id, GradoUpdateDTO dto) {
        Grado grado = gradoRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Grado no encontrado"));

        validarUnicidadTituloYCodigo(id, dto.getTitulo(), dto.getCodigo(), grado.getDocenteId());

        grado.setTitulo(dto.getTitulo());
        grado.setCodigo(dto.getCodigo());

        Grado saved = gradoRepository.save(grado);

        List<Alumno> alumnos = alumnoRepository.findByGradoId(id);
        GradoDTO result = GradoDTO.fromEntity(saved);
        result.setAlumnos(alumnos.stream().map(AlumnoDTO::fromEntity).collect(Collectors.toList()));
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
}