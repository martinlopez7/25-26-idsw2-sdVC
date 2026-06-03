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
@Table(name = "alumnos", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"docente_id", "dni"})
})
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Alumno {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nombre;

    @Column(nullable = false)
    private String apellidos;

    @Column(nullable = false)
    private String dni;

    @Column(nullable = false)
    private Long docenteId;

    @ManyToOne(optional = true)
    @JoinColumn(name = "grado_id")
    @OnDelete(action = OnDeleteAction.SET_NULL)
    private Grado grado;

    @ManyToMany
    @JoinTable(
        name = "alumnos_asignaturas",
        joinColumns = @JoinColumn(name = "alumno_id"),
        inverseJoinColumns = @JoinColumn(name = "asignatura_id")
    )
    @OnDelete(action = OnDeleteAction.CASCADE)
    private List<Asignatura> asignaturas = new ArrayList<>();
}