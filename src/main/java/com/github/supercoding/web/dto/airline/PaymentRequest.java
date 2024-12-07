package com.github.supercoding.web.dto.airline;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class PaymentRequest {

    private List<Integer> userIds;
    private List<Integer> airlineTicketIds;

}
