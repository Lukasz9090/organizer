package pl.com.organizer.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import pl.com.organizer.enums.RoleEnum;
import pl.com.organizer.exception.UserExistException;
import pl.com.organizer.exception.UserNotFoundException;
import pl.com.organizer.model.Role;
import pl.com.organizer.model.User;
import pl.com.organizer.repository.RoleRepository;
import pl.com.organizer.repository.UserRepository;

import java.util.Optional;
import java.util.Set;

@Service
public class MainService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final EmailService emailService;
    private final PasswordEncoder passwordEncoder;

    public MainService(UserRepository userRepository,
                       RoleRepository roleRepository,
                       EmailService emailService,
                       PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.emailService = emailService;
        this.passwordEncoder = passwordEncoder;
    }

    public void sendEmailToResetPassword(String username) {
        User userToResetPassword = getUserByUsername(username);
        userToResetPassword.setResetPasswordNumber(userToResetPassword.confirmationNumberGenerator());

        emailService.createEmailMessage(username,
                "Budget organizer - link to reset password",
                "email-message-reset-password-template",
                "http://localhost:8080/home/set-new-password?id=" + userToResetPassword.getResetPasswordNumber());

        userRepository.save(userToResetPassword);
    }

    private User getUserByUsername(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User with email: " + email + " was not found."));
    }

    public User findUserByResetPasswordNumber(String confirmResetNumber) {
        return userRepository.findByResetPasswordNumber(confirmResetNumber)
                .orElseThrow(() -> new UserNotFoundException("Invalid confirmation number. Please contact us."));
    }

    public void createNewUser(User user) {
        Optional<User> usernameExist = userRepository.findByEmail(user.getEmail());
        if (usernameExist.isPresent()) {
            throw new UserExistException("User with email: " + user.getEmail() + " already exist");
        }

        Role userRole = roleRepository.findByRole(RoleEnum.ROLE_USER);
        user.setRole(Set.of(userRole));
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setConfirmationNumber(user.confirmationNumberGenerator());

        userRepository.save(user);

        emailService.createEmailMessage(user.getEmail(),
                "Budget organizer - account confirmation link",
                "email-message-confirm-account-template",
                "http://localhost:8080/home/confirm-account?id=" + user.getConfirmationNumber());
    }
}
