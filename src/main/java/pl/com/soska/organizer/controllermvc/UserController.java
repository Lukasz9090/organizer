package pl.com.soska.organizer.controllermvc;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
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
    public String register (Model model){
        model.addAttribute("newUser", new User());
        return "register-form";
    }

    @PostMapping("/added-user")
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
    public String login (){
        return "logged-page";
    }

    @GetMapping("/add-spending")
    public String login (Model model){
        Spending spending = new Spending();
        model.addAttribute(spending);
        return "add-spending-page";
    }

    @PostMapping("/added-spending")
    public String addSpending(@Valid @ModelAttribute Spending spending,
                              BindingResult result,
                              Principal principal){
        if (result.hasErrors()){
            return "add-spending-page";
        }
        else {
            String username = principal.getName();
            userService.addSpendingToUser(username, spending);
            return "redirect:add-spending";
        }
    }

    @GetMapping("/create-report")
    public String createReport (Model model){
        model.addAttribute("reportSettings", new ReportSettings());
        return "report-generator-page";
    }

    @PostMapping("/add-data-to-report")
    @ResponseBody
    public ResponseEntity<byte[]> addDate (@ModelAttribute ReportSettings reportSettings,
                                                         Principal principal){

        String username = principal.getName();
        byte[] pdfContents = reportGenerator.generatePdfReport(username, reportSettings);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.add("content-disposition", "inline; filename=" + "Spending report.pdf");

        return new ResponseEntity<>(pdfContents, headers, HttpStatus.OK);
    }
}
