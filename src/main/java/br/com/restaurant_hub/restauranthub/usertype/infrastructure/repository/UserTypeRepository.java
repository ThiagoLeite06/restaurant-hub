package br.com.restaurant_hub.restauranthub.usertype.infrastructure.repository;

import br.com.restaurant_hub.restauranthub.usertype.domain.entity.UserType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserTypeRepository extends JpaRepository<UserType, Long> {
    Optional<UserType> findByName(String name);
    Boolean existsByName(String name);
}