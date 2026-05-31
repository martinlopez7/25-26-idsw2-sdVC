package com.martin.exam_generator.repository;

import com.martin.exam_generator.entities.Grado;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GradoRepository extends JpaRepository<Grado, Long> {

    List<Grado> findByDocenteId(Long docenteId);

    @Query("SELECT g FROM Grado g WHERE g.docenteId = :docenteId AND " +
            "(LOWER(g.titulo) LIKE LOWER(CONCAT('%', :criterio, '%')) OR " +
            "LOWER(g.codigo) LIKE LOWER(CONCAT('%', :criterio, '%')))")
    List<Grado> findByDocenteIdAndCriterio(@Param("docenteId") Long docenteId,
                                           @Param("criterio") String criterio);

    boolean existsByDocenteIdAndCodigo(Long docenteId, String codigo);
}