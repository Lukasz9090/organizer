package pl.com.soska.organizer.controller;

import org.springframework.stereotype.Controller;
import pl.com.soska.organizer.service.UserService;

@Controller
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }


}
