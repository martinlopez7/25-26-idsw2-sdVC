package com.martin.exam_generator.controller;

import com.martin.exam_generator.dto.AlumnoDTO;
import com.martin.exam_generator.dto.AsignaturaCreateDTO;
import com.martin.exam_generator.dto.AsignaturaDTO;
import com.martin.exam_generator.dto.AsignaturaUpdateDTO;
import com.martin.exam_generator.exception.EntityNotFoundException;
import com.martin.exam_generator.security.JwtTokenProvider;
import com.martin.exam_generator.service.AsignaturaService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/asignaturas")
public class AsignaturasController {

    private final AsignaturaService asignaturaService;
    private final JwtTokenProvider jwtTokenProvider;

    public AsignaturasController(AsignaturaService asignaturaService, JwtTokenProvider jwtTokenProvider) {
        this.asignaturaService = asignaturaService;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @GetMapping("/mias")
    public ResponseEntity<List<AsignaturaDTO>> listarAsignaturas(
            @RequestHeader("Authorization") String authHeader,
            @RequestParam(required = false) String filtro) {

        Long docenteId = jwtTokenProvider.extractDocenteId(authHeader.replace("Bearer ", ""));
        List<AsignaturaDTO> asignaturas;

        if (filtro != null && !filtro.isEmpty()) {
            asignaturas = asignaturaService.filtrarAsignaturas(docenteId, filtro);
        } else {
            asignaturas = asignaturaService.obtenerAsignaturasDelDocente(docenteId);
        }

        return ResponseEntity.ok(asignaturas);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AsignaturaDTO> obtenerAsignatura(
            @RequestHeader("Authorization") String authHeader,
            @PathVariable Long id) {

        Long docenteId = jwtTokenProvider.extractDocenteId(authHeader.replace("Bearer ", ""));

        try {
            AsignaturaDTO asignatura = asignaturaService.obtenerAsignaturaPorId(id, docenteId);
            return ResponseEntity.ok(asignatura);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    public ResponseEntity<?> crearAsignatura(
            @RequestHeader("Authorization") String authHeader,
            @Valid @RequestBody AsignaturaCreateDTO dto) {

        Long docenteId = jwtTokenProvider.extractDocenteId(authHeader.replace("Bearer ", ""));

        if (asignaturaService.existsByCodigoAndDocenteId(dto.getCodigo(), docenteId)) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", "Ya existe una asignatura con ese código"));
        }

        AsignaturaDTO creada = asignaturaService.crearAsignatura(dto, docenteId);
        return ResponseEntity.status(HttpStatus.CREATED).body(creada);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> actualizarAsignatura(
            @RequestHeader("Authorization") String authHeader,
            @PathVariable Long id,
            @Valid @RequestBody AsignaturaUpdateDTO dto) {

        Long docenteId = jwtTokenProvider.extractDocenteId(authHeader.replace("Bearer ", ""));

        try {
            AsignaturaDTO actualizada = asignaturaService.actualizarAsignatura(id, dto, docenteId);
            return ResponseEntity.ok(actualizada);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping("/{id}/alumnos-disponibles")
    public ResponseEntity<List<AlumnoDTO>> obtenerAlumnosDisponibles(
            @RequestHeader("Authorization") String authHeader,
            @PathVariable Long id) {

        Long docenteId = jwtTokenProvider.extractDocenteId(authHeader.replace("Bearer ", ""));

        try {
            List<AlumnoDTO> alumnos = asignaturaService.getAlumnosDisponibles(id, docenteId);
            return ResponseEntity.ok(alumnos);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/{id}/alumnos/{alumnoId}")
    public ResponseEntity<?> matricularAlumno(
            @RequestHeader("Authorization") String authHeader,
            @PathVariable Long id,
            @PathVariable Long alumnoId) {

        Long docenteId = jwtTokenProvider.extractDocenteId(authHeader.replace("Bearer ", ""));

        try {
            AsignaturaDTO asignatura = asignaturaService.addAlumno(id, alumnoId, docenteId);
            return ResponseEntity.ok(asignatura);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @DeleteMapping("/{id}/alumnos/{alumnoId}")
    public ResponseEntity<?> desmatricularAlumno(
            @RequestHeader("Authorization") String authHeader,
            @PathVariable Long id,
            @PathVariable Long alumnoId) {

        Long docenteId = jwtTokenProvider.extractDocenteId(authHeader.replace("Bearer ", ""));

        try {
            AsignaturaDTO asignatura = asignaturaService.removeAlumno(id, alumnoId, docenteId);
            return ResponseEntity.ok(asignatura);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminarAsignatura(
            @RequestHeader("Authorization") String authHeader,
            @PathVariable Long id) {

        Long docenteId = jwtTokenProvider.extractDocenteId(authHeader.replace("Bearer ", ""));

        try {
            asignaturaService.eliminarAsignatura(id, docenteId);
            return ResponseEntity.noContent().build();
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }
}