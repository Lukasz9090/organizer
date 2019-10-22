package pl.com.organizer.controllermvc;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import pl.com.organizer.model.Expense;
import pl.com.organizer.service.ExpenseService;

import javax.validation.Valid;
import java.security.Principal;

@Controller
public class ExpenseController {

    private final ExpenseService expenseService;

    public ExpenseController(ExpenseService expenseService) {
        this.expenseService = expenseService;
    }

    @GetMapping("/user/add-expense")
    public String addExpense(Model model) {
        Expense expense = new Expense();
        model.addAttribute(expense);
        return "add-expenses-page";
    }

    @PostMapping("/user/add-expense")
    public String addExpense(@Valid @ModelAttribute Expense expense,
                              BindingResult result,
                              Principal principal) {
        if (result.hasErrors()) {
            return "add-expenses-page";
        }
        expenseService.addExpenseToUser(principal.getName(), expense);
        return "redirect:/user/add-expense";
    }
}