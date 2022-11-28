package de.schneider.robin.backend.db.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "person")
//@Data
//@Setter // not needed, mapstruct uses builder in requestToEntity
@Getter // needed, mapstruct uses it in entityToResponse
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Person {

    @Id
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "person_id", updatable = false, nullable = false)
    private UUID personId;

    @Column(name = "firstname", nullable = false)
    private String firstname;

    @Column(name = "lastname", nullable = false)
    private String lastname;

    @CreationTimestamp
    @Column(name = "creation_timestamp", nullable = false, updatable = false)
    private Instant creationTimestamp;

    @OneToOne(optional = false, cascade = CascadeType.ALL)
    @JoinColumn(name = "person_attributes_id") // pk in PersonAttributes
    private PersonAttributes personAttributes;

    @ManyToOne(optional = false, cascade = CascadeType.ALL)
    @JoinColumn(name = "physical_address_id") // pk in PhysicalAddress
    private PhysicalAddress physicalAddress;

    @OneToMany(mappedBy = "person", cascade = CascadeType.ALL)
//    @JoinColumn(name = "person") // extra field in MailAddress
    private List<MailAddress> mailAddresses;

    @ManyToMany(cascade = { CascadeType.ALL })
    @JoinTable(name = "person_hobby_mapping", // extra table
            joinColumns = { @JoinColumn(name = "person_id") },
            inverseJoinColumns = { @JoinColumn(name = "hobby_id") })
    private List<Hobby> hobbies;

}
