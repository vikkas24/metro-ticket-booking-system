package com.metrobooking.controller;
import com.metrobooking.dto.*;
import com.metrobooking.model.User;
import com.metrobooking.service.TicketService;
import jakarta.validation.Valid;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import java.util.List;
@RestController @RequestMapping("/api/tickets")
public class TicketController {
  private final TicketService tickets;
  public TicketController(TicketService t){ tickets = t; }
  @PostMapping("/calculate-fare") public FareResponse fare(@Valid @RequestBody BookingRequest r){ return tickets.calculate(r); }
  @PostMapping("/book") public ResponseEntity<TicketResponse> book(@RequestAttribute("user") User u, @Valid @RequestBody BookingRequest r){
    return ResponseEntity.status(HttpStatus.CREATED).body(tickets.book(u, r)); }
  @GetMapping("/my-tickets") public List<TicketResponse> mine(@RequestAttribute("user") User u){ return tickets.myTickets(u); }
  @GetMapping("/{number}") public TicketResponse one(@RequestAttribute("user") User u, @PathVariable String number){ return tickets.details(u, number); }
  @PutMapping("/{number}/cancel") public TicketResponse cancel(@RequestAttribute("user") User u, @PathVariable String number){ return tickets.cancel(u, number); }
}
