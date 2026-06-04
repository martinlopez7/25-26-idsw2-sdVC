package com.martin.exam_generator.controller;

import com.martin.exam_generator.dto.GenerarExamenesRequest;
import com.martin.exam_generator.dto.GenerarExamenesResponse;
import com.martin.exam_generator.security.JwtTokenProvider;
import com.martin.exam_generator.service.ExamenService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/examenes")
public class ExamenesController {

    private final ExamenService examenService;
    private final JwtTokenProvider jwtTokenProvider;

    public ExamenesController(ExamenService examenService, JwtTokenProvider jwtTokenProvider) {
        this.examenService = examenService;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @PostMapping("/generar")
    public ResponseEntity<?> generarExamenes(
            @RequestHeader("Authorization") String authHeader,
            @Valid @RequestBody GenerarExamenesRequest request) {

        Long docenteId = jwtTokenProvider.extractDocenteId(authHeader.replace("Bearer ", ""));

        try {
            GenerarExamenesResponse response = examenService.generarExamenes(request, docenteId);
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        } catch (IllegalStateException e) {
            return ResponseEntity.status(409).body(Map.of("error", e.getMessage()));
        } catch (jakarta.persistence.EntityNotFoundException e) {
            return ResponseEntity.status(404).body(Map.of("error", "Asignatura no encontrada"));
        }
    }

    @DeleteMapping("/generar/cancelar")
    public ResponseEntity<?> cancelarGeneracion(
            @RequestHeader("Authorization") String authHeader) {

        examenService.cancelarGeneracion();
        return ResponseEntity.noContent().build();
    }
}