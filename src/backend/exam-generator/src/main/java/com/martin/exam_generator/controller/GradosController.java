package com.martin.exam_generator.controller;

import com.martin.exam_generator.dto.AlumnoDTO;
import com.martin.exam_generator.dto.GradoCreateDTO;
import com.martin.exam_generator.dto.GradoDTO;
import com.martin.exam_generator.dto.GradoUpdateDTO;
import com.martin.exam_generator.security.JwtTokenProvider;
import com.martin.exam_generator.service.GradoService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/grados")
public class GradosController {

    private final GradoService gradoService;
    private final JwtTokenProvider jwtTokenProvider;

    public GradosController(GradoService gradoService, JwtTokenProvider jwtTokenProvider) {
        this.gradoService = gradoService;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @GetMapping("/mios")
    public ResponseEntity<List<GradoDTO>> listarGrados(
            @RequestHeader("Authorization") String authHeader,
            @RequestParam(required = false) String filtro) {

        Long docenteId = jwtTokenProvider.extractDocenteId(authHeader.replace("Bearer ", ""));
        List<GradoDTO> grados;

        if (filtro != null && !filtro.isEmpty()) {
            grados = gradoService.filtrarGradosDelDocente(docenteId, filtro);
        } else {
            grados = gradoService.obtenerGradosDelDocente(docenteId);
        }

        return ResponseEntity.ok(grados);
    }

    @GetMapping("/{id}")
    public ResponseEntity<GradoDTO> obtenerGradoPorId(@PathVariable Long id) {
        GradoDTO grado = gradoService.obtenerGradoPorId(id);
        return ResponseEntity.ok(grado);
    }

    @PostMapping
    public ResponseEntity<GradoDTO> crearGrado(
            @RequestHeader("Authorization") String authHeader,
            @Valid @RequestBody GradoCreateDTO dto) {

        Long docenteId = jwtTokenProvider.extractDocenteId(authHeader.replace("Bearer ", ""));
        GradoDTO created = gradoService.crearGrado(dto, docenteId);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<GradoDTO> actualizarGrado(
            @PathVariable Long id,
            @Valid @RequestBody GradoUpdateDTO dto) {
        GradoDTO updated = gradoService.actualizarGrado(id, dto);
        return ResponseEntity.ok(updated);
    }

    @GetMapping("/sin-grado")
    public ResponseEntity<List<AlumnoDTO>> listarAlumnosSinGrado(
            @RequestHeader("Authorization") String authHeader) {
        Long docenteId = jwtTokenProvider.extractDocenteId(authHeader.replace("Bearer ", ""));
        List<AlumnoDTO> alumnos = gradoService.obtenerAlumnosSinGrado(docenteId);
        return ResponseEntity.ok(alumnos);
    }

    @PutMapping("/{gradoId}/alumnos/{alumnoId}")
    public ResponseEntity<AlumnoDTO> anadirAlumnoAGrado(
            @PathVariable Long gradoId,
            @PathVariable Long alumnoId) {
        AlumnoDTO alumno = gradoService.anadirAlumnoAGrado(gradoId, alumnoId);
        return ResponseEntity.ok(alumno);
    }

    @DeleteMapping("/{gradoId}/alumnos/{alumnoId}")
    public ResponseEntity<Void> quitarAlumnoDeGrado(
            @PathVariable Long gradoId,
            @PathVariable Long alumnoId) {
        gradoService.quitarAlumnoDeGrado(gradoId, alumnoId);
        return ResponseEntity.noContent().build();
    }
}