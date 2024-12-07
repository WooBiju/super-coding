package com.github.supercoding.repository.passenger;


import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.github.supercoding.repository.users.UserEntity;
import jakarta.persistence.*;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
@Entity
@Table(name = "passenger" )
public class Passenger {

    @Id @Column(name = "passenger_id") @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer passengerId;


    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id",unique = true,updatable = false)
    private UserEntity user;

    @Column(name = "passport_num",length = 50)
    private String passportNum;



}
