package com.github.supercoding.service;

import com.github.supercoding.repository.airlineTicket.AirTicketJpaRepository;
import com.github.supercoding.repository.airlineTicket.AirlineTicket;
import com.github.supercoding.repository.airlineTicket.AirlineTicketFlightInfo;
import com.github.supercoding.repository.airlineTicket.AirlineTicketRepository;
import com.github.supercoding.repository.flight.Flight;
import com.github.supercoding.repository.passenger.Passenger;
import com.github.supercoding.repository.passenger.PassengerJpaRepository;

import com.github.supercoding.repository.payments.Payment;
import com.github.supercoding.repository.payments.PaymentRepository;
import com.github.supercoding.repository.reservations.FlightPriceAndCharge;
import com.github.supercoding.repository.reservations.Reservation;
import com.github.supercoding.repository.reservations.ReservationJpaRepository;
import com.github.supercoding.repository.reservations.ReservationRepository;
import com.github.supercoding.repository.users.UserEntity;
import com.github.supercoding.repository.users.UserJpaRepository;

import com.github.supercoding.service.exceptions.InvalidValueException;
import com.github.supercoding.service.exceptions.NotFoundException;
import com.github.supercoding.service.mapper.TicketMapper;
import com.github.supercoding.web.dto.airline.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class AirReservationService {

    private final UserJpaRepository userJpaRepository;

    private final AirTicketJpaRepository airTicketJpaRepository;

    private final PassengerJpaRepository passengerJpaRepository;

    private final ReservationJpaRepository reservationJpaRepository;
    private final ReservationRepository reservationRepository;

    private final PaymentRepository paymentRepository;

    public List<Ticket> findUserFavoritePlaceTickets(Integer userId, String ticketType) {
        // 필요한 Repository : UserRepository , AirlineTicketRepository
        // 1. 유저 userId 를 가져와서 , 선호하는 여행지 도출
        // 2. 선호하는 여행지와 ticketType 으로 AirlineTicket table 질의 해서 필요한 AirlineTicket 얻기
        // 3. 이 둘의 정보를 조합해서 Ticket DTO 만들기

        // 예외처리
        Set<String> ticketTypeSet = new HashSet<>(Arrays.asList("편도","왕복"));
        if (!ticketTypeSet.contains(ticketType)) {
            throw new InvalidValueException("해당 TicketType " + ticketType + "은 지원하지 않습니다.");
        }

        UserEntity userEntity = userJpaRepository.findById(userId)
                .orElseThrow(()->new NotFoundException("해당 "+ userId  +" 유저를 찾을 수 없습니다."));

        String likePlace = userEntity.getLikeTravelPlace();

        List<AirlineTicket> airlineTickets = airTicketJpaRepository.findAirlineTicketsByArrivalLocationAndTicketType(likePlace,ticketType);

        if(airlineTickets.isEmpty()) {
            throw new NotFoundException("해당 likePlace: " + likePlace + " 와 TicketType: " + ticketType + "에 해당하는 항공권 찾을 수 없습니다.");
        }

        List<Ticket> tickets = airlineTickets.stream().map(TicketMapper.INSTANCE::airlineTicketToTicket).collect(Collectors.toList());
        return tickets;

    }

    @Transactional(transactionManager = "tm2")
    public ReservationResult makeReservation(ReservationRequest reservationRequest) {
        // 필요한 Repository : ReservationRepository, Join table (flight/airline_ticket) , PassengerRepository

        // 0. userId, airline_ticket_id
        Integer userId = reservationRequest.getUserId();
        Integer airlineTicketId = reservationRequest.getAirlineTicketId();

        AirlineTicket airlineTicket = airTicketJpaRepository.findById(airlineTicketId)
                .orElseThrow(() -> new NotFoundException("airlineTicket 찾을 수 없습니다."));

        // 1. Passenger ID
        Passenger passenger = passengerJpaRepository.findPassengerByUserUserId(userId)
                .orElseThrow(()-> new NotFoundException("요청하신 userId " + userId + "에 해당하는 PassengerId 찾을 수 없습니다."));

        Integer passengerId = passenger.getPassengerId();

        // 2. 정보 불러오기

        List<Flight> flightList = airlineTicket.getFlightList();

        if(flightList.isEmpty()) {
            throw new NotFoundException("AirlineTicket Id " + airlineTicketId + " 에 해당하는 항공편과 항공권 찾을 수 없습니다.");
        }

        Boolean isSuccess =  false;

        // 3. reservation 생성
        Reservation reservation = new Reservation(passenger,airlineTicket);
        try{
            reservationJpaRepository.save(reservation);
            isSuccess = true;
        } catch (RuntimeException e) {
            throw new NotFoundException("예약을 등록하는 과정에서 오류가 발생했습니다.");
        }




        // 4. ReservationResult DTO 만들기
        List<Integer> prices = flightList.stream().map(Flight::getFlightPrice).map(Double::intValue).collect(Collectors.toList());
        List<Integer> charges = flightList.stream().map(Flight::getCharge).map(Double::intValue).collect(Collectors.toList());
        Integer tax = airlineTicket.getTax().intValue();
        Integer totalPrice = airlineTicket.getTotalPrice().intValue();

        return new ReservationResult(prices,charges,tax,totalPrice,isSuccess);

    }

    @Transactional(transactionManager = "tm2")
    public Integer makePayments(PaymentRequest paymentRequest) {
        // 필요한 Repository : PaymentRepository , reservationRepository , passengerRepository

        //      2-1. 만약 userId 와 airline_ticket 해당하는 Reservation 이 2개 이상이면 실패
        //      2-2. 만약 UserId 와 airline_ticket 해당하는 Reservation 이 0개면 실패

        // 0. userIds, airlineTicketIds 추출 및 정상 Input 조건 확인
        List<Integer> userIds = paymentRequest.getUserIds();
        List<Integer> airlineTicketIds = paymentRequest.getAirlineTicketIds();

        // 추가 요구사항
        if(userIds.size() != airlineTicketIds.size()){
            log.error("userId 와 airlineTicketIds 는 항상 같아야 합니다.");
            throw new RuntimeException("userId 와 airlineTicketIds 는 항상 같아야 합니다.");
        }
        // 1. 각 userIds 에 해당하는 passengers 검색 및 id 추출
//            List<Integer> passengerIds = userIds.stream().map((passengerRepository::findPassengerByUserId))
//                    .map(Passenger::getPassengerId).toList();
        List passengerIds = userIds.stream()
                .map(userId -> { Passenger passenger= passengerJpaRepository.findPassengerByUserUserId(userId)
                        .orElseThrow(()-> new NotFoundException("요청하신 userId " + userId + "에 해당하는 PassengerId 찾을 수 없습니다."));
            if (passenger == null) {
                log.error(userId + " 와 맞는 승객을 찾을 수 없습니다.");
                throw new RuntimeException("Passenger not found for userId: " + userId);
            }
            return passenger.getPassengerId(); }) .toList();


        List<Reservation> reservationCandidateList = new ArrayList<>();
        int successCount = 0;

        for (int i = 0; i < userIds.size(); i++) {
            Integer passengerId = (Integer) passengerIds.get(i);
            Integer airlineTicketId = airlineTicketIds.get(i);

            // 2. 각 passenger 와 airline_ticket 에 해당하는 reservation 검색
            Reservation reservation = reservationJpaRepository.findReservationByPassengerPassengerIdAndAirlineTicketTicketId(passengerId, airlineTicketId);
            reservationCandidateList.add(reservation);
        }
            // 3. Reservation 을 찾았는데, 해당 reservation 이 이미 "확정"상태이면 결제 실패
            for(Reservation reservation : reservationCandidateList){
                if(reservation == null) continue;
                if(reservation.getReservationStatus().equals("확정")) continue;

                // 4. 모든 조건 만족하는 Reservation 를 찾고 passengerId 와 ReservationId로 Payment 생성 후 count++
                Payment paymentNew = new Payment(reservation.getReservationId(), reservation.getPassenger().getPassengerId());
                Boolean success = paymentRepository.savePayment(paymentNew);

                // 5. Reservation 상태 "대기" -> "확정"으로 변경.
                if(success){
                    successCount++;
                    reservationRepository.updateReservationStatus(reservation.getReservationId(),"확정");
                }
            }

        return successCount;
    }

    public Double findUserFlightSumPrice(Integer userId) {
        // 1. flight_price , Charge 구하기
        List<FlightPriceAndCharge> flightPriceAndCharges = reservationJpaRepository.findFlightPriceAndCharge(userId);

        // 2. 모든 Flight_price 와 charge 의 각각 합을 구하고

        Double flightSum = flightPriceAndCharges.stream().mapToDouble(FlightPriceAndCharge::getFlightPrice).sum();
        Double chargeSum = flightPriceAndCharges.stream().mapToDouble(FlightPriceAndCharge::getCharge).sum();

        // 3. 두개의 합을 다시 더하고 Return
        return flightSum + chargeSum;

    }
}
