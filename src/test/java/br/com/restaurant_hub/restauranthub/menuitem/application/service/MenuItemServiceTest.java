package br.com.restaurant_hub.restauranthub.menuitem.application.service;

import br.com.restaurant_hub.restauranthub.exception.InvalidUserDataException;
import br.com.restaurant_hub.restauranthub.menuitem.application.dto.CreateMenuItemRequest;
import br.com.restaurant_hub.restauranthub.menuitem.application.dto.MenuItemResponse;
import br.com.restaurant_hub.restauranthub.menuitem.domain.entity.MenuItem;
import br.com.restaurant_hub.restauranthub.menuitem.infrastructure.repository.MenuItemRepository;
import br.com.restaurant_hub.restauranthub.restaurant.domain.entity.Restaurant;
import br.com.restaurant_hub.restauranthub.restaurant.domain.enums.CuisineType;
import br.com.restaurant_hub.restauranthub.restaurant.infrastructure.repository.RestaurantRepository;
import br.com.restaurant_hub.restauranthub.user.domain.entity.User;
import br.com.restaurant_hub.restauranthub.usertype.domain.entity.UserType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("MenuItemService Tests")
class MenuItemServiceTest {

    @Mock
    private MenuItemRepository menuItemRepository;

    @Mock
    private RestaurantRepository restaurantRepository;

    @InjectMocks
    private MenuItemServiceImpl menuItemService;

    private CreateMenuItemRequest validRequest;
    private Restaurant restaurant;
    private MenuItem savedMenuItem;

    @BeforeEach
    void setUp() {
        validRequest = new CreateMenuItemRequest(
                "Hambúrguer Artesanal",
                "Delicioso hambúrguer com carne artesanal",
                new BigDecimal("25.90"),
                false,
                "/images/hamburger.jpg",
                1L
        );

        UserType ownerType = new UserType();
        ownerType.setName("OWNER");

        User owner = new User();
        owner.setId(UUID.randomUUID());
        owner.setName("João Owner");
        owner.setUserType(ownerType);

        restaurant = new Restaurant();
        restaurant.setId(1L);
        restaurant.setName("Burger House");
        restaurant.setAddress("Rua das Flores, 123");
        restaurant.setCuisineType(CuisineType.BRASILEIRA);
        restaurant.setOwner(owner);
        restaurant.setActive(true);

        savedMenuItem = new MenuItem();
        savedMenuItem.setId(1L);
        savedMenuItem.setName("Hambúrguer Artesanal");
        savedMenuItem.setDescription("Delicioso hambúrguer com carne artesanal");
        savedMenuItem.setPrice(new BigDecimal("25.90"));
        savedMenuItem.setAvailableOnlyInRestaurant(false);
        savedMenuItem.setPhotoPath("/images/hamburger.jpg");
        savedMenuItem.setRestaurant(restaurant);
        savedMenuItem.setActive(true);
        savedMenuItem.setCreatedAt(LocalDateTime.now());
        savedMenuItem.setUpdatedAt(LocalDateTime.now());
    }

    @Test
    @DisplayName("Should create menu item successfully")
    void shouldCreateMenuItemSuccessfully() {
        // Given
        when(restaurantRepository.findById(validRequest.restaurantId())).thenReturn(Optional.of(restaurant));
        when(menuItemRepository.existsByNameAndRestaurantId(validRequest.name(), validRequest.restaurantId())).thenReturn(false);
        when(menuItemRepository.save(any(MenuItem.class))).thenReturn(savedMenuItem);

        // When
        MenuItemResponse result = menuItemService.createMenuItem(validRequest);

        // Then
        assertNotNull(result);
        assertEquals(savedMenuItem.getId(), result.id());
        assertEquals(savedMenuItem.getName(), result.name());
        assertEquals(savedMenuItem.getDescription(), result.description());
        assertEquals(savedMenuItem.getPrice(), result.price());
        assertEquals(savedMenuItem.getAvailableOnlyInRestaurant(), result.availableOnlyInRestaurant());
        assertEquals(savedMenuItem.getPhotoPath(), result.photoPath());

        verify(restaurantRepository).findById(validRequest.restaurantId());
        verify(menuItemRepository).existsByNameAndRestaurantId(validRequest.name(), validRequest.restaurantId());
        verify(menuItemRepository).save(any(MenuItem.class));
    }

