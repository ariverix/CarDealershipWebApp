package org.example.apicontract.endpoints;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.example.apicontract.dto.CustomerRequest;
import org.example.apicontract.dto.CustomerResponse;
import org.example.apicontract.dto.StatusResponse;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "customers", description = "API для работы с клиентами (HAL)")
public interface CustomerApi {

    @Operation(summary = "Получить всех клиентов")
    @ApiResponse(responseCode = "200", description = "Список клиентов с гипермедиа-ссылками")
    @GetMapping("/api/customers")
    CollectionModel<EntityModel<CustomerResponse>> getAllCustomers();

    @Operation(summary = "Получить клиента по ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Клиент найден"),
            @ApiResponse(responseCode = "404", description = "Клиент не найден",
                    content = @Content(schema = @Schema(implementation = StatusResponse.class)))
    })
    @GetMapping("/api/customers/{id}")
    EntityModel<CustomerResponse> getCustomerById(@PathVariable Long id);

    @Operation(summary = "Создать нового клиента")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Клиент создан"),
            @ApiResponse(responseCode = "400", description = "Ошибка валидации",
                    content = @Content(schema = @Schema(implementation = StatusResponse.class)))
    })
    @PostMapping("/api/customers")
    @ResponseStatus(HttpStatus.CREATED)
    ResponseEntity<EntityModel<CustomerResponse>> createCustomer(@Valid @RequestBody CustomerRequest request);

    @Operation(summary = "Удалить клиента")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Клиент удалён"),
            @ApiResponse(responseCode = "404", description = "Клиент не найден",
                    content = @Content(schema = @Schema(implementation = StatusResponse.class)))
    })
    @DeleteMapping("/api/customers/{id}")
    ResponseEntity<EntityModel<StatusResponse>> deleteCustomer(@PathVariable Long id);
}
