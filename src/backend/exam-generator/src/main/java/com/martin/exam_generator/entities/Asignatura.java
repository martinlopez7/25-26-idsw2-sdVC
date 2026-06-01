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
@Table(name = "asignaturas")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Asignatura {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String titulo;

    @Column(nullable = false)
    private String codigo;

    @Column(nullable = false)
    private String cursoAcademico;

    @Column(nullable = false)
    private Long docenteId;

    @ManyToMany
    @JoinTable(
        name = "asignatura_grado",
        joinColumns = @JoinColumn(name = "asignatura_id"),
        inverseJoinColumns = @JoinColumn(name = "grado_id")
    )
    @OnDelete(action = OnDeleteAction.CASCADE)
    private List<Grado> grados = new ArrayList<>();
}