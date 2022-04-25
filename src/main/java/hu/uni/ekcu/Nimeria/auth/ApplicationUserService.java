package hu.uni.ekcu.Nimeria.auth;

import hu.uni.ekcu.Nimeria.auth.requests.ProfileDetailsRequest;
import hu.uni.ekcu.Nimeria.auth.requests.ProfileDetailsWithIdRequest;
import hu.uni.ekcu.Nimeria.auth.requests.UpdateProfileDetailsRequest;
import hu.uni.ekcu.Nimeria.exercise.exception.BadRequestException;
import hu.uni.ekcu.Nimeria.registration.token.ConfirmationToken;
import hu.uni.ekcu.Nimeria.registration.token.ConfirmationTokenRepository;
import hu.uni.ekcu.Nimeria.registration.token.ConfirmationTokenService;
import hu.uni.ekcu.Nimeria.solution.SolutionRepository;
import hu.uni.ekcu.Nimeria.solution.requests.SolutionGetRequest;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
public class ApplicationUserService implements UserDetailsService {

    private final static String USER_NOT_FOUND_MESSAGE = "username %s not found";
    private final ApplicationUserRepository applicationUserRepository;
    private final PasswordEncoder passwordEncoder;
    private final ConfirmationTokenService confirmationTokenService;
    private final ConfirmationTokenRepository confirmationTokenRepository;
    private final SolutionRepository solutionRepository;


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
            else throw new BadRequestException("email already taken");

        }

        if (usernameAlreadyInUse)
            throw new BadRequestException("username already in use");

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
            throw new BadRequestException("No user exists with id: " + id);

        confirmationTokenRepository.DeleteAllRowsByUserId(id);

        solutionRepository.deleteAllBySolutionPK_User(applicationUserRepository.getById(id));

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
            throw new BadRequestException("You can only modify your own profile!" + currentPrincipalName + " " +  request.getUsername());

        ApplicationUser user = applicationUserRepository.getApplicationUserByUsername(request.getUsername());

        if (applicationUserRepository.findByEmail(request.getEmail()).isPresent() && !request.getEmail().equals(user.getEmail()))
            throw new BadRequestException("Email already in use");

        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setEmail(request.getEmail());
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setCountry(request.getCountry());

        applicationUserRepository.save(user);

        return "Profile details modified successfully!";
    }

    @Transactional
    public void deleteProfile(){

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentPrincipalName = authentication.getName();

        if (applicationUserRepository.findByUsername(currentPrincipalName).isEmpty())
            throw new UsernameNotFoundException(String.format(USER_NOT_FOUND_MESSAGE, currentPrincipalName));

        confirmationTokenRepository.DeleteAllRowsByUserId(applicationUserRepository.getApplicationUserByUsername(currentPrincipalName).getId());

        ApplicationUser user = applicationUserRepository.getApplicationUserByUsername(currentPrincipalName);

        solutionRepository.deleteAllBySolutionPK_User(user);


        applicationUserRepository.deleteById(user.getId());
    }

    public String updateAnyUser(UpdateProfileDetailsRequest request, Long id){

        ApplicationUser user = applicationUserRepository.getById(id);

        if (applicationUserRepository.findByUsername(request.getUsername()).isPresent() && !user.getUsername().equals(request.getUsername()) )
            throw new BadRequestException("Username already exists!");

        user.setUsername(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword()));

        if (applicationUserRepository.findByEmail(request.getEmail()).isPresent() && !user.getEmail().equals(request.getEmail()) )
            throw new BadRequestException("Email already in use!");
        user.setEmail(request.getEmail());
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setCountry(request.getCountry());

        applicationUserRepository.save(user);

        return "Profile details modified successfully by admin!";
    }

    public List<ProfileDetailsWithIdRequest> getAllUsers(){
        List<ApplicationUser> applicationUsers = applicationUserRepository.findAll();

        List<ProfileDetailsWithIdRequest> profileDetailsWithIdRequests = new ArrayList<>();

        for (int i = 0; i < applicationUsers.stream().count(); i++){
            profileDetailsWithIdRequests.add(new ProfileDetailsWithIdRequest(
                    applicationUsers.get(i).getUsername(),
                    applicationUsers.get(i).getEmail(),
                    applicationUsers.get(i).getFirstName(),
                    applicationUsers.get(i).getLastName(),
                    applicationUsers.get(i).getCountry(),
                    applicationUsers.get(i).getScore(),
                    applicationUsers.get(i).getId()
            ));
        }

        return profileDetailsWithIdRequests;

    }

    public void addPointsToProfile(Long points){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentPrincipalName = authentication.getName();

        ApplicationUser user = applicationUserRepository.getApplicationUserByUsername(currentPrincipalName);
        user.setScore(user.getScore() + points);
        applicationUserRepository.save(user);
    }
}
