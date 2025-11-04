package org.example.apicontract.endpoints;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.example.apicontract.dto.CarRequest;
import org.example.apicontract.dto.CarResponse;
import org.example.apicontract.dto.StatusResponse;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "cars", description = "API для работы с автомобилями (HAL)")
public interface CarApi {

    @Operation(summary = "Получить все автомобили")
    @ApiResponse(responseCode = "200", description = "Список автомобилей с гипермедиа-ссылками")
    @GetMapping("/api/cars")
    CollectionModel<EntityModel<CarResponse>> getAllCars();

    @Operation(summary = "Получить автомобиль по ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Автомобиль найден"),
            @ApiResponse(responseCode = "404", description = "Автомобиль не найден",
                    content = @Content(schema = @Schema(implementation = StatusResponse.class)))
    })
    @GetMapping("/api/cars/{id}")
    EntityModel<CarResponse> getCarById(@PathVariable Long id);

    @Operation(summary = "Создать новый автомобиль")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Автомобиль создан"),
            @ApiResponse(responseCode = "400", description = "Ошибка валидации",
                    content = @Content(schema = @Schema(implementation = StatusResponse.class)))
    })
    @PostMapping("/api/cars")
    @ResponseStatus(HttpStatus.CREATED)
    ResponseEntity<EntityModel<CarResponse>> createCar(@Valid @RequestBody CarRequest request);

    @Operation(summary = "Обновить автомобиль")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Автомобиль обновлён"),
            @ApiResponse(responseCode = "404", description = "Автомобиль не найден",
                    content = @Content(schema = @Schema(implementation = StatusResponse.class)))
    })
    @PutMapping("/api/cars/{id}")
    EntityModel<CarResponse> updateCar(@PathVariable Long id, @Valid @RequestBody CarRequest request);

    @Operation(summary = "Удалить автомобиль")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Автомобиль удалён"),
            @ApiResponse(responseCode = "404", description = "Автомобиль не найден",
                    content = @Content(schema = @Schema(implementation = StatusResponse.class)))
    })
    @DeleteMapping("/api/cars/{id}")
    ResponseEntity<EntityModel<StatusResponse>> deleteCar(@PathVariable Long id);
}
