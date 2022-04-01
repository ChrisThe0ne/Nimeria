package hu.uni.ekcu.Nimeria.auth;

import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class ApplicationUserService implements UserDetailsService {

    private final static String USER_NOT_FOUND_MESSAGE = "username %s not found"; // TODO: create exception
    private final ApplicationUserRepository applicationUserRepository;
    private final PasswordEncoder passwordEncoder;


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return applicationUserRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException(String.format(USER_NOT_FOUND_MESSAGE, username)));
    }

    public String signUpUser(ApplicationUser user){
        boolean userAlreadyExists = applicationUserRepository.findByUsername(user.getEmail())
                .isPresent();
        if (userAlreadyExists)
            throw new IllegalStateException("Email already taken");

        String encodedPassword = passwordEncoder.encode(user.getPassword());

        user.setPassword(encodedPassword);

        applicationUserRepository.save(user);

        //TODO send confirmation token
        return "it works";
    }
}
