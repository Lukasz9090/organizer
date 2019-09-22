package pl.com.soska.organizer.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import pl.com.soska.organizer.model.Spending;

@Repository
public interface SpendingRepository extends MongoRepository<Spending, String> {
}
