package de.schneider.robin.lib.api.model.predefined;

import com.fasterxml.jackson.annotation.JsonProperty;
import de.schneider.robin.lib.api.validation.MailContactValidation;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MailAddressRequest { // will be used via pom.xml importMappings, replaces the component in yml

    @MailContactValidation
    @JsonProperty("contact")
    private String contact;

    @JsonProperty("is_business")
    private Boolean isBusiness;
}
