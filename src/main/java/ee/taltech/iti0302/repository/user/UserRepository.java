package ee.taltech.iti0302.repository.user;

import ee.taltech.iti0302.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {

    @Override
    Optional<User> findById(Integer integer);

    Optional<User> findByEmail(String email);
}
