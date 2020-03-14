package pl.com.organizer.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import pl.com.organizer.enums.RoleEnum;
import pl.com.organizer.model.Role;

@Repository
public interface RoleRepository extends MongoRepository<Role, String> {
    Role findByRole(RoleEnum roleEnum);
}
