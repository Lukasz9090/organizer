package pl.com.organizer.service;

import org.springframework.stereotype.Service;
import pl.com.organizer.model.Expense;
import pl.com.organizer.model.User;
import pl.com.organizer.repository.UserRepository;

import java.util.Arrays;

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
        userToAddExpense.getExpenses().add(expenseWithCorrectAmountFormat);
        userRepository.save(userToAddExpense);
    }

    private Expense checkingAmountFormat (Expense expense){
        if (expense.getAmount().contains(".")){
            String [] splitAmount = expense.getAmount().split("\\.");
            switch (splitAmount[1].length()){
                case 0:
                    expense.setAmount(expense.getAmount() + "00");
                    break;
                case 1:
                    expense.setAmount(expense.getAmount() + "0");
                    break;
                case 2:
                    break;
            }
        } else {
            String correctAmount = expense.getAmount() + ".00";
            expense.setAmount(correctAmount);
        }
        return expense;
    }
}