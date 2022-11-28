package de.schneider.robin.backend.db.model;

import de.schneider.robin.backend.db.type.Country;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.UUID;

@Entity
@Table(name = "person_attributes")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PersonAttributes {

    @Id
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "person_attributes_id", updatable = false, nullable = false)
    private UUID personAttributesId;

    @Column(name = "age", nullable = false)
    private int age;

    @Column(name = "height", nullable = false)
    private double height;

    @Column(name = "country", nullable = false)
    @Enumerated(EnumType.STRING)
    private Country country;

}
