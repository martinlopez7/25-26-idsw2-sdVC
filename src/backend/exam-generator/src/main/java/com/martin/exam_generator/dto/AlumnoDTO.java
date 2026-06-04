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
    private Long gradoId;
    private String gradoNombre;

    public static AlumnoDTO fromEntity(Alumno alumno) {
        AlumnoDTO dto = new AlumnoDTO();
        dto.setId(alumno.getId());
        dto.setNombre(alumno.getNombre());
        dto.setApellidos(alumno.getApellidos());
        dto.setDni(alumno.getDni());
        if (alumno.getGrado() != null) {
            dto.setGradoId(alumno.getGrado().getId());
            dto.setGradoNombre(alumno.getGrado().getTitulo());
        }
        return dto;
    }
}