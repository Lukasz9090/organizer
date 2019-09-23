package pl.com.soska.organizer.service;

import org.springframework.stereotype.Service;
import pl.com.soska.organizer.exception.UserExistException;
import pl.com.soska.organizer.exception.UserNotFoundException;
import pl.com.soska.organizer.model.User;
import pl.com.soska.organizer.repository.UserRepository;

import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User getUserByUsername(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User with email: " + email + " was not found."));
    }

    public void createUser(User user) {
        Optional<User> usernameExist = userRepository.findByEmail(user.getEmail());
        if (usernameExist.isPresent()) {
            throw new UserExistException("User with email: " + user.getEmail() + " already exist");
        }
        userRepository.save(user);
    }

    public void deleteUser(User user) {
        userRepository.findByEmail(user.getEmail())
                .orElseThrow(() -> new UserNotFoundException("User with email: " + user.getEmail() + " was not found."));
        userRepository.delete(user);
    }
}
