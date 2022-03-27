package hu.uni.ekcu.Nimeria.exercise;

import hu.uni.ekcu.Nimeria.exercise.exception.BadRequestException;
import hu.uni.ekcu.Nimeria.exercise.exception.ExerciseNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@AllArgsConstructor
@Service
public class ExerciseService {

    private final ExerciseRepository exerciseRepository;

    public List<Exercise> getAllExercises(){
        return exerciseRepository.findAll();
    }

    public void addExercise(Exercise exercise){
        Boolean descriptionMatches = exerciseRepository.descriptionMatches(exercise.getDescription());
        if (descriptionMatches)
            throw new BadRequestException("Exercise already exists with that description!");
        exerciseRepository.save(exercise);
    }

    public void deleteExercise(Long exerciseId){
        if (!exerciseRepository.existsById(exerciseId))
            throw new ExerciseNotFoundException("Exercise with id: " + exerciseId + " does not exists");
        exerciseRepository.deleteById(exerciseId);
    }
}
