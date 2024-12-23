package com.github.supercoding.web.controller;

import com.github.supercoding.repository.UserDetails.CustomUserDetails;
import com.github.supercoding.repository.flight.FlightInfo;
import com.github.supercoding.service.AirReservationService;
import com.github.supercoding.service.exceptions.InvalidValueException;
import com.github.supercoding.service.exceptions.NotAcceptException;
import com.github.supercoding.service.exceptions.NotFoundException;
import com.github.supercoding.web.dto.airline.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/v1/api/air-reservation/")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "항공권 예약")
public class AirReservationController {

    private final AirReservationService airReservationService;

    @GetMapping("/tickets")
    @Operation(summary = "선호하는 ticket 검색")
    public TicketResponse findAirlineTickets(@AuthenticationPrincipal CustomUserDetails customUserDetails,
                                             @RequestParam("airline-ticket-type")String ticketType){

            Integer userId = customUserDetails.getUserId();
            List<Ticket> tickets = airReservationService.findUserFavoritePlaceTickets(userId,ticketType);
            //log.info("findTickets 호출됨: userId={}, ticketType={}", userId, ticketType);
            return new TicketResponse(tickets);
    }


    @PostMapping("/reservations")
    @Operation(summary = "ticket 예약 진행")
    @ResponseStatus(HttpStatus.CREATED)
    public ReservationResult makeReservation(@RequestBody ReservationRequest reservationRequest) {
            ReservationResult reservationResult =  airReservationService.makeReservation(reservationRequest);
            return reservationResult;
    }

    @PostMapping("/payments")
    @Operation(summary = "예약한 ticket 결제")
    public String makePayments(@RequestBody PaymentRequest paymentRequest ) {
        Integer successPayments = airReservationService.makePayments(paymentRequest);
        return "요청하신 결제 중 " + successPayments + "건 진행완료 되었습니다.";
    }

    @GetMapping("/users-sum-price")
    @Operation(summary = "userId의 예약한 항공편과 수수료 총합")
    public Double findUserFlightSumPrice(@RequestParam("user-id") Integer userId) {
        Double sum = airReservationService.findUserFlightSumPrice(userId);
        return sum;
    }

    @Operation(summary = "편도|왕복 의 비행기 Pageable")
    @GetMapping("/flight-pageable")
    public Page<FlightInfo> findFlightWithTicketType(@RequestParam("type") String ticketType, Pageable pageable) {
        return airReservationService.findFlightsWithTypeAndPageable(ticketType,pageable);
    }

    @Operation(summary = "userId 의 예약한 항공편들의 목적지 출력")
    @GetMapping("/username-arrival-location")
    public Set<String> findUser(@RequestParam("username") String userName) {
        return airReservationService.findFlightArrivalLocation(userName);
    }


}
