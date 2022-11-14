package ru.practicum.explorewithme.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.explorewithme.dto.EndpointHitDto;
import ru.practicum.explorewithme.service.EndpointHitService;

@RestController
@RequiredArgsConstructor
public class Controller {

    private final EndpointHitService endpointHitService;

    @PostMapping("/hits")
    public void addEndpointHit(EndpointHitDto endpointHitDto) {
        endpointHitService.addEndpointHit(endpointHitDto);
    }
}
