package org.example.auditservice.config.listeners;

import org.example.eventscontract.events.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class EventListener {

    private static final Logger log = LoggerFactory.getLogger(EventListener.class);

    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(name = "audit-cars", durable = "true"),
            exchange = @Exchange(name = "dealership-exchange", type = "topic"),
            key = "car.added"
    ))
    public void onCarAdded(CarAddedEvent event) {
        log.info("[AUDIT] Добавлен автомобиль: {} {} ({}) — цена {}$",
                event.brand(), event.model(), event.year(), event.price());
    }

    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(name = "audit-customers", durable = "true"),
            exchange = @Exchange(name = "dealership-exchange", type = "topic"),
            key = "customer.registered"
    ))
    public void onCustomerRegistered(CustomerRegisteredEvent event) {
        log.info("[AUDIT] Зарегистрирован клиент: {} {} (тел. {})",
                event.firstName(), event.lastName(), event.phone());
    }

    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(name = "audit-employees", durable = "true"),
            exchange = @Exchange(name = "dealership-exchange", type = "topic"),
            key = "employee.hired"
    ))
    public void onEmployeeHired(EmployeeHiredEvent event) {
        log.info("[AUDIT] Новый сотрудник: {} {} — должность: {}",
                event.firstName(), event.lastName(), event.position());
    }

    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(name = "audit-sales", durable = "true"),
            exchange = @Exchange(name = "dealership-exchange", type = "topic"),
            key = "sale.created"
    ))
    public void onSaleCreated(SaleCreatedEvent event) {
        log.info("[AUDIT] Продажа ID={} → автомобиль={}, цена={}₽",
                event.saleId(), event.carId(), event.salePrice());
    }

    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(name = "audit-sales-delete", durable = "true"),
            exchange = @Exchange(name = "dealership-exchange", type = "topic"),
            key = "sale.deleted"
    ))
    public void onSaleDeleted(SaleDeletedEvent event) {
        log.info("[AUDIT] Продажа ID={} удалена", event.saleId());
    }
}
