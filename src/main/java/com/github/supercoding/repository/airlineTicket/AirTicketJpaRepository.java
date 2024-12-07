package com.github.supercoding.repository.airlineTicket;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AirTicketJpaRepository extends JpaRepository<AirlineTicket,Integer> {

    List<AirlineTicket> findAirlineTicketsByArrivalLocationAndTicketType(String likePlace, String ticketType);

}
