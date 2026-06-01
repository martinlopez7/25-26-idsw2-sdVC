package com.martin.exam_generator.controller;

import com.martin.exam_generator.dto.AsignaturaCreateDTO;
import com.martin.exam_generator.dto.AsignaturaDTO;
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
}