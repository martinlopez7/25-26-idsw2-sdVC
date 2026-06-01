package com.martin.exam_generator.dto;

import com.martin.exam_generator.entities.Asignatura;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AsignaturaDTO {

    private Long id;
    private String titulo;
    private String codigo;
    private String cursoAcademico;

    public static AsignaturaDTO fromEntity(Asignatura asignatura) {
        return new AsignaturaDTO(
                asignatura.getId(),
                asignatura.getTitulo(),
                asignatura.getCodigo(),
                asignatura.getCursoAcademico()
        );
    }
}