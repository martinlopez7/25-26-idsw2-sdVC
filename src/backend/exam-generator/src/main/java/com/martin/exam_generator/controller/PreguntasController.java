package com.martin.exam_generator.controller;

import com.martin.exam_generator.dto.PreguntaCreateDTO;
import com.martin.exam_generator.dto.PreguntaDTO;
import com.martin.exam_generator.dto.PreguntaUpdateDTO;
import com.martin.exam_generator.security.JwtTokenProvider;
import com.martin.exam_generator.service.PreguntaService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/preguntas")
public class PreguntasController {

    private final PreguntaService preguntaService;
    private final JwtTokenProvider jwtTokenProvider;

    public PreguntasController(PreguntaService preguntaService, JwtTokenProvider jwtTokenProvider) {
        this.preguntaService = preguntaService;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @PostMapping
    public ResponseEntity<PreguntaDTO> crearPregunta(
            @Valid @RequestBody PreguntaCreateDTO dto,
            @RequestHeader("Authorization") String authHeader) {

        Long docenteId = jwtTokenProvider.extractDocenteId(authHeader.replace("Bearer ", ""));
        PreguntaDTO preguntaCreada = preguntaService.crearPregunta(dto, docenteId);
        return ResponseEntity.status(HttpStatus.CREATED).body(preguntaCreada);
    }

    @GetMapping("/mias")
    public ResponseEntity<List<PreguntaDTO>> listarPreguntas(
            @RequestHeader("Authorization") String authHeader,
            @RequestParam(required = false) String filtro) {

        Long docenteId = jwtTokenProvider.extractDocenteId(authHeader.replace("Bearer ", ""));
        List<PreguntaDTO> preguntas;

        if (filtro != null && !filtro.isEmpty()) {
            preguntas = preguntaService.filtrarPreguntasDelDocente(docenteId, filtro);
        } else {
            preguntas = preguntaService.obtenerPreguntasDelDocente(docenteId);
        }

        return ResponseEntity.ok(preguntas);
    }

    @GetMapping("/asignatura/{asignaturaId}")
    public ResponseEntity<List<PreguntaDTO>> listarPreguntasPorAsignatura(
            @PathVariable Long asignaturaId,
            @RequestParam(required = false) String filtro) {

        List<PreguntaDTO> preguntas;

        if (filtro != null && !filtro.isEmpty()) {
            preguntas = preguntaService.filtrarPreguntasPorAsignatura(asignaturaId, filtro);
        } else {
            preguntas = preguntaService.obtenerPreguntasPorAsignatura(asignaturaId);
        }

        return ResponseEntity.ok(preguntas);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PreguntaDTO> obtenerPregunta(@PathVariable Long id) {
        PreguntaDTO pregunta = preguntaService.obtenerPreguntaPorId(id);
        return ResponseEntity.ok(pregunta);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PreguntaDTO> actualizarPregunta(
            @PathVariable Long id,
            @Valid @RequestBody PreguntaUpdateDTO dto,
            @RequestHeader("Authorization") String authHeader) {

        Long docenteId = jwtTokenProvider.extractDocenteId(authHeader.replace("Bearer ", ""));
        PreguntaDTO preguntaActualizada = preguntaService.actualizarPregunta(id, dto, docenteId);
        return ResponseEntity.ok(preguntaActualizada);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarPregunta(
            @PathVariable Long id,
            @RequestHeader("Authorization") String authHeader) {

        Long docenteId = jwtTokenProvider.extractDocenteId(authHeader.replace("Bearer ", ""));
        preguntaService.eliminarPregunta(id, docenteId);
        return ResponseEntity.noContent().build();
    }
}
