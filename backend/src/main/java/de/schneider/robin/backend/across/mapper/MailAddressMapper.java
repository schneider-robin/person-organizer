package de.schneider.robin.backend.across.mapper;

import de.schneider.robin.backend.db.model.MailAddress;
import de.schneider.robin.lib.api.model.MailAddressResponse;
import de.schneider.robin.lib.api.model.predefined.MailAddressRequest;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface MailAddressMapper {

    MailAddressResponse entityToResponse(
            MailAddress mailAddress
    );

    MailAddress requestToEntity(
            MailAddressRequest request
    );

}
