package ru.practicum.explorewithme.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.explorewithme.dao.EndpointHitRepository;
import ru.practicum.explorewithme.dto.EndpointHitDto;
import ru.practicum.explorewithme.mapper.EndpointHitMapper;
import ru.practicum.explorewithme.model.EndpointHit;

@Service
@RequiredArgsConstructor
public class EndpointHitServiceImpl implements EndpointHitService{

    private final EndpointHitRepository endpointHitRepository;

    @Override
    public void addEndpointHit(EndpointHitDto endpointHitDto) {
        EndpointHit endpointHit = EndpointHitMapper.toEndpointHit(endpointHitDto);
        endpointHitRepository.save(endpointHit);
    }
}
