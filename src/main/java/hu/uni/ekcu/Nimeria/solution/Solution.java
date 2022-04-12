package hu.uni.ekcu.Nimeria.solution;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.time.LocalDateTime;

@ToString
@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table
public class Solution {

    @EmbeddedId
    private SolutionPk solutionPK;

    @Column(nullable = false)
    private String solution;

    private LocalDateTime submittedAt;

    private Long points;
}
