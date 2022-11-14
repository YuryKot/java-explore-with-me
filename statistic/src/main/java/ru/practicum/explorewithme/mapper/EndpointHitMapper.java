package ru.practicum.explorewithme.mapper;

import ru.practicum.explorewithme.dto.EndpointHitDto;
import ru.practicum.explorewithme.model.EndpointHit;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class EndpointHitMapper {

    final static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public static EndpointHit toEndpointHit(EndpointHitDto endpointHitDto) {
        EndpointHit endpointHit = new EndpointHit();
        endpointHit.setIp(endpointHitDto.getIp());
        endpointHit.setUri(endpointHit.getUri());
        endpointHit.setApp(endpointHit.getApp());
        LocalDateTime timestamp = LocalDateTime.parse(endpointHitDto.getTimestamp(), formatter);
        endpointHit.setTimestamp(timestamp);
        return endpointHit;
    }
}
