package com.metrobooking.dto;
import jakarta.validation.constraints.*;
import java.time.LocalDate;
public record BookingRequest(@NotNull(message="Please select a source station.") Long sourceStationId,
  @NotNull(message="Please select a destination station.") Long destinationStationId,
  @NotNull(message="Passenger count is required.") @Min(value=1, message="At least 1 passenger is required.") @Max(value=10, message="Maximum 10 passengers per booking.") Integer passengerCount,
  LocalDate journeyDate) {}
