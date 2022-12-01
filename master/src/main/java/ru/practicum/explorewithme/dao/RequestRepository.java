package ru.practicum.explorewithme.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.explorewithme.model.Request;
import ru.practicum.explorewithme.model.StatusRequest;

import java.util.List;

public interface RequestRepository extends JpaRepository<Request, Long> {

    List<Request> findByEvent_Id(Long eventId);

    List<Request> findByEvent_IdAndStatus(Long eventId, StatusRequest statusRequest);

    List<Request> findByRequester_Id(Long userId);

    Request findByRequester_IdAndEvent_Id(Long userId, Long eventId);
}
