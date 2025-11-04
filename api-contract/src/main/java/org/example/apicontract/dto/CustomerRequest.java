package org.example.apicontract.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record CustomerRequest(
        @NotBlank(message = "Имя не может быть пустым")
        String firstName,

        @NotBlank(message = "Фамилия не может быть пустой")
        String lastName,

        @NotBlank(message = "Телефон не может быть пустым")
        @Pattern(regexp = "^\\+?[0-9]{10,15}$", message = "Некорректный формат телефона")
        String phone,

        @Email(message = "Некорректный email")
        String email
) {}