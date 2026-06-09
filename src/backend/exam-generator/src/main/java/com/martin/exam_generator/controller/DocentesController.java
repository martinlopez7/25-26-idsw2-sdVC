package com.martin.exam_generator.controller;

import com.martin.exam_generator.dto.usuario.UsuarioCreateDTO;
import com.martin.exam_generator.dto.usuario.UsuarioDTO;
import com.martin.exam_generator.dto.usuario.UsuarioUpdateDTO;
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

    @GetMapping("/{id}")
    public ResponseEntity<UsuarioDTO> obtenerDocente(@PathVariable Long id) {
        UsuarioDTO docente = usuarioService.obtenerDocentePorId(id);
        return ResponseEntity.ok(docente);
    }

    @PostMapping
    public ResponseEntity<UsuarioDTO> crearDocente(@Valid @RequestBody UsuarioCreateDTO dto) {
        UsuarioDTO created = usuarioService.crearDocente(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UsuarioDTO> actualizarDocente(@PathVariable Long id, @Valid @RequestBody UsuarioUpdateDTO dto) {
        UsuarioDTO updated = usuarioService.actualizarDocente(id, dto);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarDocente(@PathVariable Long id) {
        usuarioService.eliminarDocente(id);
        return ResponseEntity.noContent().build();
    }
}
