package com.martin.exam_generator.dto.asignacion;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AsignarTodosRequest {
    @NotEmpty(message = "La lista de asignaciones no puede estar vacía")
    @Valid
    private List<AsignacionDTO> asignaciones;
}