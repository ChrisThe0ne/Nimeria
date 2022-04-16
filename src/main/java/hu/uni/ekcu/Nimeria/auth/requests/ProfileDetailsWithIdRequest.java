package hu.uni.ekcu.Nimeria.auth.requests;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@EqualsAndHashCode
@ToString
public class ProfileDetailsWithIdRequest extends ProfileDetailsRequest {

    private final Long id;

    public ProfileDetailsWithIdRequest(String username,
                                       String email,
                                       String firstName,
                                       String lastName,
                                       String country,
                                       Long score,
                                       Long id) {
        super(username, email, firstName, lastName, country, score);
        this.id = id;
    }
}
