package com.martin.exam_generator.controller;

import com.martin.exam_generator.dto.config.ConfiguracionExportDTO;
import com.martin.exam_generator.security.JwtTokenProvider;
import com.martin.exam_generator.service.ConfiguracionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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

    @PostMapping("/importar")
    public ResponseEntity<Void> importarConfiguracionGlobal(
            @RequestHeader("Authorization") String authHeader,
            @RequestParam("archivo") MultipartFile archivo) {
        String token = authHeader.replace("Bearer ", "");
        Long docenteId = jwtTokenProvider.extractDocenteId(token);

        try {
            com.fasterxml.jackson.databind.ObjectMapper mapper = new com.fasterxml.jackson.databind.ObjectMapper();
            mapper.findAndRegisterModules();
            ConfiguracionExportDTO configuracion = mapper.readValue(archivo.getBytes(), ConfiguracionExportDTO.class);
            configuracionService.importarConfiguracionGlobal(configuracion, docenteId);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().build();
        }
    }
}