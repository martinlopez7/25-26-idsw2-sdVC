package com.martin.exam_generator.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RespuestaUpdateDTO {

    private Long id;

    @NotBlank(message = "La opción no puede estar vacía")
    private String opcion;

    @NotNull(message = "El campo esCorrecta no puede ser nulo")
    private Boolean esCorrecta;
}
