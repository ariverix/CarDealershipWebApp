package org.example.apicontract.endpoints;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.example.apicontract.dto.EmployeeRequest;
import org.example.apicontract.dto.EmployeeResponse;
import org.example.apicontract.dto.StatusResponse;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "employees", description = "API для работы с сотрудниками (HAL)")
public interface EmployeeApi {

    @Operation(summary = "Получить всех сотрудников")
    @ApiResponse(responseCode = "200", description = "Список сотрудников с гипермедиа-ссылками")
    @GetMapping("/api/employees")
    CollectionModel<EntityModel<EmployeeResponse>> getAllEmployees();

    @Operation(summary = "Получить сотрудника по ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Сотрудник найден"),
            @ApiResponse(responseCode = "404", description = "Сотрудник не найден",
                    content = @Content(schema = @Schema(implementation = StatusResponse.class)))
    })
    @GetMapping("/api/employees/{id}")
    EntityModel<EmployeeResponse> getEmployeeById(@PathVariable Long id);

    @Operation(summary = "Создать нового сотрудника")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Сотрудник создан"),
            @ApiResponse(responseCode = "400", description = "Ошибка валидации",
                    content = @Content(schema = @Schema(implementation = StatusResponse.class)))
    })
    @PostMapping("/api/employees")
    @ResponseStatus(HttpStatus.CREATED)
    ResponseEntity<EntityModel<EmployeeResponse>> createEmployee(@Valid @RequestBody EmployeeRequest request);

    @Operation(summary = "Удалить сотрудника")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Сотрудник удалён"),
            @ApiResponse(responseCode = "404", description = "Сотрудник не найден",
                    content = @Content(schema = @Schema(implementation = StatusResponse.class)))
    })
    @DeleteMapping("/api/employees/{id}")
    ResponseEntity<EntityModel<StatusResponse>> deleteEmployee(@PathVariable Long id);
}
