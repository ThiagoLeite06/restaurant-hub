package br.com.restaurant_hub.restauranthub.integration;

import br.com.restaurant_hub.restauranthub.user.application.dto.CreateUserRequest;
import br.com.restaurant_hub.restauranthub.user.infrastructure.repository.UserRepository;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureWebMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;

import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureWebMvc
@ActiveProfiles("test")
@Transactional
@DisplayName("User Integration Tests")
class UserIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserRepository userRepository;



    @BeforeEach
    void setUp() {
        userRepository.deleteAll();
    }

    @Test
    @DisplayName("Should register user successfully")
    void shouldRegisterUserSuccessfully() throws Exception {
        // Given
        CreateUserRequest request = new CreateUserRequest(
                "João Silva",
                "joao@test.com",
                "joao.silva",
                "password123",
                "Rua das Flores, 123",
                "1"
        );

        // When & Then
        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("João Silva"))
                .andExpect(jsonPath("$.email").value("joao@test.com"))
                .andExpect(jsonPath("$.login").value("joao.silva"));
    }

    @Test
    @DisplayName("Should return validation error for invalid email")
    void shouldReturnValidationErrorForInvalidEmail() throws Exception {
        // Given
        CreateUserRequest request = new CreateUserRequest(
                "João Silva",
                "invalid-email",
                "joao.silva",
                "password123",
                "Rua das Flores, 123",
                "1"
        );

        // When & Then
        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }


}