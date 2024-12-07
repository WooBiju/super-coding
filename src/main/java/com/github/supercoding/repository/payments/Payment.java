package com.github.supercoding.repository.payments;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Objects;

// Entity
@Getter
@Setter
@EqualsAndHashCode(of = "paymentId")
@NoArgsConstructor
@Entity
@Table(name = "payment")
public class Payment {

    @Id @Column(name = "payment_id" ) @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer paymentId;

    @Column(name = "passenger_id")
    private Integer passengerId;

    @Column(name = "reservation_id")
    private Integer reservationId;

    @Column(name = "pay_at")
    private LocalDateTime payTime;


    public Payment(Integer reservationId, Integer PassengerId) {
        this.reservationId = reservationId;
        this.passengerId = PassengerId;
        this.payTime = LocalDateTime.now();
    }

}
