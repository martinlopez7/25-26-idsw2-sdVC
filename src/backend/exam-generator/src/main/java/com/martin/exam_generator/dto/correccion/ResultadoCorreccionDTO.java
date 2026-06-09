package com.martin.exam_generator.dto.correccion;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResultadoCorreccionDTO {
    private int numeroPagina;
    private int nota;
}