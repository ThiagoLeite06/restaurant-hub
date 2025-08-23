package br.com.restaurant_hub.restauranthub.user.application.service;

import br.com.restaurant_hub.restauranthub.user.application.dto.*;
import br.com.restaurant_hub.restauranthub.user.domain.entity.User;
import org.springframework.data.domain.Page;

import java.util.Optional;
import java.util.UUID;

public interface UserService {
    User createUser(CreateUserRequest request);
    
    Optional<User> findById(UUID id);
    
    Page<User> findAll(Integer page, Integer pageSize, String orderBy);
    
    Boolean deleteById(UUID id);
    
    Optional<User> updateById(UUID id, UpdateUserRequest request);
    
    UserResponse findByLogin(String login);
    
    void changePassword(UUID userId, String currentPassword, String newPassword);
}