package org.example.apicontract.dto;

import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.core.Relation;

import java.time.LocalDate;
import java.util.Objects;

@Relation(collectionRelation = "sales", itemRelation = "sale")
public class SaleResponse extends RepresentationModel<SaleResponse> {

    private final Long id;
    private final CarResponse car;
    private final CustomerResponse customer;
    private final EmployeeResponse employee;
    private final Double salePrice;
    private final LocalDate saleDate;

    public SaleResponse(Long id, CarResponse car, CustomerResponse customer,
                        EmployeeResponse employee, Double salePrice, LocalDate saleDate) {
        this.id = id;
        this.car = car;
        this.customer = customer;
        this.employee = employee;
        this.salePrice = salePrice;
        this.saleDate = saleDate;
    }

    public Long getId() { return id; }
    public CarResponse getCar() { return car; }
    public CustomerResponse getCustomer() { return customer; }
    public EmployeeResponse getEmployee() { return employee; }
    public Double getSalePrice() { return salePrice; }
    public LocalDate getSaleDate() { return saleDate; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        SaleResponse that = (SaleResponse) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(car, that.car) &&
                Objects.equals(customer, that.customer) &&
                Objects.equals(employee, that.employee) &&
                Objects.equals(salePrice, that.salePrice) &&
                Objects.equals(saleDate, that.saleDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), id, car, customer, employee, salePrice, saleDate);
    }
}