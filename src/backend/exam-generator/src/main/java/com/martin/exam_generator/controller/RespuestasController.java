package com.martin.exam_generator.controller;

import com.martin.exam_generator.dto.RespuestaCreateDTO;
import com.martin.exam_generator.dto.RespuestaDTO;
import com.martin.exam_generator.dto.RespuestaUpdateDTO;
import com.martin.exam_generator.security.JwtTokenProvider;
import com.martin.exam_generator.service.PreguntaService;
import com.martin.exam_generator.service.RespuestaService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/respuestas")
public class RespuestasController {

    private final RespuestaService respuestaService;
    private final PreguntaService preguntaService;
    private final JwtTokenProvider jwtTokenProvider;

    public RespuestasController(RespuestaService respuestaService, PreguntaService preguntaService, JwtTokenProvider jwtTokenProvider) {
        this.respuestaService = respuestaService;
        this.preguntaService = preguntaService;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @GetMapping("/pregunta/{preguntaId}")
    public ResponseEntity<List<RespuestaDTO>> listarRespuestasPorPregunta(
            @PathVariable Long preguntaId,
            @RequestHeader("Authorization") String authHeader,
            @RequestParam(required = false) String filtro) {

        Long docenteId = jwtTokenProvider.extractDocenteId(authHeader.replace("Bearer ", ""));

        if (!preguntaService.verificarPreguntaPerteneceADocente(preguntaId, docenteId)) {
            return ResponseEntity.notFound().build();
        }

        List<RespuestaDTO> respuestas;
        if (filtro != null && !filtro.isEmpty()) {
            respuestas = respuestaService.filtrarRespuestasPorPregunta(preguntaId, filtro);
        } else {
            respuestas = respuestaService.obtenerRespuestasPorPregunta(preguntaId);
        }

        return ResponseEntity.ok(respuestas);
    }

    @PostMapping
    public ResponseEntity<RespuestaDTO> crearRespuesta(
            @Valid @RequestBody RespuestaCreateDTO dto,
            @RequestHeader("Authorization") String authHeader) {

        Long docenteId = jwtTokenProvider.extractDocenteId(authHeader.replace("Bearer ", ""));
        RespuestaDTO creada = respuestaService.crearRespuesta(dto, docenteId);
        return ResponseEntity.status(201).body(creada);
    }

    @GetMapping("/{id}")
    public ResponseEntity<RespuestaDTO> obtenerRespuesta(
            @PathVariable Long id,
            @RequestHeader("Authorization") String authHeader) {

        Long docenteId = jwtTokenProvider.extractDocenteId(authHeader.replace("Bearer ", ""));
        RespuestaDTO respuesta = respuestaService.obtenerRespuestaPorId(id, docenteId);
        return ResponseEntity.ok(respuesta);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarRespuesta(
            @PathVariable Long id,
            @RequestHeader("Authorization") String authHeader) {

        Long docenteId = jwtTokenProvider.extractDocenteId(authHeader.replace("Bearer ", ""));
        respuestaService.eliminarRespuesta(id, docenteId);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<RespuestaDTO> actualizarRespuesta(
            @PathVariable Long id,
            @Valid @RequestBody RespuestaUpdateDTO dto,
            @RequestHeader("Authorization") String authHeader) {

        Long docenteId = jwtTokenProvider.extractDocenteId(authHeader.replace("Bearer ", ""));
        RespuestaDTO actualizada = respuestaService.actualizarRespuesta(id, dto, docenteId);
        return ResponseEntity.ok(actualizada);
    }
}
