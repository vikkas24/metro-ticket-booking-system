package com.metrobooking.repository;
import com.metrobooking.model.Ticket;
import org.springframework.data.jpa.repository.*;
import java.math.BigDecimal;
import java.util.*;
public interface TicketRepository extends JpaRepository<Ticket,Long> {
  List<Ticket> findByUserIdOrderByBookingTimeDesc(Long userId);
  List<Ticket> findAllByOrderByBookingTimeDesc();
  Optional<Ticket> findByTicketNumber(String ticketNumber);
  boolean existsByTicketNumber(String ticketNumber);
  long countByStatus(String status);
  @Query("select coalesce(sum(t.totalFare),0) from Ticket t where t.status <> 'CANCELLED'")
  BigDecimal totalRevenue();
}
