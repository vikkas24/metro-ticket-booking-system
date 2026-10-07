package com.metrobooking.repository;
import com.metrobooking.model.Station;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
public interface StationRepository extends JpaRepository<Station,Long> {
  List<Station> findByActiveTrueOrderByStationOrder();
  List<Station> findAllByOrderByStationOrder();
  boolean existsByCodeIgnoreCase(String code);
}
