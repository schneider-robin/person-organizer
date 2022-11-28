package de.schneider.robin.backend.across.mapper;

import de.schneider.robin.backend.db.model.PersonAttributes;
import de.schneider.robin.lib.api.model.PersonAttributesRequest;
import de.schneider.robin.lib.api.model.PersonAttributesResponse;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PersonAttributesMapper {

    PersonAttributesResponse entityToResponse(
            PersonAttributes personAttributes
    );

    PersonAttributes requestToEntity(
            PersonAttributesRequest request
    );
}
