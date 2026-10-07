package com.metrobooking.service;
import com.metrobooking.dto.TicketResponse;
import com.metrobooking.repository.*;
import org.springframework.stereotype.Service;
import java.util.*;
@Service
public class AdminService {
  private final UserRepository users; private final StationRepository stations; private final TicketRepository tickets;
  public AdminService(UserRepository u, StationRepository s, TicketRepository t){ users = u; stations = s; tickets = t; }
  public Map<String,Object> dashboard(){
    Map<String,Object> m = new LinkedHashMap<>();
    m.put("totalUsers", users.count()); m.put("totalStations", stations.count()); m.put("totalTickets", tickets.count());
    m.put("activeTickets", tickets.countByStatus("BOOKED")); m.put("cancelledTickets", tickets.countByStatus("CANCELLED"));
    m.put("totalRevenue", tickets.totalRevenue()); return m;
  }
  public List<TicketResponse> allTickets(){ return tickets.findAllByOrderByBookingTimeDesc().stream().map(TicketResponse::of).toList(); }
  /** Password hashes are never returned. */
  public List<Map<String,Object>> allUsers(){
    return users.findAll().stream().map(u -> { Map<String,Object> m = new LinkedHashMap<>();
      m.put("id", u.id); m.put("name", u.name); m.put("email", u.email); m.put("phone", u.phone); m.put("role", u.role); m.put("createdAt", u.createdAt); return m; }).toList();
  }
}
