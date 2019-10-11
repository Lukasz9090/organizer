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

    @GetMapping("/confirm-account")
    public String confirmAccountPage(@RequestParam (value = "id", required = true) String confirmNumber,
                                     Model model){
        try{
            userService.confirmEmailAddress(confirmNumber);
            model.addAttribute("message", "Your e-mail address has been verified. You can sign in now.");
            return "confirm-account-page";
        }catch (UserNotFoundException e){
            model.addAttribute("message", e.getMessage());
            return "confirm-account-page";
        }
    }
}
