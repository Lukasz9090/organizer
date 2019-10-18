package pl.com.organizer.controllermvc;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import pl.com.organizer.model.ChangePassword;
import pl.com.organizer.service.UserService;

import javax.validation.Valid;
import java.security.Principal;

@Controller
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/user")
    public String loggedPage() {
        return "logged-main-page";
    }

    @GetMapping("/user/change-password")
    public String changeAccountPassword(Model model) {
        ChangePassword changePassword = new ChangePassword();
        model.addAttribute(changePassword);
        return "change-password-page";
    }

    @PostMapping("/user/change-password")
    public String changeAccountPassword(@Valid @ModelAttribute ChangePassword changePassword,
                         BindingResult result,
                         Principal principal) {
        String username = principal.getName();
        boolean passwordMatchingCheck = userService.passwordChecking(username, changePassword.getOldPassword());

        if (!passwordMatchingCheck) {
            result.rejectValue("oldPassword", "error.changePassword", "Please enter correct password.");
        }
        if (result.hasErrors()) {
            return "change-password-page";
        }
        userService.changePassword(username, changePassword.getNewPassword());
        return "success-password-change-page";
    }

    @GetMapping("/user/delete-account")
    public String deleteAccount() {
        return "delete-account-page";
    }

    @DeleteMapping("/user/delete-account")
    public String confirmDeleteAccount(@RequestParam String passwordToCheck,
                                       Model model,
                                       Principal principal) {
        String username = principal.getName();
        boolean passwordMatchingCheck = userService.passwordChecking(username, passwordToCheck);

        if (!passwordMatchingCheck) {
            model.addAttribute("errorMessage", "");
            return "delete-account-page";
        }
        userService.deleteUser(username);
        return "redirect:/";
    }
}