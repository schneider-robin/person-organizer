package de.schneider.robin.backend.db.repository;

import de.schneider.robin.backend.db.model.PhysicalAddress;
import org.springframework.data.repository.CrudRepository;

import java.util.UUID;

public interface PhysicalAddressRepository extends
        CrudRepository<PhysicalAddress, UUID>
{
    PhysicalAddress findPhysicalAddressByPhysicalAddressId(
            UUID id
    );
}
