package com.martin.exam_generator.controller;

import com.martin.exam_generator.dto.AlumnoCreateDTO;
import com.martin.exam_generator.dto.AlumnoDTO;
import com.martin.exam_generator.security.JwtTokenProvider;
import com.martin.exam_generator.service.AlumnoService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/alumnos")
public class AlumnosController {

    private final AlumnoService alumnoService;
    private final JwtTokenProvider jwtTokenProvider;

    public AlumnosController(AlumnoService alumnoService, JwtTokenProvider jwtTokenProvider) {
        this.alumnoService = alumnoService;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @GetMapping("/mios")
    public ResponseEntity<List<AlumnoDTO>> listarAlumnos(
            @RequestHeader("Authorization") String authHeader,
            @RequestParam(required = false) String filtro) {

        Long docenteId = jwtTokenProvider.extractDocenteId(authHeader.replace("Bearer ", ""));
        List<AlumnoDTO> alumnos;

        if (filtro != null && !filtro.isEmpty()) {
            alumnos = alumnoService.filtrarAlumnos(docenteId, filtro);
        } else {
            alumnos = alumnoService.obtenerAlumnosDelDocente(docenteId);
        }

        return ResponseEntity.ok(alumnos);
    }

    @PostMapping
    public ResponseEntity<AlumnoDTO> crearAlumno(
            @RequestHeader("Authorization") String authHeader,
            @Valid @RequestBody AlumnoCreateDTO dto) {

        Long docenteId = jwtTokenProvider.extractDocenteId(authHeader.replace("Bearer ", ""));
        AlumnoDTO created = alumnoService.crearAlumno(dto, docenteId);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }
}