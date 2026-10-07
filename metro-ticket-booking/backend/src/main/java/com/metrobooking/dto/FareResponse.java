package com.metrobooking.dto;
import java.math.BigDecimal;
public record FareResponse(String source, String destination, int passengerCount, BigDecimal farePerPassenger, BigDecimal totalFare) {}
