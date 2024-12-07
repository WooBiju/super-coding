package com.github.supercoding.service.mapper;

import com.github.supercoding.repository.airlineTicket.AirlineTicket;
import com.github.supercoding.web.dto.airline.Ticket;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Mapper
public interface TicketMapper {

    static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    TicketMapper INSTANCE = Mappers.getMapper(TicketMapper.class);

    //메서드
    @Mapping(target = "depart",source = "departureLocation")
    @Mapping(target = "arrival",source = "arrivalLocation")
    @Mapping(target = "departureTime",source = "departureAt",qualifiedByName = "convert")
    @Mapping(target = "returnTime",source = "returnAt",qualifiedByName = "convert")
    Ticket airlineTicketToTicket(AirlineTicket airlineTicket);

    // 따로 메서드 정의해서 mapper 에 적용 (포멧)
    @Named("convert")
    static String localDateTimeToString(LocalDateTime localDateTime){
        if (localDateTime != null) return localDateTime.format(formatter);
        else return null;
    }
}
