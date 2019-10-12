package pl.com.organizer.controllermvc;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import pl.com.organizer.exception.UserNotFoundException;
import pl.com.organizer.model.ChangePassword;
import pl.com.organizer.model.User;
import pl.com.organizer.service.UserService;

import javax.validation.Valid;
import java.security.Principal;

@Controller
public class ConfirmController {

    private final UserService userService;

    public ConfirmController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/home/confirm-account")
    public String confirmAccountPage(@RequestParam(value = "id", required = true) String confirmNumber,
                                     Model model) {
        try {
            userService.confirmEmailAddress(confirmNumber);
            model.addAttribute("message", "Your e-mail address has been verified. You can sign in now.");
            return "confirm-account-page";
        } catch (UserNotFoundException e) {
            model.addAttribute("message", e.getMessage());
            return "confirm-account-page";
        }
    }

    @GetMapping("/home/set-new-password")
    public String createNewPassword(@RequestParam(value = "id", required = true) String confirmNumber,
                                    Model model) {
        ChangePassword changePassword = new ChangePassword();
        model.addAttribute(changePassword);
        model.addAttribute("id", confirmNumber);
        return "create-new-password";
    }

    @PostMapping("/home/set-new-password")
    public String newPass(@RequestParam(value = "id", required = true) String confirmNumber,
                          @Valid @ModelAttribute ChangePassword changePassword,
                          BindingResult result) {
        if (result.hasErrors()) {
            return "create-new-password";
        }

        try {
            User user = userService.findUserByResetPasswordNumber(confirmNumber);
            userService.changePassword(user.getEmail(), changePassword.getNewPassword());
            return "success-password-change-page";
        } catch (UserNotFoundException e) {
//            TODO add something when exception
            return "main-page";

        }
    }
}