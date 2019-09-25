package pl.com.soska.organizer.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import pl.com.soska.organizer.enums.RoleEnum;
import pl.com.soska.organizer.exception.UserExistException;
import pl.com.soska.organizer.exception.UserNotFoundException;
import pl.com.soska.organizer.model.Role;
import pl.com.soska.organizer.model.User;
import pl.com.soska.organizer.repository.RoleRepository;
import pl.com.soska.organizer.repository.UserRepository;

import java.util.*;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository,
                       RoleRepository roleRepository,
                       PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
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
        Role userRole = roleRepository.findByRole(RoleEnum.ROLE_USER);
        user.setRole(userRole);
        String passwordHash = passwordEncoder.encode(user.getPassword());
        user.setPassword(passwordHash);
        userRepository.save(user);
    }

    public void deleteUser(User user) {
        userRepository.findByEmail(user.getEmail())
                .orElseThrow(() -> new UserNotFoundException("User with email: " + user.getEmail() + " was not found."));
        userRepository.delete(user);
    }
}
