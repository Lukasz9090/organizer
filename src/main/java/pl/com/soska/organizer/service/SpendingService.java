package pl.com.soska.organizer.service;

import org.springframework.stereotype.Service;
import pl.com.soska.organizer.exception.UserNotFoundException;
import pl.com.soska.organizer.model.Spending;
import pl.com.soska.organizer.model.User;
import pl.com.soska.organizer.repository.UserRepository;

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