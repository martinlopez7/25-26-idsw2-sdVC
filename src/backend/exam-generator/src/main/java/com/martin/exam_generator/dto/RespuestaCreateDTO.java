package com.martin.exam_generator.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RespuestaCreateDTO {

    @NotNull(message = "El preguntaId no puede ser nulo")
    private Long preguntaId;

    @NotBlank(message = "La opcion no puede estar vacia")
    private String opcion;

    @NotNull(message = "El campo esCorrecta no puede ser nulo")
    private Boolean esCorrecta;
}