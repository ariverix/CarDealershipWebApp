package org.example.cardealershiprest.service;

import org.example.apicontract.dto.EmployeeRequest;
import org.example.apicontract.dto.EmployeeResponse;
import org.example.apicontract.dto.StatusResponse;
import org.example.apicontract.exception.ResourceNotFoundException;
import org.example.eventscontract.events.EmployeeHiredEvent;
import org.example.cardealershiprest.config.RabbitMQConfig;
import org.example.cardealershiprest.model.Employee;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class EmployeeService {

    private final List<Employee> employees = new ArrayList<>();
    private final AtomicLong idCounter = new AtomicLong(1);
    private final RabbitTemplate rabbitTemplate;

    public EmployeeService(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;

        employees.add(new Employee(idCounter.getAndIncrement(), "Алексей", "Иванов", "Менеджер", "alexey@dealership.ru"));
        employees.add(new Employee(idCounter.getAndIncrement(), "Ольга", "Кузнецова", "Механик", "olga@dealership.ru"));
    }

    public List<EmployeeResponse> getAllEmployees() {
        return employees.stream().map(this::toResponse).toList();
    }

    public EmployeeResponse getEmployeeById(Long id) {
        Employee e = employees.stream()
                .filter(it -> it.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("Сотрудник", id));
        return toResponse(e);
    }

    public EmployeeResponse createEmployee(EmployeeRequest request) {
        Employee e = new Employee(
                idCounter.getAndIncrement(),
                request.firstName(),
                request.lastName(),
                request.position(),
                request.email()
        );
        employees.add(e);

        // 🔹 Отправляем событие EmployeeHiredEvent
        var event = new EmployeeHiredEvent(e.getId(), e.getFirstName(), e.getLastName(), e.getPosition());
        rabbitTemplate.convertAndSend(
                RabbitMQConfig.EXCHANGE_NAME,
                RabbitMQConfig.RK_EMPLOYEE_HIRED,
                event
        );

        return toResponse(e);
    }

    public StatusResponse deleteEmployee(Long id) {
        Employee e = employees.stream()
                .filter(it -> it.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("Сотрудник", id));
        employees.remove(e);
        return new StatusResponse("success", "Сотрудник удалён");
    }

    private EmployeeResponse toResponse(Employee e) {
        return new EmployeeResponse(
                e.getId(), e.getFirstName(), e.getLastName(),
                e.getPosition(), e.getEmail(), e.getHiredAt()
        );
    }
}
