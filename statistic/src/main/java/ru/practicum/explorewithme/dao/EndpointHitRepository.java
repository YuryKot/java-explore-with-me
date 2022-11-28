package ru.practicum.explorewithme.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import ru.practicum.explorewithme.model.EndpointHit;
import ru.practicum.explorewithme.model.ViewStats;

import java.time.LocalDateTime;
import java.util.List;

public interface EndpointHitRepository extends JpaRepository<EndpointHit, Long>, QuerydslPredicateExecutor<EndpointHit> {

    @Query("select e.app as app, e.uri as uri, count(e.id) as hits" +
            " from EndpointHit e" +
            " where e.uri in ?1 " +
            " and e.timestamp between ?2 and ?3" +
            " group by e.app, e.uri" +
            " order by hits")
    List<ViewStats> getStatsByUrisAndStartAndStop(List<String> uris, LocalDateTime start, LocalDateTime end);

    @Query(value = "select s.APP as app, s.URI as uri, count(s.IP) as hits " +
            " from (select distinct APP, URI, IP from endpoint_hits " +
            " where URI in ?1 " +
            " and TIMESTAMP between ?2 and ?3) as s " +
            " group by s.APP, s.URI" +
            " order by hits", nativeQuery = true)
    List<ViewStats> getStatsByUrisAndStartAndStopUniqIp(List<String> uris, LocalDateTime start, LocalDateTime end);

    @Query("select e.app as app, e.uri as uri, count(e.id) as hits" +
            " from EndpointHit e" +
            " where e.timestamp between ?1 and ?2" +
            " group by e.app, e.uri" +
            " order by hits")
    List<ViewStats> getStatsByStartAndStop(LocalDateTime startDateTime, LocalDateTime endDateTime);

    @Query(value = "select s.APP as app, s.URI as uri, count(s.IP) as hits " +
            " from (select distinct APP, URI, IP from endpoint_hits " +
            " where TIMESTAMP between ?1 and ?2) as s " +
            " group by s.APP, s.URI" +
            " order by hits", nativeQuery = true)
    List<ViewStats> getStatsByStartAndStopUniqIp(LocalDateTime startDateTime, LocalDateTime endDateTime);
}
