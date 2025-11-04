package org.example.cardealershiprest.controller;

import org.example.apicontract.dto.CustomerRequest;
import org.example.apicontract.dto.CustomerResponse;
import org.example.apicontract.dto.StatusResponse;
import org.example.apicontract.endpoints.CustomerApi;
import org.example.cardealershiprest.assemblers.CustomerModelAssembler;
import org.example.cardealershiprest.service.CustomerService;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CustomerController implements CustomerApi {

    private final CustomerService customerService;
    private final CustomerModelAssembler customerAssembler;

    public CustomerController(CustomerService customerService, CustomerModelAssembler customerAssembler) {
        this.customerService = customerService;
        this.customerAssembler = customerAssembler;
    }

    @Override
    public CollectionModel<EntityModel<CustomerResponse>> getAllCustomers() {
        return customerAssembler.toCollectionModel(customerService.getAllCustomers());
    }

    @Override
    public EntityModel<CustomerResponse> getCustomerById(Long id) {
        return customerAssembler.toModel(customerService.getCustomerById(id));
    }

    @Override
    public ResponseEntity<EntityModel<CustomerResponse>> createCustomer(CustomerRequest request) {
        var created = customerService.createCustomer(request);
        var entity = customerAssembler.toModel(created);
        return ResponseEntity.created(entity.getRequiredLink("self").toUri()).body(entity);
    }

    @Override
    public StatusResponse deleteCustomer(Long id) {
        return customerService.deleteCustomer(id);
    }
}
