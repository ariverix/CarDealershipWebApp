package org.example.apicontract.dto;

import jakarta.validation.constraints.*;

import java.time.Year;

public record CarRequest(
        @NotBlank(message = "Марка не может быть пустой")
        String brand,

        @NotBlank(message = "Модель не может быть пустой")
        String model,

        @Min(value = 1900, message = "Год должен быть больше 1900")
        Integer year,

        @Positive(message = "Цена должна быть положительной")
        Double price,

        @NotBlank(message = "VIN не может быть пустым")
        @Size(min = 17, max = 17, message = "VIN должен содержать 17 символов")
        String vin
) {
    @AssertTrue(message = "Год не может быть больше текущего")
    private boolean isYearValid() {
        if (year == null) return true;
        return year <= Year.now().getValue();
    }
}
