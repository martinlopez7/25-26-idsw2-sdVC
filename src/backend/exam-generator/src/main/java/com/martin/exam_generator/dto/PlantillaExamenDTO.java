package com.martin.exam_generator.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PlantillaExamenDTO {
    private String id;
    private Long asignaturaId;
    private String tituloAsignatura;
    private String evaluacion;
    private int numPreguntas;
    private Long alumnoId;
    private String claveCorreccion;
    private Boolean asignada;
    private Long gradoId;
}