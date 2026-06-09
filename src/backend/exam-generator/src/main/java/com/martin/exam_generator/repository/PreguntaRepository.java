package com.martin.exam_generator.repository;

import com.martin.exam_generator.entities.Pregunta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PreguntaRepository extends JpaRepository<Pregunta, Long> {

    List<Pregunta> findByDocenteId(Long docenteId);

    @Query("SELECT p FROM Pregunta p WHERE p.docenteId = :docenteId AND " +
            "(LOWER(p.enunciado) LIKE LOWER(CONCAT('%', :criterio, '%')) OR " +
            "LOWER(p.tema) LIKE LOWER(CONCAT('%', :criterio, '%')) OR " +
            "LOWER(p.dificultad) LIKE LOWER(CONCAT('%', :criterio, '%')))")
    List<Pregunta> findByDocenteIdAndCriterio(@Param("docenteId") Long docenteId,
                                              @Param("criterio") String criterio);

    List<Pregunta> findByAsignaturaId(Long asignaturaId);

    List<Pregunta> findByAsignaturaIdAndTemaIn(Long asignaturaId, List<String> temas);

    @Query("SELECT p FROM Pregunta p WHERE p.asignaturaId = :asignaturaId AND " +
            "(LOWER(p.enunciado) LIKE LOWER(CONCAT('%', :criterio, '%')) OR " +
            "LOWER(p.tema) LIKE LOWER(CONCAT('%', :criterio, '%')) OR " +
            "LOWER(p.dificultad) LIKE LOWER(CONCAT('%', :criterio, '%')))")
    List<Pregunta> findByAsignaturaIdAndCriterio(@Param("asignaturaId") Long asignaturaId,
                                                 @Param("criterio") String criterio);

    boolean existsByAsignaturaIdAndEnunciadoIgnoreCase(Long asignaturaId, String enunciado);

    boolean existsByAsignaturaIdAndEnunciadoIgnoreCaseAndIdNot(Long asignaturaId, String enunciado, Long id);

    void deleteByDocenteId(Long docenteId);
}
