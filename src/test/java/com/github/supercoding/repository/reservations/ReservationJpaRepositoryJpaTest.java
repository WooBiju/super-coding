package com.github.supercoding.repository.reservations;

import com.github.supercoding.repository.airlineTicket.AirTicketJpaRepository;
import com.github.supercoding.repository.airlineTicket.AirlineTicket;
import com.github.supercoding.repository.passenger.Passenger;
import com.github.supercoding.repository.passenger.PassengerJpaRepository;
import com.github.supercoding.service.AirReservationService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.relational.core.sql.In;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

// DB 연결 테스트
@DataJpaTest // slice Test => Dao Lay /Jpa 사용하고 있는 애들만 부름
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Slf4j
class ReservationJpaRepositoryJpaTest {

    // Repository 만 가능함
//    @Autowired
//    private AirReservationService airReservationService;

    @Autowired
    private ReservationJpaRepository reservationJpaRepository;

    @Autowired
    private PassengerJpaRepository passengerJpaRepository;

    @Autowired
    private AirTicketJpaRepository airTicketJpaRepository;

    @DisplayName("ReservationRapository 로 항공편 가격과 수수료 검색")
    @Test
    void findFlightPriceAndCharge() {
        //given
        Integer userId = 10;

        //when
        List<FlightPriceAndCharge> flightPriceAndCharges = reservationJpaRepository.findFlightPriceAndCharge(userId);

        //then
        log.info("flightPriceAndCharges: {}", flightPriceAndCharges);

    }
    @DisplayName("Reservation 예약 진행")
    @Test
    void saveReservation() {
        // given
        Integer userId = 10;
        Integer ticketId = 5;

        Passenger passenger = passengerJpaRepository.findPassengerByUserUserId(userId).get();
        AirlineTicket airlineTicket = airTicketJpaRepository.findById(5).get();

        // when
        Reservation reservation = new Reservation(passenger,airlineTicket);
        Reservation res = reservationJpaRepository.save(reservation);

        // then
        log.info("reservation: {}", res);
        assertEquals(res.getPassenger(), passenger);
        assertEquals(res.getAirlineTicket(), airlineTicket);

    }
}