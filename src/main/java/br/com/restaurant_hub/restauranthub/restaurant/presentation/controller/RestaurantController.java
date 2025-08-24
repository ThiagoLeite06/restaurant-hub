package br.com.restaurant_hub.restauranthub.restaurant.presentation.controller;

import br.com.restaurant_hub.restauranthub.common.dto.ApiResponse;
import br.com.restaurant_hub.restauranthub.common.dto.PaginationResponse;
import br.com.restaurant_hub.restauranthub.restaurant.application.dto.*;
import br.com.restaurant_hub.restauranthub.restaurant.application.service.RestaurantService;
import br.com.restaurant_hub.restauranthub.restaurant.domain.entity.Restaurant;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.Optional;

@RestController
@RequestMapping("/api/restaurants")
public class RestaurantController {
    
    private final RestaurantService restaurantService;
    
    public RestaurantController(RestaurantService restaurantService) {
        this.restaurantService = restaurantService;
    }
    
    @GetMapping
    public ResponseEntity<ApiResponse<RestaurantResponse>> findAll(
            @RequestParam(name = "page", defaultValue = "0") Integer page,
            @RequestParam(name = "pageSize", defaultValue = "5") Integer pageSize,
            @RequestParam(name = "orderBy", defaultValue = "desc") String orderBy) {
        
        Page<Restaurant> pageResponse = restaurantService.findAll(page, pageSize, orderBy);
        
        return ResponseEntity.ok(new ApiResponse<>(
                pageResponse.getContent().stream()
                        .map(this::mapToResponse)
                        .toList(),
                new PaginationResponse(
                        pageResponse.getNumber(),
                        pageResponse.getSize(),
                        pageResponse.getTotalElements(),
                        pageResponse.getTotalPages()
                )
        ));
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<RestaurantResponse> findById(@PathVariable Long id) {
        Optional<Restaurant> restaurant = restaurantService.findById(id);
        return restaurant.isPresent() 
                ? ResponseEntity.ok(mapToResponse(restaurant.get())) 
                : ResponseEntity.notFound().build();
    }
    
    @PostMapping
    public ResponseEntity<RestaurantResponse> createRestaurant(@Valid @RequestBody CreateRestaurantRequest request) {
        Restaurant restaurant = restaurantService.createRestaurant(request);
        RestaurantResponse response = mapToResponse(restaurant);
        return ResponseEntity.created(URI.create("/api/restaurants/" + response.id()))
                .body(response);
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<RestaurantResponse> updateById(@PathVariable Long id,
                                                        @Valid @RequestBody UpdateRestaurantRequest request) {
        Optional<Restaurant> restaurant = restaurantService.updateById(id, request);
        return restaurant.isPresent() 
                ? ResponseEntity.ok(mapToResponse(restaurant.get())) 
                : ResponseEntity.notFound().build();
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRestaurant(@PathVariable Long id) {
        Boolean deleted = restaurantService.deleteById(id);
        return deleted ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }
    
    private RestaurantResponse mapToResponse(Restaurant restaurant) {
        return new RestaurantResponse(
                restaurant.getId().toString(),
                restaurant.getName(),
                restaurant.getAddress(),
                restaurant.getCuisineType(),
                restaurant.getCuisineType().getDisplayName(),
                restaurant.getOpeningHours(),
                restaurant.getOwner().getId().toString(),
                restaurant.getOwner().getName(),
                restaurant.getActive(),
                restaurant.getCreatedAt(),
                restaurant.getUpdatedAt()
        );
    }
}