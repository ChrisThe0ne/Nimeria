package hu.uni.ekcu.Nimeria.auth;

import hu.uni.ekcu.Nimeria.auth.requests.UpdateProfileDetailsRequest;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "api/v1/userManagement")
@AllArgsConstructor
public class UserManagementController {

    private ApplicationUserService applicationUserService;

    @DeleteMapping(path = "deleteAnyUser/{userid}")
    public String deleteAnyUser(@PathVariable("userid") Long id){
        return applicationUserService.deleteAnyUser(id);
    }

    @PutMapping(path = "updateAnyUser/{userid}")
    public String updateAnyUser(@RequestBody UpdateProfileDetailsRequest request, @PathVariable("userid") Long id){
        return applicationUserService.updateAnyUser(request, id);
    }

    @GetMapping(path = "getAllUsers")
    public List<ApplicationUser> getAllUsers(){
        return applicationUserService.getAllUsers();
    }
}
