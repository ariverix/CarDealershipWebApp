package org.example.apicontract.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.time.LocalDate;

public record SaleRequest(
        @NotNull(message = "ID автомобиля обязателен")
        Long carId,

        @NotNull(message = "ID клиента обязателен")
        Long customerId,

        @NotNull(message = "ID сотрудника обязателен")
        Long employeeId,

        @Positive(message = "Цена должна быть положительной")
        Double salePrice,

        @NotNull(message = "Дата продажи обязательна")
        LocalDate saleDate
) {}