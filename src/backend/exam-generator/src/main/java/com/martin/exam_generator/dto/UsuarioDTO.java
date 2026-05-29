package com.martin.exam_generator.dto;

import com.martin.exam_generator.entities.Usuario;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UsuarioDTO {
    private Long id;
    private String username;
    private String tipoActor;

    public static UsuarioDTO fromEntity(Usuario usuario) {
        return new UsuarioDTO(
            usuario.getId(),
            usuario.getUsername(),
            usuario.getTipoActor().name()
        );
    }
}