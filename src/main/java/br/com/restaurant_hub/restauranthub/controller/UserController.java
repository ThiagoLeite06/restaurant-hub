package br.com.restaurant_hub.restauranthub.controller;

import br.com.restaurant_hub.restauranthub.controller.dto.*;

import br.com.restaurant_hub.restauranthub.mapper.UserMapper;
import br.com.restaurant_hub.restauranthub.entity.UserEntity;
import br.com.restaurant_hub.restauranthub.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/api/user")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService, UserMapper userMapper) {
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<UserEntity>> findAll(
            @RequestParam(name = "page", defaultValue = "0") Integer page,
            @RequestParam(name = "pageSize", defaultValue = "5") Integer pageSize,
            @RequestParam(name = "orderBy", defaultValue = "desc") String orderBy,
            @RequestParam(name = "userType", required = false) String userType) {
        var pageResponse = userService.findAll(page, pageSize, orderBy);

        return ResponseEntity.ok(new ApiResponse<>(
                pageResponse.getContent(),
                new PaginationResponse(
                        pageResponse.getNumber(),
                        pageResponse.getSize(),
                        pageResponse.getTotalElements(),
                        pageResponse.getTotalPages())));
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserEntity> findById(@PathVariable Long id) {
        var user = userService.findById(id);

        return user.isPresent() ? ResponseEntity.ok(user.get()) : ResponseEntity.notFound().build();
    }

    @PostMapping
    public ResponseEntity<Void> createUser(@Valid @RequestBody CreateUserRequest dto) {

        var user = userService.createUser(dto);

        return ResponseEntity.created(URI.create("/api/user/" + user.getId())).build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserEntity> updateById(@PathVariable Long id, @RequestBody UpdateUserRequest dto) {
        var user = userService.updateById(id, dto);

        return user.isPresent() ? ResponseEntity.ok(user.get()) : ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<UserEntity> deleteUser(@PathVariable Long id) {
        var deleted = userService.deleteById(id);
        return deleted ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }

    @PutMapping("/{id}/password")
    public ResponseEntity<Void> changePassword(
            @PathVariable Long id,
            @Valid @RequestBody ChangePasswordRequest request) {

        userService.changePassword(id, request.currentPassword(), request.newPassword());
        return ResponseEntity.noContent().build();
    }
}
