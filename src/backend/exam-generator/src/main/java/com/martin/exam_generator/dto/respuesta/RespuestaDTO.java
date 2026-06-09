package com.martin.exam_generator.dto.respuesta;

import com.martin.exam_generator.entities.Respuesta;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RespuestaDTO {

    private Long id;
    private String opcion;
    private Boolean esCorrecta;
    private Long preguntaId;

    public static RespuestaDTO fromEntity(Respuesta respuesta) {
        return new RespuestaDTO(
                respuesta.getId(),
                respuesta.getOpcion(),
                respuesta.getEsCorrecta(),
                respuesta.getPregunta().getId()
        );
    }
}