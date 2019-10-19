package pl.com.organizer.service;

import org.springframework.stereotype.Service;
import pl.com.organizer.exception.UserNotFoundException;
import pl.com.organizer.model.User;
import pl.com.organizer.repository.UserRepository;

@Service
public class ConfirmService {

    private final UserRepository userRepository;

    public ConfirmService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void confirmEmailAddress (String confirmationNumber){
        User userToConfirmEmail = getUserByConfirmationNumber(confirmationNumber);

        userToConfirmEmail.setEmailAddressConfirmationStatus(true);
        userToConfirmEmail.setConfirmationNumber("Account confirmed");
        userRepository.save(userToConfirmEmail);
    }

    private User getUserByConfirmationNumber (String confirmationNumber){
        return userRepository.findByConfirmationNumber(confirmationNumber)
                .orElseThrow(() ->  new UserNotFoundException("Invalid confirmation number. Please contact us."));
    }
}
