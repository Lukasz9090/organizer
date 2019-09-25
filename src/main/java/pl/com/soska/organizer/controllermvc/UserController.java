package pl.com.soska.organizer.controllermvc;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
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
        model.addAttribute("user", new User());
        return "register-form";
    }

    @PostMapping("register")
    public String addUser(@ModelAttribute User user,
                          BindingResult bindResult){
        if(bindResult.hasErrors()){
            return "register-form";
        }
        else {
            userService.createUser(user);
            return "register-success";
        }
    }
}
