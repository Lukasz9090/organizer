package pl.com.soska.organizer.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import pl.com.soska.organizer.model.User;

import java.util.Optional;

@Repository
public interface UserRepository extends MongoRepository<User, String> {
    public Optional <User> findByEmail(String email);
}
