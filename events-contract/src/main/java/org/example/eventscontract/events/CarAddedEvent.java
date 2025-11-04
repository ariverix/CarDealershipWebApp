package org.example.eventscontract.events;
import java.io.Serializable;

public record CarAddedEvent(
        Long carId,
        String brand,
        String model,
        Integer year,
        Double price
) implements Serializable {}