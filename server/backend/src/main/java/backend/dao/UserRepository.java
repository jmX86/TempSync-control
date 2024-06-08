package backend.dao;

import backend.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUserName(String userName);

    Optional<User> findByUserEmail(String userEmail);

    int countAllByUserName(String userName);

    int countAllByUserEmail(String userEmail);
}
