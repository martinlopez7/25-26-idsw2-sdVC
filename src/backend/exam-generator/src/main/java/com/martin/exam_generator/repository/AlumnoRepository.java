package com.martin.exam_generator.repository;

import com.martin.exam_generator.entities.Alumno;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AlumnoRepository extends JpaRepository<Alumno, Long> {

    List<Alumno> findByDocenteId(Long docenteId);

    @Query("SELECT a FROM Alumno a WHERE a.docenteId = :docenteId AND " +
            "(LOWER(a.nombre) LIKE LOWER(CONCAT('%', :criterio, '%')) OR " +
            "LOWER(a.apellidos) LIKE LOWER(CONCAT('%', :criterio, '%')) OR " +
            "LOWER(a.dni) LIKE LOWER(CONCAT('%', :criterio, '%')))")
    List<Alumno> findByDocenteIdAndCriterio(@Param("docenteId") Long docenteId,
                                            @Param("criterio") String criterio);

}