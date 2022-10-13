package com.example.restapiforsocialmedia.admin;

import com.example.restapiforsocialmedia.user.UserDataService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/v1/admin/user")
public class AdminPersonController {
    private final UserDataService userDataService;

    public AdminPersonController(UserDataService userDataService) {
        this.userDataService = userDataService;
    }
    @PostMapping("/ban/{username}")
    public void banPerson(@PathVariable String username) {
        userDataService.banPerson(username);
    }
    @PostMapping("/unban/{username}")
    public void unbanPerson(@PathVariable String username) {
        userDataService.unbanPerson(username);
    }
    @DeleteMapping("/delete/{username}")
    public void deletePerson(@PathVariable String username) {
        userDataService.deletePerson(username);
    }
    @PostMapping("/undelete/{username}")
    public void undeletePerson(@PathVariable String username) {
        userDataService.undeletePerson(username);
    }
    @PatchMapping("/make-admin/{username}")
    public void makeAdmin(@PathVariable String username) {
        userDataService.makeAdmin(username);
    }
    @PatchMapping("/make-user/{username}")
    public void makeUser(@PathVariable String username) {
        userDataService.makeUser(username);
    }
   /* @GetMapping("/statistics/most-popular")
     public void getMostPopular() {
        personService.getMostPopular();
    }*/
}
