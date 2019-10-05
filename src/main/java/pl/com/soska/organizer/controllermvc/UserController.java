package pl.com.soska.organizer.controllermvc;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import pl.com.soska.organizer.exception.UserExistException;
import pl.com.soska.organizer.model.ChangePassword;
import pl.com.soska.organizer.model.User;
import pl.com.soska.organizer.service.UserService;

import javax.validation.Valid;
import java.security.Principal;

@Controller
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/login")
    public String loginPage() {
        return "login-page";
    }

    @GetMapping("/logged")
    public String login() {
        return "logged-page";
    }

    @GetMapping("/register")
    public String register(Model model) {
        User user = new User();
        model.addAttribute(user);
        return "register-page";
    }

    @PostMapping("/register/add-user")
    public String addUser(@Valid @ModelAttribute User user,
                          BindingResult result) {
        if (result.hasErrors()) {
            return "register-page";
        }
        try {
            userService.createUser(user);
            return "register-success-page";
        } catch (UserExistException e) {
            result.rejectValue("email", "error.user", e.getMessage());
            return "register-page";
        }
    }

    @GetMapping("/change-password")
    public String changePassword(Model model){
        ChangePassword changePassword = new ChangePassword();
        model.addAttribute(changePassword);
        return "change-password-page";
    }

    @PostMapping("/change-password/confirm")
    public String change (@Valid @ModelAttribute ChangePassword changePassword,
                          BindingResult result,
                          Principal principal){
        String username = principal.getName();
        boolean passwordMatchingCheck = userService.passwordChecking(username, changePassword.getOldPassword());

        if (!passwordMatchingCheck){
            result.rejectValue("oldPassword", "error.changePassword", "Wrong old password");
        }

        if (result.hasErrors()){
            return "change-password-page";
        }
        userService.changePassword(username, changePassword.getNewPassword());
        return "success-password-change-page";
    }

    @GetMapping("/delete-account")
    public String deleteAccount(){
        return "delete-account-page";
    }

    @DeleteMapping("/delete-account/confirm")
    public String confirmDeleteAccount (@RequestParam String passwordToCheck,
                                        Model model,
                                        Principal principal){
        String username = principal.getName();
        boolean passwordMatchingCheck = userService.passwordChecking(username, passwordToCheck);

        if (!passwordMatchingCheck){
            model.addAttribute("errorMessage", "");
            return "delete-account-page";
        }
        userService.deleteUser(username);
        return "redirect:/";
    }
}