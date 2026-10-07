package com.metrobooking.service;
import com.metrobooking.model.Station;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
/** All fare rules live here. Each band is {maxStationsInclusive, fare}. */
@Service
public class FareService {
  private static final int[][] BANDS = {{3,10},{6,20},{10,30},{15,40}};
  private static final int FARE_ABOVE_LAST_BAND = 50;
  public BigDecimal farePerPassenger(Station from, Station to){
    int stations = Math.abs(from.stationOrder - to.stationOrder);
    for (int[] band : BANDS) if (stations <= band[0]) return BigDecimal.valueOf(band[1]);
    return BigDecimal.valueOf(FARE_ABOVE_LAST_BAND);
  }
}
