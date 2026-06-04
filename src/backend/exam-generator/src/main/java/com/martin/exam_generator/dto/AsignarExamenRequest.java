package com.martin.exam_generator.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AsignarExamenRequest {
    @NotNull(message = "ID de plantilla es obligatorio")
    private String plantillaId;

    @NotNull(message = "ID de alumno es obligatorio")
    private Long alumnoId;
}