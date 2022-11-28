package de.schneider.robin.backend.across.security;

import de.schneider.robin.backend.across.error.SecurityUserNotFoundError;
import de.schneider.robin.backend.db.repository.SecurityUserRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@Log4j2
@Qualifier("customUserDetailsService")
public class CustomUserDetailsService implements UserDetailsService {

    private final SecurityUserRepository securityUserRepository;

    public CustomUserDetailsService(
            SecurityUserRepository securityUserRepository
    ) {
        this.securityUserRepository = securityUserRepository;
    }

    @Override
    public UserDetails loadUserByUsername(
            String username
    ) throws UsernameNotFoundException {

        UserDetails userDetails = securityUserRepository
                .findByUsername(username)
                .map(SecurityUserDetails::new)
                .orElseThrow(() -> new SecurityUserNotFoundError(username));
        // Unfortunately this exception will be caught by the Spring Security (use the debugger to see it)
        log.debug("SecurityUserDetails: " + userDetails);
        return userDetails;
    }
}
