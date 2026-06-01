package com.martin.exam_generator.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AsignaturaCreateDTO {

    @NotBlank(message = "El título es obligatorio")
    @Size(min = 2, max = 100, message = "El título debe tener entre 2 y 100 caracteres")
    private String titulo;

    @NotBlank(message = "El código es obligatorio")
    @Size(min = 2, max = 20, message = "El código debe tener entre 2 y 20 caracteres")
    private String codigo;

    @NotBlank(message = "El curso académico es obligatorio")
    @Size(min = 2, max = 50, message = "El curso académico debe tener entre 2 y 50 caracteres")
    private String cursoAcademico;
}