package com.metrobooking.dto;
import com.metrobooking.model.Ticket;
import java.math.BigDecimal;
import java.time.*;
public record TicketResponse(String ticketNumber, String source, String destination, int passengerCount, BigDecimal farePerPassenger,
  BigDecimal totalFare, LocalDate journeyDate, LocalDateTime bookingTime, String status, String passenger) {
  public static TicketResponse of(Ticket t){
    return new TicketResponse(t.ticketNumber, t.source.name, t.destination.name, t.passengerCount, t.farePerPassenger, t.totalFare,
      t.journeyDate, t.bookingTime, t.status, t.user.name);
  }
}
