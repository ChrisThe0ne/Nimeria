package hu.uni.ekcu.Nimeria.registration;

import hu.uni.ekcu.Nimeria.auth.AppUserRole;
import hu.uni.ekcu.Nimeria.auth.ApplicationUser;
import hu.uni.ekcu.Nimeria.auth.ApplicationUserService;
import hu.uni.ekcu.Nimeria.registration.token.ConfirmationToken;
import hu.uni.ekcu.Nimeria.registration.token.ConfirmationTokenService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@AllArgsConstructor
public class RegistrationService {

    private final ApplicationUserService applicationUserService;
    private final EmailValidator emailValidator;
    private final ConfirmationTokenService confirmationTokenService;

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

    @Transactional
    public String confirmToken(String token) {
        ConfirmationToken confirmationToken = confirmationTokenService
                .getToken(token)
                .orElseThrow(() ->
                        new IllegalStateException("token not found"));

        if (confirmationToken.getConfirmedAt() != null) {
            throw new IllegalStateException("email already confirmed");
        }

        LocalDateTime expiredAt = confirmationToken.getExpiresAt();

        if (expiredAt.isBefore(LocalDateTime.now())) {
            throw new IllegalStateException("token expired");
        }

        confirmationTokenService.setConfirmedAt(token);
        applicationUserService.enableAppUser(
                confirmationToken.getApplicationUser().getEmail());
        return "confirmed";
    }
}
