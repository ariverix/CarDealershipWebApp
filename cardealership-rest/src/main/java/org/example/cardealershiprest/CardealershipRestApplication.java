package org.example.cardealershiprest;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.hateoas.config.EnableHypermediaSupport;

@SpringBootApplication(
        scanBasePackages = {
                "org.example.cardealershiprest",
                "org.example.apicontract"
        },
        exclude = {DataSourceAutoConfiguration.class}
)
@EnableHypermediaSupport(type = EnableHypermediaSupport.HypermediaType.HAL)
public class CardealershipRestApplication {
    public static void main(String[] args) {
        SpringApplication.run(CardealershipRestApplication.class, args);
    }
}