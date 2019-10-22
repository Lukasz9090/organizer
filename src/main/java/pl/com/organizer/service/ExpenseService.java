package pl.com.organizer.service;

import org.springframework.stereotype.Service;
import pl.com.organizer.model.Expense;
import pl.com.organizer.model.User;
import pl.com.organizer.repository.UserRepository;

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
        userToAddExpense.getExpenses().add(expense);
        userRepository.save(userToAddExpense);
    }
}