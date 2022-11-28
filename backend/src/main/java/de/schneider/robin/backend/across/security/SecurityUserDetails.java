package de.schneider.robin.backend.across.security;

import de.schneider.robin.backend.db.model.SecurityUser;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Arrays;
import java.util.Collection;
import java.util.stream.Collectors;

public class SecurityUserDetails implements UserDetails {

    private final SecurityUser securityUser;

    public SecurityUserDetails(SecurityUser securityUser) {
        this.securityUser = securityUser;
    }

    @Override
    public String getUsername() {
        return securityUser.getUsername();
    }

    @Override
    public String getPassword() {
        return securityUser.getPassword();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Arrays
                .stream(securityUser.getRoles().split(","))
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}