package pl.com.organizer.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import pl.com.organizer.enums.RoleEnum;
import pl.com.organizer.exception.UserNotFoundException;
import pl.com.organizer.mail.EmailService;
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
    private final EmailService emailService;

    public UserService(UserRepository userRepository,
                       RoleRepository roleRepository,
                       PasswordEncoder passwordEncoder,
                       EmailService emailService) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.emailService = emailService;
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
        user.setConfirmationNumber(user.confirmationNumberGenerator());

        userRepository.save(user);

        emailService.send(user.getEmail(), "Budget organizer confirmation link",
                "http://localhost:8080/confirm-account?id=" + user.getConfirmationNumber());
    }

    public void sendEmailToResetPassword(String username){
        User userToResetPassword = getUserByUsername(username);
        userToResetPassword.setResetPasswordNumber(userToResetPassword.confirmationNumberGenerator());
        emailService.send(username, "Budget organizer - reset password", "http://localhost:8080/set-new-password?id=" + userToResetPassword.getResetPasswordNumber());
        userRepository.save(userToResetPassword);
    }

    public User findUserByResetPasswordNumber(String confirmResetNumber){
        return userRepository.findByResetPasswordNumber(confirmResetNumber)
                .orElseThrow(() ->  new UserNotFoundException("Invalid confirmation number"));
    }

    public void confirmEmailAddress (String confirmationNumber){
        User userToConfirmEmail = userRepository.findByConfirmationNumber(confirmationNumber)
                .orElseThrow(() ->  new UserNotFoundException("Invalid confirmation number"));

        userToConfirmEmail.setEmailAddressConfirmationStatus(true);
        userToConfirmEmail.setConfirmationNumber("Confirmed");
        userRepository.save(userToConfirmEmail);
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