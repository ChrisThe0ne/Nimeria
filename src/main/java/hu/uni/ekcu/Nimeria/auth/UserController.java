package hu.uni.ekcu.Nimeria.auth;

import hu.uni.ekcu.Nimeria.auth.requests.ProfileDetailsRequest;
import hu.uni.ekcu.Nimeria.auth.requests.UpdateProfileDetailsRequest;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "api/v1/user")
@AllArgsConstructor
public class UserController {

    private ApplicationUserService applicationUserService;

    @DeleteMapping(path = "deleteAnyUser/{userid}")
    public String deleteAnyUser(@PathVariable("userid") Long id){
        return applicationUserService.deleteAnyUser(id);
    }

    @GetMapping(path = "{username}")
    public ProfileDetailsRequest returnProfileDetails(@PathVariable("username") String username){
        return applicationUserService.returnProfileDetails(username);
    }

    @PutMapping(path = "modifyProfile")
    public String updateProfileDetails(@RequestBody UpdateProfileDetailsRequest request){
        return applicationUserService.updateProfileDetails(request);
    }


}
