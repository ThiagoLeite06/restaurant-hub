package br.com.restaurant_hub.restauranthub.restaurant.application.service;

import br.com.restaurant_hub.restauranthub.exception.InvalidUserDataException;
import br.com.restaurant_hub.restauranthub.restaurant.application.dto.CreateRestaurantRequest;
import br.com.restaurant_hub.restauranthub.restaurant.application.dto.UpdateRestaurantRequest;
import br.com.restaurant_hub.restauranthub.restaurant.domain.entity.Restaurant;
import br.com.restaurant_hub.restauranthub.restaurant.domain.enums.CuisineType;
import br.com.restaurant_hub.restauranthub.restaurant.infrastructure.repository.RestaurantRepository;
import br.com.restaurant_hub.restauranthub.user.domain.entity.User;
import br.com.restaurant_hub.restauranthub.user.infrastructure.repository.UserRepository;
import br.com.restaurant_hub.restauranthub.usertype.domain.entity.UserType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("RestaurantServiceImpl Tests")
class RestaurantServiceImplTest {

    @Mock
    private RestaurantRepository restaurantRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private RestaurantServiceImpl restaurantService;

    private Restaurant restaurant;
    private User owner;
    private UserType ownerType;
    private CreateRestaurantRequest createRestaurantRequest;
    private UpdateRestaurantRequest updateRestaurantRequest;
    private UUID ownerId;
    private Long restaurantId;

    @BeforeEach
    void setUp() {
        ownerId = UUID.randomUUID();
        restaurantId = 1L;

        ownerType = new UserType();
        ownerType.setId(1L);
        ownerType.setName("OWNER");
        ownerType.setDescription("Proprietário de restaurante");

        owner = new User();
        owner.setId(ownerId);
        owner.setName("João Owner");
        owner.setEmail("joao@owner.com");
        owner.setLogin("joao.owner");
        owner.setUserType(ownerType);
        owner.setCreatedAt(LocalDateTime.now());
        owner.setUpdatedAt(LocalDateTime.now());

        restaurant = new Restaurant();
        restaurant.setId(restaurantId);
        restaurant.setName("Pizzaria Bella Napoli");
        restaurant.setAddress("Rua Augusta, 1234 - São Paulo, SP");
        restaurant.setCuisineType(CuisineType.ITALIANA);
        restaurant.setOpeningHours("Ter-Dom: 18h às 23h");
        restaurant.setOwner(owner);
        restaurant.setCreatedAt(LocalDateTime.now());
        restaurant.setUpdatedAt(LocalDateTime.now());

        createRestaurantRequest = new CreateRestaurantRequest(
                "Pizzaria Bella Napoli",
                "Rua Augusta, 1234 - São Paulo, SP",
                CuisineType.ITALIANA,
                "Ter-Dom: 18h às 23h",
                ownerId
        );

        updateRestaurantRequest = new UpdateRestaurantRequest(
                "Pizzaria Bella Napoli - Renovada",
                "Nova Rua, 456 - São Paulo, SP",
                CuisineType.JAPONESA,
                "Seg-Dom: 19h às 00h",
                ownerId
        );
    }

    @Test
    @DisplayName("Should create restaurant successfully")
    void shouldCreateRestaurantSuccessfully() {
        // Given
        when(restaurantRepository.existsByName(createRestaurantRequest.name())).thenReturn(false);
        when(userRepository.findById(ownerId)).thenReturn(Optional.of(owner));
        when(restaurantRepository.existsByOwnerId(ownerId)).thenReturn(false);
        when(restaurantRepository.save(any(Restaurant.class))).thenReturn(restaurant);

        // When
        Restaurant result = restaurantService.createRestaurant(createRestaurantRequest);

        // Then
        assertNotNull(result);
        assertEquals(restaurant.getName(), result.getName());
        assertEquals(restaurant.getAddress(), result.getAddress());
        assertEquals(restaurant.getCuisineType(), result.getCuisineType());
        assertEquals(restaurant.getOpeningHours(), result.getOpeningHours());

        verify(restaurantRepository).existsByName(createRestaurantRequest.name());
        verify(userRepository).findById(ownerId);
        verify(restaurantRepository).existsByOwnerId(ownerId);
        verify(restaurantRepository).save(any(Restaurant.class));
    }

