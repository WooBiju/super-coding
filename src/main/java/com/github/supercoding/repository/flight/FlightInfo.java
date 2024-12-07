package com.github.supercoding.repository.flight;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class FlightInfo {

    private String flightId;
    private LocalDateTime departureAt;
    private LocalDateTime arrivalAt;
    private String departureLocation;
    private String arrivalLocation;


}
