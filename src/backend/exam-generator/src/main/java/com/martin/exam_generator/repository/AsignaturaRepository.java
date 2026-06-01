package com.martin.exam_generator.repository;

import com.martin.exam_generator.entities.Asignatura;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AsignaturaRepository extends JpaRepository<Asignatura, Long> {

    List<Asignatura> findByDocenteId(Long docenteId);

    @Query("SELECT a FROM Asignatura a WHERE a.docenteId = :docenteId AND " +
           "(LOWER(a.titulo) LIKE LOWER(CONCAT('%', :criterio, '%')) OR " +
           "LOWER(a.codigo) LIKE LOWER(CONCAT('%', :criterio, '%')) OR " +
           "LOWER(a.cursoAcademico) LIKE LOWER(CONCAT('%', :criterio, '%')))")
    List<Asignatura> findByDocenteIdAndCriterio(@Param("docenteId") Long docenteId, @Param("criterio") String criterio);

    boolean existsByIdAndDocenteId(Long id, Long docenteId);

    boolean existsByCodigoAndDocenteIdAndIdNot(String codigo, Long docenteId, Long id);
}