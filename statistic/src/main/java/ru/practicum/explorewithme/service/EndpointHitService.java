package ru.practicum.explorewithme.service;

import ru.practicum.explorewithme.dto.EndpointHitDto;
import ru.practicum.explorewithme.model.ViewStats;

import java.util.List;


public interface EndpointHitService {
    void addEndpointHit(EndpointHitDto endpointHitDto);

    List<ViewStats> getStats(String start, String end, List<String> uris, Boolean unique);
}
