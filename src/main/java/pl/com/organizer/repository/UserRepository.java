package pl.com.organizer.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import pl.com.organizer.model.User;

import java.util.Optional;

@Repository
public interface UserRepository extends MongoRepository<User, String> {
    Optional <User> findByEmail(String email);
    Optional <User> findByConfirmationNumber(String confirmationNumber);
    Optional <User> findByResetPasswordNumber(String resetPasswordNumber);
}
