package br.com.restaurant_hub.restauranthub.restaurant.application.service;

import br.com.restaurant_hub.restauranthub.exception.InvalidUserDataException;
import br.com.restaurant_hub.restauranthub.restaurant.application.dto.CreateRestaurantRequest;
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

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("RestaurantService Tests")
class RestaurantServiceTest {

    @Mock
    private RestaurantRepository restaurantRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private RestaurantServiceImpl restaurantService;

    private CreateRestaurantRequest validRequest;
    private User ownerUser;
    private UserType ownerUserType;
    private Restaurant savedRestaurant;

    @BeforeEach
    void setUp() {
        UUID ownerId = UUID.randomUUID();

        validRequest = new CreateRestaurantRequest(
                "Restaurante Teste",
                "Rua das Flores, 123",
                CuisineType.BRASILEIRA,
                "Seg-Dom: 11h às 23h",
                ownerId
        );

        ownerUserType = new UserType();
        ownerUserType.setId(1L);
        ownerUserType.setName("OWNER");
        ownerUserType.setDescription("Proprietário de restaurante");

        ownerUser = new User();
        ownerUser.setId(ownerId);
        ownerUser.setName("João Owner");
        ownerUser.setEmail("joao@owner.com");
        ownerUser.setLogin("joao.owner");
        ownerUser.setUserType(ownerUserType);

        savedRestaurant = new Restaurant();
        savedRestaurant.setId(1L);
        savedRestaurant.setName("Restaurante Teste");
        savedRestaurant.setAddress("Rua das Flores, 123");
        savedRestaurant.setCuisineType(CuisineType.BRASILEIRA);
        savedRestaurant.setOpeningHours("Seg-Dom: 11h às 23h");
        savedRestaurant.setOwner(ownerUser);
        savedRestaurant.setActive(true);
        savedRestaurant.setCreatedAt(LocalDateTime.now());
        savedRestaurant.setUpdatedAt(LocalDateTime.now());
    }

    @Test
    @DisplayName("Should create restaurant successfully")
    void shouldCreateRestaurantSuccessfully() {
        // Given
        when(restaurantRepository.existsByName(validRequest.name())).thenReturn(false);
        when(userRepository.findById(validRequest.ownerId())).thenReturn(Optional.of(ownerUser));
        when(restaurantRepository.existsByOwnerId(validRequest.ownerId())).thenReturn(false);
        when(restaurantRepository.save(any(Restaurant.class))).thenReturn(savedRestaurant);

        // When
        Restaurant result = restaurantService.createRestaurant(validRequest);

        // Then
        assertNotNull(result);
        assertEquals(savedRestaurant.getName(), result.getName());
        assertEquals(savedRestaurant.getAddress(), result.getAddress());
        assertEquals(savedRestaurant.getCuisineType(), result.getCuisineType());
        assertEquals(savedRestaurant.getOpeningHours(), result.getOpeningHours());
        assertEquals(savedRestaurant.getOwner(), result.getOwner());

        verify(restaurantRepository).existsByName(validRequest.name());
        verify(userRepository).findById(validRequest.ownerId());
        verify(restaurantRepository).existsByOwnerId(validRequest.ownerId());
        verify(restaurantRepository).save(any(Restaurant.class));
    }

    @Test
    @DisplayName("Should throw exception when restaurant name already exists")
    void shouldThrowExceptionWhenRestaurantNameAlreadyExists() {
        // Given
        when(restaurantRepository.existsByName(validRequest.name())).thenReturn(true);

        // When & Then
        InvalidUserDataException exception = assertThrows(
                InvalidUserDataException.class,
                () -> restaurantService.createRestaurant(validRequest)
        );

        assertEquals("Já existe um restaurante com este nome", exception.getMessage());
        verify(restaurantRepository).existsByName(validRequest.name());
        verify(userRepository, never()).findById(any());
        verify(restaurantRepository, never()).save(any(Restaurant.class));
    }

    @Test
    @DisplayName("Should throw exception when owner not found")
    void shouldThrowExceptionWhenOwnerNotFound() {
        // Given
        when(restaurantRepository.existsByName(validRequest.name())).thenReturn(false);
        when(userRepository.findById(validRequest.ownerId())).thenReturn(Optional.empty());

        // When & Then
        InvalidUserDataException exception = assertThrows(
                InvalidUserDataException.class,
                () -> restaurantService.createRestaurant(validRequest)
        );

        assertEquals("Usuário não encontrado", exception.getMessage());
        verify(restaurantRepository).existsByName(validRequest.name());
        verify(userRepository).findById(validRequest.ownerId());
        verify(restaurantRepository, never()).save(any(Restaurant.class));
    }

    @Test
    @DisplayName("Should throw exception when user is not OWNER")
    void shouldThrowExceptionWhenUserIsNotOwner() {
        // Given
        UserType clientUserType = new UserType();
        clientUserType.setName("CLIENT");
        ownerUser.setUserType(clientUserType);

        when(restaurantRepository.existsByName(validRequest.name())).thenReturn(false);
        when(userRepository.findById(validRequest.ownerId())).thenReturn(Optional.of(ownerUser));

        // When & Then
        InvalidUserDataException exception = assertThrows(
                InvalidUserDataException.class,
                () -> restaurantService.createRestaurant(validRequest)
        );

        assertEquals("Usuário deve ser do tipo OWNER para possuir um restaurante", exception.getMessage());
        verify(restaurantRepository).existsByName(validRequest.name());
        verify(userRepository).findById(validRequest.ownerId());
        verify(restaurantRepository, never()).existsByOwnerId(any());
        verify(restaurantRepository, never()).save(any(Restaurant.class));
    }

    @Test
    @DisplayName("Should throw exception when owner already has a restaurant")
    void shouldThrowExceptionWhenOwnerAlreadyHasRestaurant() {
        // Given
        when(restaurantRepository.existsByName(validRequest.name())).thenReturn(false);
        when(userRepository.findById(validRequest.ownerId())).thenReturn(Optional.of(ownerUser));
        when(restaurantRepository.existsByOwnerId(validRequest.ownerId())).thenReturn(true);

        // When & Then
        InvalidUserDataException exception = assertThrows(
                InvalidUserDataException.class,
                () -> restaurantService.createRestaurant(validRequest)
        );

        assertEquals("Usuário já possui um restaurante cadastrado", exception.getMessage());
        verify(restaurantRepository).existsByName(validRequest.name());
        verify(userRepository).findById(validRequest.ownerId());
        verify(restaurantRepository).existsByOwnerId(validRequest.ownerId());
        verify(restaurantRepository, never()).save(any(Restaurant.class));
    }

    @Test
    @DisplayName("Should find restaurant by id successfully")
    void shouldFindRestaurantByIdSuccessfully() {
        // Given
        Long restaurantId = 1L;
        when(restaurantRepository.findById(restaurantId)).thenReturn(Optional.of(savedRestaurant));

        // When
        Optional<Restaurant> result = restaurantService.findById(restaurantId);

        // Then
        assertTrue(result.isPresent());
        assertEquals(savedRestaurant, result.get());
        verify(restaurantRepository).findById(restaurantId);
    }

    @Test
    @DisplayName("Should return empty when restaurant not found")
    void shouldReturnEmptyWhenRestaurantNotFound() {
        // Given
        Long restaurantId = 1L;
        when(restaurantRepository.findById(restaurantId)).thenReturn(Optional.empty());

        // When
        Optional<Restaurant> result = restaurantService.findById(restaurantId);

        // Then
        assertTrue(result.isEmpty());
        verify(restaurantRepository).findById(restaurantId);
    }
}