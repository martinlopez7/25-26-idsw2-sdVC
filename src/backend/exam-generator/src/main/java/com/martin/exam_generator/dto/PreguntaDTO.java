package com.martin.exam_generator.dto;

import com.martin.exam_generator.entities.Pregunta;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PreguntaDTO {

    private Long id;
    private String enunciado;
    private String tema;
    private String dificultad;
    private Boolean habilitada;
    private Long asignaturaId;
    private List<RespuestaDTO> respuestas;

    public static PreguntaDTO fromEntity(Pregunta pregunta) {
        List<RespuestaDTO> respuestaDTOs = null;
        if (pregunta.getRespuestas() != null) {
            respuestaDTOs = pregunta.getRespuestas().stream()
                    .map(RespuestaDTO::fromEntity)
                    .collect(Collectors.toList());
        }

        return new PreguntaDTO(
                pregunta.getId(),
                pregunta.getEnunciado(),
                pregunta.getTema().name(),
                pregunta.getDificultad().name(),
                pregunta.getHabilitada(),
                pregunta.getAsignaturaId(),
                respuestaDTOs
        );
    }
}