package br.com.restaurant_hub.restauranthub.user.application.usecase;

import br.com.restaurant_hub.restauranthub.user.application.dto.UpdateUserRequest;
import br.com.restaurant_hub.restauranthub.user.application.dto.UserResponse;
import br.com.restaurant_hub.restauranthub.user.application.service.UserService;
import br.com.restaurant_hub.restauranthub.user.domain.entity.User;
import br.com.restaurant_hub.restauranthub.usertype.domain.entity.UserType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("UpdateUserUseCase Simple Tests")
class UpdateUserUseCaseSimpleTest {

    @Mock
    private UserService userService;

    @InjectMocks
    private UpdateUserUseCase updateUserUseCase;

    private UUID userId;
    private User existingUser;
    private UpdateUserRequest updateRequest;

    @BeforeEach
    void setUp() {
        userId = UUID.randomUUID();

        UserType userType = new UserType();
        userType.setId(1L);
        userType.setName("CLIENT");

        existingUser = new User();
        existingUser.setId(userId);
        existingUser.setName("João Silva");
        existingUser.setEmail("joao@test.com");
        existingUser.setLogin("joao.silva");
        existingUser.setUserType(userType);
        existingUser.setCreatedAt(LocalDateTime.now());
        existingUser.setUpdatedAt(LocalDateTime.now());

        updateRequest = new UpdateUserRequest(
                "João Santos",
                "Nova Rua, 456"
        );
    }

    @Test
    @DisplayName("Should update user successfully")
    void shouldUpdateUserSuccessfully() {
        // Given
        when(userService.updateById(userId, updateRequest)).thenReturn(Optional.of(existingUser));

        // When
        Optional<UserResponse> result = updateUserUseCase.execute(userId, updateRequest);

        // Then
        assertTrue(result.isPresent());
        verify(userService).updateById(userId, updateRequest);
    }

    @Test
    @DisplayName("Should return empty when user not found")
    void shouldReturnEmptyWhenUserNotFound() {
        // Given
        when(userService.updateById(userId, updateRequest)).thenReturn(Optional.empty());

        // When
        Optional<UserResponse> result = updateUserUseCase.execute(userId, updateRequest);

        // Then
        assertTrue(result.isEmpty());
        verify(userService).updateById(userId, updateRequest);
    }
}