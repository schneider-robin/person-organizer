package de.schneider.robin.backend.across.mapper;

import de.schneider.robin.backend.db.model.PhysicalAddress;
import de.schneider.robin.lib.api.model.PhysicalAddressRequest;
import de.schneider.robin.lib.api.model.PhysicalAddressResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface PhysicalAddressMapper {

    @Mapping(target = "streetAndCity", expression = "java(mergeStreetAndCity(physicalAddress))")
    PhysicalAddressResponse entityToResponse(
            PhysicalAddress physicalAddress
    );

    default String mergeStreetAndCity(
            PhysicalAddress physicalAddress
    ) {
        return physicalAddress.getStreet() + " " + physicalAddress.getCity();
    }

    PhysicalAddress requestToEntity(
            PhysicalAddressRequest request
    );
}
