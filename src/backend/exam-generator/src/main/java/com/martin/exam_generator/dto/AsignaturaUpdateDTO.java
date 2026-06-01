package com.martin.exam_generator.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AsignaturaUpdateDTO {

    @NotBlank(message = "El título es obligatorio")
    private String titulo;

    @NotBlank(message = "El código es obligatorio")
    private String codigo;

    @NotBlank(message = "El curso académico es obligatorio")
    private String cursoAcademico;

    private List<Long> gradoIds;
}