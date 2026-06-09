package com.martin.exam_generator.dto.asignatura;

import com.martin.exam_generator.dto.alumno.AlumnoDTO;
import com.martin.exam_generator.dto.grado.GradoDTO;
import com.martin.exam_generator.entities.Asignatura;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AsignaturaDTO {

    private Long id;
    private String titulo;
    private String codigo;
    private String cursoAcademico;
    private List<GradoDTO> grados = new ArrayList<>();
    private List<AlumnoDTO> alumnos = new ArrayList<>();

    public static AsignaturaDTO fromEntity(Asignatura asignatura) {
        AsignaturaDTO dto = new AsignaturaDTO(
                asignatura.getId(),
                asignatura.getTitulo(),
                asignatura.getCodigo(),
                asignatura.getCursoAcademico(),
                new ArrayList<>(),
                new ArrayList<>()
        );

        if (asignatura.getGrados() != null) {
            dto.setGrados(asignatura.getGrados().stream()
                    .map(GradoDTO::fromEntity)
                    .collect(Collectors.toList()));
        }

        return dto;
    }
}