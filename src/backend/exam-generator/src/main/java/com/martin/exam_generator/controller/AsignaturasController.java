package com.martin.exam_generator.controller;

import com.martin.exam_generator.dto.AsignaturaDTO;
import com.martin.exam_generator.security.JwtTokenProvider;
import com.martin.exam_generator.service.AsignaturaService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
        List<AsignaturaDTO> asignaturas = asignaturaService.obtenerAsignaturasDelDocente(docenteId);
        AsignaturaDTO asignatura = asignaturas.stream()
                .filter(a -> a.getId().equals(id))
                .findFirst()
                .orElse(null);

        if (asignatura == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(asignatura);
    }
}