package com.martin.exam_generator.controller;

import com.martin.exam_generator.dto.GradoCreateDTO;
import com.martin.exam_generator.dto.GradoDTO;
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

    @PostMapping
    public ResponseEntity<GradoDTO> crearGrado(
            @RequestHeader("Authorization") String authHeader,
            @Valid @RequestBody GradoCreateDTO dto) {

        Long docenteId = jwtTokenProvider.extractDocenteId(authHeader.replace("Bearer ", ""));
        GradoDTO created = gradoService.crearGrado(dto, docenteId);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }
}