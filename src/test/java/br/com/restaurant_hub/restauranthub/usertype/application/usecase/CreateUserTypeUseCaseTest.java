package br.com.restaurant_hub.restauranthub.usertype.application.usecase;

import br.com.restaurant_hub.restauranthub.exception.InvalidUserDataException;
import br.com.restaurant_hub.restauranthub.usertype.application.dto.CreateUserTypeRequest;
import br.com.restaurant_hub.restauranthub.usertype.application.dto.UserTypeResponse;
import br.com.restaurant_hub.restauranthub.usertype.application.service.UserTypeService;
import br.com.restaurant_hub.restauranthub.usertype.domain.entity.UserType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("CreateUserTypeUseCase Tests")
class CreateUserTypeUseCaseTest {

    @Mock
    private UserTypeService userTypeService;

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
        when(userTypeService.createUserType(validRequest)).thenReturn(savedUserType);

        // When
        UserTypeResponse result = createUserTypeUseCase.execute(validRequest);

        // Then
        assertNotNull(result);
        assertEquals(savedUserType.getId().toString(), result.id());
        assertEquals(savedUserType.getName(), result.name());
        assertEquals(savedUserType.getDescription(), result.description());

        verify(userTypeService).createUserType(validRequest);
    }

    @Test
    @DisplayName("Should throw exception when name already exists")
    void shouldThrowExceptionWhenNameAlreadyExists() {
        // Given
        when(userTypeService.createUserType(validRequest))
                .thenThrow(new InvalidUserDataException("Tipo de usuário com este nome já existe"));

        // When & Then
        InvalidUserDataException exception = assertThrows(
                InvalidUserDataException.class,
                () -> createUserTypeUseCase.execute(validRequest)
        );

        assertEquals("Tipo de usuário com este nome já existe", exception.getMessage());
        verify(userTypeService).createUserType(validRequest);
    }

    @Test
    @DisplayName("Should handle null request gracefully")
    void shouldHandleNullRequestGracefully() {
        // When & Then
        assertThrows(
                NullPointerException.class,
                () -> createUserTypeUseCase.execute(null)
        );
    }

    @Test
    @DisplayName("Should create user type with null description")
    void shouldCreateUserTypeWithNullDescription() {
        // Given
        CreateUserTypeRequest requestWithoutDescription = new CreateUserTypeRequest(
                "MANAGER",
                null
        );

        when(userTypeService.createUserType(requestWithoutDescription)).thenReturn(savedUserType);

        // When
        UserTypeResponse result = createUserTypeUseCase.execute(requestWithoutDescription);

        // Then
        assertNotNull(result);
        verify(userTypeService).createUserType(requestWithoutDescription);
    }
}