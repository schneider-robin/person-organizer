package de.schneider.robin.backend.db.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.util.UUID;

@Entity
@Table(name = "mail_address")
@Data
@ToString(exclude = { "person" }) // avoid stackoverflow error of circular dependency
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MailAddress {

    @Id
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "mail_address_id", updatable = false, nullable = false)
    private UUID mailAddressId;

    @Column(name = "contact", nullable = false)
    private String contact;

    @Column(name = "is_business", nullable = false)
    private Boolean isBusiness;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "person_id", nullable = false)
    private Person person;

}
