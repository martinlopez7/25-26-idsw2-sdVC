package com.martin.exam_generator.dto.grado;

import com.martin.exam_generator.dto.alumno.AlumnoDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AlumnoPorGradoDTO {
    private Long gradoId;
    private String gradoNombre;
    private List<AlumnoDTO> alumnos;
}