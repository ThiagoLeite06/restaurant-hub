package br.com.restaurant_hub.restauranthub.menuitem.application.service;

import br.com.restaurant_hub.restauranthub.common.dto.ApiResponse;
import br.com.restaurant_hub.restauranthub.common.dto.PaginationResponse;
import br.com.restaurant_hub.restauranthub.exception.InvalidUserDataException;
import br.com.restaurant_hub.restauranthub.exception.UserNotFoundException;
import br.com.restaurant_hub.restauranthub.menuitem.application.dto.CreateMenuItemRequest;
import br.com.restaurant_hub.restauranthub.menuitem.application.dto.MenuItemResponse;
import br.com.restaurant_hub.restauranthub.menuitem.application.dto.UpdateMenuItemRequest;
import br.com.restaurant_hub.restauranthub.menuitem.domain.entity.MenuItem;
import br.com.restaurant_hub.restauranthub.menuitem.infrastructure.repository.MenuItemRepository;
import br.com.restaurant_hub.restauranthub.restaurant.domain.entity.Restaurant;
import br.com.restaurant_hub.restauranthub.restaurant.infrastructure.repository.RestaurantRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class MenuItemServiceImpl implements MenuItemService {
    
    @Autowired
    private MenuItemRepository menuItemRepository;
    
    @Autowired
    private RestaurantRepository restaurantRepository;
    
    @Override
    @Transactional
    public MenuItemResponse createMenuItem(CreateMenuItemRequest request) {
        Restaurant restaurant = restaurantRepository.findById(request.restaurantId())
            .orElseThrow(() -> new UserNotFoundException("Restaurante não encontrado"));
        
        if (menuItemRepository.existsByNameAndRestaurantId(request.name(), request.restaurantId())) {
            throw new InvalidUserDataException("Já existe um item com este nome neste restaurante");
        }
        
        MenuItem menuItem = new MenuItem();
        menuItem.setName(request.name());
        menuItem.setDescription(request.description());
        menuItem.setPrice(request.price());
        menuItem.setAvailableOnlyInRestaurant(request.availableOnlyInRestaurant() != null ? request.availableOnlyInRestaurant() : false);
        menuItem.setPhotoPath(request.photoPath());
        menuItem.setRestaurant(restaurant);
        
        MenuItem savedItem = menuItemRepository.save(menuItem);
        return mapToResponse(savedItem);
    }
    
    @Override
    public MenuItemResponse findMenuItemById(Long id) {
        MenuItem menuItem = menuItemRepository.findByIdAndActiveTrue(id)
            .orElseThrow(() -> new UserNotFoundException("Item do cardápio não encontrado"));
        return mapToResponse(menuItem);
    }
    
    @Override
    public ApiResponse<MenuItemResponse> findAllMenuItems(Integer page, Integer pageSize, String orderBy) {
        Sort.Direction direction = "desc".equalsIgnoreCase(orderBy) ? Sort.Direction.DESC : Sort.Direction.ASC;
        Pageable pageable = PageRequest.of(page, pageSize, Sort.by(direction, "name"));
        
        Page<MenuItem> menuItemsPage = menuItemRepository.findByActiveTrue(pageable);
        
        List<MenuItemResponse> items = menuItemsPage.getContent()
            .stream()
            .map(this::mapToResponse)
            .toList();
            
        return new ApiResponse<>(
            items,
            new PaginationResponse(
                menuItemsPage.getNumber(),
                menuItemsPage.getSize(),
                menuItemsPage.getTotalElements(),
                menuItemsPage.getTotalPages()
            )
        );
    }
    
    @Override
    public List<MenuItemResponse> findMenuItemsByRestaurant(Long restaurantId) {
        List<MenuItem> menuItems = menuItemRepository.findByRestaurantIdAndActiveTrue(restaurantId);
        return menuItems.stream().map(this::mapToResponse).toList();
    }
    
    @Override
    public ApiResponse<MenuItemResponse> findMenuItemsByRestaurant(Long restaurantId, Integer page, Integer pageSize, String orderBy) {
        Sort.Direction direction = "desc".equalsIgnoreCase(orderBy) ? Sort.Direction.DESC : Sort.Direction.ASC;
        Pageable pageable = PageRequest.of(page, pageSize, Sort.by(direction, "name"));
        
        Page<MenuItem> menuItemsPage = menuItemRepository.findByRestaurantIdAndActiveTrue(restaurantId, pageable);
        
        List<MenuItemResponse> items = menuItemsPage.getContent()
            .stream()
            .map(this::mapToResponse)
            .toList();
            
        return new ApiResponse<>(
            items,
            new PaginationResponse(
                menuItemsPage.getNumber(),
                menuItemsPage.getSize(),
                menuItemsPage.getTotalElements(),
                menuItemsPage.getTotalPages()
            )
        );
    }
    
    @Override
    @Transactional
    public MenuItemResponse updateMenuItem(Long id, UpdateMenuItemRequest request) {
        MenuItem menuItem = menuItemRepository.findByIdAndActiveTrue(id)
            .orElseThrow(() -> new UserNotFoundException("Item do cardápio não encontrado"));
        
        if (request.name() != null) {
            if (menuItemRepository.existsByNameAndRestaurantId(request.name(), menuItem.getRestaurant().getId()) 
                && !menuItem.getName().equals(request.name())) {
                throw new InvalidUserDataException("Já existe um item com este nome neste restaurante");
            }
            menuItem.setName(request.name());
        }
        
        if (request.description() != null) {
            menuItem.setDescription(request.description());
        }
        
        if (request.price() != null) {
            menuItem.setPrice(request.price());
        }
        
        if (request.availableOnlyInRestaurant() != null) {
            menuItem.setAvailableOnlyInRestaurant(request.availableOnlyInRestaurant());
        }
        
        if (request.photoPath() != null) {
            menuItem.setPhotoPath(request.photoPath());
        }
        
        if (request.active() != null) {
            menuItem.setActive(request.active());
        }
        
        MenuItem updatedItem = menuItemRepository.save(menuItem);
        return mapToResponse(updatedItem);
    }
    
    @Override
    @Transactional
    public void deleteMenuItem(Long id) {
        MenuItem menuItem = menuItemRepository.findByIdAndActiveTrue(id)
            .orElseThrow(() -> new UserNotFoundException("Item do cardápio não encontrado"));
        
        menuItem.setActive(false);
        menuItemRepository.save(menuItem);
    }
    
    @Override
    public List<MenuItemResponse> searchMenuItemsByName(Long restaurantId, String name) {
        List<MenuItem> menuItems = menuItemRepository.findByRestaurantIdAndNameContainingIgnoreCase(restaurantId, name);
        return menuItems.stream().map(this::mapToResponse).toList();
    }
    
    @Override
    public Long countActiveItemsByRestaurant(Long restaurantId) {
        return menuItemRepository.countActiveItemsByRestaurantId(restaurantId);
    }
    
    private MenuItemResponse mapToResponse(MenuItem menuItem) {
        return new MenuItemResponse(
            menuItem.getId().toString(),
            menuItem.getName(),
            menuItem.getDescription(),
            menuItem.getPrice(),
            menuItem.getAvailableOnlyInRestaurant(),
            menuItem.getPhotoPath(),
            menuItem.getRestaurant().getId().toString(),
            menuItem.getRestaurant().getName(),
            menuItem.getActive(),
            menuItem.getCreatedAt(),
            menuItem.getUpdatedAt()
        );
    }
}