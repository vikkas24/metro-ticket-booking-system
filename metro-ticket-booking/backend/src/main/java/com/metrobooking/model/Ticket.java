package com.metrobooking.model;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.*;
@Entity @Table(name="tickets")
public class Ticket {
  @Id @GeneratedValue(strategy=GenerationType.IDENTITY) public Long id;
  @Column(name="ticket_number") public String ticketNumber;
  @ManyToOne(fetch=FetchType.EAGER) @JoinColumn(name="user_id") public User user;
  @ManyToOne(fetch=FetchType.EAGER) @JoinColumn(name="source_station_id") public Station source;
  @ManyToOne(fetch=FetchType.EAGER) @JoinColumn(name="destination_station_id") public Station destination;
  @Column(name="passenger_count") public int passengerCount;
  @Column(name="fare_per_passenger") public BigDecimal farePerPassenger;
  @Column(name="total_fare") public BigDecimal totalFare;
  @Column(name="booking_time") public LocalDateTime bookingTime = LocalDateTime.now();
  @Column(name="journey_date") public LocalDate journeyDate;
  public String status = "BOOKED";
}
