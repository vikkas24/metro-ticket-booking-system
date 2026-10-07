package com.metrobooking.model;
import jakarta.persistence.*;
import java.time.LocalDateTime;
@Entity @Table(name="stations")
public class Station {
  @Id @GeneratedValue(strategy=GenerationType.IDENTITY) public Long id;
  public String name, code, line;
  @Column(name="station_order") public Integer stationOrder;
  public boolean active = true;
  @Column(name="created_at") public LocalDateTime createdAt = LocalDateTime.now();
}
