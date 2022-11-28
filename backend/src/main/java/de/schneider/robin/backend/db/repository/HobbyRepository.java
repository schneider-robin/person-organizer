package de.schneider.robin.backend.db.repository;

import de.schneider.robin.backend.db.model.Hobby;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.UUID;

public interface HobbyRepository extends
        CrudRepository<Hobby, UUID>
{
    Hobby findHobbyByHobbyId(
            UUID id
    );

    List<Hobby> findHobbiesByHobbyIdIn(
            List<UUID> hobbyIdList
    );
}
