package hu.uni.ekcu.Nimeria.solution;

import hu.uni.ekcu.Nimeria.solution.requests.SolutionGetRequest;
import hu.uni.ekcu.Nimeria.solution.requests.SolutionRecordRequest;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "api/v1/solution")
@AllArgsConstructor
public class SolutionController {

    private SolutionService solutionService;

    @PostMapping
    @PreAuthorize("hasAnyAuthority('USER', 'ADMIN')")
    public String recordSolution(@RequestBody SolutionRecordRequest request){
        return solutionService.recordSolution(request);
    }

    @GetMapping
    @PreAuthorize("hasAnyAuthority('USER', 'ADMIN')")
    public List<SolutionGetRequest> getSolutionsByUser(){
        return solutionService.getSolutionsByUser();
    }

}
