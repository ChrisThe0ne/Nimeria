package hu.uni.ekcu.Nimeria.solution;

import hu.uni.ekcu.Nimeria.auth.ApplicationUser;
import hu.uni.ekcu.Nimeria.exercise.Exercise;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SolutionRepository extends JpaRepository<Solution, SolutionPk> {
    List<Solution> getSolutionsBySolutionPK_User(ApplicationUser user);

    void deleteAllBySolutionPK_Exercise(Exercise exercise);

    void deleteAllBySolutionPK_User(ApplicationUser user);
}
