package com.metrobooking.model;
import jakarta.persistence.*;
import java.time.LocalDateTime;
@Entity @Table(name="users")
public class User {
  @Id @GeneratedValue(strategy=GenerationType.IDENTITY) public Long id;
  public String name, email, password, phone;
  public String role = "USER";
  @Column(name="created_at") public LocalDateTime createdAt = LocalDateTime.now();
}
