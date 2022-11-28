package de.schneider.robin.backend.across.mapper;

import org.mapstruct.Mapper;

import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;

@Mapper(componentModel = "spring")
public interface OffsetDateTimeMapper {

    ZoneId DEFAULT_ZONE_ID = ZoneOffset.UTC;
    ZoneId GERMANY_ZONE_ID_V1 = ZoneId.systemDefault();
    ZoneId GERMANY_ZONE_ID_V2 = ZoneId.of("Europe/Berlin");
    ZoneId GERMANY_ZONE_ID_V3 = ZoneOffset.of("+02:00");

    default Instant toInstant(
            OffsetDateTime dateTime
    ) {
        return (null == dateTime) ? null : Instant.from(dateTime);
    }

    default OffsetDateTime fromInstant(
            Instant instant
    ) {
        return (null == instant) ? null : OffsetDateTime.ofInstant(instant, GERMANY_ZONE_ID_V2);
    }
}
