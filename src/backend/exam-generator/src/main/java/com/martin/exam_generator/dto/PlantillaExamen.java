package com.martin.exam_generator.dto;

import com.martin.exam_generator.entities.EvaluacionExamen;
import com.martin.exam_generator.entities.Pregunta;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PlantillaExamen implements Serializable {

    private static final long serialVersionUID = 1L;

    private String id;
    private Long asignaturaId;
    private String tituloAsignatura;
    private EvaluacionExamen evaluacion;
    private List<PreguntaSeleccionada> preguntas;
    private Boolean clavePendiente;
    private Long alumnoId;
    private String claveCorreccion;
    private Boolean asignada;
    private LocalDateTime fechaGeneracion;
    private Long gradoId;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PreguntaSeleccionada implements Serializable {
        private static final long serialVersionUID = 1L;

        private Long preguntaId;
        private String enunciado;
        private String tema;
        private Pregunta.Dificultad dificultad;
        private List<RespuestaSeleccionada> respuestas;
        private Integer posicionOriginal;
        private Integer posicionAleatoria;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class RespuestaSeleccionada implements Serializable {
        private static final long serialVersionUID = 1L;

        private Long respuestaId;
        private String opcion;
        private Boolean esCorrecta;
        private Integer posicionOriginal;
        private Integer posicionAleatoria;
    }

    public static PlantillaExamen crear(Long asignaturaId, String tituloAsignatura, EvaluacionExamen evaluacion,
                                         List<PreguntaSeleccionada> preguntas, Long gradoId) {
        PlantillaExamen plantilla = new PlantillaExamen();
        plantilla.setId(UUID.randomUUID().toString());
        plantilla.setAsignaturaId(asignaturaId);
        plantilla.setTituloAsignatura(tituloAsignatura);
        plantilla.setEvaluacion(evaluacion);
        plantilla.setPreguntas(preguntas);
        plantilla.setClavePendiente(true);
        plantilla.setAsignada(false);
        plantilla.setFechaGeneracion(LocalDateTime.now());
        plantilla.setGradoId(gradoId);
        return plantilla;
    }
}