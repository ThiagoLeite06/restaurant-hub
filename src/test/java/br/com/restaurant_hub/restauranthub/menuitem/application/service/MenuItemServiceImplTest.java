package br.com.restaurant_hub.restauranthub.menuitem.application.service;

import br.com.restaurant_hub.restauranthub.common.dto.ApiResponse;
import br.com.restaurant_hub.restauranthub.exception.InvalidUserDataException;
import br.com.restaurant_hub.restauranthub.exception.UserNotFoundException;
import br.com.restaurant_hub.restauranthub.menuitem.application.dto.CreateMenuItemRequest;
import br.com.restaurant_hub.restauranthub.menuitem.application.dto.MenuItemResponse;
import br.com.restaurant_hub.restauranthub.menuitem.application.dto.UpdateMenuItemRequest;
import br.com.restaurant_hub.restauranthub.menuitem.domain.entity.MenuItem;
import br.com.restaurant_hub.restauranthub.menuitem.infrastructure.repository.MenuItemRepository;
import br.com.restaurant_hub.restauranthub.restaurant.domain.entity.Restaurant;
import br.com.restaurant_hub.restauranthub.restaurant.domain.enums.CuisineType;
import br.com.restaurant_hub.restauranthub.restaurant.infrastructure.repository.RestaurantRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("MenuItemServiceImpl Tests")
class MenuItemServiceImplTest {

    @Mock
    private MenuItemRepository menuItemRepository;

    @Mock
    private RestaurantRepository restaurantRepository;

    @InjectMocks
    private MenuItemServiceImpl menuItemService;

    private MenuItem menuItem;
    private Restaurant restaurant;
    private CreateMenuItemRequest createMenuItemRequest;
    private UpdateMenuItemRequest updateMenuItemRequest;
    private Long restaurantId;
    private Long menuItemId;

    @BeforeEach
    void setUp() {
        restaurantId = 1L;
        menuItemId = 1L;

        restaurant = new Restaurant();
        restaurant.setId(restaurantId);
        restaurant.setName("Pizzaria Bella Napoli");
        restaurant.setAddress("Rua Augusta, 1234 - São Paulo, SP");
        restaurant.setCuisineType(CuisineType.ITALIANA);
        restaurant.setOpeningHours("Ter-Dom: 18h às 23h");

        menuItem = new MenuItem();
        menuItem.setId(menuItemId);
        menuItem.setName("Pizza Margherita");
        menuItem.setDescription("Pizza tradicional com molho de tomate, mussarela e manjericão");
        menuItem.setPrice(new BigDecimal("35.90"));
        menuItem.setAvailableOnlyInRestaurant(false);
        menuItem.setPhotoPath("/images/pizza-margherita.jpg");
        menuItem.setRestaurant(restaurant);
        menuItem.setActive(true);
        menuItem.setCreatedAt(LocalDateTime.now());
        menuItem.setUpdatedAt(LocalDateTime.now());

        createMenuItemRequest = new CreateMenuItemRequest(
                "Pizza Margherita",
                "Pizza tradicional com molho de tomate, mussarela e manjericão",
                new BigDecimal("35.90"),
                false,
                "/images/pizza-margherita.jpg",
                restaurantId
        );

        updateMenuItemRequest = new UpdateMenuItemRequest(
                "Pizza Margherita Especial",
                "Pizza tradicional com molho de tomate, mussarela premium e manjericão fresco",
                new BigDecimal("39.90"),
                true,
                "/images/pizza-margherita-especial.jpg",
                true
        );
    }

    @Test
    @DisplayName("Should create menu item successfully")
    void shouldCreateMenuItemSuccessfully() {
        // Given
        when(restaurantRepository.findById(restaurantId)).thenReturn(Optional.of(restaurant));
        when(menuItemRepository.existsByNameAndRestaurantId(createMenuItemRequest.name(), restaurantId)).thenReturn(false);
        when(menuItemRepository.save(any(MenuItem.class))).thenReturn(menuItem);

        // When
        MenuItemResponse result = menuItemService.createMenuItem(createMenuItemRequest);

        // Then
        assertNotNull(result);
        assertEquals(menuItem.getId().toString(), result.id());
        assertEquals(menuItem.getName(), result.name());
        assertEquals(menuItem.getDescription(), result.description());
        assertEquals(menuItem.getPrice(), result.price());

        verify(restaurantRepository).findById(restaurantId);
        verify(menuItemRepository).existsByNameAndRestaurantId(createMenuItemRequest.name(), restaurantId);
        verify(menuItemRepository).save(any(MenuItem.class));
    }

