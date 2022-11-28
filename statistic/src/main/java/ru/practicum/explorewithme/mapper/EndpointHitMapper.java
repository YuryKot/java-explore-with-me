package ru.practicum.explorewithme.mapper;

import ru.practicum.explorewithme.dto.EndpointHitDto;
import ru.practicum.explorewithme.model.EndpointHit;

import java.time.LocalDateTime;

public class EndpointHitMapper {

    public static EndpointHit toEndpointHit(EndpointHitDto endpointHitDto) {
        EndpointHit endpointHit = new EndpointHit();
        endpointHit.setIp(endpointHitDto.getIp());
        endpointHit.setUri(endpointHitDto.getUri());
        endpointHit.setApp(endpointHitDto.getApp());
        LocalDateTime timestamp = DateTimeMapper.toLocalDateTime(endpointHitDto.getTimestamp());
        endpointHit.setTimestamp(timestamp);
        return endpointHit;
    }
}
