package org.example.cardealershiprest.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    public static final String EXCHANGE_NAME = "dealership-exchange";

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
    public Jackson2JsonMessageConverter converter() {
        ObjectMapper m = new ObjectMapper();
        m.registerModule(new JavaTimeModule());
        m.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        return new Jackson2JsonMessageConverter(m);
    }

    @Bean
    public RabbitAdmin rabbitAdmin(ConnectionFactory factory) {
        return new RabbitAdmin(factory);
    }

    @Bean
    public RabbitTemplate template(ConnectionFactory factory, Jackson2JsonMessageConverter converter) {
        RabbitTemplate t = new RabbitTemplate(factory);
        t.setExchange(EXCHANGE_NAME);
        t.setMessageConverter(converter);
        return t;
    }
}
