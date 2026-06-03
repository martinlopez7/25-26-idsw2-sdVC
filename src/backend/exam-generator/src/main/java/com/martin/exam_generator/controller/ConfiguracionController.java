package com.martin.exam_generator.controller;

import com.martin.exam_generator.dto.ConfiguracionExportDTO;
import com.martin.exam_generator.security.JwtTokenProvider;
import com.martin.exam_generator.service.ConfiguracionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/configuracion")
public class ConfiguracionController {

    private final ConfiguracionService configuracionService;
    private final JwtTokenProvider jwtTokenProvider;

    public ConfiguracionController(ConfiguracionService configuracionService, JwtTokenProvider jwtTokenProvider) {
        this.configuracionService = configuracionService;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @GetMapping("/exportar")
    public ResponseEntity<ConfiguracionExportDTO> exportarConfiguracionGlobal(
            @RequestHeader("Authorization") String authHeader) {
        String token = authHeader.replace("Bearer ", "");
        Long docenteId = jwtTokenProvider.extractDocenteId(token);
        ConfiguracionExportDTO configuracion = configuracionService.exportarConfiguracionGlobal(docenteId);
        return ResponseEntity.ok(configuracion);
    }
}