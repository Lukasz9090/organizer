package pl.com.organizer.service;

import org.springframework.stereotype.Service;
import pl.com.organizer.exception.UserNotFoundException;
import pl.com.organizer.model.Spending;
import pl.com.organizer.model.User;
import pl.com.organizer.repository.UserRepository;

@Service
public class SpendingService {

    private final UserRepository userRepository;
    private final UserService userService;

    public SpendingService(UserRepository userRepository,
                           UserService userService) {
        this.userRepository = userRepository;
        this.userService = userService;
    }

    public void addSpendingToUser(String username, Spending spending) {
        User userToAddSpending = userService.getUserByUsername(username);
        userToAddSpending.getSpending().add(spending);
        userRepository.save(userToAddSpending);
    }
}