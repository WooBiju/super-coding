package com.github.supercoding.repository.reservations;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.sql.Timestamp;

@Repository
public class ReservationJdbcTemplateDao implements ReservationRepository {

    private JdbcTemplate jdbcTemplate;

    public ReservationJdbcTemplateDao(@Qualifier("jdbcTemplate2") JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

//    static RowMapper<Reservation> reservationRowMapper = (((rs, rowNum) ->
//            new Reservation.ReservationBuilder()
//                    .reservationId(rs.getInt("reservation_id"))
//                    .airlineTicket.getTicketId(rs.getInt("airline_ticket_id"))
//                    .passengerId(rs.getInt("passenger_id"))
//                    .reservationStatus(rs.getNString("reservation_status"))
//                    .reserveAt(rs.getDate("reserve_at").toLocalDate().atStartOfDay())
//                    .build()
//    ));
//
//
//    @Override
//    public Boolean saveReservation(Reservation reservation) {
//
//        try{
//            Integer rowNums = jdbcTemplate.update("INSERT INTO reservation(passenger_id,airline_ticket_id,reservation_status,reserve_at) VALUES (?,?,?,?)",
//                    reservation.getPassenger().getPassengerId(),reservation.getAirlineTicketId(),reservation.getReservationStatus(),
//                    new Date(Timestamp.valueOf(reservation.getReserveAt()).getTime()));
//            return rowNums > 0;
//        }catch (Exception e){
//            throw new RuntimeException();
//        }
//
//    }
//
//    @Override
//    public Reservation findReservationWithPassengerIdAndAirlineTicketId(Integer passengerId, Integer airlineTicketId) {
//        try {
//            return jdbcTemplate.queryForObject("SELECT * FROM reservation WHERE passenger_id = ? AND airline_ticket_id = ?", reservationRowMapper, passengerId, airlineTicketId);
//        }catch (Exception e) {
//            return null;
//        }
//    }
//
    @Override
    public void updateReservationStatus(Integer reservationId, String status) {
        jdbcTemplate.update("UPDATE reservation " +
                                " SET reservation_status = ? " +
                                " WHERE reservation_id = ?", status, reservationId);

    }
}
