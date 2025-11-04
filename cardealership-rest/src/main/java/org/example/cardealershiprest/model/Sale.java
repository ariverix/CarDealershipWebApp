package org.example.cardealershiprest.model;

import java.time.LocalDate;

public class Sale {
    private Long id;
    private Long carId;
    private Long customerId;
    private Long employeeId;
    private Double salePrice;
    private LocalDate saleDate;

    public Sale(Long id, Long carId, Long customerId, Long employeeId, Double salePrice, LocalDate saleDate) {
        this.id = id;
        this.carId = carId;
        this.customerId = customerId;
        this.employeeId = employeeId;
        this.salePrice = salePrice;
        this.saleDate = saleDate;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getCarId() { return carId; }
    public void setCarId(Long carId) { this.carId = carId; }

    public Long getCustomerId() { return customerId; }
    public void setCustomerId(Long customerId) { this.customerId = customerId; }

    public Long getEmployeeId() { return employeeId; }
    public void setEmployeeId(Long employeeId) { this.employeeId = employeeId; }

    public Double getSalePrice() { return salePrice; }
    public void setSalePrice(Double salePrice) { this.salePrice = salePrice; }

    public LocalDate getSaleDate() { return saleDate; }
    public void setSaleDate(LocalDate saleDate) { this.saleDate = saleDate; }
}