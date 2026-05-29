package com.martin.exam_generator.repository;

import com.martin.exam_generator.entities.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    Optional<Usuario> findByUsername(String username);

    List<Usuario> findByTipoActor(Usuario.TipoActor tipoActor);

    @Query("SELECT u FROM Usuario u WHERE u.tipoActor = :tipoActor AND " +
           "(LOWER(u.username) LIKE LOWER(CONCAT('%', :criterio, '%')))")
    List<Usuario> findByTipoActorAndCriterio(@Param("tipoActor") Usuario.TipoActor tipoActor,
                                             @Param("criterio") String criterio);

    boolean existsByUsername(String username);
    boolean existsByDni(String dni);
    boolean existsByEmail(String email);
}
