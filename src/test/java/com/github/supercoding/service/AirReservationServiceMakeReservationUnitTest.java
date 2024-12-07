package com.github.supercoding.service;

import com.github.supercoding.repository.airlineTicket.AirTicketJpaRepository;
import com.github.supercoding.repository.airlineTicket.AirlineTicket;
import com.github.supercoding.repository.flight.Flight;
import com.github.supercoding.repository.passenger.Passenger;
import com.github.supercoding.repository.passenger.PassengerJpaRepository;
import com.github.supercoding.repository.reservations.Reservation;
import com.github.supercoding.repository.reservations.ReservationJpaRepository;
import com.github.supercoding.repository.users.UserEntity;
import com.github.supercoding.service.exceptions.NotFoundException;
import com.github.supercoding.web.dto.airline.ReservationRequest;
import com.github.supercoding.web.dto.airline.ReservationResult;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@Slf4j
class AirReservationServiceMakeReservationUnitTest {

    @Mock
    private PassengerJpaRepository passengerJpaRepository;

    @Mock
    private ReservationJpaRepository reservationJpaRepository;

    @Mock
    private AirTicketJpaRepository airTicketJpaRepository;

    @InjectMocks
    private AirReservationService airReservationService;

    // Mock 과 같은 어노테이션이 적용된 필드들을 자동으로 초기화하고 모킹 객체를 설정
    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @DisplayName("정상적으로 makeReservation 이 작동하는 경우")
    @Test
    void makeReservation() {
        // given
        Integer userId = 5;
        Integer airlineTicketId = 10;
        ReservationRequest reservation = new ReservationRequest(userId,airlineTicketId);

        AirlineTicket airlineTicket = AirlineTicket.builder().ticketType("왕복")
                .arrivalLocation("파리")
                .departureLocation("서울")
                .returnAt(LocalDateTime.now())
                .ticketId(airlineTicketId)
                .tax(1234.0)
                .totalPrice(15000.0)
                .build();

        List<Flight> flightList = Arrays.asList(
                new Flight(1,airlineTicket,LocalDateTime.now(),LocalDateTime.now(),"서울","파리",20000.0,5000.0),
                new Flight(2,airlineTicket,LocalDateTime.now(),LocalDateTime.now(),"서울","파리",20000.0,5000.0),
                new Flight(3,airlineTicket,LocalDateTime.now(),LocalDateTime.now(),"서울","파리",20000.0,5000.0),
                new Flight(4,airlineTicket,LocalDateTime.now(),LocalDateTime.now(),"서울","파리",20000.0,5000.0),
                new Flight(5,airlineTicket,LocalDateTime.now(),LocalDateTime.now(),"서울","파리",20000.0,5000.0),
                new Flight(6,airlineTicket,LocalDateTime.now(),LocalDateTime.now(),"서울","파리",20000.0,5000.0)

        );

        airlineTicket.setFlightList(flightList);

        Passenger passenger = Passenger.builder()
                .passengerId(1234)
                .passportNum("1234")
                .user(new UserEntity())
                .build();


        // when
        when(airTicketJpaRepository.findById(any())).thenReturn(Optional.ofNullable(airlineTicket));
        when(passengerJpaRepository.findPassengerByUserUserId(any())).thenReturn(Optional.ofNullable(passenger));
        when(reservationJpaRepository.save(any())).thenReturn(null);


        // then
        ReservationResult reservationResult = airReservationService.makeReservation(reservation);
        log.info(reservationResult.toString());

    }
    @DisplayName("airlineTicket 이 없을 경우 오류 발생")
    @Test
    void makeReservation2() {
        // given
        Integer userId = 5;
        Integer airlineTicketId = 10;
        ReservationRequest reservation = new ReservationRequest(userId,airlineTicketId);

        AirlineTicket airlineTicket = null;

        List<Flight> flightList = Arrays.asList(
                new Flight(1,airlineTicket,LocalDateTime.now(),LocalDateTime.now(),"서울","파리",20000.0,5000.0),
                new Flight(2,airlineTicket,LocalDateTime.now(),LocalDateTime.now(),"서울","파리",20000.0,5000.0),
                new Flight(3,airlineTicket,LocalDateTime.now(),LocalDateTime.now(),"서울","파리",20000.0,5000.0),
                new Flight(4,airlineTicket,LocalDateTime.now(),LocalDateTime.now(),"서울","파리",20000.0,5000.0),
                new Flight(5,airlineTicket,LocalDateTime.now(),LocalDateTime.now(),"서울","파리",20000.0,5000.0),
                new Flight(6,airlineTicket,LocalDateTime.now(),LocalDateTime.now(),"서울","파리",20000.0,5000.0)

        );

        Passenger passenger = Passenger.builder()
                .passengerId(1234)
                .passportNum("1234")
                .user(new UserEntity())
                .build();

        // when
        when(airTicketJpaRepository.findById(any())).thenReturn(Optional.ofNullable(airlineTicket));
        when(passengerJpaRepository.findPassengerByUserUserId(any())).thenReturn(Optional.ofNullable(passenger));
        when(reservationJpaRepository.save(any())).thenReturn(null);

        // then
        assertThrows(NotFoundException.class, () -> airReservationService.makeReservation(reservation));
    }

