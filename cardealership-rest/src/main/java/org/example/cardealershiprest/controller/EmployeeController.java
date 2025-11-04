package org.example.cardealershiprest.controller;

import org.example.apicontract.dto.EmployeeRequest;
import org.example.apicontract.dto.EmployeeResponse;
import org.example.apicontract.dto.StatusResponse;
import org.example.apicontract.endpoints.EmployeeApi;
import org.example.cardealershiprest.assemblers.EmployeeModelAssembler;
import org.example.cardealershiprest.service.EmployeeService;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class EmployeeController implements EmployeeApi {

    private final EmployeeService employeeService;
    private final EmployeeModelAssembler employeeAssembler;

    public EmployeeController(EmployeeService employeeService, EmployeeModelAssembler employeeAssembler) {
        this.employeeService = employeeService;
        this.employeeAssembler = employeeAssembler;
    }

    @Override
    public CollectionModel<EntityModel<EmployeeResponse>> getAllEmployees() {
        return employeeAssembler.toCollectionModel(employeeService.getAllEmployees());
    }

    @Override
    public EntityModel<EmployeeResponse> getEmployeeById(Long id) {
        return employeeAssembler.toModel(employeeService.getEmployeeById(id));
    }

    @Override
    public ResponseEntity<EntityModel<EmployeeResponse>> createEmployee(EmployeeRequest request) {
        var created = employeeService.createEmployee(request);
        var entity = employeeAssembler.toModel(created);
        return ResponseEntity.created(entity.getRequiredLink("self").toUri()).body(entity);
    }

    @Override
    public StatusResponse deleteEmployee(Long id) {
        return employeeService.deleteEmployee(id);
    }
}
