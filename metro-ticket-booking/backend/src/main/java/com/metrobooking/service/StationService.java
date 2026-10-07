package com.metrobooking.service;
import com.metrobooking.dto.StationRequest;
import com.metrobooking.exception.*;
import com.metrobooking.model.Station;
import com.metrobooking.repository.StationRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import java.util.List;
@Service
public class StationService {
  private final StationRepository repo;
  public StationService(StationRepository repo){ this.repo = repo; }
  public List<Station> active(){ return repo.findByActiveTrueOrderByStationOrder(); }
  public List<Station> all(){ return repo.findAllByOrderByStationOrder(); }
  public Station get(Long id){ return repo.findById(id).orElseThrow(() -> new ResourceNotFoundException("Station not found.")); }
  public Station create(StationRequest r){
    if (repo.existsByCodeIgnoreCase(r.code())) throw new ApiException(HttpStatus.CONFLICT, "Station code already exists.");
    return repo.save(apply(new Station(), r));
  }
  public Station update(Long id, StationRequest r){
    Station s = get(id);
    if (!s.code.equalsIgnoreCase(r.code()) && repo.existsByCodeIgnoreCase(r.code())) throw new ApiException(HttpStatus.CONFLICT, "Station code already exists.");
    return repo.save(apply(s, r));
  }
  /** Soft delete: tickets keep referencing the station, so it is only deactivated. */
  public void deactivate(Long id){ Station s = get(id); s.active = false; repo.save(s); }
  private Station apply(Station s, StationRequest r){
    s.name = r.name().trim(); s.code = r.code().trim().toUpperCase(); s.line = r.line(); s.stationOrder = r.stationOrder();
    if (r.active() != null) s.active = r.active(); return s;
  }
}
