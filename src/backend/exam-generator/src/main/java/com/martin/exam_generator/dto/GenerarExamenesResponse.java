package com.martin.exam_generator.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GenerarExamenesResponse {

    private List<String> plantillaExamenIds;
    private Integer totalExamenesGenerados;
}