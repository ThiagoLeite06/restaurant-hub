package br.com.restaurant_hub.restauranthub.menuitem.application.service;

import br.com.restaurant_hub.restauranthub.common.dto.ApiResponse;
import br.com.restaurant_hub.restauranthub.menuitem.application.dto.CreateMenuItemRequest;
import br.com.restaurant_hub.restauranthub.menuitem.application.dto.MenuItemResponse;
import br.com.restaurant_hub.restauranthub.menuitem.application.dto.UpdateMenuItemRequest;

import java.util.List;

public interface MenuItemService {
    
    MenuItemResponse createMenuItem(CreateMenuItemRequest request);
    
    MenuItemResponse findMenuItemById(Long id);
    
    ApiResponse<MenuItemResponse> findAllMenuItems(Integer page, Integer pageSize, String orderBy);
    
    List<MenuItemResponse> findMenuItemsByRestaurant(Long restaurantId);
    
    ApiResponse<MenuItemResponse> findMenuItemsByRestaurant(Long restaurantId, Integer page, Integer pageSize, String orderBy);
    
    MenuItemResponse updateMenuItem(Long id, UpdateMenuItemRequest request);
    
    void deleteMenuItem(Long id);
    
    List<MenuItemResponse> searchMenuItemsByName(Long restaurantId, String name);
    
    Long countActiveItemsByRestaurant(Long restaurantId);
}