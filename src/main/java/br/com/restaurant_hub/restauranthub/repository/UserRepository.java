package br.com.restaurant_hub.restauranthub.repository;

import br.com.restaurant_hub.restauranthub.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByLogin(String login);
    Optional<User> findByEmail(String email);
    boolean existsByLogin(String login);
    boolean existsByEmail(String email);
    boolean existsByLoginAndIdNot(String login, Long id);
    boolean existsByEmailAndIdNot(String email, Long id);
}
