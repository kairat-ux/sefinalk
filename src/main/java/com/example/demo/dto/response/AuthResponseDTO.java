// ПУТЬ: src/main/java/com/example/demo/dto/response/AuthResponseDTO.java

package com.example.demo.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuthResponseDTO {
    private String token;
    private String tokenType = "Bearer";
    private Long expiresIn;
    private UserResponseDTO user;
}