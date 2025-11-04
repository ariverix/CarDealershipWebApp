package org.example.apicontract.dto;

import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.core.Relation;

import java.time.LocalDateTime;
import java.util.Objects;

@Relation(collectionRelation = "cars", itemRelation = "car")
public class CarResponse extends RepresentationModel<CarResponse> {

    private final Long id;
    private final String brand;
    private final String model;
    private final Integer year;
    private final Double price;
    private final String vin;
    private final boolean sold;
    private final LocalDateTime createdAt;
    private final String category;

    public CarResponse(Long id, String brand, String model, Integer year, Double price,
                       String vin, boolean sold, LocalDateTime createdAt, String category) {
        this.id = id;
        this.brand = brand;
        this.model = model;
        this.year = year;
        this.price = price;
        this.vin = vin;
        this.sold = sold;
        this.createdAt = createdAt;
        this.category = category;
    }

    public Long getId() { return id; }
    public String getBrand() { return brand; }
    public String getModel() { return model; }
    public Integer getYear() { return year; }
    public Double getPrice() { return price; }
    public String getVin() { return vin; }
    public boolean isSold() { return sold; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public String getCategory() { return category; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        CarResponse that = (CarResponse) o;
        return sold == that.sold &&
                Objects.equals(id, that.id) &&
                Objects.equals(brand, that.brand) &&
                Objects.equals(model, that.model) &&
                Objects.equals(year, that.year) &&
                Objects.equals(price, that.price) &&
                Objects.equals(vin, that.vin) &&
                Objects.equals(createdAt, that.createdAt) &&
                Objects.equals(category, that.category);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), id, brand, model, year, price, vin, sold, createdAt, category);
    }
}
