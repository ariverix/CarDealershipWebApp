package org.example.cardealershiprest.service;

import org.example.apicontract.dto.CustomerRequest;
import org.example.apicontract.dto.CustomerResponse;
import org.example.apicontract.dto.StatusResponse;
import org.example.apicontract.exception.ResourceNotFoundException;
import org.example.cardealershiprest.config.RabbitMQConfig;
import org.example.cardealershiprest.model.Customer;
import org.example.eventscontract.events.CustomerRegisteredEvent;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class CustomerService {

    private final List<Customer> customers = new ArrayList<>();
    private final AtomicLong idCounter = new AtomicLong(1);
    private final RabbitTemplate rabbitTemplate;

    public CustomerService(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;

        customers.add(new Customer(idCounter.getAndIncrement(), "Иван", "Петров", "+79001234567", "ivan@mail.ru"));
        customers.add(new Customer(idCounter.getAndIncrement(), "Мария", "Сидорова", "+79007654321", "maria@mail.ru"));
    }

    public List<CustomerResponse> getAllCustomers() {
        return customers.stream().map(this::toResponse).toList();
    }

    public CustomerResponse getCustomerById(Long id) {
        Customer c = customers.stream()
                .filter(it -> it.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("Клиент", id));
        return toResponse(c);
    }

    public CustomerResponse createCustomer(CustomerRequest request) {
        Customer c = new Customer(
                idCounter.getAndIncrement(),
                request.firstName(),
                request.lastName(),
                request.phone(),
                request.email()
        );
        customers.add(c);

        var event = new CustomerRegisteredEvent(c.getId(), c.getFirstName(), c.getLastName(), c.getPhone());
        rabbitTemplate.convertAndSend(
                RabbitMQConfig.EXCHANGE_NAME,
                RabbitMQConfig.RK_CUSTOMER_REGISTERED,
                event
        );

        return toResponse(c);
    }

    public StatusResponse deleteCustomer(Long id) {
        Customer c = customers.stream()
                .filter(it -> it.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("Клиент", id));
        customers.remove(c);
        return new StatusResponse("success", "Клиент удалён");
    }

    private CustomerResponse toResponse(Customer c) {
        return new CustomerResponse(
                c.getId(), c.getFirstName(), c.getLastName(),
                c.getPhone(), c.getEmail(), c.getRegisteredAt()
        );
    }
}
