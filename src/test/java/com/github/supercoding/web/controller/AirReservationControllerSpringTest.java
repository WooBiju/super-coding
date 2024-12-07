package com.github.supercoding.web.controller;

import jakarta.persistence.Id;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc    // MockMvc 를 자동으로 설정해줌 , HTTP 요청 쉽게 테스트 가능
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)  // 테스트 데이터베이스 설정 자동으로 처리
@Slf4j
class AirReservationControllerSpringTest {

    @Autowired
    private MockMvc mockMvc;   // MockMvc : Spring MVC 컨트롤러 테스트 하는데 사용

    @DisplayName("Find Airline Ticket 성공")
    @Test
    void findAirlineTickets() throws Exception {
        // given
        Integer userId = 5;
        String ticketType = "왕복";

        // when
        String content = mockMvc.perform(
                get("/v1/api/air-reservation/tickets")
                        .param("user-Id", userId.toString())
                        .param("airline-ticket-type",ticketType)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString(StandardCharsets.UTF_8);

        log.info("결과: " + content);


    }
}