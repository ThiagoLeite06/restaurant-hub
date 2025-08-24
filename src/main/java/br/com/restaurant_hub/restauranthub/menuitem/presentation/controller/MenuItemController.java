package br.com.restaurant_hub.restauranthub.menuitem.presentation.controller;

import br.com.restaurant_hub.restauranthub.common.dto.ApiResponse;
import br.com.restaurant_hub.restauranthub.menuitem.application.dto.CreateMenuItemRequest;
import br.com.restaurant_hub.restauranthub.menuitem.application.dto.MenuItemResponse;
import br.com.restaurant_hub.restauranthub.menuitem.application.dto.UpdateMenuItemRequest;
import br.com.restaurant_hub.restauranthub.menuitem.application.service.MenuItemService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/menu-items")
public class MenuItemController {
    
    @Autowired
    private MenuItemService menuItemService;
    
    @PostMapping
    public ResponseEntity<MenuItemResponse> createMenuItem(@Valid @RequestBody CreateMenuItemRequest request) {
        MenuItemResponse menuItem = menuItemService.createMenuItem(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(menuItem);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<MenuItemResponse> getMenuItemById(@PathVariable Long id) {
        MenuItemResponse menuItem = menuItemService.findMenuItemById(id);
        return ResponseEntity.ok(menuItem);
    }
    
    @GetMapping
    public ResponseEntity<ApiResponse<MenuItemResponse>> getAllMenuItems(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int pageSize,
            @RequestParam(defaultValue = "asc") String orderBy) {
        
        ApiResponse<MenuItemResponse> menuItems = menuItemService.findAllMenuItems(page, pageSize, orderBy);
        return ResponseEntity.ok(menuItems);
    }
    
    @GetMapping("/restaurant/{restaurantId}")
    public ResponseEntity<List<MenuItemResponse>> getMenuItemsByRestaurant(@PathVariable Long restaurantId) {
        List<MenuItemResponse> menuItems = menuItemService.findMenuItemsByRestaurant(restaurantId);
        return ResponseEntity.ok(menuItems);
    }
    
    @GetMapping("/restaurant/{restaurantId}/paginated")
    public ResponseEntity<ApiResponse<MenuItemResponse>> getMenuItemsByRestaurantPaginated(
            @PathVariable Long restaurantId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int pageSize,
            @RequestParam(defaultValue = "asc") String orderBy) {
        
        ApiResponse<MenuItemResponse> menuItems = menuItemService.findMenuItemsByRestaurant(restaurantId, page, pageSize, orderBy);
        return ResponseEntity.ok(menuItems);
    }
    
    @GetMapping("/restaurant/{restaurantId}/search")
    public ResponseEntity<List<MenuItemResponse>> searchMenuItemsByName(
            @PathVariable Long restaurantId,
            @RequestParam String name) {
        
        List<MenuItemResponse> menuItems = menuItemService.searchMenuItemsByName(restaurantId, name);
        return ResponseEntity.ok(menuItems);
    }
    
    @GetMapping("/restaurant/{restaurantId}/count")
    public ResponseEntity<Long> countMenuItemsByRestaurant(@PathVariable Long restaurantId) {
        Long count = menuItemService.countActiveItemsByRestaurant(restaurantId);
        return ResponseEntity.ok(count);
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<MenuItemResponse> updateMenuItem(
            @PathVariable Long id, 
            @Valid @RequestBody UpdateMenuItemRequest request) {
        
        MenuItemResponse updatedMenuItem = menuItemService.updateMenuItem(id, request);
        return ResponseEntity.ok(updatedMenuItem);
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMenuItem(@PathVariable Long id) {
        menuItemService.deleteMenuItem(id);
        return ResponseEntity.noContent().build();
    }
}