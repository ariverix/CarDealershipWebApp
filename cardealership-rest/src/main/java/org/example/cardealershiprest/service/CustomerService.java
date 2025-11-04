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
        Customer customer = customers.stream()
                .filter(it -> it.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("Клиент", id));
        return toResponse(customer);
    }

    public CustomerResponse createCustomer(CustomerRequest request) {
        Customer customer = new Customer(
                idCounter.getAndIncrement(),
                request.firstName(),
                request.lastName(),
                request.phone(),
                request.email()
        );
        customers.add(customer);

        var event = new CustomerRegisteredEvent(customer.getId(), customer.getFirstName(), customer.getLastName(), customer.getPhone());
        rabbitTemplate.convertAndSend(
                RabbitMQConfig.EXCHANGE_NAME,
                RabbitMQConfig.RK_CUSTOMER_REGISTERED,
                event
        );

        return toResponse(customer);
    }

    public StatusResponse deleteCustomer(Long id) {
        Customer customer = customers.stream()
                .filter(it -> it.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("Клиент", id));
        customers.remove(customer);
        return new StatusResponse("success", "Клиент удалён");
    }

    private CustomerResponse toResponse(Customer customer) {
        return new CustomerResponse(
                customer.getId(), customer.getFirstName(), customer.getLastName(),
                customer.getPhone(), customer.getEmail(), customer.getRegisteredAt()
        );
    }
}
