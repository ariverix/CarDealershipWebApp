package org.example.eventscontract.events;

import java.io.Serializable;

public record SaleDeletedEvent(Long saleId) implements Serializable {}