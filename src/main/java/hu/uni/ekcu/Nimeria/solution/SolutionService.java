package hu.uni.ekcu.Nimeria.solution;

import hu.uni.ekcu.Nimeria.auth.ApplicationUser;
import hu.uni.ekcu.Nimeria.auth.ApplicationUserRepository;
import hu.uni.ekcu.Nimeria.auth.ApplicationUserService;
import hu.uni.ekcu.Nimeria.exercise.ExerciseRepository;
import hu.uni.ekcu.Nimeria.exercise.exception.BadRequestException;
import hu.uni.ekcu.Nimeria.solution.requests.SolutionGetRequest;
import hu.uni.ekcu.Nimeria.solution.requests.SolutionRecordRequest;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@Service
public class SolutionService {

    private final SolutionRepository solutionRepository;
    private final ApplicationUserRepository applicationUserRepository;
    private final ApplicationUserService applicationUserService;
    private final ExerciseRepository exerciseRepository;

    public String recordSolution(SolutionRecordRequest request){

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentPrincipalName = authentication.getName();

        SolutionPk primaryKey = new SolutionPk(
                applicationUserRepository.getApplicationUserByUsername(currentPrincipalName),
                exerciseRepository.getById(request.getExerciseId()));

        if (solutionRepository.findById(primaryKey).isPresent())
            throw new BadRequestException("User already solved this exercise!");

        if (!exerciseRepository.getById(request.getExerciseId()).getSolution().equals(request.getSolution()))
            throw new BadRequestException("The solution is not correct!");

        Long pointsAwarded = exerciseRepository.getById(request.getExerciseId()).getFullPoints()- request.getMinusPoints();

        if (pointsAwarded < 1)
            throw new BadRequestException("Minus points for tips configured incorrectly");

        applicationUserService.addPointsToProfile(pointsAwarded);

        solutionRepository.save(
                new Solution(
                        primaryKey,
                        request.getSolution(),
                        LocalDateTime.now(),
                        pointsAwarded));

        return "Solution Saved";
    }

    public List<SolutionGetRequest> getSolutionsByUser(){

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentPrincipalName = authentication.getName();

        ApplicationUser user = applicationUserRepository.getApplicationUserByUsername(currentPrincipalName);

        List<Solution> solutions = solutionRepository.getSolutionsBySolutionPK_User(user);

        List<SolutionGetRequest> solutionGetRequests = new ArrayList<>();

        if(solutions.isEmpty())
            throw new BadRequestException("No solutions submitted by this user");

        for (int i = 0; i < solutions.stream().count(); i++){
            solutionGetRequests.add(new SolutionGetRequest(
                    solutions.get(i).getSolutionPK().getExercise().getId(),
                    solutions.get(i).getSolution(),
                    solutions.get(i).getSubmittedAt(),
                    solutions.get(i).getPoints()
            ));

        }

        return solutionGetRequests;
    }
}
