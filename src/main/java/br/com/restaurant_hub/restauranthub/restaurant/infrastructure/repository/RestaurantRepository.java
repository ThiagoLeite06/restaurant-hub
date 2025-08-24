package br.com.restaurant_hub.restauranthub.restaurant.infrastructure.repository;

import br.com.restaurant_hub.restauranthub.restaurant.domain.entity.Restaurant;
import br.com.restaurant_hub.restauranthub.restaurant.domain.enums.CuisineType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface RestaurantRepository extends JpaRepository<Restaurant, Long> {
    Optional<Restaurant> findByName(String name);
    List<Restaurant> findByCuisineType(CuisineType cuisineType);
    Page<Restaurant> findByActiveTrue(Pageable pageable);
    Boolean existsByName(String name);
    Boolean existsByOwnerId(UUID ownerId);
    Optional<Restaurant> findByOwnerId(UUID ownerId);
}