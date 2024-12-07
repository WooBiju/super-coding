package com.github.supercoding.repository.reservations;

import org.springframework.stereotype.Repository;

@Repository
public interface ReservationRepository {
    //Boolean saveReservation(Reservation reservation);

    //Reservation findReservationWithPassengerIdAndAirlineTicketId(Integer userId, Integer airlineTicketId);

    void updateReservationStatus(Integer reservationId, String status);
}
