package backend.rest.security;

import backend.BackendApplication;
import backend.domain.Role;
import backend.domain.User;
import backend.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import static org.springframework.security.core.authority.AuthorityUtils.commaSeparatedStringToAuthorityList;

@Service
public class AppUserDetailsService implements UserDetailsService {

    @Value("${tempsync.admin.username}")
    private String adminUsername;

    @Value("${tempsync.admin.password}")
    private String adminPassword;

    @Autowired
    private UserService userService;

    private final Logger logger = LoggerFactory.getLogger(AppUserDetailsService.class);

    @Autowired
    private PasswordEncoder encoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        if(username.equals(adminUsername)){
            logger.debug("Admin login");

            return new UserAuthWrap(new User("user@email.com", adminUsername, encoder.encode(adminPassword), new Role("ADMIN")));
        }

        if(username.matches(BackendApplication.EMAIL_FORMAT)){
            User requestedUser = userService.findUserByEmail(username).orElseThrow(() ->
                    new UsernameNotFoundException("Username or password mismatch.")
            );

            return new UserAuthWrap(requestedUser);
        }

        if(username.matches(BackendApplication.USERNAME_FORMAT)){
            User requestedUser = userService.findUserByUsername(username).orElseThrow(() ->
                    new UsernameNotFoundException("Username or password mismatch.")
            );

            return new UserAuthWrap(requestedUser);
        }

        throw new UsernameNotFoundException("Username or password mismatch.");
    }
}
