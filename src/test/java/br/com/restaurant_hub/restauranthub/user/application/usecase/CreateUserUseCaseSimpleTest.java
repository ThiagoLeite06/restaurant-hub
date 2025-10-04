package br.com.restaurant_hub.restauranthub.user.application.usecase;

import br.com.restaurant_hub.restauranthub.user.application.dto.CreateUserRequest;
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
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("CreateUserUseCase Simple Tests")
class CreateUserUseCaseSimpleTest {

    @Mock
    private UserService userService;

    @InjectMocks
    private CreateUserUseCase createUserUseCase;

    private CreateUserRequest validRequest;
    private User savedUser;

    @BeforeEach
    void setUp() {
        validRequest = new CreateUserRequest(
                "João Silva",
                "joao@test.com",
                "joao.silva",
                "password123",
                "Rua das Flores, 123",
                "1"
        );

        UserType userType = new UserType();
        userType.setId(1L);
        userType.setName("CLIENT");
        userType.setDescription("Cliente do sistema");

        savedUser = new User();
        savedUser.setId(UUID.randomUUID());
        savedUser.setName("João Silva");
        savedUser.setEmail("joao@test.com");
        savedUser.setLogin("joao.silva");
        savedUser.setPassword("encodedPassword");
        savedUser.setAddress("Rua das Flores, 123");
        savedUser.setUserType(userType);
        savedUser.setCreatedAt(LocalDateTime.now());
        savedUser.setUpdatedAt(LocalDateTime.now());
    }

    @Test
    @DisplayName("Should create user successfully")
    void shouldCreateUserSuccessfully() {
        // Given
        when(userService.createUser(any(CreateUserRequest.class))).thenReturn(savedUser);

        // When
        UserResponse result = createUserUseCase.execute(validRequest);

        // Then
        assertNotNull(result);
        assertEquals(savedUser.getId().toString(), result.id());
        assertEquals(savedUser.getName(), result.name());
        assertEquals(savedUser.getEmail(), result.email());
        assertEquals(savedUser.getLogin(), result.login());
        assertEquals(savedUser.getAddress(), result.address());

        verify(userService).createUser(validRequest);
    }

    @Test
    @DisplayName("Should throw exception when request is null")
    void shouldThrowExceptionWhenRequestIsNull() {
        // When & Then
        assertThrows(
                NullPointerException.class,
                () -> createUserUseCase.execute(null)
        );
    }
}