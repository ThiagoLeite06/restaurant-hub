package br.com.restaurant_hub.restauranthub.menuitem.infrastructure.repository;

import br.com.restaurant_hub.restauranthub.menuitem.domain.entity.MenuItem;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MenuItemRepository extends JpaRepository<MenuItem, Long> {
    
    List<MenuItem> findByRestaurantIdAndActiveTrue(Long restaurantId);
    
    Page<MenuItem> findByRestaurantIdAndActiveTrue(Long restaurantId, Pageable pageable);
    
    Page<MenuItem> findByActiveTrue(Pageable pageable);
    
    Optional<MenuItem> findByIdAndActiveTrue(Long id);
    
    Boolean existsByNameAndRestaurantId(String name, Long restaurantId);
    
    @Query("SELECT m FROM MenuItem m WHERE m.restaurant.id = :restaurantId AND m.active = true AND m.name ILIKE %:name%")
    List<MenuItem> findByRestaurantIdAndNameContainingIgnoreCase(@Param("restaurantId") Long restaurantId, @Param("name") String name);
    
    @Query("SELECT COUNT(m) FROM MenuItem m WHERE m.restaurant.id = :restaurantId AND m.active = true")
    Long countActiveItemsByRestaurantId(@Param("restaurantId") Long restaurantId);
}