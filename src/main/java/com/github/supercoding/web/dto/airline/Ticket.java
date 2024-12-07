package com.github.supercoding.web.dto.airline;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.github.supercoding.repository.airlineTicket.AirlineTicket;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.format.DateTimeFormatter;

@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
@NoArgsConstructor
@Getter
@Setter
@ToString
public class Ticket {
    private String depart;
    private String arrival;
    private String departureTime;
    private String returnTime;
    private Integer ticketId;

    private static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public Ticket(AirlineTicket airlineTicket) {
        this.ticketId = airlineTicket.getTicketId();
        this.depart = airlineTicket.getDepartureLocation();
        this.arrival = airlineTicket.getArrivalLocation();
        this.departureTime = airlineTicket.getDepartureAt().format(formatter);
        this.returnTime = airlineTicket.getReturnAt().format(formatter);

    }

}
