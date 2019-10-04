package pl.com.soska.organizer.controllermvc;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import pl.com.soska.organizer.exception.UserExistException;
import pl.com.soska.organizer.model.ChangePassword;
import pl.com.soska.organizer.model.User;
import pl.com.soska.organizer.service.ReportGeneratorService;
import pl.com.soska.organizer.service.UserService;

import javax.validation.Valid;
import java.security.Principal;

@Controller
public class UserController {

    private final UserService userService;
    private final ReportGeneratorService reportGeneratorService;

    public UserController(UserService userService, ReportGeneratorService reportGeneratorService) {
        this.userService = userService;
        this.reportGeneratorService = reportGeneratorService;
    }

    @GetMapping("/register")
    public String register(Model model) {
        User user = new User();
        model.addAttribute(user);
        return "register-form";
    }

    @PostMapping("/added-user")
    public String addUser(@Valid @ModelAttribute User user, BindingResult result) {
        if (result.hasErrors()) {
            return "register-form";
        }
        try {
            userService.createUser(user);
            return "register-success";
        } catch (UserExistException e) {
            result.rejectValue("email", "error.user", e.getMessage());
            return "register-form";
        }
    }

    @GetMapping("/login")
    public String loginPage() {
        return "login-form";
    }

    @GetMapping("/logged")
    public String login() {
        return "logged-page";
    }

    @GetMapping("/delete-account")
    public String deleteAccount(){
        return "delete-page";
    }

    @DeleteMapping("/confirm-deletion")
    public String confirmDeleteAccount (@RequestParam String passwordToCheck, Model model, Principal principal){
        String username = principal.getName();
        boolean match = userService.passwordChecking(username, passwordToCheck);

        if (!match){
            model.addAttribute("errorMessage", "");
            return "delete-page";
        }

        userService.deleteUser(username);
        return "redirect:/";
    }

    @GetMapping("/change-password")
    public String changePassword(Model model){
        ChangePassword changePassword = new ChangePassword();
        model.addAttribute(changePassword);
        return "change-password-page";
    }

    @PostMapping("/change")
    public String change (@Valid @ModelAttribute ChangePassword changePassword, BindingResult result, Principal principal){

        boolean match = userService.passwordChecking(principal.getName(), changePassword.getOldPassword());

        if (!match){
            result.rejectValue("oldPassword", "error.changePassword", "Wrong old password");
            return "change-password-page";
        }

        if (result.hasErrors()){
            return "change-password-page";
        }
        userService.changePassword(principal.getName(), changePassword.getNewPassword());
        return "success-password-change";
    }
}
