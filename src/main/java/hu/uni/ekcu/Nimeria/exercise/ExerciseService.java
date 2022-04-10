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

    public String addExercise(ExerciseRequest request){
        Boolean descriptionMatches = exerciseRepository.descriptionMatches(request.getDescription());
        if (descriptionMatches)
            throw new BadRequestException("Exercise already exists with that description!");

        Exercise exercise = new Exercise(request.getDescription(),
                request.getSolution(),
                request.getHintOne(),
                request.getHintTwo(),
                request.getHintThree(),
                request.getFullPoints(),
                request.getHintOnePoints(),
                request.getHintTwoPoints(),
                request.getHintThreePoints());

        exerciseRepository.save(exercise);

        return "Exercise saved";
    }

    public String deleteExercise(Long exerciseId){
        if (!exerciseRepository.existsById(exerciseId))
            throw new ExerciseNotFoundException("Exercise with id: " + exerciseId + " does not exists");
        exerciseRepository.deleteById(exerciseId);

        return "Exercise with id: " + exerciseId + " successfully deleted!";
    }

    public String updateExercise(Exercise exercise){
        Boolean exerciseExists = exerciseRepository.findById(exercise.getId()).isPresent();
        if (!exerciseExists)
            throw new BadRequestException("Exercise does not exists with that Id!");

        exerciseRepository.save(exercise);

        return "Exercise updated";
    }
}
