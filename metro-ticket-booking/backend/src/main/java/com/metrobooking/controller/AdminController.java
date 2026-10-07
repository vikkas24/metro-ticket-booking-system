package com.metrobooking.controller;
import com.metrobooking.dto.*;
import com.metrobooking.model.Station;
import com.metrobooking.service.*;
import jakarta.validation.Valid;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import java.util.*;
@RestController @RequestMapping("/api/admin")
public class AdminController {
  private final AdminService admin; private final StationService stations;
  public AdminController(AdminService a, StationService s){ admin = a; stations = s; }
  @GetMapping("/dashboard") public Map<String,Object> dashboard(){ return admin.dashboard(); }
  @GetMapping("/tickets") public List<TicketResponse> tickets(){ return admin.allTickets(); }
  @GetMapping("/users") public List<Map<String,Object>> users(){ return admin.allUsers(); }
  @GetMapping("/stations") public List<Station> stations(){ return stations.all(); }
  @PostMapping("/stations") public ResponseEntity<Station> create(@Valid @RequestBody StationRequest r){ return ResponseEntity.status(HttpStatus.CREATED).body(stations.create(r)); }
  @PutMapping("/stations/{id}") public Station update(@PathVariable Long id, @Valid @RequestBody StationRequest r){ return stations.update(id, r); }
  @DeleteMapping("/stations/{id}") public Map<String,String> deactivate(@PathVariable Long id){ stations.deactivate(id); return Map.of("message","Station deactivated."); }
}
