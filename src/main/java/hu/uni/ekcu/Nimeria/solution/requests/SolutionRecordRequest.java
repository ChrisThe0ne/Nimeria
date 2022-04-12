package hu.uni.ekcu.Nimeria.solution.requests;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@AllArgsConstructor
@EqualsAndHashCode
@ToString
public class SolutionRecordRequest {

    private final String solution;

    private final Long exerciseId;

    private final Long minusPoints;
}
