package org.example.cardealershiprest.model;

import java.time.LocalDateTime;

public class Car {
    private Long id;
    private String brand;
    private String model;
    private Integer year;
    private Double price;
    private String vin;
    private LocalDateTime createdAt;
    private boolean sold;
    private String category;

    public Car(Long id, String brand, String model, Integer year, Double price, String vin) {
        this.id = id;
        this.brand = brand;
        this.model = model;
        this.year = year;
        this.price = price;
        this.vin = vin;
        this.createdAt = LocalDateTime.now();
        this.sold = false;
        this.category = "Не указана";
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getBrand() { return brand; }
    public void setBrand(String brand) { this.brand = brand; }

    public String getModel() { return model; }
    public void setModel(String model) { this.model = model; }

    public Integer getYear() { return year; }
    public void setYear(Integer year) { this.year = year; }

    public Double getPrice() { return price; }
    public void setPrice(Double price) { this.price = price; }

    public String getVin() { return vin; }
    public void setVin(String vin) { this.vin = vin; }

    public LocalDateTime getCreatedAt() { return createdAt; }

    public boolean isSold() { return sold; }
    public void setSold(boolean sold) { this.sold = sold; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }
}
