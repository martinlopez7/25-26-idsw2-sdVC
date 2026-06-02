package com.martin.exam_generator.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "respuestas")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Respuesta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String opcion;

    @Column(nullable = false)
    private Boolean esCorrecta = false;

    @ManyToOne
    @JoinColumn(name = "pregunta_id", nullable = false)
    private Pregunta pregunta;
}