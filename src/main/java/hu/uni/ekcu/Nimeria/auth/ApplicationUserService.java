package hu.uni.ekcu.Nimeria.auth;

import hu.uni.ekcu.Nimeria.registration.token.ConfirmationToken;
import hu.uni.ekcu.Nimeria.registration.token.ConfirmationTokenRepository;
import hu.uni.ekcu.Nimeria.registration.token.ConfirmationTokenService;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@AllArgsConstructor
public class ApplicationUserService implements UserDetailsService {

    private final static String USER_NOT_FOUND_MESSAGE = "username %s not found"; // TODO: create exception
    private final ApplicationUserRepository applicationUserRepository;
    private final PasswordEncoder passwordEncoder;
    private final ConfirmationTokenService confirmationTokenService;


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return applicationUserRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException(String.format(USER_NOT_FOUND_MESSAGE, username)));
    }

    public String signUpUser(ApplicationUser user){
        boolean emailAlreadyInUse = applicationUserRepository.findByEmail(user.getEmail()).isPresent();

        boolean usernameAlreadyInUse = applicationUserRepository.findByUsername(user.getUsername()).isPresent();

        if (emailAlreadyInUse)
            throw new IllegalStateException("email already taken");
        if (usernameAlreadyInUse)
            throw new IllegalStateException("username already in use");

        String encodedPassword = passwordEncoder.encode(user.getPassword());

        user.setPassword(encodedPassword);

        applicationUserRepository.save(user);

        String token = UUID.randomUUID().toString();

        ConfirmationToken confirmationToken = new ConfirmationToken(
                token,
                LocalDateTime.now(),
                LocalDateTime.now().plusMinutes(15),
                user
        );

        confirmationTokenService.saveConfirmationToken(confirmationToken);

        //TODO: send email

        return token;
    }
    public int enableAppUser(String email) {
        return applicationUserRepository.enableAppUser(email);
    }
}
