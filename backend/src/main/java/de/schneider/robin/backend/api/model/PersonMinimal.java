package de.schneider.robin.backend.api.model;

import com.opencsv.bean.CsvBindByName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PersonMinimal {

    @CsvBindByName(column = "FIRST_NAME", required = true)
    private String firstname;

    @CsvBindByName(column = "LAST_NAME", required = true)
    private String lastname;
}
