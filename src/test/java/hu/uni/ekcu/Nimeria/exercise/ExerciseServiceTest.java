package hu.uni.ekcu.Nimeria.exercise;

import hu.uni.ekcu.Nimeria.exercise.requests.ExerciseRequest;
import hu.uni.ekcu.Nimeria.solution.SolutionRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class ExerciseServiceTest {

    @Mock
    private ExerciseRepository exerciseRepository;
    @Mock
    private SolutionRepository solutionRepository;
    private ExerciseService underTest;

    @BeforeEach
    void setUp() {
        underTest = new ExerciseService(exerciseRepository, solutionRepository);
    }

    @Test
    void canAddExercise(){
        //given
        ExerciseRequest exercise = new ExerciseRequest(
                "leiras",
                "megoldas",
                "elso Tipp",
                "masodik Tipp",
                "harmadik Tipp",
                5,
                1,
                1,
                2
        );
        //when
        underTest.addExercise(exercise);
        //then
        ArgumentCaptor<Exercise> exerciseRequestArgumentCaptor = ArgumentCaptor.forClass(Exercise.class);

        verify(exerciseRepository).save(exerciseRequestArgumentCaptor.capture());

        Exercise capturedExercise = exerciseRequestArgumentCaptor.getValue();
        assertThat(new ExerciseRequest(
                capturedExercise.getDescription(),
                capturedExercise.getSolution(),
                capturedExercise.getHintOne(),
                capturedExercise.getHintTwo(),
                capturedExercise.getHintThree(),
                capturedExercise.getFullPoints(),
                capturedExercise.getHintOnePoints(),
                capturedExercise.getHintTwoPoints(),
                capturedExercise.getHintThreePoints()
                )).isEqualTo(exercise);
    }


    @Test
    void canGetAllExercisesForUsers() {
        //when
        underTest.getAllExercisesForUsers();
        //then
        verify(exerciseRepository).findAll();
    }
}