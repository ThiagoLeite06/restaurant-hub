package br.com.restaurant_hub.restauranthub.user.presentation.controller;

import br.com.restaurant_hub.restauranthub.common.dto.ApiResponse;
import br.com.restaurant_hub.restauranthub.common.dto.PaginationResponse;
import br.com.restaurant_hub.restauranthub.user.application.dto.*;
import br.com.restaurant_hub.restauranthub.user.application.usecase.*;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/user")
public class UserController {
    
    private final CreateUserUseCase createUserUseCase;
    private final FindUserByIdUseCase findUserByIdUseCase;
    private final FindAllUsersUseCase findAllUsersUseCase;
    private final UpdateUserUseCase updateUserUseCase;
    private final ChangePasswordUseCase changePasswordUseCase;
    private final DeleteUserUseCase deleteUserUseCase;
    
    public UserController(CreateUserUseCase createUserUseCase,
                         FindUserByIdUseCase findUserByIdUseCase,
                         FindAllUsersUseCase findAllUsersUseCase,
                         UpdateUserUseCase updateUserUseCase,
                         ChangePasswordUseCase changePasswordUseCase,
                         DeleteUserUseCase deleteUserUseCase) {
        this.createUserUseCase = createUserUseCase;
        this.findUserByIdUseCase = findUserByIdUseCase;
        this.findAllUsersUseCase = findAllUsersUseCase;
        this.updateUserUseCase = updateUserUseCase;
        this.changePasswordUseCase = changePasswordUseCase;
        this.deleteUserUseCase = deleteUserUseCase;
    }
    
    @GetMapping
    public ResponseEntity<ApiResponse<UserResponse>> findAll(
            @RequestParam(name = "page", defaultValue = "0") Integer page,
            @RequestParam(name = "pageSize", defaultValue = "5") Integer pageSize,
            @RequestParam(name = "orderBy", defaultValue = "desc") String orderBy) {
        
        var pageResponse = findAllUsersUseCase.execute(page, pageSize, orderBy);

        return ResponseEntity.ok(new ApiResponse<>(
                pageResponse.getContent(),
                new PaginationResponse(
                        pageResponse.getNumber(),
                        pageResponse.getSize(),
                        pageResponse.getTotalElements(),
                        pageResponse.getTotalPages()
                )
        ));
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> findById(@PathVariable String id) {
        var user = findUserByIdUseCase.execute(UUID.fromString(id));
        return user.isPresent() ? ResponseEntity.ok(user.get()) : ResponseEntity.notFound().build();
    }
    
    @PostMapping
    public ResponseEntity<Void> createUser(@Valid @RequestBody CreateUserRequest request) {
        UserResponse user = createUserUseCase.execute(request);
        return ResponseEntity.created(URI.create("/api/user/" + user.id())).build();
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<UserResponse> updateById(@PathVariable UUID id,
                                                  @RequestBody UpdateUserRequest request) {
        var user = updateUserUseCase.execute(id, request);
        return user.isPresent() ? ResponseEntity.ok(user.get()) : ResponseEntity.notFound().build();
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable String id) {
        Boolean deleted = deleteUserUseCase.execute(UUID.fromString(id));
        return deleted ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }
    
    @PutMapping("/{id}/password")
    public ResponseEntity<Void> changePassword(@PathVariable UUID id,
                                              @Valid @RequestBody ChangePasswordRequest request) {
        changePasswordUseCase.execute(id, request);
        return ResponseEntity.noContent().build();
    }
}