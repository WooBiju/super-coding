package com.github.supercoding.service.mapper;

import com.github.supercoding.repository.flight.Flight;
import com.github.supercoding.repository.flight.FlightInfo;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

@Mapper
public interface FlightMapper {

    FlightMapper INSTANCE = Mappers.getMapper(FlightMapper.class);

    FlightInfo flightToFlightInfo(Flight flight) ;
}
