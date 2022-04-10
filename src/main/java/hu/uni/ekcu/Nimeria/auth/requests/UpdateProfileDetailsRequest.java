package hu.uni.ekcu.Nimeria.auth.requests;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@AllArgsConstructor
@EqualsAndHashCode
@ToString
public class UpdateProfileDetailsRequest {

    private final String username;
    private final String email;
    private final String password;
    private final String firstName;
    private final String lastName;
    private final String country;
}
