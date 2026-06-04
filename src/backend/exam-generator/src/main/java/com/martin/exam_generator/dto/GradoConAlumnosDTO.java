package com.martin.exam_generator.dto;

import com.martin.exam_generator.entities.Grado;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GradoConAlumnosDTO {

    private Long id;
    private String titulo;
    private String codigo;
    private Integer numAlumnos;
}