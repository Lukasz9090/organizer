package pl.com.soska.organizer.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import pl.com.soska.organizer.enums.RoleEnum;
import pl.com.soska.organizer.model.Role;

@Repository
public interface RoleRepository extends MongoRepository<Role, String> {
    Role findByRole(RoleEnum roleEnum);
}
