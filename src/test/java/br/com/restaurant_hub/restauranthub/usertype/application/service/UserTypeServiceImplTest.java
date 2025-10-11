package br.com.restaurant_hub.restauranthub.usertype.application.service;

import br.com.restaurant_hub.restauranthub.exception.InvalidUserDataException;
import br.com.restaurant_hub.restauranthub.usertype.application.dto.CreateUserTypeRequest;
import br.com.restaurant_hub.restauranthub.usertype.application.dto.UpdateUserTypeRequest;
import br.com.restaurant_hub.restauranthub.usertype.domain.entity.UserType;
import br.com.restaurant_hub.restauranthub.usertype.infrastructure.repository.UserTypeRepository;
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


import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("UserTypeServiceImpl Tests")
class UserTypeServiceImplTest {

    @Mock
    private UserTypeRepository userTypeRepository;

    @InjectMocks
    private UserTypeServiceImpl userTypeService;

    private UserType userType;
    private CreateUserTypeRequest createUserTypeRequest;
    private UpdateUserTypeRequest updateUserTypeRequest;
    private Long userTypeId;

    @BeforeEach
    void setUp() {
        userTypeId = 1L;

        userType = new UserType();
        userType.setId(userTypeId);
        userType.setName("MANAGER");
        userType.setDescription("Gerente do restaurante");

        createUserTypeRequest = new CreateUserTypeRequest(
                "MANAGER",
                "Gerente do restaurante"
        );

        updateUserTypeRequest = new UpdateUserTypeRequest(
                "MANAGER_UPDATED",
                "Gerente do restaurante atualizado"
        );
    }

    @Test
    @DisplayName("Should create user type successfully")
    void shouldCreateUserTypeSuccessfully() {
        // Given
        when(userTypeRepository.existsByName(createUserTypeRequest.name())).thenReturn(false);
        when(userTypeRepository.save(any(UserType.class))).thenReturn(userType);

        // When
        UserType result = userTypeService.createUserType(createUserTypeRequest);

        // Then
        assertNotNull(result);
        assertEquals(userType.getName(), result.getName());
        assertEquals(userType.getDescription(), result.getDescription());

        verify(userTypeRepository).existsByName(createUserTypeRequest.name());
        verify(userTypeRepository).save(any(UserType.class));
    }

    @Test
    @DisplayName("Should throw exception when user type name already exists")
    void shouldThrowExceptionWhenUserTypeNameAlreadyExists() {
        // Given
        when(userTypeRepository.existsByName(createUserTypeRequest.name())).thenReturn(true);

        // When & Then
        InvalidUserDataException exception = assertThrows(
                InvalidUserDataException.class,
                () -> userTypeService.createUserType(createUserTypeRequest)
        );

        assertEquals("Tipo de usuário já existente", exception.getMessage());
        verify(userTypeRepository).existsByName(createUserTypeRequest.name());
        verify(userTypeRepository, never()).save(any());
    }

    @Test
    @DisplayName("Should find user type by id successfully")
    void shouldFindUserTypeByIdSuccessfully() {
        // Given
        when(userTypeRepository.findById(userTypeId)).thenReturn(Optional.of(userType));

        // When
        Optional<UserType> result = userTypeService.findById(userTypeId);

        // Then
        assertTrue(result.isPresent());
        assertEquals(userType, result.get());
        verify(userTypeRepository).findById(userTypeId);
    }

    @Test
    @DisplayName("Should return empty when user type not found by id")
    void shouldReturnEmptyWhenUserTypeNotFoundById() {
        // Given
        when(userTypeRepository.findById(userTypeId)).thenReturn(Optional.empty());

        // When
        Optional<UserType> result = userTypeService.findById(userTypeId);

        // Then
        assertTrue(result.isEmpty());
        verify(userTypeRepository).findById(userTypeId);
    }

    @Test
    @DisplayName("Should find all user types with pagination")
    void shouldFindAllUserTypesWithPagination() {
        // Given
        Page<UserType> userTypePage = new PageImpl<>(List.of(userType));
        when(userTypeRepository.findAll(any(PageRequest.class))).thenReturn(userTypePage);

        // When
        Page<UserType> result = userTypeService.findAll(0, 10, "asc");

        // Then
        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        assertEquals(userType, result.getContent().get(0));
        verify(userTypeRepository).findAll(any(PageRequest.class));
    }

    @Test
    @DisplayName("Should update user type successfully")
    void shouldUpdateUserTypeSuccessfully() {
        // Given
        when(userTypeRepository.findById(userTypeId)).thenReturn(Optional.of(userType));
        when(userTypeRepository.save(any(UserType.class))).thenReturn(userType);

        // When
        Optional<UserType> result = userTypeService.updateById(userTypeId, updateUserTypeRequest);

        // Then
        assertTrue(result.isPresent());
        verify(userTypeRepository).findById(userTypeId);
        verify(userTypeRepository).save(any(UserType.class));
    }

