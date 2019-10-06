package pl.com.organizer.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import pl.com.organizer.enums.RoleEnum;
import pl.com.organizer.exception.UserNotFoundException;
import pl.com.organizer.model.Role;
import pl.com.organizer.model.User;
import pl.com.organizer.repository.RoleRepository;
import pl.com.organizer.repository.UserRepository;
import pl.com.organizer.exception.UserExistException;

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

    public void createUser(User user) {
        Optional<User> usernameExist = userRepository.findByEmail(user.getEmail());
        if (usernameExist.isPresent()) {
            throw new UserExistException("User with email: " + user.getEmail() + " already exist");
        }

        Role userRole = roleRepository.findByRole(RoleEnum.ROLE_USER);
        user.setRole(Set.of(userRole));
        String passwordHash = passwordEncoder.encode(user.getPassword());
        user.setPassword(passwordHash);
        userRepository.save(user);
    }

    public boolean passwordChecking(String username, String oldPasswordToCheck){
        String oldPasswordInDb = getUserByUsername(username).getPassword();
        return passwordEncoder.matches(oldPasswordToCheck, oldPasswordInDb);
    }

    private User getUserByUsername(String email) {
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