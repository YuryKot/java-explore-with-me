package ru.practicum.explorewithme.mapper;

import ru.practicum.explorewithme.dto.ParticipationRequestDto;
import ru.practicum.explorewithme.model.Request;

import java.time.format.DateTimeFormatter;

public class RequestMapper {


    public static ParticipationRequestDto toParticipationRequestDto(Request request) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        ParticipationRequestDto participationRequestDto = new ParticipationRequestDto();
        participationRequestDto.setId(request.getId());
        participationRequestDto.setCreated(formatter.format(request.getCreated()));
        participationRequestDto.setEvent(request.getEvent().getId());
        participationRequestDto.setRequester(request.getRequester().getId());
        participationRequestDto.setStatus(request.getStatus().toString());
        return participationRequestDto;
    }
}