    @Test
    @DisplayName("Should return empty when user type not found for update")
    void shouldReturnEmptyWhenUserTypeNotFoundForUpdate() {
        // Given
        when(userTypeRepository.findById(userTypeId)).thenReturn(Optional.empty());

        // When
        Optional<UserType> result = userTypeService.updateById(userTypeId, updateUserTypeRequest);

        // Then
        assertTrue(result.isEmpty());
        verify(userTypeRepository).findById(userTypeId);
        verify(userTypeRepository, never()).save(any());
    }

    @Test
    @DisplayName("Should delete user type successfully")
    void shouldDeleteUserTypeSuccessfully() {
        // Given
        when(userTypeRepository.existsById(userTypeId)).thenReturn(true);

        // When
        Boolean result = userTypeService.deleteById(userTypeId);

        // Then
        assertTrue(result);
        verify(userTypeRepository).existsById(userTypeId);
        verify(userTypeRepository).deleteById(userTypeId);
    }

    @Test
    @DisplayName("Should return false when user type not found for deletion")
    void shouldReturnFalseWhenUserTypeNotFoundForDeletion() {
        // Given
        when(userTypeRepository.existsById(userTypeId)).thenReturn(false);

        // When
        Boolean result = userTypeService.deleteById(userTypeId);

        // Then
        assertFalse(result);
        verify(userTypeRepository).existsById(userTypeId);
        verify(userTypeRepository, never()).deleteById(any());
    }

    @Test
    @DisplayName("Should check if user type exists by name")
    void shouldCheckIfUserTypeExistsByName() {
        // Given
        String userTypeName = "MANAGER";
        when(userTypeRepository.existsByName(userTypeName)).thenReturn(true);

        // When
        Boolean result = userTypeService.existsByName(userTypeName);

        // Then
        assertTrue(result);
        verify(userTypeRepository).existsByName(userTypeName);
    }

    @Test
    @DisplayName("Should return false when user type does not exist by name")
    void shouldReturnFalseWhenUserTypeDoesNotExistByName() {
        // Given
        String userTypeName = "NONEXISTENT";
        when(userTypeRepository.existsByName(userTypeName)).thenReturn(false);

        // When
        Boolean result = userTypeService.existsByName(userTypeName);

        // Then
        assertFalse(result);
        verify(userTypeRepository).existsByName(userTypeName);
    }

    @Test
    @DisplayName("Should update only name when description is null")
    void shouldUpdateOnlyNameWhenDescriptionIsNull() {
        // Given
        UpdateUserTypeRequest partialUpdateRequest = new UpdateUserTypeRequest(
                "NEW_NAME",
                null
        );
        when(userTypeRepository.findById(userTypeId)).thenReturn(Optional.of(userType));
        when(userTypeRepository.save(any(UserType.class))).thenReturn(userType);

        // When
        Optional<UserType> result = userTypeService.updateById(userTypeId, partialUpdateRequest);

        // Then
        assertTrue(result.isPresent());
        verify(userTypeRepository).findById(userTypeId);
        verify(userTypeRepository).save(any(UserType.class));
    }

    @Test
    @DisplayName("Should update only description when name is null")
    void shouldUpdateOnlyDescriptionWhenNameIsNull() {
        // Given
        UpdateUserTypeRequest partialUpdateRequest = new UpdateUserTypeRequest(
                null,
                "New description"
        );
        when(userTypeRepository.findById(userTypeId)).thenReturn(Optional.of(userType));
        when(userTypeRepository.save(any(UserType.class))).thenReturn(userType);

        // When
        Optional<UserType> result = userTypeService.updateById(userTypeId, partialUpdateRequest);

        // Then
        assertTrue(result.isPresent());
        verify(userTypeRepository).findById(userTypeId);
        verify(userTypeRepository).save(any(UserType.class));
    }

    @Test
    @DisplayName("Should handle empty strings in update request")
    void shouldHandleEmptyStringsInUpdateRequest() {
        // Given
        UpdateUserTypeRequest emptyUpdateRequest = new UpdateUserTypeRequest(
                "",
                ""
        );
        when(userTypeRepository.findById(userTypeId)).thenReturn(Optional.of(userType));
        when(userTypeRepository.save(any(UserType.class))).thenReturn(userType);

        // When
        Optional<UserType> result = userTypeService.updateById(userTypeId, emptyUpdateRequest);

        // Then
        assertTrue(result.isPresent());
        verify(userTypeRepository).findById(userTypeId);
        verify(userTypeRepository).save(any(UserType.class));
    }
}