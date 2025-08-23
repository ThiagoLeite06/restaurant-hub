package br.com.restaurant_hub.restauranthub.usertype.presentation.controller;

import br.com.restaurant_hub.restauranthub.common.dto.ApiResponse;
import br.com.restaurant_hub.restauranthub.common.dto.PaginationResponse;
import br.com.restaurant_hub.restauranthub.usertype.application.dto.*;
import br.com.restaurant_hub.restauranthub.usertype.application.usecase.*;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/api/user-type")
public class UserTypeController {
    
    private final CreateUserTypeUseCase createUserTypeUseCase;
    private final FindUserTypeByIdUseCase findUserTypeByIdUseCase;
    private final FindAllUserTypesUseCase findAllUserTypesUseCase;
    private final UpdateUserTypeUseCase updateUserTypeUseCase;
    private final DeleteUserTypeUseCase deleteUserTypeUseCase;
    
    public UserTypeController(CreateUserTypeUseCase createUserTypeUseCase,
                             FindUserTypeByIdUseCase findUserTypeByIdUseCase,
                             FindAllUserTypesUseCase findAllUserTypesUseCase,
                             UpdateUserTypeUseCase updateUserTypeUseCase,
                             DeleteUserTypeUseCase deleteUserTypeUseCase) {
        this.createUserTypeUseCase = createUserTypeUseCase;
        this.findUserTypeByIdUseCase = findUserTypeByIdUseCase;
        this.findAllUserTypesUseCase = findAllUserTypesUseCase;
        this.updateUserTypeUseCase = updateUserTypeUseCase;
        this.deleteUserTypeUseCase = deleteUserTypeUseCase;
    }
    
    @GetMapping
    public ResponseEntity<ApiResponse<UserTypeResponse>> findAll(
            @RequestParam(name = "page", defaultValue = "0") Integer page,
            @RequestParam(name = "pageSize", defaultValue = "5") Integer pageSize,
            @RequestParam(name = "orderBy", defaultValue = "desc") String orderBy) {
        
        Page<UserTypeResponse> pageResponse = findAllUserTypesUseCase.execute(page, pageSize, orderBy);
        
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
    public ResponseEntity<UserTypeResponse> findById(@PathVariable Long id) {
        var userType = findUserTypeByIdUseCase.execute(id);
        return userType.isPresent() ? ResponseEntity.ok(userType.get()) : ResponseEntity.notFound().build();
    }
    
    @PostMapping
    public ResponseEntity<Void> createUserType(@Valid @RequestBody CreateUserTypeRequest request) {
        UserTypeResponse userType = createUserTypeUseCase.execute(request);
        return ResponseEntity.created(URI.create("/api/user-type/" + userType.id())).build();
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<UserTypeResponse> updateById(@PathVariable Long id,
                                                      @Valid @RequestBody UpdateUserTypeRequest request) {
        var userType = updateUserTypeUseCase.execute(id, request);
        return userType.isPresent() ? ResponseEntity.ok(userType.get()) : ResponseEntity.notFound().build();
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUserType(@PathVariable Long id) {
        Boolean deleted = deleteUserTypeUseCase.execute(id);
        return deleted ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }
}