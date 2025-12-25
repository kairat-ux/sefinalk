// ПУТЬ: src/main/java/com/example/demo/dto/request/UserRegistrationRequestDTO.java

package com.example.demo.dto.request;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserRegistrationRequestDTO {

    @NotBlank(message = "Email не может быть пустым")
    @Email(message = "Email должен быть валидным")
    private String email;

    @NotBlank(message = "Пароль не может быть пустым")
    @Size(min = 6, message = "Пароль должен быть минимум 6 символов")
    private String password;

    @NotBlank(message = "Имя не может быть пустым")
    private String firstName;

    @NotBlank(message = "Фамилия не может быть пустой")
    private String lastName;

    @Pattern(regexp = "^\\+7\\s?7\\d{2}\\s?\\d{3}\\s?\\d{2}\\s?\\d{2}$",
             message = "Номер телефона должен быть в формате +7 7XX XXX XX XX")
    private String phone;
}