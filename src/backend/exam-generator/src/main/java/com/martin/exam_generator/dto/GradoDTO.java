package com.martin.exam_generator.dto;

import com.martin.exam_generator.entities.Grado;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GradoDTO {

    private Long id;
    private String titulo;
    private String codigo;
    private List<AlumnoDTO> alumnos = new ArrayList<>();

    public GradoDTO(Long id, String titulo, String codigo) {
        this.id = id;
        this.titulo = titulo;
        this.codigo = codigo;
        this.alumnos = new ArrayList<>();
    }

    public static GradoDTO fromEntity(Grado grado) {
        return new GradoDTO(
                grado.getId(),
                grado.getTitulo(),
                grado.getCodigo(),
                new ArrayList<>()
        );
    }
}