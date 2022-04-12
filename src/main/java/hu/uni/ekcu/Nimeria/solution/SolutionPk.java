package hu.uni.ekcu.Nimeria.solution;

import hu.uni.ekcu.Nimeria.auth.ApplicationUser;
import hu.uni.ekcu.Nimeria.exercise.Exercise;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Embeddable;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.io.Serializable;

@AllArgsConstructor
@NoArgsConstructor
@Embeddable
@Getter
@Setter
public class SolutionPk implements Serializable {

    @ManyToOne
    @JoinColumn(name = "user_id")
    private ApplicationUser user;

    @ManyToOne
    @JoinColumn(name = "exercise_id")
    private Exercise exercise;
}
