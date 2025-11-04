package org.example.cardealershiprest.service;

import org.example.apicontract.dto.SaleRequest;
import org.example.apicontract.dto.SaleResponse;
import org.example.apicontract.dto.StatusResponse;
import org.example.apicontract.exception.DuplicateResourceException;
import org.example.apicontract.exception.ResourceNotFoundException;
import org.example.cardealershiprest.config.RabbitMQConfig;
import org.example.cardealershiprest.model.Sale;
import org.example.eventscontract.events.SaleCreatedEvent;
import org.example.eventscontract.events.SaleDeletedEvent;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class SaleService {

    private final List<Sale> sales = new ArrayList<>();
    private final AtomicLong idCounter = new AtomicLong(1);
    private final CarService carService;
    private final CustomerService customerService;
    private final EmployeeService employeeService;
    private final RabbitTemplate rabbitTemplate;

    public SaleService(CarService carService, CustomerService customerService,
                       EmployeeService employeeService, RabbitTemplate rabbitTemplate) {
        this.carService = carService;
        this.customerService = customerService;
        this.employeeService = employeeService;
        this.rabbitTemplate = rabbitTemplate;

        Sale seed = new Sale(idCounter.getAndIncrement(), 1L, 1L, 1L, 34000.0, LocalDate.now());
        sales.add(seed);
        carService.markAsSold(seed.getCarId());
    }

    public List<SaleResponse> getAllSales() {
        return sales.stream().map(this::toResponse).toList();
    }

    public SaleResponse getSaleById(Long id) {
        Sale s = sales.stream()
                .filter(it -> it.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("Продажа", id));
        return toResponse(s);
    }

    public SaleResponse createSale(SaleRequest req) {
        carService.getCarById(req.carId());
        customerService.getCustomerById(req.customerId());
        employeeService.getEmployeeById(req.employeeId());

        boolean alreadySold = sales.stream().anyMatch(s -> s.getCarId().equals(req.carId()));
        if (alreadySold) {
            throw new DuplicateResourceException("Продажа", "carId", String.valueOf(req.carId()));
        }

        Sale sale = new Sale(idCounter.getAndIncrement(), req.carId(), req.customerId(),
                req.employeeId(), req.salePrice(), req.saleDate());
        sales.add(sale);

        carService.markAsSold(req.carId());

        var event = new SaleCreatedEvent(
                sale.getId(), sale.getCarId(), sale.getCustomerId(),
                sale.getEmployeeId(), sale.getSalePrice(), sale.getSaleDate()
        );
        rabbitTemplate.convertAndSend(RabbitMQConfig.EXCHANGE_NAME,
                RabbitMQConfig.RK_SALE_CREATED, event);

        return toResponse(sale);
    }

    public StatusResponse deleteSale(Long id) {
        Sale sale = sales.stream()
                .filter(it -> it.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("Продажа", id));
        sales.remove(sale);

        var event = new SaleDeletedEvent(id);
        rabbitTemplate.convertAndSend(RabbitMQConfig.EXCHANGE_NAME,
                RabbitMQConfig.RK_SALE_DELETED, event);

        return new StatusResponse("success", "Продажа удалена");
    }

    private SaleResponse toResponse(Sale s) {
        return new SaleResponse(
                s.getId(),
                carService.getCarById(s.getCarId()),
                customerService.getCustomerById(s.getCustomerId()),
                employeeService.getEmployeeById(s.getEmployeeId()),
                s.getSalePrice(),
                s.getSaleDate()
        );
    }
}
