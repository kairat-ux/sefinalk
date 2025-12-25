package com.example.demo.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserUpdateRequestDTO {

    private String firstName;

    private String lastName;

    @Email(message = "Email должен быть валидным")
    private String email;

    @Pattern(regexp = "^\\+7\\s?7\\d{2}\\s?\\d{3}\\s?\\d{2}\\s?\\d{2}$",
             message = "Номер телефона должен быть в формате +7 7XX XXX XX XX")
    private String phone;
}
