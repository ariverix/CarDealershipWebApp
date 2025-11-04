package org.example.apicontract.endpoints;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.example.apicontract.dto.SaleRequest;
import org.example.apicontract.dto.SaleResponse;
import org.example.apicontract.dto.StatusResponse;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "sales", description = "API для работы с продажами (HAL)")
public interface SaleApi {

    @Operation(summary = "Получить все продажи")
    @ApiResponse(responseCode = "200", description = "Список продаж с гипермедиа-ссылками")
    @GetMapping("/api/sales")
    CollectionModel<EntityModel<SaleResponse>> getAllSales();

    @Operation(summary = "Получить продажу по ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Продажа найдена"),
            @ApiResponse(responseCode = "404", description = "Продажа не найдена",
                    content = @Content(schema = @Schema(implementation = StatusResponse.class)))
    })
    @GetMapping("/api/sales/{id}")
    EntityModel<SaleResponse> getSaleById(@PathVariable Long id);

    @Operation(summary = "Создать новую продажу")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Продажа создана"),
            @ApiResponse(responseCode = "400", description = "Ошибка валидации или недопустимая операция",
                    content = @Content(schema = @Schema(implementation = StatusResponse.class))),
            @ApiResponse(responseCode = "404", description = "Автомобиль, клиент или сотрудник не найдены",
                    content = @Content(schema = @Schema(implementation = StatusResponse.class)))
    })
    @PostMapping("/api/sales")
    @ResponseStatus(HttpStatus.CREATED)
    ResponseEntity<EntityModel<SaleResponse>> createSale(@Valid @RequestBody SaleRequest request);

    @Operation(summary = "Удалить продажу")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Продажа удалена"),
            @ApiResponse(responseCode = "404", description = "Продажа не найдена",
                    content = @Content(schema = @Schema(implementation = StatusResponse.class)))
    })
    @DeleteMapping("/api/sales/{id}")
    StatusResponse deleteSale(@PathVariable Long id);
}
