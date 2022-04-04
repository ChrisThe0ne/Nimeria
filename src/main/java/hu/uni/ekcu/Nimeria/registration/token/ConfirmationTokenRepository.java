package hu.uni.ekcu.Nimeria.registration.token;

import hu.uni.ekcu.Nimeria.auth.ApplicationUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface ConfirmationTokenRepository extends JpaRepository<ConfirmationToken, Long> {

    Optional<ConfirmationToken> findByToken(String token);

    ConfirmationToken findByApplicationUser(ApplicationUser user);



    @Transactional
    @Modifying
    @Query("UPDATE ConfirmationToken c " +
            "SET c.confirmedAt = ?2 " +
            "WHERE c.token = ?1")
    int updateConfirmedAt(String token,
                          LocalDateTime confirmedAt);


    @Transactional
    @Modifying
    @Query("UPDATE ConfirmationToken c " +
            "SET c.token = ?2, c.createdAt = ?3, c.expiresAt = ?4 " +
            "WHERE c.token = ?1")
    void updateToken(String token, String newToken, LocalDateTime currentTime, LocalDateTime newExpiresAt);

    //UPDATE token expiration date
    @Transactional
    @Modifying
    @Query("UPDATE ConfirmationToken c " +
            "SET c.expiresAt = ?2 " +
            "WHERE c.token = ?1")
    void updateExpiresAt(String token, LocalDateTime expiresAt);
}
