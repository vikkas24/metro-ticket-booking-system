package com.metrobooking.dto;
import jakarta.validation.constraints.*;
public record RegisterRequest(@NotBlank(message="Name is required.") @Size(max=100) String name,
  @NotBlank(message="Email is required.") @Email(message="Enter a valid email.") String email,
  @NotBlank(message="Password is required.") @Size(min=6, message="Password must be at least 6 characters.") String password,
  @Pattern(regexp="^$|^[0-9]{10}$", message="Phone must be 10 digits.") String phone) {}
