package hu.uni.ekcu.Nimeria.exercise;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(path = "api/v1/exercises")
@AllArgsConstructor
public class ExerciseController {

    private final ExerciseService exerciseService;

    @GetMapping
    public List<Exercise> getAllExercises(){
        return exerciseService.getAllExercises();
    }

    @PostMapping
    public void addExercise(@Valid @RequestBody Exercise exercise){
        exerciseService.addExercise(exercise);
    }

    @DeleteMapping(path = "{exerciseId}")
    public void deleteExercise(@PathVariable("exerciseId") Long exerciseId){
        exerciseService.deleteExercise(exerciseId);
    }
}
