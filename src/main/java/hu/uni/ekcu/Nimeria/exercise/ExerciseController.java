package hu.uni.ekcu.Nimeria.exercise;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

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
    public String addExercise(@RequestBody ExerciseRequest request){
        return exerciseService.addExercise(request);
    }

    @DeleteMapping(path = "{exerciseId}")
    public String deleteExercise(@PathVariable("exerciseId") Long exerciseId){
        return exerciseService.deleteExercise(exerciseId);
    }

    @PutMapping
    public String updateExercise(@RequestBody Exercise exercise){
        return exerciseService.updateExercise(exercise);
    }
}
