package br.com.restaurant_hub.restauranthub.restaurant.application.service;

import br.com.restaurant_hub.restauranthub.restaurant.application.dto.*;
import br.com.restaurant_hub.restauranthub.restaurant.domain.entity.Restaurant;
import org.springframework.data.domain.Page;

import java.util.Optional;

public interface RestaurantService {
    
    Restaurant createRestaurant(CreateRestaurantRequest request);
    
    Optional<Restaurant> findById(Long id);

    Page<Restaurant> findAll(Integer page, Integer pageSize, String orderBy);

    Optional<Restaurant> updateById(Long id, UpdateRestaurantRequest request);
    
    Boolean deleteById(Long id);
    
    Boolean existsByName(String name);
}