    @Test
    @DisplayName("Should throw exception when restaurant not found")
    void shouldThrowExceptionWhenRestaurantNotFound() {
        // Given
        when(restaurantRepository.findById(validRequest.restaurantId())).thenReturn(Optional.empty());

        // When & Then
        InvalidUserDataException exception = assertThrows(
                InvalidUserDataException.class,
                () -> menuItemService.createMenuItem(validRequest)
        );

        assertEquals("Restaurante não encontrado", exception.getMessage());
        verify(restaurantRepository).findById(validRequest.restaurantId());
        verify(menuItemRepository, never()).save(any(MenuItem.class));
    }

    @Test
    @DisplayName("Should throw exception when menu item name already exists in restaurant")
    void shouldThrowExceptionWhenMenuItemNameAlreadyExistsInRestaurant() {
        // Given
        when(restaurantRepository.findById(validRequest.restaurantId())).thenReturn(Optional.of(restaurant));
        when(menuItemRepository.existsByNameAndRestaurantId(validRequest.name(), validRequest.restaurantId())).thenReturn(true);

        // When & Then
        InvalidUserDataException exception = assertThrows(
                InvalidUserDataException.class,
                () -> menuItemService.createMenuItem(validRequest)
        );

        assertEquals("Já existe um item com este nome no restaurante", exception.getMessage());
        verify(restaurantRepository).findById(validRequest.restaurantId());
        verify(menuItemRepository).existsByNameAndRestaurantId(validRequest.name(), validRequest.restaurantId());
        verify(menuItemRepository, never()).save(any(MenuItem.class));
    }

    @Test
    @DisplayName("Should find menu item by id successfully")
    void shouldFindMenuItemByIdSuccessfully() {
        // Given
        Long menuItemId = 1L;
        when(menuItemRepository.findById(menuItemId)).thenReturn(Optional.of(savedMenuItem));

        // When
        MenuItemResponse result = menuItemService.findMenuItemById(menuItemId);

        // Then
        assertNotNull(result);
        assertEquals(savedMenuItem.getId(), result.id());
        assertEquals(savedMenuItem.getName(), result.name());
        verify(menuItemRepository).findById(menuItemId);
    }

    @Test
    @DisplayName("Should throw exception when menu item not found")
    void shouldThrowExceptionWhenMenuItemNotFound() {
        // Given
        Long menuItemId = 1L;
        when(menuItemRepository.findById(menuItemId)).thenReturn(Optional.empty());

        // When & Then
        InvalidUserDataException exception = assertThrows(
                InvalidUserDataException.class,
                () -> menuItemService.findMenuItemById(menuItemId)
        );

        assertEquals("Item do cardápio não encontrado", exception.getMessage());
        verify(menuItemRepository).findById(menuItemId);
    }

    @Test
    @DisplayName("Should validate price is positive")
    void shouldValidatePriceIsPositive() {
        // Given
        CreateMenuItemRequest invalidRequest = new CreateMenuItemRequest(
                "Item Inválido",
                "Descrição",
                new BigDecimal("-10.00"),
                false,
                "/path/image.jpg",
                1L
        );

        when(restaurantRepository.findById(invalidRequest.restaurantId())).thenReturn(Optional.of(restaurant));

        // When & Then
        InvalidUserDataException exception = assertThrows(
                InvalidUserDataException.class,
                () -> menuItemService.createMenuItem(invalidRequest)
        );

        assertEquals("Preço deve ser maior que zero", exception.getMessage());
        verify(restaurantRepository).findById(invalidRequest.restaurantId());
        verify(menuItemRepository, never()).save(any(MenuItem.class));
    }
}