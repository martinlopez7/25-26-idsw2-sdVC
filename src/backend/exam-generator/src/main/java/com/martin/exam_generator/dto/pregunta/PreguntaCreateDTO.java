package com.martin.exam_generator.dto.pregunta;

import com.martin.exam_generator.entities.Pregunta;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PreguntaCreateDTO {

    @NotNull(message = "La asignatura es obligatoria")
    private Long asignaturaId;

    @NotBlank(message = "El enunciado es obligatorio")
    @Size(min = 10, max = 500, message = "El enunciado debe tener entre 10 y 500 caracteres")
    private String enunciado;

    @NotBlank(message = "El tema es obligatorio")
    @Size(min = 3, max = 100, message = "El tema debe tener entre 3 y 100 caracteres")
    private String tema;

    @NotNull(message = "La dificultad es obligatoria")
    private DificultadEnum dificultad;

    public enum DificultadEnum {
        FACIL, MEDIO, DIFICIL
    }

    public Pregunta toEntity() {
        Pregunta pregunta = new Pregunta();
        pregunta.setAsignaturaId(this.asignaturaId);
        pregunta.setEnunciado(this.enunciado);
        pregunta.setTema(this.tema);
        pregunta.setDificultad(Pregunta.Dificultad.valueOf(this.dificultad.name()));
        pregunta.setHabilitada(false);
        return pregunta;
    }
}
