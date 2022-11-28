//package de.schneider.robin.backend.across.security.unused;
//
//import org.springframework.security.authentication.AuthenticationProvider;
//import org.springframework.security.authentication.InternalAuthenticationServiceException;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.authentication.dao.AbstractUserDetailsAuthenticationProvider;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.AuthenticationException;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.security.core.userdetails.UsernameNotFoundException;
//import org.springframework.stereotype.Component;
//
//import java.util.ArrayList;
//
//@Component
//public class CustomAuthenticationProvider extends AbstractUserDetailsAuthenticationProvider {
//
//    private UserDetailsService userDetailsService;
//
//    @Override
//    protected void additionalAuthenticationChecks(
//            UserDetails userDetails, UsernamePasswordAuthenticationToken authentication
//    ) throws AuthenticationException {
//
//    }
//
//    @Override
//    public Authentication authenticate(Authentication authentication)
//            throws AuthenticationException {
//
//        String name = authentication.getName();
//        String password = authentication.getCredentials().toString();
//
//        if (true) {
//
//            // use the credentials
//            // and authenticate against the third-party system
//            return new UsernamePasswordAuthenticationToken(
//                    name, password, new ArrayList<>());
//        } else {
//            return null;
//        }
//    }
//
//    @Override
//    protected UserDetails retrieveUser(
//            String username, UsernamePasswordAuthenticationToken authentication
//    ) throws AuthenticationException {
//        try {
//            UserDetails loadedUser = this.getUserDetailsService().loadUserByUsername(username);
//            if (loadedUser == null) {
//                throw new InternalAuthenticationServiceException(
//                        "UserDetailsService returned null, which is an interface contract violation");
//            }
//            return loadedUser;
//        }
//        catch (UsernameNotFoundException ex) {
//            System.out.println("");
//            throw ex;
//        }
//    }
//
//    @Override
//    public boolean supports(Class<?> authentication) {
//        return authentication.equals(UsernamePasswordAuthenticationToken.class);
//    }
//
//    public void setUserDetailsService(UserDetailsService userDetailsService) {
//        this.userDetailsService = userDetailsService;
//    }
//
//    protected UserDetailsService getUserDetailsService() {
//        return this.userDetailsService;
//    }
//}
