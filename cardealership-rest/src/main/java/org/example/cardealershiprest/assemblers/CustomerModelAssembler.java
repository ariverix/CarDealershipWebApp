package org.example.cardealershiprest.assemblers;

import org.example.apicontract.dto.CustomerResponse;
import org.example.cardealershiprest.controller.CustomerController;
import org.example.cardealershiprest.controller.SaleController;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@Component
public class CustomerModelAssembler implements RepresentationModelAssembler<CustomerResponse, EntityModel<CustomerResponse>> {

    @Override
    public EntityModel<CustomerResponse> toModel(CustomerResponse customer) {
        return EntityModel.of(
                customer,
                linkTo(methodOn(CustomerController.class).getCustomerById(customer.getId())).withSelfRel(),
                linkTo(methodOn(CustomerController.class).getAllCustomers()).withRel("collection"),
                linkTo(methodOn(SaleController.class).getAllSales()).withRel("sales")
        );
    }

    @Override
    public CollectionModel<EntityModel<CustomerResponse>> toCollectionModel(Iterable<? extends CustomerResponse> customers) {
        return RepresentationModelAssembler.super.toCollectionModel(customers)
                .add(linkTo(methodOn(CustomerController.class).getAllCustomers()).withSelfRel());
    }
}
