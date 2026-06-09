package com.martin.exam_generator.controller;

import com.martin.exam_generator.dto.alumno.AlumnoDTO;
import com.martin.exam_generator.dto.asignacion.AsignarExamenRequest;
import com.martin.exam_generator.dto.asignacion.AsignarTodosRequest;
import com.martin.exam_generator.dto.correccion.ResultadoCorreccionDTO;
import com.martin.exam_generator.dto.generacion.GenerarExamenesRequest;
import com.martin.exam_generator.dto.generacion.GenerarExamenesResponse;
import com.martin.exam_generator.dto.generacion.PlantillaExamenDTO;
import com.martin.exam_generator.dto.grado.AlumnoPorGradoDTO;
import com.martin.exam_generator.dto.grado.GradoDTO;
import com.martin.exam_generator.security.JwtTokenProvider;
import com.martin.exam_generator.service.CorreccionService;
import com.martin.exam_generator.service.ExamenService;
import jakarta.validation.Valid;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/examenes")
public class ExamenesController {

    private final ExamenService examenService;
    private final CorreccionService correccionService;
    private final JwtTokenProvider jwtTokenProvider;

    public ExamenesController(ExamenService examenService, CorreccionService correccionService, JwtTokenProvider jwtTokenProvider) {
        this.examenService = examenService;
        this.correccionService = correccionService;
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

    @GetMapping("/plantillas")
    public ResponseEntity<?> obtenerPlantillas(
            @RequestHeader("Authorization") String authHeader) {

        List<PlantillaExamenDTO> plantillas = examenService.obtenerPlantillas();
        return ResponseEntity.ok(plantillas);
    }

    @GetMapping("/alumnos-por-grado")
    public ResponseEntity<?> obtenerAlumnosPorGrado(
            @RequestHeader("Authorization") String authHeader,
            @RequestParam Long asignaturaId) {

        Long docenteId = jwtTokenProvider.extractDocenteId(authHeader.replace("Bearer ", ""));

        try {
            Map<GradoDTO, List<AlumnoDTO>> alumnosPorGrado = examenService.obtenerAlumnosPorGrado(asignaturaId, docenteId);

            List<AlumnoPorGradoDTO> result = alumnosPorGrado.entrySet().stream()
                    .map(entry -> {
                        AlumnoPorGradoDTO dto = new AlumnoPorGradoDTO();
                        dto.setGradoId(entry.getKey().getId());
                        dto.setGradoNombre(entry.getKey().getTitulo());
                        dto.setAlumnos(entry.getValue());
                        return dto;
                    })
                    .collect(Collectors.toList());

            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @PostMapping("/asignar")
    public ResponseEntity<?> asignarExamen(
            @RequestHeader("Authorization") String authHeader,
            @Valid @RequestBody AsignarExamenRequest request) {

        try {
            examenService.asignarExamen(request.getPlantillaId(), request.getAlumnoId());
            return ResponseEntity.ok(Map.of("mensaje", "Examen asignado correctamente"));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        } catch (IllegalStateException e) {
            return ResponseEntity.status(409).body(Map.of("error", e.getMessage()));
        }
    }

    @PostMapping("/asignar-todos")
    public ResponseEntity<?> asignarTodos(
            @RequestHeader("Authorization") String authHeader,
            @Valid @RequestBody AsignarTodosRequest request) {

        try {
            int asignadas = examenService.asignarTodos(request.getAsignaciones());
            return ResponseEntity.ok(Map.of("mensaje", "Exámenes asignados correctamente", "totalAsignadas", asignadas));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        } catch (IllegalStateException e) {
            return ResponseEntity.status(409).body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping("/asignados/pdf-lote")
    public ResponseEntity<?> descargarPdfLote(
            @RequestHeader("Authorization") String authHeader) {

        try {
            byte[] zipBytes = examenService.generarZipExamenes();

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
            headers.setContentDispositionFormData("attachment", "examenes.zip");

            return ResponseEntity.ok()
                    .headers(headers)
                    .body(zipBytes);
        } catch (IllegalStateException e) {
            return ResponseEntity.status(409).body(Map.of("error", e.getMessage()));
        }
    }

    @DeleteMapping("/plantillas")
    public ResponseEntity<?> cancelarAsignacion(
            @RequestHeader("Authorization") String authHeader) {

        examenService.cancelarGeneracion();
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/corregir")
    public ResponseEntity<?> corregirExamenes(
            @RequestHeader("Authorization") String authHeader,
            @RequestParam("archivo") MultipartFile archivo) {

        if (archivo.isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of("error", "El archivo PDF no puede estar vacío"));
        }

        String contentType = archivo.getContentType();
        if (contentType == null || !contentType.equals("application/pdf")) {
            return ResponseEntity.badRequest().body(Map.of("error", "El archivo debe ser un PDF"));
        }

        try {
            List<ResultadoCorreccionDTO> resultados = correccionService.procesarPdf(archivo);
            return ResponseEntity.ok(resultados);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
}