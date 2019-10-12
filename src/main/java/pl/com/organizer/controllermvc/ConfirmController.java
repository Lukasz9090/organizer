package pl.com.organizer.controllermvc;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import pl.com.organizer.exception.UserNotFoundException;
import pl.com.organizer.service.UserService;

@Controller
public class ConfirmController {

    private final UserService userService;

    public ConfirmController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/home/confirm-account")
    public String confirmCreatedAccount(@RequestParam(value = "id", required = true) String numberToConfirmEmailAddress,
                                     Model model) {
        try {
            userService.confirmEmailAddress(numberToConfirmEmailAddress);
            model.addAttribute("message", "Your e-mail address has been verified. You can sign in now.");
            return "default-success-page";
        } catch (UserNotFoundException e) {
            model.addAttribute("message", e.getMessage());
            return "default-success-page";
        }
    }
}