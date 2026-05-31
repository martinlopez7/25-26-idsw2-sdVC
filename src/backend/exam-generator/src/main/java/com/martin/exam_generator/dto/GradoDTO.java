package com.martin.exam_generator.dto;

import com.martin.exam_generator.entities.Grado;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GradoDTO {

    private Long id;
    private String titulo;
    private String codigo;

    public static GradoDTO fromEntity(Grado grado) {
        GradoDTO dto = new GradoDTO();
        dto.setId(grado.getId());
        dto.setTitulo(grado.getTitulo());
        dto.setCodigo(grado.getCodigo());
        return dto;
    }
}