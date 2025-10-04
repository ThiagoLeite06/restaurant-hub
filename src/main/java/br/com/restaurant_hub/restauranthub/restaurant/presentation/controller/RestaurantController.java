package br.com.restaurant_hub.restauranthub.restaurant.presentation.controller;

import br.com.restaurant_hub.restauranthub.common.dto.ApiResponse;
import br.com.restaurant_hub.restauranthub.common.dto.PaginationResponse;
import br.com.restaurant_hub.restauranthub.restaurant.application.dto.*;
import br.com.restaurant_hub.restauranthub.restaurant.application.service.RestaurantService;
import br.com.restaurant_hub.restauranthub.restaurant.domain.entity.Restaurant;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.Optional;

@RestController
@RequestMapping("/api/restaurants")
@Tag(name = "Restaurantes", description = "Operações relacionadas ao gerenciamento de restaurantes")
@SecurityRequirement(name = "bearerAuth")
public class RestaurantController {
    
    private final RestaurantService restaurantService;
    
    public RestaurantController(RestaurantService restaurantService) {
        this.restaurantService = restaurantService;
    }
    
    @GetMapping
    @Operation(
        summary = "Listar restaurantes",
        description = "Retorna uma lista paginada de restaurantes"
    )
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200",
            description = "Lista de restaurantes retornada com sucesso",
            content = @Content(schema = @Schema(implementation = ApiResponse.class))
        )
    })
    public ResponseEntity<ApiResponse<RestaurantResponse>> findAll(
            @Parameter(description = "Número da página (inicia em 0)")
            @RequestParam(name = "page", defaultValue = "0") Integer page,
            @Parameter(description = "Tamanho da página")
            @RequestParam(name = "pageSize", defaultValue = "5") Integer pageSize,
            @Parameter(description = "Ordenação (asc ou desc)")
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
    @Operation(
        summary = "Buscar restaurante por ID",
        description = "Retorna um restaurante específico pelo seu ID"
    )
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200",
            description = "Restaurante encontrado",
            content = @Content(schema = @Schema(implementation = RestaurantResponse.class))
        ),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "404",
            description = "Restaurante não encontrado"
        )
    })
    public ResponseEntity<RestaurantResponse> findById(
            @Parameter(description = "ID do restaurante") @PathVariable Long id) {
        Optional<Restaurant> restaurant = restaurantService.findById(id);
        return restaurant.isPresent() 
                ? ResponseEntity.ok(mapToResponse(restaurant.get())) 
                : ResponseEntity.notFound().build();
    }
    
    @PostMapping
    @Operation(
        summary = "Criar restaurante",
        description = "Cria um novo restaurante. Apenas usuários do tipo OWNER podem criar restaurantes."
    )
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "201",
            description = "Restaurante criado com sucesso",
            content = @Content(schema = @Schema(implementation = RestaurantResponse.class))
        ),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "400",
            description = "Dados inválidos ou usuário não é OWNER"
        ),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "409",
            description = "Nome do restaurante já existe ou usuário já possui um restaurante"
        )
    })
    public ResponseEntity<RestaurantResponse> createRestaurant(
            @Parameter(description = "Dados do restaurante a ser criado")
            @Valid @RequestBody CreateRestaurantRequest request) {
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