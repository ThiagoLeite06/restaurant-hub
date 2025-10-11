package br.com.restaurant_hub.restauranthub.user.application.service;

import br.com.restaurant_hub.restauranthub.exception.InvalidUserDataException;
import br.com.restaurant_hub.restauranthub.exception.UserNotFoundException;
import br.com.restaurant_hub.restauranthub.user.application.dto.CreateUserRequest;
import br.com.restaurant_hub.restauranthub.user.application.dto.UpdateUserRequest;
import br.com.restaurant_hub.restauranthub.user.application.dto.UserResponse;
import br.com.restaurant_hub.restauranthub.user.domain.entity.User;
import br.com.restaurant_hub.restauranthub.user.infrastructure.repository.UserRepository;
import br.com.restaurant_hub.restauranthub.usertype.application.service.UserTypeService;
import br.com.restaurant_hub.restauranthub.usertype.domain.entity.UserType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("UserServiceImpl Tests")
class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private UserTypeService userTypeService;

    @InjectMocks
    private UserServiceImpl userService;

    private User user;
    private UserType userType;
    private CreateUserRequest createUserRequest;
    private UpdateUserRequest updateUserRequest;
    private UUID userId;

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

        createUserRequest = new CreateUserRequest(
                "João Silva",
                "joao@test.com",
                "joao.silva",
                "password123",
                "Rua das Flores, 123",
                "1"
        );

        updateUserRequest = new UpdateUserRequest(
                "João Santos",
                "Nova Rua, 456"
        );
    }

    @Test
    @DisplayName("Should create user successfully")
    void shouldCreateUserSuccessfully() {
        // Given
        when(userRepository.existsByEmail(createUserRequest.email())).thenReturn(false);
        when(userRepository.existsByLogin(createUserRequest.login())).thenReturn(false);
        when(passwordEncoder.encode(createUserRequest.password())).thenReturn("encodedPassword");
        when(userTypeService.findById(1L)).thenReturn(Optional.of(userType));
        when(userRepository.save(any(User.class))).thenReturn(user);

        // When
        User result = userService.createUser(createUserRequest);

        // Then
        assertNotNull(result);
        assertEquals(user.getName(), result.getName());
        assertEquals(user.getEmail(), result.getEmail());
        assertEquals(user.getLogin(), result.getLogin());

        verify(userRepository).existsByEmail(createUserRequest.email());
        verify(userRepository).existsByLogin(createUserRequest.login());
        verify(passwordEncoder).encode(createUserRequest.password());
        verify(userTypeService).findById(1L);
        verify(userRepository).save(any(User.class));
    }

    @Test
    @DisplayName("Should throw exception when email already exists")
    void shouldThrowExceptionWhenEmailAlreadyExists() {
        // Given
        when(userRepository.existsByEmail(createUserRequest.email())).thenReturn(true);

        // When & Then
        InvalidUserDataException exception = assertThrows(
                InvalidUserDataException.class,
                () -> userService.createUser(createUserRequest)
        );

        assertEquals("Email já está em uso", exception.getMessage());
        verify(userRepository).existsByEmail(createUserRequest.email());
        verify(userRepository, never()).existsByLogin(any());
        verify(userRepository, never()).save(any());
    }

    @Test
    @DisplayName("Should throw exception when login already exists")
    void shouldThrowExceptionWhenLoginAlreadyExists() {
        // Given
        when(userRepository.existsByEmail(createUserRequest.email())).thenReturn(false);
        when(userRepository.existsByLogin(createUserRequest.login())).thenReturn(true);

        // When & Then
        InvalidUserDataException exception = assertThrows(
                InvalidUserDataException.class,
                () -> userService.createUser(createUserRequest)
        );

        assertEquals("Login já está em uso", exception.getMessage());
        verify(userRepository).existsByEmail(createUserRequest.email());
        verify(userRepository).existsByLogin(createUserRequest.login());
        verify(userRepository, never()).save(any());
    }

    @Test
    @DisplayName("Should throw exception when user type not found")
    void shouldThrowExceptionWhenUserTypeNotFound() {
        // Given
        when(userRepository.existsByEmail(createUserRequest.email())).thenReturn(false);
        when(userRepository.existsByLogin(createUserRequest.login())).thenReturn(false);
        when(passwordEncoder.encode(createUserRequest.password())).thenReturn("encodedPassword");
        when(userTypeService.findById(1L)).thenReturn(Optional.empty());

        // When & Then
        InvalidUserDataException exception = assertThrows(
                InvalidUserDataException.class,
                () -> userService.createUser(createUserRequest)
        );

        assertEquals("Tipo de usuário não encontrado", exception.getMessage());
        verify(userTypeService).findById(1L);
        verify(userRepository, never()).save(any());
    }

    @Test
    @DisplayName("Should find all users with pagination")
    void shouldFindAllUsersWithPagination() {
        // Given
        Page<User> userPage = new PageImpl<>(List.of(user));
        when(userRepository.findAll(any(PageRequest.class))).thenReturn(userPage);

        // When
        Page<User> result = userService.findAll(0, 10, "desc");

        // Then
        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        assertEquals(user, result.getContent().get(0));
        verify(userRepository).findAll(any(PageRequest.class));
    }

    @Test
    @DisplayName("Should find user by id successfully")
    void shouldFindUserByIdSuccessfully() {
        // Given
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        // When
        Optional<User> result = userService.findById(userId);

        // Then
        assertTrue(result.isPresent());
        assertEquals(user, result.get());
        verify(userRepository).findById(userId);
    }

    @Test
    @DisplayName("Should return empty when user not found by id")
    void shouldReturnEmptyWhenUserNotFoundById() {
        // Given
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        // When
        Optional<User> result = userService.findById(userId);

        // Then
        assertTrue(result.isEmpty());
        verify(userRepository).findById(userId);
    }

    @Test
    @DisplayName("Should delete user successfully")
    void shouldDeleteUserSuccessfully() {
        // Given
        when(userRepository.existsById(userId)).thenReturn(true);

        // When
        Boolean result = userService.deleteById(userId);

        // Then
        assertTrue(result);
        verify(userRepository).existsById(userId);
        verify(userRepository).deleteById(userId);
    }

    @Test
    @DisplayName("Should return false when user not found for deletion")
    void shouldReturnFalseWhenUserNotFoundForDeletion() {
        // Given
        when(userRepository.existsById(userId)).thenReturn(false);

        // When
        Boolean result = userService.deleteById(userId);

        // Then
        assertFalse(result);
        verify(userRepository).existsById(userId);
        verify(userRepository, never()).deleteById(any());
    }

    @Test
    @DisplayName("Should update user successfully")
    void shouldUpdateUserSuccessfully() {
        // Given
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(userRepository.save(any(User.class))).thenReturn(user);

        // When
        Optional<User> result = userService.updateById(userId, updateUserRequest);

        // Then
        assertTrue(result.isPresent());
        verify(userRepository).findById(userId);
        verify(userRepository).save(any(User.class));
    }

    @Test
    @DisplayName("Should return empty when user not found for update")
    void shouldReturnEmptyWhenUserNotFoundForUpdate() {
        // Given
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        // When
        Optional<User> result = userService.updateById(userId, updateUserRequest);

        // Then
        assertTrue(result.isEmpty());
        verify(userRepository).findById(userId);
        verify(userRepository, never()).save(any());
    }

    @Test
    @DisplayName("Should find user by login successfully")
    void shouldFindUserByLoginSuccessfully() {
        // Given
        when(userRepository.findByLogin("joao.silva")).thenReturn(Optional.of(user));

        // When
        UserResponse result = userService.findByLogin("joao.silva");

        // Then
        assertNotNull(result);
        assertEquals(user.getId().toString(), result.id());
        assertEquals(user.getName(), result.name());
        assertEquals(user.getEmail(), result.email());
        assertEquals(user.getLogin(), result.login());
        verify(userRepository).findByLogin("joao.silva");
    }

    @Test
    @DisplayName("Should throw exception when login is empty")
    void shouldThrowExceptionWhenLoginIsEmpty() {
        // When & Then
        InvalidUserDataException exception = assertThrows(
                InvalidUserDataException.class,
                () -> userService.findByLogin("")
        );

        assertEquals("Login não pode ser vazio", exception.getMessage());
        verify(userRepository, never()).findByLogin(any());
    }

    @Test
    @DisplayName("Should throw exception when user not found by login")
    void shouldThrowExceptionWhenUserNotFoundByLogin() {
        // Given
        when(userRepository.findByLogin("nonexistent")).thenReturn(Optional.empty());

        // When & Then
        UserNotFoundException exception = assertThrows(
                UserNotFoundException.class,
                () -> userService.findByLogin("nonexistent")
        );

        verify(userRepository).findByLogin("nonexistent");
    }

    @Test
    @DisplayName("Should change password successfully")
    void shouldChangePasswordSuccessfully() {
        // Given
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("currentPassword", "encodedPassword")).thenReturn(true);
        when(passwordEncoder.encode("newPassword")).thenReturn("newEncodedPassword");
        when(userRepository.save(any(User.class))).thenReturn(user);

        // When
        assertDoesNotThrow(() -> userService.changePassword(userId, "currentPassword", "newPassword"));

        // Then
        verify(userRepository).findById(userId);
        verify(passwordEncoder).matches("currentPassword", "encodedPassword");
        verify(passwordEncoder).encode("newPassword");
        verify(userRepository).save(any(User.class));
    }

    @Test
    @DisplayName("Should throw exception when user id is null for password change")
    void shouldThrowExceptionWhenUserIdIsNullForPasswordChange() {
        // When & Then
        InvalidUserDataException exception = assertThrows(
                InvalidUserDataException.class,
                () -> userService.changePassword(null, "currentPassword", "newPassword")
        );

        assertEquals("ID do usuário não pode ser nulo", exception.getMessage());
    }

    @Test
    @DisplayName("Should throw exception when current password is empty")
    void shouldThrowExceptionWhenCurrentPasswordIsEmpty() {
        // When & Then
        InvalidUserDataException exception = assertThrows(
                InvalidUserDataException.class,
                () -> userService.changePassword(userId, "", "newPassword")
        );

        assertEquals("Senha atual é obrigatória", exception.getMessage());
    }

    @Test
    @DisplayName("Should throw exception when new password is empty")
    void shouldThrowExceptionWhenNewPasswordIsEmpty() {
        // When & Then
        InvalidUserDataException exception = assertThrows(
                InvalidUserDataException.class,
                () -> userService.changePassword(userId, "currentPassword", "")
        );

        assertEquals("Nova senha é obrigatória", exception.getMessage());
    }

    @Test
    @DisplayName("Should throw exception when current password is incorrect")
    void shouldThrowExceptionWhenCurrentPasswordIsIncorrect() {
        // Given
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("wrongPassword", "encodedPassword")).thenReturn(false);

        // When & Then
        InvalidUserDataException exception = assertThrows(
                InvalidUserDataException.class,
                () -> userService.changePassword(userId, "wrongPassword", "newPassword")
        );

        assertEquals("Senha atual incorreta", exception.getMessage());
        verify(userRepository).findById(userId);
        verify(passwordEncoder).matches("wrongPassword", "encodedPassword");
        verify(userRepository, never()).save(any());
    }
}