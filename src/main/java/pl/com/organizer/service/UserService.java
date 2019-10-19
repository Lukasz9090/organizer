package pl.com.organizer.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import pl.com.organizer.exception.UserNotFoundException;
import pl.com.organizer.model.User;
import pl.com.organizer.repository.UserRepository;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository,
                       PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public boolean passwordChecking(String username, String oldPasswordToCheck){
        String oldPasswordInDb = getUserByUsername(username).getPassword();
        return passwordEncoder.matches(oldPasswordToCheck, oldPasswordInDb);
    }

    public User getUserByUsername(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User with email: " + email + " was not found."));
    }

    public void changePassword(String username, String newPassword){
        User userToChangePassword = getUserByUsername(username);
        String passwordHash = passwordEncoder.encode(newPassword);

        userToChangePassword.setPassword(passwordHash);
        userRepository.save(userToChangePassword);
    }

    public void deleteUser(String username) {
        User userToDelete = getUserByUsername(username);
        userRepository.delete(userToDelete);
    }
}