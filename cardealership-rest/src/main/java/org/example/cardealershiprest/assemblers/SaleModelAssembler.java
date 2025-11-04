package org.example.cardealershiprest.assemblers;

import org.example.apicontract.dto.SaleResponse;
import org.example.cardealershiprest.controller.CarController;
import org.example.cardealershiprest.controller.CustomerController;
import org.example.cardealershiprest.controller.EmployeeController;
import org.example.cardealershiprest.controller.SaleController;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class SaleModelAssembler implements RepresentationModelAssembler<SaleResponse, EntityModel<SaleResponse>> {

    @Override
    public EntityModel<SaleResponse> toModel(SaleResponse sale) {
        return EntityModel.of(
                sale,
                linkTo(methodOn(SaleController.class).getSaleById(sale.getId())).withSelfRel(),
                linkTo(methodOn(SaleController.class).getAllSales()).withRel("collection"),
                linkTo(methodOn(CarController.class).getCarById(sale.getCar().getId())).withRel("car"),
                linkTo(methodOn(CustomerController.class).getCustomerById(sale.getCustomer().getId())).withRel("customer"),
                linkTo(methodOn(EmployeeController.class).getEmployeeById(sale.getEmployee().getId())).withRel("employee")
        );
    }

    @Override
    public CollectionModel<EntityModel<SaleResponse>> toCollectionModel(Iterable<? extends SaleResponse> sales) {
        var collection = RepresentationModelAssembler.super.toCollectionModel(sales);
        return collection.add(linkTo(methodOn(SaleController.class).getAllSales()).withSelfRel());
    }
}
