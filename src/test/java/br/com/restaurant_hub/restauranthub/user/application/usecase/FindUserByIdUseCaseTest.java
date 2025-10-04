package br.com.restaurant_hub.restauranthub.user.application.usecase;

import br.com.restaurant_hub.restauranthub.user.application.dto.UserResponse;
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
@DisplayName("FindUserByIdUseCase Tests")
class FindUserByIdUseCaseTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private FindUserByIdUseCase findUserByIdUseCase;

    private UUID userId;
    private User user;
    private UserType userType;

    @BeforeEach
    void setUp() {
        userId = UUID.randomUUID();

        userType = new UserType();
        userType.setId(1L);
        userType.setName("CLIENT");
        userType.setDescription("Cliente do sistema");

        user = new User();
        user.setId(userId);
        user.setName("João Silva");
        user.setEmail("joao@test.com");
        user.setLogin("joao.silva");
        user.setPassword("encodedPassword");
        user.setAddress("Rua das Flores, 123");
        user.setUserType(userType);
        user.setCreatedAt(LocalDateTime.now());
        user.setUpdatedAt(LocalDateTime.now());
    }

    @Test
    @DisplayName("Should find user by id successfully")
    void shouldFindUserByIdSuccessfully() {
        // Given
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        // When
        Optional<UserResponse> result = findUserByIdUseCase.execute(userId);

        // Then
        assertTrue(result.isPresent());
        UserResponse userResponse = result.get();
        assertEquals(user.getId().toString(), userResponse.id());
        assertEquals(user.getName(), userResponse.name());
        assertEquals(user.getEmail(), userResponse.email());
        assertEquals(user.getLogin(), userResponse.login());
        assertEquals(user.getAddress(), userResponse.address());
        assertEquals(user.getUserType().getId(), userResponse.userTypeId());
        assertEquals(user.getUserType().getName(), userResponse.userTypeName());

        verify(userRepository).findById(userId);
    }

    @Test
    @DisplayName("Should return empty when user not found")
    void shouldReturnEmptyWhenUserNotFound() {
        // Given
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        // When
        Optional<UserResponse> result = findUserByIdUseCase.execute(userId);

        // Then
        assertTrue(result.isEmpty());
        verify(userRepository).findById(userId);
    }

    @Test
    @DisplayName("Should throw exception when id is null")
    void shouldThrowExceptionWhenIdIsNull() {
        // When & Then
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> findUserByIdUseCase.execute(null)
        );

        assertEquals("ID não pode ser nulo", exception.getMessage());
        verify(userRepository, never()).findById(any());
    }
}