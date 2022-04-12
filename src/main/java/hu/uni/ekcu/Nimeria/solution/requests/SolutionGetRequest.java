package hu.uni.ekcu.Nimeria.solution.requests;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@EqualsAndHashCode
@ToString
public class SolutionGetRequest {

    private final Long exerciseId;

    private final String solution;

    private final LocalDateTime submittedAt;

    private final Long points;

}
