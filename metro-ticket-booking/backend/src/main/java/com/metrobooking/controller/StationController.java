package com.metrobooking.controller;
import com.metrobooking.model.Station;
import com.metrobooking.service.StationService;
import org.springframework.web.bind.annotation.*;
import java.util.List;
@RestController @RequestMapping("/api/stations")
public class StationController {
  private final StationService stations;
  public StationController(StationService s){ stations = s; }
  @GetMapping public List<Station> all(){ return stations.active(); }
  @GetMapping("/{id}") public Station one(@PathVariable Long id){ return stations.get(id); }
}
