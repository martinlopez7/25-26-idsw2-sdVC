package com.martin.exam_generator.dto.generacion;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ConfigGradoDTO {

    @NotNull(message = "Número de exámenes es obligatorio")
    @Min(value = 1, message = "Debe generar al menos 1 examen")
    @Max(value = 1000, message = "No puede superar 1000 exámenes")
    private Integer numExamenes;

    @NotNull(message = "Número de tipos de examen es obligatorio")
    @Min(value = 1, message = "Debe tener al menos 1 tipo de examen")
    @Max(value = 1000, message = "No puede superar 1000 tipos de examen")
    private Integer numTiposExamen;

    @NotNull(message = "Proporción de dificultad es obligatoria")
    private ProporcionDificultadDTO proporcionDificultad;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ProporcionDificultadDTO {
        @Min(value = 0, message = "El porcentaje no puede ser negativo")
        @Max(value = 100, message = "El porcentaje no puede superar 100")
        private Integer facil;

        @Min(value = 0, message = "El porcentaje no puede ser negativo")
        @Max(value = 100, message = "El porcentaje no puede superar 100")
        private Integer medio;

        @Min(value = 0, message = "El porcentaje no puede ser negativo")
        @Max(value = 100, message = "El porcentaje no puede superar 100")
        private Integer dificil;
    }
}