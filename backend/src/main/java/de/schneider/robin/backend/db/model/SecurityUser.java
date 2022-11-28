package de.schneider.robin.backend.db.model;

import lombok.Getter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.UUID;

@Entity
@Table(name = "security_user")
@Getter
public class SecurityUser {

    @Id
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "org.hibernate.id.UUIDGenerator")
    private UUID userId;

    private String username;

    private String password;

    private String roles;

    public SecurityUser() {}

    public SecurityUser(
            String username, String password, String roles)
    {
        this.username = username;
        this.password = password;
        this.roles = roles;
    }


}
