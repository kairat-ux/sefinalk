// ПУТЬ: src/main/java/com/example/demo/dto/response/RestaurantDetailResponseDTO.java

package com.example.demo.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RestaurantDetailResponseDTO {
    private Long id;
    private String name;
    private String description;
    private String address;
    private String city;
    private String phone;
    private String email;
    private BigDecimal rating;
    private Integer totalReviews;
    private LocalDateTime createdAt;
}