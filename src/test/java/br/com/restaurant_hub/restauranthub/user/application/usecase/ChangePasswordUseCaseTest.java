package br.com.restaurant_hub.restauranthub.user.application.usecase;

import br.com.restaurant_hub.restauranthub.exception.InvalidUserDataException;
import br.com.restaurant_hub.restauranthub.exception.UserNotFoundException;
import br.com.restaurant_hub.restauranthub.user.application.dto.ChangePasswordRequest;
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
@DisplayName("ChangePasswordUseCase Tests")
class ChangePasswordUseCaseTest {

    @Mock
    private UserService userService;

    @InjectMocks
    private ChangePasswordUseCase changePasswordUseCase;

    private UUID userId;
    private ChangePasswordRequest changePasswordRequest;

    @BeforeEach
    void setUp() {
        userId = UUID.randomUUID();
        changePasswordRequest = new ChangePasswordRequest(
                "oldPassword",
                "newPassword123"
        );
    }

    @Test
    @DisplayName("Should change password successfully")
    void shouldChangePasswordSuccessfully() {
        // Given
        doNothing().when(userService).changePassword(userId, "oldPassword", "newPassword123");

        // When
        assertDoesNotThrow(() -> changePasswordUseCase.execute(userId, changePasswordRequest));

        // Then
        verify(userService).changePassword(userId, "oldPassword", "newPassword123");
    }

    @Test
    @DisplayName("Should throw exception when user not found")
    void shouldThrowExceptionWhenUserNotFound() {
        // Given
        doThrow(new UserNotFoundException("Usuário não encontrado"))
                .when(userService).changePassword(userId, "oldPassword", "newPassword123");

        // When & Then
        UserNotFoundException exception = assertThrows(
                UserNotFoundException.class,
                () -> changePasswordUseCase.execute(userId, changePasswordRequest)
        );

        assertEquals("Usuário não encontrado", exception.getMessage());
        verify(userService).changePassword(userId, "oldPassword", "newPassword123");
    }

    @Test
    @DisplayName("Should throw exception when current password is incorrect")
    void shouldThrowExceptionWhenCurrentPasswordIsIncorrect() {
        // Given
        doThrow(new InvalidUserDataException("Senha atual incorreta"))
                .when(userService).changePassword(userId, "oldPassword", "newPassword123");

        // When & Then
        InvalidUserDataException exception = assertThrows(
                InvalidUserDataException.class,
                () -> changePasswordUseCase.execute(userId, changePasswordRequest)
        );

        assertEquals("Senha atual incorreta", exception.getMessage());
        verify(userService).changePassword(userId, "oldPassword", "newPassword123");
    }

    @Test
    @DisplayName("Should handle null id gracefully")
    void shouldHandleNullIdGracefully() {
        // Given
        doThrow(new IllegalArgumentException("ID não pode ser nulo"))
                .when(userService).changePassword(null, "oldPassword", "newPassword123");

        // When & Then
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> changePasswordUseCase.execute(null, changePasswordRequest)
        );

        assertEquals("ID não pode ser nulo", exception.getMessage());
        verify(userService).changePassword(null, "oldPassword", "newPassword123");
    }

    @Test
    @DisplayName("Should handle null request gracefully")
    void shouldHandleNullRequestGracefully() {
        // When & Then
        assertThrows(
                NullPointerException.class,
                () -> changePasswordUseCase.execute(userId, null)
        );
    }

    @Test
    @DisplayName("Should throw exception when new password is same as current")
    void shouldThrowExceptionWhenNewPasswordIsSameAsCurrent() {
        // Given
        ChangePasswordRequest samePasswordRequest = new ChangePasswordRequest(
                "oldPassword",
                "oldPassword"
        );
        
        doThrow(new InvalidUserDataException("A nova senha deve ser diferente da senha atual"))
                .when(userService).changePassword(userId, "oldPassword", "oldPassword");

        // When & Then
        InvalidUserDataException exception = assertThrows(
                InvalidUserDataException.class,
                () -> changePasswordUseCase.execute(userId, samePasswordRequest)
        );

        assertEquals("A nova senha deve ser diferente da senha atual", exception.getMessage());
        verify(userService).changePassword(userId, "oldPassword", "oldPassword");
    }
}