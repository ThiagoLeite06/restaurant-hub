package br.com.restaurant_hub.restauranthub.user.application.usecase;

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

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("DeleteUserUseCase Tests")
class DeleteUserUseCaseTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private DeleteUserUseCase deleteUserUseCase;

    private UUID userId;
    private User user;

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
        user.setUserType(userType);
        user.setCreatedAt(LocalDateTime.now());
        user.setUpdatedAt(LocalDateTime.now());
    }

    @Test
    @DisplayName("Should delete user successfully")
    void shouldDeleteUserSuccessfully() {
        // Given
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        // When
        Boolean result = deleteUserUseCase.execute(userId);

        // Then
        assertTrue(result);
        verify(userRepository).findById(userId);
        verify(userRepository).delete(user);
    }

    @Test
    @DisplayName("Should return false when user not found")
    void shouldReturnFalseWhenUserNotFound() {
        // Given
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        // When
        Boolean result = deleteUserUseCase.execute(userId);

        // Then
        assertFalse(result);
        verify(userRepository).findById(userId);
        verify(userRepository, never()).delete(any(User.class));
    }

    @Test
    @DisplayName("Should throw exception when id is null")
    void shouldThrowExceptionWhenIdIsNull() {
        // When & Then
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> deleteUserUseCase.execute(null)
        );

        assertEquals("ID não pode ser nulo", exception.getMessage());
        verify(userRepository, never()).findById(any());
        verify(userRepository, never()).delete(any(User.class));
    }
}