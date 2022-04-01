package hu.uni.ekcu.Nimeria.registration;

import hu.uni.ekcu.Nimeria.auth.AppUserRole;
import hu.uni.ekcu.Nimeria.auth.ApplicationUser;
import hu.uni.ekcu.Nimeria.auth.ApplicationUserService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class RegistrationService {

    private final ApplicationUserService applicationUserService;
    private final EmailValidator emailValidator;

    public String register(RegistrationRequest request) {
        boolean isEmailValid = emailValidator.test(request.getEmail());

        if (!isEmailValid)
            throw new IllegalStateException("email not valid");
        return applicationUserService.signUpUser(
                new ApplicationUser(
                        request.getUsername(),
                        request.getPassword(),
                        request.getEmail(),
                        request.getFirstName(),
                        request.getLastName(),
                        request.getCountry(),
                        1L,
                        AppUserRole.USER
                )
        );
    }
}
