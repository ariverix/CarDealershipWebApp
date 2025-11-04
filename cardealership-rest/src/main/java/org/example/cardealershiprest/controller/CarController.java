package org.example.cardealershiprest.controller;

import org.example.apicontract.dto.CarRequest;
import org.example.apicontract.dto.CarResponse;
import org.example.apicontract.endpoints.CarApi;
import org.example.cardealershiprest.assemblers.CarModelAssembler;
import org.example.cardealershiprest.service.CarService;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CarController implements CarApi {

    private final CarService carService;
    private final CarModelAssembler carAssembler;

    public CarController(CarService carService, CarModelAssembler carAssembler) {
        this.carService = carService;
        this.carAssembler = carAssembler;
    }

    @Override
    public CollectionModel<EntityModel<CarResponse>> getAllCars() {
        return carAssembler.toCollectionModel(carService.getAllCars());
    }

    @Override
    public EntityModel<CarResponse> getCarById(Long id) {
        return carAssembler.toModel(carService.getCarById(id));
    }

    @Override
    public ResponseEntity<EntityModel<CarResponse>> createCar(CarRequest request) {
        var created = carService.createCar(request);
        var entity = carAssembler.toModel(created);
        return ResponseEntity.created(entity.getRequiredLink("self").toUri()).body(entity);
    }

    @Override
    public EntityModel<CarResponse> updateCar(Long id, CarRequest request) {
        return carAssembler.toModel(carService.updateCar(id, request));
    }

    @Override
    public void deleteCar(Long id) {
        carService.deleteCar(id);
    }
}
