package hu.uni.ekcu.Nimeria.exercise;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class ExerciseRepositoryTest {

    @Autowired
    private ExerciseRepository underTest;

    @Test
    void itShouldCheckIfDescriptionMatches() {
        //given
        Exercise exercise = new Exercise(
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
        underTest.save(exercise);
        //when
        boolean matches = underTest.descriptionMatches("leiras");

        //then
        assertThat(matches).isTrue();
    }
}