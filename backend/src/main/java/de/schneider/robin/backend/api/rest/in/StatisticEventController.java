package de.schneider.robin.backend.api.rest.in;

import de.schneider.robin.backend.db.model.StatisticEvent;
import de.schneider.robin.backend.db.repository.StatisticEventRepository;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.log4j.Log4j2;
import org.springframework.context.annotation.Profile;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@Log4j2
@Tag(name = "StatisticEventController", description = "store and retrieve statistic events")
public class StatisticEventController {

    private final StatisticEventRepository statisticEventRepository;

    public StatisticEventController(
            StatisticEventRepository statisticEventRepository
    ) {
        this.statisticEventRepository = statisticEventRepository;
    }

    @PostMapping(path = {"/event-log"})
    public void saveStatisticEvent(
            @RequestParam(name = "message") String message
    ) {
        statisticEventRepository.save(new StatisticEvent(message));
    }

    @GetMapping(path = {"/event-log/all"})
    public List<StatisticEvent> getAllStatisticEvents(

    ) {
        return statisticEventRepository.findAll();
    }

}
