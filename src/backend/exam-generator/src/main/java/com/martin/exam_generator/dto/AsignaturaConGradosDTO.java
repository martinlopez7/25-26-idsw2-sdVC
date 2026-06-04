package com.martin.exam_generator.dto;

import com.martin.exam_generator.entities.Asignatura;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AsignaturaConGradosDTO {

    private Long id;
    private String titulo;
    private String codigo;
    private String cursoAcademico;
    private List<GradoConAlumnosDTO> grados = new ArrayList<>();
    private List<AlumnoDTO> alumnos = new ArrayList<>();
}