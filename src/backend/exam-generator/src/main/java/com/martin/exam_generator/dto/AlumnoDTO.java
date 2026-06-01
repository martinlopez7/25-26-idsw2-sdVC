package com.martin.exam_generator.dto;

import com.martin.exam_generator.entities.Alumno;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AlumnoDTO {

    private Long id;
    private String nombre;
    private String apellidos;
    private String dni;

    public static AlumnoDTO fromEntity(Alumno alumno) {
        return new AlumnoDTO(
                alumno.getId(),
                alumno.getNombre(),
                alumno.getApellidos(),
                alumno.getDni()
        );
    }
}