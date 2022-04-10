package hu.uni.ekcu.Nimeria.auth;

import hu.uni.ekcu.Nimeria.auth.requests.ProfileDetailsRequest;
import hu.uni.ekcu.Nimeria.auth.requests.UpdateProfileDetailsRequest;
import hu.uni.ekcu.Nimeria.email.EmailSender;
import hu.uni.ekcu.Nimeria.registration.RegistrationService;
import hu.uni.ekcu.Nimeria.registration.token.ConfirmationToken;
import hu.uni.ekcu.Nimeria.registration.token.ConfirmationTokenRepository;
import hu.uni.ekcu.Nimeria.registration.token.ConfirmationTokenService;
import lombok.AllArgsConstructor;
import org.apache.tomcat.jni.Local;
import org.apache.tomcat.jni.User;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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
    private final ConfirmationTokenRepository confirmationTokenRepository;


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return applicationUserRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException(String.format(USER_NOT_FOUND_MESSAGE, username)));
    }

    public String signUpUser(ApplicationUser user){
        boolean emailAlreadyInUse = applicationUserRepository.findByEmail(user.getEmail()).isPresent();

        boolean usernameAlreadyInUse = applicationUserRepository.findByUsername(user.getUsername()).isPresent();



        if (emailAlreadyInUse) {

            if (!applicationUserRepository.getApplicationUserByEmail(user.getEmail()).isEnabled()){ // if user is not enabled (not confirmed email yet)

                if (LocalDateTime.now().isAfter(confirmationTokenRepository.findByApplicationUser(applicationUserRepository.getApplicationUserByEmail(user.getEmail())).getExpiresAt())){ // and user token is expired

                    String newToken = UUID.randomUUID().toString(); //create new token


                    confirmationTokenService.setTokenValueAndExpirationTime(confirmationTokenRepository.findByApplicationUser(applicationUserRepository.getApplicationUserByEmail(user.getEmail())).getToken(), newToken); //update token in the tokens table

                    return newToken; //Just for POSTMAN visualization
                }

                return confirmationTokenRepository.findByApplicationUser(applicationUserRepository.getApplicationUserByEmail(user.getEmail())).getToken(); //Just for POSTMAN visualization

            }
            else throw new IllegalStateException("email already taken");

        }

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


        return token;
    }
    public int enableAppUser(String email) {
        return applicationUserRepository.enableAppUser(email);
    }

    public String deleteAnyUser(Long id) {
        boolean userExistsWithThatId = applicationUserRepository.findById(id).isPresent();

        if (!userExistsWithThatId)
            throw new IllegalStateException("No user exists with id: " + id);

        confirmationTokenRepository.DeleteAllRowsByUserId(id);

        //TODO delete completed exercises by this id

        applicationUserRepository.deleteById(id);



        return "User with id: " + id + " deleted successfully";
    }

    public ProfileDetailsRequest returnProfileDetails(String username){


        boolean usernameExists = applicationUserRepository.findByUsername(username).isPresent();

        if (!usernameExists)
            throw new UsernameNotFoundException(String.format(USER_NOT_FOUND_MESSAGE, username));

        ApplicationUser user = applicationUserRepository.getApplicationUserByUsername(username);

        return new ProfileDetailsRequest(user.getUsername(),
                user.getEmail(),
                user.getFirstName(),
                user.getLastName(),
                user.getCountry(),
                user.getScore());
    }

    public String updateProfileDetails(UpdateProfileDetailsRequest request){

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentPrincipalName = authentication.getName();

        if (!request.getUsername().equals(currentPrincipalName))
            throw new IllegalStateException("You can only modify your own profile!" + currentPrincipalName + " " +  request.getUsername());

        ApplicationUser user = applicationUserRepository.getApplicationUserByUsername(request.getUsername());

        if (applicationUserRepository.findByEmail(request.getEmail()).isPresent() && !request.getEmail().equals(user.getEmail()))
            throw new IllegalStateException("Email already in use");

        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setEmail(request.getEmail());
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setCountry(request.getCountry());

        applicationUserRepository.save(user);

        return "Profile details modified successfully!";
    }


}
