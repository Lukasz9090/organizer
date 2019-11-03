package pl.com.organizer.service;

import org.springframework.stereotype.Service;
import pl.com.organizer.model.Expense;
import pl.com.organizer.model.User;
import pl.com.organizer.repository.UserRepository;

import java.util.Arrays;
import java.util.regex.Pattern;

@Service
public class ExpenseService {

    private final UserRepository userRepository;
    private final UserService userService;

    public ExpenseService(UserRepository userRepository,
                          UserService userService) {
        this.userRepository = userRepository;
        this.userService = userService;
    }

    public void addExpenseToUser(String username, Expense expense) {
        User userToAddExpense = userService.getUserByUsername(username);
        Expense expenseWithCorrectAmountFormat = checkingAmountFormat(expense);
        addExpense(userToAddExpense, expenseWithCorrectAmountFormat);
        userRepository.save(userToAddExpense);
    }

    Expense checkingAmountFormat(Expense expense){
        String amount = expense.getAmount();

        if (amount.matches("-?[0-9]{1,6}\\.[0-9]")) {
            expense.setAmount(expense.getAmount() + "0");
            return expense;
        }
        else if (amount.matches("-?[0-9]{1,6}\\.")){
            expense.setAmount(expense.getAmount() + "00");
            return expense;
        }
        else if (amount.matches("-?[0-9]{1,6}")){
            expense.setAmount(expense.getAmount() + ".00");
            return expense;
        }
        return expense;
    }

    void addExpense (User user, Expense expense){
        user.getExpenses().add(expense);
    }
}