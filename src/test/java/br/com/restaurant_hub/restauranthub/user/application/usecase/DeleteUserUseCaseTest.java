package br.com.restaurant_hub.restauranthub.user.application.usecase;

import br.com.restaurant_hub.restauranthub.user.application.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("DeleteUserUseCase Tests")
class DeleteUserUseCaseTest {

    @Mock
    private UserService userService;

    @InjectMocks
    private DeleteUserUseCase deleteUserUseCase;

    private UUID userId;

    @BeforeEach
    void setUp() {
        userId = UUID.randomUUID();
    }

    @Test
    @DisplayName("Should delete user successfully")
    void shouldDeleteUserSuccessfully() {
        // Given
        when(userService.deleteById(userId)).thenReturn(true);

        // When
        Boolean result = deleteUserUseCase.execute(userId);

        // Then
        assertTrue(result);
        verify(userService).deleteById(userId);
    }

    @Test
    @DisplayName("Should return false when user not found")
    void shouldReturnFalseWhenUserNotFound() {
        // Given
        when(userService.deleteById(userId)).thenReturn(false);

        // When
        Boolean result = deleteUserUseCase.execute(userId);

        // Then
        assertFalse(result);
        verify(userService).deleteById(userId);
    }

    @Test
    @DisplayName("Should handle null id gracefully")
    void shouldHandleNullIdGracefully() {
        // Given
        when(userService.deleteById(null)).thenReturn(false);

        // When
        Boolean result = deleteUserUseCase.execute(null);

        // Then
        assertFalse(result);
        verify(userService).deleteById(null);
    }
}