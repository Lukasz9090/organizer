package pl.com.organizer.service;

import org.springframework.stereotype.Service;
import pl.com.organizer.exception.UserNotFoundException;
import pl.com.organizer.model.Spending;
import pl.com.organizer.model.User;
import pl.com.organizer.repository.UserRepository;

@Service
public class SpendingService {

    private final UserRepository userRepository;

    public SpendingService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void addSpendingToUser(String username, Spending spending) {
        User userToAddSpending = userRepository.findByEmail(username)
                .orElseThrow(() -> new UserNotFoundException("User with email: " + username + " was not found."));
        userToAddSpending.getSpending().add(spending);
        userRepository.save(userToAddSpending);
    }
}