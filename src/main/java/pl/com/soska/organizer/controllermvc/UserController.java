package pl.com.soska.organizer.controllermvc;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import pl.com.soska.organizer.exception.UserExistException;
import pl.com.soska.organizer.model.ChangePassword;
import pl.com.soska.organizer.model.ReportSettings;
import pl.com.soska.organizer.model.Spending;
import pl.com.soska.organizer.model.User;
import pl.com.soska.organizer.service.ReportGenerator;
import pl.com.soska.organizer.service.UserService;

import javax.validation.Valid;
import java.security.Principal;

@Controller
public class UserController {

    private final UserService userService;
    private final ReportGenerator reportGenerator;

    public UserController(UserService userService, ReportGenerator reportGenerator) {
        this.userService = userService;
        this.reportGenerator = reportGenerator;
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

    @GetMapping("/add-spending")
    public String login(Model model) {
        Spending spending = new Spending();
        model.addAttribute(spending);
        return "add-spending-page";
    }

    @PostMapping("/add-spending")
    public String addSpending(@Valid @ModelAttribute Spending spending,
                              BindingResult result,
                              Principal principal) {
        if (result.hasErrors()) {
            return "add-spending-page";
        } else {
            String username = principal.getName();
            userService.addSpendingToUser(username, spending);
            return "redirect:add-spending";
        }
    }

    @GetMapping("/create-report")
    public String createReport(Model model) {
        ReportSettings reportSettings = new ReportSettings();
        model.addAttribute("reportSettings", reportSettings);
        return "report-generator-page";
    }

    @PostMapping("/report")
    @ResponseBody
    public ResponseEntity<byte[]> addDate(@ModelAttribute ReportSettings reportSettings,
                                          Principal principal) {
        String username = principal.getName();
        byte[] pdfContents = reportGenerator.generatePdfReport(username, reportSettings);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.add("content-disposition", "inline; filename=" + "Spending report.pdf");

        return new ResponseEntity<>(pdfContents, headers, HttpStatus.OK);
    }

    @GetMapping("/delete-account")
    public String deleteAccount(){
        return "delete-page";
    }

    @DeleteMapping("/confirm-deletion")
    public String confirmDeleteAccount (Principal principal){
        String username = principal.getName();
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
