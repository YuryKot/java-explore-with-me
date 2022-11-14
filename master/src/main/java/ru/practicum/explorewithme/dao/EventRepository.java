package ru.practicum.explorewithme.dao;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.explorewithme.model.Event;
import ru.practicum.explorewithme.model.EventState;

import java.time.LocalDateTime;
import java.util.List;

public interface EventRepository extends JpaRepository<Event, Long> {

    List<Event> findByInitiator_Id(Long id, Pageable pageable);

    @Query("select e from Event e where " +
            "(lower(e.annotation) like lower(concat('%', ?1, '%')) or " +
            "lower(e.description) like lower(concat('%', ?1, '%'))) and " +
            "e.category in ?2 and " +
            "e.paid = ?3 and " +
            "e.eventDate between ?4 and ?5 and " +
            "e.state = ?6 " +
            "order by e.eventDate")
    List<Event> getEventsWithFilter(String text, List<Integer> categories, Boolean paid,
                                    LocalDateTime start, LocalDateTime end, EventState state, Pageable pageable);

}
