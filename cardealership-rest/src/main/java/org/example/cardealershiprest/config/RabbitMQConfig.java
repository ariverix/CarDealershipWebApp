package org.example.cardealershiprest.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Centralised RabbitMQ configuration for the REST gateway.
 * Declares the exchange, queues, bindings and a JSON message converter that keeps date/time values readable.
 */
@Configuration
public class RabbitMQConfig {

    public static final String EXCHANGE_NAME = "dealership-exchange";

    public static final String QUEUE_AUDIT = "audit-queue";
    public static final String QUEUE_ANALYTICS = "analytics-queue";

    public static final String RK_SALE_CREATED = "sale.created";
    public static final String RK_SALE_DELETED = "sale.deleted";
    public static final String RK_CAR_ADDED = "car.added";
    public static final String RK_CUSTOMER_REGISTERED = "customer.registered";
    public static final String RK_EMPLOYEE_HIRED = "employee.hired";

    @Bean
    public TopicExchange exchange() {
        return new TopicExchange(EXCHANGE_NAME, true, false);
    }

    @Bean
    public Queue auditQueue() {
        return new Queue(QUEUE_AUDIT, true);
    }

    @Bean
    public Queue analyticsQueue() {
        return new Queue(QUEUE_ANALYTICS, true);
    }

    @Bean
    public Binding auditBinding(Queue auditQueue, TopicExchange exchange) {
        return BindingBuilder.bind(auditQueue).to(exchange).with("#");
    }

    @Bean
    public Binding analyticsBinding(Queue analyticsQueue, TopicExchange exchange) {
        return BindingBuilder.bind(analyticsQueue).to(exchange).with("sale.*");
    }

    @Bean
    public Jackson2JsonMessageConverter converter() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        return new Jackson2JsonMessageConverter(mapper);
    }

    @Bean
    public RabbitAdmin rabbitAdmin(ConnectionFactory factory) {
        return new RabbitAdmin(factory);
    }

    @Bean
    public RabbitTemplate template(ConnectionFactory factory, Jackson2JsonMessageConverter converter) {
        RabbitTemplate template = new RabbitTemplate(factory);
        template.setExchange(EXCHANGE_NAME);
        template.setMessageConverter(converter);
        return template;
    }
}
