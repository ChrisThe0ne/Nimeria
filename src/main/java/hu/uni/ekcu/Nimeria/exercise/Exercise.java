package hu.uni.ekcu.Nimeria.exercise;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;

@ToString
@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
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

}
