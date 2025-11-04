package org.example.cardealershiprest.assemblers;

import org.example.apicontract.dto.EmployeeResponse;
import org.example.cardealershiprest.controller.EmployeeController;
import org.example.cardealershiprest.controller.SaleController;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class EmployeeModelAssembler implements RepresentationModelAssembler<EmployeeResponse, EntityModel<EmployeeResponse>> {

    @Override
    public EntityModel<EmployeeResponse> toModel(EmployeeResponse employee) {
        return EntityModel.of(
                employee,
                linkTo(methodOn(EmployeeController.class).getEmployeeById(employee.getId())).withSelfRel(),
                linkTo(methodOn(EmployeeController.class).getAllEmployees()).withRel("collection"),
                linkTo(methodOn(SaleController.class).getAllSales()).withRel("sales")
        );
    }

    @Override
    public CollectionModel<EntityModel<EmployeeResponse>> toCollectionModel(Iterable<? extends EmployeeResponse> employees) {
        var collection = RepresentationModelAssembler.super.toCollectionModel(employees);
        return collection.add(linkTo(methodOn(EmployeeController.class).getAllEmployees()).withSelfRel());
    }
}
