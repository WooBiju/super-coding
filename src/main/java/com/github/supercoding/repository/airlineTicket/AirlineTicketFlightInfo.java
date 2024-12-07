package com.github.supercoding.repository.airlineTicket;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AirlineTicketFlightInfo {
    private Integer ticketId;
    private Integer prices;
    private Integer charge;
    private Integer tax;
    private Integer totalPrice;

    public AirlineTicketFlightInfo(Integer ticketId, Double prices, Double charge, Double tax, Double totalPrice) {
        this.ticketId = ticketId;
        this.prices = prices.intValue();
        this.charge = charge.intValue();
        this.tax = tax.intValue();
        this.totalPrice = totalPrice.intValue();
    }

}