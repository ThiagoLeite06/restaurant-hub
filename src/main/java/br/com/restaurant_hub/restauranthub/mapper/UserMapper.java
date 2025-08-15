package br.com.restaurant_hub.restauranthub.mapper;

import br.com.restaurant_hub.restauranthub.model.User;
import br.com.restaurant_hub.restauranthub.model.UserType;
import br.com.restaurant_hub.restauranthub.model.dto.UserRequest;
import br.com.restaurant_hub.restauranthub.model.dto.UserResponse;
import br.com.restaurant_hub.restauranthub.model.dto.UserUpdateRequest;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class UserMapper {

    public UserResponse toResponse(User user) {
        if (user == null) {
            return null;
        }
        return new UserResponse(
            user.getId(),
            user.getName(),
            user.getEmail(),
            user.getLogin(),
            user.getLastUpdated(),
            user.getAddress()
        );
    }

    public List<UserResponse> toResponseList(List<User> users) {
        if (users == null) {
            return null;
        }
        return users.stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public User toEntity(UserRequest userRequest) {
        if (userRequest == null) {
            return null;
        }
        User user = new User();
        user.setName(userRequest.name());
        user.setEmail(userRequest.email());
        user.setLogin(userRequest.login());
        user.setPassword(userRequest.password());
        user.setAddress(userRequest.address());
        user.setUserType(userRequest.userType() != null ? userRequest.userType() : UserType.CUSTOMER);
        return user;
    }

    public User toEntityForUpdate(UserUpdateRequest userUpdateRequest) {
        if (userUpdateRequest == null) {
            return null;
        }
        User user = new User();
        user.setName(userUpdateRequest.name());
        user.setEmail(userUpdateRequest.email());
        user.setLogin(userUpdateRequest.login());
        user.setAddress(userUpdateRequest.address());
        return user;
    }
}
