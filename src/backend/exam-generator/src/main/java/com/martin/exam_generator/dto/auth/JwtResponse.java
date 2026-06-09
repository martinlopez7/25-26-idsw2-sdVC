package com.martin.exam_generator.dto.auth;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class JwtResponse {
    private String token;
    private String type;
    private String tipoActor;

    public JwtResponse(String token, String tipoActor) {
        this.token = token;
        this.type = "Bearer";
        this.tipoActor = tipoActor;
    }
}
