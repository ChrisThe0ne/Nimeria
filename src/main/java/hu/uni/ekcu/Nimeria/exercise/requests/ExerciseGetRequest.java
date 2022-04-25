package hu.uni.ekcu.Nimeria.exercise.requests;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@AllArgsConstructor
@EqualsAndHashCode
@ToString
public class ExerciseGetRequest {

    private final Long id;
    private final String description;
    private final String hintOne;
    private final String hintTwo;
    private final String hintThree;
    private final Integer fullPoints;
    private final Integer hintOnePoints;
    private final Integer hintTwoPoints;
    private final Integer hintThreePoints;

}
