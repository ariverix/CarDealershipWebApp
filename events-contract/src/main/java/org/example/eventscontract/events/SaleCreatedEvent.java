package org.example.eventscontract.events;

import java.io.Serializable;
import java.time.LocalDate;

public record SaleCreatedEvent(
        Long saleId,
        Long carId,
        Long customerId,
        Long employeeId,
        Double salePrice,
        LocalDate saleDate
) implements Serializable {}