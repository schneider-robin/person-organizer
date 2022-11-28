package de.schneider.robin.backend.db.repository;

import de.schneider.robin.backend.db.model.StatisticEvent;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface StatisticEventRepository extends MongoRepository<StatisticEvent, String> {
}

