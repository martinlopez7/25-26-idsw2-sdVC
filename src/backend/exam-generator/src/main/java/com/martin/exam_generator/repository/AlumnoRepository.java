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

    boolean existsByDocenteIdAndDni(Long docenteId, String dni);

    boolean existsByDocenteIdAndDniAndIdNot(Long docenteId, String dni, Long id);

    List<Alumno> findByGradoId(Long gradoId);

    @Query("SELECT a FROM Alumno a WHERE a.docenteId = :docenteId AND a.grado IS NULL")
    List<Alumno> findByDocenteIdAndGradoIsNull(@Param("docenteId") Long docenteId);

    @Query("SELECT a FROM Alumno a WHERE a.docenteId = :docenteId " +
           "AND a.grado.id IN :gradoIds " +
           "AND a.id NOT IN " +
           "(SELECT al.id FROM Alumno al JOIN al.asignaturas asig WHERE asig.id = :asignaturaId)")
    List<Alumno> findAvailableAlumnosForAsignatura(@Param("docenteId") Long docenteId,
                                                   @Param("asignaturaId") Long asignaturaId,
                                                   @Param("gradoIds") List<Long> gradoIds);

    @Query("SELECT COUNT(a) FROM Alumno a JOIN a.asignaturas asig WHERE a.grado.id = :gradoId AND asig.id = :asignaturaId")
    int countByGradoIdAndAsignaturaId(@Param("gradoId") Long gradoId, @Param("asignaturaId") Long asignaturaId);

    @Query("SELECT a FROM Alumno a JOIN a.asignaturas asig WHERE asig.id = :asignaturaId AND a.grado.id = :gradoId")
    List<Alumno> findByAsignaturaIdAndGradoId(@Param("asignaturaId") Long asignaturaId, @Param("gradoId") Long gradoId);

    @Query("SELECT a FROM Alumno a JOIN a.asignaturas asig WHERE asig.id = :asignaturaId AND a.grado.id IN :gradoIds")
    List<Alumno> findByAsignaturaIdAndGradoIdIn(@Param("asignaturaId") Long asignaturaId, @Param("gradoIds") List<Long> gradoIds);

    void deleteByDocenteId(Long docenteId);
}