package org.example.cardealershiprest.assemblers;


import org.example.apicontract.dto.CarResponse;
import org.example.cardealershiprest.controller.CarController;
import org.example.cardealershiprest.controller.SaleController;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@Component
public class CarModelAssembler implements RepresentationModelAssembler<CarResponse, EntityModel<CarResponse>> {

    @Override
    public EntityModel<CarResponse> toModel(CarResponse car) {
        return EntityModel.of(
                car,
                linkTo(methodOn(CarController.class).getCarById(car.getId())).withSelfRel(),
                linkTo(methodOn(CarController.class).getAllCars()).withRel("collection"),
                linkTo(methodOn(SaleController.class).getAllSales()).withRel("sales")
        );
    }

    @Override
    public CollectionModel<EntityModel<CarResponse>> toCollectionModel(Iterable<? extends CarResponse> cars) {
        return RepresentationModelAssembler.super.toCollectionModel(cars)
                .add(linkTo(methodOn(CarController.class).getAllCars()).withSelfRel());
    }
}
