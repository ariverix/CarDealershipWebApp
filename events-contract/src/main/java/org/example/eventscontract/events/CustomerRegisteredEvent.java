package org.example.eventscontract.events;

import java.io.Serializable;

public record CustomerRegisteredEvent(
        Long customerId,
        String firstName,
        String lastName,
        String phone
) implements Serializable {}