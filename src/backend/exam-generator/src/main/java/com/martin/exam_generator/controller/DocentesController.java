package com.martin.exam_generator.controller;

import com.martin.exam_generator.dto.UsuarioCreateDTO;
import com.martin.exam_generator.dto.UsuarioDTO;
import com.martin.exam_generator.service.UsuarioService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/docentes")
public class DocentesController {

    private final UsuarioService usuarioService;

    public DocentesController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @GetMapping
    public ResponseEntity<List<UsuarioDTO>> listarDocentes(@RequestParam(required = false) String criterio) {
        List<UsuarioDTO> docentes;
        if (criterio != null && !criterio.isEmpty()) {
            docentes = usuarioService.filtrarDocentes(criterio);
        } else {
            docentes = usuarioService.obtenerTodosLosDocentes();
        }
        return ResponseEntity.ok(docentes);
    }

    @PostMapping
    public ResponseEntity<UsuarioDTO> crearDocente(@Valid @RequestBody UsuarioCreateDTO dto) {
        UsuarioDTO created = usuarioService.crearDocente(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }
}