    @Test
    @DisplayName("Should throw exception when restaurant name already exists")
    void shouldThrowExceptionWhenRestaurantNameAlreadyExists() {
        // Given
        when(restaurantRepository.existsByName(createRestaurantRequest.name())).thenReturn(true);

        // When & Then
        InvalidUserDataException exception = assertThrows(
                InvalidUserDataException.class,
                () -> restaurantService.createRestaurant(createRestaurantRequest)
        );

        assertEquals("Nome do restaurante já está em uso", exception.getMessage());
        verify(restaurantRepository).existsByName(createRestaurantRequest.name());
        verify(userRepository, never()).findById(any());
        verify(restaurantRepository, never()).save(any());
    }

    @Test
    @DisplayName("Should throw exception when owner not found")
    void shouldThrowExceptionWhenOwnerNotFound() {
        // Given
        when(restaurantRepository.existsByName(createRestaurantRequest.name())).thenReturn(false);
        when(userRepository.findById(ownerId)).thenReturn(Optional.empty());

        // When & Then
        InvalidUserDataException exception = assertThrows(
                InvalidUserDataException.class,
                () -> restaurantService.createRestaurant(createRestaurantRequest)
        );

        assertEquals("Usuário proprietário não encontrado", exception.getMessage());
        verify(restaurantRepository).existsByName(createRestaurantRequest.name());
        verify(userRepository).findById(ownerId);
        verify(restaurantRepository, never()).save(any());
    }

    @Test
    @DisplayName("Should throw exception when user is not OWNER type")
    void shouldThrowExceptionWhenUserIsNotOwnerType() {
        // Given
        UserType clientType = new UserType();
        clientType.setId(2L);
        clientType.setName("CLIENT");
        owner.setUserType(clientType);

        when(restaurantRepository.existsByName(createRestaurantRequest.name())).thenReturn(false);
        when(userRepository.findById(ownerId)).thenReturn(Optional.of(owner));

        // When & Then
        InvalidUserDataException exception = assertThrows(
                InvalidUserDataException.class,
                () -> restaurantService.createRestaurant(createRestaurantRequest)
        );

        assertEquals("Usuário deve ser do tipo OWNER para possuir um restaurante", exception.getMessage());
        verify(restaurantRepository).existsByName(createRestaurantRequest.name());
        verify(userRepository).findById(ownerId);
        verify(restaurantRepository, never()).save(any());
    }

    @Test
    @DisplayName("Should throw exception when owner already has a restaurant")
    void shouldThrowExceptionWhenOwnerAlreadyHasRestaurant() {
        // Given
        when(restaurantRepository.existsByName(createRestaurantRequest.name())).thenReturn(false);
        when(userRepository.findById(ownerId)).thenReturn(Optional.of(owner));
        when(restaurantRepository.existsByOwnerId(ownerId)).thenReturn(true);

        // When & Then
        InvalidUserDataException exception = assertThrows(
                InvalidUserDataException.class,
                () -> restaurantService.createRestaurant(createRestaurantRequest)
        );

        assertEquals("Usuário já possui um restaurante cadastrado", exception.getMessage());
        verify(restaurantRepository).existsByName(createRestaurantRequest.name());
        verify(userRepository).findById(ownerId);
        verify(restaurantRepository).existsByOwnerId(ownerId);
        verify(restaurantRepository, never()).save(any());
    }

    @Test
    @DisplayName("Should find restaurant by id successfully")
    void shouldFindRestaurantByIdSuccessfully() {
        // Given
        when(restaurantRepository.findById(restaurantId)).thenReturn(Optional.of(restaurant));

        // When
        Optional<Restaurant> result = restaurantService.findById(restaurantId);

        // Then
        assertTrue(result.isPresent());
        assertEquals(restaurant, result.get());
        verify(restaurantRepository).findById(restaurantId);
    }

