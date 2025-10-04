package br.com.restaurant_hub.restauranthub.user.application.usecase;

import br.com.restaurant_hub.restauranthub.exception.InvalidUserDataException;
import br.com.restaurant_hub.restauranthub.exception.UserNotFoundException;
import br.com.restaurant_hub.restauranthub.user.application.dto.ChangePasswordRequest;
import br.com.restaurant_hub.restauranthub.user.domain.entity.User;
import br.com.restaurant_hub.restauranthub.user.infrastructure.repository.UserRepository;
import br.com.restaurant_hub.restauranthub.usertype.domain.entity.UserType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("ChangePasswordUseCase Tests")
class ChangePasswordUseCaseTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private ChangePasswordUseCase changePasswordUseCase;

    private UUID userId;
    private User user;
    private ChangePasswordRequest changePasswordRequest;

    @BeforeEach
    void setUp() {
        userId = UUID.randomUUID();

        UserType userType = new UserType();
        userType.setId(1L);
        userType.setName("CLIENT");

        user = new User();
        user.setId(userId);
        user.setName("João Silva");
        user.setEmail("joao@test.com");
        user.setLogin("joao.silva");
        user.setPassword("encodedOldPassword");
        user.setUserType(userType);
        user.setCreatedAt(LocalDateTime.now());
        user.setUpdatedAt(LocalDateTime.now());

        changePasswordRequest = new ChangePasswordRequest(
                "oldPassword",
                "newPassword123"
        );
    }

    @Test
    @DisplayName("Should change password successfully")
    void shouldChangePasswordSuccessfully() {
        // Given
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("oldPassword", "encodedOldPassword")).thenReturn(true);
        when(passwordEncoder.encode("newPassword123")).thenReturn("encodedNewPassword");
        when(userRepository.save(any(User.class))).thenReturn(user);

        // When
        assertDoesNotThrow(() -> changePasswordUseCase.execute(userId, changePasswordRequest));

        // Then
        verify(userRepository).findById(userId);
        verify(passwordEncoder).matches("oldPassword", "encodedOldPassword");
        verify(passwordEncoder).encode("newPassword123");
        verify(userRepository).save(user);
    }

    @Test
    @DisplayName("Should throw exception when user not found")
    void shouldThrowExceptionWhenUserNotFound() {
        // Given
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        // When & Then
        UserNotFoundException exception = assertThrows(
                UserNotFoundException.class,
                () -> changePasswordUseCase.execute(userId, changePasswordRequest)
        );

        assertEquals("Usuário não encontrado", exception.getMessage());
        verify(userRepository).findById(userId);
        verify(passwordEncoder, never()).matches(anyString(), anyString());
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    @DisplayName("Should throw exception when current password is incorrect")
    void shouldThrowExceptionWhenCurrentPasswordIsIncorrect() {
        // Given
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("oldPassword", "encodedOldPassword")).thenReturn(false);

        // When & Then
        InvalidUserDataException exception = assertThrows(
                InvalidUserDataException.class,
                () -> changePasswordUseCase.execute(userId, changePasswordRequest)
        );

        assertEquals("Senha atual incorreta", exception.getMessage());
        verify(userRepository).findById(userId);
        verify(passwordEncoder).matches("oldPassword", "encodedOldPassword");
        verify(passwordEncoder, never()).encode(anyString());
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    @DisplayName("Should throw exception when id is null")
    void shouldThrowExceptionWhenIdIsNull() {
        // When & Then
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> changePasswordUseCase.execute(null, changePasswordRequest)
        );

        assertEquals("ID não pode ser nulo", exception.getMessage());
        verify(userRepository, never()).findById(any());
    }

    @Test
    @DisplayName("Should throw exception when request is null")
    void shouldThrowExceptionWhenRequestIsNull() {
        // When & Then
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> changePasswordUseCase.execute(userId, null)
        );

        assertEquals("Request não pode ser nulo", exception.getMessage());
        verify(userRepository, never()).findById(any());
    }

    @Test
    @DisplayName("Should throw exception when new password is same as current")
    void shouldThrowExceptionWhenNewPasswordIsSameAsCurrent() {
        // Given
        ChangePasswordRequest samePasswordRequest = new ChangePasswordRequest(
                "oldPassword",
                "oldPassword"
        );

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("oldPassword", "encodedOldPassword")).thenReturn(true);

        // When & Then
        InvalidUserDataException exception = assertThrows(
                InvalidUserDataException.class,
                () -> changePasswordUseCase.execute(userId, samePasswordRequest)
        );

        assertEquals("A nova senha deve ser diferente da senha atual", exception.getMessage());
        verify(userRepository).findById(userId);
        verify(passwordEncoder).matches("oldPassword", "encodedOldPassword");
        verify(passwordEncoder, never()).encode(anyString());
        verify(userRepository, never()).save(any(User.class));
    }
}