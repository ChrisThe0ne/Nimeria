package hu.uni.ekcu.Nimeria.exercise;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ExerciseRepository extends JpaRepository<Exercise, Long> {

    @Query("" +
            "SELECT CASE WHEN COUNT(s) > 0 THEN " +
            "TRUE ELSE FALSE END " +
            "FROM Exercise s " +
            "WHERE s.description = ?1")
    Boolean descriptionMatches(String description);
}
