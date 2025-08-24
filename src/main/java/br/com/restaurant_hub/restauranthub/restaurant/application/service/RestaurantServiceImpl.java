package br.com.restaurant_hub.restauranthub.restaurant.application.service;

import br.com.restaurant_hub.restauranthub.exception.InvalidUserDataException;
import br.com.restaurant_hub.restauranthub.restaurant.application.dto.*;
import br.com.restaurant_hub.restauranthub.restaurant.domain.entity.Restaurant;
import br.com.restaurant_hub.restauranthub.restaurant.domain.enums.CuisineType;
import br.com.restaurant_hub.restauranthub.restaurant.infrastructure.repository.RestaurantRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.Optional;
import java.util.function.Consumer;

@Service
@Transactional
public class RestaurantServiceImpl implements RestaurantService {
    
    private final RestaurantRepository restaurantRepository;
    
    public RestaurantServiceImpl(RestaurantRepository restaurantRepository) {
        this.restaurantRepository = restaurantRepository;
    }
    
    @Override
    public Restaurant createRestaurant(CreateRestaurantRequest request) {
        if (restaurantRepository.existsByName(request.name())) {
            throw new InvalidUserDataException("Nome do restaurante já está em uso");
        }
        
        Restaurant restaurant = new Restaurant();
        restaurant.setName(request.name());
        restaurant.setAddress(request.address());
        restaurant.setCuisineType(request.cuisineType());
        restaurant.setOpeningHours(request.openingHours());
        
        return restaurantRepository.save(restaurant);
    }
    
    @Override
    @Transactional(readOnly = true)
    public Optional<Restaurant> findById(Long id) {
        return restaurantRepository.findById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Restaurant> findAll(Integer page, Integer pageSize, String orderBy) {
        PageRequest pageRequest = getPageRequest(page, pageSize, orderBy);
        return restaurantRepository.findAll(pageRequest);
    }

    @Override
    public Optional<Restaurant> updateById(Long id, UpdateRestaurantRequest request) {
        return restaurantRepository.findById(id)
                .map(restaurant -> {
                    updateFieldIfPresent(request.name(), restaurant::setName);
                    updateFieldIfPresent(request.address(), restaurant::setAddress);
                    if (request.cuisineType() != null) {
                        restaurant.setCuisineType(request.cuisineType());
                    }
                    updateFieldIfPresent(request.openingHours(), restaurant::setOpeningHours);
                    return restaurantRepository.save(restaurant);
                });
    }
    
    @Override
    public Boolean deleteById(Long id) {
        Boolean exists = restaurantRepository.existsById(id);
        if (exists) {
            restaurantRepository.deleteById(id);
        }
        return exists;
    }
    
    @Override
    @Transactional(readOnly = true)
    public Boolean existsByName(String name) {
        return restaurantRepository.existsByName(name);
    }
    
    private PageRequest getPageRequest(Integer page, Integer pageSize, String orderBy) {
        Sort.Direction direction = "asc".equalsIgnoreCase(orderBy) ? Sort.Direction.ASC : Sort.Direction.DESC;
        return PageRequest.of(page, pageSize, Sort.by(direction, "name"));
    }

    private void updateFieldIfPresent(String value, Consumer<String> setter) {
        if (StringUtils.hasText(value)) {
            setter.accept(value);
        }
    }
}