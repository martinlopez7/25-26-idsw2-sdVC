package com.martin.exam_generator.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "preguntas")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Pregunta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String enunciado;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Tema tema;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Dificultad dificultad;

    @Column(nullable = false)
    private Boolean habilitada = true;

    @Column(nullable = false)
    private Long docenteId;

    @Column(nullable = false)
    private Long asignaturaId;

    @OneToMany(mappedBy = "pregunta", cascade = CascadeType.ALL, orphanRemoval = true)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private List<Respuesta> respuestas = new ArrayList<>();

    public enum Tema {
        TEMA_1,
        TEMA_2,
        TEMA_3,
        TEMA_4,
        TEMA_5
    }

    public enum Dificultad {
        FACIL,
        MEDIO,
        DIFICIL
    }
}