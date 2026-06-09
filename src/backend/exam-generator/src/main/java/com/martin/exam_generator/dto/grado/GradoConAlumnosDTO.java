package com.martin.exam_generator.dto.grado;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GradoConAlumnosDTO {

    private Long id;
    private String titulo;
    private String codigo;
    private Integer numAlumnos;
}