package de.schneider.robin.backend.across.security;

import de.schneider.robin.backend.db.model.SecurityUser;
import de.schneider.robin.backend.db.repository.SecurityUserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    // No more WebSecurityConfigurerAdapter, because it is deprecated

    private final AuthenticationEntryPoint authEntryPoint;
    private final AccessDeniedHandler accessDeniedHandler;

    public SecurityConfig(
            CustomAuthenticationEntryPoint authEntryPoint,
            AccessDeniedHandler accessDeniedHandler
    ) {
        this.authEntryPoint = authEntryPoint;
        this.accessDeniedHandler = accessDeniedHandler;
    }

    @Bean
    public SecurityFilterChain filterChain(
            HttpSecurity http
    ) throws Exception {
        return http
                .csrf().disable() // allow POST requests
                .headers(headers -> headers.frameOptions().disable()) // allow h2-console
                .cors().and() // allow cross-origin requests (e.g. from frontend)

                // Authentication via UserDetailsService and SecurityUserRepository (load user from db)
                .httpBasic(withDefaults()) // required username and password in request header
                .exceptionHandling()
                .authenticationEntryPoint(authEntryPoint) // custom 401 handling
                .accessDeniedHandler(accessDeniedHandler).and() // custom 403 handling

                // Authorization via matchers (path and optional http method)
                // In addition we can create checks on method level (e.g. @PreAuthorize, e.g. for graphql)
                .authorizeRequests(authorize -> authorize

                        // order of statements is important, from specific to general !!!
                        // use mvcMatchers, not antMatchers (don't secure trailing slash, https://youtu.be/4GJzxZnCdBU)

                        // only authenticated users can create a new normal (not minimal) person
                        // doesn't matter if the user has the role BASIC, SUPER or ADMIN
                        .mvcMatchers(HttpMethod.POST, "/protected/person").authenticated()
                        .mvcMatchers(HttpMethod.POST, "/protected/person/valid-body").authenticated()

                        // to create a new random person, you need to be authenticated and have higher role than BASIC
                        .mvcMatchers(HttpMethod.POST, "/protected/person/random").hasAnyRole("SUPER", "ADMIN")

                        // to delete all people in the system, you need to be an ADMIN // or via .hasAuthority("ROLE_ADMIN")
                        .mvcMatchers(HttpMethod.DELETE, "/protected/people/delete-all").hasRole("ADMIN")

                        // for all protected subpaths that come in the future, restrict for now to ADMIN
                        // in that way you don't forget to establish a check (independent of the http method)
                        .mvcMatchers("/protected/**").hasRole("ADMIN") // ** means all possible subpaths

                        .anyRequest().permitAll() // enable in general all other endpoints, including swagger-ui, etc.

                )
                .build();
    }

    @Bean
    CommandLineRunner commandLineRunner(
            SecurityUserRepository userRepo
    ) {
        // spring uses the prefix "ROLE_" for roles by default
        // https://stackoverflow.com/questions/33205236/spring-security-added-prefix-role-to-all-roles-name
        return args -> {
            userRepo.save(new SecurityUser("basic", encoder().encode("basic"), "ROLE_BASIC"));
            userRepo.save(new SecurityUser("super", encoder().encode("super"), "ROLE_BASIC,ROLE_SUPER"));
            userRepo.save(new SecurityUser("admin", encoder().encode("admin"), "ROLE_BASIC,ROLE_SUPER,ROLE_ADMIN"));
        };
    }

    @Bean
    public PasswordEncoder encoder(){
        return new BCryptPasswordEncoder();
    }

//    // Alternative: in-memory user details
//    // Here you don't need the "ROLE_" prefix
//
//    @Bean
//    public UserDetailsService userDetailsService() {
//        UserDetails basicUser = User.builder()
//                .username("basic")
//                .password(encoder().encode("basic"))
//                .roles("BASIC")
//                .build();
//        UserDetails superUser = User.builder()
//                .username("super")
//                .password(encoder().encode("super"))
//                .roles("BASIC", "SUPER")
//                .build();
//        UserDetails adminUser = User.builder()
//                .username("admin")
//                .password(encoder().encode("admin"))
//                .roles("BASIC", "SUPER", "ADMIN")
//                .build();
//        return new InMemoryUserDetailsManager(basicUser, superUser, adminUser);
//    }

}