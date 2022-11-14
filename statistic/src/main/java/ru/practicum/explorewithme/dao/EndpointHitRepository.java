package ru.practicum.explorewithme.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.explorewithme.model.EndpointHit;

public interface EndpointHitRepository extends JpaRepository<EndpointHit, Long> {
}
