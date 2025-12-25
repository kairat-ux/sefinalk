// ПУТЬ: src/main/java/com/example/demo/dto/request/RestaurantCreateRequestDTO.java

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
public class RestaurantCreateRequestDTO {

    @NotBlank(message = "Название ресторана не может быть пустым")
    private String name;

    private String description;

    @NotBlank(message = "Адрес не может быть пустым")
    private String address;

    @NotBlank(message = "Город не может быть пустым")
    private String city;

    private String zipCode;

    @Pattern(regexp = "^\\+7\\s?\\d{3}\\s?\\d{3}\\s?\\d{2}\\s?\\d{2}$",
             message = "Номер телефона должен быть в формате +7 XXX XXX XX XX")
    private String phone;

    private String email;
}