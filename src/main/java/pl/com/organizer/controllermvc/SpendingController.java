package pl.com.organizer.controllermvc;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import pl.com.organizer.model.Spending;
import pl.com.organizer.service.SpendingService;

import javax.validation.Valid;
import java.security.Principal;

@Controller
public class SpendingController {

    private final SpendingService spendingService;

    public SpendingController(SpendingService spendingService) {
        this.spendingService = spendingService;
    }

    @GetMapping("/user/add-spending")
    public String addSpending(Model model) {
        Spending spending = new Spending();
        model.addAttribute(spending);
        return "add-expenses-page";
    }

    @PostMapping("/user/add-spending")
    public String addSpending(@Valid @ModelAttribute Spending spending,
                              BindingResult result,
                              Principal principal) {
        if (result.hasErrors()) {
            return "add-expenses-page";
        }
        spendingService.addSpendingToUser(principal.getName(), spending);
        return "redirect:/user/add-spending";
    }
}