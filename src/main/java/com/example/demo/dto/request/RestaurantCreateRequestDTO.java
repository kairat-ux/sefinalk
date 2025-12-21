// ПУТЬ: src/main/java/com/example/demo/dto/request/RestaurantCreateRequestDTO.java

package com.example.demo.dto.request;

import jakarta.validation.constraints.NotBlank;
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
    private String phone;
    private String email;
}