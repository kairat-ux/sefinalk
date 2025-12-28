

package com.example.demo.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserRoleUpdateRequestDTO {

    @NotBlank(message = "Роль не может быть пустой")
    @Pattern(regexp = "USER|OWNER|ADMIN", message = "Роль должна быть USER, OWNER или ADMIN")
    private String role;
}