    @Test
    @DisplayName("Should throw exception when restaurant not found")
    void shouldThrowExceptionWhenRestaurantNotFound() {
        // Given
        when(restaurantRepository.findById(restaurantId)).thenReturn(Optional.empty());

        // When & Then
        UserNotFoundException exception = assertThrows(
                UserNotFoundException.class,
                () -> menuItemService.createMenuItem(createMenuItemRequest)
        );

        assertEquals("Restaurante não encontrado", exception.getMessage());
        verify(restaurantRepository).findById(restaurantId);
        verify(menuItemRepository, never()).save(any());
    }

    @Test
    @DisplayName("Should throw exception when menu item name already exists in restaurant")
    void shouldThrowExceptionWhenMenuItemNameAlreadyExistsInRestaurant() {
        // Given
        when(restaurantRepository.findById(restaurantId)).thenReturn(Optional.of(restaurant));
        when(menuItemRepository.existsByNameAndRestaurantId(createMenuItemRequest.name(), restaurantId)).thenReturn(true);

        // When & Then
        InvalidUserDataException exception = assertThrows(
                InvalidUserDataException.class,
                () -> menuItemService.createMenuItem(createMenuItemRequest)
        );

        assertEquals("Já existe um item com este nome neste restaurante", exception.getMessage());
        verify(restaurantRepository).findById(restaurantId);
        verify(menuItemRepository).existsByNameAndRestaurantId(createMenuItemRequest.name(), restaurantId);
        verify(menuItemRepository, never()).save(any());
    }

    @Test
    @DisplayName("Should find menu item by id successfully")
    void shouldFindMenuItemByIdSuccessfully() {
        // Given
        when(menuItemRepository.findByIdAndActiveTrue(menuItemId)).thenReturn(Optional.of(menuItem));

        // When
        MenuItemResponse result = menuItemService.findMenuItemById(menuItemId);

        // Then
        assertNotNull(result);
        assertEquals(menuItem.getId().toString(), result.id());
        assertEquals(menuItem.getName(), result.name());
        assertEquals(menuItem.getDescription(), result.description());
        assertEquals(menuItem.getPrice(), result.price());
        verify(menuItemRepository).findByIdAndActiveTrue(menuItemId);
    }

    @Test
    @DisplayName("Should throw exception when menu item not found by id")
    void shouldThrowExceptionWhenMenuItemNotFoundById() {
        // Given
        when(menuItemRepository.findByIdAndActiveTrue(menuItemId)).thenReturn(Optional.empty());

        // When & Then
        UserNotFoundException exception = assertThrows(
                UserNotFoundException.class,
                () -> menuItemService.findMenuItemById(menuItemId)
        );

        assertEquals("Item do cardápio não encontrado", exception.getMessage());
        verify(menuItemRepository).findByIdAndActiveTrue(menuItemId);
    }

    @Test
    @DisplayName("Should find all menu items with pagination")
    void shouldFindAllMenuItemsWithPagination() {
        // Given
        Page<MenuItem> menuItemPage = new PageImpl<>(List.of(menuItem));
        when(menuItemRepository.findByActiveTrue(any(Pageable.class))).thenReturn(menuItemPage);

        // When
        ApiResponse<MenuItemResponse> result = menuItemService.findAllMenuItems(0, 10, "asc");

        // Then
        assertNotNull(result);
        assertNotNull(result.data());
        assertEquals(1, result.data().size());
        assertEquals(menuItem.getName(), result.data().get(0).name());
        verify(menuItemRepository).findByActiveTrue(any(Pageable.class));
    }

    @Test
    @DisplayName("Should find menu items by restaurant")
    void shouldFindMenuItemsByRestaurant() {
        // Given
        when(menuItemRepository.findByRestaurantIdAndActiveTrue(restaurantId)).thenReturn(List.of(menuItem));

        // When
        List<MenuItemResponse> result = menuItemService.findMenuItemsByRestaurant(restaurantId);

        // Then
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(menuItem.getName(), result.get(0).name());
        verify(menuItemRepository).findByRestaurantIdAndActiveTrue(restaurantId);
    }

