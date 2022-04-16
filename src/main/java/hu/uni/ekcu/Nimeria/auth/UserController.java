package hu.uni.ekcu.Nimeria.auth;

import hu.uni.ekcu.Nimeria.auth.requests.ProfileDetailsRequest;
import hu.uni.ekcu.Nimeria.auth.requests.ProfileDetailsWithIdRequest;
import hu.uni.ekcu.Nimeria.auth.requests.UpdateProfileDetailsRequest;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "api/v1/user")
@AllArgsConstructor
public class UserController {

    private ApplicationUserService applicationUserService;

    @GetMapping(path = "{username}")
    @PreAuthorize("hasAnyAuthority('USER', 'ADMIN')")
    public ProfileDetailsRequest returnProfileDetails(@PathVariable("username") String username){
        return applicationUserService.returnProfileDetails(username);
    }

    @PutMapping(path = "modifyProfile")
    @PreAuthorize("hasAnyAuthority('USER', 'ADMIN')")
    public String updateProfileDetails(@RequestBody UpdateProfileDetailsRequest request){
        return applicationUserService.updateProfileDetails(request);
    }

    @DeleteMapping(path = "deleteProfile")
    @PreAuthorize("hasAnyAuthority('USER', 'ADMIN')")
    public void deleteProfile(){
        applicationUserService.deleteProfile();
    }

    @DeleteMapping(path = "deleteAnyUser/{userid}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public String deleteAnyUser(@PathVariable("userid") Long id){
        return applicationUserService.deleteAnyUser(id);
    }

    @PutMapping(path = "updateAnyUser/{userid}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public String updateAnyUser(@RequestBody UpdateProfileDetailsRequest request, @PathVariable("userid") Long id){
        return applicationUserService.updateAnyUser(request, id);
    }

    @GetMapping(path = "getAllUsers")
    @PreAuthorize("hasAuthority('ADMIN')")
    public List<ProfileDetailsWithIdRequest> getAllUsers(){
        return applicationUserService.getAllUsers();
    }
}
