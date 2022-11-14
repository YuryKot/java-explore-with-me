package ru.practicum.explorewithme.dto;

import lombok.Data;

@Data
public class ParticipationRequestDto {

    private Long id;

    private String created;

    private long event;

    private long requester;

    private String status;
}
