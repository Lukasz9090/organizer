package pl.com.organizer.controllermvc;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import pl.com.organizer.exception.UserExistException;
import pl.com.organizer.exception.UserNotFoundException;
import pl.com.organizer.model.ChangePassword;
import pl.com.organizer.model.User;
import pl.com.organizer.service.UserService;

import javax.validation.Valid;

@Controller
public class MainController {

    private final UserService userService;

    public MainController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/")
    public String main(){
        return "redirect:/home";
    }

    @GetMapping("/home")
    public String mainPage(){
        return "main-page";
    }

    @GetMapping("/home/login")
    public String loginPage() {
        return "login-page";
    }

    @GetMapping("/home/login/reset-password")
    public String resetAccountPasswordWriteEmail() {
        return "reset-password-page-write-email";
    }

    @PostMapping("/home/login/new-password")
    public String resetAccountPasswordWriteEmail(@RequestParam String emailAddress,
                                Model model) {
        try {
            model.addAttribute("message", "Check your mailbox");
            userService.sendEmailToResetPassword(emailAddress);
            return "default-success-page";
        } catch (UserNotFoundException e) {
            model.addAttribute("errorMessage", "Account with this email does not exist");
            return "reset-password-page-write-email";
        }
    }

    @GetMapping("/home/set-new-password")
    public String resetAccountPasswordSetNewPassword(@RequestParam(value = "id", required = true) String numberToCreateNewPassword,
                                       Model model) {
        ChangePassword changePassword = new ChangePassword();
        model.addAttribute(changePassword);
        model.addAttribute("id", numberToCreateNewPassword);
        return "reset-password-page-write-new-password";
    }

    @PostMapping("/home/set-new-password")
    public String resetAccountPasswordSetNewPassword(@RequestParam(value = "id", required = true) String numberToCreateNewPassword,
                                                     @Valid @ModelAttribute ChangePassword changePassword,
                                                     BindingResult result,
                                                     Model model) {
        if (result.hasErrors()) {
            return "reset-password-page-write-new-password";
        }
        try {
            User user = userService.findUserByResetPasswordNumber(numberToCreateNewPassword);
            userService.changePassword(user.getEmail(), changePassword.getNewPassword());
            model.addAttribute("message", "Password reset with success. You can sign in now.");
            return "default-success-page";
        } catch (UserNotFoundException e) {
//            TODO add something when exception
            return "main-page";
        }
    }

    @GetMapping("/home/register")
    public String registerNewUser(Model model) {
        User user = new User();
        model.addAttribute(user);
        return "register-page";
    }

    @PostMapping("/home/register")
    public String registerNewUser(@Valid @ModelAttribute User user,
                          BindingResult result,
                          Model model) {
        if (result.hasErrors()) {
            return "register-page";
        }
        try {
            userService.createUser(user);
            model.addAttribute("message", "Check your mailbox and confirm your account.");
            return "default-success-page";
        } catch (UserExistException e) {
            result.rejectValue("email", "error.user", e.getMessage());
            return "register-page";
        }
    }
}
