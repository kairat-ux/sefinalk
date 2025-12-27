// ПУТЬ: src/main/java/com/example/demo/dto/request/ReviewUpdateRequestDTO.java

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
public class ReviewUpdateRequestDTO {

    @NotNull(message = "Рейтинг обязателен")
    @Min(1)
    @Max(5)
    private Integer rating;

    @Size(max = 1000, message = "Комментарий не может быть больше 1000 символов")
    private String comment;
}
