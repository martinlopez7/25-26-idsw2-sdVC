package com.martin.exam_generator.dto;

import com.martin.exam_generator.entities.EvaluacionExamen;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GenerarExamenesRequest {

    @NotNull(message = "ID de asignatura es obligatorio")
    private Long asignaturaId;

    @NotNull(message = "Tipo de evaluación es obligatorio")
    private EvaluacionExamen evaluacion;

    @NotEmpty(message = "Debe seleccionar al menos un tema")
    private List<String> temas;

    @NotNull(message = "Número de preguntas es obligatorio")
    @Positive(message = "El número de preguntas debe ser mayor que 0")
    private Integer numPreguntas;

    @NotNull(message = "Configuración por grado es obligatoria")
    @Valid
    private Map<Long, ConfigGradoDTO> configPorGrado;
}