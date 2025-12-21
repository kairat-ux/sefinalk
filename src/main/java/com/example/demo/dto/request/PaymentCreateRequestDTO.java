// ПУТЬ: src/main/java/com/example/demo/dto/request/PaymentCreateRequestDTO.java

package com.example.demo.dto.request;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PaymentCreateRequestDTO {

    @NotNull(message = "ID бронирования обязателен")
    private Long reservationId;

    @NotNull(message = "Сумма обязательна")
    @DecimalMin(value = "0.1", message = "Сумма должна быть больше 0")
    private BigDecimal amount;

    @NotNull(message = "Способ оплаты обязателен")
    private String paymentMethod;
}