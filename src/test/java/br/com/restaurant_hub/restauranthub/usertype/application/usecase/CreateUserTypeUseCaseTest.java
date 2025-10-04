package br.com.restaurant_hub.restauranthub.usertype.application.usecase;

import br.com.restaurant_hub.restauranthub.exception.InvalidUserDataException;
import br.com.restaurant_hub.restauranthub.usertype.application.dto.CreateUserTypeRequest;
import br.com.restaurant_hub.restauranthub.usertype.application.dto.UserTypeResponse;
import br.com.restaurant_hub.restauranthub.usertype.domain.entity.UserType;
import br.com.restaurant_hub.restauranthub.usertype.infrastructure.repository.UserTypeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("CreateUserTypeUseCase Tests")
class CreateUserTypeUseCaseTest {

    @Mock
    private UserTypeRepository userTypeRepository;

    @InjectMocks
    private CreateUserTypeUseCase createUserTypeUseCase;

    private CreateUserTypeRequest validRequest;
    private UserType savedUserType;

    @BeforeEach
    void setUp() {
        validRequest = new CreateUserTypeRequest(
                "MANAGER",
                "Gerente do restaurante"
        );

        savedUserType = new UserType();
        savedUserType.setId(1L);
        savedUserType.setName("MANAGER");
        savedUserType.setDescription("Gerente do restaurante");

    }

    @Test
    @DisplayName("Should create user type successfully")
    void shouldCreateUserTypeSuccessfully() {
        // Given
        when(userTypeRepository.existsByName(validRequest.name())).thenReturn(false);
        when(userTypeRepository.save(any(UserType.class))).thenReturn(savedUserType);

        // When
        UserTypeResponse result = createUserTypeUseCase.execute(validRequest);

        // Then
        assertNotNull(result);
        assertEquals(savedUserType.getId(), result.id());
        assertEquals(savedUserType.getName(), result.name());
        assertEquals(savedUserType.getDescription(), result.description());


        verify(userTypeRepository).existsByName(validRequest.name());
        verify(userTypeRepository).save(any(UserType.class));
    }

    @Test
    @DisplayName("Should throw exception when name already exists")
    void shouldThrowExceptionWhenNameAlreadyExists() {
        // Given
        when(userTypeRepository.existsByName(validRequest.name())).thenReturn(true);

        // When & Then
        InvalidUserDataException exception = assertThrows(
                InvalidUserDataException.class,
                () -> createUserTypeUseCase.execute(validRequest)
        );

        assertEquals("Tipo de usuário com este nome já existe", exception.getMessage());
        verify(userTypeRepository).existsByName(validRequest.name());
        verify(userTypeRepository, never()).save(any(UserType.class));
    }

    @Test
    @DisplayName("Should throw exception when request is null")
    void shouldThrowExceptionWhenRequestIsNull() {
        // When & Then
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> createUserTypeUseCase.execute(null)
        );

        assertEquals("Request não pode ser nulo", exception.getMessage());
        verify(userTypeRepository, never()).save(any(UserType.class));
    }

    @Test
    @DisplayName("Should create user type with null description")
    void shouldCreateUserTypeWithNullDescription() {
        // Given
        CreateUserTypeRequest requestWithoutDescription = new CreateUserTypeRequest(
                "MANAGER",
                null
        );

        when(userTypeRepository.existsByName(requestWithoutDescription.name())).thenReturn(false);
        when(userTypeRepository.save(any(UserType.class))).thenReturn(savedUserType);

        // When
        UserTypeResponse result = createUserTypeUseCase.execute(requestWithoutDescription);

        // Then
        assertNotNull(result);
        verify(userTypeRepository).existsByName(requestWithoutDescription.name());
        verify(userTypeRepository).save(any(UserType.class));
    }
}