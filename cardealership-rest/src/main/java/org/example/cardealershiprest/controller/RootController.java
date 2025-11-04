package org.example.cardealershiprest.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Tag(name = "root", description = "Точка входа в API")
@RestController
@RequestMapping("/api")
public class RootController {

    @GetMapping
    public RepresentationModel<?> getRoot() {
        RepresentationModel<?> root = new RepresentationModel<>();
        root.add(
                linkTo(methodOn(CarController.class).getAllCars()).withRel("cars"),
                linkTo(methodOn(CustomerController.class).getAllCustomers()).withRel("customers"),
                linkTo(methodOn(EmployeeController.class).getAllEmployees()).withRel("employees"),
                linkTo(methodOn(SaleController.class).getAllSales()).withRel("sales"),
                Link.of("/swagger-ui/index.html").withRel("documentation")
        );
        return root;
    }
}