    @Test
    @DisplayName("Should find menu items by restaurant with pagination")
    void shouldFindMenuItemsByRestaurantWithPagination() {
        // Given
        Page<MenuItem> menuItemPage = new PageImpl<>(List.of(menuItem));
        when(menuItemRepository.findByRestaurantIdAndActiveTrue(eq(restaurantId), any(Pageable.class))).thenReturn(menuItemPage);

        // When
        ApiResponse<MenuItemResponse> result = menuItemService.findMenuItemsByRestaurant(restaurantId, 0, 10, "desc");

        // Then
        assertNotNull(result);
        assertNotNull(result.data());
        assertEquals(1, result.data().size());
        assertEquals(menuItem.getName(), result.data().get(0).name());
        verify(menuItemRepository).findByRestaurantIdAndActiveTrue(eq(restaurantId), any(Pageable.class));
    }

    @Test
    @DisplayName("Should update menu item successfully")
    void shouldUpdateMenuItemSuccessfully() {
        // Given
        when(menuItemRepository.findByIdAndActiveTrue(menuItemId)).thenReturn(Optional.of(menuItem));
        when(menuItemRepository.existsByNameAndRestaurantId(updateMenuItemRequest.name(), restaurantId)).thenReturn(false);
        when(menuItemRepository.save(any(MenuItem.class))).thenReturn(menuItem);

        // When
        MenuItemResponse result = menuItemService.updateMenuItem(menuItemId, updateMenuItemRequest);

        // Then
        assertNotNull(result);
        verify(menuItemRepository).findByIdAndActiveTrue(menuItemId);
        verify(menuItemRepository).save(any(MenuItem.class));
    }

    @Test
    @DisplayName("Should throw exception when menu item not found for update")
    void shouldThrowExceptionWhenMenuItemNotFoundForUpdate() {
        // Given
        when(menuItemRepository.findByIdAndActiveTrue(menuItemId)).thenReturn(Optional.empty());

        // When & Then
        UserNotFoundException exception = assertThrows(
                UserNotFoundException.class,
                () -> menuItemService.updateMenuItem(menuItemId, updateMenuItemRequest)
        );

        assertEquals("Item do cardápio não encontrado", exception.getMessage());
        verify(menuItemRepository).findByIdAndActiveTrue(menuItemId);
        verify(menuItemRepository, never()).save(any());
    }

    @Test
    @DisplayName("Should delete menu item successfully (soft delete)")
    void shouldDeleteMenuItemSuccessfully() {
        // Given
        when(menuItemRepository.findByIdAndActiveTrue(menuItemId)).thenReturn(Optional.of(menuItem));
        when(menuItemRepository.save(any(MenuItem.class))).thenReturn(menuItem);

        // When
        assertDoesNotThrow(() -> menuItemService.deleteMenuItem(menuItemId));

        // Then
        verify(menuItemRepository).findByIdAndActiveTrue(menuItemId);
        verify(menuItemRepository).save(any(MenuItem.class));
    }

    @Test
    @DisplayName("Should throw exception when menu item not found for deletion")
    void shouldThrowExceptionWhenMenuItemNotFoundForDeletion() {
        // Given
        when(menuItemRepository.findByIdAndActiveTrue(menuItemId)).thenReturn(Optional.empty());

        // When & Then
        UserNotFoundException exception = assertThrows(
                UserNotFoundException.class,
                () -> menuItemService.deleteMenuItem(menuItemId)
        );

        assertEquals("Item do cardápio não encontrado", exception.getMessage());
        verify(menuItemRepository).findByIdAndActiveTrue(menuItemId);
        verify(menuItemRepository, never()).save(any());
    }

    @Test
    @DisplayName("Should search menu items by name")
    void shouldSearchMenuItemsByName() {
        // Given
        String searchName = "Pizza";
        when(menuItemRepository.findByRestaurantIdAndNameContainingIgnoreCase(restaurantId, searchName))
                .thenReturn(List.of(menuItem));

        // When
        List<MenuItemResponse> result = menuItemService.searchMenuItemsByName(restaurantId, searchName);

        // Then
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(menuItem.getName(), result.get(0).name());
        verify(menuItemRepository).findByRestaurantIdAndNameContainingIgnoreCase(restaurantId, searchName);
    }

    @Test
    @DisplayName("Should count active items by restaurant")
    void shouldCountActiveItemsByRestaurant() {
        // Given
        Long expectedCount = 5L;
        when(menuItemRepository.countActiveItemsByRestaurantId(restaurantId)).thenReturn(expectedCount);

        // When
        Long result = menuItemService.countActiveItemsByRestaurant(restaurantId);

        // Then
        assertEquals(expectedCount, result);
        verify(menuItemRepository).countActiveItemsByRestaurantId(restaurantId);
    }
}