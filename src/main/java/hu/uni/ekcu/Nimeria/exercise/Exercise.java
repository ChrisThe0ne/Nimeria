package hu.uni.ekcu.Nimeria.exercise;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;

@ToString
@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
@Entity
@Table
public class Exercise {
    @Id
    @SequenceGenerator(
            name = "exercise_sequence",
            sequenceName = "exercise_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            generator = "exercise_sequence",
            strategy = GenerationType.SEQUENCE)
    private Long id;

    @NotBlank
    @Column(nullable = false)
    private String description;

    @NotBlank
    @Column(nullable = false)
    private String solution;

    private String hintOne;
    private String hintTwo;
    private String hintThree;
    private Integer fullPoints;
    private Integer hintOnePoints;
    private Integer hintTwoPoints;
    private Integer hintThreePoints;

    public Exercise(String description, String solution, String hintOne, String hintTwo, String hintThree, Integer fullPoints, Integer hintOnePoints, Integer hintTwoPoints, Integer hintThreePoints) {
        this.description = description;
        this.solution = solution;
        this.hintOne = hintOne;
        this.hintTwo = hintTwo;
        this.hintThree = hintThree;
        this.fullPoints = fullPoints;
        this.hintOnePoints = hintOnePoints;
        this.hintTwoPoints = hintTwoPoints;
        this.hintThreePoints = hintThreePoints;
    }
}
