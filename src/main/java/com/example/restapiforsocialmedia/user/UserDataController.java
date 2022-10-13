package com.example.restapiforsocialmedia.user;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/users")
public class UserDataController {
    private final UserDataService userDataService;

    public UserDataController(UserDataService userDataService) {
        this.userDataService = userDataService;
    }

    @PostMapping
    public void createPerson(@RequestBody UserDataRegisterDTO userDataRegisterDTO) {
        userDataService.createPerson(userDataRegisterDTO);
    }

    @GetMapping
    public UserDataPublicDTO getPerson(Authentication authentication) {
        if (authentication == null) {
            return null;
        }
        return userDataService.getPersonByUsername(authentication.getName());
    }

    @GetMapping("/followers/{page}")
    public List<UserDataBasicPublicDTO> getFollowers(Authentication authentication, @PathVariable int page) {
        return userDataService.getFollowers(authentication.getName(), page);
    }

    @GetMapping("/following/{page}")
    public List<UserDataBasicPublicDTO> getFollowing(Authentication authentication, @PathVariable int page) {
        return userDataService.getFollowing(authentication.getName(), page);
    }
    @PatchMapping("/follow/{username}")
    public void followPerson(Authentication authentication, @PathVariable String username) {
        userDataService.followPerson(authentication.getName(), username);
    }

    @GetMapping("/{username}")
    public UserDataPublicDTO getPersonByUsername(@PathVariable String username) {
        return userDataService.getPersonByUsername(username);
    }
    @GetMapping("/search/{username}/{page}")
    public List<UserDataBasicPublicDTO> searchPersonByUsernameContaining(@PathVariable String username, @PathVariable int page) {
        return userDataService.searchPersonByUsernameContaining(username, page);
    }

    @PutMapping
    public void updatePerson(Authentication authentication, @RequestBody UserDataPublicDTO userDataPublicDTO) {
        userDataService.updatePerson(authentication.getName(), userDataPublicDTO);
    }

    @DeleteMapping
    public void deletePerson(Authentication authentication) {
        userDataService.deletePerson(authentication.getName());
    }

    @PatchMapping("/upload-profile-picture")
    public void uploadProfilePicture(Authentication authentication, @RequestBody UserDataBasicPublicDTO base64) {
        userDataService.uploadProfilePicture(authentication.getName(), base64);
    }

    @PatchMapping("/i-am-teapot")
    @ResponseStatus(HttpStatus.I_AM_A_TEAPOT)
    public void iAmTeapot() {
    }
}