    @Test
    @DisplayName("Should return empty when restaurant not found by id")
    void shouldReturnEmptyWhenRestaurantNotFoundById() {
        // Given
        when(restaurantRepository.findById(restaurantId)).thenReturn(Optional.empty());

        // When
        Optional<Restaurant> result = restaurantService.findById(restaurantId);

        // Then
        assertTrue(result.isEmpty());
        verify(restaurantRepository).findById(restaurantId);
    }

    @Test
    @DisplayName("Should find all restaurants with pagination")
    void shouldFindAllRestaurantsWithPagination() {
        // Given
        Page<Restaurant> restaurantPage = new PageImpl<>(List.of(restaurant));
        when(restaurantRepository.findAll(any(PageRequest.class))).thenReturn(restaurantPage);

        // When
        Page<Restaurant> result = restaurantService.findAll(0, 10, "asc");

        // Then
        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        assertEquals(restaurant, result.getContent().get(0));
        verify(restaurantRepository).findAll(any(PageRequest.class));
    }

    @Test
    @DisplayName("Should update restaurant successfully")
    void shouldUpdateRestaurantSuccessfully() {
        // Given
        UpdateRestaurantRequest simpleUpdateRequest = new UpdateRestaurantRequest(
                "Pizzaria Bella Napoli - Renovada",
                "Nova Rua, 456 - São Paulo, SP",
                CuisineType.JAPONESA,
                "Seg-Dom: 19h às 00h",
                null // Não atualizar owner
        );
        
        when(restaurantRepository.findById(restaurantId)).thenReturn(Optional.of(restaurant));
        when(restaurantRepository.save(any(Restaurant.class))).thenReturn(restaurant);

        // When
        Optional<Restaurant> result = restaurantService.updateById(restaurantId, simpleUpdateRequest);

        // Then
        assertTrue(result.isPresent());
        verify(restaurantRepository).findById(restaurantId);
        verify(restaurantRepository).save(any(Restaurant.class));
    }

    @Test
    @DisplayName("Should return empty when restaurant not found for update")
    void shouldReturnEmptyWhenRestaurantNotFoundForUpdate() {
        // Given
        when(restaurantRepository.findById(restaurantId)).thenReturn(Optional.empty());

        // When
        Optional<Restaurant> result = restaurantService.updateById(restaurantId, updateRestaurantRequest);

        // Then
        assertTrue(result.isEmpty());
        verify(restaurantRepository).findById(restaurantId);
        verify(restaurantRepository, never()).save(any());
    }

    @Test
    @DisplayName("Should delete restaurant successfully")
    void shouldDeleteRestaurantSuccessfully() {
        // Given
        when(restaurantRepository.existsById(restaurantId)).thenReturn(true);

        // When
        Boolean result = restaurantService.deleteById(restaurantId);

        // Then
        assertTrue(result);
        verify(restaurantRepository).existsById(restaurantId);
        verify(restaurantRepository).deleteById(restaurantId);
    }

    @Test
    @DisplayName("Should return false when restaurant not found for deletion")
    void shouldReturnFalseWhenRestaurantNotFoundForDeletion() {
        // Given
        when(restaurantRepository.existsById(restaurantId)).thenReturn(false);

        // When
        Boolean result = restaurantService.deleteById(restaurantId);

        // Then
        assertFalse(result);
        verify(restaurantRepository).existsById(restaurantId);
        verify(restaurantRepository, never()).deleteById(any());
    }

    @Test
    @DisplayName("Should check if restaurant exists by name")
    void shouldCheckIfRestaurantExistsByName() {
        // Given
        String restaurantName = "Test Restaurant";
        when(restaurantRepository.existsByName(restaurantName)).thenReturn(true);

        // When
        Boolean result = restaurantService.existsByName(restaurantName);

        // Then
        assertTrue(result);
        verify(restaurantRepository).existsByName(restaurantName);
    }

    @Test
    @DisplayName("Should return false when restaurant does not exist by name")
    void shouldReturnFalseWhenRestaurantDoesNotExistByName() {
        // Given
        String restaurantName = "Nonexistent Restaurant";
        when(restaurantRepository.existsByName(restaurantName)).thenReturn(false);

        // When
        Boolean result = restaurantService.existsByName(restaurantName);

        // Then
        assertFalse(result);
        verify(restaurantRepository).existsByName(restaurantName);
    }
}