package br.com.restaurant_hub.restauranthub.mapper;

import br.com.restaurant_hub.restauranthub.entity.UserEntity;
import br.com.restaurant_hub.restauranthub.entity.UserType;
import br.com.restaurant_hub.restauranthub.controller.dto.CreateUserRequest;
import br.com.restaurant_hub.restauranthub.controller.dto.UpdateUserRequest;
import br.com.restaurant_hub.restauranthub.controller.dto.UserResponse;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class UserMapper {

    public UserResponse toResponse(UserEntity user) {
        if (user == null) {
            return null;
        }
        return new UserResponse(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getLogin(),
                user.getAddress());
    }

    public List<UserResponse> toResponseList(List<UserEntity> users) {
        if (users == null) {
            return null;
        }
        return users.stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public UserEntity toEntity(CreateUserRequest userRequest) {
        if (userRequest == null) {
            return null;
        }
        UserEntity user = new UserEntity();
        user.setName(userRequest.name());
        user.setEmail(userRequest.email());
        user.setLogin(userRequest.login());
        user.setPassword(userRequest.password());
        user.setAddress(userRequest.address());
        user.setUserType(userRequest.userType() != null ? userRequest.userType() : UserType.CUSTOMER);
        return user;
    }

    public UserEntity toEntityForUpdate(UpdateUserRequest updateUserRequest) {
        if (updateUserRequest == null) {
            return null;
        }
        UserEntity user = new UserEntity();
        user.setName(updateUserRequest.name());
        user.setEmail(updateUserRequest.email());
        user.setLogin(updateUserRequest.login());
        user.setAddress(updateUserRequest.address());
        return user;
    }
}
