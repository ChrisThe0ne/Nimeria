package hu.uni.ekcu.Nimeria.exercise;

import hu.uni.ekcu.Nimeria.exercise.requests.ExerciseGetRequest;
import hu.uni.ekcu.Nimeria.exercise.requests.ExerciseRequest;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "api/v1/exercise")
@AllArgsConstructor
public class ExerciseController {

    private final ExerciseService exerciseService;

    @GetMapping(path = {"{exerciseId}"})
    @PreAuthorize("hasAnyAuthority('USER', 'ADMIN')")
    public ExerciseGetRequest getExerciseById(@PathVariable("exerciseId") Long exerciseId){
        return exerciseService.getExerciseById(exerciseId);
    }

    @GetMapping
    @PreAuthorize("hasAnyAuthority('USER', 'ADMIN')")
    public List<Exercise> getAllExercises(){
        return exerciseService.getAllExercises();
    }

    @PostMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    public String addExercise(@RequestBody ExerciseRequest request){
        return exerciseService.addExercise(request);
    }

    @DeleteMapping(path = "{exerciseId}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public String deleteExercise(@PathVariable("exerciseId") Long exerciseId){
        return exerciseService.deleteExercise(exerciseId);
    }

    @PutMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    public String updateExercise(@RequestBody Exercise exercise){
        return exerciseService.updateExercise(exercise);
    }
}
