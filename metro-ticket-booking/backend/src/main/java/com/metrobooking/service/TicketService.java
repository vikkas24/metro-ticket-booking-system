package com.metrobooking.service;
import com.metrobooking.dto.*;
import com.metrobooking.exception.*;
import com.metrobooking.model.*;
import com.metrobooking.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
@Service
public class TicketService {
  private final TicketRepository tickets; private final StationRepository stations; private final FareService fares;
  public TicketService(TicketRepository t, StationRepository s, FareService f){ tickets = t; stations = s; fares = f; }

  public FareResponse calculate(BookingRequest r){
    Station[] route = validateRoute(r);
    BigDecimal each = fares.farePerPassenger(route[0], route[1]);
    return new FareResponse(route[0].name, route[1].name, r.passengerCount(), each, each.multiply(BigDecimal.valueOf(r.passengerCount())));
  }
  @Transactional
  public TicketResponse book(User user, BookingRequest r){
    if (r.journeyDate() == null || r.journeyDate().isBefore(LocalDate.now()))
      throw new InvalidBookingException("Please select a valid journey date (today or later).");
    Station[] route = validateRoute(r); FareResponse fare = calculate(r);
    Ticket t = new Ticket(); t.user = user; t.source = route[0]; t.destination = route[1]; t.passengerCount = r.passengerCount();
    t.farePerPassenger = fare.farePerPassenger(); t.totalFare = fare.totalFare(); t.journeyDate = r.journeyDate(); t.ticketNumber = newTicketNumber();
    return TicketResponse.of(tickets.save(t));
  }
  public List<TicketResponse> myTickets(User u){ return tickets.findByUserIdOrderByBookingTimeDesc(u.id).stream().map(TicketResponse::of).toList(); }
  public TicketResponse details(User u, String number){ return TicketResponse.of(owned(u, number)); }
  @Transactional
  public TicketResponse cancel(User u, String number){
    Ticket t = owned(u, number);
    if ("CANCELLED".equals(t.status)) throw new InvalidBookingException("Ticket is already cancelled.");
    if ("COMPLETED".equals(t.status)) throw new InvalidBookingException("Completed tickets cannot be cancelled.");
    if (!LocalDate.now().isBefore(t.journeyDate)) throw new InvalidBookingException("Tickets can only be cancelled before the journey date.");
    t.status = "CANCELLED"; return TicketResponse.of(tickets.save(t));
  }
  /** Other users' tickets are reported as "not found" so ticket numbers are not leaked. */
  private Ticket owned(User u, String number){
    Ticket t = tickets.findByTicketNumber(number).orElseThrow(() -> new ResourceNotFoundException("Ticket not found."));
    if (!t.user.id.equals(u.id)) throw new ResourceNotFoundException("Ticket not found.");
    return t;
  }
  private Station[] validateRoute(BookingRequest r){
    if (r.sourceStationId().equals(r.destinationStationId())) throw new InvalidBookingException("Source and destination cannot be the same.");
    Station a = stations.findById(r.sourceStationId()).orElseThrow(() -> new ResourceNotFoundException("Source station not found."));
    Station b = stations.findById(r.destinationStationId()).orElseThrow(() -> new ResourceNotFoundException("Destination station not found."));
    if (!a.active || !b.active) throw new InvalidBookingException("Selected station is not currently active.");
    return new Station[]{a, b};
  }
  private String newTicketNumber(){
    String n;
    do { n = "MTB-" + LocalDate.now().format(DateTimeFormatter.BASIC_ISO_DATE) + "-" + UUID.randomUUID().toString().substring(0,6).toUpperCase(); }
    while (tickets.existsByTicketNumber(n));
    return n;
  }
}
