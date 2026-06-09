package com.martin.exam_generator.dto.config;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ConfiguracionExportDTO {

    private String version;
    private LocalDateTime fechaExportacion;
    private DocenteOrigenDTO docenteOrigen;
    private ElementosExportDTO elementos;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DocenteOrigenDTO {
        private Long id;
        private String nombre;
        private String apellidos;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ElementosExportDTO {
        private List<GradoExportDTO> grados;
        private List<AsignaturaExportDTO> asignaturas;
        private List<AlumnoExportDTO> alumnos;
        private List<PreguntaExportDTO> preguntas;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class GradoExportDTO {
        private Long id;
        private String titulo;
        private String codigo;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class AsignaturaExportDTO {
        private Long id;
        private String titulo;
        private String codigo;
        private String cursoAcademico;
        private List<Long> gradoIds;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class AlumnoExportDTO {
        private Long id;
        private String nombre;
        private String apellidos;
        private String dni;
        private Long gradoId;
        private List<Long> asignaturaIds;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PreguntaExportDTO {
        private Long id;
        private String enunciado;
        private String tema;
        private String dificultad;
        private Long asignaturaId;
        private List<RespuestaExportDTO> respuestas;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class RespuestaExportDTO {
        private String opcion;
        private Boolean esCorrecta;
    }
}