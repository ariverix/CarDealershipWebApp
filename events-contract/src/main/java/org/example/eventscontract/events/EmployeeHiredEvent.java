package org.example.eventscontract.events;

import java.io.Serializable;

public record EmployeeHiredEvent(
        Long employeeId,
        String firstName,
        String lastName,
        String position
) implements Serializable {}