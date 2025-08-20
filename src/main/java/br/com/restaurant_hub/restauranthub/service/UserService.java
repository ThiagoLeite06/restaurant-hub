package br.com.restaurant_hub.restauranthub.service;

import br.com.restaurant_hub.restauranthub.controller.dto.CreateUserRequest;
import br.com.restaurant_hub.restauranthub.controller.dto.UpdateUserRequest;
import br.com.restaurant_hub.restauranthub.entity.UserEntity;
import br.com.restaurant_hub.restauranthub.controller.dto.UserResponse;
import org.springframework.data.domain.Page;

import java.util.Optional;

public interface UserService {
    UserEntity createUser(CreateUserRequest dto);

    Optional<UserEntity> findById(Long id);

    Page<UserEntity> findAll(Integer page, Integer pageSize, String orderBy);

    boolean deleteById(Long id);

    Optional<UserEntity> updateById(Long id, UpdateUserRequest dto);

    UserResponse findByLogin(String login);

    void changePassword(Long userId, String currentPassword, String newPassword);
}
