package pl.com.soska.organizer.controllermvc;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import pl.com.soska.organizer.model.Spending;
import pl.com.soska.organizer.model.User;
import pl.com.soska.organizer.service.UserService;

import java.security.Principal;

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
    public String login (Model model){
        model.addAttribute("newSpending", new Spending());
        return "logged-page";
    }

    @PostMapping("/add-spending")
    public String addSpending(@ModelAttribute Spending spending, Principal principal){
        String username = principal.getName();
        userService.addSpendingToUser(username, spending);
        return "redirect:logged";
    }
}