    @DisplayName("Passenger 못찾는 경우 에러 발생")
    @Test
    void makeReservation3() {
        // given
        Integer userId = 5;
        Integer airlineTicketId = 10;
        ReservationRequest reservation = new ReservationRequest(userId,airlineTicketId);

        AirlineTicket airlineTicket = AirlineTicket.builder().ticketType("왕복")
                .arrivalLocation("파리")
                .departureLocation("서울")
                .departureAt(LocalDateTime.now())
                .returnAt(LocalDateTime.now())
                .ticketId(airlineTicketId)
                .tax(1234.0)
                .totalPrice(15000.0)
                .build();

        List<Flight> flightList = Arrays.asList(
                new Flight(1,airlineTicket,LocalDateTime.now(),LocalDateTime.now(),"서울","파리",20000.0,5000.0),
                new Flight(2,airlineTicket,LocalDateTime.now(),LocalDateTime.now(),"서울","파리",20000.0,5000.0),
                new Flight(3,airlineTicket,LocalDateTime.now(),LocalDateTime.now(),"서울","파리",20000.0,5000.0),
                new Flight(4,airlineTicket,LocalDateTime.now(),LocalDateTime.now(),"서울","파리",20000.0,5000.0),
                new Flight(5,airlineTicket,LocalDateTime.now(),LocalDateTime.now(),"서울","파리",20000.0,5000.0),
                new Flight(6,airlineTicket,LocalDateTime.now(),LocalDateTime.now(),"서울","파리",20000.0,5000.0)

        );
        airlineTicket.setFlightList(flightList);

        Passenger passenger = null;

        // when
        when(airTicketJpaRepository.findById(any())).thenReturn(Optional.ofNullable(airlineTicket));
        when(passengerJpaRepository.findPassengerByUserUserId(any())).thenReturn(Optional.ofNullable(passenger));
        when(reservationJpaRepository.save(any())).thenReturn(null);

        // then

        assertThrows(NotFoundException.class, () -> airReservationService.makeReservation(reservation));


    }

    @DisplayName("airlineTicket 에 flight 가 없으면 에러")
    @Test
    void makeReservation4() {
        // given
        Integer userId = 5;
        Integer airlineTicketId = 10;
        ReservationRequest reservationRequest = new ReservationRequest(userId, airlineTicketId);

        AirlineTicket airlineTicket = AirlineTicket.builder()
                .ticketType("왕복")
                .arrivalLocation("파리")
                .departureLocation("서울")
                .departureAt(LocalDateTime.now())
                .returnAt(LocalDateTime.now())
                .ticketId(airlineTicketId)
                .flightList(new ArrayList<>())
                .tax(1234.0)
                .totalPrice(15000.0)
                .build();

        Passenger passenger = Passenger.builder()
                .passengerId(1234)
                .passportNum("1234")
                .user(new UserEntity())
                .build();

        // when
        when(airTicketJpaRepository.findById(any())).thenReturn(Optional.ofNullable(airlineTicket));
        when(passengerJpaRepository.findPassengerByUserUserId(userId)).thenReturn(Optional.ofNullable(passenger));
        when(reservationJpaRepository.save(any())).thenReturn(null);

        // then
        assertThrows(NotFoundException.class, () ->
                airReservationService.makeReservation(reservationRequest)
        );
    }
}