package de.schneider.robin.backend.across.mapper;

import de.schneider.robin.backend.db.model.Hobby;
import de.schneider.robin.lib.api.model.HobbyRequest;
import de.schneider.robin.lib.api.model.HobbyResponse;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface HobbyMapper {

    HobbyResponse entityToResponse(
            Hobby hobby
    );

    Hobby requestToEntity(
            HobbyRequest request
    );

}
