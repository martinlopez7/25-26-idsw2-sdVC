package com.martin.exam_generator.dto;

import com.martin.exam_generator.entities.Pregunta;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PreguntaUpdateDTO {

    @NotBlank(message = "El enunciado no puede estar vacío")
    private String enunciado;

    @NotBlank(message = "El tema no puede estar vacío")
    private String tema;

    @NotNull(message = "La dificultad no puede ser nula")
    private Pregunta.Dificultad dificultad;

    @NotNull(message = "El campo habilitada no puede ser nulo")
    private Boolean habilitada;

    private List<RespuestaUpdateDTO> respuestas;
}
