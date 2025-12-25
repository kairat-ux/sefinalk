package com.example.demo.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RestaurantUpdateRequestDTO {

    @Size(max = 100)
    private String name;

    private String description;

    @Size(max = 255)
    private String address;

    @Size(max = 100)
    private String city;

    @Size(max = 20)
    private String zipCode;

    @Size(max = 20)
    private String phone;

    @Email
    @Size(max = 100)
    private String email;
}
