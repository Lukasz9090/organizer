package pl.com.soska.organizer.controllermvc;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import pl.com.soska.organizer.model.User;
import pl.com.soska.organizer.service.UserService;

@Controller
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/register")
    public String register (Model model){
        model.addAttribute("newUser", new User());
        return "register-form";
    }

    @PostMapping("/add-user")
    public String addUser (@ModelAttribute User user){
        userService.createUser(user);
        return "register-success";
    }

    @GetMapping("/login")
    public String loginPage (Model model){
        model.addAttribute("user", new User());
        return "login-form";
    }

    @GetMapping("/logged")
    public String login (){
        return "logged-page";
    }
}
