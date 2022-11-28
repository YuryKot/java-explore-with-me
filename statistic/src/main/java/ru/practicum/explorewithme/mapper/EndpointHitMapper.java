package ru.practicum.explorewithme.mapper;

import ru.practicum.explorewithme.dto.EndpointHitDto;
import ru.practicum.explorewithme.model.EndpointHit;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class EndpointHitMapper {

    public static EndpointHit toEndpointHit(EndpointHitDto endpointHitDto) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        EndpointHit endpointHit = new EndpointHit();
        endpointHit.setIp(endpointHitDto.getIp());
        endpointHit.setUri(endpointHitDto.getUri());
        endpointHit.setApp(endpointHitDto.getApp());
        LocalDateTime timestamp = LocalDateTime.parse(endpointHitDto.getTimestamp(), formatter);
        endpointHit.setTimestamp(timestamp);
        return endpointHit;
    }
}
