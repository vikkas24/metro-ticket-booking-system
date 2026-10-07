package com.metrobooking.dto;
import jakarta.validation.constraints.*;
public record StationRequest(@NotBlank(message="Station name is required.") String name, @NotBlank(message="Station code is required.") String code,
  String line, @NotNull(message="Station order is required.") Integer stationOrder, Boolean active) {}
